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
import interfaces.I10_Config_departamento;
import interfaces.I10_Nuevo_departamento;
import interfaces.admin.I03_Tab_NuevoJefe;
import interfaces.admin.I04_Tab_AdminInicio;
import interfaces.admin.I05_Tab_EliminaJefe;
import interfaces.admin.I06_Tab_Contratos_Admin;
import interfaces.empleado.I02_cuadrEmpl;
import interfaces.empleado.I07_empleado_estadisticas;
import interfaces.general.cuadrantes.I_Cuadrante;
import interfaces.general.mensajeria.I02_Tab_Mensajeria;
import interfaces.jefe.I02_Tab_Jefe_Empleados;
import interfaces.jefe.I09_Tab_Contratos;
import aplicacion.Vista;
import aplicacion.datos.Empleado;
import aplicacion.utilidades.Util;

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
	
	private Label lEstado;
	private ProgressBar pbEstado;
	private Date fechaSeleccionada;
	private Tray tray;
	private Thread algRunner;
	private DateTime calendario;
	private Combo cDepartamentos;
	private I_Cuadrante ic; 
	//apaño de última hora para ejecutar el algoritmo en un thread
	private int tmAnio, tmMes, tmDia;
	private String tmDep;
	private Button itsMagic, bGuardarCambios; 
	private TabFolder tabFolder;
	private Composite estado;
	private int primerDiaGenerarCuadrante;
	
	public I02_Principal(Shell shell, Display display, ResourceBundle bundle,
			Locale locale, Vista vista) {
		this.shell = shell;
		this.display = display;
		this.bundle = bundle;
		this.locale = locale;
		this.vista = vista;
		if (vista.getEmpleadoActual()!=null) crearVentana(vista.getEmpleadoActual().getRango());
	}
	
	
	/**
	 * Este hilo espera a que cargue la caché, y luego carga el cuadrante
	 * @author Daniel D
	 */
	public class CuadranteLoader extends Thread {
		public synchronized void run() {
			setName("CuadranteLoader");
			try {
				while (!vista.isCacheCargada()) {
					sleep(50);
				}
			} catch (Exception e) {}
			if (!display.isDisposed()) {
				display.asyncExec(new Runnable() {
					public void run() {
						for (int i=0; i<vista.getEmpleadoActual().getDepartamentosId().size(); i++) {
							cDepartamentos.add(vista.getDepartamento(vista.getEmpleadoActual().getDepartamentosId().get(i)).getNombreDepartamento());
						}
						cDepartamentos.setEnabled(true);
						cDepartamentos.select(0);
						tmDep = cDepartamentos.getText();
						ic.setDepartamento(tmDep);
						itsMagic.setEnabled(true);
					}
				});
				ic.setDia(tmDia, tmMes, tmAnio);
			}
		}
	}
	
	/**
	 * Este hilo ejecuta el algoritmo en segundo plano
	 * @author Daniel D
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
			vista.infoDebug("I02_Principal", "Llamando al algoritmo para la fecha " + tmMes + " de " + tmAnio + ", dep. " + tmDep);
			// AQUI SE LLAMA AL ALGORITMO
			// la variable primerDiaGenerarCuadrante lleva el primer día

			algoritmo.TurnoMatic t = new algoritmo.TurnoMatic(tmMes, tmAnio, vista, tmDep);
			final ResultadoTurnoMatic resultado = t.ejecutaAlgoritmo();
			// quitar cuadrante de la fecha del calendario de la cache
			// añadir t.getcuadrante a cache
			vista.eliminaCuadranteCache(tmMes, tmAnio, tmDep);
			vista.insertCuadranteCache(resultado.getCuadrante());
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
	
	private void crearBarraMenu() {
		// Una barra de menús
		Menu barra = new Menu(shell, SWT.BAR);
		shell.setMenuBar(barra);
		// Con un elemento "archivo"
		MenuItem archivoItem = new MenuItem(barra, SWT.CASCADE);
		archivoItem.setText(bundle.getString("I02_men_Archivo"));
	
		MenuItem debugMenu = new MenuItem(barra, SWT.CASCADE);
		debugMenu.setText("Debug");
		debugMenu.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new I19_Excepcion(vista, null);
			}
		});
	
		// Y un submenú de persiana asociado al elemento
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		archivoItem.setMenu(submenu);
		// Aquí los elementos del submenú

/*		// Item Abrir
		MenuItem itemAbrir = new MenuItem(submenu, SWT.PUSH);
		itemAbrir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// TODO
				// System.out.println("Pulsado abrir");
			}
		});

		// Texto del item de menú
		itemAbrir.setText(bundle.getString("I02_men_itm_Abrir") + "\tCtrl+"
				+ bundle.getString("I02_men_itm_abriracc"));
		// Acceso rápido (ctrl+a)
		itemAbrir.setAccelerator(SWT.MOD1
				+ bundle.getString("I02_men_itm_abriracc").charAt(0));
*/
		// Item Imprimir
		MenuItem itemImprimir = new MenuItem(submenu, SWT.PUSH);
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

		// Item Cerrar sesión
		MenuItem itemCerrarSesion = new MenuItem(submenu, SWT.PUSH);
		itemCerrarSesion.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
//				vista.login();
			}
		});
		// Texto del item de menú
		itemCerrarSesion.setText(bundle.getString("I02_men_itm_CerrarSes") + "\tCtrl+"
				+ bundle.getString("I02_men_itm_cerrarSesacc"));
		// Acceso rápido (ctrl+c)
		itemCerrarSesion.setAccelerator(SWT.MOD1 + bundle.getString("I02_men_itm_cerrarSesacc").charAt(0));
		itemCerrarSesion.setEnabled(false);
		
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
				String helppath = "/Ayuda/" + locale.getCountry() + "/index.html";
				new I12_Ayuda(display, locale, bundle, helppath);
			}
		});
		helpHelpItem.setAccelerator(SWT.F1);
		setProgreso("Cargando datos", 10);
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
				ic.setDepartamento(cDepartamentos.getText());
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
								SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
						messageBox.setText(bundle.getString("Aviso"));
						// TODO usar bundle
						messageBox.setMessage("¿Quiere empezar calcular todo el mes? (en caso contrario, se empezará" +
								" en el día seleccionado)");
						if (messageBox.open()==SWT.YES) 
							primerDiaGenerarCuadrante = 1;
						else
							primerDiaGenerarCuadrante = tmDia;
						// Borrar tabla trabaja para esa fecha y ese departamento
						vista.eliminaMesTrabaja(tmMes, tmAnio, tmDep);
						vista.setProgreso(bundle.getString("I02_lab_GenerandoCuads"), 0);
						vista.setCursorEspera();
						algRunner = new AlgoritmoRun();
						algRunner.start();

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
	 * @author Daniel Dionne && Carlos Sánchez
	 */
	private void crearTabAdminDepartamentos(TabFolder tabFolder) {
		TabItem tabItemDepartamentos = new TabItem(tabFolder, SWT.NONE);
		tabItemDepartamentos.setText(bundle.getString("Departamentos"));
		tabItemDepartamentos.setImage(vista.getImagenes().getIco_chicos());

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

		// ArrayList<String> array = vista.getEmpleadoActual()
		// .getDepartamentosId();
		ArrayList<String> array = vista.getNombreTodosDepartamentos();
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
				new I10_Config_departamento(shell, bundle, vista,
						cmbDepartamentos.getText(),cmbDepartamentos,true);
			}
		});

		// Button "New Department"
		Button bNew = new Button(cBut, SWT.PUSH);
		bNew.setText(bundle.getString("I02_but_Nuevo_dep"));
		bNew.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bNew.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// System.out.println("I02 :: Pulsado Nuevo Departamento");
				new I10_Nuevo_departamento(shell,bundle,vista,cmbDepartamentos);
			}
		});
		
		// Button "Delete Department"
		Button bDel = new Button(cBut, SWT.PUSH);
		bDel.setText(bundle.getString("I02_but_Del_dep"));
		bDel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bDel.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if(vista.tieneEmpleados(cmbDepartamentos.getText())){
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I02_err_elim"));
					e.doit = messageBox.open () == SWT.CLOSE;
				}else{//si no tiene ningun empleado
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I02_confirm_elim_dep"));
					if(messageBox.open () == SWT.OK){
						vista.eliminaDepartamento(cmbDepartamentos.getText());
					}
				}
			}
		});

		Composite cInfo = new Composite(cDepartamentos, SWT.BORDER);
		cInfo.setLayout(new GridLayout(2, false));
		cInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		final Text lContenido = new Text(cInfo,SWT.READ_ONLY | SWT.MULTI |SWT.V_SCROLL);
		lContenido.setEditable(false);
		lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
		lContenido.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		
		//listener para el combo y mostrar la info debajo
		cmbDepartamentos.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
			}
		});

	}

	private void crearTabJefeDepartamentos(TabFolder tabFolder) {
		TabItem tabItemDepartamentos = new TabItem(tabFolder, SWT.NONE);
		tabItemDepartamentos.setText(bundle.getString("Departamentos"));
		tabItemDepartamentos.setImage(vista.getImagenes().getIco_chicos());

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
		
		ArrayList<String> array = vista.getNombreDepartamento(vista.getEmpleadoActual());
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				cmbDepartamentos.add(array.get(i));
			}
		}
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
				new I10_Config_departamento(shell, bundle, vista,
						cmbDepartamentos.getText(),cmbDepartamentos,false);
			}
		});

		Composite cInfo = new Composite(cDepartamentos, SWT.BORDER);
		cInfo.setLayout(new GridLayout(2, false));
		cInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		final Text lContenido = new Text(cInfo, SWT.READ_ONLY | SWT.MULTI |SWT.V_SCROLL);
		lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
		lContenido.setEditable(false);
		lContenido.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		
		//listener para el combo y mostrar la info debajo
		cmbDepartamentos.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
			}
		});

	}
	
	/**
	 * Crea un tab con un listado de contratos
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Jose Maria Martin
	 */
	private void crearTabJefeContratos(TabFolder tabFolder) {
		new I09_Tab_Contratos(tabFolder,vista,bundle,locale);
	}
	
	/**
	 * Crea un tab con un listado de contratos para el menú de administrador
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Jose Maria Martin
	 */
	private void crearTabAdminContratos(TabFolder tabFolder) {
		new I06_Tab_Contratos_Admin(tabFolder,vista,bundle,locale);
	}

	/**
	 * Crea un tab de inicio para el administrador
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Roberto Garcia & Jose Maria Martin
	 */
	private void crearTabAdminInicio(TabFolder tabFolder) {
		new I04_Tab_AdminInicio(bundle,vista,tabFolder);
	}

	/**
	 * Crea un tab de administrador para crear nuevos jefes
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author Roberto Garcia
	 */
	private void crearTabAdminNuevoJefe(TabFolder tabFolder) {
		new I03_Tab_NuevoJefe(tabFolder,bundle,vista);

	}

	/**
	 * Crea un tab de administrador para eliminar gerentes
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @author David Rodilla
	 */
	private void crearTabAdminEliminaJefe(TabFolder tabFolder) {
		new I05_Tab_EliminaJefe(bundle,vista,tabFolder);
	}

	/**
	 * Crea un tab de empleado para mostrar el cuadrante
	 * 
	 * @param tabFolder
	 */
	private void crearTabEmpleadoCuadrantes(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Empleado:Cuadrantes");
		tabItem.setImage(vista.getImagenes().getIco_chico());

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
		Empleado empleado = this.vista.getEmpleadoActual();
		final I02_cuadrEmpl cuadrante = new I02_cuadrEmpl(cCuadrantesDer,
				false, bundle, empleado, fechaSeleccionada, vista);
		cuadrante.setSemanal();

		// Creamos el calendario
		final DateTime calendario = new DateTime(cBotones, SWT.CALENDAR);
		calendario.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int day = calendario.getDay();
				int month = calendario.getMonth();
				int year = calendario.getYear();
				fechaSeleccionada = Date.valueOf(Util.aFormatoDate(Integer
						.toString(year), Integer.toString(month + 1), Integer
						.toString(day)));
				cuadrante.actualizaFecha(fechaSeleccionada);
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
				} else{
				//	bPorSemanas.setFocus();
				}
				//	cuadrante.setMensual();

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
					//crear ventana informando
					/*
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					messageBox.setText(bundle.getString("Mensaje"));
					// Diferentes iconos:
					messageBox.setMessage("Vista por meses, aun no creada");
					if (messageBox.open() == SWT.OK) {
						
						bPorSemanas.setFocus();
					}*/
				
					
					
				} else{
					
				}
				//	cuadrante.setSemanal();

			}
		});
	}

	private void crearTabEmpleadoEstadisticas(TabFolder tabFolder) {
		new I07_empleado_estadisticas(bundle,vista,tabFolder);
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