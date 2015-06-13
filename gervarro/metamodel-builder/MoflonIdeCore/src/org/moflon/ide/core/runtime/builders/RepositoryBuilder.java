package org.moflon.ide.core.runtime.builders;

import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl.URIMap;
import org.gervarro.eclipse.workspace.util.AntPatternCondition;
import org.gervarro.eclipse.workspace.util.RelevantElementCollector;
import org.gervarro.eclipse.workspace.util.VisitorCondition;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.codegen.eclipse.MonitoredMetamodelLoader;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.runtime.CleanVisitor;
import org.moflon.properties.MoflonPropertiesContainerHelper;
import org.moflon.util.UncheckedCoreException;

import MoflonPropertyContainer.MoflonPropertiesContainer;

public class RepositoryBuilder extends AbstractVisitorBuilder {

	public RepositoryBuilder() {
		super(new AntPatternCondition(new String[] { "model/*.ecore" }));
	}
	
	@Override
	protected void processResource(IResource ecoreFile, int kind,
			Map<String, String> args, IProgressMonitor monitor) {
		CoreActivator.deleteMarkers(ecoreFile, IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
		final MoflonPropertiesContainer moflonProperties =
				MoflonPropertiesContainerHelper.load(getProject(), monitor);
		
		final ResourceSet set = CodeGeneratorPlugin.createDefaultResourceSet();

		final SubProgressMonitor metamodelLoaderMonitor = new SubProgressMonitor(monitor, 10);
		final MonitoredMetamodelLoader metamodelLoader =
				new MonitoredMetamodelLoader(set, (IFile) ecoreFile, moflonProperties);
		final IStatus metamodelLoaderStatus = metamodelLoader.run(metamodelLoaderMonitor);

		// Calculate interesting projects
		final IProject metamodelProject = ResourcesPlugin.getWorkspace().getRoot().getProject(
				moflonProperties.getMetaModelProject().getMetaModelProjectName());
		addInterestingProject(metamodelProject);
		
		final URIMap uriMap = (URIMap) set.getURIConverter().getURIMap();
		for (Resource resource : set.getResources()) {
			final URI uri = CodeGeneratorPlugin.getResolvedPlatformResourceURI(uriMap, resource.getURI());
			if (resource != ecoreFile && uri.isPlatformResource() && uri.segmentCount() >= 2) {
				final IProject project = CodeGeneratorPlugin.getWorkspaceProject(uri);
				if (project != null && !project.equals(getProject())) {
					addInterestingProject(project);
				}
			}
		}

		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		try {
			processProblemStatus(metamodelLoaderStatus, ecoreFile);
		} catch (CoreException e) {
			throw new UncheckedCoreException(e);
		}
		
		// TODO gervarro: Re-add code generation later on
//		final String engineIdentifier =
//				CodeGeneratorPlugin.getMethodBodyHandler(moflonProperties);
//		final MethodBodyHandler codeGenerationTask =
//				(MethodBodyHandler) Platform.getAdapterManager().loadAdapter(ecoreFile, engineIdentifier);
//		codeGenerationTask.setMoflonProperties(moflonProperties);
//
//		logger.info("Generating SDM code for: " + getProject());
//		
//		final IStatus status = codeGenerationTask.run(monitor);
	}

	@Override
	public ISchedulingRule getRule(int kind, Map<String, String> args) {
		return getProject();
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		VisitorCondition folderCondition = createCleanVisitorCondition();
		getProject().accept(createCleanVisitor(folderCondition), IResource.DEPTH_INFINITE, IContainer.INCLUDE_HIDDEN);
		CoreActivator.deleteMarkers(getProject(), IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
	}

	protected CleanVisitor createCleanVisitor(final VisitorCondition condition) {
		return new CleanVisitor(getProject(), condition);
	}

	protected VisitorCondition createCleanVisitorCondition() {
		return new AntPatternCondition(new String[] { "gen/**.java", "debug/**" });
	}

	@Override
	protected RelevantElementCollector createBuildVisitor(final VisitorCondition condition) {
		return new RelevantElementCollector(getProject(), condition);
	}
}
