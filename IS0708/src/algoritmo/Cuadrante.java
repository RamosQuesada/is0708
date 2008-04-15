package algoritmo;

import java.util.ArrayList;

import aplicacion.utilidades.Util;

/**
 * Esta clase corresponde con la salida del algoritmo,un arraylist de objetos de la clase Trabaja
 * @author DavidMartin & Miguel Angel Diaz
 */
public class Cuadrante {
	
	/**
	 * Mes del cuadrante 
	 */
	private int mes;
	
	/**
	 * Año del cuadrante
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
	 * Constructora con parámetros
	 * @param mes Mes
	 * @param anio Año
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
	 * Consulta del año
	 * @return año
	 */
	public int getAnio() {
		return anio;
	}

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

	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}

	/**
	 * Consulta la lista de trabaja
	 * @return lista de trabaja
	 */
	public ArrayList<Trabaja> getListaTrabajaDia(int dia){
		return cuad[dia];
	}
	
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

	public void setCuad(ArrayList<Trabaja>[] cuad) {
		this.cuad = cuad;
	}

	/**
	 * Consulta el departamento
	 * @return identificador del departamento
	 */
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
	
	public void eliminaTrabajaDesdeDia(int dia){
		for (int i=dia; i<numDias; i++){
			cuad[i] = new ArrayList<Trabaja>();
		}
	}

}
