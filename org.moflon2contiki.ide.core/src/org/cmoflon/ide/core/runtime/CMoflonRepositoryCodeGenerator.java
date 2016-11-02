package org.cmoflon.ide.core.runtime;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.cmoflon.ide.core.runtime.codegeneration.CMoflonCodeGenerator;
import org.cmoflon.ide.core.runtime.codegeneration.CMoflonCodeGeneratorTask;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.core.utilities.eMoflonEMFUtil;

/**
 * Mimics {@link RepositoryCodeGenerator}. Needed to invoke {@link CMoflonCodeGenerator}
 * @author David Giessing
 *
 */
public class CMoflonRepositoryCodeGenerator {

	private static final Logger logger = Logger.getLogger(CMoflonRepositoryCodeGenerator.class);

	   protected IProject project;

	   public CMoflonRepositoryCodeGenerator(final IProject project)
	   {
	      this.project = project;
	   }
	
	public boolean generateCode(final IProgressMonitor monitor,Properties contikiProperties){
		 IFile ecoreFile;
		try {
			ecoreFile = getEcoreFileAndHandleMissingFile();
			if (!ecoreFile.exists())
	         {
	            logger.warn("Unable to generate code for " + project.getName()
	                  + ",  as no Ecore file according to naming convention (capitalizeFirstLetter.lastSegmentOf.projectName) was found!");
	            return false;
	         }

	         final ResourceSet resourceSet = CodeGeneratorPlugin.createDefaultResourceSet();
	         eMoflonEMFUtil.installCrossReferencers(resourceSet);
	         monitor.worked(1);
	         CMoflonCodeGeneratorTask gen= new CMoflonCodeGeneratorTask(ecoreFile, resourceSet);
	         gen.run(WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	protected IFile getEcoreFileAndHandleMissingFile() throws CoreException
	   {
	      if (!doesEcoreFileExist())
	         createMarkersForMissingEcoreFile();

	      return getEcoreFile();
	   }
	
	protected IFile getEcoreFile()
	   {
	      return getEcoreFile(project);
	   }
	public static IFile getEcoreFile(final IProject p)
	   {
	      String ecoreFileName = MoflonUtil.getDefaultNameOfFileInProjectWithoutExtension(p.getName());
	      return p.getFolder(WorkspaceHelper.MODEL_FOLDER).getFile(ecoreFileName + WorkspaceHelper.ECORE_FILE_EXTENSION);
	   }
	private boolean doesEcoreFileExist()
	   {
	      return getEcoreFile().exists();
	   }
	
	private void createMarkersForMissingEcoreFile() throws CoreException
	   {
	      IFile ecoreFile = getEcoreFile();
	      logger.error("Unable to generate code: " + ecoreFile + " does not exist in project!");

	      // Create marker
	      final IMarker marker = project.createMarker(IMarker.PROBLEM);
	      marker.setAttribute(IMarker.MESSAGE, "Cannot find: " + ecoreFile.getProjectRelativePath().toString());
	      marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
	      marker.setAttribute(IMarker.LOCATION, ecoreFile.getProjectRelativePath().toString());
	   }

	public static boolean isEcoreFileOfProject(final IResource resource, final IProject p)
	   {
	      return resource.exists() && resource.getProjectRelativePath().equals(getEcoreFile(p).getProjectRelativePath());
	   }
}
