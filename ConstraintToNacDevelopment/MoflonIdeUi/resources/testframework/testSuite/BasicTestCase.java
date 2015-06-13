package org.moflon.tie.testexecution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.moflon.tie.testgeneration.ModelWrapper;
import org.moflon.util.eMoflonEMFUtil;

import TGGLanguage.algorithm.ApplicationTypes;

import junit.framework.TestCase;

public class BasicTestCase extends TestCase
{

   private final File testCaseInputFile;
   private final List<File> testCaseOutputFiles;
   private final SetupSUT sutSetupHelper;
   
   private final EObject inputModel;
   private final HashSet<ModelWrapper> expectedOutputModelWrappers;
   
   private boolean serializedFailed = true;
   
   public BasicTestCase(SetupSUT sutSetupHelper, File testCaseInputFile, List<File> testCaseOutputFiles)
   {
      super(testCaseInputFile.getName());
      this.testCaseInputFile = testCaseInputFile;
      this.testCaseOutputFiles = new ArrayList<File>(testCaseOutputFiles);
      this.sutSetupHelper = sutSetupHelper;
      try
      {
         this.inputModel = eMoflonEMFUtil.loadModelWithDependencies(testCaseInputFile.getCanonicalPath(), sutSetupHelper.getHelper().getResourceSet());
         this.expectedOutputModelWrappers = new HashSet<>();
         loadOutputModels();
      } catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }
   
   private void loadOutputModels() throws IOException {
      for (File tempOutputModelFile : testCaseOutputFiles) {
         EObject o = eMoflonEMFUtil.loadModelWithDependencies(tempOutputModelFile.getCanonicalPath(), sutSetupHelper.getHelper().getResourceSet());
         ModelWrapper wrapper = new ModelWrapper(o);
         expectedOutputModelWrappers.add(wrapper);
      }
   }

   @Override
   protected void runTest() throws Throwable
   {
      ApplicationTypes direction = sutSetupHelper.determineDirection(testCaseInputFile.getName());
      EObject actualOutputModel = sutSetupHelper.performTransformation(direction, inputModel);
      boolean success = expectedOutputModelWrappers.contains(new ModelWrapper(actualOutputModel));
      if (!success && serializedFailed) {
         eMoflonEMFUtil.saveModel(actualOutputModel, testCaseInputFile.getAbsolutePath().replaceAll("\\\\","/").replace("/in/", "/FAILED/") + ".FAILED_OUTPUT.xmi");
      }
      assertTrue(success);
   }

}
