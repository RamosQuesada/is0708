package interfaces.graficos;

import java.awt.image.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Day;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import aplicacion.utilidades.Util;
/**
 * Clase que genera graficos XY
 * @author Jose María Martín Blázquez
 *
 */
public class XYChart extends Chart{
	/**
	 * Constructor de la clase
	 *
	 */
	public XYChart(ArrayList<String> fechas,ArrayList<Integer> cantidades){
		super(fechas,cantidades);
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
//		 Create a simple XY chart
		XYSeries series = new XYSeries("Grafico XY");
		for(int i=0;i<fechas.size();i++){
			Date fecha=new Date(0);
			try {
				fecha=Util.stringADate(fechas.get(i));
			} catch (Exception e) {
				System.out.println("Fecha incorecta");
				e.printStackTrace();
			}
//			GregorianCalendar calendar=new GregorianCalendar();
//			calendar.
			series.add(fecha.getDate(), cantidades.get(i));
		}
		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		// Generate the graph
		// JFreeChart chart = ChartFactory.createXYLineChart(”Crecimiento
		// Ubuntu”, // Title
		JFreeChart chart = ChartFactory.createXYAreaChart("Evolución Ventas", // Title
				"Fecha", // x-axis Label
				"Ventas", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);

         BufferedImage image = chart.createBufferedImage(700,700);
        return image;
    }	
}