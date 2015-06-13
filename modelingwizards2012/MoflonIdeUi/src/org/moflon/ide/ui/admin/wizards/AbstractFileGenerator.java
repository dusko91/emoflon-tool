package org.moflon.ide.ui.admin.wizards;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.moflon.ide.core.admin.WorkspaceHelper;

public abstract class AbstractFileGenerator {
	
	protected String message = "";
	
	protected IProject project;
	
	protected IProgressMonitor monitor;
	
	public AbstractFileGenerator(IProject project){
		this.project = project;
		this.monitor = new NullProgressMonitor();
	}
	
	public String doFinish() throws Exception{	
		prepareCodegen();
		generate();
		return message;
	}
	
	protected void generate() throws Exception, CoreException{
		
		Map<String, String> fileNamesToContents = extractFileNamesToContents();
		for(String fileName : fileNamesToContents.keySet()){
			String content = fileNamesToContents.get(fileName);
			addFileAndCheckSuccess(fileName, content);
		}
	}
	protected void addFileAndCheckSuccess(String fileName, String content)
			throws CoreException {
		if (!addFile(fileName, content)) {
			message += fileName + " already exists! \n"
					+ "Please don't forget to edit appropriately ... \n";
		}
	}

	protected boolean addFile(String fileName, String content)
			throws CoreException {
		IFile file = project.getFile(fileName);
		if (!file.exists()) {
			WorkspaceHelper.addFile(project, fileName, content, monitor);
			return true;
		} else {
			return false;
		}
	}
	
	abstract protected void prepareCodegen();
	abstract protected Map<String, String> extractFileNamesToContents() throws Exception;
}
