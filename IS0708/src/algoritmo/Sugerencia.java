/**
 * 
 */
package algoritmo;

import java.sql.Time;

/**
 * @author Alberto
 * el campo tipo:
 * 0 es para una sugerencia de falta que no tiene mayor consecuencia
 * 1 es para una sugerencia de falta de mas del 50% de personal
 * 2 es para una sugerencia de falta extrema
 */
public class Sugerencia {
	
	private String sugerencia;
	private int tipo;
	private int faltas;
	private int minimo;
	private Time horaIni;
	private Time horaFin;
	private int dia;
	
	
	public Sugerencia(int faltas,int minimo, Time ini, Time fin, int dia,int tipo){
		this.faltas=faltas;
		this.minimo=minimo;
		this.horaIni=ini;
		this.horaFin=fin;
		this.dia=dia;
		this.tipo=tipo;
	}
	
	
	public void ampliar(int horas, int minutos){
		horaFin.setHours(horaFin.getHours()+horas);
		horaFin.setMinutes(horaFin.getMinutes()+minutos);
	}
	
	/**
	 * @return the faltas
	 */
	public int getFaltas() {
		return faltas;
	}
	/**
	 * @param faltas the faltas to set
	 */
	public void setFaltas(int faltas) {
		this.faltas = faltas;
	}
	/**
	 * @return the horaFin
	 */
	public Time getHoraFin() {
		return horaFin;
	}
	/**
	 * @param horaFin the horaFin to set
	 */
	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}
	/**
	 * @return the horaIni
	 */
	public Time getHoraIni() {
		return horaIni;
	}
	/**
	 * @param horaIni the horaIni to set
	 */
	public void setHoraIni(Time horaIni) {
		this.horaIni = horaIni;
	}
	/**
	 * @return the sugerencia
	 */
	public String getSugerencia() {
		return sugerencia;
	}
	/**
	 * @param sugerencia the sugerencia to set
	 */
	public void setSugerencia(String sugerencia) {
		this.sugerencia = sugerencia;
	}
	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}
	
	
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public String toString(){
		String cadena="El dia "+dia+", desde las "+horaIni.getHours()+":"+horaIni.getMinutes()+" hasta las "+horaFin.getHours()+":"+horaFin.getMinutes()+", faltan "+faltas+" empleados.";
		if (tipo==1){cadena=cadena+"Falta mas del 50% del personal.";}
		else{if(tipo==2){cadena=cadena+"Situación crítica. Falta todo el personal.";}}
		return cadena;
	}
	
	public int puntuacion(){
		int resul=0;
		resul=(horaFin.getHours()-horaIni.getHours())*60+(horaFin.getMinutes()-horaIni.getMinutes());
		resul=resul*(faltas/minimo);
		return resul;
	}
	

}
