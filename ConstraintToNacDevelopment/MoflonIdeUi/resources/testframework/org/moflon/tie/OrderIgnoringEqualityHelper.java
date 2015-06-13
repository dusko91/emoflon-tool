package org.moflon.tie.testgeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

public class OrderIgnoringEqualityHelper extends EqualityHelper
{
   private static final long serialVersionUID = -5436600482877274283L;
   
   private static OrderIgnoringEqualityHelper helper;
   
   private OrderIgnoringEqualityHelper() {
   }
   
   public static OrderIgnoringEqualityHelper getInstance() {
      if (helper == null)
         helper = new OrderIgnoringEqualityHelper();
      return helper;
   }
   
   @Override
   public boolean equals(EObject eObject1, EObject eObject2)
   {
      // If the first object is null, the second object must be null.
      //
      if (eObject1 == null)
      {
         return eObject2 == null;
      }

      // We know the first object isn't null, so if the second one is, it can't be equal.
      //
      if (eObject2 == null)
      {
         return false;
      }

      // If eObject1 and eObject2 are the same instance...
      //
      if (eObject1 == eObject2)
      {
         // Match them and return true.
         //
         return true;
      }

      // If eObject1 is a proxy...
      //
      if (eObject1.eIsProxy())
      {
         // Then the other object must be a proxy with the same URI.
         //
         if (((InternalEObject) eObject1).eProxyURI().equals(((InternalEObject) eObject2).eProxyURI()))
         {
            return true;
         } else
         {
            return false;
         }
      }
      // If eObject1 isn't a proxy but eObject2 is, they can't be equal.
      //
      else if (eObject2.eIsProxy())
      {
         return false;
      }

      // If they don't have the same class, they can't be equal.
      //
      EClass eClass = eObject1.eClass();
      if (eClass != eObject2.eClass())
      {
         return false;
      }

      // Assume from now on that they match.
      //

      // Check all the values.
      //
      for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i)
      {
         // Ignore derived features.
         //
         EStructuralFeature feature = eClass.getEStructuralFeature(i);
         if (!feature.isDerived())
         {
            // CHANGE THE DEFAULT IMPL RIGHT HERE
            // only compare non-reference features (aka attributes)
            if (!(feature instanceof EReference)) {
               if (!haveEqualFeature(eObject1, eObject2, feature))
               {
                  remove(eObject1);
                  remove(eObject2);
                  return false;
               }
            }
         }
      }

      // There's no reason they aren't equal, so they are.
      //
      return true;
   }

   @Override
   public boolean equals(List<EObject> list1, List<EObject> list2)
   {
      int size = list1.size();
      if (size != list2.size())
      {
        return false;
      }

      ArrayList<EObject> l1 = new ArrayList<EObject>(list1);
      ArrayList<EObject> l2 = new ArrayList<EObject>(list2);
      
      Collections.sort(l1, listOfEObjectComparator);
      Collections.sort(l2, listOfEObjectComparator);
      
      for (int i=0; i<size; i++) {
         if (!equals(l1.get(i), l2.get(i)))
            return false;
      }

      return true;
   }
   
   private static Comparator<EObject> listOfEObjectComparator = new Comparator<EObject>() {

      @Override
      public int compare(EObject o1, EObject o2)
      {
         final String targetString1 = getCharacteristicString(o1);
         final String targetString2 = getCharacteristicString(o2);

         return targetString1.compareTo(targetString2);
      }
      
      private String getCharacteristicString(EObject object) {
         return object.toString().replaceAll(object.getClass().getName(), "").replaceAll(Integer.toHexString(object.hashCode()), "");
       }
   };
}
