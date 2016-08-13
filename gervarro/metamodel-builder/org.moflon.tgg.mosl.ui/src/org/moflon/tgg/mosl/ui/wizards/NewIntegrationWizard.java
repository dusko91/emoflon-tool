package org.moflon.tgg.mosl.ui.wizards;

import static org.moflon.core.utilities.WorkspaceHelper.addAllFolders;
import static org.moflon.core.utilities.WorkspaceHelper.addAllFoldersAndFile;
import static org.moflon.core.utilities.WorkspaceHelper.createSubmonitorWith1Tick;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.gervarro.eclipse.workspace.autosetup.ProjectConfigurator;
import org.gervarro.eclipse.workspace.autosetup.WorkspaceAutoSetupModule;
import org.gervarro.eclipse.workspace.util.ProjectUtil;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.core.utilities.LogUtils;
import org.moflon.ide.core.runtime.MoflonProjectCreator;
import org.moflon.ide.core.runtime.ProjectNatureAndBuilderConfiguratorTask;
import org.moflon.tgg.mosl.builder.MOSLTGGNature;
import org.moflon.tgg.mosl.defaults.AttrCondDefLibraryProvider;
import org.moflon.tgg.mosl.defaults.DefaultFilesHelper;
import org.moflon.util.plugins.MetamodelProperties;

public class NewIntegrationWizard extends NewRepositoryWizard implements ProjectConfigurator
{

   private static final Logger logger = Logger.getLogger(NewIntegrationWizard.class);

   public static final String NEW_INTEGRATION_PROJECT_WIZARD_ID = "org.moflon.tgg.mosl.newIntegrationProject";

   @Override
   public void addPages()
   {
      projectInfo = new NewIntegrationProjectInfoPage();
      addPage(projectInfo);
   }

   @Override
   void createProject(IProgressMonitor monitor, IProject project, MetamodelProperties metamodelProperties) throws CoreException
   {
      metamodelProperties.put(MetamodelProperties.TYPE_KEY, MetamodelProperties.INTEGRATION_KEY);
      MoflonProjectCreator createMoflonProject = new MoflonProjectCreator(project, metamodelProperties);
      ResourcesPlugin.getWorkspace().run(createMoflonProject, createSubmonitorWith1Tick(monitor));

      final ProjectNatureAndBuilderConfiguratorTask natureAndBuilderConfiguratorTask = new ProjectNatureAndBuilderConfiguratorTask(project, false);
      natureAndBuilderConfiguratorTask.updateNatureIDs(this, true);
      natureAndBuilderConfiguratorTask.updateBuildSpecs(this, true);
      WorkspaceTask.execute(natureAndBuilderConfiguratorTask, false);
   }

   @Override
   void generateDefaultFiles(final IProgressMonitor monitor, IProject project) throws CoreException
   {
      String defaultSchema = DefaultFilesHelper.generateDefaultSchema(project.getName());
      IPath pathToSchema = new Path("src/org/moflon/tgg/mosl/Schema.tgg");
      addAllFoldersAndFile(project, pathToSchema, defaultSchema, createSubmonitorWith1Tick(monitor));

      addAllFolders(project, "src/org/moflon/tgg/mosl/rules", createSubmonitorWith1Tick(monitor));

      try
      {
         AttrCondDefLibraryProvider.syncAttrCondDefLibrary(project);
      } catch (IOException e)
      {
         LogUtils.error(logger, e);
      }

      // WorkspaceHelperUI.openDefaultEditorForFile(project.getFile(pathToSchema));
   }

   @Override
   public ICommand[] updateBuildSpecs(IProjectDescription description, ICommand[] buildSpecs, boolean added) throws CoreException
   {
      final String xtextBuilderID = "org.eclipse.xtext.ui.shared.xtextBuilder";
      final String moslTGGBuilderID = "org.moflon.tgg.mosl.codeadapter.mosltggbuilder";
      if (added)
      {
         int integrationBuilderPosition = ProjectUtil.indexOf(buildSpecs, "org.moflon.ide.core.runtime.builders.IntegrationBuilder");
         // Insert or move Xtext builder before IntegrationBuilder
         int xtextBuilderPosition = ProjectUtil.indexOf(buildSpecs, xtextBuilderID);
         if (xtextBuilderPosition < 0)
         {
            final ICommand xtextBuilder = description.newCommand();
            xtextBuilder.setBuilderName(xtextBuilderID);
            buildSpecs = Arrays.copyOf(buildSpecs, buildSpecs.length + 1);
            xtextBuilderPosition = buildSpecs.length - 1;
            buildSpecs[xtextBuilderPosition] = xtextBuilder;
         }
         if (integrationBuilderPosition < xtextBuilderPosition)
         {
            final ICommand xtextBuilder = buildSpecs[xtextBuilderPosition];
            System.arraycopy(buildSpecs, integrationBuilderPosition, buildSpecs, integrationBuilderPosition + 1,
                  xtextBuilderPosition - integrationBuilderPosition);
            xtextBuilderPosition = integrationBuilderPosition++;
            buildSpecs[xtextBuilderPosition] = xtextBuilder;
         }
         // Insert or move MOSL-TGG builder before IntegrationBuilder and after Xtext builder
         int moslTGGBuilderPosition = ProjectUtil.indexOf(buildSpecs, moslTGGBuilderID);
         if (moslTGGBuilderPosition < 0)
         {
            final ICommand moslTGGBuilder = description.newCommand();
            moslTGGBuilder.setBuilderName(moslTGGBuilderID);
            buildSpecs = Arrays.copyOf(buildSpecs, buildSpecs.length + 1);
            moslTGGBuilderPosition = buildSpecs.length - 1;
            buildSpecs[moslTGGBuilderPosition] = moslTGGBuilder;
         }
         assert xtextBuilderPosition < integrationBuilderPosition;
         if (xtextBuilderPosition > moslTGGBuilderPosition)
         {
            final ICommand moslTGGBuilder = buildSpecs[moslTGGBuilderPosition];
            System.arraycopy(buildSpecs, moslTGGBuilderPosition + 1, buildSpecs, moslTGGBuilderPosition, xtextBuilderPosition - moslTGGBuilderPosition);
            moslTGGBuilderPosition = xtextBuilderPosition--;
            buildSpecs[moslTGGBuilderPosition] = moslTGGBuilder;
         }
         if (integrationBuilderPosition < moslTGGBuilderPosition)
         {
            final ICommand moslTGGBuilder = buildSpecs[moslTGGBuilderPosition];
            System.arraycopy(buildSpecs, integrationBuilderPosition, buildSpecs, integrationBuilderPosition + 1,
                  moslTGGBuilderPosition - integrationBuilderPosition);
            moslTGGBuilderPosition = integrationBuilderPosition++;
            buildSpecs[moslTGGBuilderPosition] = moslTGGBuilder;
         }
      } else
      {
         int xtextBuilderPosition = ProjectUtil.indexOf(buildSpecs, xtextBuilderID);
         if (xtextBuilderPosition >= 0)
         {
            ICommand[] oldBuilderSpecs = buildSpecs;
            buildSpecs = new ICommand[oldBuilderSpecs.length - 1];
            if (xtextBuilderPosition > 0)
            {
               System.arraycopy(oldBuilderSpecs, 0, buildSpecs, 0, xtextBuilderPosition);
            }
            if (xtextBuilderPosition == buildSpecs.length)
            {
               System.arraycopy(oldBuilderSpecs, xtextBuilderPosition + 1, buildSpecs, xtextBuilderPosition, buildSpecs.length - xtextBuilderPosition);
            }
         }
         int moslTGGBuilderPosition = ProjectUtil.indexOf(buildSpecs, moslTGGBuilderID);
         if (moslTGGBuilderPosition >= 0)
         {
            ICommand[] oldBuilderSpecs = buildSpecs;
            buildSpecs = new ICommand[oldBuilderSpecs.length - 1];
            if (moslTGGBuilderPosition > 0)
            {
               System.arraycopy(oldBuilderSpecs, 0, buildSpecs, 0, moslTGGBuilderPosition);
            }
            if (moslTGGBuilderPosition == buildSpecs.length)
            {
               System.arraycopy(oldBuilderSpecs, moslTGGBuilderPosition + 1, buildSpecs, moslTGGBuilderPosition, buildSpecs.length - moslTGGBuilderPosition);
            }
         }
      }
      return buildSpecs;
   }

   @Override
   public String[] updateNatureIDs(String[] natureIDs, boolean added) throws CoreException
   {
      final String xtextNatureID = "org.eclipse.xtext.ui.shared.xtextNature";
      final String moslTGGNatureID = MOSLTGGNature.NATURE_ID;
      if (added)
      {
         if (ProjectUtil.indexOf(natureIDs, xtextNatureID) < 0)
         {
            natureIDs = Arrays.copyOf(natureIDs, natureIDs.length + 1);
            natureIDs[natureIDs.length - 1] = xtextNatureID;
         }
         if (ProjectUtil.indexOf(natureIDs, moslTGGNatureID) < 0)
         {
            natureIDs = Arrays.copyOf(natureIDs, natureIDs.length + 1);
            natureIDs[natureIDs.length - 1] = moslTGGNatureID;
         }
      } else
      {
         int xtextNaturePosition = ProjectUtil.indexOf(natureIDs, xtextNatureID);
         if (xtextNaturePosition >= 0)
         {
            natureIDs = WorkspaceAutoSetupModule.remove(natureIDs, xtextNaturePosition);
         }
         int moslTGGNaturePosition = ProjectUtil.indexOf(natureIDs, moslTGGNatureID);
         if (xtextNaturePosition >= 0)
         {
            natureIDs = WorkspaceAutoSetupModule.remove(natureIDs, moslTGGNaturePosition);
         }
      }
      return natureIDs;
   }
}
