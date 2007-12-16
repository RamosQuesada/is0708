package interfaces;

import java.util.Locale;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import aplicacion.Vista;

/**
 * Clase que crea la ventana de eleccion de tipo de mensaje
 * @author David Rodilla
 */
public class I15_Mensaje_nuevo {
	/**
	 * Ventana que llama a la de esta clase
	 */
	private Shell _padre = null;
	
	private ResourceBundle _bundle;
	private Locale _locale;
	private Vista _vista;

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
	public I15_Mensaje_nuevo(Shell padre,ResourceBundle bundle, Locale locale, Vista vista) {
		_padre = padre;
		_bundle = bundle;
		_locale = locale;
		_vista = vista;
		mostrarVentana();
	}
	
	/**
	 * Metodo que muestra por pantalla la nueva ventana
	 */
	public void mostrarVentana() {
		final Shell shell = new Shell (_padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_mens_l = new Image(_padre.getDisplay(), I15_Mensaje_nuevo.class.getResourceAsStream("ico_mens1_v.gif"));
		
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
		bMInterna.setText(this._bundle.getString("I15_but_mensajeriaint"));
		bPetBaja.setText(this._bundle.getString("I15_but_peticionbaja"));
		bPCHorario.setText(this._bundle.getString("I15_but_peticioncambhorario"));
		bAceptar.setText(this._bundle.getString("Aceptar"));
		bCancelar.setText(this._bundle.getString("Cancelar"));
		
		//Introducimos los valores y eventos de peticion de Mensajeria interna
		bMInterna.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bMInterna.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion mensajeria interna in");
				opcion_actual=I15_Mensaje_nuevo.MENSAJERIA_INTERNA;
				
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
				opcion_actual=I15_Mensaje_nuevo.PETICION_BAJA;
				
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
				opcion_actual=I15_Mensaje_nuevo.CAMBIO_HORARIO;
				
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
					//mostrar pantalla indicandolo
					MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText (_bundle.getString("Mensaje"));
					messageBox.setMessage (_bundle.getString("selectipomens"));
					e.doit = messageBox.open () == SWT.CLOSE;
				}
				if(opcion_actual==MENSAJERIA_INTERNA){
					I14_Mensajeria_Interna ventana = new I14_Mensajeria_Interna(_padre,_bundle, _vista);
					shell.dispose();

				}
				if(opcion_actual==PETICION_BAJA){
					I16_Peticion_baja ventana = new I16_Peticion_baja(_padre,_bundle);
					shell.dispose();

				}
				if(opcion_actual==CAMBIO_HORARIO){
					I18_Cambio_horario ventana = new I18_Cambio_horario(_padre,_bundle);
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
		
		// Bot�n por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(_padre.getBounds().width/2 + _padre.getBounds().x - shell.getSize().x/2, _padre.getBounds().height/2 + _padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}
