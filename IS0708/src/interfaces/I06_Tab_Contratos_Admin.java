package interfaces;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
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

import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;
import aplicacion.datos.Turno;

public class I06_Tab_Contratos_Admin extends Thread {
	final ResourceBundle bundle;

	final Vista vista;

	final TabFolder tabFolder;

	final Table tablaContratos;

	final Image ico_cuadrante;

	//private boolean datosInterfazCargados = false;

	private Button bNuevoContrato;

	private Button bModificarContrato;

	private Button bEliminarContrato;

	private int indiceJefe;
	
	//private Locale locale;

	/**
	 * Implementa un hilo que coge los empleados del departamento del servidor.
	 */
//	public void run() {
//		// boolean run = true;
//		try {
//			while (!vista.isCacheCargada()) {
//				sleep(5000);
//			}
//		} catch (Exception e) {
//		}
//		// Coge los datos de todos los contratos
//		// contratos = vista.getListaContratosDepartamento();
//		datosInterfazCargados = true;
//		// while (run) {
//
//		if (tablaContratos.isDisposed()) {
//		} // run = false;
//		else {
//			if (!tablaContratos.isDisposed()) {
//				// Actualizar tabla
//				if (!tablaContratos.isDisposed()) {
//					tablaContratos.getDisplay().asyncExec(new Runnable() {
//						public void run() {
//							mostrarContratos();
//							bNuevoContrato.setEnabled(true);
//							bModificarContrato.setEnabled(true);
//							bEliminarContrato.setEnabled(true);
//						}
//					});
//				}
//			}
//			try {
//				// Espera 10 segundos (¿cómo lo dejamos?)
//				sleep(10000);
//			} catch (Exception e) {
//			}
//		}
//	}
//
//	// }
//
//	/**
//	 * Añade a la tabla tablaContratos, cada uno de los contratos disponibles
//	 * del departamento del usuario registrado
//	 * 
//	 */
//	private void mostrarContratos() {
//		if (vista.isCacheCargada() && datosInterfazCargados) {
//			tablaContratos.removeAll();
//			for (int i = 0; i < vista.getListaContratosDepartamento().size(); i++) {
//				TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
//				Contrato c = vista.getListaContratosDepartamento().get(i);
//				tItem.setText(0, Integer.toString(c.getNumeroContrato()));
//				ArrayList<Empleado> emp = vista.getEmpleados(null, null, c
//						.getNumeroContrato(), null, null, null, null);
//				if (indiceJefe == -1) {
//					for (int j = 0; j < emp.size(); j++) {
//						Empleado aux = emp.get(j);
//						if (aux.getRango() == 2)
//							indiceJefe = i;
//					}
//				}
//				String empleados = "";
//				for (int j = 0; j < emp.size(); j++) {
//					Empleado e = emp.get(j);
//					empleados += e.getNombre() + " " + e.getApellido1();
//					if (j != emp.size() - 1)
//						empleados += ",";
//				}
//				tItem.setText(1, empleados);
//				tItem.setText(2, Integer.toString(c.getTurnoInicial()));
//				tItem.setText(3, c.getNombreContrato());
//				tItem.setText(4, c.getPatron());
//				tItem.setText(5, Integer.toString(c.getDuracionCiclo()));
//				tItem.setText(6, Double.toString(c.getSalario()));
//				tItem.setText(7, Integer.toString(c.getTipoContrato()));
//			}
//		}
//	}
	
	private void mostrarContratos() {
			ArrayList <Empleado> jefes=vista.getEmpleados(null, null, null, null, null, null, 2);
			ArrayList <Contrato> contratosJefes=new ArrayList <Contrato>();
			for (int j=0;j<jefes.size();j++){
				contratosJefes.add(jefes.get(j).getContrato(vista));
			}
			tablaContratos.removeAll();
			for (int i = 0; i < contratosJefes.size(); i++) {
				TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
				Contrato c = contratosJefes.get(i);
				tItem.setText(0, Integer.toString(c.getNumeroContrato()));
				ArrayList<Empleado> emp = vista.getEmpleados(null, null, c
						.getNumeroContrato(), null, null, null, null);
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

	/**
	 * Constructor. Crea un tab con un listado de contratos
	 * 
	 * @param tabFolder
	 *            el tabFolder donde colocarlo
	 * @param vista
	 *            la vista de la aplicación
	 * @param bundle
	 *            la herramienta de idiomas
	 * @author Jose Maria Martin
	 */
	public I06_Tab_Contratos_Admin(final TabFolder tabFolder, final Vista vista,
			final ResourceBundle bundle, final Locale locale) {
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;
		//this.locale = locale;
		this.indiceJefe = -1;
		ico_cuadrante = new Image(tabFolder.getDisplay(), I02_Principal.class
				.getResourceAsStream("ico_cuadrante.gif"));

		TabItem tabItemContratos = new TabItem(tabFolder, SWT.NONE);
		tabItemContratos.setText(bundle.getString("Contratos"));
		tabItemContratos.setImage(ico_cuadrante);

		final Composite cContratos = new Composite(tabFolder, SWT.NONE);
		tabItemContratos.setControl(cContratos);

		cContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		cContratos.setLayout(new GridLayout(3, false));

		tablaContratos = new Table(cContratos, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		start();
		tablaContratos.setLinesVisible(true);
		tablaContratos.setHeaderVisible(true);
		String[] titles = { bundle.getString("Contrato"),
				bundle.getString("I05_nombre_jefe"),
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

		for (int i = 0; i < titles.length; i++) {
			tablaContratos.getColumn(i).pack();
		}
		tablaContratos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));
		tablaContratos.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				// // Configurar tamaño de las columnas
				int ancho = tablaContratos.getSize().x;
				tablaContratos.getColumn(0).setWidth(ancho / 40 * 3);
				tablaContratos.getColumn(1).setWidth(ancho / 8 * 2);
				tablaContratos.getColumn(2).setWidth(ancho / 40 * 3);
				tablaContratos.getColumn(3).setWidth(ancho / 17 * 3);
				tablaContratos.getColumn(4).setWidth(ancho / 20 * 3);
				tablaContratos.getColumn(5).setWidth(ancho / 30 * 3);
				tablaContratos.getColumn(6).setWidth(ancho / 40 * 3);
				tablaContratos.getColumn(7).setWidth(ancho / 30 * 3);
			}

			public void controlMoved(ControlEvent arg0) {
			}

		});
		mostrarContratos();
		bNuevoContrato = new Button(cContratos, SWT.PUSH);
		bModificarContrato = new Button(cContratos, SWT.PUSH);
		bEliminarContrato = new Button(cContratos, SWT.PUSH);
		//bNuevoContrato.setEnabled(false);
		//bModificarContrato.setEnabled(false);
		//bEliminarContrato.setEnabled(false);

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
//				I09_1_Creacion_contratos i09 = new I09_1_Creacion_contratos(
//						tabFolder.getShell(), bundle,locale, vista, 0, -1, null);
//				while (!i09.getShell().isDisposed()) {
//					if (!tabFolder.getShell().getDisplay().readAndDispatch()) {
//						tabFolder.getShell().getDisplay().sleep();
//					}
//				}
//				Contrato c = i09.getContratoInsertado();
//				if (c != null) {
//					// vista.getListaContratosDepartamento().add(c);
//					// tablaContratos.removeAll();
//					ArrayList<Integer> ids = i09.getTurnosInsertados();
//					int idc = c.getNumeroContrato();
//					for (int i = 0; i < ids.size(); i++) {
//						int idt = ids.get(i);
//						if (idt != c.getTurnoInicial() && (idc != -1))
//							// CAMBIAR cuando este actualizada la vista
//							vista.insertTurnoPorContrato(idt, idc);
//						// vista.getControlador().insertTurnoPorContrato(idt,
//						// idc);
//					}
//					ids.clear();
//					ids = i09.getTurnosAñadidos();
//					for (int i = 0; i < ids.size(); i++) {
//						int idt = ids.get(i);
//						if (idt != c.getTurnoInicial() && (idc != -1))
//							// CAMBIAR cuando este actualizada la vista
//							vista.insertTurnoPorContrato(idt, idc);
//						// vista.getControlador().insertTurnoPorContrato(idt,
//						// idc);
//					}
//					TableItem tItem = new TableItem(tablaContratos, SWT.NONE);
//					tItem.setText(0, Integer.toString(c.getNumeroContrato()));
//					//ArrayList<Empleado> emp = vista.getEmpleados(null, null, c
//					//		.getNumeroContrato(), null, null, null, null);
//					String empleados = "";
//					tItem.setText(1, empleados);
//					tItem.setText(2, Integer.toString(c.getTurnoInicial()));
//					tItem.setText(3, c.getNombreContrato());
//					tItem.setText(4, c.getPatron());
//					tItem.setText(5, Integer.toString(c.getDuracionCiclo()));
//					tItem.setText(6, Double.toString(c.getSalario()));
//					tItem.setText(7, Integer.toString(c.getTipoContrato()));
//					// mostrarContratos();
//					// tablaContratos.redraw();
//				}
				MessageBox messageBox = new MessageBox(tabFolder
						.getShell(), SWT.APPLICATION_MODAL
						| SWT.ICON_INFORMATION | SWT.OK);
				messageBox.setMessage("Aun no operativo");
				messageBox.open();
			}
		});

		bModificarContrato.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

//				MessageBox msgBox = new MessageBox(tabFolder.getShell(),
//						SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.OK
//								| SWT.CANCEL);
//				msgBox
//						.setMessage(bundle
//								.getString("I09_aviso_inconsistencias"));
//				msgBox.setText("Warning");
//				int resp = msgBox.open();
//				if (resp == SWT.OK) {
//					if (tablaContratos.getSelectionIndex() > -1) {
//						if (tablaContratos.getSelectionIndex() == indiceJefe) {
//							MessageBox messageBox = new MessageBox(tabFolder
//									.getShell(), SWT.APPLICATION_MODAL
//									| SWT.ICON_INFORMATION | SWT.OK);
//							messageBox.setMessage(bundle
//									.getString("I09_no_elim_modif_jefe"));
//							messageBox.open();
//						} else {
//							TableItem it = tablaContratos
//									.getItem(tablaContratos.getSelectionIndex());
//							I09_1_Creacion_contratos i09c = new I09_1_Creacion_contratos(
//									tabFolder.getShell(), bundle,locale, vista, 1,
//									Integer.parseInt(it.getText(0)),
//									vista.getListaContratosDepartamento().get(
//											tablaContratos.getSelectionIndex()));
//							int index = tablaContratos.getSelectionIndex();
//							while (!i09c.getShell().isDisposed()) {
//								if (!tabFolder.getShell().getDisplay()
//										.readAndDispatch()) {
//									tabFolder.getShell().getDisplay().sleep();
//								}
//							}
//							Contrato caux = i09c.getContratoModificado();
//							if (caux != null) {
//								it = tablaContratos.getItem(index);
//								it.setText(2, Integer.toString(caux
//										.getTurnoInicial()));
//								it.setText(3, caux.getNombreContrato());
//								it.setText(4, caux.getPatron());
//								it.setText(5, Integer.toString(caux
//										.getDuracionCiclo()));
//								it.setText(6, Double
//										.toString(caux.getSalario()));
//								it.setText(7, Integer.toString(caux
//										.getTipoContrato()));
//
//							}
//						}
//					} else {
//						MessageBox messageBox = new MessageBox(tabFolder
//								.getShell(), SWT.APPLICATION_MODAL
//								| SWT.ICON_INFORMATION | SWT.OK);
//						messageBox.setMessage(bundle
//								.getString("I09_bot_modif_contrato_no_select"));
//						messageBox.open();
//					}
//				}
				MessageBox messageBox = new MessageBox(tabFolder
						.getShell(), SWT.APPLICATION_MODAL
						| SWT.ICON_INFORMATION | SWT.OK);
				messageBox.setMessage("Aun no operativo");
				messageBox.open();
			}
		});

		bEliminarContrato.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
//				MessageBox msgBox = new MessageBox(tabFolder.getShell(),
//						SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.OK
//								| SWT.CANCEL);
//				msgBox
//						.setMessage(bundle
//								.getString("I09_aviso_inconsistencias"));
//				msgBox.setText("Warning");
//				int resp = msgBox.open();
//				if (resp == SWT.OK) {
//					if (tablaContratos.getSelectionIndex() > -1) {
//						if (!tablaContratos.getItem(
//								tablaContratos.getSelectionIndex()).getText(1)
//								.equals("")) {
//							MessageBox messageBox = new MessageBox(tabFolder
//									.getShell(), SWT.APPLICATION_MODAL
//									| SWT.ICON_WARNING | SWT.OK);
//							messageBox
//									.setMessage(bundle
//											.getString("I09_no_se_puede_elim_contrato"));
//							messageBox.open();
//						} else {
//							MessageBox messageBox = new MessageBox(tabFolder
//									.getShell(), SWT.APPLICATION_MODAL
//									| SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
//							messageBox.setMessage(bundle
//									.getString("I09_bot_elim_contrato"));
//							int response = messageBox.open();
//							if (response == SWT.OK) {
//								int index = tablaContratos.getSelectionIndex();
//								TableItem tit = tablaContratos.getItem(index);
//								// eliminar los turnos que no pertenecen a otro
//								// contrato
//								ArrayList<Contrato> auxContratos = vista
//										.getListaContratosDepartamento();
//								ArrayList<Integer> turnosFuera = new ArrayList<Integer>();
//								int idOut = Integer.valueOf(tit.getText(0));
//								ArrayList<Turno> turnosOut = vista
//										.getControlador()
//										.getTurnosDeUnContrato(idOut);
//								int apariciones = 0;
//								for (int k = 0; k < turnosOut.size(); k++) {
//									int idEliminado = turnosOut.get(k)
//											.getIdTurno();
//									for (int i = 0; i < auxContratos.size(); i++) {
//										Contrato aux = auxContratos.get(i);
//										if (aux.getNumeroContrato() != idOut) {
//											ArrayList<Turno> auxTurnos = vista
//													.getControlador()
//													.getTurnosDeUnContrato(
//															aux
//																	.getNumeroContrato());
//											for (int j = 0; j < auxTurnos
//													.size(); j++) {
//												if (idEliminado == auxTurnos
//														.get(j).getIdTurno())
//													apariciones++;
//											}
//										}
//									}
//									if (apariciones == 0)
//										turnosFuera.add(idEliminado);
//								}
//								boolean okis = true;
//								if (turnosFuera.size() == 0)
//									okis = true;
//								else {
//									for (int i = 0; i < turnosFuera.size(); i++)
//										okis = okis
//												&& vista
//														.eliminaTurno(turnosFuera
//																.get(i));
//								}
//								// CAMBIAR
//								okis = okis && vista.eliminaContrato(idOut);
//								// boolean
//								// okis=vista.getControlador().eliminaContrato(Integer.valueOf(tit.getText(0)));
//								// okis = okis
//								// && vista.getControlador()
//								// .eliminaContratoConTurnos(
//								// Integer.valueOf(tit
//								// .getText(0)));
//								okis = okis
//										&& vista
//												.eliminaContratoConTurnos(Integer
//														.valueOf(tit.getText(0)));
//								if (okis) {
//									// tablaContratos.removeAll();
//									// vista.getListaContratosDepartamento().remove(index);
//
//									tablaContratos.remove(index);
//									// mostrarContratos();
//									// tablaContratos.redraw();
//									MessageBox messageBox2 = new MessageBox(
//											tabFolder.getShell(),
//											SWT.APPLICATION_MODAL | SWT.OK
//													| SWT.ICON_INFORMATION);
//									messageBox2.setText("Info");
//									messageBox2.setMessage(bundle
//											.getString("I09_elim_Contrato"));
//									messageBox2.open();
//								} else {
//									MessageBox messageBox2 = new MessageBox(
//											tabFolder.getShell(),
//											SWT.APPLICATION_MODAL | SWT.OK
//													| SWT.ICON_ERROR);
//									messageBox2.setText(bundle
//											.getString("Error"));
//									messageBox2
//											.setMessage(bundle
//													.getString("I09_err_elim_Contrato"));
//									messageBox2.open();
//								}
//							}
//						}
//					} else {
//						MessageBox messageBox = new MessageBox(tabFolder
//								.getShell(), SWT.APPLICATION_MODAL
//								| SWT.ICON_INFORMATION | SWT.OK);
//						messageBox.setMessage(bundle
//								.getString("I09_bot_elim_contrato_no_select"));
//						messageBox.open();
//					}
//				}
				MessageBox messageBox = new MessageBox(tabFolder
						.getShell(), SWT.APPLICATION_MODAL
						| SWT.ICON_INFORMATION | SWT.OK);
				messageBox.setMessage("Aun no operativo");
				messageBox.open();
			}
		});

	}
}
