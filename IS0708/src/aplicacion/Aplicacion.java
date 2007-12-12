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
	private static Display display;
	private static Empleado empleadoActual;
	
	public static void main (String[] args) {
		// Creación del modelo
		Database db = new Database();
		
		
		LanguageChanger l = new LanguageChanger();
		// 0 español
		// 1 polaco
		// 2 inglés
		l.cambiarLocale(0);

		display = new Display ();
		Shell shell = new Shell(display);

			
		// Login y conexión a la base de datos
		I01_Login login = new I01_Login(shell, l.getBundle(), db);
		boolean identificado = false;
		while (!identificado) {
			login.mostrarVentana();
			while (!login.dialog.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			if (login.getBotonPulsado()==1) {
				// Si llega aquí, ya ha conexión con la base de datos
				if (login.getNumeroVendedor()==0) {
					if (login.getPassword()=="admin")
						System.out.println("Administrador identificado");
				}
				else {
					// TODO BD Si el usuario existe en la base de datos, cargarlo en
					//la variable empleadoActual
					System.out.println("Empleado identificado: " + login.getNumeroVendedor());
				}
				identificado = true;
				// si no, mostrar mensaje de error
			}
			else {
				//Salir de la aplicación
				display.dispose();
				identificado = true; // Para que salga del bucle
				if (db.conexionAbierta())
					db.cerrarConexion();
			}
		}
		
		// Si todavía no he cerrado el display, ya he hecho login correctamente
		if (!display.isDisposed()) {
			// Crear controlador
			Controlador controlador = new Controlador(db,login.getNumeroVendedor());
			// Crear vista
			Vista vista = new Vista(shell, l.getBundle(), l.getCurrentLocale(), controlador, db);
			controlador.incluyeVista(vista);
			
			// Este bucle mantiene la ventana abierta
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
			if (!db.conexionAbierta())
				db.cerrarConexion();
		}
	}
}