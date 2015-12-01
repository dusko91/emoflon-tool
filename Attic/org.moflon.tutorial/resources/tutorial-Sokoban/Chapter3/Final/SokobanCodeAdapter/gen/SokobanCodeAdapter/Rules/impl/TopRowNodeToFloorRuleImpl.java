/**
 */
package SokobanCodeAdapter.Rules.impl;

import MocaTree.File;
import MocaTree.MocaTreeFactory;
import MocaTree.Node;

import SokobanCodeAdapter.NodeToBoard;
import SokobanCodeAdapter.NodeToFloor;

import SokobanCodeAdapter.Rules.RulesPackage;
import SokobanCodeAdapter.Rules.TopRowNodeToFloorRule;

import SokobanCodeAdapter.SokobanCodeAdapterFactory;

import SokobanLanguage.Admin;
import SokobanLanguage.Board;
import SokobanLanguage.Floor;
import SokobanLanguage.Goal;
import SokobanLanguage.Server;
import SokobanLanguage.SokobanLanguageFactory;
import SokobanLanguage.Wall;

import TGGLanguage.csp.*;

import TGGRuntime.Edge;
import TGGRuntime.IsApplicableMatch;
import TGGRuntime.IsApplicableRuleResult;
import TGGRuntime.IsAppropriateRuleResult;
import TGGRuntime.Match;
import TGGRuntime.PerformRuleResult;
import TGGRuntime.TGGRuntimeFactory;

import TGGRuntime.impl.AbstractRuleImpl;

import csp.constraints.*;

import de.upb.tools.sdm.*;

import java.lang.reflect.InvocationTargetException;

import java.util.*;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Top Row Node To Floor Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class TopRowNodeToFloorRuleImpl extends AbstractRuleImpl implements
		TopRowNodeToFloorRule {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TopRowNodeToFloorRuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RulesPackage.Literals.TOP_ROW_NODE_TO_FLOOR_RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PerformRuleResult perform_FWD(IsApplicableMatch isApplicableMatch) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		Board board = null;
		Node boardNode = null;
		Node columnNode = null;
		Floor leftNeighbor = null;
		NodeToBoard nodeToBoard = null;
		Node rowNode = null;
		Iterator fujaba__IterIsApplicableMatchToCsp = null;
		CSP csp = null;
		NodeToFloor nodeToFloor = null;
		Floor floor = null;
		PerformRuleResult ruleresult = null;
		Edge rowNode__children__columnNode = null;
		Edge nodeToFloor__source__columnNode = null;
		Edge nodeToFloor__target__floor = null;
		Edge floor__left__leftNeighbor = null;
		Edge board__floors__floor = null;

		// story node 'perform transformation'
		try {
			fujaba__Success = false;

			_TmpObject = (isApplicableMatch.getObject("board"));

			// ensure correct type and really bound of object board
			JavaSDM.ensure(_TmpObject instanceof Board);
			board = (Board) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("boardNode"));

			// ensure correct type and really bound of object boardNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			boardNode = (Node) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("columnNode"));

			// ensure correct type and really bound of object columnNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			columnNode = (Node) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("leftNeighbor"));

			// ensure correct type and really bound of object leftNeighbor
			JavaSDM.ensure(_TmpObject instanceof Floor);
			leftNeighbor = (Floor) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("nodeToBoard"));

			// ensure correct type and really bound of object nodeToBoard
			JavaSDM.ensure(_TmpObject instanceof NodeToBoard);
			nodeToBoard = (NodeToBoard) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("rowNode"));

			// ensure correct type and really bound of object rowNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			rowNode = (Node) _TmpObject;
			// check object isApplicableMatch is really bound
			JavaSDM.ensure(isApplicableMatch != null);
			// check isomorphic binding between objects columnNode and boardNode 
			JavaSDM.ensure(!columnNode.equals(boardNode));

			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// check isomorphic binding between objects rowNode and columnNode 
			JavaSDM.ensure(!rowNode.equals(columnNode));

			// iterate to-many link attributeInfo from isApplicableMatch to csp
			fujaba__Success = false;

			fujaba__IterIsApplicableMatchToCsp = isApplicableMatch
					.getAttributeInfo().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterIsApplicableMatchToCsp.hasNext()) {
				try {
					_TmpObject = fujaba__IterIsApplicableMatchToCsp.next();

					// ensure correct type and really bound of object csp
					JavaSDM.ensure(_TmpObject instanceof CSP);
					csp = (CSP) _TmpObject;

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			if (!fujaba__Success) {
				fujaba__Success = true;
				csp = null;
			}
			// attribute condition
			JavaSDM.ensure(rowNode.getIndex() == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(boardNode.getName(), "BOARD") == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(columnNode.getName(), "COLUMN") == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(rowNode.getName(), "ROW") == 0);

			// attribute condition
			JavaSDM.ensure(leftNeighbor.getRow() == 0);

			// create object nodeToFloor
			nodeToFloor = SokobanCodeAdapterFactory.eINSTANCE
					.createNodeToFloor();

			// create object floor
			floor = SokobanLanguageFactory.eINSTANCE.createFloor();

			// assign attribute floor
			floor.setRow(0);
			// assign attribute floor
			floor.setCol(((Number) csp.getAttributeVariable("floor", "col")
					.getValue()).intValue());

			// create link
			nodeToFloor.setSource(columnNode);

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(nodeToFloor,
					floor, "target");

			// create link
			floor.setBoard(board);

			// create link
			floor.setLeft(leftNeighbor);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'collect translated elements'
		try {
			fujaba__Success = false;

			// check object columnNode is really bound
			JavaSDM.ensure(columnNode != null);
			// check object floor is really bound
			JavaSDM.ensure(floor != null);
			// check object nodeToFloor is really bound
			JavaSDM.ensure(nodeToFloor != null);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE.createPerformRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(true);

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					nodeToFloor, "createdLinkElements");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					floor, "createdElements");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					columnNode, "translatedElements");
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'bookkeeping for edges'
		try {
			fujaba__Success = false;

			// check object board is really bound
			JavaSDM.ensure(board != null);
			// check object boardNode is really bound
			JavaSDM.ensure(boardNode != null);
			// check object columnNode is really bound
			JavaSDM.ensure(columnNode != null);
			// check object floor is really bound
			JavaSDM.ensure(floor != null);
			// check object leftNeighbor is really bound
			JavaSDM.ensure(leftNeighbor != null);
			// check object nodeToBoard is really bound
			JavaSDM.ensure(nodeToBoard != null);
			// check object nodeToFloor is really bound
			JavaSDM.ensure(nodeToFloor != null);
			// check object rowNode is really bound
			JavaSDM.ensure(rowNode != null);
			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// check isomorphic binding between objects boardNode and board 
			JavaSDM.ensure(!boardNode.equals(board));

			// check isomorphic binding between objects columnNode and board 
			JavaSDM.ensure(!columnNode.equals(board));

			// check isomorphic binding between objects floor and board 
			JavaSDM.ensure(!floor.equals(board));

			// check isomorphic binding between objects leftNeighbor and board 
			JavaSDM.ensure(!leftNeighbor.equals(board));

			// check isomorphic binding between objects nodeToBoard and board 
			JavaSDM.ensure(!nodeToBoard.equals(board));

			// check isomorphic binding between objects nodeToFloor and board 
			JavaSDM.ensure(!nodeToFloor.equals(board));

			// check isomorphic binding between objects rowNode and board 
			JavaSDM.ensure(!rowNode.equals(board));

			// check isomorphic binding between objects columnNode and boardNode 
			JavaSDM.ensure(!columnNode.equals(boardNode));

			// check isomorphic binding between objects floor and boardNode 
			JavaSDM.ensure(!floor.equals(boardNode));

			// check isomorphic binding between objects leftNeighbor and boardNode 
			JavaSDM.ensure(!leftNeighbor.equals(boardNode));

			// check isomorphic binding between objects nodeToBoard and boardNode 
			JavaSDM.ensure(!nodeToBoard.equals(boardNode));

			// check isomorphic binding between objects nodeToFloor and boardNode 
			JavaSDM.ensure(!nodeToFloor.equals(boardNode));

			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// check isomorphic binding between objects floor and columnNode 
			JavaSDM.ensure(!floor.equals(columnNode));

			// check isomorphic binding between objects leftNeighbor and columnNode 
			JavaSDM.ensure(!leftNeighbor.equals(columnNode));

			// check isomorphic binding between objects nodeToBoard and columnNode 
			JavaSDM.ensure(!nodeToBoard.equals(columnNode));

			// check isomorphic binding between objects nodeToFloor and columnNode 
			JavaSDM.ensure(!nodeToFloor.equals(columnNode));

			// check isomorphic binding between objects rowNode and columnNode 
			JavaSDM.ensure(!rowNode.equals(columnNode));

			// check isomorphic binding between objects leftNeighbor and floor 
			JavaSDM.ensure(!leftNeighbor.equals(floor));

			// check isomorphic binding between objects nodeToBoard and floor 
			JavaSDM.ensure(!nodeToBoard.equals(floor));

			// check isomorphic binding between objects nodeToFloor and floor 
			JavaSDM.ensure(!nodeToFloor.equals(floor));

			// check isomorphic binding between objects rowNode and floor 
			JavaSDM.ensure(!rowNode.equals(floor));

			// check isomorphic binding between objects nodeToBoard and leftNeighbor 
			JavaSDM.ensure(!nodeToBoard.equals(leftNeighbor));

			// check isomorphic binding between objects nodeToFloor and leftNeighbor 
			JavaSDM.ensure(!nodeToFloor.equals(leftNeighbor));

			// check isomorphic binding between objects rowNode and leftNeighbor 
			JavaSDM.ensure(!rowNode.equals(leftNeighbor));

			// check isomorphic binding between objects nodeToFloor and nodeToBoard 
			JavaSDM.ensure(!nodeToFloor.equals(nodeToBoard));

			// check isomorphic binding between objects rowNode and nodeToBoard 
			JavaSDM.ensure(!rowNode.equals(nodeToBoard));

			// check isomorphic binding between objects rowNode and nodeToFloor 
			JavaSDM.ensure(!rowNode.equals(nodeToFloor));

			// create object rowNode__children__columnNode
			rowNode__children__columnNode = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// create object nodeToFloor__source__columnNode
			nodeToFloor__source__columnNode = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// create object nodeToFloor__target__floor
			nodeToFloor__target__floor = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// create object floor__left__leftNeighbor
			floor__left__leftNeighbor = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// create object board__floors__floor
			board__floors__floor = TGGRuntimeFactory.eINSTANCE.createEdge();

			// assign attribute rowNode__children__columnNode
			rowNode__children__columnNode.setName("children");
			// assign attribute nodeToFloor__source__columnNode
			nodeToFloor__source__columnNode.setName("source");
			// assign attribute nodeToFloor__target__floor
			nodeToFloor__target__floor.setName("target");
			// assign attribute board__floors__floor
			board__floors__floor.setName("floors");
			// assign attribute floor__left__leftNeighbor
			floor__left__leftNeighbor.setName("left");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					rowNode__children__columnNode, "translatedEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					nodeToFloor__source__columnNode, "createdEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					nodeToFloor__target__floor, "createdEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					floor__left__leftNeighbor, "createdEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					board__floors__floor, "createdEdges");

			// create link
			rowNode__children__columnNode.setSrc(rowNode);

			// create link
			rowNode__children__columnNode.setTrg(columnNode);

			// create link
			nodeToFloor__source__columnNode.setTrg(columnNode);

			// create link
			nodeToFloor__source__columnNode.setSrc(nodeToFloor);

			// create link
			nodeToFloor__target__floor.setSrc(nodeToFloor);

			// create link
			board__floors__floor.setSrc(board);

			// create link
			floor__left__leftNeighbor.setTrg(leftNeighbor);

			// create link
			nodeToFloor__target__floor.setTrg(floor);

			// create link
			board__floors__floor.setTrg(floor);

			// create link
			floor__left__leftNeighbor.setSrc(floor);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// statement node 'perform postprocessing'
		// No post processing method found
		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAppropriate_FWD(Match match, Node boardNode, Node rowNode,
			Node columnNode) {
		boolean fujaba__Success = false;
		Edge rowNode__children__columnNode = null;
		Edge boardNode__children__rowNode = null;

		// statement node 'Solve CSP'
		// Create CSP
		CSP csp = CspFactory.eINSTANCE.createCSP();

		// Create literals

		// Create attribute variables

		// Create explicit parameters

		// Create unbound variables

		// Create constraints

		// Solve CSP

		// statement node 'Check CSP'
		fujaba__Success = csp.check();
		if (fujaba__Success) {
			// story node 'collect elements to be translated'
			try {
				fujaba__Success = false;

				// check object columnNode is really bound
				JavaSDM.ensure(columnNode != null);
				// check object match is really bound
				JavaSDM.ensure(match != null);
				// check object rowNode is really bound
				JavaSDM.ensure(rowNode != null);
				// check isomorphic binding between objects rowNode and columnNode 
				JavaSDM.ensure(!rowNode.equals(columnNode));

				// create object rowNode__children__columnNode
				rowNode__children__columnNode = TGGRuntimeFactory.eINSTANCE
						.createEdge();

				// assign attribute rowNode__children__columnNode
				rowNode__children__columnNode.setName("children");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						columnNode, "toBeTranslatedElements");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						rowNode__children__columnNode, "toBeTranslatedEdges");

				// create link
				rowNode__children__columnNode.setTrg(columnNode);

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(
						rowNode__children__columnNode, rowNode, "src");
				fujaba__Success = true;
			} catch (JavaSDMException fujaba__InternalException) {
				fujaba__Success = false;
			}

			// story node 'collect context elements'
			try {
				fujaba__Success = false;

				// check object boardNode is really bound
				JavaSDM.ensure(boardNode != null);
				// check object match is really bound
				JavaSDM.ensure(match != null);
				// check object rowNode is really bound
				JavaSDM.ensure(rowNode != null);
				// check isomorphic binding between objects rowNode and boardNode 
				JavaSDM.ensure(!rowNode.equals(boardNode));

				// create object boardNode__children__rowNode
				boardNode__children__rowNode = TGGRuntimeFactory.eINSTANCE
						.createEdge();

				// assign attribute boardNode__children__rowNode
				boardNode__children__rowNode.setName("children");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						boardNode__children__rowNode, "contextEdges");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						boardNode, "contextNodes");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						rowNode, "contextNodes");

				// create link
				boardNode__children__rowNode.setSrc(boardNode);

				// create link
				boardNode__children__rowNode.setTrg(rowNode);

				fujaba__Success = true;
			} catch (JavaSDMException fujaba__InternalException) {
				fujaba__Success = false;
			}

			// statement node 'register objects to match'
			match.registerObject("boardNode", boardNode);
			match.registerObject("rowNode", rowNode);
			match.registerObject("columnNode", columnNode);

			return true;

		} else {
			return false;

		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsApplicableRuleResult isApplicable_FWD(Match match) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass eClass = null;
		Iterator fujaba__IterEClassToPerformOperation = null;
		EOperation performOperation = null;
		IsApplicableRuleResult ruleresult = null;
		Node boardNode = null;
		Node columnNode = null;
		Node rowNode = null;
		IsApplicableMatch isApplicableMatch = null;
		Iterator fujaba__IterBoardToLeftNeighbor = null;
		Floor leftNeighbor = null;
		Board board = null;
		Iterator fujaba__IterBoardNodeToNodeToBoard = null;
		NodeToBoard nodeToBoard = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from eClass to performOperation
			fujaba__Success = false;

			fujaba__IterEClassToPerformOperation = eClass.getEOperations()
					.iterator();

			while (!(fujaba__Success)
					&& fujaba__IterEClassToPerformOperation.hasNext()) {
				try {
					performOperation = (EOperation) fujaba__IterEClassToPerformOperation
							.next();

					// check object performOperation is really bound
					JavaSDM.ensure(performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							performOperation.getName(), "perform_FWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsApplicableRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);
			// assign attribute ruleresult
			ruleresult.setRule("TopRowNodeToFloorRule");

			// create link
			ruleresult.setPerformOperation(performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'core match'
		try {
			fujaba__Success = false;

			_TmpObject = (match.getObject("boardNode"));

			// ensure correct type and really bound of object boardNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			boardNode = (Node) _TmpObject;
			_TmpObject = (match.getObject("columnNode"));

			// ensure correct type and really bound of object columnNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			columnNode = (Node) _TmpObject;
			_TmpObject = (match.getObject("rowNode"));

			// ensure correct type and really bound of object rowNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			rowNode = (Node) _TmpObject;
			// check object match is really bound
			JavaSDM.ensure(match != null);
			// check isomorphic binding between objects columnNode and boardNode 
			JavaSDM.ensure(!columnNode.equals(boardNode));

			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// check isomorphic binding between objects rowNode and columnNode 
			JavaSDM.ensure(!rowNode.equals(columnNode));

			// attribute condition
			JavaSDM.ensure(rowNode.getIndex() == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(boardNode.getName(), "BOARD") == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(columnNode.getName(), "COLUMN") == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(rowNode.getName(), "ROW") == 0);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'find context'
		try {
			fujaba__Success = false;

			// check object boardNode is really bound
			JavaSDM.ensure(boardNode != null);
			// check object columnNode is really bound
			JavaSDM.ensure(columnNode != null);
			// check object rowNode is really bound
			JavaSDM.ensure(rowNode != null);
			// check isomorphic binding between objects columnNode and boardNode 
			JavaSDM.ensure(!columnNode.equals(boardNode));

			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// check isomorphic binding between objects rowNode and columnNode 
			JavaSDM.ensure(!rowNode.equals(columnNode));

			// check link children from rowNode to boardNode
			JavaSDM.ensure(boardNode.equals(rowNode.getParentNode()));

			// check link children from columnNode to rowNode
			JavaSDM.ensure(rowNode.equals(columnNode.getParentNode()));

			// iterate to-many link source from boardNode to nodeToBoard
			fujaba__Success = false;

			fujaba__IterBoardNodeToNodeToBoard = new ArrayList(
					org.moflon.core.utilities.eMoflonEMFUtil.getOppositeReference(
							boardNode, NodeToBoard.class, "source")).iterator();

			while (fujaba__IterBoardNodeToNodeToBoard.hasNext()) {
				try {
					nodeToBoard = (NodeToBoard) fujaba__IterBoardNodeToNodeToBoard
							.next();

					// check object nodeToBoard is really bound
					JavaSDM.ensure(nodeToBoard != null);
					// bind object
					board = nodeToBoard.getTarget();

					// check object board is really bound
					JavaSDM.ensure(board != null);

					// iterate to-many link floors from board to leftNeighbor
					fujaba__Success = false;

					fujaba__IterBoardToLeftNeighbor = new ArrayList(
							board.getFloors()).iterator();

					while (fujaba__IterBoardToLeftNeighbor.hasNext()) {
						try {
							leftNeighbor = (Floor) fujaba__IterBoardToLeftNeighbor
									.next();

							// check object leftNeighbor is really bound
							JavaSDM.ensure(leftNeighbor != null);
							// attribute condition
							JavaSDM.ensure(rowNode.getIndex() == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									boardNode.getName(), "BOARD") == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									columnNode.getName(), "COLUMN") == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									rowNode.getName(), "ROW") == 0);

							// attribute condition
							JavaSDM.ensure(leftNeighbor.getRow() == 0);

							// create object isApplicableMatch
							isApplicableMatch = TGGRuntimeFactory.eINSTANCE
									.createIsApplicableMatch();

							// statement node 'solve CSP'
							// Create CSP
							CSP csp = CspFactory.eINSTANCE.createCSP();
							isApplicableMatch.getAttributeInfo().add(csp);

							// Create literals
							Variable<Number> literal0 = CspFactory.eINSTANCE
									.createVariable("literal0", true, csp);
							literal0.setValue(1);

							// Create attribute variables
							Variable<Number> var_leftNeighbor_col = CspFactory.eINSTANCE
									.createVariable("leftNeighbor.col", true,
											csp);
							var_leftNeighbor_col
									.setValue(leftNeighbor.getCol());
							Variable<Number> var_columnNode_index = CspFactory.eINSTANCE
									.createVariable("columnNode.index", true,
											csp);
							var_columnNode_index
									.setValue(columnNode.getIndex());

							// Create explicit parameters

							// Create unbound variables
							Variable<Number> var_floor_col = CspFactory.eINSTANCE
									.createVariable("floor.col", csp);

							// Create constraints
							Sub sub = new Sub();
							Eq<Number> eq = new Eq<Number>();

							csp.getConstraints().add(sub);
							csp.getConstraints().add(eq);

							// Solve CSP
							sub.setRuleName("");
							sub.solve(var_floor_col, literal0,
									var_leftNeighbor_col);
							eq.setRuleName("");
							eq.solve(var_floor_col, var_columnNode_index);

							// Snapshot pattern match on which CSP is solved
							isApplicableMatch.registerObject("boardNode",
									boardNode);
							isApplicableMatch
									.registerObject("rowNode", rowNode);
							isApplicableMatch.registerObject("columnNode",
									columnNode);
							isApplicableMatch.registerObject("nodeToBoard",
									nodeToBoard);
							isApplicableMatch.registerObject("board", board);
							isApplicableMatch.registerObject("leftNeighbor",
									leftNeighbor);

							// statement node 'check CSP'
							fujaba__Success = csp.check();
							if (fujaba__Success) {
								// story node 'add match to rule result'
								try {
									fujaba__Success = false;

									// check object isApplicableMatch is really bound
									JavaSDM.ensure(isApplicableMatch != null);
									// check object ruleresult is really bound
									JavaSDM.ensure(ruleresult != null);
									// assign attribute ruleresult
									ruleresult.setSuccess(true);

									// create link
									ruleresult.getIsApplicableMatch().add(
											isApplicableMatch);

									fujaba__Success = true;
								} catch (JavaSDMException fujaba__InternalException) {
									fujaba__Success = false;
								}

							} else {

							}

							fujaba__Success = true;
						} catch (JavaSDMException fujaba__InternalException) {
							fujaba__Success = false;
						}
					}
					JavaSDM.ensure(fujaba__Success);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PerformRuleResult perform_BWD(IsApplicableMatch isApplicableMatch) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		Board board = null;
		Node boardNode = null;
		Floor floor = null;
		Floor leftNeighbor = null;
		NodeToBoard nodeToBoard = null;
		Node rowNode = null;
		Iterator fujaba__IterIsApplicableMatchToCsp = null;
		CSP csp = null;
		Node columnNode = null;
		NodeToFloor nodeToFloor = null;
		PerformRuleResult ruleresult = null;
		Edge nodeToFloor__source__columnNode = null;
		Edge floor__left__leftNeighbor = null;
		Edge nodeToFloor__target__floor = null;
		Edge board__floors__floor = null;
		Edge rowNode__children__columnNode = null;

		// story node 'perform transformation'
		try {
			fujaba__Success = false;

			_TmpObject = (isApplicableMatch.getObject("board"));

			// ensure correct type and really bound of object board
			JavaSDM.ensure(_TmpObject instanceof Board);
			board = (Board) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("boardNode"));

			// ensure correct type and really bound of object boardNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			boardNode = (Node) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("floor"));

			// ensure correct type and really bound of object floor
			JavaSDM.ensure(_TmpObject instanceof Floor);
			floor = (Floor) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("leftNeighbor"));

			// ensure correct type and really bound of object leftNeighbor
			JavaSDM.ensure(_TmpObject instanceof Floor);
			leftNeighbor = (Floor) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("nodeToBoard"));

			// ensure correct type and really bound of object nodeToBoard
			JavaSDM.ensure(_TmpObject instanceof NodeToBoard);
			nodeToBoard = (NodeToBoard) _TmpObject;
			_TmpObject = (isApplicableMatch.getObject("rowNode"));

			// ensure correct type and really bound of object rowNode
			JavaSDM.ensure(_TmpObject instanceof Node);
			rowNode = (Node) _TmpObject;
			// check object isApplicableMatch is really bound
			JavaSDM.ensure(isApplicableMatch != null);
			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// check isomorphic binding between objects leftNeighbor and floor 
			JavaSDM.ensure(!leftNeighbor.equals(floor));

			// iterate to-many link attributeInfo from isApplicableMatch to csp
			fujaba__Success = false;

			fujaba__IterIsApplicableMatchToCsp = isApplicableMatch
					.getAttributeInfo().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterIsApplicableMatchToCsp.hasNext()) {
				try {
					_TmpObject = fujaba__IterIsApplicableMatchToCsp.next();

					// ensure correct type and really bound of object csp
					JavaSDM.ensure(_TmpObject instanceof CSP);
					csp = (CSP) _TmpObject;

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			if (!fujaba__Success) {
				fujaba__Success = true;
				csp = null;
			}
			// attribute condition
			JavaSDM.ensure(rowNode.getIndex() == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(boardNode.getName(), "BOARD") == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(rowNode.getName(), "ROW") == 0);

			// attribute condition
			JavaSDM.ensure(leftNeighbor.getRow() == 0);

			// attribute condition
			JavaSDM.ensure(floor.getRow() == 0);

			// create object columnNode
			columnNode = MocaTreeFactory.eINSTANCE.createNode();

			// create object nodeToFloor
			nodeToFloor = SokobanCodeAdapterFactory.eINSTANCE
					.createNodeToFloor();

			// assign attribute columnNode
			columnNode.setName("COLUMN");
			// assign attribute columnNode
			columnNode.setIndex(((Number) csp.getAttributeVariable(
					"columnNode", "index").getValue()).intValue());

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(nodeToFloor,
					floor, "target");

			// create link
			columnNode.setParentNode(rowNode);

			// create link
			nodeToFloor.setSource(columnNode);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'collect translated elements'
		try {
			fujaba__Success = false;

			// check object columnNode is really bound
			JavaSDM.ensure(columnNode != null);
			// check object floor is really bound
			JavaSDM.ensure(floor != null);
			// check object nodeToFloor is really bound
			JavaSDM.ensure(nodeToFloor != null);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE.createPerformRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(true);

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					floor, "translatedElements");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					columnNode, "createdElements");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					nodeToFloor, "createdLinkElements");
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'bookkeeping for edges'
		try {
			fujaba__Success = false;

			// check object board is really bound
			JavaSDM.ensure(board != null);
			// check object boardNode is really bound
			JavaSDM.ensure(boardNode != null);
			// check object columnNode is really bound
			JavaSDM.ensure(columnNode != null);
			// check object floor is really bound
			JavaSDM.ensure(floor != null);
			// check object leftNeighbor is really bound
			JavaSDM.ensure(leftNeighbor != null);
			// check object nodeToBoard is really bound
			JavaSDM.ensure(nodeToBoard != null);
			// check object nodeToFloor is really bound
			JavaSDM.ensure(nodeToFloor != null);
			// check object rowNode is really bound
			JavaSDM.ensure(rowNode != null);
			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// check isomorphic binding between objects boardNode and board 
			JavaSDM.ensure(!boardNode.equals(board));

			// check isomorphic binding between objects columnNode and board 
			JavaSDM.ensure(!columnNode.equals(board));

			// check isomorphic binding between objects floor and board 
			JavaSDM.ensure(!floor.equals(board));

			// check isomorphic binding between objects leftNeighbor and board 
			JavaSDM.ensure(!leftNeighbor.equals(board));

			// check isomorphic binding between objects nodeToBoard and board 
			JavaSDM.ensure(!nodeToBoard.equals(board));

			// check isomorphic binding between objects nodeToFloor and board 
			JavaSDM.ensure(!nodeToFloor.equals(board));

			// check isomorphic binding between objects rowNode and board 
			JavaSDM.ensure(!rowNode.equals(board));

			// check isomorphic binding between objects columnNode and boardNode 
			JavaSDM.ensure(!columnNode.equals(boardNode));

			// check isomorphic binding between objects floor and boardNode 
			JavaSDM.ensure(!floor.equals(boardNode));

			// check isomorphic binding between objects leftNeighbor and boardNode 
			JavaSDM.ensure(!leftNeighbor.equals(boardNode));

			// check isomorphic binding between objects nodeToBoard and boardNode 
			JavaSDM.ensure(!nodeToBoard.equals(boardNode));

			// check isomorphic binding between objects nodeToFloor and boardNode 
			JavaSDM.ensure(!nodeToFloor.equals(boardNode));

			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// check isomorphic binding between objects floor and columnNode 
			JavaSDM.ensure(!floor.equals(columnNode));

			// check isomorphic binding between objects leftNeighbor and columnNode 
			JavaSDM.ensure(!leftNeighbor.equals(columnNode));

			// check isomorphic binding between objects nodeToBoard and columnNode 
			JavaSDM.ensure(!nodeToBoard.equals(columnNode));

			// check isomorphic binding between objects nodeToFloor and columnNode 
			JavaSDM.ensure(!nodeToFloor.equals(columnNode));

			// check isomorphic binding between objects rowNode and columnNode 
			JavaSDM.ensure(!rowNode.equals(columnNode));

			// check isomorphic binding between objects leftNeighbor and floor 
			JavaSDM.ensure(!leftNeighbor.equals(floor));

			// check isomorphic binding between objects nodeToBoard and floor 
			JavaSDM.ensure(!nodeToBoard.equals(floor));

			// check isomorphic binding between objects nodeToFloor and floor 
			JavaSDM.ensure(!nodeToFloor.equals(floor));

			// check isomorphic binding between objects rowNode and floor 
			JavaSDM.ensure(!rowNode.equals(floor));

			// check isomorphic binding between objects nodeToBoard and leftNeighbor 
			JavaSDM.ensure(!nodeToBoard.equals(leftNeighbor));

			// check isomorphic binding between objects nodeToFloor and leftNeighbor 
			JavaSDM.ensure(!nodeToFloor.equals(leftNeighbor));

			// check isomorphic binding between objects rowNode and leftNeighbor 
			JavaSDM.ensure(!rowNode.equals(leftNeighbor));

			// check isomorphic binding between objects nodeToFloor and nodeToBoard 
			JavaSDM.ensure(!nodeToFloor.equals(nodeToBoard));

			// check isomorphic binding between objects rowNode and nodeToBoard 
			JavaSDM.ensure(!rowNode.equals(nodeToBoard));

			// check isomorphic binding between objects rowNode and nodeToFloor 
			JavaSDM.ensure(!rowNode.equals(nodeToFloor));

			// create object nodeToFloor__source__columnNode
			nodeToFloor__source__columnNode = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// create object floor__left__leftNeighbor
			floor__left__leftNeighbor = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// create object nodeToFloor__target__floor
			nodeToFloor__target__floor = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// create object board__floors__floor
			board__floors__floor = TGGRuntimeFactory.eINSTANCE.createEdge();

			// create object rowNode__children__columnNode
			rowNode__children__columnNode = TGGRuntimeFactory.eINSTANCE
					.createEdge();

			// assign attribute rowNode__children__columnNode
			rowNode__children__columnNode.setName("children");
			// assign attribute nodeToFloor__source__columnNode
			nodeToFloor__source__columnNode.setName("source");
			// assign attribute nodeToFloor__target__floor
			nodeToFloor__target__floor.setName("target");
			// assign attribute board__floors__floor
			board__floors__floor.setName("floors");
			// assign attribute floor__left__leftNeighbor
			floor__left__leftNeighbor.setName("left");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					nodeToFloor__source__columnNode, "createdEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					floor__left__leftNeighbor, "translatedEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					nodeToFloor__target__floor, "createdEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					board__floors__floor, "translatedEdges");

			// create link
			org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(ruleresult,
					rowNode__children__columnNode, "createdEdges");

			// create link
			rowNode__children__columnNode.setSrc(rowNode);

			// create link
			nodeToFloor__source__columnNode.setTrg(columnNode);

			// create link
			rowNode__children__columnNode.setTrg(columnNode);

			// create link
			nodeToFloor__source__columnNode.setSrc(nodeToFloor);

			// create link
			nodeToFloor__target__floor.setSrc(nodeToFloor);

			// create link
			board__floors__floor.setSrc(board);

			// create link
			floor__left__leftNeighbor.setTrg(leftNeighbor);

			// create link
			board__floors__floor.setTrg(floor);

			// create link
			nodeToFloor__target__floor.setTrg(floor);

			// create link
			floor__left__leftNeighbor.setSrc(floor);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// statement node 'perform postprocessing'
		// No post processing method found
		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAppropriate_BWD(Match match, Board board,
			Floor leftNeighbor, Floor floor) {
		boolean fujaba__Success = false;
		Edge floor__left__leftNeighbor = null;
		Edge board__floors__floor = null;
		Edge board__floors__leftNeighbor = null;

		// statement node 'Solve CSP'
		// Create CSP
		CSP csp = CspFactory.eINSTANCE.createCSP();

		// Create literals
		Variable<Number> literal0 = CspFactory.eINSTANCE.createVariable(
				"literal0", true, csp);
		literal0.setValue(1);

		// Create attribute variables
		Variable<Number> var_floor_col = CspFactory.eINSTANCE.createVariable(
				"floor.col", true, csp);
		var_floor_col.setValue(floor.getCol());
		Variable<Number> var_leftNeighbor_col = CspFactory.eINSTANCE
				.createVariable("leftNeighbor.col", true, csp);
		var_leftNeighbor_col.setValue(leftNeighbor.getCol());

		// Create explicit parameters

		// Create unbound variables

		// Create constraints
		Sub sub = new Sub();

		csp.getConstraints().add(sub);

		// Solve CSP
		sub.setRuleName("");
		sub.solve(var_floor_col, literal0, var_leftNeighbor_col);

		// statement node 'Check CSP'
		fujaba__Success = csp.check();
		if (fujaba__Success) {
			// story node 'collect elements to be translated'
			try {
				fujaba__Success = false;

				// check object board is really bound
				JavaSDM.ensure(board != null);
				// check object floor is really bound
				JavaSDM.ensure(floor != null);
				// check object leftNeighbor is really bound
				JavaSDM.ensure(leftNeighbor != null);
				// check object match is really bound
				JavaSDM.ensure(match != null);
				// check isomorphic binding between objects leftNeighbor and floor 
				JavaSDM.ensure(!leftNeighbor.equals(floor));

				// create object floor__left__leftNeighbor
				floor__left__leftNeighbor = TGGRuntimeFactory.eINSTANCE
						.createEdge();

				// create object board__floors__floor
				board__floors__floor = TGGRuntimeFactory.eINSTANCE.createEdge();

				// assign attribute board__floors__floor
				board__floors__floor.setName("floors");
				// assign attribute floor__left__leftNeighbor
				floor__left__leftNeighbor.setName("left");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						floor__left__leftNeighbor, "toBeTranslatedEdges");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						floor, "toBeTranslatedElements");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						board__floors__floor, "toBeTranslatedEdges");

				// create link
				floor__left__leftNeighbor.setSrc(floor);

				// create link
				board__floors__floor.setTrg(floor);

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(
						board__floors__floor, board, "src");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(
						floor__left__leftNeighbor, leftNeighbor, "trg");
				fujaba__Success = true;
			} catch (JavaSDMException fujaba__InternalException) {
				fujaba__Success = false;
			}

			// story node 'collect context elements'
			try {
				fujaba__Success = false;

				// check object board is really bound
				JavaSDM.ensure(board != null);
				// check object leftNeighbor is really bound
				JavaSDM.ensure(leftNeighbor != null);
				// check object match is really bound
				JavaSDM.ensure(match != null);
				// create object board__floors__leftNeighbor
				board__floors__leftNeighbor = TGGRuntimeFactory.eINSTANCE
						.createEdge();

				// assign attribute board__floors__leftNeighbor
				board__floors__leftNeighbor.setName("floors");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						board, "contextNodes");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						leftNeighbor, "contextNodes");

				// create link
				org.moflon.core.utilities.eMoflonEMFUtil.addOppositeReference(match,
						board__floors__leftNeighbor, "contextEdges");

				// create link
				board__floors__leftNeighbor.setSrc(board);

				// create link
				board__floors__leftNeighbor.setTrg(leftNeighbor);

				fujaba__Success = true;
			} catch (JavaSDMException fujaba__InternalException) {
				fujaba__Success = false;
			}

			// statement node 'register objects to match'
			match.registerObject("board", board);
			match.registerObject("leftNeighbor", leftNeighbor);
			match.registerObject("floor", floor);

			return true;

		} else {
			return false;

		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsApplicableRuleResult isApplicable_BWD(Match match) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass eClass = null;
		Iterator fujaba__IterEClassToPerformOperation = null;
		EOperation performOperation = null;
		IsApplicableRuleResult ruleresult = null;
		Board board = null;
		Floor floor = null;
		Floor leftNeighbor = null;
		IsApplicableMatch isApplicableMatch = null;
		Iterator fujaba__IterBoardNodeToRowNode = null;
		Node rowNode = null;
		Node boardNode = null;
		Iterator fujaba__IterBoardToNodeToBoard = null;
		NodeToBoard nodeToBoard = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from eClass to performOperation
			fujaba__Success = false;

			fujaba__IterEClassToPerformOperation = eClass.getEOperations()
					.iterator();

			while (!(fujaba__Success)
					&& fujaba__IterEClassToPerformOperation.hasNext()) {
				try {
					performOperation = (EOperation) fujaba__IterEClassToPerformOperation
							.next();

					// check object performOperation is really bound
					JavaSDM.ensure(performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							performOperation.getName(), "perform_BWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsApplicableRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);
			// assign attribute ruleresult
			ruleresult.setRule("TopRowNodeToFloorRule");

			// create link
			ruleresult.setPerformOperation(performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'core match'
		try {
			fujaba__Success = false;

			_TmpObject = (match.getObject("board"));

			// ensure correct type and really bound of object board
			JavaSDM.ensure(_TmpObject instanceof Board);
			board = (Board) _TmpObject;
			_TmpObject = (match.getObject("floor"));

			// ensure correct type and really bound of object floor
			JavaSDM.ensure(_TmpObject instanceof Floor);
			floor = (Floor) _TmpObject;
			_TmpObject = (match.getObject("leftNeighbor"));

			// ensure correct type and really bound of object leftNeighbor
			JavaSDM.ensure(_TmpObject instanceof Floor);
			leftNeighbor = (Floor) _TmpObject;
			// check object match is really bound
			JavaSDM.ensure(match != null);
			// check isomorphic binding between objects leftNeighbor and floor 
			JavaSDM.ensure(!leftNeighbor.equals(floor));

			// attribute condition
			JavaSDM.ensure(leftNeighbor.getRow() == 0);

			// attribute condition
			JavaSDM.ensure(floor.getRow() == 0);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'find context'
		try {
			fujaba__Success = false;

			// check object board is really bound
			JavaSDM.ensure(board != null);
			// check object floor is really bound
			JavaSDM.ensure(floor != null);
			// check object leftNeighbor is really bound
			JavaSDM.ensure(leftNeighbor != null);
			// check isomorphic binding between objects leftNeighbor and floor 
			JavaSDM.ensure(!leftNeighbor.equals(floor));

			// check link floors from floor to board
			JavaSDM.ensure(board.equals(floor.getBoard()));

			// check link floors from leftNeighbor to board
			JavaSDM.ensure(board.equals(leftNeighbor.getBoard()));

			// check link right from floor to leftNeighbor
			JavaSDM.ensure(leftNeighbor.equals(floor.getLeft()));

			// iterate to-many link target from board to nodeToBoard
			fujaba__Success = false;

			fujaba__IterBoardToNodeToBoard = new ArrayList(
					org.moflon.core.utilities.eMoflonEMFUtil.getOppositeReference(board,
							NodeToBoard.class, "target")).iterator();

			while (fujaba__IterBoardToNodeToBoard.hasNext()) {
				try {
					nodeToBoard = (NodeToBoard) fujaba__IterBoardToNodeToBoard
							.next();

					// check object nodeToBoard is really bound
					JavaSDM.ensure(nodeToBoard != null);
					// bind object
					boardNode = nodeToBoard.getSource();

					// check object boardNode is really bound
					JavaSDM.ensure(boardNode != null);

					// iterate to-many link children from boardNode to rowNode
					fujaba__Success = false;

					fujaba__IterBoardNodeToRowNode = new ArrayList(
							boardNode.getChildren("ROW", 0)).iterator();

					while (fujaba__IterBoardNodeToRowNode.hasNext()) {
						try {
							_TmpObject = fujaba__IterBoardNodeToRowNode.next();

							// ensure correct type and really bound of object rowNode
							JavaSDM.ensure(_TmpObject instanceof Node);
							rowNode = (Node) _TmpObject;
							// check isomorphic binding between objects rowNode and boardNode 
							JavaSDM.ensure(!rowNode.equals(boardNode));

							// attribute condition
							JavaSDM.ensure(rowNode.getIndex() == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									boardNode.getName(), "BOARD") == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									rowNode.getName(), "ROW") == 0);

							// attribute condition
							JavaSDM.ensure(leftNeighbor.getRow() == 0);

							// attribute condition
							JavaSDM.ensure(floor.getRow() == 0);

							// create object isApplicableMatch
							isApplicableMatch = TGGRuntimeFactory.eINSTANCE
									.createIsApplicableMatch();

							// statement node 'solve CSP'
							// Create CSP
							CSP csp = CspFactory.eINSTANCE.createCSP();
							isApplicableMatch.getAttributeInfo().add(csp);

							// Create literals

							// Create attribute variables
							Variable<Number> var_floor_col = CspFactory.eINSTANCE
									.createVariable("floor.col", true, csp);
							var_floor_col.setValue(floor.getCol());

							// Create explicit parameters

							// Create unbound variables
							Variable<Number> var_columnNode_index = CspFactory.eINSTANCE
									.createVariable("columnNode.index", csp);

							// Create constraints
							Eq<Number> eq = new Eq<Number>();

							csp.getConstraints().add(eq);

							// Solve CSP
							eq.setRuleName("");
							eq.solve(var_floor_col, var_columnNode_index);

							// Snapshot pattern match on which CSP is solved
							isApplicableMatch.registerObject("boardNode",
									boardNode);
							isApplicableMatch
									.registerObject("rowNode", rowNode);
							isApplicableMatch.registerObject("nodeToBoard",
									nodeToBoard);
							isApplicableMatch.registerObject("board", board);
							isApplicableMatch.registerObject("leftNeighbor",
									leftNeighbor);
							isApplicableMatch.registerObject("floor", floor);

							// statement node 'check CSP'
							fujaba__Success = csp.check();
							if (fujaba__Success) {
								// story node 'add match to rule result'
								try {
									fujaba__Success = false;

									// check object isApplicableMatch is really bound
									JavaSDM.ensure(isApplicableMatch != null);
									// check object ruleresult is really bound
									JavaSDM.ensure(ruleresult != null);
									// assign attribute ruleresult
									ruleresult.setSuccess(true);

									// create link
									ruleresult.getIsApplicableMatch().add(
											isApplicableMatch);

									fujaba__Success = true;
								} catch (JavaSDMException fujaba__InternalException) {
									fujaba__Success = false;
								}

							} else {

							}

							fujaba__Success = true;
						} catch (JavaSDMException fujaba__InternalException) {
							fujaba__Success = false;
						}
					}
					JavaSDM.ensure(fujaba__Success);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsAppropriateRuleResult isAppropriate_FWD_Node_21(Node boardNode) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass __eClass = null;
		Iterator fujaba__Iter__eClassTo__performOperation = null;
		EOperation __performOperation = null;
		IsAppropriateRuleResult ruleresult = null;
		Match match = null;
		Iterator fujaba__IterRowNodeToColumnNode = null;
		Node columnNode = null;
		Iterator fujaba__IterBoardNodeToRowNode = null;
		Node rowNode = null;
		Iterator fujaba__IterRuleresultToMatch = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object __eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			__eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from __eClass to __performOperation
			fujaba__Success = false;

			fujaba__Iter__eClassTo__performOperation = __eClass
					.getEOperations().iterator();

			while (!(fujaba__Success)
					&& fujaba__Iter__eClassTo__performOperation.hasNext()) {
				try {
					__performOperation = (EOperation) fujaba__Iter__eClassTo__performOperation
							.next();

					// check object __performOperation is really bound
					JavaSDM.ensure(__performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							__performOperation.getName(), "isApplicable_FWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsAppropriateRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);

			// create link
			ruleresult.setPerformOperation(__performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'test core match kernel'
		try {
			fujaba__Success = false;

			// check object boardNode is really bound
			JavaSDM.ensure(boardNode != null);
			// iterate to-many link children from boardNode to rowNode
			fujaba__Success = false;

			fujaba__IterBoardNodeToRowNode = new ArrayList(
					boardNode.getChildren("ROW", 0)).iterator();

			while (fujaba__IterBoardNodeToRowNode.hasNext()) {
				try {
					_TmpObject = fujaba__IterBoardNodeToRowNode.next();

					// ensure correct type and really bound of object rowNode
					JavaSDM.ensure(_TmpObject instanceof Node);
					rowNode = (Node) _TmpObject;
					// check isomorphic binding between objects rowNode and boardNode 
					JavaSDM.ensure(!rowNode.equals(boardNode));

					// iterate to-many link children from rowNode to columnNode
					fujaba__Success = false;

					fujaba__IterRowNodeToColumnNode = new ArrayList(
							rowNode.getChildren("COLUMN")).iterator();

					while (fujaba__IterRowNodeToColumnNode.hasNext()) {
						try {
							_TmpObject = fujaba__IterRowNodeToColumnNode.next();

							// ensure correct type and really bound of object columnNode
							JavaSDM.ensure(_TmpObject instanceof Node);
							columnNode = (Node) _TmpObject;
							// check isomorphic binding between objects columnNode and boardNode 
							JavaSDM.ensure(!columnNode.equals(boardNode));

							// check isomorphic binding between objects rowNode and columnNode 
							JavaSDM.ensure(!rowNode.equals(columnNode));

							// attribute condition
							JavaSDM.ensure(rowNode.getIndex() == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									boardNode.getName(), "BOARD") == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									columnNode.getName(), "COLUMN") == 0);

							// attribute condition
							JavaSDM.ensure(JavaSDM.stringCompare(
									rowNode.getName(), "ROW") == 0);

							// story node 'test core match'
							try {
								fujaba__Success = false;

								// check object boardNode is really bound
								JavaSDM.ensure(boardNode != null);
								// check object columnNode is really bound
								JavaSDM.ensure(columnNode != null);
								// check object rowNode is really bound
								JavaSDM.ensure(rowNode != null);
								// check object ruleresult is really bound
								JavaSDM.ensure(ruleresult != null);
								// check isomorphic binding between objects columnNode and boardNode 
								JavaSDM.ensure(!columnNode.equals(boardNode));

								// check isomorphic binding between objects rowNode and boardNode 
								JavaSDM.ensure(!rowNode.equals(boardNode));

								// check isomorphic binding between objects rowNode and columnNode 
								JavaSDM.ensure(!rowNode.equals(columnNode));

								// check link children from rowNode to boardNode
								JavaSDM.ensure(boardNode.equals(rowNode
										.getParentNode()));

								// check link children from columnNode to rowNode
								JavaSDM.ensure(rowNode.equals(columnNode
										.getParentNode()));

								// attribute condition
								JavaSDM.ensure(rowNode.getIndex() == 0);

								// attribute condition
								JavaSDM.ensure(JavaSDM.stringCompare(
										boardNode.getName(), "BOARD") == 0);

								// attribute condition
								JavaSDM.ensure(JavaSDM.stringCompare(
										columnNode.getName(), "COLUMN") == 0);

								// attribute condition
								JavaSDM.ensure(JavaSDM.stringCompare(
										rowNode.getName(), "ROW") == 0);

								// create object match
								match = TGGRuntimeFactory.eINSTANCE
										.createMatch();

								// statement node 'bookkeeping with generic isAppropriate method'
								fujaba__Success = this.isAppropriate_FWD(match,
										boardNode, rowNode, columnNode);
								if (fujaba__Success) {
									// story node 'Add match to rule result'
									try {
										fujaba__Success = false;

										// check object match is really bound
										JavaSDM.ensure(match != null);
										// check object ruleresult is really bound
										JavaSDM.ensure(ruleresult != null);

										// create link
										match.setIsAppropriateRuleResult(ruleresult);

										fujaba__Success = true;
									} catch (JavaSDMException fujaba__InternalException) {
										fujaba__Success = false;
									}

								} else {

								}
								fujaba__Success = true;
							} catch (JavaSDMException fujaba__InternalException) {
								fujaba__Success = false;
							}

							fujaba__Success = true;
						} catch (JavaSDMException fujaba__InternalException) {
							fujaba__Success = false;
						}
					}
					JavaSDM.ensure(fujaba__Success);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'set success'
		try {
			fujaba__Success = false;

			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// iterate to-many link matches from ruleresult to match
			fujaba__Success = false;

			fujaba__IterRuleresultToMatch = ruleresult.getMatches().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterRuleresultToMatch.hasNext()) {
				try {
					match = (Match) fujaba__IterRuleresultToMatch.next();

					// check object match is really bound
					JavaSDM.ensure(match != null);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// assign attribute ruleresult
			ruleresult.setSuccess(true);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsAppropriateRuleResult isAppropriate_FWD_Node_22(Node rowNode) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass __eClass = null;
		Iterator fujaba__Iter__eClassTo__performOperation = null;
		EOperation __performOperation = null;
		IsAppropriateRuleResult ruleresult = null;
		Match match = null;
		Iterator fujaba__IterRowNodeToColumnNode = null;
		Node columnNode = null;
		Node boardNode = null;
		Iterator fujaba__IterRuleresultToMatch = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object __eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			__eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from __eClass to __performOperation
			fujaba__Success = false;

			fujaba__Iter__eClassTo__performOperation = __eClass
					.getEOperations().iterator();

			while (!(fujaba__Success)
					&& fujaba__Iter__eClassTo__performOperation.hasNext()) {
				try {
					__performOperation = (EOperation) fujaba__Iter__eClassTo__performOperation
							.next();

					// check object __performOperation is really bound
					JavaSDM.ensure(__performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							__performOperation.getName(), "isApplicable_FWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsAppropriateRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);

			// create link
			ruleresult.setPerformOperation(__performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'test core match kernel'
		try {
			fujaba__Success = false;

			// check object rowNode is really bound
			JavaSDM.ensure(rowNode != null);
			// bind object
			boardNode = rowNode.getParentNode();

			// check object boardNode is really bound
			JavaSDM.ensure(boardNode != null);

			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// iterate to-many link children from rowNode to columnNode
			fujaba__Success = false;

			fujaba__IterRowNodeToColumnNode = new ArrayList(
					rowNode.getChildren("COLUMN")).iterator();

			while (fujaba__IterRowNodeToColumnNode.hasNext()) {
				try {
					_TmpObject = fujaba__IterRowNodeToColumnNode.next();

					// ensure correct type and really bound of object columnNode
					JavaSDM.ensure(_TmpObject instanceof Node);
					columnNode = (Node) _TmpObject;
					// check isomorphic binding between objects columnNode and boardNode 
					JavaSDM.ensure(!columnNode.equals(boardNode));

					// check isomorphic binding between objects rowNode and columnNode 
					JavaSDM.ensure(!rowNode.equals(columnNode));

					// attribute condition
					JavaSDM.ensure(rowNode.getIndex() == 0);

					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(boardNode.getName(),
							"BOARD") == 0);

					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(columnNode.getName(),
							"COLUMN") == 0);

					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(rowNode.getName(),
							"ROW") == 0);

					// story node 'test core match'
					try {
						fujaba__Success = false;

						// check object boardNode is really bound
						JavaSDM.ensure(boardNode != null);
						// check object columnNode is really bound
						JavaSDM.ensure(columnNode != null);
						// check object rowNode is really bound
						JavaSDM.ensure(rowNode != null);
						// check object ruleresult is really bound
						JavaSDM.ensure(ruleresult != null);
						// check isomorphic binding between objects columnNode and boardNode 
						JavaSDM.ensure(!columnNode.equals(boardNode));

						// check isomorphic binding between objects rowNode and boardNode 
						JavaSDM.ensure(!rowNode.equals(boardNode));

						// check isomorphic binding between objects rowNode and columnNode 
						JavaSDM.ensure(!rowNode.equals(columnNode));

						// check link children from rowNode to boardNode
						JavaSDM.ensure(boardNode.equals(rowNode.getParentNode()));

						// check link children from columnNode to rowNode
						JavaSDM.ensure(rowNode.equals(columnNode
								.getParentNode()));

						// attribute condition
						JavaSDM.ensure(rowNode.getIndex() == 0);

						// attribute condition
						JavaSDM.ensure(JavaSDM.stringCompare(
								boardNode.getName(), "BOARD") == 0);

						// attribute condition
						JavaSDM.ensure(JavaSDM.stringCompare(
								columnNode.getName(), "COLUMN") == 0);

						// attribute condition
						JavaSDM.ensure(JavaSDM.stringCompare(rowNode.getName(),
								"ROW") == 0);

						// create object match
						match = TGGRuntimeFactory.eINSTANCE.createMatch();

						// statement node 'bookkeeping with generic isAppropriate method'
						fujaba__Success = this.isAppropriate_FWD(match,
								boardNode, rowNode, columnNode);
						if (fujaba__Success) {
							// story node 'Add match to rule result'
							try {
								fujaba__Success = false;

								// check object match is really bound
								JavaSDM.ensure(match != null);
								// check object ruleresult is really bound
								JavaSDM.ensure(ruleresult != null);

								// create link
								match.setIsAppropriateRuleResult(ruleresult);

								fujaba__Success = true;
							} catch (JavaSDMException fujaba__InternalException) {
								fujaba__Success = false;
							}

						} else {

						}
						fujaba__Success = true;
					} catch (JavaSDMException fujaba__InternalException) {
						fujaba__Success = false;
					}

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'set success'
		try {
			fujaba__Success = false;

			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// iterate to-many link matches from ruleresult to match
			fujaba__Success = false;

			fujaba__IterRuleresultToMatch = ruleresult.getMatches().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterRuleresultToMatch.hasNext()) {
				try {
					match = (Match) fujaba__IterRuleresultToMatch.next();

					// check object match is really bound
					JavaSDM.ensure(match != null);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// assign attribute ruleresult
			ruleresult.setSuccess(true);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsAppropriateRuleResult isAppropriate_FWD_Node_23(Node columnNode) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass __eClass = null;
		Iterator fujaba__Iter__eClassTo__performOperation = null;
		EOperation __performOperation = null;
		IsAppropriateRuleResult ruleresult = null;
		Match match = null;
		Node boardNode = null;
		Node rowNode = null;
		Iterator fujaba__IterRuleresultToMatch = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object __eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			__eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from __eClass to __performOperation
			fujaba__Success = false;

			fujaba__Iter__eClassTo__performOperation = __eClass
					.getEOperations().iterator();

			while (!(fujaba__Success)
					&& fujaba__Iter__eClassTo__performOperation.hasNext()) {
				try {
					__performOperation = (EOperation) fujaba__Iter__eClassTo__performOperation
							.next();

					// check object __performOperation is really bound
					JavaSDM.ensure(__performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							__performOperation.getName(), "isApplicable_FWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsAppropriateRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);

			// create link
			ruleresult.setPerformOperation(__performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'test core match kernel'
		try {
			fujaba__Success = false;

			// check object columnNode is really bound
			JavaSDM.ensure(columnNode != null);
			// bind object
			rowNode = columnNode.getParentNode();

			// check object rowNode is really bound
			JavaSDM.ensure(rowNode != null);

			// check isomorphic binding between objects rowNode and columnNode 
			JavaSDM.ensure(!rowNode.equals(columnNode));

			// bind object
			boardNode = rowNode.getParentNode();

			// check object boardNode is really bound
			JavaSDM.ensure(boardNode != null);

			// check isomorphic binding between objects columnNode and boardNode 
			JavaSDM.ensure(!columnNode.equals(boardNode));

			// check isomorphic binding between objects rowNode and boardNode 
			JavaSDM.ensure(!rowNode.equals(boardNode));

			// attribute condition
			JavaSDM.ensure(rowNode.getIndex() == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(boardNode.getName(), "BOARD") == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(columnNode.getName(), "COLUMN") == 0);

			// attribute condition
			JavaSDM.ensure(JavaSDM.stringCompare(rowNode.getName(), "ROW") == 0);

			// story node 'test core match'
			try {
				fujaba__Success = false;

				// check object boardNode is really bound
				JavaSDM.ensure(boardNode != null);
				// check object columnNode is really bound
				JavaSDM.ensure(columnNode != null);
				// check object rowNode is really bound
				JavaSDM.ensure(rowNode != null);
				// check object ruleresult is really bound
				JavaSDM.ensure(ruleresult != null);
				// check isomorphic binding between objects columnNode and boardNode 
				JavaSDM.ensure(!columnNode.equals(boardNode));

				// check isomorphic binding between objects rowNode and boardNode 
				JavaSDM.ensure(!rowNode.equals(boardNode));

				// check isomorphic binding between objects rowNode and columnNode 
				JavaSDM.ensure(!rowNode.equals(columnNode));

				// check link children from rowNode to boardNode
				JavaSDM.ensure(boardNode.equals(rowNode.getParentNode()));

				// check link children from columnNode to rowNode
				JavaSDM.ensure(rowNode.equals(columnNode.getParentNode()));

				// attribute condition
				JavaSDM.ensure(rowNode.getIndex() == 0);

				// attribute condition
				JavaSDM.ensure(JavaSDM.stringCompare(boardNode.getName(),
						"BOARD") == 0);

				// attribute condition
				JavaSDM.ensure(JavaSDM.stringCompare(columnNode.getName(),
						"COLUMN") == 0);

				// attribute condition
				JavaSDM.ensure(JavaSDM.stringCompare(rowNode.getName(), "ROW") == 0);

				// create object match
				match = TGGRuntimeFactory.eINSTANCE.createMatch();

				// statement node 'bookkeeping with generic isAppropriate method'
				fujaba__Success = this.isAppropriate_FWD(match, boardNode,
						rowNode, columnNode);
				if (fujaba__Success) {
					// story node 'Add match to rule result'
					try {
						fujaba__Success = false;

						// check object match is really bound
						JavaSDM.ensure(match != null);
						// check object ruleresult is really bound
						JavaSDM.ensure(ruleresult != null);

						// create link
						match.setIsAppropriateRuleResult(ruleresult);

						fujaba__Success = true;
					} catch (JavaSDMException fujaba__InternalException) {
						fujaba__Success = false;
					}

				} else {

				}
				fujaba__Success = true;
			} catch (JavaSDMException fujaba__InternalException) {
				fujaba__Success = false;
			}

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'set success'
		try {
			fujaba__Success = false;

			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// iterate to-many link matches from ruleresult to match
			fujaba__Success = false;

			fujaba__IterRuleresultToMatch = ruleresult.getMatches().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterRuleresultToMatch.hasNext()) {
				try {
					match = (Match) fujaba__IterRuleresultToMatch.next();

					// check object match is really bound
					JavaSDM.ensure(match != null);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// assign attribute ruleresult
			ruleresult.setSuccess(true);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsAppropriateRuleResult isAppropriate_BWD_Board_3(Board board) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass __eClass = null;
		Iterator fujaba__Iter__eClassTo__performOperation = null;
		EOperation __performOperation = null;
		IsAppropriateRuleResult ruleresult = null;
		Match match = null;
		Floor leftNeighbor = null;
		Iterator fujaba__IterBoardToFloor = null;
		Floor floor = null;
		Iterator fujaba__IterRuleresultToMatch = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object __eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			__eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from __eClass to __performOperation
			fujaba__Success = false;

			fujaba__Iter__eClassTo__performOperation = __eClass
					.getEOperations().iterator();

			while (!(fujaba__Success)
					&& fujaba__Iter__eClassTo__performOperation.hasNext()) {
				try {
					__performOperation = (EOperation) fujaba__Iter__eClassTo__performOperation
							.next();

					// check object __performOperation is really bound
					JavaSDM.ensure(__performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							__performOperation.getName(), "isApplicable_BWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsAppropriateRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);

			// create link
			ruleresult.setPerformOperation(__performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'test core match kernel'
		try {
			fujaba__Success = false;

			// check object board is really bound
			JavaSDM.ensure(board != null);
			// iterate to-many link floors from board to floor
			fujaba__Success = false;

			fujaba__IterBoardToFloor = new ArrayList(board.getFloors())
					.iterator();

			while (fujaba__IterBoardToFloor.hasNext()) {
				try {
					floor = (Floor) fujaba__IterBoardToFloor.next();

					// check object floor is really bound
					JavaSDM.ensure(floor != null);
					// bind object
					leftNeighbor = floor.getLeft();

					// check object leftNeighbor is really bound
					JavaSDM.ensure(leftNeighbor != null);

					// check isomorphic binding between objects leftNeighbor and floor 
					JavaSDM.ensure(!leftNeighbor.equals(floor));

					// check link floors from leftNeighbor to board
					JavaSDM.ensure(board.equals(leftNeighbor.getBoard()));

					// attribute condition
					JavaSDM.ensure(leftNeighbor.getRow() == 0);

					// attribute condition
					JavaSDM.ensure(floor.getRow() == 0);

					// story node 'test core match'
					try {
						fujaba__Success = false;

						// check object board is really bound
						JavaSDM.ensure(board != null);
						// check object floor is really bound
						JavaSDM.ensure(floor != null);
						// check object leftNeighbor is really bound
						JavaSDM.ensure(leftNeighbor != null);
						// check object ruleresult is really bound
						JavaSDM.ensure(ruleresult != null);
						// check isomorphic binding between objects leftNeighbor and floor 
						JavaSDM.ensure(!leftNeighbor.equals(floor));

						// check link floors from floor to board
						JavaSDM.ensure(board.equals(floor.getBoard()));

						// check link floors from leftNeighbor to board
						JavaSDM.ensure(board.equals(leftNeighbor.getBoard()));

						// check link right from floor to leftNeighbor
						JavaSDM.ensure(leftNeighbor.equals(floor.getLeft()));

						// attribute condition
						JavaSDM.ensure(floor.getRow() == 0);

						// attribute condition
						JavaSDM.ensure(leftNeighbor.getRow() == 0);

						// create object match
						match = TGGRuntimeFactory.eINSTANCE.createMatch();

						// statement node 'bookkeeping with generic isAppropriate method'
						fujaba__Success = this.isAppropriate_BWD(match, board,
								leftNeighbor, floor);
						if (fujaba__Success) {
							// story node 'Add match to rule result'
							try {
								fujaba__Success = false;

								// check object match is really bound
								JavaSDM.ensure(match != null);
								// check object ruleresult is really bound
								JavaSDM.ensure(ruleresult != null);

								// create link
								match.setIsAppropriateRuleResult(ruleresult);

								fujaba__Success = true;
							} catch (JavaSDMException fujaba__InternalException) {
								fujaba__Success = false;
							}

						} else {

						}
						fujaba__Success = true;
					} catch (JavaSDMException fujaba__InternalException) {
						fujaba__Success = false;
					}

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'set success'
		try {
			fujaba__Success = false;

			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// iterate to-many link matches from ruleresult to match
			fujaba__Success = false;

			fujaba__IterRuleresultToMatch = ruleresult.getMatches().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterRuleresultToMatch.hasNext()) {
				try {
					match = (Match) fujaba__IterRuleresultToMatch.next();

					// check object match is really bound
					JavaSDM.ensure(match != null);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// assign attribute ruleresult
			ruleresult.setSuccess(true);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsAppropriateRuleResult isAppropriate_BWD_Floor_9(Floor leftNeighbor) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass __eClass = null;
		Iterator fujaba__Iter__eClassTo__performOperation = null;
		EOperation __performOperation = null;
		IsAppropriateRuleResult ruleresult = null;
		Match match = null;
		Floor floor = null;
		Board board = null;
		Iterator fujaba__IterRuleresultToMatch = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object __eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			__eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from __eClass to __performOperation
			fujaba__Success = false;

			fujaba__Iter__eClassTo__performOperation = __eClass
					.getEOperations().iterator();

			while (!(fujaba__Success)
					&& fujaba__Iter__eClassTo__performOperation.hasNext()) {
				try {
					__performOperation = (EOperation) fujaba__Iter__eClassTo__performOperation
							.next();

					// check object __performOperation is really bound
					JavaSDM.ensure(__performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							__performOperation.getName(), "isApplicable_BWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsAppropriateRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);

			// create link
			ruleresult.setPerformOperation(__performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'test core match kernel'
		try {
			fujaba__Success = false;

			// check object leftNeighbor is really bound
			JavaSDM.ensure(leftNeighbor != null);
			// bind object
			board = leftNeighbor.getBoard();

			// check object board is really bound
			JavaSDM.ensure(board != null);

			// bind object
			floor = leftNeighbor.getRight();

			// check object floor is really bound
			JavaSDM.ensure(floor != null);

			// check isomorphic binding between objects leftNeighbor and floor 
			JavaSDM.ensure(!leftNeighbor.equals(floor));

			// check link floors from floor to board
			JavaSDM.ensure(board.equals(floor.getBoard()));

			// attribute condition
			JavaSDM.ensure(leftNeighbor.getRow() == 0);

			// attribute condition
			JavaSDM.ensure(floor.getRow() == 0);

			// story node 'test core match'
			try {
				fujaba__Success = false;

				// check object board is really bound
				JavaSDM.ensure(board != null);
				// check object floor is really bound
				JavaSDM.ensure(floor != null);
				// check object leftNeighbor is really bound
				JavaSDM.ensure(leftNeighbor != null);
				// check object ruleresult is really bound
				JavaSDM.ensure(ruleresult != null);
				// check isomorphic binding between objects leftNeighbor and floor 
				JavaSDM.ensure(!leftNeighbor.equals(floor));

				// check link floors from floor to board
				JavaSDM.ensure(board.equals(floor.getBoard()));

				// check link floors from leftNeighbor to board
				JavaSDM.ensure(board.equals(leftNeighbor.getBoard()));

				// check link right from floor to leftNeighbor
				JavaSDM.ensure(leftNeighbor.equals(floor.getLeft()));

				// attribute condition
				JavaSDM.ensure(leftNeighbor.getRow() == 0);

				// attribute condition
				JavaSDM.ensure(floor.getRow() == 0);

				// create object match
				match = TGGRuntimeFactory.eINSTANCE.createMatch();

				// statement node 'bookkeeping with generic isAppropriate method'
				fujaba__Success = this.isAppropriate_BWD(match, board,
						leftNeighbor, floor);
				if (fujaba__Success) {
					// story node 'Add match to rule result'
					try {
						fujaba__Success = false;

						// check object match is really bound
						JavaSDM.ensure(match != null);
						// check object ruleresult is really bound
						JavaSDM.ensure(ruleresult != null);

						// create link
						match.setIsAppropriateRuleResult(ruleresult);

						fujaba__Success = true;
					} catch (JavaSDMException fujaba__InternalException) {
						fujaba__Success = false;
					}

				} else {

				}
				fujaba__Success = true;
			} catch (JavaSDMException fujaba__InternalException) {
				fujaba__Success = false;
			}

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'set success'
		try {
			fujaba__Success = false;

			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// iterate to-many link matches from ruleresult to match
			fujaba__Success = false;

			fujaba__IterRuleresultToMatch = ruleresult.getMatches().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterRuleresultToMatch.hasNext()) {
				try {
					match = (Match) fujaba__IterRuleresultToMatch.next();

					// check object match is really bound
					JavaSDM.ensure(match != null);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// assign attribute ruleresult
			ruleresult.setSuccess(true);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsAppropriateRuleResult isAppropriate_BWD_Floor_10(Floor floor) {
		boolean fujaba__Success = false;
		Object _TmpObject = null;
		EClass __eClass = null;
		Iterator fujaba__Iter__eClassTo__performOperation = null;
		EOperation __performOperation = null;
		IsAppropriateRuleResult ruleresult = null;
		Match match = null;
		Floor leftNeighbor = null;
		Board board = null;
		Iterator fujaba__IterRuleresultToMatch = null;

		// story node 'prepare return value'
		try {
			fujaba__Success = false;

			_TmpObject = (this.eClass());

			// ensure correct type and really bound of object __eClass
			JavaSDM.ensure(_TmpObject instanceof EClass);
			__eClass = (EClass) _TmpObject;
			// iterate to-many link eOperations from __eClass to __performOperation
			fujaba__Success = false;

			fujaba__Iter__eClassTo__performOperation = __eClass
					.getEOperations().iterator();

			while (!(fujaba__Success)
					&& fujaba__Iter__eClassTo__performOperation.hasNext()) {
				try {
					__performOperation = (EOperation) fujaba__Iter__eClassTo__performOperation
							.next();

					// check object __performOperation is really bound
					JavaSDM.ensure(__performOperation != null);
					// attribute condition
					JavaSDM.ensure(JavaSDM.stringCompare(
							__performOperation.getName(), "isApplicable_BWD") == 0);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// create object ruleresult
			ruleresult = TGGRuntimeFactory.eINSTANCE
					.createIsAppropriateRuleResult();

			// assign attribute ruleresult
			ruleresult.setSuccess(false);

			// create link
			ruleresult.setPerformOperation(__performOperation);

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'test core match kernel'
		try {
			fujaba__Success = false;

			// check object floor is really bound
			JavaSDM.ensure(floor != null);
			// bind object
			board = floor.getBoard();

			// check object board is really bound
			JavaSDM.ensure(board != null);

			// bind object
			leftNeighbor = floor.getLeft();

			// check object leftNeighbor is really bound
			JavaSDM.ensure(leftNeighbor != null);

			// check isomorphic binding between objects leftNeighbor and floor 
			JavaSDM.ensure(!leftNeighbor.equals(floor));

			// check link floors from leftNeighbor to board
			JavaSDM.ensure(board.equals(leftNeighbor.getBoard()));

			// attribute condition
			JavaSDM.ensure(leftNeighbor.getRow() == 0);

			// attribute condition
			JavaSDM.ensure(floor.getRow() == 0);

			// story node 'test core match'
			try {
				fujaba__Success = false;

				// check object board is really bound
				JavaSDM.ensure(board != null);
				// check object floor is really bound
				JavaSDM.ensure(floor != null);
				// check object leftNeighbor is really bound
				JavaSDM.ensure(leftNeighbor != null);
				// check object ruleresult is really bound
				JavaSDM.ensure(ruleresult != null);
				// check isomorphic binding between objects leftNeighbor and floor 
				JavaSDM.ensure(!leftNeighbor.equals(floor));

				// check link floors from floor to board
				JavaSDM.ensure(board.equals(floor.getBoard()));

				// check link floors from leftNeighbor to board
				JavaSDM.ensure(board.equals(leftNeighbor.getBoard()));

				// check link right from floor to leftNeighbor
				JavaSDM.ensure(leftNeighbor.equals(floor.getLeft()));

				// attribute condition
				JavaSDM.ensure(floor.getRow() == 0);

				// attribute condition
				JavaSDM.ensure(leftNeighbor.getRow() == 0);

				// create object match
				match = TGGRuntimeFactory.eINSTANCE.createMatch();

				// statement node 'bookkeeping with generic isAppropriate method'
				fujaba__Success = this.isAppropriate_BWD(match, board,
						leftNeighbor, floor);
				if (fujaba__Success) {
					// story node 'Add match to rule result'
					try {
						fujaba__Success = false;

						// check object match is really bound
						JavaSDM.ensure(match != null);
						// check object ruleresult is really bound
						JavaSDM.ensure(ruleresult != null);

						// create link
						match.setIsAppropriateRuleResult(ruleresult);

						fujaba__Success = true;
					} catch (JavaSDMException fujaba__InternalException) {
						fujaba__Success = false;
					}

				} else {

				}
				fujaba__Success = true;
			} catch (JavaSDMException fujaba__InternalException) {
				fujaba__Success = false;
			}

			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		// story node 'set success'
		try {
			fujaba__Success = false;

			// check object ruleresult is really bound
			JavaSDM.ensure(ruleresult != null);
			// iterate to-many link matches from ruleresult to match
			fujaba__Success = false;

			fujaba__IterRuleresultToMatch = ruleresult.getMatches().iterator();

			while (!(fujaba__Success)
					&& fujaba__IterRuleresultToMatch.hasNext()) {
				try {
					match = (Match) fujaba__IterRuleresultToMatch.next();

					// check object match is really bound
					JavaSDM.ensure(match != null);

					fujaba__Success = true;
				} catch (JavaSDMException fujaba__InternalException) {
					fujaba__Success = false;
				}
			}
			JavaSDM.ensure(fujaba__Success);
			// assign attribute ruleresult
			ruleresult.setSuccess(true);
			fujaba__Success = true;
		} catch (JavaSDMException fujaba__InternalException) {
			fujaba__Success = false;
		}

		return ruleresult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments)
			throws InvocationTargetException {
		switch (operationID) {
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_FWD__MATCH_NODE_NODE_NODE:
			return isAppropriate_FWD((Match) arguments.get(0),
					(Node) arguments.get(1), (Node) arguments.get(2),
					(Node) arguments.get(3));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___PERFORM_FWD__ISAPPLICABLEMATCH:
			return perform_FWD((IsApplicableMatch) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPLICABLE_FWD__MATCH:
			return isApplicable_FWD((Match) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_BWD__MATCH_BOARD_FLOOR_FLOOR:
			return isAppropriate_BWD((Match) arguments.get(0),
					(Board) arguments.get(1), (Floor) arguments.get(2),
					(Floor) arguments.get(3));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___PERFORM_BWD__ISAPPLICABLEMATCH:
			return perform_BWD((IsApplicableMatch) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPLICABLE_BWD__MATCH:
			return isApplicable_BWD((Match) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_FWD_NODE_21__NODE:
			return isAppropriate_FWD_Node_21((Node) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_FWD_NODE_22__NODE:
			return isAppropriate_FWD_Node_22((Node) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_FWD_NODE_23__NODE:
			return isAppropriate_FWD_Node_23((Node) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_BWD_BOARD_3__BOARD:
			return isAppropriate_BWD_Board_3((Board) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_BWD_FLOOR_9__FLOOR:
			return isAppropriate_BWD_Floor_9((Floor) arguments.get(0));
		case RulesPackage.TOP_ROW_NODE_TO_FLOOR_RULE___IS_APPROPRIATE_BWD_FLOOR_10__FLOOR:
			return isAppropriate_BWD_Floor_10((Floor) arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} //TopRowNodeToFloorRuleImpl