package aplicacion;

import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que implementa la mensajeria de un empleado, esto es el conjunto
 * de mensajes entrantes y salientes que puede ver un empleado en un 
 * momento dado
 * @author Carlos Sanchez Garcia
 *
 */
public class Mensajeria {
	
	/**
	 * Contolador de la aplicacion
	 */
	private Controlador controlador;
	
	/**
	 * Lista de mensajes 
	 */
	private ArrayList<Mensaje> mensajesActuales;
	
	/**
	 * Numero de mensaje (ordenados por fecha), por el que empieza la vision
	 * de mensajes del empleado
	 */
	private int inicio;
	
	/**
	 * Numero de mensajes a visionar
	 */
	private int tamaño;
	
	/**
	 * Identificador del empleado
	 */
	private int id;
	
	/**
	 * Constructor de la mensajeria
	 * @param controlador Controlador de la aplicacion
	 * @param id Identificador del empleado
	 */
	public Mensajeria(Controlador controlador,int id){
		this.controlador=controlador;
		this.id=id;
		inicio=-1;
		tamaño=-1;
	}
	
	/**
	 * Función que devuelve un ArrayList mensajes entrantes indicados
	 * @return
	*/
	public ArrayList<Mensaje> dameMensajesEntrantes(int inicio,int num_mensajes){
		//ArrayList<Mensaje> lista=controlador.getMensajes(id);
		return(this.mensajesActuales=controlador.getMensajesEntrantes(id, inicio, num_mensajes));
		
	/**
	 * Función que devuelve un ArrayList mensajes salientes indicados
	 * @return
	*/	
	public void dameMensajesSalientes(int inicio,int num_mensajes){
		//ArrayList<Mensaje> lista=controlador.getMensajes(id);
		this.mensajesActuales=controlador.getMensajesSalientes(id, inicio, num_mensajes);
	}
	
	
	/**
	 * Función que marca un mensaje indicado
	 * @return
	*/
	public void marcarMensaje(int numRelativo){
		Mensaje mensajeAMarcar = this.mensajesActuales.get(numRelativo);
		this.controlador.marcarMensaje(mensajeAMarcar);
	}
	
	
	/**
	 * Función que elimina un mensaje entrante indicado
	 * @return
	*/
	public void eliminarMensajeEntrante(int numRelativo){
		Mensaje mensajeAEliminar = this.mensajesActuales.get(numRelativo);
		this.controlador.eliminaMensaje(mensajeAEliminar);
		dameMensajesEntrantes(inicio,tamaño);
	}
	
	
	/**
	 * Función que elimina un mensaje saliente indicado
	 * @return
	*/
	public void eliminarMensajeSaliente(int numRelativo){
		Mensaje mensajeAEliminar = this.mensajesActuales.get(numRelativo);
		this.controlador.eliminaMensaje(mensajeAEliminar);
		dameMensajesSalientes(inicio,tamaño);
	}
	
	/**
	 * Funcion que introduce crea un mensaje 
	 */
	public void creaMensaje(int idmensaje, int remitente, Date fecha, String asunto, String texto){
		this.controlador.insertMensaje(new Mensaje(idmensaje, remitente,  fecha,  asunto, texto));
	}
	
	
	
}
