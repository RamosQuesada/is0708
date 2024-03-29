package interfaces.jefe;
/*******************************************************************************
 * INTERFAZ I-08.1 :: Creación de empleado
 *   por Daniel Dionne
 *   
 * Interfaz para dar de alta un empleado nuevo.
 * ver 0.1
 *******************************************************************************/

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ColorDialog;

import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;
import aplicacion.utilidades.Util;

import interfaces.general.DialogSeleccionFecha;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.sql.Date;

// TODO Mostrar elección de rangos inferiores al usuario
public class DialogAnadirEmpleado {
	private Shell padre = null;
	private ResourceBundle bundle;
	private Vista vista;
	private Date fechaContrato;
	private Date fechaAlta;
	private Date fechaNacimiento;
	private Date fechaFContrato;
	private ArrayList<Contrato> contratos;
	//http://java.sun.com/j2se/1.4.2/docs/api/java/util/GregorianCalendar.html 
	public DialogAnadirEmpleado(Shell padre, ResourceBundle bundle, Vista vista) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;
		fechaContrato = new Date(0);
		fechaAlta = new Date(0);
		fechaNacimiento = new Date(0);
		contratos = vista.getListaContratosDepartamento();
		mostrarVentana();
	}
	
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
		final Text   tNVend			= new Text  (grupoIzq, SWT.BORDER);
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
		final Label  lExperiencia	= new Label (grupoDer, SWT.LEFT);
		final Combo  cExperiencia	= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lDepto			= new Label (grupoDer, SWT.LEFT);
		final Combo  cDepto			= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bFContrato		= new Button(grupoDer, SWT.PUSH);
		final Text   tFContrato		= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bFAlta			= new Button(grupoDer, SWT.PUSH);
		final Text   tFAlta			= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bFFContrato	= new Button(grupoDer, SWT.PUSH);
		final Text   tFFContrato	= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bColor			= new Button(grupoDer, SWT.PUSH);
		final Label  lColor			= new Label	(grupoDer,  SWT.NONE);
		
		final Button bAceptar		= new Button(shell, SWT.PUSH);
		final Button bCancelar		= new Button(shell, SWT.PUSH);
		
		lNVend			.setText(bundle.getString("Vendedor"));
		lPassword		.setText(bundle.getString("Contrasena"));
		lEMail			.setText(bundle.getString("EMail"));
		lNombre			.setText(bundle.getString("Nombre"));
		lApell1			.setText(bundle.getString("I08_lab_Apellido1"));
		lApell2			.setText(bundle.getString("I08_lab_Apellido2"));
		bFNacimiento	.setText(bundle.getString("I08_lab_FNacimiento"));
		lSexo			.setText(bundle.getString("Sexo"));
		lIdioma			.setText(bundle.getString("Idioma"));
		lContrato		.setText(bundle.getString("I08_lab_TipoContrato"));
		lExperiencia	.setText(bundle.getString("Experiencia"));
		lDepto			.setText(bundle.getString("Departamento"));
		bFAlta			.setText(bundle.getString("I08_lab_FAlta"));
		bFContrato		.setText(bundle.getString("I08_lab_FContr"));
		bFFContrato		.setText(bundle.getString("I08_lab_FFContr"));
		bColor			.setText(bundle.getString("I08_lab_SelColor"));

		lNVend		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tNVend		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
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
		lExperiencia.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cExperiencia.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lDepto		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cDepto		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		bFAlta		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFAlta		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		bFFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		bColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		lColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		
		bAceptar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		grupoIzq.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		grupoDer.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		tNVend.setTextLimit(8);
		cSexo.setItems (new String [] {	bundle.getString("Femenino"),
										bundle.getString("Masculino")});
		cIdioma.setItems (new String [] {	bundle.getString("esp"),
											bundle.getString("eng"),
											bundle.getString("pol")});
		
		cExperiencia.setItems (new String [] {	bundle.getString("Principiante"),
												bundle.getString("Experto")});
		
		for (int i=1; i<contratos.size(); i++)
			cContrato.add(contratos.get(i).getNombreContrato());
		
		ArrayList<String> departamentos = vista.getEmpleadoActual().getDepartamentosId();
		for (int i=0; i<departamentos.size(); i++) {
			cDepto.add(departamentos.get(i));
		}
		cSexo.select(0);
		cContrato.select(0);
		cExperiencia.select(0);
		cDepto.select(0);
		cIdioma.select(0);
		
		
		shell.setText(bundle.getString("I08_but_NuevoEmpleado"));
		shell.setLayout(layout);
		
		bAceptar.setText(bundle.getString("Aceptar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
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
				fechaNacimiento = i17.getFecha();
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fechaNacimiento!=null)
				tFNacimiento.setText(String.valueOf(fechaNacimiento.getDate())+" " + bundle.getString("artiFecha(de)")+" "  + meses[fechaNacimiento.getMonth()]+" " + bundle.getString("artiFecha(de)")+" "  + String.valueOf(fechaNacimiento.getYear()+1900));
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
				fechaContrato = i17.getFecha(); 				
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fechaContrato != null)
					tFContrato.setText(String.valueOf(fechaContrato.getDate())+" " + bundle.getString("artiFecha(de)")+" " + meses[fechaContrato.getMonth()]+" " + bundle.getString("artiFecha(de)")+" "  + String.valueOf(fechaContrato.getYear()+1900));				
			}
		};
		bFContrato.addSelectionListener(sabFContrato);
		
		// Listener para el selector de fecha de alta
		SelectionAdapter sabFAlta = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				DialogSeleccionFecha i17 = new DialogSeleccionFecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				fechaAlta = i17.getFecha(); 
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fechaAlta != null)
				tFAlta.setText(String.valueOf(fechaAlta.getDate()) +" " + bundle.getString("artiFecha(de)")+" " + meses[fechaAlta.getMonth()]+" " + bundle.getString("artiFecha(de)")+" "  + (String.valueOf(fechaAlta.getYear()+1900)));
			}
		};
		bFAlta.addSelectionListener(sabFAlta);
		
		// Listener para el selector de fecha de final de contrato
		SelectionAdapter sabFFContrato = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				DialogSeleccionFecha i17 = new DialogSeleccionFecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				fechaFContrato = i17.getFecha(); 
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fechaFContrato != null)
				tFFContrato.setText(String.valueOf(fechaFContrato.getDate()) +" " + bundle.getString("artiFecha(de)")+" " + meses[fechaFContrato.getMonth()]+" " + bundle.getString("artiFecha(de)")+" "  + (String.valueOf(fechaFContrato.getYear()+1900)));
			}
		};
		bFFContrato.addSelectionListener(sabFFContrato);
		
		
		
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
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Comprueba el número de vendedor (campo obligatorio)
				int n = Util.convertirNVend(tNVend.getText());
				if (n<0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I01_err_NumVendedor1"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNVend y seleccionar texto
					tNVend.setFocus();
					tNVend.selectAll();
				}
				else if (n==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_NVend0"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNVend y seleccionar texto
					tNVend.setFocus();
					tNVend.selectAll();
				}
				// Comprueba que la contraseña no es vacía (campo obligatorio)
				else if (tPassword.getText().length()==0) {
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
				else if (tFNacimiento.getText().length()==0 || tFContrato.getText().length()==0 || tFAlta.getText().length()==0 || tFFContrato.getText().length()==0 ) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_Fecha"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tFNacimiento ,Fcontrato,Falta y seleccionar texto
					tFNacimiento.setFocus();
					tFNacimiento.selectAll();
					tFContrato.setFocus();
					tFContrato.selectAll();
					tFAlta.setFocus();
					tFAlta.selectAll();
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
				else if (vista.getEmpleado(n) != null) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_NVendExiste"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNVend y seleccionar texto
					tNVend.setFocus();
					tNVend.selectAll();
				}
				// Si todo está bien, inserta el empleado
				else {
					String cont = cContrato.getText();
					int id=0;
					int turno=0;
					for (int i=0; i<contratos.size(); i++) {
						String nombre = contratos.get(i).getNombreContrato();
						if (cont.equals(nombre)) {
							id = contratos.get(i).getNumeroContrato();
							turno = contratos.get(i).getTurnoInicial();
							break;
						}
					}

					
					Empleado emp = new Empleado(vista.getEmpleadoActual().getEmplId(), Util.convertirNVend(tNVend.getText()), tNombre.getText(), tApell1.getText(), tApell2.getText(), fechaNacimiento, cSexo.getSelectionIndex(), tEMail.getText(), tPassword.getText(), cExperiencia.getSelectionIndex(), 0, id, fechaContrato, fechaAlta, null, cDepto.getText(), null, 0, cIdioma.getSelectionIndex(), turno, 0);
					emp.setColor(lColor.getBackground());
					emp.setFinContrato(fechaFContrato);
					vista.insertEmpleado(emp);

					shell.dispose();
				}
			}
		};
		
		bCancelar.addSelectionListener(sabCancelar);
		bAceptar.addSelectionListener(sabAceptar);

		// Botón por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}