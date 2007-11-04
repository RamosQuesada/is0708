/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal - Jefe
 *   por Daniel Dionne
 *   
 * Interfaz principal de la aplicaci�n, vista de jefe.
 * ver 0.1
 *******************************************************************************/


package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

import interfaces.I01;
import interfaces.I02_cuadr;
import interfaces.I08_1;

public class I02 {
	private static Display display;
	private Shell shell;
	Image icoGr, icoPq, ico_imprimir, ico_mens_l, ico_mens, ico_cuadrante, ico_chico, ico_chica, ico_chicos;
	
	
	public static void main (String[] args) {
		// Este bucle mantiene la ventana abierta
		display = new Display ();
		I02 estaClase = new I02();
		estaClase.crearVentana();
		estaClase.shell.open();
		while (!estaClase.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	private void crearBarraMenu() {
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
		// Item Abrir
		MenuItem itemAbrir = new MenuItem (submenu, SWT.PUSH);
		itemAbrir.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.out.println("Pulsado Abrir");
			}
		});
		// Texto del item de men�
		itemAbrir.setText ("Abrir \tCtrl+A");
		// Acceso r�pido (ctrl+a)
		itemAbrir.setAccelerator (SWT.MOD1 + 'A');		
		
		// Item Imprimir
		MenuItem itemImprimir = new MenuItem (submenu, SWT.PUSH);
		itemImprimir.setImage(ico_imprimir);
		itemImprimir.setText("&Imprimir");

		// Item Salir
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
		
	}
	private void crearCompositeCuadrantes (Composite c) {
		// Configuraci�n del composite
		c.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCc = new GridLayout();
		lCc.numColumns = 5;
		//lCc.makeColumnsEqualWidth = true;
		c.setLayout(lCc);
		
		// Componentes del composite
		Label lDepartamentos = new Label(c, SWT.NONE);
		lDepartamentos.setText("Dpto.");
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Combo cDepartamentos = new Combo(c, SWT.BORDER | SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cDepartamentos.setItems(new String[] {"Ba�os", "Cocinas"});
		cDepartamentos.select(0);

		// Un composite para albergar el canvas de los cuadrantes
		Composite cCuadrantesDer = new Composite (c, SWT.BORDER);
		cCuadrantesDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 3));
		GridLayout lCuadrantesDer = new GridLayout();
		lCuadrantesDer.numColumns = 4;
		cCuadrantesDer.setLayout(lCuadrantesDer);
		
		I02_cuadr cuadr = new I02_cuadr(cCuadrantesDer);

		Label lCuadr = new Label (cCuadrantesDer, SWT.CENTER);
		lCuadr.setText("Aqu� se muestran los cuadrantes");
		lCuadr.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1));

		Label lCalendario = new Label (c, SWT.LEFT);
		lCalendario.setText("Calendario");
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));

		
		final DateTime calendario = new DateTime (c, SWT.CALENDAR);
		calendario.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				System.out.println ("Fecha cambiada a "+ String.valueOf(calendario.getDay()) + " de " + meses[calendario.getMonth()]+ " de " + String.valueOf(calendario.getYear()));
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));

	}
	private void crearCompositeMensajes (Composite c) {
		c.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCc = new GridLayout();
		lCc.numColumns = 4;
		lCc.makeColumnsEqualWidth = true;
		c.setLayout(lCc);
		
		Table tablaMensajes = new Table (c, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
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
			tItem.setText (3, "Aqu� va lo que quepa del principio del mensaje");
			tItem.setText (4, "25/10/2007");
			
		}
		for (int i=0; i<titles.length; i++) {
			tablaMensajes.getColumn (i).pack ();
		}	
		//table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		final Button bMensNuevo = new Button(c, SWT.PUSH);
		bMensNuevo.setText("Nuevo");
		bMensNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bMensResponder = new Button(c, SWT.PUSH);
		bMensResponder.setText("Responder");
		bMensResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bMensEliminar = new Button(c, SWT.PUSH);
		bMensEliminar.setText("Eliminar");
		bMensEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bMensMarcar = new Button(c, SWT.PUSH);
		bMensMarcar.setText("Marcar");
		bMensMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

	}
	private void crearCompositeEmpleados(Composite c) {
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCc = new GridLayout();
		lCc.numColumns = 2;
		c.setLayout(lCc);
		
		final Composite cEmplIzq = new Composite (c, SWT.NONE);
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

		final Composite cEmplDer = new Composite (c, SWT.NONE);
		cEmplDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCEmplDer = new GridLayout();
		lCEmplDer.numColumns = 4;
		lCEmplDer.makeColumnsEqualWidth = true;
		cEmplDer.setLayout(lCEmplDer);

		Table tablaEmpleados = new Table (cEmplDer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaEmpleados.setLinesVisible (true);
		tablaEmpleados.setHeaderVisible (true);
		String[] titles2 = {" ", "N� vend", "Nombre", "Departamento", "Contrato","Tel�fono",""};
		for (int i=0; i<titles2.length; i++) {
			TableColumn column = new TableColumn (tablaEmpleados, SWT.NONE);
			column.setText (titles2 [i]);
		}	
		for (int i=0; i<10; i++) {
			TableItem tItem = new TableItem (tablaEmpleados, SWT.NONE);
			tItem.setImage(ico_chica);
			tItem.setText (1, "56468546");
			tItem.setText (2, "Mandarina Gonz�lez");
			tItem.setText (3, "Discos");
			tItem.setText (4, "6:40h");
			tItem.setText (5, "911234567");
			tItem.setImage(6, ico_mens);
		}
		for (int i=0; i<titles2.length; i++) {
			tablaEmpleados.getColumn (i).pack ();
		}	
		//table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaEmpleados.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		final Button bEmplNuevo = new Button(cEmplDer, SWT.PUSH);
		bEmplNuevo.setText("Nuevo");
		bEmplNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		bEmplNuevo.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				I08_1 ventana = new I08_1(shell);
			}
		});
				
		final Button bEmplVer = new Button(cEmplDer, SWT.PUSH);
		bEmplVer.setText("Ver");
		bEmplVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Button bEmplEditar = new Button(cEmplDer, SWT.PUSH);
		bEmplEditar.setText("Editar");
		bEmplEditar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bEmplBaja = new Button(cEmplDer, SWT.PUSH);
		bEmplBaja.setText("Dar de baja");
		bEmplBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}
	private void crearCompositeDepartamentos(Composite c) {
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCc = new GridLayout();
		lCc.numColumns = 3;
		c.setLayout(lCc);

		Label lDepartamentos = new Label(c, SWT.LEFT);
		lDepartamentos.setText("Departamento");
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Combo cDepartamentos = new Combo(c, SWT.BORDER | SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cDepartamentos.setItems(new String[] {"Ba�os", "Cocinas"});
		cDepartamentos.select(0);
		
		Button bConfig = new Button(c,SWT.PUSH);
		bConfig.setText("Configurar este departamento");
		bConfig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Composite cInfo = new Composite(c,SWT.BORDER);
		GridLayout lCcInfo = new GridLayout();
		lCcInfo.numColumns = 2;
		cInfo.setLayout(lCc);
		cInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		Label lContenido = new Label(cInfo,SWT.CENTER);
		lContenido.setText("Aqu� va informaci�n del departamento");
		lContenido.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
		
	}
	private void crearVistaJefe() {
		// Crear menu tabs
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
		TabItem tabItemDepartamentos = new TabItem (tabFolder, SWT.NONE);
		tabItemDepartamentos.setText ("Departamentos");
		tabItemDepartamentos.setImage(ico_chicos);
		
		
		final Composite cCuadrantes = new Composite (tabFolder, SWT.NONE);
		crearCompositeCuadrantes(cCuadrantes);				
		
		final Composite cMensajes = new Composite (tabFolder, SWT.NONE);
		crearCompositeMensajes(cMensajes);
		
		final Composite cEmpleados = new Composite (tabFolder, SWT.NONE);
		crearCompositeEmpleados(cEmpleados);
		
		final Composite cDepartamentos = new Composite (tabFolder, SWT.NONE);
		crearCompositeDepartamentos(cDepartamentos);
		
		// Asignar cada panel a su tab 
		tabItemCuadrantes   .setControl(cCuadrantes);
		tabItemMensajes     .setControl(cMensajes);
		tabItemEmpleados    .setControl(cEmpleados);
		tabItemDepartamentos.setControl(cDepartamentos);
	}
	private void crearVentana() {
		// Crear la ventana
		shell = new Shell (display);
		shell.setText("Turno-matic");

		// Cargar iconos
		icoGr = new Image(display, I02.class.getResourceAsStream("icoGr.gif"));
		icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));
		ico_imprimir = new Image(display, I02.class.getResourceAsStream("ico_imprimir.gif"));
		ico_mens_l = new Image(display, I02.class.getResourceAsStream("ico_mens1_v.gif"));
		ico_mens = new Image(display, I02.class.getResourceAsStream("ico_mens2_v.gif"));
		ico_cuadrante = new Image(display, I02.class.getResourceAsStream("ico_cuadrante.gif"));
		ico_chico = new Image(display, I02.class.getResourceAsStream("ico_chico.gif"));
		ico_chica = new Image(display, I02.class.getResourceAsStream("ico_chica.gif"));
		ico_chicos = new Image(display, I02.class.getResourceAsStream("ico_chicos.gif"));
		
		// Dos iconos de tama�o diferente para SO's que los necesiten
		shell.setImages(new Image[] {icoPq,icoGr});

		crearBarraMenu();
		
		final Tray tray = display.getSystemTray();
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		shell.setImage(icoPq);
		if (tray != null) {			
			trayItem.setImage(icoPq);
		}
		
		// Crear layout principal
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		
		// Poblar ventana
		crearVistaJefe();
		
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada en la pantalla
		shell.setLocation(display.getBounds().width/2 - shell.getSize().x/2, display.getBounds().height/2 - shell.getSize().y/2);
		shell.open();
		
		// Login
		//I01 login = new I01(shell);
		
		// Preguntar antes de salir
		shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event e) {
				MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_WARNING);
				messageBox.setText ("Mensaje");
// Diferentes iconos: http://www.developer.com/java/other/article.php/10936_3330861_2
				messageBox.setMessage ("�Desea cerrar la aplicaci�n?");
				e.doit = messageBox.open () == SWT.YES;
			}
		});

	}
}
	