/**
 * 
 */
package interfaces.jefe;

import java.sql.Time;
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
 * falta: 
 * -ajustar layouts para hacerlo bonito 
 * -poner los strings con lo del idioma 
 * -meter los minutos a los label
 * -dar funcionalidad al boton guardar cambios
 * -obtener los dias de alguna tabla
 * 
 */
public class SubTabConfiguracionDias {

	private Vista vista=null;
	private String departamento;
	
	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="83,31"
	private Composite composite = null;  //  @jve:decl-index=0:
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
	private ScrolledComposite scroll = null;  //  @jve:decl-index=0:
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
		gTiposDia.setText(bundle.getString("ConfiguracionHorarios_label_tipos"));
		gTiposDia.setLayout(gridLayout4);
		createCTipos();
		gTiposDia.setLayoutData(gridData1);
		rLunes = new Button(cTipos, SWT.RADIO);
		rLunes.setText(bundle.getString("lunes"));
		rLunes.setEnabled(false);
		rLunes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 seleccion=1;
				 ArrayList<String> horario=vista.getHorarioDpto(departamento);
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 1),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
						 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
				 scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				 composite.layout();
			}
		});
		rMartes = new Button(cTipos, SWT.RADIO);
		rMartes.setText(bundle.getString("martes"));
		rMartes.setEnabled(false);
		rMartes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=2;
				ArrayList<String> horario=vista.getHorarioDpto(departamento);
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 2),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
						 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rMiercoles = new Button(cTipos, SWT.RADIO);
		rMiercoles.setText(bundle.getString("miercoles"));
		rMiercoles.setEnabled(false);
		rMiercoles.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=3;
				ArrayList<String> horario=vista.getHorarioDpto(departamento);
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 3),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
						 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rJueves = new Button(cTipos, SWT.RADIO);
		rJueves.setText(bundle.getString("jueves"));
		rJueves.setEnabled(false);
		rJueves.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=4;
				ArrayList<String> horario=vista.getHorarioDpto(departamento);
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 4),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
						 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rViernes = new Button(cTipos, SWT.RADIO);
		rViernes.setText(bundle.getString("viernes"));
		rViernes.setEnabled(false);
		rViernes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=5;
				ArrayList<String> horario=vista.getHorarioDpto(departamento);
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 5),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
						 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rSabado = new Button(cTipos, SWT.RADIO);
		rSabado.setText(bundle.getString("sabado"));
		rSabado.setEnabled(false);
		rSabado.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=6;
				ArrayList<String> horario=vista.getHorarioDpto(departamento);
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 6),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
						 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		rDomingo = new Button(cTipos, SWT.RADIO);
		rDomingo.setText(bundle.getString("domingo"));
		rDomingo.setEnabled(false);
		rDomingo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				seleccion=7;
				ArrayList<String> horario=vista.getHorarioDpto(departamento);
				 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 7),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
						 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
		
		
		
		final ArrayList<Button> otrosTipos=new ArrayList<Button>();
	/*	for(int i=0;i<3;i++){
			otrosTipos.add(new Button(cTipos, SWT.RADIO));
			otrosTipos.get(i).setText(bundle.getString("Tipo")+(i+1));
			final int cuenta=i;
			otrosTipos.get(i).addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					seleccion=8+cuenta;
					ArrayList<String> horario=vista.getHorarioDpto(departamento);
					 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 8+cuenta),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
							 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
					scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					composite.layout();
				}
			});		
			cTipos.layout();
			gTiposDia.layout();
		}*/		
		
		bNuevoTipo = new Button(gTiposDia, SWT.NONE);
		bNuevoTipo.setText(bundle.getString("ConfiguracionHorarios_boton_nuevo"));
		bNuevoTipo.setLayoutData(gridData2);
		bNuevoTipo.setEnabled(false);
		bNuevoTipo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Button b=new Button(cTipos, SWT.RADIO);
				b.setText("Tipo Nuevo");
				otrosTipos.add(b);
				final int cuenta=otrosTipos.size()-1;
				b.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						seleccion=8+cuenta;
						ArrayList<String> horario=vista.getHorarioDpto(departamento);
						 rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 8+cuenta),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
								 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));
						scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
						composite.layout();
					}
				});
				cTipos.layout();
				gTiposDia.layout();
			}
		});
		bEliminar = new Button(gTiposDia, SWT.NONE);
		bEliminar.setText(bundle.getString("ConfiguracionHorarios_boton_eliminar"));
		bEliminar.setLayoutData(gridData3);
		bEliminar.setEnabled(false);
		bEliminar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 if (seleccion <7){
					 MessageBox messageBox = new MessageBox(sShell,
								SWT.ICON_INFORMATION | SWT.OK);
						messageBox
								.setMessage(bundle.getString("ConfiguracionHorarios_error_eliminar"));
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
		bModificar.setText(bundle.getString("ConfiguracionHorarios_modificar_tipo"));
		bModificar.setLayoutData(gridData14);
		bModificar.setEnabled(false);
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
		gNumerosDia.setText(bundle.getString("ConfiguracionHorarios_min_max"));
		gNumerosDia.setLayout(gridLayout1);
		gNumerosDia.setLayoutData(gridData4);
		
		int k=5;
		
		lHorario=new ArrayList<Label>();
		lMinimo=new ArrayList<Label>();
		lMaximo=new ArrayList<Label>();
		sMinimo=new ArrayList<Spinner>();
		sMaximo=new ArrayList<Spinner>();
		
		createCNumeros();
	/*	ArrayList<String> horario=vista.getHorarioDpto(departamento);
		rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 0),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
				 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));	*/
		
		bGuardar = new Button(gNumerosDia, SWT.NONE);
		bGuardar.setText(bundle.getString("ConfiguracionHorarios_boton_guardar"));
		bGuardar.setLayoutData(gridData5);
		bGuardar.setEnabled(false);
		
		bGuardar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// TODO guardar las modificaciones				
			}
		});
		
	}

	private void rellenar(ArrayList<Object[]> horarios,Time apertura,Time cierre) {
			
		setAperturaCierre();
				
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
		
		for (int i=apertura.getHours();i<cierre.getHours();i++){
			Label label1=new Label(cNumeros,SWT.NONE);
			label1.setText("Desde las "+(Integer)horarios.get(i)[0]+" a las "+((Integer)horarios.get(i)[0]+1));
			lHorario.add(label1);
			
			Label minimo=new Label(cNumeros,SWT.NONE);
			minimo.setText(bundle.getString("Minimo"));
			lMinimo.add(minimo);
			
			Spinner spMinimo=new Spinner(cNumeros, SWT.BORDER);
			spMinimo.setSelection(((Integer)horarios.get(i)[1]));
			spMinimo.setMinimum(0);
			sMinimo.add(spMinimo);			
			
			Label maximo=new Label(cNumeros,SWT.NONE);
			maximo.setText(bundle.getString("Maximo"));
			lMaximo.add(maximo);
			
			
			Spinner spMaximo=new Spinner(cNumeros, SWT.BORDER);
			spMaximo.setSelection(((Integer)horarios.get(i)[1]));
			spMaximo.setMinimum(1);
			sMaximo.add(spMaximo);

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
	//	ArrayList<String> horario=vista.getHorarioDpto(departamento);
		
		//crea el grupo
		gHoras = new Group(composite, SWT.NONE);
		gHoras.setText("Horarios de apertura y cierre");
		gHoras.setLayoutData(gridData6);
		gHoras.setLayout(gridLayout2);
		lApertura = new Label(gHoras, SWT.NONE);
		lApertura.setText("Apertura:");
		lApertura.setLayoutData(gridData8);
		sApertura = new Spinner(gHoras, SWT.BORDER);
	//	sApertura.setSelection(Integer.valueOf(horario.get(0).substring(0, 2)));
		sApertura.setMaximum(23);
		sApertura.setLayoutData(gridData10);
		sApertura.setEnabled(false);
		sAperturaMin = new Spinner(gHoras, SWT.BORDER);
		sAperturaMin.setMaximum(59);
		sAperturaMin.setIncrement(5);
		sAperturaMin.setEnabled(false);
	//	sAperturaMin.setSelection(Integer.valueOf(horario.get(0).substring(3, 5)));
		lCierre = new Label(gHoras, SWT.NONE);
		lCierre.setText("Cierre:");
		lCierre.setLayoutData(gridData9);
		sCierre = new Spinner(gHoras, SWT.BORDER);
	//	sCierre.setSelection(Integer.valueOf(horario.get(1).substring(0, 2)));
		sCierre.setMaximum(23);
		sCierre.setMinimum(1);
		sCierre.setLayoutData(gridData7);
		sCierre.setEnabled(false);
		sCierreMin = new Spinner(gHoras, SWT.BORDER);
		sCierreMin.setMaximum(59);
		sCierreMin.setIncrement(5);
		sCierreMin.setEnabled(false);
	//	sCierreMin.setSelection(Integer.valueOf(horario.get(1).substring(3, 5)));
		bEstablecer = new Button(gHoras, SWT.NONE);
		bEstablecer.setText("Establecer");
		bEstablecer.setLayoutData(gridData11);
		bEstablecer.setEnabled(false);
		bEstablecer.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// TODO guardar la hora de apertura y cierre para este departamento este dia, y actualizar lo de abajo
				vista.setHorarioDpto(departamento,new Time (sApertura.getSelection(),sAperturaMin.getSelection(),0),new Time(sCierre.getSelection(),sCierreMin.getSelection(),0));
				rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, seleccion),new Time(sApertura.getSelection(),0,0),
						 new Time(sCierre.getSelection(),0,0));
				scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				composite.layout();
			}
		});
	}
	
	private void setAperturaCierre(){
		ArrayList<String> horario=vista.getHorarioDpto(departamento);
		sApertura.setSelection(Integer.valueOf(horario.get(0).substring(0, 2)));
		sAperturaMin.setSelection(Integer.valueOf(horario.get(0).substring(3, 5)));
		sCierre.setSelection(Integer.valueOf(horario.get(1).substring(0, 2)));
		sCierreMin.setSelection(Integer.valueOf(horario.get(1).substring(3, 5)));
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
		scroll = new ScrolledComposite(padre, SWT.H_SCROLL | SWT.V_SCROLL );
		scroll.setExpandHorizontal(true);
	    scroll.setExpandVertical(true);
		createComposite();
		scroll.setContent(composite);
		scroll.setLayoutData(gridData);
		scroll.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public SubTabConfiguracionDias(Vista vista, String departamento, Composite padre, ResourceBundle bundle, Shell shell){
		this.vista=vista;
		this.departamento=departamento;
		this.sShell=shell;
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
	
	public void setGridData(GridData g){
		this.scroll.setLayoutData(g);
	}

	public Composite getControl() {
		// TODO Auto-generated method stub
		return scroll;
	}
	
	public void activar(String departamento){
		this.departamento=departamento;
		
		rLunes.setEnabled(true);
		rMartes.setEnabled(true);
		rMiercoles.setEnabled(true);
		rJueves.setEnabled(true);
		rViernes.setEnabled(true);
		rSabado.setEnabled(true);
		rDomingo.setEnabled(true);
		bNuevoTipo.setEnabled(true);
		bModificar.setEnabled(true);
		bEliminar.setEnabled(true);
		bGuardar.setEnabled(true);
		sApertura.setEnabled(true);
		sAperturaMin.setEnabled(true);
		sCierre.setEnabled(true);
		sCierreMin.setEnabled(true);
		bEstablecer.setEnabled(true);
		
	/*	ArrayList<String> horario=vista.getHorarioDpto(departamento);
		rellenar(vista.getDistribucionDepartamentoDiaSemana(departamento, 1),new Time(Integer.parseInt(horario.get(0).substring(0,2)),0,0),
				 new Time(Integer.parseInt(horario.get(1).substring(0,2)),0,0));*/
		this.gHoras.layout();
		this.gNumerosDia.layout();
		this.gTiposDia.layout();
		this.composite.layout();
		this.scroll.layout();
		
	}

}
