package algoritmo;

import java.util.ArrayList;

/**
 * Esta clase contiene el max/min numero de personas y patr�n de expertos/principiantes, que se van a utilizar en la clase calendario
 * @author DavidMartin
 */
public class HoraCalendario {

	private int max;
	private int min;
	private int expertos;
	private int principiantes;
	
	/**
	 * Constructora por defecto. Inicializa atributos min y max a 0 y patr�n a 1
	 */
	public HoraCalendario(){
		max=0;
		min=0;
		expertos=1;
		principiantes=1;	
	}
	
	/**
	 * Constructora con par�metros
	 * @param max N�mero m�ximo de empleados simultaneos en el departamento
	 * @param min N�mero m�nimo de empleados simultaneos en el departamento
	 * @param exp N�mero de empleados expertos por cada inexpert 
	 * @param inexpert N�mero de empleados principiantes simultaneos en el departamento
	 */
	public HoraCalendario(int max,int min,int exp,int inexpert){//Otra constructora
		this.max=max;
		this.min=min;
		this.expertos=exp;
		this.principiantes=inexpert;	
	}
	
	/**
	 * Recupera el n�mero m�ximo de empleados simultaneos en el departamento
	 * @return N�mero m�ximo de empleados
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * Cambia el n�mero m�ximo de empleados simultaneos en el departamento
	 * @param max N�mero m�ximo de empleados
	 */
	public void setMax(int max) {
		this.max = max;
	}
	/**
	 * Recupera el n�mero m�nimo de empleados simultaneos en el departamento
	 * @return N�mero m�nimo de empleados
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 * Cambia el n�mero m�nimo de empleados simultaneos en el departamento
	 * @param min N�mero m�nimo de empleados
	 */
	public void setMin(int min) {
		this.min = min;
	}
	
	/**
	 * Consulta el n�mero de expertos del patr�n
	 * @return
	 */
	public int getExpertos() {
		return expertos;
	}
	
	/**
	 * Modifica el n�mero de expertos del patr�n
	 * @param expertos
	 */
	public void setExpertos(int expertos) {
		this.expertos = expertos;
	}
	
	/**
	 * Consulta el n�mero de principiantes del patr�n
	 * @return principiantes
	 */
	public int getPrincipiantes() {
		return principiantes;
	}
	
	/**
	 * Modifica el n�mero de principiantes del patr�n
	 * @param principiantes
	 */
	public void setPrincipiantes(int principiantes) {
		this.principiantes = principiantes;
	}
	
}
