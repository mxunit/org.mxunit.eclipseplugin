package org.mxunit.eclipseplugin.actions.resultsactions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.mxunit.eclipseplugin.MXUnitPlugin;
import org.mxunit.eclipseplugin.MXUnitPluginLog;
import org.mxunit.eclipseplugin.actions.util.TreeHelper;
import org.mxunit.eclipseplugin.model.ITest;
import org.mxunit.eclipseplugin.model.TestMethod;
import org.mxunit.eclipseplugin.views.MXUnitView;

/**
 * Opens test case results in the eclipse internal web browser
 * @author Marc Esher
 *
 */
public final class BrowserAction extends Action {
	
	private MXUnitView view;	
	private TreeHelper treeHelper;
	
	public BrowserAction(MXUnitView view){
		this.view = view;
	}
	
	public void setTreeHelper(TreeHelper treeHelper){
		this.treeHelper = treeHelper;
	}
	
	public void run(){
		IWorkbenchBrowserSupport browserSupport = view.getSite().getWorkbenchWindow().getWorkbench().getBrowserSupport();
		IWebBrowser browser;
		try {
			File file = createTempFile();
			
			browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.AS_VIEW, "mxunit", "TestCase", "Test case results");
			browser.openURL( file.toURI().toURL() );
			
		} catch (PartInitException e) {
			MXUnitPluginLog.logError("PartInitException in BrowserAction",e);
		} catch (MalformedURLException e) {
			MXUnitPluginLog.logError("MalformedURLException in BrowserAction",e);
		} catch (IOException e) {
			MXUnitPluginLog.logError("IOException in BrowserAction",e);
		}
		
	}
	
	private File createTempFile() throws IOException{
		File temp = File.createTempFile("mxunitbrowser", ".html");	
        temp.deleteOnExit();    
        // Write to temp file
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));        
        try {
            out.write(createOutputString());
        } catch (Exception e) {
        	MXUnitPluginLog.logError("Error creating temp file in BrowserAction",e);
        }
        
        out.close();		
		return temp;
	}
	
	/*
	 * forgive me, father, for not being more clever and xslt-ish about this.
	 */
	private String createOutputString(){
		String output = getStyleBlock() + "<h1>Test Output</h1><a name='top'></a><ul>";
		ITest[] methods = treeHelper.getRunnableMethods();
		
		for (int i = 0; i < methods.length; i++) {
			output += "<li><a href='#" 
				+ methods[i].getParent().getName() + methods[i].getName()
				+ "'>"
				+ methods[i].getName()
				+ "</a></li>";
		}
		output += "</ul><hr>";
		
		for (int i = 0; i < methods.length; i++) {
			TestMethod method = (TestMethod)methods[i];
            //yes, virginia, this kind of concatenation IS faster than
			//stringbuffer.append()
            if(method.getOutput().trim().length()>0){
            	output += "<h2>" + method.getName() + "</h2>"
            		+ "<a name='" + method.getParent().getName() + method.getName() + "'></a>"
            		+ "<p class='status "+method.getStatus()+"'>Status: " + method.getStatus() +"</p>"
            		+ "<p><b>Result:</b> " + method.getResult() + "</p>"
            		+ method.getOutput()
            		+ "<p><a href='#top'>back to top</a><hr></p>";
            }
        }
		return output;
	}
	
	private String getStyleBlock(){
		String style = "";
		try {
			URL cssURL = MXUnitPlugin.getDefault().getBundle().getEntry("/style/defaultBrowserViewStyle.css");
			File styleFile = new File(FileLocator.toFileURL(cssURL).getFile());
			style = FileUtils.readFileToString(styleFile);
		} catch (IOException e) {
			MXUnitPluginLog.logError("Could not load stylesheet file: ", e);
		}
		return "<style> " + style + "</style> ";
	}

}
