package org.moflon.tgg.mosl.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.moflon.tgg.mosl.services.TGGGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTGGParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'schema'", "'source'", "'{'", "'}'", "'target'", "'correspondence'", "'attribute conditions'", "'extends'", "'-src->'", "'-trg->'", "'abstract'", "'rule'", "' with '", "'('", "','", "')'", "'sync:'", "'gen:'", "'B'", "'F'", "'|'", "':'", "'.'", "' : '", "'-'", "'->'", "'++ '", "'! '", "'import'", "'.*'"
    };
    public static final int RULE_STRING=6;
    public static final int RULE_SL_COMMENT=8;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__37=37;
    public static final int T__16=16;
    public static final int T__38=38;
    public static final int T__17=17;
    public static final int T__39=39;
    public static final int T__18=18;
    public static final int T__11=11;
    public static final int T__33=33;
    public static final int T__12=12;
    public static final int T__34=34;
    public static final int T__13=13;
    public static final int T__35=35;
    public static final int T__14=14;
    public static final int T__36=36;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_ID=4;
    public static final int RULE_WS=9;
    public static final int RULE_ANY_OTHER=10;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=5;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__40=40;
    public static final int T__20=20;
    public static final int T__21=21;

    // delegates
    // delegators


        public InternalTGGParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalTGGParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalTGGParser.tokenNames; }
    public String getGrammarFileName() { return "../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g"; }



     	private TGGGrammarAccess grammarAccess;
     	
        public InternalTGGParser(TokenStream input, TGGGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "TripleGraphGrammar";	
       	}
       	
       	@Override
       	protected TGGGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleTripleGraphGrammar"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:67:1: entryRuleTripleGraphGrammar returns [EObject current=null] : iv_ruleTripleGraphGrammar= ruleTripleGraphGrammar EOF ;
    public final EObject entryRuleTripleGraphGrammar() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTripleGraphGrammar = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:68:2: (iv_ruleTripleGraphGrammar= ruleTripleGraphGrammar EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:69:2: iv_ruleTripleGraphGrammar= ruleTripleGraphGrammar EOF
            {
             newCompositeNode(grammarAccess.getTripleGraphGrammarRule()); 
            pushFollow(FOLLOW_ruleTripleGraphGrammar_in_entryRuleTripleGraphGrammar75);
            iv_ruleTripleGraphGrammar=ruleTripleGraphGrammar();

            state._fsp--;

             current =iv_ruleTripleGraphGrammar; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTripleGraphGrammar85); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTripleGraphGrammar"


    // $ANTLR start "ruleTripleGraphGrammar"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:76:1: ruleTripleGraphGrammar returns [EObject current=null] : ( ( (lv_schema_0_0= ruleSchema ) )? ( (lv_rules_1_0= ruleRule ) )? ) ;
    public final EObject ruleTripleGraphGrammar() throws RecognitionException {
        EObject current = null;

        EObject lv_schema_0_0 = null;

        EObject lv_rules_1_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:79:28: ( ( ( (lv_schema_0_0= ruleSchema ) )? ( (lv_rules_1_0= ruleRule ) )? ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:80:1: ( ( (lv_schema_0_0= ruleSchema ) )? ( (lv_rules_1_0= ruleRule ) )? )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:80:1: ( ( (lv_schema_0_0= ruleSchema ) )? ( (lv_rules_1_0= ruleRule ) )? )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:80:2: ( (lv_schema_0_0= ruleSchema ) )? ( (lv_rules_1_0= ruleRule ) )?
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:80:2: ( (lv_schema_0_0= ruleSchema ) )?
            int alt1=2;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:81:1: (lv_schema_0_0= ruleSchema )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:81:1: (lv_schema_0_0= ruleSchema )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:82:3: lv_schema_0_0= ruleSchema
                    {
                     
                    	        newCompositeNode(grammarAccess.getTripleGraphGrammarAccess().getSchemaSchemaParserRuleCall_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleSchema_in_ruleTripleGraphGrammar131);
                    lv_schema_0_0=ruleSchema();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getTripleGraphGrammarRule());
                    	        }
                           		set(
                           			current, 
                           			"schema",
                            		lv_schema_0_0, 
                            		"Schema");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:98:3: ( (lv_rules_1_0= ruleRule ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0>=21 && LA2_0<=22)||LA2_0==39) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:99:1: (lv_rules_1_0= ruleRule )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:99:1: (lv_rules_1_0= ruleRule )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:100:3: lv_rules_1_0= ruleRule
                    {
                     
                    	        newCompositeNode(grammarAccess.getTripleGraphGrammarAccess().getRulesRuleParserRuleCall_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleRule_in_ruleTripleGraphGrammar153);
                    lv_rules_1_0=ruleRule();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getTripleGraphGrammarRule());
                    	        }
                           		add(
                           			current, 
                           			"rules",
                            		lv_rules_1_0, 
                            		"Rule");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTripleGraphGrammar"


    // $ANTLR start "entryRuleSchema"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:126:1: entryRuleSchema returns [EObject current=null] : iv_ruleSchema= ruleSchema EOF ;
    public final EObject entryRuleSchema() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSchema = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:127:2: (iv_ruleSchema= ruleSchema EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:128:2: iv_ruleSchema= ruleSchema EOF
            {
             newCompositeNode(grammarAccess.getSchemaRule()); 
            pushFollow(FOLLOW_ruleSchema_in_entryRuleSchema192);
            iv_ruleSchema=ruleSchema();

            state._fsp--;

             current =iv_ruleSchema; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSchema202); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSchema"


    // $ANTLR start "ruleSchema"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:135:1: ruleSchema returns [EObject current=null] : ( ( (lv_imports_0_0= ruleImport ) )* otherlv_1= 'schema' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'source' otherlv_4= '{' ( ( ruleQualifiedName ) )* otherlv_6= '}' )? (otherlv_7= 'target' otherlv_8= '{' ( ( ruleQualifiedName ) )* otherlv_10= '}' )? (otherlv_11= 'correspondence' otherlv_12= '{' ( (lv_correspondenceTypes_13_0= ruleCorrType ) )* otherlv_14= '}' )? (otherlv_15= 'attribute conditions' otherlv_16= '{' ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )* otherlv_18= '}' )? ) ;
    public final EObject ruleSchema() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_11=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token otherlv_15=null;
        Token otherlv_16=null;
        Token otherlv_18=null;
        EObject lv_imports_0_0 = null;

        EObject lv_correspondenceTypes_13_0 = null;

        EObject lv_attributeCondDefs_17_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:138:28: ( ( ( (lv_imports_0_0= ruleImport ) )* otherlv_1= 'schema' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'source' otherlv_4= '{' ( ( ruleQualifiedName ) )* otherlv_6= '}' )? (otherlv_7= 'target' otherlv_8= '{' ( ( ruleQualifiedName ) )* otherlv_10= '}' )? (otherlv_11= 'correspondence' otherlv_12= '{' ( (lv_correspondenceTypes_13_0= ruleCorrType ) )* otherlv_14= '}' )? (otherlv_15= 'attribute conditions' otherlv_16= '{' ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )* otherlv_18= '}' )? ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:139:1: ( ( (lv_imports_0_0= ruleImport ) )* otherlv_1= 'schema' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'source' otherlv_4= '{' ( ( ruleQualifiedName ) )* otherlv_6= '}' )? (otherlv_7= 'target' otherlv_8= '{' ( ( ruleQualifiedName ) )* otherlv_10= '}' )? (otherlv_11= 'correspondence' otherlv_12= '{' ( (lv_correspondenceTypes_13_0= ruleCorrType ) )* otherlv_14= '}' )? (otherlv_15= 'attribute conditions' otherlv_16= '{' ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )* otherlv_18= '}' )? )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:139:1: ( ( (lv_imports_0_0= ruleImport ) )* otherlv_1= 'schema' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'source' otherlv_4= '{' ( ( ruleQualifiedName ) )* otherlv_6= '}' )? (otherlv_7= 'target' otherlv_8= '{' ( ( ruleQualifiedName ) )* otherlv_10= '}' )? (otherlv_11= 'correspondence' otherlv_12= '{' ( (lv_correspondenceTypes_13_0= ruleCorrType ) )* otherlv_14= '}' )? (otherlv_15= 'attribute conditions' otherlv_16= '{' ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )* otherlv_18= '}' )? )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:139:2: ( (lv_imports_0_0= ruleImport ) )* otherlv_1= 'schema' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'source' otherlv_4= '{' ( ( ruleQualifiedName ) )* otherlv_6= '}' )? (otherlv_7= 'target' otherlv_8= '{' ( ( ruleQualifiedName ) )* otherlv_10= '}' )? (otherlv_11= 'correspondence' otherlv_12= '{' ( (lv_correspondenceTypes_13_0= ruleCorrType ) )* otherlv_14= '}' )? (otherlv_15= 'attribute conditions' otherlv_16= '{' ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )* otherlv_18= '}' )?
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:139:2: ( (lv_imports_0_0= ruleImport ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==39) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:140:1: (lv_imports_0_0= ruleImport )
            	    {
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:140:1: (lv_imports_0_0= ruleImport )
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:141:3: lv_imports_0_0= ruleImport
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getSchemaAccess().getImportsImportParserRuleCall_0_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleImport_in_ruleSchema248);
            	    lv_imports_0_0=ruleImport();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getSchemaRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"imports",
            	            		lv_imports_0_0, 
            	            		"Import");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            otherlv_1=(Token)match(input,11,FOLLOW_11_in_ruleSchema261); 

                	newLeafNode(otherlv_1, grammarAccess.getSchemaAccess().getSchemaKeyword_1());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:161:1: ( (lv_name_2_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:162:1: (lv_name_2_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:162:1: (lv_name_2_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:163:3: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSchema278); 

            			newLeafNode(lv_name_2_0, grammarAccess.getSchemaAccess().getNameIDTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSchemaRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_2_0, 
                    		"ID");
            	    

            }


            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:179:2: (otherlv_3= 'source' otherlv_4= '{' ( ( ruleQualifiedName ) )* otherlv_6= '}' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==12) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:179:4: otherlv_3= 'source' otherlv_4= '{' ( ( ruleQualifiedName ) )* otherlv_6= '}'
                    {
                    otherlv_3=(Token)match(input,12,FOLLOW_12_in_ruleSchema296); 

                        	newLeafNode(otherlv_3, grammarAccess.getSchemaAccess().getSourceKeyword_3_0());
                        
                    otherlv_4=(Token)match(input,13,FOLLOW_13_in_ruleSchema308); 

                        	newLeafNode(otherlv_4, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_3_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:187:1: ( ( ruleQualifiedName ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==RULE_ID) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:188:1: ( ruleQualifiedName )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:188:1: ( ruleQualifiedName )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:189:3: ruleQualifiedName
                    	    {

                    	    			if (current==null) {
                    	    	            current = createModelElement(grammarAccess.getSchemaRule());
                    	    	        }
                    	            
                    	     
                    	    	        newCompositeNode(grammarAccess.getSchemaAccess().getSourceTypesEPackageCrossReference_3_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleQualifiedName_in_ruleSchema331);
                    	    ruleQualifiedName();

                    	    state._fsp--;

                    	     
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,14,FOLLOW_14_in_ruleSchema344); 

                        	newLeafNode(otherlv_6, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_3_3());
                        

                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:206:3: (otherlv_7= 'target' otherlv_8= '{' ( ( ruleQualifiedName ) )* otherlv_10= '}' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==15) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:206:5: otherlv_7= 'target' otherlv_8= '{' ( ( ruleQualifiedName ) )* otherlv_10= '}'
                    {
                    otherlv_7=(Token)match(input,15,FOLLOW_15_in_ruleSchema359); 

                        	newLeafNode(otherlv_7, grammarAccess.getSchemaAccess().getTargetKeyword_4_0());
                        
                    otherlv_8=(Token)match(input,13,FOLLOW_13_in_ruleSchema371); 

                        	newLeafNode(otherlv_8, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_4_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:214:1: ( ( ruleQualifiedName ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==RULE_ID) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:215:1: ( ruleQualifiedName )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:215:1: ( ruleQualifiedName )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:216:3: ruleQualifiedName
                    	    {

                    	    			if (current==null) {
                    	    	            current = createModelElement(grammarAccess.getSchemaRule());
                    	    	        }
                    	            
                    	     
                    	    	        newCompositeNode(grammarAccess.getSchemaAccess().getTargetTypesEPackageCrossReference_4_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleQualifiedName_in_ruleSchema394);
                    	    ruleQualifiedName();

                    	    state._fsp--;

                    	     
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    otherlv_10=(Token)match(input,14,FOLLOW_14_in_ruleSchema407); 

                        	newLeafNode(otherlv_10, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_4_3());
                        

                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:233:3: (otherlv_11= 'correspondence' otherlv_12= '{' ( (lv_correspondenceTypes_13_0= ruleCorrType ) )* otherlv_14= '}' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==16) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:233:5: otherlv_11= 'correspondence' otherlv_12= '{' ( (lv_correspondenceTypes_13_0= ruleCorrType ) )* otherlv_14= '}'
                    {
                    otherlv_11=(Token)match(input,16,FOLLOW_16_in_ruleSchema422); 

                        	newLeafNode(otherlv_11, grammarAccess.getSchemaAccess().getCorrespondenceKeyword_5_0());
                        
                    otherlv_12=(Token)match(input,13,FOLLOW_13_in_ruleSchema434); 

                        	newLeafNode(otherlv_12, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_5_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:241:1: ( (lv_correspondenceTypes_13_0= ruleCorrType ) )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==RULE_ID) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:242:1: (lv_correspondenceTypes_13_0= ruleCorrType )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:242:1: (lv_correspondenceTypes_13_0= ruleCorrType )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:243:3: lv_correspondenceTypes_13_0= ruleCorrType
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getSchemaAccess().getCorrespondenceTypesCorrTypeParserRuleCall_5_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleCorrType_in_ruleSchema455);
                    	    lv_correspondenceTypes_13_0=ruleCorrType();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getSchemaRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"correspondenceTypes",
                    	            		lv_correspondenceTypes_13_0, 
                    	            		"CorrType");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);

                    otherlv_14=(Token)match(input,14,FOLLOW_14_in_ruleSchema468); 

                        	newLeafNode(otherlv_14, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_5_3());
                        

                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:263:3: (otherlv_15= 'attribute conditions' otherlv_16= '{' ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )* otherlv_18= '}' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==17) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:263:5: otherlv_15= 'attribute conditions' otherlv_16= '{' ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )* otherlv_18= '}'
                    {
                    otherlv_15=(Token)match(input,17,FOLLOW_17_in_ruleSchema483); 

                        	newLeafNode(otherlv_15, grammarAccess.getSchemaAccess().getAttributeConditionsKeyword_6_0());
                        
                    otherlv_16=(Token)match(input,13,FOLLOW_13_in_ruleSchema495); 

                        	newLeafNode(otherlv_16, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_6_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:271:1: ( (lv_attributeCondDefs_17_0= ruleAttrCondDef ) )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==RULE_ID) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:272:1: (lv_attributeCondDefs_17_0= ruleAttrCondDef )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:272:1: (lv_attributeCondDefs_17_0= ruleAttrCondDef )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:273:3: lv_attributeCondDefs_17_0= ruleAttrCondDef
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getSchemaAccess().getAttributeCondDefsAttrCondDefParserRuleCall_6_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleAttrCondDef_in_ruleSchema516);
                    	    lv_attributeCondDefs_17_0=ruleAttrCondDef();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getSchemaRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"attributeCondDefs",
                    	            		lv_attributeCondDefs_17_0, 
                    	            		"AttrCondDef");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    otherlv_18=(Token)match(input,14,FOLLOW_14_in_ruleSchema529); 

                        	newLeafNode(otherlv_18, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_6_3());
                        

                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSchema"


    // $ANTLR start "entryRuleCorrType"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:301:1: entryRuleCorrType returns [EObject current=null] : iv_ruleCorrType= ruleCorrType EOF ;
    public final EObject entryRuleCorrType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCorrType = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:302:2: (iv_ruleCorrType= ruleCorrType EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:303:2: iv_ruleCorrType= ruleCorrType EOF
            {
             newCompositeNode(grammarAccess.getCorrTypeRule()); 
            pushFollow(FOLLOW_ruleCorrType_in_entryRuleCorrType567);
            iv_ruleCorrType=ruleCorrType();

            state._fsp--;

             current =iv_ruleCorrType; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleCorrType577); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCorrType"


    // $ANTLR start "ruleCorrType"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:310:1: ruleCorrType returns [EObject current=null] : (this_CorrTypeDef_0= ruleCorrTypeDef | this_TypeExtension_1= ruleTypeExtension ) ;
    public final EObject ruleCorrType() throws RecognitionException {
        EObject current = null;

        EObject this_CorrTypeDef_0 = null;

        EObject this_TypeExtension_1 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:313:28: ( (this_CorrTypeDef_0= ruleCorrTypeDef | this_TypeExtension_1= ruleTypeExtension ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:314:1: (this_CorrTypeDef_0= ruleCorrTypeDef | this_TypeExtension_1= ruleTypeExtension )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:314:1: (this_CorrTypeDef_0= ruleCorrTypeDef | this_TypeExtension_1= ruleTypeExtension )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==RULE_ID) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==18) ) {
                    alt12=2;
                }
                else if ( (LA12_1==13) ) {
                    alt12=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:315:5: this_CorrTypeDef_0= ruleCorrTypeDef
                    {
                     
                            newCompositeNode(grammarAccess.getCorrTypeAccess().getCorrTypeDefParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleCorrTypeDef_in_ruleCorrType624);
                    this_CorrTypeDef_0=ruleCorrTypeDef();

                    state._fsp--;

                     
                            current = this_CorrTypeDef_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:325:5: this_TypeExtension_1= ruleTypeExtension
                    {
                     
                            newCompositeNode(grammarAccess.getCorrTypeAccess().getTypeExtensionParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleTypeExtension_in_ruleCorrType651);
                    this_TypeExtension_1=ruleTypeExtension();

                    state._fsp--;

                     
                            current = this_TypeExtension_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCorrType"


    // $ANTLR start "entryRuleTypeExtension"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:341:1: entryRuleTypeExtension returns [EObject current=null] : iv_ruleTypeExtension= ruleTypeExtension EOF ;
    public final EObject entryRuleTypeExtension() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTypeExtension = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:342:2: (iv_ruleTypeExtension= ruleTypeExtension EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:343:2: iv_ruleTypeExtension= ruleTypeExtension EOF
            {
             newCompositeNode(grammarAccess.getTypeExtensionRule()); 
            pushFollow(FOLLOW_ruleTypeExtension_in_entryRuleTypeExtension686);
            iv_ruleTypeExtension=ruleTypeExtension();

            state._fsp--;

             current =iv_ruleTypeExtension; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTypeExtension696); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTypeExtension"


    // $ANTLR start "ruleTypeExtension"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:350:1: ruleTypeExtension returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= 'extends' ( (otherlv_2= RULE_ID ) ) ) ;
    public final EObject ruleTypeExtension() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;

         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:353:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= 'extends' ( (otherlv_2= RULE_ID ) ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:354:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= 'extends' ( (otherlv_2= RULE_ID ) ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:354:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= 'extends' ( (otherlv_2= RULE_ID ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:354:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= 'extends' ( (otherlv_2= RULE_ID ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:354:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:355:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:355:1: (lv_name_0_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:356:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTypeExtension738); 

            			newLeafNode(lv_name_0_0, grammarAccess.getTypeExtensionAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getTypeExtensionRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            otherlv_1=(Token)match(input,18,FOLLOW_18_in_ruleTypeExtension755); 

                	newLeafNode(otherlv_1, grammarAccess.getTypeExtensionAccess().getExtendsKeyword_1());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:376:1: ( (otherlv_2= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:377:1: (otherlv_2= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:377:1: (otherlv_2= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:378:3: otherlv_2= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getTypeExtensionRule());
            	        }
                    
            otherlv_2=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTypeExtension775); 

            		newLeafNode(otherlv_2, grammarAccess.getTypeExtensionAccess().getSuperCorrTypeCrossReference_2_0()); 
            	

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTypeExtension"


    // $ANTLR start "entryRuleCorrTypeDef"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:397:1: entryRuleCorrTypeDef returns [EObject current=null] : iv_ruleCorrTypeDef= ruleCorrTypeDef EOF ;
    public final EObject entryRuleCorrTypeDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCorrTypeDef = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:398:2: (iv_ruleCorrTypeDef= ruleCorrTypeDef EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:399:2: iv_ruleCorrTypeDef= ruleCorrTypeDef EOF
            {
             newCompositeNode(grammarAccess.getCorrTypeDefRule()); 
            pushFollow(FOLLOW_ruleCorrTypeDef_in_entryRuleCorrTypeDef811);
            iv_ruleCorrTypeDef=ruleCorrTypeDef();

            state._fsp--;

             current =iv_ruleCorrTypeDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleCorrTypeDef821); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCorrTypeDef"


    // $ANTLR start "ruleCorrTypeDef"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:406:1: ruleCorrTypeDef returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' otherlv_2= '-src->' ( ( ruleQualifiedName ) ) otherlv_4= '-trg->' ( ( ruleQualifiedName ) ) otherlv_6= '}' ) ;
    public final EObject ruleCorrTypeDef() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;

         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:409:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' otherlv_2= '-src->' ( ( ruleQualifiedName ) ) otherlv_4= '-trg->' ( ( ruleQualifiedName ) ) otherlv_6= '}' ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:410:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' otherlv_2= '-src->' ( ( ruleQualifiedName ) ) otherlv_4= '-trg->' ( ( ruleQualifiedName ) ) otherlv_6= '}' )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:410:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' otherlv_2= '-src->' ( ( ruleQualifiedName ) ) otherlv_4= '-trg->' ( ( ruleQualifiedName ) ) otherlv_6= '}' )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:410:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' otherlv_2= '-src->' ( ( ruleQualifiedName ) ) otherlv_4= '-trg->' ( ( ruleQualifiedName ) ) otherlv_6= '}'
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:410:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:411:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:411:1: (lv_name_0_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:412:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleCorrTypeDef863); 

            			newLeafNode(lv_name_0_0, grammarAccess.getCorrTypeDefAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getCorrTypeDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            otherlv_1=(Token)match(input,13,FOLLOW_13_in_ruleCorrTypeDef880); 

                	newLeafNode(otherlv_1, grammarAccess.getCorrTypeDefAccess().getLeftCurlyBracketKeyword_1());
                
            otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleCorrTypeDef892); 

                	newLeafNode(otherlv_2, grammarAccess.getCorrTypeDefAccess().getSrcKeyword_2());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:436:1: ( ( ruleQualifiedName ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:437:1: ( ruleQualifiedName )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:437:1: ( ruleQualifiedName )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:438:3: ruleQualifiedName
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getCorrTypeDefRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getCorrTypeDefAccess().getSourceEClassCrossReference_3_0()); 
            	    
            pushFollow(FOLLOW_ruleQualifiedName_in_ruleCorrTypeDef915);
            ruleQualifiedName();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_4=(Token)match(input,20,FOLLOW_20_in_ruleCorrTypeDef927); 

                	newLeafNode(otherlv_4, grammarAccess.getCorrTypeDefAccess().getTrgKeyword_4());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:455:1: ( ( ruleQualifiedName ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:456:1: ( ruleQualifiedName )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:456:1: ( ruleQualifiedName )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:457:3: ruleQualifiedName
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getCorrTypeDefRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getCorrTypeDefAccess().getTargetEClassCrossReference_5_0()); 
            	    
            pushFollow(FOLLOW_ruleQualifiedName_in_ruleCorrTypeDef950);
            ruleQualifiedName();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_6=(Token)match(input,14,FOLLOW_14_in_ruleCorrTypeDef962); 

                	newLeafNode(otherlv_6, grammarAccess.getCorrTypeDefAccess().getRightCurlyBracketKeyword_6());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCorrTypeDef"


    // $ANTLR start "entryRuleRule"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:482:1: entryRuleRule returns [EObject current=null] : iv_ruleRule= ruleRule EOF ;
    public final EObject entryRuleRule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRule = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:483:2: (iv_ruleRule= ruleRule EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:484:2: iv_ruleRule= ruleRule EOF
            {
             newCompositeNode(grammarAccess.getRuleRule()); 
            pushFollow(FOLLOW_ruleRule_in_entryRuleRule998);
            iv_ruleRule=ruleRule();

            state._fsp--;

             current =iv_ruleRule; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleRule1008); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRule"


    // $ANTLR start "ruleRule"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:491:1: ruleRule returns [EObject current=null] : ( ( (lv_imports_0_0= ruleImport ) )* ( (lv_abstract_1_0= 'abstract' ) )? otherlv_2= 'rule' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'extends' ( (otherlv_5= RULE_ID ) ) )? otherlv_6= ' with ' ( ( ruleQualifiedName ) ) (otherlv_8= 'source' otherlv_9= '{' ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )* otherlv_11= '}' )? (otherlv_12= 'target' otherlv_13= '{' ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )* otherlv_15= '}' )? (otherlv_16= 'correspondence' otherlv_17= '{' ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )* otherlv_19= '}' )? (otherlv_20= 'attribute conditions' otherlv_21= '{' ( (lv_attrConditions_22_0= ruleAttrCond ) )* otherlv_23= '}' )? ) ;
    public final EObject ruleRule() throws RecognitionException {
        EObject current = null;

        Token lv_abstract_1_0=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_12=null;
        Token otherlv_13=null;
        Token otherlv_15=null;
        Token otherlv_16=null;
        Token otherlv_17=null;
        Token otherlv_19=null;
        Token otherlv_20=null;
        Token otherlv_21=null;
        Token otherlv_23=null;
        EObject lv_imports_0_0 = null;

        EObject lv_sourcePatterns_10_0 = null;

        EObject lv_targetPatterns_14_0 = null;

        EObject lv_correspondencePatterns_18_0 = null;

        EObject lv_attrConditions_22_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:494:28: ( ( ( (lv_imports_0_0= ruleImport ) )* ( (lv_abstract_1_0= 'abstract' ) )? otherlv_2= 'rule' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'extends' ( (otherlv_5= RULE_ID ) ) )? otherlv_6= ' with ' ( ( ruleQualifiedName ) ) (otherlv_8= 'source' otherlv_9= '{' ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )* otherlv_11= '}' )? (otherlv_12= 'target' otherlv_13= '{' ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )* otherlv_15= '}' )? (otherlv_16= 'correspondence' otherlv_17= '{' ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )* otherlv_19= '}' )? (otherlv_20= 'attribute conditions' otherlv_21= '{' ( (lv_attrConditions_22_0= ruleAttrCond ) )* otherlv_23= '}' )? ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:495:1: ( ( (lv_imports_0_0= ruleImport ) )* ( (lv_abstract_1_0= 'abstract' ) )? otherlv_2= 'rule' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'extends' ( (otherlv_5= RULE_ID ) ) )? otherlv_6= ' with ' ( ( ruleQualifiedName ) ) (otherlv_8= 'source' otherlv_9= '{' ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )* otherlv_11= '}' )? (otherlv_12= 'target' otherlv_13= '{' ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )* otherlv_15= '}' )? (otherlv_16= 'correspondence' otherlv_17= '{' ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )* otherlv_19= '}' )? (otherlv_20= 'attribute conditions' otherlv_21= '{' ( (lv_attrConditions_22_0= ruleAttrCond ) )* otherlv_23= '}' )? )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:495:1: ( ( (lv_imports_0_0= ruleImport ) )* ( (lv_abstract_1_0= 'abstract' ) )? otherlv_2= 'rule' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'extends' ( (otherlv_5= RULE_ID ) ) )? otherlv_6= ' with ' ( ( ruleQualifiedName ) ) (otherlv_8= 'source' otherlv_9= '{' ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )* otherlv_11= '}' )? (otherlv_12= 'target' otherlv_13= '{' ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )* otherlv_15= '}' )? (otherlv_16= 'correspondence' otherlv_17= '{' ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )* otherlv_19= '}' )? (otherlv_20= 'attribute conditions' otherlv_21= '{' ( (lv_attrConditions_22_0= ruleAttrCond ) )* otherlv_23= '}' )? )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:495:2: ( (lv_imports_0_0= ruleImport ) )* ( (lv_abstract_1_0= 'abstract' ) )? otherlv_2= 'rule' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'extends' ( (otherlv_5= RULE_ID ) ) )? otherlv_6= ' with ' ( ( ruleQualifiedName ) ) (otherlv_8= 'source' otherlv_9= '{' ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )* otherlv_11= '}' )? (otherlv_12= 'target' otherlv_13= '{' ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )* otherlv_15= '}' )? (otherlv_16= 'correspondence' otherlv_17= '{' ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )* otherlv_19= '}' )? (otherlv_20= 'attribute conditions' otherlv_21= '{' ( (lv_attrConditions_22_0= ruleAttrCond ) )* otherlv_23= '}' )?
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:495:2: ( (lv_imports_0_0= ruleImport ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==39) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:496:1: (lv_imports_0_0= ruleImport )
            	    {
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:496:1: (lv_imports_0_0= ruleImport )
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:497:3: lv_imports_0_0= ruleImport
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getRuleAccess().getImportsImportParserRuleCall_0_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleImport_in_ruleRule1054);
            	    lv_imports_0_0=ruleImport();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getRuleRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"imports",
            	            		lv_imports_0_0, 
            	            		"Import");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:513:3: ( (lv_abstract_1_0= 'abstract' ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==21) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:514:1: (lv_abstract_1_0= 'abstract' )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:514:1: (lv_abstract_1_0= 'abstract' )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:515:3: lv_abstract_1_0= 'abstract'
                    {
                    lv_abstract_1_0=(Token)match(input,21,FOLLOW_21_in_ruleRule1073); 

                            newLeafNode(lv_abstract_1_0, grammarAccess.getRuleAccess().getAbstractAbstractKeyword_1_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getRuleRule());
                    	        }
                           		setWithLastConsumed(current, "abstract", true, "abstract");
                    	    

                    }


                    }
                    break;

            }

            otherlv_2=(Token)match(input,22,FOLLOW_22_in_ruleRule1099); 

                	newLeafNode(otherlv_2, grammarAccess.getRuleAccess().getRuleKeyword_2());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:532:1: ( (lv_name_3_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:533:1: (lv_name_3_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:533:1: (lv_name_3_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:534:3: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleRule1116); 

            			newLeafNode(lv_name_3_0, grammarAccess.getRuleAccess().getNameIDTerminalRuleCall_3_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getRuleRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_3_0, 
                    		"ID");
            	    

            }


            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:550:2: (otherlv_4= 'extends' ( (otherlv_5= RULE_ID ) ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==18) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:550:4: otherlv_4= 'extends' ( (otherlv_5= RULE_ID ) )
                    {
                    otherlv_4=(Token)match(input,18,FOLLOW_18_in_ruleRule1134); 

                        	newLeafNode(otherlv_4, grammarAccess.getRuleAccess().getExtendsKeyword_4_0());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:554:1: ( (otherlv_5= RULE_ID ) )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:555:1: (otherlv_5= RULE_ID )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:555:1: (otherlv_5= RULE_ID )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:556:3: otherlv_5= RULE_ID
                    {

                    			if (current==null) {
                    	            current = createModelElement(grammarAccess.getRuleRule());
                    	        }
                            
                    otherlv_5=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleRule1154); 

                    		newLeafNode(otherlv_5, grammarAccess.getRuleAccess().getSupertypesRuleCrossReference_4_1_0()); 
                    	

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,23,FOLLOW_23_in_ruleRule1168); 

                	newLeafNode(otherlv_6, grammarAccess.getRuleAccess().getWithKeyword_5());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:571:1: ( ( ruleQualifiedName ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:572:1: ( ruleQualifiedName )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:572:1: ( ruleQualifiedName )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:573:3: ruleQualifiedName
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getRuleRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getRuleAccess().getSchemaSchemaCrossReference_6_0()); 
            	    
            pushFollow(FOLLOW_ruleQualifiedName_in_ruleRule1191);
            ruleQualifiedName();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:586:2: (otherlv_8= 'source' otherlv_9= '{' ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )* otherlv_11= '}' )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==12) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:586:4: otherlv_8= 'source' otherlv_9= '{' ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )* otherlv_11= '}'
                    {
                    otherlv_8=(Token)match(input,12,FOLLOW_12_in_ruleRule1204); 

                        	newLeafNode(otherlv_8, grammarAccess.getRuleAccess().getSourceKeyword_7_0());
                        
                    otherlv_9=(Token)match(input,13,FOLLOW_13_in_ruleRule1216); 

                        	newLeafNode(otherlv_9, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_7_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:594:1: ( (lv_sourcePatterns_10_0= ruleObjectVariablePattern ) )*
                    loop16:
                    do {
                        int alt16=2;
                        int LA16_0 = input.LA(1);

                        if ( (LA16_0==RULE_ID||(LA16_0>=37 && LA16_0<=38)) ) {
                            alt16=1;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:595:1: (lv_sourcePatterns_10_0= ruleObjectVariablePattern )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:595:1: (lv_sourcePatterns_10_0= ruleObjectVariablePattern )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:596:3: lv_sourcePatterns_10_0= ruleObjectVariablePattern
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getRuleAccess().getSourcePatternsObjectVariablePatternParserRuleCall_7_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleObjectVariablePattern_in_ruleRule1237);
                    	    lv_sourcePatterns_10_0=ruleObjectVariablePattern();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getRuleRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"sourcePatterns",
                    	            		lv_sourcePatterns_10_0, 
                    	            		"ObjectVariablePattern");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop16;
                        }
                    } while (true);

                    otherlv_11=(Token)match(input,14,FOLLOW_14_in_ruleRule1250); 

                        	newLeafNode(otherlv_11, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_7_3());
                        

                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:616:3: (otherlv_12= 'target' otherlv_13= '{' ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )* otherlv_15= '}' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==15) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:616:5: otherlv_12= 'target' otherlv_13= '{' ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )* otherlv_15= '}'
                    {
                    otherlv_12=(Token)match(input,15,FOLLOW_15_in_ruleRule1265); 

                        	newLeafNode(otherlv_12, grammarAccess.getRuleAccess().getTargetKeyword_8_0());
                        
                    otherlv_13=(Token)match(input,13,FOLLOW_13_in_ruleRule1277); 

                        	newLeafNode(otherlv_13, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_8_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:624:1: ( (lv_targetPatterns_14_0= ruleObjectVariablePattern ) )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==RULE_ID||(LA18_0>=37 && LA18_0<=38)) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:625:1: (lv_targetPatterns_14_0= ruleObjectVariablePattern )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:625:1: (lv_targetPatterns_14_0= ruleObjectVariablePattern )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:626:3: lv_targetPatterns_14_0= ruleObjectVariablePattern
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getRuleAccess().getTargetPatternsObjectVariablePatternParserRuleCall_8_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleObjectVariablePattern_in_ruleRule1298);
                    	    lv_targetPatterns_14_0=ruleObjectVariablePattern();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getRuleRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"targetPatterns",
                    	            		lv_targetPatterns_14_0, 
                    	            		"ObjectVariablePattern");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);

                    otherlv_15=(Token)match(input,14,FOLLOW_14_in_ruleRule1311); 

                        	newLeafNode(otherlv_15, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_8_3());
                        

                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:646:3: (otherlv_16= 'correspondence' otherlv_17= '{' ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )* otherlv_19= '}' )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==16) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:646:5: otherlv_16= 'correspondence' otherlv_17= '{' ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )* otherlv_19= '}'
                    {
                    otherlv_16=(Token)match(input,16,FOLLOW_16_in_ruleRule1326); 

                        	newLeafNode(otherlv_16, grammarAccess.getRuleAccess().getCorrespondenceKeyword_9_0());
                        
                    otherlv_17=(Token)match(input,13,FOLLOW_13_in_ruleRule1338); 

                        	newLeafNode(otherlv_17, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_9_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:654:1: ( (lv_correspondencePatterns_18_0= ruleCorrVariablePattern ) )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==RULE_ID||(LA20_0>=37 && LA20_0<=38)) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:655:1: (lv_correspondencePatterns_18_0= ruleCorrVariablePattern )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:655:1: (lv_correspondencePatterns_18_0= ruleCorrVariablePattern )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:656:3: lv_correspondencePatterns_18_0= ruleCorrVariablePattern
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getRuleAccess().getCorrespondencePatternsCorrVariablePatternParserRuleCall_9_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleCorrVariablePattern_in_ruleRule1359);
                    	    lv_correspondencePatterns_18_0=ruleCorrVariablePattern();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getRuleRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"correspondencePatterns",
                    	            		lv_correspondencePatterns_18_0, 
                    	            		"CorrVariablePattern");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);

                    otherlv_19=(Token)match(input,14,FOLLOW_14_in_ruleRule1372); 

                        	newLeafNode(otherlv_19, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_9_3());
                        

                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:676:3: (otherlv_20= 'attribute conditions' otherlv_21= '{' ( (lv_attrConditions_22_0= ruleAttrCond ) )* otherlv_23= '}' )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==17) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:676:5: otherlv_20= 'attribute conditions' otherlv_21= '{' ( (lv_attrConditions_22_0= ruleAttrCond ) )* otherlv_23= '}'
                    {
                    otherlv_20=(Token)match(input,17,FOLLOW_17_in_ruleRule1387); 

                        	newLeafNode(otherlv_20, grammarAccess.getRuleAccess().getAttributeConditionsKeyword_10_0());
                        
                    otherlv_21=(Token)match(input,13,FOLLOW_13_in_ruleRule1399); 

                        	newLeafNode(otherlv_21, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_10_1());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:684:1: ( (lv_attrConditions_22_0= ruleAttrCond ) )*
                    loop22:
                    do {
                        int alt22=2;
                        int LA22_0 = input.LA(1);

                        if ( (LA22_0==RULE_ID) ) {
                            alt22=1;
                        }


                        switch (alt22) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:685:1: (lv_attrConditions_22_0= ruleAttrCond )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:685:1: (lv_attrConditions_22_0= ruleAttrCond )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:686:3: lv_attrConditions_22_0= ruleAttrCond
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getRuleAccess().getAttrConditionsAttrCondParserRuleCall_10_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleAttrCond_in_ruleRule1420);
                    	    lv_attrConditions_22_0=ruleAttrCond();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getRuleRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"attrConditions",
                    	            		lv_attrConditions_22_0, 
                    	            		"AttrCond");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop22;
                        }
                    } while (true);

                    otherlv_23=(Token)match(input,14,FOLLOW_14_in_ruleRule1433); 

                        	newLeafNode(otherlv_23, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_10_3());
                        

                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRule"


    // $ANTLR start "entryRuleAttrCond"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:714:1: entryRuleAttrCond returns [EObject current=null] : iv_ruleAttrCond= ruleAttrCond EOF ;
    public final EObject entryRuleAttrCond() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttrCond = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:715:2: (iv_ruleAttrCond= ruleAttrCond EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:716:2: iv_ruleAttrCond= ruleAttrCond EOF
            {
             newCompositeNode(grammarAccess.getAttrCondRule()); 
            pushFollow(FOLLOW_ruleAttrCond_in_entryRuleAttrCond1471);
            iv_ruleAttrCond=ruleAttrCond();

            state._fsp--;

             current =iv_ruleAttrCond; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAttrCond1481); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttrCond"


    // $ANTLR start "ruleAttrCond"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:723:1: ruleAttrCond returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_values_2_0= ruleParamValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )* )? otherlv_5= ')' ) ;
    public final EObject ruleAttrCond() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_values_2_0 = null;

        EObject lv_values_4_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:726:28: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_values_2_0= ruleParamValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )* )? otherlv_5= ')' ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:727:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_values_2_0= ruleParamValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )* )? otherlv_5= ')' )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:727:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_values_2_0= ruleParamValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )* )? otherlv_5= ')' )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:727:2: ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_values_2_0= ruleParamValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )* )? otherlv_5= ')'
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:727:2: ( (otherlv_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:728:1: (otherlv_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:728:1: (otherlv_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:729:3: otherlv_0= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getAttrCondRule());
            	        }
                    
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAttrCond1526); 

            		newLeafNode(otherlv_0, grammarAccess.getAttrCondAccess().getNameAttrCondDefCrossReference_0_0()); 
            	

            }


            }

            otherlv_1=(Token)match(input,24,FOLLOW_24_in_ruleAttrCond1538); 

                	newLeafNode(otherlv_1, grammarAccess.getAttrCondAccess().getLeftParenthesisKeyword_1());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:744:1: ( ( (lv_values_2_0= ruleParamValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )* )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==RULE_ID) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:744:2: ( (lv_values_2_0= ruleParamValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )*
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:744:2: ( (lv_values_2_0= ruleParamValue ) )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:745:1: (lv_values_2_0= ruleParamValue )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:745:1: (lv_values_2_0= ruleParamValue )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:746:3: lv_values_2_0= ruleParamValue
                    {
                     
                    	        newCompositeNode(grammarAccess.getAttrCondAccess().getValuesParamValueParserRuleCall_2_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleParamValue_in_ruleAttrCond1560);
                    lv_values_2_0=ruleParamValue();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getAttrCondRule());
                    	        }
                           		add(
                           			current, 
                           			"values",
                            		lv_values_2_0, 
                            		"ParamValue");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:762:2: (otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) ) )*
                    loop24:
                    do {
                        int alt24=2;
                        int LA24_0 = input.LA(1);

                        if ( (LA24_0==25) ) {
                            alt24=1;
                        }


                        switch (alt24) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:762:4: otherlv_3= ',' ( (lv_values_4_0= ruleParamValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,25,FOLLOW_25_in_ruleAttrCond1573); 

                    	        	newLeafNode(otherlv_3, grammarAccess.getAttrCondAccess().getCommaKeyword_2_1_0());
                    	        
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:766:1: ( (lv_values_4_0= ruleParamValue ) )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:767:1: (lv_values_4_0= ruleParamValue )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:767:1: (lv_values_4_0= ruleParamValue )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:768:3: lv_values_4_0= ruleParamValue
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getAttrCondAccess().getValuesParamValueParserRuleCall_2_1_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleParamValue_in_ruleAttrCond1594);
                    	    lv_values_4_0=ruleParamValue();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getAttrCondRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"values",
                    	            		lv_values_4_0, 
                    	            		"ParamValue");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop24;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,26,FOLLOW_26_in_ruleAttrCond1610); 

                	newLeafNode(otherlv_5, grammarAccess.getAttrCondAccess().getRightParenthesisKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttrCond"


    // $ANTLR start "entryRuleAttrCondDef"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:796:1: entryRuleAttrCondDef returns [EObject current=null] : iv_ruleAttrCondDef= ruleAttrCondDef EOF ;
    public final EObject entryRuleAttrCondDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttrCondDef = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:797:2: (iv_ruleAttrCondDef= ruleAttrCondDef EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:798:2: iv_ruleAttrCondDef= ruleAttrCondDef EOF
            {
             newCompositeNode(grammarAccess.getAttrCondDefRule()); 
            pushFollow(FOLLOW_ruleAttrCondDef_in_entryRuleAttrCondDef1646);
            iv_ruleAttrCondDef=ruleAttrCondDef();

            state._fsp--;

             current =iv_ruleAttrCondDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAttrCondDef1656); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttrCondDef"


    // $ANTLR start "ruleAttrCondDef"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:805:1: ruleAttrCondDef returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_params_2_0= ruleParam ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )* )? otherlv_5= ')' otherlv_6= '{' otherlv_7= 'sync:' ( (lv_allowedSyncAdornments_8_0= ruleAdornment ) ) (otherlv_9= ',' ( (lv_allowedAdornments_10_0= ruleAdornment ) ) )* otherlv_11= 'gen:' ( ( (lv_allowedGenAdornments_12_0= ruleAdornment ) ) (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )* )? otherlv_15= '}' ) ;
    public final EObject ruleAttrCondDef() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_13=null;
        Token otherlv_15=null;
        EObject lv_params_2_0 = null;

        EObject lv_params_4_0 = null;

        AntlrDatatypeRuleToken lv_allowedSyncAdornments_8_0 = null;

        AntlrDatatypeRuleToken lv_allowedAdornments_10_0 = null;

        AntlrDatatypeRuleToken lv_allowedGenAdornments_12_0 = null;

        AntlrDatatypeRuleToken lv_allowedAdornments_14_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:808:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_params_2_0= ruleParam ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )* )? otherlv_5= ')' otherlv_6= '{' otherlv_7= 'sync:' ( (lv_allowedSyncAdornments_8_0= ruleAdornment ) ) (otherlv_9= ',' ( (lv_allowedAdornments_10_0= ruleAdornment ) ) )* otherlv_11= 'gen:' ( ( (lv_allowedGenAdornments_12_0= ruleAdornment ) ) (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )* )? otherlv_15= '}' ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:809:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_params_2_0= ruleParam ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )* )? otherlv_5= ')' otherlv_6= '{' otherlv_7= 'sync:' ( (lv_allowedSyncAdornments_8_0= ruleAdornment ) ) (otherlv_9= ',' ( (lv_allowedAdornments_10_0= ruleAdornment ) ) )* otherlv_11= 'gen:' ( ( (lv_allowedGenAdornments_12_0= ruleAdornment ) ) (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )* )? otherlv_15= '}' )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:809:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_params_2_0= ruleParam ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )* )? otherlv_5= ')' otherlv_6= '{' otherlv_7= 'sync:' ( (lv_allowedSyncAdornments_8_0= ruleAdornment ) ) (otherlv_9= ',' ( (lv_allowedAdornments_10_0= ruleAdornment ) ) )* otherlv_11= 'gen:' ( ( (lv_allowedGenAdornments_12_0= ruleAdornment ) ) (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )* )? otherlv_15= '}' )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:809:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_params_2_0= ruleParam ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )* )? otherlv_5= ')' otherlv_6= '{' otherlv_7= 'sync:' ( (lv_allowedSyncAdornments_8_0= ruleAdornment ) ) (otherlv_9= ',' ( (lv_allowedAdornments_10_0= ruleAdornment ) ) )* otherlv_11= 'gen:' ( ( (lv_allowedGenAdornments_12_0= ruleAdornment ) ) (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )* )? otherlv_15= '}'
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:809:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:810:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:810:1: (lv_name_0_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:811:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAttrCondDef1698); 

            			newLeafNode(lv_name_0_0, grammarAccess.getAttrCondDefAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAttrCondDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            otherlv_1=(Token)match(input,24,FOLLOW_24_in_ruleAttrCondDef1715); 

                	newLeafNode(otherlv_1, grammarAccess.getAttrCondDefAccess().getLeftParenthesisKeyword_1());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:831:1: ( ( (lv_params_2_0= ruleParam ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )* )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==RULE_ID) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:831:2: ( (lv_params_2_0= ruleParam ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )*
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:831:2: ( (lv_params_2_0= ruleParam ) )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:832:1: (lv_params_2_0= ruleParam )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:832:1: (lv_params_2_0= ruleParam )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:833:3: lv_params_2_0= ruleParam
                    {
                     
                    	        newCompositeNode(grammarAccess.getAttrCondDefAccess().getParamsParamParserRuleCall_2_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleParam_in_ruleAttrCondDef1737);
                    lv_params_2_0=ruleParam();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getAttrCondDefRule());
                    	        }
                           		add(
                           			current, 
                           			"params",
                            		lv_params_2_0, 
                            		"Param");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:849:2: (otherlv_3= ',' ( (lv_params_4_0= ruleParam ) ) )*
                    loop26:
                    do {
                        int alt26=2;
                        int LA26_0 = input.LA(1);

                        if ( (LA26_0==25) ) {
                            alt26=1;
                        }


                        switch (alt26) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:849:4: otherlv_3= ',' ( (lv_params_4_0= ruleParam ) )
                    	    {
                    	    otherlv_3=(Token)match(input,25,FOLLOW_25_in_ruleAttrCondDef1750); 

                    	        	newLeafNode(otherlv_3, grammarAccess.getAttrCondDefAccess().getCommaKeyword_2_1_0());
                    	        
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:853:1: ( (lv_params_4_0= ruleParam ) )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:854:1: (lv_params_4_0= ruleParam )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:854:1: (lv_params_4_0= ruleParam )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:855:3: lv_params_4_0= ruleParam
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getAttrCondDefAccess().getParamsParamParserRuleCall_2_1_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleParam_in_ruleAttrCondDef1771);
                    	    lv_params_4_0=ruleParam();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getAttrCondDefRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"params",
                    	            		lv_params_4_0, 
                    	            		"Param");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop26;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,26,FOLLOW_26_in_ruleAttrCondDef1787); 

                	newLeafNode(otherlv_5, grammarAccess.getAttrCondDefAccess().getRightParenthesisKeyword_3());
                
            otherlv_6=(Token)match(input,13,FOLLOW_13_in_ruleAttrCondDef1799); 

                	newLeafNode(otherlv_6, grammarAccess.getAttrCondDefAccess().getLeftCurlyBracketKeyword_4());
                
            otherlv_7=(Token)match(input,27,FOLLOW_27_in_ruleAttrCondDef1811); 

                	newLeafNode(otherlv_7, grammarAccess.getAttrCondDefAccess().getSyncKeyword_5());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:883:1: ( (lv_allowedSyncAdornments_8_0= ruleAdornment ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:884:1: (lv_allowedSyncAdornments_8_0= ruleAdornment )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:884:1: (lv_allowedSyncAdornments_8_0= ruleAdornment )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:885:3: lv_allowedSyncAdornments_8_0= ruleAdornment
            {
             
            	        newCompositeNode(grammarAccess.getAttrCondDefAccess().getAllowedSyncAdornmentsAdornmentParserRuleCall_6_0()); 
            	    
            pushFollow(FOLLOW_ruleAdornment_in_ruleAttrCondDef1832);
            lv_allowedSyncAdornments_8_0=ruleAdornment();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getAttrCondDefRule());
            	        }
                   		add(
                   			current, 
                   			"allowedSyncAdornments",
                    		lv_allowedSyncAdornments_8_0, 
                    		"Adornment");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:901:2: (otherlv_9= ',' ( (lv_allowedAdornments_10_0= ruleAdornment ) ) )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==25) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:901:4: otherlv_9= ',' ( (lv_allowedAdornments_10_0= ruleAdornment ) )
            	    {
            	    otherlv_9=(Token)match(input,25,FOLLOW_25_in_ruleAttrCondDef1845); 

            	        	newLeafNode(otherlv_9, grammarAccess.getAttrCondDefAccess().getCommaKeyword_7_0());
            	        
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:905:1: ( (lv_allowedAdornments_10_0= ruleAdornment ) )
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:906:1: (lv_allowedAdornments_10_0= ruleAdornment )
            	    {
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:906:1: (lv_allowedAdornments_10_0= ruleAdornment )
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:907:3: lv_allowedAdornments_10_0= ruleAdornment
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getAttrCondDefAccess().getAllowedAdornmentsAdornmentParserRuleCall_7_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAdornment_in_ruleAttrCondDef1866);
            	    lv_allowedAdornments_10_0=ruleAdornment();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getAttrCondDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"allowedAdornments",
            	            		lv_allowedAdornments_10_0, 
            	            		"Adornment");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);

            otherlv_11=(Token)match(input,28,FOLLOW_28_in_ruleAttrCondDef1880); 

                	newLeafNode(otherlv_11, grammarAccess.getAttrCondDefAccess().getGenKeyword_8());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:927:1: ( ( (lv_allowedGenAdornments_12_0= ruleAdornment ) ) (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )* )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( ((LA30_0>=29 && LA30_0<=30)) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:927:2: ( (lv_allowedGenAdornments_12_0= ruleAdornment ) ) (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )*
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:927:2: ( (lv_allowedGenAdornments_12_0= ruleAdornment ) )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:928:1: (lv_allowedGenAdornments_12_0= ruleAdornment )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:928:1: (lv_allowedGenAdornments_12_0= ruleAdornment )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:929:3: lv_allowedGenAdornments_12_0= ruleAdornment
                    {
                     
                    	        newCompositeNode(grammarAccess.getAttrCondDefAccess().getAllowedGenAdornmentsAdornmentParserRuleCall_9_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleAdornment_in_ruleAttrCondDef1902);
                    lv_allowedGenAdornments_12_0=ruleAdornment();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getAttrCondDefRule());
                    	        }
                           		add(
                           			current, 
                           			"allowedGenAdornments",
                            		lv_allowedGenAdornments_12_0, 
                            		"Adornment");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:945:2: (otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) ) )*
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( (LA29_0==25) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:945:4: otherlv_13= ',' ( (lv_allowedAdornments_14_0= ruleAdornment ) )
                    	    {
                    	    otherlv_13=(Token)match(input,25,FOLLOW_25_in_ruleAttrCondDef1915); 

                    	        	newLeafNode(otherlv_13, grammarAccess.getAttrCondDefAccess().getCommaKeyword_9_1_0());
                    	        
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:949:1: ( (lv_allowedAdornments_14_0= ruleAdornment ) )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:950:1: (lv_allowedAdornments_14_0= ruleAdornment )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:950:1: (lv_allowedAdornments_14_0= ruleAdornment )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:951:3: lv_allowedAdornments_14_0= ruleAdornment
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getAttrCondDefAccess().getAllowedAdornmentsAdornmentParserRuleCall_9_1_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleAdornment_in_ruleAttrCondDef1936);
                    	    lv_allowedAdornments_14_0=ruleAdornment();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getAttrCondDefRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"allowedAdornments",
                    	            		lv_allowedAdornments_14_0, 
                    	            		"Adornment");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop29;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_15=(Token)match(input,14,FOLLOW_14_in_ruleAttrCondDef1952); 

                	newLeafNode(otherlv_15, grammarAccess.getAttrCondDefAccess().getRightCurlyBracketKeyword_10());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttrCondDef"


    // $ANTLR start "entryRuleAdornment"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:979:1: entryRuleAdornment returns [String current=null] : iv_ruleAdornment= ruleAdornment EOF ;
    public final String entryRuleAdornment() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAdornment = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:980:2: (iv_ruleAdornment= ruleAdornment EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:981:2: iv_ruleAdornment= ruleAdornment EOF
            {
             newCompositeNode(grammarAccess.getAdornmentRule()); 
            pushFollow(FOLLOW_ruleAdornment_in_entryRuleAdornment1989);
            iv_ruleAdornment=ruleAdornment();

            state._fsp--;

             current =iv_ruleAdornment.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAdornment2000); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAdornment"


    // $ANTLR start "ruleAdornment"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:988:1: ruleAdornment returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (kw= 'B' | kw= 'F' ) (kw= '|' (kw= 'B' | kw= 'F' ) )* ) ;
    public final AntlrDatatypeRuleToken ruleAdornment() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:991:28: ( ( (kw= 'B' | kw= 'F' ) (kw= '|' (kw= 'B' | kw= 'F' ) )* ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:992:1: ( (kw= 'B' | kw= 'F' ) (kw= '|' (kw= 'B' | kw= 'F' ) )* )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:992:1: ( (kw= 'B' | kw= 'F' ) (kw= '|' (kw= 'B' | kw= 'F' ) )* )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:992:2: (kw= 'B' | kw= 'F' ) (kw= '|' (kw= 'B' | kw= 'F' ) )*
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:992:2: (kw= 'B' | kw= 'F' )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==29) ) {
                alt31=1;
            }
            else if ( (LA31_0==30) ) {
                alt31=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }
            switch (alt31) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:993:2: kw= 'B'
                    {
                    kw=(Token)match(input,29,FOLLOW_29_in_ruleAdornment2039); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getAdornmentAccess().getBKeyword_0_0()); 
                        

                    }
                    break;
                case 2 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1000:2: kw= 'F'
                    {
                    kw=(Token)match(input,30,FOLLOW_30_in_ruleAdornment2058); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getAdornmentAccess().getFKeyword_0_1()); 
                        

                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1005:2: (kw= '|' (kw= 'B' | kw= 'F' ) )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==31) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1006:2: kw= '|' (kw= 'B' | kw= 'F' )
            	    {
            	    kw=(Token)match(input,31,FOLLOW_31_in_ruleAdornment2073); 

            	            current.merge(kw);
            	            newLeafNode(kw, grammarAccess.getAdornmentAccess().getVerticalLineKeyword_1_0()); 
            	        
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1011:1: (kw= 'B' | kw= 'F' )
            	    int alt32=2;
            	    int LA32_0 = input.LA(1);

            	    if ( (LA32_0==29) ) {
            	        alt32=1;
            	    }
            	    else if ( (LA32_0==30) ) {
            	        alt32=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 32, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt32) {
            	        case 1 :
            	            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1012:2: kw= 'B'
            	            {
            	            kw=(Token)match(input,29,FOLLOW_29_in_ruleAdornment2087); 

            	                    current.merge(kw);
            	                    newLeafNode(kw, grammarAccess.getAdornmentAccess().getBKeyword_1_1_0()); 
            	                

            	            }
            	            break;
            	        case 2 :
            	            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1019:2: kw= 'F'
            	            {
            	            kw=(Token)match(input,30,FOLLOW_30_in_ruleAdornment2106); 

            	                    current.merge(kw);
            	                    newLeafNode(kw, grammarAccess.getAdornmentAccess().getFKeyword_1_1_1()); 
            	                

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAdornment"


    // $ANTLR start "entryRuleParam"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1032:1: entryRuleParam returns [EObject current=null] : iv_ruleParam= ruleParam EOF ;
    public final EObject entryRuleParam() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParam = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1033:2: (iv_ruleParam= ruleParam EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1034:2: iv_ruleParam= ruleParam EOF
            {
             newCompositeNode(grammarAccess.getParamRule()); 
            pushFollow(FOLLOW_ruleParam_in_entryRuleParam2149);
            iv_ruleParam=ruleParam();

            state._fsp--;

             current =iv_ruleParam; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParam2159); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParam"


    // $ANTLR start "ruleParam"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1041:1: ruleParam returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleQualifiedName ) ) ) ;
    public final EObject ruleParam() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;

         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1044:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleQualifiedName ) ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1045:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleQualifiedName ) ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1045:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleQualifiedName ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1045:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleQualifiedName ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1045:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1046:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1046:1: (lv_name_0_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1047:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParam2201); 

            			newLeafNode(lv_name_0_0, grammarAccess.getParamAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getParamRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            otherlv_1=(Token)match(input,32,FOLLOW_32_in_ruleParam2218); 

                	newLeafNode(otherlv_1, grammarAccess.getParamAccess().getColonKeyword_1());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1067:1: ( ( ruleQualifiedName ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1068:1: ( ruleQualifiedName )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1068:1: ( ruleQualifiedName )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1069:3: ruleQualifiedName
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getParamRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getParamAccess().getTypeEDataTypeCrossReference_2_0()); 
            	    
            pushFollow(FOLLOW_ruleQualifiedName_in_ruleParam2241);
            ruleQualifiedName();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParam"


    // $ANTLR start "entryRuleParamValue"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1090:1: entryRuleParamValue returns [EObject current=null] : iv_ruleParamValue= ruleParamValue EOF ;
    public final EObject entryRuleParamValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParamValue = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1091:2: (iv_ruleParamValue= ruleParamValue EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1092:2: iv_ruleParamValue= ruleParamValue EOF
            {
             newCompositeNode(grammarAccess.getParamValueRule()); 
            pushFollow(FOLLOW_ruleParamValue_in_entryRuleParamValue2277);
            iv_ruleParamValue=ruleParamValue();

            state._fsp--;

             current =iv_ruleParamValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParamValue2287); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParamValue"


    // $ANTLR start "ruleParamValue"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1099:1: ruleParamValue returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' ( (otherlv_2= RULE_ID ) ) ) ;
    public final EObject ruleParamValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;

         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1102:28: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' ( (otherlv_2= RULE_ID ) ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1103:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' ( (otherlv_2= RULE_ID ) ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1103:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' ( (otherlv_2= RULE_ID ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1103:2: ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' ( (otherlv_2= RULE_ID ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1103:2: ( (otherlv_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1104:1: (otherlv_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1104:1: (otherlv_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1105:3: otherlv_0= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getParamValueRule());
            	        }
                    
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParamValue2332); 

            		newLeafNode(otherlv_0, grammarAccess.getParamValueAccess().getObjectVarObjectVariablePatternCrossReference_0_0()); 
            	

            }


            }

            otherlv_1=(Token)match(input,33,FOLLOW_33_in_ruleParamValue2344); 

                	newLeafNode(otherlv_1, grammarAccess.getParamValueAccess().getFullStopKeyword_1());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1120:1: ( (otherlv_2= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1121:1: (otherlv_2= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1121:1: (otherlv_2= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1122:3: otherlv_2= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getParamValueRule());
            	        }
                    
            otherlv_2=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParamValue2364); 

            		newLeafNode(otherlv_2, grammarAccess.getParamValueAccess().getTypeEAttributeCrossReference_2_0()); 
            	

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParamValue"


    // $ANTLR start "entryRuleCorrVariablePattern"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1141:1: entryRuleCorrVariablePattern returns [EObject current=null] : iv_ruleCorrVariablePattern= ruleCorrVariablePattern EOF ;
    public final EObject entryRuleCorrVariablePattern() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCorrVariablePattern = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1142:2: (iv_ruleCorrVariablePattern= ruleCorrVariablePattern EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1143:2: iv_ruleCorrVariablePattern= ruleCorrVariablePattern EOF
            {
             newCompositeNode(grammarAccess.getCorrVariablePatternRule()); 
            pushFollow(FOLLOW_ruleCorrVariablePattern_in_entryRuleCorrVariablePattern2400);
            iv_ruleCorrVariablePattern=ruleCorrVariablePattern();

            state._fsp--;

             current =iv_ruleCorrVariablePattern; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleCorrVariablePattern2410); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCorrVariablePattern"


    // $ANTLR start "ruleCorrVariablePattern"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1150:1: ruleCorrVariablePattern returns [EObject current=null] : ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) otherlv_4= '{' otherlv_5= '-src->' ( (otherlv_6= RULE_ID ) ) otherlv_7= '-trg->' ( (otherlv_8= RULE_ID ) ) otherlv_9= '}' ) ;
    public final EObject ruleCorrVariablePattern() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        EObject lv_op_0_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1153:28: ( ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) otherlv_4= '{' otherlv_5= '-src->' ( (otherlv_6= RULE_ID ) ) otherlv_7= '-trg->' ( (otherlv_8= RULE_ID ) ) otherlv_9= '}' ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1154:1: ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) otherlv_4= '{' otherlv_5= '-src->' ( (otherlv_6= RULE_ID ) ) otherlv_7= '-trg->' ( (otherlv_8= RULE_ID ) ) otherlv_9= '}' )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1154:1: ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) otherlv_4= '{' otherlv_5= '-src->' ( (otherlv_6= RULE_ID ) ) otherlv_7= '-trg->' ( (otherlv_8= RULE_ID ) ) otherlv_9= '}' )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1154:2: ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) otherlv_4= '{' otherlv_5= '-src->' ( (otherlv_6= RULE_ID ) ) otherlv_7= '-trg->' ( (otherlv_8= RULE_ID ) ) otherlv_9= '}'
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1154:2: ( (lv_op_0_0= ruleOperator ) )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( ((LA34_0>=37 && LA34_0<=38)) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1155:1: (lv_op_0_0= ruleOperator )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1155:1: (lv_op_0_0= ruleOperator )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1156:3: lv_op_0_0= ruleOperator
                    {
                     
                    	        newCompositeNode(grammarAccess.getCorrVariablePatternAccess().getOpOperatorParserRuleCall_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleOperator_in_ruleCorrVariablePattern2456);
                    lv_op_0_0=ruleOperator();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getCorrVariablePatternRule());
                    	        }
                           		set(
                           			current, 
                           			"op",
                            		lv_op_0_0, 
                            		"Operator");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1172:3: ( (lv_name_1_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1173:1: (lv_name_1_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1173:1: (lv_name_1_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1174:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleCorrVariablePattern2474); 

            			newLeafNode(lv_name_1_0, grammarAccess.getCorrVariablePatternAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getCorrVariablePatternRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,34,FOLLOW_34_in_ruleCorrVariablePattern2491); 

                	newLeafNode(otherlv_2, grammarAccess.getCorrVariablePatternAccess().getSpaceColonSpaceKeyword_2());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1194:1: ( ( ruleQualifiedName ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1195:1: ( ruleQualifiedName )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1195:1: ( ruleQualifiedName )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1196:3: ruleQualifiedName
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getCorrVariablePatternRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getCorrVariablePatternAccess().getTypeCorrTypeCrossReference_3_0()); 
            	    
            pushFollow(FOLLOW_ruleQualifiedName_in_ruleCorrVariablePattern2514);
            ruleQualifiedName();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_4=(Token)match(input,13,FOLLOW_13_in_ruleCorrVariablePattern2526); 

                	newLeafNode(otherlv_4, grammarAccess.getCorrVariablePatternAccess().getLeftCurlyBracketKeyword_4());
                
            otherlv_5=(Token)match(input,19,FOLLOW_19_in_ruleCorrVariablePattern2538); 

                	newLeafNode(otherlv_5, grammarAccess.getCorrVariablePatternAccess().getSrcKeyword_5());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1217:1: ( (otherlv_6= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1218:1: (otherlv_6= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1218:1: (otherlv_6= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1219:3: otherlv_6= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getCorrVariablePatternRule());
            	        }
                    
            otherlv_6=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleCorrVariablePattern2558); 

            		newLeafNode(otherlv_6, grammarAccess.getCorrVariablePatternAccess().getSourceObjectVariablePatternCrossReference_6_0()); 
            	

            }


            }

            otherlv_7=(Token)match(input,20,FOLLOW_20_in_ruleCorrVariablePattern2570); 

                	newLeafNode(otherlv_7, grammarAccess.getCorrVariablePatternAccess().getTrgKeyword_7());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1234:1: ( (otherlv_8= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1235:1: (otherlv_8= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1235:1: (otherlv_8= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1236:3: otherlv_8= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getCorrVariablePatternRule());
            	        }
                    
            otherlv_8=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleCorrVariablePattern2590); 

            		newLeafNode(otherlv_8, grammarAccess.getCorrVariablePatternAccess().getTargetObjectVariablePatternCrossReference_8_0()); 
            	

            }


            }

            otherlv_9=(Token)match(input,14,FOLLOW_14_in_ruleCorrVariablePattern2602); 

                	newLeafNode(otherlv_9, grammarAccess.getCorrVariablePatternAccess().getRightCurlyBracketKeyword_9());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCorrVariablePattern"


    // $ANTLR start "entryRuleObjectVariablePattern"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1259:1: entryRuleObjectVariablePattern returns [EObject current=null] : iv_ruleObjectVariablePattern= ruleObjectVariablePattern EOF ;
    public final EObject entryRuleObjectVariablePattern() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObjectVariablePattern = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1260:2: (iv_ruleObjectVariablePattern= ruleObjectVariablePattern EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1261:2: iv_ruleObjectVariablePattern= ruleObjectVariablePattern EOF
            {
             newCompositeNode(grammarAccess.getObjectVariablePatternRule()); 
            pushFollow(FOLLOW_ruleObjectVariablePattern_in_entryRuleObjectVariablePattern2638);
            iv_ruleObjectVariablePattern=ruleObjectVariablePattern();

            state._fsp--;

             current =iv_ruleObjectVariablePattern; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleObjectVariablePattern2648); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleObjectVariablePattern"


    // $ANTLR start "ruleObjectVariablePattern"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1268:1: ruleObjectVariablePattern returns [EObject current=null] : ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) (otherlv_4= '{' ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )* otherlv_6= '}' )? ) ;
    public final EObject ruleObjectVariablePattern() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_op_0_0 = null;

        EObject lv_linkVariablePatterns_5_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1271:28: ( ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) (otherlv_4= '{' ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )* otherlv_6= '}' )? ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1272:1: ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) (otherlv_4= '{' ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )* otherlv_6= '}' )? )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1272:1: ( ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) (otherlv_4= '{' ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )* otherlv_6= '}' )? )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1272:2: ( (lv_op_0_0= ruleOperator ) )? ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ' : ' ( ( ruleQualifiedName ) ) (otherlv_4= '{' ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )* otherlv_6= '}' )?
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1272:2: ( (lv_op_0_0= ruleOperator ) )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( ((LA35_0>=37 && LA35_0<=38)) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1273:1: (lv_op_0_0= ruleOperator )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1273:1: (lv_op_0_0= ruleOperator )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1274:3: lv_op_0_0= ruleOperator
                    {
                     
                    	        newCompositeNode(grammarAccess.getObjectVariablePatternAccess().getOpOperatorParserRuleCall_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleOperator_in_ruleObjectVariablePattern2694);
                    lv_op_0_0=ruleOperator();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getObjectVariablePatternRule());
                    	        }
                           		set(
                           			current, 
                           			"op",
                            		lv_op_0_0, 
                            		"Operator");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1290:3: ( (lv_name_1_0= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1291:1: (lv_name_1_0= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1291:1: (lv_name_1_0= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1292:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectVariablePattern2712); 

            			newLeafNode(lv_name_1_0, grammarAccess.getObjectVariablePatternAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getObjectVariablePatternRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,34,FOLLOW_34_in_ruleObjectVariablePattern2729); 

                	newLeafNode(otherlv_2, grammarAccess.getObjectVariablePatternAccess().getSpaceColonSpaceKeyword_2());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1312:1: ( ( ruleQualifiedName ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1313:1: ( ruleQualifiedName )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1313:1: ( ruleQualifiedName )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1314:3: ruleQualifiedName
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getObjectVariablePatternRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getObjectVariablePatternAccess().getTypeEClassCrossReference_3_0()); 
            	    
            pushFollow(FOLLOW_ruleQualifiedName_in_ruleObjectVariablePattern2752);
            ruleQualifiedName();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1327:2: (otherlv_4= '{' ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )* otherlv_6= '}' )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==13) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1327:4: otherlv_4= '{' ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )* otherlv_6= '}'
                    {
                    otherlv_4=(Token)match(input,13,FOLLOW_13_in_ruleObjectVariablePattern2765); 

                        	newLeafNode(otherlv_4, grammarAccess.getObjectVariablePatternAccess().getLeftCurlyBracketKeyword_4_0());
                        
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1331:1: ( (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern ) )*
                    loop36:
                    do {
                        int alt36=2;
                        int LA36_0 = input.LA(1);

                        if ( (LA36_0==35||(LA36_0>=37 && LA36_0<=38)) ) {
                            alt36=1;
                        }


                        switch (alt36) {
                    	case 1 :
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1332:1: (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern )
                    	    {
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1332:1: (lv_linkVariablePatterns_5_0= ruleLinkVariablePattern )
                    	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1333:3: lv_linkVariablePatterns_5_0= ruleLinkVariablePattern
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getObjectVariablePatternAccess().getLinkVariablePatternsLinkVariablePatternParserRuleCall_4_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleLinkVariablePattern_in_ruleObjectVariablePattern2786);
                    	    lv_linkVariablePatterns_5_0=ruleLinkVariablePattern();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getObjectVariablePatternRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"linkVariablePatterns",
                    	            		lv_linkVariablePatterns_5_0, 
                    	            		"LinkVariablePattern");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop36;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,14,FOLLOW_14_in_ruleObjectVariablePattern2799); 

                        	newLeafNode(otherlv_6, grammarAccess.getObjectVariablePatternAccess().getRightCurlyBracketKeyword_4_2());
                        

                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleObjectVariablePattern"


    // $ANTLR start "entryRuleLinkVariablePattern"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1361:1: entryRuleLinkVariablePattern returns [EObject current=null] : iv_ruleLinkVariablePattern= ruleLinkVariablePattern EOF ;
    public final EObject entryRuleLinkVariablePattern() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLinkVariablePattern = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1362:2: (iv_ruleLinkVariablePattern= ruleLinkVariablePattern EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1363:2: iv_ruleLinkVariablePattern= ruleLinkVariablePattern EOF
            {
             newCompositeNode(grammarAccess.getLinkVariablePatternRule()); 
            pushFollow(FOLLOW_ruleLinkVariablePattern_in_entryRuleLinkVariablePattern2837);
            iv_ruleLinkVariablePattern=ruleLinkVariablePattern();

            state._fsp--;

             current =iv_ruleLinkVariablePattern; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLinkVariablePattern2847); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLinkVariablePattern"


    // $ANTLR start "ruleLinkVariablePattern"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1370:1: ruleLinkVariablePattern returns [EObject current=null] : ( ( (lv_op_0_0= ruleOperator ) )? otherlv_1= '-' ( (otherlv_2= RULE_ID ) ) otherlv_3= '->' ( (otherlv_4= RULE_ID ) ) ) ;
    public final EObject ruleLinkVariablePattern() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_op_0_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1373:28: ( ( ( (lv_op_0_0= ruleOperator ) )? otherlv_1= '-' ( (otherlv_2= RULE_ID ) ) otherlv_3= '->' ( (otherlv_4= RULE_ID ) ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1374:1: ( ( (lv_op_0_0= ruleOperator ) )? otherlv_1= '-' ( (otherlv_2= RULE_ID ) ) otherlv_3= '->' ( (otherlv_4= RULE_ID ) ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1374:1: ( ( (lv_op_0_0= ruleOperator ) )? otherlv_1= '-' ( (otherlv_2= RULE_ID ) ) otherlv_3= '->' ( (otherlv_4= RULE_ID ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1374:2: ( (lv_op_0_0= ruleOperator ) )? otherlv_1= '-' ( (otherlv_2= RULE_ID ) ) otherlv_3= '->' ( (otherlv_4= RULE_ID ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1374:2: ( (lv_op_0_0= ruleOperator ) )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( ((LA38_0>=37 && LA38_0<=38)) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1375:1: (lv_op_0_0= ruleOperator )
                    {
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1375:1: (lv_op_0_0= ruleOperator )
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1376:3: lv_op_0_0= ruleOperator
                    {
                     
                    	        newCompositeNode(grammarAccess.getLinkVariablePatternAccess().getOpOperatorParserRuleCall_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleOperator_in_ruleLinkVariablePattern2893);
                    lv_op_0_0=ruleOperator();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getLinkVariablePatternRule());
                    	        }
                           		set(
                           			current, 
                           			"op",
                            		lv_op_0_0, 
                            		"Operator");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,35,FOLLOW_35_in_ruleLinkVariablePattern2906); 

                	newLeafNode(otherlv_1, grammarAccess.getLinkVariablePatternAccess().getHyphenMinusKeyword_1());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1396:1: ( (otherlv_2= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1397:1: (otherlv_2= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1397:1: (otherlv_2= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1398:3: otherlv_2= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getLinkVariablePatternRule());
            	        }
                    
            otherlv_2=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkVariablePattern2926); 

            		newLeafNode(otherlv_2, grammarAccess.getLinkVariablePatternAccess().getTypeEReferenceCrossReference_2_0()); 
            	

            }


            }

            otherlv_3=(Token)match(input,36,FOLLOW_36_in_ruleLinkVariablePattern2938); 

                	newLeafNode(otherlv_3, grammarAccess.getLinkVariablePatternAccess().getHyphenMinusGreaterThanSignKeyword_3());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1413:1: ( (otherlv_4= RULE_ID ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1414:1: (otherlv_4= RULE_ID )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1414:1: (otherlv_4= RULE_ID )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1415:3: otherlv_4= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getLinkVariablePatternRule());
            	        }
                    
            otherlv_4=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkVariablePattern2958); 

            		newLeafNode(otherlv_4, grammarAccess.getLinkVariablePatternAccess().getTargetObjectVariablePatternCrossReference_4_0()); 
            	

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLinkVariablePattern"


    // $ANTLR start "entryRuleOperator"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1434:1: entryRuleOperator returns [EObject current=null] : iv_ruleOperator= ruleOperator EOF ;
    public final EObject entryRuleOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperator = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1435:2: (iv_ruleOperator= ruleOperator EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1436:2: iv_ruleOperator= ruleOperator EOF
            {
             newCompositeNode(grammarAccess.getOperatorRule()); 
            pushFollow(FOLLOW_ruleOperator_in_entryRuleOperator2994);
            iv_ruleOperator=ruleOperator();

            state._fsp--;

             current =iv_ruleOperator; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperator3004); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOperator"


    // $ANTLR start "ruleOperator"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1443:1: ruleOperator returns [EObject current=null] : ( ( (lv_value_0_1= '++ ' | lv_value_0_2= '! ' ) ) ) ;
    public final EObject ruleOperator() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_1=null;
        Token lv_value_0_2=null;

         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1446:28: ( ( ( (lv_value_0_1= '++ ' | lv_value_0_2= '! ' ) ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1447:1: ( ( (lv_value_0_1= '++ ' | lv_value_0_2= '! ' ) ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1447:1: ( ( (lv_value_0_1= '++ ' | lv_value_0_2= '! ' ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1448:1: ( (lv_value_0_1= '++ ' | lv_value_0_2= '! ' ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1448:1: ( (lv_value_0_1= '++ ' | lv_value_0_2= '! ' ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1449:1: (lv_value_0_1= '++ ' | lv_value_0_2= '! ' )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1449:1: (lv_value_0_1= '++ ' | lv_value_0_2= '! ' )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==37) ) {
                alt39=1;
            }
            else if ( (LA39_0==38) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1450:3: lv_value_0_1= '++ '
                    {
                    lv_value_0_1=(Token)match(input,37,FOLLOW_37_in_ruleOperator3048); 

                            newLeafNode(lv_value_0_1, grammarAccess.getOperatorAccess().getValuePlusSignPlusSignSpaceKeyword_0_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getOperatorRule());
                    	        }
                           		setWithLastConsumed(current, "value", lv_value_0_1, null);
                    	    

                    }
                    break;
                case 2 :
                    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1462:8: lv_value_0_2= '! '
                    {
                    lv_value_0_2=(Token)match(input,38,FOLLOW_38_in_ruleOperator3077); 

                            newLeafNode(lv_value_0_2, grammarAccess.getOperatorAccess().getValueExclamationMarkSpaceKeyword_0_1());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getOperatorRule());
                    	        }
                           		setWithLastConsumed(current, "value", lv_value_0_2, null);
                    	    

                    }
                    break;

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOperator"


    // $ANTLR start "entryRuleImport"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1485:1: entryRuleImport returns [EObject current=null] : iv_ruleImport= ruleImport EOF ;
    public final EObject entryRuleImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImport = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1486:2: (iv_ruleImport= ruleImport EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1487:2: iv_ruleImport= ruleImport EOF
            {
             newCompositeNode(grammarAccess.getImportRule()); 
            pushFollow(FOLLOW_ruleImport_in_entryRuleImport3128);
            iv_ruleImport=ruleImport();

            state._fsp--;

             current =iv_ruleImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImport3138); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleImport"


    // $ANTLR start "ruleImport"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1494:1: ruleImport returns [EObject current=null] : (otherlv_0= 'import' ( (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard ) ) ) ;
    public final EObject ruleImport() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_importedNamespace_1_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1497:28: ( (otherlv_0= 'import' ( (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard ) ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1498:1: (otherlv_0= 'import' ( (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard ) ) )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1498:1: (otherlv_0= 'import' ( (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard ) ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1498:3: otherlv_0= 'import' ( (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard ) )
            {
            otherlv_0=(Token)match(input,39,FOLLOW_39_in_ruleImport3175); 

                	newLeafNode(otherlv_0, grammarAccess.getImportAccess().getImportKeyword_0());
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1502:1: ( (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1503:1: (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1503:1: (lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1504:3: lv_importedNamespace_1_0= ruleQualifiedNameWithWildcard
            {
             
            	        newCompositeNode(grammarAccess.getImportAccess().getImportedNamespaceQualifiedNameWithWildcardParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleQualifiedNameWithWildcard_in_ruleImport3196);
            lv_importedNamespace_1_0=ruleQualifiedNameWithWildcard();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getImportRule());
            	        }
                   		set(
                   			current, 
                   			"importedNamespace",
                    		lv_importedNamespace_1_0, 
                    		"QualifiedNameWithWildcard");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleImport"


    // $ANTLR start "entryRuleQualifiedNameWithWildcard"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1528:1: entryRuleQualifiedNameWithWildcard returns [String current=null] : iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF ;
    public final String entryRuleQualifiedNameWithWildcard() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedNameWithWildcard = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1529:2: (iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1530:2: iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF
            {
             newCompositeNode(grammarAccess.getQualifiedNameWithWildcardRule()); 
            pushFollow(FOLLOW_ruleQualifiedNameWithWildcard_in_entryRuleQualifiedNameWithWildcard3233);
            iv_ruleQualifiedNameWithWildcard=ruleQualifiedNameWithWildcard();

            state._fsp--;

             current =iv_ruleQualifiedNameWithWildcard.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleQualifiedNameWithWildcard3244); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQualifiedNameWithWildcard"


    // $ANTLR start "ruleQualifiedNameWithWildcard"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1537:1: ruleQualifiedNameWithWildcard returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_QualifiedName_0= ruleQualifiedName kw= '.*' ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedNameWithWildcard() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_QualifiedName_0 = null;


         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1540:28: ( (this_QualifiedName_0= ruleQualifiedName kw= '.*' ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1541:1: (this_QualifiedName_0= ruleQualifiedName kw= '.*' )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1541:1: (this_QualifiedName_0= ruleQualifiedName kw= '.*' )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1542:5: this_QualifiedName_0= ruleQualifiedName kw= '.*'
            {
             
                    newCompositeNode(grammarAccess.getQualifiedNameWithWildcardAccess().getQualifiedNameParserRuleCall_0()); 
                
            pushFollow(FOLLOW_ruleQualifiedName_in_ruleQualifiedNameWithWildcard3291);
            this_QualifiedName_0=ruleQualifiedName();

            state._fsp--;


            		current.merge(this_QualifiedName_0);
                
             
                    afterParserOrEnumRuleCall();
                
            kw=(Token)match(input,40,FOLLOW_40_in_ruleQualifiedNameWithWildcard3309); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getQualifiedNameWithWildcardAccess().getFullStopAsteriskKeyword_1()); 
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQualifiedNameWithWildcard"


    // $ANTLR start "entryRuleQualifiedName"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1566:1: entryRuleQualifiedName returns [String current=null] : iv_ruleQualifiedName= ruleQualifiedName EOF ;
    public final String entryRuleQualifiedName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedName = null;


        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1567:2: (iv_ruleQualifiedName= ruleQualifiedName EOF )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1568:2: iv_ruleQualifiedName= ruleQualifiedName EOF
            {
             newCompositeNode(grammarAccess.getQualifiedNameRule()); 
            pushFollow(FOLLOW_ruleQualifiedName_in_entryRuleQualifiedName3350);
            iv_ruleQualifiedName=ruleQualifiedName();

            state._fsp--;

             current =iv_ruleQualifiedName.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleQualifiedName3361); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQualifiedName"


    // $ANTLR start "ruleQualifiedName"
    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1575:1: ruleQualifiedName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;

         enterRule(); 
            
        try {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1578:28: ( (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1579:1: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            {
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1579:1: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1579:6: this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleQualifiedName3401); 

            		current.merge(this_ID_0);
                
             
                newLeafNode(this_ID_0, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0()); 
                
            // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1586:1: (kw= '.' this_ID_2= RULE_ID )*
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==33) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // ../org.moflon.tgg.mosl/src-gen/org/moflon/tgg/mosl/parser/antlr/internal/InternalTGG.g:1587:2: kw= '.' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,33,FOLLOW_33_in_ruleQualifiedName3420); 

            	            current.merge(kw);
            	            newLeafNode(kw, grammarAccess.getQualifiedNameAccess().getFullStopKeyword_1_0()); 
            	        
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleQualifiedName3435); 

            	    		current.merge(this_ID_2);
            	        
            	     
            	        newLeafNode(this_ID_2, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1()); 
            	        

            	    }
            	    break;

            	default :
            	    break loop40;
                }
            } while (true);


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQualifiedName"

    // Delegated rules


    protected DFA1 dfa1 = new DFA1(this);
    static final String DFA1_eotS =
        "\10\uffff";
    static final String DFA1_eofS =
        "\1\3\7\uffff";
    static final String DFA1_minS =
        "\1\13\1\4\2\uffff\1\41\1\4\1\13\1\41";
    static final String DFA1_maxS =
        "\1\47\1\4\2\uffff\1\50\1\4\1\47\1\50";
    static final String DFA1_acceptS =
        "\2\uffff\1\1\1\2\4\uffff";
    static final String DFA1_specialS =
        "\10\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\2\11\uffff\2\3\20\uffff\1\1",
            "\1\4",
            "",
            "",
            "\1\5\6\uffff\1\6",
            "\1\7",
            "\1\2\11\uffff\2\3\20\uffff\1\1",
            "\1\5\6\uffff\1\6"
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "80:2: ( (lv_schema_0_0= ruleSchema ) )?";
        }
    }
 

    public static final BitSet FOLLOW_ruleTripleGraphGrammar_in_entryRuleTripleGraphGrammar75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTripleGraphGrammar85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSchema_in_ruleTripleGraphGrammar131 = new BitSet(new long[]{0x0000008000600002L});
    public static final BitSet FOLLOW_ruleRule_in_ruleTripleGraphGrammar153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSchema_in_entryRuleSchema192 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSchema202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImport_in_ruleSchema248 = new BitSet(new long[]{0x0000008000000800L});
    public static final BitSet FOLLOW_11_in_ruleSchema261 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSchema278 = new BitSet(new long[]{0x0000000000039002L});
    public static final BitSet FOLLOW_12_in_ruleSchema296 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleSchema308 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleSchema331 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleSchema344 = new BitSet(new long[]{0x0000000000038002L});
    public static final BitSet FOLLOW_15_in_ruleSchema359 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleSchema371 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleSchema394 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleSchema407 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_16_in_ruleSchema422 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleSchema434 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleCorrType_in_ruleSchema455 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleSchema468 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleSchema483 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleSchema495 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleAttrCondDef_in_ruleSchema516 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleSchema529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCorrType_in_entryRuleCorrType567 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleCorrType577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCorrTypeDef_in_ruleCorrType624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTypeExtension_in_ruleCorrType651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTypeExtension_in_entryRuleTypeExtension686 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTypeExtension696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTypeExtension738 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleTypeExtension755 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTypeExtension775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCorrTypeDef_in_entryRuleCorrTypeDef811 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleCorrTypeDef821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleCorrTypeDef863 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleCorrTypeDef880 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleCorrTypeDef892 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleCorrTypeDef915 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_ruleCorrTypeDef927 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleCorrTypeDef950 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleCorrTypeDef962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleRule_in_entryRuleRule998 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleRule1008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImport_in_ruleRule1054 = new BitSet(new long[]{0x0000008000600000L});
    public static final BitSet FOLLOW_21_in_ruleRule1073 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_ruleRule1099 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleRule1116 = new BitSet(new long[]{0x0000000000840000L});
    public static final BitSet FOLLOW_18_in_ruleRule1134 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleRule1154 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_ruleRule1168 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleRule1191 = new BitSet(new long[]{0x0000000000039002L});
    public static final BitSet FOLLOW_12_in_ruleRule1204 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleRule1216 = new BitSet(new long[]{0x0000006000004010L});
    public static final BitSet FOLLOW_ruleObjectVariablePattern_in_ruleRule1237 = new BitSet(new long[]{0x0000006000004010L});
    public static final BitSet FOLLOW_14_in_ruleRule1250 = new BitSet(new long[]{0x0000000000038002L});
    public static final BitSet FOLLOW_15_in_ruleRule1265 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleRule1277 = new BitSet(new long[]{0x0000006000004010L});
    public static final BitSet FOLLOW_ruleObjectVariablePattern_in_ruleRule1298 = new BitSet(new long[]{0x0000006000004010L});
    public static final BitSet FOLLOW_14_in_ruleRule1311 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_16_in_ruleRule1326 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleRule1338 = new BitSet(new long[]{0x0000006000004010L});
    public static final BitSet FOLLOW_ruleCorrVariablePattern_in_ruleRule1359 = new BitSet(new long[]{0x0000006000004010L});
    public static final BitSet FOLLOW_14_in_ruleRule1372 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleRule1387 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleRule1399 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleAttrCond_in_ruleRule1420 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleRule1433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAttrCond_in_entryRuleAttrCond1471 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAttrCond1481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAttrCond1526 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleAttrCond1538 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_ruleParamValue_in_ruleAttrCond1560 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_25_in_ruleAttrCond1573 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleParamValue_in_ruleAttrCond1594 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_26_in_ruleAttrCond1610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAttrCondDef_in_entryRuleAttrCondDef1646 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAttrCondDef1656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAttrCondDef1698 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleAttrCondDef1715 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_ruleParam_in_ruleAttrCondDef1737 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_25_in_ruleAttrCondDef1750 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleParam_in_ruleAttrCondDef1771 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_26_in_ruleAttrCondDef1787 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleAttrCondDef1799 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleAttrCondDef1811 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_ruleAdornment_in_ruleAttrCondDef1832 = new BitSet(new long[]{0x0000000012000000L});
    public static final BitSet FOLLOW_25_in_ruleAttrCondDef1845 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_ruleAdornment_in_ruleAttrCondDef1866 = new BitSet(new long[]{0x0000000012000000L});
    public static final BitSet FOLLOW_28_in_ruleAttrCondDef1880 = new BitSet(new long[]{0x0000000060004000L});
    public static final BitSet FOLLOW_ruleAdornment_in_ruleAttrCondDef1902 = new BitSet(new long[]{0x0000000002004000L});
    public static final BitSet FOLLOW_25_in_ruleAttrCondDef1915 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_ruleAdornment_in_ruleAttrCondDef1936 = new BitSet(new long[]{0x0000000002004000L});
    public static final BitSet FOLLOW_14_in_ruleAttrCondDef1952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAdornment_in_entryRuleAdornment1989 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAdornment2000 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleAdornment2039 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_30_in_ruleAdornment2058 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_31_in_ruleAdornment2073 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_29_in_ruleAdornment2087 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_30_in_ruleAdornment2106 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_ruleParam_in_entryRuleParam2149 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParam2159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParam2201 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_ruleParam2218 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleParam2241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParamValue_in_entryRuleParamValue2277 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParamValue2287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParamValue2332 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_ruleParamValue2344 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParamValue2364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCorrVariablePattern_in_entryRuleCorrVariablePattern2400 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleCorrVariablePattern2410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleCorrVariablePattern2456 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleCorrVariablePattern2474 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_ruleCorrVariablePattern2491 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleCorrVariablePattern2514 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleCorrVariablePattern2526 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleCorrVariablePattern2538 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleCorrVariablePattern2558 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_ruleCorrVariablePattern2570 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleCorrVariablePattern2590 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleCorrVariablePattern2602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectVariablePattern_in_entryRuleObjectVariablePattern2638 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleObjectVariablePattern2648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleObjectVariablePattern2694 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectVariablePattern2712 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_ruleObjectVariablePattern2729 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleObjectVariablePattern2752 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleObjectVariablePattern2765 = new BitSet(new long[]{0x0000006800004000L});
    public static final BitSet FOLLOW_ruleLinkVariablePattern_in_ruleObjectVariablePattern2786 = new BitSet(new long[]{0x0000006800004000L});
    public static final BitSet FOLLOW_14_in_ruleObjectVariablePattern2799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkVariablePattern_in_entryRuleLinkVariablePattern2837 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLinkVariablePattern2847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleLinkVariablePattern2893 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_ruleLinkVariablePattern2906 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkVariablePattern2926 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_ruleLinkVariablePattern2938 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkVariablePattern2958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_entryRuleOperator2994 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperator3004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_ruleOperator3048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_ruleOperator3077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImport_in_entryRuleImport3128 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImport3138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_ruleImport3175 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleQualifiedNameWithWildcard_in_ruleImport3196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleQualifiedNameWithWildcard_in_entryRuleQualifiedNameWithWildcard3233 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleQualifiedNameWithWildcard3244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_ruleQualifiedNameWithWildcard3291 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_ruleQualifiedNameWithWildcard3309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleQualifiedName_in_entryRuleQualifiedName3350 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleQualifiedName3361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleQualifiedName3401 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_33_in_ruleQualifiedName3420 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleQualifiedName3435 = new BitSet(new long[]{0x0000000200000002L});

}