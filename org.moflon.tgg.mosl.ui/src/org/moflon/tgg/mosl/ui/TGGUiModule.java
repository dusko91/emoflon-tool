/*
 * generated by Xtext
 */
package org.moflon.tgg.mosl.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.moflon.tgg.mosl.ui.highlighting.MOSLHighlightingConfiguration;

/**
 * Use this class to register components to be used within the IDE.
 */
public class TGGUiModule extends org.moflon.tgg.mosl.ui.AbstractTGGUiModule {
	public TGGUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	
	public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration()
	{
	   return MOSLHighlightingConfiguration.class;
	}
}