package interfaces;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class I19_Excepcion {
	private Shell shell;
	
	public I19_Excepcion(Display display, Exception excepcion) {		

		shell = new Shell (display, SWT.CLOSE | SWT.RESIZE);
		
		//Layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setText("¡Excepcioooooooooon!");

		final Composite cCuerpoMen = new Composite (shell, SWT.BORDER);
		cCuerpoMen.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cCuerpoMen.setLayout(new GridLayout(1, false));
		final Label lMensaje	= new Label(cCuerpoMen, SWT.LEFT);
		final Text tMensaje = new Text(cCuerpoMen,SWT.BORDER);
		tMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Random rnd = new Random(5);
		String e = "Ha ocurrido una excepción";
		int r = rnd.nextInt(8);
		System.out.println(r);
		switch (r) {
		case 0:
			e+= " y la aplicación ha hecho PUM.";
			break;
		case 1:
			e+= " y la aplicación se ha ido a tomar por culo.";
			break;
		case 2:
			e+= " y ya estoy hasta los cojones.";
			break;
		case 3:
			e+= ". Se notificará a Gervás de forma automática.";
			break;
		case 4:
			e+= " de la ostia. Mira, mira:";
			break;
		case 5:
			e+= ". Cómo no.";
			break;
		case 6:
			e+= ". A depurar tocan.";
			break;
		case 7:
			e+= ". TOMA TOMA Y TOMAAAAAAA.";
			break;

		}
		e += "\n\n" + excepcion.toString() + "\n";
		for (int i=0; i < excepcion.getStackTrace().length; i++) {
			e+= "\tat " + excepcion.getStackTrace()[i].toString() + "\n";
		}
		excepcion.printStackTrace();
		lMensaje.setText(e);
		tMensaje.setText("CIERRA ESTA VENTANA. TODAVÍA NO HACE NADA.");
		//tMensaje.setText("Por favor, explica brevemente cómo ha ocurrido este error.");
		
		final Composite cEnviarCancelar = new Composite (shell, SWT.BORDER);
		cEnviarCancelar.setLayoutData(new GridData(SWT.FILL, SWT.DOWN, true, false, 1, 1));
		GridLayout lEnviarCancelar = new GridLayout();
		lEnviarCancelar.numColumns = 2;
		cEnviarCancelar.setLayout(lEnviarCancelar);
		
		final Button bEnviar = new Button(cEnviarCancelar, SWT.PUSH);
		bEnviar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bEnviar.setText("Enviar");
		
		final Button bCancelar = new Button(cEnviarCancelar, SWT.PUSH);
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bCancelar.setText("Cancelar");
		
		bCancelar.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
				
			}
			
		});
		tMensaje.setFocus();
		tMensaje.selectAll();
		
		// Botón por defecto bAceptar
		shell.setDefaultButton(bEnviar);
		
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		shell.setSize(shell.getSize().x,shell.getSize().y+300);
		// Mostrar ventana centrada
		shell.setLocation(display.getClientArea().width/2 - shell.getSize().x/2, display.getClientArea().height/2 - shell.getSize().y/2);
		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}
}
