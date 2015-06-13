package org.moflon.autotest.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.moflon.Activator;
import org.moflon.autotest.AutoTestActivator;

public class EnterpriseArchitectHelper {

	private static final String COMMAND_LINE_EA_EXPORT = "/resources/commandLineEAExport/MOFLON2EAExportImportTest.exe";

	public static void delegateToEnterpriseArchitect(IProject project)
			throws IOException, InterruptedException {
		URL pathToExe = Activator.getPathRelToPlugIn(COMMAND_LINE_EA_EXPORT,
				AutoTestActivator.PLUGIN_ID);
		IFile eap = project.getFile(project.getName().concat(".eap"));
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("\""
				+ new File(pathToExe.getPath()).getAbsolutePath() + "\""
				+ " -e --eap " + "\"" + eap.getLocation() + "\"");
		pr.waitFor();
	}

}
