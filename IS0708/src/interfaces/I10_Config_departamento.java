package interfaces;

/*******************************************************************************
 * INTERFACE :: ManageDepartment
 *   by Carlos Sánchez and a little collaboration from Aneta
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import aplicacion.Vista;
import aplicacion.datos.Departamento;
import aplicacion.datos.Empleado;
public class I10_Config_departamento {
	private Shell padre;
	private Shell shell;
	private Vista vista;
	private String nombre;
	
	private Label labName;
	private Label labChooseBoss;
	
	private Text tName;
	
	private boolean admin;// true si es admin

	private Button bAccept;
	private Button bCancel;
	private Button bJefe;
	
	private ResourceBundle bundle;
	
	private Combo father;
	
	/** Constructor for new department */
	public I10_Config_departamento(Shell padre, ResourceBundle bundle, Vista vista,String nombre,Combo father,boolean admin) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;
		this.nombre=nombre;
		this.father = father;
		this.admin = admin;
		createWindow();
	}

	private void createWindow() {
		shell = new Shell(padre, SWT.CLOSE | SWT.CLOSE | SWT.APPLICATION_MODAL );
		shell.setText(bundle.getString("I10_config_dep"));
		shell.setLayout(new GridLayout(2,true));
		
		// Permite cerrar la ventana pulsando ESC


		//add Components into Window
		addComponents();
	
		//default button is "Accept"
		shell.setDefaultButton(bAccept);
		
		shell.pack();
		shell.setLocation(padre.getBounds().width / 2 + padre.getBounds().x
				- shell.getSize().x / 2, padre.getBounds().height / 2
				+ padre.getBounds().y - shell.getSize().y / 2);
		shell.open();

	}
	
	/**add components into window*/
	private void addComponents(){
		if(admin){
			final Group group = new Group(shell, SWT.NONE);
			group.setText(bundle.getString("Departamento"));
			group.setLayout(new GridLayout(1,false));
			group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));

			group.setLayout(new GridLayout(3,false));
			group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));

			labName = new Label (group, SWT.NONE);
			labName.setText(bundle.getString("Nombre"));
			labName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	

			tName = new Text  (group, SWT.BORDER);	
			tName.setSize(100,20);
			tName.setText(nombre);
			tName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));

			bJefe= new Button(group, SWT.CHECK);
			bJefe.setText(bundle.getString("I10_cambiar_jefe"));
			bJefe.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,2,1));

			labChooseBoss= new Label (group, SWT.NONE);
			labChooseBoss.setText(bundle.getString("I10_elige_jefe"));
			labChooseBoss.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,3,1));
			

			final Combo cmbJefes = new Combo(group, SWT.BORDER
					| SWT.READ_ONLY);
			cmbJefes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 4, 1));

			ArrayList<String> array = vista.getNombreTodosJefes();
			if (array != null) {
				for (int i = 0; i < array.size(); i++) {
					cmbJefes.add(array.get(i));
				}
			}
			cmbJefes.select(0);
			cmbJefes.setEnabled(false);
			

			//Buttons "Accept" and "Cancel"

			bAccept	= new Button(shell, SWT.PUSH);
			bAccept.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			bAccept.setText(bundle.getString("Aceptar"));

			
			bJefe.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if(bJefe.getSelection())
						cmbJefes.setEnabled(true);
					else
						cmbJefes.setEnabled(false);	
				}
			});

			bAccept.addSelectionListener (
					new SelectionAdapter () {
						public void widgetSelected (SelectionEvent e) {
							/*if(integerCheck(tNumber.getText())==true){
					// TODO Asignar jefe departamento
					//Empleado jefe = vista.getEmpleado(tNombre.getIdEmpl());
					//Departamento departamento = new Departamento(tName.getText(),Integer.parseInt(tNumber.getText()),jefe);
					//vista.insertDepartamento(departamento);
					//
					shell.dispose();
				}else{
					//show message for user
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I10_err_check_number"));
					e.doit = messageBox.open () == SWT.CLOSE;
					System.out.println("Non-integer value in Number field: "+tNumber.getText());
				}		
							 */
							if(tName.getText()!=""){
								//cambiamos el nombre
								vista.cambiarNombreDepartamento(nombre,tName.getText());
								if(bJefe.getSelection()){
									String numjefe=(cmbJefes.getText().subSequence(cmbJefes.getText().length()-8, cmbJefes.getText().length())).toString();
									vista.cambiarJefeDepartamento(tName.getText(),numjefe);
								}
								father.removeAll();
								ArrayList<String> array = vista.getNombreTodosDepartamentos();
								if (array != null) {
									for (int i = 0; i < array.size(); i++) {
										father.add(array.get(i));
									}
								}
								// cmbDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
								father.select(0);
								shell.dispose();
							}else{//si no se ha metido texto
								MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
								messageBox.setText (bundle.getString("Mensaje"));
								messageBox.setMessage (bundle.getString("I10_err_string_vacio"));
								e.doit = messageBox.open () == SWT.CLOSE;
							}
						}
					});

			bCancel		= new Button(shell, SWT.PUSH);		
			bCancel	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			bCancel	.setText(bundle.getString("Cancelar"));
			bCancel	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			bCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
				}
			});
		}else{//si es jefe de departamento
			final Group group = new Group(shell, SWT.NONE);
			group.setText(bundle.getString("Departamento"));
			group.setLayout(new GridLayout(1,false));
			group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));

			group.setLayout(new GridLayout(3,false));
			group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));

			labName = new Label (group, SWT.NONE);
			labName.setText(bundle.getString("Nombre"));
			labName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	

			tName = new Text  (group, SWT.BORDER);	
			tName.setSize(100,20);
			tName.setText(nombre);
			tName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));

			//Buttons "Accept" and "Cancel"

			bAccept	= new Button(shell, SWT.PUSH);
			bAccept.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			bAccept.setText(bundle.getString("Aceptar"));




			bAccept.addSelectionListener (
					new SelectionAdapter () {
						public void widgetSelected (SelectionEvent e) {
							if(tName.getText()!=""){
								//cambiamos el nombre
								vista.cambiarNombreDepartamento(nombre,tName.getText());
								father.removeAll();
								ArrayList<String> array = vista.getNombreTodosDepartamentos();
								if (array != null) {
									for (int i = 0; i < array.size(); i++) {
										father.add(array.get(i));
									}
								}
								// cmbDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
								father.select(0);
								shell.dispose();
							}else{//si no se ha metido texto
								MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
								messageBox.setText (bundle.getString("Mensaje"));
								messageBox.setMessage (bundle.getString("I10_err_string_vacio"));
								e.doit = messageBox.open () == SWT.CLOSE;
							}
						}
					});

			bCancel		= new Button(shell, SWT.PUSH);		
			bCancel	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			bCancel	.setText(bundle.getString("Cancelar"));
			bCancel	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			bCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
				}
			});
		}
	}

	/**check if the String text is interger*/
	private boolean integerCheck(String string){
	    try {
	    	int n = Integer.parseInt( string );
	    	return true;
	    } catch (Exception e) {
	    	System.out.println("Non-integer value");
	        return false;
	    }
	}
		
}
