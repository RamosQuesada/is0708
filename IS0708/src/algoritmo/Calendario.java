package algoritmo;

import java.util.ArrayList;
import aplicacion.Util;

/**
 * Esta clase representa a un calendario donde vamos a almacenar los dias festivos, el
 * max/min numero de empleados y el numero de expertos e inexpertos
 * @author DavidMartin & Miguel Angel Diaz
 */
public class Calendario {
	
	private int mes;
	private int anio;
	private int numDias;//numero de dias del mes
	private HoraCalendario cal[][];//esta matriz contiene para cada dia y para cada hora un objeto de la clase Horacalendario
	
	public Calendario(){//constructora por defecto
		mes = 0;
		anio = 0;
		numDias = 0;
		cal = null;
	}
	
	public Calendario(int mes,int anio){
		this.mes = mes;
		this.anio = anio;
		numDias = Util.dameDias(mes,anio);
		cal=new HoraCalendario[numDias][24];//el calendario tendra un numero de dias dependiendo del mes y de 0 a 23 horas
		//ahora inicializamos el calendario
		for (int i=0;i<numDias;i++){
			for (int j=0;j<24;j++){
				//Aqui debemos rellenar los datos accediendo a las base de datos
				//PRUEBA
				//cal[i][j]=new HoraCalendario(4,0,0,0);
				cal[i][j]=new HoraCalendario(0,0,0,0);
			}
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
	
	public HoraCalendario[][] getCal() {
		return cal;
	}
	
	public void setCal(HoraCalendario[][] cal) {
		this.cal = cal;
	}
	
	/**
	 * Actualiza la información de una hora concreta del calendario
	 * @param dia
	 * @param hora
	 * @param max
	 * @param min
	 * @param exp
	 * @param princ
	 */
	public void actualizaHora(int dia, int hora, int max, int min, int exp, int princ){
		if (esCorrecto(dia, hora)){
			cal[dia][hora].setMax(max);
			cal[dia][hora].setMin(min);
			cal[dia][hora].setExpertos(exp);
			cal[dia][hora].setPrincipiantes(princ);
		}
	}
	
	public int getMaxHora(int dia, int hora){
		if (esCorrecto(dia, hora))
			return cal[dia][hora].getMax();
		else return -1;
	}
	
	public int getMinHora(int dia, int hora){
		if (esCorrecto(dia, hora))
			return cal[dia][hora].getMin();
		else return -1;
		}
	
	public int getExpHora(int dia, int hora){
		if (esCorrecto(dia, hora))
			return cal[dia][hora].getExpertos();
		else return -1;
		}
	
	public int getPrincHora(int dia, int hora){
		if (esCorrecto(dia, hora))
			return cal[dia][hora].getPrincipiantes();
		else return -1;
		}
	
	public boolean esCorrecto(int dia, int hora){
		return (dia>=1 && dia<=numDias && hora>=0 && hora<24);
	}
}
