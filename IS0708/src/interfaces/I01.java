/*******************************************************************************
 * INTERFAZ I-01 :: Identificación
 *   por Daniel Dionne
 *   
 * Interfaz de identificación del usuario.
 * ver 0.1
 *******************************************************************************/


package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class I01 {
	private Shell padre = null;

	public I01(Shell padre) {
		this.padre = padre;
		mostrarVentana();
	}
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Label lUsuario  = new Label(shell, SWT.LEFT);
		final Text  tUsuario  = new Text(shell, SWT.BORDER);
		final Label lPassword = new Label(shell, SWT.LEFT);
		final Text  tPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);

	
		final Button bAceptar  = new Button(shell, SWT.PUSH);
		final Button bCancelar = new Button(shell, SWT.PUSH);

		// Dos iconos de tamaño diferente para SO's que los necesiten
		Image icoGr = new Image(padre.getDisplay(), I01.class.getResourceAsStream("icoGr.gif"));
		Image icoPq = new Image(padre.getDisplay(), I01.class.getResourceAsStream("icoPq.gif"));
		shell.setImages(new Image[] {icoPq,icoGr});
		shell.setText("Identificación");
		shell.setVisible(true);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 8;
		layout.makeColumnsEqualWidth = true;

		shell.setLayout(layout);

		lUsuario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lUsuario.setText("Vendedor");
		tUsuario.setEditable(true);
		tUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 5, 1));

		lPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lPassword.setText("Contraseña");
		
		tPassword.setEditable(true);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 5, 1));

		bAceptar.setText("Aceptar");
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));
		
		bCancelar.setText("Cancelar");
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));

		// Un SelectionAdapter con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};

		// Un SelectionAdapter con lo que hace el botón bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tUsuario.getText().length()!=8) {
					I11 ventana = new I11();
					ventana.mostrarMensaje("El número de vendedor debe tener 8 cifras.");
					// Enfocar tUsuario y seleccionar texto
					tUsuario.setFocus();
					tUsuario.selectAll();
				}
				else shell.dispose();
			}
		};

		bCancelar.addSelectionListener(sabCancelar);
		bAceptar.addSelectionListener(sabAceptar);

		// Botón por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}