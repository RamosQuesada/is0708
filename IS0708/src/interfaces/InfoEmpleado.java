package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import aplicacion.datos.Empleado;

public class InfoEmpleado {
	private Shell shell;
	private Display display;
	public InfoEmpleado(Empleado e) {
		display = Display.getDefault();
		shell = new Shell(display,SWT.NONE);
		shell.setLayout(new GridLayout(1,true));
		shell.setSize(200, 100);
		Composite c = new Composite(shell, SWT.NONE);
		c.setLayout(new GridLayout(1,true));
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Label label = new Label(c, SWT.NONE);
		label.setText("Nombre empleado");
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		label.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e){};
			public void mouseDown(MouseEvent e){};
			public void mouseUp(MouseEvent e){
				display.dispose();
			};
		});
	}
	public void dibujaInfoEmpleado() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	};

	public static void main(String [] args) {
		InfoEmpleado info = new InfoEmpleado (null);
		info.dibujaInfoEmpleado();
	}

}
