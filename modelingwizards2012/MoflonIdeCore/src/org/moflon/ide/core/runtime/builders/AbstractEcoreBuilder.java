package org.moflon.ide.core.runtime.builders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.action.ValidateAction.EclipseResourcesUtil;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.admin.MoflonProperties;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.core.runtime.EMFCodegeneratorHelper;
import org.moflon.ide.core.runtime.SDMCodegeneratorHelper;
import static org.moflon.util.EAInterfaceUriHelper.*;

import MoflonValidationPlugin.MoflonValidationPluginFactory;
import MoflonValidationPlugin.ValidationClass;
import MoflonValidationPlugin.ecore.EcoreFactory;
import MoflonValidationPlugin.sdm.SdmFactory;
import MoflonValidationPlugin.status.EMoflonStatus;
import MoflonValidationPlugin.status.ValidationResult;
import MoflonValidationPlugin.tgg.TggFactory;
import SDMLanguage.activities.Activity;
import SDMLanguage.activities.ActivityEdge;
import SDMLanguage.activities.StartNode;
import SDMLanguage.activities.StopNode;
import SDMLanguage.activities.StoryNode;
import SDMLanguage.patterns.LinkVariable;
import SDMLanguage.patterns.ObjectVariable;
import de.uni_kassel.fujaba.codegen.sequencer.SDMParseException;

public abstract class AbstractEcoreBuilder extends AbstractBuilder
{
   private static final Logger logger = Logger.getLogger(AbstractEcoreBuilder.class);
   private static ArrayList<IPath> pathsThatShouldNotBeDeleted = new ArrayList<IPath>();

   public static void clean(IProgressMonitor monitor, IProject project) throws CoreException {
	   monitor.beginTask("Cleaning " + project, 1*WorkspaceHelper.PROGRESS_SCALE);
	      
	      // Remove all problem markers
	      project.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
	      
	      // Remove generated code but retain "facades"
	      cleanFolder(project.getFolder("gen"), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE), project);
	      
	      // Remove debug data
	      cleanDebug(project.getFolder("debug"), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE), project);
	      
	      // Remove generated model files
	      cleanModels(project.getFolder("model"), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE), project);
	      
	      monitor.done();
   }
   
   @Override
   protected void clean(IProgressMonitor monitor) throws CoreException
   {
     clean(monitor, getProject());
   }
   
   // Delete all data within debug folder
   private static void cleanDebug(IFolder folder, IProgressMonitor monitor, IProject project) throws CoreException
   {
      if(!folder.exists())
         return;
         
      monitor.beginTask("Inspecting " + folder.getName(), folder.members().length * WorkspaceHelper.PROGRESS_SCALE);
      
      for (IResource resource : folder.members())
      {
    	 // keep SVN data 
         if (!resource.getName().startsWith(".")){
            if (resource.getType() == IResource.FOLDER){
               cleanDebug((IFolder) resource, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE), project);               
            }
            else{
               resource.delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));               
            }
         }
         else
            monitor.worked(1*WorkspaceHelper.PROGRESS_SCALE);
      }
   }
   
   // Delete generated models within model folder
   private static void cleanModels(IFolder folder, IProgressMonitor monitor, IProject project) throws CoreException
   {
      monitor.beginTask("Inspecting " + folder.getName(), folder.members().length * WorkspaceHelper.PROGRESS_SCALE);
      
      for (IResource resource : folder.members())
      {
    	 // keep SVN data 
         if (!resource.getName().startsWith(".")){
        	// only delete generated models directly in folder 'model'
            if (resource.getType() != IResource.FOLDER)
            	if(new MoflonProperties(project, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE)).getReplaceGenModel())
            		if(resource.getName().endsWith(".genmodel") || resource.getName().endsWith(IntegrationBuilder.SUFFIX_GEN_ECORE) || resource.getName().endsWith(IntegrationBuilder.SUFFIX_SMA))
            			resource.delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
         }
         else
            monitor.worked(1*WorkspaceHelper.PROGRESS_SCALE);
      }
   }

   // Recursively iterate through all subfolders and delete everything apart from content in "facade" folders
   private static void cleanFolder(IFolder folder, IProgressMonitor monitor, IProject project) throws CoreException
   {
      monitor.beginTask("Inspecting " + folder.getName(), folder.members().length * WorkspaceHelper.PROGRESS_SCALE);
      
      for (IResource resource : folder.members())
      {
         if (!resource.getName().startsWith(".")){
            if (resource.getType() == IResource.FOLDER){
               cleanFolder((IFolder) resource, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE), project);
               if(shouldBeDeleted(resource, monitor, project)){
                  resource.delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
               }
            }
            else{
               if(shouldBeDeleted(resource, monitor, project)){
                  resource.delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
               }
               else
                  monitor.worked(1*WorkspaceHelper.PROGRESS_SCALE);
            }
         }
         else
            monitor.worked(1*WorkspaceHelper.PROGRESS_SCALE);
      }
   }
   
   // Decides if a resource should be deleted
   private static boolean shouldBeDeleted(IResource resource, IProgressMonitor monitor, IProject project){
      MoflonProperties moflonProperties = new MoflonProperties(project, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
      Collection<String> preservePaths = moflonProperties.getPreservePaths();
      IPath path = resource.getProjectRelativePath();
      
      if(preservePaths.contains(resource.getFullPath().toString())){
         pathsThatShouldNotBeDeleted.add(path);
         return false;
      }
      
      if(resource.getType() == IResource.FOLDER){
         if(resource.getName().endsWith("facade") && resource.getType() == IResource.FOLDER){
            return false;
         }
         else if (resource.getName().equals("impl") && resource.getType() == IResource.FOLDER){
            if(path.segment(path.segmentCount()-2).endsWith("facade")){
               pathsThatShouldNotBeDeleted.add(path);
               return false;
            }
         }
         for (IPath currPath : pathsThatShouldNotBeDeleted)
         {
            if(currPath.toString().startsWith(path.toString())){
               return false;
            }
            
         }
         return true;
      }
      if(path.toString().matches(".*/*facade/impl/.*")){
         if(path.toString().matches(".*/*facade/impl/FacadeFactoryImpl\\.java")){
            return true;
         }
         else if (path.toString().matches(".*/*facade/impl/FacadePackageImpl\\.java")){
            return true;
         }
         return false;
      }
      return true;
   }

   public static IStatus performValidation(IProgressMonitor monitor, Resource ecoreResource, IProject ecoreProject) throws CoreException
   {
      ValidationClass validationClass = MoflonValidationPluginFactory.eINSTANCE.createValidationClass();

      EClass sdmRulesEClass = SdmFactory.eINSTANCE.createSDMRules().eClass();
      EClass tggRulesEClass = TggFactory.eINSTANCE.createTGGRules().eClass();
      EClass ecoreRulesEClass = EcoreFactory.eINSTANCE.createEcoreRules().eClass();
      boolean isTgg = ecoreProject.hasNature(CoreActivator.INTEGRATION_NATURE_ID);
      validationClass.setIsIntegrationProject(isTgg);
      
      EPackage ePackage = (EPackage) ecoreResource.getContents().get(0);
      
      ValidationResult validationResult = validationClass.doValidation(ecoreRulesEClass, tggRulesEClass, sdmRulesEClass, ePackage);

      // Create markers for statusContainers
      EclipseResourcesUtil util = new EclipseResourcesUtil();
      util.deleteMarkers(ecoreResource);
      boolean isValid = createMarkersForMofStatusContainer(validationResult, ecoreResource, util);

      // Create propertyfile
      IProject specificationProject = new MoflonProperties(ecoreProject, monitor).getMetamodelProject();
      IPath path = specificationProject.getLocation();
      path = path.append("/validation");

      createPropertyFile(path, validationResult, ecoreProject);

      if (isValid)
         return new Status(IStatus.OK, CoreActivator.PLUGIN_ID, IStatus.OK, ecoreProject.getName() + " is valid!", null);
      else
         return new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, IStatus.OK, ecoreProject.getName() + " is invalid, please check the created problem markers for further details.", null);
   }
   
   public static boolean createMarkersForMofStatusContainer(ValidationResult validationResult, Resource ecoreResource, EclipseResourcesUtil util)
   {
      boolean isValid = true;
      for (EMoflonStatus eMoflonStatus : validationResult.getRuleResults())
      {
         if (eMoflonStatus.getSeverity() != eMoflonStatus.getOK())
         {
            EObject[] dummyArray = new EObject[1];
            dummyArray[0] = eMoflonStatus.getRelatedObject();
            util.createMarkers(ecoreResource, new BasicDiagnostic(eMoflonStatus.getSeverity(), "EMoflon Validation Plugin", 0, eMoflonStatus.getMessage(),
                  dummyArray));
            isValid = false;
         }
      }

      return isValid;
   }
   
   public static void createPropertyFile(IPath path, ValidationResult validationResult, IProject ecoreProject)
   {
	
      String filename = path.toString() + "/" + ecoreProject.getName() + ".validation.properties";
      File propertyFile = new File(filename);
      if(propertyFile.getParentFile().mkdir())
      {
    	  
      }
      try
      {
         FileWriter fileWriter = new FileWriter(propertyFile);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         int i = 0;
         for (EMoflonStatus eMofStatus : validationResult.getRuleResults())
         {
            if (eMofStatus.getSeverity() == eMofStatus.getERROR())
            {
               String uriFragment = "";
               if (eMofStatus.getRelatedObject() != null)
               {
                  EObject relatedObject = eMofStatus.getRelatedObject();
                  uriFragment = computeURIString(relatedObject);
               }
               bufferedWriter.newLine();
               bufferedWriter.write("Error" + i + ".message = " + eMofStatus.getMessage());
               bufferedWriter.newLine();
               bufferedWriter.write("Error" + i + ".path = " + uriFragment);
               bufferedWriter.newLine();
               i++;
            }
         }
         bufferedWriter.flush();
         fileWriter.close();
      }

      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   public static String computeURIString(EObject eObject)
   {
      String uriString = "";
      if (eObject instanceof ObjectVariable)
      {
         ObjectVariable objectVariable = (ObjectVariable) eObject;
         StoryNode storyNode = objectVariable.getPattern().getStoryNode();
         uriString = computeURIString(storyNode) + DELIM + getObjVarString(objectVariable.getName());
      } else if (eObject instanceof LinkVariable)
      {
         LinkVariable linkVariable = (LinkVariable) eObject;
         StoryNode storyNode = linkVariable.getPattern().getStoryNode();
         uriString = computeURIString(storyNode) + DELIM + getLinkVarString(linkVariable.getTarget().getName(), linkVariable.getSource().getName());
      }

      else if (eObject instanceof StoryNode)
      {
         StoryNode storyNode = (StoryNode) eObject;
         Activity activity = storyNode.getOwningActivity();
         uriString = computeURIString(activity) + DELIM + getStoryNodeString(storyNode.getName());
      } else if (eObject instanceof StopNode)
      {
         StopNode stopNode = (StopNode) eObject;
         Activity activity = stopNode.getOwningActivity();
         uriString = computeURIString(activity) + DELIM + getStopNodeString(stopNode.getName());
      } else if (eObject instanceof StartNode)
      {
         StartNode startNode = (StartNode) eObject;
         Activity activity = startNode.getOwningActivity();
         uriString = computeURIString(activity) + DELIM + getStartNodeString(startNode.getName());
      } else if (eObject instanceof ActivityEdge)
      {
         ActivityEdge activityEdge = (ActivityEdge) eObject;
         Activity activity = activityEdge.getOwningActivity();
         uriString = computeURIString(activity) + DELIM + getActivityEdgeString(activityEdge.getSource().getName(), activityEdge.getTarget().getName());
      } else if (eObject instanceof Activity)
      {
         Activity activity = (Activity) eObject;
         EOperation owningOperation = activity.getOwningOperation();
         uriString = computeURIString(owningOperation) + DELIM + getActivityString();
      } else if (eObject instanceof EOperation)
      {
         EOperation eOperation = (EOperation) eObject;
         EClass eClass = eOperation.getEContainingClass();
         uriString = computeURIString(eClass) + DELIM + getEOperationString(eOperation.getName());         
      } else if (eObject instanceof EClass)
      {
         EClass eClass = (EClass) eObject;
         EPackage ePackage = eClass.getEPackage();
         uriString = computeURIString(ePackage) + DELIM + getEClassString(eClass.getName());
      } else if (eObject instanceof EPackage)
      {
         EPackage ePackage = (EPackage) eObject;
         EPackage eParentPackage = ePackage.getESuperPackage();
         if (eParentPackage != null)
            uriString = computeURIString(eParentPackage) + DELIM + getEPackageString(ePackage.getName());
         else
            uriString = getEPackageString(ePackage.getName());
      }
      return uriString;
   }
   
   @Override
   protected boolean processResource(IProgressMonitor monitor) throws CoreException
   {
      SDMCodegeneratorHelper sdmCodegen = null;
      try
      {
         monitor.beginTask(getProgressBarMessage(), 20 * WorkspaceHelper.PROGRESS_SCALE);

         MoflonProperties moflonProperties = new MoflonProperties(getProject(), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));

         // Retrieve Ecore file
         IFile ecoreFile = getEcoreFile();
         if (!ecoreFile.exists())
         {
            logger.fatal("Unable to generate code: " + ecoreFile + " does not exist in project!");

            // Create marker
            IMarker marker = getProject().createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.MESSAGE, "Cannot find: " + ecoreFile.getProjectRelativePath().toString());
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            marker.setAttribute(IMarker.LOCATION, ecoreFile.getProjectRelativePath().toString());

            return false;
         }
         
         /* 1. Initialize Fujaba and EMF related components */
         monitor.subTask(getProject().getName() + ": Initializing CodeGen2 and EMF...");
         sdmCodegen = new SDMCodegeneratorHelper(ecoreFile, moflonProperties.debugMode(), moflonProperties.isGenTracingInstrumentation());
         monitor.worked(2 * WorkspaceHelper.PROGRESS_SCALE);
         
         // Validate project */
         if(!moflonProperties.skipValidation()){
            IStatus status = performValidation(monitor, sdmCodegen.getEcoreResource(), getProject());
            if(!status.isOK())
               throw new CoreException(status);
         }

         // Generate code for SDMs and embed the results as GenModel-sourced annotations for each eOperation
         // The EMF codegenerator automatically interprets these annotations as method implementations
         monitor.subTask(getProject().getName() + ": Invoking SDM codegenerator...");
         ResourceSet resourceSet = sdmCodegen.generateCode();
         monitor.worked(6 * WorkspaceHelper.PROGRESS_SCALE);

         /* 2. Generate EMF code with eclipse codegenerator */
         logger.debug("Generate EMF code with eclipse codegenerator");
         
         // Initialize EMF codegenerator with settings
         String basePackage = "";

         String modelFolder = WorkspaceHelper.GEN_FOLDER;
         String genmodelExtension = WorkspaceHelper.GEN_MODEL_EXT;

         IPath modelDirectory = ecoreFile.getProject().getFolder(modelFolder).getFullPath();

         EMFCodegeneratorHelper emfCodegen = new EMFCodegeneratorHelper(ecoreFile, basePackage, modelDirectory);

         // Generate GenModel if it doesn't exist or user explicitly wishes to replace it
         if (!getProject().getFile("model/" + getProject().getName() + genmodelExtension).exists() || moflonProperties.getReplaceGenModel())
            emfCodegen.createGenModel(resourceSet);

         // Generate Code
         monitor.subTask(getProject().getName() + ": Invoking EMF codegenerator...");
         emfCodegen.generateCode(resourceSet, new SubProgressMonitor(monitor, 9 * WorkspaceHelper.PROGRESS_SCALE));
         logger.debug("Returned from EMF codegenerator");

         // Add all dependencies
         WorkspaceHelper.addEMFDependenciesToClassPath(new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE), getProject());
      } catch (SDMParseException e)
      {
         String message = "Problems with SDMs: " + e.getMessage() + " in " + e.getContext().getQualifiedDisplayName() + " from " + e.getContext().getParentElement();
         logger.error(message);
         e.printStackTrace();

         Status status = new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, message, e);
         throw new CoreException(status);
      } catch (Exception e)
      {
         logger.error("Unable to build ecore file: " + e);
         e.printStackTrace();

         Status status = new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, "Problems generating code: " + e.getClass() + ", " + e.getMessage(), e);
         throw new CoreException(status);
      }

      // Add mapping for editors
      CoreActivator.addMappingForProject(getProject());
      monitor.worked(1*WorkspaceHelper.PROGRESS_SCALE);

      // Everything worked so safe to release (if an exception flew do NOT release!)
      if (sdmCodegen != null)
         sdmCodegen.release();

      monitor.done();

      return true;
   }

   protected IFile getEcoreFile()
   {
      return getProject().getFolder(WorkspaceHelper.MODEL_FOLDER).getFile(getProject().getName() + WorkspaceHelper.ECORE_FILE_EXTENSION);
   }

   @Override
   public boolean visit(IResource resource) throws CoreException
   {
      IPath pathToResource = resource.getProjectRelativePath();

      // Make sure changes are from the right ecore file according to convention
      if (pathToResource.equals(new Path(WorkspaceHelper.MODEL_FOLDER + resource.getProject().getName() + WorkspaceHelper.ECORE_FILE_EXTENSION)))
      {
         // Only generate code if resource wasn't deleted!
         if (resource.exists())
         {
            logger.debug("Build due to changes to: " + resource);
            return processResource(subMonitor);
         }
      }

      return false;
   }

   @Override
   public boolean visit(IResourceDelta delta) throws CoreException
   {
      // Get changes and call visit on all
      boolean buildSuccessful = false;
      IResourceDelta[] changes = delta.getAffectedChildren();
      for (int i = 0; i < changes.length; i++)
      {
         buildSuccessful = visit(changes[i].getResource());
         visit(changes[i]);
      }
      return buildSuccessful;
   }

}
