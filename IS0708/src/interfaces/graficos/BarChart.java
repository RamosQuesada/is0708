package interfaces.graficos;

import java.awt.image.*;
import java.util.ArrayList;

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
	public BarChart(ArrayList<String> fechas,ArrayList<Double> cantidades){
		super(fechas,cantidades);
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i=0;i<fechas.size();i++){
			dataset.addValue(cantidades.get(i), "Ventas", Integer.toString(i+1)/*fechas.get(i)*/);
		}
		String NumeroVentas = "Numero de Ventas";
		String Ventas = "Ventas realizadas";
		JFreeChart chart = ChartFactory.createBarChart(Ventas, "Fecha",
				NumeroVentas, dataset, PlotOrientation.VERTICAL, false, true,
				false);

        BufferedImage image = chart.createBufferedImage(700,700);
        return image;
    }	
}
