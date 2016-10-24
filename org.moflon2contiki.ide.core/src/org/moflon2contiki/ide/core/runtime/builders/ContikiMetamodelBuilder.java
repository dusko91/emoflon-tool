package org.moflon2contiki.ide.core.runtime.builders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.moflon.codegen.eclipse.ValidationStatus;
import org.moflon.core.mocatomoflon.Exporter;
import org.moflon.core.utilities.ErrorReporter;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.eclipse.resource.SDMEnhancedEcoreResource;
import org.moflon.ide.core.properties.MetamodelProjectUtil;
import org.moflon.ide.core.properties.MocaTreeEAPropertiesReader;
import org.moflon.ide.core.runtime.builders.AbstractBuilder;
import org.moflon.ide.core.runtime.builders.MetamodelBuilder;
import org.moflon.sdm.compiler.democles.validation.result.ErrorMessage;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon.util.plugins.manifest.PluginURIToResourceURIRemapper;
import org.moflon2contiki.ide.core.ContikiCoreActivator;
import org.moflon2contiki.ide.core.runtime.ResourceFillingMocaToMoflon2ContikiTransformation;
import org.moflon2contiki.ide.core.utilities.Moflon2ContikiWorkspaceHelper;
import org.eclipse.emf.ecore.resource.Resource;


/**
 * Builder for Projects with {@link ContikiMetamodelNature}. Similar to {@link MetamodelBuilder}. Triggers the {@link ResourceFillingMocaToMoflon2ContikiTransformation}.
 * @author David Giessing
 *
 */
public class ContikiMetamodelBuilder extends AbstractBuilder{
	
	public static final Logger logger = Logger.getLogger(ContikiMetamodelBuilder.class);
	
	public static final String BUILDER_ID = "org.moflon2contiki.ide.core.runtime.builders.ContikiMetamodelBuilder";
	
	private MocaTreeEAPropertiesReader mocaTreeReader = new MocaTreeEAPropertiesReader();

	   @Override
	   protected void cleanResource(final IProgressMonitor monitor) throws CoreException
	   {
		  //TODO: check if change needed
	      MetamodelProjectUtil.cleanTempFolder(getProject(), monitor);
	   }

	   @Override
	   protected boolean processResource(final IProgressMonitor monitor) throws CoreException
	   {
	      final MultiStatus mocaToMoflonStatus = new MultiStatus(ContikiCoreActivator.getModuleID(), 0, BUILDER_ID + " failed", null);
	      try
	      {
	         logger.debug("Start processing .temp folder");
	         monitor.beginTask(getProgressBarMessage(), 140);

	         if (Moflon2ContikiWorkspaceHelper.getExportedMocaTree(this.getProject()).exists())
	         {
	            final IProgressMonitor exporterSubMonitor = Moflon2ContikiWorkspaceHelper.createSubMonitor(monitor, 100);
	            Exporter exporter = null;
	            try
	            {
	               final Map<String, MetamodelProperties> properties = readProjectProperties();
	               exporterSubMonitor.beginTask("Running MOCA-to-eMoflon transformation", properties.keySet().size());

	               exporter = new ResourceFillingMocaToMoflon2ContikiTransformation(mocaTreeReader.getResourceSet(), properties, exporterSubMonitor);
	               exporter.mocaToEcore(mocaTreeReader.getMocaTree());
	               for (final ErrorMessage message : exporter.getMocaToMoflonReport().getErrorMessages())
	               {
	                  mocaToMoflonStatus.add(ValidationStatus.createValidationStatus(message));
	               }
	               if (exporter.getEpackages().isEmpty())
	                  throw new CoreException(new Status(IStatus.ERROR, ContikiCoreActivator.getModuleID(), "Unable to transform exported files to ecore models."));
	            } finally
	            {
	               exporterSubMonitor.done();
	            }
	            PluginURIToResourceURIRemapper.createPluginToResourceMap(mocaTreeReader.getResourceSet());
	            monitor.worked(20);

	            saveGeneratedMetamodels();
	            monitor.worked(20);
	         }
	      } catch (Exception e)
	      {
	         logger.warn("Unable to update created projects: " + e.getMessage() == null ? e.toString() : e.getMessage());
	         e.printStackTrace();
	      } finally
	      {
	         monitor.done();
	      }

	      handleErrorsInEclipse(mocaToMoflonStatus);

	      ContikiCoreActivator.getDefault().setDirty(this.getProject(), false);

	      return mocaToMoflonStatus.isOK();
	   }

	   @Override
	   public boolean visit(final IResource resource) throws CoreException
	   {
	      if (isExportTree(resource))
	      {
	         final IProgressMonitor progressMonitorForIncrementalChanges = this.getProgressMonitorForIncrementalChanges();
	         processResource(Moflon2ContikiWorkspaceHelper.createSubMonitor(progressMonitorForIncrementalChanges, 100));
	      }

	      return false;
	   }

	   @Override
	   public boolean visit(final IResourceDelta delta) throws CoreException
	   {
	      // Get changes and call visit on .temp folder
	      IResourceDelta[] changes = delta.getAffectedChildren();
	      for (final IResourceDelta change : changes)
	      {
	         IResource resource = change.getResource();
	         if (isExportTree(resource) && wasChangedOrAdded(change))
	            return visit(resource);
	         else
	            visit(change);
	      }

	      return false;
	   }

	   private void handleErrorsInEclipse(final IStatus validationStatus)
	   {
	      IProject metamodelProject = getProject();
	      IFile eapFile = metamodelProject.getFile(metamodelProject.getName() + ".eap");

	      if (eapFile.exists())
	      {
	         ErrorReporter eclipseErrorReporter = (ErrorReporter) Platform.getAdapterManager().loadAdapter(eapFile,
	               "org.moflon.compiler.sdm.democles.eclipse.EclipseErrorReporter");
	         if (eclipseErrorReporter != null)
	         {
	            if (!validationStatus.isOK())
	            {

	               eclipseErrorReporter.report(validationStatus);
	            }
	         }
	      }
	   }

	   private void saveGeneratedMetamodels()
	   {
	      // Prepare save options
	      Map<Object, Object> saveOnlyIfChangedOption = new HashMap<>();
	      saveOnlyIfChangedOption.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
	      saveOnlyIfChangedOption.put(SDMEnhancedEcoreResource.SAVE_GENERATED_PACKAGE_CROSSREF_URIS, true);
	      // Persist resources (metamodels, tgg files and moflon.properties files)
	      mocaTreeReader.getResourceSet().getResources().remove(mocaTreeReader.getMocaTreeResource());
	      for (Resource resource : mocaTreeReader.getResourceSet().getResources())
	      {
	         try
	         {
	            resource.save(saveOnlyIfChangedOption);
	         } catch (IOException e)
	         {
	            logger.debug(resource.getURI() + ": " + e.getMessage());
	         }
	      }
	   }

	   protected Map<String, MetamodelProperties> readProjectProperties() throws CoreException
	   {
	      return mocaTreeReader.getProperties(getProject());
	   }

	   private boolean isExportTree(final IResource resource)
	   {
	      return resource.getName().endsWith(WorkspaceHelper.MOCA_XMI_FILE_EXTENSION) && resource.getParent().getName().equals(WorkspaceHelper.TEMP_FOLDER);
	   }

	   private boolean wasChangedOrAdded(final IResourceDelta change)
	   {
	      return change.getKind() == IResourceDelta.CHANGED || change.getKind() == IResourceDelta.ADDED;
	   }

}
