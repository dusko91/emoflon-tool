/**
 */
package org.moflon.tgg.mosl.tgg.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.moflon.tgg.mosl.tgg.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.moflon.tgg.mosl.tgg.TggPackage
 * @generated
 */
public class TggAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static TggPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TggAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = TggPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TggSwitch<Adapter> modelSwitch =
    new TggSwitch<Adapter>()
    {
      @Override
      public Adapter caseTripleGraphGrammar(TripleGraphGrammar object)
      {
        return createTripleGraphGrammarAdapter();
      }
      @Override
      public Adapter caseSpecFile(SpecFile object)
      {
        return createSpecFileAdapter();
      }
      @Override
      public Adapter caseSchema(Schema object)
      {
        return createSchemaAdapter();
      }
      @Override
      public Adapter caseCorrType(CorrType object)
      {
        return createCorrTypeAdapter();
      }
      @Override
      public Adapter caseTypeExtension(TypeExtension object)
      {
        return createTypeExtensionAdapter();
      }
      @Override
      public Adapter caseCorrTypeDef(CorrTypeDef object)
      {
        return createCorrTypeDefAdapter();
      }
      @Override
      public Adapter caseRule(Rule object)
      {
        return createRuleAdapter();
      }
      @Override
      public Adapter caseAttrCond(AttrCond object)
      {
        return createAttrCondAdapter();
      }
      @Override
      public Adapter caseAttrCondDef(AttrCondDef object)
      {
        return createAttrCondDefAdapter();
      }
      @Override
      public Adapter caseParam(Param object)
      {
        return createParamAdapter();
      }
      @Override
      public Adapter caseParamValue(ParamValue object)
      {
        return createParamValueAdapter();
      }
      @Override
      public Adapter caseCorrVariablePattern(CorrVariablePattern object)
      {
        return createCorrVariablePatternAdapter();
      }
      @Override
      public Adapter caseObjectVariablePattern(ObjectVariablePattern object)
      {
        return createObjectVariablePatternAdapter();
      }
      @Override
      public Adapter caseLinkVariablePattern(LinkVariablePattern object)
      {
        return createLinkVariablePatternAdapter();
      }
      @Override
      public Adapter caseOperator(Operator object)
      {
        return createOperatorAdapter();
      }
      @Override
      public Adapter caseImport(Import object)
      {
        return createImportAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.TripleGraphGrammar <em>Triple Graph Grammar</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.TripleGraphGrammar
   * @generated
   */
  public Adapter createTripleGraphGrammarAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.SpecFile <em>Spec File</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.SpecFile
   * @generated
   */
  public Adapter createSpecFileAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.Schema <em>Schema</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.Schema
   * @generated
   */
  public Adapter createSchemaAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.CorrType <em>Corr Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.CorrType
   * @generated
   */
  public Adapter createCorrTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.TypeExtension <em>Type Extension</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.TypeExtension
   * @generated
   */
  public Adapter createTypeExtensionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.CorrTypeDef <em>Corr Type Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.CorrTypeDef
   * @generated
   */
  public Adapter createCorrTypeDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.Rule <em>Rule</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.Rule
   * @generated
   */
  public Adapter createRuleAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.AttrCond <em>Attr Cond</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.AttrCond
   * @generated
   */
  public Adapter createAttrCondAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.AttrCondDef <em>Attr Cond Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.AttrCondDef
   * @generated
   */
  public Adapter createAttrCondDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.Param <em>Param</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.Param
   * @generated
   */
  public Adapter createParamAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.ParamValue <em>Param Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.ParamValue
   * @generated
   */
  public Adapter createParamValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.CorrVariablePattern <em>Corr Variable Pattern</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.CorrVariablePattern
   * @generated
   */
  public Adapter createCorrVariablePatternAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.ObjectVariablePattern <em>Object Variable Pattern</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.ObjectVariablePattern
   * @generated
   */
  public Adapter createObjectVariablePatternAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern <em>Link Variable Pattern</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.LinkVariablePattern
   * @generated
   */
  public Adapter createLinkVariablePatternAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.Operator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.Operator
   * @generated
   */
  public Adapter createOperatorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.moflon.tgg.mosl.tgg.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.moflon.tgg.mosl.tgg.Import
   * @generated
   */
  public Adapter createImportAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //TggAdapterFactory
