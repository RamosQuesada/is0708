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
 * Interfaz de usuario I-01 :: Identificaci�n
 * @author Daniel Dionne
 */
public class I01_Login extends Thread{
	private Shell padre = null;
	private ResourceBundle bundle;
	private int numeroVendedor;
	String password;
	public Shell dialog;
	private int botonPulsado;
	Database db;
	Text tEstado;
	ProgressBar pbProgreso;
	
	public void run() {
		// Conectar con la base de datos
		db.abrirConexion();
		if (!dialog.isDisposed())
			if (!db.conexionAbierta()) {
				dialog.getDisplay().asyncExec(new Runnable () {
					public void run() {
						MessageBox messageBox = new MessageBox(dialog, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
						messageBox.setText (bundle.getString("Error"));
						messageBox.setMessage (bundle.getString("I01_err_Conexion"));
					}
				});
			}
			else {
				dialog.getDisplay().asyncExec(new Runnable () {
					public void run() {
						// Rellenar la barra de progreso
						// Por alguna raz�n oculta de los threads, aun habiendo comprobado
						// antes si el dialog sigue presente, si no lo compruebo de nuevo
						// a veces da error.
						if (!dialog.isDisposed()) 
							pbProgreso.setSelection(100);
					}
				});
			}
		else {
			// En este caso se ha cerrado la aplicaci�n antes de que termine de conectar.			
			if (db.conexionAbierta())
				db.cerrarConexion();
		}
	}
	
	public I01_Login(Shell padre, ResourceBundle bundle, Database db) {
		this.padre = padre;
		this.bundle = bundle;
		this.db = db;
		numeroVendedor = 0;
		password = "";
		botonPulsado = -1;
		this.start();
	}
		
	/**	
	 * Crea la ventana
	 * @author Daniel Dionne
	 */
	public synchronized void mostrarVentana() {
		Image fondo = new Image(padre.getDisplay(), I01_Login.class.getResourceAsStream("intro.png"));
		dialog = new Shell (padre, SWT.NONE | SWT.APPLICATION_MODAL);
		
		// Esto hace que los labels no tengan fondo
		dialog.setBackgroundMode(SWT.INHERIT_DEFAULT);
		dialog.setLayout(new GridLayout());
		
		// Contenido de la ventana
		Composite contenido = new Composite(dialog, SWT.NONE);
		contenido.setLayout(new GridLayout(8,true));
		contenido.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
				
		final Label lUsuario  = new Label(contenido, SWT.LEFT);
		final Text  tUsuario  = new Text(contenido, SWT.BORDER);
		final Label lPassword = new Label(contenido, SWT.LEFT);
		final Text  tPassword = new Text(contenido, SWT.BORDER | SWT.PASSWORD);

		final Button bAceptar  = new Button(contenido, SWT.PUSH);
		final Button bCancelar = new Button(contenido, SWT.PUSH);

		// Dos iconos de tama�o diferente para SO's que los necesiten
		Image icoGr = new Image(padre.getDisplay(), I01_Login.class.getResourceAsStream("icoGr.gif"));
		Image icoPq = new Image(padre.getDisplay(), I01_Login.class.getResourceAsStream("icoPq.gif"));
		dialog.setImages(new Image[] {icoPq,icoGr});
		dialog.setText(bundle.getString("I01_tit_Ident"));
		
		// Contenido de la ventana		
		lUsuario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lUsuario.setText(bundle.getString("Vendedor"));
		tUsuario.setEditable(true);
		tUsuario.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		tUsuario.setTextLimit(8);
		lPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
		lPassword.setText(bundle.getString("Contrase�a"));
		//TODO quitar esto
		tUsuario.setText("00000002");
		tPassword.setText("blabla");
		
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

		bAceptar.setText(bundle.getString("Aceptar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 4, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 4, 1));

		//tEstado = new Text(contenido, SWT.LEFT);
		//tEstado.setText("Conectando con la base de datos");
		//tEstado.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 4, 1));

		pbProgreso = new ProgressBar(contenido,SWT.FILL);
		pbProgreso.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 8, 1));
		pbProgreso.setSelection(50);
		
		// Un SelectionAdapter con lo que hace el bot�n bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				botonPulsado=0;
				dialog.dispose();	
			}
		};

		// Un SelectionAdapter con lo que hace el bot�n bAceptar
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
						// bloquear interfaz, cursor esperar, bucle de espera
						dialog.setCursor(new Cursor(dialog.getDisplay(), SWT.CURSOR_WAIT));
						bAceptar.setEnabled(false);
						tUsuario.setEnabled(false);
						tPassword.setEnabled(false);
						// TODO esperar por si todav�a no ha terminado
						while (!db.conexionAbierta()) {
							if (!dialog.isDisposed() && !dialog.getDisplay().readAndDispatch()) {
								dialog.getDisplay().sleep();
							}
						}
						dialog.setCursor(new Cursor(dialog.getDisplay(), SWT.CURSOR_ARROW));
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

		// Bot�n por defecto bAceptar
		dialog.setDefaultButton(bAceptar);

		// Ajustar el tama�o de la ventana al contenido
		dialog.setBackgroundImage(fondo);
		dialog.setSize(500,400);

		// Mostrar ventana centrada sobre la pantalla
		dialog.setLocation(padre.getDisplay().getClientArea().width/2 - dialog.getSize().x/2, padre.getDisplay().getClientArea().height/2 - dialog.getSize().y/2);
		dialog.open();
	}

	/**
	 * Devuelve el n�mero de vendedor introducido por el usuario.
	 * @return un entero con el n�mero de vendedor
	 * @author Daniel Dionne
	 */
	public int getNumeroVendedor() {
		return numeroVendedor;
	}
	
	/**
	 * Devuelve la clave introducida por el usuario.
	 * @return un String con la clave
	 * @author Daniel Dionne
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Devuelve el bot�n que se ha pulsado
	 * @return	<li>-1 - Ning�n bot�n
	 * 			<li> 0 - Cancelar
	 * 			<li> 1 - Aceptar
	 * @author Daniel Dionne
	 */
	public int getBotonPulsado() {
		return botonPulsado;
	}
}