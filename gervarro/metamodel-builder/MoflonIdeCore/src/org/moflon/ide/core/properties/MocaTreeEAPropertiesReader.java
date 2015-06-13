package org.moflon.ide.core.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.moflon.ide.core.CoreActivator;

import MocaTree.Attribute;
import MocaTree.Node;
import MocaTree.Text;

public class MocaTreeEAPropertiesReader
{
   private static final Logger logger = Logger.getLogger(MocaTreeEAPropertiesReader.class);

   private Node mocaTree;
   
   public MocaTreeEAPropertiesReader(final Node mocaTree) {
	   this.mocaTree = mocaTree;
   }

   public Map<String, MetamodelProperties> getProperties(IProject project) throws CoreException
   {
	   Map<String, MetamodelProperties> properties = getProperties(mocaTree);
	   properties.keySet().forEach(p->properties.get(p).setMetamodelProjectName(project.getName()));
	   return properties;
   }

   public Map<String, MetamodelProperties> getProperties(final Node rootNode) throws CoreException
   {
      Map<String, MetamodelProperties> propertiesMap = new HashMap<>();
      Node exportedTree = (Node) rootNode.getChildren().get(0);

      assert "exportedTree".equals(exportedTree.getName());

      EList<Text> rootPackages = exportedTree.getChildren();
      for (final Text rootText : rootPackages)
      {
         final Node rootPackage = (Node) rootText;
         MetamodelProperties properties = getProjectProperties(rootPackage);
         propertiesMap.put(properties.get(MetamodelProperties.PLUGIN_ID_KEY), properties);
      }

      return propertiesMap;
   }

   public MetamodelProperties getProjectProperties(final Node rootNode) throws CoreException
   {
      final MetamodelProperties properties = new MetamodelProperties();

      properties.put(MetamodelProperties.NAME_KEY, getValueForProperty("Moflon::Name", rootNode));
      properties.put(MetamodelProperties.NAME_KEY, getValueForProperty("Moflon::NsPrefix", rootNode));
      properties.put(MetamodelProperties.NS_URI_KEY, getValueForProperty("Moflon::NsUri", rootNode));
      properties.put(MetamodelProperties.PLUGIN_ID_KEY, getValueForProperty("Moflon::PluginID", rootNode));
      properties.put(MetamodelProperties.EXPORT_FLAG_KEY, getValueForProperty("Moflon::Export", rootNode));
      properties.put(MetamodelProperties.VALIDATED_FLAG_KEY, getValueForProperty("Moflon::Validated", rootNode));
      properties.put(MetamodelProperties.WORKING_SET_KEY, getValueForProperty("Moflon::WorkingSet", rootNode));

      switch (rootNode.getName())
      {
      case "EPackage":
         properties.put(MetamodelProperties.TYPE_KEY, MetamodelProperties.REPOSITORY_KEY);
         break;
      case "TGG":
         properties.put(MetamodelProperties.TYPE_KEY, MetamodelProperties.INTEGRATION_KEY);
         break;
      default:
         logger.warn("Unknown node type in Moca tree: " + rootNode.getName());
      }

      properties.setDependencies(extractDependencies(rootNode));

      properties.setDefaultValues();

      return properties;
   }

   public List<String> extractDependencies(final Node rootPackage) throws CoreException
   {
      Collection<Text> dependenciesNodes = rootPackage.getChildren("dependencies");

      if (dependenciesNodes.size() != 1)
      {
         throw new CoreException(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, "Missing dependencies nodes for project " + rootPackage.getName()));
      }

      final List<String> dependencies = new ArrayList<>();
      final Node dependenciesNode = (Node) dependenciesNodes.iterator().next();

      for (final Text text : dependenciesNode.getChildren())
      {
         final String dependency = ((Node) text).getName();
         dependencies.add(dependency);
      }

      return dependencies;
   }

   private String getValueForProperty(final String property, final Node rootPackage) throws CoreException
   {
      String value;
      Collection<Attribute> attributes = rootPackage.getAttribute(property);
      if (!attributes.isEmpty())
      {
         value = attributes.iterator().next().getValue();
      } else
      {
         throw new CoreException(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, "Missing property " + property + " for project " + rootPackage.getName()));
      }
      return value;
   }
}
