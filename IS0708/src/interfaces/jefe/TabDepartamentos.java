package interfaces.jefe;

import interfaces.admin.ShellConfigDepartamento;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import aplicacion.Vista;

public class TabDepartamentos extends Thread{
	
	final Vista vista;
	final ResourceBundle bundle;
	final TabFolder tabFolder;
	final Shell padre;
	private TabFolder fCentro;
	private boolean datosInterfazCargados;
	private SubTabConfiguracionDias cInfoHorario;
	private Combo cmbDepartamentos;
	private Text lContenido;
	private HorarioMes calendario;
	private Combo comboMes;
	private Combo comboAnio;
	
	public TabDepartamentos(TabFolder tabFolder, Vista vista, ResourceBundle bundle, Shell padre) {
		this.vista = vista;
		this.bundle = bundle;
		this.tabFolder = tabFolder;
		this.padre=padre;
		crearTabJefeDepartamentos();
	}
	
	
	/**
	 * Implementa un hilo que coge los empleados del departamento del servidor.
	 */
	public void run() {
		try {
			while (!vista.isCacheCargada()) {
				sleep(5000);
			}
		} catch (Exception e) {
		}

		datosInterfazCargados = true;
		// while (run) {

		if (fCentro.isDisposed()) {
		} // run = false;
		else {
			if (!fCentro.isDisposed()) {
				// Actualizar tabla
				if (!fCentro.isDisposed()) {
					fCentro.getDisplay().asyncExec(new Runnable() {
						public void run() {
							activar();
						}
					});
				//	activar();
				}
			}
			try {
				// Espera 10 segundos (¿cómo lo dejamos?)
				sleep(10000);
			} catch (Exception e) {
			}
		}
	}
	
	protected void activar() {		
		ArrayList<String> array = vista.getNombreDepartamentosJefe(vista.getEmpleadoActual());
	/*	if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				cmbDepartamentos.add(array.get(i));
			}
		}
		cmbDepartamentos.select(0);
		
		calendario=new HorarioMes(fCentro, padre, 5, 2008);	
		calendario.setMes(5, 2008);*/
		
		
		
		cInfoHorario.activar(array.get(0));	
		cmbDepartamentos.setEnabled(true);
		
		//asigno el tab del control de personal para cada dia
	//	fCentro.getItem(0).setControl(cInfoHorario.getControl());
	//	fCentro.getItem(1).setControl(calendario.getComposite());
		
		//Escribo el texto en el label
		lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
	}


	private void crearTabJefeDepartamentos() {
		TabItem tabItemDepartamentos = new TabItem(tabFolder, SWT.NONE);
		tabItemDepartamentos.setText(bundle.getString("Departamentos"));
		tabItemDepartamentos.setImage(vista.getImagenes().getIco_chicos());

		final Composite cDepartamentos = new Composite(tabFolder, SWT.NONE);
		tabItemDepartamentos.setControl(cDepartamentos);

		cDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		cDepartamentos.setLayout(new GridLayout(3, false));

		Label lDepartamentos = new Label(cDepartamentos, SWT.LEFT);
		lDepartamentos.setText(bundle.getString("Departamento"));
		lDepartamentos.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		cmbDepartamentos = new Combo(cDepartamentos, SWT.BORDER
				| SWT.READ_ONLY);
		cmbDepartamentos.setEnabled(false);
		cmbDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		
		ArrayList<String> array = vista.getNombreDepartamentosJefe(vista.getEmpleadoActual());
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				cmbDepartamentos.add(array.get(i));
			}
		}
		cmbDepartamentos.select(0);

		// Composite for Buttons: "New Department" and "Configure Department"
		Composite cBut = new Composite(cDepartamentos, SWT.LEFT);
		cBut.setLayout(new GridLayout(2, false));

		// Button "Configure Department"
		Button bConfig = new Button(cBut, SWT.PUSH);
		bConfig.setText(bundle.getString("I02_but_Config_dep"));
		bConfig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		bConfig.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out
						.println("I02 :: Pulsado Configuración departamentos: "
								+ cmbDepartamentos.getText());
				new ShellConfigDepartamento(tabFolder.getShell(), bundle, vista,
						cmbDepartamentos.getText(),cmbDepartamentos,false);
			}
		});
		
		fCentro= new TabFolder(cDepartamentos, SWT.NONE);
		fCentro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		//comentado hasta que no pete. para probar dscomentar
		cInfoHorario=new SubTabConfiguracionDias(vista, null, fCentro,bundle,padre);
		
		final Composite cMes=new Composite(fCentro,SWT.BORDER);
		cMes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		cMes.setLayout(new GridLayout(2, false));
		
		calendario=new HorarioMes(cMes, padre, 5, 2008);	
		calendario.setMes(5, 2008);
		
		final Composite cCombos=new Composite(cMes,SWT.NONE);
		cCombos.setLayout(new GridLayout(2,false));
				
		comboMes=new Combo(cCombos,SWT.BORDER | SWT.READ_ONLY);
		comboMes.setLayoutData(new GridData(SWT.CENTER,SWT.BEGINNING,true,false));
		comboAnio=new Combo(cCombos,SWT.BORDER | SWT.READ_ONLY);
		comboAnio.setLayoutData(new GridData(SWT.CENTER,SWT.BEGINNING,true,false));
		final Button bSetMes=new Button(cCombos,SWT.PUSH);
		bSetMes.setLayoutData(new GridData(SWT.FILL,SWT.BEGINNING,true,true,2,1));
		bSetMes.setText(bundle.getString("TabDepartamentos_bSetMes"));
		
		bSetMes.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				calendario.setMes(comboMes.getSelectionIndex()+1, comboAnio.getSelectionIndex()+2008);
			}
		});
		
		setCombos();
	
		lContenido = new Text(fCentro, SWT.READ_ONLY | SWT.MULTI |SWT.V_SCROLL);
		
		lContenido.setEditable(false);
		lContenido.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		
		TabItem tab3=new TabItem(fCentro, SWT.NONE);
		tab3.setText(bundle.getString("TabDepartamentos_tab3"));
		tab3.setControl(lContenido);
		//comentado para ke no pete al resto
		TabItem tab1=new TabItem(fCentro, SWT.NONE);
		tab1.setText(bundle.getString("TabDepartamentos_tab1"));
		tab1.setControl(cInfoHorario.getControl());
		
		TabItem tab2=new TabItem(fCentro, SWT.NONE);
		tab2.setText(bundle.getString("TabDepartamentos_tab2"));
		tab2.setControl(cMes);
		
		
		
			
		//listener para el combo y mostrar la info debajo
		cmbDepartamentos.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
	//			lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
			}
		});
		
		start();

	}


	private void setCombos() {
		comboMes.add(bundle.getString("enero"));
		comboMes.add(bundle.getString("febrero"));
		comboMes.add(bundle.getString("marzo"));
		comboMes.add(bundle.getString("abril"));
		comboMes.add(bundle.getString("mayo"));
		comboMes.add(bundle.getString("junio"));
		comboMes.add(bundle.getString("julio"));
		comboMes.add(bundle.getString("agosto"));
		comboMes.add(bundle.getString("septiembre"));
		comboMes.add(bundle.getString("octubre"));
		comboMes.add(bundle.getString("noviembre"));
		comboMes.add(bundle.getString("diciembre"));
		comboMes.select(0);
		
		//los años deben empezar en 2008
		String[] anios={"2008","2009","2010","2011","2012","2013","2014","2015"};
		comboAnio.setItems(anios);
		comboAnio.select(0);	
	}

}
