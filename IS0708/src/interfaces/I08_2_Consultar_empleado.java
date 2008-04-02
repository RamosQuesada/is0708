package interfaces;
/*******************************************************************************
 * INTERFAZ I-08.2 :: Ver de empleado
 *   por Jakub Chudzinski
 *   
 * Interfaz para dar de alta un empleado nuevo.
 * ver 0.1
 *******************************************************************************/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ColorDialog;

import aplicacion.datos.Empleado;
import aplicacion.utilidades.Util;
import impresion.Imprimir;

import java.util.Calendar;
import java.util.ResourceBundle;

public class I08_2_Consultar_empleado {
	private Shell padre = null;
	private Empleado empleado;
	private ResourceBundle bundle;
	public I08_2_Consultar_empleado(Shell padre, Empleado empleado, ResourceBundle bundle) {
		this.padre = padre;
		this.empleado = empleado;
		this.bundle = bundle;
		mostrarVentana();
	}
	
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);
		final Image ico_chico = new Image(padre.getDisplay(), I01_Login.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(padre.getDisplay(), I01_Login.class.getResourceAsStream("ico_chica.gif"));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		//layout.makeColumnsEqualWidth = true;

		shell.setLayoutData(layout);
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
		
		//grupoIzq
		final Label  lNVend			= new Label (grupoIzq, SWT.LEFT);
		final Text   tNVend			= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lPassword		= new Label (grupoIzq, SWT.LEFT);
		final Text   tPassword		= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lNombre		= new Label (grupoIzq, SWT.LEFT);
		final Text   tNombre		= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lApell1		= new Label (grupoIzq, SWT.LEFT);
		final Text   tApell1		= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lApell2		= new Label (grupoIzq, SWT.LEFT);
		final Text   tApell2		= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lEMail			= new Label (grupoIzq, SWT.LEFT);
		final Text   tEMail			= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lAnoNac		= new Label (grupoIzq, SWT.LEFT);
		final Text   tAnoNac		= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label  lSexo			= new Label (grupoIzq, SWT.LEFT);
		final Text   tSexo			= new Text  (grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		//grupoDer
		final Label  lContrato		= new Label (grupoDer, SWT.LEFT);
		final Text   tContrato		= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lExperiencia	= new Label (grupoDer, SWT.LEFT);
		final Text   tExperiencia	= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lDepto			= new Label (grupoDer, SWT.LEFT);
		final Text   tDepto			= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lFContrato		= new Label (grupoDer, SWT.LEFT);
		final Text   tFContrato		= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lFAlta			= new Label (grupoDer, SWT.LEFT);
		final Text   tFAlta			= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label  lColor			= new Label	(grupoDer, SWT.LEFT);
		final Text   tColor			= new Text  (grupoDer, SWT.BORDER | SWT.READ_ONLY);

		
		final Button bImprimir		= new Button(shell, SWT.PUSH);
		final Button bCancelar		= new Button(shell, SWT.PUSH);
		// Labels
		lNVend			.setText(bundle.getString("Vendedor"));
		lPassword		.setText(bundle.getString("Contrasena"));
		lEMail			.setText(bundle.getString("EMail"));
		lNombre			.setText(bundle.getString("Nombre"));
		lApell1			.setText(bundle.getString("I08_lab_Apellido1"));
		lApell2			.setText(bundle.getString("I08_lab_Apellido2"));
		lAnoNac			.setText(bundle.getString("I08_2_lab_FNacimiento"));
		lSexo			.setText(bundle.getString("Sexo"));
		lContrato		.setText(bundle.getString("I08_lab_TipoContrato"));
		lExperiencia	.setText(bundle.getString("Experiencia"));
		lDepto			.setText(bundle.getString("Departamento"));
		lFAlta			.setText(bundle.getString("I08_2_lab_FAlta"));
		lFContrato		.setText(bundle.getString("I08_2_lab_FContr"));
		lColor			.setText(bundle.getString("I08_2_lab_SelColor"));
		// pone datos de empleado
		try{
			tNVend			.setText(""+empleado.getEmplId());
			//tPassword		.setText(empleado.getPassword());
			tPassword		.setText("********");
			tEMail			.setText(empleado.getEmail());
			tNombre			.setText(empleado.getNombre());
			tApell1			.setText(empleado.getApellido1());
			tApell2			.setText(empleado.getApellido2());
			tAnoNac			.setText(""+empleado.getFechaNac().getDay()+"/"+empleado.getFechaNac().getMonth()+"/"+empleado.getFechaNac().getYear());
			tSexo			.setText(""+empleado.getSexo());
			tContrato		.setText("");
			tExperiencia	.setText("");
			tDepto			.setText("");
		 	tFAlta			.setText(""+empleado.getFechaNac().getDate());
		 	tFContrato		.setText(""+empleado.getFechaNac().getDate());
		 	tColor			.setBackground(empleado.getColor());
		}catch(Exception e){
		}
		
	 	
	 	
		

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
		tSexo		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lContrato	.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lExperiencia.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tExperiencia.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lDepto		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tDepto		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		lFAlta		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tFAlta		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		lColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		tColor		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		
		bImprimir	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		grupoIzq.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		grupoDer.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		String s; 
		if(empleado.getSexo()==0){
			s="femenino"; 
			shell.setImage(ico_chica);
		}else {
			s="masculino" ;
			shell.setImage(ico_chico);
		}
		tSexo.setText(s);
		String e; 
		if(empleado.getGrupo()==0)e="principiante"; else e="experto";
		tExperiencia.setText(e);
		tDepto.setText("");
		
		shell.setLayout(layout);
		
		bImprimir.setText(bundle.getString("Imprimir"));
		bImprimir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));



		
		// Un listener con lo que hace el bot�n bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};


		
		bCancelar.addSelectionListener(sabCancelar);
		bImprimir.addListener(SWT.PUSH, new Listener(){
			public void handleEvent(Event event) {
				Imprimir im = new Imprimir(padre.getDisplay());
				GC gc = new GC(shell);
				Image image = new Image(shell.getDisplay(), shell.getClientArea().height, shell.getClientArea().width);		
				gc.drawImage(image, shell.getClientArea().x, shell.getClientArea().y);
				
			}	
		});

		// Bot�n por defecto bAceptar
		shell.setDefaultButton(bImprimir);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
		


		
	}
	
}