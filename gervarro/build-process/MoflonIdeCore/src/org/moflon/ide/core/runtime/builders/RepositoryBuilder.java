package org.moflon.ide.core.runtime.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.moflon.properties.MoflonProperties;

public class RepositoryBuilder extends AbstractEcoreBuilder
{
	private static final Logger logger = Logger.getLogger(RepositoryBuilder.class);

	private final String[] supportedTracingMethods =
	{ "SDMTraceUtil.logPatternEnter", "SDMTraceUtil.logOperationEnter", "SDMTraceUtil.logOperationExit", "SDMTraceUtil.logBindObjVar",
			 "SDMTraceUtil.logObjDeletion", "SDMTraceUtil.logObjCreation", "SDMTraceUtil.logLinkCreation", "SDMTraceUtil.logLinkDeletion" };

	@Override
	protected boolean processResource(IProgressMonitor monitor) throws CoreException
	{
		boolean success = super.processResource(monitor);

		try
		{
			if (new MoflonProperties(getProject(), monitor).isGenTracingInstrumentation())
				createBreakPointsForSDMDebugging(getProject().getFolder("gen"));
		}
		catch (IOException e)
		{
			logger.error("Unable to create breakpoints: " + e);
		}

		return success;
	}

	protected void createBreakPointsForSDMDebugging(IResource resource)
	{
		try
		{
			if (resource instanceof IFile)
			{
				IFile file = (IFile) resource;
				if (file.getFileExtension().equals("java"))
					scanLinesAndMakeBreakPointsForFile(file);
			}
			else if (resource instanceof IFolder)
			{
				IFolder folder = (IFolder) resource;
				for (IResource member : folder.members())
					createBreakPointsForSDMDebugging(member);
			}
		}
		catch (CoreException | IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void scanLinesAndMakeBreakPointsForFile(IFile file) throws CoreException, IOException
	{
		List<String> lines = Files.readAllLines(Paths.get(file.getRawLocation().makeAbsolute().toOSString()), StandardCharsets.UTF_8);
		for (int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			if (lineIsRelevant(line))
			{
				ICompilationUnit javaType = (ICompilationUnit) JavaCore.create(file);
				JDIDebugModel.createLineBreakpoint(file, javaType.getTypes()[0].getFullyQualifiedName(), i+1, -1, -1, 0, true, null);
			}
		}
	}

	private boolean lineIsRelevant(String line)
	{
		line = line.trim();
		for (String supportedTracingMethod : supportedTracingMethods)
		{
			if (line.startsWith(supportedTracingMethod))
				return true;
		}

		return false;
	}
}
