package org.moflon.ide.ui.admin.actions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EcoreFactoryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.moflon.Activator;
import org.moflon.tie.DotToSDMLangTGG;
import org.moflon.tie.DotToTGGLangTGG;
import org.moflon.util.IntegratorHelper;
import org.moflon.util.WorkspaceHelper;
import org.moflon.util.eMoflonSDMUtil;
import org.moflon.moca.MocaTreeSorter;
import org.moflon.moca.dot.unparser.DotUnparserAdapter;
import org.moflon.moca.dot.unparser.SimpleDotUnparserAdapter;
import org.moflon.util.eMoflonEMFUtil;

import sun.security.krb5.internal.crypto.EType;
import DotToPG.DotToPGConverter;
import DotToPG.impl.DotToPGFactoryImpl;
import DotToTGGTGG.FolderToTripleGraphGrammar;
import DotToTGGTGG.Rules.FolderToTGGRule;
import DotToTGGTGG.Rules.impl.RulesFactoryImpl;
import DotToTGGTGG.impl.DotToTGGTGGFactoryImpl;
import DotToTGGTGG.impl.DotToTGGTGGPackageImpl;
import DotToTGGTGG.util.DotToTGGTGGAdapterFactory;
import Moca.CodeAdapter;
import Moca.MocaFactory;
import Moca.impl.MocaFactoryImpl;
import Moca.unparser.TemplateUnparser;
import MocaTree.Attribute;
import MocaTree.File;
import MocaTree.Folder;
import MocaTree.MocaTreeFactory;
import MocaTree.Node;
import MocaTree.impl.MocaTreeFactoryImpl;
import SDMLanguage.activities.Activity;
import SDMLanguage.activities.ActivityNode;
import SDMLanguage.activities.StartNode;
import TGGLanguage.TGGLanguagePackage;
import TGGLanguage.TripleGraphGrammar;
import TGGLanguage.analysis.StaticAnalysis;
import TGGRuntime.CorrespondenceModel;

public class ConvertToDot implements IWorkbenchWindowActionDelegate {
	private static final Logger logger = Logger.getLogger(ConvertToDot.class);

	private IFile model;

	private IProgressMonitor monitor = new NullProgressMonitor();

	private HashMap<EObject, Integer> hashToId = new HashMap<EObject, Integer>();
	private int index = 0;

	@Override
	public void run(IAction action) {
		logger.debug("Creating dot representation.");

		modelToDot();

		try {
			model.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loads a model and creates .dot-files
	 */
	private void modelToDot() {
		// Load model
      try
      {
         EObject root = eMoflonEMFUtil.loadModelWithDependenciesAndCrossReferencer(URI.createPlatformResourceURI(model.getFullPath().toString(), true), null);
         logger.debug("Loaded: " + root);

         if (root instanceof TripleGraphGrammar) {
            treeToDot(new DotUnparserAdapter(), modelToTree_TGG((TripleGraphGrammar) root));
         } else if (root instanceof EPackage) {
            treeToDot(new DotUnparserAdapter(), modelToTree_Ecore((EPackage) root));
         } else if (root instanceof CorrespondenceModel) {
        	 treeToDot(new DotUnparserAdapter(), modelToTree_CorrModel((CorrespondenceModel) root));
         } else
            treeToDot(new SimpleDotUnparserAdapter(), modelToTree_Simple(root));
         
      } catch (Exception e)
      {
         e.printStackTrace();
         logger.debug(Arrays.toString(e.getStackTrace()));
      }

	}

	/**
	 * unparses the data of the folder with the given unparser and calls for svg
	 * creation
	 * 
	 * @param unparser
	 * @param folder
	 */
	private void treeToDot(TemplateUnparser unparser, Folder folder) {
		CodeAdapter codeAdapter = MocaFactory.eINSTANCE.createCodeAdapter();
		codeAdapter.getUnparser().add(unparser);
		codeAdapter.unparse(model.getProject().getLocation().toOSString()
				+ java.io.File.separator + "visualisation", folder);
		createSVG(model.getProject().getLocation().toOSString()
				+ java.io.File.separator + "visualisation", folder);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// Check that an IFile is selected
		if (selection != null && selection instanceof StructuredSelection) {
			StructuredSelection fileSelection = (StructuredSelection) selection;

			// Assign files
			Object chosen = fileSelection.getFirstElement();

			if (chosen != null && chosen instanceof IFile) {
				model = (IFile) fileSelection.getFirstElement();
				return;
			}
		}

		action.setEnabled(false);
	}

	/**
	 * handles the creation of a dot structure contained in a MocaTree that
	 * creates a dot visualisation for each tgg rule
	 * 
	 * @param tgg
	 * @return
	 */
	private Folder modelToTree_TGG(TripleGraphGrammar tgg) {
		IntegratorHelper helper = null;
		Folder folder = null;
		try {
			helper = new DotToTGGLangTGG();
			helper.setTrg(tgg);

			URL pathToPlugin = Activator.getPathRelToPlugIn("/",
					"org.moflon.visualization.DotToTGGTGG");
			helper.setRules((StaticAnalysis) eMoflonEMFUtil
					.loadAndInitModelWithDependencies(
							TGGLanguagePackage.eINSTANCE,
							pathToPlugin.getFile() + "/model/DotToTGGTGG"
									+ ".gen.sma.xmi", helper.getResourceSet()));
			logger.debug("Loaded rules");

			helper.integrateBackward();
			logger.debug("TGG backward transformation completed");

			folder = (Folder) helper.getSrc();
			folder.setName(getFileName());
			MocaTreeSorter.sort(folder);
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(Arrays.toString(e.getStackTrace()));
		}

		return folder;
	}

	private Folder modelToTree_CorrModel(CorrespondenceModel corrModel) {
		DotToPGConverter converter = DotToPGFactoryImpl.eINSTANCE.createDotToPGConverter();
		ResourceSet newSet = new ResourceSetImpl();
		eMoflonEMFUtil.addToResourceSet(newSet, converter);
		eMoflonEMFUtil.addToResourceSet(newSet, corrModel);

		eMoflonEMFUtil.addToResourceSet(newSet, corrModel.getSource());
		eMoflonEMFUtil.addToResourceSet(newSet, corrModel.getTarget());
		
		Folder folder = MocaTreeFactoryImpl.eINSTANCE.createFolder();
		folder.setName("Precedence");
		
		eMoflonEMFUtil.addToResourceSet(newSet, folder);

		folder.getFile().add(converter.convert(corrModel.getSourcePrecedenceGraph()));
		folder.getFile().add(converter.convert(corrModel.getTargetPrecedenceGraph()));
		
		for(File f : folder.getFile())
			MocaTreeSorter.sort(f);
		
		return folder;
	}

	/**
	 * handles the creation of a dot structure contained in a MocaTree that
	 * creates a dot visualisation for every operation of every class
	 * 
	 * @param epackage
	 * @return
	 */
	private Folder modelToTree_Ecore(EPackage epackage) {
		Folder rootFolder = MocaTreeFactoryImpl.eINSTANCE.createFolder();
		rootFolder.setName(getFileName());

		return modelToTree_Ecore(epackage, rootFolder);
	}

	private Folder modelToTree_Ecore(EPackage epackage, Folder folder) {
		// translate all supported content of a package
		Iterator<EClassifier> classifierIterator = epackage.getEClassifiers()
				.iterator();
		while (classifierIterator.hasNext()) {
			EClassifier eclassifier = classifierIterator.next();
			if (eclassifier instanceof EClass) {
				Folder subFolder = MocaTreeFactoryImpl.eINSTANCE.createFolder();
				subFolder = modelToTree_Ecore((EClass) eclassifier, subFolder);
				if (!subFolder.getFile().isEmpty()) {
					subFolder.setName(eclassifier.getName());
					folder.getSubFolder().add(subFolder);
				}
			}
		}

		// recursive call to translate all subfolders
		Iterator<EPackage> folderIterator = epackage.getESubpackages()
				.iterator();
		while (folderIterator.hasNext()) {
			EPackage subpackage = folderIterator.next();
			Folder subFolder = MocaTreeFactoryImpl.eINSTANCE.createFolder();
			subFolder = modelToTree_Ecore(subpackage, subFolder);
			if (!subFolder.getFile().isEmpty()
					|| !subFolder.getSubFolder().isEmpty()) {
				subFolder.setName(subpackage.getName());
				folder.getSubFolder().add(subFolder);
			}
		}

		return folder;
	}

	private Folder modelToTree_Ecore(EClass eclass, Folder folder) {
		Iterator<EOperation> eOpIterator = eclass.getEOperations().iterator();
		Folder patternFolder = null;
		while (eOpIterator.hasNext()) {
			EOperation eOperation = eOpIterator.next();
			Activity activity = null;
			// if sdm has been found -> integrate and connect file to folder
			// structure
			if ((activity = getActivity(eOperation)) != null) {
				IntegratorHelper helper = null;
				try {
					// initialize tgg and integrate
					helper = new DotToSDMLangTGG();
					URL pathToPlugin = Activator.getPathRelToPlugIn("/",
							"org.moflon.visualization.DotToSDMLanguageTGG");

					logger.debug("Retrieved: " + pathToPlugin + ", "
							+ pathToPlugin.getFile() + ", "
							+ pathToPlugin.getPath());

					helper.setRules((StaticAnalysis) eMoflonEMFUtil
							.loadAndInitModelWithDependencies(
									TGGLanguagePackage.eINSTANCE,
									pathToPlugin.getFile()
											+ "/model/DotToSDMLanguageTGG"
											+ ".gen.sma.xmi",
									helper.getResourceSet()));

					for (ActivityNode node : activity.getOwnedActivityNode()) {
						if (node instanceof StartNode) {
							String name = eclass.getName() + "::"
									+ eOperation.getName() + "(";
							for (int i = 0; i < eOperation.getEParameters()
									.size(); i++) {
								EParameter eParam = eOperation.getEParameters()
										.get(i);
								if (i != 0)
									name += node.getName() + ", ";
								name += eParam.getEType().getName() + " "
										+ eParam.getName();
							}
							name += ")";
							node.setName(name);
						}
					}

					helper.setTrg(activity);
					helper.integrateBackward();

					Folder genFolder = (Folder) helper.getSrc();

					File file = genFolder.getFile().get(0);
					file.setName(eOperation.getName()
							+ getParameterString(eOperation) + ".dot");
					MocaTreeSorter.sort(file);
					folder.getFile().add(file);
					if (patternFolder == null) {
						patternFolder = genFolder.getSubFolder().get(0);
						folder.getSubFolder().add(patternFolder);
					} else {
						patternFolder.getFile().addAll(
								genFolder.getSubFolder().get(0).getFile());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		return folder;
	}

	private String getFileName() {
		return model.getFullPath().lastSegment();
	}

	/**
	 * handles the creation of a simple generic dot structure that fits most
	 * ecore models
	 * 
	 * @param root
	 * @return
	 */
	private Folder modelToTree_Simple(EObject root) {
		Node rootNode = MocaTreeFactory.eINSTANCE.createNode();
		rootNode.setName("root");

		// Traverse along the containment edges (sufficient enough due to the
		// transitive containment hierarchy each Ecore model adheres to)
		Iterator<EObject> contents = root.eAllContents();

		// Add root
		Collection<EObject> allContents = new ArrayList<EObject>();
		allContents.add(root);
		while (contents.hasNext())
			allContents.add(contents.next());

		// Handle nodes in graph
		hashToId.clear();
		index = 0;
		Iterator<EObject> verticies = allContents.iterator();
		while (verticies.hasNext()) {
			// Element in model
			EObject node = verticies.next();

			// Build tree
			Node treeNode = MocaTreeFactory.eINSTANCE.createNode();
			treeNode.setName(setName(node));
			treeNode.setParentNode(rootNode);

			handleAttributes(node, treeNode);

			Node treeNodeRefs = MocaTreeFactory.eINSTANCE.createNode();
			treeNodeRefs.setName("REFS");
			treeNodeRefs.setParentNode(treeNode);
			treeNodeRefs.setIndex(1);

			// Retrieve all EReference instances
			Set<EStructuralFeature> references = eMoflonEMFUtil
					.getAllReferences(node);

			// Iterate through all references
			for (EStructuralFeature reference : references) {
				Node refName = MocaTreeFactory.eINSTANCE.createNode();
				refName.setName(reference.getName());
				refName.setParentNode(treeNodeRefs);

				// Check if the reference to be handled is a containment edge
				// (i.e., node contains s.th.)
				if (reference.getUpperBound() != 1)
					// Edge is n-ary: edge exists only once, but points to many
					// contained EObjects
					for (Object containedObject : (EList) node.eGet(reference,
							true)) {
						EObject element = (EObject) containedObject;
						Node target = MocaTreeFactory.eINSTANCE.createNode();
						target.setName(setName(element));
						target.setParentNode(refName);
					}
				// else a standard reference was found
				else {
					EObject target = (EObject) node.eGet(reference, true);
					Node targetNode = MocaTreeFactory.eINSTANCE.createNode();
					targetNode.setName(setName(target));
					targetNode.setParentNode(refName);
				}

			}
		}

		File file = MocaTreeFactoryImpl.eINSTANCE.createFile();
		file.setName(model.getFullPath().removeFileExtension()
				.addFileExtension("dot").lastSegment());
		file.setRootNode(rootNode);
		Folder folder = MocaTreeFactoryImpl.eINSTANCE.createFolder();
		folder.setName(getFileName());
		folder.getFile().add(file);

		return folder;
	}

	private void handleAttributes(EObject node, Node treeNode) {
		Node properties = MocaTreeFactory.eINSTANCE.createNode();
		properties.setName("PROPERTIES");
		properties.setParentNode(treeNode);
		properties.setIndex(0);

		int i = 0;
		for (EAttribute feature : node.eClass().getEAllAttributes()) {
			Attribute featureAttr = MocaTreeFactory.eINSTANCE.createAttribute();
			featureAttr.setIndex(i++);
			featureAttr.setName(feature.getName());
			featureAttr.setValue(node.eGet(feature) == null ? ""
					: prepareStringForDOT(node.eGet(feature).toString()));

			featureAttr.setNode(properties);
		}
	}

	private String prepareStringForDOT(String string) {
		String s = string.replace("{", "\\{");
		s = s.replace("}", "\\}");
		s = s.replace("\"", "\\\"");
		s = s.replace("\n", "\\n ");

		return s;
	}

	private String setName(EObject node) {
		if (!hashToId.containsKey(node))
			hashToId.put(node, index++);

		return "n" + hashToId.get(node) + "_" + node.eClass().getName();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}

	/**
	 * tries to create .svg out of .dot-files that are found in the specified
	 * location that is given by path + folder
	 * 
	 * @param path
	 * @param folder
	 */
	private void createSVG(String path, Folder folder) {
		// recursive method for all subfolders
		for (Folder subFolder : folder.getSubFolder()) {
			createSVG(path + java.io.File.separator + folder.getName(),
					subFolder);
		}

		// create svg for all .dot files
		for (File file : folder.getFile()) {
			String filePath = path + java.io.File.separator + folder.getName()
					+ java.io.File.separator + file.getName();
			String cmd = "dot -Tsvg \"" + filePath + "\" -o \""
					+ filePath.replaceFirst(".dot", ".svg") + "\"";
			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Could not find dot. Please check if it has been installed correctly.");
			}
		}
	}

	private String getParameterString(EOperation eoperation) {
		String parameters = "(";
		for (int i = 0; i < eoperation.getEParameters().size(); i++) {
			String eParamType = eoperation.getEParameters().get(i).getEType()
					.getName();
			if (i != 0)
				parameters += ", ";

			parameters += eParamType;
		}
		return parameters + ")";
	}

	private Activity getActivity(EOperation eoperation) {
		ResourceSet resourceSet = new ResourceSetImpl();
		eMoflonEMFUtil.addToResourceSet(resourceSet, eoperation.eContainer());
		return eMoflonSDMUtil
				.getActivityFromEOperation(eoperation, resourceSet);
	}
}
