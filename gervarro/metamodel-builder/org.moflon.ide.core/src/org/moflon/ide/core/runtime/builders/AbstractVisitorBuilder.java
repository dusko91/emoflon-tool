package org.moflon.ide.core.runtime.builders;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.gervarro.eclipse.workspace.util.RelevantElementCollectingBuilder;
import org.gervarro.eclipse.workspace.util.VisitorCondition;
import org.moflon.ide.core.CoreActivator;

abstract public class AbstractVisitorBuilder extends RelevantElementCollectingBuilder {
	public static final Comparator<IProject> PROJECT_COMPARATOR =
			new Comparator<IProject>() {
				@Override
				public int compare(IProject left, IProject right) {
					return left.getName().compareTo(right.getName());
				}
			};
			
	protected final TreeSet<IProject> interestingProjects =
			new TreeSet<IProject>(PROJECT_COMPARATOR);
	
	protected AbstractVisitorBuilder(VisitorCondition condition) {
		super(condition);
	}

	protected void processResourceDelta(final IResourceDelta delta, final int kind,
			final Map<String,String> args, final IProgressMonitor monitor) {
		if (delta.getKind() != IResourceDelta.REMOVED) {
			super.processResourceDelta(delta, kind, args, monitor);
		}
	}

	public final boolean addInterestingProject(IProject project) {
		return interestingProjects.add(project);
	}

	protected final IProject[] calculateInterestingProjects() {
		IProject[] result = new IProject[interestingProjects.size()];
		return interestingProjects.toArray(result);
	}
	
	protected final void processProblemStatus(IStatus status, IResource resource) throws CoreException {
		if (status.isOK()) {
			return;
		}
		if (status.isMultiStatus()) {
			for (IStatus child : ((MultiStatus) status).getChildren()) {
				processProblemStatus(child, resource);
			}
		} else {
			CoreActivator.createProblemMarker(resource, status.getMessage(),
					CoreActivator.convertStatusSeverityToMarkerSeverity(status.getSeverity()),
					resource.getProjectRelativePath().toString());
		}
	}
}
