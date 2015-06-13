package org.moflon.ide.texteditor.wizards;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.text.rules.ITokenScanner;

import org.eclipse.emf.ecore.*;
import org.moflon.ide.texteditor.editors.MoflonTextEditorConfiguration;

public class TextEditorConfigurationClassLoader {

	
	public MoflonTextEditorConfiguration loadClass(IProject selectedProject){
		
		IJavaProject javaProject = JavaCore.create(selectedProject);

		String[] classPathEntries = null;
		try {
			classPathEntries = JavaRuntime
					.computeDefaultRuntimeClassPath(javaProject);
		} catch (CoreException e2) {
			e2.printStackTrace();
		}

		List<URL> urlList = new ArrayList<URL>();
		for (int i = 0; i < classPathEntries.length; i++) {
			String entry = classPathEntries[i];
			IPath path = new Path(entry);
			URL url = null;
			try {
				url = path.toFile().toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			urlList.add(url);
		}

		URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
		//URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
		URLClassLoader classLoader = new URLClassLoader(urls, EcorePackage.eINSTANCE.getClass().getClassLoader());
		
		Class<?> clazz = null;
		

		// Convention, class name is "project name" + "Editor" and the class is
		// in
		// the directory src/org/moflon/moca/
		try {
			clazz = classLoader.loadClass("org.moflon.texteditor."
					+ selectedProject.getName() + "TextEditorConfiguration");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
		
		Object result = null;
		try {
			result = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return (MoflonTextEditorConfiguration) result;
	}
}
