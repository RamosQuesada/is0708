/*******************************************************************************
 * INTERFAZ DE PRUEBA
 *   por Daniel Dionne
 *   
 * Interfaz de prueba para ver c�mo funciona SWT.
 * M�s ejemplos en: 
 *   http://www.eclipse.org/swt/widgets/
 *******************************************************************************/


package paquete_prueba;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class Interfaz_swt {

	public static void main(String[] IS0708) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);

		final Text texto = new Text(shell, SWT.BORDER);

		final Button boton = new Button(shell, SWT.PUSH);

		shell.setText("Interfaz de ejemplo");
		shell.setVisible(true);

		GridLayout layout = new GridLayout();
		layout.numColumns = 4;

		shell.setLayout(layout);

		// Una barra de men�s
		Menu barra = new Menu (shell, SWT.BAR);
		shell.setMenuBar (barra);
		// Con un elemento "archivo"
		MenuItem archivoItem = new MenuItem (barra, SWT.CASCADE);
		archivoItem.setText ("&Archivo");
		// Y un submen� de persiana asociado al elemento
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		archivoItem.setMenu (submenu);
		// Aqu� los elementos del submen�
		MenuItem itemSeleccionar = new MenuItem (submenu, SWT.PUSH);
		itemSeleccionar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				// Esto es lo que hace el elemento del men�
				texto.selectAll();
			}
		});
		// Texto del item de men�
		itemSeleccionar.setText ("Seleccionar &todo \tCtrl+A");
		// Acceso r�pido (ctrl+a)
		itemSeleccionar.setAccelerator (SWT.MOD1 + 'A');
		MenuItem itemSalir = new MenuItem (submenu, SWT.PUSH);
		itemSalir.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				// Esto es lo que hace el segundo elemento
				display.dispose();
			}
		});
		// Texto del item de men�
		itemSalir.setText("&Salir \tCtrl+S");
		// Acceso r�pido (ctrl+s)
		itemSalir.setAccelerator (SWT.MOD1 + 'S');

		texto.setEditable(true);
		texto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1));

		boton.setText("Mostrar en la esquina");
		boton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		final Tray tray = display.getSystemTray();
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		
		if (tray != null) {			
			Image image = new Image(display, Interfaz_swt.class.getResourceAsStream("icono.gif"));
			trayItem.setImage(image);
		}

		// Un SelectionAdapter con lo que hace el bot�n
		SelectionAdapter sel = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tray != null) {
					ToolTip tip = new ToolTip(shell, SWT.BALLOON  | SWT.ICON_INFORMATION);
					tip.setMessage(texto.getText());
					tip.setText("Aviso");
					trayItem.setToolTip(tip);
					tip.setVisible(true);
				}
			}
		};

		boton.addSelectionListener(sel);

		shell.pack();
		shell.setSize(400, 100);
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