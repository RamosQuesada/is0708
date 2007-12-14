package algoritmo;

import aplicacion.Util;

/**
 * Esta clase contiene el max/min numero de personas,patron de expertos/principiantes e idTurno que se van a utilizar en la clase calendario
 * @author DavidMartin
 */
public class HoraCalendario {

	private int max;
	private int min;
	private int expertos;
	private int principiantes;
	private String idTurno;
	
	/**
	 * Constructora por defecto. Inicializa atributos min y max a 0 y patron a 1
	 */
	public HoraCalendario(){
		max = 0;
		min = 0;
		expertos = 1;
		principiantes = 1;	
		idTurno="";
	}
	
	/**
	 * Constructora con parametros
	 * @param max Numero maximo de empleados simultaneos en el departamento
	 * @param min Numero minimo de empleados simultaneos en el departamento
	 * @param exp Numero de empleados expertos por cada inexpert 
	 * @param inexpert Numero de empleados principiantes simultaneos en el departamento
	 * @param idturno Especifica el turno del trabajador
	 */
	public HoraCalendario(int max,int min,int exp,int inexpert,String turno){//Otra constructora
		this.max = max;
		this.min = min;
		this.expertos = exp;
		this.principiantes = inexpert;	
		this.idTurno=turno;
	}
	
	/**
	 * Constructora con parametros
	 * @param max Numero maximo de empleados simultaneos en el departamento
	 * @param min Numero minimo de empleados simultaneos en el departamento
	 * @param patron Patron de empleados "XeYp"
	 */
	public HoraCalendario(int max,int min,String patron,String turno){//Otra constructora
		this.max = max;
		this.min = min;
		expertos = Util.numExpertos(patron);
		principiantes=Util.numPrincipiantes(patron);	
		this.idTurno=turno;
	}
	
	/**
	 * Recupera el numero maximo de empleados simultaneos en el departamento
	 * @return Numero maximo de empleados
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * Cambia el numero maximo de empleados simultaneos en el departamento
	 * @param max Numero maximo de empleados
	 */
	public void setMax(int max) {
		this.max = max;
	}
	/**
	 * Recupera el numero minimo de empleados simultaneos en el departamento
	 * @return Numero minimo de empleados
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 * Cambia el numero minimo de empleados simultaneos en el departamento
	 * @param min Numero minimo de empleados
	 */
	public void setMin(int min) {
		this.min = min;
	}
	
	/**
	 * Consulta el numero de expertos del patron
	 * @return
	 */
	public int getExpertos() {
		return expertos;
	}
	
	/**
	 * Modifica el numero de expertos del patron
	 * @param expertos
	 */
	public void setExpertos(int expertos) {
		this.expertos = expertos;
	}
	
	/**
	 * Consulta el numero de principiantes del patron
	 * @return principiantes
	 */
	public int getPrincipiantes() {
		return principiantes;
	}
	
	/**
	 * Modifica el numero de principiantes del patron
	 * @param principiantes
	 */
	public void setPrincipiantes(int principiantes) {
		this.principiantes = principiantes;
	}
	
	/**
	 * Consulta el patron "XeYp"
	 * @return patron
	 */
	public String getPatron(){
		return Util.patron(expertos, principiantes);
	}
	
	/**
	 * Modifica el patron "XeYp"
	 * @param patron
	 */
	public void setPatron(String patron){
		expertos = Util.numExpertos(patron);
		principiantes = Util.numPrincipiantes(patron);
	}

	/**
	 * Consulta idTurno
	 * @return idTurno
	 */
	public String getIdTurno() {
		return idTurno;
	}

	/**
	 * Modifica el Turno
	 * @param idTurno
	 */
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}
	
}
