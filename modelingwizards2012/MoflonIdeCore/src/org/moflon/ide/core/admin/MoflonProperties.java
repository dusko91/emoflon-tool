package org.moflon.ide.core.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.moflon.Activator;
import org.moflon.ide.core.CoreActivator;

public class MoflonProperties
{
   // Used to identify projects that are part of moflon
   private static final String CORE = "CORE";
   
   // Used to preserve genModels that have been customized
   private static final String REPLACE_GENMODEL = "REPLACE_GENMODEL";
   
   // Generate debug data or not
   private static final String DEBUG_MODE = "DEBUG_MODE";
   
   // List of paths to be preserved on cleaning
   private static final String PRESERVE_PATHS = "PRESERVE_PATHS";

   // Reference to project containing metamodel (EAP) necessary for validation
   public static final String METAMODEL_PROJECT = "METAMODEL_PROJECT";
   
   // Used when working with external non-moflon EMF projects (which don't follow our URI and naming conventions)
   private static final String ADDITIONAL_USED_GEN_PACKAGES = "ADDITIONAL_USED_GEN_PACKAGES";
   private static final String ADDITIONAL_DEPENDENCIES = "ADDITIONAL_DEPENDENCIES";
   private static final String IMPORT_MAPPINGS = "IMPORT_MAPPINGS";
   
   // Turn on/off tracing 
   private static final String GEN_TRACING_INSTRUMENTATION = "GEN_TRACING_INSTRUMENTATION";

   
   /* ........................................... */
   
   
   private static Logger logger = Logger.getLogger(CoreActivator.class);

   // Name of property file
   private static final String MOFLON_CONFIG_FILE = "moflon.properties";

   // Path to default property file
   private static final String PATH_TO_DEFAULT_MOFLON_CONFIG_FILE = "resources/defaultFiles/" + MOFLON_CONFIG_FILE;
   private static final String SKIP_VALIDATION = "SKIP_VALIDATION";

   // Additional used gen packages
   
   // Property file
   private MoflonPropertyFile properties;

   // Current project
   private IProject project;

   // Monitor to indicate progress
   private IProgressMonitor monitor;

   // Load property file if present in project or create default
   private MoflonPropertyFile loadPropertyFile()
   {
      try
      {
         IFile propertiesFile = project.getFile(MOFLON_CONFIG_FILE);

         // Create default property file if necessary
         if (!propertiesFile.exists())
         {
            URL source = Activator.getPathRelToPlugIn(PATH_TO_DEFAULT_MOFLON_CONFIG_FILE, CoreActivator.PLUGIN_ID);
            propertiesFile.create(source.openStream(), true, monitor);
         }

         // Load property file in project
         properties = new MoflonPropertyFile();
         InputStream contents = propertiesFile.getContents(true);
         properties.load(contents);
         contents.close();
      } catch (Exception e)
      {
         e.printStackTrace();
         logger.error("Unable to load property file for " + project.getName());
      }

      return properties;
   }

   private class MoflonPropertyFile extends Properties
   {
      private static final long serialVersionUID = 2950712749113368739L;

      /**
       * Returns the Value as boolean
       * 
       * @param name
       * @return the boolean representation of the value of the referenced property or <b>false</b> if the property was not found  
       */
      public boolean getValueAsBoolean(String name)
      {
         if (!containsKey(name))
            return false;
         
         return Boolean.parseBoolean(getProperty(name));
      }

      /**
       * Returns the value of the name
       * 
       * @param name
       * @return if the key does't contain in the property, null is returned
       */
      public String getValueAsString(String name)
      {
         return getProperty(name, "");
      }

      /**
       * Returns an integer value of the value that is given by the key name
       * 
       * @param name
       * @return if the value is not a integer value -1 is returned (Number format exception )
       * @throws NullPointerException
       *            if the name is null
       */
      @SuppressWarnings("unused")
      public int getValueAsInt(String name)
      {
         String value = getProperty(name);
         return (value == null) ? -1 : Integer.parseInt(value);
      }

      /**
       * Returns an integer value of the value that is given by the key name
       * 
       * @param name
       * @return if the value is not a integer value -1 is returned (Number format exception )
       * @throws NullPointerException
       *            if the name is null
       */
      @SuppressWarnings("unused")
      public double getValueAsDouble(String name)
      {
    	 String value = getProperty(name);
         return (value == null) ? -1.0 : Double.parseDouble(value);
      }

      /**
       * Returns the value of the key name as vector. The given String is split by the charactar ';' and the array is
       * added to the vector
       * 
       * @param name
       * @return
       * @throws NullPointerException
       */
      public Collection<String> getValueAsVector(String name)
      {
         Collection<String> out = new Vector<String>();
         String va = getProperty(name);
         if (va != null){
            String[] split = va.split(";");
            for (String act : split)
               out.add(act);
         }
         
         return out;
      }

      /**
       * Saves the value by the key in the properties
       * 
       * @param key
       * @param value
       */
      @SuppressWarnings("unused")
      public void setPropertyAsBoolean(String key, boolean value)
      {
         setProperty(key, Boolean.toString(value));
      }

      /**
       * Saves the value by the key in the properties
       * 
       * @param key
       * @param value
       */
      public void setPropertyAsString(String key, String value)
      {
         setProperty(key, value);
      }

      /**
       * Saves the value by the key in the properties
       * 
       * @param key
       * @param value
       */
      @SuppressWarnings("unused")
      public void setPropertyAsInt(String key, int value)
      {
         setProperty(key, String.valueOf(value));
      }

      /**
       * Saves the value by the key in the properties
       * 
       * @param key
       * @param value
       */
      @SuppressWarnings("unused")
      public void setPropertyAsDouble(String key, double value)
      {
         setProperty(key, String.valueOf(value));
      }

      /**
       * Saves the value by the key in the properties. The strings that is saved is seperatet by the charactar ';'.
       * 
       * @param key
       * @param value
       * @throws NullPointerException
       */
      @SuppressWarnings("unused")
      public void setPropertyAsVector(String key, Vector<String> value)
      {
         String val = "";
         for (String act : value)
            val += act + ";";

         setProperty(key, val.substring(0, val.length() - 1));
      }
   }

   /* Interface for clients */

   /**
    * Loads all properties defined for the specified project. If there is no user defined properties file a default
    * property file is created using a template from the plugin.
    * 
    * Progress is indicated using monitor.
    * 
    * Clients should create a new instance of this class every time changes to the property file are possible or
    * expected.
    */
   public MoflonProperties(IProject project, IProgressMonitor monitor)
   {
      this.project = project;
      this.monitor = monitor;

      monitor.beginTask("", 1 * WorkspaceHelper.PROGRESS_SCALE);
      properties = loadPropertyFile();
      monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);
      monitor.done();
   }

   public void setMetamodelProject(IProject metamodelProject){
      properties.setPropertyAsString(METAMODEL_PROJECT, metamodelProject.getName());
   }
   
   public IProject getMetamodelProject() throws CoreException{
      String metamodelProject = properties.getValueAsString(METAMODEL_PROJECT);
      
      if(metamodelProject == null){
         IStatus status = new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, IStatus.OK, 
               "Moflon property file is missing entry for metamodel!", null);
         throw new CoreException(status); 
      }
      
      return ResourcesPlugin.getWorkspace().getRoot().getProject(metamodelProject);
   }
   
   public boolean getReplaceGenModel()
   {
      return properties.getValueAsBoolean(REPLACE_GENMODEL);
   }

   public boolean debugMode()
   {
      return properties.getValueAsBoolean(DEBUG_MODE);
   }
   
   public boolean isGenTracingInstrumentation()
   {
	   return properties.getValueAsBoolean(GEN_TRACING_INSTRUMENTATION);
   }

   public boolean isCoreProject()
   {
      return properties.getValueAsBoolean(CORE);
   }
   
   public Collection<String> getPreservePaths(){
      return properties.getValueAsVector(PRESERVE_PATHS);
   }
   
   public void addToPreservePath(String path){
      String paths = properties.getValueAsString(PRESERVE_PATHS);
      Boolean pathExists = false;
      String[] pathArray = paths.split(";");
      for (String cpath : pathArray)
      {
         if(cpath.trim().equals(path.trim()))
            pathExists = true;
      }
      if(!pathExists){
         if(!paths.endsWith(";") && !pathArray[0].equals("")){
            paths= paths.concat(";");
         }
         paths = paths.concat(path);
      }
      properties.setPropertyAsString(PRESERVE_PATHS, paths);
      try
      {
         persistProperties();
      } catch (CoreException e)
      {
         e.printStackTrace();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   public void removeFromPreservePath(String path){
      String paths = properties.getValueAsString(PRESERVE_PATHS);
      Boolean pathChanged = false;
      String[] pathArray = paths.split(";");

      ArrayList<Integer> toDelete = new ArrayList<Integer>(); 
      for (int i = 0; i < pathArray.length;i++)
      {
         String cpath = pathArray[i];
         if(cpath.trim().equals(path.trim())){
            toDelete.add(i);
            pathChanged= true;
         }
      }
      
      
      if(pathChanged){
         String newPathList ="";
         int i = 0;
         for (String currpath : pathArray)
         {
            if(!toDelete.contains(i)){
               newPathList = newPathList.concat(currpath).concat(";");
            }
            i++;
         }
         properties.setPropertyAsString(PRESERVE_PATHS, newPathList);
      }
      try
      {
         persistProperties();
      } catch (CoreException e)
      {
         e.printStackTrace();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   public void persistProperties() throws CoreException, IOException {
      StringBuilderWriter sbw = new StringBuilderWriter();
      properties.store(sbw, "Moflon Property File");
      project.getFile(MOFLON_CONFIG_FILE).setContents(new ByteArrayInputStream(sbw.toString().getBytes()), true, true, new NullProgressMonitor());
   }
   
   public Collection<String> getAdditionalUsedGenPackage(){
      return properties.getValueAsVector(ADDITIONAL_USED_GEN_PACKAGES);
   }

   public Collection<String> getAdditionalDependencies()
   {
      return properties.getValueAsVector(ADDITIONAL_DEPENDENCIES);
   }

   public boolean skipValidation()
   {
      return properties.getValueAsBoolean(SKIP_VALIDATION);
   }
   
   public HashMap<String, String> getImportMappings()
   {
      Collection<String> mappings = properties.getValueAsVector(IMPORT_MAPPINGS);
      HashMap<String, String> old2new = new HashMap<String, String>();
      for (String mapping : mappings)
      {
         String[] pair = mapping.split("->");
         old2new.put(pair[0], pair[1]);
      }
      
      return old2new;
   }
}
