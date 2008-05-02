package interfaces.graficos;

import java.awt.image.*;
import java.util.Date;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import aplicacion.utilidades.Util;
/**
 * Clase que genera graficos de lineas
 * @author Jose María Martín Blázquez
 *
 */
public class TimeSeriesChart extends Chart{
	/**
	 * Indica si los datos que se introducen pertenecen a un año
	 */
	private boolean isAnual;
	
	/**
	 * Constructor de la clase
	 *
	 */
	public TimeSeriesChart(ArrayList<String> fechas,ArrayList<Double> cantidades,boolean anual){
		super(fechas,cantidades);
		this.isAnual=anual;
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
//		 Create a time series chart
		org.jfree.data.time.TimeSeries pop = new org.jfree.data.time.TimeSeries(
				"Linea de Evolución", Day.class);
		if(!isAnual){
		for(int i=0;i<fechas.size();i++){
				Date fecha=new Date(0);
				try {
					fecha=Util.stringADate(fechas.get(i));
				}catch (Exception e) {
					System.out.println("Fecha incorrecta");
					e.printStackTrace();
				}
				int dia=fecha.getDate();
				int mes=fecha.getMonth();
				int año=fecha.getYear()+1900;
				pop.addOrUpdate(new Day(dia,mes+1,año), cantidades.get(i));
			}
		}
		else {
			String tag= fechas.get(0);
			String año = tag.substring(tag.indexOf(" ")+1, tag.length());
			for (int i=0;i<fechas.size();i++){
				pop.addOrUpdate(new Day(1,(i+1),Integer.valueOf(año)), cantidades.get(i));
			}
		}
		//TimeSeriesDataItem it=new TimeSeriesDataItem(null, 1);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(pop);
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Evolución Ventas", "Fecha", "Numero Unidades", dataset,
				true, true, false);

         BufferedImage image = chart.createBufferedImage(700,700);
        return image;
    }
}
