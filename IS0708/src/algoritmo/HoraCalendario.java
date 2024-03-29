package algoritmo;

import aplicacion.utilidades.Util;

/**
 * Esta clase contiene el max/min numero de personas, patron de expertos/principiantes e idTurno que se van a utilizar en la clase calendario
 * @author DavidMartin & Miguel Angel Diaz
 */
public class HoraCalendario {

	private int max;
	private int min;
	private int expertos;
	private int principiantes;
	
	/**
	 * Constructora por defecto. Inicializa atributos min y max a 0 y patron a 1
	 */
	public HoraCalendario(){
		max = 0;
		min = 0;
		expertos = 1;
		principiantes = 1;	
	}
	
	/**
	 * Constructora con parámetros
	 * @param max Número máximo de empleados simultáneos en el departamento
	 * @param min Número mínimo de empleados simultáneos en el departamento
	 * @param exp Número de empleados expertos por cada inexperto 
	 * @param inexpert Número de empleados principiantes simultáneos en el departamento
	 */
	public HoraCalendario(int max, int min, int exp, int inexp){
		this.max = max;
		this.min = min;
		this.expertos = exp;
		this.principiantes = inexp;	
	}
	
	/**
	 * Constructora con parámetros
	 * @param max Número máximo de empleados simultáneos en el departamento
	 * @param min Número mínimo de empleados simultáneos en el departamento
	 * @param patron Patrón de empleados "XeYp"
	 */
	public HoraCalendario(int max, int min, String patron){
		this.max = max;
		this.min = min;
		expertos = Util.numExpertos(patron);
		principiantes = Util.numPrincipiantes(patron);	
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
	 * Consulta si el departamento esta cerrado
	 * @return true si min y max son 0
	 */
	public boolean horaLibre(){
		return ((min == 0) && (max == 0));
	}
	
}
