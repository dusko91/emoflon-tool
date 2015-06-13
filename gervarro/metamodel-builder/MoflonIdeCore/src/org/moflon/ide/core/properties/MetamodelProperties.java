package org.moflon.ide.core.properties;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.moflon.ide.core.CoreActivator;
import org.moflon.util.MoflonUtil;
import org.moflon.util.plugins.ManifestFileUpdater;

/**
 * Data transfer object for properties generated together with the metamodel
 *
 */
public class MetamodelProperties
{

   public static final String REPOSITORY_KEY = "repository";

   public static final String INTEGRATION_KEY = "integration";

   public static final String NAME_KEY = "name";

   public static final String PLUGIN_ID_KEY = "pluginId";

   public static final String TYPE_KEY = "type";

   public static final String WORKING_SET_KEY = "workingSet";

   public static final String IS_PLUGIN_KEY = "isPlugin";

   public static final String JAVA_VERION = "javaVersion";

   public static final String DEPENDENCIES_KEY = "dependencies";

   public static final String NS_URI_KEY = "nsURI";

   public static final String EXPORT_FLAG_KEY = "exportProject";

   public static final String VALIDATED_FLAG_KEY = "isValidated";

   private static final Logger logger = Logger.getLogger(MetamodelProperties.class);

   private static final String METAMODEL_PROJECT_NAME_KEY = "metamodelProject";

   private Map<String, String> data = new HashMap<>();

   public MetamodelProperties()
   {
      this.data = new HashMap<String, String>();
   }

   public MetamodelProperties(final Map<String, String> data)
   {
      this.data = new HashMap<>(data);
   }

   public boolean containsKey(final String key)
   {
      return this.data.containsKey(key);
   }

   public String get(final String key)
   {
      return this.data.get(key);
   }

   public void put(final String key, final String value)
   {
      this.data.put(key, value);
   }

   public void setMetamodelProjectName(String metamodelProjectName)
   {
      this.put(METAMODEL_PROJECT_NAME_KEY, metamodelProjectName);
   }

   public String getMetamodelProjectName()
   {
      return this.get(METAMODEL_PROJECT_NAME_KEY);
   }

   public String getType()
   {
      return get(MetamodelProperties.TYPE_KEY);
   }

   public boolean isRepositoryProject()
   {
      return REPOSITORY_KEY.equals(this.getType());
   }

   public boolean isIntegrationProject()
   {
      return INTEGRATION_KEY.equals(this.getType());
   }

   public String getNsUri()
   {
      return this.get(NS_URI_KEY);
   }

   public String getProjectName()
   {
      return this.get(NAME_KEY);
   }

   public boolean isExported()
   {
      return !"false".equals(this.get(EXPORT_FLAG_KEY));
   }

   public Collection<String> getDependencies()
   {
      if (!this.data.containsKey(DEPENDENCIES_KEY))
         return null;

      return this.get(DEPENDENCIES_KEY).isEmpty() ? Collections.<String> emptyList() : Arrays.asList(this.get(DEPENDENCIES_KEY).split(","));
   }

   public Collection<URI> getDependenciesAsURIs()  
   {
      return getDependencies().stream().filter(dep -> !dep.equals(ManifestFileUpdater.IGNORE_PLUGIN_ID))
                              .map(dep -> MoflonUtil.getDefaultURIToEcoreFileInPlugin(dep))
                              .collect(Collectors.toSet());
   }

   public void setDefaultValues()
   {
      if (!this.containsKey(JAVA_VERION))
         this.put(JAVA_VERION, "JavaSE-1.8");
   }

   /**
    * Reads the given properties and produces a mapping from project to properties.
    */
   public static Map<String, MetamodelProperties> createPropertiesMap(final Properties properties)
   {
      Map<String, MetamodelProperties> projectMap = new HashMap<>();
      for (Object key : properties.keySet())
      {
         int indexOfDelimiter = ((String) key).lastIndexOf(".");
         String projectId = ((String) key).substring(0, indexOfDelimiter);
         String property = ((String) key).substring(indexOfDelimiter + 1);
         String value = properties.getProperty((String) key);

         if (!projectMap.containsKey(projectId))
         {
            MetamodelProperties metamodelProperties = new MetamodelProperties();
            metamodelProperties.setDefaultValues();
            metamodelProperties.put(MetamodelProperties.PLUGIN_ID_KEY, projectId);
            metamodelProperties.put(MetamodelProperties.NAME_KEY, projectId);
            projectMap.put(projectId, metamodelProperties);
         }
         projectMap.get(projectId).put(property, value);
      }
      return projectMap;
   }

   /**
    * Tries to parse the given file as a properties file and produces a mapping from repository project to its
    * properties
    * 
    */
   public static Map<String, MetamodelProperties> readEAProperties(final IFile propertyFile) throws CoreException
   {
      Properties properties = new Properties();

      try
      {
         InputStream streamToPropertiesFile = propertyFile.getContents();
         properties.load(streamToPropertiesFile);
         streamToPropertiesFile.close();
         logger.debug("Properties loaded: " + properties);
      } catch (Exception e)
      {
         logger.warn("Unable to load properties file or file not existing: " + e);

         createMarkerForMissingExportedFiles(propertyFile);
         throw new CoreException(new Status(IStatus.WARNING, CoreActivator.PLUGIN_ID, "Unable to load properties file or file not existing: " + e));
      }

      Map<String, MetamodelProperties> projectMap = createPropertiesMap(properties);

      logger.debug("Parsed project map: " + projectMap);
      return projectMap;
   }

   public static void createMarkerForMissingExportedFiles(final IFile propertyFile) throws CoreException
   {
      IMarker marker = propertyFile.getProject().createMarker(IMarker.PROBLEM);
      marker.setAttribute(IMarker.MESSAGE,
            "Cannot find any exported files to build. Please note that by convention, your active EAP file MUST have the same name as your project!");
      marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
      marker.setAttribute(IMarker.LOCATION, propertyFile.getProjectRelativePath().toString());
   }

   public void setDependencies(final List<String> dependencies)
   {
      this.put(DEPENDENCIES_KEY, dependencies.stream().collect(Collectors.joining(",")));
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return "MetamodelProperties [data=" + data + "]";
   }

}