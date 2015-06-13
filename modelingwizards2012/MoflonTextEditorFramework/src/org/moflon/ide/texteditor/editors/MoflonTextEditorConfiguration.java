package org.moflon.ide.texteditor.editors;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.swt.graphics.RGB;
import org.moflon.ide.texteditor.helpers.MarkerHelper;
import org.moflon.ide.texteditor.helpers.TextAttributeHelper;

import Moca.Problem;

public abstract class MoflonTextEditorConfiguration implements ITokenScanner,
		IContentAssistProcessor, M2TSynchronizer {
   
	// text highlighting variables
	private RuleBasedScanner ruleBasedScanner;
	private MoflonKeywordRuleCreator keywordRuleCreator;
	private MoflonWordPatternRuleCreator wordPatternRuleCreator;

	private Vector<IRule> defaultRules;
	private Vector<IRule> contextDependentRules;

	private IDocument document;
	private WhitespaceDetector whitespaceDetector;
	private char[] delimiters = new char[] {};
	public final static int BOLD 			= 1 << 0;
	public final static int ITALIC 			= 1 << 1;
	public final static int UNDERLINED 		= 1 << 30;
	public final static int STRIKETHROUGH   = 1 << 29;		

	// template variables
	private MoflonTextCompletionProcessor templateCompletionProcessor;
	private int cursorOffset;
	private boolean cursorBasedPositioning = false;
	
	private IProject project;

	public MoflonTextEditorConfiguration() {
		configureTextHighlighting();
		configureTemplates();
	}

	public void setProject(IProject project) {
		this.project = project;
	}
	
	@Override
	public void setRange(IDocument document, int offset, int length) {
		this.document = document;
		ruleBasedScanner.setRange(document, offset, length);
	}

	@Override
	public IToken nextToken() {

		highlightWord();
		defaultRules.addAll(defaultRules.size() - 2, contextDependentRules);
		ruleBasedScanner.setRules(defaultRules.toArray(new IRule[defaultRules
				.size()]));
		IToken token = ruleBasedScanner.nextToken();
		defaultRules.removeAll(contextDependentRules);
		contextDependentRules.removeAllElements();
		return token;
	}

	@Override
	public int getTokenOffset() {
		return ruleBasedScanner.getTokenOffset();
	}

	@Override
	public int getTokenLength() {
		return ruleBasedScanner.getTokenLength();
	}

	// abstract text highlighting methods
	abstract public void setKeyWords();

	abstract public void highlightWord();

	abstract public void highlightSequence();

	abstract public char[] getDelimiters();

	// abstract template methods
	abstract public Collection<MoflonEditorTemplate> getTemplates();
	
	//abstract error marker methods
	abstract public void getProblems(); 
	
	/**
	 * Adds a Marker in the TextEditor for a given Problem. 
	 * The Marker will be bound to the Resource, given in the Problem. 
	 * @param problem The problem, the Marker will report.
	 * @param pathToTextfiles The path, where the file is located that caused the problem (e.g. "instances/in").
	 */
	protected void AddMarker(Problem problem, String pathToTextfiles){
		IResource resource = project.findMember(pathToTextfiles+"/"+problem.getSource());
		MarkerHelper.reportError(resource, problem);
	}
	
	/**
	 * Adds a Marker in the TextEditor for a given Exception. 
	 * As no Resource is given, the Marker will be bound to the current Project.
	 * @param exception The exception, the Marker will report.
	 */
	protected void AddMarker(Exception exception){
		MarkerHelper.reportError(project,exception);
	}

	protected MoflonKeywordRuleCreator addKeyWord(String keyword) {
		keywordRuleCreator.setKeyword(keyword);
		return keywordRuleCreator;
	}

	/**
	 * highlights a range
	 * @param color defines the color of the text.
	 * @return
	 */
	protected MoflonWordPatternRuleCreator highlightRange(COLORS color) {
		wordPatternRuleCreator.setTextAttribute(TextAttributeHelper.createTextAttribute(color, null, null, 0, 0, 0));
		return wordPatternRuleCreator;
	}
	
	/**
	 * highlights a range
	 * @param color defines the color of the text.
	 * @param style defines text style attributes. Values: BOLD, ITALIC.  If style is 0, no style will be applied.
	 * @return
	 */
	protected MoflonWordPatternRuleCreator highlightRange(COLORS color, int style) {
		wordPatternRuleCreator.setTextAttribute(TextAttributeHelper.createTextAttribute(color, null, null, 0, style, 0));
		return wordPatternRuleCreator;
	}
	
	/**
	 * highlights a range
	 * @param color defines the color of the text.
	 * @param bgColor defines the color of the background. If bgColor is null, color wont be changed. 
	 * @param fontName defines the font. If fontName is null, font wont be changed.
	 * @param fontHeight defines the size of the text. If fontHeight is 0, size wont be changed.
	 * @param style defines text style attributes. Values: BOLD, ITALIC.  If style is 0, no style will be applied.
	 * @param lineStyle defines line style of text. Values: UNDERLINED, STRIKETHROUGH. If lineStyle is 0, no lines will be applied.
	 * @return
	 */
	protected MoflonWordPatternRuleCreator highlightRange(COLORS color, COLORS bgColor, String fontName, int fontHeight, int style, int lineStyle) {
		wordPatternRuleCreator.setTextAttribute(TextAttributeHelper.createTextAttribute(color, bgColor, fontName, fontHeight, style, lineStyle));
		return wordPatternRuleCreator;
	}

	protected String getWord(int index) {

		try {
			if (index >= 0) {
				return getWordAhead(index);
			} else {
				return getWordBack(index);
			}
		}

		catch (Exception e) {
			return "";
		}

	}

		
	/**
	 * Highlights a given Token
	 * @param color defines the color of the text.
	 * @param bgColor defines the color of the background. If bgColor is null, color wont be changed. 
	 * @param fontName defines the font. If fontName is null, font wont be changed.
	 * @param fontHeight defines the size of the text. If fontHeight is 0, size wont be changed.
	 * @param style defines text style attributes. Values: BOLD, ITALIC.  If style is 0, no style will be applied.
	 * @param lineStyle defines line style of text. Values: UNDERLINED, STRIKETHROUGH. If lineStyle is 0, no lines will be applied.
	 */
	protected void highlight(COLORS color, COLORS bgColor, String fontName, int fontHeight, int style, int lineStyle) {

		TextAttribute textAttribute = TextAttributeHelper.createTextAttribute(color, bgColor, fontName, fontHeight, style, lineStyle);
		IToken token = new Token(textAttribute);
		IRule rule = new WordRule(new WordDetector(delimiters), token);
		contextDependentRules.add(rule);
	}


	/**
	 * Highlights a given Token
	 * @param color defines the color of the text.
	 * @param style defines text style attributes. Values: BOLD, ITALIC.  If style is 0, no style will be applied.
	 */
	protected void highlight(COLORS color, int style) {
		highlight(color, null, null, 0, style, 0);
	}

	/**
	 * Highlights a given Token
	 * @param color defines the color of the text.
	 */
	protected void highlight(COLORS color) {
		highlight(color, null, null, 0, 0, 0);
	}

	private String getWordAhead(int index) throws Exception {
		int wordOffset;
		if (cursorBasedPositioning)
			wordOffset = cursorOffset;
		else
			wordOffset = ruleBasedScanner.getTokenOffset()
					+ ruleBasedScanner.getTokenLength();

		String result = "";
		for (int i = 0; i <= index; i++) {
			result = "";
			char c = document.getChar(wordOffset);

			while (whitespaceDetector.isWhitespace(c)) {
				c = document.getChar(++wordOffset);
			}

			if (isDelimiter(c)) {
				result += c;
				wordOffset++;
				continue;
			}

			while (!whitespaceDetector.isWhitespace(c) && !isDelimiter(c)
					&& c != ruleBasedScanner.EOF) {
				result = result + c;
				c = document.getChar(++wordOffset);
			}
			while (whitespaceDetector.isWhitespace(c)) {
				try {
					c = document.getChar(++wordOffset);

				} catch (Exception e) {
					break;
				}
			}
		}
		return result;
	}

	private String getWordBack(int index) throws Exception {
		int wordOffset;
		if (cursorBasedPositioning)
			wordOffset = cursorOffset - 1;
		else
			wordOffset = ruleBasedScanner.getTokenOffset()
					+ ruleBasedScanner.getTokenLength() - 1;
		
		String result = "";
		for (int i = 0; i > index; i--) {
			result = "";
			char c = document.getChar(wordOffset);

			while (whitespaceDetector.isWhitespace(c)) {
				c = document.getChar(--wordOffset);
			}

			if (isDelimiter(c)) {
				result += c;
				wordOffset--;
				continue;
			}

			while (!whitespaceDetector.isWhitespace(c) && !isDelimiter(c)) {
				result = c + result;
				try {
					c = document.getChar(--wordOffset);
				} catch (Exception e) {
					break;
				}

			}

		}
		return result;
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		cursorOffset = offset;
		cursorBasedPositioning = true;
		Collection<MoflonEditorTemplate> moflonTemplatesUnsorted = getTemplates();
		cursorBasedPositioning = false;
		TreeSet<MoflonEditorTemplate> priorityQueue = new TreeSet<MoflonEditorTemplate>();
		for (MoflonEditorTemplate moflonTemplate : moflonTemplatesUnsorted) {
			priorityQueue.add(moflonTemplate);
		}
		Template[] templates = priorityQueue.toArray(new Template[priorityQueue
				.size()]);
		templateCompletionProcessor.setTemplates(templates);
		return templateCompletionProcessor.computeCompletionProposals(viewer,
				offset);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return templateCompletionProcessor.computeContextInformation(viewer,
				offset);
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {

		return templateCompletionProcessor
				.getCompletionProposalAutoActivationCharacters();
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return templateCompletionProcessor
				.getContextInformationAutoActivationCharacters();
	}

	@Override
	public String getErrorMessage() {
		return templateCompletionProcessor.getErrorMessage();
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return templateCompletionProcessor.getContextInformationValidator();
	}

	private void configureTextHighlighting() {
		ruleBasedScanner = new RuleBasedScanner();
		defaultRules = new Vector<IRule>();
		contextDependentRules = new Vector<IRule>();
		whitespaceDetector = new WhitespaceDetector();
		delimiters = getDelimiters();

		// create rules
		WhitespaceRule whiteSpaceRule = new WhitespaceRule(whitespaceDetector,
				Token.WHITESPACE);
		defaultRules.add(whiteSpaceRule);

		wordPatternRuleCreator = new MoflonWordPatternRuleCreator();
		highlightSequence();
		defaultRules.addAll(wordPatternRuleCreator.getRules());

		keywordRuleCreator = new MoflonKeywordRuleCreator(new WordDetector(
				delimiters));
		setKeyWords();
		defaultRules.addAll(keywordRuleCreator.getRules());

		IToken defaultToken = new Token(new TextAttribute(ColorManager
				.getInstance().getColor(new RGB(0, 0, 0))));
		IRule rule = new WordRule(new WordDetector(delimiters), defaultToken);
		defaultRules.add(rule);

		ruleBasedScanner.setRules(defaultRules.toArray(new IRule[defaultRules
				.size()]));
	}

	private void configureTemplates() {
		templateCompletionProcessor = new MoflonTextCompletionProcessor();
	}

	private boolean isDelimiter(char c) {
		for (int i = 0; i < delimiters.length; i++) {
			if (c == delimiters[i])
				return true;
		}
		return false;
	}
	
	protected String getModelPath(String textPath){
		HashMap<String,String> modelToTextPathes = this.getModelPathesToTextPathes();
		for(String modelPath : modelToTextPathes.keySet()){
			if(modelToTextPathes.get(modelPath).equals(textPath))
				return modelPath;
		}
		return null;
	}
	
	protected String getTextPath(String modelPath){
		HashMap<String,String> modelToTextPathes = this.getModelPathesToTextPathes();
		if(modelToTextPathes.containsKey(modelPath)){
			return modelToTextPathes.get(modelPath);
		}
		return null;
	}
	
	protected String abs(String projectRelativePath){
		if(projectRelativePath.startsWith("./"))
			projectRelativePath = projectRelativePath.substring(1);
		if(!projectRelativePath.startsWith("/"))
			projectRelativePath = "/" + projectRelativePath;
		String result = project.getLocation().toString() + projectRelativePath;
		return result;
	}

}
