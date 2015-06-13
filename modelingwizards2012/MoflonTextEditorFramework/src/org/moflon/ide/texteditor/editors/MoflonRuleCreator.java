package org.moflon.ide.texteditor.editors;

import java.util.Collection;

import org.eclipse.jface.text.rules.IRule;

abstract class MoflonRuleCreator {

	protected abstract Collection<IRule> getRules();
}
