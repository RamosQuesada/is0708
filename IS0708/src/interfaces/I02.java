/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal
 *   por Daniel Dionne
 *   
 * Interfaz principal de la aplicación.
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

import paquete_prueba.Interfaz_swt;

public class I02 {

	public static void main(String[] IS0708) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);

		
		// Dos iconos de tamaño diferente para SO's que los necesiten
		Image icoGr = new Image(display, I02.class.getResourceAsStream("icoGr.gif"));
		Image icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));
		shell.setImages(new Image[] {icoPq,icoGr});
		shell.setText("Turno-matic");
		shell.setVisible(true);

		// Una barra de menús
		Menu barra = new Menu (shell, SWT.BAR);
		shell.setMenuBar (barra);
		// Con un elemento "archivo"
		MenuItem archivoItem = new MenuItem (barra, SWT.CASCADE);
		archivoItem.setText ("&Archivo");
		// Y un submenú de persiana asociado al elemento
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		archivoItem.setMenu (submenu);
		// Aquí los elementos del submenú
		MenuItem itemSeleccionar = new MenuItem (submenu, SWT.PUSH);
		itemSeleccionar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				// Esto es lo que hace el elemento del menú
				System.out.println("Pulsado Seleccionar todo");
			}
		});
		// Texto del item de menú
		itemSeleccionar.setText ("Seleccionar &todo \tCtrl+A");
		// Acceso rápido (ctrl+a)
		itemSeleccionar.setAccelerator (SWT.MOD1 + 'A');		
		MenuItem itemImprimir = new MenuItem (submenu, SWT.PUSH);
		Image ico_imprimir = new Image(display, I02.class.getResourceAsStream("ico_imprimir.gif"));
		itemImprimir.setImage(ico_imprimir);
		itemImprimir.setText("&Imprimir");

		MenuItem itemSalir = new MenuItem (submenu, SWT.PUSH);
		itemSalir.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				// Esto es lo que hace el segundo elemento
				display.dispose();
			}
		});
		// Texto del item de menú
		itemSalir.setText("&Salir \tCtrl+S");
		// Acceso rápido (ctrl+s)
		itemSalir.setAccelerator (SWT.MOD1 + 'S');
		
		final Tray tray = display.getSystemTray();
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		
		shell.setImage(icoPq);
		if (tray != null) {			
			trayItem.setImage(icoPq);
		}
		
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 2;
		

		shell.setLayout(lShell);
		
		final Composite cIzq = new Composite (shell, SWT.BORDER);
		final Composite cDer = new Composite (shell, SWT.BORDER);
		cIzq.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1));
		cDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCIzq = new GridLayout();
		lCIzq.numColumns = 4;
		lCIzq.makeColumnsEqualWidth = true;
		cIzq.setLayout(lCIzq);
		cDer.setLayout(new GridLayout());
		
		
		final Label lHorarios = new Label (cIzq, SWT.LEFT);
		lHorarios.setText("Horarios");
		lHorarios.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1));
		
		final DateTime calendario = new DateTime (cIzq, SWT.CALENDAR);
		calendario.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				System.out.println ("Fecha cambiada a "+ String.valueOf(calendario.getDay()) + " de " + meses[calendario.getMonth()]+ " de " + String.valueOf(calendario.getYear()));
			}
		});
		calendario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));
		
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada en la pantalla
		shell.setLocation(display.getBounds().width/2 - shell.getSize().x/2, display.getBounds().height/2 - shell.getSize().y/2);
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