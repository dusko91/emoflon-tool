/*
 * generated by Fujaba - CodeGen2
 */
package de.uni_kassel.fujaba.codegen.emf.writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import de.uni_kassel.features.ReferenceHandler; // requires Fujaba5/libs/features.jar in classpath
import de.uni_kassel.features.annotation.util.Property; // requires Fujaba5/libs/features.jar in classpath
import de.uni_kassel.fujaba.codegen.engine.Information;
import de.uni_paderborn.fujaba.metamodel.common.FCodeStyle;
import de.uni_paderborn.fujaba.metamodel.common.FProject;
import de.uni_paderborn.fujaba.metamodel.common.FStereotype;
import de.uni_paderborn.fujaba.metamodel.structure.FClass;
import de.uni_paderborn.fujaba.metamodel.structure.FPackage;
import de.uni_paderborn.fujaba.uml.common.UMLProject;
import de.upb.tools.fca.FEmptyIterator;
import de.upb.tools.fca.FHashSet; // requires Fujaba5/libs/RuntimeTools.jar in classpath
import de.upb.tools.sdm.JavaSDM; // requires Fujaba5/libs/RuntimeTools.jar in classpath
import de.upb.tools.sdm.JavaSDMException;


public class RootPackageInformation extends Information
{


   public void calcRootPackages (FProject project )
   {
      boolean fujaba__Success = false;
      Object _TmpObject = null;
      UMLProject umlProject = null;
      FCodeStyle style = null;
      Iterator fujaba__IterRootToSubPackage = null;
      FPackage subPackage = null;
      FPackage root = null;
      FPackage additionalPackage = null;

      // story pattern successor
      try 
      {
         fujaba__Success = false; 

         _TmpObject = project;

         // ensure correct type and really bound of object umlProject
         JavaSDM.ensure ( _TmpObject instanceof UMLProject );
         umlProject = (UMLProject) _TmpObject;

         // search to-one link rootPackage from umlProject to root
         root = umlProject.getRootPackage ();

         // check object root is really bound
         JavaSDM.ensure ( root != null );

         // iterate to-many link $link.InstanceOf.Name from root to subPackage
         fujaba__Success = false;
         fujaba__IterRootToSubPackage = new de.uni_kassel.sdm.Path (root, "packages*");
         while ( fujaba__IterRootToSubPackage.hasNext () )
         {
            try
            {
               _TmpObject =  fujaba__IterRootToSubPackage.next ();

               // ensure correct type and really bound of object subPackage
               JavaSDM.ensure ( _TmpObject instanceof FPackage );
               subPackage = (FPackage) _TmpObject;

               // check isomorphic binding between objects subPackage and root
               JavaSDM.ensure ( !subPackage.equals (root) );

               // search to-one link codeStyle from subPackage to style
               style = subPackage.getCodeStyle ();

               // check object style is really bound
               JavaSDM.ensure ( style != null );

               // attribute condition name == "emf"
               JavaSDM.ensure ( JavaSDM.stringCompare ((String) style.getName (), "emf") == 0 );

               // create link root packages from this to subPackage
               this.addToRootPackages (subPackage);



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

      // story pattern storypatternwiththis
      try 
      {
         fujaba__Success = false; 

         additionalPackage = parseForRootPackage (root);

         // check object additionalPackage is really bound
         JavaSDM.ensure ( additionalPackage != null );
         // create link root packages from this to additionalPackage
         this.addToRootPackages (additionalPackage);

         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

      return ;
   }

   protected FPackage parseForRootPackage (FPackage p )
   {
      boolean fujaba__Success = false;
      FCodeStyle style = null;
      Iterator fujaba__IterDeclaredClassToStereotype = null;
      FStereotype stereotype = null;
      Iterator fujaba__IterPToDeclaredClass = null;
      FClass declaredClass = null;
      FPackage currentCandidate = null;
      FPackage rootCandidate = null;
      Iterator fujaba__IterPToDeclaredPackage = null;
      FPackage declaredPackage = null;

      // story pattern storypatternwiththis
      try 
      {
         fujaba__Success = false; 

         // check object p is really bound
         JavaSDM.ensure ( p != null );
         // iterate to-many link declaredInPackage from p to declaredClass
         fujaba__Success = false;
         fujaba__IterPToDeclaredClass = p.iteratorOfDeclares ();

         while ( !(fujaba__Success) && fujaba__IterPToDeclaredClass.hasNext () )
         {
            try
            {
               declaredClass = (FClass) fujaba__IterPToDeclaredClass.next ();

               // check object declaredClass is really bound
               JavaSDM.ensure ( declaredClass != null );
               // search to-one link inheritedCodeStyle from declaredClass to style
               style = declaredClass.getInheritedCodeStyle ();

               // check object style is really bound
               JavaSDM.ensure ( style != null );

               // attribute condition name == "emf"
               JavaSDM.ensure ( JavaSDM.stringCompare ((String) style.getName (), "emf") == 0 );


               // check negative bindings
               try
               {
                  fujaba__Success = false;

                  // iterate to-many link stereotypes from declaredClass to stereotype
                  fujaba__Success = false;
                  fujaba__IterDeclaredClassToStereotype = declaredClass.iteratorOfStereotypes ();

                  while ( !(fujaba__Success) && fujaba__IterDeclaredClassToStereotype.hasNext () )
                  {
                     try
                     {
                        stereotype = (FStereotype) fujaba__IterDeclaredClassToStereotype.next ();

                        // check object stereotype is really bound
                        JavaSDM.ensure ( stereotype != null );
                        // attribute condition name == FStereotype.REFERENCE
                        JavaSDM.ensure ( JavaSDM.stringCompare ((String) stereotype.getName (), FStereotype.REFERENCE) == 0 );


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

               fujaba__Success = !(fujaba__Success);

               JavaSDM.ensure ( fujaba__Success );


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
         return p;

      }
      // story pattern storypatternwiththis
      try 
      {
         fujaba__Success = false; 

         // check object p is really bound
         JavaSDM.ensure ( p != null );
         // iterate to-many link packages from p to declaredPackage
         fujaba__Success = false;
         fujaba__IterPToDeclaredPackage = p.iteratorOfPackages ();

         while ( fujaba__IterPToDeclaredPackage.hasNext () )
         {
            try
            {
               declaredPackage = (FPackage) fujaba__IterPToDeclaredPackage.next ();

               // check object declaredPackage is really bound
               JavaSDM.ensure ( declaredPackage != null );
               // check isomorphic binding between objects p and declaredPackage
               JavaSDM.ensure ( !p.equals (declaredPackage) );

               // check link root packages from this to declaredPackage
               JavaSDM.ensure (!(this.hasInRootPackages (declaredPackage)));

               // story pattern successor
               try 
               {
                  fujaba__Success = false; 

                  currentCandidate = parseForRootPackage(declaredPackage);

                  // check object currentCandidate is really bound
                  JavaSDM.ensure ( currentCandidate != null );
                  fujaba__Success = true;
               }
               catch ( JavaSDMException fujaba__InternalException )
               {
                  fujaba__Success = false;
               }

               if ( fujaba__Success )
               {
                  // story pattern Successor of successor
                  try 
                  {
                     fujaba__Success = false; 

                     // check object currentCandidate is really bound
                     JavaSDM.ensure ( currentCandidate != null );
                     // check object rootCandidate is really bound
                     JavaSDM.ensure ( rootCandidate != null );
                     // check isomorphic binding between objects rootCandidate and currentCandidate
                     JavaSDM.ensure ( !rootCandidate.equals (currentCandidate) );

                     fujaba__Success = true;
                  }
                  catch ( JavaSDMException fujaba__InternalException )
                  {
                     fujaba__Success = false;
                  }

                  if ( fujaba__Success )
                  {
                     return p;

                  }
                  // story pattern Successor of Successor of successor
                  try 
                  {
                     fujaba__Success = false; 

                     rootCandidate = currentCandidate;

                     // check object rootCandidate is really bound
                     JavaSDM.ensure ( rootCandidate != null );
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

      return rootCandidate;
   }

   public void reset ()
   {
      boolean fujaba__Success = false;

      // story pattern 
      try 
      {
         fujaba__Success = false; 

         // collabStat call
         this.removeAllFromRootPackages(  );
         fujaba__Success = true;
      }
      catch ( JavaSDMException fujaba__InternalException )
      {
         fujaba__Success = false;
      }

   }

   /**
    * <pre>
    *           0..1     root packages     0..n
    * RootPackageInformation ------------------------> FPackage
    *           rootPackageInformation               rootPackages
    * </pre>
    */
   public static final String PROPERTY_ROOT_PACKAGES = "rootPackages";

   @Property( name = PROPERTY_ROOT_PACKAGES, kind = ReferenceHandler.ReferenceKind.TO_MANY,
         adornment = ReferenceHandler.Adornment.NONE)
   private FHashSet<FPackage> rootPackages;

   @Property( name = PROPERTY_ROOT_PACKAGES )
   public Set<? extends FPackage> getRootPackages()
   {
      return ((this.rootPackages == null)
              ? Collections.EMPTY_SET
              : Collections.unmodifiableSet(this.rootPackages));
   }

   @Property( name = PROPERTY_ROOT_PACKAGES )
   public boolean addToRootPackages (FPackage value)
   {
      boolean changed = false;

      if (value != null)
      {
         if (this.rootPackages == null)
         {
            this.rootPackages = new FHashSet<FPackage> ();

         }
      
         changed = this.rootPackages.add (value);
      
      }
      return changed;
   }

   @Property( name = PROPERTY_ROOT_PACKAGES )
   public RootPackageInformation withRootPackages (FPackage value)
   {
      addToRootPackages (value);
      return this;
   }

   public RootPackageInformation withoutRootPackages (FPackage value)
   {
      removeFromRootPackages (value);
      return this;
   }


   public boolean removeFromRootPackages (FPackage value)
   {
      boolean changed = false;

      if ((this.rootPackages != null) && (value != null))
      {
      
         changed = this.rootPackages.remove (value);
      
      }
      return changed;
   }

   @Property( name = PROPERTY_ROOT_PACKAGES )
   public void removeAllFromRootPackages (){
      if (this.rootPackages != null && this.rootPackages.size () > 0)
      {
      
         this.rootPackages.clear();
      
      }
   }

   @Property( name = PROPERTY_ROOT_PACKAGES )
   public boolean hasInRootPackages (FPackage value)
   {
      return ((this.rootPackages != null) &&
              (value != null) &&
              this.rootPackages.contains (value));
   }

   @Property( name = PROPERTY_ROOT_PACKAGES )
   public Iterator<? extends FPackage> iteratorOfRootPackages ()
   {
      return ((this.rootPackages == null)
              ? FEmptyIterator.<FPackage>get ()
              : this.rootPackages.iterator ());
   }

   @Property( name = PROPERTY_ROOT_PACKAGES )
   public int sizeOfRootPackages ()
   {
      return ((this.rootPackages == null)
              ? 0
              : this.rootPackages.size ());
   }

   public void removeYou()
   {
      this.removeAllFromRootPackages ();
      super.removeYou ();
   }
}

