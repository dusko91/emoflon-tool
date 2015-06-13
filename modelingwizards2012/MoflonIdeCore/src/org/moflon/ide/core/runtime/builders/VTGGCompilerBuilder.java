package org.moflon.ide.core.runtime.builders;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.core.runtime.EMFCodegeneratorHelper;
import org.moflon.ide.core.runtime.SDMCodegeneratorHelper;
import org.moflon.moca.CSPCodeAdapter;
import org.moflon.moca.VTGGCodeGenerator;
import org.moflon.util.MoflonUtil;
import org.moflon.util.eMoflonEMFUtil;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import TGGLanguage.TGGRule;
import TGGLanguage.TripleGraphGrammar;
import TGGLanguage.compiler.Compiler;
import TGGLanguage.compiler.CompilerFactory;
import TGGLanguage.csp.CSP;
import TGGLanguage.csp.CspPackage;
import csp.CSPNotSolvableException;

public class VTGGCompilerBuilder extends AbstractEcoreBuilder
{

	   public static final String SUFFIX_ECORE = ".ecore";

	   public static final String SUFFIX_IMPL_ECORE = ".impl.ecore";
	   
	   public static final String SUFFIX_SMA = "gen.sma.xmi";
	   
	   private static final Logger logger = Logger.getLogger(VTGGCompilerBuilder.class);

	   @Override
	   protected boolean processResource(IProgressMonitor monitor) throws CoreException
	   {
		   System.err.println("-----> progressing ViewBuild");
		   logger.debug("############### ViewBuilder started ##############");
		   Map<String, String> operationHashMap = new HashMap<String, String>(); 
		   
		   //create SDMHelper and generate SDM code
		   SDMCodegeneratorHelper genHelper = new SDMCodegeneratorHelper(getEcoreFile(), true, true);
		   try {
			   genHelper.generateCode();
			   logger.debug("############### SDM-code generated ##############");
		   } 
		   catch (Exception e) {
			   logger.debug("############### couldn't generate code for SDMs ##############");
			   e.printStackTrace();
		   }
		   operationHashMap = SDMCodegeneratorHelper.getCodeHashMap();
		   
		   IResource ecoreFile = getEcoreFile();
		   //create CodeGenerator filled with EOperation HashMap and generate code
		   VTGGCodeGenerator codeGen = new VTGGCodeGenerator(ecoreFile, operationHashMap);
		   codeGen.transform();
		   
		   monitor.done();
		   return true;
	   }

	   @Override
	   public boolean visit(IResource resource) throws CoreException
	   {
		   
		   IPath pathToResource = resource.getProjectRelativePath(); 
		   
		   // Make sure changes are from the right ecore file according to convention 
		   if (pathToResource.equals(new Path(WorkspaceHelper.MODEL_FOLDER + resource.getProject().getName() + WorkspaceHelper.ECORE_FILE_EXTENSION))) 
		   { 
		       // Only generate code if resource wasn't deleted! 
		       if (resource.exists()) 
		       { 
		          logger.debug("Build due to changes to: " + pathToResource); 
		          return processResource(subMonitor); 
		       }
		   }
		   return false;
		
	   }
	   
	   
	   @Override
	   protected IFile getEcoreFile()
	   {
		  // return file: [projectname]/model/[projectname].ecore
	      return getProject().getFolder(WorkspaceHelper.MODEL_FOLDER).getFile(getProject().getName() + SUFFIX_ECORE);
		   //return getProject().getFolder(WorkspaceHelper.MODEL_FOLDER).getFile("Sichtenmetamodell.impl.ecore");
	   }

	   private Resource translateTGGFile(ResourceSet resourceSet, URI ecoreFileURI, URI tggFileURI)
	   {
	      Resource ecoreResource = resourceSet.getResource(ecoreFileURI, true);

	      Resource tggResource = resourceSet.getResource(tggFileURI, true);
	      TripleGraphGrammar tgg = (TripleGraphGrammar) tggResource.getContents().get(0);

	      // Parse csp specs
	      parseCspSpecs(tgg);

	      // Create SDMs from TGG specification and place as annotations in corresponding EOperations
	      Resource compilerContainer = resourceSet.createResource(URI.createURI("compiler"));
	      Compiler compiler = CompilerFactory.eINSTANCE.createCompiler();
	      compilerContainer.getContents().add(compiler);
	 
	      try {
	         compiler.deriveOperationalRules(tgg);
	      }
	      catch (CSPNotSolvableException e) {
	         IFile debugFile = getEcoreFile().getProject().getFolder("/debug").getFile("csp_error.xmi");
	         eMoflonEMFUtil.saveModel(CspPackage.eINSTANCE, e.getCsp(), debugFile.getLocation().toOSString());
	         throw e;
	      }
	      

	      // Persist static analysis data
	      URI smaFileURI = URI.createPlatformResourceURI(getEcoreFile().getFullPath().toOSString(), true);
	      eMoflonEMFUtil.saveModel(compiler.getStaticAnalysis(), smaFileURI.trimFileExtension().trimFileExtension().appendFileExtension(SUFFIX_SMA));

	      return ecoreResource;
	   }

	   private void parseCspSpecs(TripleGraphGrammar tgg)
	   {
	      for (TGGRule rule : tgg.getTggRule())
	      {
	         String cspSpec = rule.getCspSpec();
	         if (cspSpec != null && cspSpec.length() > 0)
	         {
	            CSP csp = new CSPCodeAdapter().parse(rule);
	            rule.setCsp(csp);
	         }
	      }
	   }
	}
