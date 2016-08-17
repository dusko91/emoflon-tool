package org.moflon.tgg.mosl.builder;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.core.utilities.EMoflonPlugin;
import org.moflon.ide.core.WorkspaceObservationLifecycleManager;
import org.osgi.framework.BundleContext;

public class MOSLTGGPlugin extends EMoflonPlugin{
	private IWorkspace workspace;
	private MOSLTGGProjectMigrator migrator;
	
	@Override
	public void start(BundleContext context) throws Exception {
		workspace = ResourcesPlugin.getWorkspace();
		migrator = new MOSLTGGProjectMigrator();
		WorkspaceTask.execute(new WorkspaceObservationLifecycleManager(
				workspace, migrator, true), false);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		WorkspaceTask.execute(new WorkspaceObservationLifecycleManager(
				workspace, migrator, false), false);
		migrator = null;
		workspace = null;
	}
	
	public static MOSLTGGPlugin getDefault(){
		MOSLTGGPlugin plugin = getPlugin(MOSLTGGPlugin.class);
		if (plugin == null)
			throw new IllegalStateException("Plugin has not yet been set!");
		return plugin;
	}


}
