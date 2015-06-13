package org.moflon.ide.core.properties;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.util.eMoflonEMFUtil;

import MocaTree.MocaTreePackage;
import MocaTree.Node;

public class MocaTreeEAPropertiesReaderTest {
	@Test
	public void testSampleFile() throws Exception {
		String projectName = "MoflonIdeCore";

		// Map<String, MetamodelProperties> properties = new
		// MocaTreeEAPropertiesReader().getProperties(xmiFile);

		URI workspaceURI = URI.createPlatformResourceURI("/", true);
		URI projectURI = URI.createURI(projectName + "/", true).resolve(
				workspaceURI);

		// Create and initialize resource set
		ResourceSet set = CodeGeneratorPlugin.createDefaultResourceSet();
		// set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
		// new EcoreResourceFactoryImpl());
		eMoflonEMFUtil.installCrossReferencers(set);
		// CoreActivator.calculatePluginToResourceMap(set);

		set.getPackageRegistry().put("http://www.moflon.org.MocaTree",
				MocaTreePackage.eINSTANCE);

		set.getURIConverter()
				.getURIMap()
				.put(URI.createPlatformResourceURI("/", true),
						URI.createFileURI(System
								.getenv("eclipse.workspace.location") + "\\"));

		// Load Moca tree
		URI mocaFileURI = URI.createURI(
				"test/resources/small_tree_with_properties.xmi", true).resolve(
				projectURI);
		Resource mocaTreeResource = set.getResource(mocaFileURI, true);

		// Create and run exporter on Moca tree
		Node mocaTree = (Node) mocaTreeResource.getContents().get(0);

		new MocaTreeEAPropertiesReader(mocaTree).getProperties(mocaTree);

	}
}
