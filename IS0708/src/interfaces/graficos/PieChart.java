package interfaces.graficos;

import java.awt.image.*;
import java.util.ArrayList;

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
	public PieChart(ArrayList<String> fechas,ArrayList<Integer> cantidades){
		super(fechas,cantidades);
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for(int i=0;i<fechas.size();i++){
			pieDataset.setValue(fechas.get(i),cantidades.get(i));
		}
		JFreeChart chart = ChartFactory.createPieChart("Ventas realizadas",
				pieDataset, true, true, false);

         BufferedImage image = chart.createBufferedImage(700,700);
        return image;
    }	
}
