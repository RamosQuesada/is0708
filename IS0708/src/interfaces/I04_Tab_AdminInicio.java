package interfaces;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import aplicacion.Vista;

/**
 * Clase que implementa la interfaz del tab de inicio y configuracion 
 * de la base de datos del menu de administrador
 * @author Jose Maria Martin
 */
public class I04_Tab_AdminInicio {
	private ResourceBundle bundle;

	private Vista vista;

	private TabFolder tabFolder;
	
	private Image ico_cuadrante;

	public I04_Tab_AdminInicio(final ResourceBundle bundle, final Vista vista,
			final TabFolder tabFolder) {
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;
		ico_cuadrante = new Image(tabFolder.getDisplay(), I04_Tab_AdminInicio.class
				.getResourceAsStream("ico_cuadrante.gif"));
		
		TabItem tabItemAdminInicio = new TabItem(tabFolder, SWT.NONE);
		tabItemAdminInicio.setText(bundle.getString("I02_admin_inicio"));
		tabItemAdminInicio.setImage(ico_cuadrante);

		// Creamos el contenido de la pestaña cuadrantes

		Composite cInicio = new Composite(tabFolder, SWT.NONE);
		tabItemAdminInicio.setControl(cInicio);

		Image _fondo_turnomatic;
		_fondo_turnomatic = new Image(tabFolder.getDisplay(), I04_Tab_AdminInicio.class
				.getResourceAsStream("admin_fondo.jpg"));

		cInicio
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
						1));

		// Le añadimos un layout
		GridLayout lInicio = new GridLayout();
		lInicio.numColumns = 2;
		cInicio.setLayout(lInicio);

		final Label bienvenido = new Label(cInicio, SWT.None);
		bienvenido.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true,
				2, 1));
		bienvenido.setText(bundle.getString("I02_bienvenido"));
		//Color color = new Color(display,0, 0, 0);
		//bienvenido.setBackground(color);

		Image logo = new Image(tabFolder.getDisplay(), I02_Principal.class
				.getResourceAsStream("LogoTM.png"));
		final Label lLogo = new Label(cInicio, SWT.None);
		lLogo.setImage(logo);
		lLogo
				.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 2,
						1));

		final Label lConfig = new Label(cInicio, SWT.None);
		lConfig.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1,
				1));
		lConfig.setText(bundle.getString("I02_configBD"));

		final Button configBD = new Button(cInicio, SWT.PUSH);
		configBD
				.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		configBD.setText("CONFIG BD");
		configBD.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				MessageBox messageBox = new MessageBox(tabFolder.getShell(),
						SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | SWT.OK
								| SWT.CANCEL);
				messageBox.setText("Reset BD");
				messageBox.setMessage(bundle.getString("I30_confirm_config"));
				if(messageBox.open()==SWT.OK){
					new I30_Info_BD(tabFolder.getShell(), bundle, vista);
				}
			}
		});

		final Label lReset = new Label(cInicio, SWT.None);
		lReset.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false,
				1, 1));
		lReset.setText(bundle.getString("I02_resetBD"));

		final Button resetBD = new Button(cInicio, SWT.PUSH);
		resetBD.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false,
				1, 1));
		resetBD.setText("RESET BD");
		resetBD.setEnabled(false);
		resetBD.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				MessageBox messageBox = new MessageBox(tabFolder.getShell(),
						SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | SWT.OK
								| SWT.CANCEL);
				messageBox.setText("Reset BD");
				messageBox.setMessage(bundle.getString("I02_confirm_reset"));
				int response = messageBox.open();
				if (response == SWT.OK) {
					System.out.println("BBDD reiniciada");
					// paquete_pruebas.GeneraDatos.reset();
					paquete_pruebas.InsertaDatosFijos.insertar();
				}
			}
		});

		cInicio.setBackgroundImage(_fondo_turnomatic);
	}
	
	
	
}
