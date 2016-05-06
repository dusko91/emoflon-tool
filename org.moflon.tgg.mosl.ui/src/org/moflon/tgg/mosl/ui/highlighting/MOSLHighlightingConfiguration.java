package org.moflon.tgg.mosl.ui.highlighting;

import org.eclipse.swt.SWT;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;

import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;
import org.moflon.tgg.mosl.ui.highlighting.rules.AbstractHighlightingRule;
import org.moflon.tgg.mosl.ui.highlighting.utils.MOSLColor;
import org.moflon.tgg.mosl.ui.highlighting.utils.MOSLHighlightProviderHelper;

public class MOSLHighlightingConfiguration extends DefaultHighlightingConfiguration
{
	
	@Override
	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		super.configure(acceptor);
		for(AbstractHighlightingRule rule : MOSLHighlightProviderHelper.getHighlightRules())
			rule.setHighlightingConfiguration(acceptor);
//		acceptor.acceptDefaultHighlighting(CREATION_ID, "Create-Operator", creationTextStyle());
//		acceptor.acceptDefaultHighlighting(DESTROY_ID, "Destroy-Operator", destroyTextStyle());
//		acceptor.acceptDefaultHighlighting(NEGATE_ID, "Negate-Operator", negateTextStyle());
//		acceptor.acceptDefaultHighlighting(REFINED_ID, "Refined", refinedTextStyle());
	}
	
   @Override
   public TextStyle keywordTextStyle()
   {
      TextStyle ts = super.keywordTextStyle();
      ts.setStyle(SWT.ITALIC);
      return ts;
   }
   
   public TextStyle refinedTextStyle(){
	      TextStyle ts = defaultTextStyle().copy();
	      ts.setColor(MOSLColor.LIGHT_BLUE.getColor());
	      return ts;
}

}
