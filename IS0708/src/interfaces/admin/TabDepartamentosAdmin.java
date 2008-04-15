package interfaces.admin;

import interfaces.I10_Nuevo_departamento;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import aplicacion.Vista;

public class TabDepartamentosAdmin {
	
	final Vista vista;
	final ResourceBundle bundle;
	final TabFolder tabFolder;
	
	public TabDepartamentosAdmin(TabFolder tabFolder, Vista vista, ResourceBundle bundle) {
		this.vista = vista;
		this.bundle = bundle;
		this.tabFolder = tabFolder;
		crearTabAdminDepartamentos();
	}
	
	/**
	 * Crea un tab con un listado de departamentos
	 * 
	 * @param tabFolder el tabFolder donde colocarlo
	 * @author Carlos Sánchez, Agustín y Alberto Benayas
	 */
	private void crearTabAdminDepartamentos() {
		
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

		final Combo cmbDepartamentos = new Combo(cDepartamentos, SWT.BORDER
				| SWT.READ_ONLY);
		cmbDepartamentos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		// ArrayList<String> array = vista.getEmpleadoActual()
		// .getDepartamentosId();
		ArrayList<String> array = vista.getNombreTodosDepartamentos();
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
				new ShellConfigDepartamento(tabFolder.getShell(), bundle, vista,
						cmbDepartamentos.getText(),cmbDepartamentos,true);
			}
		});

		// Button "New Department"
		Button bNew = new Button(cBut, SWT.PUSH);
		bNew.setText(bundle.getString("I02_but_Nuevo_dep"));
		bNew.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bNew.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new I10_Nuevo_departamento(tabFolder.getShell(),bundle,vista,cmbDepartamentos);
			}
		});
		
		// Button "Delete Department"
		Button bDel = new Button(cBut, SWT.PUSH);
		bDel.setText(bundle.getString("I02_but_Del_dep"));
		bDel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bDel.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if(vista.tieneEmpleados(cmbDepartamentos.getText())){
					MessageBox messageBox = new MessageBox (tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I02_err_elim"));
					e.doit = messageBox.open () == SWT.CLOSE;
				}else{//si no tiene ningun empleado
					MessageBox messageBox = new MessageBox (tabFolder.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I02_confirm_elim_dep"));
					if(messageBox.open () == SWT.OK){
						vista.eliminaDepartamento(cmbDepartamentos.getText());
					}
				}
			}
		});

		Composite cInfo = new Composite(cDepartamentos, SWT.BORDER);
		cInfo.setLayout(new GridLayout(2, false));
		cInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		final Text lContenido = new Text(cInfo,SWT.READ_ONLY | SWT.MULTI |SWT.V_SCROLL);
		lContenido.setEditable(false);
		lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
		lContenido.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		
		//listener para el combo y mostrar la info debajo
		cmbDepartamentos.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				lContenido.setText(vista.infoDpto(cmbDepartamentos.getText()));
			}
		});

	}
}
