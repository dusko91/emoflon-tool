package org.moflon.tie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.moflon.tgg.mosl.codeadapter.CorrTypeToEClass;
import org.moflon.tgg.mosl.codeadapter.CorrVariablePatternToTGGObjectVariable;
import org.moflon.tgg.mosl.codeadapter.ExpressionToExpression;
import org.moflon.tgg.mosl.codeadapter.LinkVariablePatternToTGGLinkVariable;
import org.moflon.tgg.mosl.codeadapter.ObjectVariablePatternToTGGObjectVariable;
import org.moflon.tgg.mosl.codeadapter.TripleGraphGrammarFileToTripleGraphGrammar;
import org.moflon.tgg.mosl.tgg.AttributeExpression;
import org.moflon.tgg.mosl.tgg.CorrType;
import org.moflon.tgg.mosl.tgg.CorrVariablePattern;
import org.moflon.tgg.mosl.tgg.Import;
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
}