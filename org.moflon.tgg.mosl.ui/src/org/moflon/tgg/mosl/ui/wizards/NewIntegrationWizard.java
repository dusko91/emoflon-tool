package org.moflon.tgg.mosl.ui.wizards;

import static org.moflon.core.utilities.WorkspaceHelper.addAllFolders;
import static org.moflon.core.utilities.WorkspaceHelper.addAllFoldersAndFile;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.ide.core.runtime.MoflonProjectCreator;
import org.moflon.ide.core.runtime.ProjectNatureAndBuilderConfiguratorTask;
import org.moflon.tgg.mosl.builder.MOSLTGGNature;
import org.moflon.tgg.mosl.defaults.AttrCondDefLibraryProvider;
import org.moflon.tgg.mosl.defaults.DefaultFilesHelper;
import org.moflon.util.plugins.MetamodelProperties;

public class NewIntegrationWizard extends NewRepositoryWizard
{
   public static final String NEW_INTEGRATION_PROJECT_WIZARD_ID = "org.moflon.tgg.mosl.newIntegrationProject";

   @Override
   public void addPages()
   {
      projectInfo = new NewIntegrationProjectInfoPage();
      addPage(projectInfo);
   }

   @Override
   protected void createProject(IProgressMonitor monitor, IProject project, MetamodelProperties metamodelProperties) throws CoreException
   {
      metamodelProperties.put(MetamodelProperties.TYPE_KEY, MetamodelProperties.INTEGRATION_KEY);
      MoflonProjectCreator createMoflonProject = new MoflonProjectCreator(project, metamodelProperties);
      final SubMonitor subMon = SubMonitor.convert(monitor, "Creating project", 2);
      ResourcesPlugin.getWorkspace().run(createMoflonProject, subMon.split(1));

      final ProjectNatureAndBuilderConfiguratorTask natureAndBuilderConfiguratorTask = new ProjectNatureAndBuilderConfiguratorTask(project, false);
      final MOSLTGGNature natureAndBuilderConfigurator = new MOSLTGGNature();
      natureAndBuilderConfiguratorTask.updateNatureIDs(natureAndBuilderConfigurator, true);
      natureAndBuilderConfiguratorTask.updateBuildSpecs(natureAndBuilderConfigurator, true);
      WorkspaceTask.executeInCurrentThread(natureAndBuilderConfiguratorTask, IWorkspace.AVOID_UPDATE, subMon.split(1));
   }

   @Override
   protected void generateDefaultFiles(final IProgressMonitor monitor, IProject project) throws CoreException
   {
      String defaultSchema = DefaultFilesHelper.generateDefaultSchema(project.getName());
      String projectName = project.getProject().getName().replaceAll(Pattern.quote("."), "/");
      IPath pathToSchema = new Path("src/" + projectName + "/org/moflon/tgg/mosl/Schema.tgg");
      final SubMonitor subMon = SubMonitor.convert(monitor, "Generating default files", 2);
      addAllFoldersAndFile(project, pathToSchema, defaultSchema, subMon.split(1));

      addAllFolders(project, "src/" + projectName + "org/moflon/tgg/mosl/rules", subMon.split(1));

      AttrCondDefLibraryProvider.syncAttrCondDefLibrary(project);
   }
}
