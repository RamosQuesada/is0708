package interfaces;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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

public class I02CambioHorario {
	private Shell padre = null;
	private ResourceBundle _bundle;
	
	private I02PeticionFecha ventana;
	private Text cFechaInicio;
	private Text cFechaFin;
	private Integer iFechaInicio=0;
	private Integer iFechaFin=0;
	
	private int _opcion_actual= I02CambioHorario.NOINICIALIZADO;
	
	private final static int NOINICIALIZADO = 0;
	private final static int TODOSDIAS = 1;
	private final static int DIAADIA = 2;
	
	public I02CambioHorario(Shell padre,ResourceBundle bundle) {
		this.padre = padre;
		_bundle = bundle;
		mostrarVentana();
	}
	
	public void mostrarVentana() {
		final Shell shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);

		final Image ico_mens_l = new Image(padre.getDisplay(), I02.class.getResourceAsStream("ico_mens1_v.gif"));
		
		//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 1;		
		shell.setLayout(lShell);
		shell.setText(_bundle.getString("peticion"));
		shell.setImage(ico_mens_l);
		
		/*    */

		
		final Composite cDuracion = new Composite (shell, SWT.BORDER);
		cDuracion.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lDuracion = new GridLayout();
		lDuracion.numColumns = 1;
		cDuracion.setLayout(lDuracion);
		
		final Button bIndefinida		= new Button(cDuracion, SWT.RADIO);
		bIndefinida.setText(_bundle.getString("petind"));
		final Button bTemporal		= new Button(cDuracion, SWT.RADIO);
		bTemporal.setText(_bundle.getString("pettem"));
		
		final Composite cFecha = new Composite (cDuracion, SWT.BORDER);
		cFecha.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lFecha = new GridLayout();
		lFecha.numColumns = 3;
		cFecha.setLayout(lFecha);

		final Label lFechaInicio	= new Label(cFecha, SWT.LEFT);
		lFechaInicio.setText(_bundle.getString("fechaini"));
		cFechaInicio =new Text (cFecha, SWT.BORDER);
		cFechaInicio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		final Button bFechaInicio	= new Button(cFecha, SWT.PUSH);
		bFechaInicio.setText(_bundle.getString("seleccionar"));
		//Introducimos los valores y eventos de Fecha Inicio
		bFechaInicio.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				ventana = new I02PeticionFecha(shell,cFechaInicio);
				//
			}				
		});
		
		cFechaInicio.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				iFechaInicio=ventana.dameCatDia();
			}
			
		});
		
		
		final Label lFechaFin	= new Label(cFecha, SWT.LEFT);
		lFechaFin.setText(_bundle.getString("fechafin"));
		cFechaFin =new Text (cFecha, SWT.BORDER);
		cFechaFin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));
		final Button bFechaFin	= new Button(cFecha, SWT.PUSH);
		bFechaFin.setText(_bundle.getString("seleccionar"));
		//Introducimos los valores y eventos de Fecha Inicio
		bFechaFin.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
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
		
		bIndefinida.setSelection(true);
		bIndefinida.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				lFechaFin.setEnabled(false);
				lFechaInicio.setEnabled(false);
				bFechaFin.setEnabled(false);
				bFechaInicio.setEnabled(false);
				cFechaFin.setEnabled(false);
				cFechaInicio.setEnabled(false);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
		);
		bTemporal.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

				//DESACTIVAR COMPOSITE DIA A DIA
				lFechaFin.setEnabled(true);
				lFechaInicio.setEnabled(true);
				bFechaFin.setEnabled(true);
				bFechaInicio.setEnabled(true);
				cFechaFin.setEnabled(true);
				cFechaInicio.setEnabled(true);
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		}
		);

		final Composite cTodosDias = new Composite (shell, SWT.BORDER);
		cTodosDias.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 0, 0));
		GridLayout lTodosDias = new GridLayout();
		lTodosDias.numColumns = 2;
		cTodosDias.setLayout(lTodosDias);
		

		//final Label lbTodosDias	= new Label(cTodosDias, SWT.LEFT);
		//Boton para la mensajeria interna
		final Button bTodosDias		= new Button(cTodosDias, SWT.RADIO);
		bTodosDias.setText("Pedir cambio todos los dias: ");
		final Combo coTodosDias = new Combo(cTodosDias, SWT.BORDER | SWT.READ_ONLY);
		coTodosDias.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 0, 0));
		coTodosDias.setItems(new String[] {
				_bundle.getString("turnomañ"), 
				_bundle.getString("turnotarde"),
				_bundle.getString("turnonoche")});
		coTodosDias.select(0);
		
		final Composite cDiaDia = new Composite (shell, SWT.BORDER);
		cDiaDia.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lDiaDia = new GridLayout();
		lDiaDia.numColumns = 2;
		cDiaDia.setLayout(lDiaDia);
		//ArrayList<Label> laDiaDia= new ArrayList<Label>();
		final String[] dias={
				_bundle.getString("lunes"),
				_bundle.getString("martes"),
				_bundle.getString("miercoles"),
				_bundle.getString("jueves"),
				_bundle.getString("viernes"),
				_bundle.getString("sabado"),
				_bundle.getString("domingo")
				};
		
		final ArrayList<Label> nombreDias = new ArrayList<Label>();
		final ArrayList<Combo> comboDias = new ArrayList<Combo>();
		for(int cont=0;cont<7;cont++){
			String dia= dias[cont];
			final Label aux = new Label(cDiaDia,SWT.CENTER);
			aux.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 0, 0));
			nombreDias.add(aux);
			aux.setText(dia);
			aux.setEnabled(false);
			comboDias.add(cont, new Combo(cDiaDia,SWT.BORDER | SWT.READ_ONLY));
			final String[] texto= new String[] {
					_bundle.getString("turnomañ"), 
					_bundle.getString("turnotarde"),
					_bundle.getString("turnonoche")
					};
			comboDias.get(cont).setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 0, 0));
			comboDias.get(cont).setItems(texto);
			comboDias.get(cont).select(0);
			comboDias.get(cont).setEnabled(false);
			
		
			
		}
		
		
		bTodosDias.setSelection(true);
		bTodosDias.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion mensajeria interna in");
				_opcion_actual=I02CambioHorario.TODOSDIAS;
				coTodosDias.setEnabled(true);
				for(int cont=0;cont<nombreDias.size();cont++){
					nombreDias.get(cont).setEnabled(false);
				}
				for(int cont=0;cont<comboDias.size();cont++){
					comboDias.get(cont).setEnabled(false);
				}
				//DESACTIVAR COMPOSITE DIA A DIA
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
			}
		}
		);


		
		final Button bDia		= new Button(cTodosDias, SWT.RADIO);
		bDia.setText(_bundle.getString("pedirhora"));

		bDia.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("peticion nuevo");
				_opcion_actual=I02CambioHorario.TODOSDIAS;
				coTodosDias.setEnabled(false);
				for(int cont=0;cont<nombreDias.size();cont++){
					nombreDias.get(cont).setEnabled(true);
				}
				for(int cont=0;cont<comboDias.size();cont++){
					comboDias.get(cont).setEnabled(true);
				}
				//DESACTIVAR COMPOSITE DIA A DIA
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
			}
		}
		);

		
		final Composite cAceptarCancelar = new Composite (shell, SWT.BORDER);
		cAceptarCancelar.setLayoutData(new GridData(SWT.FILL, SWT.DOWN, true, false, 1, 1));
		GridLayout lAceptarCancelar = new GridLayout();
		lAceptarCancelar.numColumns = 2;
		cAceptarCancelar.setLayout(lAceptarCancelar);
		
		//Botones aceptar y cancelar
		final Button bAceptar		= new Button(cAceptarCancelar, SWT.PUSH);
		final Button bCancelar		= new Button(cAceptarCancelar, SWT.PUSH);
		
		//Introducimos los textos a los botones
		bAceptar.setText(_bundle.getString("enviar"));
		bCancelar.setText(_bundle.getString("cancelar1"));
				//Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				if(iFechaFin<iFechaInicio){
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_INFORMATION);
					messageBox.setText (_bundle.getString("Mensaje"));
					messageBox.setMessage (_bundle.getString("errper"));
					messageBox.open();
					//if( messageBox.open () == SWT.YES){
					//	shell.dispose();
					//}
									
				}
				else {
				shell.dispose();
				}
			}	
		});
		
		//Introducimos los valores y eventos de Cancelar
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
		
		shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event e) {

			}
		});
	}
	public void ponTextoFechaI(){
		cFechaInicio.setText("aaa");
	}
}
