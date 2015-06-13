package org.moflon.ide.ui.decorators;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.moflon.ide.core.CoreActivator;

public class MOSLLightweightDecorator extends LabelProvider implements ILightweightLabelDecorator
{
   public static final String DECORATOR_ID = "org.moflon.ide.ui.decorators.MOSLLightweightDecorator";
   
   @Override
   public void decorate(Object element, IDecoration decoration)
   {
      if (!(element instanceof IProject))
         return;
      
      if (CoreActivator.getDefault().isMoslDirty(((IProject) element).getName()))
         decoration.addPrefix("*** ");
   }
}
