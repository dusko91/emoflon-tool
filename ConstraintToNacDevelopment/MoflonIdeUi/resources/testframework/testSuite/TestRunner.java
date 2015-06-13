package org.moflon.tie.testexecution;

import java.io.File;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.AllTests;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

@RunWith(AllTests.class)
public final class TestRunner extends Suite
{
   private final static String TEST_DATA_CONTAINING_SUBFOLDER = "testmodels";

   protected TestRunner(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError
   {
      super(klass, suiteClasses);
   }

   public static TestSuite suite()
   {
      // create a new TestSuite that serves as a container
      TestSuite suite = new TestSuite();
      // add the actual TestSuite to the container
      File startDirectory = new File(TEST_DATA_CONTAINING_SUBFOLDER);
      if (!startDirectory.exists())
         throw new IllegalStateException();
      suite.addTest(new GroupingTestSuite(startDirectory));
      return suite;
   }
}