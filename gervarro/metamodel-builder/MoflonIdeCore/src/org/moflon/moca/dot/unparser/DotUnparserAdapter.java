package org.moflon.moca.dot.unparser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.moflon.MoflonDependenciesPlugin;
import org.moflon.ide.core.CoreActivator;

import Moca.unparser.impl.TemplateUnparserImpl;

public class DotUnparserAdapter extends TemplateUnparserImpl 
{
  
  @Override
  public boolean canUnparseFile(String fileName) 
  {
    return fileName.endsWith(".dot");
  }

  @Override
  protected String callMainRule(CommonTreeNodeStream tree, StringTemplateGroup templates) throws RecognitionException 
  {
    DotTreeGrammar dotTreeGrammar = new DotTreeGrammar(tree);
    dotTreeGrammar.setTemplateLib(templates);  
    StringTemplate st = dotTreeGrammar.rootGraph().st;
    if (st==null) {
      return "";
    }
    else {
      return st.toString();
    }  
  }

  @Override
  protected StringTemplateGroup getStringTemplateGroup() throws FileNotFoundException 
  {
	  URL templateFile = MoflonDependenciesPlugin.getPathRelToPlugIn("resources/templates/Dot.stg", CoreActivator.PLUGIN_ID);

	     InputStreamReader reader = null;
	     try
	     {
	        reader = new InputStreamReader(templateFile.openStream());
	     } catch (IOException e)
	     {
	        e.printStackTrace();
	     }
	     
	  return new StringTemplateGroup(reader);
  }

  @Override
  protected String[] getTokenNames() 
  {
    return DotTreeGrammar.tokenNames;
  }
}