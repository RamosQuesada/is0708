package interfaces;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import aplicacion.Vista;
import aplicacion.datos.Empleado;

public class I02_Tab_Jefe_Empleados extends Thread{
	
	final ResourceBundle _bundle;
	final Vista _vista;
	final TabFolder _tabFolder;
	final Table tablaEmpleados;
	final Image ico_chico, ico_chica, ico_mens;
	private boolean datosInterfazCargados = false;
	
	//Variables globales para objetos de la vetana
	Text tEmplDpto;
	Text tEmplNVend;
	Text tEmplNombre;
	Combo cEmplContr;
	
	/**
	 * Implementa un hilo que coge los empleados del departamento del servidor.
	 */
	public void run() {
		boolean run = true;
		try {
			while (!_vista.isCacheCargada()) {
				sleep(5000);
			}
		} catch (Exception e) {}
		// Coge los datos de todos los contratos
		for (int i=0; i<_vista.getEmpleados().size(); i++) {
			_vista.getEmpleados().get(i).getContrato(_vista);
		}
		
		datosInterfazCargados = true;
		while (run) {
			
			if (tablaEmpleados.isDisposed() || _vista.getEmpleadoActual().getEmplId()==0) run = false;
			else {
				if (!tablaEmpleados.isDisposed()) {
					// Actualizar tabla
					if (!tablaEmpleados.isDisposed()) {
						tablaEmpleados.getDisplay().asyncExec(new Runnable () {
							public void run() {
								mostrarEmpleados();
							}
						});
					}
				}
				try {
					// TODO Espera 10 segundos (¿cómo lo dejamos?)
					sleep(10000);					
				} catch (Exception e) {}
			}
		}
	}
	
	private void mostrarEmpleados() {
		
		int x = tablaEmpleados.getSelectionIndex();
		if (_vista.isCacheCargada() && datosInterfazCargados) {
			tablaEmpleados.removeAll();
			ArrayList<Empleado> listaFiltrada = (ArrayList<Empleado>) _vista.getEmpleados().clone();
			
			if (tEmplNombre.getText() != "") {
				for (int i = 0; i < listaFiltrada.size(); i++) {
					if (!listaFiltrada.get(i).getNombreCompleto().toLowerCase().contains(tEmplNombre.getText().toLowerCase())) {
						listaFiltrada.remove(i);
						i--;
					}
				}
			}
			if (tEmplNVend.getText() != "") { 
				for (int i = 0; i < listaFiltrada.size(); i++) {
					int nvend = listaFiltrada.get(i).getEmplId(); 
					if (!Integer.toString(nvend).toLowerCase().contains(tEmplNVend.getText().toLowerCase())) {
						listaFiltrada.remove(i);
						i--;
					}
				}
			}
			if (tEmplDpto.getText() != "") { 
				for (int i = 0; i < listaFiltrada.size(); i++) {					 
					if (!listaFiltrada.get(i).getDepartamentoId().toLowerCase().contains(tEmplDpto.getText().toLowerCase())) {
						listaFiltrada.remove(i);
						i--;
					}
				}
			}
			
			if (cEmplContr.getSelectionIndex() != 0) { 
				for (int i = 0; i < listaFiltrada.size(); i++) {					 
					if (!listaFiltrada.get(i).getNombreContrato().toLowerCase().contains(cEmplContr.getText().toLowerCase())) {
						listaFiltrada.remove(i);
						i--;
					}
				}
			}
			
			for (int i = 0; i < listaFiltrada.size(); i++) {
				TableItem tItem = new TableItem(tablaEmpleados, SWT.NONE);
				
				if (listaFiltrada.get(i).getSexo()==0)
					tItem.setImage(ico_chica);
				else 
					tItem.setImage(ico_chico);
				
				tItem.setText(1, String.valueOf(listaFiltrada.get(i).getEmplId()));
				tItem.setText(2, listaFiltrada.get(i).getNombreCompleto());
				tItem.setText(3, listaFiltrada.get(i).getDepartamentoId());
				aplicacion.datos.Contrato c = listaFiltrada.get(i).getContrato(_vista);
				if (c!=null) tItem.setText(4, c.getNombreContrato());
				else         tItem.setText(4, "Error");
				tItem.setText(5, "911234567");
				tItem.setImage(6, ico_mens);				
			}
		}
		// table.setSize (table.computeSize (SWT.DEFAULT, 200));
		tablaEmpleados.setSelection(x);
	}
	
	/**
	 * Constructor. Crea un tab con un listado de empleados
	 * 
	 * @param tabFolder el tabFolder donde colocarlo
	 * @author Daniel Dionne
	 */
	public I02_Tab_Jefe_Empleados(TabFolder tabFolder, Vista vista, ResourceBundle bundle ) {
		_bundle = bundle;
		_vista = vista;
		_tabFolder = tabFolder;
		ico_chico = new Image(tabFolder.getDisplay(), I02_Tab_Jefe_Empleados.class.getResourceAsStream("ico_chico.gif"));
		ico_chica = new Image(tabFolder.getDisplay(), I02_Tab_Jefe_Empleados.class.getResourceAsStream("ico_chica.gif"));
		ico_mens = new Image(tabFolder.getDisplay(), I02_Tab_Jefe_Empleados.class.getResourceAsStream("ico_mens1_v.gif"));
		
		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText(bundle.getString("Empleados"));
		tabItemEmpleados.setImage(ico_chico);

		final Composite cEmpleados = new Composite(tabFolder, SWT.NONE);
		tabItemEmpleados.setControl(cEmpleados);

		cEmpleados.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		cEmpleados.setLayout(new GridLayout(2, false));

		final Composite cEmplIzq = new Composite(cEmpleados, SWT.NONE);
		cEmplIzq.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1,
				1));
		cEmplIzq.setLayout(new GridLayout(2, false));

		Label lEmplFiltro = new Label(cEmplIzq, SWT.NONE);
		lEmplFiltro.setText(bundle.getString("I02_lab_Filtro"));
		lEmplFiltro.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 2, 1));

		Label lEmplNombre = new Label(cEmplIzq, SWT.NONE);
		lEmplNombre.setText(bundle.getString("Nombre"));
		lEmplNombre.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));

		tEmplNombre = new Text(cEmplIzq, SWT.BORDER);
		tEmplNombre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		Label lEmplNVend = new Label(cEmplIzq, SWT.NONE);
		lEmplNVend.setText(bundle.getString("I02_lab_NVend"));
		lEmplNVend.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		tEmplNVend = new Text(cEmplIzq, SWT.BORDER);
		tEmplNVend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		Label lEmplDpto = new Label(cEmplIzq, SWT.NONE);
		lEmplDpto.setText(bundle.getString("I02_lab_Dpto"));
		lEmplDpto.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		tEmplDpto = new Text(cEmplIzq, SWT.BORDER);
		tEmplDpto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		Label lEmplContr = new Label(cEmplIzq, SWT.NONE);
		lEmplContr.setText(bundle.getString("Contrato"));
		lEmplContr.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		cEmplContr = new Combo(cEmplIzq, SWT.BORDER | SWT.READ_ONLY);
		cEmplContr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,				
				1, 1));
		
		//cEmplContr.add("prueba");	
		while (!_vista.isCacheCargada() && _vista.getEmpleadoActual().getEmplId() != 00000000) {
		//	sleep(5000);
		}
		cEmplContr.add("Todos");
		for (int i=1; i<_vista.getListaContratosDepartamento().size(); i++)
			cEmplContr.add(_vista.getListaContratosDepartamento().get(i).getNombreContrato());
		
		cEmplContr.select(0);
		final Composite cEmplDer = new Composite(cEmpleados, SWT.NONE);
		cEmplDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		cEmplDer.setLayout(new GridLayout(4, true));

		tablaEmpleados = new Table(cEmplDer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		start();
		tablaEmpleados.setLinesVisible(true);
		tablaEmpleados.setHeaderVisible(true);
		String[] titles2 = { " ", bundle.getString("I02_lab_NVend"),
				bundle.getString("Nombre"), bundle.getString("Departamento"),
				bundle.getString("Contrato"), bundle.getString("Telefono"), "" };
		for (int i = 0; i < titles2.length; i++) {
			TableColumn column = new TableColumn(tablaEmpleados, SWT.NONE);
			column.setText(titles2[i]);
		}
		
		tablaEmpleados.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 4, 1));
		tablaEmpleados.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				// Configurar tamaño de las columnas
				int ancho = tablaEmpleados.getSize().x ;
				tablaEmpleados.getColumn(0).setWidth(20);				// ico			x
				tablaEmpleados.getColumn(1).setWidth(ancho/20*3);		// nvend		15
				tablaEmpleados.getColumn(2).setWidth(ancho/5*2);		// nombre		40
				tablaEmpleados.getColumn(3).setWidth(ancho/20*3-20);	// departamento	15
				tablaEmpleados.getColumn(4).setWidth(ancho/20*3-20);	// contrato		15
				tablaEmpleados.getColumn(5).setWidth(ancho/20*3);		// teléfono		15
				tablaEmpleados.getColumn(6).setWidth(20);				// mensaje		x
			}
			public void controlMoved(ControlEvent e) {};
		});

		final Button bEmplNuevo = new Button(cEmplDer, SWT.PUSH);
		bEmplNuevo.setText(bundle.getString("Nuevo"));
		bEmplNuevo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1, 1));

		bEmplNuevo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new I08_1_Anadir_empleado(_tabFolder.getShell(), _bundle, _vista);
				mostrarEmpleados();
			}
		});

		/*final Button bEmplVer = new Button(cEmplDer, SWT.PUSH);
		bEmplVer.setText(bundle.getString("I02_but_Ver"));
		bEmplVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
			
		bEmplVer.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO BD Coger empleado seleccionado de la BD y mostrarlo en
				// la ventana

				// employee created for tests
				Color col = new Color(_tabFolder.getDisplay(), 10, 0, 50);

				// Empleado emp = new Empleado(1, 12345678, "phil", "colins",
				// "-", new Date("12/12/09"), 1, "phil.colins@gmail.com", "", 1,
				// 1, 1,1, new Date("12/12/09"),new Date("12/12/09"), col,null,
				// null);
				// new I08_2_Consultar_empleado(shell, emp, bundle);
			}
		});*/

		final Button bEmplEditar = new Button(cEmplDer, SWT.PUSH);
		bEmplEditar.setText(bundle.getString("Editar"));
		bEmplEditar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1, 1));

		bEmplEditar.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				final int idVend;
				int aux1=tablaEmpleados.getSelectionIndex();
				if (aux1<0){
					MessageBox messageBox = new MessageBox (_tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText ("Error");
					messageBox.setMessage ("Para editar debe seleccionar previamente un empleado de la tabla");					
					e.doit = messageBox.open () == SWT.YES;
				}else{
					TableItem[] aux=tablaEmpleados.getSelection();
					idVend = (Integer)Integer.valueOf(aux[0].getText(1));
					new I08_1_Editar_empleado(_tabFolder.getShell(),_bundle, _vista,idVend);
				}
			}
		});
		
		final Button bEmplBaja = new Button(cEmplDer, SWT.PUSH);

		bEmplBaja.setText(bundle.getString("I02_but_Eliminar"));
		bEmplBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1, 1));
		//bEmplBaja.setEnabled(false);
		bEmplBaja.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				final int idVend;
				int aux1=tablaEmpleados.getSelectionIndex();
				if (aux1<0){
					MessageBox messageBox = new MessageBox (_tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText ("Error");
					messageBox.setMessage ("Para Dar de Baja a un empleado debe seleccionar previamente un empleado de la tabla");					
					e.doit = messageBox.open () == SWT.YES;
				}else{
					MessageBox messageBox = new MessageBox (_tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
					messageBox.setText ("¡ATENCION!");
					messageBox.setMessage ("Se dispone a borrar un empleado de la Base de Datos, ¿Desea Continuar?");					
					if (e.doit = messageBox.open () == SWT.OK) {					
						TableItem[] aux=tablaEmpleados.getSelection();
						idVend = (Integer)Integer.valueOf(aux[0].getText(1));
						_vista.eliminaEmpleado(idVend);
					}
				}
			}
		});
						
		cEmplContr.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {				
				comboCambiado(arg0);
			}
		});
		
		tEmplNombre.addKeyListener(new KeyListener(){			 

			public void keyPressed(KeyEvent arg0) {				
				
			}

			public void keyReleased(KeyEvent arg0) {
				nombreEscrito(arg0);
			}
		});
		
		tEmplNVend.addKeyListener(new KeyListener(){			 

			public void keyPressed(KeyEvent arg0) {				
				
			}

			public void keyReleased(KeyEvent arg0) {
				nombreEscrito(arg0);
			}
		});
		
		tEmplDpto.addKeyListener(new KeyListener(){			 

			public void keyPressed(KeyEvent arg0) {				
				
			}

			public void keyReleased(KeyEvent arg0) {
				nombreEscrito(arg0);
			}
		});
	
	}
	
	public void nombreEscrito(KeyEvent e) {
		mostrarEmpleados();
	}
	
	public void comboCambiado(ModifyEvent e) {
		mostrarEmpleados();
	}
}
