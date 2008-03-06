/**
 * 
 */
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
	
	public Resumen(int dias,Cuadrante cuadrante){
		this.dias=dias;
		Analisis a=new Analisis(dias,cuadrante);
		sugerencias=a.generarResumen();
		informe=a.analizarFaltas();
		
	}
	
	public void add(int dia, String sugerencia){
		sugerencias[dia].add(sugerencia);
	}
	
	public ArrayList<String> leerDia(int dia){
		return sugerencias[dia];
	}
	public ArrayLis<String> getInforme(){
		return informe;
	}

}
