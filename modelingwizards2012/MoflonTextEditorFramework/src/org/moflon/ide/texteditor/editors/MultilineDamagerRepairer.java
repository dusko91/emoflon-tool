package org.moflon.ide.texteditor.editors;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

public class MultilineDamagerRepairer extends DefaultDamagerRepairer {

	public MultilineDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
	}

	@Override
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e,
			boolean documentPartitioningChanged) {
		return partition;
	}

}
