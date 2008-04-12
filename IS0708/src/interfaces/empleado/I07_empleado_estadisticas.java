package interfaces.empleado;

import interfaces.imagenes.CargadorImagenes;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import aplicacion.Vista;

public class I07_empleado_estadisticas {
	
	private ResourceBundle bundle;
	private Vista vista;
	private TabFolder tabFolder;
	private Label lFondo;
	public I07_empleado_estadisticas(final ResourceBundle bundle, final Vista vista,
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

//		final Label lComparar = new Label(cEstIzq, SWT.LEFT);
//		lComparar.setText(bundle.getString("compararcon"));
//		lComparar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
//				1));
//		Combo cComparar = new Combo(cEstIzq, SWT.BORDER | SWT.READ_ONLY);
//		cComparar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
//				1, 1));
//		cComparar.setItems(new String[] { bundle.getString("nadie"),
//				bundle.getString("empleadomedio"),
//				bundle.getString("mejorsemana"), bundle.getString("mejormes"),
//				bundle.getString("mejorano") });
//
//		cComparar.select(0);
//		cComparar.setVisibleItemCount(6);

		final Label lTipoGrafico = new Label(cEstIzq, SWT.CENTER);
		lTipoGrafico.setText(bundle.getString("datosvis"));
		lTipoGrafico.setFont(new org.eclipse.swt.graphics.Font(
				org.eclipse.swt.widgets.Display.getDefault(), "Arial", 9,
				org.eclipse.swt.SWT.BOLD));
		lTipoGrafico.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false,
				1, 1));

//		final Button bVentasTotales = new Button(cEstIzq, SWT.RADIO);
//		bVentasTotales.setText(bundle.getString("verventastot"));
//		bVentasTotales.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
//				false, 1, 1));
//		// Creamos un oyente
//		bVentasTotales.addFocusListener(new FocusListener() {
//			// Seleccionado por semanas
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas totales in");
//
//			}
//
//			// No seleccionado por semanas
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas totales out");
//			}
//		});
//	//
//		final Button bVentasPTiempo = new Button(cEstIzq, SWT.RADIO);
//		bVentasPTiempo.setText(this.bundle.getString("ventaspertime"));
//		bVentasPTiempo.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
//				false, 1, 1));
//		// Creamos un oyente
//		bVentasPTiempo.addFocusListener(new FocusListener() {
//			// Seleccionado por semanas
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas por tiempo de trabajo in");
//
//			}
//
//			// No seleccionado por semanas
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas por tiempo de trabajo out");
//			}
//		});
//
//		final Button bVentasPPrecio = new Button(cEstIzq, SWT.RADIO);
//		bVentasPPrecio.setText(this.bundle.getString("ventasporprecio"));
//		bVentasPPrecio.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
//				false, 1, 1));
//		// Creamos un oyente
//		bVentasPPrecio.addFocusListener(new FocusListener() {
//			// Seleccionado por semanas
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas por precio producto in");
//
//			}
//
//			// No seleccionado por semanas
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas por precio producto out");
//			}
//		});
//
//		final Button bVentasPDepartamento = new Button(cEstIzq, SWT.RADIO);
//		bVentasPDepartamento.setText(this.bundle.getString("ventaspordpto"));
//		bVentasPDepartamento.setLayoutData(new GridData(SWT.LEFT, SWT.FILL,
//				false, false, 1, 1));
//		// Creamos un oyente
//		bVentasPDepartamento.addFocusListener(new FocusListener() {
//			// Seleccionado por semanas
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas por departamento in");
//
//			}
//
//			// No seleccionado por semanas
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("Ventas por departamento out");
//			}
//		});

//		final Label lPrueba2 = new Label(cEstDer, SWT.SIMPLE);
//		lPrueba2.setText("Aqui se visualizarian las graficas");
//		lPrueba2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1,
//				1));
		final Button bGraficaBarras = new Button(cEstIzq, SWT.PUSH);
		bGraficaBarras.setText(bundle.getString("I07_graf_barras"));
		bGraficaBarras.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		
		// Creamos un oyente
		bGraficaBarras.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				dataset.setValue(5, "Compras", "Enero");
				dataset.setValue(7, "Compras", "Febrero");
				dataset.setValue(9, "Compras", "Marzo");
				dataset.setValue(5, "Compras", "Abril");
				dataset.setValue(10, "Compras", "Mayo");
				String NumeroCompras = "Numero de compras";
				String compras = "Compras realizadas";
				JFreeChart chart = ChartFactory.createBarChart(compras, compras,
						NumeroCompras, dataset, PlotOrientation.VERTICAL, false, true,
						false);
				try {
					File f = new File("src/interfaces/imagenes/Barchart.png");
					ChartUtilities.saveChartAsJPEG(f, chart, 500, 300);
					//Image grafica = new CargadorImagenes(display).getGrafica();
//					final Label lFondo = new Label(cEstDer, SWT.NONE);
//					lFondo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,1));
					lFondo.setImage(new Image(tabFolder.getDisplay(), CargadorImagenes.class.getResourceAsStream("Barchart.png")));
				} catch (IOException e) {
					System.err.println("Error creando grafico.");
				}
				
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
//				 Create a time series chart
				org.jfree.data.time.TimeSeries pop = new org.jfree.data.time.TimeSeries(
						"Linea de Crecimiento", Day.class);
				pop.add(new Day(2, 1, 2007), 100);
				pop.add(new Day(2, 2, 2007), 150);
				pop.add(new Day(2, 3, 2007), 200);
				pop.add(new Day(2, 4, 2007), 250);
				pop.add(new Day(2, 5, 2007), 300);
				pop.add(new Day(2, 6, 2007), 1500);
				TimeSeriesCollection dataset = new TimeSeriesCollection();
				dataset.addSeries(pop);
				JFreeChart chart = ChartFactory.createTimeSeriesChart(
						"Crecimiento Ubuntu", "Fecha", "Numero Usuarios", dataset,
						true, true, false);
				try {
					File f = new File("src/interfaces/imagenes/TimeSeries.png");
					ChartUtilities.saveChartAsJPEG(f, chart, 500, 300);
					//Image grafica = new CargadorImagenes(display).getGrafica();
//					final Label lFondo = new Label(cEstDer, SWT.NONE);
//					lFondo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,1));
					lFondo.setImage(new Image(tabFolder.getDisplay(), CargadorImagenes.class.getResourceAsStream("TimeSeries.png")));
				} catch (IOException e) {
					System.err.println("Error creando grafico.");
				}
				
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
//				 //		 Create a simple pie chart
				DefaultPieDataset pieDataset = new DefaultPieDataset();
				pieDataset.setValue("Ubuntu", new Integer(75));
				pieDataset.setValue("Xubuntu", new Integer(10));
				pieDataset.setValue("Kubuntu", new Integer(10));
				pieDataset.setValue("Otros", new Integer(5));
				JFreeChart chart = ChartFactory.createPieChart("Sistemas Operativos",
						pieDataset, true, true, false);
				try {
					File f = new File("src/interfaces/imagenes/PieChart.png");
					ChartUtilities.saveChartAsJPEG(f, chart, 500, 300);
					//Image grafica = new CargadorImagenes(display).getGrafica();
//					final Label lFondo = new Label(cEstDer, SWT.NONE);
//					lFondo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,1));
					lFondo.setImage(new Image(tabFolder.getDisplay(), CargadorImagenes.class.getResourceAsStream("PieChart.png")));
				} catch (Exception e) {
					System.out.println("Error creando grafico.");
				}
				
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
//				 Create a simple XY chart
				XYSeries series = new XYSeries(bundle.getString("I07_graf_xy"));
				series.add(1, 10);
				series.add(2, 20);
				series.add(3, 10);
				series.add(4, 30);
				series.add(5, 40);
				// Add the series to your data set
				XYSeriesCollection dataset = new XYSeriesCollection();
				dataset.addSeries(series);
				// Generate the graph
				// JFreeChart chart = ChartFactory.createXYLineChart(”Crecimiento
				// Ubuntu”, // Title
				JFreeChart chart = ChartFactory.createXYAreaChart("XY Chart", // Title
						"Tiempo", // x-axis Label
						"Usuarios", // y-axis Label
						dataset, // Dataset
						PlotOrientation.VERTICAL, // Plot Orientation
						true, // Show Legend
						true, // Use tooltips
						false // Configure chart to generate URLs?
						);
				try {
					File f = new File("src/interfaces/imagenes/XYChart.png");
					ChartUtilities.saveChartAsJPEG(f, chart, 500, 300);
					//Image grafica = new CargadorImagenes(display).getGrafica();
//					final Label lFondo = new Label(cEstDer, SWT.NONE);
//					lFondo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,1));
					lFondo.setImage(new Image(tabFolder.getDisplay(), CargadorImagenes.class.getResourceAsStream("XYChart.png")));
				} catch (IOException e) {
					System.err.println("Error creando grafico.");
				}
				
			}
		});
		
		lFondo = new Label(cEstDer, SWT.NONE);
		lFondo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,1));
		lFondo.setImage(new Image(tabFolder.getDisplay(), CargadorImagenes.class.getResourceAsStream("Barchart.png")));

	}
	

}
