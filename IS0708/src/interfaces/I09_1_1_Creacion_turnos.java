package interfaces;
/*******************************************************************************
 * INTERFAZ I-09.1 :: Creaci�n de un turno
 *   por Daniel Dionne
 *   
 * Interfaz para crear un nuevo turno.
 * ver 0.1
 *******************************************************************************/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.sql.Time;
import java.util.ResourceBundle;
import java.util.ArrayList;
import org.eclipse.swt.graphics.*;

import aplicacion.Empleado;
import aplicacion.Posicion;
import aplicacion.Turno;
import aplicacion.Vista;

public class I09_1_1_Creacion_turnos {
	private Shell padre = null;
	private Shell shell = null;
	private ResourceBundle bundle;
	private aplicacion.Turno turno;
	private int modo;
	private int alto, ancho;
	private int idTurno;
	private int idContrato;
	private Turno turnoInsertado;
	private Vista vista;
	private Turno turnoModificado;
	public I09_1_1_Creacion_turnos(Shell padre, Vista vista, ResourceBundle bundle,int modo,int idTurno, int idContrato, Turno tm) {
		this.padre = padre;
		this.bundle = bundle;
		this.modo=modo;
		this.idTurno=idTurno;
		this.idContrato=idContrato;
		this.vista=vista;
		turnoModificado=tm;
		shell = new Shell (padre, SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);
		mostrarVentana();
	}
	
	public Shell getShell(){
		return shell;
	}
	public void mostrarVentana() {		
		if (modo==0)	shell.setText(bundle.getString("I09_lab_NuevoTurno"));
		else shell.setText(bundle.getString("I09_lab_ModifTurno"));
		shell.setLayout(new GridLayout(2,true));
		//Permite cerrar la ventana pulsando ESC
		shell.addListener (SWT.Traverse, new Listener () {
			public void handleEvent (Event event) {
				switch (event.detail) {
					case SWT.TRAVERSE_ESCAPE:
						shell.close ();
						event.detail = SWT.TRAVERSE_NONE;
						event.doit = false;
						break;
				}
			}
		});

		final Group grupo1 = new Group(shell, SWT.NONE);
		final Group grupo2 = new Group(shell, SWT.NO_BACKGROUND);
		grupo1.setText(bundle.getString("Nombre"));
		grupo2.setText(bundle.getString("Turno"));

// Grupo1 - Nombre
		grupo1.setLayout(new GridLayout(2,false));
		final Label  lDesc		= new Label (grupo1, SWT.LEFT);
		final Text   tDesc		= new Text  (grupo1, SWT.BORDER);
		lDesc			.setText(bundle.getString("I09_lab_desc_Turno"));
		lDesc			.setLayoutData	(new GridData(SWT.LEFT,SWT.CENTER,false,true,1,1));
		tDesc			.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		tDesc			.setToolTipText(bundle.getString("I09_tip_IdTurno"));
		if (modo==0) tDesc.setText("");
		else tDesc.setText(turnoModificado.getDescripcion());

// Grupo 2 - Turno
		GridLayout g = new GridLayout(1,false);
		g.verticalSpacing=0;
		GridData gd = new GridData(SWT.FILL,SWT.FILL,false,true,1,1);
		gd.heightHint=30;
		grupo2.setLayout(g);
		grupo2.setLayoutData(gd);
		
		// TODO Coger hora de apertura y cierre del departamento
		final I_Cuadrante ic = new I_Cuadrante(vista, 0, 0, "", 4, 9, 23);
		ic.setCompositeUnTurno(grupo2);
		if (modo==1) {
			I_Turno itaux=new I_Turno(turnoModificado);
			//ic.setTurno(turnoModificado);
		}
		final Button bAceptar	= new Button(shell, SWT.PUSH);
		final Button bCancelar	= new Button(shell, SWT.PUSH);		
		
		bAceptar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancelar	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		grupo1.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		grupo2.setLayoutData		(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		
		bAceptar	.setText(bundle.getString("Aceptar"));
		bCancelar	.setText(bundle.getString("Cancelar"));
		bAceptar	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		bCancelar	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		// Listener para el selector de fecha de alta
		SelectionAdapter sabFAlta = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
//				new I02PeticionFecha(shell, tFAlta);
			}
		};
//		bFAlta.addSelectionListener(sabFAlta);
		
		
		// Un listener con lo que hace el bot�n bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};

		// Un listener con lo que hace el bot�n bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//modo = 0 es nuevo turno y modo =1 es modificar
				String desc = tDesc.getText();
				Turno it=ic.getTurno();
//				System.out.println(it.getHoraEntrada());
//				System.out.println(it.getHoraSalida());
//				System.out.println(it.getHoraDescanso());
//				System.out.println(it.getTDescanso());
				if (modo==0){					
					turnoInsertado = new Turno(0,desc,it.getHoraEntrada(),it.getHoraSalida(),it.getHoraDescanso(),it.getTDescanso());
					//CAMBIAR
					int id=vista.getControlador().insertTurno(turnoInsertado);
					turnoInsertado = new Turno(id,desc,it.getHoraEntrada(),it.getHoraSalida(),it.getHoraDescanso(),it.getTDescanso());
					if (id!=-1){
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText("Info");
						messageBox.setMessage(bundle.getString("I09_insert_Turno"));
						messageBox.open();
					}
					else {
						turnoInsertado = null;
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
						messageBox.setText(bundle.getString("Error"));
						messageBox.setMessage(bundle.getString("I09_err_insert_Turno"));
						messageBox.open();
					}
					
					shell.dispose();
				}
				else{
					//CAMBIAR
					boolean okis=vista.getControlador().modificarTurno(idTurno,desc,it.getHoraEntrada(),it.getHoraSalida(),it.getHoraDescanso(),it.getTDescanso());
					turnoModificado = new Turno(idTurno,desc,it.getHoraEntrada(),it.getHoraSalida(),it.getHoraDescanso(),it.getTDescanso());
					if (okis){
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setText("Info");
						messageBox.setMessage(bundle.getString("I09_modif_Turno"));
						messageBox.open();
					}
					else {
						turnoModificado=null;
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_ERROR);
						messageBox.setText(bundle.getString("Error"));
						messageBox.setMessage(bundle.getString("I09_err_modif_Turno"));
						messageBox.open();
					}
					shell.dispose();
				}
			}
		};
		
		bCancelar.addSelectionListener(sabCancelar);
		bAceptar.addSelectionListener(sabAceptar);


		// Bot�n por defecto bAceptar
		shell.setDefaultButton(bAceptar);
		// Ajustar el tama�o de la ventana al contenido
		shell.pack();
		shell.setSize(new Point(350, shell.getSize().y));
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		shell.open();
	}
	
	public Turno getTurnoInsertado(){
		return turnoInsertado;
	}
	
	public Turno getTurnoModificado(){
		return turnoModificado;
	}
	
}