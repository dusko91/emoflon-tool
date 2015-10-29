/**
 */
package org.moflon.tgg.mosl.tgg;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Link Variable Pattern</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getOp <em>Op</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getType <em>Type</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.moflon.tgg.mosl.tgg.TggPackage#getLinkVariablePattern()
 * @model
 * @generated
 */
public interface LinkVariablePattern extends EObject
{
  /**
   * Returns the value of the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Op</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Op</em>' containment reference.
   * @see #setOp(Operator)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getLinkVariablePattern_Op()
   * @model containment="true"
   * @generated
   */
  Operator getOp();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getOp <em>Op</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op</em>' containment reference.
   * @see #getOp()
   * @generated
   */
  void setOp(Operator value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' reference.
   * @see #setType(EReference)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getLinkVariablePattern_Type()
   * @model
   * @generated
   */
  EReference getType();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getType <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' reference.
   * @see #getType()
   * @generated
   */
  void setType(EReference value);

  /**
   * Returns the value of the '<em><b>Target</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target</em>' reference.
   * @see #setTarget(ObjectVariablePattern)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getLinkVariablePattern_Target()
   * @model
   * @generated
   */
  ObjectVariablePattern getTarget();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getTarget <em>Target</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target</em>' reference.
   * @see #getTarget()
   * @generated
   */
  void setTarget(ObjectVariablePattern value);

} // LinkVariablePattern
