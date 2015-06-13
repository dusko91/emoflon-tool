package org.moflon.ide.core.runtime.builders;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.admin.MoflonProperties;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.util.eMoflonEMFUtil;

import MocaToMoflonTransformation.Exporter;
import MocaToMoflonTransformation.MocaToMoflonTransformationFactory;
import MocaTree.Node;
import SDMLanguage.activities.Activity;
import TGGLanguage.TripleGraphGrammar;

public class MetamodelBuilder extends AbstractBuilder
{
   private static final Logger logger = Logger.getLogger(MetamodelBuilder.class);

   private static final String DEPENDENCIES = "dependencies";

   private static String REPOSITORY_KEY = "repository";

   private static String INTEGRATION_KEY = "integration";

   private static String NAME_KEY = "name";

   private static String TYPE_KEY = "type";

   private static String WORKING_SET_KEY = "workingSet";

   private static String VIEW_KEY = "view"; 
   
   private static String VTTGRULESET = "vtggruleset";
   
   @Override
   protected void clean(IProgressMonitor monitor) throws CoreException
   {
      monitor.beginTask(getProgressBarMessage(), 1 * WorkspaceHelper.PROGRESS_SCALE);
      getProject().getFolder(WorkspaceHelper.TEMP_FOLDER).delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
      getProject().deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
      monitor.done();
   }

   @Override
   protected boolean processResource(IProgressMonitor monitor) throws CoreException
   {
      logger.debug("Start processing .temp folder");

      // Start task with 20 work units
      monitor.beginTask(getProgressBarMessage(), 22 * WorkspaceHelper.PROGRESS_SCALE);

      // 1. Load property file and retrieve hash table
      Properties properties = new Properties();
//--
      IProject project = getProject();
      IFile propertyFile = project.getFile(WorkspaceHelper.TEMP_FOLDER + "/" + project.getName() + ".properties");
      try
      {
         InputStream streamToPropertiesFile = propertyFile.getContents();
         properties.load(streamToPropertiesFile);
         streamToPropertiesFile.close();
         logger.debug("Properties loaded: " + properties);
      } catch (Exception e)
      {
         logger.debug("unable to load properties file or file not existing: " + e);

         // Create marker
         IMarker marker = getProject().createMarker(IMarker.PROBLEM);
         marker.setAttribute(IMarker.MESSAGE, "Cannot find any exported files to build");
         marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
         marker.setAttribute(IMarker.LOCATION, propertyFile.getProjectRelativePath().toString());

         return false;
      }
//--
      monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

      // 2. Iterate through properties and build temporary project map      
      Map<String, Map<String, String>> projectMap = tempMapBuild(properties);
      
      logger.debug("Parsed project map: " + projectMap);

      monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

      // 3. Create/update appropriate projects
      Collection<IJavaProject> dependentProjects = checkORUpdateProjects(monitor, projectMap);

      // 4. Check if all dependencies are fulfilled or can be corrected
      handleDependencies(dependentProjects, monitor);

      // 5. Clean and build projects with updated build paths;
      for (IJavaProject dependentProject : dependentProjects)
      {
         dependentProject.getProject().build(CLEAN_BUILD, newSPM(monitor));
         dependentProject.getProject().build(FULL_BUILD, newSPM(monitor));
      }

      monitor.done();

      return true;
   }

private Collection<IJavaProject> checkORUpdateProjects(IProgressMonitor monitor,
		Map<String, Map<String, String>> projectMap) throws CoreException,JavaModelException {

    Collection<IJavaProject> dependentProjects = new HashSet<IJavaProject>();
    
	for (Map<String, String> projectProperties : projectMap.values())
      {
         String type = projectProperties.get(TYPE_KEY);
         String name = projectProperties.get(NAME_KEY);
         String workingSetName = projectProperties.get(WORKING_SET_KEY);
         if (type.equals(REPOSITORY_KEY) || type.equals(INTEGRATION_KEY) || type.equals(VIEW_KEY))
         {
            IProject currentProject = getProject().getWorkspace().getRoot().getProject(name);
            // Create project if not existing
            createNewProjects(monitor, type, name, currentProject);

            // Move project to appropriate working set
            IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
            IWorkingSet workingSet = workingSetManager.getWorkingSet(workingSetName);
            if (workingSet == null)
            {
               workingSet = workingSetManager.createWorkingSet(workingSetName, new IAdaptable[] {});
               workingSet.setId(CoreActivator.JAVA_WORKING_SET_ID);
               workingSetManager.addWorkingSet(workingSet);
            }
            workingSetManager.addToWorkingSets(currentProject, new IWorkingSet[] { workingSet });

            /* Update ecore files */

            // Get source and destination files
            IFile projectEcoreFile = currentProject.getFile(WorkspaceHelper.MODEL_FOLDER + name + WorkspaceHelper.ECORE_FILE_EXTENSION);
            IFile tempEcoreFile = null;

            tempEcoreFile = createEcoreFile(monitor, name, projectEcoreFile,
					tempEcoreFile);

            // Extrahandling for View-Projects
            updateViewProjects(monitor, projectProperties, type, currentProject);
            
            // Extra handling for integration projects
            updateIntegrationProjects(monitor, type, name, currentProject,
					tempEcoreFile);

            // Set dependencies for all projects
            IJavaProject currentJavaProject = JavaCore.create(currentProject);
            dependentProjects.add(currentJavaProject);
            setDependentProjects(monitor, projectProperties, currentJavaProject);
         } else
         {
            logger.error("found unrecognized type of project: ignore this project");
            continue;
         }
      }
	return dependentProjects;
}

private void createNewProjects(IProgressMonitor monitor, String type,
		String name, IProject currentProject) throws CoreException,
		JavaModelException {
	if (!currentProject.exists())
	{
	   // Create project
	   IProject newProjectHandle = WorkspaceHelper.createProject(name, CoreActivator.PLUGIN_ID, newSPM(monitor));

	   // Create default structure in project
	   IFolder gen = WorkspaceHelper.addFolder(newProjectHandle, "gen", newSPM(monitor));
	   WorkspaceHelper.addFolder(newProjectHandle, "lib", newSPM(monitor));
	   WorkspaceHelper.addFolder(newProjectHandle, "model", newSPM(monitor));
	   WorkspaceHelper.addFolder(newProjectHandle, "instances", newSPM(monitor));

	   // Add Nature and Builders
	   addNatureandBuilders(monitor, type, newProjectHandle);

	   // Set up as consistent Java project
	   IJavaProject javaProject = WorkspaceHelper.setUpAsJavaProject(newProjectHandle, newSPM(monitor));
	   WorkspaceHelper.setAsSourceFolderInBuildpath(javaProject, new IFolder[] { gen }, newSPM(monitor));

	   // Create default property file and set metamodel project
	   MoflonProperties moflonProperties = new MoflonProperties(currentProject, newSPM(monitor));
	   moflonProperties.setMetamodelProject(getProject());
	   try
	   {
	      moflonProperties.persistProperties();
	   } catch (IOException e)
	   {
	      e.printStackTrace();
	      logger.error("Unable to persist moflon properties.");
	   }
	}
}

private void setDependentProjects(IProgressMonitor monitor,
		Map<String, String> projectProperties, IJavaProject currentJavaProject)
		throws JavaModelException {
	if (projectProperties.containsKey(DEPENDENCIES))
	{
	   String[] dependencies = projectProperties.get(DEPENDENCIES).split(",");

	   for (String dependency : dependencies)
	   {
	      IJavaProject dependencyJavaProject = JavaCore.create(getProject().getWorkspace().getRoot().getProject(dependency));
	      WorkspaceHelper.setProjectOnBuildpath(currentJavaProject, dependencyJavaProject, newSPM(monitor));
	   }
	}
}

private void addNatureandBuilders(IProgressMonitor monitor, String type,
		IProject newProjectHandle) throws CoreException {
	if (type.equals(REPOSITORY_KEY))
	      WorkspaceHelper.addNature(newProjectHandle, CoreActivator.REPOSITORY_NATURE_ID, newSPM(monitor));
	   else if(type.equals(VIEW_KEY)){
	      WorkspaceHelper.addNature(newProjectHandle, CoreActivator.VIEW_NATURE_ID, newSPM(monitor));
	      logger.debug("Add view Nature.");
	  }else
	      WorkspaceHelper.addNature(newProjectHandle, CoreActivator.INTEGRATION_NATURE_ID, newSPM(monitor));
}

private IFile createEcoreFile(IProgressMonitor monitor, String name,
		IFile projectEcoreFile, IFile tempEcoreFile) {
	try
	{
	   tempEcoreFile = createEcoreFile(name);

	   // Delete destination files if necessary
	   if (projectEcoreFile.exists())
	      projectEcoreFile.delete(true, newSPM(monitor));

	   // Copy files
	   tempEcoreFile.copy(projectEcoreFile.getFullPath(), IResource.FORCE | IResource.DERIVED, newSPM(monitor));
	} catch (Exception e)
	{
	   e.printStackTrace();
	   logger.error("unable to update ecore files: " + e);
	}
	return tempEcoreFile;
}

private void updateIntegrationProjects(IProgressMonitor monitor, String type,
		String name, IProject currentProject, IFile tempEcoreFile) {
	if (type.equals(INTEGRATION_KEY))
	{
	   IFile projectTGGFile = currentProject.getFile(WorkspaceHelper.MODEL_FOLDER + "/" + name + WorkspaceHelper.TGG_FILE_EXTENSION);
	   IFile tempTGGFile = getProject().getFile(WorkspaceHelper.TEMP_FOLDER + "/" + name + WorkspaceHelper.TGG_FILE_EXTENSION);
	   try
	   {
	      // Delete destination files if necessary
	      if (projectTGGFile.exists())
	         projectTGGFile.delete(true, newSPM(monitor));

	      // Copy files
	      tempTGGFile.copy(projectTGGFile.getFullPath(), IResource.FORCE | IResource.DERIVED, newSPM(monitor));

	      // Create .gen.ecore
	      IFile projectGenEcoreFile = currentProject.getFile(WorkspaceHelper.MODEL_FOLDER + name + ".gen" + WorkspaceHelper.ECORE_FILE_EXTENSION);
	      if (projectGenEcoreFile.exists())
	         projectGenEcoreFile.delete(true, newSPM(monitor));

	      tempEcoreFile.copy(projectGenEcoreFile.getFullPath(), IResource.FORCE | IResource.DERIVED, newSPM(monitor));
	   } catch (CoreException e)
	   {
	      logger.error("unable to update tgg file: " + e);
	   }
	}
}

private void updateViewProjects(IProgressMonitor monitor,
		Map<String, String> projectProperties, String type,
		IProject currentProject) {
	if(type.equals(VIEW_KEY)) {
		IFile projectTGGFile, projectTGGEcoreFile = null;
		IFile tempTGGFile, tempTGGEcoreFile = null;

		// and view projects, where the tgg file is an extra property
		logger.debug("vttgruleset: " + projectProperties.get(VTTGRULESET));
		projectTGGFile = currentProject.getFile(WorkspaceHelper.MODEL_FOLDER + "/" + projectProperties.get(VTTGRULESET) + WorkspaceHelper.TGG_FILE_EXTENSION);
		projectTGGEcoreFile = currentProject.getFile(WorkspaceHelper.MODEL_FOLDER + "/" + projectProperties.get(VTTGRULESET) + WorkspaceHelper.ECORE_FILE_EXTENSION);
		tempTGGFile = getProject().getFile(WorkspaceHelper.TEMP_FOLDER + "/" + projectProperties.get(VTTGRULESET) + WorkspaceHelper.TGG_FILE_EXTENSION);
		tempTGGEcoreFile = getProject().getFile(WorkspaceHelper.TEMP_FOLDER + "/" + projectProperties.get(VTTGRULESET) + WorkspaceHelper.ECORE_FILE_EXTENSION);
		
		logger.debug("Try to copy TGG-Rules");
		try
		{
	      // Delete destination files if necessary
	      if (projectTGGFile.exists())
	         projectTGGFile.delete(true, newSPM(monitor));

	      if (projectTGGEcoreFile.exists())
	          projectTGGEcoreFile.delete(true, newSPM(monitor));
	      // Copy files
	      tempTGGFile.copy(projectTGGFile.getFullPath(), IResource.FORCE | IResource.DERIVED, newSPM(monitor));
	      tempTGGEcoreFile.copy(projectTGGEcoreFile.getFullPath(), IResource.FORCE | IResource.DERIVED, newSPM(monitor));

	      // Create .gen.ecore
	      /*IFile projectGenEcoreFile = currentProject.getFile(WorkspaceHelper.MODEL_FOLDER + name + ".gen" + WorkspaceHelper.ECORE_FILE_EXTENSION);
	      if (projectGenEcoreFile.exists())
	         projectGenEcoreFile.delete(true, new SubProgressMonitor(monitor, 1 * 1000));

	      tempEcoreFile.copy(projectGenEcoreFile.getFullPath(), IResource.FORCE | IResource.DERIVED, new SubProgressMonitor(monitor, 1 * 1000));*/
	   } catch (CoreException e)
	   {
	      logger.error("unable to update vtgg file: " + e);
	   }
	}
}

private Map<String, Map<String, String>> tempMapBuild(Properties properties) {
    Map<String, Map<String, String>> projectMap = new HashMap<String, Map<String, String>>();
	for (Object key : properties.keySet())
      {
         int indexOfDelimiter = ((String) key).lastIndexOf(".");
         String ID = ((String) key).substring(0, indexOfDelimiter);
         String property = ((String) key).substring(indexOfDelimiter + 1);
         String value = properties.getProperty((String) key);

         if (!projectMap.containsKey(ID))
         {
            projectMap.put(ID, new HashMap<String, String>());
            projectMap.get(ID).put("name", ID);
         }
         projectMap.get(ID).put(property, value);
      }
	return projectMap;
}

private SubProgressMonitor newSPM(IProgressMonitor monitor) {
	return new SubProgressMonitor(monitor, 1 * 1000);
}

   private IFile createEcoreFile(String name) throws CoreException, IOException
   {
      IFile ecoreFile = getProject().getFile(WorkspaceHelper.TEMP_FOLDER + "/" + name + WorkspaceHelper.ECORE_FILE_EXTENSION);
      IFile mocaFile = getProject().getFile(WorkspaceHelper.TEMP_FOLDER + "/" + getProject().getName() + ".moca.xmi");

      // Use tree to recreate ecore files if possible
      if (mocaFile.exists())
      {
         // Load tree
         URI mocaFileURI = URI.createPlatformResourceURI(mocaFile.getFullPath().toString(), true);
         Node projectTree = (Node) eMoflonEMFUtil.loadModel(mocaFileURI, null);

         // Create EPackages
         Exporter exporter = MocaToMoflonTransformationFactory.eINSTANCE.createExporter();
         exporter.mocaToEcore(projectTree);

         if (exporter.getEpackages().isEmpty())
            throw new CoreException(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, "Unable to transform exported files to ecore models."));

         // Fill resource set
         ResourceSet packages = new ResourceSetImpl();
         for (EPackage outermostPackage : new ArrayList<EPackage>(exporter.getEpackages()))
         {
            // Create resource with URI from package
            Resource resource = packages.createResource(URI.createURI(outermostPackage.getNsURI()));

            // Add package to resource
            resource.getContents().add(outermostPackage);
         }

         // Now that metamodels are all in resources, persist SDMs
         for (Activity sdm : exporter.getSDMs().keySet())
         {
            eMoflonEMFUtil.embedSDMInEAnnotation(sdm, exporter.getSDMs().get(sdm), packages);
         }
         // Persist metamodels
         for (Resource resource : packages.getResources())
         {
            // Create outputstream for target file
            EObject eObject = resource.getContents().get(0);
            if (eObject instanceof EPackage)
            {
               EPackage pack = (EPackage) eObject;
               IFile file = getProject().getFile(WorkspaceHelper.TEMP_FOLDER + "/" + pack.getName() + WorkspaceHelper.ECORE_FILE_EXTENSION);
               OutputStream outputStream = new FileOutputStream(file.getLocation().toString());

               // Persist resource to outputstream and refresh workspace
               resource.save(outputStream, null);
               outputStream.close();
               file.refreshLocal(IResource.DEPTH_ZERO, null);
            }
         }
         
         // Persist TGG models
         for (TripleGraphGrammar tgg :  new ArrayList<TripleGraphGrammar>(exporter.getTggexporter().getTriplegraphgrammar()))
         {
            IFile file = getProject().getFile(WorkspaceHelper.TEMP_FOLDER + "/" + tgg.getName() + WorkspaceHelper.TGG_FILE_EXTENSION);
            eMoflonEMFUtil.saveModel(tgg, eMoflonEMFUtil.createFileURI(file.getLocation().toString(), false), packages);
            file.refreshLocal(IResource.DEPTH_ZERO, null);
         }
      }

      // Check if ecore file was created
      if(!ecoreFile.exists()){
         IStatus status = new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, "Unable to find " + ecoreFile.getName() + ". Have you exported everything?");
         throw new CoreException(status);
      }
      
      return ecoreFile;
   }

   private void handleDependencies(Collection<IJavaProject> allProjects, IProgressMonitor monitor) throws CoreException
   {
      for (IJavaProject iJavaProject : allProjects)
      {
         // Add eMoflon specific dependencies - but only if project is not part of Moflon itself!
         MoflonProperties moflonProperties = new MoflonProperties(iJavaProject.getProject(),
               new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
         if (!moflonProperties.isCoreProject())
         {
            WorkspaceHelper.setContainerOnBuildPath(iJavaProject, WorkspaceHelper.MOFLON_CONTAINER);

            if (iJavaProject.getProject().hasNature(CoreActivator.INTEGRATION_NATURE_ID))
            {
               WorkspaceHelper.setContainerOnBuildPath(iJavaProject, WorkspaceHelper.TIE_CONTAINER);
               WorkspaceHelper.setContainerOnBuildPath(iJavaProject, WorkspaceHelper.MOSL_CONTAINER);
            }

            for (IProject project : WorkspaceHelper.getProjectsOnBuildPath(iJavaProject.getProject()))
            {
               // Trouble try to fix some known cases
               if (project.getName().equals("MocaTree"))
               {
                  // Moca specific resolution
                  WorkspaceHelper.removeProjectFromBuildPath(iJavaProject, project);
                  WorkspaceHelper.setContainerOnBuildPath(iJavaProject, WorkspaceHelper.MOCA_CONTAINER);
               }

               // Handle moflon projects to avoid clashes with plugin
               if (isPartOfMoflon(project))
               {
                  WorkspaceHelper.removeProjectFromBuildPath(iJavaProject, project);
                  WorkspaceHelper.setContainerOnBuildPath(iJavaProject, WorkspaceHelper.MOSL_CONTAINER);
               }

               // Other resolutions ...
            }

            // Add some classpath containers
            if ("true".equals(getProject().getPersistentProperty(WorkspaceHelper.MOCA_SUPPORT)))
            {
               WorkspaceHelper.setContainerOnBuildPath(iJavaProject, WorkspaceHelper.MOCA_CONTAINER);
            }
         }
      }
   }

   private boolean isPartOfMoflon(IProject project)
   {
      return project.getName().equals("TGGLanguage") || project.getName().equals("SDMLanguage") || project.getName().equals("TGGRuntime");
   }

   @Override
   public boolean visit(IResource resource) throws CoreException
   {
      // Test if resource is .temp folder
      if (resource.getName().equals(WorkspaceHelper.TEMP_FOLDER))
      {
         logger.debug("Found .temp folder");
         processResource(subMonitor);
         return true;
      }

      return false;
   }

   @Override
   public boolean visit(IResourceDelta delta) throws CoreException
   {
      // Get changes and call visit on .temp folder
      IResourceDelta[] changes = delta.getAffectedChildren();
      for (int i = 0; i < changes.length; i++)
      {
         IResource resource = changes[i].getResource();
         if (resource.getName().equals(WorkspaceHelper.TEMP_FOLDER)
               && (changes[i].getKind() == IResourceDelta.CHANGED || changes[i].getKind() == IResourceDelta.ADDED))
         {
            return visit(resource);
         } else
            visit(changes[i]);
      }

      return false;
   }
}
