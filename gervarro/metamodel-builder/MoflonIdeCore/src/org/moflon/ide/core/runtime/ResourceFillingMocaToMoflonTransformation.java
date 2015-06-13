package org.moflon.ide.core.runtime;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.dependency.PackageRemappingDependency;
import org.moflon.ide.core.properties.MetamodelProperties;
import org.moflon.ide.core.runtime.builders.MetamodelBuilder;
import org.moflon.properties.MoflonPropertiesContainerHelper;
import org.moflon.util.MoflonUtil;
import org.moflon.util.UncheckedCoreException;

import MocaTree.Node;
import MoflonPropertyContainer.MoflonPropertiesContainer;

public class ResourceFillingMocaToMoflonTransformation extends BasicResourceFillingMocaToMoflonTransformation {
   private Map<String, MetamodelProperties> propertiesMap;

   private IProgressMonitor monitor;

   public ResourceFillingMocaToMoflonTransformation(final ResourceSet resourceSet,
		   final MetamodelBuilder resourceSetProcessor,
		   final IProject metamodelProject,
		   final Map<String, MetamodelProperties> propertiesMap,
		   final IProgressMonitor progressMonitor) {
	   super(resourceSet, resourceSetProcessor, metamodelProject);
	   this.monitor = progressMonitor;
	   this.propertiesMap = propertiesMap;
   }

   @Override
   public void handleOutermostPackage(final Node node, final EPackage outermostPackage)
   {
//      final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
//      final String projectName = getProjectName(node);
//      final String exported = lookupAttribute(node, MOCA_TREE_ATTRIBUTE_EXPORT);
//
//      // userDefinedNamespaceURI should point to the ecore file from which code was generated
//      // E.g., platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore
//      final URI namespaceURI = URI.createURI(lookupAttribute(node, MOCA_TREE_ATTRIBUTE_NS_URI));
//
//      if (exported.equals("true"))
//      {
//         if (MOCA_TREE_ATTRIBUTE_REPOSITORY_PROJECT.equals(node.getName()) || MOCA_TREE_ATTRIBUTE_INTEGRATION_PROJECT.equals(node.getName()))
//         {
//            // Handling (creating/opening) projects in Eclipse workspace
//            IProject workspaceProject = workspaceRoot.getProject(projectName);
//            if (!workspaceProject.exists())
//            {
//               handleOrReportMissingProject(node, workspaceProject);
//            }
//            assert workspaceProject != null && workspaceProject.exists();
//            if (!workspaceProject.isAccessible())
//            {
//               handleOrReportClosedProject(node, workspaceProject);
//            }
//            assert workspaceProject.isAccessible();
//
//            PackageRemappingDependency dependency = new PackageRemappingDependency(namespaceURI, false, false);
//     	      Resource resource = dependency.getResource(set, false, true);
//            resource.getContents().add(outermostPackage);
//            setEPackageURI(outermostPackage);
//            
//            handleOpenProject(node, workspaceProject);
//         } else
//         {
//            reportError("Project " + getProjectName(node) + " has unknown type " + node.getName());
//         }
//      } else
//      {
//         try
//         {
//            if (!MOCA_TREE_ATTRIBUTE_REPOSITORY_PROJECT.equals(node.getName()))
//            {
//               reportError("Project " + getProjectName(node) + " must always be exported");
//            }
//
//     	      final IProject project = workspaceRoot.getProject(projectName);
//            final PackageRemappingDependency resourceLoader = getResourceLoaderForNonExportedMetamodel(project, namespaceURI);
//            final Resource resource = resourceLoader.getResource(set, true, project.isAccessible());
//
//            final EPackage ePackage = (EPackage) resource.getContents().get(0);
//
//            // Copy EPackage URIs from existing metamodel to the constructed metamodel
//            copyEPackageURI(ePackage, outermostPackage);
//
//            // Replace existing metamodel content with the constructed metamodel in resourceSet
//            SDMEnhancedEcoreResource resourceReplacement = new SDMEnhancedEcoreResource(resource.getURI());
//            resourceReplacement.getContents().add(outermostPackage);
//            
//            // Make sure that this resource is not saved
//            resourceReplacement.getDefaultSaveOptions().put(SDMEnhancedEcoreResource.READ_ONLY, true);
//            
//            // If set contains resource remove it first
//            if(set.getResources().contains(resource))
//               set.getResources().remove(resource);
//               
//            // Now add wrapper resource 
//            set.getResources().add(resourceReplacement);
//         } catch (Exception e)
//         {
//            e.printStackTrace();
//         }
//      }
	   super.handleOutermostPackage(node, outermostPackage);
	   monitor.worked(1);
   }
   
   private final PackageRemappingDependency getResourceLoaderForNonExportedMetamodel(final IProject project,
		   final URI namespaceURI) {
	   if (project.isAccessible()) {
		   try
         {
            MoflonUtil.createMapping(set, project);
         } catch (IOException e)
         {
            e.printStackTrace();
         }
		   return new PackageRemappingDependency(namespaceURI, false, false);
	   } else {
		   return new PackageRemappingDependency(namespaceURI, true, true);
	   }
   }

   protected String getProjectName(final Node node)
   {
      return lookupAttribute(node, MOFLON_TREE_ATTRIBUTE_NAME);
   }

   protected String getPathToEcoreFile(final Node node)
   {
      return MoflonUtil.getDefaultPathToEcoreFileInProject(lookupAttribute(node, MOFLON_TREE_ATTRIBUTE_NAME));
   }

   protected void handleMissingProject(final Node node, final IProject project)
   {
	   final MetamodelProperties properties = propertiesMap.get(project.getName());
	   final MoflonProjectCreator moflonProjectCreator =
			   new MoflonProjectCreator(project, properties);
	   try {
		   WorkspaceTask.execute(moflonProjectCreator, false);
	   } catch (CoreException e) {
		   MOCA_TO_MOFLON_TRANSFORMATION_LOGGER.fatal("The creation of project " + project.getName() + " was interrupted by the user.");
		   reportError(e.getMessage());
	}
   }
   
   protected void handleClosedProject(final Node node, final IProject project)
   {
      try
      {
         project.open(new NullProgressMonitor());
      } catch (final CoreException e)
      {
         throw new UncheckedCoreException(e);
      }
   }

   protected void handleOpenProject(final Node node, final IProject project)
   {
      final MetamodelProperties properties = propertiesMap.get(project.getName());
      final MoflonPropertiesContainer moflonProps =
    		  createOrLoadMoflonProperties(project, properties.getMetamodelProjectName());
      final OpenProjectHandler openProjectHandler =
    		  new OpenProjectHandler(project, properties, moflonProps);
      try {
    	  WorkspaceTask.execute(openProjectHandler, false);
      } catch (CoreException e) {
    	  MOCA_TO_MOFLON_TRANSFORMATION_LOGGER.fatal("The handling of project " + project.getName() + " was interrupted by the user.");
    	  reportError(e.getMessage());
      }
   }

   private final MoflonPropertiesContainer createOrLoadMoflonProperties(final IProject project, final String metamodelProject)
   {
      IFile moflonProps = project.getFile(MoflonPropertiesContainerHelper.MOFLON_CONFIG_FILE);
      
      if (moflonProps.exists()) {
    	  URI projectURI = URI.createPlatformResourceURI(project.getName() + "/", true);
    	  URI moflonPropertiesURI = URI.createURI(MoflonPropertiesContainerHelper.MOFLON_CONFIG_FILE).resolve(projectURI);
    	  Resource moflonPropertiesResource = set.getResource(moflonPropertiesURI, true);
    	  return (MoflonPropertiesContainer) moflonPropertiesResource.getContents().get(0);
      } else {
    	  return MoflonPropertiesContainerHelper.createDefaultPropertiesContainer(project.getName(), metamodelProject);
      }
   }
}
