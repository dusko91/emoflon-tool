/**
 */
package org.moflon.tgg.mosl.tgg;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.moflon.tgg.mosl.tgg.TggPackage
 * @generated
 */
public interface TggFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  TggFactory eINSTANCE = org.moflon.tgg.mosl.tgg.impl.TggFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Triple Graph Grammar</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Triple Graph Grammar</em>'.
   * @generated
   */
  TripleGraphGrammar createTripleGraphGrammar();

  /**
   * Returns a new object of class '<em>Spec File</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Spec File</em>'.
   * @generated
   */
  SpecFile createSpecFile();

  /**
   * Returns a new object of class '<em>Schema</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Schema</em>'.
   * @generated
   */
  Schema createSchema();

  /**
   * Returns a new object of class '<em>Corr Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Corr Type</em>'.
   * @generated
   */
  CorrType createCorrType();

  /**
   * Returns a new object of class '<em>Type Extension</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Type Extension</em>'.
   * @generated
   */
  TypeExtension createTypeExtension();

  /**
   * Returns a new object of class '<em>Corr Type Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Corr Type Def</em>'.
   * @generated
   */
  CorrTypeDef createCorrTypeDef();

  /**
   * Returns a new object of class '<em>Rule</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Rule</em>'.
   * @generated
   */
  Rule createRule();

  /**
   * Returns a new object of class '<em>Attr Cond</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Attr Cond</em>'.
   * @generated
   */
  AttrCond createAttrCond();

  /**
   * Returns a new object of class '<em>Attr Cond Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Attr Cond Def</em>'.
   * @generated
   */
  AttrCondDef createAttrCondDef();

  /**
   * Returns a new object of class '<em>Param</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Param</em>'.
   * @generated
   */
  Param createParam();

  /**
   * Returns a new object of class '<em>Param Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Param Value</em>'.
   * @generated
   */
  ParamValue createParamValue();

  /**
   * Returns a new object of class '<em>Corr Variable Pattern</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Corr Variable Pattern</em>'.
   * @generated
   */
  CorrVariablePattern createCorrVariablePattern();

  /**
   * Returns a new object of class '<em>Object Variable Pattern</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Object Variable Pattern</em>'.
   * @generated
   */
  ObjectVariablePattern createObjectVariablePattern();

  /**
   * Returns a new object of class '<em>Link Variable Pattern</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Link Variable Pattern</em>'.
   * @generated
   */
  LinkVariablePattern createLinkVariablePattern();

  /**
   * Returns a new object of class '<em>Operator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operator</em>'.
   * @generated
   */
  Operator createOperator();

  /**
   * Returns a new object of class '<em>Import</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Import</em>'.
   * @generated
   */
  Import createImport();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  TggPackage getTggPackage();

} //TggFactory
