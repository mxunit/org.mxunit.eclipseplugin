package org.mxunit.eclipseplugin.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.mxunit.eclipseplugin.MXUnitPluginLog;
import org.mxunit.eclipseplugin.model.TestSuite;
import org.mxunit.eclipseplugin.views.MXUnitView;

/**
 * Runs a selected item in the Resource Navigator as an MXUnit test
 * @author Marc Esher
 *
 */
public final class RunSelectedResourceActionDelegate implements IViewActionDelegate {

	private IResource[] selectedResources;
	private LoadMethodsAction loadAction;
	private RunTestsAction runAction;
	
	public void run(IAction action) {

		try {			
			TestSuiteCreator testSuiteCreator = new TestSuiteCreator();	 
			
			if( !testSuiteCreator.isResourceConfigured(selectedResources[0]) ){
				testSuiteCreator.alertIfResourceNotConfigured(selectedResources[0]);
				return;
			}else{
				MXUnitView view = (MXUnitView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(MXUnitView.ID);
				
				TestSuite suite = testSuiteCreator.createSuite(selectedResources);
				//System.out.println(suite);
				view.getTestsViewer().setInput(suite);
                view.getTestsViewer().getTree().selectAll();      
                view.enableActions();
                
                loadAction = new LoadMethodsAction(view);
                runAction = new RunTestsAction(view);
                
                loadAction.run();                               
                runAction.run();
			}				
			
		} catch (PartInitException e) {
			MXUnitPluginLog.logError("Exception in RunSelectedResourceActionDelegate",e);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		Object[] selected = ((StructuredSelection) selection).toArray();
		selectedResources = new IResource[selected.length];
		for (int i = 0; i < selected.length; i++) {
			selectedResources[i] = (IResource) selected[i];
		}
	}

	public void init(IViewPart view) {
		// TODO Auto-generated method stub
		
	}

}
