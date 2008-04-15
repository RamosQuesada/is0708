package interfaces.admin;


import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
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
public class TabInicio {
	private ResourceBundle bundle;
	private Vista vista;
	private TabFolder tabFolder;

	public TabInicio(final ResourceBundle bundle, final Vista vista,
		final TabFolder tabFolder) {
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;
		
		TabItem tabItemAdminInicio = new TabItem(tabFolder, SWT.NONE);
		tabItemAdminInicio.setText(bundle.getString("I02_admin_inicio"));
		tabItemAdminInicio.setImage(vista.getImagenes().getIco_cuadrante());

		// Creamos el contenido de la pestaña cuadrantes

		Composite cInicio = new Composite(tabFolder, SWT.NONE);
		tabItemAdminInicio.setControl(cInicio);


		cInicio.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Le añadimos un layout
		GridLayout l = new GridLayout(2, false);
		l.verticalSpacing = 0;
		l.marginLeft=0;
		l.marginBottom=0;
		l.marginRight=0;
		l.marginTop=0;
		cInicio.setLayout(l);

		final Label lFondo = new Label(cInicio, SWT.NONE);
		lFondo.setImage(vista.getImagenes().getFondoAdmin());
		lFondo.setLayoutData(new GridData(SWT.FILL, SWT.TOP , false, false, 1,1));
		lFondo.setBackground(new Color(vista.getDisplay(), 210, 165, 62));

		Composite compDer = new Composite(cInicio, SWT.NONE);
		compDer.setLayout(new GridLayout(2, false));
		compDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,2));

		final Label lFondo2 = new Label(cInicio, SWT.NONE);
		lFondo2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,1));
		lFondo2.setBackground(new Color(vista.getDisplay(), 210, 165, 62));

		final Label bienvenido = new Label(compDer, SWT.WRAP);
		bienvenido.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		bienvenido.setText(bundle.getString("I02_bienvenido"));

		final Label lConfig = new Label(compDer, SWT.WRAP);
		lConfig.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
		lConfig.setText(bundle.getString("I02_configBD"));

		final Button configBD = new Button(compDer, SWT.PUSH);
		configBD.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
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
					new ShellInfoBD(tabFolder.getShell(), bundle, vista);
				}
			}
		});

		final Label lReset = new Label(compDer, SWT.WRAP);
		lReset.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 1, 1));
		lReset.setText(bundle.getString("I02_resetBD"));

		final Button resetBD = new Button(compDer, SWT.PUSH);
		resetBD.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
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
					// paquete_pruebas.GeneraDatos.reset();
					paquete_pruebas.InsertaDatosFijos.insertarNdepart(20);
					System.out.println("BBDD reiniciada");
				}
			}
		});
	}
}
