package interfaces;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Clase que crea la ventana de eleccion de tipo de mensaje
 * @author David Rodilla
 */
public class I02MensajeNuevo {
	/**
	 * Ventana que llama a la de esta clase
	 */
	private Shell padre = null;

	/**Constante para indicar que la opcion actual no esta inicializada aun */
	private final static int NO_INICIALIZADO=0;
	
	/**Constante para indicar que la opcion actual es enviar
	 *  mensaje por mensajeria interna */
	private final static int MENSAJERIA_INTERNA=1;
	/**Constante para indicar que la opcion actual es enviar
	 *  mensaje de peticion de baja */
	private final static int PETICION_BAJA=2;
	/**Constante para indicar que la opcion actual es enviar
	 *  mensaje de peticion cambio de horario */
	private final static int CAMBIO_HORARIO=3;

	/** Entero que indica la opcion seleccionada en un momento */
	private int opcion_actual= NO_INICIALIZADO;
	
	/** Constructor
	 * @param padre ventana que llama a la actual
	 */
	public I02MensajeNuevo(Shell padre) {
		this.padre = padre;
		mostrarVentana();
	}
	
	/**
	 * Metodo que muestra por pantalla la nueva ventana
	 */
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_mens_l = new Image(padre.getDisplay(), I02.class.getResourceAsStream("ico_mens1_v.gif"));
		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setText("Tipo de mensaje :");
		shell.setImage(ico_mens_l);
		
		/*  a continuacion 3 radio butons para la eleccion del tipo de mensaje  */
		
		//Boton para la mensajeria interna
		final Button bMInterna		= new Button(shell, SWT.RADIO);
		//Boton para la peticion de baja laboral
		final Button bPetBaja		= new Button(shell, SWT.RADIO);
		//Boton para la peticion del cambio de horario
		final Button bPCHorario		= new Button(shell, SWT.RADIO);
		
		
		final Composite cAceptarCancelar = new Composite (shell, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botones aceptar y cancelar
		final Button bAceptar		= new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptarCancelar, SWT.PUSH);
		
		//Introducimos los textos a los botones
		bMInterna.setText("Mensajeria Interna");
		bPetBaja.setText("Peticion de Baja");
		bPCHorario.setText("Peticion de cambio de horario");
		bAceptar.setText("Aceptar");
		bCancelar.setText("Cancelar");
		
		//Introducimos los valores y eventos de peticion de Mensajeria interna
		bMInterna.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bMInterna.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion mensajeria interna in");
				opcion_actual=I02MensajeNuevo.MENSAJERIA_INTERNA;
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion mensajeria interna out");
				
			}
		}
		);

		
		
		//Introducimos los valores y eventos de peticion de baja
		bPetBaja.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bPetBaja.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion baja in");
				opcion_actual=I02MensajeNuevo.PETICION_BAJA;
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion baja out");
				
			}
		}
		);
		
		//Introducimos los valores y eventos de peticion de cambio de horario
		bPCHorario.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion cambio horario in");
				opcion_actual=I02MensajeNuevo.CAMBIO_HORARIO;
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion cambio horario out");
				
			}
		}
		);
		
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
				if(opcion_actual==MENSAJERIA_INTERNA){
					I02MensajeriaInterna ventana = new I02MensajeriaInterna(padre);
					shell.dispose();

				}
				if(opcion_actual==PETICION_BAJA){
					I02PeticionBaja ventana = new I02PeticionBaja(padre);
					shell.dispose();

				}
				if(opcion_actual==CAMBIO_HORARIO){
					I02CambioHorario ventana = new I02CambioHorario(padre);
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
		
		// Botón por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}
