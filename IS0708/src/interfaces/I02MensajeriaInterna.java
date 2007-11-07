package interfaces;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class I02MensajeriaInterna {
	private Shell padre = null;

	private final static int NO_INICIALIZADO=0;
	private final static int MENSAJERIA_INSTANTANEA=1;
	private final static int PETICION_BAJA=2;
	private final static int CAMBIO_HORARIO=3;

	
	private int opcion_actual= NO_INICIALIZADO;
	
	public I02MensajeriaInterna(Shell padre) {
		this.padre = padre;
		mostrarVentana();
	}
	
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_mens_l = new Image(padre.getDisplay(), I02.class.getResourceAsStream("ico_mens1_v.gif"));
		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setText("Mensajeria Interna:");
		shell.setImage(ico_mens_l);
		
		/*    */

		final Composite cGrupo = new Composite (shell, SWT.BORDER);
		cGrupo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 1;
		cGrupo.setLayout(lGrupo);
		
		final Label lDestinatario	= new Label(cGrupo, SWT.LEFT);
		lDestinatario.setText("Destinatario: ");
		Combo cDestinatarios = new Combo(cGrupo, SWT.BORDER | SWT.READ_ONLY);
		cDestinatarios.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cDestinatarios.setItems(new String[] {"Ramon Alcarria Jareño", "Fernando Gonzalez Angulo", "Carlos Eskudero Palmeiro","Tomas Villar Sánchez"});
		cDestinatarios.select(0);
		final Label lAsunto	= new Label(cGrupo, SWT.LEFT);
		lAsunto.setText("Asunto: ");
		final Text  tAsunto	= new Text (cGrupo, SWT.BORDER);
		tAsunto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		final Label lMensaje	= new Label(cGrupo, SWT.LEFT);
		lMensaje.setText("Mensaje: ");
		final Text  tMensaje	= new Text (cGrupo, SWT.MULTI | SWT.WRAP);
		tMensaje.setSize(100,200);
		tMensaje.computeSize(100, 100, true);
		tMensaje.setBounds(0, 0, 100, 100);
		tMensaje.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		
		final Composite cAceptarCancelar = new Composite (shell, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botones aceptar y cancelar
		final Button bAceptar		= new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptarCancelar, SWT.PUSH);
		
		//Introducimos los textos a los botones
		bAceptar.setText("Aceptar");
		bCancelar.setText("Cancelar");
				//Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println(opcion_actual);
				if(opcion_actual==NO_INICIALIZADO){
					System.out.println("hola");
					//mostrar pantalla indicandolo
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText ("Mensaje");
					messageBox.setMessage ("Seleccione un tipo de mensaje");
					e.doit = messageBox.open () == SWT.CLOSE;
				}
				if(opcion_actual==MENSAJERIA_INSTANTANEA){
					System.out.println("HOLA1");
					//mostrar pantalla indicandolo

				}
				if(opcion_actual==PETICION_BAJA){
					System.out.println("HOLA2");
					//mostrar pantalla indicandolo

				}
				if(opcion_actual==CAMBIO_HORARIO){
					System.out.println("HOLA3");
					//mostrar pantalla indicandolo

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
		
		// Botón por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}
