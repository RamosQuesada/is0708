package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.Locale;

import aplicacion.Empleado;

import impresion.Imprimir;
import interfaces.I01;
import interfaces.I02_cuadr;
import interfaces.I08_1;
import interfaces.I09_1;

/**
 * Interfaz I-02 :: Ventana principal - Jefe
 * @author Daniel Dionne
 * 
 */
public class I02 {

	Shell shell;
	Display display;
	ResourceBundle bundle;
	Locale locale;
	Image icoGr, icoPq, ico_imprimir, ico_mens_l, ico_mens, ico_cuadrante,
			ico_chico, ico_chica, ico_chicos;

	
	private ArrayList<Empleado> empleados;
	
	private ImageData Img;

	public I02(Shell shell, Display display, ResourceBundle bundle, Locale locale, 
			ArrayList<Empleado> empleados) {
		this.shell = shell;
		this.display = display;
		this.empleados = empleados;
		this.bundle = bundle;
		this.locale = locale;

		//ponImageDia();
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
				imprimir.imprimirImage(Img);
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

	private void crearTabCuadrantes(TabFolder tabFolder) {
		TabItem tabItemCuadrantes = new TabItem(tabFolder, SWT.NONE);
		tabItemCuadrantes.setText(bundle.getString("Cuadrantes"));
		tabItemCuadrantes.setImage(ico_cuadrante);

		final Composite cCuadrantes = new Composite(tabFolder, SWT.NONE);
		tabItemCuadrantes.setControl(cCuadrantes);
		// Configuración del composite
		cCuadrantes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		cCuadrantes.setLayout(new GridLayout(5, false));

		// Componentes del composite
		Label lDepartamentos = new Label(cCuadrantes, SWT.NONE);
		lDepartamentos.setText(bundle.getString("I02_lab_Dpto"));
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		Combo cDepartamentos = new Combo(cCuadrantes, SWT.BORDER | SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
		cDepartamentos.select(0);

		// Un canvas para albergar el gráfico de los cuadrantes
		// NO_BACKGROUND + doble buffer para evitar parpadeo
		Composite cCuadrante = new Composite(cCuadrantes, SWT.BORDER);
		cCuadrante.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 5));

		final I02_cuadr cuadrante = new I02_cuadr(cCuadrante, false, empleados);
		Img = cuadrante.dameImageImprimible();
		
		Label lCalendario = new Label(cCuadrantes, SWT.LEFT);
		lCalendario.setText(bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));

		final DateTime calendario = new DateTime(cCuadrantes, SWT.CALENDAR | SWT.SHORT);
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
		final Button bPorMes = new Button(cCuadrantes, SWT.RADIO);
		bPorMes.setText(bundle.getString("I02_but_Verpormes"));
		bPorMes.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		// Oyente para saber cuando se ha modificado la seleccion del boton
		bPorMes.addListener(SWT.Selection, new Listener() {
			// Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorMes.getSelection()) {
					cuadrante.ponImageMes();
					Img = cuadrante.dameImageImprimible();
					cuadrante.setMensual();
				} else
					cuadrante.ponImageDia();
					Img = cuadrante.dameImageImprimible();
					cuadrante.setDiario();			
			}
		});

		final Button bPorSemanas = new Button(cCuadrantes, SWT.RADIO);
		bPorSemanas.setText(bundle.getString("I02_but_Verpordia"));
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				2, 1));

		bPorMes.setSelection(true);
	}

	private void crearTabMensajes(TabFolder tabFolder) {
		TabItem tabItemMensajes = new TabItem(tabFolder, SWT.NONE);
		tabItemMensajes.setText(bundle.getString("Mensajes"));
		tabItemMensajes.setImage(ico_mens_l);
		
		final Composite cMensajes = new Composite(tabFolder, SWT.NONE);		
		tabItemMensajes.setControl(cMensajes);
		
		cMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cMensajes.setLayout(new GridLayout(4, true));

		Table tablaMensajes = new Table(cMensajes, SWT.MULTI | SWT.BORDER  | SWT.FULL_SELECTION);
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
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

		final Button bMensNuevo = new Button(cMensajes, SWT.PUSH);
		bMensNuevo.setText(bundle.getString("Nuevo"));
		bMensNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bMensResponder = new Button(cMensajes, SWT.PUSH);
		bMensResponder.setText(bundle.getString("I02_but_Responder"));
		bMensResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,	false, 1, 1));

		final Button bMensEliminar = new Button(cMensajes, SWT.PUSH);
		bMensEliminar.setText(bundle.getString("I02_but_Eliminar"));
		bMensEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bMensMarcar = new Button(cMensajes, SWT.PUSH);
		bMensMarcar.setText(bundle.getString("I02_but_Marcar"));
		bMensMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

	}

	private void crearTabEmpleados(TabFolder tabFolder) {
		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText(bundle.getString("Empleados"));
		tabItemEmpleados.setImage(ico_chico);

		final Composite cEmpleados = new Composite(tabFolder, SWT.NONE);		
		tabItemEmpleados.setControl(cEmpleados);
		
		cEmpleados.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cEmpleados.setLayout(new GridLayout(2, false));

		final Composite cEmplIzq = new Composite(cEmpleados, SWT.NONE);
		cEmplIzq.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		cEmplIzq.setLayout(new GridLayout(2, false));

		Label lEmplFiltro = new Label(cEmplIzq, SWT.NONE);
		lEmplFiltro.setText(bundle.getString("I02_lab_Filtro"));
		lEmplFiltro.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));

		Label lEmplNombre = new Label(cEmplIzq, SWT.NONE);
		lEmplNombre.setText(bundle.getString("Nombre"));
		lEmplNombre.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		Text tEmplNombre = new Text(cEmplIzq, SWT.BORDER);
		tEmplNombre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,	1, 1));

		Label lEmplNVend = new Label(cEmplIzq, SWT.NONE);
		lEmplNVend.setText(bundle.getString("I02_lab_NVend"));
		lEmplNVend.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,	1, 1));

		Text tEmplNVend = new Text(cEmplIzq, SWT.BORDER);
		tEmplNVend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Label lEmplDpto = new Label(cEmplIzq, SWT.NONE);
		lEmplDpto.setText(bundle.getString("I02_lab_Dpto"));
		lEmplDpto.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		Text tEmplDpto = new Text(cEmplIzq, SWT.BORDER);
		tEmplDpto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Label lEmplContr = new Label(cEmplIzq, SWT.NONE);
		lEmplContr.setText(bundle.getString("Contrato"));
		lEmplContr.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		Combo cEmplContr = new Combo(cEmplIzq, SWT.BORDER);
		cEmplContr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Composite cEmplDer = new Composite(cEmpleados, SWT.NONE);
		cEmplDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cEmplDer.setLayout(new GridLayout(4, true));

		Table tablaEmpleados = new Table(cEmplDer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
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
		bEmplNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		bEmplNuevo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new I08_1(shell, bundle);
			}
		});

		final Button bEmplVer = new Button(cEmplDer, SWT.PUSH);
		bEmplVer.setText(bundle.getString("I02_but_Ver"));
		bEmplVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,	1));
		bEmplVer.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// employ created for tests
				Color col = new Color(display, 10, 0, 50);
				Empleado eS = new Empleado(1, "M. Jackson", new Color (display, 104, 228,  85));
				
				Empleado emp = new Empleado(eS, 12345678, "phil", "colins", "-", new Date("12/12/09"), 1, "phil.colins@gmail.com", "", 1, null, null, new Date("12/12/09"),new Date("12/12/09"), col );
				new I08_2(shell, emp, bundle);
			}
		});

		final Button bEmplEditar = new Button(cEmplDer, SWT.PUSH);
		bEmplEditar.setText(bundle.getString("Editar"));
		bEmplEditar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Button bEmplBaja = new Button(cEmplDer, SWT.PUSH);

		bEmplBaja.setText(bundle.getString("I02_but_Eliminar"));
		bEmplBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		// FIXME Jakub, you shouldn't print the list from the screen, you should
		// print your own by hand. This image is not scalable.
		cEmpleados.addListener(SWT.MouseMove, new Listener(){
			public void handleEvent(Event e) {
				Composite cHelp = (Composite)cEmpleados.getChildren()[1];
				cHelp.getClientArea();
				Rectangle rect = cHelp.getClientArea();
		          // Create buffer for double buffering
		          Image image = new Image(display, rect.width, rect.height);
		          GC gc1 = new GC(cEmpleados.getChildren()[1]);
		          gc1.copyArea(image, rect.y,rect.x);
		          Img=image.getImageData();
			}
		});

	}

	private void crearTabDepartamentos(TabFolder tabFolder) {
		TabItem tabItemDepartamentos = new TabItem(tabFolder, SWT.NONE);
		tabItemDepartamentos.setText(bundle.getString("Departamentos"));
		tabItemDepartamentos.setImage(ico_chicos);

		final Composite cDepartamentos = new Composite(tabFolder, SWT.NONE);
		tabItemDepartamentos.setControl(cDepartamentos);
		
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cDepartamentos.setLayout(new GridLayout(3, false));

		Label lDepartamentos = new Label(cDepartamentos, SWT.LEFT);
		lDepartamentos.setText(bundle.getString("Departamento"));
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		final Combo cmbDepartamentos = new Combo(cDepartamentos,SWT.BORDER |SWT.READ_ONLY);
		cmbDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		cmbDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
		cmbDepartamentos.select(0);

		//Composite for Buttons: "New Department" and "Configure Department"
		Composite cBut = new Composite(cDepartamentos,SWT.LEFT);
		cBut.setLayout(new GridLayout(2, false));
		
		//Button "Configure Department"
		Button bConfig = new Button(cBut, SWT.PUSH);
		bConfig.setText(bundle.getString("I02_but_Config_dep"));
		bConfig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		bConfig.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Pulsado Configuración departamentos: "+cmbDepartamentos.getText().toString());
				new I10_ManageDepartament(shell, bundle, bundle.getString("I02_but_Config_dep"));
			}
		});
		
		//Button "New Department"
		Button bNew = new Button(cBut, SWT.PUSH);
		bNew.setText(bundle.getString("I02_but_Nuevo_dep"));
		bNew.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		bNew.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Pulsado Nuevo Departamento");
				new I10_ManageDepartament(shell, bundle, bundle.getString("I02_but_Nuevo_dep"));
			}
		});
	

		
		Composite cInfo = new Composite(cDepartamentos, SWT.BORDER);
		cInfo.setLayout(new GridLayout(2, false));
		cInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		Label lContenido = new Label(cInfo, SWT.CENTER);
		lContenido.setText("Aquí va información del departamento");
		lContenido.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));

	}

	private void crearTabContratos(TabFolder tabFolder) { 		
		TabItem tabItemContratos = new TabItem(tabFolder, SWT.NONE);
		tabItemContratos.setText(bundle.getString("Contratos"));
		tabItemContratos.setImage(ico_cuadrante);

		final Composite cContratos = new Composite(tabFolder, SWT.NONE);
		tabItemContratos.setControl(cContratos);
		
		cContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cContratos.setLayout(new GridLayout(3,false));
		
		Table tablaContratos = new Table(cContratos, SWT.MULTI | SWT.BORDER  | SWT.FULL_SELECTION);
		tablaContratos.setLinesVisible(true);
		tablaContratos.setHeaderVisible(true);
		String[] titles = { "Nombre contrato", "Empleados", "Total"};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaContratos, SWT.NONE);
			column.setText(titles[i]);
		}
		// TODO Quitar este ejemplo
		TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
		tItem.setText(0, "6:40h");
		tItem.setText(1, "Mike Olfield, Lou Vega, Paul McCartney, Ricky Martin");
		tItem.setText(2, "4");
		tItem = new TableItem(tablaContratos, SWT.NONE);
		tItem.setText(0, "Días sueltos");
		tItem.setText(1, "Alicia Keys, Ana Torroja, Marylin Manson");
		tItem.setText(2, "3");
		for (int i = 0; i < titles.length; i++) {
			tablaContratos.getColumn(i).pack();
		}
		tablaContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3,1));
		
		final Button bNuevoContrato		= new Button(cContratos, SWT.PUSH);
		final Button bModificarContrato	= new Button(cContratos, SWT.PUSH);
		final Button bEliminarContrato	= new Button(cContratos	, SWT.PUSH);
		
		bNuevoContrato		.setText("Nuevo contrato");
		bModificarContrato	.setText("Modificar contrato");
		bEliminarContrato	.setText("Eliminar contrato");
		bNuevoContrato		.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,1));
		bModificarContrato	.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,1));
		bEliminarContrato	.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,1));		

		bNuevoContrato.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new I09_1(shell, bundle);
			}
		});

	}
	
	private void crearVistaJefe() {
		// Crear menu tabs
		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		crearTabCuadrantes(tabFolder);
		crearTabMensajes(tabFolder);
		crearTabEmpleados(tabFolder);		
		crearTabDepartamentos(tabFolder);
		crearTabContratos(tabFolder);
	}

	public void crearVentana() {
		// Crear la ventana
		shell.setText(bundle.getString("Turno-matic"));// idiomas igual siempre

		// Cargar iconos
		icoGr			= new Image(display, I02.class.getResourceAsStream("icoGr.gif"));
		icoPq			= new Image(display, I02.class.getResourceAsStream("icoPq.gif"));
		ico_imprimir	= new Image(display, I02.class.getResourceAsStream("ico_imprimir.gif"));
		ico_mens_l		= new Image(display, I02.class.getResourceAsStream("ico_mens1_v.gif"));
		ico_mens		= new Image(display, I02.class.getResourceAsStream("ico_mens2_v.gif"));
		ico_cuadrante	= new Image(display, I02.class.getResourceAsStream("ico_cuadrante.gif"));
		ico_chico		= new Image(display, I02.class.getResourceAsStream("ico_chico.gif"));
		ico_chica		= new Image(display, I02.class.getResourceAsStream("ico_chica.gif"));
		ico_chicos		= new Image(display, I02.class.getResourceAsStream("ico_chicos.gif"));

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

		// Poblar ventana seg
		crearVistaJefe();

		// Crear una barra de estado
		Composite estado = new Composite(shell,SWT.BORDER);
		estado.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,1));
		estado.setLayout(new GridLayout(2,false));
		Label lEstado = new Label(estado,SWT.LEFT);
		ProgressBar pbEstado = new ProgressBar(estado,SWT.RIGHT);
		lEstado.setText("Conectando con la base de datos.");
		lEstado.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1,1));
		pbEstado.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		pbEstado.setSelection(85);

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
}