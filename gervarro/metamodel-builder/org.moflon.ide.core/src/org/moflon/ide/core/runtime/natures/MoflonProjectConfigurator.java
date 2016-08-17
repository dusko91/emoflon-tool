package org.moflon.ide.core.runtime.natures;

import java.util.Arrays;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.gervarro.eclipse.workspace.autosetup.WorkspaceAutoSetupModule;
import org.gervarro.eclipse.workspace.util.ProjectUtil;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.CoreActivator;

public class MoflonProjectConfigurator extends ProjectConfiguratorNature {
	private final String natureID;
	private final String builderID;

	public MoflonProjectConfigurator(final boolean isIntegrationProject) {
		this.natureID = isIntegrationProject ? 
				WorkspaceHelper.INTEGRATION_NATURE_ID : WorkspaceHelper.REPOSITORY_NATURE_ID;
		this.builderID = isIntegrationProject ?
				CoreActivator.INTEGRATION_BUILDER_ID : CoreActivator.REPOSITORY_BUILDER_ID;
	}

	@Override
	public String[] updateNatureIDs(String[] natureIDs, final boolean added) throws CoreException {
		if (added) {
			if (ProjectUtil.indexOf(natureIDs, natureID) < 0) {
				natureIDs = Arrays.copyOf(natureIDs, natureIDs.length + 1);
				natureIDs[natureIDs.length-1] = natureID;
			}
		} else {
			int naturePosition = ProjectUtil.indexOf(natureIDs, natureID);
			if (naturePosition >= 0) {
				natureIDs = WorkspaceAutoSetupModule.remove(natureIDs, naturePosition);
			}
		}
		return natureIDs;
	}

	@Override
	public ICommand[] updateBuildSpecs(final IProjectDescription description, ICommand[] buildSpecs, final boolean added) throws CoreException {
		if (added) {
			int javaBuilderPosition = ProjectUtil.indexOf(buildSpecs, "org.eclipse.jdt.core.javabuilder");
			int moflonBuilderPosition = ProjectUtil.indexOf(buildSpecs, builderID);
			if (moflonBuilderPosition < 0) {
				final ICommand moflonBuilder = description.newCommand();
				moflonBuilder.setBuilderName(builderID);
				buildSpecs = Arrays.copyOf(buildSpecs, buildSpecs.length + 1);
				moflonBuilderPosition = buildSpecs.length - 1;
				buildSpecs[moflonBuilderPosition] = moflonBuilder;
			} 
			if (javaBuilderPosition < moflonBuilderPosition) {
				final ICommand moflonBuilder = buildSpecs[moflonBuilderPosition];
				System.arraycopy(buildSpecs, javaBuilderPosition, buildSpecs, javaBuilderPosition+1, moflonBuilderPosition-javaBuilderPosition);
				moflonBuilderPosition = javaBuilderPosition++;
				buildSpecs[moflonBuilderPosition] = moflonBuilder;
			}
		} else {
			int moflonBuilderPosition = ProjectUtil.indexOf(buildSpecs, builderID);
			if (moflonBuilderPosition >= 0) {
				ICommand[] oldBuilderSpecs = buildSpecs;
				buildSpecs = new ICommand[oldBuilderSpecs.length - 1];
				if (moflonBuilderPosition > 0) {
					System.arraycopy(oldBuilderSpecs, 0, buildSpecs, 0, moflonBuilderPosition);
				}
				if (moflonBuilderPosition == buildSpecs.length) {
					System.arraycopy(oldBuilderSpecs, moflonBuilderPosition+1, buildSpecs, moflonBuilderPosition, buildSpecs.length-moflonBuilderPosition);
				}
			}
		}
		return buildSpecs;
	}
}