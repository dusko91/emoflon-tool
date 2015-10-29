/**
 */
package org.moflon.tgg.mosl.tgg.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.moflon.tgg.mosl.tgg.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TggFactoryImpl extends EFactoryImpl implements TggFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TggFactory init()
  {
    try
    {
      TggFactory theTggFactory = (TggFactory)EPackage.Registry.INSTANCE.getEFactory(TggPackage.eNS_URI);
      if (theTggFactory != null)
      {
        return theTggFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new TggFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TggFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case TggPackage.TRIPLE_GRAPH_GRAMMAR: return createTripleGraphGrammar();
      case TggPackage.SPEC_FILE: return createSpecFile();
      case TggPackage.SCHEMA: return createSchema();
      case TggPackage.CORR_TYPE: return createCorrType();
      case TggPackage.TYPE_EXTENSION: return createTypeExtension();
      case TggPackage.CORR_TYPE_DEF: return createCorrTypeDef();
      case TggPackage.RULE: return createRule();
      case TggPackage.ATTR_COND: return createAttrCond();
      case TggPackage.ATTR_COND_DEF: return createAttrCondDef();
      case TggPackage.PARAM: return createParam();
      case TggPackage.PARAM_VALUE: return createParamValue();
      case TggPackage.CORR_VARIABLE_PATTERN: return createCorrVariablePattern();
      case TggPackage.OBJECT_VARIABLE_PATTERN: return createObjectVariablePattern();
      case TggPackage.LINK_VARIABLE_PATTERN: return createLinkVariablePattern();
      case TggPackage.OPERATOR: return createOperator();
      case TggPackage.IMPORT: return createImport();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TripleGraphGrammar createTripleGraphGrammar()
  {
    TripleGraphGrammarImpl tripleGraphGrammar = new TripleGraphGrammarImpl();
    return tripleGraphGrammar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SpecFile createSpecFile()
  {
    SpecFileImpl specFile = new SpecFileImpl();
    return specFile;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Schema createSchema()
  {
    SchemaImpl schema = new SchemaImpl();
    return schema;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CorrType createCorrType()
  {
    CorrTypeImpl corrType = new CorrTypeImpl();
    return corrType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TypeExtension createTypeExtension()
  {
    TypeExtensionImpl typeExtension = new TypeExtensionImpl();
    return typeExtension;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CorrTypeDef createCorrTypeDef()
  {
    CorrTypeDefImpl corrTypeDef = new CorrTypeDefImpl();
    return corrTypeDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Rule createRule()
  {
    RuleImpl rule = new RuleImpl();
    return rule;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttrCond createAttrCond()
  {
    AttrCondImpl attrCond = new AttrCondImpl();
    return attrCond;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttrCondDef createAttrCondDef()
  {
    AttrCondDefImpl attrCondDef = new AttrCondDefImpl();
    return attrCondDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Param createParam()
  {
    ParamImpl param = new ParamImpl();
    return param;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ParamValue createParamValue()
  {
    ParamValueImpl paramValue = new ParamValueImpl();
    return paramValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CorrVariablePattern createCorrVariablePattern()
  {
    CorrVariablePatternImpl corrVariablePattern = new CorrVariablePatternImpl();
    return corrVariablePattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ObjectVariablePattern createObjectVariablePattern()
  {
    ObjectVariablePatternImpl objectVariablePattern = new ObjectVariablePatternImpl();
    return objectVariablePattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LinkVariablePattern createLinkVariablePattern()
  {
    LinkVariablePatternImpl linkVariablePattern = new LinkVariablePatternImpl();
    return linkVariablePattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operator createOperator()
  {
    OperatorImpl operator = new OperatorImpl();
    return operator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Import createImport()
  {
    ImportImpl import_ = new ImportImpl();
    return import_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TggPackage getTggPackage()
  {
    return (TggPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static TggPackage getPackage()
  {
    return TggPackage.eINSTANCE;
  }

} //TggFactoryImpl
