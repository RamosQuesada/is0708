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

public class I02 {

	public static void main(String[] IS0708) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);

		Image icoGr = new Image(display, I02.class.getResourceAsStream("icoGr.gif"));
		Image icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));
		Image ico_imprimir = new Image(display, I02.class.getResourceAsStream("ico_imprimir.gif"));
		Image ico_mens_l = new Image(display, I02.class.getResourceAsStream("ico_mens1_v.gif"));
		Image ico_mens = new Image(display, I02.class.getResourceAsStream("ico_mens2_v.gif"));
		Image ico_cuadrante = new Image(display, I02.class.getResourceAsStream("ico_cuadrante.gif"));
		
		
		// Dos iconos de tamaño diferente para SO's que los necesiten
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
				System.out.println("Pulsado Abrir");
			}
		});
		// Texto del item de menú
		itemSeleccionar.setText ("Abrir \tCtrl+A");
		// Acceso rápido (ctrl+a)
		itemSeleccionar.setAccelerator (SWT.MOD1 + 'A');		
		
		MenuItem itemImprimir = new MenuItem (submenu, SWT.PUSH);
		itemImprimir.setImage(ico_imprimir);
		itemImprimir.setText("&Imprimir");

		MenuItem itemSalir = new MenuItem (submenu, SWT.PUSH);
		itemSalir.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				shell.close();
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
		lShell.numColumns = 1;		
		shell.setLayout(lShell);

		final TabFolder tabFolder = new TabFolder (shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TabItem tabItem1 = new TabItem (tabFolder, SWT.NONE);
		tabItem1.setText ("Cuadrantes");
		tabItem1.setImage(ico_cuadrante);
		TabItem tabItem2 = new TabItem (tabFolder, SWT.NONE);
		tabItem2.setText ("Mensajes");
		tabItem2.setImage(ico_mens_l);
		
		final Composite cCuadrantes = new Composite (tabFolder, SWT.NONE);
		cCuadrantes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCuadrantes = new GridLayout();
		lCuadrantes.numColumns = 4;
		//lCuadrantes.makeColumnsEqualWidth = true;
		cCuadrantes.setLayout(lCuadrantes);
		
		final Label lCalendario = new Label (cCuadrantes, SWT.LEFT);
		lCalendario.setText("Calendario");
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		final Composite cCuadrantesDer = new Composite (cCuadrantes, SWT.BORDER);
		cCuadrantesDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2));
		GridLayout lCDer = new GridLayout();
		lCDer.numColumns = 4;
		cCuadrantesDer.setLayout(lCDer);

		final DateTime calendario = new DateTime (cCuadrantes, SWT.CALENDAR);
		calendario.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				System.out.println ("Fecha cambiada a "+ String.valueOf(calendario.getDay()) + " de " + meses[calendario.getMonth()]+ " de " + String.valueOf(calendario.getYear()));
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
						
		final Composite cMensajes = new Composite (tabFolder, SWT.BORDER);
		cMensajes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCMensajes = new GridLayout();
		lCMensajes.numColumns = 4;
		lCMensajes.makeColumnsEqualWidth = true;
		cMensajes.setLayout(lCMensajes);
		
		Table table = new Table (cMensajes, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		String[] titles = {" ", "De", "Asunto", "Mensaje"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}	
		int count = 12;
		for (int i=0; i<count; i++) {
			TableItem tItem = new TableItem (table, SWT.NONE);
			tItem.setImage(ico_mens);
			tItem.setText (1, "Remitente");
			tItem.setText (2, "Asunto del mensaje");
			tItem.setText (3, "Aquí va lo que quepa del principio del mensaje");
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	
		//table.setSize (table.computeSize (SWT.DEFAULT, 200));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		final Button bLeer = new Button(cMensajes, SWT.PUSH);
		bLeer.setText("Leer");
		bLeer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bResponder = new Button(cMensajes, SWT.PUSH);
		bResponder.setText("Responder");
		bResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bEliminar = new Button(cMensajes, SWT.PUSH);
		bEliminar.setText("Eliminar");
		bEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bMarcar = new Button(cMensajes, SWT.PUSH);
		bMarcar.setText("Marcar");
		bMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		tabItem1.setControl(cCuadrantes);
		tabItem2.setControl(cMensajes);
		
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada en la pantalla
		shell.setLocation(display.getBounds().width/2 - shell.getSize().x/2, display.getBounds().height/2 - shell.getSize().y/2);
		shell.open();
		
		// Preguntar antes de salir
		shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event event) {
				MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
				messageBox.setText ("Mensaje");
				messageBox.setMessage ("¿Desea cerrar la aplicación?");
				event.doit = messageBox.open () == SWT.YES;
			}
		});

		// Este bucle mantiene la ventana abierta
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}