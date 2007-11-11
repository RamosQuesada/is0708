package aplicacion;

import interfaces.I02;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Turno_matic {
	private static Display display;
	
	public static void main (String[] args) {
		display = new Display ();
		Shell shell = new Shell(display);
		I02 i = new I02(shell, display);
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