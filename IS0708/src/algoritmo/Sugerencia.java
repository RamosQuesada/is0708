/**
 * 
 */
package algoritmo;

import java.sql.Time;

/**
 * @author Alberto
 *
 */
public class Sugerencia {
	
	private String sugerencia;
	private int tipo;
	private int faltas;
	private Time horaIni;
	private Time horaFin;
	
	
	public Sugerencia(int faltas, Time ini, Time fin){
		this.faltas=faltas;
		this.horaIni=ini;
		this.horaFin=fin;
		this.tipo=1;//TODO pensar algo para el tipo
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

}
