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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class I02PeticionFecha {
	private Shell padre = null;
	private Text texto_asociado;
	
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
		
		calendario.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String [] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
				final String fecha2=(String.valueOf(calendario.getDay()) + " de " + meses[calendario.getMonth()]+ " de " + String.valueOf(calendario.getYear()));
				texto_asociado.setText(fecha2);
				shell.dispose();
			}
			
		});
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));
		

		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
}
