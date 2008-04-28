package interfaces.admin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aplicacion.Vista;
import aplicacion.utilidades.EncriptCadena;
import aplicacion.utilidades.Util;

//import aplicacion.Vista;

/**
 * Clase que crea una ventana para introducir datos de configuracion relativos a
 * la base de datos
 * 
 * @author Jose Maria Martin
 */

public class ShellInfoBD {
	private Shell _padre = null;

	private ResourceBundle _bundle;
	
	private Vista _vista;

	public ShellInfoBD(Shell padre, ResourceBundle bundle, Vista vista) {
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
		// Establecemos el layout del shell		
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 2;
		shell.setLayout(lShell);	
		shell.setText(this._bundle.getString("I30_label_ip"));
		shell.setImage(_vista.getImagenes().getIcoPq());
		final Label db_label = new Label(shell, SWT.LEFT);
		final Text db_text = new Text(shell, SWT.LEFT | SWT.BORDER);
		GridData data = new GridData();
		data.widthHint = 100;
		db_label.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		db_text.setLayoutData(data);
		final Label IP_label = new Label(shell, SWT.LEFT);
		final Text IP_text = new Text(shell, SWT.LEFT | SWT.BORDER);
		data = new GridData();
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
		final Text password_text = new Text(shell, SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
		data = new GridData();
		data.widthHint = 100;
		password_label.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		password_text.setLayoutData(data);
		final Label passwordAdmin_label = new Label(shell, SWT.LEFT);		
		final Text passwordAdmin_text = new Text(shell, SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
		data = new GridData();
		data.widthHint = 100;
		passwordAdmin_label.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		passwordAdmin_text.setLayoutData(data);
		
		final Button bAceptar = new Button(shell, SWT.PUSH);
		final Button bCancelar = new Button(shell, SWT.PUSH);

		db_label.setText(this._bundle.getString("I30_label_db_name"));
		IP_label.setText(this._bundle.getString("I30_label_ip"));
		username_label.setText(this._bundle.getString("I30_label_username"));
		password_label.setText(this._bundle.getString("I30_label_password"));
		passwordAdmin_label.setText(this._bundle.getString("I30_label_passwordAdmin"));
		
		FileInputStream is;
		try {
			is = new FileInputStream(Util.rutaTrabajo() + File.separator + "configBD");
			DataInputStream dis = new DataInputStream(is);
			String name = dis.readUTF();
			String ip= dis.readUTF();
			String username=dis.readUTF();
			String contrasena=EncriptCadena.desencripta(dis.readUTF());
			String descodificacion=EncriptCadena.desencripta(dis.readUTF());
			System.out.println(ip);
			System.out.println(username);
			System.out.println(contrasena);
			System.out.println(descodificacion);
			System.out.println(name);
			db_text.setText(name);
			IP_text.setText(ip);
			username_text.setText(username);
			password_text.setText(contrasena);
			passwordAdmin_text.setText(descodificacion);
			
		} catch (FileNotFoundException e1) {
			System.out.println("No se encuentra el archivo");
			e1.printStackTrace();
		} catch (IOException e2) {
			System.out.println("Error de entrada salida");
			e2.printStackTrace();
		}
		bAceptar.setText(this._bundle.getString("Aceptar"));
		bCancelar.setText(this._bundle.getString("Cancelar"));
		
//		Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("aceptar pulsado");
				String name=db_text.getText();
				String ip=IP_text.getText();
				String username=username_text.getText();
				String password=password_text.getText();
				String admin=passwordAdmin_text.getText();
				if ((ip=="")||(username=="")||(password=="")||(admin=="")||(name=="")) {
					MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
					messageBox.setText ("Error");
					messageBox.setMessage (_bundle.getString("I30_err_empty"));
					messageBox.open();
				}
				else {	
					FileOutputStream os;
					try {
						os = new FileOutputStream(Util.rutaTrabajo() + File.separator + "configBD");
						DataOutputStream dos = new DataOutputStream(os);
						dos.writeUTF(name);
						dos.writeUTF(ip);
						dos.writeUTF(username);
						String codificacionPassword=EncriptCadena.encripta(password);
						dos.writeUTF(codificacionPassword);
						String codificacionAdmin=EncriptCadena.encripta(admin);
						dos.writeUTF(codificacionAdmin);
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION | SWT.OK);
						messageBox.setText ("Info");
						messageBox.setMessage (_bundle.getString("I30_cambios_ok"));
						messageBox.open();
					} catch (FileNotFoundException e1) {
						System.out.println("No se encuentra el archivo");
						e1.printStackTrace();
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
						messageBox.setText ("Error");
						messageBox.setMessage (_bundle.getString("I30_cambios_mal"));
						messageBox.open();
					} catch (IOException e2) {
						System.out.println("Error de entrada salida");
						e2.printStackTrace();
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
						messageBox.setText ("Error");
						messageBox.setMessage (_bundle.getString("I30_cambios_mal"));
						messageBox.open();
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
//
//	public static void main(String []args){
//		LanguageChanger l = new LanguageChanger();
//
//		ResourceBundle bundle = l.getBundle();
//		
//		I30_Info_BD ventana=new I30_Info_BD(null,bundle,null);
//
//	}
//
}
