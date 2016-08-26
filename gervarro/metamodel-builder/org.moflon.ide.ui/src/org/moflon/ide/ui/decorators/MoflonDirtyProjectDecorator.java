package org.moflon.ide.ui.decorators;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

public class MoflonDirtyProjectDecorator extends LabelProvider implements ILightweightLabelDecorator
{
   private static final String REQUIRES_REBUILD_PREFIX = "*** ";
   private static final String AUTOBUILD_OFF_PREFIX = "*** ";
   public static final String DECORATOR_ID = "org.moflon.ide.ui.decorators.MoflonDirtyProjectDecorator";
   
   private Set<IProject> metamodelProjectsRequiringRebuild = new HashSet<>();
   private Set<IProject> projectsWithoutAutobuild = new HashSet<>();
   
   /**
    * Prefixes the project name of an eMoflon project with  {@link IS_DIRTY_PREFIX}.
    * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang.Object, org.eclipse.jface.viewers.IDecoration)
    */
   @Override
   public void decorate(final Object element, final IDecoration decoration)
   {
      if (metamodelProjectsRequiringRebuild.contains(element))
         decoration.addPrefix(REQUIRES_REBUILD_PREFIX);
      if (projectsWithoutAutobuild.contains(element))
         decoration.addPrefix(AUTOBUILD_OFF_PREFIX);
   }

   public void setMetamodelProjectRequiresRebuild(final IProject project, final boolean needsRebuild)
   {
      if (needsRebuild)
         metamodelProjectsRequiringRebuild.add(project);
      else
         metamodelProjectsRequiringRebuild.remove(project);
      this.fireLabelProviderChanged(new LabelProviderChangedEvent(this, project));
   }
   
   public void setAutobuildEnabled(final IProject project, final boolean autobuildEnabled)
   {
      if (!autobuildEnabled)
         projectsWithoutAutobuild.add(project);
      else
         projectsWithoutAutobuild.remove(project);
      this.fireLabelProviderChanged(new LabelProviderChangedEvent(this, project));
   }
}
