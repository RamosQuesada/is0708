package interfaces;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;

public class I05_Tab_EliminaJefe {
	
	private ResourceBundle bundle;

	private Vista vista;

	private TabFolder tabFolder;
	
	private Image ico_chico;
	
	private Table tablaJefes;

	public I05_Tab_EliminaJefe(ResourceBundle bundle, Vista vista,
			TabFolder tabFolder) {
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;
		//cargamos las imagenes
		ico_chico = new Image(tabFolder.getDisplay(), 
				I05_Tab_EliminaJefe.class.getResourceAsStream("ico_chico.gif"));
		
		TabItem tabItemEmpleados = new TabItem(tabFolder, SWT.NONE);
		tabItemEmpleados.setText("Admin:Elimina Jefe");
		tabItemEmpleados.setImage(ico_chico);

		// Creamos un composite para la pestaña de mensajes
		final Composite cEliminaJefe = new Composite(tabFolder, SWT.NONE);
		tabItemEmpleados.setControl(cEliminaJefe);

		cEliminaJefe.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		GridLayout lTJ = new GridLayout();
		lTJ.numColumns = 1;
		lTJ.makeColumnsEqualWidth = true;
		cEliminaJefe.setLayout(lTJ);
		// 1º elegimos el gerente que queremos eliminar
		final Label nombreJefe = new Label(cEliminaJefe, SWT.None);
		nombreJefe.setText(bundle.getString("I05_selec_jefe"));
//		final Combo comboGerenteElim = new Combo(cEliminaJefe, SWT.BORDER
//				| SWT.READ_ONLY);
//		final String[] textoListaGerentes = new String[] { "GERENTE1",
//				"GERENTE2", "GERENTE3" };
//		comboGerenteElim.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
//				true, 0, 0));
//		comboGerenteElim.setItems(textoListaGerentes);
//		comboGerenteElim.select(0);
		final Composite cTablaJefes = new Composite(cEliminaJefe, SWT.NONE);
		cTablaJefes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		cTablaJefes.setLayout(new GridLayout(2, true));

		tablaJefes = new Table(cTablaJefes, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tablaJefes.setLinesVisible(true);
		tablaJefes.setHeaderVisible(true);
		String[] titles = {bundle.getString("I05_nombre_jefe"),bundle.getString("Departamentos")};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaJefes, SWT.NONE);
			column.setText(titles[i]);
		}
		
		tablaJefes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 1, 1));
		tablaJefes.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				// Configurar tamaño de las columnas
				int ancho = tablaJefes.getSize().x ;
				tablaJefes.getColumn(0).setWidth(ancho/5*2);
				tablaJefes.getColumn(1).setWidth(ancho/3*2);		
				
			}

			public void controlMoved(ControlEvent arg0) {
			}			
		});
		
		ArrayList<String> arrayJefes = vista.getNombreTodosJefes();
		for (int i = 0; i < vista.getListaContratosDepartamento().size(); i++) {
			TableItem tItem = new TableItem(tablaJefes, SWT.NONE);
			String nombre = arrayJefes.get(i);		
			tItem.setText(0, nombre);
			//tItem.setText(2, Integer.toString(c.getTurnoInicial()));			
		}
		
		//composite de la derecha
		final Composite cDcha = new Composite(cTablaJefes, SWT.NONE);
		cDcha.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		cDcha.setLayout(new GridLayout(1, true));
		
		final Label lSust = new Label(cDcha, SWT.None);
		lSust.setText(bundle.getString("I05_select_sustituto"));
		final Combo cmbJefes = new Combo(cDcha, SWT.BORDER | SWT.READ_ONLY);
		cmbJefes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		
		if (arrayJefes != null) {
			for (int i = 0; i < arrayJefes.size(); i++) {
				cmbJefes.add(arrayJefes.get(i));
			}
		}
		cmbJefes.select(0);
//		final Label opcionJefes = new Label(cEliminaJefe, SWT.None);
//		opcionJefes
//				.setText("¿Que desea hacer con los empleados del gerente seleccionado?:");
//
//		final Button bDejarSinAsignar = new Button(cEliminaJefe, SWT.RADIO);
//		bDejarSinAsignar.setText("Dejar sin asignar:");
//		bDejarSinAsignar.setSelection(true);
//		final Button bAsignarAUnGerente = new Button(cEliminaJefe, SWT.RADIO);
//		bAsignarAUnGerente.setText("Asignar a otro gerente:");
//
//		final Combo comboGerenteSust = new Combo(cEliminaJefe, SWT.BORDER
//				| SWT.READ_ONLY);
//		comboGerenteSust.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
//				true, 0, 0));
//		comboGerenteSust.setItems(textoListaGerentes);
//		comboGerenteSust.select(0);
//
//		final Button bAsignarAGerentes = new Button(cEliminaJefe, SWT.RADIO);
//		bAsignarAGerentes.setText("Seleccionar asignacion uno a uno:");
//		// Introducimos manualmente unos mensajes por defecto
//		final Composite cEliminaGerente2 = new Composite(cEliminaJefe,
//				SWT.BORDER);
//
//		cEliminaGerente2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
//				true, 1, 1));
//		GridLayout lTJ2 = new GridLayout();
//		lTJ2.numColumns = 4;
//		lTJ2.makeColumnsEqualWidth = true;
//		cEliminaGerente2.setLayout(lTJ2);
//		cEliminaGerente2.setEnabled(false);
//		cEliminaGerente2.setVisible(false);
//		comboGerenteSust.setEnabled(false);
//
//		bDejarSinAsignar.addFocusListener(new FocusListener() {
//
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				cEliminaGerente2.setEnabled(false);
//				cEliminaGerente2.setVisible(false);
//				comboGerenteSust.setEnabled(false);
//			}
//
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		bAsignarAUnGerente.addFocusListener(new FocusListener() {
//
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				cEliminaGerente2.setEnabled(false);
//				cEliminaGerente2.setVisible(false);
//				comboGerenteSust.setEnabled(true);
//			}
//
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		bAsignarAGerentes.addFocusListener(new FocusListener() {
//
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				cEliminaGerente2.setEnabled(true);
//				cEliminaGerente2.setVisible(true);
//				comboGerenteSust.setEnabled(false);
//			}
//
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		for (int i = 0; i < 3; i++) {
//			/*
//			 * TableItem tItem = new TableItem (tablaJefes, SWT.NONE);
//			 * tItem.setText (0, "Nombre"); tItem.setText (1, "Apellidos");
//			 * tItem.setText (2, "Departamento");
//			 */
//			final Label nombreJefe = new Label(cEliminaGerente2, SWT.None);
//			nombreJefe.setText("nombre Jefe");
//			final Label apellidosJefe = new Label(cEliminaGerente2, SWT.None);
//			apellidosJefe.setText("apellidos Jefe");
//			final Label departamentoJefe = new Label(cEliminaGerente2, SWT.None);
//			departamentoJefe.setText("departamento Jefe");
//			final Combo combo = new Combo(cEliminaGerente2, SWT.BORDER
//					| SWT.READ_ONLY);
//			final String[] texto = new String[] { "GERENTE1", "GERENTE2",
//					"GERENTE3" };
//			combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true,
//					0, 0));
//			combo.setItems(texto);
//			combo.select(0);
//		}

		// Creamos los distintos botones
		final Button bEliminar = new Button(cEliminaJefe, SWT.PUSH);
		bEliminar.setText("Eliminar");
		bEliminar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		// Creamos un oyente
		bEliminar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

			}
		});
	}
	
	

}
