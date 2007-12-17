package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TabFolder;
import java.util.ResourceBundle;


import aplicacion.Empleado;
import aplicacion.Mensaje;
import aplicacion.Util;
import aplicacion.Vista;

public class I02_Tab_Mensajeria extends Thread{
	private final Vista vista;
	private final ResourceBundle bundle;
	private TabFolder tabFolder;
	private ArrayList<Mensaje> mensajesEntrantes;
	private Table tablaMensajes;
	private final Image ico_mens, ico_mens_l;
	// Los caracteres a previsualizar de un mensaje
	final int prevTextoMens = 50; 
	// Los caracteres a previsualizar de un asunto de mensaje
	final int prevAsuntoMens = 20;
	// El número de mensajes a mostrar por hoja
	final int num_men_hoja = 10;

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
	
	public void run() {
		setName("I02 - Load messages");
		// Coger mensajes
		mensajesEntrantes = vista.getMensajesEntrantes(vista.getEmpleadoActual().getEmplId(), 0, num_men_hoja);
		tabFolder.getDisplay().asyncExec(new Runnable () {
			public void run() {
				// Actualizar tabla
				// TODO DE MOMENTO OBTENEMOS LOS 10 PRIMEROS MENSAJES,
				// Falta un botón para ver los más antiguos
				//System.out.print(mensajesEntrantes.get(1).getAsunto());
				Empleado remitente;
				String nombre_remitente;
				int i = 0;
				while (i < mensajesEntrantes.size() && i < num_men_hoja) {
					TableItem tItem = new TableItem(tablaMensajes, SWT.NONE);
					//tItem.setImage(ico_mens);
					remitente = vista.getEmpleado(mensajesEntrantes.get(i).getRemitente());
					nombre_remitente = (remitente.getNombreCompleto());
					tItem.setText(1, nombre_remitente);
					tItem.setText(2, Util.recortarTexto(mensajesEntrantes.get(i)
							.getAsunto(), prevAsuntoMens));
					tItem.setText(3, Util.recortarTexto(mensajesEntrantes.get(i)
							.getTexto(), prevTextoMens));
					tItem.setText(4, Util.dateAString(mensajesEntrantes.get(i)
							.getFecha()));
					i++;
				}
				for (int j = 0; j < 5; j++) {
					tablaMensajes.getColumn(j).pack();
				}
			}
		});
	}
	public synchronized void crearTab() {

		TabItem tabItemMensajes = new TabItem(tabFolder, SWT.NONE);
		tabItemMensajes.setText(bundle.getString("Mensajes"));
		tabItemMensajes.setImage(ico_mens_l);
		
		final Composite cMensajes = new Composite(tabFolder, SWT.NONE);
		tabItemMensajes.setControl(cMensajes);
		
		cMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cMensajes.setLayout(new GridLayout(4, true));
		
		tablaMensajes = new Table(cMensajes,
		SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaMensajes.setLinesVisible(true);
		tablaMensajes.setHeaderVisible(true);
		start();
		tablaMensajes.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
				Mensaje m = mensajesEntrantes.get(tablaMensajes.getSelectionIndex());
				new I14_Escribir_mensaje(tabFolder.getShell(),bundle,vista,m);	
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
		tablaMensajes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		true, 4, 1));
		
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
		
	}
}