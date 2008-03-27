package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
import java.sql.Date;

import aplicacion.Vista;

public class I17_Seleccion_fecha {
	private Shell padre = null;
	private Text texto_asociado;
	//private Double fechaNumerica;
	private DateTime calendario;
	private boolean seleccionado=false;
	private Date fecha;
	private Shell shell; 
	public I17_Seleccion_fecha(Shell padre) {
		this.padre = padre;
		mostrarVentana();
	}
	
	/**
	 * Devuelve la fecha seleccionada
	 * @return la fecha seleccionada
	 */
	public Date getFecha(){
		return fecha;
	}
	public void mostrarVentana() {
		shell = new Shell (padre, SWT.BORDER | SWT.APPLICATION_MODAL);
		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		
		//Introducimos los textos a los botones
		calendario = new DateTime (shell, SWT.CALENDAR);
		calendario.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));
		
		calendario.addMouseListener(new MouseListener () {
			public void mouseDoubleClick(MouseEvent e) {
				// TODO ¿Y esto por qué no va?
				fecha = new Date(calendario.getYear(),calendario.getMonth(),calendario.getDay());
				shell.dispose();
			}
			public void mouseUp(MouseEvent e) {};
			public void mouseDown(MouseEvent e) {};
		});
		final Composite cAcepCanc = new Composite (shell, SWT.BORDER);
		cAcepCanc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		GridLayout lAcepCanc = new GridLayout();
		lAcepCanc.numColumns = 2;
		cAcepCanc.setLayout(lAcepCanc);
		
		final Button bEnviar	= new Button(cAcepCanc, SWT.PUSH);
		bEnviar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bEnviar.setText("Seleccionar");
		//Introducimos los valores y eventos de Fecha Inicio
		bEnviar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				// TODO ¿Se puede evitar usar métodos obsoletos?
				fecha = new Date(calendario.getYear(),calendario.getMonth(),calendario.getDay());
				shell.dispose();
			}				
		});
		
		final Button bCancelar	= new Button(cAcepCanc, SWT.PUSH);
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bCancelar.setText("Cancelar");
		//Introducimos los valores y eventos de Fecha Inicio
		bCancelar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				fecha = null;
				shell.dispose();
			}				
		});
		

		// Ajustar el tamaño de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el cursor
		shell.setLocation(shell.getDisplay().getCursorLocation().x + - shell.getSize().x/2, shell.getDisplay().getCursorLocation().y  - shell.getSize().y/2);
		shell.open();
	}

	public Integer dameCatDia() {
		// TODO Auto-generated method stub
		return (calendario.getYear()*10000+calendario.getMonth()*100+calendario.getDay());
	}
	public boolean isDisposed() {
		return shell.isDisposed(); 
	}
}