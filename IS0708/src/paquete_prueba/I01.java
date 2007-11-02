/*******************************************************************************
 * INTERFAZ I-01 :: Identificaci�n
 *   por Daniel Dionne
 *   
 * Interfaz de identificaci�n del usuario.
 *******************************************************************************/


package paquete_prueba;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class I01 {

	public static void main(String[] IS0708) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);

		final Label lUsuario = new Label(shell, SWT.LEFT);
		final Text tUsuario  = new Text(shell, SWT.BORDER);
		final Label lPassword = new Label(shell, SWT.LEFT);
		final Text tPassword = new Text(shell, SWT.BORDER);

	
		final Button bAceptar = new Button(shell, SWT.PUSH);
		final Button bCancelar = new Button(shell, SWT.PUSH);

		shell.setText("Identificaci�n del usuario");
		shell.setVisible(true);

		GridLayout layout = new GridLayout();
		layout.numColumns = 8;
		layout.makeColumnsEqualWidth = true;

		shell.setLayout(layout);

		lUsuario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lUsuario.setText("Vendedor");
		tUsuario.setEditable(true);
		tUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 5, 1));

		lPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lPassword.setText("Contrase�a");
		tPassword.setEditable(true);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 5, 1));

		bAceptar.setText("Aceptar");
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));
		
		bCancelar.setText("Cancelar");
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));

		// Un SelectionAdapter con lo que hace el bot�n
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		};

		bCancelar.addSelectionListener(sabCancelar);

		shell.pack();
		shell.setSize(200, 150);
		shell.setLocation(500, 400);
		shell.open();

		// Este bucle mantiene la ventana abierta
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}