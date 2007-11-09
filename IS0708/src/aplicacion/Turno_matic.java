package aplicacion;

import interfaces.I02;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import interfaces.I02;

public class Turno_matic {
	private static Display display;
	
	public static void main (String[] args) {
		// Este bucle mantiene la ventana abierta
		display = new Display ();
		Shell shell = new Shell(display);
		Turno_matic estaClase = new Turno_matic();
		I02 i = new I02(shell, display);
		i.crearVentana();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
