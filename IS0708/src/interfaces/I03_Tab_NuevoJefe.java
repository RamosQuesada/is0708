package interfaces;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Departamento;
import aplicacion.datos.Empleado;
import aplicacion.utilidades.Util;

public class I03_Tab_NuevoJefe {
	private Image ico_chico;
	private ResourceBundle bundle;
	private Vista vista;
	private TabFolder tabFolder;
	private String tmDep;
	private Shell shellPadre;
	ArrayList<Contrato> contratos;
	
	  
	public I03_Tab_NuevoJefe(TabFolder tabF,ResourceBundle b,Vista v,Shell s){
		
		//inicializamos las variables de la clase
		this.bundle = b;
		this.vista = v;
		this.tabFolder = tabF;
		this.shellPadre = s;
		
		//cargamos las imagenes
		ico_chico = new Image(tabFolder.getDisplay(), 
				I03_Tab_NuevoJefe.class.getResourceAsStream("ico_chico.gif"));
		
		
		//creamos la ventana
		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText(bundle.getString("I02_admin_jefe"));
		tabItemEmpleados.setImage(ico_chico);

		// Creamos el contenido de la pestaña cuadrantes
		final Composite cNuevoJefe = new Composite(tabFolder, SWT.BORDER);
		tabItemEmpleados.setControl(cNuevoJefe);

		cNuevoJefe.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
				0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 1;
		cNuevoJefe.setLayout(lGrupo);

		final Label lTitulo = new Label(cNuevoJefe, SWT.LEFT);
		lTitulo.setText(bundle.getString("I02_lab_etiquetaMarco"));
		final Composite cNuevoJefe2 = new Composite(cNuevoJefe,
				SWT.BORDER);
		cNuevoJefe2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 0, 0));
		GridLayout lGrupo2 = new GridLayout();
		lGrupo2.numColumns = 2;
		cNuevoJefe2.setLayout(lGrupo2);
		
		/*
				
		final Label lNombre = new Label(cNuevoGerente2, SWT.LEFT);
		lNombre.setText(bundle.getString("I02_lab_nombre"));
		final Text tNombre = new Text(cNuevoGerente2, SWT.BORDER);
		tNombre.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				0, 0));

		final Label lApellidos = new Label(cNuevoGerente2, SWT.LEFT);
		lApellidos.setText(bundle.getString("I02_lab_apellidos"));
		final Text tApellidos = new Text(cNuevoGerente2, SWT.BORDER);
		tApellidos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 0, 0));

		final Label lNombreUsuario = new Label(cNuevoGerente2, SWT.LEFT);
		lNombreUsuario.setText(bundle.getString("I02_lab_nombreUsuario"));
		final Text tNombreUsuario = new Text(cNuevoGerente2, SWT.BORDER);
		tNombreUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 0, 0));
		
		final Button bClaveAuto = new Button(cNuevoGerente2, SWT.RADIO);
		bClaveAuto.setText(bundle.getString("I02_lab_claveAuto"));
		final Button bClaveManual = new Button(cNuevoGerente2, SWT.RADIO);
		bClaveAuto.setSelection(false);
		bClaveManual.setText(bundle.getString("I02_lab_claveManual"));
		final Label lContra = new Label(cNuevoGerente2, SWT.LEFT);
		lContra.setText(bundle.getString("I02_lab_clave"));
		final Text tPassword = new Text(cNuevoGerente2, SWT.BORDER);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				0, 0));
		//tPassword.setText(aplicacion.Util.obtenerClave());
		bClaveManual.setSelection(true);
		tPassword.setEditable(true);
		
		
		
		final Label lDepartamento = new Label(cNuevoGerente2, SWT.LEFT);
		lDepartamento.setText(bundle.getString("I02_lab_departamento"));
		final Combo cDepartamentos = new Combo(cNuevoGerente2, SWT.BORDER | SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 0, 0));
		
		ArrayList <Departamento> listDept= vista.getControlador().getTodosDepartamentos();
		for(int i = 0; i< listDept.size();i++){
			cDepartamentos.add(listDept.get(i).getNombreDepartamento());		
		}
		cDepartamentos.setEnabled(true);
		
		
		tmDep = cDepartamentos.getText();

		bClaveAuto.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				tPassword.setEditable(false);
				tPassword.setText(aplicacion.utilidades.Util.obtenerClave());
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		bClaveManual.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				tPassword.setEditable(true);
				tPassword.setText("");

			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		 
		
		*/
		final Composite cAceptarCancelar = new Composite(cNuevoJefe,
				SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.DOWN,
				false, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);

		// Botones aceptar y cancelar
		final Button bAceptar = new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar = new Button(cAceptarCancelar, SWT.PUSH);

		// Introducimos los textos a los botones
		// bOClave.setText("Obtener clave");
		bAceptar.setText(bundle.getString("Aceptar"));
		bCancelar.setText(bundle.getString("Cancelar"));
		// Introducimos los valores y eventos de Aceptar

		bAceptar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		
		
		/*bAceptar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(shell,
						SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
				messageBox.setText("CONFIRMACION");
				messageBox.setMessage("¿Desea guardar el nuevo jefe?");
				if (messageBox.open() == SWT.YES) {					
					if(tNombre.getText().equals("")){
						MessageBox message = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.OK
										| SWT.ICON_INFORMATION);
						message.setText("Info");
						message.setMessage(bundle
								.getString("I02_dlg_nombre"));
						message.open();
					}else
					if(tApellidos.getText().equals("")){
						MessageBox message = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.OK
										| SWT.ICON_INFORMATION);
						message.setText("Info");
						message.setMessage(bundle
								.getString("I02_dlg_apellidos"));
						message.open();
					}else
					if(tNombreUsuario.getText().equals("")){
						MessageBox message = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.OK
										| SWT.ICON_INFORMATION);
						message.setText("Info");
						message.setMessage(bundle
								.getString("I02_dlg_nombreUsuario"));
						message.open();
					}else
					if(tPassword.getText().equals("")){
						MessageBox message = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.OK
										| SWT.ICON_INFORMATION);
						message.setText("Info");
						message.setMessage(bundle
								.getString("I02_dlg_clave"));
						message.open();
					}else
					if(cDepartamentos.getSelectionIndex()<0){
						MessageBox message = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.OK
										| SWT.ICON_INFORMATION);
						message.setText("Info");
						message.setMessage(bundle
								.getString("I02_dlg_departamento"));
						message.open();
					}else{
						
						vista.getControlador().insertDepartamentoPruebas(cDepartamentos.getItem(cDepartamentos.getSelectionIndex()), new Integer(tNombreUsuario.getText()));
					}
						
				}
			}
		});*/
		
		
		// Introducimos los valores y eventos de Cancelar
		bCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		/*bCancelar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tNombreUsuario.setText("");
				tNombre.setText("");
				tApellidos.setText("");
				bClaveManual.setSelection(true);
				bClaveAuto.setSelection(false);
				tPassword.setText("");
				tPassword.setEditable(true);
				
			}
		});*/
	}
	
	/*public void mostrarVentana() {		
		final Shell shell = new Shell (shellPadre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_chico = new Image(padre.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(padre.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_chica.gif"));
		
		final Image ico_esp = new Image(padre.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_esp.gif"));
		final Image ico_usa = new Image(padre.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_usa.gif"));
		final Image ico_pol = new Image(padre.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_pol.gif"));

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
		
		shell.setImage(ico_chico);
		shell.setImage(ico_chica);
		
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
				if (fechaNacimiento!=null)
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
				if (fechaContrato != null)
					tFContrato.setText(String.valueOf(fechaContrato.getDate()) + " de " + meses[fechaContrato.getMonth()]+ " de " + String.valueOf(fechaContrato.getYear()));				
			}
		};
		bFContrato.addSelectionListener(sabFContrato);
		
		// Listener para el selector de fecha de alta
		SelectionAdapter sabFAlta = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				fechaAlta = i17.getFecha(); 
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				if (fechaAlta != null)
				tFAlta.setText(String.valueOf(fechaAlta.getDate()) + " de " + meses[fechaAlta.getMonth()]+ " de " + String.valueOf(fechaAlta.getYear()));
			}
		};
		bFAlta.addSelectionListener(sabFAlta);
		
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
				else if (tFNacimiento.getText().length()==0 || tFContrato.getText().length()==0 || tFAlta.getText().length()==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_Fecha"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNombre y seleccionar texto
					//t.setFocus();
					//tNombre.selectAll();
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
				// Si todo está bien, inserta el empleado
				else {
					String cont = cContrato.getText();
					int id=0;
					
					for (int i=0; i<contratos.size(); i++) {
						String nombre = contratos.get(i).getNombreContrato();
						if (cont.equals(nombre)) {
							id = contratos.get(i).getNumeroContrato();
							break;
						}
					}

					Empleado emp = new Empleado(vista.getEmpleadoActual().getEmplId(), n, tNombre.getText(), tApell1.getText(), tApell2.getText(), fechaNacimiento, cSexo.getSelectionIndex(), tEMail.getText(), tPassword.getText(), cExperiencia.getSelectionIndex(), 0, id, fechaContrato, fechaAlta, null, cDepto.getText(), null, 0, cIdioma.getSelectionIndex(),0);
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
	}*/
}
