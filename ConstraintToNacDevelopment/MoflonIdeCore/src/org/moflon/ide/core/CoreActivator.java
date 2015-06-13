package org.moflon.ide.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jdt.core.JavaCore;
import org.moflon.Activator;
import org.moflon.ide.core.console.MoflonConsole;
import org.moflon.properties.MoflonProperties;
import org.moflon.util.MoflonUtil;
import org.moflon.util.WorkspaceHelper;
import org.osgi.framework.BundleContext;

import de.uni_paderborn.fujaba.versioning.Versioning;

/**
 * The Activator controls the plug-in life cycle and contains state and functionality that can be used throughout the
 * plugin. Constants used in various places in the plugin should also be defined in the Activator.
 * 
 * Core (non gui) functionality that can be useful for other Moflon eclipse plugins should be implemented here.
 * 
 * 
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
public class CoreActivator extends Plugin
{
   private static final Logger logger = Logger.getLogger(CoreActivator.class);
   
   // Log4J file
   private static final String LOG4J_CONFIG_PROPERTIES = "log4jConfig.properties";

   // Default resources path
   private static final String RESOURCES_DEFAULT_FILES_PATH = "resources/defaultFiles/";

   // The plug-in ID
   public static final String PLUGIN_ID = "org.moflon.ide.core";

   // Nature and builder IDs
   @Deprecated
   public static final String JAVA_NATURE_ID = JavaCore.NATURE_ID;

   public static final String REPOSITORY_BUILDER_ID = "org.moflon.ide.core.runtime.builders.RepositoryBuilder";

   public static final String METAMODEL_NATURE_ID = "org.moflon.ide.ui.runtime.natures.MetamodelNature";

   public static final String METAMODEL_BUILDER_ID = "org.moflon.ide.core.runtime.builders.MetamodelBuilder";

   public static final String INTEGRATION_BUILDER_ID = "org.moflon.ide.core.runtime.builders.IntegrationBuilder";

   public static final String MOSL_NATURE_ID = "org.moflon.ide.ui.runtime.natures.MOSLNature";

   public static final String MOSL_BUILDER_ID = "org.moflon.ide.core.runtime.builders.MOSLBuilder";

   public static final String VIEW_NATURE_ID = "org.moflon.ide.ui.runtime.natures.VTGGCompilerNature";

   public static final String VIEW_BUILDER_ID = "org.moflon.ide.core.runtime.builders.ViewIntegrationBuilder";

   public static final String ANTLR_NATURE_ID = "org.moflon.ide.ui.runtime.natures.AntlrNature";

   public static final String ANTLR_BUILDER_ID = "org.moflon.ide.core.runtime.builders.AntlrBuilder";

   public static final String JAVA_WORKING_SET_ID = "org.eclipse.jdt.ui.JavaWorkingSetPage";

   public static final String TESTFRAMEWORK_NATURE_ID = "org.moflon.ide.ui.runtime.natures.TestframeworkNature";

   public static final String TESTFRAMEWORK_BUILDER_ID = "org.moflon.ide.core.runtime.builders.TestframeworkBuilder";

   public static final String MOFLON_CLASSPATH_CONTAINER_ID = "org.moflon.ide.MOFLON_CONTAINER";

   public static final String MOSL_CLASSPATH_CONTAINER_ID = "org.moflon.ide.MOSL";

   public static final String TGG_TESTGEN_CLASSPATH_CONTAINER_ID = "org.moflon.ide.TGG_TESTGEN_CONTAINER";   
   
   // The shared instance
   private static CoreActivator plugin;

   // The config file used for logging in plugin
   private File configFile;

   private Map<String, Boolean> isMoslDirty = new HashMap<>();

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

      // Set up Fujaba Workspace
      Job job1 = new Job("Moflon: Setting up Fujaba Workspace") {
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            File fujabaWorkspace = getPathInStateLocation("FujabaWorkspace").toFile();

            if (!fujabaWorkspace.exists())
               fujabaWorkspace.mkdir();
            else
               try
               {
                  FileUtils.cleanDirectory(fujabaWorkspace);
               } catch (IOException e)
               {
                  logger.error("Unable to clean FujabaWorkspace!");
               }

            Versioning.initialize(fujabaWorkspace);

            return new Status(IStatus.OK, CoreActivator.PLUGIN_ID, IStatus.OK, "", null);
         }
      };
      job1.schedule();

      // Add mappings of all projects in Workspace for Ecore modelbrowser
      Job job2 = new Job("Moflon: Adding projects to registry") {
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
            {
               try
               {
                  if (project.isOpen() && WorkspaceHelper.isMoflonProject(project))
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
      job2.schedule();
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
         configureLogging(configFile.toURI().toURL());
      } catch (MalformedURLException e)
      {
         logger.error("URL to configFile is malformed: " + configFile);
         e.printStackTrace();
      }
   }
   
   /**
    * Set up logging globally
    * 
    * @param configFile
    *           URL to log4j property configuration file
    */
   public static boolean configureLogging(URL configFile)
   {
      try
      {
         Logger root = Logger.getRootLogger();
         String configurationStatus = "";
         if (configFile != null)
         {
            // Configure system using config
            PropertyConfigurator.configure(configFile);
            configurationStatus = "Log4j successfully configured using " + configFile;
         } else
         {
            configurationStatus = "Set up logging without config file!";
         }

         // Set format and scheme for output in logfile
         Logger.getRootLogger().addAppender(new MoflonConsole());
         
         // Indicate success
         root.info(configurationStatus);
         root.info("Logging to eMoflon console");
         return true;
      } catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
   }

   /**
    * @return Config file in state location of client (usually
    *         $workspace/.metadata/.plugins/org.moflon.ide.core/log4jConfig.properties)
    */
   public File getConfigFile()
   {
      return configFile;
   }

   public boolean isMoslDirty(String project)
   {
      return isMoslDirty.containsKey(project) ? isMoslDirty.get(project) : true;
   }

   public void setMoslDirty(String project, boolean isMoslDirty)
   {
      this.isMoslDirty.put(project, isMoslDirty);
   }

   public static void addMappingForProject(IProject project) throws CoreException
   {
      URI moflonURI = URI.createURI(MoflonUtil.getMoflonDefaultURIForProject(project.getName()));
      String ending = WorkspaceHelper.ECORE_FILE_EXTENSION;

      // For integration projects map to .gen.ecore file
      if (project.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID))
         ending = ".gen" + ending;

      // If project is part of Moflon there already exists a mapping from plugin so skip
      if (new MoflonProperties(project, new NullProgressMonitor()).isCoreProject())
         return;

      // Map moflon uris to platform uris of projects in workspace
      URI platformURI = URI.createPlatformResourceURI(project.getName() + "/model/" + project.getName() + ending, true);
      URIConverter.URI_MAP.put(moflonURI, platformURI);
   }
}
