package org.moflon.ide.core.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.gervarro.eclipse.workspace.util.VisitorCondition;

public final class CleanVisitor implements IResourceVisitor {
	private final IProject project;
	private final VisitorCondition condition;

	public CleanVisitor(IProject project, VisitorCondition condition) {
		this.project = project;
		this.condition = condition;
	}

	public boolean visit(final IResource resource) {
		final int resourceType = resource.getType();
		if (resourceType == IResource.PROJECT) {
			return resource.isAccessible() && resource.getProject() == project;
		} else if (resourceType != IResource.ROOT) {
			final String path = 
					resource.getProjectRelativePath().toString();
			boolean exactMatchFound = condition.isExactMatch(path);
			if (exactMatchFound) {
				if (resource.isAccessible()) {
					try {
						resource.delete(true, new NullProgressMonitor());
					} catch (CoreException e) {
						// Do nothing
					}
				}
				return false;
			} else {
				return condition.isPrefixMatch(path);
			}
		}
		return false;
	}
	
}
