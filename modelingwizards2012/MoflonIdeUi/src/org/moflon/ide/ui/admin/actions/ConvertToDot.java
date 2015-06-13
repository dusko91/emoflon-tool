package org.moflon.ide.ui.admin.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.moca.dot.unparser.DotUnparserAdapter;
import org.moflon.util.eMoflonEMFUtil;

import MocaTree.Attribute;
import MocaTree.MocaTreeFactory;
import MocaTree.Node;

public class ConvertToDot implements IWorkbenchWindowActionDelegate
{
   private static final Logger logger = Logger.getLogger(ConvertToDot.class);

   private IFile model;

   private IProgressMonitor monitor = new NullProgressMonitor();

   private HashMap<EObject, Integer> hashToId = new HashMap<EObject, Integer>();
   private int index = 0;

   @Override
   public void run(IAction action)
   {
      IFile dotRep = model.getProject().getFile(model.getProjectRelativePath().removeFileExtension().addFileExtension("dot"));
      try
      {
         if (dotRep.exists())
            dotRep.delete(true, monitor);

         String content = modelToDot();
         WorkspaceHelper.addFile(dotRep, content, monitor);
      } catch (Exception e)
      {
         e.printStackTrace();
         logger.error("Unable to create dot representation of " + model.getFullPath());
      }
   }

   private String modelToDot()
   {
      // Load model
      EObject root = eMoflonEMFUtil.loadModel(URI.createPlatformResourceURI(model.getFullPath().toString(), true), null);

      // Convert to tree
      Node tree = modelToTree(root);

      // Tree to dot
      String dot = treeToDot(tree);
      return dot;
   }

   private String treeToDot(Node tree)
   {
      DotUnparserAdapter dotUnparser = new DotUnparserAdapter();
      return dotUnparser.unparse(tree);
   }

   @Override
   public void selectionChanged(IAction action, ISelection selection)
   {
      // Check that an IFile is selected
      if (selection != null && selection instanceof StructuredSelection)
      {
         StructuredSelection fileSelection = (StructuredSelection) selection;

         // Assign files
         Object chosen = fileSelection.getFirstElement();

         if (chosen != null && chosen instanceof IFile)
         {
            model = (IFile) fileSelection.getFirstElement();
            return;
         }
      }

      action.setEnabled(false);
   }

   private Node modelToTree(EObject root)
   {
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
      while (verticies.hasNext())
      {
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
         Set<EStructuralFeature> references = eMoflonEMFUtil.getAllReferences(node);

         // Iterate through all references
         for (EStructuralFeature reference : references)
         {
            Node refName = MocaTreeFactory.eINSTANCE.createNode();
            refName.setName(reference.getName());
            refName.setParentNode(treeNodeRefs);

            // Check if the reference to be handled is a containment edge
            // (i.e., node contains s.th.)
            if (reference.getUpperBound() == -1)
               // Edge is n-ary: edge exists only once, but points to many
               // contained EObjects
               for (Object containedObject : (EList) node.eGet(reference, true))
               {
                  EObject element = (EObject) containedObject;
                  Node target = MocaTreeFactory.eINSTANCE.createNode();
                  target.setName(setName(element));
                  target.setParentNode(refName);
               }
            // else a standard reference was found
            else
            {
               EObject target = (EObject) node.eGet(reference, true);
               Node targetNode = MocaTreeFactory.eINSTANCE.createNode();
               targetNode.setName(setName(target));
               targetNode.setParentNode(refName);
            }

         }
      }

      return rootNode;
   }

   private void handleAttributes(EObject node, Node treeNode)
   {
      Node properties = MocaTreeFactory.eINSTANCE.createNode();
      properties.setName("PROPERTIES");
      properties.setParentNode(treeNode);
      properties.setIndex(0);
      
      int i = 0;
      for (EAttribute feature : node.eClass().getEAllAttributes())
      {
         Attribute featureAttr = MocaTreeFactory.eINSTANCE.createAttribute();
         featureAttr.setIndex(i++);
         featureAttr.setName(feature.getName());
         featureAttr.setValue(node.eGet(feature) == null? "" : node.eGet(feature).toString());
         
         featureAttr.setNode(properties);
      }
   }

   private String setName(EObject node)
   {
      if(!hashToId.containsKey(node))
         hashToId.put(node, index++);
         
      return "n" + hashToId.get(node) + "_" + node.eClass().getName();
   }

   @Override
   public void dispose()
   {
   }

   @Override
   public void init(IWorkbenchWindow window)
   {
   }
}
