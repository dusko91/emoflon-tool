package autobuildplugin.preferences;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.viewers.StructuredSelection;
import org.moflon.moca.sch.parser.SchParserAdapter;
import org.moflon.moca.tgg.parser.TggParserAdapter;
import org.moflon.util.eMoflonEMFUtil;

import Moca.CodeAdapter;
import Moca.MocaFactory;
import TGGLanguage.TripleGraphGrammar;
import TextualTGGCodeAdapter.Transformer;
import TextualTGGCodeAdapter.impl.TextualTGGCodeAdapterFactoryImpl;
import autobuildplugin.Activator;
import autobuildplugin.WorkspaceAutoBuildHandler;

public class CompileTextualTGGsHelper
{

   private static final Logger logger = Logger.getLogger(CompileTextualTGGsHelper.class);
   
   public static void start()
   {
      Job job = new Job("Creating and copying models ...") {

         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            
            WorkspaceAutoBuildHandler.turnOffAutoBuild();

            IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
            for (IProject iProject : projects)
            {
               // Check for MOSL folder...
               IFolder moslFolder = iProject.getFolder("MOSL");

               // If project contains MOSL folder
               if (moslFolder.exists())
               {

                  logger.debug("Found MOSL in Project "+iProject.getName());
                  
                  IResource[] resources;
                  try
                  {
                     resources = moslFolder.members();

                     if (iProject.getFolder(".temp").exists())
                        iProject.getFolder(".temp").delete(true,monitor);
                    
                     iProject.getFolder(".temp").create(true, true, monitor);
                     
                     for (IResource resource : resources)
                     {

                        if (resource instanceof IFolder)
                        {
                        
                           IFolder textualProjectInFolder = (IFolder) resource;
                           
                           logger.debug("Creating tgg.xmi and ecore for "+ textualProjectInFolder.getName());
                           
                           // Overwrite tgg.xmi and ecore files in the .temp folder
                           buildFromText(iProject,textualProjectInFolder, iProject.getFolder(".temp"), monitor);
                        }

                     }
                  } catch (CoreException e)
                  {
                     logger.debug(e.getMessage());
                  }
               }
            }
           
            WorkspaceAutoBuildHandler.turnOnAutoBuild();
            
            //refreshWorkSpace(monitor);
            
            //runJUnitTest(monitor);
            
            return new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, "Model building was successful.", null);
         }

      };

      job.schedule();
     
   }

   protected static IStatus buildFromText(IProject project, IFolder inputTextFolder, IFolder outputModelFolder, IProgressMonitor monitor)
   {

      File dir = inputTextFolder.getLocation().toFile();

      CodeAdapter codeAdapter = MocaFactory.eINSTANCE.createCodeAdapter();
      codeAdapter.getParser().add(new SchParserAdapter());
      codeAdapter.getParser().add(new TggParserAdapter());

      // Perform text to tree
      MocaTree.Folder tree = codeAdapter.parse(dir);

      // DEBUG
      // String astLocation = testDataProjectFolder.getLocation().toString() + "\\" + "AST.xmi";
      // eMoflonEMFUtil.saveModel(MocaTreePackage.eINSTANCE,tree, astLocation);
      // DEBUG

      // Perform tree to model
      Transformer transformer = TextualTGGCodeAdapterFactoryImpl.eINSTANCE.createTransformer();

      TripleGraphGrammar tgg = transformer.build(tree);
      // No tgg created or no package found
      if (tgg == null || transformer.getSrcPackage() == null || transformer.getTrgPackage() == null)
      {
         return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Model could not be created.");
      }

      // Persist models

      // Fill resource set with source and target metamodels
      ResourceSet resourceSet = new ResourceSetImpl();

      // Add source mm to resource set
      Resource src = resourceSet.createResource(URI.createURI(transformer.getSrcPackage().getNsURI()));
      src.getContents().add(transformer.getSrcPackage());

      // Add trg mm to resource set
      Resource trg = resourceSet.createResource(URI.createURI(transformer.getTrgPackage().getNsURI()));
      trg.getContents().add(transformer.getTrgPackage());

      try
      {
         // Persist corr mm with resource set
         Resource corr = resourceSet.createResource(URI.createURI(transformer.getCorrPackage().getNsURI()));
         corr.getContents().add(transformer.getCorrPackage());

         String packageLocation = outputModelFolder.getLocation().toString() + "\\" + transformer.getCorrPackage().getName() + ".ecore";
         OutputStream outputStream = new FileOutputStream(new File(packageLocation));
         corr.save(outputStream, null);
         outputStream.close();

         // Persist tgg
         String tggLocation = outputModelFolder.getLocation().toString() + "\\" + transformer.getCorrPackage().getName() + ".tgg.xmi";
         eMoflonEMFUtil.saveModel(tgg, tggLocation, resourceSet);

         String srcName = transformer.getSrcPackage().getName();
         String trgName = transformer.getTrgPackage().getName();
         String corrName = transformer.getCorrPackage().getName();
         addPropertyFile(project, monitor, outputModelFolder, corrName, srcName, trgName);

      } catch (IOException e)
      {
         return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e);
      } catch (CoreException e)
      {
         return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e);
      }

      try
      {
         outputModelFolder.refreshLocal(IResource.DEPTH_ONE, monitor);
      } catch (CoreException e)
      {
         return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e);
      }

      return new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, "Models building from text was successful.", null);

   }

   private static void addPropertyFile(IProject project, IProgressMonitor monitor, IFolder folder, String corr, String src, String trg) throws IOException,
         CoreException
   {

      // Defining property File name
      String propertyFileName = project.getName() + ".properties";
      IFile propertiesFile = folder.getFile(propertyFileName);

      // Creating the property file if not existing
      if (!propertiesFile.exists())
      {
         InputStream source = new ByteArrayInputStream("".getBytes());
         propertiesFile.create(source, true, monitor);
      }

      // Defining properties
      Properties properties = new Properties();

      // Load properties from file
      InputStream contents = propertiesFile.getContents(true);
      properties.load(contents);
      contents.close();

      // Setting the properties
      properties.setProperty(corr + ".type", "integration");
      properties.setProperty(corr + ".workingSet", project.getName());
      properties.setProperty(corr + ".dependencies", src + "," + trg);

      // Persist properties
      StringBuilderWriter sbw = new StringBuilderWriter();
      properties.store(sbw, "Moflon Property File");
      propertiesFile.setContents(new ByteArrayInputStream(sbw.toString().getBytes()), true, true, new NullProgressMonitor());
   }
   
   /**
    * Refreshes the workspace, enables the eclipse autobuild process until this operation is finished to generate the
    * models by the moflon EAAddin.
    * 
    * @param monitor
    * @param store
    */
   private static void refreshWorkSpace(IProgressMonitor monitor)
   {

      IWorkspace ws = ResourcesPlugin.getWorkspace();
      try
      {
         ws.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
         WorkspaceAutoBuildHandler.turnOnAutoBuild();
         try
         {
            monitor.subTask("Refreshing workspace ...");
            //Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);
         } catch (OperationCanceledException e)
         {
            e.printStackTrace();
         } 
         WorkspaceAutoBuildHandler.turnOffAutoBuild();
         ws.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      monitor.worked(20);

   }
   
   /**
    * Looking up for a project with the name 'EclipseTestSuite' and perfoming this project as junit test suite.
    * 
    * @param monitor
    * @param store
    */
   private static void runJUnitTest(IProgressMonitor monitor)
   {
      monitor.subTask("Running JUnit testsuite ...");

      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject[] projects = root.getProjects();
      IProject testProject = null;
      for (int i = 0; i < projects.length; i++)
      {
         if (projects[i].getName().compareTo("EclipseTestSuite") == 0)
         {
            testProject = projects[i];
         }
      }

      ILaunchShortcut shortcut = new JUnitLaunchShortcut();
      if (testProject != null)
      {
         shortcut.launch(new StructuredSelection(testProject), ILaunchManager.RUN_MODE);
      }

   }

}
