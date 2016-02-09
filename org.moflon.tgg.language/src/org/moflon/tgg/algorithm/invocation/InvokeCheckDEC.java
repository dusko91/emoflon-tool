package org.moflon.tgg.algorithm.invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.moflon.tgg.algorithm.datastructures.TripleMatch;
import org.moflon.tgg.language.analysis.RulesTable;
import org.moflon.tgg.runtime.AttributeConstraintsRuleResult;
import org.moflon.tgg.runtime.RuleResult;


public class InvokeCheckDEC implements
		Function<TripleMatch, Boolean> {

	private RulesTable lookupMethods;
	
	public InvokeCheckDEC(RulesTable lookupMethods) {
		this.lookupMethods = lookupMethods;
	}

	@Override
	public Boolean apply(TripleMatch match) {
		
		EOperation checkDECop = lookupMethods.getRules().stream().filter(r -> r.getRuleName().equals(match.getRuleName())).findAny().get().getCheckDECMethod();
		EList<EParameter> checkDECparameters = checkDECop.getEParameters();
		
		EList<EObject> parameterBindings = new BasicEList<>();
		for(int i = 0; i < checkDECparameters.size(); i++){
			parameterBindings.add(i, match.getNode(checkDECparameters.get(i).getName()));
		}		
		return (Boolean) InvokeUtil.invokeOperationWithNArguments(EcoreUtil.create(checkDECop.getEContainingClass()), checkDECop, parameterBindings);   
	}
}