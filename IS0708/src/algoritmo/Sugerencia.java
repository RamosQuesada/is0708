/**
 * 
 */
package algoritmo;

import java.sql.Time;
import java.util.Date;

import aplicacion.utilidades.Util;

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
	//private int dia;
	private Date fecha;
	
	/**
	 * Constructora por parámetros
	 * @param faltas Número de personas que faltan
	 * @param minimo Mínimo de empleados
	 * @param ini Hora de inicio de la franja
	 * @param fin Hora de fin de la franja
	 * @param dia Día de la sugerencia
	 * @param tipo Tipo de sugerencia
	 */
	public Sugerencia(int faltas, int minimo, Time ini, Time fin, /*int dia*/Date fecha, int tipo){
		this.faltas=faltas;
		this.minimo=minimo;
		this.horaIni=ini;
		this.horaFin=fin;
		//this.dia=dia;
		this.fecha=fecha;
		this.tipo=tipo;
	}
	
	/**
	 * Amplía la sugerencia un periodo de tiempo
	 * @param horas Horas a aumentar la sugerencia
	 * @param minutos Minutos a aumentar la sugerencia
	 */
	public void ampliar(int horas, int minutos){
		horaFin.setHours(horaFin.getHours()+horas);
		horaFin.setMinutes(horaFin.getMinutes()+minutos);
	}
	
	/**
	 * Devuelve el número de personas que faltan en la franja de la sugerencia
	 * @return faltas
	 */
	public int getFaltas() {
		return faltas;
	}
	
	/**
	 * Establece el número de personas que faltan en la franja de la sugerencia
	 * @param faltas Las faltas a establecer
	 */
	public void setFaltas(int faltas) {
		this.faltas = faltas;
	}
	
	/**
	 * Devuelve la hora de fin de la franja de la sugerencia
	 * @return horaFin
	 */
	public Time getHoraFin() {
		return horaFin;
	}
	
	/**
	 * Establece la hora de fin de la franja de la sugerencia
	 * @param horaFin La horaFin a establecer
	 */
	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}
	
	/**
	 * Devuelve la hora de inicio de la franja de la sugerencia
	 * @return horaIni
	 */
	public Time getHoraIni() {
		return horaIni;
	}
	
	/**
	 * Establece la hora de inicio de la franja de la sugerencia
	 * @param horaIni La horaIni a establecer
	 */
	public void setHoraIni(Time horaIni) {
		this.horaIni = horaIni;
	}
	
	/**
	 * Devuelve la sugerencia de la franja
	 * @return sugerencia
	 */
	public String getSugerencia() {
		return sugerencia;
	}
	
	/**
	 * Establece la sugerencia de la franja
	 * @param sugerencia La sugerencia a establecer
	 */
	public void setSugerencia(String sugerencia) {
		this.sugerencia = sugerencia;
	}
	
	/**
	 * Devuelve el tipo de la sugerencia
	 * @return tipo
	 */
	public int getTipo() {
		return tipo;
	}
	
	/**
	 * Establece el tipo de la sugerencia
	 * @param tipo El tipo a establecer
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Transforma la información de la sugerencia a una cadena de texto
	 * @return La cadena de la sugerencia
	 */
	public String toString(){
		int aux1=horaIni.getMinutes();
		String strAux1=new String();
		if (aux1<10) {
			strAux1="0";
			strAux1=strAux1.concat(Integer.toString(aux1));
		} else
			strAux1=Integer.toString(aux1);
		
		int aux2=horaFin.getMinutes();	                                                                                  
		String strAux2=new String();
		if (aux2<10) {
			strAux2="0";
			strAux2=strAux2.concat(Integer.toString(aux2));
		} else
			strAux2=Integer.toString(aux2);
		
		int diaSug = fecha.getDate()+1;
		if (diaSug > Util.dameDias(fecha.getMonth(),fecha.getYear()-1900)) diaSug=1;
		
		//String cadena="El dia "+(dia+1)+", desde las "+horaIni.getHours()+":"+strAux1+" hasta las "+horaFin.getHours()+":"+strAux2+", faltan "+faltas+" empleados.";
		String cadena="El dia "+(diaSug)+", desde las "+horaIni.getHours()+":"+strAux1+" hasta las "+horaFin.getHours()+":"+strAux2+", faltan "+faltas+" empleados.";
		if (tipo==1){cadena=cadena+" Falta mas del 50% del personal.";}
		else{if(tipo==2){cadena=cadena+" Situación crítica. Falta todo el personal.";}}
		return cadena;
	}
	
	/**
	 * Calcula la puntuación de una sugerencia
	 * @return El valor de la puntuación
	 */
	public double puntuacion () {
		double resul=0;
		double faltasD=(double)faltas;
		double minD=(double)minimo;
		resul=(horaFin.getHours()-horaIni.getHours())*60+(horaFin.getMinutes()-horaIni.getMinutes());
		resul=resul/60.0; //para hacer calculos con horas
		resul=resul*(faltasD/minD);
		return resul;
	}
	
}
