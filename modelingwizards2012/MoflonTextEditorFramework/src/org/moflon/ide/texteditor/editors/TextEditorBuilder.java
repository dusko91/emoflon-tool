package org.moflon.ide.texteditor.editors;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.moflon.ide.texteditor.helpers.MarkerHelper;

public class TextEditorBuilder extends IncrementalProjectBuilder {


	public static M2TSynchronizer synchronizer = null;
	
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {

		if(synchronizer != null){
			try{
				IResourceDelta delta = getDelta(getProject());
				Vector<IResourceDelta> changedDeltas = this.getChangedDeltaLeaves(delta);
				for(IResourceDelta change : changedDeltas){
					IResource resource = change.getResource();
					IPath path =  resource.getLocation();
					IProject project = resource.getProject();
					String projectPath = project.getLocation().toString();
					String modelPath = getPathOfModelToSync(path);
					String textPath = getPathOfTextToSync(path);
					String pathToRefresh = null;
					MarkerHelper.removeMarkers(project);		
					if(modelPath != null){
						synchronizer.onSave(path.toString());				
						pathToRefresh = modelPath.substring(projectPath.length()+1);
						
					}
					else if(textPath != null){
						synchronizer.syncText(path.toString());
						pathToRefresh = textPath.substring(projectPath.length()+1);
					}

					IFile fileToRefresh = project.getFile(pathToRefresh);
					fileToRefresh.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					synchronizer.getProblems();
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	
		
		return null;
	}
	
	private Vector<IResourceDelta> getChangedDeltaLeaves(IResourceDelta delta){
		
		Vector<IResourceDelta> result = new Vector<IResourceDelta>(); 
		
		IPath deltaPath = delta.getResource().getLocation();
		if(getPathOfTextToSync(deltaPath) != null || getPathOfModelToSync(deltaPath) != null)
			result.add(delta);
		
		for(IResourceDelta child : delta.getAffectedChildren(IResourceDelta.CHANGED)){
			result.addAll(getChangedDeltaLeaves(child));
		}
		
		return result;		
	}
	
	private String getPathOfTextToSync(IPath modelPath){
		String pathStg = modelPath.toString();
		HashMap<String, String> modelToTextPathes = synchronizer.getModelPathesToTextPathes();
		
		if(modelToTextPathes.containsKey(pathStg))
			return modelToTextPathes.get(pathStg);
		
		return null;
	}
	
	private String getPathOfModelToSync(IPath textPath){
		String pathStg = textPath.toString();
		HashMap<String, String> modelToTextPathes = synchronizer.getModelPathesToTextPathes();
		
		boolean valueFound = false;
		
		if(modelToTextPathes.containsValue(pathStg)){
			valueFound = true;
		}
		
		if(valueFound){
			for(String modelPath : modelToTextPathes.keySet()){
				if(modelToTextPathes.get(modelPath).equals(pathStg))
					return modelPath;
			}
		}
		return null;
	}
}
