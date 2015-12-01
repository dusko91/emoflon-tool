/*
 * generated by Fujaba - CodeGen2
 */
package de.uni_kassel.fujaba.codegen.rules.engine;
import java.util.Set;

import de.uni_kassel.features.ReferenceHandler; // requires Fujaba5/libs/features.jar in classpath
import de.uni_kassel.features.annotation.util.Property; // requires Fujaba5/libs/features.jar in classpath
import de.uni_kassel.fujaba.codegen.rules.ExecuteStoryPatternOperation;
import de.uni_kassel.fujaba.codegen.rules.Token;


public abstract class OperationDependencies
{


   /**
    * <pre>
    *           0..n     sequentialDependencies     0..1
    * OperationDependencies ------------------------- ExecutionPlanEngine
    *           sequentialDependencies               engine1
    * </pre>
    */
   public static final String PROPERTY_ENGINE1 = "engine1";

   @Property( name = PROPERTY_ENGINE1, partner = ExecutionPlanEngine.PROPERTY_SEQUENTIAL_DEPENDENCIES, kind = ReferenceHandler.ReferenceKind.TO_ONE,
         adornment = ReferenceHandler.Adornment.PARENT)
   private ExecutionPlanEngine engine1;

   @Property( name = PROPERTY_ENGINE1 )
   public boolean setEngine1 (ExecutionPlanEngine value)
   {
      boolean changed = false;

      if (this.engine1 != value)
      {
      
         ExecutionPlanEngine oldValue = this.engine1;
         OperationDependencies source = this;
         if (this.engine1 != null)
         {
            this.engine1 = null;
            oldValue.removeFromSequentialDependencies (this);
         }
         this.engine1 = value;

         if (value != null)
         {
            value.addToSequentialDependencies (this);
         }
         changed = true;
      
      }
      return changed;
   }

   @Property( name = PROPERTY_ENGINE1 )
   public OperationDependencies withEngine1 (ExecutionPlanEngine value)
   {
      setEngine1 (value);
      return this;
   }

   public ExecutionPlanEngine getEngine1 ()
   {
      return this.engine1;
   }

   /**
    * <pre>
    *           0..n     nestingDependencies     0..1
    * OperationDependencies ------------------------- ExecutionPlanEngine
    *           nestingDependencies               engine
    * </pre>
    */
   public static final String PROPERTY_ENGINE = "engine";

   @Property( name = PROPERTY_ENGINE, partner = ExecutionPlanEngine.PROPERTY_NESTING_DEPENDENCIES, kind = ReferenceHandler.ReferenceKind.TO_ONE,
         adornment = ReferenceHandler.Adornment.PARENT)
   private ExecutionPlanEngine engine;

   @Property( name = PROPERTY_ENGINE )
   public boolean setEngine (ExecutionPlanEngine value)
   {
      boolean changed = false;

      if (this.engine != value)
      {
      
         ExecutionPlanEngine oldValue = this.engine;
         OperationDependencies source = this;
         if (this.engine != null)
         {
            this.engine = null;
            oldValue.removeFromNestingDependencies (this);
         }
         this.engine = value;

         if (value != null)
         {
            value.addToNestingDependencies (this);
         }
         changed = true;
      
      }
      return changed;
   }

   @Property( name = PROPERTY_ENGINE )
   public OperationDependencies withEngine (ExecutionPlanEngine value)
   {
      setEngine (value);
      return this;
   }

   public ExecutionPlanEngine getEngine ()
   {
      return this.engine;
   }

   public abstract void getDependencies (ExecuteStoryPatternOperation plan , Token operation , Set dependencies );

   public void removeYou()
   {
      this.setEngine1 (null);
      this.setEngine (null);
   }
}

