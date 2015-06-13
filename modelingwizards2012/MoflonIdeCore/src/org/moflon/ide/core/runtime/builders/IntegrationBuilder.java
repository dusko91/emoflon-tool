package org.moflon.ide.core.runtime.builders;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.core.runtime.EMFCodegeneratorHelper;
import org.moflon.moca.CSPCodeAdapter;
import org.moflon.moca.MocaUtil;
import org.moflon.moca.tggUserDefinedConstraint.unparser.TGGUserDefinedConstraintUnparserAdapter;
import org.moflon.util.MoflonUtil;
import org.moflon.util.eMoflonEMFUtil;

import TGGLanguage.TGGRule;
import TGGLanguage.TripleGraphGrammar;
import TGGLanguage.compiler.Compiler;
import TGGLanguage.compiler.CompilerFactory;
import TGGLanguage.csp.CSP;
import TGGLanguage.csp.CspPackage;
import csp.CSPNotSolvableException;


public class IntegrationBuilder extends AbstractEcoreBuilder
{

   public static final String SUFFIX_GEN_ECORE = ".gen.ecore";

   public static final String SUFFIX_SMA = "gen.sma.xmi";
   
   private Collection<MocaTree.Node> userDefinedConstraints;

   @Override
   protected boolean processResource(IProgressMonitor monitor) throws CoreException
   {
      monitor.beginTask(getProgressBarMessage(), 20 * WorkspaceHelper.PROGRESS_SCALE);

      // Create a ResourceSet for resources
      ResourceSet resourceSet = new ResourceSetImpl();
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new EcoreResourceFactoryImpl());

      // Create .gen.ecore afresh
      IFile projectEcoreFile = super.getEcoreFile();
      IFile projectGenEcoreFile = getEcoreFile();
      if (projectGenEcoreFile.exists())
         projectGenEcoreFile.delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
      projectEcoreFile.copy(projectGenEcoreFile.getFullPath(), IResource.FORCE | IResource.DERIVED, new SubProgressMonitor(monitor,
            1 * WorkspaceHelper.PROGRESS_SCALE));

      // Load basic Ecore file for generated SDMs from TGG
      CoreActivator.addMappingForProject(getProject());
      URI ecoreFileURI = URI.createURI(MoflonUtil.getMoflonDefaultURIForProject(getProject().getName()), true);
      EMFCodegeneratorHelper.loadModel(ecoreFileURI, resourceSet);
      monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

      // Add dependencies from buildpath: source and target
      EMFCodegeneratorHelper.loadBuildPathDependenciesAsMoflonURI(resourceSet, getProject());
      monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

      // Load TGG file
      IResource tggFile = getProject().getFile("/model/" + getProject().getName() + WorkspaceHelper.TGG_FILE_EXTENSION);
      URI tggFileURI = URI.createPlatformResourceURI(tggFile.getFullPath().toOSString(), true);

      // Compile TGGs to SDMs
      monitor.subTask(getProject().getName() + ": Translating TGG model to SDMs...");
      Resource ecoreResource = translateTGGFile(resourceSet, ecoreFileURI, tggFileURI);
      monitor.worked(5 * WorkspaceHelper.PROGRESS_SCALE);

      // Save Ecore file
      eMoflonEMFUtil.saveModel(ecoreResource.getContents().get(0), URI.createFileURI(projectGenEcoreFile.getLocation().toString()));

      monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

      // Build ecore file normally
      boolean returnValue = super.processResource(new SubProgressMonitor(monitor, 10 * WorkspaceHelper.PROGRESS_SCALE));

      // Generate user defined constraints
      generateUserDefinedConstraints();
      
      monitor.done();

      return returnValue;
   }
   
   /**
    * Generates the files to implement user defined constraints and fills them with basic content. <br>
    * An existing file won't be changed even if the definition of the constraint has been changed.
    * @throws CoreException 
    */
   private void generateUserDefinedConstraints() throws CoreException
   {
      NullProgressMonitor monitor = new NullProgressMonitor();
      TGGUserDefinedConstraintUnparserAdapter unparser = new TGGUserDefinedConstraintUnparserAdapter();

      if (userDefinedConstraints.size() != 0)
      {
         // Create required folder structure
         WorkspaceHelper.addAllFolders(getProject(), "src/csp/constraints", monitor);
         
         for (MocaTree.Node node : userDefinedConstraints)
         {
            String content = unparser.unparse(node);
            String nameInUpperCase = MocaUtil.firstToUpper(node.getName());
            String path = "src/csp/constraints/" + nameInUpperCase + ".java";

            // Ignore existing files
            if (!getProject().getFile(path).exists())
               WorkspaceHelper.addFile(getProject(), path, content, monitor);
         }
      }
   }

   @Override
   protected IFile getEcoreFile()
   {
      return getProject().getFolder(WorkspaceHelper.MODEL_FOLDER).getFile(getProject().getName() + SUFFIX_GEN_ECORE);
   }

   private Resource translateTGGFile(ResourceSet resourceSet, URI ecoreFileURI, URI tggFileURI)
   {
      Resource ecoreResource = resourceSet.getResource(ecoreFileURI, true);

      Resource tggResource = resourceSet.getResource(tggFileURI, true);
      TripleGraphGrammar tgg = (TripleGraphGrammar) tggResource.getContents().get(0);

      // Parse csp specs
      parseCspSpecs(tgg);

      // Create SDMs from TGG specification and place as annotations in corresponding EOperations
      Resource compilerContainer = resourceSet.createResource(URI.createURI("compiler"));
      Compiler compiler = CompilerFactory.eINSTANCE.createCompiler();
      compilerContainer.getContents().add(compiler);
 
      try {
         compiler.deriveOperationalRules(tgg);
      }
      catch (CSPNotSolvableException e) {
         IFile debugFile = getEcoreFile().getProject().getFolder("/debug").getFile("csp_error.xmi");
         eMoflonEMFUtil.saveModel(CspPackage.eINSTANCE, e.getCsp(), debugFile.getLocation().toOSString());
         throw e;
      }
      

      // Persist static analysis data
      URI smaFileURI = URI.createPlatformResourceURI(getEcoreFile().getFullPath().toOSString(), true);
      eMoflonEMFUtil.saveModel(compiler.getStaticAnalysis(), smaFileURI.trimFileExtension().trimFileExtension().appendFileExtension(SUFFIX_SMA));

      return ecoreResource;
   }

   private void parseCspSpecs(TripleGraphGrammar tgg)
   {
      userDefinedConstraints = new ArrayList<MocaTree.Node>();
      for (TGGRule rule : tgg.getTggRule())
      {
         String cspSpec = rule.getCspSpec();
         if (cspSpec != null && cspSpec.length() > 0)
         {
            CSPCodeAdapter adapter = new CSPCodeAdapter();
            CSP csp = adapter.parse(rule);
            userDefinedConstraints.addAll(adapter.getUserDefinedConstraints());
            rule.setCsp(csp);
         }
      }
   }
}
