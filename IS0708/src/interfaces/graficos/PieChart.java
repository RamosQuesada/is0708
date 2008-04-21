package interfaces.graficos;

import java.awt.image.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
/**
 * Clase que genera graficos de sectores
 * @author José María Martín Blázquez
 *
 */
public class PieChart extends Chart{
	/**
	 * Constructor de la clase
	 *
	 */
	public PieChart(){
		
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Enero", new Integer(75));
		pieDataset.setValue("Febrero", new Integer(10));
		pieDataset.setValue("Marzo", new Integer(10));
		pieDataset.setValue("Resto", new Integer(5));
		JFreeChart chart = ChartFactory.createPieChart("Ventas realizadas",
				pieDataset, true, true, false);

         BufferedImage image = chart.createBufferedImage(300,300);
        return image;
    }	
}
