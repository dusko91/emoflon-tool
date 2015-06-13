package org.moflon.tie.testexecution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import junit.framework.TestSuite;

class GroupingTestSuite extends TestSuite
{
   
   private final File testDataDir;
   private final String pathToDirectory;
   private final SetupSUT sutSetupHelper;

   public GroupingTestSuite(File testDataDir)
   {
      super(testDataDir.getName() + "__" + getTimeStamp().replaceAll("\\ ", "_"));
      this.testDataDir = testDataDir;
      this.pathToDirectory = testDataDir.getPath();
      // configure SUT
      this.sutSetupHelper = new SetupSUT(); 
      sutSetupHelper.setPathToStrategy(pathToDirectory);
      
      try
      {
         createTestCases();
      } catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }
   
   private void createTestCases() throws FileNotFoundException, IOException {
      File mappingFile = new File(testDataDir.getAbsolutePath() + File.separator + "grouping.map");
      if (!mappingFile.exists())
         throw new FileNotFoundException("Could not determine mapping file \"grouping.map\".");
      
      BufferedReader br = new BufferedReader(new FileReader(mappingFile));
      try {
         String line;
         System.out.println("Creating test cases ");
         int x = 0;
         while ((line = br.readLine()) != null)
         {
            final String leftSide = line.substring(0, line.indexOf("=")).replaceAll("\"", "");
            final File inputModelFile = new File(leftSide);
            
            List<File> outputModelFiles = new LinkedList<>();
            String rightSide = line.substring(line.indexOf("[")+1, line.length()-1);         
            for (StringTokenizer t = new StringTokenizer(rightSide, ";"); t.hasMoreTokens(); ) {
               String token = t.nextToken();
               token = token.replaceAll("\"", "");
               File tempOutputModelFile = new File(token);
               outputModelFiles.add(tempOutputModelFile);
            }
            
            // create new TestCase
            this.addTest(new BasicTestCase(sutSetupHelper, inputModelFile, outputModelFiles));            
            System.out.print(".");
            System.out.flush();
            x++;
            if (x > 80) {
               System.out.print(System.getProperty("line.separator"));
               System.out.flush();
               x = 0;
            }
         }
         System.out.println(" done!");
      } catch (IOException e) {
         throw e;
      } finally {
         br.close();
      }
   }
   
   private static String getTimeStamp() {
      final Date now = Calendar.getInstance().getTime();
      final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY);   
      return df.format(now);
   }

}
