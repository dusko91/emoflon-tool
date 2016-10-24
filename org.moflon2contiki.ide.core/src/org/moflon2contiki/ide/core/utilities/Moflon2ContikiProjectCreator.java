package org.moflon2contiki.ide.core.utilities;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.moflon.core.propertycontainer.MoflonPropertiesContainer;
import org.moflon.core.propertycontainer.MoflonPropertiesContainerHelper;
import org.moflon.core.propertycontainer.SDMCodeGeneratorIds;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.runtime.MoflonProjectCreator;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon2contiki.ide.core.ContikiCoreActivator;

/**
 * Replaces {@link MoflonProjectCreator}. Replacement was necessary as addNatureAndBuilders is declared private and can therefore not be overriden.
 * Also adds the default property files needed for code generation.
 * @author David Giessing
 *
 */
public class Moflon2ContikiProjectCreator implements IWorkspaceRunnable{
	
	 private String projectName;
	 private static String constantPropertiesContent=
			 "#Set to True if dropping unidirectional edges is desired \n"
	 		+"dropUnidirectionalEdges = true\n"
			+"#set number of matches allowed per PM method\n"
	 		+"MAX_MATCH_COUNT = 20\n"
	 		+"#place the Names of the tcMethods in the Metamodel here as CSV. Naming should be: tc_<name>\n"
			+"tcMethods = \n"
	 		+"#place the parameters for the tc method call here as CSV. should look like: tc_<name> = value, value\n"
			+"# it is also possible to use the constants from down here, for this the value should be const-<constname>\n"
	 		+"#Define your Constants here: Should look like const-<constname>=value\n";
	 private static String mapPropertiesContent=
			 "#Define your Mapping here: \n"
			 + "#the Key is the EClass, and the value is the C Struct you want it to be mapped to \n"
			 + "Node = networkaddr_t\n"
	 		 + "Link = neighbor_t\n";
	 /*
	  * TODO: extend this list until it includes all EMoflon built in types
	  */
	   private String type;

	   private String metaModelProjectName;

	   public void setProjectName(final String projectName)
	   {
	      this.projectName = projectName;
	   }

	   public void setType(final String type)
	   {
	      this.type = type;
	   }

	   public void setMetaModelProjectName(final String metaModelProjectName)
	   {
	      this.metaModelProjectName = metaModelProjectName;
	   }

	   @Override
	   public void run(final IProgressMonitor monitor) throws CoreException
	   {
	      final IWorkspace workspace = ResourcesPlugin.getWorkspace();
	      final IWorkspaceRoot workspaceRoot = workspace.getRoot();
	      final IProject workspaceProject = workspaceRoot.getProject(projectName);
	      if (workspaceProject.exists())
	      {
	         return;
	      }
	      try
	      {
	         monitor.beginTask("Creating project " + projectName, 12);
	         final IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);

	         workspaceProject.create(description, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	         workspaceProject.open(WorkspaceHelper.createSubmonitorWith1Tick(monitor));

	         createFoldersIfNecessary(workspaceProject, WorkspaceHelper.createSubMonitor(monitor, 4));
	         addGitIgnoreFiles(workspaceProject, WorkspaceHelper.createSubMonitor(monitor, 2));

	         addNatureAndBuilders(monitor, this.type, workspaceProject);

	         MoflonPropertiesContainer moflonProperties = MoflonPropertiesContainerHelper.createDefaultPropertiesContainer(workspaceProject.getName(),
	               metaModelProjectName);
	         setDefaultCodeGenerator(moflonProperties);
	         monitor.worked(1);

	         MoflonPropertiesContainerHelper.save(moflonProperties, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	         
	         WorkspaceHelper.addFile(workspaceProject,workspaceProject.getName()+"Constants.properties",constantPropertiesContent,WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	         
	         WorkspaceHelper.addFile(workspaceProject,workspaceProject.getName()+"EClassToStructs.properties",mapPropertiesContent,WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	      } finally
	      {
	         monitor.done();
	      }
	   }

	   private static void addGitIgnoreFiles(final IProject project, final IProgressMonitor monitor) throws CoreException
	   {
	      try
	      {
	         monitor.beginTask("Creating .gitignore files", 2);
	         IFile genGitIgnore = WorkspaceHelper.getGenFolder(project).getFile(".gitignore");
	         if (!genGitIgnore.exists())
	         {
	            genGitIgnore.create(new ByteArrayInputStream("*".getBytes()), true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	         }

	         IFile modelGitIgnore = WorkspaceHelper.getModelFolder(project).getFile(".gitignore");
	         if (!modelGitIgnore.exists())
	         {
	            modelGitIgnore.create(new ByteArrayInputStream("*".getBytes()), true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	         }
	      } finally
	      {
	         monitor.done();
	      }
	   }

	   public static void createFoldersIfNecessary(final IProject project, final IProgressMonitor monitor) throws CoreException
	   {
	      try
	      {
	         monitor.beginTask("Creating folders within project", 5);
	         WorkspaceHelper.createFolderIfNotExists(WorkspaceHelper.getGenFolder(project), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	         WorkspaceHelper.createFolderIfNotExists(WorkspaceHelper.getModelFolder(project), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	      } finally
	      {
	         monitor.done();
	      }
	   }

	   private void setDefaultCodeGenerator(final MoflonPropertiesContainer moflonProps)
	   {
	      if (type.equals(MetamodelProperties.INTEGRATION_KEY))
	         moflonProps.getSdmCodegeneratorHandlerId().setValue(SDMCodeGeneratorIds.DEMOCLES_REVERSE_NAVI);
	      else
	         moflonProps.getSdmCodegeneratorHandlerId().setValue(SDMCodeGeneratorIds.DEMOCLES);
	   }

	   private void addNatureAndBuilders(final IProgressMonitor monitor, final String type, final IProject newProjectHandle) throws CoreException
	   {
	      try
	      {
	            WorkspaceHelper.addNature(newProjectHandle, ContikiCoreActivator.REPOSITORY_NATURE_ID, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
	      } finally
	      {
	         monitor.done();
	      }
	   }
}
