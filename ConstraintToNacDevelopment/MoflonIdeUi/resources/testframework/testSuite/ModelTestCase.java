package org.moflon.tie.testexecution;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.moflon.util.EmfCompareUtil;
import org.moflon.util.eMoflonEMFUtil;

import TGGLanguage.algorithm.ApplicationTypes;
import junit.framework.Assert;
import junit.framework.TestCase;

public class ModelTestCase extends TestCase
{

   private File testCaseInputFile;

   private ModelTestSuite containingTestSuite;

   private SetupSUT strategySpecificSetupSUT;

   private EObject linkToInputModel;

   private EObject inputModel;

   private EObject expectedOutputModel;

   public ModelTestCase(File testCaseInputFile, ModelTestSuite modelTestSuite)
   {
      super();
      this.containingTestSuite = modelTestSuite;
      this.testCaseInputFile = testCaseInputFile;

      setName(testCaseInputFile.getName());

      strategySpecificSetupSUT = containingTestSuite.getSetupSUT();

      inputModel = eMoflonEMFUtil.loadModelWithDependencies(testCaseInputFile.getPath(), strategySpecificSetupSUT.getHelper().getResourceSet());
      File outputFile = new File(strategySpecificSetupSUT.getExpectedPath() + testCaseInputFile.getName());
      expectedOutputModel = eMoflonEMFUtil.loadModelWithDependencies(outputFile.getPath(), strategySpecificSetupSUT.getHelper().getResourceSet());
      // necessary to resolve all known valid results for the transformation of the input model. these known valid
      // results are administrated by the associated TestSuite
      linkToInputModel = containingTestSuite.registerModel(inputModel, expectedOutputModel);
   }

   protected void runTest() throws Throwable
   {
      String testCaseNameWithoutFiletype = testCaseInputFile.getName().substring(0, testCaseInputFile.getName().length() - 4);

      ApplicationTypes direction = strategySpecificSetupSUT.determineDirection(testCaseNameWithoutFiletype);

      // the transformation of the SUT
      EObject created = strategySpecificSetupSUT.performTransformation(direction, inputModel);

      // save result of SUT transformation
      // String outPath = containingTestSuite.getPathToDirectory() +"//out//";
      // eMoflonEMFUtil.saveModel(created, outPath);
      // doesnt work:
      // ERROR org.moflon.util.eMoflonEMFUtil - Unable to save model to
      // file:/C:/workspace/demoWorskpace-praesentation/testProject/testmodels/strategy1/out.
      // Error:C:\workspace\demoWorskpace-praesentation\testProject\testmodels\strategy1\out (Zugriff verweigert)

      // load expected Model
      EObject expected = strategySpecificSetupSUT.loadExpected(testCaseNameWithoutFiletype, direction);

      HashMap<EObject, HashSet<EObject>> potentialResultMap = null;

      if (expected.eClass().getEPackage().equals(containingTestSuite.getSetupSUT().getSourcePackage()))
      {
         potentialResultMap = containingTestSuite.getTargetDomainPotentialResultMap();
      } else if (expected.eClass().getEPackage().equals(containingTestSuite.getSetupSUT().getTargetPackage()))
      {
         potentialResultMap = containingTestSuite.getSourceDomainPotentialResultMap();
      }

      // its important to use the 'linkToInputModel' and not the input model of the testCase, since the
      // 'linkToInputModel' is the shared key of all identical input models
      HashSet<EObject> allKnownOutputModelsToTheInputModel = potentialResultMap.get(linkToInputModel);

      int minDifferences = Integer.MAX_VALUE;

      for (EObject anyKnownOutputModel : allKnownOutputModelsToTheInputModel)
      {
         int differences = EmfCompareUtil.compareAndFilter(anyKnownOutputModel, created, true).size();
         minDifferences = (differences < minDifferences) ? differences : minDifferences;
      }

      Assert.assertEquals(0, minDifferences);
   }
}
