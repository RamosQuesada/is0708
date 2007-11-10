/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal Empleado
 *   por David Rodilla
 *   
 * Interfaz principal de la aplicación para un empleado
 * ver 0.1
 *******************************************************************************/


package interfaces;

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


	/**
	 * Constructor de clase
	 * (crea la ventana)
	 */
	public I02Empleado() {
		//creamos un display y un shell donde emplazar los widgets (elementos swt)
		final Display display = new Display ();
		final Shell shell = new Shell (display);

		//creamos los iconos que vamos a utilizar en el programa
		final Image icoGr = new Image(display, I02.class.getResourceAsStream("icoGr.gif"));
		final Image icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));
		final Image ico_imprimir = new Image(display, I02.class.getResourceAsStream("ico_imprimir.gif"));
		final Image ico_mens_l = new Image(display, I02.class.getResourceAsStream("ico_mens1_v.gif"));
		final Image ico_mens = new Image(display, I02.class.getResourceAsStream("ico_mens2_v.gif"));
		final Image ico_cuadrante = new Image(display, I02.class.getResourceAsStream("ico_cuadrante.gif"));
		final Image ico_chico = new Image(display, I02.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(display, I02.class.getResourceAsStream("ico_chica.gif"));
		final Image ico_estadisticas = new Image(display, I02.class.getResourceAsStream("grafica_1.ico"));
		
		
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
				
		//Establecemos el layout externo
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);

		//Creamos distintas pestañas para las distintas funcionalidades
		final TabFolder tabFolder = new TabFolder (shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//Pestaña para ver los horarios (Cuadrantes)
		TabItem tabItemCuadrantes = new TabItem (tabFolder, SWT.NONE);
		tabItemCuadrantes.setText ("Cuadrantes");
		tabItemCuadrantes.setImage(ico_cuadrante);
		
		//Pestaña para ver o enviar mensajes
		TabItem tabItemMensajes = new TabItem (tabFolder, SWT.NONE);
		tabItemMensajes.setText ("Mensajes");
		tabItemMensajes.setImage(ico_mens_l);
		
		//Pestaña para ver las estadisticas de ventas
		TabItem tabItemVerEstadisticas = new TabItem (tabFolder, SWT.NONE);
		tabItemVerEstadisticas.setText ("Ver estadísticas");
		tabItemVerEstadisticas.setImage(ico_estadisticas);
		

		//Creamos el contenido de la pestaña cuadrantes
		final Composite cCuadrantes = new Composite (tabFolder, SWT.NONE);
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
		lCalendario.setText("Calendario");
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		//Creamos un composite para la zona donde se mostrara el calendario		
		final Composite cCuadrantesDer = new Composite (cCuadrantes, SWT.BORDER);
		cCuadrantesDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCuadrantesDer = new GridLayout();
		lCuadrantesDer.numColumns = 1;
		cCuadrantesDer.setLayout(lCuadrantesDer);
		final Label lCuadr1=new Label (cCuadrantesDer, SWT.CENTER);
		lCuadr1.setText("Aquí se mostrarán los cuadrantes");
		
		//Creamos el calendario		
		final DateTime calendario = new DateTime (cBotones, SWT.CALENDAR);
		calendario.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				int day=calendario.getDay();
				int month=calendario.getMonth();
				int year=calendario.getYear();
				System.out.println ("Fecha cambiada a "+ String.valueOf(day) + " de " + meses[month]+ " de " + String.valueOf(year));
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		


		//Creamos los botones para ver el horario por dias o semanas		
		final Button bPorDias = new Button(cBotones, SWT.RADIO);
		bPorDias.setText("Horario dia");
		bPorDias.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//Oyente para saber cuando se ha modificado la seleccion del boton
		bPorDias.addFocusListener(new FocusListener(){

			//Seleccionado por dias
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				lCuadr1.setText("SE MOSTRARIAN POR DIAS");

				
			}
			
			//No seleccionado por dias
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
			}
		}
		);
		
		//Creamos un boton para la seleccion del horario por semanas
		final Button bPorSemanas = new Button(cBotones, SWT.RADIO);
		bPorSemanas.setText("Horario semana");
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//Creamos un oyente
		bPorSemanas.addFocusListener(new FocusListener(){
			//Seleccionado por semanas
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("por semanas in");
				lCuadr1.setText("SE MOSTRARIAN POR SEMANAS");
			}
			
			//No seleccionado por semanas
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("por semanas out");			
			}
		}
		);
		


		//Creamos un composite para la pestaña de mensajes
		final Composite cMensajes = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCMensajes = new GridLayout();
		lCMensajes.numColumns = 4;
		lCMensajes.makeColumnsEqualWidth = true;
		cMensajes.setLayout(lCMensajes);
		
		//Creamos una tabla para los mensajes
		Table tablaMensajes = new Table (cMensajes, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaMensajes.setLinesVisible (true);
		tablaMensajes.setHeaderVisible (true);
		String[] titles = {" ", "De", "Asunto", "Mensaje", "Fecha"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (tablaMensajes, SWT.NONE);
			column.setText (titles [i]);
		}	
		
		//Introducimos manualmente unos mensajes por defecto
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
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		//Creamos los distintos botones
		//Mensaje nuevo
		final Button bMensNuevo = new Button(cMensajes, SWT.PUSH);
		bMensNuevo.setText("Nuevo");
		bMensNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		//Creamos un oyente
		bMensNuevo.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				I02MensajeNuevo ventana = new I02MensajeNuevo(shell);
			}
		});
		
		//Responder
		final Button bMensResponder = new Button(cMensajes, SWT.PUSH);
		bMensResponder.setText("Responder");
		bMensResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		//Eliminar mensaje
		final Button bMensEliminar = new Button(cMensajes, SWT.PUSH);
		bMensEliminar.setText("Eliminar");
		bMensEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		//Marcar mensaje
		final Button bMensMarcar = new Button(cMensajes, SWT.PUSH);
		bMensMarcar.setText("Marcar");
		bMensMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		//Enviar mensaje
		final Composite cEnviarMensaje = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCEnviarMensaje = new GridLayout();
		lCEnviarMensaje.numColumns = 2;
		cEnviarMensaje.setLayout(lCEnviarMensaje);
		
		//Enlazamos composites y pestañas
		tabItemCuadrantes.setControl(cCuadrantes);
		tabItemMensajes.setControl(cMensajes);
		
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
	
	public static void main(String[] IS0708) {
		I02Empleado empleado = new I02Empleado();
	}
}