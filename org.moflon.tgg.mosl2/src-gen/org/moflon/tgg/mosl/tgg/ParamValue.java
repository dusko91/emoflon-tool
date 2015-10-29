/**
 */
package org.moflon.tgg.mosl.tgg;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Param Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.ParamValue#getObjectVar <em>Object Var</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.ParamValue#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.moflon.tgg.mosl.tgg.TggPackage#getParamValue()
 * @model
 * @generated
 */
public interface ParamValue extends EObject
{
  /**
   * Returns the value of the '<em><b>Object Var</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Object Var</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Object Var</em>' reference.
   * @see #setObjectVar(ObjectVariablePattern)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getParamValue_ObjectVar()
   * @model
   * @generated
   */
  ObjectVariablePattern getObjectVar();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.ParamValue#getObjectVar <em>Object Var</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Object Var</em>' reference.
   * @see #getObjectVar()
   * @generated
   */
  void setObjectVar(ObjectVariablePattern value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' reference.
   * @see #setType(EAttribute)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getParamValue_Type()
   * @model
   * @generated
   */
  EAttribute getType();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.ParamValue#getType <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' reference.
   * @see #getType()
   * @generated
   */
  void setType(EAttribute value);

} // ParamValue
