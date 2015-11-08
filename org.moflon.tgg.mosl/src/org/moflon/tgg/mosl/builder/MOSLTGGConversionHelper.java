package org.moflon.tgg.mosl.builder;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.Schema;
import org.moflon.tgg.mosl.tgg.TggFactory;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile;
import org.moflon.tgg.mosl.tgg.impl.TggFactoryImpl;


public class MOSLTGGConversionHelper extends AbstractHandler {
	

	public static void generateTGGModel(IResource resource) {
		
		try {
			if(resource.getProjectRelativePath().equals(resource.getProject().getProjectRelativePath().append("/src/org/moflon/tgg/mosl"))){
				IFolder moslFolder = IFolder.class.cast(resource);
				XtextResourceSet resourceSet = new XtextResourceSet();

				IFile schemaFile = moslFolder.getFile("Schema.tgg");
				Resource schema = resourceSet.createResource(URI.createPlatformResourceURI(schemaFile.getFullPath().toString(), true));
				schema.load(null);
				
				EcoreUtil.resolveAll(resourceSet);
				TripleGraphGrammarFile tggRoot = (TripleGraphGrammarFile) schema.getContents().get(0);				
				
				//TODO Load all rules
				
				IFolder moslRulesFolder = moslFolder.getFolder("rules");
				EList<Rule> rules = new BasicEList<Rule>((tggRoot).getRules());
				IFile ruleFile;
				Resource ruleRes;
				EObject rule;
				
				
				for (IResource iResource : moslRulesFolder.members()) {
					if(iResource instanceof IFile){
						ruleFile = (IFile) iResource;
						ruleRes = resourceSet.createResource(URI.createPlatformResourceURI(ruleFile.getFullPath().toString(), true));
						ruleRes.load(null);
						EcoreUtil.resolveAll(resourceSet);
						rule = ruleRes.getContents().get(0).eContents().get(0);
						if(rule instanceof Rule) {
							rules.add((Rule) rule);
						}
					}
				}
				tggRoot.getRules().addAll(rules);

				URI xmiURI = URI.createPlatformResourceURI(moslFolder.getProject().getFullPath().toString() + 
						"/XtextInstances/xmi/" + tggRoot.getSchema().getName() + ".tgg.xmi", true);
				Resource xmiResource = resourceSet.createResource(xmiURI);
				xmiResource.getContents().add(tggRoot);
				xmiResource.save(null);
				
				
				//TODO Add everything to form a single container
				
				//TODO Invoke TGG forward transformation to produce TGG model
				
				//TODO Persist TGG model in /model folder of current project according to naming convention
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					String projName = tggFile.getProject().getFullPath().toString() + "/";
					URI tggFileURI = URI.createPlatformResourceURI(projName + "XtextInstances/dsl/" + projName + 
							tggFile.getName().substring(0, tggFile.getName().lastIndexOf(".xmi")), true);

					XtextResourceSet resourceSet = new XtextResourceSet();
					Resource tgg = resourceSet.createResource(URI.createPlatformResourceURI(tggFile.getFullPath().toString(), true));
					tgg.load(null);
					EcoreUtil.resolveAll(resourceSet);

					XtextResource xtextResource = (XtextResource) resourceSet.createResource(tggFileURI);
					xtextResource.getContents().add(tgg.getContents().get(0));
					EcoreUtil.resolveAll(xtextResource);		
					
					SaveOptions.Builder options = SaveOptions.newBuilder();
					options.format();
					options.noValidation();
					
					xtextResource.save(options.getOptions().toOptionsMap());
					
					
					/************************************************************
					 * Approach #2:	Generates Separate tgg-File (Causes Error)
					 *			Caused by: java.lang.RuntimeException: No EObjectDescription could be found in Scope 
					 *				Rule.schema for TripleGraphGrammarFile.schema->Schema'LearningBoxToDictionaryIntegration'
					 *			Semantic Object: TripleGraphGrammarFile.rules[0]->Rule'BoxToDictionaryRule'
					 ************************************************************/
					
//					IFile tggFile = (IFile)file;
//					XtextResourceSet resourceSet = new XtextResourceSet();
//					
//					Resource tgg = resourceSet.createResource(URI.createPlatformResourceURI(tggFile.getFullPath().toString(), true));
//					tgg.load(null);
//					EcoreUtil.resolveAll(resourceSet);
//					
//					TripleGraphGrammarFile tggRootXMI = (TripleGraphGrammarFile) tgg.getContents().get(0);
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
//					
//					tggRootDSL.setSchema(schema);
//					XtextResource xtextResource = (XtextResource) resourceSet.createResource(tggFileURI);
//					xtextResource.getContents().add(tggRootDSL);
//					EcoreUtil.resolveAll(xtextResource);
//					
//					SaveOptions.Builder options = SaveOptions.newBuilder();
//					options.format();
//					options.noValidation();
//					xtextResource.save(options.getOptions().toOptionsMap());
//					
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
//						tggRootDSL.getRules().clear();;
//						tggRootDSL.getRules().add(rule);
//						
//						xtextResource = (XtextResource) resourceSet.createResource(tggFileURI);
//						xtextResource.getContents().add(tggRootDSL);
//						
//						EcoreUtil.resolveAll(xtextResource);
//						xtextResource.save(options.getOptions().toOptionsMap());
//					}
					
					//TODO Invoke TGG backward transformation to get the mosl tgg model
					
					//TODO Persist mosl tgg model as set of xtext files
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
