package org.moflon.tgg.mosl.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.gervarro.eclipse.workspace.util.AntPatternCondition;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.runtime.builders.AbstractVisitorBuilder;

public class MoslTGGBuilder extends AbstractVisitorBuilder {
	public static final String BUILDER_ID = "org.moflon.tgg.mosl.codeadapter.mosltggbuilder";

	public MoslTGGBuilder() {
		super(new AntPatternCondition(new String[] { "src/org/moflon/tgg/mosl" }));
	}

	@Override
	protected AntPatternCondition getTriggerCondition(IProject project) {
		try {
			if (project.hasNature(WorkspaceHelper.REPOSITORY_NATURE_ID) ||
					project.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID)) {
				return new AntPatternCondition(new String[] { "gen/**" });
			}
		} catch (final CoreException e) {
			// Do nothing
		}
		return new AntPatternCondition(new String[0]);
	}

	@Override
	protected void processResource(IResource resource, int kind, Map<String, String> args, IProgressMonitor monitor) {
        new MOSLTGGConversionHelper().generateTGGModel(resource);
        removeXtextMarkers();
	}

	private final void removeXtextMarkers() {
		try {
			getProject().deleteMarkers(org.eclipse.xtext.ui.MarkerTypes.FAST_VALIDATION, true, IResource.DEPTH_INFINITE);
		} catch (final CoreException e) {
			e.printStackTrace();
		}
	}
}
