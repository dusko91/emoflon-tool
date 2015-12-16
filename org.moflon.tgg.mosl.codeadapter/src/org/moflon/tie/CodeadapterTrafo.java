package org.moflon.tie;

import java.io.IOException;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.moflon.tgg.algorithm.synchronization.SynchronizationHelper;
import org.moflon.tgg.language.Domain;
import org.moflon.tgg.language.DomainType;
import org.moflon.tgg.language.TGGLinkVariable;
import org.moflon.tgg.language.TripleGraphGrammar;
import org.moflon.tgg.language.csp.AttributeVariable;
import org.moflon.tgg.language.csp.Literal;
import org.moflon.tgg.language.csp.LocalVariable;
import org.moflon.tgg.language.csp.Variable;
import org.moflon.tgg.mosl.codeadapter.AttrCondToTGGConstraint;
import org.moflon.tgg.mosl.codeadapter.AttributeAssignmentToAttributeAssignment;
import org.moflon.tgg.mosl.codeadapter.AttributeConstraintToConstraint;
import org.moflon.tgg.mosl.codeadapter.CodeadapterPackage;
import org.moflon.tgg.mosl.codeadapter.CorrVariablePatternToTGGObjectVariable;
import org.moflon.tgg.mosl.codeadapter.ExpressionToExpression;
import org.moflon.tgg.mosl.codeadapter.LinkVariablePatternToTGGLinkVariable;
import org.moflon.tgg.mosl.codeadapter.ObjectVariablePatternToTGGObjectVariable;
import org.moflon.tgg.mosl.codeadapter.ParamValueToVariable;
import org.moflon.tgg.mosl.codeadapter.TripleGraphGrammarFileToTripleGraphGrammar;
import org.moflon.tgg.mosl.tgg.AttrCond;
import org.moflon.tgg.mosl.tgg.AttributeExpression;
import org.moflon.tgg.mosl.tgg.CorrType;
import org.moflon.tgg.mosl.tgg.CorrTypeDef;
import org.moflon.tgg.mosl.tgg.CorrVariablePattern;
import org.moflon.tgg.mosl.tgg.Import;
import org.moflon.tgg.mosl.tgg.ObjectVariablePattern;
import org.moflon.tgg.mosl.tgg.ParamValue;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.Schema;
import org.moflon.tgg.mosl.tgg.TggFactory;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile;
import org.moflon.tgg.mosl.tgg.Using;
import org.moflon.tgg.runtime.RuntimePackage;
import org.moflon.tgg.tggproject.TGGProject;

import SDMLanguage.expressions.ComparisonExpression;
import SDMLanguage.patterns.LinkVariable;
import SDMLanguage.patterns.patternExpressions.AttributeValueExpression;


public class CodeadapterTrafo extends SynchronizationHelper{

   public CodeadapterTrafo(String pathToProject)
   {
	   super(CodeadapterPackage.eINSTANCE, pathToProject);
   }
   
   public CodeadapterTrafo(String pathToProject, ResourceSet set)
   {
	   super(CodeadapterPackage.eINSTANCE, pathToProject, set);
   }
   
   public CodeadapterTrafo()
   {
//	   CodeadapterPackage.eINSTANCE.toString();
//	   System.out.println(CodeadapterPackage.eINSTANCE);
      super(CodeadapterPackage.eINSTANCE, ".");
   }

	public static void main(String[] args) throws IOException {
		// Set up logging
        BasicConfigurator.configure();

		// Forward Transformation
        CodeadapterTrafo helper = new CodeadapterTrafo();
        helper.setVerbose(true);
		helper.performForward("instances/fwd.src.xmi");
		
		// Backward Transformation
		helper = new CodeadapterTrafo();
		helper.performBackward("instances/bwd.src.xmi");
	}

	public void performForward() {
		integrateForward();

		saveTrg("instances/fwd.trg.xmi");
		saveCorr("instances/fwd.corr.xmi");
		saveSynchronizationProtocol("instances/fwd.protocol.xmi");

		System.out.println("Completed forward transformation!");
	}

	public void performForward(EObject srcModel) {
		setSrc(srcModel);
		performForward();
	}

	public void performForward(String source) {
		try {
			loadSrc(source);
			performForward();
		} catch (IllegalArgumentException iae) {
			System.err.println("Unable to load " + source + ", "
					+ iae.getMessage());
			return;
		}
	}

	public void performBackward() {
		integrateBackward();

		saveSrc("instances/bwd.trg.xmi");
		saveCorr("instances/bwd.corr.xmi");
		saveSynchronizationProtocol("instances/bwd.protocol.xmi");

		System.out.println("Completed backward transformation!");
	}

	public void performBackward(EObject targetModel) {
		setTrg(targetModel);
		performBackward();
	}

	public void performBackward(String target) {
		try {
			loadTrg(target);
			performBackward();
		} catch (IllegalArgumentException iae) {
			System.err.println("Unable to load " + target + ", "
					+ iae.getMessage());
			return;
		}
	}
	
	
	public void postProcessForward(){
		TripleGraphGrammarFile tggFile = (TripleGraphGrammarFile) getSrc();
		TGGProject tggProject = (TGGProject) getTrg();
		EPackage corrPackage = tggProject.getCorrPackage();

		
		for (EClassifier classifier : corrPackage.getEClassifiers()) {
			if (classifier instanceof EClass) {
				EClass corr = (EClass) classifier;
				for (CorrType corrType : tggFile.getSchema().getCorrespondenceTypes()) {
					if(corrType instanceof CorrTypeDef && corrType.getName().equals(corr.getName())){
						CorrTypeDef corrTypeDef = (CorrTypeDef) corrType;
						EReference ref = EcoreFactory.eINSTANCE.createEReference();
						ref.setName("source");
						ref.setLowerBound(1);
						ref.setEType(corrTypeDef.getSource());
						corr.getEStructuralFeatures().add(ref);
						
						ref = EcoreFactory.eINSTANCE.createEReference();
						ref.setName("target");
						ref.setLowerBound(1);
						ref.setEType(corrTypeDef.getTarget());
						corr.getEStructuralFeatures().add(ref);
					}
				}
				corr.getESuperTypes().add(RuntimePackage.Literals.ABSTRACT_CORRESPONDENCE);
			}
		}
		
		for (EPackage pkg : corrPackage.getESubpackages()) {
			if (pkg.getName().equals("Rules")) {
				for (EClassifier classifier : pkg.getEClassifiers()) {
					if (classifier instanceof EClass) {
						EClass rule = (EClass) classifier;
						rule.getESuperTypes().add(RuntimePackage.Literals.ABSTRACT_RULE);
					}
				}
			}
		}		
		
		for (EObject corr : getCorr().getCorrespondences()) {
			if(corr instanceof TripleGraphGrammarFileToTripleGraphGrammar){
				TripleGraphGrammarFileToTripleGraphGrammar tggCorr = (TripleGraphGrammarFileToTripleGraphGrammar) corr;
				
				for (Domain domain : tggProject.getTgg().getDomain()) {
					if(domain.getType() == DomainType.SOURCE){
						EPackage sourceType = tggCorr.getSource().getSchema().getSourceTypes().get(0);
						domain.getMetamodel().setOutermostPackage(sourceType);
						domain.getMetamodel().setName(sourceType.getName());
					}
					if(domain.getType() == DomainType.TARGET){
						EPackage targetType = tggCorr.getSource().getSchema().getTargetTypes().get(0);
						domain.getMetamodel().setOutermostPackage(targetType);
						domain.getMetamodel().setName(targetType.getName());
					}
					if(domain.getType() == DomainType.CORRESPONDENCE){
						domain.getMetamodel().setOutermostPackage(corrPackage);
						domain.getMetamodel().setName(corrPackage.getName());
					}
				}
			}
			// Inline Assignments / Constraints
			if(corr instanceof AttributeAssignmentToAttributeAssignment){
				AttributeAssignmentToAttributeAssignment attrCorr = (AttributeAssignmentToAttributeAssignment) corr;
				attrCorr.getTarget().setAttribute(attrCorr.getSource().getAttribute());
			}
			if(corr instanceof AttributeConstraintToConstraint){
				AttributeConstraintToConstraint attrCorr = (AttributeConstraintToConstraint) corr;
				ComparisonExpression compExp = (ComparisonExpression) attrCorr.getTarget().getConstraintExpression();
				
				((AttributeValueExpression) compExp.getLeftExpression()).setAttribute(attrCorr.getSource().getAttribute());
			}
			if(corr instanceof ExpressionToExpression){
				ExpressionToExpression attrExpCorr = (ExpressionToExpression) corr;
				if(attrExpCorr.getTarget() instanceof AttributeValueExpression){
					((AttributeValueExpression) attrExpCorr.getTarget()).setAttribute(((AttributeExpression) attrExpCorr.getSource()).getAttribute());
				}
			}

			// CSP PostProcessing
			if(corr instanceof AttrCondToTGGConstraint){
				AttrCondToTGGConstraint cspCorr = (AttrCondToTGGConstraint) corr;
				
				EList<Variable> trgVariables = new BasicEList<Variable>();
				
				for (ParamValue paramVal : cspCorr.getSource().getValues()) {
					for (Variable var : cspCorr.getTarget().getVariables()) {
						if(var instanceof AttributeVariable && paramVal instanceof org.moflon.tgg.mosl.tgg.AttributeVariable){
							org.moflon.tgg.mosl.tgg.AttributeVariable srcAttr = (org.moflon.tgg.mosl.tgg.AttributeVariable) paramVal;
							AttributeVariable trgAttr = (AttributeVariable) var;
							if(srcAttr.getAttribute().equals(trgAttr.getAttribute()) && srcAttr.getObjectVar().getName().equals(trgAttr.getObjectVariable())) trgVariables.add(trgAttr);
						}
						if(var instanceof LocalVariable && paramVal instanceof org.moflon.tgg.mosl.tgg.LocalVariable){
							org.moflon.tgg.mosl.tgg.LocalVariable srcAttr = (org.moflon.tgg.mosl.tgg.LocalVariable) paramVal;
							LocalVariable trgAttr = (LocalVariable) var;
							if(srcAttr.getName().equals(trgAttr.getName())) trgVariables.add(trgAttr);
						}
						if(var instanceof Literal && paramVal instanceof org.moflon.tgg.mosl.tgg.Literal){
							org.moflon.tgg.mosl.tgg.Literal srcAttr = (org.moflon.tgg.mosl.tgg.Literal) paramVal;
							Literal trgAttr = (Literal) var;
							if(srcAttr.getValue().equals(trgAttr.getValue())) trgVariables.add(trgAttr);
						}
					}
				}
				
				cspCorr.getTarget().getVariables().clear();
				cspCorr.getTarget().getVariables().addAll(trgVariables);
				
			}
			
			
			if(corr instanceof ObjectVariablePatternToTGGObjectVariable){
				ObjectVariablePatternToTGGObjectVariable ovCorr = (ObjectVariablePatternToTGGObjectVariable) corr;
				ovCorr.getTarget().setType(ovCorr.getSource().getType());
			}

			if(corr instanceof LinkVariablePatternToTGGLinkVariable){
				LinkVariablePatternToTGGLinkVariable lvCorr = (LinkVariablePatternToTGGLinkVariable) corr;
				TGGLinkVariable tggLV = lvCorr.getTarget();
				EReference lvType = lvCorr.getSource().getType();
				
				tggLV.setType(lvType);
				tggLV.setName(lvType.getName());
			}
			
			if(corr instanceof CorrVariablePatternToTGGObjectVariable){
				CorrVariablePatternToTGGObjectVariable cvCorr = (CorrVariablePatternToTGGObjectVariable) corr;
				CorrVariablePattern corrVarPattern = cvCorr.getSource();
				
				EClass absCorr = (EClass) corrPackage.getEClassifier(corrVarPattern.getType().getName());
				cvCorr.getTarget().setType(absCorr);
				
				for (LinkVariable lv : cvCorr.getTarget().getOutgoingLink()) {
					if (lv.getName().equals("source")) {
						lv.setType((EReference) absCorr.getEStructuralFeature("source"));
					}
					if (lv.getName().equals("target")) {
						lv.setType((EReference) absCorr.getEStructuralFeature("target"));
					}
				}
			}
		}
	}

	public void postProcessBackward() {
		TripleGraphGrammarFile tggFile = (TripleGraphGrammarFile) getSrc();
		Schema moslSchema = tggFile.getSchema();
		TripleGraphGrammar tgg = ((TGGProject) getTrg()).getTgg();
		
		Import importName;
		Using using = TggFactory.eINSTANCE.createUsing();
		using.setImportedNamespace("ecore.*");
		moslSchema.getUsing().add(using);
		
		for (Domain domain : tgg.getDomain()) {
			if(domain.getType() == DomainType.SOURCE){
				EPackage outermostPackage = domain.getMetamodel().getOutermostPackage();
				importName = TggFactory.eINSTANCE.createImport();
				importName.setName("platform:/resource/"+outermostPackage.getNsPrefix()+"/model/"+outermostPackage.getNsPrefix()+".ecore");
				moslSchema.getImports().add(importName);
				
				moslSchema.getSourceTypes().add(outermostPackage);
			}
			if(domain.getType() == DomainType.TARGET){
				EPackage outermostPackage = domain.getMetamodel().getOutermostPackage();
				importName = TggFactory.eINSTANCE.createImport();
				importName.setName("platform:/resource/"+outermostPackage.getNsPrefix()+"/model/"+outermostPackage.getNsPrefix()+".ecore");
				moslSchema.getImports().add(importName);
				
				moslSchema.getTargetTypes().add(outermostPackage);
			}
			if(domain.getType() == DomainType.CORRESPONDENCE){
				for (CorrType corrType : moslSchema.getCorrespondenceTypes()) {
					if (corrType instanceof CorrTypeDef) {
						CorrTypeDef corrTypeDef = (CorrTypeDef) corrType;
						for (EClassifier classifier : domain.getMetamodel().getOutermostPackage().getEClassifiers()) {
							if (classifier instanceof EClass && classifier.getName().equals(corrTypeDef.getName())) {
								EClass corr = (EClass) classifier;
								for (EReference ref : corr.getEAllReferences()) {
									if (ref.getName().equals("source")) {
										corrTypeDef.setSource((EClass) ref.getEType());
									}
									if (ref.getName().equals("target")) {
										corrTypeDef.setTarget((EClass) ref.getEType());
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		for (Rule rule : tggFile.getRules()) {
			using = TggFactory.eINSTANCE.createUsing();
			using.setImportedNamespace(rule.getSchema().getName() +".*");
			rule.getUsing().add(using);
		}

		for (EObject corr : getCorr().getCorrespondences()) {

			// Inline Assignments / Constraints
			if(corr instanceof AttributeAssignmentToAttributeAssignment){
				AttributeAssignmentToAttributeAssignment attrCorr = (AttributeAssignmentToAttributeAssignment) corr;
				attrCorr.getSource().setAttribute(attrCorr.getTarget().getAttribute());
			}
			if(corr instanceof AttributeConstraintToConstraint){
				AttributeConstraintToConstraint attrCorr = (AttributeConstraintToConstraint) corr;
				ComparisonExpression compExp = (ComparisonExpression) attrCorr.getTarget().getConstraintExpression();
				attrCorr.getSource().setAttribute(((AttributeValueExpression) compExp.getLeftExpression()).getAttribute());
			}
			if(corr instanceof ExpressionToExpression){
				ExpressionToExpression attrExpCorr = (ExpressionToExpression) corr;
				if(attrExpCorr.getTarget() instanceof AttributeValueExpression){
					System.out.println("Reached");
					((AttributeExpression) attrExpCorr.getSource()).setAttribute(((AttributeValueExpression) attrExpCorr.getTarget()).getAttribute());
				}
			}
			
			// CSP PostProcessing
			if(corr instanceof ParamValueToVariable){
				ParamValueToVariable cspCorr = (ParamValueToVariable) corr;
				
				if (cspCorr.getTarget() instanceof AttributeVariable) {
					AttributeVariable trgAttr = (AttributeVariable) cspCorr.getTarget();
					org.moflon.tgg.mosl.tgg.AttributeVariable srcAttr = (org.moflon.tgg.mosl.tgg.AttributeVariable) cspCorr.getSource();
					
					Rule rule = (Rule) srcAttr.eContainer().eContainer();
					String ovName = trgAttr.getObjectVariable();

					for (ObjectVariablePattern ov : rule.getSourcePatterns()) {
						if(ov.getName().equals(ovName)) srcAttr.setObjectVar(ov);
					}
					for (ObjectVariablePattern ov : rule.getTargetPatterns()) {
						if(ov.getName().equals(ovName)) srcAttr.setObjectVar(ov);
					}
				}
			}
			
			if(corr instanceof ObjectVariablePatternToTGGObjectVariable){
				ObjectVariablePatternToTGGObjectVariable ovCorr = (ObjectVariablePatternToTGGObjectVariable) corr;
				ovCorr.getSource().setType((EClass) ovCorr.getTarget().getType());
			}

			if(corr instanceof LinkVariablePatternToTGGLinkVariable){
				LinkVariablePatternToTGGLinkVariable lvCorr = (LinkVariablePatternToTGGLinkVariable) corr;
				lvCorr.getSource().setType(lvCorr.getTarget().getType());
			}
			
			if(corr instanceof CorrVariablePatternToTGGObjectVariable){
				CorrVariablePatternToTGGObjectVariable cvCorr = (CorrVariablePatternToTGGObjectVariable) corr;
				for (CorrType corrType : moslSchema.getCorrespondenceTypes()) {
					if(corrType.getName().equals(cvCorr.getTarget().getType().getName())) {
						cvCorr.getSource().setType(corrType);
					}
				}
			}
		}
	}
}

















