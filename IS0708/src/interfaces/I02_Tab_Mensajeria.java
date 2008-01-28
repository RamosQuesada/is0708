package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TabFolder;
import java.util.ResourceBundle;

import aplicacion.Mensaje;
import aplicacion.Util;
import aplicacion.Vista;

public class I02_Tab_Mensajeria extends Thread{
	private final Vista vista;
	private final ResourceBundle bundle;
	private TabFolder tabFolder;
	private ArrayList<Mensaje> mensajesEntrantes;
	private ArrayList<String> remitentes; 
	private Table tablaMensajes;
	private Label lMensajes;
	private final Image ico_mens, ico_mens_l;
	// Los caracteres a previsualizar de un mensaje
	final int prevTextoMens = 50; 
	// Los caracteres a previsualizar de un asunto de mensaje
	final int prevAsuntoMens = 20;
	// El número de mensajes a mostrar por hoja
	final int num_men_hoja = 10;
	// El primer mensaje a mostrar (aumenta al pinchar en "ver más")
	private int primerMensaje = 0;
	// Este argumento sirve para que el hilo se ejecute indefinidamente o solo una vez
	// (para las llamadas puntuales de actualizar, siguientes y anteriores

	private Button bMensAnteriores,bMensSiguientes,bActualizar;
	
	public I02_Tab_Mensajeria (TabFolder tabFolder, Vista vista, ResourceBundle bundle) {
		this.vista = vista;
		this.bundle = bundle;
		this.tabFolder = tabFolder;
		ico_mens_l = new Image(tabFolder.getDisplay(), 
				I02_Tab_Mensajeria.class.getResourceAsStream("ico_mens1_v.gif"));
		ico_mens = new Image(tabFolder.getDisplay(),
				I02_Principal.class.getResourceAsStream("ico_mens2_v.gif"));
		crearTab();
	}
	
	/**
	 * Implementa un hilo que coge los mensajes del servidor.
	 */
	public synchronized void run() {
		setName("I02 - Load messages");
		boolean run = true;
		while (run) {
			if (tablaMensajes.isDisposed() || vista.getEmpleadoActual().getEmplId()==0) run = false;
			else {
				if (!tabFolder.isDisposed()) {
					// Cargar mensajes
					cargarMensajes();
					// Actualizar tabla
					if (!tabFolder.isDisposed()) {
						tabFolder.getDisplay().asyncExec(new Runnable () {
							public void run() {
							mostrarMensajes();
						} 
						});
					}
				}
				try {
					// TODO Espera 10 segundos (¿cómo lo dejamos?)
					wait(10000);
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * Muestra los mensajes cargados en el interfaz.
	 */
	private void mostrarMensajes() {
		tablaMensajes.removeAll();
		int i = 0;
		// Crear una fuente cursiva y otra negrita
		FontData[] fd = tablaMensajes.getFont().getFontData();
		fd[0].setStyle(SWT.ITALIC);
		Font fCursiva = new Font(tablaMensajes.getDisplay(),fd);
		fd[0].setStyle(SWT.BOLD);
		Font fNegrita = new Font(tablaMensajes.getDisplay(),fd);
		
		while (i < mensajesEntrantes.size() && i < num_men_hoja) {
			TableItem tItem = new TableItem(tablaMensajes, SWT.NONE);
			System.out.println("mensaje marcado: " + mensajesEntrantes.get(i).isMarcado());
			if (mensajesEntrantes.get(i).isMarcado())
				tItem.setFont(fNegrita);
//TODO mostrar mensajes leídos o no leídos
//			if (mensajesEntrantes.get(i).isLeído()) {
//				tItem.setFont(fCursiva);
//				tItem.setImage(ico_mens_l);
//			}	
//			else 
//				tItem.setImage(ico_mens);
			tItem.setText(1, remitentes.get(i));
			
			tItem.setText(2, Util.recortarTexto(mensajesEntrantes.get(i).getAsunto(), prevAsuntoMens));
			tItem.setText(3, Util.recortarTexto(mensajesEntrantes.get(i).getTexto(), prevTextoMens));
			tItem.setText(4, Util.dateAString(mensajesEntrantes.get(i).getFecha()));
			i++;
		}
		lMensajes.setText(bundle.getString("I02_lab_MostrandoMensajes1") + " " + String.valueOf(primerMensaje+1) + " " + 
				bundle.getString("I02_lab_MostrandoMensajes2") + " " + String.valueOf(primerMensaje+num_men_hoja));
		tablaMensajes.setEnabled(true);
		tablaMensajes.setCursor(new Cursor(tablaMensajes.getDisplay(), SWT.CURSOR_ARROW));
		bMensSiguientes.setEnabled(true);
		bMensAnteriores.setEnabled(true);
		bActualizar.setEnabled(true);
	}
	
	/**
	 * Carga los mensajes de la base de datos
	 */
	private void cargarMensajes() {
		// Carga mensajes
		vista.infoDebug("I02_Tab_Mensajeria", "Cargando mensajes desde hilo I02 - Load messages");
		mensajesEntrantes = vista.getMensajesEntrantes(vista.getEmpleadoActual().getEmplId(), primerMensaje, num_men_hoja);
		// Carga remitentes
		vista.infoDebug("I02_Tab_Mensajeria", "Cargando remitentes de los mensajes desde hilo I02 - Load messages");
		remitentes = new ArrayList<String>();
		// Añadir nombre remitentes a lista remitentes
		for (int i = 0; i < mensajesEntrantes.size(); i++) {
			remitentes.add(vista.getEmpleado(mensajesEntrantes.get(i).getRemitente()).getNombreCompleto());				
		}
		vista.infoDebug("I02_Tab_Mensajeria", "Acabado");
	}
	
	public synchronized void crearTab() {
		// Crear tab
		TabItem tabItemMensajes = new TabItem(tabFolder, SWT.NONE);
		tabItemMensajes.setText(bundle.getString("Mensajes"));
		tabItemMensajes.setImage(ico_mens_l);
		
		final Composite cMensajes = new Composite(tabFolder, SWT.NONE);
		tabItemMensajes.setControl(cMensajes);
		
		cMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cMensajes.setLayout(new GridLayout(7, true));
		
		lMensajes = new Label(cMensajes,SWT.NONE);
		lMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false, 7, 1));
		lMensajes.setText(bundle.getString("I02_lab_CargandoMensajes"));
		
		tablaMensajes = new Table(cMensajes,SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaMensajes.setEnabled(false);
		tablaMensajes.setLinesVisible(true);
		tablaMensajes.setHeaderVisible(true);

		// Iniciar hilo de cargar mensajes
		start();
		tablaMensajes.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
				// Si se ha pinchado en algún email (y no fuera)
				if (tablaMensajes.getSelectionIndex()!=-1) {
					Mensaje m = mensajesEntrantes.get(tablaMensajes.getSelectionIndex());
					new I14_Escribir_mensaje(tabFolder.getShell(),bundle,vista,m,0,"");
				}
			}
			public void mouseUp(MouseEvent e) {};
			public void mouseDown(MouseEvent e) {};
		});
		
		String[] titles = { " ", bundle.getString("I02_mens_De"),
		bundle.getString("Asunto"), bundle.getString("Mensaje"),
		bundle.getString("Fecha") };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaMensajes, SWT.NONE);
			column.setText(titles[i]);
		}
		
		// table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 7, 1));
		tablaMensajes.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				// Configurar tamaño de las columnas 5 10 10 65 10
				int ancho = tablaMensajes.getSize().x ;
				tablaMensajes.getColumn(0).setWidth(20);
				tablaMensajes.getColumn(1).setWidth(ancho / 4);
				tablaMensajes.getColumn(2).setWidth(ancho / 8);
				tablaMensajes.getColumn(3).setWidth(ancho - 4*(ancho/8)-25);
				tablaMensajes.getColumn(4).setWidth(ancho / 8);
			}
			public void controlMoved(ControlEvent e) {};
		});
		
		// Añadir botones
		final Button bMensNuevo = new Button(cMensajes, SWT.PUSH);
		bMensNuevo.setText(bundle.getString("Nuevo"));
		bMensNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
		1, 1));
		bMensNuevo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new I15_Mensaje_nuevo(tabFolder.getShell(), bundle, vista);
			}
		});
		
		final Button bMensResponder = new Button(cMensajes, SWT.PUSH);
		bMensResponder.setText(bundle.getString("I02_but_Responder"));
		bMensResponder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		false, 1, 1));
		
		final Button bMensEliminar = new Button(cMensajes, SWT.PUSH);
		bMensEliminar.setText(bundle.getString("I02_but_Eliminar"));
		bMensEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		false, 1, 1));
		
		final Button bMensMarcar = new Button(cMensajes, SWT.PUSH);
		bMensMarcar.setText(bundle.getString("I02_but_Marcar"));
		bMensMarcar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
		1, 1));

		bMensMarcar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tablaMensajes.getSelectionIndex()>=0) {
					vista.marcarMensaje(mensajesEntrantes.get(tablaMensajes.getSelectionIndex()));
					desplazarVentanaMensajes(0);
				}
			}
		});
		
		bMensAnteriores = new Button(cMensajes, SWT.PUSH);
		bMensAnteriores.setText(bundle.getString("I02_but_Anteriores"));
		bMensAnteriores.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
		1, 1));

		bMensAnteriores.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				desplazarVentanaMensajes(-num_men_hoja);
			}
		});

		bMensSiguientes = new Button(cMensajes, SWT.PUSH);
		bMensSiguientes.setText(bundle.getString("I02_but_Siguientes"));
		bMensSiguientes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
		1, 1));

		bMensSiguientes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				desplazarVentanaMensajes(num_men_hoja);
			}
		});

		bActualizar = new Button(cMensajes, SWT.PUSH);
		bActualizar.setText(bundle.getString("Actualizar"));
		bActualizar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
		1, 1));

		bActualizar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				desplazarVentanaMensajes(0);
			}
		});
	}
	
	/**
	 * Desplaza la ventana (ámbito) de mensajes que estamos viendo
	 * @param desp
	 */
	private synchronized void desplazarVentanaMensajes(int desp) {
		lMensajes.setText(bundle.getString("I02_lab_CargandoMensajes"));
		tablaMensajes.removeAll();
		tablaMensajes.setEnabled(false);
		primerMensaje+=desp;
		bMensSiguientes.setEnabled(false);
		bMensAnteriores.setEnabled(false);
		bActualizar.setEnabled(false);
		if (primerMensaje<0) primerMensaje=0;
		tablaMensajes.setCursor(new Cursor(tablaMensajes.getDisplay(), SWT.CURSOR_WAIT));
		notify();
	}

}