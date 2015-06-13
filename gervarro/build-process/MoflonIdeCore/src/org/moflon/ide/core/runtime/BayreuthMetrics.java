package org.moflon.ide.core.runtime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.moflon.util.WorkspaceHelper;

import de.fujaba.preferences.StandaloneProjectPreferenceStoreBuilder;
import de.fujaba.preferences.WorkspacePreferenceStore;
import de.uni_paderborn.fujaba.app.SimpleFujabaPersistencySupport;
import de.uni_paderborn.fujaba.metamodel.common.FElement;
import de.uni_paderborn.fujaba.metamodel.common.FStereotype;
import de.uni_paderborn.fujaba.metamodel.factories.FFactory;
import de.uni_paderborn.fujaba.preferences.FujabaPreferencesManager;
import de.uni_paderborn.fujaba.project.ProjectLoader;
import de.uni_paderborn.fujaba.project.ProjectManager;
import de.uni_paderborn.fujaba.uml.behavior.UMLActivity;
import de.uni_paderborn.fujaba.uml.behavior.UMLActivityDiagram;
import de.uni_paderborn.fujaba.uml.behavior.UMLCollabStat;
import de.uni_paderborn.fujaba.uml.behavior.UMLLink;
import de.uni_paderborn.fujaba.uml.behavior.UMLObject;
import de.uni_paderborn.fujaba.uml.behavior.UMLPath;
import de.uni_paderborn.fujaba.uml.behavior.UMLStatement;
import de.uni_paderborn.fujaba.uml.behavior.UMLStoryActivity;
import de.uni_paderborn.fujaba.uml.behavior.UMLTransition;
import de.uni_paderborn.fujaba.uml.common.UMLCommentary;
import de.uni_paderborn.fujaba.uml.common.UMLProject;
import de.uni_paderborn.fujaba.uml.structure.UMLAssoc;
import de.uni_paderborn.fujaba.uml.structure.UMLAttr;
import de.uni_paderborn.fujaba.uml.structure.UMLClass;
import de.uni_paderborn.fujaba.uml.structure.UMLGeneralization;
import de.uni_paderborn.fujaba.uml.structure.UMLMethod;
import de.uni_paderborn.fujaba.uml.structure.UMLPackage;
import de.uni_paderborn.fujaba.versioning.VersioningLoader;

/**
 * This class was kindly provided by Thomas Buchmann, Bernhard Westfechtel and Sabine Winetzhammer. For more information
 * regarding the applied metrics and their interpretation we refer to
 * "T. Buchmann, B. Westfechtel, S. Winetzhammer: The Added Value of Programmed Graph Transformations - A Case Study from Software Configuration"
 * .
 * 
 * @author Thomas Buchmann, Bernhard Westfechtel and Sabine Winetzhammer
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
public class BayreuthMetrics
{

   private static BayreuthMetrics instance = null;

   private static final Logger logger = Logger.getLogger(BayreuthMetrics.class);

   private static final String DEBUG_FILE_EXTENSION = "ctr";

   private UMLProject currentProject = null;

   private int numberOfPackages = 0;

   private int numberOfClasses = 0;

   private int numberOfInterfaces = 0;

   private int numberOfAbstractClasses = 0;

   private int numberOfStoryDiagrams = 0;

   private int numberOfStoryPatterns = 0;

   private int numberOfObjects = 0;

   private int numberOfPaths = 0;

   private int numberOfLinks = 0;

   private int numberOfAttributes = 0;

   private int numberOfMethods = 0;

   private int numberOfGeneralizations = 0;

   private int numberOfAssocs = 0;

   private int numberOfStatements = 0;

   private int numberOfCollabCalls = 0;

   private int numberOfForEachActivities = 0;

   private int numberOfNegativeObjects = 0;

   private int numberOfSetObjects = 0;

   private Map<String, Integer> classesPerPackage = new HashMap<String, Integer>();

   private Map<String, Integer> patternsPerStoryDiag = new HashMap<String, Integer>();

   private Map<String, Integer> cyclomaticComplexity = new HashMap<String, Integer>();

   public static BayreuthMetrics getInstance()
   {
      if (instance != null)
         return instance;
      else
      {
         instance = new BayreuthMetrics();
         return instance;
      }
   }

   public void calculateMetrics(IResource selectedResource, IProgressMonitor monitor)
   {
      IProgressMonitor subMon = new SubProgressMonitor(monitor, 9 * WorkspaceHelper.PROGRESS_SCALE);

      // Create Fujaba means to load a project
      ProjectManager manager = ProjectManager.get();
      manager.setDefaultFormat(DEBUG_FILE_EXTENSION);
      subMon.worked(1 * WorkspaceHelper.PROGRESS_SCALE);

      // Initialize core preferences
      VersioningLoader versioningLoader = new VersioningLoader(false);
      manager.registerProjectLoader(DEBUG_FILE_EXTENSION, versioningLoader);
      FujabaPreferencesManager.setPreferenceStore(FujabaPreferencesManager.FUJABA_CORE_PREFERENCES, WorkspacePreferenceStore.getInstance());
      FujabaPreferencesManager.setProjectPreferenceStoreBuilder(new StandaloneProjectPreferenceStoreBuilder());
      ProjectLoader.setPersistencySupport(new SimpleFujabaPersistencySupport());
      subMon.worked(2 * WorkspaceHelper.PROGRESS_SCALE);

      // Load project with Fujaba means
      try
      {
         currentProject = (UMLProject) manager.loadProject(selectedResource.getLocation().toFile(), null, true);
      } catch (Exception e)
      {
         logger.error("Unable to load Fujaba project file " + selectedResource.getName());
         e.printStackTrace();
      } finally
      {
         subMon.worked(3 * WorkspaceHelper.PROGRESS_SCALE);
      }
      // Call actual calculation
      if (currentProject != null)
      {
         calculate(currentProject, selectedResource.getLocation().toFile().getParent() + File.separator + "metrics");
         subMon.worked(3 * WorkspaceHelper.PROGRESS_SCALE);
      } else
         logger.warn("No project was loaded from file " + selectedResource.getName());

      monitor.done();
   }

   private void calculate(UMLProject currentProject, String persistencyPath)
   {
      classesPerPackage.clear();
      patternsPerStoryDiag.clear();
      cyclomaticComplexity.clear();
      numberOfPackages = 0;
      numberOfClasses = 0;
      numberOfInterfaces = 0;
      numberOfAbstractClasses = 0;
      numberOfStoryDiagrams = 0;
      numberOfStoryPatterns = 0;
      numberOfObjects = 0;
      numberOfPaths = 0;
      numberOfLinks = 0;
      numberOfAttributes = 0;
      numberOfMethods = 0;
      numberOfGeneralizations = 0;
      numberOfAssocs = 0;
      numberOfStatements = 0;
      numberOfCollabCalls = 0;
      numberOfForEachActivities = 0;
      numberOfNegativeObjects = 0;
      numberOfSetObjects = 0;

      removeAllComments(currentProject);
      FFactory<UMLPackage> packFactory = currentProject.getFromFactories(UMLPackage.class);
      FFactory<UMLClass> clazzFactory = currentProject.getFromFactories(UMLClass.class);
      FFactory<UMLActivityDiagram> sdFactory = currentProject.getFromFactories(UMLActivityDiagram.class);
      FFactory<UMLObject> objFactory = currentProject.getFromFactories(UMLObject.class);
      FFactory<UMLLink> linkFactory = currentProject.getFromFactories(UMLLink.class);
      FFactory<UMLPath> pathFactory = currentProject.getFromFactories(UMLPath.class);
      FFactory<UMLAttr> attrFactory = currentProject.getFromFactories(UMLAttr.class);
      FFactory<UMLMethod> methodFactory = currentProject.getFromFactories(UMLMethod.class);
      FFactory<UMLGeneralization> genFactory = currentProject.getFromFactories(UMLGeneralization.class);
      FFactory<UMLAssoc> assocFactory = currentProject.getFromFactories(UMLAssoc.class);
      FFactory<UMLStatement> statementFactory = currentProject.getFromFactories(UMLStatement.class);
      FFactory<UMLCollabStat> collabFactory = currentProject.getFromFactories(UMLCollabStat.class);
      FFactory<UMLStoryActivity> activityFactory = currentProject.getFromFactories(UMLStoryActivity.class);

      numberOfPackages = getPackages(currentProject);
      numberOfStoryDiagrams = getNumbers(sdFactory);
      numberOfObjects = getNumbers(objFactory);
      numberOfLinks = getNumbers(linkFactory);
      numberOfPaths = getNumbers(pathFactory);
      numberOfAttributes = getNumbers(attrFactory);
      numberOfMethods = getNumbers(methodFactory);
      numberOfGeneralizations = getNumbers(genFactory);
      numberOfAssocs = getNumbers(assocFactory);
      numberOfStatements = getNumbers(statementFactory);
      numberOfCollabCalls = getNumbers(collabFactory);

      Iterator it = clazzFactory.iteratorOfProducts();
      numberOfInterfaces = 0;
      numberOfClasses = 0;
      numberOfAbstractClasses = 0;
      while (it.hasNext())
      {
         UMLClass clazz = (UMLClass) it.next();
         if (clazz.hasKeyInStereotypes(FStereotype.REFERENCE))
            continue;
         if (clazz.hasKeyInStereotypes(FStereotype.INTERFACE))
            numberOfInterfaces++;
         else
            numberOfClasses++;
         if (clazz.isAbstract())
            numberOfAbstractClasses++;
      }

      it = activityFactory.iteratorOfProducts();
      numberOfStoryPatterns = 0;
      numberOfForEachActivities = 0;
      while (it.hasNext())
      {
         UMLStoryActivity act = (UMLStoryActivity) it.next();
         numberOfStoryPatterns++;
         if (act.isForEach())
            numberOfForEachActivities++;
      }

      getClassesPerPackage(currentProject);
      getPatternsPerStoryDiag(currentProject);
      getCyclomaticComplexity(currentProject);

      it = objFactory.iteratorOfProducts();
      while (it.hasNext())
      {
         UMLObject obj = (UMLObject) it.next();
         if (obj.getType() == UMLObject.NEGATIVE)
            numberOfNegativeObjects++;
         else if (obj.getType() == UMLObject.SET)
            numberOfSetObjects++;
      }

      save(currentProject.getName(), persistencyPath);
   }

   private int getNumbers(FFactory<?> factory)
   {
      Iterator it = factory.iteratorOfProducts();
      int number = 0;
      while (it.hasNext())
      {
         it.next();
         number++;
      }

      return number;
   }

   private void getClassesPerPackage(UMLProject project)
   {
      FFactory<UMLPackage> packFactory = project.getFromFactories(UMLPackage.class);

      Iterator it = packFactory.iteratorOfProducts();
      while (it.hasNext())
      {
         UMLPackage pack = (UMLPackage) it.next();

         classesPerPackage.put(pack.getFullPackageName(), pack.sizeOfDeclares());
      }
   }

   private void getPatternsPerStoryDiag(UMLProject project)
   {
      FFactory<UMLActivityDiagram> sdFactory = project.getFromFactories(UMLActivityDiagram.class);

      Iterator it = sdFactory.iteratorOfProducts();

      while (it.hasNext())
      {
         UMLActivityDiagram diag = (UMLActivityDiagram) it.next();
         Iterator iter = diag.iteratorOfElements();
         int storyPatterns = 0;
         while (iter.hasNext())
         {
            FElement elem = (FElement) iter.next();
            if (elem instanceof UMLStoryActivity)
               storyPatterns++;
         }

         patternsPerStoryDiag.put(diag.getFullName(), storyPatterns);
      }
   }

   private int getPackages(UMLProject project)
   {
      FFactory<UMLPackage> packFac = project.getFromFactories(UMLPackage.class);

      Iterator it = packFac.iteratorOfProducts();

      int counter = 0;
      while (it.hasNext())
      {
         UMLPackage current = (UMLPackage) it.next();

         // prüfen, ob alle Klassen references sind...
         boolean bRef = true;
         Iterator iter = current.iteratorOfDeclares();
         while (iter.hasNext())
         {
            UMLClass c = (UMLClass) iter.next();
            if (!c.hasKeyInStereotypes(FStereotype.REFERENCE))
               bRef = false;
         }
         if (!bRef)
            counter++;
      }
      return counter;
   }

   private void getCyclomaticComplexity(UMLProject project)
   {
      FFactory<UMLActivityDiagram> sdFactory = project.getFromFactories(UMLActivityDiagram.class);

      Iterator it = sdFactory.iteratorOfProducts();

      while (it.hasNext())
      {
         UMLActivityDiagram diag = (UMLActivityDiagram) it.next();

         // nur die Aktivitäten und Transitionen zählen...
         Iterator iter = diag.iteratorOfElements();
         int cyclomatic = 0;
         while (iter.hasNext())
         {
            FElement elem = (FElement) iter.next();

            if (elem instanceof UMLActivity || elem instanceof UMLTransition)
               cyclomatic++;
         }
         cyclomaticComplexity.put(diag.getFullName(), cyclomatic);
      }
   }

   private void save(String projectName, String path)
   {

      if (new File(path).mkdir())
         logger.info("Created new folder " + path);

      String filename = path + File.separator + projectName + ".csv";

      String headings[] = { "Number of Packages", "Total number of Classes", "Number of Interfaces", "Number of abstract Classes",
            "Total number of Attributes", "Total number of Methods", "Number of Story Diagrams", "Number of Story Patterns", "Total number of Objects",
            "Total number of Links", "Total Number of Paths", "Number of Generalizations", "Number of Associations", "Number of Statement Activities",
            "Number of Collab Calls", "Number of ForEach Activities", "Number of negative Objects", "Number of set objects" };

      Object data[] = { numberOfPackages, numberOfClasses, numberOfInterfaces, numberOfAbstractClasses, numberOfAttributes, numberOfMethods,
            numberOfStoryDiagrams, numberOfStoryPatterns, numberOfObjects, numberOfLinks, numberOfPaths, numberOfGeneralizations, numberOfAssocs,
            numberOfStatements, numberOfCollabCalls, numberOfForEachActivities, numberOfNegativeObjects, numberOfSetObjects };

      writeFile(filename, headings, data);

      filename = path + File.separator + projectName + "_classesPerPackage.csv";
      String h[] = { "Package name", "Number of Classes" };
      List<Object> list = new ArrayList<Object>();
      Iterator<Entry<String, Integer>> it = classesPerPackage.entrySet().iterator();
      while (it.hasNext())
      {
         Entry<String, Integer> e = it.next();
         list.add(e.getKey());
         list.add(e.getValue());
      }
      writeFile(filename, h, list.toArray());

      filename = path + File.separator + projectName + "_patternsPerStoryDiag.csv";
      String h2[] = { "Method name", "Number of Patterns" };
      list.clear();
      it = patternsPerStoryDiag.entrySet().iterator();
      while (it.hasNext())
      {
         Entry<String, Integer> e = it.next();
         list.add(e.getKey());
         list.add(e.getValue());
      }
      writeFile(filename, h2, list.toArray());

      filename = path + File.separator + projectName + "_cyclomatic.csv";
      String h3[] = { "Method name", "Cyclomatic Complexity" };
      list.clear();
      it = cyclomaticComplexity.entrySet().iterator();
      while (it.hasNext())
      {
         Entry<String, Integer> e = it.next();
         list.add(e.getKey());
         list.add(e.getValue());
      }
      writeFile(filename, h3, list.toArray());
   }

   private void writeFile(String filename, String[] headings, Object[] data)
   {
      File file = new File(filename);
      try
      {
         if (!file.createNewFile())
            return;

         FileOutputStream os = new FileOutputStream(file);

         String output = "These metrics are provided by Thomas Buchmann, Bernhard Westfechtel and Sabine Winetzhammer.\r\nFor more information we refer to \"T. Buchmann, B. Westfechtel, S. Winetzhammer: The Added Value of Programmed Graph Transformations - A Case Study from Software Configuration\".\r\n\r\n";
         for (int i = 0; i < headings.length; i++)
         {
            output += headings[i];
            if (i < headings.length - 1)
               output += ";";
            else if (i == headings.length - 1)
               output += "\r\n";
         }

         for (int j = 0; j < data.length; j++)
         {
            output += data[j];
            if (j != 0 && (j + 1) % headings.length == 0)
               output += "\r\n";
            else
               output += ";";
         }

         os.write(output.getBytes());
         os.flush();
         os.close();
      } catch (IOException e)
      {
         logger.error("Could not write file " + filename);
         e.printStackTrace();
      }
   }

   private void removeAllComments(UMLProject project)
   {
      FFactory<UMLCommentary> commentFactory = project.getFromFactories(UMLCommentary.class);

      Iterator it = commentFactory.iteratorOfProducts();

      while (it.hasNext())
      {
         ((UMLCommentary) it.next()).removeYou();
      }
   }

}
