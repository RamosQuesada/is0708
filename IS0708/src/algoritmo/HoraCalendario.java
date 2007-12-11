package algoritmo;

import java.util.ArrayList;

/**
 * Esta clase contiene el max/min numero de personas y patrón de expertos/inexpertos, que se van a utilizar en la clase calendario
 * @author DavidMartin
 */
public class HoraCalendario {

	private int max;
	private int min;
	private int expertos;
	private int inexpertos;
	
	/**
	 * Constructora por defecto. Inicializa atributos min y max a 0 y patrón a 1
	 */
	public HoraCalendario(){
		max=0;
		min=0;
		expertos=1;
		inexpertos=1;	
	}
	
	/**
	 * Constructora con parámetros
	 * @param max Número máximo de empleados simultaneos en el departamento
	 * @param min Número mínimo de empleados simultaneos en el departamento
	 * @param exp Número de empleados expertos por cada inexpert 
	 * @param inexpert Número de empleados inexpertos simultaneos en el departamento
	 */
	public HoraCalendario(int max,int min,int exp,int inexpert){//Otra constructora
		this.max=max;
		this.min=min;
		this.expertos=exp;
		this.inexpertos=inexpert;	
	}
	
	/**
	 * Recupera el número máximo de empleados simultaneos en el departamento
	 * @return Número máximo de empleados
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * Cambia el número máximo de empleados simultaneos en el departamento
	 * @param max Número máximo de empleados
	 */
	public void setMax(int max) {
		this.max = max;
	}
	/**
	 * Recupera el número mínimo de empleados simultaneos en el departamento
	 * @return Número mínimo de empleados
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 * Cambia el número mínimo de empleados simultaneos en el departamento
	 * @param min Número mínimo de empleados
	 */
	public void setMin(int min) {
		this.min = min;
	}
	
	/**
	 * Consulta el número de expertos del patrón
	 * @return
	 */
	public int getExpertos() {
		return expertos;
	}
	
	public void setExpertos(int expertos) {
		this.expertos = expertos;
	}
	
	public int getInexpertos() {
		return inexpertos;
	}
	
	public void setInexpertos(int inexpertos) {
		this.inexpertos = inexpertos;
	}
	
}
