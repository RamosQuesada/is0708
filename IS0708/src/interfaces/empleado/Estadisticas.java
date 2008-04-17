package interfaces.empleado;

import interfaces.graficos.BarChart;
import interfaces.graficos.PieChart;
import interfaces.graficos.TimeSeriesChart;
import interfaces.graficos.XYChart;
import java.util.ResourceBundle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


import aplicacion.Vista;

public class Estadisticas {
	
	private ResourceBundle bundle;
	private Vista vista;
	private TabFolder tabFolder;
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
