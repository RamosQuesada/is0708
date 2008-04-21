package interfaces.graficos;

import java.awt.image.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;
/**
 * Clase que genera graficos de lineas
 * @author Jose María Martín Blázquez
 *
 */
public class TimeSeriesChart extends Chart{
	/**
	 * Constructor de la clase
	 *
	 */
	public TimeSeriesChart(){
		
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
//		 Create a time series chart
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
				"Crecimiento Ventas", "Fecha", "Numero Unidades", dataset,
				true, true, false);

         BufferedImage image = chart.createBufferedImage(300,300);
        return image;
    }
}
