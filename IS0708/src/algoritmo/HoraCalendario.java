package algoritmo;

import java.util.ArrayList;

/**
 * Esta clase contiene el max/min numero de personas y numero de expertos/inexpertos,que se van a utilizar en la clase calendario
 * @author DavidMartin
 */
public class HoraCalendario {

	private int max;
	private int min;//
	private int expertos;//
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
