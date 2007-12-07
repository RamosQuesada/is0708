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
import org.eclipse.swt.graphics.Image;
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
	
	//List of Atributes of Departament in DB 
	private final String[] arrayLabel = {"Atr1", "Atr2", "Atr3", "Atr4"};

	private ResourceBundle bundle;

	private aplicacion.Turno turno;

	private int alto, ancho;

	private Cuadrante cuadrante;
	
	private Image iconDep;
	
	public I10_ManageDepartament(Shell padre, ResourceBundle bundle,
			String windowName, String[] newArrayDB) {
		// TODO Auto-generated constructor stub
		this.padre = padre;
		this.bundle = bundle;
		
		//*****************
		//creamos los iconos que vamos a utilizar en el programa
		//_icoGr = new Image(_display, I02.class.getResourceAsStream("icoGr.gif"));
		//*************************************************************
		
		arrayDB = newArrayDB;
		createWindow(windowName);
	}
	
	public I10_ManageDepartament(Shell padre, ResourceBundle bundle,
			String windowName) {
		// TODO Auto-generated constructor stub
		this.padre = padre;
		this.bundle = bundle;
				
		String[]newArrayDB = {"","","",""};
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
		
		//adding icon to window
		iconDep = new Image(padre.getDisplay(), I02.class.getResourceAsStream("ico_chicos.gif"));
		shellWindow.setImage(iconDep);
	
		final Group group = new Group(shellWindow, SWT.NONE);
		group.setText(bundle.getString("Departamento"));
		group.setLayout(new GridLayout(1,true));
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		

		//Group - Keeps Variables of Department 
		group.setLayout(new GridLayout(2,true));
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		
		final Label[] arrayL = new Label[this.getArrayDB().length];
		final Text[] arrayT = new Text[this.getArrayDB().length];
		
		for (int i=0 ;i< this.getArrayDB().length; i++){
			System.out.println(" "+i);
			
			arrayL[i] = new Label (group, SWT.NONE);
			arrayL[i].setText(arrayLabel[i]);
			arrayL[i].setLayoutData	(new GridData(SWT.CENTER,SWT.CENTER,true,true,1,1));
			
			arrayT[i] = new Text  (group, SWT.BORDER);	
			arrayT[i].setText(this.getArrayDB()[i]);	
			arrayT[i].setSize(100,20);
			arrayT[i].setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,true,true,1,1));
		}
		
		//Buttons "Accept" and "Cancel"
		
		final Button bAccept		= new Button(shellWindow, SWT.PUSH);
		bAccept	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bAccept	.setText(bundle.getString("Aceptar"));
		bAccept	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Button bCancel		= new Button(shellWindow, SWT.PUSH);		
		bCancel	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancel	.setText(bundle.getString("Cancelar"));
		bCancel	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//Adding listeners to Buttons	
		// on action "Cancel"
		SelectionAdapter onCancel = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shellWindow.dispose();	
			}
		};

		// on action "Accept"
		SelectionAdapter onAccept = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Send datas to BD");
			}
		};
		
		bCancel.addSelectionListener(onCancel);
		bAccept.addSelectionListener(onAccept);

		//default button is "Accept"
		shellWindow.setDefaultButton(bAccept);
		
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
