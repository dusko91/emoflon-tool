package org.moflon.ide.texteditor.editors;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

public class MoflonWordPatternRuleCreator extends MoflonRuleCreator {

	private String lastStartSequence;
	private IToken token;
	
	private Vector<IRule> wordpatternRules = new Vector<IRule>();
	
	@Override
	protected Collection<IRule> getRules(){
		return wordpatternRules;
	}
	
	public MoflonWordPatternRuleCreator startsWith(String startSequence){
		lastStartSequence = startSequence;
		return this;
	}
	
	public void endsWith(String endSequence){
		
		if(lastStartSequence != null && token != null){
			wordpatternRules.add(new MultiLineRule(lastStartSequence, endSequence, token));
		}
		
		lastStartSequence = null;
		token = null;
	}
	
	protected void setTextAttribute(TextAttribute attribute){
		token = new Token(attribute);
	}

}
