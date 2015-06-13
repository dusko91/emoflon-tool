package org.moflon.ide.ui.admin.assist.mosl;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class MOSLMarkerResolutionGenerator implements IMarkerResolutionGenerator
{

   @Override
   public IMarkerResolution[] getResolutions(IMarker marker)
   {
      try {
         if ("MissingPattern".equals(marker.getAttribute("errorType"))) {
            return new IMarkerResolution[] { new MissingPatternResolution() };
         }
      } catch (CoreException ce) {
         ce.printStackTrace();
      }
      return new IMarkerResolution[] {};
   }

}
