/*
 * generated by Xtext
 */
package org.moflon.tgg.mosl.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.moflon.tgg.mosl.services.TGGGrammarAccess;

public class TGGParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private TGGGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected org.moflon.tgg.mosl.parser.antlr.internal.InternalTGGParser createParser(XtextTokenStream stream) {
		return new org.moflon.tgg.mosl.parser.antlr.internal.InternalTGGParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "TripleGraphGrammar";
	}
	
	public TGGGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(TGGGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}
