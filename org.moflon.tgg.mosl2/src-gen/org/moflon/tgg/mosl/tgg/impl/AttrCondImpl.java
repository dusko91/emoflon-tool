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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.moflon.tgg.mosl.tgg.AttrCond;
import org.moflon.tgg.mosl.tgg.AttrCondDef;
import org.moflon.tgg.mosl.tgg.ParamValue;
import org.moflon.tgg.mosl.tgg.TggPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attr Cond</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.AttrCondImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.AttrCondImpl#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AttrCondImpl extends MinimalEObjectImpl.Container implements AttrCond
{
  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected AttrCondDef name;

  /**
   * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValues()
   * @generated
   * @ordered
   */
  protected EList<ParamValue> values;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AttrCondImpl()
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
    return TggPackage.Literals.ATTR_COND;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttrCondDef getName()
  {
    if (name != null && name.eIsProxy())
    {
      InternalEObject oldName = (InternalEObject)name;
      name = (AttrCondDef)eResolveProxy(oldName);
      if (name != oldName)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, TggPackage.ATTR_COND__NAME, oldName, name));
      }
    }
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttrCondDef basicGetName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(AttrCondDef newName)
  {
    AttrCondDef oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TggPackage.ATTR_COND__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ParamValue> getValues()
  {
    if (values == null)
    {
      values = new EObjectContainmentEList<ParamValue>(ParamValue.class, this, TggPackage.ATTR_COND__VALUES);
    }
    return values;
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
      case TggPackage.ATTR_COND__VALUES:
        return ((InternalEList<?>)getValues()).basicRemove(otherEnd, msgs);
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
      case TggPackage.ATTR_COND__NAME:
        if (resolve) return getName();
        return basicGetName();
      case TggPackage.ATTR_COND__VALUES:
        return getValues();
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
      case TggPackage.ATTR_COND__NAME:
        setName((AttrCondDef)newValue);
        return;
      case TggPackage.ATTR_COND__VALUES:
        getValues().clear();
        getValues().addAll((Collection<? extends ParamValue>)newValue);
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
      case TggPackage.ATTR_COND__NAME:
        setName((AttrCondDef)null);
        return;
      case TggPackage.ATTR_COND__VALUES:
        getValues().clear();
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
      case TggPackage.ATTR_COND__NAME:
        return name != null;
      case TggPackage.ATTR_COND__VALUES:
        return values != null && !values.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //AttrCondImpl
