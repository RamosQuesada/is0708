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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class I08_1 {
	private Shell padre = null;

	public I08_1(Shell padre) {
		this.padre = padre;
		mostrarVentana();
	}
	
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_chico = new Image(padre.getDisplay(), I01.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(padre.getDisplay(), I01.class.getResourceAsStream("ico_chica.gif"));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		//layout.makeColumnsEqualWidth = true;

		final Group grupoIzq = new Group(shell, SWT.NONE);
		final Group grupoDer = new Group(shell, SWT.NONE);
		grupoIzq.setText("Datos personales");
		grupoDer.setText("Datos laborales");

		GridLayout lGrupoIzq = new GridLayout();
		lGrupoIzq.numColumns = 2;
		grupoIzq.setLayout(lGrupoIzq);
		GridLayout lGrupoDer = new GridLayout();
		lGrupoDer.numColumns = 2;
		grupoDer.setLayout(lGrupoDer);
		
		final Label lNVend			= new Label(grupoIzq, SWT.LEFT);
		final Text  tNVend			= new Text (grupoIzq, SWT.BORDER);
		final Label lPassword		= new Label(grupoIzq, SWT.LEFT);
		final Text  tPassword		= new Text (grupoIzq, SWT.BORDER);
		final Label lNombre			= new Label(grupoIzq, SWT.LEFT);
		final Text  tNombre			= new Text (grupoIzq, SWT.BORDER);
		final Label lApell1			= new Label(grupoIzq, SWT.LEFT);
		final Text  tApell1			= new Text (grupoIzq, SWT.BORDER);
		final Label lApell2			= new Label(grupoIzq, SWT.LEFT);
		final Text  tApell2			= new Text (grupoIzq, SWT.BORDER);
		final Label lAnoNac			= new Label(grupoIzq, SWT.LEFT);
		final Text  tAnoNac			= new Text (grupoIzq, SWT.BORDER);
		final Label lSexo			= new Label(grupoIzq, SWT.LEFT);
		final Combo cSexo			= new Combo(grupoIzq, SWT.BORDER | SWT.READ_ONLY);
		final Label lContrato		= new Label(grupoDer, SWT.LEFT);
		final Combo cContrato		= new Combo(grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label lExperiencia	= new Label(grupoDer, SWT.LEFT);
		final Combo cExperiencia	= new Combo(grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Label lDepto			= new Label(grupoDer, SWT.LEFT);
		final Combo cDepto			= new Combo(grupoDer, SWT.BORDER | SWT.READ_ONLY);
		final Button bAceptar		= new Button(shell, SWT.PUSH);
		final Button bCancelar		= new Button(shell, SWT.PUSH);
		
		lNVend.setText("Vendedor");
		lPassword.setText("Contraseña");
		lNombre.setText("Nombre");
		lApell1.setText("1er apellido");
		lApell2.setText("2º  apellido");
		lAnoNac.setText("Año de nacimiento");
		lSexo.setText("Sexo");
		lContrato.setText("Tipo de contrato");
		lExperiencia.setText("Experiencia");
		lDepto.setText("Departamento");
		

		lNVend.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tNVend.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		lPassword.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tPassword.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lNombre.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tNombre.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lApell1.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tApell1.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lApell2.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tApell2.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lAnoNac.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tAnoNac.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lSexo.setLayoutData			(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cSexo.setLayoutData			(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lContrato.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cContrato.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lExperiencia.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cExperiencia.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		lDepto.setLayoutData		(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		cDepto.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bAceptar.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		
		grupoIzq.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		grupoDer.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		cSexo.setItems (new String [] {"Femenino", "Masculino",});
		cContrato.setItems (new String [] {"6:40", "Días sueltos",});
		cExperiencia.setItems (new String [] {"Iniciación", "Profesional",});
		cDepto.setItems (new String [] {"Cocina", "Baño",});
		cSexo.select(0);
		cContrato.select(0);
		cExperiencia.select(0);
		cDepto.select(0);
		
		shell.setImage(ico_chico);

		shell.setImage(ico_chica);
		shell.setText("Nuevo empleado");
		shell.setLayout(layout);
		
		bAceptar.setText("Aceptar");
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		bCancelar.setText("Cancelar");
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		SelectionAdapter sacSexo = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (cSexo.getSelectionIndex()==0)
					shell.setImage(ico_chica);
				else
					shell.setImage(ico_chico);
			}
		};
		
		cSexo.addSelectionListener(sacSexo);
		
		// Un SelectionAdapter con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};

		// Un SelectionAdapter con lo que hace el botón bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tNVend.getText().length()!=8) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText ("Mensaje");
					messageBox.setMessage ("El número de vendedor debe tener 8 cifras.");
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNVend y seleccionar texto
					tNVend.setFocus();
					tNVend.selectAll();
				}
				else shell.dispose();
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