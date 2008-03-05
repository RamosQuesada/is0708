/**
 * 
 */
package algoritmo;

import java.util.ArrayList;

/**
 * @author Alberto
 * Esta clase devuelve el resultado del analisis
 * se debe usar asi
 * instanciar una clase analisis: 
 * Analisis a =new Analisis(31,cuadrante);
 * Resumen r=a.generarResumen();
 * y ya usar el objeto Resumen
 * 
 */
public class Resumen {
	
	private ArrayList<String>[] sugerencias;
	private int dias;
	
	public Resumen(int dias){
		this.dias=dias;
		for (int i=0;i<dias;i++){
			sugerencias[i]=new ArrayList<String>();			
		}
	}
	
	public void add(int dia, String sugerencia){
		sugerencias[dia].add(sugerencia);
	}
	
	public ArrayList<String> leerDia(int dia){
		return sugerencias[dia];
	}

}
