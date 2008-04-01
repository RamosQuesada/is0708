package interfaces;
import idiomas.LanguageChanger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aplicacion.Vista;

//import aplicacion.Vista;

/**
 * Clase que crea una ventana para introducir datos de configuraci�n relativos a
 * la base de datos
 * 
 * @author Jose Maria Martin
 */

public class I30_Info_BD {
	private Shell _padre = null;

	private ResourceBundle _bundle;
	
	private Vista _vista;

	public I30_Info_BD(Shell padre, ResourceBundle bundle, Vista vista) {
		_padre = padre;
		_bundle = bundle;
		_vista = vista;
		mostrarVentana();
	}

	/**
	 * Metodo que muestra por pantalla la nueva ventana
	 */
	public void mostrarVentana() {
		final Shell shell = new Shell(_padre, SWT.CLOSE | SWT.APPLICATION_MODAL);
		final Image icono = new Image(null, I30_Info_BD.class.getResourceAsStream("icoPq.gif"));
		// Establecemos el layout del shell		
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 2;
		shell.setLayout(lShell);	
		shell.setText(this._bundle.getString("I30_label_ip"));
		shell.setImage(icono);
		final Label IP_label = new Label(shell, SWT.LEFT);
		final Text IP_text = new Text(shell, SWT.LEFT | SWT.BORDER);
		GridData data = new GridData();
		data.widthHint = 100;
		IP_label.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		IP_text.setLayoutData(data);
		final Label username_label = new Label(shell, SWT.LEFT);
		final Text username_text = new Text(shell, SWT.LEFT | SWT.BORDER);
		data = new GridData();
		data.widthHint = 100;
		username_label.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		username_text.setLayoutData(data);
		final Label password_label = new Label(shell, SWT.LEFT);		
		final Text password_text = new Text(shell, SWT.LEFT | SWT.BORDER);
		data = new GridData();
		data.widthHint = 100;
		password_label.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		password_text.setLayoutData(data);
		final Label passwordAdmin_label = new Label(shell, SWT.LEFT);		
		final Text passwordAdmin_text = new Text(shell, SWT.LEFT | SWT.BORDER);
		data = new GridData();
		data.widthHint = 100;
		passwordAdmin_label.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		passwordAdmin_text.setLayoutData(data);
		
		//IP_label.setText("ServerIP");
		//username_label.setText("ServerUsername");
		//password_label.setText("ServerPassword");
		
		/*IP_label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		IP_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		username_label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		username_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		password_label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		password_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));*/

//		final Composite cAceptarCancelar = new Composite(shell, SWT.BORDER);
//		cAceptarCancelar.setBounds(100, 100, 200,200);
//		cAceptarCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
//				true, 1, 1));
//		GridLayout lAceptarCancelar = new GridLayout();
//		lAceptarCancelar.numColumns = 2;
//		lAceptarCancelar.makeColumnsEqualWidth=true;
//		cAceptarCancelar.setLayout(lAceptarCancelar);
//
//		// Botones aceptar y cancelar
//		final Button bAceptar = new Button(cAceptarCancelar, SWT.PUSH);
//		final Button bCancelar = new Button(cAceptarCancelar, SWT.PUSH);
		
		final Button bAceptar = new Button(shell, SWT.PUSH);
		final Button bCancelar = new Button(shell, SWT.PUSH);

		IP_label.setText(this._bundle.getString("I30_label_ip"));
		username_label.setText(this._bundle.getString("I30_label_username"));
		password_label.setText(this._bundle.getString("I30_label_password"));
		passwordAdmin_label.setText(this._bundle.getString("I30_label_passwordAdmin"));

		bAceptar.setText(this._bundle.getString("Aceptar"));
		bCancelar.setText(this._bundle.getString("Cancelar"));
		
//		Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("aceptar pulsado");	
				String ip=IP_text.getText();
				String username=username_text.getText();
				String password=password_text.getText();
				String admin=passwordAdmin_text.getText();
				if ((ip=="")||(username=="")||(password=="")||(admin=="")) {
					/*final Shell shell2 = new Shell(shell, SWT.CLOSE | SWT.APPLICATION_MODAL);
					GridLayout lShell2 = new GridLayout();
					lShell2.numColumns = 1;
					shell2.setLayout(lShell2);
					shell2.setText("Error");
					final Label label = new Label(shell2, SWT.CENTER);
					label.setText("No deje campos en blanco");
					final Button bAceptar2 = new Button(shell2, SWT.PUSH);
					bAceptar2.setText("Aceptar");
					bAceptar2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					bAceptar2.addSelectionListener (new SelectionAdapter () {
						public void widgetSelected (SelectionEvent e) {
							shell2.dispose();
						}
					});
					shell2.setDefaultButton(bAceptar2);
					// Ajustar el tama�o de la ventana al contenido
					shell2.pack();
					shell2.setLocation(shell.getLocation());
					shell2.open();
					shell2.setVisible(true);
					while (!shell2.isDisposed()) {
						if (!shell2.getDisplay().readAndDispatch()) {
							shell2.getDisplay().sleep();
						}
					}*/
					MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
					messageBox.setText ("Error");
					messageBox.setMessage (_bundle.getString("I30_err_empty"));
					messageBox.open();
				}
				else {	
					FileOutputStream os;
					try {
						os = new FileOutputStream("src"+File.separator+"interfaces"+File.separator+"configBD");
						DataOutputStream dos = new DataOutputStream(os);
						dos.writeUTF(ip);
						dos.writeUTF(username);
						dos.writeUTF(password);
						dos.writeUTF(admin);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						System.out.println("No se encuentra el archivo");
						e1.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						System.out.println("Error de entrada salida");
						e2.printStackTrace();
					}  
					shell.dispose();
				}
			}
		});
		
		//Introducimos los valores y eventos de Cancelar
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bCancelar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				shell.dispose();
			}				
		});
		
		// Boton por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		if (_padre != null)
			// Mostrar ventana centrada sobre el padre
			shell.setLocation(_padre.getBounds().width / 2
					+ _padre.getBounds().x - shell.getSize().x / 2, _padre
					.getBounds().height
					/ 2 + _padre.getBounds().y - shell.getSize().y / 2);
		else
			shell.setLocation(400, 400);
		
		shell.open();
		shell.setVisible(true);
//		 Este bucle mantiene la ventana abierta
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}

	public static void main(String []args){
		LanguageChanger l = new LanguageChanger();

		ResourceBundle bundle = l.getBundle();
		
		I30_Info_BD ventana=new I30_Info_BD(null,bundle,null);

	}

}
