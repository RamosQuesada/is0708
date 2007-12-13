package aplicacion;

import java.util.ArrayList;

/**
 * 
 * @author Carlos Sanchez Garcia
 *
 */
public class Mensajeria {
	
	private Controlador controlador;
	
	public Mensajeria(Controlador controlador){
		this.controlador=controlador;
	}
	
	/**
	 * Función que devuelve un ArrayList con los 10 primeros mensajes
	 * @return
	 */
	public ArrayList primerosMensajes(int id){
		ArrayList lista=controlador.getMensajes(id);
		return null;
	}
	
}
