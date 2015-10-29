/**
 */
package org.moflon.tgg.mosl.tgg.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.moflon.tgg.mosl.tgg.ObjectVariablePattern;
import org.moflon.tgg.mosl.tgg.ParamValue;
import org.moflon.tgg.mosl.tgg.TggPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Param Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.ParamValueImpl#getObjectVar <em>Object Var</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.ParamValueImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParamValueImpl extends MinimalEObjectImpl.Container implements ParamValue
{
  /**
   * The cached value of the '{@link #getObjectVar() <em>Object Var</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getObjectVar()
   * @generated
   * @ordered
   */
  protected ObjectVariablePattern objectVar;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected EAttribute type;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ParamValueImpl()
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
    return TggPackage.Literals.PARAM_VALUE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ObjectVariablePattern getObjectVar()
  {
    if (objectVar != null && objectVar.eIsProxy())
    {
      InternalEObject oldObjectVar = (InternalEObject)objectVar;
      objectVar = (ObjectVariablePattern)eResolveProxy(oldObjectVar);
      if (objectVar != oldObjectVar)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, TggPackage.PARAM_VALUE__OBJECT_VAR, oldObjectVar, objectVar));
      }
    }
    return objectVar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ObjectVariablePattern basicGetObjectVar()
  {
    return objectVar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setObjectVar(ObjectVariablePattern newObjectVar)
  {
    ObjectVariablePattern oldObjectVar = objectVar;
    objectVar = newObjectVar;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TggPackage.PARAM_VALUE__OBJECT_VAR, oldObjectVar, objectVar));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getType()
  {
    if (type != null && type.eIsProxy())
    {
      InternalEObject oldType = (InternalEObject)type;
      type = (EAttribute)eResolveProxy(oldType);
      if (type != oldType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, TggPackage.PARAM_VALUE__TYPE, oldType, type));
      }
    }
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute basicGetType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setType(EAttribute newType)
  {
    EAttribute oldType = type;
    type = newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TggPackage.PARAM_VALUE__TYPE, oldType, type));
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
      case TggPackage.PARAM_VALUE__OBJECT_VAR:
        if (resolve) return getObjectVar();
        return basicGetObjectVar();
      case TggPackage.PARAM_VALUE__TYPE:
        if (resolve) return getType();
        return basicGetType();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case TggPackage.PARAM_VALUE__OBJECT_VAR:
        setObjectVar((ObjectVariablePattern)newValue);
        return;
      case TggPackage.PARAM_VALUE__TYPE:
        setType((EAttribute)newValue);
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
      case TggPackage.PARAM_VALUE__OBJECT_VAR:
        setObjectVar((ObjectVariablePattern)null);
        return;
      case TggPackage.PARAM_VALUE__TYPE:
        setType((EAttribute)null);
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
      case TggPackage.PARAM_VALUE__OBJECT_VAR:
        return objectVar != null;
      case TggPackage.PARAM_VALUE__TYPE:
        return type != null;
    }
    return super.eIsSet(featureID);
  }

} //ParamValueImpl
