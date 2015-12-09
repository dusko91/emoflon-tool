package org.moflon.tgg.mosl.builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.moflon.core.utilities.MoflonUtilitiesActivator;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.moflon.tgg.language.TripleGraphGrammar;
import org.moflon.tgg.mosl.scoping.AttrCondDefLibraryProvider;
import org.moflon.tgg.mosl.tgg.AttrCond;
import org.moflon.tgg.mosl.tgg.AttrCondDef;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile;
import org.moflon.tgg.tggproject.TGGProject;
import org.moflon.tgg.tggproject.TggprojectFactory;
import org.moflon.tie.CodeadapterTrafo;


public class MOSLTGGConversionHelper extends AbstractHandler {
	
	public static class MyURIHandler extends URIHandlerImpl {
		@Override
	    public URI deresolve(URI uri)
	    {
//			if (uri.isPlatformResource()) {
//				URI uriTemp = URI.createPlatformPluginURI(uri.toPlatformString(true), true);
//				return uriTemp.appendFragment(uri.fragment());
//			}
//			else {
				String fileExt = uri.trimFileExtension().fileExtension();
				if (fileExt != null && fileExt.equals("pre")) {
					return uri.trimFileExtension().trimFileExtension().appendFileExtension("ecore");
				}				
				return uri;
//			}
	    }
	}
	

	public static void generateTGGModel(IResource resource) {
		
		try {
			IProject project = resource.getProject();
			if(resource.getProjectRelativePath().equals(project.getProjectRelativePath().append("/src/org/moflon/tgg/mosl"))){
				String projectPath = project.getFullPath().toString();				
				IFolder moslFolder = IFolder.class.cast(resource);
				XtextResourceSet resourceSet = new XtextResourceSet();

				AttrCondDefLibraryProvider.syncAttrCondDefLibrary(resourceSet, projectPath);
				
				TripleGraphGrammarFile xtextParsedTGG = createTGGFileAndLoadSchema(resourceSet, moslFolder);	
				loadAllRulesToTGGFile(xtextParsedTGG, resourceSet, moslFolder);

				//Save intermediate result of Xtext parsing
				Map<Object,Object> options = new HashMap<Object,Object>();
				options.put(XMLResource.OPTION_URI_HANDLER, new MyURIHandler());
				
				saveTGGFileToXMI(xtextParsedTGG, resourceSet, options, projectPath);
				
				
				//Invoke TGG forward transformation to produce TGG model		
				String pathToThisPlugin = MoflonUtilitiesActivator.getPathRelToPlugIn("/", MOSLTGGPlugin.getDefault().getPluginId()).getFile();
				
				CodeadapterTrafo helper = new CodeadapterTrafo(pathToThisPlugin);
				helper.getResourceSet().getResources().add(xtextParsedTGG.eResource());
				helper.setSrc(xtextParsedTGG);
				helper.integrateForward();
				helper.postProcessForward();

				TGGProject tggProject = (TGGProject) helper.getTrg();
				String saveTargetName = projectPath + "/model/" + xtextParsedTGG.getSchema().getName();
				saveInternalTGGModelToXMI(tggProject, resourceSet, options, saveTargetName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveTGGFileToXMI(TripleGraphGrammarFile xtextParsedTGG, XtextResourceSet resourceSet,
			Map<Object, Object> options, String projectPath) throws IOException {
		
		String xmiFilePath = projectPath + "/XtextInstances/xmi/" + xtextParsedTGG.getSchema().getName() + ".tgg.xmi";
		URI xmiURI = URI.createPlatformPluginURI(xmiFilePath, false);
		Resource xmiResource = resourceSet.createResource(xmiURI);
		xmiResource.getContents().add(xtextParsedTGG);
		xmiResource.save(options);
	}

	private static void loadAllRulesToTGGFile(TripleGraphGrammarFile xtextParsedTGG, XtextResourceSet resourceSet,
			IFolder moslFolder) throws CoreException, IOException {
		
		IFolder moslRulesFolder = moslFolder.getFolder("rules");
		EList<Rule> rules = new BasicEList<Rule>();
		EList<AttrCondDef> usedAttrCondDefs = new BasicEList<AttrCondDef>();
		
		for (IResource iResource : moslRulesFolder.members()) {
			if(iResource instanceof IFile){
				Rule rule = loadRule(iResource, resourceSet, moslFolder);
				if (rule != null) {
					rules.add(rule);
					// Copy used AttrCondDefs from Library
					for (AttrCond attrCond : rule.getAttrConditions()) {
						if (!usedAttrCondDefs.contains(attrCond.getName()) && !xtextParsedTGG.getSchema().getAttributeCondDefs().contains(attrCond.getName())) {
							usedAttrCondDefs.add(attrCond.getName());
						}
					}
				}
			}
		}
		xtextParsedTGG.getSchema().getAttributeCondDefs().addAll(usedAttrCondDefs);
		xtextParsedTGG.getRules().addAll(rules);
	}

	private static Rule loadRule(IResource iResource, XtextResourceSet resourceSet, IFolder moslFolder) throws IOException {
		IFile ruleFile = (IFile) iResource;
		XtextResource ruleRes = (XtextResource) resourceSet.createResource(URI.createPlatformPluginURI(ruleFile.getFullPath().toString(), false));
		ruleRes.load(null);
		EcoreUtil.resolveAll(resourceSet);
		
		EObject ruleEObj = ruleRes.getContents().get(0).eContents().get(0);
		if(ruleEObj instanceof Rule) {
			return (Rule) ruleEObj;
		}
		return null;
	}

	private static TripleGraphGrammarFile createTGGFileAndLoadSchema(XtextResourceSet resourceSet, IFolder moslFolder) throws IOException {
		IFile schemaFile = moslFolder.getFile("Schema.tgg");
		XtextResource schemaResource = (XtextResource) resourceSet.createResource(URI.createPlatformPluginURI(schemaFile.getFullPath().toString(), false));
		schemaResource.load(null);
		EcoreUtil.resolveAll(resourceSet);
//		TripleGraphGrammarFile xtextParsedTGG = (TripleGraphGrammarFile) schemaResource.getContents().get(0);
		return (TripleGraphGrammarFile) schemaResource.getContents().get(0);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		try {
			ISelection selection = HandlerUtil.getCurrentSelection(event);

			if (selection instanceof IStructuredSelection) {
				Object file = ((IStructuredSelection) selection).getFirstElement();
				if(file instanceof IFile){
					/************************************************************
					 * Approach #1:	Generates Single tgg-File
					 ************************************************************/
					
					IFile tggFile = (IFile)file;
					
					ResourceSet resourceSet =  eMoflonEMFUtil.createDefaultResourceSet();
					TGGProject tggProject = createTGGProject(tggFile, resourceSet);
					
					
					String pathToThisPlugin = MoflonUtilitiesActivator.getPathRelToPlugIn("/", MOSLTGGPlugin.getDefault().getPluginId()).getFile();			
					CodeadapterTrafo helper = new CodeadapterTrafo(pathToThisPlugin, resourceSet);
					
					helper.setTrg(tggProject);
					helper.integrateBackward();					
					helper.postProcessBackward();
					
					
					TripleGraphGrammarFile tggModel = (TripleGraphGrammarFile) helper.getSrc();
					String projectPath = tggFile.getProject().getFullPath().toString();
					
//					saveXtextTGGModelToXMI(tggModel, projectPath);
					saveXtextTGGModelToTGGFile(tggModel, projectPath);
					
					
					
					/************************************************************
					 * End of Approach #1.
					 ************************************************************/
					/************************************************************
					 * Approach #2:	Generates Separate tgg-File (Causes Error)
					 *			Caused by: java.lang.RuntimeException: No EObjectDescription could be found in Scope 
					 *				Rule.schema for TripleGraphGrammarFile.schema->Schema'LearningBoxToDictionaryIntegration'
					 *			Semantic Object: TripleGraphGrammarFile.rules[0]->Rule'BoxToDictionaryRule'
					 ************************************************************/
//					IFile tggFile = (IFile)file;
//					ResourceSet resourceSet = new ResourceSetImpl();
//					
//					XMIResource tggXMI = (XMIResource) resourceSet.createResource(URI.createPlatformResourceURI(tggFile.getFullPath().toString(), true));
//					tggXMI.load(null);
//					EcoreUtil.resolveAll(resourceSet);
//					
//					TripleGraphGrammarFile tggRootXMI = (TripleGraphGrammarFile) tggXMI.getContents().get(0);
//					
//					// Create: Schema.tgg
//					Schema schema = tggRootXMI.getSchema();
//					String tggIntName = schema.getName()+"/";
//					String projName = tggFile.getProject().getFullPath().toString();
//					
//					URI tggFileURI = URI.createPlatformResourceURI(projName + "/XtextInstances/dsl/" + tggIntName + 
//							"Schema.tgg", true);
//					
//					TripleGraphGrammarFile tggRootDSL = TggFactoryImpl.init().createTripleGraphGrammarFile();
//					tggRootDSL.setSchema(schema);
//					
//					XtextResourceSet xtextResourceSet = new XtextResourceSet();
//					XtextResource xtextResource = (XtextResource) xtextResourceSet.createResource(tggFileURI);
//					xtextResource.getContents().add(tggRootDSL);					
//
//					// Create: All *Rule.tgg
//					EList<Rule> rules = tggRootXMI.getRules();
//					Rule rule;
//					while(!rules.isEmpty()){
//						rule = rules.get(0);
//						tggFileURI = URI.createPlatformResourceURI(projName + "/XtextInstances/dsl/" + tggIntName + 
//								rule.getName() + ".tgg", true);
//						tggRootDSL = TggFactoryImpl.init().createTripleGraphGrammarFile();
//
//
//						tggRootDSL.getRules().clear();
//						tggRootDSL.getRules().add(rule);
//						
//						xtextResource = (XtextResource) xtextResourceSet.createResource(tggFileURI);
//						xtextResource.getContents().add(tggRootDSL);
//					}
//					
//					EList<Resource> resources = xtextResourceSet.getResources();
//					SaveOptions.Builder options = SaveOptions.newBuilder();
//					options.format();
//					options.noValidation();
//
//					for (int i = 0; i < resources.size(); i++) {
//						EcoreUtil.resolveAll(resourceSet);
//						EcoreUtil.resolveAll(xtextResourceSet);
//						((XtextResource) resources.get(i)).save(options.getOptions().toOptionsMap());
//					}
					/************************************************************
					 * End of Approach #2.
					 ************************************************************/
					
					//TODO Invoke TGG backward transformation to get the mosl tgg model
					
					//TODO Persist mosl tgg model as set of xtext files
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}


	private static void saveInternalTGGModelToXMI(TGGProject tggProject, XtextResourceSet resourceSet, Map<Object, Object> options, String saveTargetName) throws IOException {
		TripleGraphGrammar tgg = tggProject.getTgg();
		EPackage corrPackage = tggProject.getCorrPackage();

		URI preEcoreXmiURI = URI.createPlatformPluginURI(saveTargetName + ".pre.ecore", false);
		Resource preEcoreResource = resourceSet.createResource(preEcoreXmiURI);
		preEcoreResource.getContents().add(corrPackage);
		preEcoreResource.save(options);
		
		URI pretggXmiURI = URI.createPlatformPluginURI(saveTargetName + ".pre.tgg.xmi", false);
		Resource pretggXmiResource = resourceSet.createResource(pretggXmiURI);
		pretggXmiResource.getContents().add(tgg);
		pretggXmiResource.save(options);
	}

	private void saveXtextTGGModelToXMI(TripleGraphGrammarFile tggModel, String projectPath) throws IOException {
		Map<Object,Object> saveOptions = new HashMap<Object,Object>();
		saveOptions.put(XMLResource.OPTION_URI_HANDLER, new MyURIHandler());
		
		XtextResourceSet xtextResourceSet = new XtextResourceSet();
		URI xmiFileURI = URI.createPlatformPluginURI(projectPath + "/XtextInstances/xmi/bwd/TGG.xmi", true);
		XMIResource pretggXmiResource = (XMIResource) xtextResourceSet.createResource(xmiFileURI);
		pretggXmiResource.getContents().add(tggModel);
		pretggXmiResource.save(saveOptions);
	}

	private void saveXtextTGGModelToTGGFile(TripleGraphGrammarFile tggModel, String projectPath) throws IOException {
		URI tggFileURI = URI.createPlatformResourceURI(projectPath + "/src/org/moflon/mosl/bwd/Schema.tgg", true);
		
		XtextResourceSet xtextResourceSet = new XtextResourceSet();
		XtextResource xtextResource = (XtextResource) xtextResourceSet.createResource(tggFileURI);
		xtextResource.getContents().add(tggModel);
		EcoreUtil.resolveAll(xtextResource);
		
		SaveOptions.Builder options = SaveOptions.newBuilder();
		options.format();
		options.noValidation();
		xtextResource.save(options.getOptions().toOptionsMap());
	}

	private TGGProject createTGGProject(IFile tggFile, ResourceSet resourceSet) throws IOException {
		String tggFilePath = tggFile.getFullPath().toString();
		Resource tggEcoreResource = resourceSet.createResource(URI.createPlatformResourceURI(tggFilePath.replace(".tgg.xmi", ".ecore"), true));
		tggEcoreResource.load(null);
		Resource tggModelResource = resourceSet.createResource(URI.createPlatformResourceURI(tggFilePath, true));
		tggModelResource.load(null);
		EcoreUtil.resolveAll(resourceSet);
		
		TGGProject tggProject = TggprojectFactory.eINSTANCE.createTGGProject();
		tggProject.setCorrPackage((EPackage) tggEcoreResource.getContents().get(0));
		tggProject.setTgg((TripleGraphGrammar) tggModelResource.getContents().get(0));
		Resource tggProjectResource = resourceSet.createResource(URI.createURI("TGGProject"));
		tggProjectResource.getContents().add(tggProject);
		
		return tggProject;
	}
}
