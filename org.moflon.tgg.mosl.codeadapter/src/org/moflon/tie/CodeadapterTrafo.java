package org.moflon.tie;

import java.io.IOException;
import org.apache.log4j.BasicConfigurator;
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
import org.moflon.tgg.mosl.codeadapter.CodeadapterPackage;
import org.moflon.tgg.mosl.codeadapter.CorrVariablePatternToTGGObjectVariable;
import org.moflon.tgg.mosl.codeadapter.LinkVariablePatternToTGGLinkVariable;
import org.moflon.tgg.mosl.codeadapter.ObjectVariablePatternToTGGObjectVariable;
import org.moflon.tgg.mosl.codeadapter.TripleGraphGrammarFileToTripleGraphGrammar;
import org.moflon.tgg.mosl.tgg.CorrType;
import org.moflon.tgg.mosl.tgg.CorrTypeDef;
import org.moflon.tgg.mosl.tgg.CorrVariablePattern;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile;
import org.moflon.tgg.runtime.RuntimePackage;
import org.moflon.tgg.tggproject.TGGProject;

import SDMLanguage.patterns.LinkVariable;


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
	
	
	public void postProcessForward(EPackage corrPackage){		
		TripleGraphGrammarFile tggFile = (TripleGraphGrammarFile) getSrc();
		TripleGraphGrammar tgg = ((TGGProject) getTrg()).getTgg();

		
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
				
				for (Domain domain : tgg.getDomain()) {
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
}