/*
 * generated by Fujaba - CodeGen2
 */
package de.uni_kassel.fujaba.codegen.statechart.engine;
import java.util.Iterator;

import de.uni_kassel.features.ReferenceHandler; // requires Fujaba5/libs/features.jar in classpath
import de.uni_kassel.features.annotation.util.Property; // requires Fujaba5/libs/features.jar in classpath
import de.uni_kassel.fujaba.codegen.classdiag.ASGElementContextToken;
import de.uni_kassel.fujaba.codegen.classdiag.ASGElementToken;
import de.uni_kassel.fujaba.codegen.engine.TokenCreationEngine;
import de.uni_kassel.fujaba.codegen.rules.Token;
import de.uni_paderborn.fujaba.metamodel.common.FElement;
import de.uni_paderborn.fujaba.uml.behavior.UMLActivity;
import de.uni_paderborn.fujaba.uml.behavior.UMLActivityDiagram;
import de.uni_paderborn.fujaba.uml.behavior.UMLComplexState;
import de.uni_paderborn.fujaba.uml.behavior.UMLStartActivity;
import de.uni_paderborn.fujaba.uml.behavior.UMLStatechart;
import de.uni_paderborn.fujaba.uml.behavior.UMLStoryActivity;
import de.uni_paderborn.fujaba.uml.behavior.UMLStoryPattern;
import de.uni_paderborn.fujaba.uml.behavior.UMLTransition;
import de.upb.tools.sdm.JavaSDM; // requires Fujaba5/libs/RuntimeTools.jar in classpath
import de.upb.tools.sdm.JavaSDMException;


public class StatechartEngine extends TokenCreationEngine
{


   public Token createToken (FElement element )
   {
      boolean fujaba__Success = false;
      Object _TmpObject = null;
      UMLStatechart statechart = null;
      UMLComplexState state = null;
      ASGElementToken token = null;
      ASGElementToken itemToken = null;
      ASGElementContextToken methodToken = null;
      Iterator fujaba__IterStateToInner = null;
      UMLStatechart inner = null;
      UMLStoryPattern pattern = null;
      UMLStoryActivity story = null;
      UMLActivity stmt = null;
      ASGElementToken stmtToken = null;
      Iterator fujaba__IterStatechartToState = null;
      UMLStartActivity start = null;
      ASGElementContextToken transToken = null;
      Iterator fujaba__IterStatechartToTrans = null;
      UMLTransition trans = null;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         _TmpObject = element;

         // ensure correct type and really bound of object statechart
         JavaSDM.ensure ( _TmpObject instanceof UMLStatechart );
         statechart = (UMLStatechart) _TmpObject;

         // search to-one link contains from statechart to state
         state = statechart.getRevContains ();

         // check object state is really bound
         JavaSDM.ensure ( state != null );


         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      if ( fujaba__Success )
      {
         // story pattern 
         try 
         {
            fujaba__Success = false; 

            // search to-one link root from this to token
            token = this.getRoot ();

            // check object token is really bound
            JavaSDM.ensure ( token != null );


            fujaba__Success = true;
         }
         catch ( JavaSDMException fujaba__InternalException )
         {
            fujaba__Success = false;
         }


      }
      else
      {
         // story pattern successor
         try 
         {
            fujaba__Success = false; 

            // check object statechart is really bound
            JavaSDM.ensure ( statechart != null );
            // create object token
            token = new ASGElementToken ( );

            // create link element from token to statechart
            token.setElement (statechart);

            // create link root from this to token
            this.setRoot (token);

            fujaba__Success = true;
         }
         catch ( JavaSDMException fujaba__InternalException )
         {
            fujaba__Success = false;
         }


      }
      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // check object statechart is really bound
         JavaSDM.ensure ( statechart != null );
         // check object token is really bound
         JavaSDM.ensure ( token != null );
         // iterate to-many link elements from statechart to state
         fujaba__Success = false;
         fujaba__IterStatechartToState = statechart.iteratorOfElements ();

         while ( fujaba__IterStatechartToState.hasNext () )
         {
            try
            {
               _TmpObject =  fujaba__IterStatechartToState.next ();

               // ensure correct type and really bound of object state
               JavaSDM.ensure ( _TmpObject instanceof UMLComplexState );
               state = (UMLComplexState) _TmpObject;

               // create object itemToken
               itemToken = new ASGElementToken ( );

               // create object methodToken
               methodToken = new ASGElementContextToken ( );

               // assign attribute methodToken
               methodToken.setContext ("methods");
               // create link children from token to itemToken
               token.addToChildren (itemToken);

               // create link element from itemToken to state
               itemToken.setElement (state);

               // create link element from methodToken to state
               methodToken.setElement (state);

               // create link children from token to methodToken
               token.addToChildren (methodToken);

               // story pattern successor
               try 
               {
                  fujaba__Success = false; 

                  // check object state is really bound
                  JavaSDM.ensure ( state != null );
                  // iterate to-many link contains from state to inner
                  fujaba__Success = false;
                  fujaba__IterStateToInner = state.iteratorOfContains ();

                  while ( fujaba__IterStateToInner.hasNext () )
                  {
                     try
                     {
                        _TmpObject =  fujaba__IterStateToInner.next ();

                        // ensure correct type and really bound of object inner
                        JavaSDM.ensure ( _TmpObject instanceof UMLStatechart );
                        inner = (UMLStatechart) _TmpObject;

                        // collabStat call
                        this.createToken (inner, itemToken);

                        fujaba__Success = true;
                     }
                     catch ( JavaSDMException fujaba__InternalException )
                     {
                        fujaba__Success = false;
                     }
                  }
                  JavaSDM.ensure (fujaba__Success);
                  fujaba__Success = true;
               }
               catch ( JavaSDMException fujaba__InternalException )
               {
                  fujaba__Success = false;
               }

               // story pattern successor
               try 
               {
                  fujaba__Success = false; 

                  // check object state is really bound
                  JavaSDM.ensure ( state != null );
                  // search to-one link activity from state to story
                  _TmpObject = state.getActivity ();

                  // ensure correct type and really bound of object story
                  JavaSDM.ensure ( _TmpObject instanceof UMLStoryActivity );
                  story = (UMLStoryActivity) _TmpObject;


                  // search to-one link storyPattern from story to pattern
                  pattern = story.getStoryPattern ();

                  // check object pattern is really bound
                  JavaSDM.ensure ( pattern != null );



                  fujaba__Success = true;
               }
               catch ( JavaSDMException fujaba__InternalException )
               {
                  fujaba__Success = false;
               }

               if ( fujaba__Success )
               {
                  // story pattern successor
                  try 
                  {
                     fujaba__Success = false; 

                     // collabStat call
                     this.createToken (pattern, methodToken);
                     fujaba__Success = true;
                  }
                  catch ( JavaSDMException fujaba__InternalException )
                  {
                     fujaba__Success = false;
                  }


               }
               else
               {
                  // story pattern 
                  try 
                  {
                     fujaba__Success = false; 

                     // check object methodToken is really bound
                     JavaSDM.ensure ( methodToken != null );
                     // check object state is really bound
                     JavaSDM.ensure ( state != null );
                     // search to-one link activity from state to stmt
                     stmt = state.getActivity ();

                     // check object stmt is really bound
                     JavaSDM.ensure ( stmt != null );

                     // check isomorphic binding between objects stmt and state
                     JavaSDM.ensure ( !stmt.equals (state) );


                     // create object stmtToken
                     stmtToken = new ASGElementToken ( );

                     // create link element from stmtToken to stmt
                     stmtToken.setElement (stmt);

                     // create link children from methodToken to stmtToken
                     methodToken.addToChildren (stmtToken);

                     fujaba__Success = true;
                  }
                  catch ( JavaSDMException fujaba__InternalException )
                  {
                     fujaba__Success = false;
                  }


               }

               fujaba__Success = true;
            }
            catch ( JavaSDMException fujaba__InternalException )
            {
               fujaba__Success = false;
            }
         }
         JavaSDM.ensure (fujaba__Success);
         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // check object statechart is really bound
         JavaSDM.ensure ( statechart != null );
         // check object token is really bound
         JavaSDM.ensure ( token != null );
         // iterate to-many link elements from statechart to trans
         fujaba__Success = false;
         fujaba__IterStatechartToTrans = statechart.iteratorOfElements ();

         while ( fujaba__IterStatechartToTrans.hasNext () )
         {
            try
            {
               _TmpObject =  fujaba__IterStatechartToTrans.next ();

               // ensure correct type and really bound of object trans
               JavaSDM.ensure ( _TmpObject instanceof UMLTransition );
               trans = (UMLTransition) _TmpObject;

               // check negative bindings
               try
               {
                  fujaba__Success = false;

                  // search to-one link exit from trans to start
                  _TmpObject = trans.getRevExit ();

                  // ensure correct type and really bound of object start
                  JavaSDM.ensure ( _TmpObject instanceof UMLStartActivity );
                  start = (UMLStartActivity) _TmpObject;




                  fujaba__Success = true;
               }
               catch ( JavaSDMException fujaba__InternalException )
               {
                  fujaba__Success = false;
               }

               fujaba__Success = !(fujaba__Success);

               JavaSDM.ensure ( fujaba__Success );

               // create object transToken
               transToken = new ASGElementContextToken ( );

               // create object methodToken
               methodToken = new ASGElementContextToken ( );

               // assign attribute methodToken
               methodToken.setContext ("methods");
               // assign attribute transToken
               transToken.setContext ("transitions");
               // create link element from transToken to trans
               transToken.setElement (trans);

               // create link element from methodToken to trans
               methodToken.setElement (trans);

               // create link children from token to methodToken
               token.addToChildren (methodToken);

               // create link children from transToken to token
               transToken.setParent (token);


               fujaba__Success = true;
            }
            catch ( JavaSDMException fujaba__InternalException )
            {
               fujaba__Success = false;
            }
         }
         JavaSDM.ensure (fujaba__Success);
         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      return token;
   }

   public boolean isResponsible (FElement element )
   {
      boolean fujaba__Success = false;
      Object _TmpObject = null;
      UMLActivityDiagram statechart = null;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         _TmpObject = element;

         // ensure correct type and really bound of object statechart
         JavaSDM.ensure ( _TmpObject instanceof UMLActivityDiagram );
         statechart = (UMLActivityDiagram) _TmpObject;

         // attribute condition statechart == true
         JavaSDM.ensure ( statechart.isStatechart () == true );

         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      if ( fujaba__Success )
      {
         return true;

      }
      return false;
   }

   /**
    * <pre>
    *           0..1     root     0..1
    * StatechartEngine ------------------------> ASGElementToken
    *           statechartEngine               root
    * </pre>
    */
   public static final String PROPERTY_ROOT = "root";

   @Property( name = PROPERTY_ROOT, kind = ReferenceHandler.ReferenceKind.TO_ONE,
         adornment = ReferenceHandler.Adornment.NONE)
   private ASGElementToken root;

   @Property( name = PROPERTY_ROOT )
   public boolean setRoot (ASGElementToken value)
   {
      boolean changed = false;

      if (this.root != value)
      {
      
         ASGElementToken oldValue = this.root;
         this.root = value;
         changed = true;
      
      }
      return changed;
   }

   @Property( name = PROPERTY_ROOT )
   public StatechartEngine withRoot (ASGElementToken value)
   {
      setRoot (value);
      return this;
   }

   public ASGElementToken getRoot ()
   {
      return this.root;
   }

   public void removeYou()
   {
      this.setRoot (null);
      super.removeYou ();
   }
}

