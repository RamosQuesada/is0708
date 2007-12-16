package interfaces;

/*******************************************************************************
 * INTERFACE :: ManageDepartment
 *   by Aneta Mironska
 *   
 * Management (adding and configurate) department.
 * ver 1.0
 *******************************************************************************/
 /**draws window for to add/edid department*/


import java.util.ArrayList;
import java.util.ResourceBundle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import aplicacion.Database;
import aplicacion.Departamento;
import aplicacion.Empleado;
import aplicacion.Vista;
public class I10_Config_departamento {
	private Shell padre;
	private Vista vista;
	private Shell shellWindow;
	
	//List of Atributes of Departament in DB 
	private Label labName;
	private Label labNumber;
	private Label labBoss;
	
	private Button butNewBoss;
	
	private Button bAccept;
	private Button bCancel;
	
	private Text tName;
	private Text tNumber;
	private Text textBoss;

	private ResourceBundle bundle;
	
	private Image iconDep;
	private ArrayList<Empleado> listaCoincidencias;
	
	/**constructor for new department*/
	public I10_Config_departamento(Shell padre, ResourceBundle bundle, Vista vista) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;

		createWindow();
	}

	/**create window for department*/
	private void createWindow() {
		//final Shell shellWindow = new Shell(padre, SWT.CLOSE | SWT.RESIZE
		shellWindow = new Shell(padre, SWT.CLOSE | SWT.CLOSE
				| SWT.APPLICATION_MODAL );
		shellWindow.setText(bundle.getString("I10_tit_dep_nuevo"));
		shellWindow.setLayout(new GridLayout(2,true));
		
		// Permite cerrar la ventana pulsando ESC
		shellWindow.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
					shellWindow.close();
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = false;
					break;
				}
			}
		});

		//add Components into Window
		this.addComponents();
		
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
				if(integerCheck(tNumber.getText())==true){
					// TODO Asignar jefe departamento
					Departamento departamento = new Departamento(labName.getText(),Integer.parseInt(labNumber.getText()),null);
					vista.insertDepartamento(departamento);
					shellWindow.dispose();
				}else{
					//show message for user
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I10_err_check_number"));
					e.doit = messageBox.open () == SWT.CLOSE;
					
					System.out.println("Non-integer value in Number field: "+tNumber.getText());
				}		
			}
		};
		
		// on action "New Boss"
		SelectionAdapter onNewBoss = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Esto no hace nada
				I08_1_Anadir_empleado I08_1_instance = new I08_1_Anadir_empleado(padre, bundle, vista);
			}
		};
		
		bCancel.addSelectionListener(onCancel);
		bAccept.addSelectionListener(onAccept);
		butNewBoss.addSelectionListener(onNewBoss);

		//default button is "Accept"
		shellWindow.setDefaultButton(bAccept);
		
		shellWindow.pack();
		shellWindow.setLocation(padre.getBounds().width / 2 + padre.getBounds().x
				- shellWindow.getSize().x / 2, padre.getBounds().height / 2
				+ padre.getBounds().y - shellWindow.getSize().y / 2);
		shellWindow.open();

	}
	/**add components into window*/
	public void addComponents(){
//		******Adding Components into Window*********************************************************************
		final Group group = new Group(shellWindow, SWT.NONE);
		group.setText(bundle.getString("Departamento"));
		group.setLayout(new GridLayout(1,false));
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		

		//Group - Keeps Variables of Department 
		group.setLayout(new GridLayout(3,false));
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		
		//components inside Group
		this.addComponentInGroup(group);
		
		//Buttons "Accept" and "Cancel"
		this.addComponentOutGroup();

	}
	/**add components which are in group*/
	public void addComponentInGroup(Group group){

		labName = new Label (group, SWT.NONE);
		labName.setText(bundle.getString("Nombre"));
		labName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	
		
		tName = new Text  (group, SWT.BORDER);	
		tName.setText(bundle.getString("Nombre"));	
		tName.setSize(100,20);
		tName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,2,1));
		
		labNumber = new Label (group, SWT.NONE);
		labNumber.setText(bundle.getString("I10_lab_num"));
		labNumber.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	
	
		tNumber = new Text  (group, SWT.BORDER);	
		tNumber.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,2,1));
		
		labBoss = new Label (group, SWT.NONE);
		labBoss.setText(bundle.getString("I10_lab_jefe"));
		labBoss.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	
		
		textBoss = new Text  (group, SWT.BORDER );
		textBoss.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));
		

		Button bBoss = new Button(group, SWT.NONE);
		bBoss.setText(bundle.getString("I10_but_seleccionar"));
		bBoss.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,true,true,1,1))	;	
		SelectionAdapter bossSelectionListener = new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				I13_Elegir_empleado elegir = new I13_Elegir_empleado(shellWindow, bundle, null);
			}
		};
		bBoss.addSelectionListener(bossSelectionListener);
		
		//CREACION DEL JEFE Y DEL DEPARTAMENTO
		/*
		String nombreJefe = textBoss.getText();
		//TODO separar nombres y apellidos y ¿coger primer empleado?
		// volver a poner
		int numeroDepartamento = Integer.valueOf(tNumber.getText());
		Empleado jefe=listaCoincidencias.get(textBoss.getSelectionIndex());
		String nombreDepartamento = tName.getText();
		
		Departamento departamento = new Departamento(nombreDepartamento,
				numeroDepartamento, jefe);
		
		this.vista.insertDepartamento(departamento);*/
		
		butNewBoss		= new Button(group, SWT.PUSH);
		butNewBoss	.setText(bundle.getString("I10_but_nuevo_jefe"));
		butNewBoss	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
	}
	
	/**add components out of group (button Accept and Cancel)*/
	public void addComponentOutGroup(){
		//Buttons "Accept" and "Cancel"
		
		bAccept	= new Button(shellWindow, SWT.PUSH);
		bAccept.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bAccept.setText(bundle.getString("Aceptar"));
		bAccept.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		bAccept.addSelectionListener (
				new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				
				String nombreJefe = textBoss.getText();
				//TODO separar nombres y apellidos y ¿coger primer empleado?
				// volver a poner
				int numeroDepartamento = Integer.valueOf(tNumber.getText());
				//Empleado jefe = listaCoincidencias.get(textBoss.getSelectionIndex());
				String nombreDepartamento = tName.getText();
				
				//Departamento departamento = new Departamento(nombreDepartamento,
				//		numeroDepartamento, jefe);
				
				//vista.insertDepartamento(departamento);
						
			}
			}
		);

		bCancel		= new Button(shellWindow, SWT.PUSH);		
		bCancel	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancel	.setText(bundle.getString("Cancelar"));
		bCancel	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	/**check if the String text is interger*/
	public boolean integerCheck(String string){
	    try {
	    	int n = Integer.parseInt( string );
	    	return true;
	    } catch (Exception e) {
	    	System.out.println("Non-integer value");
	        return false;
	    }
	}
		
}
