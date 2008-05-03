package interfaces.empleado;

import interfaces.I16_Peticion_baja;
import interfaces.I18_Cambio_horario;
import interfaces.general.mensajeria.ShellEscribirMensaje;
import interfaces.general.mensajeria.ShellMensajeNuevo;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import aplicacion.datos.Empleado;

public class CambiarDatos {
	/**
	 * Ventana que llama a la de esta clase
	 */
	private Shell _padre = null;
	private ResourceBundle _bundle;
	private Vista _vista;
	private String scontraseña;
	private String srepe_contraseña;

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
		
		final Composite cGrupo = new Composite (shell, SWT.NONE);
		cGrupo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 1;
		cGrupo.setLayout(lGrupo);
		
		int idioma= this._vista.getEmpleadoActual().getIdioma();
		



		final Composite cGrupo2 = new Composite (cGrupo, SWT.NONE);
		cGrupo2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 0, 0));
		GridLayout lGrupo2 = new GridLayout();
		lGrupo2.numColumns = 1;
		cGrupo2.setLayout(lGrupo2);
		final Label lcontraseñaActual	= new Label(cGrupo2, SWT.LEFT);
		lcontraseñaActual.setText(_bundle.getString("ContrasenaActual"));

		final Text tcontraseñaActual =new Text (cGrupo2, SWT.BORDER);
		tcontraseñaActual.setEchoChar('*');
		tcontraseñaActual.setTextLimit(10);
		tcontraseñaActual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		final Label lcontraseña	= new Label(cGrupo2, SWT.NONE);
		lcontraseña.setText(_bundle.getString("ContrasenaNueva"));
		final Text tcontraseña =new Text (cGrupo2, SWT.BORDER);
		tcontraseña.setEchoChar('*');
		tcontraseña.setTextLimit(10);
		tcontraseña.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));

		final Label lcontraseña2	= new Label(cGrupo2, SWT.NONE);
		lcontraseña2.setText(_bundle.getString("DeNuevoContrasena"));
		final Text tcontraseña2 =new Text (cGrupo2, SWT.BORDER);
		tcontraseña2.setEchoChar('*');
		tcontraseña2.setTextLimit(10);
		tcontraseña2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		

		
		final Label lseleccionIdioma	= new Label(cGrupo, SWT.LEFT);
		lseleccionIdioma.setText(_bundle.getString("ElegirIdioma"));
		
		final Composite cGrupo3 = new Composite (cGrupo, SWT.NONE);
		cGrupo3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lGrupo3 = new GridLayout();
		lGrupo3.numColumns = 3;
		cGrupo3.setLayout(lGrupo3);
		final Button espanol		= new Button(cGrupo3, SWT.RADIO);
		espanol.setText(_bundle.getString("Espanol"));
		final Button ingles		= new Button(cGrupo3, SWT.RADIO);
		ingles.setText(_bundle.getString("Ingles"));
		final Button Polaco		= new Button(cGrupo3, SWT.RADIO);
		Polaco.setText(_bundle.getString("Polaco"));
		
		Empleado empleado = _vista.getEmpleadoActual();
		if (empleado.getIdioma()==0) espanol.setSelection(true);
		if (empleado.getIdioma()==1) ingles.setSelection(true);
		if (empleado.getIdioma()==2) Polaco.setSelection(true);
		final Composite cAceptar = new Composite (cGrupo, SWT.NONE);
		cAceptar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lAceptar = new GridLayout();
		lAceptar.numColumns = 2;
		cAceptar.setLayout(lAceptar);
		final Button bAceptar		= new Button(cAceptar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptar, SWT.PUSH);
		bAceptar.setText( _bundle.getString("Aceptar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		bCancelar.setText(_bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				int idioma=1;
				String passw = tcontraseña.getText();
				if(espanol.getSelection()){idioma=0;}
				else if(ingles.getSelection()){idioma=1;}
				else if(Polaco.getSelection()){idioma=2;}
				if((tcontraseña.getText().length()>0)&&((tcontraseña.getText().compareTo(tcontraseña2.getText())==0))&&(((tcontraseñaActual.getText().compareTo(_vista.getEmpleadoActual().getPassword())==0)))){			

					System.out.println("idioma"+idioma);
					Empleado empleado = _vista.getEmpleadoActual();
					try{
						_vista.modificarEmpleado(empleado.getEmplId(), empleado.getNombre(),
							empleado.getApellido1(), empleado.getApellido2(),
							empleado.getFechaNac(), empleado.getSexo(),
							empleado.getEmail(),passw, empleado.getGrupo(), empleado.getFcontrato(),
							empleado.getFAlta(),empleado.getFelicidad(),idioma,empleado.getRango() ,
							empleado.getTurnoFavorito(),empleado.getContratoId(),empleado.getColor());
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Error"));
						messageBox.setMessage ("modificacion realizada");
						messageBox.open ();
					}
					catch(Exception exc){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Error"));
						messageBox.setMessage ("error en la modificacion, inténtelo mas tarde");
						messageBox.open ();
					}
					shell.dispose();
				}
				else{
					if((tcontraseñaActual.getText().compareTo(_vista.getEmpleadoActual().getPassword())!=0)){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Error"));
						messageBox.setMessage ("contraseña antigua incorrecta");
						messageBox.open ();
					}
					else if(((tcontraseña.getText().length()==0))&&(tcontraseña2.getText().length()==0)){
						Empleado empleado = _vista.getEmpleadoActual();
						_vista.modificarEmpleado(empleado.getEmplId(), empleado.getNombre(),
								empleado.getApellido1(), empleado.getApellido2(),
								empleado.getFechaNac(), empleado.getSexo(),
								empleado.getEmail(),empleado.getPassword(), empleado.getGrupo(), empleado.getFcontrato(),
								empleado.getFAlta(),empleado.getFelicidad(),idioma,empleado.getRango() ,
								empleado.getTurnoFavorito(),empleado.getContratoId(),empleado.getColor());
							MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
							messageBox.setText (_bundle.getString("Error"));
							messageBox.setMessage ("Idioma Modificado");
							messageBox.open ();
							shell.dispose();
					}
					else if(((tcontraseña.getText().compareTo(tcontraseña2.getText())!=0))&&(tcontraseña.getText().length()>0)){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Error"));
						messageBox.setMessage ("la nueva contraseña no coincide");
						messageBox.open ();
					}
					else if((tcontraseña.getText().length()==0)&&(tcontraseña2.getText().length()==0)){
						Empleado empleado = _vista.getEmpleadoActual();
						try{
							_vista.modificarEmpleado(empleado.getEmplId(), empleado.getNombre(),
								empleado.getApellido1(), empleado.getApellido2(),
								empleado.getFechaNac(), empleado.getSexo(),
								empleado.getEmail(),empleado.getPassword(), empleado.getGrupo(), empleado.getFcontrato(),
								empleado.getFAlta(),empleado.getFelicidad(),idioma,empleado.getRango() ,
								empleado.getTurnoFavorito(),empleado.getContratoId(),empleado.getColor());
							MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
							messageBox.setText (_bundle.getString("Error"));
							messageBox.setMessage ("modificacion realizada");
							messageBox.open ();
						}
						catch(Exception exc){
							MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
							messageBox.setText (_bundle.getString("Error"));
							messageBox.setMessage ("error en la modificacion, inténtelo mas tarde");
							messageBox.open ();
						}
						shell.dispose();
					}
					else if(tcontraseña.getText().length()==0){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Error"));
						messageBox.setMessage ("la nueva contraseña esta vacía");
						messageBox.open ();	
					}
				}
			}				
		});
		bCancelar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				shell.dispose();
			}				
		});

		
		// Bot�n por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(_padre.getBounds().width/2 + _padre.getBounds().x - shell.getSize().x/2, _padre.getBounds().height/2 + _padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}

}
