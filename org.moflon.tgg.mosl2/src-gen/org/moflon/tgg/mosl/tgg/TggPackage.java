/**
 */
package org.moflon.tgg.mosl.tgg;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.moflon.tgg.mosl.tgg.TggFactory
 * @model kind="package"
 * @generated
 */
public interface TggPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "tgg";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.moflon.org/ide/tgg/mosl/TGG";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "tgg";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  TggPackage eINSTANCE = org.moflon.tgg.mosl.tgg.impl.TggPackageImpl.init();

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.TripleGraphGrammarImpl <em>Triple Graph Grammar</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.TripleGraphGrammarImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getTripleGraphGrammar()
   * @generated
   */
  int TRIPLE_GRAPH_GRAMMAR = 0;

  /**
   * The feature id for the '<em><b>Schema</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRIPLE_GRAPH_GRAMMAR__SCHEMA = 0;

  /**
   * The feature id for the '<em><b>Rules</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRIPLE_GRAPH_GRAMMAR__RULES = 1;

  /**
   * The number of structural features of the '<em>Triple Graph Grammar</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRIPLE_GRAPH_GRAMMAR_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.SpecFileImpl <em>Spec File</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.SpecFileImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getSpecFile()
   * @generated
   */
  int SPEC_FILE = 1;

  /**
   * The feature id for the '<em><b>Imports</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SPEC_FILE__IMPORTS = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SPEC_FILE__NAME = 1;

  /**
   * The number of structural features of the '<em>Spec File</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SPEC_FILE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.SchemaImpl <em>Schema</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.SchemaImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getSchema()
   * @generated
   */
  int SCHEMA = 2;

  /**
   * The feature id for the '<em><b>Imports</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__IMPORTS = SPEC_FILE__IMPORTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__NAME = SPEC_FILE__NAME;

  /**
   * The feature id for the '<em><b>Source Types</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__SOURCE_TYPES = SPEC_FILE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target Types</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__TARGET_TYPES = SPEC_FILE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Correspondence Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__CORRESPONDENCE_TYPES = SPEC_FILE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Attribute Cond Defs</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__ATTRIBUTE_COND_DEFS = SPEC_FILE_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Schema</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_FEATURE_COUNT = SPEC_FILE_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.CorrTypeImpl <em>Corr Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.CorrTypeImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getCorrType()
   * @generated
   */
  int CORR_TYPE = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_TYPE__NAME = 0;

  /**
   * The number of structural features of the '<em>Corr Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_TYPE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.TypeExtensionImpl <em>Type Extension</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.TypeExtensionImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getTypeExtension()
   * @generated
   */
  int TYPE_EXTENSION = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_EXTENSION__NAME = CORR_TYPE__NAME;

  /**
   * The feature id for the '<em><b>Super</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_EXTENSION__SUPER = CORR_TYPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Type Extension</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_EXTENSION_FEATURE_COUNT = CORR_TYPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.CorrTypeDefImpl <em>Corr Type Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.CorrTypeDefImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getCorrTypeDef()
   * @generated
   */
  int CORR_TYPE_DEF = 5;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_TYPE_DEF__NAME = CORR_TYPE__NAME;

  /**
   * The feature id for the '<em><b>Source</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_TYPE_DEF__SOURCE = CORR_TYPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_TYPE_DEF__TARGET = CORR_TYPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Corr Type Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_TYPE_DEF_FEATURE_COUNT = CORR_TYPE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.RuleImpl <em>Rule</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.RuleImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getRule()
   * @generated
   */
  int RULE = 6;

  /**
   * The feature id for the '<em><b>Imports</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__IMPORTS = SPEC_FILE__IMPORTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__NAME = SPEC_FILE__NAME;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__ABSTRACT = SPEC_FILE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Supertypes</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__SUPERTYPES = SPEC_FILE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Schema</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__SCHEMA = SPEC_FILE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Source Patterns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__SOURCE_PATTERNS = SPEC_FILE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Target Patterns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__TARGET_PATTERNS = SPEC_FILE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Correspondence Patterns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__CORRESPONDENCE_PATTERNS = SPEC_FILE_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Attr Conditions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__ATTR_CONDITIONS = SPEC_FILE_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>Rule</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE_FEATURE_COUNT = SPEC_FILE_FEATURE_COUNT + 7;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.AttrCondImpl <em>Attr Cond</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.AttrCondImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getAttrCond()
   * @generated
   */
  int ATTR_COND = 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND__NAME = 0;

  /**
   * The feature id for the '<em><b>Values</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND__VALUES = 1;

  /**
   * The number of structural features of the '<em>Attr Cond</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl <em>Attr Cond Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getAttrCondDef()
   * @generated
   */
  int ATTR_COND_DEF = 8;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND_DEF__NAME = 0;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND_DEF__PARAMS = 1;

  /**
   * The feature id for the '<em><b>Allowed Sync Adornments</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND_DEF__ALLOWED_SYNC_ADORNMENTS = 2;

  /**
   * The feature id for the '<em><b>Allowed Adornments</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND_DEF__ALLOWED_ADORNMENTS = 3;

  /**
   * The feature id for the '<em><b>Allowed Gen Adornments</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND_DEF__ALLOWED_GEN_ADORNMENTS = 4;

  /**
   * The number of structural features of the '<em>Attr Cond Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTR_COND_DEF_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.ParamImpl <em>Param</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.ParamImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getParam()
   * @generated
   */
  int PARAM = 9;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM__NAME = 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM__TYPE = 1;

  /**
   * The number of structural features of the '<em>Param</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.ParamValueImpl <em>Param Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.ParamValueImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getParamValue()
   * @generated
   */
  int PARAM_VALUE = 10;

  /**
   * The feature id for the '<em><b>Object Var</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_VALUE__OBJECT_VAR = 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_VALUE__TYPE = 1;

  /**
   * The number of structural features of the '<em>Param Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAM_VALUE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.CorrVariablePatternImpl <em>Corr Variable Pattern</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.CorrVariablePatternImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getCorrVariablePattern()
   * @generated
   */
  int CORR_VARIABLE_PATTERN = 11;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_VARIABLE_PATTERN__OP = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_VARIABLE_PATTERN__NAME = 1;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_VARIABLE_PATTERN__TYPE = 2;

  /**
   * The feature id for the '<em><b>Source</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_VARIABLE_PATTERN__SOURCE = 3;

  /**
   * The feature id for the '<em><b>Target</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_VARIABLE_PATTERN__TARGET = 4;

  /**
   * The number of structural features of the '<em>Corr Variable Pattern</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CORR_VARIABLE_PATTERN_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl <em>Object Variable Pattern</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getObjectVariablePattern()
   * @generated
   */
  int OBJECT_VARIABLE_PATTERN = 12;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBJECT_VARIABLE_PATTERN__OP = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBJECT_VARIABLE_PATTERN__NAME = 1;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBJECT_VARIABLE_PATTERN__TYPE = 2;

  /**
   * The feature id for the '<em><b>Link Variable Patterns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS = 3;

  /**
   * The number of structural features of the '<em>Object Variable Pattern</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBJECT_VARIABLE_PATTERN_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.LinkVariablePatternImpl <em>Link Variable Pattern</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.LinkVariablePatternImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getLinkVariablePattern()
   * @generated
   */
  int LINK_VARIABLE_PATTERN = 13;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_VARIABLE_PATTERN__OP = 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_VARIABLE_PATTERN__TYPE = 1;

  /**
   * The feature id for the '<em><b>Target</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_VARIABLE_PATTERN__TARGET = 2;

  /**
   * The number of structural features of the '<em>Link Variable Pattern</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_VARIABLE_PATTERN_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.OperatorImpl <em>Operator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.OperatorImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getOperator()
   * @generated
   */
  int OPERATOR = 14;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR__VALUE = 0;

  /**
   * The number of structural features of the '<em>Operator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.moflon.tgg.mosl.tgg.impl.ImportImpl <em>Import</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.moflon.tgg.mosl.tgg.impl.ImportImpl
   * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getImport()
   * @generated
   */
  int IMPORT = 15;

  /**
   * The feature id for the '<em><b>Imported Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__IMPORTED_NAMESPACE = 0;

  /**
   * The number of structural features of the '<em>Import</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT_FEATURE_COUNT = 1;


  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.TripleGraphGrammar <em>Triple Graph Grammar</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Triple Graph Grammar</em>'.
   * @see org.moflon.tgg.mosl.tgg.TripleGraphGrammar
   * @generated
   */
  EClass getTripleGraphGrammar();

  /**
   * Returns the meta object for the containment reference '{@link org.moflon.tgg.mosl.tgg.TripleGraphGrammar#getSchema <em>Schema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Schema</em>'.
   * @see org.moflon.tgg.mosl.tgg.TripleGraphGrammar#getSchema()
   * @see #getTripleGraphGrammar()
   * @generated
   */
  EReference getTripleGraphGrammar_Schema();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.TripleGraphGrammar#getRules <em>Rules</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Rules</em>'.
   * @see org.moflon.tgg.mosl.tgg.TripleGraphGrammar#getRules()
   * @see #getTripleGraphGrammar()
   * @generated
   */
  EReference getTripleGraphGrammar_Rules();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.SpecFile <em>Spec File</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Spec File</em>'.
   * @see org.moflon.tgg.mosl.tgg.SpecFile
   * @generated
   */
  EClass getSpecFile();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.SpecFile#getImports <em>Imports</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Imports</em>'.
   * @see org.moflon.tgg.mosl.tgg.SpecFile#getImports()
   * @see #getSpecFile()
   * @generated
   */
  EReference getSpecFile_Imports();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.SpecFile#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.moflon.tgg.mosl.tgg.SpecFile#getName()
   * @see #getSpecFile()
   * @generated
   */
  EAttribute getSpecFile_Name();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.Schema <em>Schema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Schema</em>'.
   * @see org.moflon.tgg.mosl.tgg.Schema
   * @generated
   */
  EClass getSchema();

  /**
   * Returns the meta object for the reference list '{@link org.moflon.tgg.mosl.tgg.Schema#getSourceTypes <em>Source Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Source Types</em>'.
   * @see org.moflon.tgg.mosl.tgg.Schema#getSourceTypes()
   * @see #getSchema()
   * @generated
   */
  EReference getSchema_SourceTypes();

  /**
   * Returns the meta object for the reference list '{@link org.moflon.tgg.mosl.tgg.Schema#getTargetTypes <em>Target Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Target Types</em>'.
   * @see org.moflon.tgg.mosl.tgg.Schema#getTargetTypes()
   * @see #getSchema()
   * @generated
   */
  EReference getSchema_TargetTypes();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.Schema#getCorrespondenceTypes <em>Correspondence Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Correspondence Types</em>'.
   * @see org.moflon.tgg.mosl.tgg.Schema#getCorrespondenceTypes()
   * @see #getSchema()
   * @generated
   */
  EReference getSchema_CorrespondenceTypes();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.Schema#getAttributeCondDefs <em>Attribute Cond Defs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Attribute Cond Defs</em>'.
   * @see org.moflon.tgg.mosl.tgg.Schema#getAttributeCondDefs()
   * @see #getSchema()
   * @generated
   */
  EReference getSchema_AttributeCondDefs();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.CorrType <em>Corr Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Corr Type</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrType
   * @generated
   */
  EClass getCorrType();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.CorrType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrType#getName()
   * @see #getCorrType()
   * @generated
   */
  EAttribute getCorrType_Name();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.TypeExtension <em>Type Extension</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type Extension</em>'.
   * @see org.moflon.tgg.mosl.tgg.TypeExtension
   * @generated
   */
  EClass getTypeExtension();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.TypeExtension#getSuper <em>Super</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Super</em>'.
   * @see org.moflon.tgg.mosl.tgg.TypeExtension#getSuper()
   * @see #getTypeExtension()
   * @generated
   */
  EReference getTypeExtension_Super();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.CorrTypeDef <em>Corr Type Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Corr Type Def</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrTypeDef
   * @generated
   */
  EClass getCorrTypeDef();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.CorrTypeDef#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Source</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrTypeDef#getSource()
   * @see #getCorrTypeDef()
   * @generated
   */
  EReference getCorrTypeDef_Source();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.CorrTypeDef#getTarget <em>Target</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Target</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrTypeDef#getTarget()
   * @see #getCorrTypeDef()
   * @generated
   */
  EReference getCorrTypeDef_Target();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.Rule <em>Rule</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Rule</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule
   * @generated
   */
  EClass getRule();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.Rule#isAbstract <em>Abstract</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Abstract</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule#isAbstract()
   * @see #getRule()
   * @generated
   */
  EAttribute getRule_Abstract();

  /**
   * Returns the meta object for the reference list '{@link org.moflon.tgg.mosl.tgg.Rule#getSupertypes <em>Supertypes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Supertypes</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule#getSupertypes()
   * @see #getRule()
   * @generated
   */
  EReference getRule_Supertypes();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.Rule#getSchema <em>Schema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Schema</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule#getSchema()
   * @see #getRule()
   * @generated
   */
  EReference getRule_Schema();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.Rule#getSourcePatterns <em>Source Patterns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Source Patterns</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule#getSourcePatterns()
   * @see #getRule()
   * @generated
   */
  EReference getRule_SourcePatterns();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.Rule#getTargetPatterns <em>Target Patterns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Target Patterns</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule#getTargetPatterns()
   * @see #getRule()
   * @generated
   */
  EReference getRule_TargetPatterns();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.Rule#getCorrespondencePatterns <em>Correspondence Patterns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Correspondence Patterns</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule#getCorrespondencePatterns()
   * @see #getRule()
   * @generated
   */
  EReference getRule_CorrespondencePatterns();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.Rule#getAttrConditions <em>Attr Conditions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Attr Conditions</em>'.
   * @see org.moflon.tgg.mosl.tgg.Rule#getAttrConditions()
   * @see #getRule()
   * @generated
   */
  EReference getRule_AttrConditions();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.AttrCond <em>Attr Cond</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Attr Cond</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCond
   * @generated
   */
  EClass getAttrCond();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.AttrCond#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Name</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCond#getName()
   * @see #getAttrCond()
   * @generated
   */
  EReference getAttrCond_Name();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.AttrCond#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Values</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCond#getValues()
   * @see #getAttrCond()
   * @generated
   */
  EReference getAttrCond_Values();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.AttrCondDef <em>Attr Cond Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Attr Cond Def</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCondDef
   * @generated
   */
  EClass getAttrCondDef();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCondDef#getName()
   * @see #getAttrCondDef()
   * @generated
   */
  EAttribute getAttrCondDef_Name();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCondDef#getParams()
   * @see #getAttrCondDef()
   * @generated
   */
  EReference getAttrCondDef_Params();

  /**
   * Returns the meta object for the attribute list '{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedSyncAdornments <em>Allowed Sync Adornments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Allowed Sync Adornments</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedSyncAdornments()
   * @see #getAttrCondDef()
   * @generated
   */
  EAttribute getAttrCondDef_AllowedSyncAdornments();

  /**
   * Returns the meta object for the attribute list '{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedAdornments <em>Allowed Adornments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Allowed Adornments</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedAdornments()
   * @see #getAttrCondDef()
   * @generated
   */
  EAttribute getAttrCondDef_AllowedAdornments();

  /**
   * Returns the meta object for the attribute list '{@link org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedGenAdornments <em>Allowed Gen Adornments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Allowed Gen Adornments</em>'.
   * @see org.moflon.tgg.mosl.tgg.AttrCondDef#getAllowedGenAdornments()
   * @see #getAttrCondDef()
   * @generated
   */
  EAttribute getAttrCondDef_AllowedGenAdornments();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.Param <em>Param</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Param</em>'.
   * @see org.moflon.tgg.mosl.tgg.Param
   * @generated
   */
  EClass getParam();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.Param#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.moflon.tgg.mosl.tgg.Param#getName()
   * @see #getParam()
   * @generated
   */
  EAttribute getParam_Name();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.Param#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see org.moflon.tgg.mosl.tgg.Param#getType()
   * @see #getParam()
   * @generated
   */
  EReference getParam_Type();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.ParamValue <em>Param Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Param Value</em>'.
   * @see org.moflon.tgg.mosl.tgg.ParamValue
   * @generated
   */
  EClass getParamValue();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.ParamValue#getObjectVar <em>Object Var</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Object Var</em>'.
   * @see org.moflon.tgg.mosl.tgg.ParamValue#getObjectVar()
   * @see #getParamValue()
   * @generated
   */
  EReference getParamValue_ObjectVar();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.ParamValue#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see org.moflon.tgg.mosl.tgg.ParamValue#getType()
   * @see #getParamValue()
   * @generated
   */
  EReference getParamValue_Type();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.CorrVariablePattern <em>Corr Variable Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Corr Variable Pattern</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrVariablePattern
   * @generated
   */
  EClass getCorrVariablePattern();

  /**
   * Returns the meta object for the containment reference '{@link org.moflon.tgg.mosl.tgg.CorrVariablePattern#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Op</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrVariablePattern#getOp()
   * @see #getCorrVariablePattern()
   * @generated
   */
  EReference getCorrVariablePattern_Op();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.CorrVariablePattern#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrVariablePattern#getName()
   * @see #getCorrVariablePattern()
   * @generated
   */
  EAttribute getCorrVariablePattern_Name();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.CorrVariablePattern#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrVariablePattern#getType()
   * @see #getCorrVariablePattern()
   * @generated
   */
  EReference getCorrVariablePattern_Type();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.CorrVariablePattern#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Source</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrVariablePattern#getSource()
   * @see #getCorrVariablePattern()
   * @generated
   */
  EReference getCorrVariablePattern_Source();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.CorrVariablePattern#getTarget <em>Target</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Target</em>'.
   * @see org.moflon.tgg.mosl.tgg.CorrVariablePattern#getTarget()
   * @see #getCorrVariablePattern()
   * @generated
   */
  EReference getCorrVariablePattern_Target();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.ObjectVariablePattern <em>Object Variable Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Object Variable Pattern</em>'.
   * @see org.moflon.tgg.mosl.tgg.ObjectVariablePattern
   * @generated
   */
  EClass getObjectVariablePattern();

  /**
   * Returns the meta object for the containment reference '{@link org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Op</em>'.
   * @see org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getOp()
   * @see #getObjectVariablePattern()
   * @generated
   */
  EReference getObjectVariablePattern_Op();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getName()
   * @see #getObjectVariablePattern()
   * @generated
   */
  EAttribute getObjectVariablePattern_Name();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getType()
   * @see #getObjectVariablePattern()
   * @generated
   */
  EReference getObjectVariablePattern_Type();

  /**
   * Returns the meta object for the containment reference list '{@link org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getLinkVariablePatterns <em>Link Variable Patterns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Link Variable Patterns</em>'.
   * @see org.moflon.tgg.mosl.tgg.ObjectVariablePattern#getLinkVariablePatterns()
   * @see #getObjectVariablePattern()
   * @generated
   */
  EReference getObjectVariablePattern_LinkVariablePatterns();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern <em>Link Variable Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Link Variable Pattern</em>'.
   * @see org.moflon.tgg.mosl.tgg.LinkVariablePattern
   * @generated
   */
  EClass getLinkVariablePattern();

  /**
   * Returns the meta object for the containment reference '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Op</em>'.
   * @see org.moflon.tgg.mosl.tgg.LinkVariablePattern#getOp()
   * @see #getLinkVariablePattern()
   * @generated
   */
  EReference getLinkVariablePattern_Op();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see org.moflon.tgg.mosl.tgg.LinkVariablePattern#getType()
   * @see #getLinkVariablePattern()
   * @generated
   */
  EReference getLinkVariablePattern_Type();

  /**
   * Returns the meta object for the reference '{@link org.moflon.tgg.mosl.tgg.LinkVariablePattern#getTarget <em>Target</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Target</em>'.
   * @see org.moflon.tgg.mosl.tgg.LinkVariablePattern#getTarget()
   * @see #getLinkVariablePattern()
   * @generated
   */
  EReference getLinkVariablePattern_Target();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.Operator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operator</em>'.
   * @see org.moflon.tgg.mosl.tgg.Operator
   * @generated
   */
  EClass getOperator();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.Operator#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.moflon.tgg.mosl.tgg.Operator#getValue()
   * @see #getOperator()
   * @generated
   */
  EAttribute getOperator_Value();

  /**
   * Returns the meta object for class '{@link org.moflon.tgg.mosl.tgg.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Import</em>'.
   * @see org.moflon.tgg.mosl.tgg.Import
   * @generated
   */
  EClass getImport();

  /**
   * Returns the meta object for the attribute '{@link org.moflon.tgg.mosl.tgg.Import#getImportedNamespace <em>Imported Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Imported Namespace</em>'.
   * @see org.moflon.tgg.mosl.tgg.Import#getImportedNamespace()
   * @see #getImport()
   * @generated
   */
  EAttribute getImport_ImportedNamespace();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  TggFactory getTggFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.TripleGraphGrammarImpl <em>Triple Graph Grammar</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.TripleGraphGrammarImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getTripleGraphGrammar()
     * @generated
     */
    EClass TRIPLE_GRAPH_GRAMMAR = eINSTANCE.getTripleGraphGrammar();

    /**
     * The meta object literal for the '<em><b>Schema</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRIPLE_GRAPH_GRAMMAR__SCHEMA = eINSTANCE.getTripleGraphGrammar_Schema();

    /**
     * The meta object literal for the '<em><b>Rules</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRIPLE_GRAPH_GRAMMAR__RULES = eINSTANCE.getTripleGraphGrammar_Rules();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.SpecFileImpl <em>Spec File</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.SpecFileImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getSpecFile()
     * @generated
     */
    EClass SPEC_FILE = eINSTANCE.getSpecFile();

    /**
     * The meta object literal for the '<em><b>Imports</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SPEC_FILE__IMPORTS = eINSTANCE.getSpecFile_Imports();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SPEC_FILE__NAME = eINSTANCE.getSpecFile_Name();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.SchemaImpl <em>Schema</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.SchemaImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getSchema()
     * @generated
     */
    EClass SCHEMA = eINSTANCE.getSchema();

    /**
     * The meta object literal for the '<em><b>Source Types</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA__SOURCE_TYPES = eINSTANCE.getSchema_SourceTypes();

    /**
     * The meta object literal for the '<em><b>Target Types</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA__TARGET_TYPES = eINSTANCE.getSchema_TargetTypes();

    /**
     * The meta object literal for the '<em><b>Correspondence Types</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA__CORRESPONDENCE_TYPES = eINSTANCE.getSchema_CorrespondenceTypes();

    /**
     * The meta object literal for the '<em><b>Attribute Cond Defs</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA__ATTRIBUTE_COND_DEFS = eINSTANCE.getSchema_AttributeCondDefs();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.CorrTypeImpl <em>Corr Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.CorrTypeImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getCorrType()
     * @generated
     */
    EClass CORR_TYPE = eINSTANCE.getCorrType();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CORR_TYPE__NAME = eINSTANCE.getCorrType_Name();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.TypeExtensionImpl <em>Type Extension</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.TypeExtensionImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getTypeExtension()
     * @generated
     */
    EClass TYPE_EXTENSION = eINSTANCE.getTypeExtension();

    /**
     * The meta object literal for the '<em><b>Super</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TYPE_EXTENSION__SUPER = eINSTANCE.getTypeExtension_Super();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.CorrTypeDefImpl <em>Corr Type Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.CorrTypeDefImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getCorrTypeDef()
     * @generated
     */
    EClass CORR_TYPE_DEF = eINSTANCE.getCorrTypeDef();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CORR_TYPE_DEF__SOURCE = eINSTANCE.getCorrTypeDef_Source();

    /**
     * The meta object literal for the '<em><b>Target</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CORR_TYPE_DEF__TARGET = eINSTANCE.getCorrTypeDef_Target();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.RuleImpl <em>Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.RuleImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getRule()
     * @generated
     */
    EClass RULE = eINSTANCE.getRule();

    /**
     * The meta object literal for the '<em><b>Abstract</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RULE__ABSTRACT = eINSTANCE.getRule_Abstract();

    /**
     * The meta object literal for the '<em><b>Supertypes</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__SUPERTYPES = eINSTANCE.getRule_Supertypes();

    /**
     * The meta object literal for the '<em><b>Schema</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__SCHEMA = eINSTANCE.getRule_Schema();

    /**
     * The meta object literal for the '<em><b>Source Patterns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__SOURCE_PATTERNS = eINSTANCE.getRule_SourcePatterns();

    /**
     * The meta object literal for the '<em><b>Target Patterns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__TARGET_PATTERNS = eINSTANCE.getRule_TargetPatterns();

    /**
     * The meta object literal for the '<em><b>Correspondence Patterns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__CORRESPONDENCE_PATTERNS = eINSTANCE.getRule_CorrespondencePatterns();

    /**
     * The meta object literal for the '<em><b>Attr Conditions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__ATTR_CONDITIONS = eINSTANCE.getRule_AttrConditions();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.AttrCondImpl <em>Attr Cond</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.AttrCondImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getAttrCond()
     * @generated
     */
    EClass ATTR_COND = eINSTANCE.getAttrCond();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ATTR_COND__NAME = eINSTANCE.getAttrCond_Name();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ATTR_COND__VALUES = eINSTANCE.getAttrCond_Values();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl <em>Attr Cond Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.AttrCondDefImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getAttrCondDef()
     * @generated
     */
    EClass ATTR_COND_DEF = eINSTANCE.getAttrCondDef();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTR_COND_DEF__NAME = eINSTANCE.getAttrCondDef_Name();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ATTR_COND_DEF__PARAMS = eINSTANCE.getAttrCondDef_Params();

    /**
     * The meta object literal for the '<em><b>Allowed Sync Adornments</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTR_COND_DEF__ALLOWED_SYNC_ADORNMENTS = eINSTANCE.getAttrCondDef_AllowedSyncAdornments();

    /**
     * The meta object literal for the '<em><b>Allowed Adornments</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTR_COND_DEF__ALLOWED_ADORNMENTS = eINSTANCE.getAttrCondDef_AllowedAdornments();

    /**
     * The meta object literal for the '<em><b>Allowed Gen Adornments</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTR_COND_DEF__ALLOWED_GEN_ADORNMENTS = eINSTANCE.getAttrCondDef_AllowedGenAdornments();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.ParamImpl <em>Param</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.ParamImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getParam()
     * @generated
     */
    EClass PARAM = eINSTANCE.getParam();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAM__NAME = eINSTANCE.getParam_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAM__TYPE = eINSTANCE.getParam_Type();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.ParamValueImpl <em>Param Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.ParamValueImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getParamValue()
     * @generated
     */
    EClass PARAM_VALUE = eINSTANCE.getParamValue();

    /**
     * The meta object literal for the '<em><b>Object Var</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAM_VALUE__OBJECT_VAR = eINSTANCE.getParamValue_ObjectVar();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAM_VALUE__TYPE = eINSTANCE.getParamValue_Type();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.CorrVariablePatternImpl <em>Corr Variable Pattern</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.CorrVariablePatternImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getCorrVariablePattern()
     * @generated
     */
    EClass CORR_VARIABLE_PATTERN = eINSTANCE.getCorrVariablePattern();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CORR_VARIABLE_PATTERN__OP = eINSTANCE.getCorrVariablePattern_Op();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CORR_VARIABLE_PATTERN__NAME = eINSTANCE.getCorrVariablePattern_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CORR_VARIABLE_PATTERN__TYPE = eINSTANCE.getCorrVariablePattern_Type();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CORR_VARIABLE_PATTERN__SOURCE = eINSTANCE.getCorrVariablePattern_Source();

    /**
     * The meta object literal for the '<em><b>Target</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CORR_VARIABLE_PATTERN__TARGET = eINSTANCE.getCorrVariablePattern_Target();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl <em>Object Variable Pattern</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.ObjectVariablePatternImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getObjectVariablePattern()
     * @generated
     */
    EClass OBJECT_VARIABLE_PATTERN = eINSTANCE.getObjectVariablePattern();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OBJECT_VARIABLE_PATTERN__OP = eINSTANCE.getObjectVariablePattern_Op();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OBJECT_VARIABLE_PATTERN__NAME = eINSTANCE.getObjectVariablePattern_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OBJECT_VARIABLE_PATTERN__TYPE = eINSTANCE.getObjectVariablePattern_Type();

    /**
     * The meta object literal for the '<em><b>Link Variable Patterns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OBJECT_VARIABLE_PATTERN__LINK_VARIABLE_PATTERNS = eINSTANCE.getObjectVariablePattern_LinkVariablePatterns();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.LinkVariablePatternImpl <em>Link Variable Pattern</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.LinkVariablePatternImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getLinkVariablePattern()
     * @generated
     */
    EClass LINK_VARIABLE_PATTERN = eINSTANCE.getLinkVariablePattern();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LINK_VARIABLE_PATTERN__OP = eINSTANCE.getLinkVariablePattern_Op();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LINK_VARIABLE_PATTERN__TYPE = eINSTANCE.getLinkVariablePattern_Type();

    /**
     * The meta object literal for the '<em><b>Target</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LINK_VARIABLE_PATTERN__TARGET = eINSTANCE.getLinkVariablePattern_Target();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.OperatorImpl <em>Operator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.OperatorImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getOperator()
     * @generated
     */
    EClass OPERATOR = eINSTANCE.getOperator();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OPERATOR__VALUE = eINSTANCE.getOperator_Value();

    /**
     * The meta object literal for the '{@link org.moflon.tgg.mosl.tgg.impl.ImportImpl <em>Import</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.moflon.tgg.mosl.tgg.impl.ImportImpl
     * @see org.moflon.tgg.mosl.tgg.impl.TggPackageImpl#getImport()
     * @generated
     */
    EClass IMPORT = eINSTANCE.getImport();

    /**
     * The meta object literal for the '<em><b>Imported Namespace</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPORT__IMPORTED_NAMESPACE = eINSTANCE.getImport_ImportedNamespace();

  }

} //TggPackage
