package org.moflon.util.plugins;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.moflon.MoflonDependenciesPlugin;
import org.moflon.util.WorkspaceHelper;

public class BuildPropertiesFileBuilder {

	private static final String BUILD_PROPERTIES_NAME = "build.properties";

	public void createBuildProperties(final IProject currentProject, final IProgressMonitor monitor)
			throws CoreException {
		monitor.beginTask("Creating build.properties", 2);

		IFile file = getBuildPropertiesFile(currentProject);
		if (file.exists()) {
			// Do not touch existing build.properties
			monitor.worked(2);
			monitor.done();
			return;
		}

		Properties buildProperties = new Properties();
		buildProperties.put("bin.includes", "META-INF/,bin/,model/,injection/,plugin.xml");
		buildProperties.put("source..", "src/,gen/");
		buildProperties.put("output..", "bin/");
		buildProperties.put("source.excludes", "test/");

		monitor.worked(1);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			buildProperties.store(stream, "");
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, MoflonDependenciesPlugin.PLUGIN_ID,
					"Error while creating build.properties: " + e.getMessage()));
		}

		if (!file.exists()) {
			WorkspaceHelper.addFile(currentProject, BUILD_PROPERTIES_NAME, stream.toString(),
					WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		} else {
			file.setContents(new ByteArrayInputStream(stream.toByteArray()), true, true,
					WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		}

		monitor.done();
	}

	public IFile getBuildPropertiesFile(final IProject currentProject) {
		return currentProject.getFile(BUILD_PROPERTIES_NAME);
	}

}
