package interfaces.graficos;

import java.awt.image.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
	public XYChart(){
		
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
//		 Create a simple XY chart
		XYSeries series = new XYSeries("Grafico XY");
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
				"Meses", // x-axis Label
				"Ventas", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);

         BufferedImage image = chart.createBufferedImage(300,300);
        return image;
    }	
}