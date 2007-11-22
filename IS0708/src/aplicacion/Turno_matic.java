package aplicacion;

import java.util.ArrayList;
import interfaces.I02;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import idiomas.LanguageChanger;

public class Turno_matic {
	private static Display display;
	
	public static void main (String[] args) {
		// La lista de empleados
		final ArrayList<Empleado> empleados;
		empleados = new ArrayList<Empleado>();
			
		LanguageChanger l = new LanguageChanger();
		// 2 inglés
		l.cambiarLocale(0);
		System.out.println(l.getCurrentLocale().getCountry());
		// Prueba del archivo de idioma
		display = new Display ();
		Shell shell = new Shell(display);
		
		// Lista provisional de empleados para hacer pruebas:
		Empleado e1 = new Empleado("M. Jackson", new Color (display, 104, 228,  85));
		Empleado e2 = new Empleado("J. Mayer",   new Color (display, 130, 130, 225));
		Empleado e3 = new Empleado("B. Jovi",    new Color (display, 240, 190, 150));
		Empleado e4 = new Empleado("H. Day",     new Color (display, 150, 150, 150));
		Empleado e5 = new Empleado("N. Furtado", new Color (display, 200, 80, 180));
		Empleado e6 = new Empleado("L. Kravitz", new Color (display, 200, 80, 100));
		e1.franjaNueva(new Posicion( 9,  6), new Posicion(14,  0));
		e1.franjaNueva(new Posicion(16,  0), new Posicion(18,  0));
		e2.franjaNueva(new Posicion(15,  0), new Posicion(22,  0));
		e3.franjaNueva(new Posicion(12,  3), new Posicion(16,  0));
		e3.franjaNueva(new Posicion(18,  0), new Posicion(22,  3));
		e4.franjaNueva(new Posicion(15,  0), new Posicion(19,  9));
		e5.franjaNueva(new Posicion(12,  0), new Posicion(16,  0));
		e6.franjaNueva(new Posicion(10,  5), new Posicion(14,  0));
		e6.franjaNueva(new Posicion(16, 10), new Posicion(19,  0));		
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);
		empleados.add(e4);
		empleados.add(e5);
		empleados.add(e6);

		I02 i = new I02(shell, display, l.getBundle(), l.getCurrentLocale(), empleados);
		i.crearVentana();
		// Este bucle mantiene la ventana abierta
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}