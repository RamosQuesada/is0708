package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class I02CambioHorario {
	private Shell padre = null;
	
	
	private Text cFechaInicio;
	private Text cFechaFin;
	
	private int _opcion_actual= I02CambioHorario.NOINICIALIZADO;
	
	private final static int NOINICIALIZADO = 0;
	private final static int TODOSDIAS = 1;
	private final static int DIAADIA = 2;
	
	public I02CambioHorario(Shell padre) {
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
		shell.setText("Peticion de Cambio de horario:");
		shell.setImage(ico_mens_l);
		
		/*    */

		

		
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
		coTodosDias.setItems(new String[] {"Turno Ma�ana", "Turno Tarde","Turno Noche"});
		coTodosDias.select(0);
		
		
		final Composite cDiaDia = new Composite (shell, SWT.BORDER);
		cDiaDia.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout lDiaDia = new GridLayout();
		lDiaDia.numColumns = 2;
		cDiaDia.setLayout(lDiaDia);
		ArrayList<Label> laDiaDia= new ArrayList<Label>();
		final String[] dias={"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
		final ArrayList<Label> nombreDias = new ArrayList<Label>();
		final ArrayList<Combo> comboDias = new ArrayList<Combo>();
		for(int cont=0;cont<7;cont++){
			String dia= dias[cont];
			final Label aux = new Label(cDiaDia,SWT.CENTER);
			aux.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 0, 0));
			nombreDias.add(aux);
			aux.setText(dia);
			comboDias.add(cont, new Combo(cDiaDia,SWT.BORDER | SWT.READ_ONLY));
			//final Combo coaux= new Combo(cDiaDia,SWT.LEFT);
			final String[] texto= new String[] {"Turno Ma�ana", "Turno Tarde","Turno Noche"};
			comboDias.get(cont).setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 0, 0));
			comboDias.get(cont).setItems(texto);
			comboDias.get(cont).select(0);
			
			
			

//			coaux.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 0, 0));
//			coaux.setItems(new String[] {"Turno Ma�ana", "Turno Tarde","Turno Noche"});
//			coaux.select(0);
			
		//	comboDias.add(coaux);
			
		}
		
		
		
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
		bDia.setText("Pedir horario dia a dia de la semana: ");

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
		bAceptar.setText(" Enviar ");
		bCancelar.setText(" Cancelar ");
				//Introducimos los valores y eventos de Aceptar
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bAceptar.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				shell.dispose();
			}	
		});
		
		//Introducimos los valores y eventos de Cancelar
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
