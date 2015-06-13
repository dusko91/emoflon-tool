package org.moflon.ide.ui.admin.wizards.texteditor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.moflon.Activator;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.ui.UIActivator;
import org.moflon.ide.ui.admin.wizards.AbstractFileGenerator;
import org.moflon.ide.ui.admin.wizards.moca.BasicFormatRenderer;

/**
 * generates a java class in the directory src/org/moflon/moca/ for setting text
 * editor properties
 * 
 * @author Amir Naseri
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */

public class TextEditorConfigurationClassGenerator extends
AbstractFileGenerator {

	private static Logger logger = Logger.getLogger(UIActivator.class);

	private StringTemplateGroup stg;

	private String packagePrefix;

	public TextEditorConfigurationClassGenerator(IProject project)  {
		super(project);
		this.packagePrefix = "org.moflon.texteditor";


		//Set classpath
		try {
			IJavaProject javaProject = JavaCore.create(project);
			Collection<IClasspathEntry> classpathEntries = new HashSet<IClasspathEntry>();
			classpathEntries.addAll(Arrays.asList(javaProject.getRawClasspath()));			
			CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_TEXT", "org.eclipse.text");
			CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_JFACE_TEXT", "org.eclipse.jface.text");
			WorkspaceHelper.setContainerOnBuildPath(classpathEntries, "org.moflon.ide.EDITOR_CONTAINER");
			javaProject.setRawClasspath(classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]), new SubProgressMonitor(monitor, 1));
		} catch (Exception e) {
			logger.debug(project.getName() + " does not appear to be a Java project...");
		}

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
	
	private String getConfigFileName(){
	   return "/src/org/moflon/texteditor/" + project.getName() + "TextEditorConfiguration.java";
	}
	
	public boolean configExists(){
	   return project.getFile(getConfigFileName()).exists();
	}

	@Override
	protected Map<String, String> extractFileNamesToContents() throws Exception {

		loadStringTemplateGroup("/resources/moca/templates/texteditor/TextEditorConfiguration.stg");

		Map<String, Object> attributes = extractTemplateParameters();


		// determine template for MocaMain (parser, unparser or both)
		String templateName = "TextEditorConfiguration";

		Map<String, String> fileNameToContent = new HashMap<String, String>();
		fileNameToContent.put(getConfigFileName(), renderTemplate(templateName, attributes));

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
		attributes.put("project", project.getName());
		return attributes;
	}

	@Override
	public String doFinish() throws Exception {
		String message = super.doFinish();
		project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
		return message;
	}
}
