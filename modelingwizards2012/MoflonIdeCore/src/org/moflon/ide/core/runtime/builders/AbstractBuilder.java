package org.moflon.ide.core.runtime.builders;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.moflon.ide.core.admin.WorkspaceHelper;

public abstract class AbstractBuilder extends IncrementalProjectBuilder implements IResourceVisitor, IResourceDeltaVisitor
{
   
   private static final Logger logger = Logger.getLogger(AbstractBuilder.class);
   
   protected IProgressMonitor subMonitor;
   
   @Override
   @SuppressWarnings("rawtypes")
   protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException
   {
      getProject().deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);

      monitor.beginTask(getProgressBarMessage(), 4 * WorkspaceHelper.PROGRESS_SCALE);

      // Default target for a build (complete, not on deltas)
      IProject targetForVisitor = getProject();

      subMonitor = new SubProgressMonitor(monitor, 3 * WorkspaceHelper.PROGRESS_SCALE);

      // Find out what kind of build it is
      if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD || kind == IncrementalProjectBuilder.AUTO_BUILD)
      {
         logger.debug("an incremental build, check what has changed and" + " only build where necessary");

         IResourceDelta delta = getDelta(targetForVisitor);

         if (delta == null)
         {
            // No changes -> perform full build
            targetForVisitor.accept(this);
         } else
         {
            // Walk through changes using visitor
            delta.accept(this);
         }
      } else
      {
         // Full-build after a clean operation
         processResource(subMonitor);
      }

      // Refresh workspace to update with changes made by interpreter etc.
      this.getProject().refreshLocal(IResource.DEPTH_ONE, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));

      monitor.done();

      // We could return a list of projects for which the builder may want to
      // request resource deltas in the next invocation. E.g. referenced
      // projects. For now we just return null
      return null;
   }

   protected String getProgressBarMessage()
   {
      return "Building " + getProject().getName();
   }

   abstract protected boolean processResource(IProgressMonitor monitor) throws CoreException;

}
