/*
 * generated by Fujaba - CodeGen2
 */
package de.uni_kassel.fujaba.codegen.rules;
import de.uni_kassel.features.ReferenceHandler; // requires Fujaba5/libs/features.jar in classpath
import de.uni_kassel.features.annotation.util.Property; // requires Fujaba5/libs/features.jar in classpath
import de.uni_paderborn.fujaba.metamodel.common.FElement;


public class CheckConstraintOperation extends Operation implements CheckAssertionOperation, ASGElementTokenInterface
{


   /**
    * <pre>
    *           0..1     element     0..1
    * CheckConstraintOperation ------------------------> FElement
    *           checkConstraintOperation               element
    * </pre>
    */
   public static final String PROPERTY_ELEMENT = "element";

   @Property( name = PROPERTY_ELEMENT, kind = ReferenceHandler.ReferenceKind.TO_ONE,
         adornment = ReferenceHandler.Adornment.NONE)
   private FElement element;

   @Property( name = PROPERTY_ELEMENT )
   public boolean setElement (FElement value)
   {
      boolean changed = false;

      if (this.element != value)
      {
      
         FElement oldValue = this.element;
         this.element = value;
         changed = true;
      
      }
      return changed;
   }

   @Property( name = PROPERTY_ELEMENT )
   public CheckConstraintOperation withElement (FElement value)
   {
      setElement (value);
      return this;
   }

   public FElement getElement ()
   {
      return this.element;
   }

   public void removeYou()
   {
      this.setElement (null);
      super.removeYou ();
   }
}

