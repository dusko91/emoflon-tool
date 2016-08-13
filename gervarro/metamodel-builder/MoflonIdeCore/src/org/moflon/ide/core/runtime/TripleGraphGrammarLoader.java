package org.moflon.ide.core.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gervarro.eclipse.task.ITask;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.dependency.PackageRemappingDependency;
import org.moflon.util.WorkspaceHelper;

import TGGLanguage.TripleGraphGrammar;

public class TripleGraphGrammarLoader implements ITask {
	private final ResourceSet set;
	private final TripleGraphGrammar tgg;

	public TripleGraphGrammarLoader(final ResourceSet set, final TripleGraphGrammar tgg) {
		this.set = set;
		this.tgg = tgg;
	}

	@Override
	public IStatus run(IProgressMonitor monitor) {
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		final IProject workspaceProject = workspaceRoot.getProject(tgg.getName());
		assert workspaceProject.isAccessible();
		final URI projectURI = CodeGeneratorPlugin.lookupProjectURI(workspaceProject);
		final URI tggURI = URI.createURI("model/" + tgg.getName() + WorkspaceHelper.PRE_TGG_FILE_EXTENSION).resolve(projectURI);
		final PackageRemappingDependency resourceLoader =
				new PackageRemappingDependency(tggURI, false, false);
		final Resource tggResource = resourceLoader.getResource(set, false);
		tggResource.getContents().add(tgg);
		return Status.OK_STATUS;
	}

	@Override
	public String getTaskName() {
		return "Loading triple graph grammar specification " + tgg.getName();
	}
}