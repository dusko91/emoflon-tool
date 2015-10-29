/**
 */
package org.moflon.tgg.mosl.tgg.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.moflon.tgg.mosl.tgg.AttrCondDef;
import org.moflon.tgg.mosl.tgg.Param;
import org.moflon.tgg.mosl.tgg.TggPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attr Cond Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl#getParams <em>Params</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl#getAllowedSyncAdornments <em>Allowed Sync Adornments</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl#getAllowedAdornments <em>Allowed Adornments</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl#getAllowedGenAdornments <em>Allowed Gen Adornments</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AttrCondDefImpl extends MinimalEObjectImpl.Container implements AttrCondDef
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getParams() <em>Params</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParams()
   * @generated
   * @ordered
   */
  protected EList<Param> params;

  /**
   * The cached value of the '{@link #getAllowedSyncAdornments() <em>Allowed Sync Adornments</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAllowedSyncAdornments()
   * @generated
   * @ordered
   */
  protected EList<String> allowedSyncAdornments;

  /**
   * The cached value of the '{@link #getAllowedAdornments() <em>Allowed Adornments</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAllowedAdornments()
   * @generated
   * @ordered
   */
  protected EList<String> allowedAdornments;

  /**
   * The cached value of the '{@link #getAllowedGenAdornments() <em>Allowed Gen Adornments</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAllowedGenAdornments()
   * @generated
   * @ordered
   */
  protected EList<String> allowedGenAdornments;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AttrCondDefImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return TggPackage.Literals.ATTR_COND_DEF;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TggPackage.ATTR_COND_DEF__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Param> getParams()
  {
    if (params == null)
    {
      params = new EObjectContainmentEList<Param>(Param.class, this, TggPackage.ATTR_COND_DEF__PARAMS);
    }
    return params;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getAllowedSyncAdornments()
  {
    if (allowedSyncAdornments == null)
    {
      allowedSyncAdornments = new EDataTypeEList<String>(String.class, this, TggPackage.ATTR_COND_DEF__ALLOWED_SYNC_ADORNMENTS);
    }
    return allowedSyncAdornments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getAllowedAdornments()
  {
    if (allowedAdornments == null)
    {
      allowedAdornments = new EDataTypeEList<String>(String.class, this, TggPackage.ATTR_COND_DEF__ALLOWED_ADORNMENTS);
    }
    return allowedAdornments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getAllowedGenAdornments()
  {
    if (allowedGenAdornments == null)
    {
      allowedGenAdornments = new EDataTypeEList<String>(String.class, this, TggPackage.ATTR_COND_DEF__ALLOWED_GEN_ADORNMENTS);
    }
    return allowedGenAdornments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case TggPackage.ATTR_COND_DEF__PARAMS:
        return ((InternalEList<?>)getParams()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case TggPackage.ATTR_COND_DEF__NAME:
        return getName();
      case TggPackage.ATTR_COND_DEF__PARAMS:
        return getParams();
      case TggPackage.ATTR_COND_DEF__ALLOWED_SYNC_ADORNMENTS:
        return getAllowedSyncAdornments();
      case TggPackage.ATTR_COND_DEF__ALLOWED_ADORNMENTS:
        return getAllowedAdornments();
      case TggPackage.ATTR_COND_DEF__ALLOWED_GEN_ADORNMENTS:
        return getAllowedGenAdornments();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case TggPackage.ATTR_COND_DEF__NAME:
        setName((String)newValue);
        return;
      case TggPackage.ATTR_COND_DEF__PARAMS:
        getParams().clear();
        getParams().addAll((Collection<? extends Param>)newValue);
        return;
      case TggPackage.ATTR_COND_DEF__ALLOWED_SYNC_ADORNMENTS:
        getAllowedSyncAdornments().clear();
        getAllowedSyncAdornments().addAll((Collection<? extends String>)newValue);
        return;
      case TggPackage.ATTR_COND_DEF__ALLOWED_ADORNMENTS:
        getAllowedAdornments().clear();
        getAllowedAdornments().addAll((Collection<? extends String>)newValue);
        return;
      case TggPackage.ATTR_COND_DEF__ALLOWED_GEN_ADORNMENTS:
        getAllowedGenAdornments().clear();
        getAllowedGenAdornments().addAll((Collection<? extends String>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case TggPackage.ATTR_COND_DEF__NAME:
        setName(NAME_EDEFAULT);
        return;
      case TggPackage.ATTR_COND_DEF__PARAMS:
        getParams().clear();
        return;
      case TggPackage.ATTR_COND_DEF__ALLOWED_SYNC_ADORNMENTS:
        getAllowedSyncAdornments().clear();
        return;
      case TggPackage.ATTR_COND_DEF__ALLOWED_ADORNMENTS:
        getAllowedAdornments().clear();
        return;
      case TggPackage.ATTR_COND_DEF__ALLOWED_GEN_ADORNMENTS:
        getAllowedGenAdornments().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case TggPackage.ATTR_COND_DEF__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case TggPackage.ATTR_COND_DEF__PARAMS:
        return params != null && !params.isEmpty();
      case TggPackage.ATTR_COND_DEF__ALLOWED_SYNC_ADORNMENTS:
        return allowedSyncAdornments != null && !allowedSyncAdornments.isEmpty();
      case TggPackage.ATTR_COND_DEF__ALLOWED_ADORNMENTS:
        return allowedAdornments != null && !allowedAdornments.isEmpty();
      case TggPackage.ATTR_COND_DEF__ALLOWED_GEN_ADORNMENTS:
        return allowedGenAdornments != null && !allowedGenAdornments.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(", allowedSyncAdornments: ");
    result.append(allowedSyncAdornments);
    result.append(", allowedAdornments: ");
    result.append(allowedAdornments);
    result.append(", allowedGenAdornments: ");
    result.append(allowedGenAdornments);
    result.append(')');
    return result.toString();
  }

} //AttrCondDefImpl
