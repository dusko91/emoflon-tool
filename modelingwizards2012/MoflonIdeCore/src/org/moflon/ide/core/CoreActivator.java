package org.moflon.ide.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.moflon.Activator;
import org.moflon.ide.core.admin.MoflonProperties;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.util.MoflonUtil;
import org.osgi.framework.BundleContext;

import de.uni_paderborn.fujaba.versioning.Versioning;

/**
 * The Activator controls the plug-in life cycle and contains state and functionality that can be used throughout the
 * plugin. Constants used in various places in the plugin should also be defined in the Activator.
 * 
 * Core (non gui) functionality that can be useful for other Moflon eclipse plugins should be implemented here.
 * 
 * 
 * @author anjorin
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
public class CoreActivator extends Plugin
{
   private static final Logger logger = Logger.getLogger(CoreActivator.class);

   // Log file
   private static final String MOFLON_LOG = "moflon.log";

   // Log4J file
   private static final String LOG4J_CONFIG_PROPERTIES = "log4jConfig.properties";

   // Default resources path
   private static final String RESOURCES_DEFAULT_FILES_PATH = "resources/defaultFiles/";

   // Default projects path
   private static final String RESOURCES_DEFAULT_PROJECTS_PATH = "resources/defaultProjects/";

   // Name of project containing classes generated from templates used by JET
   private static final String JET_TEMPLATE_PROJECT = ".JETEmitters";

   // The plug-in ID
   public static final String PLUGIN_ID = "org.moflon.ide.core";

   // Nature and builder IDs
   public static final String REPOSITORY_NATURE_ID = "org.moflon.ide.ui.runtime.natures.RepositoryNature";

   public static final String REPOSITORY_BUILDER_ID = "org.moflon.ide.core.runtime.builders.RepositoryBuilder";

   public static final String METAMODEL_NATURE_ID = "org.moflon.ide.ui.runtime.natures.MetamodelNature";

   public static final String METAMODEL_BUILDER_ID = "org.moflon.ide.core.runtime.builders.MetamodelBuilder";

   public static final String INTEGRATION_NATURE_ID = "org.moflon.ide.ui.runtime.natures.IntegrationNature";

   public static final String INTEGRATION_BUILDER_ID = "org.moflon.ide.core.runtime.builders.IntegrationBuilder";
   
   public static final String MOSL_NATURE_ID = "org.moflon.ide.ui.runtime.natures.MOSLNature";
   
   public static final String MOSL_BUILDER_ID = "org.moflon.ide.core.runtime.builders.MOSLBuilder";
   
   public static final String VIEW_NATURE_ID = "org.moflon.ide.ui.runtime.natures.VTGGCompilerNature";
   
   public static final String VIEW_BUILDER_ID = "org.moflon.ide.core.runtime.builders.VTGGCompilerBuilder";
   
   public static final String JAVA_WORKING_SET_ID = "org.eclipse.jdt.ui.JavaWorkingSetPage";

   // The shared instance
   private static CoreActivator plugin;
   
   // The logfile used for plugin
   private File logFile;

   // The config file used for logging in plugin
   private File configFile;

   // Singleton instance
   public static CoreActivator getDefault()
   {
      return plugin;
   }

   @Override
   public void start(BundleContext context) throws Exception
   {
      super.start(context);
      plugin = this;

      // Configure logging for eMoflon
      setUpLogging();
      
      // Configure templates for EMF code generation with JET
      Job job1 = new Job("Moflon: Configuring templates") {
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            IStatus status = null;
            try
            {
               setUpCodegeneration(monitor);
               status = new Status(IStatus.OK, CoreActivator.PLUGIN_ID, IStatus.OK, "", null);
            } catch (Exception e)
            {
               status = new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, IStatus.OK, "", null);
               e.printStackTrace();
            }

            return status;
         }
      };
      job1.schedule();
      
      
      // Set up Fujaba Workspace
      Job job2 = new Job("Moflon: Setting up Fujaba Workspace") {
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            File fujabaWorkspace = getPathInStateLocation("FujabaWorkspace").toFile();

            if (!fujabaWorkspace.exists())
               fujabaWorkspace.mkdir();

            Versioning.initialize(fujabaWorkspace);

            return new Status(IStatus.OK, CoreActivator.PLUGIN_ID, IStatus.OK, "", null);
         }
      };
      job2.schedule();
      
      // Add mappings of all projects in Workspace for Ecore modelbrowser
      Job job3 = new Job("Moflon: Adding projects to registry"){
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
            {
               try
               {
                  if (project.isOpen() && (project.hasNature(REPOSITORY_NATURE_ID) || project.hasNature(INTEGRATION_NATURE_ID)))
                     addMappingForProject(project);
               } catch (CoreException e)
               {
                  e.printStackTrace();
                  return new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, IStatus.ERROR, "", null);
               }
            }

            return new Status(IStatus.OK, CoreActivator.PLUGIN_ID, IStatus.OK, "", null);
         }
         
      };
      job3.schedule();
   }

   private void setUpCodegeneration(IProgressMonitor monitor) throws CoreException, IOException, URISyntaxException, JETException
   {
      // Delete project in Workspace if necessary
      IProject jetProject = ResourcesPlugin.getWorkspace().getRoot().getProject(JET_TEMPLATE_PROJECT);
      jetProject.delete(true, true, monitor);

      // Copy default project into Workspace
      IPath pathToJetProject = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(jetProject.getFullPath());

      URL sourceDir = Activator.getPathRelToPlugIn(RESOURCES_DEFAULT_PROJECTS_PATH + JET_TEMPLATE_PROJECT, PLUGIN_ID);
      File destination = pathToJetProject.toFile();
      MoflonUtil.copyDirToDir(sourceDir, destination, new NotFileFilter(new NameFileFilter(new String[] { ".svn", ".cvs" })));

      // Refresh workspace and create project
      jetProject.create(monitor);
      jetProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
      jetProject.open(monitor);

      // Set classpath and build
      IJavaProject javaProject = JavaCore.create(jetProject);

      Collection<IClasspathEntry> classpathEntries = new HashSet<IClasspathEntry>();

      classpathEntries.addAll(Arrays.asList(javaProject.getRawClasspath()));

      CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_COMMON", "org.eclipse.emf.common");
      CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_ECORE", "org.eclipse.emf.ecore");
      CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_CODEGEN", "org.eclipse.emf.codegen");
      CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_CODEGEN_ECORE", "org.eclipse.emf.codegen.ecore");

      WorkspaceHelper.setContainerOnBuildPath(classpathEntries, "org.moflon.ide.MOFLON_CONTAINER");

      javaProject.setRawClasspath(classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]), new SubProgressMonitor(monitor, 1));

      jetProject.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
   }

   @Override
   public void stop(BundleContext context) throws Exception
   {
      plugin = null;
      super.stop(context);
   }

   /**
    * Used when the plugin has to store resources on the client machine and eclipse installation + current workspace.
    * This location reserved for the plugin is called the "state location" and is usually in
    * pathToWorkspace/.metadata/pluginName
    * 
    * @param filename
    *           Appended to the state location. This is the name of the resource to be saved.
    * @return path to location reserved for the plugin which can be used to store resources
    */
   public IPath getPathInStateLocation(String filename)
   {
      return getStateLocation().append(filename);
   }

   /**
    * Initialize log and configuration file. Configuration file is created with default contents if necessary. Log4J is
    * setup properly and configured with a console and logfile appender.
    */
   private void setUpLogging()
   {
      // Create logFile in storage space for plugin
      logFile = getPathInStateLocation(MOFLON_LOG).toFile();

      // Create configFile if necessary also in plugin storage space
      configFile = getPathInStateLocation(LOG4J_CONFIG_PROPERTIES).toFile();

      if (!configFile.exists())
      {
         try
         {
            // Copy default configuration to state location
            URL defaultConfigFile = Activator.getPathRelToPlugIn(RESOURCES_DEFAULT_FILES_PATH + LOG4J_CONFIG_PROPERTIES, PLUGIN_ID);

            FileUtils.copyURLToFile(defaultConfigFile, configFile);
         } catch (Exception e)
         {
            logger.error("Unable to open default config file.");
            e.printStackTrace();
         }
      }

      // Configure Log4J
      reconfigureLogging();
   }

   /**
    * Call to ensure that log4j is setup properly and configured. Can be called multiple times. Forces Plugin to be
    * loaded and started, ensuring that log file and config file exist.
    */
   public void reconfigureLogging()
   {
      try
      {
         MoflonUtil.configureLogging(configFile.toURI().toURL(), logFile.getAbsolutePath());
      } catch (MalformedURLException e)
      {
         logger.error("URL to configFile is malformed: " + configFile);
         e.printStackTrace();
      }
   }

   /**
    * @return Log file in state location of client ($workspace/.metadata/.plugins/org.moflon.ide.core/moflon.log)
    */
   public File getLogFile()
   {
      return logFile;
   }

   /**
    * @return Config file in state location of client (usually
    *         $workspace/.metadata/.plugins/org.moflon.ide.core/log4jConfig.properties)
    */
   public File getConfigFile()
   {
      return configFile;
   }

   public static void addMappingForProject(IProject project) throws CoreException
   {
      URI moflonURI = URI.createURI(MoflonUtil.getMoflonDefaultURIForProject(project.getName()));
      String ending = WorkspaceHelper.ECORE_FILE_EXTENSION;

      // For integration projects map to .gen.ecore file
      if (project.hasNature(INTEGRATION_NATURE_ID))
         ending = ".gen" + ending;

      // If project is part of Moflon there already exists a mapping from plugin so skip
      if(new MoflonProperties(project, new NullProgressMonitor()).isCoreProject())
         return;
      
      
      // Map moflon uris to platform uris of projects in workspace
      URI platformURI = URI.createPlatformResourceURI(project.getName() + "/model/" + project.getName() + ending, true);
      URIConverter.URI_MAP.put(moflonURI, platformURI);
   }
}
