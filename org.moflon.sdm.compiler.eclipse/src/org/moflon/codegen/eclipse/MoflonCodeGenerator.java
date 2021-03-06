package org.moflon.codegen.eclipse;

import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.JobGroup;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory.Descriptor;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gervarro.eclipse.task.ITask;
import org.moflon.codegen.CodeGenerator;
import org.moflon.codegen.MethodBodyHandler;
import org.moflon.core.propertycontainer.MoflonPropertiesContainer;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.moca.inject.CodeInjector;
import org.moflon.moca.inject.CodeInjectorImpl;
import org.moflon.moca.inject.InjectionManager;
import org.moflon.moca.inject.extractors.CompilerInjectionExtractorImpl;
import org.moflon.moca.inject.extractors.UserInjectionExtractorImpl;

public class MoflonCodeGenerator extends GenericMoflonProcess
{
   private static final Logger logger = Logger.getLogger(MoflonCodeGenerator.class);

   private InjectionManager injectionManager;

   private GenModel genModel;

   public MoflonCodeGenerator(final IFile ecoreFile, final ResourceSet resourceSet)
   {
      super(ecoreFile, resourceSet);
   }

   @Override
   public String getTaskName()
   {
      return "Generating code";
   }

   @Override
   public IStatus processResource(final IProgressMonitor monitor)
   {
      try
      {
         final int totalWork = 5 + 10 + 10 + 15 + 35 + 30 + 5;
         final MoflonPropertiesContainer moflonProperties = getMoflonProperties();
         final SubMonitor subMon = SubMonitor.convert(monitor, "Code generation task for " + moflonProperties.getProjectName(), totalWork);
         final String fullProjectName = getFullProjectName(moflonProperties);
         logger.info("Generating code for: " + fullProjectName);

         long toc = System.nanoTime();

         final Resource resource = getEcoreResource();
         final EPackage ePackage = (EPackage) resource.getContents().get(0);

         // (1) Instantiate code generation engine
         final String engineID = CodeGeneratorPlugin.getMethodBodyHandler(getMoflonProperties());
         final MethodBodyHandler methodBodyHandler = (MethodBodyHandler) Platform.getAdapterManager().loadAdapter(this, engineID);
         subMon.worked(5);
         if (methodBodyHandler == null)
         {
            return new Status(IStatus.ERROR, CodeGeneratorPlugin.getModuleID(), "Unknown method body handler: " + engineID + ". Code generation aborted.");
         }
         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }

         // (2.1) Validate SDMs
         final ITask validator = methodBodyHandler.createValidator(ePackage);
         StatusHolder validationStatusHolder = new StatusHolder();
         final WorkspaceJob validationJob = new WorkspaceJob(engineID) {
            @Override
            public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException
            {
               final SubMonitor subMon = SubMonitor.convert(monitor, "Validation job", 100);
               validationStatusHolder.status = validator.run(subMon.split(100));
               return Status.OK_STATUS;
            }
         };
         JobGroup jobGroup = new JobGroup("Validation job group", 1, 1);
         validationJob.setJobGroup(jobGroup);
         validationJob.schedule();
         //         final IStatus validatorStatus = validationJob.runInWorkspace(subMon.split(10));
         int timeoutForValidationTaskInMillis = getPreferencesStorage().getValidationTimeout();
         jobGroup.join(timeoutForValidationTaskInMillis, subMon.split(10));

         if (validationJob.getResult() == null)
         {
            //TODO@rkluge: This is a really ugly hack that should be removed as soon as a more elegant solution is available
            //validationJob.getThread().stop();
            throw new OperationCanceledException("Validation took longer than " + (timeoutForValidationTaskInMillis / 1000)
                  + "s. This could(!) mean that some of your patterns have no valid search plan. You may increase the timeout value using the eMoflon property page");
         }

         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }

         final IStatus validationStatus = validationStatusHolder.status;
         if (validationStatus.matches(IStatus.ERROR))
         {
            return validationStatus;
         }

         // (2.2) Weave MOSL-GT control flow
         final IStatus mgtLoadStatus = this.loadMgtFiles();
         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }

         if (mgtLoadStatus.matches(IStatus.ERROR))
         {
            return mgtLoadStatus;
         }

         final ITask weaver = methodBodyHandler.createControlFlowWeaver(ePackage);
         final IStatus weaverStatus = weaver.run(subMon.split(10));

         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }

         if (weaverStatus.matches(IStatus.ERROR))
         {
            return weaverStatus;
         }

         // (3) Build or load GenModel
         final MonitoredGenModelBuilder genModelBuilderJob = new MonitoredGenModelBuilder(getResourceSet(), getAllResources(), getEcoreFile(), true,
               getMoflonProperties());
         final IStatus genModelBuilderStatus = genModelBuilderJob.run(subMon.split(15));
         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }
         if (genModelBuilderStatus.matches(IStatus.ERROR))
         {
            return genModelBuilderStatus;
         }
         this.genModel = genModelBuilderJob.getGenModel();

         // (4) Load injections
         final IProject project = getEcoreFile().getProject();

         final IStatus injectionStatus = createInjections(project, genModel);
         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }
         if (injectionStatus.matches(IStatus.ERROR))
         {
            return injectionStatus;
         }

         // (5) Process GenModel
         subMon.subTask("Processing SDMs for project " + project.getName());
         final ITask genModelProcessor = methodBodyHandler.createGenModelProcessor(this, resource);
         final IStatus genModelProcessorStatus = genModelProcessor.run(subMon.split(35));
         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }
         if (genModelProcessorStatus.matches(IStatus.ERROR))
         {
            return genModelProcessorStatus;
         }

         // (6) Generate code
         subMon.subTask("Generating code for project " + project.getName());
         final Descriptor codeGenerationEngine = methodBodyHandler.createCodeGenerationEngine(this, resource);
         final CodeGenerator codeGenerator = new CodeGenerator(codeGenerationEngine);
         final IStatus codeGenerationStatus = codeGenerator.generateCode(genModel, new BasicMonitor.EclipseSubProgress(subMon, 30));
         if (subMon.isCanceled())
         {
            return Status.CANCEL_STATUS;
         }
         if (codeGenerationStatus.matches(IStatus.ERROR))
         {
            return codeGenerationStatus;
         }
         subMon.worked(5);

         long tic = System.nanoTime();

         logger.info(String.format(Locale.US, "Completed in %.3fs", (tic - toc) / 1e9));

         final boolean everythingOK = validationStatus.isOK() && injectionStatus.isOK() && weaverStatus.isOK();
         return everythingOK ? new Status(IStatus.OK, CodeGeneratorPlugin.getModuleID(), "Code generation succeeded")
               : new MultiStatus(CodeGeneratorPlugin.getModuleID(), validationStatus.getCode(),
                     new IStatus[] { validationStatus, weaverStatus, injectionStatus }, "Code generation warnings/errors", null);
      } catch (final Exception e)
      {
         logger.debug(WorkspaceHelper.printStacktraceToString(e));
         return new Status(IStatus.ERROR, CodeGeneratorPlugin.getModuleID(), IStatus.ERROR,
               e.getClass().getName() + " occurred during eMoflon code generation. Message: '" + e.getMessage() + "'. (Stacktrace is logged with level debug)",
               e);
      }
   }

   /**
    * This routine identifies and loads all .mgt files in the current project.
    * 
    * For each .mgt file, an appropriate resource is created in this generator's resource set ({@link #getResourceSet()}
    */
   private IStatus loadMgtFiles() throws CoreException
   {
      getProject().accept(new IResourceVisitor() {

         @Override
         public boolean visit(IResource resource) throws CoreException
         {
            if (resource.getName().equals("bin"))
               return false;

            if (isMOSLGTFile(resource))
            {
               final Resource schemaResource = (Resource) getResourceSet()
                     .createResource(URI.createPlatformResourceURI(resource.getAdapter(IFile.class).getFullPath().toString(), false));
               try
               {
                  schemaResource.load(null);
               } catch (final IOException e)
               {
                  throw new CoreException(
                        new Status(IStatus.ERROR, WorkspaceHelper.getPluginId(getClass()), "Problems while loading MOSL-GT specification", e));
               }
            }
            return true;
         }

         private boolean isMOSLGTFile(IResource resource)
         {
            final IFile file = resource.getAdapter(IFile.class);
            return resource != null && resource.exists() && file != null && WorkspaceHelper.MOSL_GT_EXTENSION.equals(file.getFileExtension());
         }

      });
      EcoreUtil.resolveAll(this.getResourceSet());

      return Status.OK_STATUS;
   }

   public final GenModel getGenModel()
   {
      return genModel;
   }

   public final InjectionManager getInjectorManager()
   {
      return injectionManager;
   }

   protected String getFullProjectName(final MoflonPropertiesContainer moflonProperties)
   {
      final String metaModelProjectName = moflonProperties.getMetaModelProject().getMetaModelProjectName();
      final String fullProjectName;
      if ("NO_META_MODEL_PROJECT_NAME_SET_YET".equals(metaModelProjectName))
      {
         fullProjectName = moflonProperties.getProjectName();
      } else
      {
         fullProjectName = metaModelProjectName + "::" + moflonProperties.getProjectName();
      }
      return fullProjectName;
   }

   /**
    * Loads the injections from the /injection folder
    */
   private IStatus createInjections(final IProject project, final GenModel genModel) throws CoreException
   {
      IFolder injectionFolder = WorkspaceHelper.addFolder(project, WorkspaceHelper.INJECTION_FOLDER, new NullProgressMonitor());
      CodeInjector injector = new CodeInjectorImpl(project.getLocation().toOSString());

      UserInjectionExtractorImpl injectionExtractor = new UserInjectionExtractorImpl(injectionFolder.getLocation().toString(), genModel);
      CompilerInjectionExtractorImpl compilerInjectionExtractor = new CompilerInjectionExtractorImpl(project, genModel);

      injectionManager = new InjectionManager(injectionExtractor, compilerInjectionExtractor, injector);
      return injectionManager.extractInjections();
   }

   private static class StatusHolder
   {
      IStatus status;
   }
}
