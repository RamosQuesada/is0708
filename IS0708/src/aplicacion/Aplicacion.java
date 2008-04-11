package aplicacion;

import org.eclipse.swt.widgets.Display;

import interfaces.general.I19_Excepcion;

/**
 * Esta es la clase principal de la aplicación. Desde aquí se crean el modelo,
 * la vista y el controlador.
 * @author Daniel Dionne
 */
public class Aplicacion {
	
	public static void main (String[] args) {
		boolean modoDebug = false;
		if (args.length>0)
			if (args[0].equals("-debug")) {
				modoDebug = true;
			}
		// Iniciar SWT
		Display display = new Display();
		
		// Crear el modelo
		Database db = new Database();
		
		// Crear el controlador
		Controlador controlador = new Controlador(db,modoDebug);

		// Crear la vista
		Vista v = new Vista(display, controlador, db);

		// Iniciar aplicación
		try {
			v.start();
		}
		catch (Exception e) {
			new I19_Excepcion(v, e);
		}
	}
}