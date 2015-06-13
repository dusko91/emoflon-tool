package org.moflon.ide.core.runtime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.moflon.Activator;
import org.moflon.backend.ecore2fujaba.Ecore2Fujaba;
import org.moflon.backend.ecore2fujaba.MethodVisitor;
import org.moflon.ide.core.admin.MoflonProperties;
import org.moflon.tracing.sdm.SDMTraceContext;
import org.moflon.tracing.sdm.SDMTraceUtil;
import org.moflon.tracing.sdm.StackTraceWrapper;

import de.fujaba.preferences.StandaloneProjectPreferenceStoreBuilder;
import de.fujaba.preferences.WorkspacePreferenceStore;
import de.uni_kassel.fujaba.codegen.CodeGenPlugin;
import de.uni_kassel.fujaba.codegen.Utility;
import de.uni_kassel.fujaba.codegen.engine.CodeGeneration;
import de.uni_kassel.fujaba.codegen.engine.CodeWritingEngine;
import de.uni_kassel.fujaba.codegen.engine.Engine;
import de.uni_kassel.fujaba.codegen.engine.JavaTokenMutatorTemplateEngine;
import de.uni_paderborn.fujaba.app.SimpleFujabaPersistencySupport;
import de.uni_paderborn.fujaba.metamodel.common.FCodeStyle;
import de.uni_paderborn.fujaba.metamodel.common.FElement;
import de.uni_paderborn.fujaba.metamodel.common.FProject;
import de.uni_paderborn.fujaba.metamodel.factories.FFactory;
import de.uni_paderborn.fujaba.preferences.FujabaCorePreferenceKeys;
import de.uni_paderborn.fujaba.preferences.FujabaPreferencesManager;
import de.uni_paderborn.fujaba.preferences.ProjectPreferenceStore;
import de.uni_paderborn.fujaba.project.ProjectLoader;
import de.uni_paderborn.fujaba.project.ProjectManager;
import de.uni_paderborn.fujaba.project.ProjectWriter;
import de.uni_paderborn.fujaba.uml.common.UMLProjectFactory;
import de.uni_paderborn.fujaba.uml.structure.UMLMethod;
import de.uni_paderborn.fujaba.versioning.VersioningLoader;
import de.uni_paderborn.fujaba.versioning.VersioningWriter;

/**
 * Used to initialize and control the SDM code generator. Is responsible for initializing and starting CodeGen2, and
 * coordinating all necessary steps. Delegates the Ecore->Fujaba transformation to {@link Ecore2Fujaba}.
 * 
 * Is accessed from the EMF templates via {@link SDMCodegeneratorHelper#retrieveCodeForOperation(EOperation)} to supply
 * the implementation code for operations (modeled via SDMs).
 * 
 * @author anjorin
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
public class SDMCodegeneratorHelper
{
   private static final Logger logger = Logger.getLogger(SDMCodegeneratorHelper.class);

   private static final String DEBUG_FILE_EXTENSION = "ctr";
   private static final String DEFAULT_NAME_FOR_TEMP_FUJABA_PROJECT = "generatedFujabaProject";
   
   private boolean debugMode;
   
   private boolean genTraceInformation;

   private IFolder debugFolder;

   private static HashMap<String, String> importMappings = new HashMap<String, String>();
   
   private static HashMap<String, String> generatedCode;

   private String generatedCodeRaw;

   // Used to keep mapping of eOperations to UMLMethod for assigning imports from CodeGen2
   private static HashMap<EOperation,UMLMethod> eOperationToUMLMethod;

   // Used to create, delete and manipulate Fujaba projects
   private ProjectManager projectManager;

   // The Fujaba project that is populated and fed to CodeGen2
   private FProject project;

   private FCodeStyle emfCodeStyle;

   // Helper for transformation
   private Ecore2Fujaba ecore2fujaba;
   
   // Common ResourceSet
   private ResourceSet resourceSet;

   private Resource ecoreResource;


   public SDMCodegeneratorHelper(IResource ecoreFile, boolean debugMode, boolean genTraceInstrumentation)
   {
      // Set data members
      this.debugMode = debugMode;
      this.genTraceInformation = genTraceInstrumentation;
      debugFolder = ecoreFile.getProject().getFolder("/debug");
      generatedCode = new HashMap<String, String>();
      
      // Get import mappings from user properties
      importMappings = new MoflonProperties(ecoreFile.getProject(), new NullProgressMonitor()).getImportMappings();

      /* Initialize fujaba and codegen related stuff */
      logger.debug("Initializing fujaba and codegen related stuff");

      // Get ProjectManager
      projectManager = ProjectManager.get();

      // Add versioning support
      VersioningLoader versioningLoader = new VersioningLoader(false);
      VersioningWriter versioningWriter = new VersioningWriter(false);
      projectManager.registerProjectLoader(DEBUG_FILE_EXTENSION, versioningLoader);
      projectManager.registerProjectWriter(DEBUG_FILE_EXTENSION, versioningWriter);

      // Initialize core preferences
      FujabaPreferencesManager.setPreferenceStore(FujabaPreferencesManager.FUJABA_CORE_PREFERENCES, WorkspacePreferenceStore.getInstance());
      FujabaPreferencesManager.setProjectPreferenceStoreBuilder(new StandaloneProjectPreferenceStoreBuilder());

      // Add persistency support
      ProjectLoader.setPersistencySupport(new SimpleFujabaPersistencySupport());

      // Initialize codegen
      new CodeGenPlugin().initialize();
      logger.debug("Started codegen plugin");

      // Create empty fujaba project
      UMLProjectFactory projectFactory = new UMLProjectFactory();
      project = projectFactory.create(null, true);
      project.setName(DEFAULT_NAME_FOR_TEMP_FUJABA_PROJECT);
      logger.debug("Created temporary fujaba project");
      
      // set some project specific properties
      initFujabaAndCodegenPreferences();
      logger.debug("Initialized Fujaba/CodeGen Properties");

      // Setup emf code style
      FFactory<FCodeStyle> codeStyleFactory = project.getFromFactories(FCodeStyle.class);
      emfCodeStyle = codeStyleFactory.getFromProducts("emf");

      if (emfCodeStyle == null)
      {
         emfCodeStyle = codeStyleFactory.create(true);
         emfCodeStyle.setName("emf");
      }

      project.setCodeStyle(emfCodeStyle);

      /* Set up ecore/emf related stuff */
      logger.debug("Set up ecore/emf related stuff");
      
      // Create a ResourceSet for resources (ecore and SDMs)
      resourceSet = new ResourceSetImpl();
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new EcoreResourceFactoryImpl());
      
      // Load model and add uri mapping to package for current project
      URI fileURI = URI.createPlatformResourceURI(ecoreFile.getFullPath().toString(), true);
      
      EMFCodegeneratorHelper.loadBuildPathDependencies(resourceSet, ecoreFile.getProject());

      ecoreResource = EMFCodegeneratorHelper.loadModel(fileURI, resourceSet).eResource();
      
      // Initialize helpers
      ecore2fujaba = new Ecore2Fujaba(project, emfCodeStyle, ecoreResource);
      
      logger.debug("Completed initialization");
   }

   private void initFujabaAndCodegenPreferences()
   {
      // init global preferences  
      // eventually SystemPreferenceStore gets polled - it uses properties stored in "System" => deposit settings there
      if (System.getProperty(FujabaCorePreferenceKeys.PROJECT_ID) == null) {
         System.setProperty(FujabaCorePreferenceKeys.PROJECT_ID, "temp"); // this is only cosmetic to prevent (log4j) WARN-messages from occurring
      }
      final String tempPropKey = FujabaCorePreferenceKeys.PROPERTY_PREFIX + DEFAULT_NAME_FOR_TEMP_FUJABA_PROJECT;
      if (System.getProperty(tempPropKey) == null) {
         System.setProperty(tempPropKey, ""); // this is only cosmetic to prevent (log4j) WARN-messages from occurring
      }
      if (System.getProperty("de.fujaba.general.FileHistory") == null) {
         System.setProperty("de.fujaba.general.FileHistory", ""); // this is only cosmetic to prevent (log4j) WARN-messages from occurring
      }

      // init settings specific for the temporal Fujaba project  
      // setting the name of the project to the ProjectPreferenceStore
      ProjectPreferenceStore projectPreferenceStore = FujabaPreferencesManager.getProjectPreferenceStore(project);
      projectPreferenceStore.setValue(FujabaCorePreferenceKeys.PROJECT_ID, DEFAULT_NAME_FOR_TEMP_FUJABA_PROJECT);
   }

   /**
    * Generates code for SDMs and stores the result in a map. This map is later on accessed from the EMF templates
    * during class diagram code generation.
    * 
    * @throws Exception
    */
   public ResourceSet generateCode() throws Exception
   {
      logger.debug("Starting to generate SDM code");
      
      /* 1. Build up fujaba project afresh */
      ecore2fujaba.createFProject();

      if (debugMode)
      {
         logger.debug("Saving generated fujaba project");
         try
         {
            // Save fujaba project
            ProjectWriter writer = projectManager.getProjectWriter(DEBUG_FILE_EXTENSION);

            if (!debugFolder.exists())
               debugFolder.create(true, false, null);

            IFile ctrFile = debugFolder.getFile("generatedFujabaProject.ctr");

            if (!ctrFile.exists())
               ctrFile.create(new ByteArrayInputStream("".getBytes()), true, null);

            writer.saveProject(project, new File(ctrFile.getLocationURI()), null);
            debugFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
            
            // Save SDMs
            for (String sdmFilename : ecore2fujaba.getSDMs().keySet())
            {
               IFile sdmFile = debugFolder.getFile(sdmFilename);
               ByteArrayInputStream sdmContents = null;
               try {
            	   sdmContents = new ByteArrayInputStream(ecore2fujaba.getSDMs().get(sdmFilename).toString().getBytes());

	               if (sdmFile.exists())
	                  sdmFile.setContents(sdmContents, IResource.FORCE, null);
	               else
	                  sdmFile.create(sdmContents, IResource.FORCE, null);
               } catch (Exception e) {
            	   throw e;
               } finally {
	               sdmContents.close();
               }
            }
            
         } catch (Exception e) {
            logger.error("Unable to save fujaba project for debugging purposes.");
            e.printStackTrace();
         }
      }

      
      /* 2. Prepare code generation */

      // Get template directory
      URL templateDir = Activator.getPathRelToPlugIn("/templates", "de.uni_kassel.fujaba.codegen");

      // Correct template directory for all code writers in responsible engine
      Engine engine = CodeGeneration.get().getEngineFor(project, "java");

      JavaTokenMutatorTemplateEngine javaTemplateEngine = (JavaTokenMutatorTemplateEngine) engine;

      for (CodeWritingEngine cw : javaTemplateEngine.getCodeWriter())
      {
         cw.getTemplateLoader().getLoader().addToContext(templateDir);
         cw.setGlobalTemplate(cw.getTemplateLoader().loadTemplate("java/global.vm"));
         cw.addToDirs(templateDir);
      }
      
      // activate tracing, if required
      if (genTraceInformation) {
     	 Utility.get().enableSdmTracing();
      } else if (Utility.get().isSdmTracingActivated()) {
    	  Utility.get().disableSdmTracing();
      }

      /* 3. Generate code and save debug info */
   	  generateCodeForSDMs(ecore2fujaba.geteOperationToUMLMethod());
   	  logger.debug("Generated SDM code");
      
      if (debugMode)
      {
         logger.debug("Saving generated code");
         try
         {
            // Save generated code
            IFile sdmDebug = debugFolder.getFile("sdmCodeFromCodeGen2");
            ByteArrayInputStream contents = new ByteArrayInputStream(generatedCode.toString().getBytes());

            IFile sdmDebugRaw = debugFolder.getFile("sdmCodeFromCodeGen2Raw");
            ByteArrayInputStream contentsRaw = new ByteArrayInputStream(generatedCodeRaw.toString().getBytes());

            if (sdmDebug.exists())
               sdmDebug.setContents(contents, IResource.FORCE, null);
            else
               sdmDebug.create(contents, IResource.FORCE, null);

            if (sdmDebugRaw.exists())
               sdmDebugRaw.setContents(contentsRaw, IResource.FORCE, null);
            else
               sdmDebugRaw.create(contentsRaw, IResource.FORCE, null);

            contents.close();
            contentsRaw.close();
            
            logger.debug("Completed saving generated fujaba project");
         } catch (Exception e)
         {
            logger.error("Unable to save generated code for debugging purposes.");
            e.printStackTrace();
         }
      }
      
      return resourceSet;
   }

   public void release()
   {
      projectManager.closeProject(project, true);
      logger.info("Closed Fujaba Project");
   }

   /**
    * Used to retrieve the code that was generated from the SDM corresponding to the given eOperation. This method is
    * called from the EMF code generation templates.
    * 
    * @param eOperation
    *           The method for which implementation code (generated from corresponding SDM) is to be retrieved.
    * @return
    */
   public static String retrieveCodeForOperation(EOperation eOperation)
   {
      return generatedCode.get(MethodVisitor.signatureFor(eOperation));
   }
   
   /**
    * Getter for the Map containing generated code for every EOperation
    * @return Unmodifiable Map filled with code for all EOperations
    */
   public static Map<String,String> getCodeHashMap()
   {
	   return Collections.unmodifiableMap(generatedCode);
	   // return (Map<String,String>)generatedCode.clone();
   }

   /**
    * Used by EMF codegenerator to collect imports for ClassImpl containing eOperation
    * 
    * @param genModel
    *           Imports are added as Strings to the genModel via genModel.addImport(...)
    * @param eOperation Current operation used to retrieve corresponding imports
    */
   public static void retrieveImports(GenModel genModel, EOperation eOperation)
   {
      // Go from eOperation to UMLMethod
      UMLMethod method = eOperationToUMLMethod.get(eOperation);

      // Handle imports for TGG rules
      if (eOperation.getEContainingClass().getEAnnotation("TGGRule")!=null) {
         addImport(genModel, "TGGLanguage.csp.*");
         addImport(genModel, "csp.constraints.*");
      }
      
      // Add import for the trace util
      if (Utility.get().isSdmTracingActivated()) {
    	  addImport(genModel, SDMTraceUtil.class.getCanonicalName());
    	  addImport(genModel, SDMTraceContext.class.getCanonicalName());
    	  addImport(genModel, StackTraceWrapper.class.getCanonicalName());
    	  addImport(genModel, "java.lang.reflect.Method");
      }
      
      // Get imports from Utility
      if (Utility.methodToImports.containsKey(method))
         for (String anImport : Utility.methodToImports.get(method))
         {
            // Only add legal imports and perform transformations if necessary
            addImport(genModel, anImport);
         }
   }

   /**
    * CodeGen2 sometimes has problems resolving template variables and these $... imports have to be removed. Imports
    * that start with "de.uni_kassel..." are also unecessary.
    * 
    * @param anImport
    * @return true if import is legal
    */
   private static void addImport(GenModel genModel, String anImport)
   {
      if(!(anImport.startsWith("${") 
         || anImport.startsWith("de.uni_kassel.features"))){
         
         // Import is legal so perform transformations if necessary
         if(anImport.startsWith("ecore")){
            anImport = anImport.replaceFirst("ecore", "org.eclipse.emf.ecore");
         }
         
         // Correct with mappings from user
         for (String old : importMappings.keySet())
         {
            if(anImport.startsWith(old)){
               anImport = anImport.replaceFirst(old, importMappings.get(old));
            }
         }
         
         genModel.addImport(anImport);
      }
   }

   private void generateCodeForSDMs(HashMap<EOperation, UMLMethod> eOperationToUMLMethod) throws Exception
   {
      // 0. Initialize collection for later (bookkeeping used by EMFCodegenerator)
      SDMCodegeneratorHelper.eOperationToUMLMethod = eOperationToUMLMethod;
      
      // 1. Pass current project to CodeGen2 for codegeneration
      String codeForProject = generateCodeForSDM(project);
      generatedCodeRaw = codeForProject;

      MethodVisitor methodVisitor = new MethodVisitor(codeForProject);
      for (EOperation eOperation : eOperationToUMLMethod.keySet())
      {
         // 2. Extract only relevant code for method
         String codeForMethod = methodVisitor.extractCodeForMethod(eOperation);

         // 3. Save generated code to be retrieved by EMF codegenerator
         generatedCode.put(MethodVisitor.signatureFor(eOperation), codeForMethod);
      }
   }

   private String generateCodeForSDM(FElement element) throws MalformedURLException
   {
      // Use codegen to generate code for project
      return de.fujaba.codegen.CodeGeneration.get().generateFElement(element).toString();
   }

   public Resource getEcoreResource()
   {
      return ecoreResource;
   }
}
