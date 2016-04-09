package org.mxunit.eclipseplugin.actions.tagcontextactions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.TableItem;
import org.mxunit.eclipseplugin.MXUnitPluginLog;
import org.mxunit.eclipseplugin.model.FailureTrace;
import org.mxunit.eclipseplugin.model.TestMethod;
import org.mxunit.eclipseplugin.model.TestMethodDataProviderElement;
import org.mxunit.eclipseplugin.views.MXUnitView;

public final class OpenInEditorAction extends Action {
	
	private MXUnitView view;
	private IWorkspace workspace = ResourcesPlugin.getWorkspace();
    private IWorkspaceRoot root = workspace.getRoot();
    
	public OpenInEditorAction(MXUnitView view){
		this.view = view;
	}
	
	public void run(){
		IFile file;
		IPath path = null;
		int line = 1;
		
		TreeSelection tests = (TreeSelection) view.getTestsViewer().getSelection();

		if (!tests.isEmpty()) {
			TestMethod testMethod;
			if(tests.getFirstElement() instanceof TestMethodDataProviderElement) {
				testMethod = (TestMethod) ((TestMethodDataProviderElement) tests.getFirstElement()).getParent();
			} else {
				testMethod = (TestMethod) tests.getFirstElement();
			}
			path = Path.fromOSString(testMethod.getParent().getFilePath()); 
			file = workspace.getRoot().getFileForLocation(path);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));
				String str;
				while ((str = reader.readLine()) != null) {
					if(str.matches("(?i)^.*function.+?"+testMethod.getName()+".*")) {
						OpenInEditorHandler.handleOpenRequest(path.toOSString() +"|" + line, path.toOSString(), line, null, true);
					}
					line++;
				}
				reader.close();
				line = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		TableItem[] rows = view.getDetailsViewer().getSelection();

		if(rows == null || rows.length == 0){
			return;
		}

		Object data = rows[0].getData();
		
		if(data instanceof FailureTrace){
			FailureTrace trace = (FailureTrace)data;
			path = new Path(trace.getFilePath());
			line = trace.getFileLine();
		//this happens when a blank line is clicked
		}else{	
			System.out.println(data);
			System.out.println("don't know what to do with data");
			return;
		}
        
        try {        
			OpenInEditorHandler.handleOpenRequest(path.toOSString() +"|" + line, path.toOSString(), line, null, true);
        
        } catch(Exception e){
        	System.out.println("inside error");
        	MXUnitPluginLog.logError("Exception opening file in OpenInEditorAction",e);
        }
	}
}
