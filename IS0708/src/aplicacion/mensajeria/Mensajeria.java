package aplicacion.mensajeria;

import java.util.ArrayList;
import java.sql.Date;

import aplicacion.Controlador;

/**
 * Clase que implementa la mensajeria de un empleado, esto es, el conjunto
 * de mensajes entrantes y salientes que puede ver un empleado en un 
 * momento dado.
 * @author Carlos Sanchez Garcia
 *
 */
public class Mensajeria {
	
	/**
	 * Contolador de la aplicacion.
	 */
	private Controlador controlador;
	
	/**
	 * Lista de mensajes.
	 */
	private ArrayList<Mensaje> mensajesActuales;
	
	/**
	 * Numero de mensaje (ordenados por fecha), por el que empieza la vision
	 * de mensajes del empleado.
	 */
	private int inicio;
	
	/**
	 * Numero de mensajes a visionar
	 */
	private int tamano;
	
	/**
	 * Identificador del empleado
	 */
	private int id;
	
	/**
	 * Constructor de la mensajeria.
	 * @param controlador Controlador de la aplicacion.
	 * @param id Identificador del empleado.
	 */
	public Mensajeria(Controlador controlador,int id){
		this.controlador=controlador;
		this.id=id;
		inicio=-1;
		tamano=-1;
	}
	
	/**
	 * Funcion que permite seleccionar una lista de mensajes entrantes dado un numero de estos.
	 * @param inicio Primer mensaje de la lista (el mas reciente).
	 * @param num_mensajes Cantidad de mensajes que deseamos seleccionar.
	 * @return Arraylist con una lista de mensajes entrantes ordenados.
	*/
	public ArrayList<Mensaje> dameMensajesEntrantes(int inicio,int num_mensajes){
		//ArrayList<Mensaje> lista=controlador.getMensajes(id);
		return(this.mensajesActuales=controlador.getMensajesEntrantes(id, inicio, num_mensajes));
	}	
	
	/**
	 * Funcion que permite seleccionar una lista de mensajes salientes dado un numero de estos.
	 * @param inicio Primer mensaje de la lista (el mas reciente).
	 * @param num_mensajes Cantidad de mensajes que deseamos seleccionar.
	 * @return Arraylist con una lista de mensajes salientes ordenados.
	*/	
	public void dameMensajesSalientes(int inicio,int num_mensajes){
		//ArrayList<Mensaje> lista=controlador.getMensajes(id);
		this.mensajesActuales=controlador.getMensajesSalientes(id, inicio, num_mensajes);
	}
	
	/**
	 * Funcion que marca un mensaje indicado.
	 * @param numRelativo Numero de mensaje que queremos resaltar.
	*/
	public void marcarMensaje(int numRelativo){
		Mensaje mensajeAMarcar = this.mensajesActuales.get(numRelativo);
		this.controlador.marcarMensaje(mensajeAMarcar);
	}
	
	/**
	 * Función que elimina un mensaje entrante indicado.
	 * @param numRelativo Numero de mensaje entrante que queremos eliminar.
	*/
	public void eliminarMensajeEntrante(int numRelativo){
		Mensaje mensajeAEliminar = this.mensajesActuales.get(numRelativo);
		this.controlador.eliminaMensaje(mensajeAEliminar);
		dameMensajesEntrantes(inicio,tamano);
	}
	
	
	/**
	 * Funcion que elimina un mensaje saliente indicado.
	 * @param numRelativo Numero de mensaje saliente que queremos eliminar. 
	*/
	public void eliminarMensajeSaliente(int numRelativo){
		Mensaje mensajeAEliminar = this.mensajesActuales.get(numRelativo);
		this.controlador.eliminaMensaje(mensajeAEliminar);
		dameMensajesSalientes(inicio,tamano);
	}
	
	/**
	 * Funcion que crea un mensaje con remitente el usuario actual y la fecha actual.
	 * @param destinatario Destinatario del mensaje.
	 * @param asunto String con el titulo del mensaje.
	 * @param texto String con el contenido del mensaje.
	 */
	public int creaMensaje(int destinatario, String asunto, String texto){
		return controlador.insertMensaje(new Mensaje(0, controlador.getEmpleadoActual().getEmplId(), destinatario, controlador.getFechaActual(),  asunto, texto, false,false));
	}
	
	/**
	 * Funcion que se encarga de contactar con el controlador para que se envie un mensaje dado.
	 * @param mensaje Mensaje que queremos enviar.
	 */
	public void enviarMensaje(Mensaje mensaje) {
		this.controlador.insertMensaje(mensaje);
	}
}
