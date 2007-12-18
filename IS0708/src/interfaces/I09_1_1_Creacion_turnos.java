package interfaces;
/*******************************************************************************
 * INTERFAZ I-09.1 :: Creaci�n de un turno
 *   por Daniel Dionne
 *   
 * Interfaz para crear un nuevo turno.
 * ver 0.1
 *******************************************************************************/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.ResourceBundle;
import java.util.ArrayList;
import org.eclipse.swt.graphics.*;

import aplicacion.Empleado;
import aplicacion.Posicion;
import aplicacion.Turno;

public class I09_1_1_Creacion_turnos {
	private Shell padre = null;
	private ResourceBundle bundle;
	private aplicacion.Turno turno;
	private int alto, ancho;
	public I09_1_1_Creacion_turnos(Shell padre, ResourceBundle bundle) {
		this.padre = padre;
		this.bundle = bundle;
		mostrarVentana();
	}
		
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);		
		shell.setText(bundle.getString("I09_lab_NuevoTurno"));
		shell.setLayout(new GridLayout(2,true));

		//Permite cerrar la ventana pulsando ESC
		shell.addListener (SWT.Traverse, new Listener () {
			public void handleEvent (Event event) {
				switch (event.detail) {
					case SWT.TRAVERSE_ESCAPE:
						shell.close ();
						event.detail = SWT.TRAVERSE_NONE;
						event.doit = false;
						break;
				}
			}
		});

		final Group grupo1 = new Group(shell, SWT.NONE);
		final Group grupo2 = new Group(shell, SWT.NO_BACKGROUND);
		grupo1.setText(bundle.getString("Nombre"));
		grupo2.setText(bundle.getString("Turno"));

// Grupo1 - Nombre
		grupo1.setLayout(new GridLayout(2,false));
		final Label  lNombre	= new Label (grupo1, SWT.LEFT);
		final Text   tNombre	= new Text  (grupo1, SWT.BORDER);

		final Label  lId		= new Label (grupo1, SWT.LEFT);
		final Text   tId		= new Text  (grupo1, SWT.BORDER);

		lNombre		.setText(bundle.getString("I09_lab_NombreTurno"));
		lNombre		.setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));
		tNombre		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		tNombre		.setToolTipText(bundle.getString("I09_tip_NombreTurno"));
		lId			.setText(bundle.getString("I09_lab_IdTurno"));
		lId			.setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));
		tId			.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		tId			.setToolTipText(bundle.getString("I09_tip_IdTurno"));

// Grupo 2 - Turno
		GridLayout g = new GridLayout(1,false);
		g.verticalSpacing=0;
		GridData gd = new GridData(SWT.FILL,SWT.FILL,false,true,1,1);
		gd.heightHint=30;
		grupo2.setLayout(g);
		grupo2.setLayoutData(gd);
		

		turno = new Turno(0,"nuevo turno","9:00:00","19:15:00","14:00:00",2);
		turno.anadeGUI(grupo2, 9, 23, 3, true, new Color(grupo2.getDisplay(),80,180,80));
		turno.anadeGUI(grupo2, 9, 23, 3, false, new Color(grupo2.getDisplay(),80,180,80));
		
		final Button bAceptar	= new Button(shell, SWT.PUSH);
		final Button bCancelar	= new Button(shell, SWT.PUSH);		
		
		bAceptar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		grupo1.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		grupo2.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		
		bAceptar	.setText(bundle.getString("Aceptar"));
		bCancelar	.setText(bundle.getString("Cancelar"));
		bAceptar	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		bCancelar	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		// Listener para el selector de fecha de alta
		SelectionAdapter sabFAlta = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
//				new I02PeticionFecha(shell, tFAlta);
			}
		};
//		bFAlta.addSelectionListener(sabFAlta);
		
		
		// Un listener con lo que hace el bot�n bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};

		// Un listener con lo que hace el bot�n bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			}
		};
		
		bCancelar.addSelectionListener(sabCancelar);
		bAceptar.addSelectionListener(sabAceptar);


		// Bot�n por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		shell.setSize(new Point(350, shell.getSize().y));
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}