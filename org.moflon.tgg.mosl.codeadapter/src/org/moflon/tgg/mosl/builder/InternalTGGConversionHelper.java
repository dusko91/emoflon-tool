package org.moflon.tgg.mosl.builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.MoflonUtilitiesActivator;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.moflon.tgg.language.TripleGraphGrammar;
import org.moflon.tgg.mosl.builder.MOSLTGGConversionHelper.MyURIHandler;
import org.moflon.tgg.mosl.scoping.AttrCondDefLibraryProvider;
import org.moflon.tgg.mosl.tgg.AttrCond;
import org.moflon.tgg.mosl.tgg.AttrCondDef;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile;
import org.moflon.tgg.tggproject.TGGProject;
import org.moflon.tie.CodeadapterTrafo;

public class InternalTGGConversionHelper extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
	  try
      {
		  ISelection selection = HandlerUtil.getCurrentSelection(event);
	
		  if (selection instanceof IStructuredSelection)
		  {
		    Object file = ((IStructuredSelection) selection).getFirstElement();
		    if (file instanceof IFile)
		    {
		       IFile tggFile = (IFile) file;
		       IFolder moslFolder = (IFolder) tggFile.getParent();
		       
		       if(moslFolder.getProjectRelativePath().toString().equals("src/org/moflon/tgg/mosl/rules")){
		    	   moslFolder = (IFolder) moslFolder.getParent();
		    	   tggFile = moslFolder.getFile("Schema.tgg");
		    	   if (!tggFile.exists()) {
		    		   
		    		   return null;
		    	   }
		       }
		       if(moslFolder.getProjectRelativePath().toString().equals("src/org/moflon/tgg/mosl")){
		    	   if(tggFile.getName().endsWith("Integration.tgg") || tggFile.getName().equals("Schema.tgg")){
		    		   String projectPath = tggFile.getProject().getFullPath().toString();
		    		   XtextResourceSet resourceSet = new XtextResourceSet();
		    		   AttrCondDefLibraryProvider.syncAttrCondDefLibrary(resourceSet, projectPath);
		    		   
				       TripleGraphGrammarFile xtextParsedTGG = createTGGFileAndLoadSchema(resourceSet, tggFile);
		    		   if (tggFile.getName().equals("Schema.tgg")) {
		    			   loadAllRulesToTGGFile(xtextParsedTGG, resourceSet, moslFolder);
		    		   }
		    		   addAttrCondDefLibraryReferencesToSchema(xtextParsedTGG);
		    		   
		    		// Save intermediate result of Xtext parsing
			           Map<Object, Object> options = new HashMap<Object, Object>();
			           options.put(XMLResource.OPTION_URI_HANDLER, new MyURIHandler());
			
			           saveTGGFileToXMI(xtextParsedTGG, resourceSet, options, projectPath);
			
			           // Invoke TGG forward transformation to produce TGG model
			           String pathToThisPlugin = MoflonUtilitiesActivator.getPathRelToPlugIn("/", MOSLTGGPlugin.getDefault().getPluginId()).getFile();
			
			           CodeadapterTrafo helper = new CodeadapterTrafo(pathToThisPlugin);
			           helper.getResourceSet().getResources().add(xtextParsedTGG.eResource());
			           helper.setSrc(xtextParsedTGG);
			           helper.integrateForward();
			           helper.postProcessForward();
			
			           TGGProject tggProject = (TGGProject) helper.getTrg();
			           saveInternalTGGModelToXMI(tggProject, resourceSet, options, tggFile.getProject().getName());
		    	   }
		       }
		    }
		  }
      } catch (IOException | CoreException e)
      {
         e.printStackTrace();
      }
	  
      return null;
	}
	
	private TripleGraphGrammarFile createTGGFileAndLoadSchema(XtextResourceSet resourceSet, IFile schemaFile) throws IOException
	{
		XtextResource schemaResource = (XtextResource) resourceSet.createResource(URI.createPlatformResourceURI(schemaFile.getFullPath().toString(), false));
	    schemaResource.load(null);
	    EcoreUtil.resolveAll(resourceSet);
	    return (TripleGraphGrammarFile) schemaResource.getContents().get(0);
	}
	
	private void loadAllRulesToTGGFile(TripleGraphGrammarFile xtextParsedTGG, XtextResourceSet resourceSet,
			IFolder moslFolder) throws CoreException, IOException {
	  IFolder moslRulesFolder = moslFolder.getFolder("rules");
      if (moslRulesFolder.exists()) {
    	  EList<Rule> rules = new BasicEList<Rule>();
	      for (IResource iResource : moslRulesFolder.members())
	      {
	         if (iResource instanceof IFile)
	         {
	            Rule rule = loadRule(iResource, resourceSet, moslFolder);
	            if (rule != null)
	            	rules.add(rule);
	         }
	      }
	      xtextParsedTGG.getRules().addAll(rules);
      }
		
	}
	
	private Rule loadRule(IResource iResource, XtextResourceSet resourceSet, IFolder moslFolder) throws IOException {
		IFile ruleFile = (IFile) iResource;
	      if (ruleFile.getFileExtension().equals("tgg")) {
	    	  XtextResource ruleRes = (XtextResource) resourceSet.createResource(URI.createPlatformResourceURI(ruleFile.getFullPath().toString(), false));
	          ruleRes.load(null);
	          EcoreUtil.resolveAll(resourceSet);

	          EObject ruleEObj = ruleRes.getContents().get(0).eContents().get(0);
	          if (ruleEObj instanceof Rule)	
	        	  return (Rule) ruleEObj;
	      }
	      return null;
	}
	private void addAttrCondDefLibraryReferencesToSchema(TripleGraphGrammarFile xtextParsedTGG) {
	   EList<AttrCondDef> usedAttrCondDefs = new BasicEList<AttrCondDef>();
	   for (Rule rule : xtextParsedTGG.getRules()) {
		   for (AttrCond attrCond : rule.getAttrConditions()) {
			   if (!usedAttrCondDefs.contains(attrCond.getName()) && !attrCond.getName().isUserDefined()) {
				   usedAttrCondDefs.add(attrCond.getName());
			   }
		   }
	   }
	   xtextParsedTGG.getSchema().getAttributeCondDefs().addAll(usedAttrCondDefs);
	}

	private void saveTGGFileToXMI(TripleGraphGrammarFile xtextParsedTGG, XtextResourceSet resourceSet,
			Map<Object, Object> options, String projectPath) throws IOException {
	  String xmiFilePath = projectPath + "/XtextInstances/xmi/" + xtextParsedTGG.getSchema().getName() + ".tgg.xmi";
      URI xmiURI = URI.createPlatformPluginURI(xmiFilePath, false);
      Resource xmiResource = resourceSet.createResource(xmiURI);
      xmiResource.getContents().add(xtextParsedTGG);
      xmiResource.save(options);
		
	}

	private void saveInternalTGGModelToXMI(TGGProject tggProject, XtextResourceSet resourceSet,
			Map<Object, Object> options, String saveTargetName) throws IOException {
	  TripleGraphGrammar tgg = tggProject.getTgg();
      EPackage corrPackage = tggProject.getCorrPackage();

      URI preEcoreXmiURI = URI.createPlatformPluginURI(saveTargetName + "/" + MoflonUtil.getDefaultPathToFileInProject(saveTargetName, ".pre.ecore"), false);
      Resource preEcoreResource = resourceSet.createResource(preEcoreXmiURI);
      preEcoreResource.getContents().add(corrPackage);
      preEcoreResource.save(options);

      URI pretggXmiURI = URI.createPlatformPluginURI(saveTargetName + "/" + MoflonUtil.getDefaultPathToFileInProject(saveTargetName, ".pre.tgg.xmi"), false);
      Resource pretggXmiResource = resourceSet.createResource(pretggXmiURI);
      pretggXmiResource.getContents().add(tgg);
      pretggXmiResource.save(options);
	}

}
