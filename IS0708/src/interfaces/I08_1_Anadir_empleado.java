package interfaces;
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
import aplicacion.Util;
import aplicacion.Database;
import java.util.ResourceBundle;

// TODO Mostrar elección de rangos inferiores al usuario
public class I08_1_Anadir_empleado {
	private Shell padre = null;
	private ResourceBundle bundle;
	private Database db;
	public I08_1_Anadir_empleado(Shell padre, ResourceBundle bundle, Database db) {
		this.padre = padre;
		this.bundle = bundle;
		this.db = db;
		mostrarVentana();
	}
	
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_chico = new Image(padre.getDisplay(), I01_Login.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(padre.getDisplay(), I01_Login.class.getResourceAsStream("ico_chica.gif"));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		//layout.makeColumnsEqualWidth = true;

		final Group grupoIzq = new Group(shell, SWT.NONE);
		final Group grupoDer = new Group(shell, SWT.NONE);
		grupoIzq.setText(bundle.getString("I08_lab_DatosPersonales"));
		grupoDer.setText(bundle.getString("I08_lab_DatosLaborales"));

		GridLayout lGrupoIzq = new GridLayout();
		lGrupoIzq.numColumns = 2;
		grupoIzq.setLayout(lGrupoIzq);
		GridLayout lGrupoDer = new GridLayout();
		lGrupoDer.numColumns = 2;
		grupoDer.setLayout(lGrupoDer);
		
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
		final Label  lAnoNac		= new Label (grupoIzq, SWT.LEFT);
		final Text   tAnoNac		= new Text  (grupoIzq, SWT.BORDER);
		final Label  lSexo			= new Label (grupoIzq, SWT.LEFT);
		final Combo  cSexo			= new Combo (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
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
		lPassword		.setText(bundle.getString("Contraseña"));
		lEMail			.setText(bundle.getString("EMail"));
		lNombre			.setText(bundle.getString("Nombre"));
		lApell1			.setText(bundle.getString("I08_lab_Apellido1"));
		lApell2			.setText(bundle.getString("I08_lab_Apellido2"));
		lAnoNac			.setText(bundle.getString("I08_lab_AnoNacimiento"));
		lSexo			.setText(bundle.getString("Sexo"));
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
		lAnoNac		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tAnoNac		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lSexo		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cSexo		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
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
		
		cSexo.setItems (new String [] {bundle.getString("Femenino"), bundle.getString("Masculino"),});
		cContrato.setItems (new String [] {"6:40", "Días sueltos",});
		cExperiencia.setItems (new String [] {bundle.getString("Principiante"), bundle.getString("Experto"),});
		cDepto.setItems (new String [] {"Cocina", "Baño",});
		cSexo.select(0);
		cContrato.select(0);
		cExperiencia.select(0);
		cDepto.select(0);
		
		shell.setImage(ico_chico);

		shell.setImage(ico_chica);
		shell.setText(bundle.getString("I08_but_NuevoEmpleado"));
		shell.setLayout(layout);
		
		bAceptar.setText(bundle.getString("Aceptar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		// Un listener para el selector de sexo
		SelectionAdapter sacSexo = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (cSexo.getSelectionIndex()==0)
					shell.setImage(ico_chica);
				else
					shell.setImage(ico_chico);
			}
		};
		cSexo.addSelectionListener(sacSexo);
		
		// Listener para el selector de fecha de contrato
		SelectionAdapter sabFContrato = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				new I17_Peticion_fecha(shell, tFContrato);
			}
		};
		bFContrato.addSelectionListener(sabFContrato);
		
		// Listener para el selector de fecha de alta
		SelectionAdapter sabFAlta = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				new I17_Peticion_fecha(shell, tFAlta);
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

		
		// Un listener con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};

		// Un listener con lo que hace el botón bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Comprueba el número de vendedor (campo obligatorio)
				int n = Util.convertirNVend(tNVend.getText());
				if (n<0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I01_err_NumVendedor"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNVend y seleccionar texto
					tNVend.setFocus();
					tNVend.selectAll();
				}
				// Comprueba la dirección de email (campo no obligatorio)
				else if (tEMail.getText().length()!=0 && !Util.comprobarEmail(tEMail.getText())) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I08_err_EmailNoValido"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tEMail y seleccionar texto
					tEMail.setFocus();
					tEMail.selectAll();
				}
				else {
					// TODO BD Guardar empleado nuevo con todos sus datos en la BD
					// Daniel Dionne :: Ya estoy yo con esta
					//db.insertarUsuario(n, tNombre.getText(), tApell1.getText(), tApell2.getText(), , cSexo.getText(), tEMail.getText(), tPassword.getText(), indicadorGrupo, fechaContrato, fechaEntrada, horasExtras, idDept, rango, idContrato, idTurno)
					
					shell.dispose();
				}
			}
		};
		
		bCancelar.addSelectionListener(sabCancelar);
		bAceptar.addSelectionListener(sabAceptar);

		// Botón por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}