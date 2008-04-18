package interfaces.jefe;
/*******************************************************************************
 * INTERFAZ I-08.2 :: Edición de empleado
 *   por Dulce
 *   
 * Interfaz para dar de editar un empleado existente.
 * ver 0.2
 *******************************************************************************/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ColorDialog;

import aplicacion.Database;
import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;
import aplicacion.datos.Turno;
import aplicacion.utilidades.Util;

import interfaces.general.DialogSeleccionFecha;

import java.util.ArrayList;
//import java.util.Date;
import java.util.ResourceBundle;
import java.sql.Date;

// TODO Mostrar elección de rangos inferiores al usuario
public class DialogEditarEmpleado {
	private Shell padre = null;
	private ResourceBundle bundle;
	private Vista vista;
	private Date fechaContrato;
	private Date fechaAlta;
	private Date fechaNacimiento;
	private int idVend;
	
	/**
	 * Constructor de la clase, crea la ventana de edicion de empleado
	 * @param padre
	 * @param bundle
	 * @param vista
	 * @param idVend
	 */
	public DialogEditarEmpleado(Shell padre, ResourceBundle bundle, Vista vista, int idVend) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;
		fechaContrato = vista.getEmpleado(idVend).getFcontrato();
		fechaAlta = vista.getEmpleado(idVend).getFAlta();
		fechaNacimiento = vista.getEmpleado(idVend).getFechaNac();
		this.idVend = idVend;
		mostrarVentana();
	}
	/**
	 * Ventana de edicion de empleado
	 * Incluye nombre, apellido1, apellido2, contraseña, mail, fecha de alta, fecha nacimiento, fecha contrato,
	 * Color asignado al cuadrante, número de vendedor, sexo, idioma y nivel de experiencia. Es decir, todas los 
	 * campos editables de los empleados.
	 */
	public void mostrarVentana() {		
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		GridLayout layout = new GridLayout(2,false);

		final Group grupoIzq = new Group(shell, SWT.NONE);
		final Group grupoDer = new Group(shell, SWT.NONE);
		grupoIzq.setText(bundle.getString("I08_lab_DatosPersonales"));
		grupoDer.setText(bundle.getString("I08_lab_DatosLaborales"));

		grupoIzq.setLayout(new GridLayout(2,false));
		grupoDer.setLayout(new GridLayout(2,false));
		
		final Label  lNVend			= new Label (grupoIzq, SWT.LEFT);
		final Label  llNVend		= new Label (grupoIzq, SWT.LEFT);
		final Label  lPassword		= new Label (grupoIzq, SWT.LEFT);
		final Text   tPassword		= new Text  (grupoIzq, SWT.BORDER);
		final Label  lNombre		= new Label (grupoIzq, SWT.LEFT);
		final Text   tNombre		= new Text  (grupoIzq, SWT.BORDER);
		final Label  lApell1		= new Label (grupoIzq, SWT.LEFT);
		final Text   tApell1		= new Text  (grupoIzq, SWT.BORDER);
		final Label  lApell2		= new Label (grupoIzq, SWT.LEFT);
		final Text   tApell2		= new Text  (grupoIzq, SWT.BORDER);
		final Label  lEMail			= new Label (grupoIzq, SWT.LEFT);
		final Text   tEMail			= new Text  (grupoIzq, SWT.BORDER);
		final Button bFNacimiento	= new Button(grupoIzq, SWT.PUSH);
		final Text   tFNacimiento	= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lSexo			= new Label (grupoIzq, SWT.LEFT);
		final Combo  cSexo			= new Combo (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lIdioma		= new Label (grupoIzq, SWT.LEFT);
		final Combo  cIdioma		= new Combo (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lContrato		= new Label (grupoDer, SWT.LEFT);
		final Combo  cContrato		= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lTurno			= new Label (grupoDer, SWT.LEFT);
		final Combo  cTurno			= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lExperiencia	= new Label (grupoDer, SWT.LEFT);
		final Combo  cExperiencia	= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lDepto			= new Label (grupoDer, SWT.LEFT);
		final Combo  cDepto			= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bFContrato		= new Button(grupoDer, SWT.PUSH);
		final Text   tFContrato		= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bFAlta			= new Button(grupoDer, SWT.PUSH);
		final Text   tFAlta			= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bColor			= new Button(grupoDer, SWT.PUSH);
		final Label  lColor			= new Label	(grupoDer,  SWT.NONE);
		
		final Button bGuardar		= new Button(shell, SWT.PUSH);
		final Button bCancelar		= new Button(shell, SWT.PUSH);
		
		// Definicion de los campos de edición
		lNVend			.setText(bundle.getString("Vendedor"));
		String aux=String.valueOf(idVend);
		llNVend.setText(aux);
		lPassword		.setText(bundle.getString("Contrasena"));
		lEMail			.setText(bundle.getString("EMail"));
		lNombre			.setText(bundle.getString("Nombre"));
		lApell1			.setText(bundle.getString("I08_lab_Apellido1"));
		lApell2			.setText(bundle.getString("I08_lab_Apellido2"));
		bFNacimiento	.setText(bundle.getString("I08_lab_FNacimiento"));
		lSexo			.setText(bundle.getString("Sexo"));
		lIdioma			.setText(bundle.getString("Idioma"));
		lContrato		.setText(bundle.getString("I08_lab_TipoContrato"));
		lTurno			.setText(bundle.getString("Turno"));
		lExperiencia	.setText(bundle.getString("Experiencia"));
		lDepto			.setText(bundle.getString("Departamento"));
		bFAlta			.setText(bundle.getString("I08_lab_FAlta"));
		bFContrato		.setText(bundle.getString("I08_lab_FContr"));
		bColor			.setText(bundle.getString("I08_lab_SelColor"));

		lNVend		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		lPassword	.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tPassword	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lEMail		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tEMail		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lNombre		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tNombre		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lApell1		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tApell1		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lApell2		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tApell2		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bFNacimiento.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFNacimiento.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		lSexo		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cSexo		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lIdioma		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cIdioma		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lContrato	.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lTurno		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cTurno		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lExperiencia.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cExperiencia.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lDepto		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cDepto		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		bFAlta		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFAlta		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		bColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		lColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		
		bGuardar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		grupoIzq.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		grupoDer.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		// Inicio de rellenado de los campos de la ventana segun el empleado seleccionado.
		
		final Empleado emp=vista.getEmpleado(idVend);	
		String aux1=String.valueOf(idVend);
		llNVend.setText(aux1);
		
		tPassword.setText(emp.getPassword());	
		tEMail.setText(emp.getEmail());		
		tNombre.setText(emp.getNombre());		
		tApell1.setText(emp.getApellido1());
		tApell2.setText(emp.getApellido2());
		
		//Rellenado de los campos referidos fechas
		String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
				bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
				bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
				bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
		tFAlta.setText(String.valueOf(emp.getFAlta().getDate())+" " + bundle.getString("artiFecha(de)")+" " + meses[emp.getFAlta().getMonth()]+" "+ bundle.getString("artiFecha(de)")+" " + String.valueOf(emp.getFAlta().getYear()+1900));
		tFNacimiento.setText(String.valueOf(emp.getFechaNac().getDate())+" " + bundle.getString("artiFecha(de)") +" "+ meses[emp.getFechaNac().getMonth()]+" "+ bundle.getString("artiFecha(de)")+" " + String.valueOf(emp.getFechaNac().getYear()+1900));
		tFContrato.setText(String.valueOf(emp.getFcontrato().getDate())+" " + bundle.getString("artiFecha(de)")+" " + meses[emp.getFcontrato().getMonth()]+" "+ bundle.getString("artiFecha(de)")+" " + String.valueOf(emp.getFcontrato().getYear()+1900));
			
		
		
		
		cSexo.setItems (new String [] {	bundle.getString("Femenino"),
										bundle.getString("Masculino")});
	
		cIdioma.setItems (new String [] {	bundle.getString("esp"),
											bundle.getString("eng"),
											bundle.getString("pol")});
			cExperiencia.setItems (new String [] {	bundle.getString("Principiante"),
												bundle.getString("Experto")});
		
		//Rellenado del campo de los departamentos posibles para dicho empleado
		ArrayList<String> departamentos = vista.getEmpleadoActual().getDepartamentosId();
		int jj=0;
		boolean cumple=true;
		
		for (int i=0; i<departamentos.size(); i++) {
			cDepto.add(departamentos.get(i));

			if (!emp.getDepartamentoId().equals(departamentos.get(i))&& cumple){
				jj=i;
			}
			else
				cumple=false;
		}
		
		//Rellenado del campo de los contratos posibles para dicho empleado
		final ArrayList<Contrato> contratos = vista.getListaContratosDepartamento();
		final ArrayList<Integer> ids = new ArrayList();
		int j=0;
		cumple=true;
		for(int i=0; i<contratos.size();i++){
			String nombre =contratos.get(i).getNombreContrato();
			int num =contratos.get(i).getNumeroContrato();
			int emp1 = emp.getContratoId();
			cContrato.add(contratos.get(i).getNombreContrato());
			ids.add(contratos.get(i).getNumeroContrato());
			if (emp.getContratoId() == num && cumple){
				j=i;
				cumple=false;
			}

		}
		cContrato.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				int j=0;
				for (int i=0; i<contratos.size();i++){
					String p1=contratos.get(i).getNombreContrato();
					String p2=cContrato.getItem(cContrato.getSelectionIndex());
					if (contratos.get(i).getNombreContrato().equals(cContrato.getItem(cContrato.getSelectionIndex()))){
						j=i;
					}
				}
				int indice = ids.get(j);
				final ArrayList<Turno> turnos = vista.getTurnosDeUnContrato(indice);
				int k=0;
				boolean cumple=true;
				int emp1=emp.getTurnoFavorito();
				cTurno.removeAll();
				for(int i=0;i<turnos.size();i++){
					String nombre=turnos.get(i).getDescripcion();
					int num=turnos.get(i).getIdTurno();

					cTurno.add(turnos.get(i).getDescripcion());
					if(emp1==num && cumple){
						k=i;
						cumple=false;
					}
					cTurno.select(k);
				}
			}
			
		});

		//Rellenado del campo de los turnos posibles para dicho empleado
		
		cSexo.select(emp.getSexo());
		cContrato.select(j);
		cExperiencia.select(emp.getGrupo());
		cDepto.select(jj);
		cIdioma.select(emp.getIdioma());
		
		// Rellenado del color
		lColor.setBackground(emp.getColor());
		
		shell.setText(bundle.getString("I08_but_EditarEmpleado"));
		shell.setLayout(layout);
		
		bGuardar.setText(bundle.getString("Guardar"));
		bGuardar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		// Listener para el selector de sexo
		SelectionAdapter sacSexo = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (cSexo.getSelectionIndex()==0)
					shell.setImage(vista.getImagenes().getIco_chica());
				else
					shell.setImage(vista.getImagenes().getIco_chico());
			}
		};
		cSexo.addSelectionListener(sacSexo);
		
		// Listener para el selector de fecha de nacimiento
		SelectionAdapter sabFNacimiento = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				DialogSeleccionFecha i17 = new DialogSeleccionFecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				Date fecha = i17.getFecha();
				
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if(fecha!=null){
					tFNacimiento.setText(String.valueOf(fecha.getDate())+" " + bundle.getString("artiFecha(de)")+" " + meses[fecha.getMonth()]+" "+ bundle.getString("artiFecha(de)")+" " + String.valueOf(fecha.getYear()+1900));
					fechaNacimiento = fecha;
				}
			}
		};
		bFNacimiento.addSelectionListener(sabFNacimiento);

		// Listener para el selector de fecha de contrato
		SelectionAdapter sabFContrato = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				DialogSeleccionFecha i17 = new DialogSeleccionFecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				
				Date fecha = i17.getFecha();
				 
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if(fecha!=null){
					tFContrato.setText(String.valueOf(fecha.getDate())+" " + bundle.getString("artiFecha(de)")+" " + meses[fecha.getMonth()]+" "+ bundle.getString("artiFecha(de)")+" " + String.valueOf(fecha.getYear()+1900));
					fechaContrato = fecha;
				}
				}
		};
		bFContrato.addSelectionListener(sabFContrato);
		
		// Listener para el selector de fecha de alta
		SelectionAdapter sabCambios = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				DialogSeleccionFecha i17 = new DialogSeleccionFecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				Date fecha = i17.getFecha();
				
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if(fecha!=null){
					tFAlta.setText(String.valueOf(fecha.getDate())+" " + bundle.getString("artiFecha(de)")+" " + meses[fecha.getMonth()]+" "+ bundle.getString("artiFecha(de)")+" " + String.valueOf(fecha.getYear()+1900));
					//String tFAlta =String.valueOf(fechaAlta.getDate()) + "-" + fechaAlta.getMonth()+ "-" + String.valueOf(fechaAlta.getYear());
					fechaAlta =  fecha;
					int aux=1;
				}
			}
		};
		bFAlta.addSelectionListener(sabCambios);
		
		// Listener para el selector de color
		SelectionAdapter sabColor = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				ColorDialog cd = new ColorDialog(shell);
				cd.setText(bundle.getString("I08_lab_SelColor"));
				cd.setRGB(new RGB(255, 255, 255));
				RGB newColor = cd.open();
				if (newColor != null) {
					lColor.setBackground(new Color(shell.getDisplay(), newColor));
				}
			}
		};
		bColor.addSelectionListener(sabColor);

		
		// Listener con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};

		// Listener con lo que hace el botón bAceptar
		SelectionAdapter sabGuardar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {			
				
				// Comprueba que la contraseña no es vacía (campo obligatorio)
				if (tPassword.getText().length()==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I01_err_Password"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tPassword y seleccionar texto
					tPassword.setFocus();
					tPassword.selectAll();
				}
				// Comprueba que el nombre no sea vacio
				else if (tNombre.getText().length()==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_NombreVacio"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNombre y seleccionar texto
					tNombre.setFocus();
					tNombre.selectAll();
				}
				// Comprueba que el apellido1 no sea vacio
				else if (tApell1.getText().length()==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_Ape1Vacio"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNombre y seleccionar texto
					tApell1.setFocus();
					tApell1.selectAll();
				}
				// Comprueba que el apellido2 no sea vacio
				else if (tApell2.getText().length()==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_Ape2Vacio"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNombre y seleccionar texto
					tApell2.setFocus();
					tApell2.selectAll();
				}
				// Comprueba que las fechas se han seleccionado
				else if (tFNacimiento.getText().length()==0 || tFContrato.getText().length()==0 || tFAlta.getText().length()==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_Fecha"));					
					e.doit = messageBox.open () == SWT.YES;

				}
				// Comprueba la dirección de email (campo no obligatorio)
				else if (tEMail.getText().length()!=0 && !Util.comprobarEmail(tEMail.getText())) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_EmailNoValido"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tEMail y seleccionar texto
					tEMail.setFocus();
					tEMail.selectAll();
				}

				// Si todo está bien, modifica el empleado					

					String nom=tNombre.getText();
					String ap1=tApell1.getText();
					String ap2=tApell2.getText();
					String mail =tEMail.getText();
					String pass=tPassword.getText();
					int sex=cSexo.getSelectionIndex();

					
					int Fel=emp.getFelicidad();
					int idiom= cIdioma.getSelectionIndex();
					int ran=emp.getRango();
					int turn= emp.getTurnoFavorito();
					
					int id =idVend;
					int j=0;
					for (int i=0; i<contratos.size();i++){
						String p1=contratos.get(i).getNombreContrato();
						String p2=cContrato.getItem(cContrato.getSelectionIndex());
						if (contratos.get(i).getNombreContrato().equals(cContrato.getItem(cContrato.getSelectionIndex()))){
							j=i;
						}
					}
					int indice = ids.get(j);
					int Exp=cExperiencia.getSelectionIndex();
					
					// Si se han modificado los campos de datos laborables mostrar mensaje de necesidad de
					// actualizacion de cuadrante
					if(emp.getContratoId()!=ids.get(j)|| emp.getGrupo()!=cExperiencia.getSelectionIndex()){
						
						
						
						MessageBox msgBox = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.OK
										| SWT.CANCEL);
						msgBox.setMessage(bundle.getString("I09_aviso_inconsistencias"));
						msgBox.setText(bundle.getString("Aviso"));
						int resp = msgBox.open();
						if (resp == SWT.OK) {
							// Si a pesar de todo esta de acuerdo
							// modificacion del empleado.
							vista.modificarEmpleado(id, nom, ap1, ap2, fechaNacimiento, sex,  mail, pass, 
								Exp, fechaContrato,fechaAlta, Fel, idiom, ran, turn, indice, lColor.getBackground());
							shell.dispose();
						}
							
					} else {
						vista.modificarEmpleado(id, nom, ap1, ap2, fechaNacimiento, sex,  mail, pass, 
								Exp, fechaContrato,fechaAlta, Fel, idiom, ran, turn, indice, lColor.getBackground());
						shell.dispose();
					}
					

						
					 

					
				}
			
		};
		

		bCancelar.addSelectionListener(sabCancelar);
		bGuardar.addSelectionListener(sabGuardar);


		// Botón por defecto bGuardar
		shell.setDefaultButton(bGuardar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
	
}