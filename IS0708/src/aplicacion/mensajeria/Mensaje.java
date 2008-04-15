package aplicacion.mensajeria;

import java.sql.Date;

/**
 * 
 * @author Carlos Sanchez Garcia
 *
 */
public class Mensaje {
	
	private int idMensaje;
	private int remitente;
	//TODO El destinatario hay que cambiarlo a un array de destinatarios
	private int destinatario;
	private Date fecha;
	private String asunto;
	private String texto;
	private boolean marcado;
	private boolean leido;
	
	/**
	 * Constructor de la clase con campos
	 * @param destinatario
	 * @param remitente
	 * @param fecha
	 * @param asunto
	 * @param texto
	 */
	public Mensaje(int idMensaje, int remitente, int destinatario, Date fecha, String asunto, String texto, boolean marcado) {
		this.idMensaje = idMensaje;
		this.destinatario = destinatario;
		this.remitente = remitente;
		this.fecha = fecha;
		this.asunto = asunto;
		this.texto = texto;
		this.marcado = marcado;
		this.leido = false;
	}
	
	public int getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(int destinatario) {
		this.destinatario = destinatario;
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
		return idMensaje;
	}
	
	public void setIdmensaje(int idmensaje) {
		this.idMensaje = idmensaje;
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

	public boolean isMarcado() {
		return marcado;
	}

	public void setMarcado(boolean marcado) {
		this.marcado = marcado;
	}

	public void setLeido()
	{
		if (leido = false)
		leido = true;
	}
	
	public boolean isLeído() {
		return leido;
	}
}