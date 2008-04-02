package algoritmo;

import java.util.ArrayList;

import aplicacion.utilidades.Util;

/**
 * Esta clase corresponde con la salida del algoritmo,un arraylist de objetos de la clase Trabaja
 * @author DavidMartin & Miguel Angel Diaz
 */

public class Cuadrante {
	
	private int mes;
	private int anio;
	private int numDias;			      //numero de dias del mes
	protected ArrayList<Trabaja> cuad[];  //vector donde en cada posicion hay una lista de los empleados que trabajan
	private String idDepartamento;	      //identificador del departamento
	
	/**
	 * Constructora con parámetros
	 * @param mes Mes
	 * @param anio Año
	 * @param idDepartamento Identificador del departamento
	 */
	public Cuadrante (int mes, int anio, String idDepartamento){
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
		
	public ArrayList<Trabaja>[] getCuad() {
		return cuad;
	}

	public void setCuad(ArrayList<Trabaja>[] cuad) {
		this.cuad = cuad;
	}

	public String getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	
	public String toString() {
		String s = "";
		s+= mes + ", " +anio+"\n";
		for (int i=0; i<numDias; i++){
			s+=String.valueOf(i+1)+"\n";
			for (int j=0; j<cuad[i].size(); j++) {
				s+=cuad[i].get(j).getIdEmpl() + " - " +cuad[i].get(j).getIdTurno() +"\n"; 
			}
		}
		return s;
	}

}
