/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal Empleado
 *   por David Rodilla
 *   
 * Interfaz principal de la aplicación para un empleado
 * ver 0.1
 *******************************************************************************/


package interfaces;

import idiomas.LanguageChanger;
import java.util.Locale;
import java.util.ResourceBundle;
import org.eclipse.swt.*;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Clase que implementa la interfaz en caso 
 * de que entre al sistema un empleado
 * @author David Rodilla
 */
public class I02Empleado {
	Shell _shell;
	Display _display;
	ResourceBundle _bundle;
	Locale _locale;
	Image _icoGr, _icoPq, _ico_imprimir, _ico_mens_l, _ico_mens, _ico_cuadrante,
			_ico_chico, _ico_chica, _ico_chicos,_ico_estadisticas;
	

	/**
	 * Constructor de clase
	 * (crea la ventana)
	 */
	public I02Empleado(Shell shell, Display display, ResourceBundle bundle, Locale locale) {
		//creamos un display y un shell donde emplazar los widgets (elementos swt)
		//display = new Display ();
		//shell = new Shell (display);
		this._shell = shell;
		this._display = display;
		this._bundle = bundle;
		this._locale = locale;
		
		//creamos los iconos que vamos a utilizar en el programa
		_icoGr = new Image(_display, I02.class.getResourceAsStream("icoGr.gif"));
		_icoPq = new Image(_display, I02.class.getResourceAsStream("icoPq.gif"));
		_ico_imprimir = new Image(_display, I02.class.getResourceAsStream("ico_imprimir.gif"));
		_ico_mens_l = new Image(_display, I02.class.getResourceAsStream("ico_mens1_v.gif"));
		_ico_mens = new Image(_display, I02.class.getResourceAsStream("ico_mens2_v.gif"));
		_ico_cuadrante = new Image(_display, I02.class.getResourceAsStream("ico_cuadrante.gif"));
		_ico_estadisticas = new Image(_display, I02.class.getResourceAsStream("grafica_1.ico"));
		
		
		// Dos iconos de tamaño diferente para SO's que los necesiten
		_shell.setImages(new Image[] {_icoPq,_icoGr});
		_shell.setText("Turno-matic");
		_shell.setVisible(true);
		
		@SuppressWarnings("unused")
		Menu barra = barraMenus();
		
		final Tray tray = _display.getSystemTray();
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		_shell.setImage(_icoPq);
		if (tray != null) {			
			trayItem.setImage(_icoPq);
		}
				
		//Establecemos el layout externo
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		_shell.setLayout(lShell);

		@SuppressWarnings("unused")
		final TabFolder tabFolder = crearPestañas();

		// Ajustar el tamaño de la ventana al contenido
		_shell.pack();
		// Mostrar ventana centrada en la pantalla
		_shell.setLocation(_display.getBounds().width/2 - _shell.getSize().x/2, _display.getBounds().height/2 - _shell.getSize().y/2);
		_shell.open();
		
		// Preguntar antes de salir
		_shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event event) {
				MessageBox messageBox = new MessageBox (_shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
				messageBox.setText ("Mensaje");
				messageBox.setMessage (_bundle.getString("cierre"));
				event.doit = messageBox.open () == SWT.YES;
			}
		});

		// Este bucle mantiene la ventana abierta
		while (!_shell.isDisposed()) {
			if (!_display.readAndDispatch()) {
				_display.sleep();
			}
		}
		_display.dispose();
	}
	
	private Menu barraMenus(){
		// Una barra de menús
		Menu barra = new Menu (_shell, SWT.BAR);
		_shell.setMenuBar (barra);
		// Con un elemento "archivo"
		MenuItem archivoItem = new MenuItem (barra, SWT.CASCADE);
		archivoItem.setText(_bundle.getString("I02_men_Archivo"));
		
		// Y un submenú de persiana asociado al elemento
		Menu submenu = new Menu (_shell, SWT.DROP_DOWN);
		archivoItem.setMenu (submenu);
		// Aquí los elementos del submenú
		MenuItem itemSeleccionar = new MenuItem (submenu, SWT.PUSH);
		itemSeleccionar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
			
			}
		});
		// Texto del item de menú
		itemSeleccionar.setText(_bundle.getString("I02_men_itm_Abrir"));
		archivoItem.setAccelerator(_bundle.getString("I02_men_itm_abriracc").charAt(0));
		MenuItem itemImprimir = new MenuItem (submenu, SWT.PUSH);
		itemImprimir.setImage(_ico_imprimir);
		itemImprimir.setText(_bundle.getString("I02_men_itm_Imprimir"));
		itemImprimir.setAccelerator(_bundle.getString("I02_men_itm_imprimiracc").charAt(0));

		MenuItem itemSalir = new MenuItem (submenu, SWT.PUSH);
		itemSalir.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				_shell.close();
			}
		});
		// Texto del item de menú
		itemSalir.setText(_bundle.getString("I02_men_itm_Salir"));
		itemSalir.setAccelerator(_bundle.getString("I02_men_itm_saliracc").charAt(0));
		// Ayuda
		MenuItem helpMenuHeader = new MenuItem(barra, SWT.CASCADE);
		helpMenuHeader.setText(_bundle.getString("I02_men_Ayuda"));
		Menu helpMenu = new Menu(_shell, SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);

		MenuItem helpHelpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpHelpItem.setText(_bundle.getString("I02_men_itm_Ayuda") + "\tF1");
		// display
		helpHelpItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new I12(_display, _locale, _bundle);
			}
		});
		helpHelpItem.setAccelerator(SWT.F1);
		return barra;
	}
	
	public TabFolder crearPestañas(){
		
		//Creamos distintas pestañas para las distintas funcionalidades
		TabFolder tabFolder = new TabFolder (_shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		final Composite cCuadrantes=creaPestañaCuadrantes(tabFolder);
		final Composite cMensajes = this.creaPestañaMensajes(tabFolder);
		final Composite cEstadisticas = creaPestañaEstadisticas(tabFolder); 
		
		//Pestaña para ver los horarios (Cuadrantes)
		TabItem tabItemCuadrantes = new TabItem (tabFolder, SWT.NONE);
		tabItemCuadrantes.setText (this._bundle.getString("Cuadrantes"));
		tabItemCuadrantes.setImage(_ico_cuadrante);
		
		//Pestaña para ver o enviar mensajes
		TabItem tabItemMensajes = new TabItem (tabFolder, SWT.NONE);
		tabItemMensajes.setText (this._bundle.getString("Mensajes"));
		tabItemMensajes.setImage(_ico_mens_l);
		
		//Pestaña para ver las estadisticas de ventas
		TabItem tabItemVerEstadisticas = new TabItem (tabFolder, SWT.NONE);
		tabItemVerEstadisticas.setText (this._bundle.getString("verestadisticas"));
		tabItemVerEstadisticas.setImage(_ico_estadisticas);	

		//Enlazamos composites y pestañas
		tabItemCuadrantes.setControl(cCuadrantes);
		tabItemMensajes.setControl(cMensajes);
		tabItemVerEstadisticas.setControl(cEstadisticas);
		return tabFolder;
	}
	
	public Composite creaPestañaCuadrantes(TabFolder tabFolder){
		//Creamos el contenido de la pestaña cuadrantes
		Composite cCuadrantes = new Composite (tabFolder, SWT.NONE);
		cCuadrantes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		//Le añadimos un layout
		GridLayout lCuadrantes = new GridLayout();
		lCuadrantes.numColumns = 2;
		cCuadrantes.setLayout(lCuadrantes);
		
		//Creamos el contenido interno de la pestaña cuadrantes
		//Creamos un composite para los botones
		final Composite cBotones = new Composite (cCuadrantes, SWT.BORDER);
		cBotones.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		GridLayout lCBotones = new GridLayout();
		lCBotones.numColumns = 1;
		cBotones.setLayout(lCBotones);
		
		//Creamos un composite para el calendario
		final Label lCalendario = new Label (cBotones, SWT.LEFT);
		lCalendario.setText(this._bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		//Creamos un composite para la zona donde se mostrara el calendario		
		final Composite cCuadrantesDer = new Composite (cCuadrantes, SWT.BORDER);
		cCuadrantesDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCuadrantesDer = new GridLayout();
		lCuadrantesDer.numColumns = 1;
		cCuadrantesDer.setLayout(lCuadrantesDer);
		//final Label lCuadr1=new Label (cCuadrantesDer, SWT.CENTER);
		//lCuadr1.setText("Aquí se mostrarán los cuadrantes");
		final I02_cuadrEmpl cuadrante = new I02_cuadrEmpl(cCuadrantesDer, false);	
		cuadrante.setSemanal();
		//Creamos el calendario		
		final DateTime calendario = new DateTime (cBotones, SWT.CALENDAR);
		calendario.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String [] meses = {	"enero","febrero",
						"marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				int day=calendario.getDay();
				int month=calendario.getMonth();
				int year=calendario.getYear();
				System.out.println ("Fecha cambiada a "+ String.valueOf(day) + " de " + meses[month]+ " de " + String.valueOf(year));
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		


		//Creamos los botones para ver el horario por dias o semanas		
		final Button bPorSemanas = new Button(cBotones, SWT.RADIO);
		bPorSemanas.setText(this._bundle.getString("Verporsemana"));
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));		
		bPorSemanas.setSelection(true);
		bPorSemanas.addListener(SWT.Selection, new Listener() {
			//Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorSemanas.getSelection()) {
					cuadrante.setSemanal();				
				}
				else cuadrante.setMensual(); 
				
			}
		});

		
		//Creamos un boton para la seleccion del horario por semanas
		final Button bPorMes = new Button(cBotones, SWT.RADIO);
		bPorMes.setText(this._bundle.getString("I02_but_Verpormes"));
		bPorMes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//Creamos un oyente
		bPorMes.addListener(SWT.Selection, new Listener() {
			//Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorMes.getSelection()) {
					cuadrante.setMensual();
				}
				else cuadrante.setSemanal();
				
			}
		});
		return cCuadrantes;
	}
	
	public Composite creaPestañaMensajes(TabFolder tabFolder){
		//Creamos un composite para la pestaña de mensajes
		Composite cMensajes = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCMensajes = new GridLayout();
		lCMensajes.numColumns = 4;
		lCMensajes.makeColumnsEqualWidth = true;
		cMensajes.setLayout(lCMensajes);
		
		//Creamos una tabla para los mensajes
		Table tablaMensajes = new Table (cMensajes, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaMensajes.setLinesVisible (true);
		tablaMensajes.setHeaderVisible (true);
		String[] titles = {" ", _bundle.getString("I02_mens_De"), _bundle.getString("asunto"), _bundle.getString("mensaje2"), _bundle.getString("Fecha")};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (tablaMensajes, SWT.NONE);
			column.setText (titles [i]);
		}	
		
		//Introducimos manualmente unos mensajes por defecto
		for (int i=0; i<12; i++) {
			TableItem tItem = new TableItem (tablaMensajes, SWT.NONE);
			tItem.setImage(_ico_mens);
			tItem.setText (1, "Remitente");
			tItem.setText (2, "Asunto del mensaje");
			tItem.setText (3, "Aquí va lo que quepa del principio del mensaje");
			tItem.setText (4, "25/10/2007");
			
		}
		for (int i=0; i<titles.length; i++) {
			tablaMensajes.getColumn (i).pack ();
		}	
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		//Creamos los distintos botones
		//Mensaje nuevo
		final Button bMensNuevo = new Button(cMensajes, SWT.PUSH);
		bMensNuevo.setText(_bundle.getString("Nuevo"));
		bMensNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		//Creamos un oyente
		bMensNuevo.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				@SuppressWarnings("unused")
				I02MensajeNuevo ventana = new I02MensajeNuevo(_shell,_bundle,_locale);
			}
		});
		
		//Responder
		final Button bMensResponder = new Button(cMensajes, SWT.PUSH);
		bMensResponder.setText(_bundle.getString("I02_but_Responder"));
		bMensResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		//Eliminar mensaje
		final Button bMensEliminar = new Button(cMensajes, SWT.PUSH);
		bMensEliminar.setText(_bundle.getString("I02_but_Eliminar"));
		bMensEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		//Marcar mensaje
		final Button bMensMarcar = new Button(cMensajes, SWT.PUSH);
		bMensMarcar.setText(_bundle.getString("I02_but_Marcar"));
		bMensMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		//Enviar mensaje
		final Composite cEnviarMensaje = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCEnviarMensaje = new GridLayout();
		lCEnviarMensaje.numColumns = 2;
		cEnviarMensaje.setLayout(lCEnviarMensaje);
		return cMensajes;
	}
	
	public Composite creaPestañaEstadisticas(TabFolder tabFolder){
		//Creamos el contenido de la pestaña estadisticas
		final Composite cEstadisticas = new Composite (tabFolder, SWT.NONE);
		cEstadisticas.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		//Le añadimos un layout
		GridLayout lEstadisticas = new GridLayout();
		lEstadisticas.numColumns = 2;
		cEstadisticas.setLayout(lEstadisticas);
		
		//Creamos el contenido interno de la pestaña cuadrantes
		//Creamos un composite para los botones
		final Composite cEstIzq = new Composite (cEstadisticas, SWT.BORDER);
		cEstIzq.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		GridLayout lEstIzq = new GridLayout();
		lEstIzq.numColumns = 1;
		lEstIzq.makeColumnsEqualWidth = true;
		cEstIzq.setLayout(lEstIzq);
		
		
		final Composite cEstDer = new Composite (cEstadisticas, SWT.BORDER);
		cEstDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lEstDer = new GridLayout();
		lEstDer.numColumns = 1;
		lEstDer.makeColumnsEqualWidth = true;
		cEstDer.setLayout(lEstDer);

		final Label lTitulo	= new Label(cEstIzq, SWT.CENTER);
		lTitulo.setText(this._bundle.getString("opcionvis"));
		lTitulo.setFont(new org.eclipse.swt.graphics.Font(
			        org.eclipse.swt.widgets.Display.getDefault(), "Arial", 10,
			        org.eclipse.swt.SWT.BOLD));
		lTitulo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		
		
		final Label lTiempo	= new Label(cEstIzq, SWT.LEFT);
		lTiempo.setText(this._bundle.getString("tiempodatos"));
		lTiempo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		Combo cTiempo = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
		cTiempo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		cTiempo.setItems(new String[] {_bundle.getString("semana"), _bundle.getString("quincena"), _bundle.getString("mes"),_bundle.getString("año")});
		cTiempo.select(0);
		
		final Label lComparar	= new Label(cEstIzq, SWT.LEFT);
		lComparar.setText(_bundle.getString("compararcon"));
		lComparar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		Combo cComparar = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
		cComparar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		cComparar.setItems(new String[] {
				_bundle.getString("nadie"),
				_bundle.getString("empleadomedio"),
				_bundle.getString("mejorsemana"),
				_bundle.getString("mejormes"),
				_bundle.getString("mejoraño")
				});
		
		cComparar.select(0);
		cComparar.setVisibleItemCount(6);
		
		final Label lTipoGrafico	= new Label(cEstIzq, SWT.CENTER);
		lTipoGrafico.setText(_bundle.getString("datosvis"));
		lTipoGrafico.setFont(new org.eclipse.swt.graphics.Font(
			        org.eclipse.swt.widgets.Display.getDefault(), "Arial", 9,
			        org.eclipse.swt.SWT.BOLD));
		lTipoGrafico.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		
		final Button bVentasTotales = new Button(cEstIzq, SWT.RADIO);
		bVentasTotales.setText(_bundle.getString("verventastot"));
		bVentasTotales.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//Creamos un oyente
		bVentasTotales.addFocusListener(new FocusListener(){
			//Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas totales in");
				
			}
			
			//No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas totales out");			
			}
		}
		);
		
		
		final Button bVentasPTiempo = new Button(cEstIzq, SWT.RADIO);
		bVentasPTiempo.setText(this._bundle.getString("ventaspertime"));
		bVentasPTiempo.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//Creamos un oyente
		bVentasPTiempo.addFocusListener(new FocusListener(){
			//Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por tiempo de trabajo in");
				
			}
			
			//No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por tiempo de trabajo out");			
			}
		}
		);
		
		final Button bVentasPPrecio = new Button(cEstIzq, SWT.RADIO);
		bVentasPPrecio.setText(this._bundle.getString("ventasporprecio"));
		bVentasPPrecio.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//Creamos un oyente
		bVentasPPrecio.addFocusListener(new FocusListener(){
			//Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por precio producto in");
				
			}
			
			//No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por precio producto out");			
			}
		}
		);

		
		final Button bVentasPDepartamento = new Button(cEstIzq, SWT.RADIO);
		bVentasPDepartamento.setText(this._bundle.getString("ventaspordpto"));
		bVentasPDepartamento.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//Creamos un oyente
		bVentasPDepartamento.addFocusListener(new FocusListener(){
			//Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por departamento in");
				
			}
			
			//No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Ventas por departamento out");			
			}
		}
		);

		
		final Label lPrueba2 = new Label (cEstDer, SWT.SIMPLE);
		lPrueba2.setText("Aqui se visualizarian las graficas");
		lPrueba2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

		return cEstadisticas;
		
	}
	
	
	
	public static void main(String[] IS0708) {
		@SuppressWarnings("unused")
				LanguageChanger l = new LanguageChanger();
		// 0 español
		// 1 polaco
		// 2 inglés
		l.cambiarLocale(2);
		// Prueba del archivo de idioma
		Display display = new Display ();
		Shell shell = new Shell(display);
		I02Empleado i = new I02Empleado(shell, display, l.getBundle(), l.getCurrentLocale());
		
	}
}