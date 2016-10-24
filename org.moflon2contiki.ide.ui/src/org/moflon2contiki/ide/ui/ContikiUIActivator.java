package org.moflon2contiki.ide.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ContikiUIActivator extends AbstractUIPlugin {

	// The shared instance
	private static ContikiUIActivator plugin;
	
	private static String bundleId;
	
	/**
	 * The constructor
	 */
	public ContikiUIActivator() {
	}

	@Override
	   public void start(final BundleContext context) throws Exception
	   {
	      super.start(context);
	      plugin = this;
	      bundleId = context.getBundle().getSymbolicName();

	      // CoreActivator.getDefault().reconfigureLogging();

	      // Configure logging for eMoflon
	      //setUpLogging();
	   }

	   @Override
	   public void stop(final BundleContext context) throws Exception
	   {
	      plugin = null;
	      bundleId = null;
	      super.stop(context);
	   }

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ContikiUIActivator getDefault() {
		return plugin;
	}

	public static String getModuleID()
	   {
	      if (bundleId == null)
	         throw new NullPointerException();
	      else
	         return bundleId;
	   }

}
