/**
 */
package org.moflon.tgg.mosl.tgg;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Corr Type Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.CorrTypeDef#getSource <em>Source</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.CorrTypeDef#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.moflon.tgg.mosl.tgg.TggPackage#getCorrTypeDef()
 * @model
 * @generated
 */
public interface CorrTypeDef extends CorrType
{
  /**
   * Returns the value of the '<em><b>Source</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Source</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Source</em>' reference.
   * @see #setSource(EClass)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getCorrTypeDef_Source()
   * @model
   * @generated
   */
  EClass getSource();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.CorrTypeDef#getSource <em>Source</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Source</em>' reference.
   * @see #getSource()
   * @generated
   */
  void setSource(EClass value);

  /**
   * Returns the value of the '<em><b>Target</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target</em>' reference.
   * @see #setTarget(EClass)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getCorrTypeDef_Target()
   * @model
   * @generated
   */
  EClass getTarget();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.CorrTypeDef#getTarget <em>Target</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target</em>' reference.
   * @see #getTarget()
   * @generated
   */
  void setTarget(EClass value);

} // CorrTypeDef
