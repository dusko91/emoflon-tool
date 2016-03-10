package org.moflon.tgg.democles.compiler.util;

import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.gervarro.democles.specification.emf.ConstraintParameter;
import org.gervarro.democles.specification.emf.Pattern;
import org.gervarro.democles.specification.emf.PatternBody;
import org.gervarro.democles.specification.emf.SpecificationFactory;
import org.gervarro.democles.specification.emf.Variable;
import org.gervarro.democles.specification.emf.constraint.emf.emf.EMFTypeFactory;
import org.gervarro.democles.specification.emf.constraint.emf.emf.EMFVariable;
import org.gervarro.democles.specification.emf.constraint.emf.emf.Reference;
import org.moflon.tgg.democles.compiler.patterntype.CorePatternTypes;
import org.moflon.tgg.language.DomainType;
import org.moflon.tgg.language.TGGLinkVariable;
import org.moflon.tgg.language.TGGObjectVariable;

import SDMLanguage.patterns.BindingOperator;
import SDMLanguage.patterns.BindingSemantics;
import SDMLanguage.patterns.LinkVariable;
import SDMLanguage.patterns.ObjectVariable;

public class CompilerUtil {
   
   public static Pattern createEmptyPattern(String name)
   {
      Pattern pattern = SpecificationFactory.eINSTANCE.createPattern();
      pattern.setName(name);
      PatternBody pbody = SpecificationFactory.eINSTANCE.createPatternBody();
      pattern.getBodies().add(pbody);
      return pattern;
   }

	public static EMFVariable createEMFVariable(ObjectVariable ov) {
		EMFVariable ev = EMFTypeFactory.eINSTANCE.createEMFVariable();
		ev.setName(ov.getName());
		ev.setEClassifier(ov.getType());
		return ev;
	}

	public static void createReference(TGGLinkVariable lv, Pattern pattern) {
	   
	   EClass eClass = (EClass) lv.getSource().getType();
	   EReference eRef = eClass.getEAllReferences().stream().filter(r -> r.getName().equals(lv.getName())).findAny().get();
	   
		Reference reference = EMFTypeFactory.eINSTANCE.createReference();
		reference.setEModelElement(eRef);
		pattern.getBodies().get(0).getConstraints().add(reference);

		ConstraintParameter param1 = SpecificationFactory.eINSTANCE.createConstraintParameter();
		reference.getParameters().add(param1);
		param1.setReference(getEMFVariable(pattern, lv.getSource().getName()));

		ConstraintParameter param2 = SpecificationFactory.eINSTANCE.createConstraintParameter();
		reference.getParameters().add(param2);
		param2.setReference(getEMFVariable(pattern, lv.getTarget().getName()));
	}
	
	public static ConstraintParameter createConstraintParameter(Variable var){
	   ConstraintParameter cp = SpecificationFactory.eINSTANCE.createConstraintParameter();
	   cp.setReference(var);
	   return cp;
	}

	public static Variable getEMFVariable(Pattern pattern, String name) {
		Optional<Variable> result;
		result = pattern.getSymbolicParameters().stream().filter(v -> v.getName().equals(name)).findAny();
		if (result.isPresent())
			return result.get();
		result = pattern.getBodies().get(0).getLocalVariables().stream().filter(v -> v.getName().equals(name))
				.findAny();
		return result.get();
	}

	public static boolean isInDomain(ObjectVariable ov, DomainType domainType) {
		return ((TGGObjectVariable)ov).getDomain().getType().equals(domainType);
	}

	public static boolean isInDomain(LinkVariable lv, DomainType domainType) {
		return ((TGGLinkVariable)lv).getDomain().getType().equals(domainType);
	}

	public static boolean isContextInDomain(ObjectVariable ov, DomainType domainType) {
		return isInDomain(ov, domainType) && ov.getBindingOperator().equals(BindingOperator.CHECK_ONLY);
	}

	public static boolean isContextInDomain(LinkVariable lv, DomainType domainType) {
		return isInDomain(lv, domainType) && lv.getBindingOperator().equals(BindingOperator.CHECK_ONLY);
	}

	public static boolean isContext(ObjectVariable ov) {
		return isContextInDomain(ov, DomainType.SOURCE) || isContextInDomain(ov, DomainType.CORRESPONDENCE)
				|| isContextInDomain(ov, DomainType.TARGET);
	}

	public static boolean isContext(LinkVariable lv) {
		return isContextInDomain(lv, DomainType.SOURCE) || isContextInDomain(lv, DomainType.CORRESPONDENCE)
				|| isContextInDomain(lv, DomainType.TARGET);
	}

	public static boolean isCreateInDomain(ObjectVariable ov, DomainType domainType) {
		return isInDomain(ov, domainType) && ov.getBindingOperator().equals(BindingOperator.CREATE);
	}

	public static boolean isCreateInDomain(LinkVariable lv, DomainType domainType) {
		return isInDomain(lv, domainType) && lv.getBindingOperator().equals(BindingOperator.CREATE);
	}

	public static boolean isCreate(ObjectVariable ov) {
		return isCreateInDomain(ov, DomainType.SOURCE) || isCreateInDomain(ov, DomainType.CORRESPONDENCE)
				|| isCreateInDomain(ov, DomainType.TARGET);
	}

	public static boolean isCreate(LinkVariable lv) {
		return isCreateInDomain(lv, DomainType.SOURCE) || isCreateInDomain(lv, DomainType.CORRESPONDENCE)
				|| isCreateInDomain(lv, DomainType.TARGET);
	}
	
	public static boolean isNegativeInDomain(ObjectVariable ov, DomainType domainType) {
	   return isInDomain(ov, domainType) && ov.getBindingSemantics().equals(BindingSemantics.NEGATIVE);
	}
	
	public static boolean isNegativeInDomain(LinkVariable lv, DomainType domainType) {
      return isInDomain(lv, domainType) && lv.getBindingSemantics().equals(BindingSemantics.NEGATIVE);
   }
}
