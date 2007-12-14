package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aplicacion.Vista;

import java.util.Locale;
import java.util.ResourceBundle;

public class I14_Mensajeria_Interna {
	private Shell _padre = null;
	private ResourceBundle _bundle;

	private final static int NO_INICIALIZADO=0;
	private final static int MENSAJERIA_INSTANTANEA=1;
	private final static int PETICION_BAJA=2;
	private final static int CAMBIO_HORARIO=3;

	
	private int opcion_actual= NO_INICIALIZADO;
	
	public I14_Mensajeria_Interna(Shell padre	,ResourceBundle bundle) {
		this._padre = padre;
		this._bundle=bundle;
		mostrarVentana();
	}
	
	public void mostrarVentana() {
		final Shell shell = new Shell (_padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_mens_l = new Image(_padre.getDisplay(), I14_Mensajeria_Interna.class.getResourceAsStream("ico_mens1_v.gif"));
		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setText(_bundle.getString("mensajeriaint"));
		shell.setImage(ico_mens_l);
		
		/*    */

		final Composite cGrupo = new Composite (shell, SWT.BORDER);
		cGrupo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 1;
		cGrupo.setLayout(lGrupo);
		
		final Label lDestinatario	= new Label(cGrupo, SWT.LEFT);
		lDestinatario.setText(_bundle.getString("destinatario"));
		Combo cDestinatarios = new Combo(cGrupo, SWT.BORDER | SWT.READ_ONLY);
		cDestinatarios.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		cDestinatarios.setItems(new String[] {"Ramon Alcarria Jare�o", "Fernando Gonzalez Angulo", "Carlos Eskudero Palmeiro","Tomas Villar S�nchez"});
		cDestinatarios.select(0);
		final Label lAsunto	= new Label(cGrupo, SWT.LEFT);
		lAsunto.setText(_bundle.getString("asunto"));
		final Text  tAsunto	= new Text (cGrupo, SWT.BORDER);
		tAsunto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));

		
		final Composite cCuerpoMen = new Composite (shell, SWT.BORDER);
		cCuerpoMen.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCuerpoMen = new GridLayout();
		lCuerpoMen.numColumns = 1;
		cCuerpoMen.setLayout(lCuerpoMen);
		final Label lMensaje	= new Label(cCuerpoMen, SWT.LEFT);		
		lMensaje.setText(_bundle.getString("Mensaje"));
		final Text  tMensaje	= new Text (cCuerpoMen, SWT.BORDER|SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
		tMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		
		
		
		
		final Composite cAceptarCancelar = new Composite (shell, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.LEFT, SWT.DOWN, true, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botones aceptar y cancelar
		final Button bAceptar		= new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptarCancelar, SWT.PUSH);
		
		//Introducimos los textos a los botones
		bAceptar.setText(_bundle.getString("enviar"));
		bCancelar.setText(_bundle.getString("cancelar1"));
				//Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println(opcion_actual);
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
				else{
					// TODO BD Guardar mensaje en la BD
					shell.dispose();
				}
			}	
		});
		
		//Introducimos los valores y eventos de Cancelar
		bCancelar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		bCancelar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				shell.dispose();
			}				
		});
		
		// Bot�n por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		
		shell.setSize(_padre.getSize());
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(_padre.getBounds().width/2 + _padre.getBounds().x - shell.getSize().x/2, _padre.getBounds().height/2 + _padre.getBounds().y - shell.getSize().y/2);
		shell.open();
		
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
