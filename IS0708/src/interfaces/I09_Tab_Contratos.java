package interfaces;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import aplicacion.Contrato;
import aplicacion.Empleado;
import aplicacion.Vista;

public class I09_Tab_Contratos extends Thread{
	final ResourceBundle bundle;
	final Vista vista;
	final TabFolder tabFolder;
	final Table tablaContratos;
	final Image ico_cuadrante;
	ArrayList <Contrato> contratos = null;
	private boolean datosInterfazCargados = false;
	
	/**
	 * Implementa un hilo que coge los empleados del departamento del servidor.
	 */
	public void run() {
		//boolean run = true;
		try {
			while (!vista.isCacheCargada()) {
				sleep(5000);
			}
		} catch (Exception e) {}
		// Coge los datos de todos los contratos
		contratos = vista.getListaContratosDepartamento();
		datosInterfazCargados = true;
		//while (run) {
			
			if (tablaContratos.isDisposed()){} //run = false;
			else {
				if (!tablaContratos.isDisposed()) {
					// Actualizar tabla
					if (!tablaContratos.isDisposed()) {
						tablaContratos.getDisplay().asyncExec(new Runnable () {
							public void run() {
								mostrarContratos();
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
	//}
	
	private void mostrarContratos() {
		if (vista.isCacheCargada() && datosInterfazCargados) {
			//tablaContratos.removeAll();
			for(int i=0;i<contratos.size();i++){
				TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
				Contrato c = contratos.get(i);
				tItem.setText(0, Integer.toString(c.getNumeroContrato()));
				ArrayList<Empleado> emp = vista.getEmpleados(null,null, c.getNumeroContrato(),
						null, null, null, null);
				String empleados = "";
				for (int j = 0; j < emp.size(); j++) {
					Empleado e = emp.get(j);
					empleados += e.getNombre() + " " + e.getApellido1();
					if (j != emp.size() - 1)
						empleados += ",";
				}
				tItem.setText(1, empleados);
				tItem.setText(2, Integer.toString(c.getTurnoInicial()));
				tItem.setText(3, c.getNombreContrato());
				tItem.setText(4, c.getPatron());
				tItem.setText(5, Integer.toString(c.getDuracionCiclo()));
				tItem.setText(6, Double.toString(c.getSalario()));
				tItem.setText(7, Integer.toString(c.getTipoContrato()));
			}
		}
	}
	
	/**
	 * Constructor. Crea un tab con un listado de contratos
	 * 
	 * @param tabFolder el tabFolder donde colocarlo
	 * @param vista la vista de la aplicación
	 * @param bundle la herramienta de idiomas
	 * @author Jose Maria Martin
	 */
	public I09_Tab_Contratos(final TabFolder tabFolder, final Vista vista, final ResourceBundle bundle ) {
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;
		ico_cuadrante = new Image(tabFolder.getDisplay(), I02_Principal.class.getResourceAsStream("ico_cuadrante.gif"));
		
		TabItem tabItemContratos = new TabItem(tabFolder, SWT.NONE);
		tabItemContratos.setText(bundle.getString("Contratos"));
		tabItemContratos.setImage(ico_cuadrante);

		final Composite cContratos = new Composite(tabFolder, SWT.NONE);
		tabItemContratos.setControl(cContratos);

		cContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		cContratos.setLayout(new GridLayout(3, false));

		tablaContratos = new Table(cContratos, SWT.MULTI
				| SWT.BORDER | SWT.FULL_SELECTION);
		start();
		tablaContratos.setLinesVisible(true);
		tablaContratos.setHeaderVisible(true);
		String[] titles = { bundle.getString("Contrato"),
				bundle.getString("Empleados"),
				bundle.getString("I09_turno_inicial"),
				bundle.getString("I09_lab_NombreContrato"),
				bundle.getString("Patron"),
				bundle.getString("I09_lab_LongitudCiclo"),
				bundle.getString("I09_lab_salario"),
				bundle.getString("I09_lab_tipo") };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaContratos, SWT.NONE);
			column.setText(titles[i]);
		}

		
		//final ArrayList <Contrato> contratos = vista.getListaContratosDepartamento();
		
		//final ArrayList <Contrato> contratos = new ArrayList <Contrato>();
		//contratos.add(new Contrato("perry",23,72,1,"1:d",50,1));
		//if(contratos!=null){
//		for(int i=0;i<contratos.size();i++){
//			TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
//			Contrato c = contratos.get(i);
//			tItem.setText(0, Integer.toString(c.getNumeroContrato()));
//			//ArrayList<Empleado> emp = vista.getControlador().getEmpleados(null,
//			//		null, c.getNumeroContrato(), null, null, null, null);
//			ArrayList<Empleado> emp = vista.getEmpleados(null,
//					null, c.getNumeroContrato(), null, null, null, null);
//			String empleados = "";
//			for (int j = 0; j < emp.size(); j++) {
//				Empleado e = emp.get(j);
//				empleados += e.getNombre() + " " + e.getApellido1();
//				if (j != emp.size() - 1)
//					empleados += ",";
//			}
//			tItem.setText(1, empleados);
//			tItem.setText(2, Integer.toString(c.getTurnoInicial()));
//			tItem.setText(3, c.getNombreContrato());
//			tItem.setText(4, c.getPatron());
//			tItem.setText(5, Integer.toString(c.getDuracionCiclo()));
//			tItem.setText(6, Double.toString(c.getSalario()));
//			tItem.setText(7, Integer.toString(c.getTipoContrato()));
//		}
		//}
		for (int i = 0; i < titles.length; i++) {
			tablaContratos.getColumn(i).pack();
		}
		tablaContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));

		final Button bNuevoContrato = new Button(cContratos, SWT.PUSH);
		final Button bModificarContrato = new Button(cContratos, SWT.PUSH);
		final Button bEliminarContrato = new Button(cContratos, SWT.PUSH);

		bNuevoContrato.setText(bundle.getString("I09_lab_NuevoContrato"));
		bModificarContrato.setText(bundle.getString("I09_Modif_contrato"));
		bEliminarContrato.setText(bundle.getString("I09_Eliminar_contrato"));
		bNuevoContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		bModificarContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		bEliminarContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));

		bNuevoContrato.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				I09_1_Creacion_contratos i09 = new I09_1_Creacion_contratos(tabFolder.getShell(), bundle, vista, 0, -1,null);
				while (!i09.getShell().isDisposed()) {
			         if (!tabFolder.getShell().getDisplay().readAndDispatch()) {
			        	 tabFolder.getShell().getDisplay().sleep();
			         }
			      }
				Contrato c=i09.getContratoInsertado();
				if (c!=null){
				contratos.add(c);	
				tablaContratos.removeAll();
				ArrayList <Integer> ids=i09.getTurnosInsertados();
				int idc=c.getNumeroContrato();
				for(int i=0;i<ids.size();i++){
					int idt=ids.get(i);
					if (idt!=c.getTurnoInicial()&&(idc!=-1)) 
						//CAMBIAR cuando este actualizada la vista
						vista.getControlador().insertTurnoPorContrato(idt, idc);
				}
								
				for(int i=0;i<contratos.size();i++){
					TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
					Contrato aux = contratos.get(i);
					tItem.setText(0, Integer.toString(aux.getNumeroContrato()));
					ArrayList <Empleado> emps=vista.getEmpleados(null, null, aux.getNumeroContrato(),null, null, null, null);
					String empleados="";
					for (int j=0;j<emps.size();j++){
						Empleado emp=emps.get(j);
						empleados+=emp.getNombre()+" "+emp.getApellido1();
						if (j!=emps.size()-1) empleados+=",";
					}
					tItem.setText(1, empleados);
					tItem.setText(2, Integer.toString(aux.getTurnoInicial()));
					tItem.setText(3, aux.getNombreContrato());
					tItem.setText(4, aux.getPatron());
					tItem.setText(5, Integer.toString(aux.getDuracionCiclo()));
					tItem.setText(6, Double.toString(aux.getSalario()));
					tItem.setText(7, Integer.toString(aux.getTipoContrato()));
				}
				tablaContratos.redraw();
				}
			}
		});

		bModificarContrato.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if(tablaContratos.getSelectionIndex()>-1){
					TableItem it=tablaContratos.getItem(tablaContratos.getSelectionIndex());
					I09_1_Creacion_contratos i09c=new I09_1_Creacion_contratos(tabFolder.getShell(), bundle, vista, 1,
							Integer.parseInt(it.getText(0)),contratos.get(tablaContratos.getSelectionIndex()));
					int index=tablaContratos.getSelectionIndex();
					while (!i09c.getShell().isDisposed()) {
				         if (!tabFolder.getShell().getDisplay().readAndDispatch()) {
				        	 tabFolder.getShell().getDisplay().sleep();
				         }
				    }
					Contrato caux=i09c.getContratoModificado();
					if(caux!=null){					
						it=tablaContratos.getItem(index);
						it.setText(2, Integer.toString(caux.getTurnoInicial()));
						it.setText(3, caux.getNombreContrato());
						it.setText(4, caux.getPatron());
						it.setText(5, Integer.toString(caux.getDuracionCiclo()));
						it.setText(6, Double.toString(caux.getSalario()));
						it.setText(7, Integer.toString(caux.getTipoContrato()));
						
					}
				} else {
					MessageBox messageBox = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION
									| SWT.OK | SWT.CANCEL);
					messageBox.setMessage(bundle
							.getString("I09_bot_modif_contrato_no_select"));
					messageBox.open();					
				}
			}
		});

		bEliminarContrato.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if(tablaContratos.getSelectionIndex()>-1){
					MessageBox messageBox = new MessageBox(tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
					messageBox.setMessage (bundle.getString("I09_bot_elim_contrato"));
					int response=messageBox.open();
					if(response==SWT.OK){
						int index=tablaContratos.getSelectionIndex();
						TableItem tit=tablaContratos.getItem(index);
						//CAMBIAR 
						boolean okis=vista.getControlador().eliminaContrato(Integer.valueOf(tit.getText(0)));
						okis=okis&&vista.getControlador().eliminaContratoConTurnos(Integer.valueOf(tit.getText(0)));
						if (okis){							
							tablaContratos.removeAll();
							contratos.remove(index);
							for(int i=0;i<contratos.size();i++){
								TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
								Contrato aux = contratos.get(i);
								tItem.setText(0, Integer.toString(aux.getNumeroContrato()));
								ArrayList <Empleado> emps=vista.getEmpleados(null, null, aux.getNumeroContrato(),null, null, null, null);
								String empleados="";
								for (int j=0;j<emps.size();j++){
									Empleado emp=emps.get(j);
									empleados+=emp.getNombre()+" "+emp.getApellido1();
									if (j!=emps.size()-1) empleados+=",";
								}
								tItem.setText(1, empleados);
								tItem.setText(2, Integer.toString(aux.getTurnoInicial()));
								tItem.setText(3, aux.getNombreContrato());
								tItem.setText(4, aux.getPatron());
								tItem.setText(5, Integer.toString(aux.getDuracionCiclo()));
								tItem.setText(6, Double.toString(aux.getSalario()));
								tItem.setText(7, Integer.toString(aux.getTipoContrato()));
							}
							tablaContratos.redraw();
							MessageBox messageBox2 = new MessageBox(tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
							messageBox2.setText("Info");
							messageBox2.setMessage(bundle.getString("I09_elim_Contrato"));
							messageBox2.open();
						}
						else {
							MessageBox messageBox2 = new MessageBox(tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
							messageBox2.setText(bundle.getString("Error"));
							messageBox2.setMessage(bundle.getString("I09_err_elim_Contrato"));
							messageBox2.open();
						}
					}
				} else {
					MessageBox messageBox = new MessageBox(tabFolder.getShell(),
							SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION
									| SWT.OK | SWT.CANCEL);
					messageBox.setMessage(bundle
							.getString("I09_bot_elim_contrato_no_select"));
					messageBox.open();
				}
			}
		});

	}
}
