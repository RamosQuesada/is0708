package aplicacion;

/**
 * Esta es la clase principal de la aplicación. Desde aquí se crean el modelo,
 * la vista y el controlador.
 * @author Daniel Dionne
 */
public class Aplicacion {
	
	public static void main (String[] args) {

		// Creación del modelo
		Database db = new Database();
		
		// Creación del controlador
		Controlador controlador = new Controlador(db);

		// Crear vista
		Vista vista = new Vista(controlador, db);
		controlador.setVista(vista);
		
	}
}