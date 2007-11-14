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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class I02PeticionFecha {
	private Shell padre = null;
	private Text texto_asociado;
	private boolean seleccionado=false;
	
	public I02PeticionFecha(Shell padre,Text texto) {
		this.padre = padre;
		texto_asociado=texto;
		mostrarVentana();
	}
	
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
		
		

		//Introducimos los textos a los botones
		final DateTime calendario = new DateTime (shell, SWT.CALENDAR);
		final String fecha3;
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));
		
		
		
		final Composite cAcepCanc = new Composite (shell, SWT.BORDER);
		cAcepCanc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		GridLayout lAcepCanc = new GridLayout();
		lAcepCanc.numColumns = 2;
		cAcepCanc.setLayout(lAcepCanc);
		
		
		
		final Button bEnviar	= new Button(cAcepCanc, SWT.PUSH);
		bEnviar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bEnviar.setText("Enviar");
		//Introducimos los valores y eventos de Fecha Inicio
		bEnviar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
			//	I02PeticionFecha ventana = new I02PeticionFecha(shell,cFechaInicio);
				final String fecha2;
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				fecha2=(String.valueOf(calendario.getDay()) + " de " + meses[calendario.getMonth()]+ " de " + String.valueOf(calendario.getYear()));					
				texto_asociado.setText(fecha2);
				shell.dispose();
			}				
		});
		
		final Button bCancelar	= new Button(cAcepCanc, SWT.PUSH);
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bCancelar.setText("Cancelar");
		//Introducimos los valores y eventos de Fecha Inicio
		bCancelar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				shell.dispose();
			}				
		});
		

		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}
