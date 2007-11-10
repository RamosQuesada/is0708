//package paquete_pruebas;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//
//public class PruebaGraficas {
//	//creamos un display y un shell donde emplazar los widgets (elementos swt)
//	final Display display = new Display ();
//	final Shell shell = new Shell (display);
//	/// STEP 1 //////////////////////////////////////////////// 
//	// create a composite suitable for displaying all of our chart data
//	Composite chartComposite = new Composite(shell,SWT.BORDER); 
//	chartComposite.setLayoutData(/* set the layout options */); 
//
//	/// STEP 2 //////////////////////////////////////////////// 
//	// create the dataset for the pie chart of the distribution 
//	DefaultPieDataset pieData = new DefaultPieDataset(); 
//	/* populate the pieData here */ 
//
//	JFreeChart chart = ChartFactory.createPie3DChart(/* some options */); 
//	// OPTIONAL: tinkering with plot options here 
//
//	/// STEP 3 //////////////////////////////////////////////// 
//	// Grab the background color from the SWT Composite 
//	// so our AWT panel "matches" the SWT Composite 
//	Color backgroundColor = chartComposite.getBackground(); 
//
//	// create the panel 
//	Panel chartPanel = SWT_AWT.new_Panel(chartComposite); 
//
//	// set the panel's background (defaults to pure white) 
//	chartPanel.setBackground(new java.awt.Color(backgroundColor.getRed(), 
//	                                                    backgroundColor.getGreen(), 
//	                                                    backgroundColor.getBlue())); 
//	// set the AWT layout manager 
//	chartPanel.setLayout(/* some AWT/Swing layout manager like BoxLayout */); 
//
//	/// STEP 4 ////////////////////////////////////////////////        
//	ChartPanel jfreeChartPanel = new ChartPanel(chart); 
//	chartPanel.add(jfreeChartPanel); 
//}
