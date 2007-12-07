package algoritmo;

import java.util.ArrayList;

/**
 * 
 * @author DavidMartin
 *
 */
/**
 * Esta clase representa el tipo de datos que va a utilizar la clase calendario
 */
public class HoraCalendario {

	/**
	 *maximo numero de empleados por hora
	 */
	private int max;
	/**
	 *minimo numero de empleados por hora
	 */
	private int min;//
	/**
	 *numero de personal experta por hora
	 */
	private int expertos;//
	/**
	 *numero de personal inexperta por hora
	 */
	private int inexpertos;//
	
	public HoraCalendario(){//Constructora por defecto
		max=0;
		min=0;
		expertos=0;
		inexpertos=0;	
	}
	public HoraCalendario(int max,int min,int exp,int inexpert){//Otra constructora
		this.max=max;
		this.min=min;
		this.expertos=exp;
		this.inexpertos=inexpert;	
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
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
