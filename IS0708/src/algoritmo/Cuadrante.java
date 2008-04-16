package algoritmo;

import java.util.ArrayList;

import aplicacion.utilidades.Util;

/**
 * Esta clase corresponde con la salida del algoritmo, un arraylist de objetos de la clase Trabaja
 * @author DavidMartin & Miguel Angel Diaz
 */
public class Cuadrante {
	
	/**
	 * Mes del cuadrante 
	 */
	private int mes;
	
	/**
	 * Anio del cuadrante
	 */
	private int anio;
	
	/**
	 * Numero de dias del mes del cuadrante
	 */
	private int numDias;
	
	/**
	 * Vector donde en cada posicion hay una lista de trabajas
	 */
	protected ArrayList<Trabaja> cuad[];
	
	/**
	 * Identificador del departamento
	 */
	private String idDepartamento;
	
	/**
	 * Constructora con parametros
	 * @param mes Mes
	 * @param anio Anio
	 * @param idDepartamento Identificador del departamento
	 */
	@SuppressWarnings("unchecked")
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
	
	/**
	 * Consulta del mes
	 * @return mes
	 */
	public int getMes() {
		return mes;
	}

	/**
	 * Modifica el mes
	 * @param mes nuevo
	 */
	public void setMes(int mes) {
		this.mes = mes;
	}

	/**
	 * Consulta del anio
	 * @return anio
	 */
	public int getAnio() {
		return anio;
	}

	/**
	 * Modifica el anio
	 * @param anio Nuevo anio
	 */
	public void setAnio(int anio) {
		this.anio = anio;
	}

	/**
	 * Consulta del numero de dias
	 * @return numero de dias
	 */
	public int getNumDias() {
		return numDias;
	}

	/**
	 * Modifica el numero de dias del mes
	 * @param mes Nuevo mes
	 */
	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}

	/**
	 * Consulta la lista de trabaja
	 * @return Lista de trabaja
	 */
	public ArrayList<Trabaja> getListaTrabajaDia(int dia){
		return cuad[dia];
	}
	
	/**
	 * Inserta trabaja en un dia determinado 
	 * @param dia Dia en el que se debe insertar el nuevo Trabaja
	 * @param trab Nuevo trabaja
	 */
	public void setTrabajaDia(int dia, Trabaja trab){
		cuad[dia].add(trab);
	}
		
	/**
	 * Consulta el cuadrante
	 * @return cuadrante
	 */
	public ArrayList<Trabaja>[] getCuad() {
		return cuad;
	}

	/**
	 * Modifica el cuadrante
	 * @param cuad Nuevo cuadrante
	 */
	public void setCuad(ArrayList<Trabaja>[] cuad) {
		this.cuad = cuad;
	}

	/**
	 * Consulta el id del departamento
	 * @return identificador del departamento
	 */
	public String getIdDepartamento() {
		return idDepartamento;
	}

	/**
	 * Modifica el id del departamento
	 * @param idDepartamento Nuevo id del departamento
	 */
	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	
	/**
	 * Obtiene informacion del cuadrante
	 * @return informacion del cuadrante
	 */
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
	
	/**
	 * Elimina la informacion del cuadrante a partir del dia dado
	 * @param dia Dia desde el que borrar los datos del cuadrante
	 */
	public void eliminaTrabajaDesdeDia(int dia){
		for (int i=dia; i<numDias; i++){
			cuad[i] = new ArrayList<Trabaja>();
		}
	}

}
