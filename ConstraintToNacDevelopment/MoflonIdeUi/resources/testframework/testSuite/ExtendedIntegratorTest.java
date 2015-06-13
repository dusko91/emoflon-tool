package org.moflon.tie.testexecution;

import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.moflon.testframework.tgg.IntegratorTest;
import org.moflon.util.eMoflonEMFUtil;

import TGGLanguage.algorithm.ApplicationTypes;

public class ExtendedIntegratorTest extends IntegratorTest
{

   protected String pathToStrategy;

   public ExtendedIntegratorTest(EPackage sourcePackage, EPackage corrPackage, EPackage targetPackage)
   {
      super(sourcePackage, corrPackage, targetPackage);
   }

   public void setPathToStrategy(String path)
   {
      pathToStrategy = path;
   }

   @Override
   public String getExpectedPath()
   {
      return pathToStrategy + File.separator + "expected" + File.separator;
   }

   @Override
   public String getFullInpath()
   {
      return pathToStrategy + File.separator + "in" + File.separator;
   }

   @Override
   public String getFullOutpath()
   {
      return outPath + File.separator + "out" + File.separator;
   }

   // makes the method accessible for the TestCase Class
   public ApplicationTypes determineDirection(String testCaseName)
   {
      return super.determineDirection(testCaseName);
   }

   // makes the method accessible for the TestCase Class and uses the redefined Path for the Model in the file-system
   public EObject loadExpected(String testCaseName, ApplicationTypes direction)
   {
      try
      {
         return eMoflonEMFUtil.loadAndInitModelWithDependencies(getOutputPackage(direction), getExpectedPath() + testCaseName + ".xmi", null);
      } catch (Exception e)
      {
         return null;
      }

   }
}
