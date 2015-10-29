/**
 */
package org.moflon.tgg.mosl.tgg;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attr Cond Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getName <em>Name</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getParams <em>Params</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedSyncAdornments <em>Allowed Sync Adornments</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedAdornments <em>Allowed Adornments</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedGenAdornments <em>Allowed Gen Adornments</em>}</li>
 * </ul>
 *
 * @see org.moflon.tgg.mosl.tgg.TggPackage#getAttrCondDef()
 * @model
 * @generated
 */
public interface AttrCondDef extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getAttrCondDef_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Params</b></em>' containment reference list.
   * The list contents are of type {@link org.moflon.tgg.mosl.tgg.Param}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Params</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Params</em>' containment reference list.
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getAttrCondDef_Params()
   * @model containment="true"
   * @generated
   */
  EList<Param> getParams();

  /**
   * Returns the value of the '<em><b>Allowed Sync Adornments</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Allowed Sync Adornments</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Allowed Sync Adornments</em>' attribute list.
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getAttrCondDef_AllowedSyncAdornments()
   * @model unique="false"
   * @generated
   */
  EList<String> getAllowedSyncAdornments();

  /**
   * Returns the value of the '<em><b>Allowed Adornments</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Allowed Adornments</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Allowed Adornments</em>' attribute list.
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getAttrCondDef_AllowedAdornments()
   * @model unique="false"
   * @generated
   */
  EList<String> getAllowedAdornments();

  /**
   * Returns the value of the '<em><b>Allowed Gen Adornments</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Allowed Gen Adornments</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Allowed Gen Adornments</em>' attribute list.
   * @see org.moflon.tgg.mosl.tgg.TggPackage#getAttrCondDef_AllowedGenAdornments()
   * @model unique="false"
   * @generated
   */
  EList<String> getAllowedGenAdornments();

} // AttrCondDef
