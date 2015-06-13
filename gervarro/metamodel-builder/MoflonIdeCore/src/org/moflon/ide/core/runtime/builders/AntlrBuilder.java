package org.moflon.ide.core.runtime.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.moflon.MoflonDependenciesPlugin;
import org.moflon.ide.core.CoreActivator;
import org.moflon.util.AntlrUtil;

public class AntlrBuilder extends AbstractBuilder
{
   private static final boolean DEBUG = false;

   private static final String EXTENSION = ".g";

   private static final Logger logger = Logger.getLogger(AntlrBuilder.class);

   private static final String MARKER = "org.moflon.ide.AntlrEditorProblem";

   private static final Pattern ERROR_REGEX = Pattern.compile("error\\((\\d+)\\):\\s+(.+):(\\d+):(\\d+): (.+)");

   private static final Pattern WARNING_REGEX = Pattern.compile("warning\\((\\d+)\\):\\s+(.+):(\\d+):(\\d+): (.+)");

   private static final Pattern ANTLR_FILENAME_PATTERN = Pattern.compile("(.*)((?:Lexer)|(?:Parser)).g");

   private static final long TIME_OUT = 60000;

   private List<String> builtParsers = new ArrayList<String>();

   @Override
   protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException
   {
      builtParsers.clear();

      return super.build(kind, args, monitor);
   }

   @Override
   public boolean visit(IResource resource) throws CoreException
   {
      processAntlrResource(resource);
      return true;
   }

   @Override
   public boolean visit(IResourceDelta delta) throws CoreException
   {
      if (delta.getResource().exists())
         processAntlrResource(delta.getResource());

      return true;
   }

   private boolean processAntlrResource(IResource resource) throws CoreException
   {
      if (resource.getName().endsWith(EXTENSION))
      {
         debug("AntlrBuilder: Processing Antlr Resource: " + resource.getName());
         if (resource.getFullPath().toString().contains("/bin/"))
         {
            debug("Skipping file because of bin location.");
            return true;
         }

         Matcher m = ANTLR_FILENAME_PATTERN.matcher(resource.getName());
         String prefix = null;
         String type = null;

         if (m.matches())
         {
            prefix = m.group(1);
            type = m.group(2);
         }

         if (prefix != null)
         {
            if (builtParsers.contains(prefix))
            {
               debug("Skipping file because we already built it.");
               return true;
            } else
            {
               builtParsers.add(prefix);
            }
         }

         try
         {
            // If parser then make sure lexer is built first
            if ("Parser".equals(type))
               compileAntlrResource(resource.getParent().findMember(prefix + "Lexer.g"));

            compileAntlrResource(resource);

            // If lexer then refresh parser
            if ("Lexer".equals(type))
               compileAntlrResource(resource.getParent().findMember(prefix + "Parser.g"));
         } catch (URISyntaxException e)
         {
            e.printStackTrace();
            MoflonDependenciesPlugin.throwCoreExceptionAsError(e.getMessage(), CoreActivator.PLUGIN_ID, e);
         }

      }
      return true;
   }

   private void compileAntlrResource(IResource resource) throws CoreException, URISyntaxException
   {
      logger.debug(new Date().toString());
      logger.debug("Processing Antlr Resource: " + resource.getName());

      resource.deleteMarkers(MARKER, false, IResource.DEPTH_ZERO);

      String javaHome = System.getProperty("java.home");
      IPath javaHomePath = new Path(javaHome);
      IPath javaPath = javaHomePath.append("bin").append("java");

      String antlrPath = URIUtil.toFile(URIUtil.toURI(AntlrUtil.getAntrlPathUrl())).getAbsolutePath();

      debug("Found java path: " + javaPath.toOSString());
      debug("Found antlr.jar: " + antlrPath);

      List<String> command = new ArrayList<String>();
      command.add(javaPath.toOSString());
      command.add("-jar");
      command.add(antlrPath);
      command.add("-o");
      command.add(resource.getParent().getRawLocation().toOSString());
      command.add(resource.getRawLocation().toOSString());

      ProcessBuilder processBuilder = new ProcessBuilder(command);
      processBuilder.redirectErrorStream(true);

      Process process = null;

      try
      {
         process = processBuilder.start();

         int res = executeCommandLine(TIME_OUT, command, process, resource);

         if (res == 0)
         {
            logger.debug("Success.");
         } else
         {
            logger.debug("Error: " + res);
         }
      } catch (IOException e)
      {
         logger.debug("Error while executing command: " + e.getMessage());
         logger.error("Error while executing command", e);
      } catch (InterruptedException e)
      {
         logger.error("Interrupted command: " + e.getMessage());
      } catch (TimeoutException e)
      {
         logger.error("Timeout (60s) reached.");
      }

      this.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
   }

   private void debug(String message)
   {
      if (DEBUG)
         logger.debug("DEBUG: " + message);
   }

   @Override
   protected boolean processResource(IProgressMonitor monitor) throws CoreException
   {
      logger.debug("Process resource.");
      getProject().accept(this);

      monitor.done();
      return true;
   }

   @Override
   protected void cleanResource(IProgressMonitor monitor) throws CoreException
   {
      cleanDirectory(getProject(), monitor);
   }

   private void cleanDirectory(IContainer container, IProgressMonitor monitor) throws CoreException
   {
      Pattern antlrFilePattern = Pattern.compile("(.+)\\.g");
      for (IResource res : container.members())
      {
         if (res.getType() == IResource.FILE)
         {
            Matcher m = antlrFilePattern.matcher(res.getName());
            if (m.matches())
            {
               deleteResource(container, m.group(1) + ".tokens", monitor);
               deleteResource(container, m.group(1) + ".java", monitor);
            }
         }
         if (res.getType() == IResource.FOLDER)
         {
            cleanDirectory((IFolder) res, monitor);
         }
      }

   }

   private void deleteResource(IContainer container, String string, IProgressMonitor monitor) throws CoreException
   {
      System.err.println("Removing file '" + string + "'");
      logger.debug("Removing file '" + string + "'");
      IResource res = container.findMember(string);
      if (res != null && res.exists())
      {
         res.delete(true, monitor);
      }
   }

   public static int executeCommandLine(final long timeout, final List<String> command, Process process, IResource resource) throws IOException,
         InterruptedException, TimeoutException
   {
      Worker worker = new Worker(process, resource);
      worker.start();
      try
      {
         worker.join(timeout);
         if (worker.exit != null)
            return worker.exit;
         else
            throw new TimeoutException();
      } catch (InterruptedException ex)
      {
         worker.interrupt();
         Thread.currentThread().interrupt();
         throw ex;
      } finally
      {
         process.destroy();
      }
   }

   private static class Worker extends Thread
   {
      private final Process process;

      private final IResource resource;

      private Integer exit;

      private Worker(Process process, IResource resource)
      {
         this.process = process;
         this.resource = resource;
      }

      public void run()
      {
         try
         {
            exit = process.waitFor();

            printAntlrMessages(resource, process);
         } catch (InterruptedException ignore)
         {
            return;
         } catch (CoreException e)
         {
            return;
         }
      }

      private void createMarker(IResource resource, int lineNumber, String message, int severity) throws CoreException
      {
         IMarker m = resource.createMarker(MARKER);
         if (lineNumber > 0)
         {
            m.setAttribute(IMarker.LINE_NUMBER, lineNumber);
         }
         m.setAttribute(IMarker.MESSAGE, message);
         m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
         m.setAttribute(IMarker.SEVERITY, severity);
      }

      private void printAntlrMessages(IResource resource, Process process) throws CoreException
      {
         BufferedReader bis = null;
         try
         {
            bis = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bis.readLine()) != null)
            {

               Matcher m = ERROR_REGEX.matcher(line);
               if (m.matches())
               {
                  createMarker(resource, Integer.parseInt(m.group(3)), m.group(5), IMarker.SEVERITY_ERROR);
                  logger.error(">> " + m.group(5));
               } else
               {
                  m = WARNING_REGEX.matcher(line);
                  if (m.matches())
                  {
                     createMarker(resource, Integer.parseInt(m.group(3)), m.group(5), IMarker.SEVERITY_WARNING);
                     logger.error(">> " + m.group(5));
                  } else
                     logger.error(">> " + line);
               }
            }

         } catch (IOException ioe)
         {
            logger.error("Error while reading stream: " + ioe.getMessage());
            logger.error("Error while reading stream", ioe);
         } finally
         {
            try
            {
               if (bis != null)
                  bis.close();
            } catch (IOException e)
            {
               // Do nothing
            }
         }
      }
   }
}
