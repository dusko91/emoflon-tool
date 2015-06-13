package org.moflon.ide.ui.admin.wizards.tie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.moflon.Activator;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.ui.UIActivator;
import org.moflon.ide.ui.admin.wizards.AbstractFileGenerator;
import org.moflon.ide.ui.admin.wizards.moca.BasicFormatRenderer;
import org.moflon.util.eMoflonEMFUtil;

public class StartIntegratorGenerator extends AbstractFileGenerator{

	private static Logger logger = Logger.getLogger(UIActivator.class);

	private StringTemplateGroup stg;

	private String packagePrefix;


	public StartIntegratorGenerator(IProject project) {
		super(project);
		this.packagePrefix = "org.moflon.tie";
	}


	@Override
	protected void prepareCodegen() {

		List<String> packageFolders = Arrays.asList(packagePrefix.split("\\."));
		String currentFolder = "src/";
		for (String folder : packageFolders) {
			currentFolder += folder + "/";
			createFolder(currentFolder);
		}
	}
	
	@Override
	protected Map<String, String> extractFileNamesToContents() throws Exception {

		loadStringTemplateGroup("/resources/moca/templates/TiE/StartIntegrator.stg");

		Map<String, Object> attributes = extractTemplateParameters();

		String fileNamePrefix = project.getProjectRelativePath()
				+ "/src/org/moflon/tie/";

		// determine template for MocaMain (parser, unparser or both)
		String templateName = "StartIntegrator";

		Map<String, String> fileNameToContent = new HashMap<String, String>();
		fileNameToContent.put(fileNamePrefix + "StartIntegrator.java",
				renderTemplate(templateName, attributes));
	
		return fileNameToContent;
	}

	private void loadStringTemplateGroup(String path) {
		try {
			InputStreamReader reader = new InputStreamReader(
					getTemplateFileURL(path).openStream());
			stg = new StringTemplateGroup(reader);
		} catch (IOException e) {
			logger.debug("unable to load template file: "
					+ getTemplateFileURL(path));
		}
	}

	private URL getTemplateFileURL(String path) {
		return Activator.getPathRelToPlugIn(path, UIActivator.PLUGIN_ID);
	}

	private IFolder createFolder(String path) {
		IFolder folder = project.getFolder(path);
		if (!folder.exists()) {
			try {
				return WorkspaceHelper.addFolder(project, path, monitor);
			} catch (CoreException e) {
				logger.debug("error while creating folder: " + path);
				e.printStackTrace();
				return null;
			}
		} else {
			return folder;
		}
	}

	private String renderTemplate(String templateName,
			Map<String, Object> attributes) throws FileNotFoundException {
		StringTemplate st = stg.getInstanceOf(templateName);
		st.registerRenderer(String.class, new BasicFormatRenderer());
		st.setAttributes(attributes);
		return st.toString();
	}


	private Map<String, Object> extractTemplateParameters() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		ArrayList<String> projects = new ArrayList<String>();
		projects.add(project.getName());
		
		for (IProject projectOnBuildPath : WorkspaceHelper
				.getProjectsOnBuildPath(project)) {
			try {
				if (projectOnBuildPath.hasNature("org.moflon.ide.ui.runtime.natures.RepositoryNature"))
					projects.add(projectOnBuildPath.getName());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		attributes.put("projects", projects);
		return attributes;
	}


	

}
