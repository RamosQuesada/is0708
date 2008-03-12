package algoritmo;

import java.util.ArrayList;

/**
 * @author Alberto
 * Esta clase devuelve el resultado del analisis
 * se debe usar asi
 * instanciar un objto de clase Resumen: 
 * Resumen r=new Resumen(dias, cuadrante);
 * y ya leer con el método leerDia(i);
 * 
 */
public class Resumen {
	
	private ArrayList<String>[] sugerencias=null;
	private ArrayList<String> informe=null;
	private int dias;
	
	/**
	 * Constructora por parámetros
	 * @param dias Número de días del mes
	 * @param cuadrante Cuadrante del mes
	 * @param estructura Estructura con la información del departamento
	 */
	public Resumen(int dias, Cuadrante cuadrante, Estructura estructura){
		this.dias=dias;
		Analisis a=new Analisis(dias,cuadrante,estructura);
		this.sugerencias=a.generarResumen();
		this.informe=a.analizarFaltas();
	}
	
	/**
	 * Añade una sugerencia al ArrayList de sugerencias de un día
	 * @param dia Día para el que añadimos la sugerencia
	 * @param sugerencia Sugerencia a añadir
	 */
	public void add(int dia, String sugerencia){
		sugerencias[dia].add(sugerencia);
	}
	
	/**
	 * Devuelve las sugerencias de un día
	 * @param dia Día dek que queremos las sugerencias
	 * @return Sugerencias del día 
	 */
	public ArrayList<String> leerDia(int dia){
		return sugerencias[dia];
	}
	
	/**
	 * Obtiene el informe
	 * @return Informe
	 */
	public ArrayList<String> getInforme(){
		return informe;
	}

}
