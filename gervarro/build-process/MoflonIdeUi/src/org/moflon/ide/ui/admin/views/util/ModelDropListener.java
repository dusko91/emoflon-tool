package org.moflon.ide.ui.admin.views.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

public class ModelDropListener extends ViewerDropAdapter
{
   private static final Logger logger = Logger.getLogger(ModelDropListener.class);

   private final Viewer viewer;

   private ArrayList<EObject> currentInput;

   public ModelDropListener(Viewer viewer)
   {
      super(viewer);
      this.viewer = viewer;
      currentInput = new ArrayList<>();
   }

   @Override
   public boolean performDrop(Object data)
   {
      if (data instanceof TreeSelection)
      {
         TreeSelection treeSel = (TreeSelection) data;

         Iterator<?> elements = treeSel.iterator();
         while (elements.hasNext())
         {
            Object element = elements.next();
            if (element instanceof EObject)
            {
               EObject root = (EObject) element;
               addToContentsToCurrentInput(root);
               updateView();
            }
         }
      } else
      {
         logger.error("Unable to handle dropped content: " + data.toString());
         throw new RuntimeException("Type not supported: " + data.getClass().toString());
      }
      return true;
   }

   private void addToContentsToCurrentInput(EObject root)
   {
      TreeIterator<EObject> elements = root.eAllContents();
      currentInput.add(root);
      while (elements.hasNext())
         currentInput.add(elements.next());
   }

   @Override
   public boolean validateDrop(Object target, int operation, TransferData transferType)
   {
      return true;
   }

   public void clear()
   {
      currentInput.clear();
      updateView();
   }

   private void updateView()
   {
      viewer.setInput(currentInput.toArray());
   }
}
