/*
 * generated by Fujaba - CodeGen2
 */
package de.uni_kassel.fujaba.codegen.dlr.writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.velocity.context.Context;

import de.uni_kassel.fujaba.codegen.CodeWriter;
import de.uni_kassel.fujaba.codegen.dlr.DLRFileToken;
import de.uni_kassel.fujaba.codegen.dlr.DLRProjectToken;
import de.uni_kassel.fujaba.codegen.dlr.DLRToken;
import de.uni_kassel.fujaba.codegen.dlr.DLRTool;
import de.uni_kassel.fujaba.codegen.dlr.ElementReference;
import de.uni_kassel.fujaba.codegen.engine.CodeWritingEngine;
import de.uni_kassel.fujaba.codegen.engine.TemplateLoader;
import de.uni_kassel.fujaba.codegen.rules.Token;
import de.uni_paderborn.fujaba.metamodel.common.FProject;
import de.upb.tools.sdm.JavaSDM; // requires Fujaba5/libs/RuntimeTools.jar in classpath
import de.upb.tools.sdm.JavaSDMException;


public abstract class DLRCodeWriter extends CodeWriter
{


   public DLRToken createDLRToken (Collection elements )
   {
      boolean fujaba__Success = false;
      Iterator fujaba__IterElementsToProject = null;
      Object _TmpObject = null;
      FProject project = null;
      DLRTool tool = null;
      Iterator fujaba__IterProjToRef = null;
      ElementReference ref = null;
      DLRProjectToken proj = null;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // check object elements is really bound
         JavaSDM.ensure ( elements != null );
         // iterate to-many link contains from elements to project
         fujaba__Success = false;
         fujaba__IterElementsToProject = elements.iterator ();

         while ( !(fujaba__Success) && fujaba__IterElementsToProject.hasNext () )
         {
            try
            {
               _TmpObject =  fujaba__IterElementsToProject.next ();

               // ensure correct type and really bound of object project
               JavaSDM.ensure ( _TmpObject instanceof FProject );
               project = (FProject) _TmpObject;


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

      // story pattern storypatternwithparams
      try 
      {
         fujaba__Success = false; 

         tool = DLRTool.get();

         // check object tool is really bound
         JavaSDM.ensure ( tool != null );
         // search to-one link current from tool to proj
         _TmpObject = tool.getToken ();

         // ensure correct type and really bound of object proj
         JavaSDM.ensure ( _TmpObject instanceof DLRProjectToken );
         proj = (DLRProjectToken) _TmpObject;


         // iterate to-many link elements from proj to ref
         fujaba__Success = false;
         fujaba__IterProjToRef = proj.iteratorOfElements ();

         while ( !(fujaba__Success) && fujaba__IterProjToRef.hasNext () )
         {
            try
            {
               ref = (ElementReference) fujaba__IterProjToRef.next ();

               // check object ref is really bound
               JavaSDM.ensure ( ref != null );
               // check optional
               if (project != null)
               {
                  // check link element from ref to project
                  JavaSDM.ensure (project.equals (ref.getElement ()));

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

      if ( fujaba__Success )
      {
         return proj.createDLRToken (elements);

      }
      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // check object project is really bound
         JavaSDM.ensure ( project != null );
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

            // check object tool is really bound
            JavaSDM.ensure ( tool != null );
            // create object proj
            proj = new DLRProjectToken ( );

            // create link current from tool to proj
            tool.setToken (proj);

            // create link projects from tool to proj
            tool.addToProjects (project, proj);

            // collabStat call
            proj.createDLRToken (proj, elements);
            fujaba__Success = true;
         }
         catch ( JavaSDMException fujaba__InternalException )
         {
            fujaba__Success = false;
         }

         return proj;

      }
      throw new IllegalStateException("No project token passed and element does not contain a project object.");
   }

   public String generateCode (Token operation )
   {
      boolean fujaba__Success = false;
      Collection elems = null;
      DLRTool tool = null;
      DLRToken token = null;
      Object _TmpObject = null;
      DLRProjectToken proj = null;
      Context context = null;
      TemplateLoader templateEngine = null;
      CodeWritingEngine engine = null;
      String code = null;
      StringBuffer sb = null;
      DLRFileToken fileToken = null;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         elems = getASGElements (operation);

         // check object elems is really bound
         JavaSDM.ensure ( elems != null );
         tool = DLRTool.get();

         // check object tool is really bound
         JavaSDM.ensure ( tool != null );
         // attribute condition includeDLR == true
         JavaSDM.ensure ( tool.isIncludeDLR () == true );

         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      if ( !( fujaba__Success ) )
      {
         return null;

      }
      // story pattern 
      try 
      {
         fujaba__Success = false; 

         token = createDLRToken (elems);

         // check object token is really bound
         JavaSDM.ensure ( token != null );
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

         // check object tool is really bound
         JavaSDM.ensure ( tool != null );
         // search to-one link current from tool to proj
         _TmpObject = tool.getToken ();

         // ensure correct type and really bound of object proj
         JavaSDM.ensure ( _TmpObject instanceof DLRProjectToken );
         proj = (DLRProjectToken) _TmpObject;



         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      // story pattern storypatternwiththis
      try 
      {
         fujaba__Success = false; 

         // search to-one link generators from this to engine
         engine = this.getEngine ();

         // check object engine is really bound
         JavaSDM.ensure ( engine != null );

         // search to-one link template loader from engine to templateEngine
         templateEngine = engine.getTemplateLoader ();

         // check object templateEngine is really bound
         JavaSDM.ensure ( templateEngine != null );

         // search to-one link context from templateEngine to context
         context = templateEngine.getContext ();

         // check object context is really bound
         JavaSDM.ensure ( context != null );




         // collabStat call
         context.put ("dlrTool", proj);
         // collabStat call
         context.put ("dlrToken", token);
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

         // check object engine is really bound
         JavaSDM.ensure ( engine != null );
         // collabStat call
         code = engine.generateCode (operation, this);
         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      if ( !( (code != null) && (code.length() > 0) ) )
      {
         return code;

      }
      // story pattern storypatternwiththis
      try 
      {
         fujaba__Success = false; 

         // check object token is really bound
         JavaSDM.ensure ( token != null );
         // create object sb
         sb = new StringBuffer ( );

         // assign attribute token
         token.setComment ((String) context.get ("opComment"));
         // collabStat call
         sb.append (token.createStartTag());
         // collabStat call
         sb.append (code);
         // collabStat call
         sb.append (token.createEndTag());
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

         // check object token is really bound
         JavaSDM.ensure ( token != null );
         // attribute condition comment == null
         JavaSDM.ensure ( JavaSDM.stringCompare ((String) token.getComment (), null) == 0 );

         // assign attribute token
         token.setComment ("Operation: " + operation.getClass().getName());
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

         _TmpObject = token;

         // ensure correct type and really bound of object fileToken
         JavaSDM.ensure ( _TmpObject instanceof DLRFileToken );
         fileToken = (DLRFileToken) _TmpObject;

         // assign attribute fileToken
         fileToken.setPath ((String) context.get ("path"));
         // assign attribute fileToken
         fileToken.setFileName ((String) context.get ("fileName"));
         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      return sb.toString();
   }

   public abstract Collection getASGElements (Token operation );

}

