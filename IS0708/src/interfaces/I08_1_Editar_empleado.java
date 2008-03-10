package interfaces;
/*******************************************************************************
 * INTERFAZ I-08.2 :: Edición de empleado
 *   por Dulce
 *   
 * Interfaz para dar de editar un empleado existente.
 * ver 0.2
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ColorDialog;

import aplicacion.Contrato;
import aplicacion.Database;
import aplicacion.Util;
import aplicacion.Vista;
import aplicacion.Empleado;

import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
//import java.sql.Date;

// TODO Mostrar elección de rangos inferiores al usuario
public class I08_1_Editar_empleado {
	private Shell padre = null;
	private ResourceBundle bundle;
	private Vista vista;
	private Date fechaContrato;
	private Date fechaAlta;
	private Date fechaNacimiento;
	private int idVend;
	//http://java.sun.com/j2se/1.4.2/docs/api/java/util/GregorianCalendar.html 
	public I08_1_Editar_empleado(Shell padre, ResourceBundle bundle, Vista vista, int idVend) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;
		fechaContrato = new Date(0);
		fechaAlta = new Date(0);
		fechaNacimiento = new Date(0);
		this.idVend = idVend;
		mostrarVentana();
	}
	
	public void mostrarVentana() {		
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_chico = new Image(padre.getDisplay(), I08_1_Editar_empleado.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(padre.getDisplay(), I08_1_Editar_empleado.class.getResourceAsStream("ico_chica.gif"));
		
		final Image ico_esp = new Image(padre.getDisplay(), I08_1_Editar_empleado.class.getResourceAsStream("ico_esp.gif"));
		final Image ico_usa = new Image(padre.getDisplay(), I08_1_Editar_empleado.class.getResourceAsStream("ico_usa.gif"));
		final Image ico_pol = new Image(padre.getDisplay(), I08_1_Editar_empleado.class.getResourceAsStream("ico_pol.gif"));

		GridLayout layout = new GridLayout(2,false);

		final Group grupoIzq = new Group(shell, SWT.NONE);
		final Group grupoDer = new Group(shell, SWT.NONE);
		grupoIzq.setText(bundle.getString("I08_lab_DatosPersonales"));
		grupoDer.setText(bundle.getString("I08_lab_DatosLaborales"));

		grupoIzq.setLayout(new GridLayout(2,false));
		grupoDer.setLayout(new GridLayout(2,false));
		
		final Label  lNVend			= new Label (grupoIzq, SWT.LEFT);
		//cambiar por un label
		
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
		final Label  lExperiencia	= new Label (grupoDer, SWT.LEFT);
		final Combo  cExperiencia	= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lDepto			= new Label (grupoDer, SWT.LEFT);
		final Combo  cDepto			= new Combo (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bFContrato		= new Button(grupoDer, SWT.PUSH);
		final Text   tFContrato		= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button cambios		= new Button(grupoDer, SWT.PUSH);
		final Text   tFAlta			= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bColor			= new Button(grupoDer, SWT.PUSH);
		final Label  lColor			= new Label	(grupoDer,  SWT.NONE);
		
		final Button bGuardar		= new Button(shell, SWT.PUSH);
		final Button bCancelar		= new Button(shell, SWT.PUSH);
		String aux = String.valueOf(idVend);
		lNVend			.setText(bundle.getString("Vendedor"));
		llNVend			.setText(aux); 
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
		cambios			.setText("I08_lab_FAlta");
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
		lExperiencia.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cExperiencia.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lDepto		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cDepto		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		cambios		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFAlta		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		bColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		lColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		
		bGuardar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		grupoIzq.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		grupoDer.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		// INICIO DE RELLENO DE DATOS
		
		final Empleado emp=vista.getEmpleado(idVend);		
		tPassword.setText(emp.getPassword());	
		tEMail.setText(emp.getEmail());		
		tNombre.setText(emp.getNombre());		
		tApell1.setText(emp.getApellido1());
		tApell2.setText(emp.getApellido2());
		tFNacimiento.setText(Util.dateAString(emp.getFechaNac()));
		tFContrato.setText(Util.dateAString(emp.getFcontrato()));	
		tFAlta.setText(Util.dateAString(emp.getFAlta()));
		
		
		cSexo.setItems (new String [] {	bundle.getString("Femenino"),
										bundle.getString("Masculino")});
	
		cIdioma.setItems (new String [] {	bundle.getString("esp"),
											bundle.getString("eng"),
											bundle.getString("pol")});
			cExperiencia.setItems (new String [] {	bundle.getString("Principiante"),
												bundle.getString("Experto")});
		
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
		
	
		cSexo.select(emp.getSexo());
		cContrato.select(j);

		cExperiencia.select(emp.getGrupo());
		cDepto.select(jj);
		cIdioma.select(emp.getIdioma());
		
		shell.setImage(ico_chico);
		shell.setImage(ico_chica);
		
		shell.setText("Editar empleado");
		shell.setLayout(layout);
		
		bGuardar.setText("Guardar");
		bGuardar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		// Listener para el selector de sexo
		SelectionAdapter sacSexo = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (cSexo.getSelectionIndex()==0)
					shell.setImage(ico_chica);
				else
					shell.setImage(ico_chico);
			}
		};
		cSexo.addSelectionListener(sacSexo);
		
		// Listener para el selector de fecha de nacimiento
		SelectionAdapter sabFNacimiento = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				fechaNacimiento = i17.getFecha();
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				tFNacimiento.setText(String.valueOf(fechaNacimiento.getDate()) + " de " + meses[fechaNacimiento.getMonth()]+ " de " + String.valueOf(fechaNacimiento.getYear()));
			}
		};
		bFNacimiento.addSelectionListener(sabFNacimiento);

		// Listener para el selector de fecha de contrato
		SelectionAdapter sabFContrato = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				fechaContrato = i17.getFecha(); 
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				tFContrato.setText(String.valueOf(fechaContrato.getDate()) + " de " + meses[fechaContrato.getMonth()]+ " de " + String.valueOf(fechaContrato.getYear()));
			}
		};
		bFContrato.addSelectionListener(sabFContrato);
		
		// Listener para el selector de fecha de alta
		SelectionAdapter sabCambios = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
//				I17_Editar_contrato i17= new I17_Editar_Contrato(shell);
				
			}
		};
		cambios.addSelectionListener(sabCambios);
		
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
				// Comprueba la dirección de email (campo no obligatorio)
				if (tEMail.getText().length()!=0 && !Util.comprobarEmail(tEMail.getText())) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_EmailNoValido"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tEMail y seleccionar texto
					tEMail.setFocus();
					tEMail.selectAll();
				}
				// Si se han modificado los campos de datos laborables mostrar mensaje de necesidad de
				// actualizacion de cuadrante
				//else if()
				// Si todo está bien, modifica el empleado
			
			//		Empleado emp = new Empleado(vista.getEmpleadoActual().getEmplId(), n, tNombre.getText(), tApell1.getText(), tApell2.getText(), fechaNacimiento, cSexo.getSelectionIndex(), tEMail.getText(), tPassword.getText(), grupoactual, 0, 0, fechaContrato, fechaAlta, null, cDepto.getText(), null, 0, cIdioma.getSelectionIndex(),0);
					Database dat=null;
					int j=0;
					for (int i=0; i<contratos.size();i++){
						if (contratos.get(i).getNombreContrato().equals(cContrato.getSelectionIndex())){
							j=i;
						}
						}
					int cont= ids.get(j);
					cont =cSexo.getSelectionIndex();
					cont = cExperiencia.getSelectionIndex();
					String prr=tNombre.getText();
					prr=tApell1.getText();
					Date d;
					try {
						d =Util.stringADate(tFNacimiento.getText());
						cont=1;
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						dat.cambiarEmpleado(idVend, prr, prr, prr, 
								Util.stringADate(tFNacimiento.getText()), cSexo.getSelectionIndex(), 
								tEMail.getText(), tPassword.getText(), cExperiencia.getSelectionIndex(), 
								Util.stringADate(tFContrato.getText()),Util.stringADate(tFAlta.getText()), emp.getFelicidad(), 
								cIdioma.getSelectionIndex(), emp.getRango(), emp.getTurnoFavorito(), 
								ids.get(j));
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					shell.dispose(); 
				}
			
		};
		

		bCancelar.addSelectionListener(sabCancelar);
		bGuardar.addSelectionListener(sabGuardar);


		// Botón por defecto bAceptar
		shell.setDefaultButton(bGuardar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
	
}