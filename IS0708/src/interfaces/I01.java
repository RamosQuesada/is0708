package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import java.util.ResourceBundle;
import aplicacion.Database;

/**
 * Interfaz de usuario I-01 :: Identificación
 * @author Daniel Dionne
 */
public class I01 {
	private Shell padre = null;
	private ResourceBundle bundle;
	private int numeroVendedor;
	String password;
	public Shell dialog;
	private int botonPulsado;
	Database db;
	Text tEstado;
	
	public I01(Shell padre, ResourceBundle bundle) {
		this.padre = padre;
		this.bundle = bundle;
		this.db = db;
		numeroVendedor = 0;
		password = "";
		botonPulsado = -1;
	}
		
	/**	
	 * Crea la ventana
	 */
	public synchronized void mostrarVentana() {
		Image fondo = new Image(padre.getDisplay(), I01.class.getResourceAsStream("intro.png"));
		dialog = new Shell (padre, SWT.NONE | SWT.APPLICATION_MODAL);
		
		//Esto hace que los labels no tengan fondo
		dialog.setBackgroundMode(SWT.INHERIT_DEFAULT);
		dialog.setLayout(new GridLayout());
		
		Composite contenido = new Composite(dialog, SWT.NONE);
		contenido.setLayout(new GridLayout(8,true));
		contenido.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		
		/* TODO
		tEstado = new Text(dialog, SWT.LEFT);
		tEstado.setText("Conectando con la base de datos");
		*/
		
		final Label lUsuario  = new Label(contenido, SWT.LEFT);
		final Text  tUsuario  = new Text(contenido, SWT.BORDER);
		final Label lPassword = new Label(contenido, SWT.LEFT);
		final Text  tPassword = new Text(contenido, SWT.BORDER | SWT.PASSWORD);

		final Button bAceptar  = new Button(contenido, SWT.PUSH);
		final Button bCancelar = new Button(contenido, SWT.PUSH);

		// Dos iconos de tamaño diferente para SO's que los necesiten
		Image icoGr = new Image(padre.getDisplay(), I01.class.getResourceAsStream("icoGr.gif"));
		Image icoPq = new Image(padre.getDisplay(), I01.class.getResourceAsStream("icoPq.gif"));
		dialog.setImages(new Image[] {icoPq,icoGr});
		dialog.setText(bundle.getString("I01_tit_Ident"));
		
		// Contenido de la ventana		
		lUsuario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lUsuario.setText(bundle.getString("Vendedor"));
		tUsuario.setEditable(true);
		tUsuario.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		tUsuario.setTextLimit(8);
		lPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lPassword.setText(bundle.getString("Contraseña"));
		//TODO quitar esto
		tUsuario.setText("00000002");
		tPassword.setText("blabla");
		
		//TODO ¿Y esto de aquí debajo para qué lo puse?
		//tPassword.setEditable(true);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

		bAceptar.setText(bundle.getString("Aceptar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 4, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 4, 1));

		// Un SelectionAdapter con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				botonPulsado=0;
				dialog.dispose();	
			}
		};

		// Un SelectionAdapter con lo que hace el botón bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tUsuario.getText().length()!=8) {
					MessageBox messageBox = new MessageBox (dialog, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I01_err_NumVendedor1"));
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tUsuario y seleccionar texto
					tUsuario.setFocus();
					tUsuario.selectAll();
				}
				else { 
					try {
						numeroVendedor = Integer.valueOf(tUsuario.getText());
						password = tPassword.getText();
						botonPulsado=1;
						dialog.dispose();
					} catch (NumberFormatException exception) {
						MessageBox messageBox = new MessageBox (dialog, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText (bundle.getString("Error"));
						messageBox.setMessage (bundle.getString("I01_err_NumVendedor2"));
						e.doit = messageBox.open () == SWT.YES;
						// Enfocar tUsuario y seleccionar texto
						tUsuario.setFocus();
						tUsuario.selectAll();
					}

				}
			}
		};

		bCancelar.addSelectionListener(sabCancelar);
		bAceptar.addSelectionListener(sabAceptar);

		// Botón por defecto bAceptar
		dialog.setDefaultButton(bAceptar);

		// Ajustar el tamaño de la ventana al contenido
		dialog.setBackgroundImage(fondo);
		dialog.setSize(500,374);

		// Mostrar ventana centrada sobre la pantalla
		dialog.setLocation(padre.getDisplay().getClientArea().width/2 - dialog.getSize().x/2, padre.getDisplay().getClientArea().height/2 - dialog.getSize().y/2);
		dialog.open();
	}

	/**
	 * Devuelve el número de vendedor introducido por el usuario.
	 * @return un entero con el número de vendedor
	 */
	public int getNumeroVendedor() {
		return numeroVendedor;
	}
	
	/**
	 * Devuelve la clave introducida por el usuario.
	 * @return un String con la clave
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Devuelve el botón que se ha pulsado
	 * @return	<li>-1 - Ningún botón
	 * 			<li> 0 - Cancelar
	 * 			<li> 1 - Aceptar
	 */
	public int getBotonPulsado() {
		return botonPulsado;
	}
}