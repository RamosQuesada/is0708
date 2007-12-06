package algoritmo;

import java.util.ArrayList;

/**
 * 
 * @author DavidMartin
 *
 */
/**
 * Esta clase corresponde con la salida del algoritmo,un arraylist de objetos de la Trabaja
 */

// FIXME David, si pones los comentarios por trozos, no te lo coge todo en el javadoc
public class Cuadrante {
	
	private int mes;
	private int anio;
	private int numDias;
	ArrayList<Trabaja> cuad[];
	
	public Cuadrante(int mes,int dias){//creacion de un cuadrante vacio
		this.mes=mes;
		this.numDias=dias;
		for(int i=0;i<numDias;i++){
			cuad[i]=new ArrayList<Trabaja>();
		}
	
	}
	

}
