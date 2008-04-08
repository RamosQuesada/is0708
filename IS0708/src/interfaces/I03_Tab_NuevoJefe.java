package interfaces;

import java.sql.Date;
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
	
	private Image ico_chico,ico_chica;	
	
	private ResourceBundle bundle;	
	
	private Vista vista;	
	
	private TabFolder tabFolder;	
	
	private String tmDep;
	
	private ArrayList<Contrato> contratos;
	
	private Date fechaContrato,fechaAlta,fechaNacimiento;
	
	private boolean colorSeleccionado;
	
	
	
	/**
	 * Constructor. Crea un TabItem para la creación de un nuevo jefe de departamento
	 * 
	 * @param tabFolder
	 * 				el tabFolder donde colocarlo
	 * @param bundle
	 * 				la herramienta de idiomas
	 * @param vista
	 * 				la vista de la aplicación
	 */  
	public I03_Tab_NuevoJefe(final TabFolder tabFolder,final ResourceBundle bundle,final Vista vista){
		
		//inicializamos las variables de la clase
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;
		this.colorSeleccionado = false;
		
		//cargamos las imagenes
		ico_chico = new Image(tabFolder.getDisplay(), 
				I03_Tab_NuevoJefe.class.getResourceAsStream("ico_chico.gif"));
		ico_chica = new Image(tabFolder.getDisplay(), 
				I03_Tab_NuevoJefe.class.getResourceAsStream("ico_chica.gif"));
		
		//creamos la nueva pestaña
		final TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText(bundle.getString("I03_admin_jefe"));
		tabItemEmpleados.setImage(ico_chico);

		// Creamos el contenido de la pestaña cuadrantes
		final Composite cNuevoJefe = new Composite(tabFolder, SWT.NONE);
		tabItemEmpleados.setControl(cNuevoJefe);

		cNuevoJefe.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
				0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 1;
		cNuevoJefe.setLayout(lGrupo);

		final Label lTitulo = new Label(cNuevoJefe, SWT.LEFT);
		lTitulo.setText(bundle.getString("I03_lab_etiquetaMarco"));
		final Composite cNuevoJefe2 = new Composite(cNuevoJefe,
				SWT.NONE);
		cNuevoJefe2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 0, 0));
		GridLayout lGrupo2 = new GridLayout();
		lGrupo2.numColumns = 2;
		cNuevoJefe2.setLayout(lGrupo2);
		
		
		//creamos el contenido del layout para rellenar los datos
		final Image ico_chico = new Image(tabFolder.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(tabFolder.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_chica.gif"));
		
		final Image ico_esp = new Image(tabFolder.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_esp.gif"));
		final Image ico_usa = new Image(tabFolder.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_usa.gif"));
		final Image ico_pol = new Image(tabFolder.getDisplay(), I08_1_Anadir_empleado.class.getResourceAsStream("ico_pol.gif"));

		GridLayout layout = new GridLayout(2,false);

		final Group grupoIzq = new Group(cNuevoJefe2, SWT.NONE);
		final Group grupoDer = new Group(cNuevoJefe2, SWT.NONE);
		grupoIzq.setText(bundle.getString("I03_lab_DatosPersonales"));
		grupoDer.setText(bundle.getString("I03_lab_DatosLaborales"));

		grupoIzq.setLayout(new GridLayout(2,false));
		grupoDer.setLayout(new GridLayout(2,false));
		
		
		final Label  lNVend			= new Label (grupoIzq, SWT.LEFT);
		final Text   tNVend			= new Text  (grupoIzq, SWT.BORDER);
		final Button bClaveAuto     = new Button(grupoIzq, SWT.RADIO);
		final Button bClaveManual   = new Button(grupoIzq, SWT.RADIO);
		final Label lContra         = new Label(grupoIzq, SWT.LEFT);
		final Text tPassword        = new Text(grupoIzq, SWT.BORDER);
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
		
		final Composite cAceptarCancelar = new Composite(cNuevoJefe,
				SWT.NONE);
		cAceptarCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.DOWN,
				false, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		// Botones aceptar y cancelar
		final Button bAceptar = new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar = new Button(cAceptarCancelar, SWT.PUSH);
		
		lNVend			.setText(bundle.getString("Vendedor"));
		bClaveAuto		.setText(bundle.getString("I03_lab_claveAuto"));
		bClaveManual	.setText(bundle.getString("I03_lab_claveManual"));
		lContra			.setText(bundle.getString("I03_lab_clave"));
		lEMail			.setText(bundle.getString("I03_lab_Email"));
		lNombre			.setText(bundle.getString("Nombre"));
		lApell1			.setText(bundle.getString("I03_lab_apellido1"));
		lApell2			.setText(bundle.getString("I03_lab_apellido2"));
		bFNacimiento	.setText(bundle.getString("I03_lab_FNacimiento"));
		lSexo			.setText(bundle.getString("Sexo"));
		lIdioma			.setText(bundle.getString("Idioma"));
		lContrato		.setText(bundle.getString("I03_lab_TipoContrato"));
		lExperiencia	.setText(bundle.getString("Experiencia"));
		lDepto			.setText(bundle.getString("Departamento"));
		bFAlta			.setText(bundle.getString("I03_lab_FAlta"));
		bFContrato		.setText(bundle.getString("I03_lab_FContr"));
		bColor			.setText(bundle.getString("I03_lab_SelColor"));

		lNVend		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tNVend		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
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
		cContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
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
		
		
		
		grupoIzq.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		grupoDer.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		bClaveManual.setSelection(true);
		bClaveAuto.setSelection(false);
		tPassword.setEditable(true);
		
		tNVend.setTextLimit(8);
		tNombre.setTextLimit(20);
		tApell1.setTextLimit(20);
		tApell2.setTextLimit(20);
		tEMail.setTextLimit(30);
		
		cSexo.setItems (new String [] {	bundle.getString("Femenino"),
										bundle.getString("Masculino")});
		cIdioma.setItems (new String [] {	bundle.getString("esp"),
											bundle.getString("eng"),
											bundle.getString("pol")});
		
		cExperiencia.setItems (new String [] {	bundle.getString("Principiante"),
												bundle.getString("Experto")});
		
		// Cogemos los contratos
		ArrayList <Empleado> jefes=vista.getEmpleados(null, null, null, null, null, null, 2);
		final ArrayList <Contrato> contratosJefes=new ArrayList();
		for (int j=0;j<jefes.size();j++){
			contratosJefes.add(jefes.get(j).getContrato(vista));
		}
		for (int i=1; i<contratosJefes.size(); i++)
			cContrato.add(contratosJefes.get(i).getNombreContrato());
		
		
		
		cSexo.select(1); //sexo masculino por defecto
		//cContrato.select(0);
		cExperiencia.select(0);
		cDepto.select(0);
		cIdioma.select(0); //español por defecto
				
		// Introducimos los textos a los botones
		bAceptar.setText(bundle.getString("Aceptar"));
		bCancelar.setText(bundle.getString("Cancelar"));
		// Introducimos los valores y eventos de Aceptar

		bAceptar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		
		
		bAceptar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
								
				//comprobamos si el campo numero de vendedor esta rellenado
				if(tNVend.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_numero"));
					message.open();
				}else
					
				//comprobamos si el campo numero de vendedor es correcto	
				if(Util.convertirNVend(tNVend.getText())<0){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_numeroFormato"));
					message.open();
				}else	
				
				//comprobamos si el campo de la contraseña es correcta
				if(tPassword.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_clave"));
					message.open();
				}else
					
				//Comprobamos si campo nombre esta rellenado
				if(tNombre.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_nombre"));
					message.open();
				}else
					
				//comprobamos si el campo apellido1 esta relleno
				if(tApell1.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_apellido1"));
					message.open();
				}else
					
				//comprobamos si el campo apellido2 esta relleno
				if(tApell2.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_apellido2"));
					message.open();
				}else
					
				//comprobamos si el campo Email esta relleno
				if(tEMail.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_email"));
					message.open();
				}else
				
				//Comprobamos si el campo Email esta bien estructurado
				if(tEMail.getText().indexOf("@")<0||tEMail.getText().indexOf(".")<0||tEMail.getText().indexOf("@")>tEMail.getText().indexOf(".")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_emailFormato"));
					message.open();
				}else
					
				//comprobamos si ha seleccionado una fecha de nacimiento
				if(cContrato.getSelectionIndex()<0){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_contrato"));
					message.open();
				}else
					
				//comprobamos si ha seleccionado un contrato
				if(tFNacimiento.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_fechaNac"));
					message.open();
				}else	
				//comrpobamos si ha seleccionado una fecha de inicio de contrato
				if(tFContrato.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_fechaInicioCont"));
					message.open();
				}else
					
				//comprobamos si ha seleccionado una fecha de alta en el departamento
				if(tFAlta.getText().equals("")){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_fechaAltaDept"));
					message.open();
				}else
				
				//Comprobamos si ha seleccionado algún color
				if(colorSeleccionado == false){
					MessageBox message = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					message.setText("Info");
					message.setMessage(bundle
							.getString("I03_err_color"));
					message.open();
				}else{
					
					//Se han rellenado todos los datos correctamente
					//muestra un mensaje de confirmación de creación
					MessageBox messageBox = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
					messageBox.setText(bundle
							.getString("I03_dlg_confirmacion"));
					messageBox.setMessage(bundle
							.getString("I03_dlg_pregunta"));
					if (messageBox.open() == SWT.YES) {	
						
						int id = contratosJefes.get(cContrato.getSelectionIndex()).getNumeroContrato();
						//insertamos el nuevo jefe en la base de datos
						//vista.getEmpleadoActual().getEmplId()
						Empleado emp = new Empleado(null, Util.convertirNVend(tNVend.getText()), tNombre.getText(), tApell1.getText(), tApell2.getText(), fechaNacimiento, 1-cSexo.getSelectionIndex(), tEMail.getText(), tPassword.getText(), cExperiencia.getSelectionIndex(), 2, id, fechaContrato, fechaAlta, null, cDepto.getText(), null, 0, cIdioma.getSelectionIndex(),-1);
						vista.getControlador().insertEmpleado(emp);
					}
				}
			}
		});
		
		
		
		// Introducimos los valores y eventos de Cancelar
		bCancelar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tNVend.setText("");
				tPassword.setText("");
				tNombre.setText("");
				tApell1.setText("");
				tApell2.setText("");
				tEMail.setText("");
				tFNacimiento.setText("");
				tFContrato.setText("");
				tFAlta.setText("");
				bClaveManual.setSelection(true);
				bClaveAuto.setSelection(false);				
				tPassword.setEditable(true);
				
			}
		});
		
		
		// Listener de el radio buttom clave manual
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
		
		
		
		// Listener de el radio buttom clave manual
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
		 
		
		// Listener para el selector de sexo
		SelectionAdapter sacSexo = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (cSexo.getSelectionIndex()==0)
					tabItemEmpleados.setImage(ico_chica);
				else
					tabItemEmpleados.setImage(ico_chico);
			}
		};
		cSexo.addSelectionListener(sacSexo);
		
		
		// Listener para el selector de fecha de nacimiento
		SelectionAdapter sabFNacimiento = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(tabFolder.getShell());
				while (!i17.isDisposed()) {
					if (!tabFolder.getDisplay().readAndDispatch()) {
						tabFolder.getDisplay().sleep();
					}
				}
				fechaNacimiento = i17.getFecha();
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fechaNacimiento!=null)
				tFNacimiento.setText(String.valueOf(fechaNacimiento.getDate()) + " de " + meses[fechaNacimiento.getMonth()]+ " de " + String.valueOf(fechaNacimiento.getYear()));
			}
		};
		bFNacimiento.addSelectionListener(sabFNacimiento);

		
		
		// Listener para el selector de fecha de contrato
		SelectionAdapter sabFContrato = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(tabFolder.getShell());
				while (!i17.isDisposed()) {
					if (!tabFolder.getDisplay().readAndDispatch()) {
						tabFolder.getDisplay().sleep();
					}
				}
				fechaContrato = i17.getFecha(); 				
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fechaContrato != null)
					tFContrato.setText(String.valueOf(fechaContrato.getDate()) + " de " + meses[fechaContrato.getMonth()]+ " de " + String.valueOf(fechaContrato.getYear()));				
			}
		};
		bFContrato.addSelectionListener(sabFContrato);
		
		
		
		// Listener para el selector de fecha de alta
		SelectionAdapter sabFAlta = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(tabFolder.getShell());
				while (!i17.isDisposed()) {
					if (!tabFolder.getDisplay().readAndDispatch()) {
						tabFolder.getDisplay().sleep();
					}
				}
				fechaAlta = i17.getFecha(); 
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fechaAlta != null)
				tFAlta.setText(String.valueOf(fechaAlta.getDate()) + " de " + meses[fechaAlta.getMonth()]+ " de " + String.valueOf(fechaAlta.getYear()));
			}
		};
		bFAlta.addSelectionListener(sabFAlta);
		
		
		
		// Listener para el selector de color
		SelectionAdapter sabColor = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				ColorDialog cd = new ColorDialog(tabFolder.getShell());
				cd.setText(bundle.getString("I03_lab_SelColor"));
				cd.setRGB(new RGB(255, 255, 255));
				RGB newColor = cd.open();
				if (newColor != null) {
					colorSeleccionado = true;
					lColor.setBackground(new Color(tabFolder.getShell().getDisplay(), newColor));
				}
			}
		};
		bColor.addSelectionListener(sabColor);
	}
	

}
