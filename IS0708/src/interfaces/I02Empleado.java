/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal Empleado
 *   por David Rodilla
 *   
 * Interfaz principal de la aplicación para un empleado
 * ver 0.1
 *******************************************************************************/


package interfaces;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.*;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class I02Empleado {

	public static void main(String[] IS0708) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);

		final Image icoGr = new Image(display, I02.class.getResourceAsStream("icoGr.gif"));
		final Image icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));
		final Image ico_imprimir = new Image(display, I02.class.getResourceAsStream("ico_imprimir.gif"));
		final Image ico_mens_l = new Image(display, I02.class.getResourceAsStream("ico_mens1_v.gif"));
		final Image ico_mens = new Image(display, I02.class.getResourceAsStream("ico_mens2_v.gif"));
		final Image ico_cuadrante = new Image(display, I02.class.getResourceAsStream("ico_cuadrante.gif"));
		final Image ico_chico = new Image(display, I02.class.getResourceAsStream("ico_chico.gif"));
		final Image ico_chica = new Image(display, I02.class.getResourceAsStream("ico_chica.gif"));
		
		
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
		TabItem tabItemEnviarMensajes = new TabItem (tabFolder, SWT.NONE);
		tabItemEnviarMensajes.setText ("EnviarMensajes");
		tabItemEnviarMensajes.setImage(ico_mens);
		TabItem tabItemVerEstadisticas = new TabItem (tabFolder, SWT.NONE);
		tabItemVerEstadisticas.setText ("Ver estadísticas");
		tabItemVerEstadisticas.setImage(ico_mens);
		
		
		final Composite cCuadrantes = new Composite (tabFolder, SWT.NONE);
		cCuadrantes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCuadrantes = new GridLayout();
		lCuadrantes.numColumns = 2;
		//lCuadrantes.makeColumnsEqualWidth = true;
		cCuadrantes.setLayout(lCuadrantes);
		
		final Composite cBotones = new Composite (cCuadrantes, SWT.BORDER);
		cBotones.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		GridLayout lCBotones = new GridLayout();
		lCBotones.numColumns = 1;
		cBotones.setLayout(lCBotones);
		
		final Label lCalendario = new Label (cBotones, SWT.LEFT);
		lCalendario.setText("Calendario");
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		
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
		
		final Composite cCuadrantesDer = new Composite (cCuadrantes, SWT.BORDER);
		cCuadrantesDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCuadrantesDer = new GridLayout();
		lCuadrantesDer.numColumns = 1;
		cCuadrantesDer.setLayout(lCuadrantesDer);
		final Label lCuadr1=new Label (cCuadrantesDer, SWT.CENTER);
		lCuadr1.setText("Aquí se mostrarán los cuadrantes");

		
		final Button bPorDias = new Button(cBotones, SWT.RADIO);
		bPorDias.setText("Horario dia");
		bPorDias.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		bPorDias.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				lCuadr1.setText("SE MOSTRARIAN POR DIAS");

				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
			}
		}
		);
		
		final Button bPorSemanas = new Button(cBotones, SWT.RADIO);
		bPorSemanas.setText("Horario semana");
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		bPorSemanas.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("por semanas in");
				lCuadr1.setText("SE MOSTRARIAN POR SEMANAS");
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("por semanas out");			
			}
		}
		);
		

		


		
		
		
		
		
		
		
		
		
		
		//MENSAJES
						
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

		final Composite cEnviarMensaje = new Composite (tabFolder, SWT.NONE);
		cMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCEnviarMensaje = new GridLayout();
		lCEnviarMensaje.numColumns = 2;
		cEnviarMensaje.setLayout(lCEnviarMensaje);
		
		final Composite cEmplIzq = new Composite (cEnviarMensaje, SWT.NONE);
		cEmplIzq.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		GridLayout lCEmplIzq = new GridLayout();
		lCEmplIzq.numColumns = 1;
		
		final Composite cEmplDer = new Composite (cEnviarMensaje, SWT.NONE);
		cEmplDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCEmplDer = new GridLayout();
		lCEmplDer.numColumns = 1;
		lCEmplDer.makeColumnsEqualWidth = true;
		cEmplDer.setLayout(lCEmplDer);
		//lCEmplIzq.makeColumnsEqualWidth = true;
		cEmplIzq.setLayout(lCEmplIzq);
		
		
		
		
		
		//ahoraaaaaaaaaaaaaaaaaa
		final Composite cMensaje = new Composite( cEmplDer, SWT.BORDER);
		cMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL,false , false, 1, 1));
		GridLayout lCMensaje = new GridLayout();
		lCMensaje.numColumns = 1;
		cMensaje.setLayout(lCMensaje);
		
		//ESTO TIENE QUE IR SI ESTA SELECIONADO EL BOTON CORRESPONDIENTE
		final Composite cMensaje1 = new Composite( cMensaje, SWT.BORDER);
		cMensaje1.setLayoutData(new GridData(SWT.FILL, SWT.FILL,false , false, 1, 1));
		GridLayout lCMensaje1 = new GridLayout();
		lCMensaje1.numColumns = 1;
		cMensaje.setLayout(lCMensaje1); 
		final Label lFInicioB=new Label (cMensaje1, SWT.NONE);
		lFInicioB.setText("FECHA INICIO BAJA");
		cMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL,false , false, 1, 1));
		final Composite fecha1= new Composite(cMensaje,SWT.NONE);
		GridLayout lCPBFecha1 = new GridLayout();
		lCPBFecha1.numColumns = 5;
		fecha1.setLayout(lCPBFecha1);
		final Text t1Fecha1 = new Text(fecha1, SWT.BORDER);
		t1Fecha1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		t1Fecha1.setText("DIA ");
		t1Fecha1.addFocusListener(
				new FocusListener(){

					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub
						t1Fecha1.setText("");
					}

					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						if(t1Fecha1.getText()==""){
							t1Fecha1.setText("DIA");					
						}
					}}
				);
		final Label lFFinB2=new Label (fecha1, SWT.CENTER);
		lFFinB2.setText(" de ");
		final Text t2Fecha1 = new Text(fecha1, SWT.BORDER);
		t2Fecha1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		t2Fecha1.setText("ESCRIBA EL MES");
		t2Fecha1.addFocusListener(
				new FocusListener(){

					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub
						t2Fecha1.setText("");
					}

					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						if(t2Fecha1.getText()==""){
							t2Fecha1.setText("ESCRIBA EL MES");					
						}
					}}
				);
		final Label lFFinB4=new Label (fecha1, SWT.CENTER);
		lFFinB4.setText(" de ");
		final Text t3Fecha1 = new Text(fecha1, SWT.BORDER);
		t3Fecha1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		t3Fecha1.setText("AÑO ");
		t3Fecha1.addFocusListener(
				new FocusListener(){

					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub
						t3Fecha1.setText("");
					}

					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						if(t3Fecha1.getText()==""){
							t3Fecha1.setText("AÑO ");					
						}
					}}
				);
		final Label lFFinB=new Label (cMensaje, SWT.CENTER);
		lFFinB.setText("FECHA FINAL BAJA");
		
		
		
		
		final Composite fecha2 = new Composite(cMensaje,SWT.NONE);
		GridLayout lCPBFecha2 = new GridLayout();
		lCPBFecha2.numColumns = 5;
		fecha2.setLayout(lCPBFecha2);
		final Text t1Fecha2 = new Text(fecha2, SWT.BORDER);
		t1Fecha2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		t1Fecha2.setText("DIA ");
		t1Fecha2.addFocusListener(
				new FocusListener(){

					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub
						t1Fecha2.setText("");
					}

					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						if(t1Fecha2.getText()==""){
							t1Fecha2.setText("DIA");					
						}
					}}
				);
		final Label lFFinB22=new Label (fecha2, SWT.CENTER);
		lFFinB22.setText(" de ");
		final Text t2Fecha2 = new Text(fecha2, SWT.BORDER);
		t2Fecha2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		t2Fecha2.setText("ESCRIBA EL MES");
		t2Fecha2.addFocusListener(
				new FocusListener(){

					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub
						t2Fecha2.setText("");
					}

					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						if(t2Fecha2.getText()==""){
							t2Fecha2.setText("ESCRIBA EL MES");					
						}
					}}
				);
		final Label lFFinB42=new Label (fecha2, SWT.CENTER);
		lFFinB42.setText(" de ");
		final Text t3Fecha2 = new Text(fecha2, SWT.BORDER);
		t3Fecha2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		t3Fecha2.setText("AÑO ");
		t3Fecha2.addFocusListener(
				new FocusListener(){

					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub
						t3Fecha2.setText("");
					}

					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						if(t3Fecha2.getText()==""){
							t3Fecha2.setText("AÑO ");					
						}
					}}
				);
		
		Label lTipoMensaje = new Label(cEmplIzq, SWT.NONE);
		lTipoMensaje.setText("Tipo de mensaje");
		lTipoMensaje.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		final Button bMenPetBaja = new Button(cEmplIzq, SWT.RADIO);
		bMenPetBaja.setText("Peticion baja");
		bMenPetBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bMenPetBaja.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion baja in");
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion baja out");
				
			}
		}
		);

		final Button bMenPetCamHor = new Button(cEmplIzq, SWT.RADIO);
		bMenPetCamHor.setText("Peticion horario");
		bMenPetCamHor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bMenPetCamHor.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion cambio horario in");
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion cambio horario out");
				
			}
		}
		);
		
		final Button bOtroMensaje = new Button(cEmplIzq, SWT.RADIO);
		bOtroMensaje.setText("Otro mensaje");
		bOtroMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bOtroMensaje.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("otro mensaje in");
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("otro mensaje out");
				
			}
		}
		);
		
		
		
		
		
		
//		
//		
//		Label lEmplNombre = new Label(cEmplIzq, SWT.NONE);
//		lEmplNombre.setText("Nombre");
//		lEmplNombre.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
//		
//		Text tEmplNombre = new Text(cEmplIzq, SWT.BORDER);
//		tEmplNombre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//		
//		Label lEmplNVend = new Label(cEmplIzq, SWT.NONE);
//		lEmplNVend.setText("N. vend");
//		lEmplNVend.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
//		
//		Text tEmplNVend = new Text(cEmplIzq, SWT.BORDER);
//		tEmplNVend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//
//		Label lEmplDpto = new Label(cEmplIzq, SWT.NONE);
//		lEmplDpto.setText("Dpto.");
//		lEmplDpto.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
//		
//		Text tEmplDpto = new Text(cEmplIzq, SWT.BORDER);
//		tEmplDpto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//
//		Label lEmplContr = new Label(cEmplIzq, SWT.NONE);
//		lEmplContr.setText("Contrato");
//		lEmplContr.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
//		Combo cElegirDestino = new Combo (cEmplIzq, SWT.BORDER);
//		cElegirDestino.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		
		
		

		
//		final Button bEmplNuevo = new Button(cEmplDer, SWT.RADIO);
//		bEmplNuevo.setText("Nuevo");
//		bEmplNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//
//		final Button bEmplVer = new Button(cEmplDer, SWT.RADIO);
//		bEmplVer.setText("Ver");
//		bEmplVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//		
//		final Button bEmplEditar = new Button(cEmplDer, SWT.RADIO);
//		bEmplEditar.setText("Editar");
//		bEmplEditar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//
//		final Button bEmplBaja = new Button(cEmplDer, SWT.PUSH);
//		bEmplBaja.setText("Dar de baja");
//		bEmplBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
				
		tabItemCuadrantes.setControl(cCuadrantes);
		tabItemMensajes.setControl(cMensajes);
		tabItemEnviarMensajes.setControl(cEnviarMensaje);
		
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