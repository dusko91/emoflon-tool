package org.moflon.ide.texteditor.editors;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * used to determine a specified character is start/part of a word or not 
 * @author mdavid
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 *
 */
public class WordDetector implements IWordDetector {

	private char[] delimiters = new char[]{};
	private boolean lastCharIsDelimiter = false;
	
	public WordDetector(char[] delimiters){
		this.delimiters = delimiters;
	}
	/**
	 * every char but whitespace can be start of a word
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	public boolean isWordStart(char c) {
		if(isDelimiter(c))
			lastCharIsDelimiter = true;
		return  !(new WhitespaceDetector().isWhitespace(c));
	}

	/**
	 * every char but whitespace can be part of a word
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {

		boolean whitespace = (new WhitespaceDetector().isWhitespace(c));
		boolean delimiter = isDelimiter(c);
		boolean result = !whitespace && !delimiter;
		if(lastCharIsDelimiter)
			result = false;
		lastCharIsDelimiter = delimiter;
		return  result;
	}
	
	public void setDelimiters(char[] delimiters){
		this.delimiters = delimiters;
	}
	
	private boolean isDelimiter(char c){
		for(int i = 0; i < delimiters.length; i++){
			if(c == delimiters[i])
				return true;
		}
		return false;
	}

}
