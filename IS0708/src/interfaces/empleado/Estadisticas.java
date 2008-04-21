package interfaces.empleado;

import interfaces.graficos.BarChart;
import interfaces.graficos.PieChart;
import interfaces.graficos.TimeSeriesChart;
import interfaces.graficos.XYChart;

import java.sql.Date;
import java.util.ResourceBundle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


import aplicacion.Vista;
import aplicacion.utilidades.Util;

public class Estadisticas {
	
	private ResourceBundle bundle;
	private Vista vista;
	private TabFolder tabFolder;
	private Date fechaSeleccionada;
	private Table tablaVentas;
	
	public Estadisticas(final ResourceBundle bundle, final Vista vista,
			final TabFolder tabFolder) {
		this.bundle = bundle;
		this.vista = vista;
		this.tabFolder = tabFolder;
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(bundle.getString("I07_tab_estadisticas_emp"));
		tabItem.setImage(vista.getImagenes().getGrafica());

		// Creamos el contenido de la pestaña estadisticas
		final Composite cEstadisticas = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(cEstadisticas);

		cEstadisticas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		// Le añadimos un layout
		GridLayout lEstadisticas = new GridLayout();
		lEstadisticas.numColumns = 2;
		cEstadisticas.setLayout(lEstadisticas);
		
		// Creamos un composite para los botones
		final Composite cEstIzq = new Composite(cEstadisticas, SWT.BORDER);
		cEstIzq.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,
				1));
		GridLayout lEstIzq = new GridLayout();
		lEstIzq.numColumns = 1;
		lEstIzq.makeColumnsEqualWidth = true;
		cEstIzq.setLayout(lEstIzq);

		final Composite cEstDer = new Composite(cEstadisticas, SWT.BORDER);		
		cEstDer.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true, true, 1, 1));
		cEstDer.setLayout(new GridLayout(1, true));
		
		tablaVentas = new Table(cEstDer, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tablaVentas.setLinesVisible(true);
		tablaVentas.setHeaderVisible(true);
		String[] titles = { bundle.getString("Fecha"),
				bundle.getString("Ventas")};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(tablaVentas, SWT.NONE);
			column.setText(titles[i]);
		}

		for (int i = 0; i < titles.length; i++) {
			tablaVentas.getColumn(i).pack();
		}
		tablaVentas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		tablaVentas.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				// // Configurar tamaño de las columnas
				int ancho = tablaVentas.getSize().x;
				tablaVentas.getColumn(0).setWidth(ancho / 10 * 5);
				tablaVentas.getColumn(1).setWidth(ancho / 10 * 5);
			}

			public void controlMoved(ControlEvent arg0) {
			}

		});
		
		final Label lCalendario = new Label(cEstIzq, SWT.LEFT);
		lCalendario.setText(this.bundle.getString("Calendario"));
		lCalendario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		
		// Creamos el calendario
		final DateTime calendario = new DateTime(cEstIzq, SWT.CALENDAR);
		calendario.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int day = calendario.getDay();
				int month = calendario.getMonth();
				int year = calendario.getYear();
				fechaSeleccionada = Date.valueOf(Util.aFormatoDate(Integer
						.toString(year), Integer.toString(month + 1), Integer
						.toString(day)));
				System.out.println(fechaSeleccionada);
			}
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		final Label lTitulo = new Label(cEstIzq, SWT.CENTER);
		lTitulo.setText(this.bundle.getString("opcionvis"));
		lTitulo.setFont(new org.eclipse.swt.graphics.Font(
				org.eclipse.swt.widgets.Display.getDefault(), "Arial", 10,
				org.eclipse.swt.SWT.BOLD));
		lTitulo
				.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
						1));

		final Label lTiempo = new Label(cEstIzq, SWT.LEFT);
		lTiempo.setText(this.bundle.getString("tiempodatos"));
		lTiempo
				.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
						1));
		Combo cTiempo = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
		cTiempo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		cTiempo.setItems(new String[] { bundle.getString("semana"),
				bundle.getString("quincena"), bundle.getString("mes"),
				bundle.getString("ano") });
		cTiempo.select(0);

		final Label lTipoGrafico = new Label(cEstIzq, SWT.CENTER);
		lTipoGrafico.setText(bundle.getString("datosvis"));
		lTipoGrafico.setFont(new org.eclipse.swt.graphics.Font(
				org.eclipse.swt.widgets.Display.getDefault(), "Arial", 9,
				org.eclipse.swt.SWT.BOLD));
		lTipoGrafico.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false,
				1, 1));
		
		final Button bGraficaBarras = new Button(cEstIzq, SWT.PUSH);
		bGraficaBarras.setText(bundle.getString("I07_graf_barras"));
		bGraficaBarras.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		
		// Creamos un oyente
		bGraficaBarras.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				BarChart a=new BarChart();
				a.creaVentana();
				
			}
		});
		
		final Button bGraficaLineas = new Button(cEstIzq, SWT.PUSH);
		bGraficaLineas.setText(bundle.getString("I07_graf_lineas"));
		bGraficaLineas.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		
		// Creamos un oyente
		bGraficaLineas.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				TimeSeriesChart a=new TimeSeriesChart();
				a.creaVentana();
				
			}
		});
		
		final Button bGraficaSectores = new Button(cEstIzq, SWT.PUSH);
		bGraficaSectores.setText(bundle.getString("I07_graf_sectores"));
		bGraficaSectores.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		
		// Creamos un oyente
		bGraficaSectores.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {				
				PieChart a=new PieChart();
				a.creaVentana();
				
			}
		});
		
		final Button bGraficaXY = new Button(cEstIzq, SWT.PUSH);
		bGraficaXY.setText("Grafico XY");
		bGraficaXY.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		
		// Creamos un oyente
		bGraficaXY.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				XYChart a=new XYChart();
				a.creaVentana();
				
			}
		});

	}
	

}
