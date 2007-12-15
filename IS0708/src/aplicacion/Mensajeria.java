package aplicacion;

import java.util.ArrayList;

/**
 * 
 * @author Carlos Sanchez Garcia
 *
 */
public class Mensajeria {
	
	private Controlador controlador;
	private ArrayList<Mensaje> mensajesActuales;
	private int inicio;
	private int tamaño;
	private int id;
	
	public Mensajeria(Controlador controlador,int id){
		this.controlador=controlador;
		this.id=id;
		inicio=-1;
		tamaño=-1;
	}
	
	/**
	 * Función que devuelve un ArrayList con los 10 primeros mensajes
	 * @return
	*/
	public void dameMensajesEntrantes(int inicio,int num_mensajes){
		//ArrayList<Mensaje> lista=controlador.getMensajes(id);
		this.mensajesActuales=controlador.getMensajesEntrantes(id, inicio, num_mensajes);
	}
	
	
	public void dameMensajesSalientes(int inicio,int num_mensajes){
		//ArrayList<Mensaje> lista=controlador.getMensajes(id);
		this.mensajesActuales=controlador.getMensajesSalientes(id, inicio, num_mensajes);
	}
	
	
	
	public void marcarMensaje(int numRelativo){
		Mensaje mensajeAMarcar = this.mensajesActuales.get(numRelativo);
		this.controlador.marcarMensaje(mensajeAMarcar);
	}
	
	
	
	public void eliminarMensajeEntrante(int numRelativo){
		Mensaje mensajeAEliminar = this.mensajesActuales.get(numRelativo);
		this.controlador.eliminaMensaje(mensajeAEliminar);
		dameMensajesEntrantes(inicio,tamaño);
	}
	
	
	
	public void eliminarMensajeSaliente(int numRelativo){
		Mensaje mensajeAEliminar = this.mensajesActuales.get(numRelativo);
		this.controlador.eliminaMensaje(mensajeAEliminar);
		dameMensajesSalientes(inicio,tamaño);
	}
	
	
	
}
