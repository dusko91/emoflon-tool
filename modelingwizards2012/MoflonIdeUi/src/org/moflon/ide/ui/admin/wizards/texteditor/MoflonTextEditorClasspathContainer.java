package org.moflon.ide.ui.admin.wizards.texteditor;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.moflon.Activator;
import org.moflon.admin.BasicClasspathContainer;


public class MoflonTextEditorClasspathContainer extends BasicClasspathContainer{

	private static final String MOFLONTEXTEDITOR = "org.moflon.ide.TextEditor";


	public MoflonTextEditorClasspathContainer(IPath containerPath, IJavaProject project)
	{
		super(containerPath, project);
	}

	@Override
	public IClasspathEntry[] getClasspathEntries()
	{
		ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();

		// create CPE_LIBRARYs of type cp entry with an attached source archive if it exists

		URL moflonTextEditor = Activator.getPathRelToPlugIn("/", MOFLONTEXTEDITOR);
		entryList.add(getEntryFor(moflonTextEditor));

		// convert the list to an array and return it
		IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
		return (IClasspathEntry[])entryList.toArray(entryArray);
	}

	@Override
	public String getDescription()
	{
		return "Moflon Text Editor Container";
	}


}
