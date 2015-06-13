package org.moflon.tie.testexecution;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.moflon.util.EmfCompareUtil;

import junit.framework.Assert;
import junit.framework.TestSuite;

public class ModelTestSuite extends TestSuite
{

   private HashMap<EObject, HashSet<EObject>> sourceDomainPotentialResultMap;

   private HashMap<EObject, HashSet<EObject>> targetDomainPotentialResultMap;

   private SetupSUT setupSUT;

   private String pathToDirectory;

   public ModelTestSuite(File testDataDir)
   {
      super(testDataDir.getName());

      pathToDirectory = testDataDir.getPath();

      sourceDomainPotentialResultMap = new HashMap<EObject, HashSet<EObject>>();
      targetDomainPotentialResultMap = new HashMap<EObject, HashSet<EObject>>();

      // necessary to resolve source, correspondence and target models
      setupSUT = new SetupSUT();
      setupSUT.setPathToStrategy(testDataDir.getPath());

      iterativeTestCaseGeneration(testDataDir);
   }

   private void iterativeTestCaseGeneration(File directory)
   {
      for (File file : directory.listFiles())
      {
         // adds TestSuits per Strategy directory
         if (file.isDirectory() && !file.getName().equals("in"))
         {
            if (!file.getName().equals("ecore") && !file.getName().equals("expected") && !file.getName().equals("log") && !file.getName().equals("out"))
            {
               addTest(new ModelTestSuite(file));
            }
         }
         // adds TestCases based on the files in the "in"-directory
         else if (file.getName().equals("in"))
         {
            for (File xmiFile : file.listFiles())
            {
               addTest(new ModelTestCase(xmiFile, this));
            }
         }
      }
   }

   @Test
   public void simpleTest()
   {
      Assert.assertTrue(true);
   }

   public EObject registerModel(EObject inputModel, EObject outputModel)
   {
      EObject inputModelKey = registerModelInternal(inputModel, outputModel);
      return inputModelKey;
   }

   private EObject registerModelInternal(EObject inputModel, EObject outputModel)
   {
      HashMap<EObject, HashSet<EObject>> potentialResultMap = null;
      if (inputModel.eClass().getEPackage().equals(setupSUT.getSourcePackage()))
      {
         potentialResultMap = sourceDomainPotentialResultMap;
      } else if (inputModel.eClass().getEPackage().equals(setupSUT.getTargetPackage()))
      {
         potentialResultMap = targetDomainPotentialResultMap;
      }
      if (potentialResultMap != null)
      {
         for (EObject keyModel : potentialResultMap.keySet())
         {
            try
            {
               int diffSize = EmfCompareUtil.compareAndFilter(keyModel, inputModel, true).size();
               // inputModel already exist as keyModel
               if (diffSize == 0)
               {
                  // add the expectedModel to the existing keyModel
                  potentialResultMap.get(keyModel).add(outputModel);
                  return keyModel;
               }
            } catch (InterruptedException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         // non of the existing keyModels equals to the new tested inputModel
         potentialResultMap.put(inputModel, new HashSet<EObject>());

         // add the expectedModel to the added inputModel
         potentialResultMap.get(inputModel).add(outputModel);
      }
      return inputModel;
   }

   public SetupSUT getSetupSUT()
   {
      return setupSUT;
   }

   public String getPathToDirectory()
   {
      return pathToDirectory;
   }

   public HashMap<EObject, HashSet<EObject>> getSourceDomainPotentialResultMap()
   {
      return sourceDomainPotentialResultMap;
   }

   public HashMap<EObject, HashSet<EObject>> getTargetDomainPotentialResultMap()
   {
      return targetDomainPotentialResultMap;
   }

}
