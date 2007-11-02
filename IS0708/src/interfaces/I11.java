/*******************************************************************************
 * INTERFAZ I-01 :: Identificaci�n
 *   por Daniel Dionne
 *   
 * Ventana para mostrar un mensaje.
 * Me parece que esto no se hace as�, parece que salta una aplicaci�n aparte.
 * ver 0.1
 *******************************************************************************/


package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;



public class I11 {

	public void mostrarMensaje(String mensaje) {
		final Display display = Display.getCurrent();
		final Shell shell = new Shell (display, SWT.CLOSE);

		final Label lMensaje  = new Label(shell, SWT.LEFT);
		final Button bAceptar  = new Button(shell, SWT.PUSH);

		// Dos iconos de tama�o diferente para SO's que los necesiten
		Image icoGr = new Image(display, I01.class.getResourceAsStream("icoGr.gif"));
		Image icoPq = new Image(display, I01.class.getResourceAsStream("icoPq.gif"));
		shell.setImages(new Image[] {icoPq,icoGr});
		shell.setText("Mensaje");
		shell.setVisible(true);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		
		shell.setLayout(layout);

		lMensaje.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lMensaje.setText(mensaje);
		
		
		bAceptar.setText("Aceptar");
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));
		
				// Un SelectionAdapter con lo que hace el bot�n bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		};

		bAceptar.addSelectionListener(sabAceptar);
		
		shell.pack();
		shell.setSize(100, 100);
		shell.setLocation(550, 450);
		shell.open();

		// Este bucle mantiene la ventana abierta
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		shell.dispose();
	}
}
