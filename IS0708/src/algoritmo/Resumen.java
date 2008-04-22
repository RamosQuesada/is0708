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
	
	private ArrayList<Sugerencia>[] sugerencias=null;
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
	public void add(int dia, Sugerencia sugerencia){
		sugerencias[dia].add(sugerencia);
	}
	
	/**
	 * Devuelve las sugerencias de un día
	 * @param dia Día dek que queremos las sugerencias
	 * @return Sugerencias del día 
	 */
	public ArrayList<Sugerencia> leerDia(int dia){
		/*ArrayList<String> sug = new ArrayList<String>();
		for (int i=0; i<sugerencias[dia].size(); i++)
			sug.add(sugerencias[dia].get(i).getSugerencia());
		return sug;*/
		return sugerencias[dia];
	}
	
	/**
	 * Obtiene el informe
	 * @return Informe
	 */
	public ArrayList<String> getInforme(){
		return informe;
	}
	
	/**
	 * Establece el informe
	 * @param informe informe que se introduce
	 */
	public void setInforme(ArrayList<String> informe) {
		this.informe = informe;
	}

	/**
	 * Devuelve las sugerencias de todos los días del mes
	 */
	public ArrayList<Sugerencia>[] getSugerencias() {
		return sugerencias;
	}
	
	/**
	 * Fija las sugerencias de todo el mes
	 * @param sugerencias
	 */
	public void setSugerencias(ArrayList<Sugerencia>[] sugerencias) {
		this.sugerencias = sugerencias;
	}

	/**
	 * Devuelve los días del mes
	 */
	public int getDias() {
		return dias;
	}

	/**
	 * Fija los dias del mes
	 * @param dias
	 */
	public void setDias(int dias) {
		this.dias = dias;
	}

}
