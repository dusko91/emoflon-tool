package org.moflon.ide.core.runtime;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl.URIMap;
import org.gervarro.eclipse.task.ITask;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.runtime.builders.MetamodelBuilder;

public class ProjectDependencyAnalyzer implements ITask {
	private final MetamodelBuilder builder;
	private final IProject metamodelProject;
	private final IProject moflonProject;
	private final EPackage metamodelRoot;
	private final Set<IProject> interestingProjects =
			new TreeSet<IProject>(MetamodelBuilder.PROJECT_COMPARATOR);

	public ProjectDependencyAnalyzer(final MetamodelBuilder builder,
			final IProject metamodelProject,
			final IProject moflonProject,
			final EPackage metamodelRoot) {
		this.builder = builder;
		this.metamodelProject = metamodelProject;
		this.moflonProject = moflonProject;
		this.metamodelRoot = metamodelRoot;
	}
	
	public static final void analyzeDependencies(final MultiStatus status,
			final TreeSet<IProject> projectReferences, final Resource resource) {
		final URIMap uriMap = (URIMap) resource.getResourceSet().getURIConverter().getURIMap();
		for (TreeIterator<EObject> j = resource.getAllContents(); j.hasNext(); ) {
			EObject eObject = j.next();
			if (eObject instanceof EDataType) {
				j.prune();
			}
			for (EObject eCrossReference : eObject.eCrossReferences()) {
				if (eCrossReference instanceof EClass) {
					final EPackage referencedEPackage = ((EClassifier) eCrossReference).getEPackage();
					if (resource != referencedEPackage.eResource()) {
						final URI uri = CodeGeneratorPlugin.getResolvedPlatformResourceURI(uriMap, referencedEPackage.eResource().getURI());
						if (uri.isPlatformResource() && uri.segmentCount() >= 2) {
							final IProject project = CodeGeneratorPlugin.getWorkspaceProject(uri);
							if (project != null) {
								projectReferences.add(project);
							} else {
								status.add(new Status(IStatus.ERROR, CoreActivator.getModuleID(),
										"Project " + uri.segment(1) + " cannot be found in the workspace"));
							}
						}
					}
				}
			}
		}
	}
	
	public final void setInterestingProjects(Set<IProject> projects) {
		interestingProjects.addAll(projects);
	}

	@Override
	public IStatus run(IProgressMonitor monitor) {
		final MultiStatus status =
				new MultiStatus(CoreActivator.getModuleID(), 0, "Project dependency analysis failed", null);
		final TreeSet<IProject> projectReferences =
				new TreeSet<IProject>(MetamodelBuilder.PROJECT_COMPARATOR);
		analyzeDependencies(status, projectReferences, metamodelRoot.eResource());
		
		for (IProject reference : projectReferences) {
			if (interestingProjects.contains(reference)) {
				builder.addInterestingProject(reference);
			}
		}
		if (!status.isOK()) {
			return status;
		}
		
		try {
			final LinkedList<IBuildConfiguration> buildConfigs =
					new LinkedList<IBuildConfiguration>();
			buildConfigs.add(metamodelProject.getBuildConfig(IBuildConfiguration.DEFAULT_CONFIG_NAME));
			for (IProject project : projectReferences) {
				if (project.isAccessible()) {
					buildConfigs.add(project.getBuildConfig(IBuildConfiguration.DEFAULT_CONFIG_NAME));
				}
			}
			final IProjectDescription description = moflonProject.getDescription();
			IBuildConfiguration[] buildConfigArray = new IBuildConfiguration[buildConfigs.size()];
			buildConfigArray = buildConfigs.toArray(buildConfigArray);
			description.setBuildConfigReferences(IBuildConfiguration.DEFAULT_CONFIG_NAME, buildConfigArray);
			moflonProject.setDescription(description, monitor);
		} catch (CoreException e) {
			return new Status(IStatus.WARNING, CoreActivator.getModuleID(), 
					"Unable to set build configuration references for project " + moflonProject.getName(), e);
		}
		return Status.OK_STATUS;
	}

	@Override
	public String getTaskName() {
		return "Analyzing project dependency";
	}
}
