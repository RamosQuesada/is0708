package interfaces;

import interfaces.general.I17_Seleccion_fecha;

import java.util.ResourceBundle;
import java.sql.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import aplicacion.Vista;

public class I16_Peticion_baja {
	private Shell _padre = null;
	private ResourceBundle _bundle;
	
	private Text cFechaInicio;
	private Text cFechaFin;
	
	private I17_Seleccion_fecha ventana;
	private Date iFechaInicio;
	private Date iFechaFin;
	
	public I16_Peticion_baja(Shell padre,ResourceBundle bundle) {
		this._padre = padre;
		this._bundle= bundle;
		
		mostrarVentana();
	}
	
	public void mostrarVentana() {
		final Shell shell = new Shell (_padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setText(_bundle.getString("I16_tit_petibaja"));
//		shell.setImage(ico_mens_l);
		
		/*    */

		final Composite cGrupo = new Composite (shell, SWT.BORDER);
		cGrupo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 3;
		cGrupo.setLayout(lGrupo);
		
		final Label lFechaInicio	= new Label(cGrupo, SWT.LEFT);
		lFechaInicio.setText(_bundle.getString("I16_lab_fechaini"));
		cFechaInicio =new Text (cGrupo, SWT.BORDER);
		cFechaInicio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
	
		final Button bFechaInicio	= new Button(cGrupo, SWT.PUSH);
		bFechaInicio.setText(_bundle.getString("seleccionar"));
		//Introducimos los valores y eventos de Fecha Inicio
		bFechaInicio.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				iFechaInicio = i17.getFecha();
			}				
		});
		
		final Label lFechaFin	= new Label(cGrupo, SWT.LEFT);
		lFechaFin.setText(_bundle.getString("I16_lab_fechafin"));
		cFechaFin =new Text (cGrupo, SWT.BORDER);
		cFechaFin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		final Button bFechaFin	= new Button(cGrupo, SWT.PUSH);
		bFechaFin.setText(_bundle.getString("seleccionar"));
		//Introducimos los valores y eventos de Fecha Inicio

		bFechaFin.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				I17_Seleccion_fecha i17 = new I17_Seleccion_fecha(shell);
				while (!i17.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				iFechaFin = i17.getFecha();
			}				
		});		
		
		final Label lMotivo	= new Label(cGrupo, SWT.LEFT);
		lMotivo.setText(_bundle.getString("I16_lab_motivobaja"));
		final Text  tMotivo	= new Text (cGrupo, SWT.BORDER);
		tMotivo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));

		final Composite cCuerpoMen = new Composite (shell, SWT.BORDER);
		cCuerpoMen.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCuerpoMen = new GridLayout();
		lCuerpoMen.numColumns = 1;
		cCuerpoMen.setLayout(lCuerpoMen);
		final Label lExposicion	= new Label(cCuerpoMen, SWT.LEFT);		
		lExposicion.setText(_bundle.getString("I16_lab_exppet"));
		final Text  tExposicion	= new Text (cCuerpoMen, SWT.BORDER|SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
		tExposicion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		
		
		
		
		final Composite cAceptarCancelar = new Composite (shell, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.RIGHT, SWT.DOWN, false, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botones aceptar y cancelar
		final Button bAceptar		= new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptarCancelar, SWT.PUSH);
		
		//Introducimos los textos a los botones
		bAceptar.setText( _bundle.getString("Aceptar"));
		bCancelar.setText(_bundle.getString("Cancelar"));
				//Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {

				if(iFechaFin.before(iFechaInicio)){
					MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
					messageBox.setText (_bundle.getString("Mensaje"));
					messageBox.setMessage (_bundle.getString("I16_err_perbaj"));
					messageBox.open();
									
				}
				else if((tMotivo.getCharCount()==0)&&(tExposicion.getCharCount()==0)){
					MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText (_bundle.getString("Mensaje"));
					messageBox.setMessage (_bundle.getString("sintextoasun"));
					if( messageBox.open () == SWT.YES){
						shell.dispose();
					}
				}	
				else if(tMotivo.getCharCount()==0){
					MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText (_bundle.getString("Mensaje"));
					messageBox.setMessage (_bundle.getString("sinasunto"));
					if( messageBox.open () == SWT.YES){
						shell.dispose();
					}
				}	
				else if(tExposicion.getCharCount()==0){
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
		bCancelar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
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
				if(!(tMotivo.getCharCount()==0)&&(tExposicion.getCharCount()==0)){
					MessageBox messageBox = new MessageBox (_padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText (_bundle.getString("Mensaje"));
					messageBox.setMessage (_bundle.getString("salimensaje"));
					if( messageBox.open () == SWT.YES){
						shell.dispose();
					}
				}	
			}
		});
	}
	public void ponTextoFechaI(){
		cFechaInicio.setText("aaa");
	}
}
