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
import java.util.ResourceBundle;

public class I01 {
	private Shell shell = null;
	private ResourceBundle bundle;
	public I01(Shell shell, ResourceBundle bundle) {
		this.shell = shell;
		this.bundle = bundle;
		mostrarVentana();
	}
	public void mostrarVentana() {
		Image fondo = new Image(shell.getDisplay(), I01.class.getResourceAsStream("intro.png"));
	//	final Shell shell = new Shell (shell, SWT.NONE | SWT.APPLICATION_MODAL);

		
		//Esto hace que los labels no tengan fondo
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setLayout(new GridLayout());
		
		Composite contenido = new Composite(shell, SWT.NONE);
		contenido.setLayout(new GridLayout(8,true));
		contenido.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		
		final Label lUsuario  = new Label(contenido, SWT.LEFT);
		final Text  tUsuario  = new Text(contenido, SWT.BORDER);
		final Label lPassword = new Label(contenido, SWT.LEFT);
		final Text  tPassword = new Text(contenido, SWT.BORDER | SWT.PASSWORD);

		final Button bAceptar  = new Button(contenido, SWT.PUSH);
		final Button bCancelar = new Button(contenido, SWT.PUSH);

		// Dos iconos de tamaño diferente para SO's que los necesiten
		Image icoGr = new Image(shell.getDisplay(), I01.class.getResourceAsStream("icoGr.gif"));
		Image icoPq = new Image(shell.getDisplay(), I01.class.getResourceAsStream("icoPq.gif"));
		shell.setImages(new Image[] {icoPq,icoGr});
		shell.setText(bundle.getString("I01_tit_Ident"));
		
		
		
		lUsuario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lUsuario.setText(bundle.getString("Vendedor"));
		tUsuario.setEditable(true);
		tUsuario.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

		lPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lPassword.setText(bundle.getString("Contraseña"));
		
		tPassword.setEditable(true);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

		bAceptar.setText(bundle.getString("Aceptar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 4, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 4, 1));

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
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I01_err_NumVendedor"));
					e.doit = messageBox.open () == SWT.YES;
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
		shell.setBackgroundImage(fondo);
		shell.setSize(500,400);
		

		// Mostrar ventana centrada sobre la pantalla
		shell.setLocation(shell.getDisplay().getClientArea().width/2 - shell.getSize().x/2, shell.getDisplay().getClientArea().height/2 - shell.getSize().y/2);
		shell.open();
	}
}