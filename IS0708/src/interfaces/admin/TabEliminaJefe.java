package interfaces.admin;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;

/**
 * Clase que implementa la interfaz del tab para eliminar jefes del menu de
 * administrador
 * 
 * @author Jose Maria Martin
 */
public class TabEliminaJefe {

	private ResourceBundle bundle;
	private Vista vista;
	private TabFolder tabFolder;
	private Table tablaJefes;
	private int seleccionado;// indice seleccionado en la tabla de jefes
	private ArrayList<String> arrayJefes;
	private Combo cmbJefes;
	private Label datosJefe;

	public TabEliminaJefe(final ResourceBundle bundle, final Vista vista,
			final TabFolder tabFolder) {
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;

		// sin seleccionar
		seleccionado = -1;

		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText("Admin:Elimina Jefe");
		tabItemEmpleados.setImage(vista.getImagenes().getIco_chico());

		// Creamos un composite para la pestaña de mensajes
		final Composite cEliminaJefe = new Composite(tabFolder, SWT.NONE);
		tabItemEmpleados.setControl(cEliminaJefe);

		cEliminaJefe.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		GridLayout lTJ = new GridLayout();
		lTJ.numColumns = 1;
		lTJ.makeColumnsEqualWidth = true;
		cEliminaJefe.setLayout(lTJ);
		// 1º elegimos el gerente que queremos eliminar
		final Label nombreJefe = new Label(cEliminaJefe, SWT.None);
		nombreJefe.setText(bundle.getString("I05_selec_jefe"));
		final Composite cTablaJefes = new Composite(cEliminaJefe, SWT.NONE);
		cTablaJefes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		cTablaJefes.setLayout(new GridLayout(2, true));

		tablaJefes = new Table(cTablaJefes, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tablaJefes.setLinesVisible(true);
		tablaJefes.setHeaderVisible(true);
		String[] titles = { bundle.getString("I05_nombre_jefe"),
				bundle.getString("Departamentos") };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaJefes, SWT.NONE);
			column.setText(titles[i]);
		}

		tablaJefes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		tablaJefes.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				// Configurar tamaño de las columnas
				int ancho = tablaJefes.getSize().x;
				tablaJefes.getColumn(0).setWidth(ancho / 5 * 2);
				tablaJefes.getColumn(1).setWidth(ancho / 3 * 2);

			}

			public void controlMoved(ControlEvent arg0) {
			}
		});
		tablaJefes.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent arg0) {
				seleccionado = tablaJefes.getSelectionIndex();
				cmbJefes.removeAll();
				if (arrayJefes != null) {
					// seleccionado=tablaJefes.getSelectionIndex();
					for (int i = 0; i < arrayJefes.size(); i++) {
						if (seleccionado > -1) {
							if (i != seleccionado)
								cmbJefes.add(arrayJefes.get(i));
						} else
							cmbJefes.add(arrayJefes.get(i));
					}
					if (seleccionado > -1)
						datosJefe.setText(arrayJefes.get(seleccionado));
					else
						datosJefe.setText(bundle
								.getString("I05_label_datos_ninguno"));
					cmbJefes.select(0);
				}

			}

		});
		arrayJefes = vista.getNombreTodosJefes();
		for (int i = 0; i < arrayJefes.size(); i++) {
			TableItem tItem = new TableItem(tablaJefes, SWT.NONE);
			String aux = arrayJefes.get(i);
			String num = aux.substring(aux.indexOf("N") + 4, aux.length());
			// System.out.println(num);
			String nombre = aux.substring(0, aux.indexOf("N") - 1);
			tItem.setText(0, nombre);
			Empleado e = new Empleado();
			e.setEmplId(Integer.valueOf(num));
			ArrayList<String> arrayDepts = vista.getNombreDepartamentosJefe(e);
			String depts = "";
			for (int j = 0; j < arrayDepts.size(); j++) {
				depts += arrayDepts.get(j);
				if (j < arrayDepts.size() - 1)
					depts += ", ";
			}
			tItem.setText(1, depts);
		}

		// composite de la derecha
		final Composite cDcha = new Composite(cTablaJefes, SWT.NONE);
		cDcha.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cDcha.setLayout(new GridLayout(1, true));

		final Label lSust = new Label(cDcha, SWT.None);
		lSust.setText(bundle.getString("I05_select_sustituto"));
		cmbJefes = new Combo(cDcha, SWT.BORDER | SWT.READ_ONLY);
		cmbJefes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		if (arrayJefes != null) {
			for (int i = 0; i < arrayJefes.size(); i++) {
				cmbJefes.add(arrayJefes.get(i));
			}
		}
		cmbJefes.select(0);

		final Label infoJefe = new Label(cDcha, SWT.None);
		infoJefe.setText(bundle.getString("I05_label_info_jefe"));
		infoJefe.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		datosJefe = new Label(cDcha, SWT.None);
		datosJefe.setText(bundle.getString("I05_label_datos_ninguno"));
		datosJefe.setForeground(new Color(cDcha.getShell().getDisplay(), 255,
				0, 0));
		datosJefe.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		final Button bQuitarSeleccion = new Button(cDcha, SWT.PUSH);
		bQuitarSeleccion.setText(bundle.getString("I05_quitar_seleccion"));
		bQuitarSeleccion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		// Creamos un oyente
		bQuitarSeleccion.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				seleccionado = -1;
				datosJefe.setText(bundle.getString("I05_label_datos_ninguno"));
				for (int i = 0; i < arrayJefes.size(); i++) {
					cmbJefes.add(arrayJefes.get(i));
				}
				cmbJefes.select(0);
			}
		});

		final Button bEliminar = new Button(cEliminaJefe, SWT.PUSH);
		bEliminar.setText(bundle.getString("I02_but_Eliminar"));
		bEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		// Creamos un oyente
		bEliminar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (seleccionado == -1) {
					MessageBox messageBox = new MessageBox(
							tabFolder.getShell(), SWT.APPLICATION_MODAL
									| SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setMessage(bundle.getString("I05_selec_jefe"));
					messageBox.open();
				} else {
					if (tablaJefes.getItemCount() > 1) {
						MessageBox messageBox3 = new MessageBox(tabFolder
								.getShell(), SWT.APPLICATION_MODAL
								| SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
						messageBox3.setMessage(bundle
								.getString("I05_confirm_elim_jefe"));
						int respuesta = messageBox3.open();
						if (respuesta == SWT.OK) {
							// obtenemos los datos del jefe sustituto
							int indSust = cmbJefes.getSelectionIndex();
							String item = cmbJefes.getItem(indSust);
							String numSust = item.substring(
									item.indexOf("N") + 4, item.length());
							System.out.println(numSust);
							String nombreSust = item.substring(0, item
									.indexOf("N") - 1);
							System.out.println(nombreSust);
							// obtenemos los datos del jefe a eliminar
							String aux = datosJefe.getText();
							String numElim = aux.substring(
									aux.indexOf("N") + 4, aux.length());
							System.out.println(numElim);
							String nombreElim = aux.substring(0, aux
									.indexOf("N") - 1);
							System.out.println(nombreElim);
							// obtenemos los departamentos a tratar al eliminar
							// su jefe
							boolean okis = true;
							Empleado emp = new Empleado();
							emp.setEmplId(Integer.valueOf(numElim));
							ArrayList<String> arrayDepts = vista
									.getNombreDepartamentosJefe(emp);
							if (arrayDepts.size()>0){
								for (int j = 0; j < arrayDepts.size(); j++) {
									String d = arrayDepts.get(j);
									System.out.println(d);
									okis = vista.cambiarJefeDepartamento(d, numSust);
									okis = okis
											&& vista
												.getControlador()
												.insertDepartamentoUsuario(
														Integer
																.valueOf(numSust),
														d);
								}
							}
							okis = okis
									&& vista.getControlador().eliminaEmpleado(
											Integer.valueOf(numElim));
							arrayJefes.remove(seleccionado);
							if (okis) {
								MessageBox messageBox2 = new MessageBox(
										tabFolder.getShell(),
										SWT.APPLICATION_MODAL
												| SWT.ICON_INFORMATION | SWT.OK);
								messageBox2.setMessage(bundle
										.getString("I05_elim_ok"));
								messageBox2.open();
								datosJefe.setText(bundle
										.getString("I05_label_datos_ninguno"));
								cmbJefes.removeAll();
								if (arrayJefes != null) {
									for (int i = 0; i < arrayJefes.size(); i++) {
										cmbJefes.add(arrayJefes.get(i));
									}
									cmbJefes.select(0);
								}
								String depts = tablaJefes.getItem(seleccionado)
										.getText(1);
								tablaJefes.remove(seleccionado);
								TableItem tItem = tablaJefes.getItem(indSust);
								if (!depts.equals("")) tItem.setText(1, tItem.getText(1) + ", "
										+ depts);
								seleccionado = -1;
							} else {
								MessageBox messageBox2 = new MessageBox(
										tabFolder.getShell(),
										SWT.APPLICATION_MODAL | SWT.ICON_ERROR
												| SWT.OK);
								messageBox2.setMessage(bundle
										.getString("I05_elim_error"));
								messageBox2.open();
							}

						}
					} else {
						MessageBox messageBox3 = new MessageBox(tabFolder
								.getShell(), SWT.APPLICATION_MODAL
								| SWT.ICON_ERROR | SWT.OK );
						messageBox3.setMessage(bundle
								.getString("I05_solo_uno"));
						messageBox3.open();
					}
				}
			}
		});
	}

}
