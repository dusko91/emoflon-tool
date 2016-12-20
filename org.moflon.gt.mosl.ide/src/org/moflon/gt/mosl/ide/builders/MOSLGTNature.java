package org.moflon.gt.mosl.ide.builders;

import java.util.Arrays;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.gervarro.eclipse.workspace.util.ProjectUtil;
import org.moflon.ide.core.runtime.natures.ProjectConfiguratorNature;

public class MOSLGTNature extends ProjectConfiguratorNature {
	public static final String NATURE_ID = "org.moflon.gt.ide.natures.MOSLGTNature";
	public static final String XTEXT_BUILDER_ID = "org.eclipse.xtext.ui.shared.xtextBuilder";
	public static final String XTEXT_NATURE_ID = "org.eclipse.xtext.ui.shared.xtextNature";

	@Override
	public ICommand[] updateBuildSpecs(final IProjectDescription description,
			ICommand[] buildSpecs, final boolean added) throws CoreException {
		if (added) {
			// Insert or move XText builder before IntegrationBuilder
			int xtextBuilderPosition = ProjectUtil.indexOf(buildSpecs, XTEXT_BUILDER_ID);
			if (xtextBuilderPosition < 0) {
				final ICommand xtextBuilder = description.newCommand();
				xtextBuilder.setBuilderName(XTEXT_BUILDER_ID);
				buildSpecs = Arrays.copyOf(buildSpecs, buildSpecs.length + 1);
				xtextBuilderPosition = buildSpecs.length - 1;
				buildSpecs[xtextBuilderPosition] = xtextBuilder;
			}
			// Insert or move MOSL-GT builder before IntegrationBuilder and after Xtext builder
			int moslBuilderPosition = ProjectUtil.indexOf(buildSpecs, MOSLGTBuilder.BUILDER_ID);
			if (moslBuilderPosition < 0) {
				final ICommand moslTGGBuilder = description.newCommand();
				moslTGGBuilder.setBuilderName(MOSLGTBuilder.BUILDER_ID);
				buildSpecs = Arrays.copyOf(buildSpecs, buildSpecs.length + 1);
				moslBuilderPosition = buildSpecs.length - 1;
				buildSpecs[moslBuilderPosition] = moslTGGBuilder;
			}
			if (xtextBuilderPosition > moslBuilderPosition) {
				final ICommand moslTGGBuilder = buildSpecs[moslBuilderPosition];
				System.arraycopy(buildSpecs, moslBuilderPosition + 1, buildSpecs, moslBuilderPosition,
						xtextBuilderPosition - moslBuilderPosition);
				moslBuilderPosition = xtextBuilderPosition--;
				buildSpecs[moslBuilderPosition] = moslTGGBuilder;
			}
		} else {
			int xtextBuilderPosition = ProjectUtil.indexOf(buildSpecs, XTEXT_BUILDER_ID);
			if (xtextBuilderPosition >= 0) {
				buildSpecs = ProjectUtil.remove(buildSpecs, xtextBuilderPosition);
			}
			int moslBuilderPosition = ProjectUtil.indexOf(buildSpecs, MOSLGTBuilder.BUILDER_ID);
			if (moslBuilderPosition >= 0) {
				buildSpecs = ProjectUtil.remove(buildSpecs, moslBuilderPosition);
			}
		}
		return buildSpecs;
	}

	@Override
	public String[] updateNatureIDs(String[] natureIDs, final boolean added) throws CoreException {
		if (added) {
			if (ProjectUtil.indexOf(natureIDs, XTEXT_NATURE_ID) < 0) {
				natureIDs = Arrays.copyOf(natureIDs, natureIDs.length + 1);
				natureIDs[natureIDs.length - 1] = XTEXT_NATURE_ID;
			}
			if (ProjectUtil.indexOf(natureIDs, NATURE_ID) < 0) {
				natureIDs = Arrays.copyOf(natureIDs, natureIDs.length + 1);
				natureIDs[natureIDs.length - 1] = NATURE_ID;
			}
		} else {
			int xtextNaturePosition = ProjectUtil.indexOf(natureIDs, XTEXT_NATURE_ID);
			if (xtextNaturePosition >= 0) {
				natureIDs = ProjectUtil.remove(natureIDs, xtextNaturePosition);
			}
			int moslTGGNaturePosition = ProjectUtil.indexOf(natureIDs, NATURE_ID);
			if (xtextNaturePosition >= 0) {
				natureIDs = ProjectUtil.remove(natureIDs, moslTGGNaturePosition);
			}
		}
		return natureIDs;
	}
}
