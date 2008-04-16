package interfaces.jefe;
/**
 * INTERFAZ I-08.1 :: Ventas de empleado
 *   
 * Interfaz para añadir ventas para un dia del empleado seleccionado
 * @author UDULCE
 *
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ColorDialog;

import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;
import aplicacion.utilidades.Util;

import interfaces.general.DialogSeleccionFecha;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.sql.Date;

public class DialogVentasEmpleado {
	private Shell padre = null;
	private ResourceBundle bundle;
	private Vista vista;
	private Date fecha;
	private int idVend;
	private int ventas;
	
	public DialogVentasEmpleado(Shell padre, ResourceBundle bundle, Vista vista) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;
		mostrarVentana();
	}
	
	public void mostrarVentana() {		
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setLayout(new GridLayout(2,true));
		
		final Group grupo = new Group(shell, SWT.NONE);
		grupo.setText(bundle.getString("ventas_empleado"));
		grupo.setLayout(new GridLayout(2,false));
		grupo.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		
		final Label  lIdVend		= new Label (grupo, SWT.LEFT);
		final Label   llIdVend		= new Label  (grupo, SWT.BORDER);
		final Label  lNventas		= new Label (grupo, SWT.LEFT);
		final Text   tNventas		= new Text  (grupo, SWT.BORDER);
		final Button bFecha			= new Button(grupo, SWT.PUSH);
		final Text   tFecha			= new Text  (grupo, SWT.BORDER | SWT.READ_ONLY);
		
		final Button bAceptar		= new Button(shell, SWT.PUSH);
		final Button bCancelar		= new Button(shell, SWT.PUSH);
		
		lIdVend				.setText(bundle.getString("Vendedor"));
		llIdVend			.setText(String.valueOf(idVend));
		lNventas			.setText(bundle.getString("Ventas"));
		bFecha				.setText(bundle.getString("Fecha"));
		
		lIdVend			.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		llIdVend		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		lNventas		.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tNventas		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		bFecha			.setLayoutData	(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
		tFecha			.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		
		bAceptar		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar		.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		
		tNventas.setTextLimit(8);
		

		shell.setText(bundle.getString("ventas_empleado"));
		
		bAceptar.setText(bundle.getString("Aceptar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		bCancelar.setText(bundle.getString("Cancelar"));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		// Listener para el selector de fecha
		SelectionAdapter sabFecha = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				DialogSeleccionFecha i17 = new DialogSeleccionFecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				fecha = i17.getFecha();
				String [] meses = {bundle.getString("enero"),bundle.getString("febrero"),bundle.getString("marzo"),
						bundle.getString("abril"),bundle.getString("mayo"),bundle.getString("junio"),
						bundle.getString("julio"),bundle.getString("agosto"),bundle.getString("septiembre"),
						bundle.getString("octubre"),bundle.getString("noviembre"),bundle.getString("diciembre")};
				if (fecha!=null)
				tFecha.setText(String.valueOf(fecha.getDate())+" " + bundle.getString("artiFecha(de)")+" "  + meses[fecha.getMonth()]+" " + bundle.getString("artiFecha(de)")+" "  + String.valueOf(fecha.getYear()+1900));
			}
		};
		bFecha.addSelectionListener(sabFecha);
		
		// Listener con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};
		
		// Listener con lo que hace el botón bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				// Comprueba que las ventas no es vacía (campo obligatorio)
				if (tNventas.getText().length()==0) {
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("err_ventas"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tNventas y seleccionar texto
					tNventas.setFocus();
					tNventas.selectAll();
				}
				// Comprueba que se ha seleccionado una fecha
				else if (tFecha.getText().length()==0){
					MessageBox messageBox = new MessageBox (shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
					messageBox.setText (bundle.getString("Error"));
					messageBox.setMessage (bundle.getString("I08_err_fecha"));					
					e.doit = messageBox.open () == SWT.YES;
					// Enfocar tFecha y seleccionar texto
					tFecha.setFocus();
					tFecha.selectAll();			
				}
				
				//añadir ventas en la tabla y en la base de datos
				
			}
		};
		
		bCancelar.addSelectionListener(sabCancelar);
		bAceptar.addSelectionListener(sabAceptar);
		// Botón por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.setSize(200, 150);
		shell.open();
	}

}
