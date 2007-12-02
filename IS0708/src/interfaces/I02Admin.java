package interfaces;

import idiomas.LanguageChanger;

import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class I02Admin {

	Shell _shell;
	Display _display;
	ResourceBundle _bundle;
	Locale _locale;
	Image _icoGr, _icoPq, _ico_imprimir, _ico_mens_l, _ico_mens, _ico_cuadrante,
			_ico_chico, _ico_chica, _ico_chicos,_ico_estadisticas,_ico_turnomatic;
	
	Image _fondo_turnomatic;
	
	Text _contra,tNombreUsuario,tApellidos,tNombre;
	
	Point tamaño;
	
	/**
	 * Constructor de clase
	 * (crea la ventana)
	 */
	public I02Admin(Shell shell, Display display, ResourceBundle bundle, Locale locale) {
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
		_ico_chicos = new Image(display, I02.class.getResourceAsStream("ico_chicos.gif"));
		_ico_turnomatic = new Image(display, I02.class.getResourceAsStream("ico_turnomatic.png"));
		_fondo_turnomatic = new Image(display, I02.class.getResourceAsStream("Turnomatic.jpg"));
		
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

	
	
	
	/**
	 * 
	 * @return
	 */
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
		MenuItem itemImprimir = new MenuItem (submenu, SWT.PUSH);
		itemImprimir.setImage(_ico_imprimir);
		itemImprimir.setText(_bundle.getString("I02_men_itm_Imprimir"));

		MenuItem itemSalir = new MenuItem (submenu, SWT.PUSH);
		itemSalir.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				_shell.close();
			}
		});
		// Texto del item de menú
		itemSalir.setText(_bundle.getString("I02_men_itm_Salir"));
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
		
		final Composite cInicio = this.creaPestañaInicio(tabFolder);
		final Composite cNuevoG = this.creaPestañaNuevoGerente(tabFolder);
		final Composite cEliminaG = creaPestañaEliminaGerente(tabFolder); 
		final Composite cOpciones = creaPestañaOpciones(tabFolder); 

		//Pestaña para ver o enviar mensajes
		TabItem tabItemInicio = new TabItem (tabFolder, SWT.NONE);
		tabItemInicio.setText (this._bundle.getString("I02AdminInicio"));
		tabItemInicio.setImage(_ico_turnomatic);
		
		//Pestaña para ver o enviar mensajes
		TabItem tabItemNuevoG = new TabItem (tabFolder, SWT.NONE);
		tabItemNuevoG.setText (this._bundle.getString("I02AdminNuevoGerente"));
		tabItemNuevoG.setImage(_ico_chicos);
		
		//Pestaña para ver las estadisticas de ventas
		TabItem tabItemEliminaG = new TabItem (tabFolder, SWT.NONE);
		tabItemEliminaG.setText (this._bundle.getString("I02AdminEliminaGerente"));
		tabItemEliminaG.setImage(_ico_chicos);
		
		//Pestaña para ver las estadisticas de ventas
		TabItem tabItemOpciones = new TabItem (tabFolder, SWT.NONE);
		tabItemOpciones.setText (this._bundle.getString("I02AdminCambiaOpciones"));
		tabItemOpciones.setImage(_ico_chicos);	

		//Enlazamos composites y pestañas
		tabItemInicio.setControl(cInicio);
		tabItemNuevoG.setControl(cNuevoG);
		tabItemEliminaG.setControl(cEliminaG);
		tabItemEliminaG.setControl(cOpciones);
		return tabFolder;
	}

	public Composite creaPestañaInicio(TabFolder tabFolder){
		//Creamos el contenido de la pestaña cuadrantes
		
		Composite cInicio = new Composite (tabFolder, SWT.NONE);
		cInicio.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//Le añadimos un layout
		GridLayout lInicio = new GridLayout();
		lInicio.numColumns = 2;
		cInicio.setLayout(lInicio);
		
		


		final Label bienvenido = new Label(cInicio,SWT.None);
		bienvenido.setLayoutData(new GridData(SWT.CENTER,SWT.TOP,true,true,1,1));
		bienvenido.setText("BIENVENIDO A TURNOMATIC");
		
		
		ImageData imagedata = _fondo_turnomatic.getImageData().scaledTo(300, 240);
		_fondo_turnomatic = new Image(_display, imagedata);

		cInicio.setBackgroundImage(this._fondo_turnomatic);
		return cInicio;
	}

	/*TRABAJO ACTUAL*/
	public Composite creaPestañaNuevoGerente(TabFolder tabFolder){
		//Creamos el contenido de la pestaña cuadrantes
		final Composite cNuevoGerente = new Composite (tabFolder, SWT.BORDER);
		cNuevoGerente.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 1;
		cNuevoGerente.setLayout(lGrupo);
		

		
		final Label lTitulo	= new Label(cNuevoGerente, SWT.LEFT);
		lTitulo.setText("Introduzca los datos del gerente");
		final Composite cNuevoGerente2 = new Composite (cNuevoGerente, SWT.BORDER);
		cNuevoGerente2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		GridLayout lGrupo2 = new GridLayout();
		lGrupo2.numColumns = 2;
		cNuevoGerente2.setLayout(lGrupo2);
		final Label lNombre	= new Label(cNuevoGerente2, SWT.LEFT);
		lNombre.setText(_bundle.getString("I02AdminNombre"));
		tNombre	= new Text (cNuevoGerente2, SWT.BORDER);
		tNombre.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0));

		final Label lApellidos	= new Label(cNuevoGerente2, SWT.LEFT);
		lApellidos.setText(_bundle.getString("I02AdminApellidos"));
		tApellidos	= new Text (cNuevoGerente2, SWT.BORDER);
		tApellidos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0));
		
		final Label lNombreUsuario	= new Label(cNuevoGerente2, SWT.LEFT);
		lNombreUsuario.setText(_bundle.getString("I02AdminNombreUsuario"));
		tNombreUsuario	= new Text (cNuevoGerente2, SWT.BORDER);
		tNombreUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0));

		final Label lContra	= new Label(cNuevoGerente2, SWT.LEFT);
		lContra.setText(_bundle.getString("I02AdminClave"));
		_contra	= new Text (cNuevoGerente2, SWT.BORDER);
		_contra.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0));
		_contra.setText(this.obtenerClave());
		_contra.setEditable(false);
		
		
		final Composite cAceptarCancelar = new Composite (cNuevoGerente, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.DOWN, false, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botones aceptar y cancelar
		final Button bAceptar		= new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptarCancelar, SWT.PUSH);
		
		//Introducimos los textos a los botones
//		bOClave.setText("Obtener clave");
		bAceptar.setText("Aceptar");
		bCancelar.setText(_bundle.getString("cancelar1"));
				//Introducimos los valores y eventos de Aceptar
		

		
		bAceptar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				MessageBox messageBox = new MessageBox (_shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
				messageBox.setText ("CONFIRMACION");
				messageBox.setMessage ("¿Desea guardar el nuevo gerente?");
				if( messageBox.open () == SWT.YES)
				{
					tNombreUsuario.setText("");
					tNombre.setText("");
					tApellidos.setText("");
					_contra.setText(obtenerClave());
				}
			}				
		});
		//Introducimos los valores y eventos de Cancelar
		bCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		bCancelar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				_shell.dispose();
			}				
		});
		
		// Botón por defecto bAceptar
		_shell.setDefaultButton(bAceptar);
		
		
		
		return cNuevoGerente;
	}
	
	public Composite creaPestañaEliminaGerente(TabFolder tabFolder){
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
	
	
	public Composite creaPestañaOpciones(TabFolder tabFolder){
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
	
	private String obtenerClave() {
		// TODO Auto-generated method stub
		Random randomizador=new Random();
		String clave = "";
		char[] chars = new char[]{
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
				'p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
				'P','Q','R','S','T','U','V','W','X','Y','Z',
				'1','2','3','4','5','6','7','8','9','0'
			};
		
		char[] clavechar= new char[8];
		for (int cont=0;cont<8;cont++){

			int r = randomizador.nextInt(chars.length);
			clavechar[cont]=chars[r];
		}
		System.out.println(clavechar);
		clave=String.copyValueOf(clavechar);
		return clave;
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
		I02Admin i = new I02Admin(shell, display, l.getBundle(), l.getCurrentLocale());
		
	}
	
}
