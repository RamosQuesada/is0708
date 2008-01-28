package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aplicacion.Mensajeria;
import aplicacion.Mensaje;
import aplicacion.Vista;

import java.util.ResourceBundle;

public class I14_Escribir_mensaje {
	private Shell _padre = null;
	private ResourceBundle _bundle;
	private Vista _vista;
	private Shell shell;
	private I13_Elegir_empleado tNombre;
	
	public I14_Escribir_mensaje(Shell padre, ResourceBundle bundle, Vista vista, Mensaje mensaje, int idEmpl, String destinatario) {
		_padre = padre;
		_bundle = bundle;
		_vista = vista;
		mostrarVentana(mensaje, idEmpl, destinatario);
		
	}

	public void mostrarVentana(Mensaje mens, int idEmpleado, String destinatario) {
		final Mensaje mensaje = mens;
		final int idEmpl = idEmpleado;
		shell = new Shell (_padre, SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);
		final Image ico_mens_l = new Image(_padre.getDisplay(), I14_Escribir_mensaje.class.getResourceAsStream("ico_mens1_v.gif"));
		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setImage(ico_mens_l);
		if (mensaje==null)
			shell.setText(_bundle.getString("I14_tit_escribirMensaje"));
		else
			shell.setText(_bundle.getString("I14_tit_leerMensaje"));
		

		final Composite cDatosMensaje = new Composite (shell, SWT.BORDER);
		cDatosMensaje.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		cDatosMensaje.setLayout(new GridLayout(1,false));
		// Destinatario y asunto
		Label lNombre = new Label(cDatosMensaje, SWT.LEFT);
		lNombre.setLayoutData(new GridData(SWT.LEFT,  SWT.CENTER, false, true, 1, 1));
		final Label lAsunto	= new Label(cDatosMensaje, SWT.LEFT);
		final Text tAsunto;
		if (mensaje==null) {
			if (idEmpl==0) {
				lNombre.setText(_bundle.getString("I14_lab_Para"));
				tNombre = new I13_Elegir_empleado(cDatosMensaje,_vista, _bundle);
			}
			else {
				lNombre.setText(_bundle.getString("I14_lab_Para")+ ": " + destinatario);
			}
			lAsunto.setText(_bundle.getString("Asunto"));
			lAsunto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
			tAsunto	= new Text (cDatosMensaje, SWT.BORDER);
			tAsunto.setTextLimit(100);
			tAsunto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		}
		else {
			lNombre.setText(_bundle.getString("I14_lab_De")+ ": " + mensaje.getRemitente());
			lAsunto.setText(_bundle.getString("Asunto") +": " + mensaje.getAsunto());
			lAsunto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
			tAsunto = null;
		}
		// TODO informar de cuándo fue enviado
		final Composite cCuerpoMen = new Composite (shell, SWT.BORDER);
		cCuerpoMen.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cCuerpoMen.setLayout(new GridLayout(1, false));
		final Label lMensaje	= new Label(cCuerpoMen, SWT.LEFT);		
		lMensaje.setText(_bundle.getString("Mensaje"));
		final Text tMensaje;
		if (mensaje==null) {
			tMensaje = new Text (cCuerpoMen, SWT.BORDER|SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
			tMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		}
		else {
			tMensaje = new Text (cCuerpoMen, SWT.BORDER|SWT.MULTI | SWT.WRAP| SWT.V_SCROLL | SWT.READ_ONLY);
			tMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			tMensaje.setText(mensaje.getTexto());
		}
		final Composite cAceptarCancelar = new Composite (shell, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.FILL, SWT.DOWN, true, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botón aceptar
		final Button bAceptar = new Button(cAceptarCancelar, SWT.PUSH);
		if (mensaje==null)
			bAceptar.setText(_bundle.getString("Enviar"));
		else
			bAceptar.setText(_bundle.getString("Cerrar"));
		bAceptar.setFocus();
		
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		if (mensaje==null) {
			final Button bCancelar = new Button(cAceptarCancelar, SWT.PUSH);
			bCancelar.setText(_bundle.getString("Cancelar"));
			
			//Introducimos los valores y eventos de Cancelar
			bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			bCancelar.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					shell.dispose();
				}				
			});
			bAceptar.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					if((tAsunto.getCharCount()==0)&&(tMensaje.getCharCount()==0)){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Mensaje"));
						messageBox.setMessage (_bundle.getString("sintextoasun"));
						if( messageBox.open () == SWT.YES){
							shell.dispose();
						}
					}	
					else if(tAsunto.getCharCount()==0){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Mensaje"));
						messageBox.setMessage (_bundle.getString("sinasunto"));
						if( messageBox.open () == SWT.YES){
							shell.dispose();
						}
					}	
					else if(tMensaje.getCharCount()==0){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Mensaje"));
						messageBox.setMessage (_bundle.getString("sintexto"));
						if( messageBox.open () == SWT.YES){
							shell.dispose();
						}
					}
					else if (idEmpl==0 && tNombre.getIdEmpl()==0) {
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
						messageBox.setText (_bundle.getString("Error"));
						messageBox.setMessage (_bundle.getString("I14_err_sinDestinatario"));
						messageBox.open ();
					}
					else {
						Mensajeria m = new Mensajeria(_vista.getControlador(), _vista.getEmpleadoActual().getEmplId());
						int destino = idEmpl;
						if (idEmpl==0) destino = tNombre.getIdEmpl();
						if (m.creaMensaje(destino, tAsunto.getText(), tMensaje.getText())>-1) {
							MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
							messageBox.setText (_bundle.getString("Enviado"));
							messageBox.setMessage (_bundle.getString("I14_lab_Enviado"));
							messageBox.open ();
						}
						else {
							MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
							messageBox.setText (_bundle.getString("Error"));
							messageBox.setMessage (_bundle.getString("I14_lab_NoEnviado"));
							messageBox.open ();
						}
						shell.dispose();
					}
				}	
			});
		}
		else {
			final Button bResponder = new Button(cAceptarCancelar, SWT.PUSH);
			bResponder.setText(_bundle.getString("I14_but_Responder"));
			bResponder.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					shell.dispose();
					new I14_Escribir_mensaje(_padre,_bundle,_vista,null,mensaje.getRemitente(),_vista.getEmpleado(mensaje.getRemitente()).getNombreCompleto());
				}
			});
			
			bAceptar.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					shell.dispose();
				}
			});
		}
		
		// Botón por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tamaño de la ventana al contenido
		
		shell.setSize(400,300);
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(_padre.getBounds().width/2 + _padre.getBounds().x - shell.getSize().x/2, _padre.getBounds().height/2 + _padre.getBounds().y - shell.getSize().y/2);
		shell.open();
		if (mensaje==null) {
		shell.addListener (SWT.Close, new Listener () {
				public void handleEvent (Event e) {
					if(!(tAsunto.getCharCount()==0)||!(tMensaje.getCharCount()==0)){
						MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
						messageBox.setText (_bundle.getString("Mensaje"));
						messageBox.setMessage (_bundle.getString("salimensaje"));
						e.doit = (messageBox.open () == SWT.YES);
					}	
				
				}
			});
		}
	}
}
