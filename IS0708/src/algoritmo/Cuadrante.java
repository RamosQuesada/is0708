package algoritmo;

import java.util.ArrayList;

/**
 * 
 * @author DavidMartin
 *
 */
/**
 * Esta clase corresponde con la salida del algoritmo,un arraylist de objetos de la clase Trabaja
 */

// FIXME David, si pones los comentarios por trozos, no te lo coge todo en el javadoc
public class Cuadrante {
	
	/**
	 * Mes
	 */
	private int mes;
	/**
	 *Año
	 */
	private int anio;
	/**
	 *Numero de dias del mes
	 */
	private int numDias;
	/**
	 *Formato del cuadrante
	 */
	ArrayList<Trabaja> cuad[];
	
	public Cuadrante(int mes,int dias){//creacion de un cuadrante vacio
		this.mes=mes;
		this.numDias=dias;
		for(int i=0;i<numDias;i++){
			cuad[i]=new ArrayList<Trabaja>();
		}
	
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public int getNumDias() {
		return numDias;
	}

	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}

	public ArrayList<Trabaja>[] getCuad() {
		return cuad;
	}

	public void setCuad(ArrayList<Trabaja>[] cuad) {
		this.cuad = cuad;
	}
	
	

}
