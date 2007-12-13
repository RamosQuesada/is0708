package aplicacion;

import java.util.ArrayList;
import interfaces.I01_Login;
import aplicacion.Database.*;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import idiomas.LanguageChanger;

/**
 * Esta es la clase principal de la aplicación. Desde aquí se crean el modelo,
 * la vista y el controlador.
 * @author Daniel Dionne
 */
public class Aplicacion {
	
	public static void main (String[] args) {
		// Creación del display y el shell
		Display display = new Display ();
		Shell shell = new Shell(display);

		// Creación del gestor de idiomas
		LanguageChanger l = new LanguageChanger();
		// 0 español
		// 1 polaco
		// 2 inglés
		l.cambiarLocale(0);

		// Creación del modelo
		Database db = new Database();
		
		// Creación del controlador
		Controlador controlador = new Controlador(db);

		// Crear vista
		Vista vista = new Vista(shell, l.getBundle(), l.getCurrentLocale(), controlador, db);
		controlador.incluyeVista(vista);
		
	}
}