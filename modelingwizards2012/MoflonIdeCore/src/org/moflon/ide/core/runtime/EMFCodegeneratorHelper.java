package org.moflon.ide.core.runtime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.ecore.Generator;
import org.eclipse.emf.codegen.ecore.genmodel.GenJDKLevel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.moflon.backend.ecore2fujaba.SDMEcore2SDMFujaba;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.admin.MoflonProperties;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.util.MoflonUtil;

import MocaTree.MocaTreePackage;
import SDMLanguage.SDMLanguagePackage;
import TGGLanguage.TGGLanguagePackage;
import TGGRuntime.TGGRuntimePackage;

/**
 * Used to initialize and control the standard EMF codegenerator
 * 
 * @author anjorin
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
public class EMFCodegeneratorHelper extends Generator
{
   private static final Logger logger = Logger.getLogger(EMFCodegeneratorHelper.class);

   // Location of dynamic templates
   private static final String PATH_TO_TEMPLATES = ".JETEmitters/templates";

   // The model file used for codegeneration
   private IResource ecoreFile;

   // The directory containing model file
   private String modelDirectory;

   // The project containing the model file
   private IJavaProject javaProject;

   public EMFCodegeneratorHelper(IResource ecoreFile, String basePackage, IPath modelDirectory)
   {
      this.ecoreFile = ecoreFile;
      this.basePackage = basePackage;
      this.modelDirectory = modelDirectory.toString();
      javaProject = JavaCore.create(ecoreFile.getProject());
   }

   /**
    * Creates the gen model with settings required for subsequent codegeneration
    * 
    * @return true if no exceptions were thrown
    */
   public boolean createGenModel(ResourceSet resourceSet)
   {
      try
      {
         String path = ecoreFile.getFullPath().toString();
         URI ecoreURI = URI.createPlatformResourceURI(path, true);
         Resource resource = resourceSet.getResource(ecoreURI, true);
         EPackage ePackage = (EPackage) resource.getContents().get(0);

         IPath genModelPath = ecoreFile.getLocation().removeFileExtension().addFileExtension("genmodel");

         URI genModelURI = URI.createFileURI(genModelPath.toString());
         Resource genModelResource = Resource.Factory.Registry.INSTANCE.getFactory(genModelURI).createResource(genModelURI);
         GenModel genModel = GenModelFactory.eINSTANCE.createGenModel();
         genModelResource.getContents().add(genModel);
         resourceSet.getResources().add(genModelResource);
         genModel.setModelDirectory(modelDirectory);
         genModel.getForeignModel().add(ecoreFile.toString());
         genModel.initialize(Collections.singleton(ePackage));
         GenPackage genPackage = genModel.getGenPackages().get(0);
         genModel.setModelName(genModelURI.trimFileExtension().lastSegment());
         genPackage.setBasePackage(basePackage);

         // Set directory for dynamic templates (changes to default EMF templates)
         genModel.setTemplateDirectory(PATH_TO_TEMPLATES);
         genModel.setDynamicTemplates(true);

         // Code formatting
         genModel.setCodeFormatting(true);

         // Enable generics
         genModel.setComplianceLevel(GenJDKLevel.JDK60_LITERAL);

         // Enable operation reflection
         genModel.setOperationReflection(true);

         // Add dependent genmodels
         addDependencies(genModel, resourceSet);

         // Save to file (with no options)
         genModelResource.save(Collections.EMPTY_MAP);

         // Refresh file
         IProject project = ecoreFile.getProject();
         project.getFile(ecoreFile.getProjectRelativePath().removeFileExtension().addFileExtension("genmodel")).refreshLocal(IResource.DEPTH_ZERO, null);
         project.getFile(ecoreFile.getProjectRelativePath().removeFileExtension().addFileExtension("genmodel")).setDerived(true, null);
      } catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }

      return true;
   }

   private void addDependencies(GenModel genModel, ResourceSet resourceSet) throws CoreException
   {
      // Determine dependent projects
      Collection<IProject> dependentProjects = WorkspaceHelper.getProjectsOnBuildPath(ecoreFile.getProject());

      // Load GenModels and add to usedGenModels
      for (IProject project : dependentProjects)
      {
         if (project.hasNature(CoreActivator.REPOSITORY_NATURE_ID))
         {
            IFile genModelFile = project.getFile("/model/" + project.getName() + ".genmodel");
            URI genModelURI = URI.createPlatformResourceURI(genModelFile.getFullPath().toString(), true);

            Resource genModelResource = resourceSet.getResource(genModelURI, true);
            GenModel dependentGenModel = (GenModel) genModelResource.getContents().get(0);

            genModel.getUsedGenPackages().addAll(dependentGenModel.getGenPackages());
         }
      }

      handleDependenciesForGenModel(resourceSet, genModel);
   }

   private void handleDependenciesForGenModel(ResourceSet resourceSet, GenModel genModel)
   {
      try
      {
         // Load Ecore GenModel and add as a dependency
         URI ecoreGenModelURI = URI.createPlatformPluginURI("org.eclipse.emf.ecore/model/Ecore.genmodel", true);
         Resource ecoreGenModelResource = resourceSet.getResource(ecoreGenModelURI, true);
         GenModel ecoreGenModel = (GenModel) ecoreGenModelResource.getContents().get(0);
         genModel.getUsedGenPackages().addAll(ecoreGenModel.getGenPackages());

         // Add TGG specific dependencies if necessary
         if (ecoreFile.getProject().hasNature(CoreActivator.INTEGRATION_NATURE_ID))
         {
            addToUsedGenModels("TGGRuntime", "org.moflon.TGGRuntime", resourceSet, genModel);
         }

         // Add Moca specific dependencies if necessary
         if (WorkspaceHelper.isContainerOnBuildPath(ecoreFile.getProject(), WorkspaceHelper.MOCA_CONTAINER))
         {
            addToUsedGenModels("MocaTree", "org.moflon.moca.MocaTree", resourceSet, genModel);
         }

         // Handle moflon dependencies if necessary
         if (WorkspaceHelper.isContainerOnBuildPath(ecoreFile.getProject(), WorkspaceHelper.MOSL_CONTAINER))
         {
            addToUsedGenModels("TGGRuntime", "org.moflon.TGGRuntime", resourceSet, genModel);
            addToUsedGenModels("SDMLanguage", "org.moflon.SDMLanguage", resourceSet, genModel);
            addToUsedGenModels("TGGLanguage", "org.moflon.TGGLanguage", resourceSet, genModel);
         }
         
         // Add dependencies specified by user
         for (String dependency : new MoflonProperties(ecoreFile.getProject(), new NullProgressMonitor()).getAdditionalUsedGenPackage())
         {
            URI depURI = URI.createURI(dependency, true);
            Resource depResource = resourceSet.getResource(depURI, true);
            GenModel depModel = (GenModel) depResource.getContents().get(0);
            genModel.getUsedGenPackages().addAll(depModel.getGenPackages());
         }
      } catch (CoreException e)
      {
         logger.error("Unable to add dependencies to genmodel.");
         e.printStackTrace();
      }

   }

   private void addToUsedGenModels(String projectName, String pluginID, ResourceSet resourceSet, GenModel genModel)
   {
      // This is the URI used to refer to the package from its Genmodel
      URI refFromGenModel = URI.createPlatformResourceURI(projectName + "/model/" + projectName + ".ecore", true);

      // This is the URI used to refer to the package in the client's workspace
      URI refInDeployedPlugin = URI.createURI(MoflonUtil.getMoflonDefaultURIForProject(projectName), true);

      // To be able to generate code, we must create the following mapping
      resourceSet.getURIConverter().getURIMap().put(refFromGenModel, refInDeployedPlugin);

      // Now retrieve the actual resource containing the package
      Resource packageResource = resourceSet.getResource(refInDeployedPlugin, true);

      // Due to the mapping, the resource is now available via refFromGenModel
      resourceSet.getResources().add(packageResource);

      // Now that the references in the genmodel can be resolved, retrieve the genmodel and add to usedGenPackages
      URI genModelURIPlatform = URI.createPlatformPluginURI(pluginID + "/model/" + projectName + ".genmodel", true);
      Resource genModelResource = resourceSet.getResource(genModelURIPlatform, true);
      GenModel genModelPackage = (GenModel) genModelResource.getContents().get(0);
      genModel.getUsedGenPackages().addAll(genModelPackage.getGenPackages());
      
      // Add genModel uri mapping
      URI genModelUriLocal = URI.createPlatformResourceURI(projectName + "/model/" + projectName + ".genmodel", true);
      resourceSet.getURIConverter().getURIMap().put(genModelUriLocal, genModelURIPlatform);
   }

   /**
    * Generates code using the model file and the gen model file
    * 
    * @throws IOException
    * @throws CoreException
    */
   public void generateCode(ResourceSet resourceSet, IProgressMonitor progressMonitor) throws IOException, CoreException
   {
      progressMonitor.beginTask("Invoking EMF generator", 1*WorkspaceHelper.PROGRESS_SCALE);
      
      String genModelName = ecoreFile.getLocation().removeFileExtension() + ".genmodel";

      Generator generator = new Generator();

      // Create a resource set and load the model file into it.
      URI genModelURI = URI.createFileURI(new File(genModelName).getAbsoluteFile().getCanonicalPath());
      Resource genModelResource = resourceSet.getResource(genModelURI, true);
      GenModel genModel = (GenModel) genModelResource.getContents().get(0);
      
      IStatus status = null;
      try {
         status = genModel.validate();
      }catch (Exception e) {
         logger.warn("Unable to validate GenModel! Trying to generate code anyway ...");
      }
      
      if (status != null && !status.isOK())
      {
         logger.fatal("GenModel is not valid: " + status.getMessage());
         generator.printStatus("", status);
         throw new CoreException(status);
      } else
      {
         org.eclipse.emf.codegen.ecore.generator.Generator gen = new org.eclipse.emf.codegen.ecore.generator.Generator();
         gen.setInput(genModel);

         genModel.setCanGenerate(true);
         genModel.setUpdateClasspath(false);

         // Due to problem with subclasses, generate redundant GenModel EAnnotations 
         createGenModelAnnotations(genModel);
         
         gen.generate(genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE, CodeGenUtil.EclipseUtil.createMonitor(progressMonitor, 1*WorkspaceHelper.PROGRESS_SCALE));
      }
   }

   /* This can be removed when we are able to extend eOperations appropriately */
   private void createGenModelAnnotations(GenModel genModel)
   {
      for (TreeIterator<EObject> itr = genModel.getGenPackages().get(0).getEcorePackage().eAllContents(); itr.hasNext();)
      {
         EObject obj = itr.next();
         
         if(obj instanceof EOperation){
            EOperation eOperation = (EOperation)obj;
            
            String codeForMethod = SDMCodegeneratorHelper.retrieveCodeForOperation(eOperation);
            if (codeForMethod != null)
            {
               // embed SDM code as annotation only if currently there is no annotation. 
               if (eOperation.getEAnnotation(SDMEcore2SDMFujaba.GENMODEL_ANNOTATION_SOURCE) == null) {               
                  EAnnotation annotation = eOperation.getEAnnotation(SDMEcore2SDMFujaba.GENMODEL_ANNOTATION_SOURCE);
                  if (annotation == null)
                  {
                     annotation = EcoreFactory.eINSTANCE.createEAnnotation();
                     annotation.setSource(SDMEcore2SDMFujaba.GENMODEL_ANNOTATION_SOURCE);
                     eOperation.getEAnnotations().add(annotation);
                  }
                  annotation.getDetails().put(SDMEcore2SDMFujaba.GENMODEL_KEY, codeForMethod);
               }
            }
         }
      }
   }

   public static EObject loadModel(URI fileURI, ResourceSet resourceSet)
   {
      // Load .ecore file
      EObject rootPackage = resourceSet.getResource(fileURI, true).getContents().get(0);
      
      // Add mapping from file uri to package (necessary to resolve references in SDMs)
      EStructuralFeature uri = rootPackage.eClass().getEStructuralFeature("nsURI");

      Map<URI, URI> uriMap = resourceSet.getURIConverter().getURIMap();
      uriMap.put(URI.createURI((String) (rootPackage.eGet(uri))), fileURI);

      return rootPackage;
   }

   /**
    * Use to load build path dependencies.
    * Can be used when the corresponding projects do not exist yet.
    * @see EMFCodegeneratorHelper#loadBuildPathDependenciesAsMoflonURI(ResourceSet, IProject)
    */
   public static void loadBuildPathDependencies(ResourceSet resourceSet, IProject project)
   {
      handleDependencies(resourceSet, project);
      
      // Load model and add uri mapping to package for projects on build path
      for (IProject dependencyProject : WorkspaceHelper.getProjectsOnBuildPath(project))
      {
         IResource dependency = dependencyProject.getFile("model/" + dependencyProject.getName() + WorkspaceHelper.ECORE_FILE_EXTENSION);
         if (dependency.exists())
         {
            URI dependencyFileURI = URI.createPlatformResourceURI(dependency.getFullPath().toString(), true);
            loadModel(dependencyFileURI, resourceSet);
         }
      }
   }

   /**
    * Use to load build path dependencies when these already exist in the workspace.
    * This is for example the case when compiling TGG to SDM.
    * @see IntegrationBuilder#processResource(IProgressMonitor monitor)
    * @see EMFCodegeneratorHelper#loadBuildPathDependencies(ResourceSet, IProject)
    */
   public static void loadBuildPathDependenciesAsMoflonURI(ResourceSet resourceSet, IProject project)
   {
      handleDependencies(resourceSet, project);
      
      // Load model and add uri mapping to package for projects on build path
      for (IProject dependencyProject : WorkspaceHelper.getProjectsOnBuildPath(project))
      {
         IResource dependency = dependencyProject.getFile("model/" + dependencyProject.getName() + WorkspaceHelper.ECORE_FILE_EXTENSION);
         if (dependency.exists())
         {
            URI dependencyFileURI = URI.createURI(MoflonUtil.getMoflonDefaultURIForProject(dependencyProject.getName()));
            loadModel(dependencyFileURI, resourceSet);
         }
      }
   }

   
   private static void handleDependencies(ResourceSet resourceSet, IProject project)
   {
      // Add Ecore as dependency
      URI ecoreModelURI = URI.createURI(EcorePackage.eNS_URI, true);
      Resource ecoreModelResource = resourceSet.getResource(ecoreModelURI, true);
      resourceSet.getResources().add(ecoreModelResource);

      // Add Moca dependencies if necessary
      if (WorkspaceHelper.isContainerOnBuildPath(project, WorkspaceHelper.MOCA_CONTAINER))
      {
         URI mocaTreeURI = URI.createURI(MocaTreePackage.eNS_URI);
         Resource mocaTreeResource = resourceSet.getResource(mocaTreeURI, true);
         mocaTreeResource.setURI(mocaTreeURI);
         resourceSet.getResources().add(mocaTreeResource);
      }

      // Add integration dependencies if necessary
      if (WorkspaceHelper.isContainerOnBuildPath(project, WorkspaceHelper.TIE_CONTAINER))
      {
         URI tggRuntimeURI = URI.createURI(TGGRuntimePackage.eNS_URI);
         Resource tggRuntimeResource = resourceSet.getResource(tggRuntimeURI, true);
         tggRuntimeResource.setURI(tggRuntimeURI);
         resourceSet.getResources().add(tggRuntimeResource);
      }
      
      // Add moflon dependencies if necessary
      if (WorkspaceHelper.isContainerOnBuildPath(project, WorkspaceHelper.MOSL_CONTAINER))
      {
         URI tggLanguageURI = URI.createURI(TGGLanguagePackage.eNS_URI);
         Resource tggLanguageResource = resourceSet.getResource(tggLanguageURI, true);
         tggLanguageResource.setURI(tggLanguageURI);
         resourceSet.getResources().add(tggLanguageResource);
         
         URI sdmLanguageURI = URI.createURI(SDMLanguagePackage.eNS_URI);
         Resource sdmLanguageResource = resourceSet.getResource(sdmLanguageURI, true);
         sdmLanguageResource.setURI(sdmLanguageURI);
         resourceSet.getResources().add(sdmLanguageResource);
         
         URI tggRuntimeURI = URI.createURI(TGGRuntimePackage.eNS_URI);
         Resource tggRuntimeResource = resourceSet.getResource(tggRuntimeURI, true);
         tggRuntimeResource.setURI(tggRuntimeURI);
         resourceSet.getResources().add(tggRuntimeResource);
      }
      
      // Add user specified dependencies
      for (String uri : new MoflonProperties(project, new NullProgressMonitor()).getAdditionalDependencies())
      {
         try
         {
            // Load resource with given URI (adds it to the resourceSet)
            URI depURI = URI.createURI(uri, true); 
            Resource depResource = resourceSet.createResource(depURI);
            depResource.load(null);
            // Get package in resource
            EPackage root = (EPackage) depResource.getContents().get(0);
            URI packageURI = URI.createURI(root.getNsURI());
            
            depResource.setURI(packageURI);
            
            // Add mapping so resource can be referred to with package URI
            resourceSet.getURIConverter().getURIMap().put(depURI, packageURI);
         } catch (IOException e)
         {
            e.printStackTrace();
         }
      }
      
      // Further handling ...
   }
}
