package interfaces;

/*******************************************************************************
 * INTERFACE :: ManageDepartment
 *   by Aneta Mironska
 *   
 * Management (adding and configurate) department.
 * ver 1.0
 *******************************************************************************/
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aplicacion.Empleado;
import aplicacion.Posicion;

public class I10_ManageDepartament {

	private Shell padre = null;
	
	private Shell shellWindow =null;
	
	private String[] arrayDB =null;

	private ResourceBundle bundle;

	private aplicacion.Turno turno;

	private int alto, ancho;

	private Cuadrante cuadrante;

	public I10_ManageDepartament(Shell padre, ResourceBundle bundle,
			String windowName) {
		// TODO Auto-generated constructor stub
		this.padre = padre;
		this.bundle = bundle;
		
		
		String[]newArrayDB = {"nazwaDepartamentu","1 parametr","2 parametr","3 parametr"};
		arrayDB = newArrayDB;
		createWindow(windowName);
	}

	private void createWindow(String windowName) {
		// TODO Auto-generated method stub
		//final Shell shellWindow = new Shell(padre, SWT.CLOSE | SWT.RESIZE
		shellWindow = new Shell(padre, SWT.CLOSE | SWT.RESIZE
				| SWT.APPLICATION_MODAL);
		shellWindow.setText(windowName);
		shellWindow.setLayout(new GridLayout(2,true));
		
		closeOnECS(shellWindow);
	
		final Group group = new Group(shellWindow, SWT.NONE);
		group.setText(bundle.getString("Departamento"));
		group.setLayout(new GridLayout(1,false));
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		

		//Group - Keeps Variables of Department 
		group.setLayout(new GridLayout(2,false));
		
		final Label[] arrayL = new Label[this.getArrayDB().length];
		final Text[] arrayT = new Text[this.getArrayDB().length];
		
		for (int i=0 ;i< this.getArrayDB().length; i++){
			System.out.println(" "+i);
			
			arrayL[i] = new Label (group, SWT.LEFT);
			arrayL[i].setText("Variable_"+i);
			arrayL[i].setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));
			
			arrayT[i] = new Text  (group, SWT.BORDER);	
			arrayT[i].setText(this.getArrayDB()[i]);
			arrayT[i].setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));
		}
		
		
		//**********************************************************
/*		final Label  lNombre	= new Label (group, SWT.LEFT);
		lNombre.setText(bundle.getString("I09_lab_NombreTurno"));
		lNombre.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));*/
		//***********************************************************
		
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));

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
	
	public String[] getArrayDB(){
		return arrayDB;
	}
	public void setArrayDB(String[]newArrayDB){
		arrayDB = newArrayDB;
	}

}
