package interfaces;

import idiomas.LanguageChanger;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
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
		
		
		ImageData imagedata; 
		imagedata= _fondo_turnomatic.getImageData().scaledTo(300, 240);
		int alto=(int)(_fondo_turnomatic.getImageData().height*0.3);
		int ancho=(int)(_fondo_turnomatic.getImageData().width*0.3);
		imagedata=_fondo_turnomatic.getImageData().scaledTo(ancho, alto);
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

		final Button bClaveAuto		= new Button(cNuevoGerente2, SWT.RADIO);
		bClaveAuto.setText("Generacion automatica de la clave");
		final Button bClaveManual			= new Button(cNuevoGerente2, SWT.RADIO);
		bClaveAuto.setSelection(true);
		bClaveAuto.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				tNombreUsuario.setText("");
				tNombre.setText("");
				tApellidos.setText("");
				_contra.setEditable(false);
				_contra.setText(obtenerClave());				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		}
		);
		
		bClaveManual.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				_contra.setEditable(true);
				tNombreUsuario.setText("");
				tNombre.setText("");
				tApellidos.setText("");
				_contra.setText("");
								
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		}
		);

		
		bClaveManual.setText("Seleccion manual de la clave");
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
		//_shell.pack();
		_shell.setDefaultButton(bAceptar);
		
		
		
		return cNuevoGerente;
	}
	
	public Composite creaPestañaEliminaGerente(TabFolder tabFolder){
		//Creamos un composite para la pestaña de mensajes
		final Composite cEliminaGerente = new Composite (tabFolder, SWT.NONE);
		cEliminaGerente.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lTJ = new GridLayout();
		lTJ.numColumns = 1;
		lTJ.makeColumnsEqualWidth = true;
		cEliminaGerente.setLayout(lTJ);
		//1º elegimos el gerente que queremos eliminar
		final Label nombreGerente=new Label(cEliminaGerente,SWT.None);
		nombreGerente.setText("Escoja el gerente a eliminar:");
		final Combo comboGerenteElim = new Combo(cEliminaGerente,SWT.HORIZONTAL | SWT.READ_ONLY);
		final String[] textoListaGerentes= new String[] {
				"GERENTE1", 
				"GERENTE2",
				"GERENTE3"
				};
		comboGerenteElim.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		comboGerenteElim.setItems(textoListaGerentes);
		comboGerenteElim.select(0);		

		
		final Label opcionJefes=new Label(cEliminaGerente,SWT.None);
		opcionJefes.setText("¿Que desea hacer con los empleados del gerente seleccionado?:");

		

		final Button bDejarSinAsignar			= new Button(cEliminaGerente, SWT.RADIO);
		bDejarSinAsignar.setText("Dejar sin asignar:");
		bDejarSinAsignar.setSelection(true);
		final Button bAsignarAUnGerente			= new Button(cEliminaGerente, SWT.RADIO);
		bAsignarAUnGerente.setText("Asignar a otro gerente:");
		
		final Combo comboGerenteSust = new Combo(cEliminaGerente,SWT.BORDER | SWT.READ_ONLY);
		comboGerenteSust.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		comboGerenteSust.setItems(textoListaGerentes);
		comboGerenteSust.select(0);
		

		final Button bAsignarAGerentes			= new Button(cEliminaGerente, SWT.RADIO);
		bAsignarAGerentes.setText("Seleccionar asignacion uno a uno:");
		//Introducimos manualmente unos mensajes por defecto
		//final ScrolledComposite cEliminaGerenteS = new ScrolledComposite (cEliminaGerente, SWT.BORDER|SWT.V_SCROLL);
		//cEliminaGerenteS.setExpandHorizontal(true);
		//cEliminaGerenteS.setExpandVertical(false);
		


		final Composite cEliminaGerente2 = new Composite (cEliminaGerente, SWT.NONE|SWT.V_SCROLL);
		//cEliminaGerenteS.setContent(cEliminaGerente2);
		cEliminaGerente2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		
		
		  final ScrollBar vBar = cEliminaGerente2.getVerticalBar();
		    vBar.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		        Point location = cEliminaGerente2.getLocation();
		      //  location.y = -vBar.getSelection();
		        cEliminaGerente2.setLocation(location);
		      }
		    });
	    
	    //cEliminaGerenteS.setMinSize(cEliminaGerente2.computeSize(SWT.DEFAULT,SWT.DEFAULT));
		GridLayout lTJ2 = new GridLayout();
		lTJ2.numColumns = 4;
		lTJ2.makeColumnsEqualWidth = true;
		cEliminaGerente2.setLayout(lTJ2);
		cEliminaGerente2.setVisible(false);		
		comboGerenteSust.setEnabled(false);

		bDejarSinAsignar.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				cEliminaGerente2.setVisible(false);		
				comboGerenteSust.setEnabled(false);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
		);

		bAsignarAUnGerente.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				cEliminaGerente2.setVisible(false);
				comboGerenteSust.setEnabled(true);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
		);
		
		
		bAsignarAGerentes.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				cEliminaGerente2.setVisible(true);
				comboGerenteSust.setEnabled(false);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		}
		);
		
		
		for (int i=0; i<12; i++) {
			/*
			TableItem tItem = new TableItem (tablaJefes, SWT.NONE);
			tItem.setText (0, "Nombre");
			tItem.setText (1, "Apellidos");
			tItem.setText (2, "Departamento");
			*/
			final Label nombreJefe=new Label(cEliminaGerente2,SWT.None);
			nombreJefe.setText("nombre Jefe"+String.valueOf(i));
			final Label apellidosJefe=new Label(cEliminaGerente2,SWT.None);
			apellidosJefe.setText("apellidos Jefe");
			final Label departamentoJefe=new Label(cEliminaGerente2,SWT.None);
			departamentoJefe.setText("departamento Jefe");
			final Combo combo = new Combo(cEliminaGerente2,SWT.BORDER | SWT.READ_ONLY);
			final String[] texto= new String[] {
					"GERENTE1", 
					"GERENTE2",
					"GERENTE3"
					};
			combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 0, 0));
			combo.setItems(texto);
			combo.select(0);
			//cEliminaGerente2.setSize(new Point(SWT.DEFAULT,100));
			
		}

		
		
		//Creamos los distintos botones
		final Button bEliminar = new Button(cEliminaGerente, SWT.PUSH);
		bEliminar.setText("Eliminar");
		bEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		//Creamos un oyente
		bEliminar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
			
			}
		});
		
				return cEliminaGerente;
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
