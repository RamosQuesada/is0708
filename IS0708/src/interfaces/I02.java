/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal - Jefe
 *   por Daniel Dionne
 *   
 * Interfaz principal de la aplicación, vista de jefe.
 * ver 0.1
 *******************************************************************************/

package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

import java.util.ResourceBundle;
import java.util.Locale;

import aplicacion.Empleado;

import impresion.Imprimir;
import interfaces.I01;
import interfaces.I02_cuadr;
import interfaces.I08_1;
import interfaces.I09_1;

public class I02 implements impresion.Imprimible{

	Shell shell;
	Display display;
	ResourceBundle bundle;
	Locale locale;
	Image icoGr, icoPq, ico_imprimir, ico_mens_l, ico_mens, ico_cuadrante,
			ico_chico, ico_chica, ico_chicos;
	
	private Image cuadranteImg;
	private Point imgSize = new Point(800, 600);
	
	private ArrayList<Empleado> empleados;

	public I02(Shell shell, Display display, ResourceBundle bundle, Locale locale, 
			ArrayList<Empleado> empleados) {
		this.shell = shell;
		this.display = display;
		this.empleados = empleados;
		this.bundle = bundle;
		this.locale = locale;
		this.cuadranteImg = new Image(this.display, imgSize.x, imgSize.y);
		ponImageDia();
	}

	private void crearBarraMenu() {
		// Una barra de menús
		Menu barra = new Menu(shell, SWT.BAR);
		shell.setMenuBar(barra);
		// Con un elemento "archivo"
		MenuItem archivoItem = new MenuItem(barra, SWT.CASCADE);
		archivoItem.setText(bundle.getString("I02_men_Archivo"));
		// Y un submenú de persiana asociado al elemento
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		archivoItem.setMenu(submenu);
		// Aquí los elementos del submenú
		// Item Abrir
		MenuItem itemAbrir = new MenuItem(submenu, SWT.PUSH);
		itemAbrir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Pulsado abrir");
			}
		});
		// Texto del item de menú
		itemAbrir.setText(bundle.getString("I02_men_itm_Abrir") + "\tCtrl+"+ bundle.getString("I02_men_itm_abriracc"));
		// Acceso rápido (ctrl+a)
		itemAbrir.setAccelerator(SWT.MOD1 + bundle.getString("I02_men_itm_abriracc").charAt(0));

		// Item Imprimir
		MenuItem itemImprimir = new MenuItem(submenu, SWT.PUSH);
		itemImprimir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Imprimir imprimir = new Imprimir(display);
				imprimir.imprimirImage(dameImageImprimible());
			}
		});
		itemImprimir.setImage(ico_imprimir);
		itemImprimir.setText(bundle.getString("I02_men_itm_Imprimir")
				+ "\tCtrl+" + bundle.getString("I02_men_itm_imprimiracc"));
		itemImprimir.setAccelerator(SWT.MOD1
				+ bundle.getString("I02_men_itm_imprimiracc").charAt(0));

		// Item Salir
		MenuItem itemSalir = new MenuItem(submenu, SWT.PUSH);
		itemSalir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.close();
			}
		});
		// Texto del item de menú
		itemSalir.setText(bundle.getString("I02_men_itm_Salir") + "\tCtrl+"
				+ bundle.getString("I02_men_itm_saliracc"));
		// Acceso rápido (ctrl+s)
		itemSalir.setAccelerator(SWT.MOD1
				+ bundle.getString("I02_men_itm_saliracc").charAt(0));

		// Ayuda
		MenuItem helpMenuHeader = new MenuItem(barra, SWT.CASCADE);
		helpMenuHeader.setText(bundle.getString("I02_men_Ayuda"));
		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);

		MenuItem helpHelpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpHelpItem.setText(bundle.getString("I02_men_itm_Ayuda") + "\tF1");
		// display
		helpHelpItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new I12(display, locale, bundle);
			}
		});
		helpHelpItem.setAccelerator(SWT.F1);

	}

	private void crearCompositeCuadrantes(Composite c) {
		// Configuración del composite
		c.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		c.setLayout(new GridLayout(5, false));

		// Componentes del composite
		Label lDepartamentos = new Label(c, SWT.NONE);
		lDepartamentos.setText(bundle.getString("I02_lab_Dpto"));
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		Combo cDepartamentos = new Combo(c, SWT.BORDER | SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		cDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
		cDepartamentos.select(0);

		// Un canvas para albergar el gráfico de los cuadrantes
		// NO_BACKGROUND + doble buffer para evitar parpadeo
		Composite cCuadrante = new Composite(c, SWT.BORDER);
		cCuadrante.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				3, 5));

		final I02_cuadr cuadrante = new I02_cuadr(cCuadrante, false, empleados);
		Label lCalendario = new Label(c, SWT.LEFT);
		lCalendario.setText(bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 2, 1));

		final DateTime calendario = new DateTime(c, SWT.CALENDAR | SWT.SHORT);
		calendario.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String[] meses = { 
						bundle.getString("enero"),		bundle.getString("febrero"),
						bundle.getString("marzo"),		bundle.getString("abril"),
						bundle.getString("mayo"),		bundle.getString("junio"),
						bundle.getString("julio"),		bundle.getString("agosto"),
						bundle.getString("septiembre"),	bundle.getString("octubre"),
						bundle.getString("noviembre"),	bundle.getString("diciembre")
				};
				System.out.println("Fecha cambiada a " +
						String.valueOf(calendario.getDay())	+ " de " +
						meses[calendario.getMonth()]		+ " de " +
						String.valueOf(calendario.getYear()));
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		final Button bPorMes = new Button(c, SWT.RADIO);
		bPorMes.setText(bundle.getString("I02_but_Verpormes"));
		bPorMes.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2,
				1));
		// Oyente para saber cuando se ha modificado la seleccion del boton
		bPorMes.addListener(SWT.Selection, new Listener() {
			// Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorMes.getSelection()) {
					ponImageMes();
					cuadrante.setMensual();
				} else
					ponImageDia();
					cuadrante.setDiario();			
			}
		});

		final Button bPorSemanas = new Button(c, SWT.RADIO);
		bPorSemanas.setText(bundle.getString("I02_but_Verpordia"));
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				2, 1));

		bPorMes.setSelection(true);
	}

	private void crearCompositeMensajes(Composite c) {
		c.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		c.setLayout(new GridLayout(4, true));

		Table tablaMensajes = new Table(c, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tablaMensajes.setLinesVisible(true);
		tablaMensajes.setHeaderVisible(true);
		String[] titles = { " ", bundle.getString("I02_mens_De"),
				bundle.getString("Asunto"), bundle.getString("Mensaje"),
				bundle.getString("Fecha") };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaMensajes, SWT.NONE);
			column.setText(titles[i]);
		}
		for (int i = 0; i < 12; i++) {
			TableItem tItem = new TableItem(tablaMensajes, SWT.NONE);
			tItem.setImage(ico_mens);
			tItem.setText(1, "Remitente");
			tItem.setText(2, "Asunto del mensaje");
			tItem.setText(3, "Aquí va lo que quepa del principio del mensaje");
			tItem.setText(4, "25/10/2007");

		}
		for (int i = 0; i < titles.length; i++) {
			tablaMensajes.getColumn(i).pack();
		}
		// table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 4, 1));

		final Button bMensNuevo = new Button(c, SWT.PUSH);
		bMensNuevo.setText(bundle.getString("Nuevo"));
		bMensNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		final Button bMensResponder = new Button(c, SWT.PUSH);
		bMensResponder.setText(bundle.getString("I02_but_Responder"));
		bMensResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));

		final Button bMensEliminar = new Button(c, SWT.PUSH);
		bMensEliminar.setText(bundle.getString("I02_but_Eliminar"));
		bMensEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));

		final Button bMensMarcar = new Button(c, SWT.PUSH);
		bMensMarcar.setText(bundle.getString("I02_but_Marcar"));
		bMensMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

	}

	private void crearCompositeEmpleados(Composite c) {
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		c.setLayout(new GridLayout(2, false));

		final Composite cEmplIzq = new Composite(c, SWT.NONE);
		cEmplIzq.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1,
				1));
		cEmplIzq.setLayout(new GridLayout(2, false));

		Label lEmplFiltro = new Label(cEmplIzq, SWT.NONE);
		lEmplFiltro.setText(bundle.getString("I02_lab_Filtro"));
		lEmplFiltro.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 2, 1));

		Label lEmplNombre = new Label(cEmplIzq, SWT.NONE);
		lEmplNombre.setText(bundle.getString("Nombre"));
		lEmplNombre.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));

		Text tEmplNombre = new Text(cEmplIzq, SWT.BORDER);
		tEmplNombre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		Label lEmplNVend = new Label(cEmplIzq, SWT.NONE);
		lEmplNVend.setText(bundle.getString("I02_lab_NVend"));
		lEmplNVend.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		Text tEmplNVend = new Text(cEmplIzq, SWT.BORDER);
		tEmplNVend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		Label lEmplDpto = new Label(cEmplIzq, SWT.NONE);
		lEmplDpto.setText(bundle.getString("I02_lab_Dpto"));
		lEmplDpto.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		Text tEmplDpto = new Text(cEmplIzq, SWT.BORDER);
		tEmplDpto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		Label lEmplContr = new Label(cEmplIzq, SWT.NONE);
		lEmplContr.setText(bundle.getString("Contrato"));
		lEmplContr.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		Combo cEmplContr = new Combo(cEmplIzq, SWT.BORDER);
		cEmplContr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		final Composite cEmplDer = new Composite(c, SWT.NONE);
		cEmplDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		cEmplDer.setLayout(new GridLayout(4, true));

		Table tablaEmpleados = new Table(cEmplDer, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tablaEmpleados.setLinesVisible(true);
		tablaEmpleados.setHeaderVisible(true);
		String[] titles2 = { " ", bundle.getString("I02_lab_NVend"),
				bundle.getString("Nombre"), bundle.getString("Departamento"),
				bundle.getString("Contrato"), bundle.getString("Teléfono"), "" };
		for (int i = 0; i < titles2.length; i++) {
			TableColumn column = new TableColumn(tablaEmpleados, SWT.NONE);
			column.setText(titles2[i]);
		}
		for (int i = 0; i < 10; i++) {
			TableItem tItem = new TableItem(tablaEmpleados, SWT.NONE);
			tItem.setImage(ico_chica);
			tItem.setText(1, "56468546");
			tItem.setText(2, "Mandarina González");
			tItem.setText(3, "Discos");
			tItem.setText(4, "6:40h");
			tItem.setText(5, "911234567");
			tItem.setImage(6, ico_mens);
		}
		for (int i = 0; i < titles2.length; i++) {
			tablaEmpleados.getColumn(i).pack();
		}
		// table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaEmpleados.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 4, 1));

		final Button bEmplNuevo = new Button(cEmplDer, SWT.PUSH);
		bEmplNuevo.setText(bundle.getString("Nuevo"));
		bEmplNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		bEmplNuevo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new I08_1(shell, bundle);
			}
		});

		final Button bEmplVer = new Button(cEmplDer, SWT.PUSH);
		bEmplVer.setText(bundle.getString("I02_but_Ver"));
		bEmplVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		final Button bEmplEditar = new Button(cEmplDer, SWT.PUSH);
		bEmplEditar.setText(bundle.getString("Editar"));
		bEmplEditar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		final Button bEmplBaja = new Button(cEmplDer, SWT.PUSH);
		bEmplBaja.setText(bundle.getString("I02_but_Eliminar"));
		bEmplBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
	}

	private void crearCompositeDepartamentos(Composite c) {
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		c.setLayout(new GridLayout(3, false));

		Label lDepartamentos = new Label(c, SWT.LEFT);
		lDepartamentos.setText(bundle.getString("Departamento"));
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		Combo cDepartamentos = new Combo(c, SWT.BORDER | SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		cDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
		cDepartamentos.select(0);

		Button bConfig = new Button(c, SWT.PUSH);
		bConfig.setText(bundle.getString("I02_but_Config_dep"));
		bConfig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		bConfig.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Pulsado Configuración departamentos");
				new I09_1(shell, bundle);
			}
		});

		
		Composite cInfo = new Composite(c, SWT.BORDER);
		cInfo.setLayout(new GridLayout(2, false));
		cInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		Label lContenido = new Label(cInfo, SWT.CENTER);
		lContenido.setText("Aquí va información del departamento");
		lContenido.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				true, 2, 1));

	}

	private void crearVistaJefe() {
		// Crear menu tabs
		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		TabItem tabItemCuadrantes = new TabItem(tabFolder, SWT.NONE);
		tabItemCuadrantes.setText(bundle.getString("Cuadrantes"));
		tabItemCuadrantes.setImage(ico_cuadrante);
		TabItem tabItemMensajes = new TabItem(tabFolder, SWT.NONE);
		tabItemMensajes.setText(bundle.getString("Mensajes"));
		tabItemMensajes.setImage(ico_mens_l);
		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText(bundle.getString("Empleados"));
		tabItemEmpleados.setImage(ico_chico);
		TabItem tabItemDepartamentos = new TabItem(tabFolder, SWT.NONE);
		tabItemDepartamentos.setText(bundle.getString("Departamentos"));
		tabItemDepartamentos.setImage(ico_chicos);

		final Composite cCuadrantes = new Composite(tabFolder, SWT.NONE);
		crearCompositeCuadrantes(cCuadrantes);

		final Composite cMensajes = new Composite(tabFolder, SWT.NONE);
		crearCompositeMensajes(cMensajes);

		final Composite cEmpleados = new Composite(tabFolder, SWT.NONE);
		crearCompositeEmpleados(cEmpleados);

		final Composite cDepartamentos = new Composite(tabFolder, SWT.NONE);
		crearCompositeDepartamentos(cDepartamentos);

		// Asignar cada panel a su tab
		tabItemCuadrantes.setControl(cCuadrantes);
		tabItemMensajes.setControl(cMensajes);
		tabItemEmpleados.setControl(cEmpleados);
		tabItemDepartamentos.setControl(cDepartamentos);
	}

	public void crearVentana() {
		// Crear la ventana
		shell.setText(bundle.getString("Turno-matic"));// idiomas igual siempre

		// Cargar iconos
		icoGr = new Image(display, I02.class.getResourceAsStream("icoGr.gif"));
		icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));
		ico_imprimir = new Image(display, I02.class
				.getResourceAsStream("ico_imprimir.gif"));
		ico_mens_l = new Image(display, I02.class
				.getResourceAsStream("ico_mens1_v.gif"));
		ico_mens = new Image(display, I02.class
				.getResourceAsStream("ico_mens2_v.gif"));
		ico_cuadrante = new Image(display, I02.class
				.getResourceAsStream("ico_cuadrante.gif"));
		ico_chico = new Image(display, I02.class
				.getResourceAsStream("ico_chico.gif"));
		ico_chica = new Image(display, I02.class
				.getResourceAsStream("ico_chica.gif"));
		ico_chicos = new Image(display, I02.class
				.getResourceAsStream("ico_chicos.gif"));

		// Dos iconos de tamaño diferente para SO's que los necesiten
		shell.setImages(new Image[] { icoPq, icoGr });

		crearBarraMenu();

		final Tray tray = display.getSystemTray();
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		shell.setImage(icoPq);
		if (tray != null) {
			trayItem.setImage(icoPq);
		}

		// Crear layout principal
		GridLayout lShell = new GridLayout(1, false);
		shell.setLayout(lShell);

		// Poblar ventana
		crearVistaJefe();

		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada en la pantalla
		shell.setLocation(
				display.getBounds().width  / 2 - shell.getSize().x / 2, 
				display.getBounds().height / 2 - shell.getSize().y / 2  );
		shell.open();

		// Login
		new I01(shell, bundle);

		// Preguntar antes de salir
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_WARNING);
				messageBox.setText(bundle.getString("Mensaje"));
				// Diferentes iconos:
				// http://www.developer.com/java/other/article.php/10936_3330861_2
				messageBox.setMessage(bundle.getString("I02_dlg_CerrarAp"));
				e.doit = messageBox.open() == SWT.YES;
			}
		});

	}

	public void ponImageDia(){
		cuadranteImg.dispose();
		cuadranteImg = new Image(this.display, imgSize.x, imgSize.y);
		GC gc1 = new GC(cuadranteImg);
		Cuadrante c = new Cuadrante(display,4,9,23,0,0,0,0,0,empleados);
		c.dibujarCuadranteDia(gc1,-1);	
		gc1.dispose();

	}
	
	public void ponImageMes(){
		cuadranteImg.dispose();
		cuadranteImg = new Image(this.display, imgSize.x, imgSize.y);
		GC gc2 = new GC(cuadranteImg);
		Cuadrante c = new Cuadrante(display,4,9,23,0,0,0,0,0,empleados);
		c.dibujarCuadranteMes(gc2);	
		gc2.dispose();
		
	}
	
	public ImageData dameImageImprimible(){
		return cuadranteImg.getImageData();
	}
}
