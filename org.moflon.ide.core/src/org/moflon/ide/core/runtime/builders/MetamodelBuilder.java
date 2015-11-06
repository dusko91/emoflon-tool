package org.moflon.ide.core.runtime.builders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.moflon.codegen.ErrorReporter;
import org.moflon.codegen.eclipse.ValidationStatus;
import org.moflon.core.mocatomoflon.Exporter;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.eclipse.resource.SDMEnhancedEcoreResource;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.properties.MetamodelProjectUtil;
import org.moflon.ide.core.properties.MocaTreeEAPropertiesReader;
import org.moflon.ide.core.runtime.ResourceFillingMocaToMoflonTransformation;
import org.moflon.ide.core.runtime.builders.hooks.PostMetamodelBuilderHook;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon.util.plugins.manifest.PluginURIToResourceURIRemapper;

import ValidationResult.ErrorMessage;

/**
 * A metamodel builder that produces plugin projects
 */
public class MetamodelBuilder extends AbstractBuilder
{
   public static final Logger logger = Logger.getLogger(MetamodelBuilder.class);

   public static final String BUILDER_ID = "org.moflon.ide.core.runtime.builders.MetamodelBuilder";

   private MocaTreeEAPropertiesReader mocaTreeReader = new MocaTreeEAPropertiesReader();

   @Override
   protected void cleanResource(final IProgressMonitor monitor) throws CoreException
   {
      MetamodelProjectUtil.cleanTempFolder(getProject(), monitor);
   }

   @Override
   protected boolean processResource(final IProgressMonitor monitor) throws CoreException
   {
      final MultiStatus mocaToMoflonStatus = new MultiStatus(CoreActivator.getModuleID(), 0, BUILDER_ID + " failed", null);
      try
      {
         logger.debug("Start processing .temp folder");
         monitor.beginTask(getProgressBarMessage(), 140);

         if (WorkspaceHelper.getExportedMocaTree(this.getProject()).exists())
         {
            final SubProgressMonitor exporterSubMonitor = WorkspaceHelper.createSubMonitor(monitor, 100);
            Exporter exporter = null;
            try
            {
               final Map<String, MetamodelProperties> properties = readProjectProperties();
               exporterSubMonitor.beginTask("Running MOCA-to-eMoflon transformation", properties.keySet().size());

               exporter = new ResourceFillingMocaToMoflonTransformation(mocaTreeReader.getResourceSet(), properties, exporterSubMonitor);
               exporter.mocaToEcore(mocaTreeReader.getMocaTree());
               for (final ErrorMessage message : exporter.getMocaToMoflonReport().getErrorMessages())
               {
                  mocaToMoflonStatus.add(ValidationStatus.createValidationStatus(message));
               }
               if (exporter.getEpackages().isEmpty())
                  throw new CoreException(new Status(IStatus.ERROR, CoreActivator.getModuleID(), "Unable to transform exported files to ecore models."));
            } finally
            {
               exporterSubMonitor.done();
            }

            PluginURIToResourceURIRemapper.createPluginToResourceMap(mocaTreeReader.getResourceSet());
            monitor.worked(20);

            saveGeneratedMetamodels();
            monitor.worked(20);

            callPostBuildHooks(mocaToMoflonStatus, mocaTreeReader, exporter);
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

      CoreActivator.getDefault().setDirty(this.getProject(), false);

      return mocaToMoflonStatus.isOK();
   }

   /**
    * This method delegates to the registered extensions of the "Post-MetamodelBuilder" extension points
    */
   private void callPostBuildHooks(final IStatus mocaToMoflonStatus, final MocaTreeEAPropertiesReader mocaTreeReader, final Exporter exporter)
   {
      final IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(PostMetamodelBuilderHook.POST_BUILD_EXTENSION_ID);
      for (final IConfigurationElement extension : extensions)
      {
         try
         {
            PostMetamodelBuilderHook metamodelBuilderHook = (PostMetamodelBuilderHook) extension.createExecutableExtension("class");
            metamodelBuilderHook.run(mocaToMoflonStatus, mocaTreeReader, exporter, getProject());
         } catch (final CoreException e)
         {
            logger.error("Problem during post-build hook: " + e.getMessage());
         }
      }
   }

   @Override
   public boolean visit(final IResource resource) throws CoreException
   {
      if (isExportTree(resource))
      {
         final IProgressMonitor progressMonitorForIncrementalChanges = this.getProgressMonitorForIncrementalChanges();
         processResource(WorkspaceHelper.createSubMonitor(progressMonitorForIncrementalChanges, 100));
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
            try
            {
               eapFile.deleteMarkers(WorkspaceHelper.MOFLON_PROBLEM_MARKER_ID, true, IResource.DEPTH_INFINITE);
            } catch (CoreException e)
            {

               e.printStackTrace();
            }
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
