package aplicacion;

import java.util.ArrayList;
import interfaces.I01_Login;
import interfaces.I02_Menu_principal;
import aplicacion.Database.*;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import idiomas.LanguageChanger;

/**
 * Este es el Controlador o clase principal de la aplicaci�n.
 * @author Daniel Dionne
 */
public class Aplicacion {
	private static Display display;
	private static Empleado empleadoActual;
	
	public static void main (String[] args) {
		// La lista de empleados
		final ArrayList<Empleado> empleados;
		empleados = new ArrayList<Empleado>();
		
		LanguageChanger l = new LanguageChanger();
		// 0 espa�ol
		// 1 polaco
		// 2 ingl�s
		l.cambiarLocale(0);

		display = new Display ();
		Shell shell = new Shell(display);
		
		// TODO Quitar esta lista provisional de empleados para hacer pruebas:
		Empleado e1 = new Empleado(1, "M. Jackson", new Color (display, 104, 228,  85));
		Empleado e2 = new Empleado(2, "J. Mayer",   new Color (display, 130, 130, 225));
		Empleado e3 = new Empleado(3, "B. Jovi",    new Color (display, 240, 190, 150));
		Empleado e4 = new Empleado(4, "H. Day",     new Color (display, 150, 150, 150));
		Empleado e5 = new Empleado(5, "N. Furtado", new Color (display, 200, 80, 180));
		Empleado e6 = new Empleado(6, "L. Kravitz", new Color (display, 200, 80, 100));
		e1.turno.franjaNueva(new Posicion( 9,  6), new Posicion(14,  0));
		e1.turno.franjaNueva(new Posicion(16,  0), new Posicion(18,  0));
		e2.turno.franjaNueva(new Posicion(15,  0), new Posicion(22,  0));
		e3.turno.franjaNueva(new Posicion(12,  3), new Posicion(16,  0));
		e3.turno.franjaNueva(new Posicion(18,  0), new Posicion(22,  3));
		e4.turno.franjaNueva(new Posicion(15,  0), new Posicion(19,  9));
		e5.turno.franjaNueva(new Posicion(12,  0), new Posicion(16,  0));
		e6.turno.franjaNueva(new Posicion(10,  5), new Posicion(14,  0));
		e6.turno.franjaNueva(new Posicion(16, 10), new Posicion(19,  0));		
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);
		empleados.add(e4);
		empleados.add(e5);
		empleados.add(e6);

		Database db = new Database();
			
		// Login y conexi�n a la base de datos
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
				// Si llega aqu�, ya ha conexi�n con la base de datos
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
				//Salir de la aplicaci�n
				display.dispose();
				identificado = true; // Para que salga del bucle
				if (db.conexionAbierta())
					db.cerrarConexion();
			}
		}
		
		// Si todav�a no he cerrado el display
		if (!display.isDisposed()) {
			// TODO Cambiar por acceso a la vista
			Controlador controlador = new Controlador(db);
			// Poblar ventana: 0 administrador, 1 empleado, 2 jefe, 3 gerente
			I02_Menu_principal vista= new I02_Menu_principal(shell, display, l.getBundle(), l.getCurrentLocale(), empleados, login.getNumeroVendedor(), empleadoActual, db,controlador);
			controlador.incluyeVista(vista);
			// Este bucle mantiene la ventana abierta
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
			// TODO Controlar si se hab�a conectado antes
			db.cerrarConexion();
		}
	}
}