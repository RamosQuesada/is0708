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
import org.eclipse.swt.custom.ScrolledComposite;

import java.util.ResourceBundle;
import org.eclipse.swt.graphics.*;

public class I09_1 {
	private Shell padre = null;
	private ResourceBundle bundle;
	
	private final int longCicloDefault = 14;
	public I09_1(Shell padre, ResourceBundle bundle) {
		this.padre = padre;
		this.bundle = bundle;
		crearVentana();
	}
	/**
	 * Esta clase muestra una ventana con casillas para marcar los turnos.
	 * Dibuja tantas filas como turnos, y tantas columnas como días haya en el ciclo. 
	 * @author Daniel
	 *
	 */
	private class CheckBoxes {
		private Button[][] cbs;
		private Shell shell;
		private Shell padre;
		public CheckBoxes (Shell padre, int longCiclo, int numTurnos) {
			this.padre = padre;
			shell = new Shell(SWT.APPLICATION_MODAL | SWT.CLOSE);
			shell.setLayout(new GridLayout(2,false));
			shell.setText(bundle.getString("I09_lab_ConfigurarCiclo"));
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
			// Este tipo de composite permite que la ventana pueda ser más estrecha
			// que el contenido pero que se siga pudiendo ver todo
			final ScrolledComposite sc = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			sc.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			
			// c1 contiene las casillas, y está dentro del scroll
			final Composite c1 = new Composite(sc, SWT.NONE);
			// Label de ayuda
			final Label lInfo			= new Label(shell, SWT.WRAP);
			// c2 tiene los botones Aceptar y Cancelar
			final Composite c2 = new Composite(shell, SWT.NONE);
			c1.setLayout(new GridLayout(longCiclo+1, false));
			c2.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			lInfo	.setText(bundle.getString("I09_info_AyudaCiclos"));
			lInfo	.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,true,1,2));
			lInfo	.setSize(200, 200);
			c2.setLayout(new GridLayout(2, true));
			c2.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			
			// Creación de las casillas. Se guardan en una matriz para poder recuperar
			// sus valores
			cbs = new Button[numTurnos][longCiclo];
			Label l;
			for (int i = -1; i < numTurnos; i++) {
				if (i==-1)
					l = new Label(c1,SWT.NONE);
				else {
					l = new Label(c1,SWT.NONE);
					l.setText("Turno " + String.valueOf(i+1));
				}
				for (int j = 1; j <= longCiclo; j++) {
					if (i==-1) {
						l = new Label(c1,SWT.NONE);
						l.setText(String.valueOf(j));
						if (j%7 ==0)
							l.setForeground(new Color(c1.getShell().getDisplay(),255,0,0));
						l.setLayoutData(new GridData(SWT.CENTER,SWT.FILL,true,true,1,1));
					}
					else {
						cbs[i][j-1] = new Button(c1,SWT.CHECK);
						cbs[i][j-1].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
					}
				}			
			}
			
			c1.pack();
			sc.setContent(c1);
			final Button bAceptar		= new Button(c2, SWT.PUSH);
			final Button bCancelar		= new Button(c2, SWT.PUSH);
			bAceptar	.setText(bundle.getString("Aceptar"));
			bCancelar	.setText(bundle.getString("Cancelar"));
			bAceptar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
			bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
			// Un listener con lo que hace el botón bCancelar
			SelectionAdapter sabCancelar = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
						shell.dispose();	
				}
			};
			bCancelar.addSelectionListener(sabCancelar);
			
			// TODO La acción del botón Aceptar
			shell.setDefaultButton(bAceptar);
			shell.pack();
			
			// Evitar que la ventana se salga de la pantalla
			if (shell.getSize().x > shell.getDisplay().getClientArea().width-100)
				shell.setSize(shell.getDisplay().getClientArea().width-100, shell.getSize().y);
			
			// Mostrar centrada sobre el padre
			shell.setLocation(this.padre.getBounds().width/2 + this.padre.getBounds().x - shell.getSize().x/2, this.padre.getBounds().height/2 + this.padre.getBounds().y - shell.getSize().y/2);
			shell.open();
		}
	}

	/**
	 * Crea el contenido de la ventana
	 */
	public void crearVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);		
		shell.setText(bundle.getString("I09_lab_NuevoContrato"));
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
		final Group grupo2 = new Group(shell, SWT.NONE);
		final Group grupo3 = new Group(shell, SWT.NONE);
		
		grupo1.setText(bundle.getString("Contrato"));
		grupo2.setText(bundle.getString("I09_lab_Turnos"));
		grupo3.setText(bundle.getString("I09_lab_Ciclo"));

// Grupo1 - Contrato
		grupo1.setLayout(new GridLayout(2,false));
		final Label  lNombre		= new Label (grupo1, SWT.LEFT);
		final Text   tContrato		= new Text  (grupo1, SWT.BORDER);
		lNombre		.setText(bundle.getString("I09_lab_NombreContrato"));
		lNombre		.setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));
		tContrato	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		tContrato	.setToolTipText(bundle.getString("I09_tip_NombreContrato"));

// Grupo 2 - Turnos
		grupo2.setLayout(new GridLayout(2,false));
		final List list = new List (grupo2, SWT.BORDER |  SWT.V_SCROLL);
		list.add("Turno 1");
		list.add("Turno 2");
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
		
// Grupo 3 - Ciclo
		grupo3.setLayout(new GridLayout(4,false));
		final Label  lLongCiclo		= new Label (grupo3, SWT.LEFT);
		final Text   tLongCiclo		= new Text  (grupo3, SWT.BORDER);
		final Button bLCCambiar = new Button(grupo3, SWT.PUSH);
		final Button bAyuda			= new Button(grupo3, SWT.PUSH);
		lLongCiclo		.setText(bundle.getString("I09_lab_LongitudCiclo"));
		lLongCiclo		.setLayoutData	(new GridData(SWT.RIGHT,SWT.CENTER,false,true,1,1));
		tLongCiclo		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,true,1,1));
		tLongCiclo		.setTextLimit(2);
		tLongCiclo		.setToolTipText(bundle.getString("I09_tip_LongCiclo"));
		tLongCiclo		.setText(String.valueOf(longCicloDefault));
		bLCCambiar.setText(bundle.getString("I09_but_Configurar"));
		bLCCambiar.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,true,1,1));

		bAyuda			.setText(bundle.getString("Ayuda"));
		bAyuda			.setLayoutData	(new GridData(SWT.RIGHT,SWT.FILL,true,true,1,1));

// Parte inferior de la ventana
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
		
		// Listener para el botón de configurar ciclo
		SelectionAdapter sabLCCambiar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				try {
					new CheckBoxes(shell, Integer.valueOf(tLongCiclo.getText()), 3);	
				} catch (Exception ex) {
					MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_WARNING);
					messageBox.setText(bundle.getString("Error"));
					messageBox.setMessage(bundle.getString("I09_err_LongCiclo"));
					messageBox.open();
					tLongCiclo.setFocus();
					tLongCiclo.setSelection(0,2);
				}				
			}
		};
		bLCCambiar.addSelectionListener(sabLCCambiar);
		
		// Listener para el botón de nuevo turno
		SelectionAdapter sabNuevoTurno = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				new I09_1_1(shell, bundle);
			}
		};
		bNuevoTurno.addSelectionListener(sabNuevoTurno);
		
		// TODO Listener para el botón bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			}
		};		
		bAceptar.addSelectionListener(sabAceptar);
		
		// Listener para el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};
		bCancelar.addSelectionListener(sabCancelar);

		shell.setDefaultButton(bAceptar);
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}