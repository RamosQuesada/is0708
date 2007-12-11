package algoritmo;

import java.util.ArrayList;

/**
 * Esta clase contiene el max/min numero de personas y patr�n de expertos/inexpertos, que se van a utilizar en la clase calendario
 * @author DavidMartin
 */
public class HoraCalendario {

	private int max;
	private int min;
	private int expertos;
	private int inexpertos;
	
	/**
	 * Constructora por defecto. Inicializa atributos min y max a 0 y patr�n a 1
	 */
	public HoraCalendario(){
		max=0;
		min=0;
		expertos=1;
		inexpertos=1;	
	}
	
	/**
	 * Constructora con par�metros
	 * @param max N�mero m�ximo de empleados simultaneos en el departamento
	 * @param min N�mero m�nimo de empleados simultaneos en el departamento
	 * @param exp N�mero de empleados expertos por cada inexpert 
	 * @param inexpert N�mero de empleados inexpertos simultaneos en el departamento
	 */
	public HoraCalendario(int max,int min,int exp,int inexpert){//Otra constructora
		this.max=max;
		this.min=min;
		this.expertos=exp;
		this.inexpertos=inexpert;	
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
