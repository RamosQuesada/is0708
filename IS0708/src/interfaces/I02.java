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
		Image ico_chico = new Image(display, I02.class.getResourceAsStream("ico_chico.gif"));
		Image ico_chica = new Image(display, I02.class.getResourceAsStream("ico_chica.gif"));
		
		
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
		lCuadr.setText("Aquí se mostrarán los cuadrantes");
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
		String[] titles = {" ", "De", "Asunto", "Mensaje", "Fecha"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (tablaMensajes, SWT.NONE);
			column.setText (titles [i]);
		}	
		for (int i=0; i<12; i++) {
			TableItem tItem = new TableItem (tablaMensajes, SWT.NONE);
			tItem.setImage(ico_mens);
			tItem.setText (1, "Remitente");
			tItem.setText (2, "Asunto del mensaje");
			tItem.setText (3, "Aquí va lo que quepa del principio del mensaje");
			tItem.setText (4, "25/10/2007");
			
		}
		for (int i=0; i<titles.length; i++) {
			tablaMensajes.getColumn (i).pack ();
		}	
		//table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		final Button bMensNuevo = new Button(cMensajes, SWT.PUSH);
		bMensNuevo.setText("Nuevo");
		bMensNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bMensResponder = new Button(cMensajes, SWT.PUSH);
		bMensResponder.setText("Responder");
		bMensResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bMensEliminar = new Button(cMensajes, SWT.PUSH);
		bMensEliminar.setText("Eliminar");
		bMensEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bMensMarcar = new Button(cMensajes, SWT.PUSH);
		bMensMarcar.setText("Marcar");
		bMensMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Composite cEmpleados = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCEmpleados = new GridLayout();
		lCEmpleados.numColumns = 2;
		cEmpleados.setLayout(lCEmpleados);
		
		final Composite cEmplIzq = new Composite (cEmpleados, SWT.NONE);
		cEmplIzq.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCEmplIzq = new GridLayout();
		lCEmplIzq.numColumns = 2;
		//lCEmplIzq.makeColumnsEqualWidth = true;
		cEmplIzq.setLayout(lCEmplIzq);
		
		Label lEmplFiltro = new Label(cEmplIzq, SWT.NONE);
		lEmplFiltro.setText("Filtro");
		lEmplFiltro.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		
		Label lEmplNombre = new Label(cEmplIzq, SWT.NONE);
		lEmplNombre.setText("Nombre");
		lEmplNombre.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
		Text tEmplNombre = new Text(cEmplIzq, SWT.BORDER);
		tEmplNombre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label lEmplNVend = new Label(cEmplIzq, SWT.NONE);
		lEmplNVend.setText("N. vend");
		lEmplNVend.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
		Text tEmplNVend = new Text(cEmplIzq, SWT.BORDER);
		tEmplNVend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Label lEmplDpto = new Label(cEmplIzq, SWT.NONE);
		lEmplDpto.setText("Dpto.");
		lEmplDpto.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
		Text tEmplDpto = new Text(cEmplIzq, SWT.BORDER);
		tEmplDpto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Label lEmplContr = new Label(cEmplIzq, SWT.NONE);
		lEmplContr.setText("Contrato");
		lEmplContr.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
		Combo cEmplContr = new Combo (cEmplIzq, SWT.BORDER);
		cEmplContr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Composite cEmplDer = new Composite (cEmpleados, SWT.NONE);
		cEmplDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCEmplDer = new GridLayout();
		lCEmplDer.numColumns = 4;
		lCEmplDer.makeColumnsEqualWidth = true;
		cEmplDer.setLayout(lCEmplDer);

		Table tablaEmpleados = new Table (cEmplDer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaEmpleados.setLinesVisible (true);
		tablaEmpleados.setHeaderVisible (true);
		String[] titles2 = {" ", "Nº vend", "Nombre", "Departamento", "Contrato",""};
		for (int i=0; i<titles2.length; i++) {
			TableColumn column = new TableColumn (tablaEmpleados, SWT.NONE);
			column.setText (titles2 [i]);
		}	
		for (int i=0; i<10; i++) {
			TableItem tItem = new TableItem (tablaEmpleados, SWT.NONE);
			tItem.setImage(ico_chica);
			tItem.setText (1, "56468546");
			tItem.setText (2, "Mandarina González");
			tItem.setText (3, "Discos");
			tItem.setText (4, "6:40h");
			tItem.setImage(5, ico_mens);
		}
		for (int i=0; i<titles2.length; i++) {
			tablaEmpleados.getColumn (i).pack ();
		}	
		//table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaEmpleados.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		final Button bEmplNuevo = new Button(cEmplDer, SWT.PUSH);
		bEmplNuevo.setText("Nuevo");
		bEmplNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bEmplVer = new Button(cEmplDer, SWT.PUSH);
		bEmplVer.setText("Ver");
		bEmplVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bEmplEditar = new Button(cEmplDer, SWT.PUSH);
		bEmplEditar.setText("Editar");
		bEmplEditar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bEmplBaja = new Button(cEmplDer, SWT.PUSH);
		bEmplBaja.setText("Dar de baja");
		bEmplBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
				
		// Asignar cada panel a su tab 
		tabItemCuadrantes.setControl(cCuadrantes);
		tabItemMensajes.setControl(cMensajes);
		tabItemEmpleados.setControl(cEmpleados);
		
		
		
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