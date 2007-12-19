package algoritmo;

import java.util.ArrayList;
import aplicacion.Util;

/**
 * Esta clase corresponde con la salida del algoritmo,un arraylist de objetos de la clase Trabaja
 * @author DavidMartin & Miguel Angel Diaz
 */

public class Cuadrante {
	
	private int mes;
	private int anio;
	private int numDias;			//Numero de dias del mes
	ArrayList<Trabaja> cuad[];		//Esta matriz seria la salida del algoritmo,un vector donde en cada posicion hay una lista de los empleados que trabajan
	private String idDepartamento;	//Identificador del departamento;
	
	public Cuadrante(int mes,int anio,String idDepartamento){//creacion de un cuadrante vacio
		this.mes = mes;
		this.numDias = Util.dameDias(mes,anio);
		this.cuad = new ArrayList[numDias];
		this.anio = anio;
		this.idDepartamento = idDepartamento;
		for(int i=0; i<numDias; i++){
			cuad[i] = new ArrayList<Trabaja>();
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

	public ArrayList<Trabaja> getListaTrabajaDia(int dia){
		return cuad[dia];
	}
	
	public void setTrabajaDia(int dia, Trabaja trab){
		cuad[dia].add(trab);
	}
		
	/**
	 * 
	 * @return
	 * no nos gusta la ocultacion
	 */
	public ArrayList<Trabaja>[] getCuad() {
		return cuad;
	}

	/**
	 * 
	 * @param cuad
	 * no nos gusta la ocultacion
	 */
	public void setCuad(ArrayList<Trabaja>[] cuad) {
		this.cuad = cuad;
	}

	public String getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	
	

}
