tree grammar TGGUserDefinedConstraintTreeGrammar;

options {
  ASTLabelType = CommonTree; 
  output       = template;
}

// Tokens used internally by Moca
// ID: ('a'..'z' | 'A'..'Z')+;
// STRING: ( ID | ('0'..'9') )+; 
// ATRIBUTE: Used as an imaginary token for coding attributes in XML files (ATTRIBUTE name=ID value=STRING)
tokens {
  ID;
  STRING;
  ATTRIBUTE; 
} 
  
@header {
package org.moflon.moca.tggUserDefinedConstraint.unparser;
import org.moflon.moca.MocaUtil;
}

@members {
private static int i = 0;
}

// tree grammar rules:
main: ^(constraintName=STRING ^('ALLOWED_ADORNMENTS' adornments+=adornment*)
      ^('SIGNATURE' parameters+=STRING*)) 
      {i++;} 
  ->  
main(constraintName={MocaUtil.firstToUpper($constraintName.text)}, adornments={$adornments}, parameters={$parameters});

adornment: value=STRING 
  -> 
adornmentCase(adornment={$value});