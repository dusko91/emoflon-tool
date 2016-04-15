package org.moflon.ide.core.runtime.builders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gervarro.eclipse.task.ITask;
import org.gervarro.eclipse.task.ProgressMonitoringJob;
import org.gervarro.eclipse.workspace.util.AntPatternCondition;
import org.gervarro.eclipse.workspace.util.RelevantElementCollector;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.moflon.eclipse.resource.SDMEnhancedEcoreResource;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.properties.MocaTreeEAPropertiesReader;
import org.moflon.ide.core.runtime.BasicResourceFillingMocaToMoflonTransformation;
import org.moflon.ide.core.runtime.NewResourceFillingMocaToMoflonTransformation;
import org.moflon.ide.core.runtime.ProjectDependencyAnalyzer;
import org.moflon.util.plugins.MetamodelProperties;

import MocaTree.Node;

/**
 * A metamodel builder that produces plugin projects
 */
public class NewMetamodelBuilder extends AbstractVisitorBuilder {

	public static final Logger logger = Logger.getLogger(NewMetamodelBuilder.class);

	public NewMetamodelBuilder() {
		super(new AntPatternCondition(new String[] { ".temp/*.moca.xmi" }));
	}

	protected void postprocess(final RelevantElementCollector buildVisitor, final int kind,
			final Map<String, String> args, final IProgressMonitor monitor) {
		if (kind == INCREMENTAL_BUILD || kind == AUTO_BUILD) {
			if (buildVisitor.getRelevantDeltas().size() == 1) {
				super.postprocess(buildVisitor, kind, args, monitor);
			}
		} else if (kind == FULL_BUILD) {
			if (buildVisitor.getRelevantResources().size() == 1) {
				super.postprocess(buildVisitor, kind, args, monitor);
			}
		}		
	}

	@Override
	protected void processResource(IResource mocaFile, int kind,
			Map<String, String> args, IProgressMonitor monitor) {
		CoreActivator.deleteMarkers(mocaFile, IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
		
		final String mocaFilePath = WorkspaceHelper.TEMP_FOLDER + "/" + getProject().getName() + WorkspaceHelper.MOCA_XMI_FILE_EXTENSION;
		if (mocaFile instanceof IFile && mocaFilePath.equals(mocaFile.getProjectRelativePath().toString()) &&
				mocaFile.isAccessible()) {
			logger.debug("Start processing .temp folder");
			monitor.beginTask("Building " + getProject().getName(), 22);
			monitor.worked(2);

			try {
				final URI workspaceURI = URI.createPlatformResourceURI("/", true);
				final URI projectURI = URI.createURI(getProject().getName() + "/", true).resolve(workspaceURI);

				// Create and initialize resource set
				final ResourceSet set = CodeGeneratorPlugin.createDefaultResourceSet();
				eMoflonEMFUtil.installCrossReferencers(set);

				// Load Moca tree in read-only mode
				final URI mocaFileURI = URI.createURI(mocaFilePath, true).resolve(projectURI);
				final Resource mocaTreeResource = set.getResource(mocaFileURI, true);
				final Node mocaTree = (Node) mocaTreeResource.getContents().get(0);

				final Map<String, MetamodelProperties> properties =
						new MocaTreeEAPropertiesReader().getProperties(getProject());

				SubProgressMonitor exporterSubMonitor = WorkspaceHelper.createSubmonitorWith1Tick(monitor);
				exporterSubMonitor.beginTask("Running MOCA-to-eMoflon transformation", properties.keySet().size());

				// Create and run exporter on Moca tree
				BasicResourceFillingMocaToMoflonTransformation exporter =
						new NewResourceFillingMocaToMoflonTransformation(set, this, getProject(), properties, exporterSubMonitor);
				exporter.mocaToEcore(mocaTree);
				exporterSubMonitor.done();

				if (exporter.getEpackages().isEmpty()) {
					final String errorMessage = "Unable to transform exported files to Ecore models";
					CoreActivator.createProblemMarker(mocaFile, errorMessage,
							IMarker.SEVERITY_ERROR, mocaFile.getProjectRelativePath().toString());
					logger.error(errorMessage);
					return;
				}

				// Remove mocaTreeResource
				set.getResources().remove(mocaTreeResource);

				// Enforce resource change notifications to update workspace plugin information
				ResourcesPlugin.getWorkspace().checkpoint(false);

				// Load resources (metamodels and tgg files)
				interestingProjects.clear();
				ITask[] taskArray = new ITask[exporter.getMetamodelLoaderTasks().size()];
				taskArray = exporter.getMetamodelLoaderTasks().toArray(taskArray);
				final IStatus metamodelLoaderStatus = ProgressMonitoringJob.executeSyncSubTasks(taskArray,
						new MultiStatus(CoreActivator.getModuleID(), 0, "Resource loading failed", null), monitor);
				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				if (!metamodelLoaderStatus.isOK()) {
					processProblemStatus(metamodelLoaderStatus, mocaFile);
					return;
				}

				// Analyze project dependencies
				ProjectDependencyAnalyzer[] dependencyAnalyzers =
						new ProjectDependencyAnalyzer[exporter.getProjectDependencyAnalyzerTasks().size()];
				dependencyAnalyzers = exporter.getProjectDependencyAnalyzerTasks().toArray(dependencyAnalyzers);
				for (ProjectDependencyAnalyzer analyzer : dependencyAnalyzers) {
					analyzer.setInterestingProjects(interestingProjects);
				}
				interestingProjects.clear();
				final IStatus projectDependencyAnalyzerStatus =
						ProgressMonitoringJob.executeSyncSubTasks(dependencyAnalyzers,
								new MultiStatus(CoreActivator.getModuleID(), 0, "Dependency analysis failed", null), monitor);
				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				if (!projectDependencyAnalyzerStatus.isOK()) {
					processProblemStatus(projectDependencyAnalyzerStatus, mocaFile);
					return;
				}

				// Prepare save options
				Map<Object, Object> saveOnlyIfChangedOption = new HashMap<Object, Object>();
				saveOnlyIfChangedOption.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
				saveOnlyIfChangedOption.put(SDMEnhancedEcoreResource.SAVE_GENERATED_PACKAGE_CROSSREF_URIS, true);

				// Persist resources (metamodels, tgg files and moflon.properties files)
				for (Resource resource : set.getResources()) {
					try {
						resource.save(saveOnlyIfChangedOption);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}

				monitor.done();
			} catch (CoreException e) {
				logger.fatal("Unable to update created projects: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
