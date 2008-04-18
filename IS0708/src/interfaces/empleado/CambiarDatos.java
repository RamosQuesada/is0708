package interfaces.empleado;

import interfaces.I16_Peticion_baja;
import interfaces.I18_Cambio_horario;
import interfaces.general.mensajeria.ShellEscribirMensaje;
import interfaces.general.mensajeria.ShellMensajeNuevo;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aplicacion.Vista;

public class CambiarDatos {
	/**
	 * Ventana que llama a la de esta clase
	 */
	private Shell _padre = null;
	private ResourceBundle _bundle;
	private Vista _vista;

	public CambiarDatos(Shell shell, ResourceBundle bundle, Vista vista) {
		_padre=shell;
		_bundle=bundle;
		_vista=vista;
		mostrarVentana();
	}
	
	/**
	 * Metodo que muestra por pantalla la nueva ventana
	 */
	public void mostrarVentana() {
		final Shell shell = new Shell (_padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setText(this._bundle.getString("I02_but_cambiarDatos"));
		shell.setImage(_vista.getImagenes().getIco_mens_l());
		
		/*  a continuacion 3 radio butons para la eleccion del tipo de mensaje  */
		
		final Composite cGrupo = new Composite (shell, SWT.BORDER);
		cGrupo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 2;
		cGrupo.setLayout(lGrupo);
		final Label lcontraseña	= new Label(cGrupo, SWT.LEFT);
		lcontraseña.setText(_bundle.getString("Contrasena"));
		final Text tcontraseña =new Text (cGrupo, SWT.BORDER);
		tcontraseña.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		final Label lcontraseña2	= new Label(cGrupo, SWT.LEFT);
		lcontraseña2.setText(_bundle.getString("DeNuevoContrasena"));
		final Text tcontraseña2 =new Text (cGrupo, SWT.BORDER);
		tcontraseña2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));		
		final Composite cGrupo2 = new Composite (cGrupo, SWT.BORDER);
		cGrupo2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lGrupo2 = new GridLayout();
		lGrupo2.numColumns = 3;
		cGrupo2.setLayout(lGrupo2);
		
		final Button espanol		= new Button(cGrupo2, SWT.RADIO);
		espanol.setText(_bundle.getString("Espanol"));
		final Button ingles		= new Button(cGrupo2, SWT.RADIO);
		ingles.setText(_bundle.getString("Ingles"));
		final Button Polaco		= new Button(cGrupo2, SWT.RADIO);
		Polaco.setText(_bundle.getString("Polaco"));

		
		// Bot�n por defecto bAceptar
		//shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(_padre.getBounds().width/2 + _padre.getBounds().x - shell.getSize().x/2, _padre.getBounds().height/2 + _padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}

}
