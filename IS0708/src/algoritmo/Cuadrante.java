package algoritmo;

import java.util.ArrayList;

/**
 * Esta clase corresponde con la salida del algoritmo,un arraylist de objetos de la clase Trabaja
 * @author DavidMartin
 */

public class Cuadrante {
	
	private int mes;
	private int anio;
	private int numDias;//numero de dias del mes
	ArrayList<Trabaja> cuad[];//esta matriz seria la salida del algoritmo,un vector donde en cada posicion hay una lista de los empleados que trabajan
	
	public Cuadrante(int mes,int dias){//creacion de un cuadrante vacio
		this.mes=mes;
		this.numDias=dias;
		this.cuad = new ArrayList[numDias];
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
