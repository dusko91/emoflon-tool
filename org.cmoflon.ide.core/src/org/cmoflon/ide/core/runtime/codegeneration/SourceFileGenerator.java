package org.cmoflon.ide.core.runtime.codegeneration;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

public class SourceFileGenerator
{

   public static final String PROCESS_BEGIN = "processBegin";

   public static final String PROCESS_END = "processEnd";

   public static final String BOOT_COMP_WAIT = "bootCompWait";

   public static final String MAIN_LOOP = "mainLoop";

   public static final String WATCHDOG_START = "watchdogStart";

   public static final String WATCHDOG_STOP = "watchdogStop";

   public static final String DROP_UNIDIRECTIONAL_EDGES = "dropUnidirectionalEdges";

   public static final String PARAMETER_CONSTANT = "parameterConstant";

   public static final String PARAMETER = "parameter";

   public static final String MEMB_DECLARATION = "membDeclaration";

   public static final String LIST_DECLARATION = "listDeclaration";

   public static final String INIT = "init";
   
   public static final String GUARD_START = "guardStart";
   
   public static final String GUARD_END= "guardEnd";

   public static final String HOPCOUNT = "hopcount";

   //TODO: contains general as well as specific parts, should be splitted later on
   public static String generateClosingPart(boolean dropUnidir, STGroup sourceGroup,boolean hopcount)
   {
      String result = "";
      if (dropUnidir)
      {
         result += (sourceGroup.getInstanceOf("/" + CMoflonTemplateConfiguration.SOURCE_FILE_GENERATOR + "/" + DROP_UNIDIRECTIONAL_EDGES).render());
      }
      result += (sourceGroup.getInstanceOf("/" + CMoflonTemplateConfiguration.SOURCE_FILE_GENERATOR + "/" + WATCHDOG_START).add("hopcount", hopcount).render());
      result += (sourceGroup.getInstanceOf("/" + CMoflonTemplateConfiguration.SOURCE_FILE_GENERATOR + "/" + PROCESS_END).render());
      return result;
   }

   /**
    * Generates most of the source files upper part 
    * @param component
    * @param algorithmName
    * @param templateGroup
    * @return
    */
   public static String generateUpperPart(String component, String algorithmName, STGroup templateGroup,boolean hopcount)
   {
      String result = "";
      ST procBegin = templateGroup.getInstanceOf("/" + CMoflonTemplateConfiguration.SOURCE_FILE_GENERATOR + "/" + PROCESS_BEGIN);
      procBegin.add("component", component);
      procBegin.add("algo", algorithmName);
      result += procBegin.render();

      ST bootComp = templateGroup.getInstanceOf("/" + CMoflonTemplateConfiguration.SOURCE_FILE_GENERATOR + "/" + BOOT_COMP_WAIT);
      bootComp.add("component", component);
      bootComp.add("hopcount", hopcount);
      result += bootComp.render();

      ST mainLoop = templateGroup.getInstanceOf("/" + CMoflonTemplateConfiguration.SOURCE_FILE_GENERATOR + "/" + MAIN_LOOP);
      mainLoop.add("hopcount", hopcount);
      mainLoop.add("component", component);
      mainLoop.add("algo", algorithmName);
      result += mainLoop.render();

      ST watchDogStop = templateGroup.getInstanceOf("/" + CMoflonTemplateConfiguration.SOURCE_FILE_GENERATOR + "/" + WATCHDOG_STOP);
      result += watchDogStop.render();

      return result;
   }

}