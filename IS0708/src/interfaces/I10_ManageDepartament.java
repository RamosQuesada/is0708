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
import org.eclipse.swt.widgets.Shell;

public class I10_ManageDepartament {
	
	private Shell padre = null;
	private ResourceBundle bundle;
	private aplicacion.Turno turno;
	private int alto, ancho;
	private Cuadrante cuadrante;

	public I10_ManageDepartament(Shell padre, ResourceBundle bundle) {
		// TODO Auto-generated constructor stub
		this.padre = padre;
		this.bundle = bundle;
		createWindow();
	}

	private void createWindow() {
		// TODO Auto-generated method stub
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);		
		//shell.setText(bundle.getString("GestionDeDepartamentos"));
		

		shell.pack();
		shell.setSize(new Point(350, shell.getSize().y));
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
		
	}


}

