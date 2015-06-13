package org.moflon.ide.core.runtime.builders;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.moflon.codegen.MethodBodyHandler;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.codegen.eclipse.MoflonCodeGenerator;
import org.moflon.codegen.eclipse.MonitoredMetamodelLoader;
import org.moflon.dependency.PackageRemappingDependency;
import org.moflon.eclipse.resource.SDMEnhancedEcoreResource;
import org.moflon.ide.core.CoreActivator;
import org.moflon.moca.CSPCodeAdapter;
import org.moflon.moca.MocaUtil;
import org.moflon.moca.tggUserDefinedConstraint.unparser.TGGUserDefinedConstraintUnparserAdapter;
import org.moflon.moca.tie.RunIntegrationGeneratorBatch;
import org.moflon.moca.tie.RunIntegrationGeneratorSync;
import org.moflon.moca.tie.RunModelGenerationGenerator;
import org.moflon.properties.MoflonPropertiesContainerHelper;
import org.moflon.util.MoflonUtil;
import org.moflon.util.WorkspaceHelper;
import org.moflon.util.eMoflonEMFUtil;
import org.moflon.util.eMoflonSDMUtil;

import MoflonPropertyContainer.BuildFilter;
import MoflonPropertyContainer.BuildFilterRule;
import MoflonPropertyContainer.BuildMode;
import MoflonPropertyContainer.MoflonPropertiesContainer;
import TGGLanguage.TGGRule;
import TGGLanguage.TripleGraphGrammar;
import TGGLanguage.algorithm.ApplicationTypes;
import TGGLanguage.analysis.StaticAnalysis;
import TGGLanguage.compiler.CompilerFactory;
import TGGLanguage.compiler.TGGCompiler;
import TGGLanguage.csp.CSP;
import TGGLanguage.modelgenerator.Precompiler;
import TGGLanguage.precompiler.CyclicContextMessage;
import TGGLanguage.precompiler.PrecompileLog;
import TGGLanguage.precompiler.PrecompileMessage;
import TGGLanguage.precompiler.PrecompilerFactory;
import TGGLanguage.precompiler.RuleProcessingMessage;
import TGGLanguage.precompiler.TGGPrecompiler;

public class IntegrationBuilder extends RepositoryBuilder {
   private static final Logger logger = Logger.getLogger(IntegrationBuilder.class);
   
   public static final String SUFFIX_SMA = ".sma.xmi";

   private Collection<MocaTree.Node> userDefinedConstraints;

   private HashMap<IFile, String> cachedGeneratedCode = new HashMap<>();

	public boolean handleResourceChange(final IResource resource, final boolean added) {
		if (added && resource.getType() == IResource.FILE) {
			IFile ecoreFile = (IFile) resource;
//			processTGG(new NullProgressMonitor());
//			processSDM(new NullProgressMonitor());
		}
		return false;
	}

	private void processTGG(final IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Building " + getProject().getName(), 20 * WorkspaceHelper.PROGRESS_SCALE);

		final SubProgressMonitor metamodelLoaderMonitor = new SubProgressMonitor(monitor, 10);

		final ResourceSet set = CodeGeneratorPlugin.createDefaultResourceSet();
		final Map<URI, URI> uriMap = set.getURIConverter().getURIMap();
		eMoflonEMFUtil.installCrossReferencers(set);
		final MoflonPropertiesContainer moflonProperties =
				MoflonPropertiesContainerHelper.load(getProject(), new NullProgressMonitor());

		// Load ecore file
		final URI ecoreFileURI = CodeGeneratorPlugin.getDefaultEcoreFileURI(getProject());
		uriMap.put(ecoreFileURI, ecoreFileURI.trimFileExtension().appendFileExtension("pre").appendFileExtension("ecore"));
		final MonitoredMetamodelLoader metamodelLoader =
				new MonitoredMetamodelLoader(set, CodeGeneratorPlugin.getDefaultEcoreFile(getProject()), moflonProperties);
		metamodelLoader.run(metamodelLoaderMonitor);
		uriMap.remove(ecoreFileURI);

		// Load tgg file
		final URI tggFileURI = getTGGFileURI(getProject());
		uriMap.put(tggFileURI, tggFileURI.trimFileExtension().appendFileExtension("pre").appendFileExtension("tgg"));
		PackageRemappingDependency tggDependency = new PackageRemappingDependency(tggFileURI);
		Resource tggResource = tggDependency.getResource(set, true);
		uriMap.remove(tggFileURI);
		
		// Compile TGGs to SDMs
		monitor.subTask(getProject().getName() + ": Translating TGG model to SDMs...");
		TripleGraphGrammar tgg = (TripleGraphGrammar) tggResource.getContents().get(0);
		
		// Create and add precompiler to resourceSet so reverse navigation of links works
		TGGPrecompiler precompiler = PrecompilerFactory.eINSTANCE.createTGGPrecompiler();
		Resource precompilerResource = set.createResource(URI.createPlatformPluginURI("/TGGLanguage/Precompiler.xmi", true));
		precompilerResource.getContents().add(precompiler);

		// Precompile rules
		precompiler.precompileTGG(tgg);
		// print refinement precompiling log
		printPrecompilerLog(precompiler.getRefinementPrecompiler().refineRules(tgg));

		// Persist tgg model after precompilation
		HashMap<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(SDMEnhancedEcoreResource.SAVE_GENERATED_PACKAGE_CROSSREF_URIS, Boolean.valueOf(true));

		Resource genTGGResource = tgg.eResource();
		try
		{
			genTGGResource.save(saveOptions);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		// Parse csp specs
		parseCspSpecs(tgg);

		// Create SDMs from TGG specification
		StaticAnalysis staticAnalysis = null;

		if (moflonProperties.getTGGBuildMode().getBuildMode() == BuildMode.SIMULTANEOUS)
		{

			Precompiler modelgenPrecompiler = TGGLanguage.modelgenerator.ModelgeneratorFactory.eINSTANCE.createPrecompiler();
			modelgenPrecompiler.getUserdefinedConstraints().addAll(userDefinedConstraints);
			modelgenPrecompiler.replaceBuiltInConstraints(tgg);

			parseCspSpecs(tgg);

			TGGLanguage.modelgenerator.Compiler compiler = TGGLanguage.modelgenerator.ModelgeneratorFactory.eINSTANCE.createCompiler();
			compiler.setProperties(moflonProperties);
			compiler.compileModelgenerationSdms(tgg);
		} else
		{
			TGGCompiler compiler = CompilerFactory.eINSTANCE.createTGGCompiler();
			Resource compilerResource = set.createResource(URI.createURI("Compiler"));
			compilerResource.getContents().add(compiler);

			compiler.setProperties(moflonProperties);

			compiler.deriveOperationalRules(tgg, ApplicationTypes.get(moflonProperties.getTGGBuildMode().getBuildMode().getValue()));
			staticAnalysis = compiler.getStaticAnalysis();
		}

		monitor.worked(5 * WorkspaceHelper.PROGRESS_SCALE);

		try
		{
			// Persist results of static analysis
			URI smaFileURI = genTGGResource.getURI().trimFileExtension().appendFileExtension(SUFFIX_SMA);
			//	         PackageRemappingDependency smaFile = new PackageRemappingDependency(smaFileURI);
			//	         Resource smaResource = smaFile.getResource(set, false);
			Resource smaResource = set.createResource(smaFileURI);
			if (staticAnalysis != null)
			{
				smaResource.getContents().add(staticAnalysis);
			}
			smaResource.save(saveOptions);

			// TODO gervarro: ???
			set.getResources().add(genTGGResource);
			Resource ecoreResource = metamodelLoader.getEcoreResource();
			ecoreResource.save(saveOptions);
			monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);
		} catch (IOException e)
		{
			throw new CoreException(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e));
		}

		// Generate user defined constraints
		generateUserDefinedConstraints();

		// Generate stub for batch transformation if necessary
		new RunIntegrationGeneratorBatch(getProject()).doFinish();

		// Generate stub for sync if necessary
		new RunIntegrationGeneratorSync(getProject()).doFinish();

		new RunModelGenerationGenerator(getProject()).doFinish();
	}

	public static final URI getTGGFileURI(IProject project) {
		final String projectName = MoflonUtil.lastCapitalizedSegmentOf(project.getName());
		final URI projectRelativeURI = URI.createURI(WorkspaceHelper.MODEL_FOLDER + "/" + projectName + WorkspaceHelper.TGG_FILE_EXTENSION);
		return projectRelativeURI.resolve(URI.createPlatformResourceURI(project.getName() + "/", true));
	}

	public boolean processSDM(IProgressMonitor monitor) throws CoreException {
		// filtered tgg rules should not be built
		ResourceSet set = CodeGeneratorPlugin.createDefaultResourceSet();
		eMoflonEMFUtil.installCrossReferencers(set);
		final MoflonPropertiesContainer moflonProperties =
				MoflonPropertiesContainerHelper.load(getProject(), new NullProgressMonitor());
		final IFile ecoreFile = CodeGeneratorPlugin.getDefaultEcoreFile(getProject());
		final MonitoredMetamodelLoader metamodelLoader =
				new MonitoredMetamodelLoader(set, ecoreFile, moflonProperties);
		metamodelLoader.run(monitor);
		filterTGGsFromBuildFilterProps(metamodelLoader.getEcoreResource());
		Resource ecoreResource = metamodelLoader.getEcoreResource();
		HashMap<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(SDMEnhancedEcoreResource.SAVE_GENERATED_PACKAGE_CROSSREF_URIS, Boolean.valueOf(true));
		try
		{
			ecoreResource.save(saveOptions);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		final MoflonCodeGenerator codeGenerationTask =
				new MoflonCodeGenerator(ecoreFile, set);

		logger.info("Generating SDM code for: " + getProject());

		final IStatus status = codeGenerationTask.run(new NullProgressMonitor());
		//		if (!status.isOK()) {
		//			throw new CoreException(status);
		//		}

		restoreCachedGeneratedCode();

		return true;
	}
	   
	   /**
	    * Generates the files to implement user defined constraints and fills them with basic content. <br>
	    * An existing file won't be changed even if the definition of the constraint has been changed.
	    * 
	    * @throws CoreException
	    */
	   private void generateUserDefinedConstraints() throws CoreException
	   {
	      NullProgressMonitor monitor = new NullProgressMonitor();
	      TGGUserDefinedConstraintUnparserAdapter unparser = new TGGUserDefinedConstraintUnparserAdapter();

	      if (userDefinedConstraints.size() != 0)
	      {
	         // Create required folder structure
	         WorkspaceHelper.addAllFolders(getProject(), "src/csp/constraints", monitor);

	         for (MocaTree.Node node : userDefinedConstraints)
	         {
	            String content = unparser.unparse(node);
	            String nameInUpperCase = MocaUtil.firstToUpper(node.getName());
	            String path = "src/csp/constraints/" + nameInUpperCase + ".java";

	            // Ignore existing files
	            if (!getProject().getFile(path).exists())
	               WorkspaceHelper.addFile(getProject(), path, content, monitor);
	         }
	      }
	   }

	   private void printPrecompilerLog(final PrecompileLog log)
	   {
	      String errorMessage = "";
	      for (PrecompileMessage error : log.getPrecompileerror())
	      {
	         if (error instanceof CyclicContextMessage)
	         {
	            CyclicContextMessage cycle = (CyclicContextMessage) error;
	            errorMessage += cycle.getMessage() + ": " + error.getRefinementrule() + " cycle found: ";

	            for (TGGRule cyclicRule : cycle.getRefineablerules().getTggrule())
	               errorMessage += cyclicRule.getName() + " ";

	            errorMessage += "\n";
	         }

	         if (error instanceof RuleProcessingMessage)
	         {
	            RuleProcessingMessage rule = (RuleProcessingMessage) error;
	            errorMessage += rule.getMessage() + ": " + error.getRefinementrule() + " basis: " + rule.getBaserule() + " | Error at "
	                  + rule.getTggobjectvariable() + "\n";
	         }
	      }
	      if (!errorMessage.equals(""))
	      {
	         errorMessage = " ------------ PreCompiler Errorlog ------------ \n" + errorMessage + " --------------------------------------";
	         logger.error("TGGLanguage Precompiler (Refinements) - " + errorMessage);
	      }

	      String warningMessage = "";
	      for (PrecompileMessage warning : log.getPrecompilewarning())
	      {
	         if (warning instanceof RuleProcessingMessage)
	         {
	            RuleProcessingMessage rule = (RuleProcessingMessage) warning;
	            warningMessage += rule.getMessage() + ": " + warning.getRefinementrule() + " basis: " + rule.getBaserule() + " | Refining: "
	                  + rule.getTggobjectvariable() + "\n";
	         }
	      }
	      if (!errorMessage.equals(""))
	      {
	         warningMessage = " ------------ PreCompiler Warninglog ------------ \n" + warningMessage + " --------------------------------------";
	         logger.warn("TGGLanguage Precompiler (Refinements) - " + warningMessage);
	      }
	   }

	   private void parseCspSpecs(final TripleGraphGrammar tgg)
	   {
	      userDefinedConstraints = new ArrayList<MocaTree.Node>();
	      for (TGGRule rule : tgg.getTggRule())
	      {
	         String cspSpec = rule.getCspSpec();
	         if (cspSpec != null && cspSpec.trim().length() > 0)
	         {
	            CSPCodeAdapter adapter = new CSPCodeAdapter();
	            CSP csp = adapter.parse(rule);
	            userDefinedConstraints.addAll(adapter.getUserDefinedConstraints());
	            rule.setCsp(csp);
	         }
	      }
	   }

   private void cacheGeneratedCodeForRules(final List<EClassifier> rulesToFilter)
   {
      cachedGeneratedCode.clear();

      List<EClassifier> rulesToDeactivate = new ArrayList<>();
      for (EClassifier rule : rulesToFilter)
      {
         IFile impl = getProject().getFile("gen/" + getProject().getName() + "/Rules/impl/" + rule.getName() + "Impl.java");

         try
         {
            StringWriter writer = new StringWriter();
            IOUtils.copy(impl.getContents(), writer, impl.getCharset());
            cachedGeneratedCode.put(impl, writer.toString());
         } catch (Exception e)
         {
            logger.error("Unable to chache code for: " + rule.getName() + ", deactivating corresponding filter...");
            rulesToDeactivate.add(rule);
         }
      }

      rulesToFilter.removeAll(rulesToDeactivate);
   }

   private void restoreCachedGeneratedCode()
   {
      for (IFile file : cachedGeneratedCode.keySet())
         try
         {
            file.setContents(IOUtils.toInputStream(cachedGeneratedCode.get(file)), IFile.FORCE, new NullProgressMonitor());
         } catch (CoreException e)
         {
            logger.error("Unable to restore code for: " + file.getName());
         }

      cachedGeneratedCode.clear();
   }
   
   private void filterTGGsFromBuildFilterProps(final Resource genEcoreResource)
   {
      MoflonPropertiesContainer moflonProperties = MoflonPropertiesContainerHelper.load(getProject(), WorkspaceHelper.createSubmonitorWith1Tick(new NullProgressMonitor()));

      Collection<BuildFilter> filter = moflonProperties.getBuildFilter();

      EPackage rootPackage = (EPackage) genEcoreResource.getContents().get(0);
      EPackage rulesPackage = rootPackage.getESubpackages().get(0);
      List<EClassifier> rulesToFilter = new ArrayList<>();
      for (EClassifier rule : rulesPackage.getEClassifiers())
      {
         for (BuildFilter bf : filter)
         {
            if (bf.isActivated())
               for (BuildFilterRule bfr : bf.getRules())
               {
                  if (bfr.getValue().equals(rule.getName()))
                     rulesToFilter.add(rule);
               }
         }
      }

      cacheGeneratedCodeForRules(rulesToFilter);

      for (EClassifier rule : rulesToFilter)
      {
         EClass ruleClass = (EClass) rule;
         for (EOperation op : ruleClass.getEOperations())
            eMoflonSDMUtil.deleteActivity(op);
      }
   }
}
