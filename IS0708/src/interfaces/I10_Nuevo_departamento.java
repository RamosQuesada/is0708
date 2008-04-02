package interfaces;

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
import aplicacion.Departamento;
import aplicacion.Empleado;
import aplicacion.Vista;

/**
 * Interfaz creación nuevo departamento
 * @author Carlos Sanchez
 *
 */
public class I10_Nuevo_departamento {
	private Shell padre;
	private Shell shell;
	private Vista vista;
	
	private Label labName;
	private Label labNumber;
	private Label labBoss;
	
	private Text tName;
	private Text tNumber;
	private Text textBoss;

	private Button butNewBoss;
	private Button bAccept;
	private Button bCancel;

	private ResourceBundle bundle;
	private I13_Elegir_empleado tNombre;
	private Combo father;
	
	/** Constructor for new department */
	public I10_Nuevo_departamento(Shell padre, ResourceBundle bundle, Vista vista,Combo father) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;
		this.father = father;
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
		tName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,2,1));
		
		labNumber = new Label (group, SWT.NONE);
		labNumber.setText(bundle.getString("I10_lab_num"));
		labNumber.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	
	
		tNumber = new Text (group, SWT.BORDER);	
		tNumber.setLayoutData (new GridData(SWT.FILL,SWT.CENTER,true,true,2,1));
		
		labBoss = new Label (group, SWT.NONE);
		labBoss.setText(bundle.getString("I10_elige_jefe"));
		labBoss.setLayoutData (new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));
		
		final Combo cmbJefes = new Combo(group, SWT.BORDER
				| SWT.READ_ONLY);
		cmbJefes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		
		ArrayList<String> array = vista.getNombreTodosJefes();
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				cmbJefes.add(array.get(i));
			}
		}
		cmbJefes.select(0);
		
		//Buttons "Accept" and "Cancel"
		
		bAccept	= new Button(shell, SWT.PUSH);
		bAccept.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bAccept.setText(bundle.getString("Aceptar"));
		bAccept.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		
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
		

		bAccept.addSelectionListener (
				new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				if(tName.getText()!=""){
					if(tNumber.getText()!="" && integerCheck(tNumber.getText())==true){
						if(vista.existeNombreDepartamento(tName.getText())){
							MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
							messageBox.setText (bundle.getString("Mensaje"));
							messageBox.setMessage (bundle.getString("I10_nombre_ya_existe"));
							e.doit = messageBox.open () == SWT.CLOSE;
						}else{
							if(vista.existeNumDepartamento(tNumber.getText())){
								MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
								messageBox.setText (bundle.getString("Mensaje"));
								messageBox.setMessage (bundle.getString("I10_num_ya_existe"));
								e.doit = messageBox.open () == SWT.CLOSE;
							}else{
								//creamos el departamento
								String numjefe=(cmbJefes.getText().subSequence(cmbJefes.getText().length()-8, cmbJefes.getText().length())).toString();
								vista.crearDepartamento(tName.getText(),tNumber.getText(),Integer.parseInt(numjefe));
								MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
								messageBox.setText (bundle.getString("Mensaje"));
								messageBox.setMessage (bundle.getString("I10_dep_creado"));
								e.doit = messageBox.open () == SWT.CLOSE;
								father.add(tName.getText());
								shell.dispose();					
							}
						}
					}else{
						if(tNumber.getText()==""){
							MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
							messageBox.setText (bundle.getString("Mensaje"));
							messageBox.setMessage (bundle.getString("I10_err_num_vacio"));
							e.doit = messageBox.open () == SWT.CLOSE;
						}else{//si no es integer
							MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
							messageBox.setText (bundle.getString("Mensaje"));
							messageBox.setMessage (bundle.getString("I10_err_check_number"));
							e.doit = messageBox.open () == SWT.CLOSE;
						}
					}
				}else{//si no se ha metido texto
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I10_err_nom_vacio"));
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

