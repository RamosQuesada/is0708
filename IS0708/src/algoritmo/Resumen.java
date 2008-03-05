/**
 * 
 */
package algoritmo;

import java.util.ArrayList;

/**
 * @author Alberto
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
