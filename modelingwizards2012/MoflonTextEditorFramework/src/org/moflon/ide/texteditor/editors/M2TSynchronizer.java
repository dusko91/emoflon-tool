package org.moflon.ide.texteditor.editors;

import java.util.HashMap;

public interface M2TSynchronizer {

	
	public HashMap<String,String> getModelPathesToTextPathes();
	
	public void onSave(String texFilePath);
	
	public void syncText(String modelFilePath);
	
	 public void getProblems();
	
}
