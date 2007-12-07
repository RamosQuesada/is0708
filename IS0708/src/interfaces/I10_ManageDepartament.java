package interfaces;

/*******************************************************************************
 * INTERFACE :: ManageDepartment
 *   by Aneta Mironska
 *   
 * Management (adding and configurate) department.
 * ver 1.0
 *******************************************************************************/
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class I10_ManageDepartament {

	private Shell padre = null;
	
	private Shell shellWindow = null;

	private ResourceBundle bundle;

	private aplicacion.Turno turno;

	private int alto, ancho;

	private Cuadrante cuadrante;

	public I10_ManageDepartament(Shell padre, ResourceBundle bundle,
			String windowName) {
		// TODO Auto-generated constructor stub
		this.padre = padre;
		this.bundle = bundle;
		createWindow(windowName);
	}

	private void createWindow(String windowName) {
		// TODO Auto-generated method stub
		shellWindow = new Shell(padre, SWT.CLOSE | SWT.RESIZE
				| SWT.APPLICATION_MODAL);
		shellWindow.setText(windowName);
		
		closeOnECS(shellWindow);

		shellWindow.pack();
		shellWindow.setSize(new Point(350, shellWindow.getSize().y));
		shellWindow.setLocation(padre.getBounds().width / 2 + padre.getBounds().x
				- shellWindow.getSize().x / 2, padre.getBounds().height / 2
				+ padre.getBounds().y - shellWindow.getSize().y / 2);
		shellWindow.open();

	}

	//allow to close window on ECS key
	public void closeOnECS(final Shell newShell) {
		
		newShell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
					newShell.close();
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = false;
					break;
				}
			}
		});
	}

}
