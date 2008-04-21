package interfaces.graficos;

import java.awt.image.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
/**
 * Clase que genera graficos de barras
 * @author Jose María Martín Blázquez
 *
 */
public class BarChart extends Chart{
	/**
	 * Constructor de la clase
	 *
	 */
	public BarChart(){
		
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(5, "Ventas", "Enero");
		dataset.setValue(7, "Ventas", "Febrero");
		dataset.setValue(9, "Ventas", "Marzo");
		dataset.setValue(5, "Ventas", "Abril");
		dataset.setValue(10, "Ventas", "Mayo");
		String NumeroVentas = "Numero de Ventas";
		String Ventas = "Ventas realizadas";
		JFreeChart chart = ChartFactory.createBarChart(Ventas, Ventas,
				NumeroVentas, dataset, PlotOrientation.VERTICAL, false, true,
				false);

         BufferedImage image = chart.createBufferedImage(300,300);
        return image;
    }	
}
