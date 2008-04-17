package interfaces.general;

import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

import algoritmo.ResultadoTurnoMatic;

import java.util.ResourceBundle;
import java.util.Locale;

import java.sql.Date;

import impresion.Imprimir;
import interfaces.admin.*;
import interfaces.empleado.Estadisticas;
import interfaces.empleado.TabCuadranteEmpleado;
import interfaces.general.cuadrantes.I_Cuadrante;
import interfaces.general.mensajeria.TabMensajeria;
import interfaces.jefe.TabDepartamentos;
import interfaces.jefe.TabEmpleados;
import interfaces.jefe.TabContratos;
import aplicacion.Vista;

/**
 * Interfaz de usuario :: Ventana principal
 */
public class ShellPrincipal {
	private Vista vista;
	private Shell shell;
	private Display display;
	private ResourceBundle bundle;
	private Locale locale;
	
	private Label lEstado;
	private ProgressBar pbEstado;
	private Date fechaSeleccionada;
	private Tray tray;
	private Thread algRunner;
	private DateTime calendario;
	private Combo cDepartamentos;
	private I_Cuadrante ic; 

	private int tmAnio, tmMes, tmDia;
	private String tmDep;
	private Button itsMagic, bGuardarCambios; 
	private TabFolder tabFolder;
	private Composite estado;
	private int primerDiaGenerarCuadrante;
	
	/**
	 * Constructor del interfaz principal.
	 * @param shell		el shell sobre el que construir la ventana (se instancia en la Vista)
	 * @param display	display sobre el que se ejecuta la aplicación (también se instancia en la Vista)
	 * @param bundle	paquete de idioma
	 * @param locale	locale actual
	 * @param vista		la Vista
	 */
	public ShellPrincipal(Shell shell, Display display, ResourceBundle bundle, Locale locale, Vista vista) {
		this.shell = shell;
		this.display = display;
		this.bundle = bundle;
		this.locale = locale;
		this.vista = vista;
		if (vista.getEmpleadoActual()!=null) crearVentana(vista.getEmpleadoActual().getRango());
	}
	
	
	/**
	 * Este hilo espera a que cargue la caché, y luego carga el cuadrante
	 * @author Daniel Dionne
	 */
	public class CuadranteLoader extends Thread {
		public synchronized void run() {
			setName("CuadranteLoader");
			try {
				// Espera a que la caché esté cargada
				while (!vista.isCacheCargada()) {
					sleep(50);
				}
			} catch (Exception e) {}
			if (!display.isDisposed()) {
				// Selecciona el día que queremos ver
				ic.setDia(tmDia, tmMes, tmAnio);
				display.asyncExec(new Runnable() {
					public void run() {
						// Introduce los departamentos en el selector de departamentos
						// y carga el cuadrante correspondiente 
						for (int i=0; i<vista.getEmpleadoActual().getDepartamentosId().size(); i++) {
							cDepartamentos.add(vista.getDepartamento(vista.getEmpleadoActual().getDepartamentosId().get(i)).getNombreDepartamento());
						}
						cDepartamentos.setEnabled(true);
						cDepartamentos.select(0);
						tmDep = cDepartamentos.getText();
						ic.setDepartamentoYCarga(tmDep);
						itsMagic.setEnabled(true);
					}
				});
			}
		}
	}
	
	/**
	 * Este hilo ejecuta el algoritmo en segundo plano.
	 * @author Daniel Dionne
	 */
	private class AlgoritmoRun extends Thread {
		public void run() {
			setName("AlgorithmExec");
			display.asyncExec(new Runnable() {
				public void run() {
					itsMagic.setEnabled(false);
					cDepartamentos.setEnabled(false);
				}
			});
			// Mostrar info de debug (si está activada la opción)
			vista.infoDebug("I02_Principal", "Llamando al algoritmo para la fecha " + tmMes + " de " + tmAnio + ", dep. " + tmDep);
			// Instanciar el algoritmo
			algoritmo.TurnoMatic t = new algoritmo.TurnoMatic(primerDiaGenerarCuadrante, tmMes, tmAnio, vista, tmDep);
			// Ejecutar y obtener resultado
			final ResultadoTurnoMatic resultado = t.ejecutaAlgoritmo();
			// Quitar cuadrante de la fecha del calendario
			vista.eliminaMesTrabaja(primerDiaGenerarCuadrante, tmMes, tmAnio, tmDep);
			// Añadir cuadrante nuevo
			vista.insertCuadrante(resultado.getCuadrante());
			// Mostrar resultado, cargar cuadrante en interfaz y redibujar
			display.asyncExec(new Runnable() {
				public void run() {
					MessageBox messageBoxResumen = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK );
					messageBoxResumen.setText(bundle.getString("Resumen"));
					String s = "";
					for (int i=0; i<resultado.getResumen().getInforme().size(); i++) {
						s+= resultado.getResumen().getInforme().get(i) +"\n";
					}
					if (s.equals("")) s=bundle.getString("Alg_CuadranteGenerado");
					messageBoxResumen.setMessage(s);
					messageBoxResumen.open();
					
					vista.setProgreso(bundle.getString("I02_lab_GenerandoCuads"), 100);
					vista.setCursorFlecha();
					ic.setDia(calendario.getDay(), calendario.getMonth()+1,	calendario.getYear());
					ic.cargarDeCache();
					ic.redibujar();
					
					itsMagic.setEnabled(true);
					cDepartamentos.setEnabled(true);
				}
			});
		}
	}
	
	/**
	 * Crea la barra de menú superior de la aplicación.
	 */
	private void crearBarraMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		// Elemento "Archivo"
		MenuItem itemArchivo = new MenuItem(menu, SWT.CASCADE);
		itemArchivo.setText(bundle.getString("I02_men_Archivo"));
	
		// Submenú de persiana asociado al elemento
		Menu subMenuArchivo = new Menu(shell, SWT.DROP_DOWN);
		itemArchivo.setMenu(subMenuArchivo);
		// Aquí los elementos del submenú

/*		// Item Abrir
		MenuItem itemAbrir = new MenuItem(submenu, SWT.PUSH);
		itemAbrir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
			}
		});

		// Texto del item de menú
		itemAbrir.setText(bundle.getString("I02_men_itm_Abrir") + "\tCtrl+"
				+ bundle.getString("I02_men_itm_abriracc"));
		// Acceso rápido (ctrl+a)
		itemAbrir.setAccelerator(SWT.MOD1
				+ bundle.getString("I02_men_itm_abriracc").charAt(0));
*/
		// Archivo - Imprimir
		MenuItem itemImprimir = new MenuItem(subMenuArchivo, SWT.PUSH);
		itemImprimir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (vista.isCacheCargada()) {
					Imprimir imprimir = new Imprimir(display);
					imprimir.imprimirImage(vista.getEmpleadoActual(), bundle);
				} else {
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.OK);
					messageBox.setText(bundle.getString("Error"));
					messageBox.setMessage(bundle
							.getString("I02_dlg_EsperarCache"));
					messageBox.open();
				}
			}
		});
		itemImprimir.setImage(vista.getImagenes().getIco_imprimir());
		itemImprimir.setText(bundle.getString("I02_men_itm_Imprimir")
				+ "\tCtrl+" + bundle.getString("I02_men_itm_imprimiracc"));
		itemImprimir.setAccelerator(SWT.MOD1
				+ bundle.getString("I02_men_itm_imprimiracc").charAt(0));

		// Archivo - Cerrar sesión
		MenuItem itemCerrarSesion = new MenuItem(subMenuArchivo, SWT.PUSH);
		itemCerrarSesion.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// TODO
//				vista.login();
			}
		});
		itemCerrarSesion.setText(bundle.getString("I02_men_itm_CerrarSes") + "\tCtrl+"
				+ bundle.getString("I02_men_itm_cerrarSesacc"));
		// Acceso rápido (ctrl+c)
		itemCerrarSesion.setAccelerator(SWT.MOD1 + bundle.getString("I02_men_itm_cerrarSesacc").charAt(0));
		itemCerrarSesion.setEnabled(false);
		
		// Archivo - Salir
		MenuItem itemSalir = new MenuItem(subMenuArchivo, SWT.PUSH);
		itemSalir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.close();
			}
		});
		itemSalir.setText(bundle.getString("I02_men_itm_Salir") + "\tCtrl+" + bundle.getString("I02_men_itm_saliracc"));
		// Acceso rápido (ctrl+s)
		itemSalir.setAccelerator(SWT.MOD1 + bundle.getString("I02_men_itm_saliracc").charAt(0));
		
		
		// Ayuda
		MenuItem helpMenuHeader = new MenuItem(menu, SWT.CASCADE);
		helpMenuHeader.setText(bundle.getString("I02_men_Ayuda"));
		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);

		// Ayuda - Ayuda
		MenuItem helpHelpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpHelpItem.setText(bundle.getString("I02_men_itm_Ayuda") + "\tF1");
		helpHelpItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String helppath = "/Ayuda/" + locale.getCountry() + "/index.html";
				new ShellAyuda(display, locale, bundle, helppath);
			}
		});
		
		// Ayuda - Enviar informe
		MenuItem menuInforme = new MenuItem(helpMenu, SWT.PUSH);
		menuInforme.setText(bundle.getString("I02_men_itm_Informe"));
		menuInforme.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new ShellExcepcion(vista, null);
			}
		});

		helpHelpItem.setAccelerator(SWT.F1);
		
		// TODO estaría guay un "acerca de"
	}

	private void crearTabJefeCuadrantes(TabFolder tabFolder) {
		// Crear el item del tabFolder
		TabItem tabItemCuadrantes = new TabItem(tabFolder, SWT.NONE);
		tabItemCuadrantes.setText(bundle.getString("Cuadrantes"));
		tabItemCuadrantes.setImage(vista.getImagenes().getIco_cuadrante());

		final Composite cCuadrantes = new Composite(tabFolder, SWT.NONE);
		tabItemCuadrantes.setControl(cCuadrantes);
		
		// Configuración del composite
		cCuadrantes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true,
				1, 1));
		cCuadrantes.setLayout(new GridLayout(5, false));

		// Componentes del composite
		Label lDepartamentos = new Label(cCuadrantes, SWT.NONE);
		lDepartamentos.setText(bundle.getString("I02_lab_Dpto"));
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		cDepartamentos = new Combo(cCuadrantes, SWT.BORDER | SWT.READ_ONLY);
		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		
		cDepartamentos.setEnabled(false);
		tmDep = cDepartamentos.getText();

		final Composite cCuadrante = new Composite(cCuadrantes, SWT.BORDER);
		cCuadrante.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 7));

		// TODO arreglar parámetros (cogerlos del departamento)
		ic = new I_Cuadrante(vista, 0, 0, tmDep, 4, 9, 23);
		Thread loader = new Thread(new CuadranteLoader());
		loader.start();
		cDepartamentos.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				ic.setDepartamentoYCarga(cDepartamentos.getText());
				tmDep = cDepartamentos.getText();
			}
		});

		Label lCalendario = new Label(cCuadrantes, SWT.LEFT);
		lCalendario.setText(bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 2, 1));

		calendario = new DateTime(cCuadrantes, SWT.CALENDAR | SWT.SHORT);
		tmAnio = calendario.getYear();
		tmMes = calendario.getMonth()+1;
		tmDia = calendario.getDay();

		ic.setDia(tmDia, tmMes, tmAnio);
		
		calendario.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tmAnio = calendario.getYear();
				tmMes = calendario.getMonth()+1;
				tmDia = calendario.getDay();
				vista.infoDebug("I02_Principal", "Fecha cambiada a "
						+ String.valueOf(calendario.getDay())
						+ " de "
						+ aplicacion.utilidades.Util.mesAString(bundle, calendario
								.getMonth()) + " de "
						+ String.valueOf(calendario.getYear()));
				vista.setCursorEspera();
				vista.setProgreso(bundle.getString("I02_lab_CargandoCuads"), 50);
				ic.setDia(calendario.getDay(), calendario.getMonth()+1,
						calendario.getYear());
				vista.setProgreso(bundle.getString("I02_lab_CargandoCuads"),100);
				vista.setCursorFlecha();
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				2, 1));
		fechaSeleccionada = new Date(calendario.getYear(), calendario
				.getMonth(), calendario.getDay());
		final Button bPorMes = new Button(cCuadrantes, SWT.RADIO);
		bPorMes.setText(bundle.getString("I02_but_Verpormes"));
		bPorMes.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		// Oyente para saber cuando se ha modificado la seleccion del boton
		/** ********************************************************* */
		bPorMes.addListener(SWT.Selection, new Listener() {
			// Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorMes.getSelection()) {
					ic.setDiario(false);
					ic.setMovCuadSemanal(false);
					//ic.redibujar();
				} else {
					ic.setDiario(true);
					ic.setMovCuadSemanal(true);
					//ic.redibujar();
				}
			}
		});
		/** ********************************************************* */
		final Button bPorSemanas = new Button(cCuadrantes, SWT.RADIO);
		bPorSemanas.setText(bundle.getString("I02_but_Verpordia"));
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				2, 1));
		bPorSemanas.setSelection(true);
		
		
		// Botón de generación de cuadrantes
		itsMagic = new Button(cCuadrantes, SWT.PUSH);
		itsMagic.setEnabled(false);
		itsMagic.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		itsMagic.setText(bundle.getString("I02_but_generarCuadrantes"));
		itsMagic.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			public void widgetSelected(SelectionEvent arg0) {
				if (vista.isCacheCargada()) {
					// Preguntar si desea sobrerescribir los datos 
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
					messageBox.setText(bundle.getString("Aviso"));
					messageBox.setMessage(
							bundle.getString("I02_dlg_CrearCuadrante1") 
							+ (calendario.getMonth()+1) + "/"
							+ calendario.getYear() + " " +
							bundle.getString("I02_dlg_CrearCuadrante2"));
					if (messageBox.open()==SWT.YES) {
						// Preguntar desde dónde quiere empezar
						messageBox = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CANCEL);
						messageBox.setText(bundle.getString("Aviso"));
						// TODO usar bundle
						messageBox.setMessage("¿Quiere calcular el mes entero? (en caso contrario, se empezará" +
								" en el día seleccionado)");
						int r = messageBox.open();
						if (r==SWT.YES) 
							primerDiaGenerarCuadrante = 1;
						else if (r==SWT.NO)
							primerDiaGenerarCuadrante = tmDia;
						if (r!=SWT.CANCEL) {
							// Borrar tabla trabaja para esa fecha y ese departamento
							vista.eliminaMesTrabaja(primerDiaGenerarCuadrante, tmMes, tmAnio, tmDep);
							vista.setProgreso(bundle.getString("I02_lab_GenerandoCuads"), 0);
							vista.setCursorEspera();
							algRunner = new AlgoritmoRun();
							algRunner.start();
						}
					}
				} else {
					MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK);
					messageBox.setText(bundle.getString("Aviso"));
					messageBox.setMessage("Por favor, espere a que se carguen los datos.");
					messageBox.open();					
				}
			}
		});

		// Botón para guardar los cambios sobre un cuadrante
		bGuardarCambios = new Button(cCuadrantes, SWT.PUSH);
		bGuardarCambios.setEnabled(false);
		bGuardarCambios.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		bGuardarCambios.setText(bundle.getString("I02_but_guardarCambios"));
		bGuardarCambios.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			public void widgetSelected(SelectionEvent arg0) {
				bGuardarCambios.setEnabled(false);
				
				// TODO Guardar los cambios
			}
		});
		ic.setComposite(cCuadrante,bPorMes,bPorSemanas, bGuardarCambios, calendario);
	}

	/**
	 * Crea un tab de mensajería
	 * 
	 * @param tabFolder el tabFolder donde colocarlo
	 */
	private void crearTabMensajes(TabFolder tabFolder) {
		new TabMensajeria(tabFolder, vista, bundle);
	}
	
	/**
	 * Crea un tab de cuadrante de empleados
	 * 
	 * @param tabFolder el tabFolder donde colocarlo
	 */
	private void crearTabEmpleadoCuadrantes(TabFolder tabFolder) {
		new TabCuadranteEmpleado(tabFolder, vista, bundle);
	}
	/**
	 * Crea un tab con un listado de empleados
	 * 
	 * @param tabFolder el tabFolder donde colocarlo
	 */
	private void crearTabJefeEmpleados(TabFolder tabFolder) {
		new TabEmpleados(tabFolder, vista, bundle);
	}

	/**
	 * Crea un tab de admin de departamentos
	 * 
	 * @param tabFolder el tabFolder donde colocarlo
	 */
	private void crearTabAdminDepartamentos(TabFolder tabFolder) {
		new TabDepartamentosAdmin(tabFolder, vista, bundle);
	}

	
	/**
	 * Crea un tab con un listado de contratos
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Jose Maria Martin
	 */
	private void crearTabJefeContratos(TabFolder tabFolder) {
		new TabContratos(tabFolder,vista,bundle,locale);
	}
	
	/**
	 * Crea un tab con un listado de contratos para el menú de administrador
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Jose Maria Martin
	 */
	private void crearTabAdminContratos(TabFolder tabFolder) {
		new TabContratosAdmin(tabFolder,vista,bundle,locale);
	}

	/**
	 * Crea un tab con un listado de contratos para el menú de administrador
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Jose Maria Martin
	 */
	private void crearTabJefeDepartamentos(TabFolder tabFolder) {
		new TabDepartamentos(tabFolder,vista,bundle,shell);
	}
	/**
	 * Crea un tab de inicio para el administrador
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Roberto Garcia & Jose Maria Martin
	 */
	private void crearTabAdminInicio(TabFolder tabFolder) {
		new TabInicio(bundle,vista,tabFolder);
	}

	/**
	 * Crea un tab de administrador para crear nuevos jefes
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Roberto Garcia
	 */
	private void crearTabAdminNuevoJefe(TabFolder tabFolder) {
		new TabNuevoJefe(tabFolder,bundle,vista);

	}

	/**
	 * Crea un tab de administrador para eliminar gerentes
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author David Rodilla
	 */
	private void crearTabAdminEliminaJefe(TabFolder tabFolder) {
		new TabEliminaJefe(bundle,vista,tabFolder);
	}

	private void crearTabEmpleadoEstadisticas(TabFolder tabFolder) {
		new Estadisticas(bundle,vista,tabFolder);
	}

	/**
	 * Crea un tab de gerente para mostrar las estadisticas
	 * 
	 * @param tabFolder
	 */
	private void crearTabGerenteEstadisticas(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Gerente:Estadisticas");
		tabItem.setImage(vista.getImagenes().getIco_chico());

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
	public void crearTabFolder(int rango) {
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		switch (rango) {
		case 0:
			// Tabs de administrador
			vista.getControlador().abrirConexionBD();
			crearTabAdminInicio(tabFolder);
//			crearTabAdminNuevoJefe(tabFolder);
//			crearTabAdminEliminaJefe(tabFolder);
			crearTabAdminDepartamentos(tabFolder);	
//			crearTabAdminContratos(tabFolder);
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

		// Dos iconos de tamaño diferente para SO's que los necesiten
		shell.setImages(new Image[] { vista.getImagenes().getIcoPq(), vista.getImagenes().getIcoGr() });

		crearBarraMenu();

		tray = display.getSystemTray();
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		shell.setImage(vista.getImagenes().getIcoPq());
		if (tray != null) {
			trayItem.setImage(vista.getImagenes().getIcoPq());
		}

		// Crear layout principal
		GridLayout lShell = new GridLayout(1, false);
		shell.setLayout(lShell);

		// Crear menu tabs
		tabFolder = new TabFolder(shell, SWT.NONE);
		// Poblar ventana: 0 administrador, 1 empleado, 2 jefe, 3 gerente
		crearTabFolder(rango);
		
		// Crear una barra de estado
		estado = new Composite(shell, SWT.BORDER);
		estado.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,1));
		estado.setLayout(new GridLayout(2, true));
		lEstado = new Label(estado, SWT.LEFT);
		pbEstado = new ProgressBar(estado, SWT.RIGHT);
		lEstado.setText("Conectado.");
		lEstado.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		pbEstado.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		pbEstado.setVisible(false);

		// Ajustar el tamaño de la ventana
		shell.setSize(700, 500);

		// Mostrar ventana centrada en la pantalla
		shell.setLocation(
				display.getBounds().width / 2 - shell.getSize().x / 2, 
				display.getBounds().height/ 2 - shell.getSize().y / 2);
		shell.open();

		// Preguntar antes de salir
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(shell,
						SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_WARNING);
				messageBox.setText(bundle.getString("Mensaje"));
				// Diferentes iconos:
				// http://www.developer.com/java/other/article.php/10936_3330861_2
				messageBox.setMessage(bundle.getString("I02_dlg_CerrarAp"));
				if (messageBox.open() == SWT.YES) {
					e.doit = true;
					vista.stop();
					vista.getControlador().cerrarConexionBD();
				}
				else e.doit = false;
			}
		});
	}

	public Shell getShell() {
		return shell;
	}
	
	public void dispose() {
		
		tabFolder.dispose();
		estado.dispose();
		// tray.getItem(0).dispose();
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
	 *            Un valor de 0 a 99, ó >100 para que desaparezca.
	 */
	public void setProgreso(String mensaje, int i) {
		final int i2 = i;
		final String mens = mensaje;
		if (!display.isDisposed()) {
			display.asyncExec(new Runnable() {
				public void run() {
					if (i2 >= 0 && i2 < 100) {
						lEstado.setText(mens);
						pbEstado.setVisible(true);
						pbEstado.setSelection(i2);
					} else if (i2 >= 100) {
						lEstado.setText("Conectado");
						pbEstado.setVisible(false);
					}
				}
			});
		}
	}
}