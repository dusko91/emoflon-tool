package org.moflon.ide.core.runtime.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.moflon.codegen.eclipse.MonitoredMetamodelLoader;
import org.moflon.dependency.DependencyHandler;
import org.moflon.dependency.FujabaCompatibleTGGInternalDependencyHandler;
import org.moflon.dependency.PackageRemappingDependency;
import org.moflon.dependency.UserDefinedProjectDependencyHandler;
import org.moflon.dependency.WorkspaceProjectDependencyHandler;
import org.moflon.eclipse.resource.SDMEnhancedEcoreResource;
import org.moflon.ide.core.CoreActivator;
import org.moflon.moca.CSPCodeAdapter;
import org.moflon.moca.MocaUtil;
import org.moflon.moca.tggUserDefinedConstraint.unparser.TGGUserDefinedConstraintUnparserAdapter;
import org.moflon.moca.tie.RunIntegrationGeneratorBatch;
import org.moflon.moca.tie.RunIntegrationGeneratorSync;
import org.moflon.properties.MoflonProperties;
import org.moflon.util.WorkspaceHelper;
import org.moflon.util.eMoflonEMFUtil;

import MoflonPropertyContainer.BuildFilter;
import MoflonPropertyContainer.BuildFilterRule;
import TGGLanguage.TGGRule;
import TGGLanguage.TripleGraphGrammar;
import TGGLanguage.compiler.CompilerFactory;
import TGGLanguage.compiler.TGGCompiler;
import TGGLanguage.csp.CSP;
import TGGLanguage.precompiler.CyclicContextMessage;
import TGGLanguage.precompiler.PrecompileLog;
import TGGLanguage.algorithm.ApplicationTypes;
import TGGLanguage.precompiler.PrecompileMessage;
import TGGLanguage.precompiler.RefinementPrecompiler;
import TGGLanguage.precompiler.PrecompilerFactory;
import TGGLanguage.precompiler.RuleProcessingMessage;
import csp.CSPNotSolvableException;

public class IntegrationBuilder extends AbstractEcoreBuilder {
	private static final Logger logger = Logger
			.getLogger(IntegrationBuilder.class);

	public static final String SUFFIX_SMA = "gen.sma.xmi";

	public static final String SUFFIX_GEN_TGG = "gen.tgg.xmi";

	private Collection<MocaTree.Node> userDefinedConstraints;

	protected ResourceSetImpl resourceSet;

	protected TripleGraphGrammar tgg;

	@Override
	protected boolean processResource(IProgressMonitor monitor)
			throws CoreException {
		processTGG(monitor);

		boolean success = processEcore(monitor);

		monitor.done();

		return success;
	}

	protected boolean processEcore(IProgressMonitor monitor)
			throws CoreException {
		// Build ecore file normally
		return super.processResource(new SubProgressMonitor(monitor,
				10 * WorkspaceHelper.PROGRESS_SCALE));
	}

	protected void processTGG(IProgressMonitor monitor) throws CoreException {
		monitor.beginTask(getProgressBarMessage(),
				20 * WorkspaceHelper.PROGRESS_SCALE);

		// Create a ResourceSet for resources
		resourceSet = new ResourceSetImpl();

		// Create .gen.ecore afresh
		IFile projectEcoreFile = super.getEcoreFile();
		IFile projectGenEcoreFile = getEcoreFile();
		if (projectGenEcoreFile.exists())
			projectGenEcoreFile.delete(true, new SubProgressMonitor(monitor,
					1 * WorkspaceHelper.PROGRESS_SCALE));
		projectEcoreFile.copy(projectGenEcoreFile.getFullPath(),
				IResource.FORCE | IResource.DERIVED, new SubProgressMonitor(
						monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));

		// Handle Ecore dependencies and load basic Ecore file for generated
		// SDMs from TGG
		DependencyHandler[] dependenciesHandlers = new DependencyHandler[] {
				new FujabaCompatibleTGGInternalDependencyHandler(),
				new WorkspaceProjectDependencyHandler(),
				new UserDefinedProjectDependencyHandler() };
		MonitoredMetamodelLoader metamodelLoader = new MonitoredMetamodelLoader(
				resourceSet, projectGenEcoreFile, dependenciesHandlers);
		metamodelLoader.run(new NullProgressMonitor());
		Resource genEcoreResource = metamodelLoader.getEcoreResource();
		URI genEcoreURI = genEcoreResource.getURI();

		eMoflonEMFUtil.installCrossReferencers(resourceSet);
		monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

		resourceSet.setPackageRegistry(new EPackageRegistryImpl(
				EPackage.Registry.INSTANCE));
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());

		monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

		// Add EcorePackage to resourceSet
		Resource ecoreResource = eMoflonEMFUtil.addToResourceSet(resourceSet,
				EcorePackage.eINSTANCE);

		// Load TGG file
		IFile tggFile = getProject().getFile(
				"/model/" + getProject().getName()
						+ WorkspaceHelper.TGG_FILE_EXTENSION);
		URI tggFileURI = URI.createPlatformResourceURI(tggFile.getFullPath()
				.toOSString(), true);

		// Compile TGGs to SDMs
		monitor.subTask(getProject().getName()
				+ ": Translating TGG model to SDMs...");
		PackageRemappingDependency tggDependency = new PackageRemappingDependency(
				tggFileURI);
		Resource tggResource = tggDependency.getResource(resourceSet, true);
		tgg = (TripleGraphGrammar) tggResource.getContents().get(0);

		// Create and add precompiler to resourceSet so reverse navigation of
		// links works
		RefinementPrecompiler rprecompiler = PrecompilerFactory.eINSTANCE
				.createRefinementPrecompiler();
		eMoflonEMFUtil.addToResourceSet(resourceSet, rprecompiler);

		// Refine rules and print compiler log
		printPrecompilerLog(rprecompiler.refineRules(tgg));

		// Parse csp specs
		parseCspSpecs(tgg);

		// filter tgg rules should not be build in this build run
		filterTGGsFromBuildFilterProps(tgg);

		// Create SDMs from TGG specification and place as annotations in
		// corresponding EOperations
		TGGCompiler compiler = CompilerFactory.eINSTANCE.createTGGCompiler();
		eMoflonEMFUtil.addToResourceSet(resourceSet, compiler);

		try {
			MoflonProperties moflonProperties = new MoflonProperties(
					getProject(), WorkspaceHelper.createSubMonitor(subMonitor));

			compiler.deriveOperationalRules(tgg, ApplicationTypes
					.get(moflonProperties.getBuildMode().getValue()));

		} catch (CSPNotSolvableException e) {
			try {
				IFile debugFile = getEcoreFile().getProject()
						.getFolder("/debug").getFile("csp_error.xmi");
				eMoflonEMFUtil.saveModel(e.getCsp(), debugFile.getLocation()
						.toOSString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw e;
		}

		monitor.worked(5 * WorkspaceHelper.PROGRESS_SCALE);

		try {
			HashMap<String, Object> saveOptions = new HashMap<String, Object>();
			saveOptions
					.put(SDMEnhancedEcoreResource.SAVE_GENERATED_PACKAGE_CROSSREF_URIS,
							Boolean.valueOf(true));

			// Persist tgg model after precompilation
			URI genTGGFileURI = genEcoreURI.trimFileExtension()
					.trimFileExtension().appendFileExtension(SUFFIX_GEN_TGG);
			Resource genTGGResource = tgg.eResource();
			genTGGResource.setURI(genTGGFileURI);
			genTGGResource.save(saveOptions);

			// Persist results of static analysis
			URI smaFileURI = genEcoreURI.trimFileExtension()
					.trimFileExtension().appendFileExtension(SUFFIX_SMA);
			PackageRemappingDependency smaFile = new PackageRemappingDependency(
					smaFileURI);
			Resource smaResource = smaFile.getResource(resourceSet, false);
			smaResource.getContents().add(compiler.getStaticAnalysis());
			smaResource.save(saveOptions);

			genEcoreResource.save(saveOptions);
			monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR,
					CoreActivator.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e));
		}

		// Generate user defined constraints
		generateUserDefinedConstraints();

		// Generate stub for batch transformation if necessary
		new RunIntegrationGeneratorBatch(getProject()).doFinish();
		
		// Generate stub for sync if necessary
		new RunIntegrationGeneratorSync(getProject()).doFinish();

		// Remove ecore from resourceSet
		resourceSet.getResources().remove(ecoreResource);
	}

	protected void persistEcoreFile(IFile projectGenEcoreFile,
			Resource ecoreResource) {
		// Save Ecore file
		eMoflonEMFUtil.saveModel(ecoreResource.getContents().get(0),
				projectGenEcoreFile.getLocation().toString());
	}

	/**
	 * Generates the files to implement user defined constraints and fills them
	 * with basic content. <br>
	 * An existing file won't be changed even if the definition of the
	 * constraint has been changed.
	 * 
	 * @throws CoreException
	 */
	protected void generateUserDefinedConstraints() throws CoreException {
		NullProgressMonitor monitor = new NullProgressMonitor();
		TGGUserDefinedConstraintUnparserAdapter unparser = new TGGUserDefinedConstraintUnparserAdapter();

		if (userDefinedConstraints.size() != 0) {
			// Create required folder structure
			WorkspaceHelper.addAllFolders(getProject(), "src/csp/constraints",
					monitor);

			for (MocaTree.Node node : userDefinedConstraints) {
				String content = unparser.unparse(node);
				String nameInUpperCase = MocaUtil.firstToUpper(node.getName());
				String path = "src/csp/constraints/" + nameInUpperCase
						+ ".java";

				// Ignore existing files
				if (!getProject().getFile(path).exists())
					WorkspaceHelper.addFile(getProject(), path, content,
							monitor);
			}
		}
	}

	@Override
	protected IFile getEcoreFile() {
		return getProject().getFolder(WorkspaceHelper.MODEL_FOLDER).getFile(
				getProject().getName() + WorkspaceHelper.SUFFIX_GEN_ECORE);
	}

	private void printPrecompilerLog(PrecompileLog log) {
		String errorMessage = "";
		for (PrecompileMessage error : log.getPrecompileerror()) {
			if (error instanceof CyclicContextMessage) {
				CyclicContextMessage cycle = (CyclicContextMessage) error;
				errorMessage += cycle.getMessage() + ": "
						+ error.getRefinementrule() + " cycle found: ";

				for (TGGRule cyclicRule : cycle.getRefineablerules()
						.getTggrule())
					errorMessage += cyclicRule.getName() + " ";

				errorMessage += "\n";
			}

			if (error instanceof RuleProcessingMessage) {
				RuleProcessingMessage rule = (RuleProcessingMessage) error;
				errorMessage += rule.getMessage() + ": "
						+ error.getRefinementrule() + " basis: "
						+ rule.getBaserule() + " | Error at "
						+ rule.getTggobjectvariable() + "\n";
			}
		}
		if (!errorMessage.equals("")) {
			errorMessage = " ------------ PreCompiler Errorlog ------------ \n"
					+ errorMessage + " --------------------------------------";
			logger.error("TGGLanguage Precompiler (Refinements) - "
					+ errorMessage);
		}

		String warningMessage = "";
		for (PrecompileMessage warning : log.getPrecompilewarning()) {
			if (warning instanceof RuleProcessingMessage) {
				RuleProcessingMessage rule = (RuleProcessingMessage) warning;
				warningMessage += rule.getMessage() + ": "
						+ warning.getRefinementrule() + " basis: "
						+ rule.getBaserule() + " | Refining: "
						+ rule.getTggobjectvariable() + "\n";
			}
		}
		if (!errorMessage.equals("")) {
			warningMessage = " ------------ PreCompiler Warninglog ------------ \n"
					+ warningMessage
					+ " --------------------------------------";
			logger.warn("TGGLanguage Precompiler (Refinements) - "
					+ warningMessage);
		}
	}

	private void filterTGGsFromBuildFilterProps(TripleGraphGrammar tgg) {
		MoflonProperties moflonProperties = new MoflonProperties(getProject(),
				WorkspaceHelper.createSubMonitor(subMonitor));

		Collection<BuildFilter> filter = moflonProperties.getBuildFilters();

		List<TGGRule> rulesToFilter = new ArrayList<TGGRule>();
		for (TGGRule rule : tgg.getTggRule()) {
			for (BuildFilter bf : filter) {
				if (bf.isActivated())
					for (BuildFilterRule bfr : bf.getRules()) {
						if (bfr.getValue().equals(rule.getName()))
							rulesToFilter.add(rule);
					}
			}
		}

		tgg.getTggRule().remove(rulesToFilter);
	}

	protected void parseCspSpecs(TripleGraphGrammar tgg) {
		userDefinedConstraints = new ArrayList<MocaTree.Node>();
		for (TGGRule rule : tgg.getTggRule()) {
			String cspSpec = rule.getCspSpec();
			if (cspSpec != null && cspSpec.trim().length() > 0) {
				CSPCodeAdapter adapter = new CSPCodeAdapter();
				CSP csp = adapter.parse(rule);
				userDefinedConstraints.addAll(adapter
						.getUserDefinedConstraints());
				rule.setCsp(csp);
			}
		}
	}
}
