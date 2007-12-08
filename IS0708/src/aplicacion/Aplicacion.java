package aplicacion;

import java.util.ArrayList;
import interfaces.I01;
import interfaces.I02;
import aplicacion.Database.*;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import idiomas.LanguageChanger;

/**
 * Este es el Controlador o clase principal de la aplicación.
 * @author Daniel Dionne
 */
public class Aplicacion {
	private static Display display;
	private Empleado empleadoActual;
	
	public static void main (String[] args) {
		// La lista de empleados
		final ArrayList<Empleado> empleados;
		empleados = new ArrayList<Empleado>();
		
		LanguageChanger l = new LanguageChanger();
		// 0 español
		// 1 polaco
		// 2 inglés
		l.cambiarLocale(0);
		//System.out.println(l.getCurrentLocale().getCountry());
		// Prueba del archivo de idioma
		display = new Display ();
		Shell shell = new Shell(display);
		
		// TODO Lista provisional de empleados para hacer pruebas:
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

		// Conectar con la base de datos
		Database db = new Database();
		db.start();
		
		// Login
		I01 login = new I01(shell, l.getBundle());
		boolean identificado = false;
		while (!identificado) {
			login.mostrarVentana();
			while (!login.dialog.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			if (login.getBotonPulsado()==1) {
				// TODO Si el usuario existe en la base de datos, cargarlo en
				//la variable empleadoActual
				System.out.println("Empleado identificado: " + login.getNumeroVendedor());
				identificado = true;
			}
			else {
				display.dispose();
				identificado = true; // Para que salga del bucle
				db.cerrarConexion();
			}
		}
		
		// Si todavía no he cerrado el display
		if (!display.isDisposed()) {
			// TODO Cambiar por acceso a la vista
			// Poblar ventana: 0 administrador, 1 empleado, 2 jefe, 3 gerente
			new I02(shell, display, l.getBundle(), l.getCurrentLocale(), empleados, login.getNumeroVendedor());
			// Este bucle mantiene la ventana abierta
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
			db.cerrarConexion();
		}
	}
}