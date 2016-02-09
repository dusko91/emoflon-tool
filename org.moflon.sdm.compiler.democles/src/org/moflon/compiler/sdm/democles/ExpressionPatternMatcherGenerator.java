package org.moflon.compiler.sdm.democles;

import org.gervarro.democles.codegen.Chain;
import org.gervarro.democles.codegen.GeneratorOperation;
import org.gervarro.democles.common.Adornment;
import org.gervarro.democles.compiler.CompilerPatternBody;

public class ExpressionPatternMatcherGenerator extends PatternMatcherGenerator {

	public ExpressionPatternMatcherGenerator(PatternMatcherCompiler delegate,
			String patternType) {
		super(delegate, patternType);
	}

	@Override
	public SearchPlanAdapter createSearchPlanAdapter(CompilerPatternBody body, Adornment adornment,
				Chain<GeneratorOperation> searchPlan, boolean multipleMatches) {
		return new ExpressionPatternMatcher(patternType, body, adornment, searchPlan, multipleMatches);
	}
}