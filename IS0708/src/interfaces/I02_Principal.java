package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

import aplicacion.Empleado;
import aplicacion.Util;

import paquete_pruebas.GeneraDatos;

import java.util.ResourceBundle;
import java.util.Locale;

import java.sql.Date;

import impresion.Imprimir;
import aplicacion.Vista;

/**
 * Interfaz de usuario I-02 :: Ventana principal - Jefe
 * 
 * @author Daniel Dionne
 * 
 */
public class I02_Principal {
	private Vista vista;
	private Shell shell;
	private Display display;
	private ResourceBundle bundle;
	private Locale locale;
	private Image icoGr, icoPq, ico_imprimir, ico_mens,
			ico_cuadrante, ico_chico, ico_chica, ico_chicos;
	private Cuadrante cuadranteActual;
	private Label lEstado;
	private ProgressBar pbEstado;
	private Date fecha;
	private ImageData Img;
	private Tray tray;

	public I02_Principal(Shell shell, Display display, ResourceBundle bundle,
			Locale locale, Vista vista) {
		this.shell = shell;
		this.display = display;
		this.bundle = bundle;
		this.locale = locale;
		this.vista = vista;
		crearVentana(vista.getEmpleadoActual().getRango());
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
				//TODO
				//System.out.println("Pulsado abrir");
			}
		});
		// Texto del item de menú
		itemAbrir.setText(bundle.getString("I02_men_itm_Abrir") + "\tCtrl+"
				+ bundle.getString("I02_men_itm_abriracc"));
		// Acceso rápido (ctrl+a)
		itemAbrir.setAccelerator(SWT.MOD1
				+ bundle.getString("I02_men_itm_abriracc").charAt(0));

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
				new I12_Ayuda(display, locale, bundle);
			}
		});
		helpHelpItem.setAccelerator(SWT.F1);

	}

	private void crearTabJefeCuadrantes(TabFolder tabFolder) {
		// TODO BD Cargar el cuadrante del departamento de empleadoActual
		// con la fecha del calendario (definido más abajo)
		// en la variable cuadranteActual

		// Crear el tabItem
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
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		Combo cDepartamentos = new Combo(cCuadrantes, SWT.BORDER
				| SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		cDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
		cDepartamentos.select(0);

		// Un canvas para albergar el gráfico de los cuadrantes
		// NO_BACKGROUND + doble buffer para evitar parpadeo
		Composite cCuadrante = new Composite(cCuadrantes, SWT.BORDER);
		cCuadrante.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				3, 5));

		// final I02_cuadr cuadrante = new I02_cuadr(cCuadrante, false,
		// vista.getEmpleado(null,
		// vista.getEmpleadoActual().getIdDepartamento(), null, null, null,
		// null));

		// TODO quitar de aquí la lista de empleados
		final ArrayList<Empleado> empleados;
		empleados = new ArrayList<Empleado>();
/*
		empleados.
		e1.turno.franjaNueva(new Posicion(9, 6), new Posicion(14, 0));
		e1.turno.franjaNueva(new Posicion(16, 0), new Posicion(18, 0));
		e2.turno.franjaNueva(new Posicion(15, 0), new Posicion(22, 0));
		e3.turno.franjaNueva(new Posicion(12, 3), new Posicion(16, 0));
		e3.turno.franjaNueva(new Posicion(18, 0), new Posicion(22, 3));
		e4.turno.franjaNueva(new Posicion(15, 0), new Posicion(19, 9));
		e5.turno.franjaNueva(new Posicion(12, 0), new Posicion(16, 0));
		e6.turno.franjaNueva(new Posicion(10, 5), new Posicion(14, 0));
		e6.turno.franjaNueva(new Posicion(16, 10), new Posicion(19, 0));

		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);
		empleados.add(e4);
		empleados.add(e5);
		empleados.add(e6);
*/
		Label lCalendario = new Label(cCuadrantes, SWT.LEFT);
		lCalendario.setText(bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,false, 2, 1));

		final DateTime calendario = new DateTime(cCuadrantes, SWT.CALENDAR
				| SWT.SHORT);
		calendario.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String[] meses = { bundle.getString("enero"),
						bundle.getString("febrero"), bundle.getString("marzo"),
						bundle.getString("abril"), bundle.getString("mayo"),
						bundle.getString("junio"), bundle.getString("julio"),
						bundle.getString("agosto"),
						bundle.getString("septiembre"),
						bundle.getString("octubre"),
						bundle.getString("noviembre"),
						bundle.getString("diciembre") };
				// TODO BD Cargar el cuadrante con la fecha correspondiente en
				// la variable cuadranteActual
				/*
				System.out.println("Fecha cambiada a "
						+ String.valueOf(calendario.getDay()) + " de "
						+ meses[calendario.getMonth()] + " de "
						+ String.valueOf(calendario.getYear()));
				*/
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,2, 1));
		Date fecha = new Date(calendario.getYear(), calendario.getMonth(),
				calendario.getDay());
//		final I02_cuadr cuadrante = new I02_cuadr(cCuadrante, false, empleados, fecha);
//		 Falta añadir el combobox de los intervalos
/*		e1.anadeGUI(cCuadrante,9,23,3, true);
		e2.anadeGUI(cCuadrante,9,23,3, false);
		e3.anadeGUI(cCuadrante,9,23,3, false);
		e4.anadeGUI(cCuadrante,9,23,3, false);
		e5.anadeGUI(cCuadrante,9,23,3, false);
*/		
//		
//		Img = cuadrante.dameImageImprimible();
/*
		final Button bPorMes = new Button(cCuadrantes, SWT.RADIO);
		bPorMes.setText(bundle.getString("I02_but_Verpormes"));
		bPorMes.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2,
				1));
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
		*/
	}

	/**
	 * Crea un tab de mensajería
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Daniel Dionne
	 */
	private void crearTabMensajes(TabFolder tabFolder) {
		new I02_Tab_Mensajeria(tabFolder, vista, bundle);
	}

	/**
	 * Crea un tab con un listado de empleados
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Daniel Dionne
	 */
	private void crearTabJefeEmpleados(TabFolder tabFolder) {
		new I02_Tab_Jefe_Empleados(tabFolder, vista, bundle);
	}

	/**
	 * Crea un tab con un listado de departamentos
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Daniel Dionne
	 */
	private void crearTabJefeDepartamentos(TabFolder tabFolder) {
		TabItem tabItemDepartamentos = new TabItem(tabFolder, SWT.NONE);
		tabItemDepartamentos.setText(bundle.getString("Departamentos"));
		tabItemDepartamentos.setImage(ico_chicos);

		final Composite cDepartamentos = new Composite(tabFolder, SWT.NONE);
		tabItemDepartamentos.setControl(cDepartamentos);

		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		cDepartamentos.setLayout(new GridLayout(3, false));

		Label lDepartamentos = new Label(cDepartamentos, SWT.LEFT);
		lDepartamentos.setText(bundle.getString("Departamento"));
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		final Combo cmbDepartamentos = new Combo(cDepartamentos, SWT.BORDER
				| SWT.READ_ONLY);
		cmbDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		ArrayList<String> array = vista.getEmpleadoActual().getDepartamentosId();
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				cmbDepartamentos.add(array.get(i));
			}
		}
		// cmbDepartamentos.setItems(new String[] { "Baños", "Cocinas" });
		cmbDepartamentos.select(0);

		// Composite for Buttons: "New Department" and "Configure Department"
		Composite cBut = new Composite(cDepartamentos, SWT.LEFT);
		cBut.setLayout(new GridLayout(2, false));

		// Button "Configure Department"
		Button bConfig = new Button(cBut, SWT.PUSH);
		bConfig.setText(bundle.getString("I02_but_Config_dep"));
		bConfig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		bConfig.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out
						.println("I02 :: Pulsado Configuración departamentos: "
								+ cmbDepartamentos.getText());
				new I10_Config_departamento(shell, bundle, vista);
			}
		});

		// Button "New Department"
		Button bNew = new Button(cBut, SWT.PUSH);
		bNew.setText(bundle.getString("I02_but_Nuevo_dep"));
		bNew.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bNew.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
//				System.out.println("I02 :: Pulsado Nuevo Departamento");
				new I10_Config_departamento(shell, bundle, vista);
			}
		});

		Composite cInfo = new Composite(cDepartamentos, SWT.BORDER);
		cInfo.setLayout(new GridLayout(2, false));
		cInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		Label lContenido = new Label(cInfo, SWT.CENTER);
		lContenido.setText("Aquí va información del departamento");
		lContenido.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				true, 2, 1));

	}

	/**
	 * Crea un tab con un listado de contratos
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Daniel Dionne
	 */
	private void crearTabJefeContratos(TabFolder tabFolder) {
		TabItem tabItemContratos = new TabItem(tabFolder, SWT.NONE);
		tabItemContratos.setText(bundle.getString("Contratos"));
		tabItemContratos.setImage(ico_cuadrante);

		final Composite cContratos = new Composite(tabFolder, SWT.NONE);
		tabItemContratos.setControl(cContratos);

		cContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		cContratos.setLayout(new GridLayout(3, false));

		Table tablaContratos = new Table(cContratos, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tablaContratos.setLinesVisible(true);
		tablaContratos.setHeaderVisible(true);
		String[] titles = { "Nombre contrato", "Empleados", "Total" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaContratos, SWT.NONE);
			column.setText(titles[i]);
		}
		// TODO Quitar este ejemplo
		TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
		tItem.setText(0, "6:40h");
		tItem
				.setText(1,
						"Mike Olfield, Lou Vega, Paul McCartney, Ricky Martin");
		tItem.setText(2, "4");
		tItem = new TableItem(tablaContratos, SWT.NONE);
		tItem.setText(0, "Días sueltos");
		tItem.setText(1, "Alicia Keys, Ana Torroja, Marylin Manson");
		tItem.setText(2, "3");
		for (int i = 0; i < titles.length; i++) {
			tablaContratos.getColumn(i).pack();
		}
		tablaContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));

		final Button bNuevoContrato = new Button(cContratos, SWT.PUSH);
		final Button bModificarContrato = new Button(cContratos, SWT.PUSH);
		final Button bEliminarContrato = new Button(cContratos, SWT.PUSH);

		bNuevoContrato.setText("Nuevo contrato");
		bModificarContrato.setText("Modificar contrato");
		bEliminarContrato.setText("Eliminar contrato");
		bNuevoContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		bModificarContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		bEliminarContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));

		bNuevoContrato.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new I09_1_Creacion_contratos(shell, bundle);
			}
		});

	}

	/**
	 * Crea un tab de inicio para el administrador
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author David Rodilla
	 */
	private void crearTabAdminInicio(TabFolder tabFolder) {
		TabItem tabItemAdminInicio = new TabItem(tabFolder, SWT.NONE);
		tabItemAdminInicio.setText("Admin:Inicio");
		tabItemAdminInicio.setImage(ico_cuadrante);

		// Creamos el contenido de la pestaña cuadrantes

		Composite cInicio = new Composite(tabFolder, SWT.NONE);
		tabItemAdminInicio.setControl(cInicio);

		Image _fondo_turnomatic;
		_fondo_turnomatic = new Image(display, I02_Principal.class.getResourceAsStream("admin_fondo.jpg"));

		cInicio.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		// Le añadimos un layout
		GridLayout lInicio = new GridLayout();
		lInicio.numColumns = 2;
		cInicio.setLayout(lInicio);

		final Label bienvenido = new Label(cInicio, SWT.None);
		bienvenido.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true,
				2, 1));
		bienvenido.setText("BIENVENIDO A TURNOMATIC");

		final Label lReset= new Label(cInicio, SWT.None);
		lReset.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false,1, 1));
		lReset.setText("Pincha este botón para reiniciar la base de datos");

		final Button resetBD = new Button(cInicio, SWT.PUSH);
		resetBD.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, false,1, 1));
		resetBD.setText("RESET BD");
		resetBD.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			public void widgetSelected(SelectionEvent arg0) {
				GeneraDatos.reset();
			}
		});
		cInicio.setBackgroundImage(_fondo_turnomatic);
	}

	/**
	 * Crea un tab de administrador para crear nuevos gerentes
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author David Rodilla
	 */
	private void crearTabAdminNuevoGerente(TabFolder tabFolder) {
		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText("Admin:Nuevo Gerente");
		tabItemEmpleados.setImage(ico_chico);

		// Creamos el contenido de la pestaña cuadrantes
		final Composite cNuevoGerente = new Composite(tabFolder, SWT.BORDER);
		tabItemEmpleados.setControl(cNuevoGerente);

		cNuevoGerente.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
				0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 1;
		cNuevoGerente.setLayout(lGrupo);

		final Label lTitulo = new Label(cNuevoGerente, SWT.LEFT);
		lTitulo.setText("Introduzca los datos del gerente");
		final Composite cNuevoGerente2 = new Composite(cNuevoGerente,
				SWT.BORDER);
		cNuevoGerente2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 0, 0));
		GridLayout lGrupo2 = new GridLayout();
		lGrupo2.numColumns = 2;
		cNuevoGerente2.setLayout(lGrupo2);
		final Label lNombre = new Label(cNuevoGerente2, SWT.LEFT);
		lNombre.setText(bundle.getString("I02AdminNombre"));
		final Text tNombre = new Text(cNuevoGerente2, SWT.BORDER);
		tNombre.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				0, 0));

		final Label lApellidos = new Label(cNuevoGerente2, SWT.LEFT);
		lApellidos.setText(bundle.getString("I02AdminApellidos"));
		final Text tApellidos = new Text(cNuevoGerente2, SWT.BORDER);
		tApellidos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 0, 0));

		final Label lNombreUsuario = new Label(cNuevoGerente2, SWT.LEFT);
		lNombreUsuario.setText(bundle.getString("I02AdminNombreUsuario"));
		final Text tNombreUsuario = new Text(cNuevoGerente2, SWT.BORDER);
		tNombreUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 0, 0));

		final Button bClaveAuto = new Button(cNuevoGerente2, SWT.RADIO);
		bClaveAuto.setText("Generacion automatica de la clave");
		final Button bClaveManual = new Button(cNuevoGerente2, SWT.RADIO);
		bClaveAuto.setSelection(true);

		bClaveManual.setText("Seleccion manual de la clave");
		final Label lContra = new Label(cNuevoGerente2, SWT.LEFT);
		lContra.setText(bundle.getString("I02AdminClave"));
		final Text tPassword = new Text(cNuevoGerente2, SWT.BORDER);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				0, 0));
		tPassword.setText(aplicacion.Util.obtenerClave());
		tPassword.setEditable(false);

		bClaveAuto.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				tNombreUsuario.setText("");
				tNombre.setText("");
				tApellidos.setText("");
				tPassword.setEditable(false);
				tPassword.setText(aplicacion.Util.obtenerClave());
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		bClaveManual.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				tPassword.setEditable(true);
				tNombreUsuario.setText("");
				tNombre.setText("");
				tApellidos.setText("");
				tPassword.setText("");

			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		final Composite cAceptarCancelar = new Composite(cNuevoGerente,
				SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.DOWN,
				false, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);

		// Botones aceptar y cancelar
		final Button bAceptar = new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar = new Button(cAceptarCancelar, SWT.PUSH);

		// Introducimos los textos a los botones
		// bOClave.setText("Obtener clave");
		bAceptar.setText("Aceptar");
		bCancelar.setText(bundle.getString("Cancelar"));
		// Introducimos los valores y eventos de Aceptar

		bAceptar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		bAceptar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(shell,
						SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
				messageBox.setText("CONFIRMACION");
				messageBox.setMessage("¿Desea guardar el nuevo gerente?");
				if (messageBox.open() == SWT.YES) {
					tNombreUsuario.setText("");
					tNombre.setText("");
					tApellidos.setText("");
					tPassword.setText(aplicacion.Util.obtenerClave());
				}
			}
		});
		// Introducimos los valores y eventos de Cancelar
		bCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		bCancelar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});

	}

	/**
	 * Crea un tab de administrador para eliminar gerentes
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author David Rodilla
	 */
	private void crearTabAdminEliminaGerente(TabFolder tabFolder) {
		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText("Admin:Elimina Gerente");
		tabItemEmpleados.setImage(ico_chico);

		// Creamos un composite para la pestaña de mensajes
		final Composite cEliminaGerente = new Composite(tabFolder, SWT.NONE);
		tabItemEmpleados.setControl(cEliminaGerente);

		cEliminaGerente.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		GridLayout lTJ = new GridLayout();
		lTJ.numColumns = 1;
		lTJ.makeColumnsEqualWidth = true;
		cEliminaGerente.setLayout(lTJ);
		// 1º elegimos el gerente que queremos eliminar
		final Label nombreGerente = new Label(cEliminaGerente, SWT.None);
		nombreGerente.setText("Escoja el gerente a eliminar:");
		final Combo comboGerenteElim = new Combo(cEliminaGerente, SWT.BORDER
				| SWT.READ_ONLY);
		final String[] textoListaGerentes = new String[] { "GERENTE1",
				"GERENTE2", "GERENTE3" };
		comboGerenteElim.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				true, 0, 0));
		comboGerenteElim.setItems(textoListaGerentes);
		comboGerenteElim.select(0);

		final Label opcionJefes = new Label(cEliminaGerente, SWT.None);
		opcionJefes
				.setText("¿Que desea hacer con los empleados del gerente seleccionado?:");

		final Button bDejarSinAsignar = new Button(cEliminaGerente, SWT.RADIO);
		bDejarSinAsignar.setText("Dejar sin asignar:");
		bDejarSinAsignar.setSelection(true);
		final Button bAsignarAUnGerente = new Button(cEliminaGerente, SWT.RADIO);
		bAsignarAUnGerente.setText("Asignar a otro gerente:");

		final Combo comboGerenteSust = new Combo(cEliminaGerente, SWT.BORDER
				| SWT.READ_ONLY);
		comboGerenteSust.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				true, 0, 0));
		comboGerenteSust.setItems(textoListaGerentes);
		comboGerenteSust.select(0);

		final Button bAsignarAGerentes = new Button(cEliminaGerente, SWT.RADIO);
		bAsignarAGerentes.setText("Seleccionar asignacion uno a uno:");
		// Introducimos manualmente unos mensajes por defecto
		final Composite cEliminaGerente2 = new Composite(cEliminaGerente,
				SWT.BORDER);

		cEliminaGerente2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		GridLayout lTJ2 = new GridLayout();
		lTJ2.numColumns = 4;
		lTJ2.makeColumnsEqualWidth = true;
		cEliminaGerente2.setLayout(lTJ2);
		cEliminaGerente2.setEnabled(false);
		cEliminaGerente2.setVisible(false);
		comboGerenteSust.setEnabled(false);

		bDejarSinAsignar.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				cEliminaGerente2.setEnabled(false);
				cEliminaGerente2.setVisible(false);
				comboGerenteSust.setEnabled(false);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		bAsignarAUnGerente.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				cEliminaGerente2.setEnabled(false);
				cEliminaGerente2.setVisible(false);
				comboGerenteSust.setEnabled(true);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		bAsignarAGerentes.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				cEliminaGerente2.setEnabled(true);
				cEliminaGerente2.setVisible(true);
				comboGerenteSust.setEnabled(false);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		for (int i = 0; i < 3; i++) {
			/*
			 * TableItem tItem = new TableItem (tablaJefes, SWT.NONE);
			 * tItem.setText (0, "Nombre"); tItem.setText (1, "Apellidos");
			 * tItem.setText (2, "Departamento");
			 */
			final Label nombreJefe = new Label(cEliminaGerente2, SWT.None);
			nombreJefe.setText("nombre Jefe");
			final Label apellidosJefe = new Label(cEliminaGerente2, SWT.None);
			apellidosJefe.setText("apellidos Jefe");
			final Label departamentoJefe = new Label(cEliminaGerente2, SWT.None);
			departamentoJefe.setText("departamento Jefe");
			final Combo combo = new Combo(cEliminaGerente2, SWT.BORDER
					| SWT.READ_ONLY);
			final String[] texto = new String[] { "GERENTE1", "GERENTE2",
					"GERENTE3" };
			combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true,
					0, 0));
			combo.setItems(texto);
			combo.select(0);
		}

		// Creamos los distintos botones
		final Button bEliminar = new Button(cEliminaGerente, SWT.PUSH);
		bEliminar.setText("Eliminar");
		bEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		// Creamos un oyente
		bEliminar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

			}
		});
	}

	/**
	 * Crea un tab de empleado para mostrar el cuadrante
	 * 
	 * @param tabFolder
	 */
	private void crearTabEmpleadoCuadrantes(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Empleado:Cuadrantes");
		tabItem.setImage(ico_chico);

		// Creamos el contenido de la pestaña cuadrantes
		Composite cCuadrantes = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(cCuadrantes);

		cCuadrantes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true,
				1, 1));
		// Le añadimos un layout
		GridLayout lCuadrantes = new GridLayout();
		lCuadrantes.numColumns = 2;
		cCuadrantes.setLayout(lCuadrantes);

		// Creamos el contenido interno de la pestaña cuadrantes
		// Creamos un composite para los botones
		final Composite cBotones = new Composite(cCuadrantes, SWT.BORDER);
		cBotones.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));
		GridLayout lCBotones = new GridLayout();
		lCBotones.numColumns = 1;
		cBotones.setLayout(lCBotones);

		// Creamos un composite para el calendario
		final Label lCalendario = new Label(cBotones, SWT.LEFT);
		lCalendario.setText(this.bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));

		// Creamos un composite para la zona donde se mostrara el calendario
		final Composite cCuadrantesDer = new Composite(cCuadrantes, SWT.BORDER);
		cCuadrantesDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		GridLayout lCuadrantesDer = new GridLayout();
		lCuadrantesDer.numColumns = 1;
		cCuadrantesDer.setLayout(lCuadrantesDer);
		// final Label lCuadr1=new Label (cCuadrantesDer, SWT.CENTER);
		// lCuadr1.setText("Aquí se mostrarían los cuadrantes");
		Empleado empleado=this.vista.getEmpleadoActual();
		final I02_cuadrEmpl cuadrante = new I02_cuadrEmpl(cCuadrantesDer,
				false, bundle,empleado,fecha,vista);
		cuadrante.setSemanal();
		// Creamos el calendario
		final DateTime calendario = new DateTime(cBotones, SWT.CALENDAR);
		calendario.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String[] meses = { "enero", "febrero", "marzo", "abril",
						"mayo", "junio", "julio", "agosto", "septiembre",
						"octubre", "noviembre", "diciembre" };
				int day = calendario.getDay();
				int month = calendario.getMonth();
				int year = calendario.getYear();
				System.out
						.println("Fecha cambiada a " + String.valueOf(day)
								+ " de " + meses[month] + " de "
								+ String.valueOf(year));
				/*
				System.out.println(Util.aFormatoDate(
						Integer.toString(year),
						Integer.toString(month),
						Integer.toString(day))
						);
						*/
				fecha= Date.valueOf(Util.aFormatoDate(Integer.toString(year),
						Integer.toString(month+1),
						Integer.toString(day))
						);
				cuadrante.actualizaFecha(fecha);
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		// Creamos los botones para ver el horario por dias o semanas
		final Button bPorSemanas = new Button(cBotones, SWT.RADIO);
		bPorSemanas.setText(this.bundle.getString("Verporsemana"));
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				2, 1));
		bPorSemanas.setSelection(true);
		bPorSemanas.addListener(SWT.Selection, new Listener() {
			// Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorSemanas.getSelection()) {
					cuadrante.setSemanal();
				} else
					cuadrante.setMensual();

			}
		});

		// Creamos un boton para la seleccion del horario por semanas
		final Button bPorMes = new Button(cBotones, SWT.RADIO);
		bPorMes.setText(this.bundle.getString("I02_but_Verpormes"));
		bPorMes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1,
				1));
		// Creamos un oyente
		bPorMes.addListener(SWT.Selection, new Listener() {
			// Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorMes.getSelection()) {
					cuadrante.setMensual();
				} else
					cuadrante.setSemanal();

			}
		});
	}

	private void crearTabEmpleadoEstadisticas(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Empleado:Estadisticas");
		tabItem.setImage(ico_chico);

		// Creamos el contenido de la pestaña estadisticas
		final Composite cEstadisticas = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(cEstadisticas);

		cEstadisticas.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				true, 1, 1));
		// Le añadimos un layout
		GridLayout lEstadisticas = new GridLayout();
		lEstadisticas.numColumns = 2;
		cEstadisticas.setLayout(lEstadisticas);

		// Creamos el contenido interno de la pestaña cuadrantes
		// Creamos un composite para los botones
		final Composite cEstIzq = new Composite(cEstadisticas, SWT.BORDER);
		cEstIzq.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,
				1));
		GridLayout lEstIzq = new GridLayout();
		lEstIzq.numColumns = 1;
		lEstIzq.makeColumnsEqualWidth = true;
		cEstIzq.setLayout(lEstIzq);

		final Composite cEstDer = new Composite(cEstadisticas, SWT.BORDER);
		cEstDer
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
						1));
		GridLayout lEstDer = new GridLayout();
		lEstDer.numColumns = 1;
		lEstDer.makeColumnsEqualWidth = true;
		cEstDer.setLayout(lEstDer);

		final Label lTitulo = new Label(cEstIzq, SWT.CENTER);
		lTitulo.setText(this.bundle.getString("opcionvis"));
		lTitulo.setFont(new org.eclipse.swt.graphics.Font(
				org.eclipse.swt.widgets.Display.getDefault(), "Arial", 10,
				org.eclipse.swt.SWT.BOLD));
		lTitulo
				.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
						1));

		final Label lTiempo = new Label(cEstIzq, SWT.LEFT);
		lTiempo.setText(this.bundle.getString("tiempodatos"));
		lTiempo
				.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
						1));
		Combo cTiempo = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
		cTiempo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		cTiempo.setItems(new String[] { bundle.getString("semana"),
				bundle.getString("quincena"), bundle.getString("mes"),
				bundle.getString("ano") });
		cTiempo.select(0);

		final Label lComparar = new Label(cEstIzq, SWT.LEFT);
		lComparar.setText(bundle.getString("compararcon"));
		lComparar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
				1));
		Combo cComparar = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
		cComparar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		cComparar.setItems(new String[] { bundle.getString("nadie"),
				bundle.getString("empleadomedio"),
				bundle.getString("mejorsemana"), bundle.getString("mejormes"),
				bundle.getString("mejorano") });

		cComparar.select(0);
		cComparar.setVisibleItemCount(6);

		final Label lTipoGrafico = new Label(cEstIzq, SWT.CENTER);
		lTipoGrafico.setText(bundle.getString("datosvis"));
		lTipoGrafico.setFont(new org.eclipse.swt.graphics.Font(
				org.eclipse.swt.widgets.Display.getDefault(), "Arial", 9,
				org.eclipse.swt.SWT.BOLD));
		lTipoGrafico.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false,
				1, 1));

		final Button bVentasTotales = new Button(cEstIzq, SWT.RADIO);
		bVentasTotales.setText(bundle.getString("verventastot"));
		bVentasTotales.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		// Creamos un oyente
		bVentasTotales.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas totales in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas totales out");
			}
		});

		final Button bVentasPTiempo = new Button(cEstIzq, SWT.RADIO);
		bVentasPTiempo.setText(this.bundle.getString("ventaspertime"));
		bVentasPTiempo.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		// Creamos un oyente
		bVentasPTiempo.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por tiempo de trabajo in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por tiempo de trabajo out");
			}
		});

		final Button bVentasPPrecio = new Button(cEstIzq, SWT.RADIO);
		bVentasPPrecio.setText(this.bundle.getString("ventasporprecio"));
		bVentasPPrecio.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		// Creamos un oyente
		bVentasPPrecio.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por precio producto in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por precio producto out");
			}
		});

		final Button bVentasPDepartamento = new Button(cEstIzq, SWT.RADIO);
		bVentasPDepartamento.setText(this.bundle.getString("ventaspordpto"));
		bVentasPDepartamento.setLayoutData(new GridData(SWT.LEFT, SWT.FILL,
				false, false, 1, 1));
		// Creamos un oyente
		bVentasPDepartamento.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por departamento in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por departamento out");
			}
		});

		final Label lPrueba2 = new Label(cEstDer, SWT.SIMPLE);
		lPrueba2.setText("Aqui se visualizarian las graficas");
		lPrueba2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1,
				1));
	}

	/**
	 * Crea un tab de gerente para mostrar las estadisticas
	 * 
	 * @param tabFolder
	 */
	private void crearTabGerenteEstadisticas(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Gerente:Estadisticas");
		tabItem.setImage(ico_chico);

		// Creamos el contenido de la pestaña estadisticas
		final Composite cEstadisticas = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(cEstadisticas);

		cEstadisticas.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				true, 1, 1));
		// Le añadimos un layout
		GridLayout lEstadisticas = new GridLayout();
		lEstadisticas.numColumns = 2;
		cEstadisticas.setLayout(lEstadisticas);

		// Creamos el contenido interno de la pestaña cuadrantes
		// Creamos un composite para los botones
		final Composite cEstIzq = new Composite(cEstadisticas, SWT.BORDER);
		cEstIzq.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,
				1));
		GridLayout lEstIzq = new GridLayout();
		lEstIzq.numColumns = 1;
		lEstIzq.makeColumnsEqualWidth = true;
		cEstIzq.setLayout(lEstIzq);

		final Composite cEstDer = new Composite(cEstadisticas, SWT.BORDER);
		cEstDer
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
						1));
		GridLayout lEstDer = new GridLayout();
		lEstDer.numColumns = 1;
		lEstDer.makeColumnsEqualWidth = true;
		cEstDer.setLayout(lEstDer);

		final Label lTitulo = new Label(cEstIzq, SWT.CENTER);
		lTitulo.setText(this.bundle.getString("opcionvis"));
		lTitulo.setFont(new org.eclipse.swt.graphics.Font(
				org.eclipse.swt.widgets.Display.getDefault(), "Arial", 10,
				org.eclipse.swt.SWT.BOLD));
		lTitulo
				.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
						1));

		final Label lTiempo = new Label(cEstIzq, SWT.LEFT);
		lTiempo.setText(this.bundle.getString("tiempodatos"));
		lTiempo
				.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
						1));
		Combo cTiempo = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
		cTiempo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		cTiempo.setItems(new String[] { bundle.getString("semana"),
				bundle.getString("quincena"), bundle.getString("mes"),
				bundle.getString("ano") });
		cTiempo.select(0);

		final Label lComparar = new Label(cEstIzq, SWT.LEFT);
		lComparar.setText(bundle.getString("compararcon"));
		lComparar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
				1));
		Combo cComparar = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
		cComparar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		cComparar.setItems(new String[] { bundle.getString("nadie"),
				bundle.getString("empleadomedio"),
				bundle.getString("mejorsemana"), bundle.getString("mejormes"),
				bundle.getString("mejorano") });

		cComparar.select(0);
		cComparar.setVisibleItemCount(6);

		final Label lTipoGrafico = new Label(cEstIzq, SWT.CENTER);
		lTipoGrafico.setText(bundle.getString("datosvis"));
		lTipoGrafico.setFont(new org.eclipse.swt.graphics.Font(
				org.eclipse.swt.widgets.Display.getDefault(), "Arial", 9,
				org.eclipse.swt.SWT.BOLD));
		lTipoGrafico.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false,
				1, 1));

		final Button bVentasTotales = new Button(cEstIzq, SWT.RADIO);
		bVentasTotales.setText(bundle.getString("verventastot"));
		bVentasTotales.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		// Creamos un oyente
		bVentasTotales.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas totales in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas totales out");
			}
		});

		final Button bVentasPTiempo = new Button(cEstIzq, SWT.RADIO);
		bVentasPTiempo.setText(this.bundle.getString("ventaspertime"));
		bVentasPTiempo.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		// Creamos un oyente
		bVentasPTiempo.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por tiempo de trabajo in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por tiempo de trabajo out");
			}
		});

		final Button bVentasPPrecio = new Button(cEstIzq, SWT.RADIO);
		bVentasPPrecio.setText(this.bundle.getString("ventasporprecio"));
		bVentasPPrecio.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		// Creamos un oyente
		bVentasPPrecio.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por precio producto in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por precio producto out");
			}
		});

		final Button bVentasPDepartamento = new Button(cEstIzq, SWT.RADIO);
		bVentasPDepartamento.setText(this.bundle.getString("ventaspordpto"));
		bVentasPDepartamento.setLayoutData(new GridData(SWT.LEFT, SWT.FILL,
				false, false, 1, 1));
		// Creamos un oyente
		bVentasPDepartamento.addFocusListener(new FocusListener() {
			// Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por departamento in");

			}

			// No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por departamento out");
			}
		});

		final Label lPrueba2 = new Label(cEstDer, SWT.SIMPLE);
		lPrueba2.setText("Aqui se visualizarian las graficas");
		lPrueba2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1,
				1));
	}

	/**
	 * Crea un tabFolder. TODO que haga los tabs dependiendo del usuario
	 * autentificado
	 */
	private void crearTabFolder(int rango) {
		// Crear menu tabs
		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		switch (rango) {
		case 0:
			// Tabs de administrador
			crearTabAdminInicio(tabFolder);
			crearTabAdminNuevoGerente(tabFolder);
			crearTabAdminEliminaGerente(tabFolder);
			crearTabJefeCuadrantes(tabFolder);
			crearTabMensajes(tabFolder);
			crearTabJefeEmpleados(tabFolder);
			crearTabJefeDepartamentos(tabFolder);
			crearTabJefeContratos(tabFolder);
			crearTabGerenteEstadisticas(tabFolder);
			crearTabEmpleadoEstadisticas(tabFolder);
			crearTabEmpleadoCuadrantes(tabFolder);
			break;
		case 2:
			// Tabs de jefe
			crearTabJefeCuadrantes(tabFolder);
			crearTabMensajes(tabFolder);
			crearTabJefeEmpleados(tabFolder);
			crearTabJefeDepartamentos(tabFolder);
			crearTabJefeContratos(tabFolder);
			break;
		case 3:
			// Tabs de gerente
			crearTabMensajes(tabFolder);
			crearTabGerenteEstadisticas(tabFolder);
			break;
		default:
			// Tabs de empleado
			crearTabEmpleadoCuadrantes(tabFolder);
			crearTabMensajes(tabFolder);
			crearTabEmpleadoEstadisticas(tabFolder);
		}
	}

	/**
	 * Crea la ventana con el rango del empleado.
	 */
	public void crearVentana(int rango) {
		// Crear la ventana
		shell.setText(bundle.getString("Turno-matic"));// idiomas igual siempre

		// Cargar iconos
		icoGr = new Image(display, I02_Principal.class
				.getResourceAsStream("icoGr.gif"));
		icoPq = new Image(display, I02_Principal.class
				.getResourceAsStream("icoPq.gif"));
		ico_imprimir = new Image(display, I02_Principal.class
				.getResourceAsStream("ico_imprimir.gif"));
		ico_cuadrante = new Image(display, I02_Principal.class
				.getResourceAsStream("ico_cuadrante.gif"));
		ico_chico = new Image(display, I02_Principal.class
				.getResourceAsStream("ico_chico.gif"));
		ico_chica = new Image(display, I02_Principal.class
				.getResourceAsStream("ico_chica.gif"));
		ico_chicos = new Image(display, I02_Principal.class
				.getResourceAsStream("ico_chicos.gif"));

		// Dos iconos de tamaño diferente para SO's que los necesiten
		shell.setImages(new Image[] { icoPq, icoGr });

		crearBarraMenu();

		tray = display.getSystemTray();
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		shell.setImage(icoPq);
		if (tray != null) {
			trayItem.setImage(icoPq);
		}

		// Crear layout principal
		GridLayout lShell = new GridLayout(1, false);
		shell.setLayout(lShell);

		// Poblar ventana: 0 administrador, 1 empleado, 2 jefe, 3 gerente
		crearTabFolder(rango);

		// Crear una barra de estado
		Composite estado = new Composite(shell, SWT.BORDER);
		estado
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
						1));
		estado.setLayout(new GridLayout(2, true));
		lEstado = new Label(estado, SWT.LEFT);
		pbEstado = new ProgressBar(estado, SWT.RIGHT);
		lEstado.setText("Conectado.");
		lEstado
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
						1));
		pbEstado.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		pbEstado.setVisible(false);

		// Ajustar el tamaño de la ventana
		shell.setSize(700, 500);

		// Mostrar ventana centrada en la pantalla
		shell.setLocation(
				display.getBounds().width / 2 - shell.getSize().x / 2, display
						.getBounds().height
						/ 2 - shell.getSize().y / 2);
		shell.open();

		// Preguntar antes de salir
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(shell,
						SWT.APPLICATION_MODAL | SWT.YES | SWT.NO
								| SWT.ICON_WARNING);
				messageBox.setText(bundle.getString("Mensaje"));
				// Diferentes iconos:
				// http://www.developer.com/java/other/article.php/10936_3330861_2
				messageBox.setMessage(bundle.getString("I02_dlg_CerrarAp"));
				e.doit = messageBox.open() == SWT.YES;
			}
		});
	}
	
	public void dispose() {
		icoGr.dispose();
		icoPq.dispose();
		ico_imprimir.dispose();
		//ico_mens.dispose();
		ico_cuadrante.dispose();
		ico_chico.dispose();
		ico_chica.dispose();
		ico_chicos.dispose();
		tray.getItem(0).dispose();
	}

	/**
	 * Muestra el parámetro en la barra de abajo.
	 * 
	 * @param estado
	 *            el String a mostrar
	 */
	public void setTextoEstado(String estado) {
		lEstado.setText(estado);
	}

	/**
	 * Ajusta la barra de progreso al valor del parámetro, y la hace desaparecer
	 * si ha terminado.
	 * 
	 * @param i
	 *            Un valor de 0 a 99, ó 100 para que desaparezca.
	 */
	public void setProgreso(String mensaje, int i) {
		if (i >= 0 && i < 100) {
			lEstado.setText(mensaje);
			pbEstado.setVisible(true);
			pbEstado.setSelection(i);
		} else if (i == 100) {
			lEstado.setText("Conectado");
			pbEstado.setVisible(false);
		}
	}
}