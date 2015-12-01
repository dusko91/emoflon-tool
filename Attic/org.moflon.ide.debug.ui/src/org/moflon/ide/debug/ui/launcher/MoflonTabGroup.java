/*******************************************************************************
 *  Copyright (c) 2005, 2009 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bjorn Freeman-Benson - initial API and implementation
 *     Wind River Systems - adopted to use with DSF
 *******************************************************************************/
package org.moflon.ide.debug.ui.launcher;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

/**
 * Tab group for a PDA application
 * <p>
 * This class is identical to the corresponding in PDA debugger implemented in org.eclipse.debug.examples.ui.
 * </p>
 */
public class MoflonTabGroup extends AbstractLaunchConfigurationTabGroup
{
   /*
    * (non-Javadoc)
    * 
    * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse.debug.ui.ILaunchConfigurationDialog,
    * java.lang.String)
    */
   public void createTabs(ILaunchConfigurationDialog dialog, String mode)
   {
      setTabs(new ILaunchConfigurationTab[] { new MoflonMainTab(), new SourceLookupTab(), new CommonTab() });
   }
}