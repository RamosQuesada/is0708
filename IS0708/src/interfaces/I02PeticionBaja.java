package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

public class I02PeticionBaja {
	private Shell padre = null;
	
	private Text cFechaInicio;
	private Text cFechaFin;
	
	private I02PeticionFecha ventana;
	private Integer iFechaInicio=0;
	private Integer iFechaFin=0;
	
	public I02PeticionBaja(Shell padre) {
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
		shell.setText("Peticion de Baja:");
		shell.setImage(ico_mens_l);
		
		/*    */

		final Composite cGrupo = new Composite (shell, SWT.BORDER);
		cGrupo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lGrupo = new GridLayout();
		lGrupo.numColumns = 3;
		cGrupo.setLayout(lGrupo);
		
		final Label lFechaInicio	= new Label(cGrupo, SWT.LEFT);
		lFechaInicio.setText("Fecha inicio: ");
		cFechaInicio =new Text (cGrupo, SWT.BORDER);
		cFechaInicio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
	
		final Button bFechaInicio	= new Button(cGrupo, SWT.PUSH);
		bFechaInicio.setText("Seleccionar");
		//Introducimos los valores y eventos de Fecha Inicio
		bFechaInicio.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("hola");
				ventana = new I02PeticionFecha(shell,cFechaInicio);
				//iFechaInicio=ventana.dameCatDia();
			}				
		});
		
		cFechaInicio.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				iFechaInicio=ventana.dameCatDia();
			}
			
		});
		
		
		final Label lFechaFin	= new Label(cGrupo, SWT.LEFT);
		lFechaFin.setText("Fecha fin: ");
		cFechaFin =new Text (cGrupo, SWT.BORDER);
		cFechaFin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		final Button bFechaFin	= new Button(cGrupo, SWT.PUSH);
		bFechaFin.setText("Seleccionar");
		//Introducimos los valores y eventos de Fecha Inicio

		bFechaFin.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("hola");
				ventana = new I02PeticionFecha(shell,cFechaFin);
				//iFechaFin=ventana.dameCatDia();

			}				
		});		
		
		
		cFechaFin.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				iFechaFin=ventana.dameCatDia();
			}
			
		});
		
		
		
		
		
		
		final Label lMotivo	= new Label(cGrupo, SWT.LEFT);
		lMotivo.setText("Motivo peticion de baja: ");
		final Text  tMotivo	= new Text (cGrupo, SWT.BORDER);
		tMotivo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));

		
		final Composite cCuerpoMen = new Composite (shell, SWT.BORDER);
		cCuerpoMen.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lCuerpoMen = new GridLayout();
		lCuerpoMen.numColumns = 1;
		cCuerpoMen.setLayout(lCuerpoMen);
		final Label lExposicion	= new Label(cCuerpoMen, SWT.LEFT);		
		lExposicion.setText("Exposicion de peticion: ");
		final Text  tExposicion	= new Text (cCuerpoMen, SWT.BORDER|SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
		tExposicion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		
		
		
		
		final Composite cAceptarCancelar = new Composite (shell, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.RIGHT, SWT.DOWN, true, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botones aceptar y cancelar
		final Button bAceptar		= new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptarCancelar, SWT.PUSH);
		
		//Introducimos los textos a los botones
		bAceptar.setText( "       Enviar       ");
		bCancelar.setText("      Cancelar      ");
				//Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("Fecha inicio"+iFechaInicio);
				System.out.println("Fecha fin"+iFechaFin);
				if(iFechaFin<iFechaInicio){
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
					messageBox.setText ("Mensaje");
					messageBox.setMessage ("Error en el periodo de baja?   ");
									
				}
				else if((tMotivo.getCharCount()==0)&&(tExposicion.getCharCount()==0)){
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText ("Mensaje");
					messageBox.setMessage ("   El mensaje no tiene texto ni asunto \n �Desea enviar este mensaje sin asunto y sin texto?");
					if( messageBox.open () == SWT.YES){
						shell.dispose();
					}
				}	
				else if(tMotivo.getCharCount()==0){
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText ("Mensaje");
					messageBox.setMessage ("        El mensaje no tiene asunto \n �Desea enviar este mensaje sin asunto?");
					if( messageBox.open () == SWT.YES){
						shell.dispose();
					}
				}	
				else if(tExposicion.getCharCount()==0){
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText ("Mensaje");
					messageBox.setMessage ("        El mensaje no tiene texto \n �Desea enviar este mensaje sin texto?");
					if( messageBox.open () == SWT.YES){
						shell.dispose();
					}
				}
				else{
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
		
		shell.setSize(padre.getSize());
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
		
		shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event e) {
				if(!(tMotivo.getCharCount()==0)&&(tExposicion.getCharCount()==0)){
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					messageBox.setText ("Mensaje");
					messageBox.setMessage ("Si sale del mensaje perdera lo escrito \n �Desea salir del mensaje?");
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
