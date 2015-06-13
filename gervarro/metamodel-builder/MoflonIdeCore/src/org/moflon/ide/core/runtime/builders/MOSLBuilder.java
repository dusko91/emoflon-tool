package org.moflon.ide.core.runtime.builders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.moflon.ide.core.CoreActivator;
import org.moflon.mosl.GuidGenerator;
import org.moflon.mosl.MOSLUtils;
import org.moflon.mosl.editor.context.ContextStore;
import org.moflon.util.WorkspaceHelper;
import org.moflon.util.eMoflonEMFUtil;

import MOSLCodeAdapter.LanguageError;
import MOSLCodeAdapter.MOSLCodeAdapterFactory;
import MOSLCodeAdapter.MOSLTransformer;
import MOSLCodeAdapter.Property;
import MOSLCodeAdapter.untransformer.MOSLUntransformer;
import MOSLCodeAdapter.untransformer.UntransformerFactory;
import Moca.CodeAdapter;
import Moca.Problem;
import MocaTree.Folder;
import MocaTree.Node;

public class MOSLBuilder extends AbstractBuilder
{   
   private static final boolean DEBUG = false;
   
   private static final Logger logger = Logger.getLogger(MOSLBuilder.class);

   private static final String MARKER = "org.moflon.ide.MOSLProblem";

   @Override
   public boolean visit(final IResource resource) throws CoreException
   {
      return false;
   }

   @Override
   public boolean visit(final IResourceDelta delta) throws CoreException
   {
      // Get changes and call visit on *.sch and *.tgg files
      IResourceDelta[] changes = delta.getAffectedChildren();
      for (int i = 0; i < changes.length; i++)
      {
         IResource resource = changes[i].getResource();
         if ("MOSL".equals(resource.getName()) && (changes[i].getKind() == IResourceDelta.CHANGED || changes[i].getKind() == IResourceDelta.ADDED))
         {
            processMOSL(subMonitor, false);
         }
         visit(changes[i]);
      }

      return false;
   }

   @Override
   protected boolean processResource(final IProgressMonitor monitor) throws CoreException
   {
      processMOSL(monitor, true);
      return false;
   }

   protected boolean processMOSL(final IProgressMonitor monitor, final boolean triggerFullBuild) throws CoreException
   {
      logger.debug("Building project: " + getProject().getName() + " (fullBuild: " + triggerFullBuild + ")");

      CodeAdapter codeAdapter = MOSLUtils.createCodeAdapter();

      IProject project = getProject();

      GuidGenerator.init(getProject().getName());
      CoreActivator.getDefault().setMoslDirty(project.getName(), true);

      project.deleteMarkers(MARKER, false, IResource.DEPTH_INFINITE);

      IFolder moslFolder = project.getFolder("MOSL");

      File dir = moslFolder.getLocation().toFile();

      // Perform text to tree
      MocaTree.Folder tree = codeAdapter.parse(dir);

      if (codeAdapter.getProblems().size() > 0)
      {
         for (Problem p : codeAdapter.getProblems())
         {
            System.err.println("ERROR: " + p.getSource() + ": " + p.getMessage());
            IResource resource = findResource(moslFolder, p.getSource());
            createMarker(resource != null ? resource : moslFolder, p.getLine(), -1, p.getMessage(), "Parser");
         }
         return false;
      }

      // Perform tree to model
      MOSLTransformer transformer = MOSLCodeAdapterFactory.eINSTANCE.createMOSLTransformer();
      transformer.setNewResolver(true);
      
      Node eaTree = null;
      try
      {
         eaTree = transformer.transformMOSLToEA(tree);
      } catch (Exception e)
      {
         createMarker(moslFolder, -1, -1, e.toString(), null);
         e.printStackTrace();

         if (DEBUG)
         {
            eMoflonEMFUtil.saveModel(eaTree, project.getLocation().toFile() + "/.debug.xmi");
         }

         return false;
      }

      if (DEBUG)
      {
         eMoflonEMFUtil.saveModel(eaTree, project.getLocation().toFile() + "/.debug.xmi");
      }

      MOSLUtils.filterErrors(transformer);

      if (transformer.getErrors().size() > 0)
      {
         for (LanguageError e : transformer.getErrors())
         {
            logger.error("ERROR: " + e.getMethodName() + ": " + e.getMessage());
            System.err.println(e);

            IResource resource = findResourceForError(moslFolder, e);
            createMarker(resource, 0, 0, e.getMessage(), e.getErrorType());
         }

         return false;
      }

      // Persist models
      IFolder temp = project.getFolder(".temp");
      if (temp.exists())
         temp.delete(true, monitor);
      temp.create(true, true, monitor);

      PrintStream outputStream = null;
      String propertiesLocation = temp.getLocation().toFile() + WorkspaceHelper.PATH_SEPARATOR + project.getName() + ".properties";
      try
      {

         eMoflonEMFUtil.saveModel(eaTree, temp.getLocation().toFile() + WorkspaceHelper.PATH_SEPARATOR + project.getName() + ".moca.xmi");

         outputStream = new PrintStream(new FileOutputStream(propertiesLocation));
         // Only write properties file in case of a full build
         if (triggerFullBuild)
         {
        	transformer.fixForeign();
            StringBuffer deps = new StringBuffer();
            for (Property p : transformer.getProperties())
            {
               if (p.getKey() == "dependency")
               {
                  deps.append(p.getValue()).append(",");
               }
            }

            for (Property p : transformer.getProperties())
            {
               if (!"dependency".equals(p.getKey()) && "foreign".compareTo(p.getKey())!=0)
               {
                  String value = p.getValue();
                  if (p.getKey().endsWith(".dependencies") && (p.getValue() == null || p.getValue().trim().length() == 0))
                  {
                     value = deps.toString();
                  }
                  if (value != null && value.trim().length() > 0)
                  {
                     outputStream.format("%s=%s\n", p.getKey(), value);
                  }
               }
            }
         }
      } catch (Exception e)
      {
         e.printStackTrace();
      } finally
      {
         outputStream.close();
      }

      temp.refreshLocal(IResource.DEPTH_ONE, monitor);

      ContextStore.getInstance().addContextInformation(project, transformer.getContextInformation());

      if (triggerFullBuild)
      {
         CoreActivator.getDefault().setMoslDirty(project.getName(), false);
      }

      return false;
   }

   private IResource findResourceForError(final IFolder moslFolder, final LanguageError e) throws CoreException
   {
      StringBuilder sb = new StringBuilder();
      String packageName = e.getPackageName();
      String className = e.getClassName();

      if (!StringUtils.isBlank(packageName))
      {
         sb.append(packageName.replace(".", WorkspaceHelper.PATH_SEPARATOR));
      }
      if (!StringUtils.isBlank(className))
      {
         sb.append(className + ".eclass");
      }

      IResource resource = findResource(moslFolder, sb.toString());

      if (resource != null)
         return resource;

      return moslFolder;
   }

   private IResource findResource(final IContainer container, final String name) throws CoreException
   {
      // Try to find the member directly
      String containerPath = container.getRawLocation().toOSString();
      if (name.startsWith(containerPath))
      {
         IResource resource = container.findMember(name.substring(containerPath.length()));
         if (resource != null)
            return resource;
      } else
      {
         IResource resource = container.findMember(name);
         if (resource != null)
            return resource;
      }

      // Else search for a valid match
      for (IResource r : container.members())
      {
         if (r instanceof IContainer)
         {
            IResource ret = findResource((IContainer) r, name);
            if (ret != null)
               return ret;
         }
         if (r.getName().equals(name))
            return r;
      }
      return null;
   }

   private void createMarker(final IResource resource, final int lineNumber, final int characterStart, final String message, final String errorType) throws CoreException
   {
      logger.debug("Creating marker on resource: " + resource);
      IMarker m = resource.createMarker(MARKER);
      if (lineNumber > 0)
      {
         m.setAttribute(IMarker.LINE_NUMBER, lineNumber);
         if (characterStart > 0)
         {
            m.setAttribute(IMarker.CHAR_START, characterStart);
         }
      }
      m.setAttribute(IMarker.MESSAGE, message);
      m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
      m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
      m.setAttribute("errorType", errorType == null ? "Unspecified" : errorType);
   }

   public static void convertEAPProjectToMOSL(final IProject project)
   {
      IFolder tempFolder = project.getFolder(".temp");
      IFolder moslFolder = project.getFolder("MOSL");
      IFolder outputFolder = project.getFolder("_MOSL");

      if(moslFolder.exists())
		try {
			moslFolder.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
      if (tempFolder.exists() && !moslFolder.exists()) {

         IFile mocaFile = tempFolder.getFile(project.getName() + ".moca.xmi");

         if (mocaFile.exists()) {
            logger.info("Converting project '" + project.getName() + "'");
            logger.info("Loading tree");
            ResourceSet set = new ResourceSetImpl();
            set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
            
            URI mocaTreeURI = URI.createPlatformResourceURI(mocaFile.getFullPath().toString(), true);
            Resource mocaTreeResource = set.getResource(mocaTreeURI, true);
            eMoflonEMFUtil.installCrossReferencers(set);
            Node tree = (Node) mocaTreeResource.getContents().get(0);

            // Transform tree
            logger.info(" - Transforming tree");
            MOSLUntransformer untransformer = UntransformerFactory.eINSTANCE.createMOSLUntransformer();
            Folder folder = untransformer.untransformEATree(tree);
            
            URI debugURI = URI.createPlatformResourceURI("/" + project.getName() + "/debug/debug.xmi", true);
            Resource debugResource = set.createResource(debugURI);
            debugResource.getContents().add(folder);
            try {
				debugResource.save(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            // Perform tree-to-text (using initial tree)
            logger.info(" - Unparsing tree");
            CodeAdapter codeAdapter = MOSLUtils.createCodeAdapter();
            codeAdapter.unparse(ResourcesPlugin.getWorkspace().getRoot().getLocation().append(outputFolder.getParent().getFullPath()).toString(), folder);

            if (codeAdapter.getProblems().size() > 0) {
               for (Problem p : codeAdapter.getProblems()) {
                  logger.error("ERROR: " + p.getSource() + ":" + p.getLine() + ":" + p.getCharacterPositionStart() + " : " + p.getMessage());
               }
            }

            logger.info(" - done");
         }
      }
   }

   @Override
   protected void cleanResource(final IProgressMonitor monitor) throws CoreException
   {
      IFolder f = getProject().getFolder(WorkspaceHelper.TEMP_FOLDER);
      if (f != null && f.exists())
      {
         f.delete(true, monitor);
         f.create(true, true, monitor);
      }
   }
}
