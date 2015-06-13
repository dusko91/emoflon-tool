package org.moflon.ide.texteditor.builtInEditors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.moflon.ide.texteditor.editors.COLORS;
import org.moflon.ide.texteditor.editors.MoflonEditorTemplate;
import org.moflon.ide.texteditor.editors.MoflonTextEditorConfiguration;
import org.osgi.framework.Bundle;

/**
 * This class provides project specific editor configuration for text highlighting and templates. Its generated methods
 * are to be filled and invoked during runtime by MOFLON Eclipse plugin in order to achieve a custom text editor
 * functionality.
 */
public class TGGRuleTextEditorConfiguration extends MoflonTextEditorConfiguration
{      
   
   private HashMap<String, MoflonEditorTemplate> contextFreeTemplates = new HashMap<String,MoflonEditorTemplate>();
   
   private Logger logger = Logger.getLogger(TGGRuleTextEditorConfiguration.class);
   
   public TGGRuleTextEditorConfiguration(){

      Properties properties = new Properties();
      
      try
      {
         Bundle bundle = Platform.getBundle("org.moflon.ide.ui");
         URL resource = bundle.getResource("resources/moca/templates/texteditor/tggruleTemplates/tggrule_templates.properties");

         if(resource != null){
            InputStream in = resource.openStream();            
            properties.load(in);
            loadTemplatesFromProperties(properties);
         }
      } catch (FileNotFoundException e)
      {
         logger.debug(e.getMessage());
         e.printStackTrace();
      } catch (IOException e)
      {
         logger.debug(e.getMessage());
         e.printStackTrace();
      }
      
   }
   
   private void loadTemplatesFromProperties(Properties properties)
   {      
      String allTemplateNames = properties.getProperty("allTemplateNames");
      String[] templateNames = allTemplateNames.split(",");
      
      for(String name : templateNames){
      
         String description = properties.getProperty(name+"."+"description");
         String pattern = properties.getProperty(name+"."+"pattern");
         String priority = properties.getProperty(name+"."+"priority");

         MoflonEditorTemplate template = new MoflonEditorTemplate(name,description,pattern,Integer.parseInt(priority));
         contextFreeTemplates.put(name, template);
      }
      
   }

   /**
    * Here, the project specific keywords and their colors should be defined. This method is called once for the
    * initialization of a text editor.
    */
   public void setKeyWords()
   {
      // "Create"-symbol
      addKeyWord("++").as(COLORS.GREEN);
      
      // Domain-symbols
      addKeyWord("<").as(COLORS.BLACK,BOLD,0);
      addKeyWord(">").as(COLORS.BLACK,BOLD,0);
      addKeyWord("<>").as(COLORS.BLACK,BOLD,0);
      addKeyWord("!").as(COLORS.BLACK,BOLD,0);
   }

   /**
    * Here, the delimiters, which specifies a boundary between words in a char sequence without whitespace, should be
    * defined. For example, in an expression like "int a=5;" the chars '=' and ';' are delimiters and shape tokens to be
    * handled separately despite the missing whitespaces. This method is called once for the initialization of a text
    * editor.
    * 
    */
   public char[] getDelimiters()
   {
      char[] delimiters = {};
      delimiters = new char[] { ',', ';', '{', '}', ':', '"', '\'', '.' };
      return delimiters;
   }

   /**
    * This method is called each time when the color of a word should be determined by Eclipse. With getWord(int) the
    * context of the position can be accessed, at which Eclipse computes currently the highlighting color. The
    * highlight(COLORS) method from the super type should also be called in order to define a (context-dependent)
    * highlighting color.
    */
   public void highlightWord()
   {      
      // Highlight rule names
      if(getWord(2).equals("<") && getWord(1).equals("{")){
         highlight(COLORS.VIOLET, BOLD);
      }
      // Highlight create-links within create-objects
      else if(getWord(1).equals("->") && !(getWord(2).startsWith("\"") || getWord(2).startsWith("'")) && inCreateObject())
         highlight(COLORS.GREEN);
      
      // Highlight assignments and constraints
      else if (getWord(1).equals("->") && (getWord(2).startsWith("\"") || getWord(2).startsWith("'")))
         highlight(COLORS.VIOLET);
      
      // Highlight types of objects
      if (getWord(-1).equals(":"))
         highlight(COLORS.BLUE);

      // Highlight names of create-links and create-objects
      else if (getWord(-1).equals("++"))
         highlight(COLORS.GREEN);
      
   }

   private boolean inCreateObject()
   {
      // Travel backwards, if you find ":" look for the magic "++"
      for(int i = 0; i > -getTokenOffset() + 2 ; i--){
         if(getWord(i).equals(":"))
            return getWord(i-2).equals("++");
         if(getWord(i).equals("}"))
            return false;
      }
      return false;
   }

   /**
    * Here, start and end strings of special text ranges defined which are to be highlighted completely. For example, a
    * javadoc starts with "/*" and ends with "*"+"/" and is highlighted completely with blue color.
    */
   public void highlightSequence()
   {
      // Highlight single quoted strings
      highlightRange(COLORS.BLACK).startsWith("'").endsWith("'");

      // Highlight double quoted strings
      highlightRange(COLORS.BLACK).startsWith("\"").endsWith("\"");
   }

   /**
    * This method returns a collection of templates when user presses ctrl+space in a text editor. With the help of
    * getWord(int) method, the context from the cursor position can be accessed.
    */
   public Collection<MoflonEditorTemplate> getTemplates()
   {
      Vector<MoflonEditorTemplate> templates = new Vector<MoflonEditorTemplate>();
      String previous = getWord(-1);

      if(previous.equals("."))
      {
         // Show all templates sorted by name
         templates.addAll(contextFreeTemplates.values());
      }
      else if(!previous.isEmpty()){
         for(String templateName : contextFreeTemplates.keySet()){
            if(templateName.startsWith(previous)){
               templates.add(contextFreeTemplates.get(templateName));
            }
         }   
      }
      return templates;
   }

   /**
    * User defines with this method an one-to-one mapping between model files (.xmi) and text files (opened with text
    * editor). The absolute paths of the files should be put into returned HashMap. You can use abs(path) function to
    * translate a project relative path to an absolute path
    */
   public HashMap<String, String> getModelPathesToTextPathes()
   {
      HashMap<String, String> m2tPathes = new HashMap<String, String>();
      // m2tPathes.put(abs("/instances/out/test.xmi"), abs("/instances/in/test.txt"));
      return m2tPathes;
   }

   /**
    * This method is called when a text file has been changed of which absolute path is a contained value in HashMap
    * returned by getModelPathesToTextPathes(). The argument is the absolute path of the changed text file. With the
    * help of calling getModelPath(textFilePath)) the path of the corresponding model file can also be accessed. Please
    * don't use any project relative path in this method. (use abs(projectRelativePath))
    */
   public void onSave(String textFilePath)
   {
      // MocaTree.File tree = codeAdapter.parseFile(new File(textFilePath), null);
      // Your textToModel transformation code here...
   }

   /**
    * This method is called when a model file has changed of which absolute path is a contained key in the HashMap
    * returned by getModelPathesToTextPathes(). The argument is the absolute path of the changed model file. Please
    * don't use any project relative path in this method. (use abs(projectRelativePath))
    */
   public void syncText(String modelFilePath)
   {
      // helper.setInputModel(MocaTreePackage.eINSTANCE, modelFilePath);
      // your model2text transformation code here
   }
   
   /**
	 * This method is called, after textToModel transformation
	 * All Problems which where collected during onSave operation can be reported as
	 * markers in the TextEditor. Problems are accessible via the codeAdapter:
	 * codeAdapter.getProblems()
	 * A maker can be created, using AddMarker(problem,pathToTextfile);
	 */  
	public void getProblems(){
	//	for (Problem problem : codeAdapter.getProblems()) {
	//		AddMarker(problem,"instances/in");
	//	}
	}
   
}

