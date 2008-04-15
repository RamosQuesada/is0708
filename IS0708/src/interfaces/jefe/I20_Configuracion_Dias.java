/**
 * 
 */
package interfaces.jefe;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.custom.ScrolledComposite;

import aplicacion.Vista;

/**
 * @author Alberto
 *
 */
public class I20_Configuracion_Dias {

	private Vista vista=null;
	private String departamento;
	
	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="83,31"
	private Composite composite = null;
	private Group gTiposDia = null;
	private Button rLunes = null;
	private Button rMartes = null;
	private Button rMiercoles = null;
	private Button rJueves = null;
	private Button rViernes = null;
	private Button rSabado = null;
	private Button rDomingo = null;
	private Button bNuevoTipo = null;
	private Button bEliminar = null;
	private Button bModificar = null;
	private Group gNumerosDia = null;
	private ArrayList<Label> lHorario = null;  //  @jve:decl-index=0:
	private ArrayList<Label> lMinimo = null;  //  @jve:decl-index=0:
	private ArrayList<Spinner> sMinimo = null;  //  @jve:decl-index=0:
	private ArrayList<Label> lMaximo = null;
	private ArrayList<Spinner> sMaximo = null;  //  @jve:decl-index=0:
	private Button bGuardar = null;
	private Group gHoras = null;
	private Label lApertura = null;
	private Spinner sApertura = null;
	private Label lCierre = null;
	private Spinner sCierre = null;
	protected int seleccion;
	private ScrolledComposite scroll = null;
	private Button bEstablecer = null;
	private Spinner sAperturaMin = null;
	private Spinner sCierreMin = null;
	protected int numero;

	protected String dpto;

	private Composite cTipos = null;

	private Composite cNumeros = null;
	private Composite padre;
	private ResourceBundle bundle;

	/**
	 * This method initializes sShell	
	 *
	 */
	private void createSShell() {
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 1;
		sShell = new Shell(SWT.CLOSE | SWT.APPLICATION_MODAL);
		sShell.setLayout(gridLayout3);
		createScroll();
		sShell.setSize(new Point(630, 392));
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite = new Composite(scroll, SWT.NONE);
		createGTiposDia();
		createGHoras();
		composite.setLayout(gridLayout);
		createGNumerosDia();
	}

	/**
	 * This method initializes gTiposDia	
	 *
	 */
	private void createGTiposDia() {
		GridData gridData14 = new GridData();
		gridData14.horizontalAlignment = GridData.FILL;
		gridData14.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 1;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.verticalAlignment = GridData.CENTER;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalSpan = 2;
		gridData1.verticalAlignment = GridData.FILL;
		gTiposDia = new Group(composite, SWT.V_SCROLL);
		gTiposDia.setText("Tipos de día");
		gTiposDia.setLayout(gridLayout4);
		createCTipos();
		gTiposDia.setLayoutData(gridData1);
		rLunes = new Button(cTipos, SWT.RADIO);
		rLunes.setText("Lunes");
		rLunes.setSelection(true);
		rLunes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 seleccion=0;// TODO Auto-generated Event stub widgetSelected()
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 0));
				 scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				 composite.layout();
			}
		});
		rMartes = new Button(cTipos, SWT.RADIO);
		rMartes.setText("Martes");
		rMartes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=1; // TODO Auto-generated Event stub widgetSelected()
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 1));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rMiercoles = new Button(cTipos, SWT.RADIO);
		rMiercoles.setText("Miercoles");
		rMiercoles.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=2; // TODO Auto-generated Event stub widgetSelected()
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 2));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rJueves = new Button(cTipos, SWT.RADIO);
		rJueves.setText("Jueves");
		rJueves.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=3; // TODO Auto-generated Event stub widgetSelected()
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 3));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rViernes = new Button(cTipos, SWT.RADIO);
		rViernes.setText("Viernes");
		rViernes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=4; // TODO Auto-generated Event stub widgetSelected()
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 4));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rSabado = new Button(cTipos, SWT.RADIO);
		rSabado.setText("Sabado");
		rSabado.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=5; // TODO Auto-generated Event stub widgetSelected()
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 5));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rDomingo = new Button(cTipos, SWT.RADIO);
		rDomingo.setText("Domingo");
		rDomingo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=6; // TODO Auto-generated Event stub widgetSelected()
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 6));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		
		
		
		final ArrayList<Button> otrosTipos=new ArrayList<Button>();
		for(int i=0;i<3;i++){
			otrosTipos.add(new Button(cTipos, SWT.RADIO));
			otrosTipos.get(i).setText("Tipo"+(i+1));
			final int cuenta=i;
			otrosTipos.get(i).addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					seleccion=7+cuenta; // TODO Auto-generated Event stub widgetSelected()
					rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 7+cuenta));
					scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					composite.layout();
				}
			});		
			cTipos.layout();
			gTiposDia.layout();
		}		
		
		bNuevoTipo = new Button(gTiposDia, SWT.NONE);
		bNuevoTipo.setText("Nuevo tipo");
		bNuevoTipo.setLayoutData(gridData2);
		bNuevoTipo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Button b=new Button(cTipos, SWT.RADIO);
				b.setText("Tipo Nuevo");
				otrosTipos.add(b);
				final int cuenta=otrosTipos.size()-1;
				b.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						seleccion=7+cuenta; // TODO Auto-generated Event stub widgetSelected()
						rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 7+cuenta));
						scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
						composite.layout();
					}
				});
				cTipos.layout();
				gTiposDia.layout();
			}
		});
		bEliminar = new Button(gTiposDia, SWT.NONE);
		bEliminar.setText("Eliminar tipo");
		bEliminar.setLayoutData(gridData3);
		bEliminar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 if (seleccion <7){
					 MessageBox messageBox = new MessageBox(sShell,
								SWT.ICON_INFORMATION | SWT.OK);
						messageBox
								.setMessage("No se puede eliminar este día de la configuración");
						messageBox.setText("Error");
						int response = messageBox.open();
				 }
				 else{
					// vista.infoDistribucionDpto(dpto, seleccion-7);
					 //TODO eliminar el dia que corresponde con seleccion
					 otrosTipos.get(seleccion-7).dispose();
					 gTiposDia.layout();
				 }
				
			}
		});
		bModificar = new Button(gTiposDia, SWT.NONE);
		bModificar.setText("Modificar tipo");
		bModificar.setLayoutData(gridData14);
		bModificar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 // TODO ventana para modificar un tipo
			}
		});
	}

	/**
	 * This method initializes gNumerosDia	
	 *
	 */
	private void createGNumerosDia() {
		GridData gridData5 = new GridData();
		gridData5.horizontalSpan = 5;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = GridData.FILL;
		gNumerosDia = new Group(composite, SWT.NONE);
		gNumerosDia.setText("Número de empleados mínimos y máximos del día");
		gNumerosDia.setLayout(gridLayout1);
		gNumerosDia.setLayoutData(gridData4);
		
		int k=5;
		
		lHorario=new ArrayList<Label>();
		lMinimo=new ArrayList<Label>();
		lMaximo=new ArrayList<Label>();
		sMinimo=new ArrayList<Spinner>();
		sMaximo=new ArrayList<Spinner>();
		
		createCNumeros();
		rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 0));	
		
		bGuardar = new Button(gNumerosDia, SWT.NONE);
		bGuardar.setText("Guardar Cambios");
		bGuardar.setLayoutData(gridData5);
		
		bGuardar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// TODO guardar las modificaciones				
			}
		});
		
	}

	private void rellenar(ArrayList<Object[]> horarios) {
			
		 for (int i=0;i<lHorario.size();i++){
			 lHorario.get(i).dispose();
			 lMinimo.get(i).dispose();
			 lMaximo.get(i).dispose();
			 sMinimo.get(i).dispose();
			 sMaximo.get(i).dispose();
		 }		 
		 
		
		 lHorario.clear();
		 lMinimo.clear();
		 lMaximo.clear();
		 sMinimo.clear();
		 sMaximo.clear();
		
		for (int i=0;i<horarios.size();i++){
			lHorario.add(new Label(cNumeros, SWT.NONE));
			lHorario.get(i).setText("Desde las "+(Integer)horarios.get(i)[0]+" a las "+((Integer)horarios.get(i)[0])+1);
			lMinimo.add(new Label(cNumeros, SWT.NONE));
			lMinimo.get(i).setText("Mínimo");
			sMinimo.add(new Spinner(cNumeros, SWT.BORDER));
			sMinimo.get(i).setSelection(((Integer)horarios.get(i)[1]));
			sMinimo.get(i).setMinimum(0);
			lMaximo.add(new Label(cNumeros, SWT.NONE));
			lMaximo.get(i).setText("Máximo");
			sMaximo.add(new Spinner(cNumeros, SWT.BORDER));
			sMaximo.get(i).setSelection((Integer)horarios.get(i)[2]);
			sMaximo.get(i).setMinimum(1);
		}
		cNumeros.layout();
		gNumerosDia.layout();
	}

	/**
	 * This method initializes gHoras	
	 *
	 */
	private void createGHoras() {
		GridData gridData11 = new GridData();
		gridData11.horizontalSpan = 6;
		gridData11.verticalAlignment = GridData.CENTER;
		gridData11.horizontalAlignment = GridData.CENTER;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = GridData.END;
		gridData10.verticalAlignment = GridData.CENTER;
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.END;
		gridData9.verticalAlignment = GridData.CENTER;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.END;
		gridData8.verticalAlignment = GridData.CENTER;
		GridData gridData7 = new GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = GridData.CENTER;
		gridData7.horizontalAlignment = GridData.END;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 6;
		gridLayout2.makeColumnsEqualWidth = true;
		
		//
		ArrayList<String> horario=vista.getHorarioDpto(departamento);
		
		//crea el grupo
		gHoras = new Group(composite, SWT.NONE);
		gHoras.setText("Horarios de apertura  cierre");
		gHoras.setLayoutData(gridData6);
		gHoras.setLayout(gridLayout2);
		lApertura = new Label(gHoras, SWT.NONE);
		lApertura.setText("Apertura:");
		lApertura.setLayoutData(gridData8);
		sApertura = new Spinner(gHoras, SWT.BORDER);
		sApertura.setSelection(Integer.valueOf(horario.get(0)));
		sApertura.setMaximum(23);
		sApertura.setLayoutData(gridData10);
		sAperturaMin = new Spinner(gHoras, SWT.BORDER);
		sAperturaMin.setMaximum(59);
		lCierre = new Label(gHoras, SWT.NONE);
		lCierre.setText("Cierre:");
		lCierre.setLayoutData(gridData9);
		sCierre = new Spinner(gHoras, SWT.BORDER);
		sCierre.setSelection(Integer.valueOf(horario.get(1)));
		sCierre.setMaximum(24);
		sCierre.setMinimum(1);
		sCierre.setLayoutData(gridData7);
		sCierreMin = new Spinner(gHoras, SWT.BORDER);
		sCierreMin.setMaximum(59);
		sCierreMin.setIncrement(5);
		bEstablecer = new Button(gHoras, SWT.NONE);
		bEstablecer.setText("Estabecer");
		bEstablecer.setLayoutData(gridData11);
		bEstablecer.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// TODO guardar la hora de apertura y cierre para este departamento este dia, y actualizar lo de abajo
				//vista.setHorarioDpto(departamento,sApertura.getSelection(),sCierre.getSelection());
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, seleccion));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
	}
	
	private void setAperturaCierre(){
		ArrayList<String> horario=vista.getHorarioDpto(departamento);
		sApertura.setSelection(Integer.valueOf(horario.get(0)));
		sCierre.setSelection(Integer.valueOf(horario.get(1)));
	}

	/**
	 * This method initializes scroll	
	 *
	 */
	private void createScroll() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		scroll = new ScrolledComposite(sShell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scroll.setExpandHorizontal(true);
	    scroll.setExpandVertical(true);
		createComposite();
		scroll.setContent(composite);
		scroll.setLayoutData(gridData);
		scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public I20_Configuracion_Dias(Vista vista, String departamento, Composite padre, ResourceBundle bundle){
		this.vista=vista;
		this.departamento=departamento;
	//	this.sShell=shell;
		this.padre=padre;
		this.bundle = bundle;
	//	createSShell();
		createScroll();
	}
	
	/**
	 * This method initializes cTipos	
	 *
	 */
	private void createCTipos() {
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.horizontalSpacing=15;
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = GridData.FILL;
		gridData12.verticalAlignment = GridData.FILL;
		cTipos = new Composite(gTiposDia, SWT.NONE);
		cTipos.setLayoutData(gridData12);
		cTipos.setLayout(gridLayout6);
	}

	/**
	 * This method initializes cNumeros	
	 *
	 */
	private void createCNumeros() {
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 5;
		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = GridData.FILL;
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.grabExcessVerticalSpace = true;
		gridData13.verticalAlignment = GridData.FILL;
		cNumeros = new Composite(gNumerosDia, SWT.NONE);
		cNumeros.setLayoutData(gridData13);
		cNumeros.setLayout(gridLayout5);
	}

/*	public static void main(String [] args){
		
		I20_Configuracion_Dias vista=new I20_Configuracion_Dias(null,null,null,null);
		Shell shell=vista.getShell();
		shell.open();

		while (!shell.isDisposed()) {
	      if (!shell.getDisplay().readAndDispatch()) {
	        shell.getDisplay().sleep();
	      }
	    }
	    shell.getDisplay().dispose();

	}*/

	private Shell getShell() {
		// TODO Auto-generated method stub
		return sShell;
	}
	
	public void setGridData(GridData g){
		this.scroll.setLayoutData(g);
	}

	public Composite getControl() {
		// TODO Auto-generated method stub
		return scroll;
	}

}
