package interfaces;
/*******************************************************************************
 * INTERFAZ I-09.1 :: Creación de un contrato
 *   por Daniel Dionne
 *   
 * Interfaz para crear un nuevo contrato.
 * ver 0.1
 *******************************************************************************/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import java.util.ResourceBundle;

public class I09_1 {
	private Shell padre = null;
	private ResourceBundle bundle;
	public I09_1(Shell padre, ResourceBundle bundle) {
		this.padre = padre;
		this.bundle = bundle;
		mostrarVentana();
	}
	
	
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);		
		shell.setText(bundle.getString("I09_lab_NuevoContrato"));
		shell.setLayout(new GridLayout(2,true));

		/*
		 * Permite cerrar la ventana pulsando escape
		 */
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
		final Group grupo2 = new Group(shell, SWT.NONE);
		final Group grupo3 = new Group(shell, SWT.NONE);
		
		grupo1.setText(bundle.getString("Contrato"));
		grupo2.setText(bundle.getString("I09_lab_Turnos"));
		grupo3.setText(bundle.getString("I09_lab_Ciclo"));


// Grupo1
		grupo1.setLayout(new GridLayout(2,false));
		final Label  lNombre		= new Label (grupo1, SWT.LEFT);
		final Text   tContrato		= new Text  (grupo1, SWT.BORDER);
		lNombre		.setText(bundle.getString("I09_lab_NombreContrato"));
		lNombre		.setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));
		tContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		tContrato	.setToolTipText(bundle.getString("I09_tip_NombreContrato"));

// Grupo 2
		grupo2.setLayout(new GridLayout(2,false));
		final List list = new List (grupo2, SWT.BORDER |  SWT.V_SCROLL);
		list.add("Turno 1");
		list.add("Turno 2");
		list.add("Turno 3");
		list.add("Turno 4");
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		final Button bNuevoTurno		= new Button(grupo2, SWT.PUSH);
		final Button bModificarTurno	= new Button(grupo2, SWT.PUSH);
		final Button bEliminarTurno		= new Button(grupo2, SWT.PUSH);
		
		bNuevoTurno		.setText("Nuevo turno");
		bModificarTurno	.setText("Modificar turno");
		bEliminarTurno	.setText("Eliminar turno");
		bNuevoTurno		.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,1));
		bModificarTurno	.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,1));
		bEliminarTurno	.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,1));
		
// Grupo 3
		grupo3.setLayout(new GridLayout(3,false));
		final Label  lLongCiclo		= new Label (grupo3, SWT.LEFT);
		final Text   tLongCiclo		= new Text  (grupo3, SWT.BORDER);
		final Button bAyuda			= new Button(grupo3, SWT.PUSH);
		lLongCiclo		.setText(bundle.getString("I09_lab_LongitudCiclo"));
		lLongCiclo		.setLayoutData	(new GridData(SWT.RIGHT,SWT.CENTER,true,true,1,1));
		tLongCiclo		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,true,1,1));
		tLongCiclo		.setTextLimit(2);
		tLongCiclo		.setToolTipText(bundle.getString("I09_tip_LongCiclo"));
		bAyuda			.setText("Ayuda");
		bAyuda			.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,true,1,1));
		
		final Composite cCiclo = new Composite(grupo3, SWT.NONE);
		int longCiclo = 26;
		int numTurnos = 3;
		cCiclo.setLayout(new GridLayout(longCiclo+1, false));
		Button b;
		Label l;
		for (int i = -1; i < numTurnos; i++) {
			if (i==-1)
				l = new Label(cCiclo,SWT.NONE);
			else {
				l = new Label(cCiclo,SWT.NONE);
				l.setText("Turno " + String.valueOf(i));
			}
			for (int j = 1; j <= longCiclo; j++) {
				if (i==-1) {
					l = new Label(cCiclo,SWT.NONE);
					l.setText(String.valueOf(j));
					l.setLayoutData(new GridData(SWT.CENTER,SWT.FILL,true,true,1,1));
				}
				else {
					b = new Button(cCiclo,SWT.CHECK);
					b.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
				}
			}			
		}
		cCiclo.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,true,3,1));
		
		

		final Button bAceptar		= new Button(shell, SWT.PUSH);
		final Button bCancelar		= new Button(shell, SWT.PUSH);		
		
		bAceptar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		grupo1.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		grupo2.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		grupo3.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		
		
		bAceptar	.setText(bundle.getString("Aceptar"));
		bCancelar	.setText(bundle.getString("Cancelar"));
		bAceptar	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bCancelar	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		// Listener para el selector de fecha de contrato
		SelectionAdapter sabFContrato = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
//				new I02PeticionFecha(shell, tFContrato);
			}
		};
//		bFContrato.addSelectionListener(sabFContrato);
		
		// Listener para el selector de fecha de alta
		SelectionAdapter sabFAlta = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
//				new I02PeticionFecha(shell, tFAlta);
			}
		};
//		bFAlta.addSelectionListener(sabFAlta);
		
		
		// Un listener con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};

		// Un listener con lo que hace el botón bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
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