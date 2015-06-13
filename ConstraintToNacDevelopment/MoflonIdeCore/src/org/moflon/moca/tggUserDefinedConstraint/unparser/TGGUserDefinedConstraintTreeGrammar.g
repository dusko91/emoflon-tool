tree grammar TGGUserDefinedConstraintTreeGrammar;

options {
  ASTLabelType = CommonTree; 
  output       = template;
}
 
// Tokens used internally by Moca
// ID: ('a'..'z' | 'A'..'Z')+;
// STRING: ( ID | ('0'..'9'))+; 
// ATRIBUTE: Used as an imaginary token for coding attributes in XML files (ATTRIBUTE name=ID value=STRING)
tokens {
  ID;
  STRING;
  ATTRIBUTE; 
} 
  
@header {
package org.moflon.moca.tggUserDefinedConstraint.unparser;
import org.moflon.moca.MocaUtil;
import org.moflon.util.MoflonUtil;
}

@members { 

public Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)	throws RecognitionException  {
	try {
        	return super.recoverFromMismatchedToken(input, ttype, follow);
	} catch(java.util.NoSuchElementException e){
      	throw new IllegalArgumentException("Your tree does not comply with your tree grammar!\n"
			+ " Problems encountered at: [" + "..." + getTreeNodeStream().LT(-1) + " " 
            + getTreeNodeStream().LT(1) + "..." + "] in tree.");
		} 
    }

private static List<String> handleTypes(List parameters){
	ArrayList<String> filtered = new ArrayList<String>();
	
	for(Object o : parameters){
		CommonTree p = (CommonTree)o;
		
		String type = MoflonUtil.eCoreTypeToJavaType(p.getText(), true);
		
		// Handle boolean => Boolean
		if(type.equals("boolean"))
		   type = "Boolean";

		filtered.add(type);
	}
	
	return filtered;
}
}

// tree grammar rules:
main: ^(constraintName=STRING ^('ALLOWED_ADORNMENTS' adornments+=adornment*) ^('SIGNATURE' parameters+=STRING*)) 
  ->  
main(constraintName={MocaUtil.firstToUpper($constraintName.text)}, adornments={$adornments}, parameters={handleTypes($parameters)});

adornment: value=STRING 
  ->
adornmentCase(adornment={$value}); 