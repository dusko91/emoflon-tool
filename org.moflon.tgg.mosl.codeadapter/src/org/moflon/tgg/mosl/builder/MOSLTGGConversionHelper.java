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
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.MoflonUtilitiesActivator;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.moflon.tgg.language.TripleGraphGrammar;
import org.moflon.tgg.mosl.scoping.AttrCondDefLibraryProvider;
import org.moflon.tgg.mosl.tgg.AttrCond;
import org.moflon.tgg.mosl.tgg.AttrCondDef;
import org.moflon.tgg.mosl.tgg.AttrCondDefLibrary;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile;
import org.moflon.tgg.tggproject.TGGProject;
import org.moflon.tgg.tggproject.TggprojectFactory;
import org.moflon.tie.CodeadapterTrafo;

public class MOSLTGGConversionHelper extends AbstractHandler
{

   public static class MyURIHandler extends URIHandlerImpl
   {
      @Override
      public URI deresolve(URI uri)
      {
         String fileExt = uri.trimFileExtension().fileExtension();
         if (fileExt != null && fileExt.equals("pre"))
         {
            return uri.trimFileExtension().trimFileExtension().appendFileExtension("ecore");
         }
         return uri;
      }
   }

   public static void generateTGGModel(IResource resource)
   {
      try
      {
         IProject project = resource.getProject();
         if (resource.getProjectRelativePath().equals(project.getProjectRelativePath().append("/src/org/moflon/tgg/mosl")))
         {
            String projectPath = project.getFullPath().toString();
            IFolder moslFolder = IFolder.class.cast(resource);
            XtextResourceSet resourceSet = new XtextResourceSet();

            AttrCondDefLibraryProvider.syncAttrCondDefLibrary(resourceSet, projectPath);

            TripleGraphGrammarFile xtextParsedTGG = createTGGFileAndLoadSchema(resourceSet, moslFolder);
            if (xtextParsedTGG != null) {
				loadAllRulesToTGGFile(xtextParsedTGG, resourceSet, moslFolder);
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
	            saveInternalTGGModelToXMI(tggProject, resourceSet, options, project.getName());
			}
            
            
         }
      } catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   private static void addAttrCondDefLibraryReferencesToSchema(TripleGraphGrammarFile xtextParsedTGG) {
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

   private static void saveTGGFileToXMI(TripleGraphGrammarFile xtextParsedTGG, XtextResourceSet resourceSet, Map<Object, Object> options, String projectPath)
         throws IOException
   {

      String xmiFilePath = projectPath + "/XtextInstances/xmi/" + xtextParsedTGG.getSchema().getName() + ".tgg.xmi";
      URI xmiURI = URI.createPlatformPluginURI(xmiFilePath, false);
      Resource xmiResource = resourceSet.createResource(xmiURI);
      xmiResource.getContents().add(xtextParsedTGG);
      xmiResource.save(options);
   }

   private static void loadAllRulesToTGGFile(TripleGraphGrammarFile xtextParsedTGG, XtextResourceSet resourceSet, IFolder moslFolder)
         throws CoreException, IOException
   {
      IFolder moslRulesFolder = moslFolder.getFolder("rules");
      
      if (moslRulesFolder.exists()) {
    	  EList<Rule> rules = new BasicEList<Rule>();
	
	      for (IResource iResource : moslRulesFolder.members())
	      {
	         if (iResource instanceof IFile)
	         {
	            Rule rule = loadRule(iResource, resourceSet, moslFolder);
	            if (rule != null)
	            {
	               rules.add(rule);
	            }
	         }
	      }
	      xtextParsedTGG.getRules().addAll(rules);
      }
      
      
   }

   private static Rule loadRule(IResource iResource, XtextResourceSet resourceSet, IFolder moslFolder) throws IOException
   {
      IFile ruleFile = (IFile) iResource;
      if (ruleFile.getFileExtension().equals("tgg")) {
    	  XtextResource ruleRes = (XtextResource) resourceSet.createResource(URI.createPlatformResourceURI(ruleFile.getFullPath().toString(), false));
          ruleRes.load(null);
          EcoreUtil.resolveAll(resourceSet);

          EObject ruleEObj = ruleRes.getContents().get(0).eContents().get(0);
          if (ruleEObj instanceof Rule)
          {
             return (Rule) ruleEObj;
          }
      }
      return null;
   }

   private static TripleGraphGrammarFile createTGGFileAndLoadSchema(XtextResourceSet resourceSet, IFolder moslFolder) throws IOException
   {
      IFile schemaFile = moslFolder.getFile("Schema.tgg");
      
      if (schemaFile.exists()) {
    	  XtextResource schemaResource = (XtextResource) resourceSet.createResource(URI.createPlatformResourceURI(schemaFile.getFullPath().toString(), false));
          schemaResource.load(null);
          EcoreUtil.resolveAll(resourceSet);
          return (TripleGraphGrammarFile) schemaResource.getContents().get(0);
      } else {
    	  return null;
      }
   }

   @Override
   public Object execute(ExecutionEvent event) throws ExecutionException
   {
      try
      {
         ISelection selection = HandlerUtil.getCurrentSelection(event);

         if (selection instanceof IStructuredSelection)
         {
            Object file = ((IStructuredSelection) selection).getFirstElement();
            if (file instanceof IFile)
            {
               IFile tggFile = (IFile) file;

               ResourceSet resourceSet = eMoflonEMFUtil.createDefaultResourceSet();
               TGGProject tggProject = createTGGProject(tggFile, resourceSet);

               String pathToThisPlugin = MoflonUtilitiesActivator.getPathRelToPlugIn("/", MOSLTGGPlugin.getDefault().getPluginId()).getFile();
               CodeadapterTrafo helper = new CodeadapterTrafo(pathToThisPlugin, resourceSet);

               helper.setTrg(tggProject);
               helper.integrateBackward();
               
               saveXtextTGGModelToXMI((TripleGraphGrammarFile) helper.getSrc(), tggFile.getProject().getFullPath().toString());
               
               helper.postProcessBackward();

               TripleGraphGrammarFile tggModel = (TripleGraphGrammarFile) helper.getSrc();
               String projectPath = tggFile.getProject().getFullPath().toString();
                              
               saveXtextTGGModelToXMI(tggModel, projectPath);
               
               saveXtextTGGModelToTGGFile(tggModel, projectPath, "/src/org/moflon/tgg/mosl" + projectPath + ".tgg");
            }
         }
      } catch (IOException e)
      {
         e.printStackTrace();
      }

      return null;
   }

   private static void saveInternalTGGModelToXMI(TGGProject tggProject, XtextResourceSet resourceSet, Map<Object, Object> options, String saveTargetName)
         throws IOException
   {
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

   private void saveXtextTGGModelToXMI(TripleGraphGrammarFile tggModel, String projectPath) throws IOException
   {
      Map<Object, Object> saveOptions = new HashMap<Object, Object>();
      saveOptions.put(XMLResource.OPTION_URI_HANDLER, new MyURIHandler());

      XtextResourceSet xtextResourceSet = new XtextResourceSet();
      URI xmiFileURI = URI.createPlatformPluginURI(projectPath + "/XtextInstances/xmi/bwd/TGG.xmi", true);
      XMIResource pretggXmiResource = (XMIResource) xtextResourceSet.createResource(xmiFileURI);
      pretggXmiResource.getContents().add(tggModel);
      pretggXmiResource.save(saveOptions);
   }

   private void saveXtextTGGModelToTGGFile(TripleGraphGrammarFile tggModel, String projectPath, String filePath) throws IOException
   {
	  URI tggFileURI = URI.createPlatformResourceURI(projectPath + filePath, true);
	  
      XtextResourceSet xtextResourceSet = new XtextResourceSet();
      XtextResource xtextResource = (XtextResource) xtextResourceSet.createResource(tggFileURI);
      AttrCondDefLibrary attrCondDefLibrary = AttrCondDefLibraryProvider.syncAttrCondDefLibrary(xtextResourceSet, projectPath);
      
      EList<AttrCondDef> libraryAttrCondDefs = new BasicEList<AttrCondDef>();
      for (AttrCondDef attrCondDef : tggModel.getSchema().getAttributeCondDefs()) {
    	  if(!attrCondDef.isUserDefined()){
    		  libraryAttrCondDefs.add(attrCondDef);
    	  }
      }
      for (Rule rule : tggModel.getRules()) {
    	for (AttrCond attrCond : rule.getAttrConditions()) {
    		for (AttrCondDef attrCondDef : attrCondDefLibrary.getAttributeCondDefs()) {
				if(attrCondDef.getName().equals(attrCond.getName().getName())){
					attrCond.setName(attrCondDef);
				}
			}
		}
      }
      tggModel.getSchema().getAttributeCondDefs().removeAll(libraryAttrCondDefs);

      xtextResource.getContents().add(tggModel);
      EcoreUtil.resolveAll(xtextResource);

      SaveOptions.Builder options = SaveOptions.newBuilder();
      options.format();
      options.noValidation();
      xtextResource.save(options.getOptions().toOptionsMap());
   }

   private TGGProject createTGGProject(IFile tggFile, ResourceSet resourceSet) throws IOException
   {
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
