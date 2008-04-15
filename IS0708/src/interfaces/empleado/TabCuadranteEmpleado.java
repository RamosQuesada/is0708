package interfaces.empleado;

import java.sql.Date;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import aplicacion.Vista;
import aplicacion.datos.Empleado;
import aplicacion.utilidades.Util;

public class TabCuadranteEmpleado {
	private Date fechaSeleccionada;
	final Vista vista;
	final ResourceBundle bundle;
	final TabFolder tabFolder;
	
	public TabCuadranteEmpleado(TabFolder tabFolder, Vista vista, ResourceBundle bundle) {
		this.tabFolder = tabFolder;
		this.vista = vista;
		this.bundle = bundle;
		crearTabEmpleadoCuadrantes();
	}
	
	/**
	 * Crea un tab de empleado para mostrar el cuadrante
	 * 
	 * @param tabFolder
	 */
	private void crearTabEmpleadoCuadrantes() {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Empleado:Cuadrantes");
		tabItem.setImage(vista.getImagenes().getIco_chico());

		// Creamos el contenido de la pestaña cuadrantes
		Composite cCuadrantes = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(cCuadrantes);

		cCuadrantes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		// Le añadimos un layout
		GridLayout lCuadrantes = new GridLayout();
		lCuadrantes.numColumns = 2;
		cCuadrantes.setLayout(lCuadrantes);

		// Creamos el contenido interno de la pestaña cuadrantes
		// Creamos un composite para los botones
		final Composite cBotones = new Composite(cCuadrantes, SWT.BORDER);
		cBotones.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));
		GridLayout lCBotones = new GridLayout();
		lCBotones.numColumns = 1;
		cBotones.setLayout(lCBotones);

		// Creamos un composite para el calendario
		final Label lCalendario = new Label(cBotones, SWT.LEFT);
		lCalendario.setText(this.bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));

		// Creamos un composite para la zona donde se mostrara el calendario
		final Composite cCuadrantesDer = new Composite(cCuadrantes, SWT.BORDER);
		cCuadrantesDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		GridLayout lCuadrantesDer = new GridLayout();
		lCuadrantesDer.numColumns = 1;
		cCuadrantesDer.setLayout(lCuadrantesDer);
		// final Label lCuadr1=new Label (cCuadrantesDer, SWT.CENTER);
		// lCuadr1.setText("Aquí se mostrarían los cuadrantes");
		Empleado empleado = this.vista.getEmpleadoActual();
		final Cuadrantes cuadrante = new Cuadrantes(cCuadrantesDer,
				false, bundle, empleado, fechaSeleccionada, vista);
		cuadrante.setSemanal();

		// Creamos el calendario
		final DateTime calendario = new DateTime(cBotones, SWT.CALENDAR);
		calendario.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int day = calendario.getDay();
				int month = calendario.getMonth();
				int year = calendario.getYear();
				fechaSeleccionada = Date.valueOf(Util.aFormatoDate(Integer
						.toString(year), Integer.toString(month + 1), Integer
						.toString(day)));
				cuadrante.actualizaFecha(fechaSeleccionada);
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		// Creamos los botones para ver el horario por dias o semanas
		final Button bPorSemanas = new Button(cBotones, SWT.RADIO);
		bPorSemanas.setText(this.bundle.getString("Verporsemana"));
		bPorSemanas.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				2, 1));
		bPorSemanas.setSelection(true);
		bPorSemanas.addListener(SWT.Selection, new Listener() {
			// Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorSemanas.getSelection()) {
					cuadrante.setSemanal();
				} else{
				//	bPorSemanas.setFocus();
				}
				//	cuadrante.setMensual();

			}
		});

		// Creamos un boton para la seleccion del horario por semanas
		final Button bPorMes = new Button(cBotones, SWT.RADIO);
		bPorMes.setText(this.bundle.getString("I02_but_Verpormes"));
		bPorMes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1,
				1));
		// Creamos un oyente
		bPorMes.addListener(SWT.Selection, new Listener() {
			// Seleccionado por mes
			public void handleEvent(Event e) {
				if (bPorMes.getSelection()) {
				
					cuadrante.setMensual();
					//crear ventana informando
					/*
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					messageBox.setText(bundle.getString("Mensaje"));
					// Diferentes iconos:
					messageBox.setMessage("Vista por meses, aun no creada");
					if (messageBox.open() == SWT.OK) {
						
						bPorSemanas.setFocus();
					}*/
				
					
					
				} else{
					
				}
				//	cuadrante.setSemanal();

			}
		});
	}
}
