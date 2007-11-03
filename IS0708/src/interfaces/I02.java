/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal
 *   por Daniel Dionne
 *   
 * Interfaz principal de la aplicaci�n.
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
		Image ico_chico = new Image(display, I02.class.getResourceAsStream("ico_chico.gif"));
		Image ico_chica = new Image(display, I02.class.getResourceAsStream("ico_chica.gif"));
		
		
		// Dos iconos de tama�o diferente para SO's que los necesiten
		shell.setImages(new Image[] {icoPq,icoGr});
		shell.setText("Turno-matic");
		shell.setVisible(true);

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
				System.out.println("Pulsado Abrir");
			}
		});
		// Texto del item de men�
		itemSeleccionar.setText ("Abrir \tCtrl+A");
		// Acceso r�pido (ctrl+a)
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
		// Texto del item de men�
		itemSalir.setText("&Salir \tCtrl+S");
		// Acceso r�pido (ctrl+s)
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
		TabItem tabItemCuadrantes = new TabItem (tabFolder, SWT.NONE);
		tabItemCuadrantes.setText ("Cuadrantes");
		tabItemCuadrantes.setImage(ico_cuadrante);
		TabItem tabItemMensajes = new TabItem (tabFolder, SWT.NONE);
		tabItemMensajes.setText ("Mensajes");
		tabItemMensajes.setImage(ico_mens_l);
		TabItem tabItemEmpleados = new TabItem (tabFolder, SWT.NONE);
		tabItemEmpleados.setText ("Empleados");
		tabItemEmpleados.setImage(ico_chico);
		
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
		GridLayout lCuadrantesDer = new GridLayout();
		lCuadrantesDer.numColumns = 4;
		cCuadrantesDer.setLayout(lCuadrantesDer);
		
		Label lCuadr = new Label (cCuadrantesDer, SWT.CENTER);
		lCuadr.setText("Aqu� se mostrar�n los cuadrantes");
		lCuadr.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1));

		final DateTime calendario = new DateTime (cCuadrantes, SWT.CALENDAR);
		calendario.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				System.out.println ("Fecha cambiada a "+ String.valueOf(calendario.getDay()) + " de " + meses[calendario.getMonth()]+ " de " + String.valueOf(calendario.getYear()));
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
						
		final Composite cMensajes = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCMensajes = new GridLayout();
		lCMensajes.numColumns = 4;
		lCMensajes.makeColumnsEqualWidth = true;
		cMensajes.setLayout(lCMensajes);
		
		Table tablaMensajes = new Table (cMensajes, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaMensajes.setLinesVisible (true);
		tablaMensajes.setHeaderVisible (true);
		String[] titles = {" ", "De", "Asunto", "Mensaje"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (tablaMensajes, SWT.NONE);
			column.setText (titles [i]);
		}	
		int count = 12;
		for (int i=0; i<count; i++) {
			TableItem tItem = new TableItem (tablaMensajes, SWT.NONE);
			tItem.setImage(ico_mens);
			tItem.setText (1, "Remitente");
			tItem.setText (2, "Asunto del mensaje");
			tItem.setText (3, "Aqu� va lo que quepa del principio del mensaje");
		}
		for (int i=0; i<titles.length; i++) {
			tablaMensajes.getColumn (i).pack ();
		}	
		//table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
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

		final Composite cEmpleados = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCEmpleados = new GridLayout();
		lCEmpleados.numColumns = 4;
		lCEmpleados.makeColumnsEqualWidth = true;
		cEmpleados.setLayout(lCEmpleados);
		
		Table tablaEmpleados = new Table (cEmpleados, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaEmpleados.setLinesVisible (true);
		tablaEmpleados.setHeaderVisible (true);
		String[] titles2 = {" ", "N� vend", "Nombre", "Departamento"};
		for (int i=0; i<titles2.length; i++) {
			TableColumn column = new TableColumn (tablaEmpleados, SWT.NONE);
			column.setText (titles2 [i]);
		}	
		count = 12;
		for (int i=0; i<count; i++) {
			TableItem tItem = new TableItem (tablaEmpleados, SWT.NONE);
			tItem.setImage(ico_chica);
			tItem.setText (1, "56468546");
			tItem.setText (2, "Mandarina Gonz�lez");
			tItem.setText (3, "Discos");
		}
		for (int i=0; i<titles2.length; i++) {
			tablaEmpleados.getColumn (i).pack ();
		}	
		//table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaEmpleados.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		final Button bEmplNuevo = new Button(cEmpleados, SWT.PUSH);
		bEmplNuevo.setText("Nuevo");
		bEmplNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bEmplVer = new Button(cEmpleados, SWT.PUSH);
		bEmplVer.setText("Ver");
		bEmplVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bEmplEditar = new Button(cEmpleados, SWT.PUSH);
		bEmplEditar.setText("Editar");
		bEmplEditar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bEmplBaja = new Button(cEmpleados, SWT.PUSH);
		bEmplBaja.setText("Dar de baja");
		bEmplBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
				
		tabItemCuadrantes.setControl(cCuadrantes);
		tabItemMensajes.setControl(cMensajes);
		tabItemEmpleados.setControl(cEmpleados);
		
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada en la pantalla
		shell.setLocation(display.getBounds().width/2 - shell.getSize().x/2, display.getBounds().height/2 - shell.getSize().y/2);
		shell.open();
		
		// Preguntar antes de salir
		shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event event) {
				MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
				messageBox.setText ("Mensaje");
				messageBox.setMessage ("�Desea cerrar la aplicaci�n?");
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