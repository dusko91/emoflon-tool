/*
 * generated by Fujaba - CodeGen2
 */
package de.uni_kassel.fujaba.codegen.emf;
import de.uni_kassel.fujaba.codegen.classdiag.ASGElementContextToken;
import de.uni_kassel.fujaba.codegen.classdiag.ASGElementToken;
import de.uni_kassel.fujaba.codegen.classdiag.engine.ClassEngine;
import de.uni_paderborn.fujaba.metamodel.common.FCodeStyle;
import de.uni_paderborn.fujaba.metamodel.common.FElement;
import de.uni_paderborn.fujaba.metamodel.structure.FClass;
import de.uni_paderborn.fujaba.metamodel.structure.FRole;
import de.upb.tools.sdm.JavaSDM; // requires Fujaba5/libs/RuntimeTools.jar in classpath
import de.upb.tools.sdm.JavaSDMException;


public class EMFClassEngine extends ClassEngine
{


   public void addAttrToken (FClass clazz , ASGElementToken token )
   {
      boolean fujaba__Success = false;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // check object clazz is really bound
         JavaSDM.ensure ( clazz != null );
         // check object token is really bound
         JavaSDM.ensure ( token != null );
         // check link element from token to clazz
         JavaSDM.ensure (clazz.equals (token.getElement ()));

         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      if ( !( fujaba__Success ) )
      {
         return ;

      }
      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // collabStat call
         super.addAttrToken( clazz, token );
         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

   }

   public void createRoleToken (FClass clazz , ASGElementToken token , FRole otherRole )
   {
      boolean fujaba__Success = false;
      ASGElementContextToken removeYouToken = null;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // check object clazz is really bound
         JavaSDM.ensure ( clazz != null );
         // check object token is really bound
         JavaSDM.ensure ( token != null );
         // check link element from token to clazz
         JavaSDM.ensure (clazz.equals (token.getElement ()));

         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      if ( !( fujaba__Success ) )
      {
         // story pattern Successor of Successor of Successor of Successor of storypatternwiththis
         try 
         {
            fujaba__Success = false; 

            // check object otherRole is really bound
            JavaSDM.ensure ( otherRole != null );
            // check object token is really bound
            JavaSDM.ensure ( token != null );
            // create object removeYouToken
            removeYouToken = new ASGElementContextToken ( );

            // assign attribute removeYouToken
            removeYouToken.setContext ("removeYou");
            // create link children from removeYouToken to token
            removeYouToken.setParent (token);

            // create link element from removeYouToken to otherRole
            removeYouToken.setElement (otherRole);

            fujaba__Success = true;
         }
         catch ( JavaSDMException fujaba__InternalException )
         {
            fujaba__Success = false;
         }

         return ;

      }
      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // collabStat call
         super.createRoleToken( clazz, token, otherRole );
         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

   }

   public boolean isResponsible (FElement element )
   {
      boolean fujaba__Success = false;
      Object _TmpObject = null;
      FClass clazz = null;
      FCodeStyle style = null;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         _TmpObject = element;

         // ensure correct type and really bound of object clazz
         JavaSDM.ensure ( _TmpObject instanceof FClass );
         clazz = (FClass) _TmpObject;

         // search to-one link inheritedCodeStyle from clazz to style
         style = clazz.getInheritedCodeStyle ();

         // check object style is really bound
         JavaSDM.ensure ( style != null );

         // attribute condition name == "emf"
         JavaSDM.ensure ( JavaSDM.stringCompare ((String) style.getName (), "emf") == 0 );


         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      if ( !( fujaba__Success ) )
      {
         return false;

      }
      return true;
   }

}

