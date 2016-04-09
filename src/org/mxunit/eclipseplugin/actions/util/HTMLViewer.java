package org.mxunit.eclipseplugin.actions.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class uses a web browser to display html
 */
public class HTMLViewer {
  /**
   * Runs the application
   */
  public void display(String html) {
    final Shell shell = new Shell(Display.getCurrent().getActiveShell(), SWT.SYSTEM_MODAL|SWT.CLOSE);
    Browser browser = createContents(shell, html);
    shell.open();
    shell.setActive();
    browser.moveAbove(null);
  }
  

  /**
   * Creates the main window's contents
   * 
   * @param shell the main window
 * @param html 
   */
  private Browser createContents(Shell shell, String html) {
    shell.setLayout(new FillLayout());

    // Create a web browser
    Browser browser = new Browser(shell, SWT.NONE);
    browser.setText(html);
    return browser;
  }

}