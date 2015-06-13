package org.moflon.ide.ui.admin.editors.mosl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.moflon.ide.texteditor.config.MoflonTextEditorConfigExtern;
import org.moflon.ide.texteditor.editors.MoflonEditorTemplate;

import MOSLCodeAdapter.context.AttributeInfo;
import MOSLCodeAdapter.context.ClassInfo;
import MOSLCodeAdapter.context.ContextInformation;
import MOSLCodeAdapter.context.LinkInfo;

public abstract class AbstractMOSLTextEditorConfiguration extends MoflonTextEditorConfigExtern {
   private static final String MOFLON_NS = "http://www.moflon.org";
	private static final List<String> ECORE_DATATYPES = Arrays.asList("EString", "EInt", "EBoolean");
	protected int sortCounter = 0;

   protected MoflonEditorTemplate createAttributeInfoTemplate(AttributeInfo ai) {
      return new MoflonEditorTemplate(ai.getName(), ai.getClassName() + " : " + ai.getType(), ai.getName(), sortCounter++);
   }
	protected MoflonEditorTemplate createLinkInfoTemplate(LinkInfo li) {
		return new MoflonEditorTemplate(li.getName(), li.getClassName() + " -> " + li.getTargetClassName(), li.getName() + " : ${object}", sortCounter++);
	}

	protected MoflonEditorTemplate createClassInfoTemplate(ClassInfo ci) {
	   return createClassInfoTemplate(ci, true);
	}

   protected MoflonEditorTemplate createClassInfoTemplate(ClassInfo ci, boolean createRelativePath) {
      String path = null; 
      if (createRelativePath) {
         path = calculateRelativePath(getPathToResource(), getPathToUri(ci.getFullName()));
      } else {
         path = ci.getShortName();
      }
      return new MoflonEditorTemplate(ci.getShortName(), ci.getFullName(), path, sortCounter++);
   }
   
	protected void addECoreDatatypes(Vector<? super MoflonEditorTemplate> templates) {
		for (String datatype : ECORE_DATATYPES) {
			templates.add(new MoflonEditorTemplate(datatype, "http://www.eclipse.org/ecore/" + datatype, datatype, sortCounter++));
		}
	}

	private List<String> getPathToUri(String string) {
		String localPath = string;
		if (string.startsWith(MOFLON_NS)) {
			localPath = string.substring(MOFLON_NS.length() + 1);
		}
		String[] aParts = localPath.split("\\.");
		List<String> parts = new ArrayList<String>(aParts.length);
		parts.addAll(Arrays.asList(aParts));
		return parts;
	}

	private List<String> getPathToResource() {
		String path = getResource().getProjectRelativePath().toString();
		String[] aParts = path.split("\\/");
		List<String> parts = new ArrayList<String>(aParts.length);
		parts.addAll(Arrays.asList(aParts));
		parts.remove(0);
		parts.remove(0);
		parts.remove(parts.size() - 1);
		return parts;
	}
	
	private String calculateRelativePath(List<String> fromPath, List<String> toPath) {
	   if ("Rules".equals(fromPath.get(fromPath.size()-1))) {
	      fromPath.remove(fromPath.size()-1);
	   }
	    StringBuilder relPath = new StringBuilder();
	    
	    int patternIndex = fromPath.indexOf("_patterns");
	    if (patternIndex != -1) {
	       for (int i = fromPath.size() - 1 ; i >= patternIndex; i--) {
	          fromPath.remove(patternIndex);
	       }
	    }
	    
	    int i = 0;
	    for (i = 0; i < fromPath.size(); i++) {
	    	if (i > toPath.size() || !fromPath.get(i).equals(toPath.get(i))) {
	    		break;
	    	}
	    }
	    
	    for (int j = 0; j < (fromPath.size() - i); j++) {
	    	relPath.append("../");
	    }
	    
	    for (int j = i; j < toPath.size(); j++) {
	    	relPath.append(toPath.get(j));
	    	if (j < toPath.size() - 1) {
	    		relPath.append("/");
	    	}
	    }
	    
	    return relPath.toString();
	}

   protected void addBoxLinkTemplates(Vector<MoflonEditorTemplate> templates, ContextInformation info)
   {
      String currentType = findCurrentBoxType();
       
       for (LinkInfo li : info.getLinks()) {
          if (li.getClassName().equals(currentType)) {
             templates.add(createLinkInfoTemplate(li));
          }
       }
   }

   protected String findCurrentBoxType()
   {
      // Find current type
       String currentType = null;
       for (int i = -1; i > -500; i--) {
          if ("{".equals(getWord(i))) {
             currentType = getWord(i-1);
             break;
          }
       }
      return currentType;
   }
   
   


}
