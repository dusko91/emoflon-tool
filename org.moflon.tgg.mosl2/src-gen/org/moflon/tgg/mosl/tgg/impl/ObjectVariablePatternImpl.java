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

import org.moflon.tgg.mosl.tgg.LinkVariablePattern;
import org.moflon.tgg.mosl.tgg.ObjectVariablePattern;
import org.moflon.tgg.mosl.tgg.Operator;
import org.moflon.tgg.mosl.tgg.TggPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Object Variable Pattern</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl#getOp <em>Op</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl#getLinkVariablePatterns <em>Link Variable Patterns</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ObjectVariablePatternImpl extends MinimalEObjectImpl.Container implements ObjectVariablePattern
{
  /**
   * The cached value of the '{@link #getOp() <em>Op</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected Operator op;

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
   * The cached value of the '{@link #getType() <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected EClass type;

  /**
   * The cached value of the '{@link #getLinkVariablePatterns() <em>Link Variable Patterns</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLinkVariablePatterns()
   * @generated
   * @ordered
   */
  protected EList<LinkVariablePattern> linkVariablePatterns;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ObjectVariablePatternImpl()
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
    return TggPackage.Literals.OBJECT_VARIABLE_PATTERN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operator getOp()
  {
    return op;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOp(Operator newOp, NotificationChain msgs)
  {
    Operator oldOp = op;
    op = newOp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TggPackage.OBJECT_VARIABLE_PATTERN__OP, oldOp, newOp);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp(Operator newOp)
  {
    if (newOp != op)
    {
      NotificationChain msgs = null;
      if (op != null)
        msgs = ((InternalEObject)op).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TggPackage.OBJECT_VARIABLE_PATTERN__OP, null, msgs);
      if (newOp != null)
        msgs = ((InternalEObject)newOp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TggPackage.OBJECT_VARIABLE_PATTERN__OP, null, msgs);
      msgs = basicSetOp(newOp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TggPackage.OBJECT_VARIABLE_PATTERN__OP, newOp, newOp));
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
      eNotify(new ENotificationImpl(this, Notification.SET, TggPackage.OBJECT_VARIABLE_PATTERN__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getType()
  {
    if (type != null && type.eIsProxy())
    {
      InternalEObject oldType = (InternalEObject)type;
      type = (EClass)eResolveProxy(oldType);
      if (type != oldType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, TggPackage.OBJECT_VARIABLE_PATTERN__TYPE, oldType, type));
      }
    }
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass basicGetType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setType(EClass newType)
  {
    EClass oldType = type;
    type = newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TggPackage.OBJECT_VARIABLE_PATTERN__TYPE, oldType, type));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<LinkVariablePattern> getLinkVariablePatterns()
  {
    if (linkVariablePatterns == null)
    {
      linkVariablePatterns = new EObjectContainmentEList<LinkVariablePattern>(LinkVariablePattern.class, this, TggPackage.OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS);
    }
    return linkVariablePatterns;
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
      case TggPackage.OBJECT_VARIABLE_PATTERN__OP:
        return basicSetOp(null, msgs);
      case TggPackage.OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS:
        return ((InternalEList<?>)getLinkVariablePatterns()).basicRemove(otherEnd, msgs);
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
      case TggPackage.OBJECT_VARIABLE_PATTERN__OP:
        return getOp();
      case TggPackage.OBJECT_VARIABLE_PATTERN__NAME:
        return getName();
      case TggPackage.OBJECT_VARIABLE_PATTERN__TYPE:
        if (resolve) return getType();
        return basicGetType();
      case TggPackage.OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS:
        return getLinkVariablePatterns();
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
      case TggPackage.OBJECT_VARIABLE_PATTERN__OP:
        setOp((Operator)newValue);
        return;
      case TggPackage.OBJECT_VARIABLE_PATTERN__NAME:
        setName((String)newValue);
        return;
      case TggPackage.OBJECT_VARIABLE_PATTERN__TYPE:
        setType((EClass)newValue);
        return;
      case TggPackage.OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS:
        getLinkVariablePatterns().clear();
        getLinkVariablePatterns().addAll((Collection<? extends LinkVariablePattern>)newValue);
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
      case TggPackage.OBJECT_VARIABLE_PATTERN__OP:
        setOp((Operator)null);
        return;
      case TggPackage.OBJECT_VARIABLE_PATTERN__NAME:
        setName(NAME_EDEFAULT);
        return;
      case TggPackage.OBJECT_VARIABLE_PATTERN__TYPE:
        setType((EClass)null);
        return;
      case TggPackage.OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS:
        getLinkVariablePatterns().clear();
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
      case TggPackage.OBJECT_VARIABLE_PATTERN__OP:
        return op != null;
      case TggPackage.OBJECT_VARIABLE_PATTERN__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case TggPackage.OBJECT_VARIABLE_PATTERN__TYPE:
        return type != null;
      case TggPackage.OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS:
        return linkVariablePatterns != null && !linkVariablePatterns.isEmpty();
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
    result.append(')');
    return result.toString();
  }

} //ObjectVariablePatternImpl
