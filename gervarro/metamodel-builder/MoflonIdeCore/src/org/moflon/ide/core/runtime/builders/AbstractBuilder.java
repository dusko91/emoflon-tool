package org.moflon.ide.core.runtime.builders;

import java.util.Map;

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
import org.moflon.util.WorkspaceHelper;

public abstract class AbstractBuilder extends IncrementalProjectBuilder implements IResourceVisitor, IResourceDeltaVisitor
{
   protected IProgressMonitor subMonitor;
   
   protected int kind = FULL_BUILD;

   @Override
   protected void clean(final IProgressMonitor monitor) throws CoreException
   {
      monitor.beginTask(getProgressBarMessage(), 1 * WorkspaceHelper.PROGRESS_SCALE);
      cleanResource(WorkspaceHelper.createSubmonitorWith1Tick(monitor));
      getProject().deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
      monitor.done();
   }

   @Override
   protected IProject[] build(final int kind, final Map<String, String> args, final IProgressMonitor monitor) throws CoreException
   {
      this.kind = kind;
      
      getProject().deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);

      monitor.beginTask(getProgressBarMessage(), 4 * WorkspaceHelper.PROGRESS_SCALE);

      subMonitor = new SubProgressMonitor(monitor, 3 * WorkspaceHelper.PROGRESS_SCALE);

      if (kind == FULL_BUILD)
      {
         // No changes -> perform full build
         processResource(subMonitor);
      } else
      {
         IResourceDelta delta = getDelta(getProject());
         if (delta != null)
         {
            // Walk through changes using visitor
            delta.accept(this);
         }
      }
      
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

   abstract protected void cleanResource(IProgressMonitor monitor) throws CoreException;

}
