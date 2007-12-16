package aplicacion;

import java.sql.Date;

/**
 * 
 * @author Carlos Sanchez Garcia
 *
 */
public class Mensaje {
	
	private int idmensaje;
	private int remitente;
	private Date fecha;
	private String asunto;
	private String texto;
	
	/**
	 * Constructor de la clase con campos
	 * @param idmensaje
	 * @param remitente
	 * @param fecha
	 * @param asunto
	 * @param texto
	 */
	public Mensaje(int idmensaje, int remitente, Date fecha, String asunto, String texto) {
		super();
		this.idmensaje = idmensaje;
		this.remitente = remitente;
		this.fecha = fecha;
		this.asunto = asunto;
		this.texto = texto;
	}
	/**
	 * Constructor por defecto de la clase
	 *
	 */
	public Mensaje(){
		
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getIdmensaje() {
		return idmensaje;
	}
	public void setIdmensaje(int idmensaje) {
		this.idmensaje = idmensaje;
	}
	public int getRemitente() {
		return remitente;
	}
	public void setRemitente(int remitente) {
		this.remitente = remitente;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
}
