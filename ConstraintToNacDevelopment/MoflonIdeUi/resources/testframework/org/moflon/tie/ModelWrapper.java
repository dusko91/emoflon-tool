package org.moflon.tie.testgeneration;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.moflon.util.EmfCompareUtil;

public class ModelWrapper
{

   final EObject modelElement;

   final int hashCode;

   public ModelWrapper(EObject rootModelElement)
   {
      this.modelElement = rootModelElement;
      this.hashCode = calculateHashCode();
   }

   private int calculateHashCode()
   {
      return (997 * countContainedElements()) ^ (991 * calculateHashValueForAttributes());
   }

   private int countContainedElements()
   {
      int result = 0;
      for (Iterator<EObject> i = modelElement.eAllContents(); i.hasNext(); i.next())
         result++;
      return result;
   }

   private int calculateHashValueForAttributes()
   {
      int result = modelElement.eClass().getClassifierID();
      for (EAttribute attr : modelElement.eClass().getEAllAttributes())
      {
         // equal strings/ints/instances of other primitive types will result in equal hashes
         // e.g. (forall s1, s2: String | (s1.equals(s2) == true) => (s1.hashCode() == s2.hashCode()))
         Object attrValue = modelElement.eGet(attr);
         if (attrValue != null)
            result = result ^ attrValue.hashCode();
      }
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (super.equals(obj))
         return true;
      if (obj.getClass() != ModelWrapper.class)
         return false;
      ModelWrapper casted = (ModelWrapper) obj;      
      EObject eObj = casted.modelElement;      
      EqualityHelper helper = OrderIgnoringEqualityHelper.getInstance();
      if (!helper.equals(modelElement, eObj))
         return false;
      
      boolean result = false;
      try {
         result = (EmfCompareUtil.compareAndFilter(modelElement, eObj, true).size() == 0);
      } catch (InterruptedException | NullPointerException e) {
         throw new RuntimeException(e);
      }
      
      return result;
   }

   @Override
   public int hashCode()
   {
      return hashCode;
   }

}
