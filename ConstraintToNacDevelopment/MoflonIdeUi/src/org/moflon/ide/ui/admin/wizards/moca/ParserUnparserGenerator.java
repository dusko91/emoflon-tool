package org.moflon.ide.ui.admin.wizards.moca;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.moflon.Activator;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.ui.UIActivator;
import org.moflon.moca.AbstractFileGenerator;
import org.moflon.moca.BasicFormatRenderer;
import org.moflon.util.WorkspaceHelper;

public class ParserUnparserGenerator extends AbstractFileGenerator
{
   private static Logger logger = Logger.getLogger(UIActivator.class);

   private AddParserAndUnparserWizardPage page;

   private String packagePrefix;

   private StringTemplateGroup stg;

   private IFolder parserFolder;

   private IFolder unparserFolder;

   public ParserUnparserGenerator(AddParserAndUnparserWizardPage page, IProject project)
   {
      super(project);
      this.page = page;
      this.packagePrefix = "org.moflon.moca." + page.getFileExtension();
   }

   @Override
   protected void prepareCodegen()
   {
      // Ensure Moca is on classpath
      WorkspaceHelper.setContainerOnBuildPath(project, WorkspaceHelper.MOCA_CONTAINER);

      // Add EMF dependencies if necessary
      WorkspaceHelper.addEMFDependenciesToClassPath(monitor, project);

      // Add Moflon dependencies
      WorkspaceHelper.setContainerOnBuildPath(project, WorkspaceHelper.MOFLON_CONTAINER);

      // Ensure packages for Parser and Unparser exist
      List<String> packageFolders = Arrays.asList(packagePrefix.split("\\."));
      String currentFolder = "src/";
      for (String folder : packageFolders)
      {
         currentFolder += folder + "/";
         createFolder(currentFolder);
      }
      if (page.createParser())
      {
         parserFolder = createFolder(currentFolder + "/parser");
      }
      if (page.createUnparser())
      {
         unparserFolder = createFolder(currentFolder + "/unparser");
      }

      createFolder("instances");
      createFolder("instances/in");

      // Add antlr nature
      try
      {
         WorkspaceHelper.addNature(project, CoreActivator.ANTLR_NATURE_ID, monitor);
      } catch (CoreException e)
      {
         logger.error(e);
      }
   }

   private Map<String, String> createParser(Technology parserTechnology) throws FileNotFoundException
   {
      // load template group and set attributes
      loadStringTemplateGroup(parserTechnology);

      // prepare attribute map (same for all technologies):
      Map<String, Object> attributes = new HashMap<String, Object>();
      attributes.put("name", page.getFileExtension());
      attributes.put("package", packagePrefix + ".parser");

      // create file name prefix for the Parser
      String fileNamePrefix = parserFolder.getProjectRelativePath() + "/" + BasicFormatRenderer.firstToUpper(page.getFileExtension());

      // generate code for the given parser technology
      Map<String, String> parserFileNamesToContents = new HashMap<String, String>();
      switch (parserTechnology)
      {
      case ANTLR:
         parserFileNamesToContents.put(fileNamePrefix + "Parser.g", renderTemplate("Parser", attributes));
         parserFileNamesToContents.put(fileNamePrefix + "Lexer.g", renderTemplate("Lexer", attributes));
         parserFileNamesToContents.put(fileNamePrefix + "ParserAdapter.java", renderTemplate("ParserAdapter", attributes));
         createTemplateFolder();
         break;
      case XML:
         parserFileNamesToContents.put(fileNamePrefix + "ParserAdapter.java", renderTemplate("ParserAdapter", attributes));
         break;
      case CUSTOM:
         parserFileNamesToContents.put(fileNamePrefix + "ParserAdapter.java", renderTemplate("ParserAdapter", attributes));
         break;
      }

      return parserFileNamesToContents;
   }

   private Map<String, String> createUnparser(Technology unparserTechnology) throws FileNotFoundException, CoreException
   {
      // load template group and set attributes
      loadStringTemplateGroup(unparserTechnology);

      // prepare attribute map (same for all technologies):
      Map<String, Object> attributes = new HashMap<String, Object>();
      attributes.put("name", page.getFileExtension());
      attributes.put("package", packagePrefix + ".unparser");

      // create file name prefix
      String fileNamePrefix = unparserFolder.getProjectRelativePath() + "/" + BasicFormatRenderer.firstToUpper(page.getFileExtension());

      Map<String, String> unparserFileNamesToContents = new HashMap<String, String>();
      // generate code for the given parser technology
      switch (unparserTechnology)
      {
      case ANTLR:
         unparserFileNamesToContents.put(fileNamePrefix + "TreeGrammar.g", renderTemplate("TreeGrammar", attributes));
         unparserFileNamesToContents.put(fileNamePrefix + "UnparserAdapter.java", renderTemplate("UnparserAdapter", attributes));
         break;
      case XML:
         unparserFileNamesToContents.put(fileNamePrefix + "UnparserAdapter.java", renderTemplate("UnparserAdapter", attributes));
         addDefaultXMLTemplate();
         break;
      case CUSTOM:
         unparserFileNamesToContents.put(fileNamePrefix + "UnparserAdapter.java", renderTemplate("UnparserAdapter", attributes));
         break;
      }
      return unparserFileNamesToContents;
   }

   private void createTemplateFolder()
   {
      // create folder "templates" if it does not exist
      IFolder templateFolder = project.getFolder(new Path("templates/"));
      if (!templateFolder.exists())
      {
         try
         {
            templateFolder.create(true, true, new NullProgressMonitor());
         } catch (CoreException e)
         {
            logger.debug("unable to create templates directory.");
            e.printStackTrace();
         }
      }
   }

   private void addDefaultXMLTemplate()
   {
      // copy the default XML template to the project
      createTemplateFolder();
      IFile templateFile = project.getFile("templates/XML.stg");
      if (!templateFile.exists())
      {
         try
         {
            URL url = Activator.getPathRelToPlugIn("resources/moca/templates/defaultTemplates/XML.stg", UIActivator.PLUGIN_ID);
            WorkspaceHelper.addFile(project, "templates/XML.stg", url, UIActivator.PLUGIN_ID, monitor);
         } catch (CoreException e)
         {
            e.printStackTrace();
         } catch (URISyntaxException e)
         {
            e.printStackTrace();
         } catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }

   private Map<String, String> createMocaMain() throws FileNotFoundException, CoreException
   {
      loadStringTemplateGroup("/resources/moca/templates/CodeAdapter/MocaMain.stg");

      Map<String, Object> attributes = new HashMap<String, Object>();
      attributes.put("name", page.getFileExtension().toLowerCase());
      String fileNamePrefix = project.getProjectRelativePath() + "/src/org/moflon/moca/";

      // determine template for MocaMain (parser, unparser or both)
      String templateName = "MocaMain";
      if (page.createParser() && page.createUnparser())
      {
         templateName += "ParserUnparser";
      } else if (page.createParser())
      {
         templateName += "Parser";
      } else
      {
         templateName += "Unparser";
      }

      Map<String, String> mocaMainFileNameToContent = new HashMap<String, String>();
      mocaMainFileNameToContent.put(fileNamePrefix + "MocaMain.java", renderTemplate(templateName, attributes));

      return mocaMainFileNameToContent;

   }

   private URL getTemplateFileURL(String path)
   {
      return Activator.getPathRelToPlugIn(path, UIActivator.PLUGIN_ID);
   }

   private void loadStringTemplateGroup(String path)
   {
      try
      {
         InputStreamReader reader = new InputStreamReader(getTemplateFileURL(path).openStream());
         stg = new StringTemplateGroup(reader);
      } catch (IOException e)
      {
         logger.debug("unable to load template file: " + getTemplateFileURL(path));
      }
   }

   private void loadStringTemplateGroup(Technology technology)
   {
      loadStringTemplateGroup("/resources/moca/templates/CodeAdapter/" + technology.toString() + ".stg");
   }

   private String renderTemplate(String templateName, Map<String, Object> attributes) throws FileNotFoundException
   {
      StringTemplate st = stg.getInstanceOf(templateName);
      st.registerRenderer(String.class, new BasicFormatRenderer());
      st.setAttributes(attributes);
      return st.toString();
   }

   private IFolder createFolder(String path)
   {
      IFolder folder = project.getFolder(path);
      if (!folder.exists())
      {
         try
         {
            return WorkspaceHelper.addFolder(project, path, monitor);
         } catch (CoreException e)
         {
            logger.debug("error while creating folder: " + path);
            e.printStackTrace();
            return null;
         }
      } else
      {
         return folder;
      }
   }

   @Override
   protected Map<String, String> extractFileNamesToContents() throws CoreException
   {
      HashMap<String, String> fileNamesToContents = new HashMap<String, String>();

      try
      {
         if (page.createParser())
         {
            Map<String, String> parserFileNamesToContents = createParser(page.getParserTechnology());
            fileNamesToContents.putAll(parserFileNamesToContents);

         }
         if (page.createUnparser())
         {
            Map<String, String> unparserFileNamesToContents = createUnparser(page.getUnparserTechnology());
            fileNamesToContents.putAll(unparserFileNamesToContents);
            createUnparser(page.getUnparserTechnology());
         }
         
         Map<String, String> mocaMainFileNameToContent = createMocaMain();
         fileNamesToContents.putAll(mocaMainFileNameToContent);
      } catch (FileNotFoundException e)
      {
         Activator.throwCoreExceptionAsError(e.getMessage(), UIActivator.PLUGIN_ID, e);
      }

      return fileNamesToContents;
   }

}
