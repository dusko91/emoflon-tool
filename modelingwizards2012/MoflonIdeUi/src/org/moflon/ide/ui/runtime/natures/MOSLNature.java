package org.moflon.ide.ui.runtime.natures;

import org.eclipse.core.runtime.CoreException;
import org.moflon.ide.core.CoreActivator;

public class MOSLNature extends AbstractNature
{

   @Override
   public void configure() throws CoreException
   {
      appendCommand(CoreActivator.MOSL_BUILDER_ID);
      
   }

}
