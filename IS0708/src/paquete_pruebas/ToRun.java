package paquete_pruebas;

import idiomas.LanguageChanger;
import interfaces.I13_Elegir_empleado;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import aplicacion.Empleado;


public class ToRun {

	private I13_Elegir_empleado elejirEmpleado;
	public static void main (String[] args) {
		// La lista de empleados
		final ArrayList<Empleado> empleados;
		empleados = new ArrayList<Empleado>();		
		LanguageChanger l = new LanguageChanger();
		l.cambiarLocale(0);
		
		Display display = new Display ();
		Shell mainshell = new Shell(display);
		mainshell.setLocation(100,100);
		
		// TODO Lista provisional de empleados para hacer pruebas:
		Empleado eS = new Empleado(1, "M. Jackson", new Color (display, 104, 228,  85));
		
		Color col = new Color(display, 108, 0, 50);
		Empleado e1 = new Empleado(eS, 12345671, "phil", "colins", "-", new Date(1985,6,23), 1, "phil.colins@gmail.com", "", 1, null, null, new Date(2000,3,3),new Date(2005,9,9), col );
		Empleado e2 = new Empleado(eS, 12345672, "bill", "andrew", "-", new Date(1985,6,23), 1, "phil.colins@gmail.com", "", 1, null, null, new Date(2000,3,3),new Date(2005,9,9), col );
		Empleado e3 = new Empleado(eS, 12345673, "dark", "soprano", "-", new Date(1985,6,23), 1, "phil.colins@gmail.com", "", 1, null, null, new Date(2000,3,3),new Date(2005,9,9), col );
		Empleado e4 = new Empleado(eS, 12345674, "angelo", "chilp", "-", new Date(1985,6,23), 1, "phil.colins@gmail.com", "", 1, null, null, new Date(2000,3,3),new Date(2005,9,9), col );
		Empleado e5 = new Empleado(eS, 12345675, "phil",  "colins", "-", new Date(1985,6,23), 1, "phil.colins@gmail.com", "", 1, null, null, new Date(2000,3,3),new Date(2005,9,9), col );
		Empleado e6 = new Empleado(eS, 12345676, "phil", "donk", "-", new Date(1985,6,23), 1, "phil.colins@gmail.com", "", 1, null, null, new Date(2000,3,3),new Date(2005,9,9), col );		
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);
		empleados.add(e4);
		empleados.add(e5);
		empleados.add(e6);

		mainshell.open();
		I13_Elegir_empleado elejirEmpleado = new I13_Elegir_empleado(mainshell, l.getBundle(), empleados);
		elejirEmpleado.mostrarVentana();
		
			// Este bucle mantiene la ventana abierta
			while (!mainshell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();

	}

}
