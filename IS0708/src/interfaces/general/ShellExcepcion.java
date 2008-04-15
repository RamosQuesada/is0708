package interfaces.general;

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

import aplicacion.Vista;

public class ShellExcepcion {
	private Shell shell;
	public ShellExcepcion(Vista v, Exception excepcion) {
		final Vista vista = v;
		Display d;
		if (excepcion != null) {
			v.getDisplay().dispose();
			d = new Display();
		}
		else {
			d = v.getDisplay();
		}
		shell = new Shell (d, SWT.CLOSE | SWT.RESIZE);
		
		//Layout del shell
		GridLayout lShell = new GridLayout();
		shell.setLayout(lShell);
		shell.setText("Informe");

		final Composite cCuerpoMen = new Composite (shell, SWT.BORDER);
		cCuerpoMen.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		cCuerpoMen.setLayout(new GridLayout(2, false));
		
		final Label lNombre = new Label(cCuerpoMen, SWT.LEFT);
		lNombre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lNombre.setText("Nombre");
		final Text tNombre = new Text(cCuerpoMen,SWT.BORDER);
		tNombre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Label lGrupo = new Label(cCuerpoMen, SWT.LEFT);
		lGrupo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lGrupo.setText("Grupo");
		final Text tGrupo = new Text(cCuerpoMen,SWT.BORDER);
		tGrupo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		
		final Label lPuesto = new Label(cCuerpoMen, SWT.LEFT);
		lPuesto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lPuesto.setText("Puesto");
		final Text tPuesto = new Text(cCuerpoMen,SWT.BORDER);
		tPuesto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		final Label lMensaje	= new Label(cCuerpoMen, SWT.LEFT);
		lMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		lMensaje.setText("Aquí puedes escribir un informe o avisarnos de un error:");
		final Text tMensaje = new Text(cCuerpoMen,SWT.BORDER | SWT.WRAP);
		tMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		String e = "";
		if (excepcion==null) e="";
		else {
			e += "\n\n" + excepcion.toString() + "\n";
			for (int i=0; i < excepcion.getStackTrace().length; i++) {
				e+= "\tat " + excepcion.getStackTrace()[i].toString() + "\n";
			}
			excepcion.printStackTrace();
		}
		

		final Composite cEnviarCancelar = new Composite (shell, SWT.BORDER);
		cEnviarCancelar.setLayoutData(new GridData(SWT.FILL, SWT.DOWN, true, false, 1, 1));
		GridLayout lEnviarCancelar = new GridLayout();
		lEnviarCancelar.numColumns = 2;
		cEnviarCancelar.setLayout(lEnviarCancelar);
		
		final String traza = e;
		final Button bEnviar = new Button(cEnviarCancelar, SWT.PUSH);
		bEnviar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bEnviar.setText("Enviar");
		bEnviar.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			public void widgetSelected(SelectionEvent arg0) {
				vista.getControlador().insertIssue(
						"Nombre: " + tNombre.getText() + "\n" + 
						"Grupo : " + tGrupo.getText()  + "\n" +
						"Puesto: " + tPuesto.getText() + "\n" + 
						"Mensaje: " + tMensaje.getText() + "\nTraza:\n" + traza);
				shell.close();
			}
			
		});
		
		final Button bCancelar = new Button(cEnviarCancelar, SWT.PUSH);
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bCancelar.setText("Cancelar");
		
		bCancelar.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
				
			}
			
		});
		tNombre.setFocus();
		tMensaje.selectAll();
				
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		shell.setSize(shell.getSize().x,shell.getSize().y+300);
		// Mostrar ventana centrada
		shell.setLocation(d.getClientArea().width/2 - shell.getSize().x/2, d.getClientArea().height/2 - shell.getSize().y/2);
		shell.open();
		while (!shell.isDisposed()) {
			if (d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}
