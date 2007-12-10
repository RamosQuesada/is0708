package interfaces;

import idiomas.LanguageChanger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



import sun.misc.Sort;

import aplicacion.Database;
import aplicacion.Empleado;
import aplicacion.Posicion;

public class I13ElejirEmpleado {
	private Shell shell;
	private ResourceBundle bundle;
	private Image icoPq;
	private String numero;
	private String apeido;
	private String departamiento;
	int itemNumbers [];
	private ArrayList<Empleado> empleadoIN;
	private ArrayList<Empleado> empleadoOUT;
	
	public I13ElejirEmpleado(Shell padre, ResourceBundle bundle, ArrayList<Empleado> empleados ) {
		this.bundle   = bundle;
		this.empleadoIN = empleados;
		this.shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
}

	public void mostrarVentana() {
		
		icoPq = new Image(shell.getDisplay(), I02.class.getResourceAsStream("icoPq.gif"));
		shell.setImage(icoPq);
		shell.setText("Search");
	//Establecemos el layout del shell
		GridLayout lShell = new GridLayout();
		lShell.numColumns = 2;		
		shell.setLayout(lShell);

	//Establecemos los grupos
		final Group grupoIzq = new Group(shell, SWT.NONE);
		grupoIzq.setText("searching criteria");
		grupoIzq.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, true, true, 1, 1));
		final Group grupoDer = new Group(shell, SWT.NONE);		
		grupoDer.setText("person list");
		grupoDer.setLayoutData(new GridData(SWT.FILL,  SWT.FILL, true, true, 1, 1));
		
		GridLayout lGrupoIzq = new GridLayout();
		lGrupoIzq.numColumns = 3;
		lGrupoIzq.verticalSpacing=3;
		grupoIzq.setLayout(lGrupoIzq);
		GridLayout lGrupoDer = new GridLayout();
		lGrupoDer.numColumns = 1;
		lGrupoDer.verticalSpacing = 3;
		grupoDer.setLayout(lGrupoDer);
		
	//Establecemos el grupoIZQ
	//NUMERO
		final Label lLNumero = new Label(grupoIzq, SWT.NONE);
		final Text   lTNumero = new Text(grupoIzq, SWT.NONE);
		Button lBNumero = new Button(grupoIzq, SWT.NONE);
		lLNumero.setLayoutData(new GridData(SWT.LEFT,  SWT.CENTER, true, true, 1, 1));
		lTNumero.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		lBNumero.setLayoutData(new GridData(SWT.RIGHT,  SWT.CENTER, true, true, 1, 1));
		lTNumero.setFont(new Font(shell.getDisplay(), new FontData("Arial", 10, SWT.NORMAL)));
		lTNumero.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				numero=lTNumero.getText();
			}		
		});
		lLNumero.setText("Number");
		lBNumero.setText("->");
	//APELLIDO
		final Label lLApellido = new Label(grupoIzq, SWT.NONE);
		final Combo  lCApellido = new Combo(grupoIzq, SWT.NONE);	
		Button lBApellido = new Button(grupoIzq, SWT.NONE);		
		lLApellido.setLayoutData(new GridData(SWT.LEFT,  SWT.CENTER, true, true, 1, 1));
		lCApellido.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		lBApellido.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		lCApellido.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				apeido = lCApellido.getText();
			}		
		});
		lLApellido.setText("Surname");
		lBApellido.setText("->");
		String EmplList [] = new String[empleadoIN.size()];
		for(int i = 0; i<empleadoIN.size(); i++){
			EmplList [i] = empleadoIN.get(i).getApellido1()+" "+empleadoIN.get(i).getApellido2()+" "+empleadoIN.get(i).getNombre();			
		}
		lCApellido.setItems(EmplList);		
	//DEPT
		final Label lLDept = new Label(grupoIzq, SWT.NONE);
		final Combo lCDept = new Combo(grupoIzq, SWT.NONE);
		Button lBDept = new Button(grupoIzq, SWT.NONE);
		lLDept.setLayoutData(new GridData(SWT.LEFT,  SWT.CENTER, true, true, 1, 1));
		lCDept.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		lBDept.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		lCDept.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				departamiento = lCDept.getText();
			}		
		});
		lLDept.setText("Departament");
		lBDept.setText("->");	
		String [] NumlList = {"bano","cocina"};
		lCDept.setItems(NumlList);

		
	////Establecemos el grupoDER
		final List  dLEmail = new List(grupoDer, SWT.MULTI | SWT.V_SCROLL);
		dLEmail.setLayoutData(new GridData(SWT.FILL,  SWT.FILL, true, true, 1, 1));	
		dLEmail.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println(dLEmail.getSelectionIndex());
			}
			public void widgetSelected(SelectionEvent e) {
				
				String str [] = dLEmail.getSelection();
				itemNumbers = dLEmail.getSelectionIndices();
				
			}
			
		});
		
		//abajo		
		Composite cButtons = new Composite(grupoDer,SWT.NONE);
		cButtons.setLayoutData(new GridData(SWT.LEFT, SWT.DOWN, true, true, 1, 1));
		cButtons.setLayout(new GridLayout(3,false));
		
		Button bAdd	    = new Button(cButtons, SWT.PUSH);
		Button bRemove	= new Button(cButtons, SWT.PUSH);
		Button bCancel	= new Button(cButtons, SWT.PUSH);

		bAdd.setText("Add");
		bCancel.setText("Cancel");
		bRemove.setText("Remove");
		bAdd    .setLayoutData (new GridData(SWT.RIGHT,  SWT.DOWN, true, true, 1, 1));
		bRemove .setLayoutData (new GridData(SWT.RIGHT,  SWT.DOWN, true, true, 1, 1));
		bCancel .setLayoutData (new GridData(SWT.RIGHT,  SWT.DOWN, true, true, 1, 1));
	//Button Listeners
	//Definicions	
	// Un listener con lo que hace el botón bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();	
			}
		};
		SelectionAdapter sabAdd = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					dameEmpleadoOut();	
					shell.dispose();
			}
		};
		SelectionAdapter sabRemove = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					dLEmail.remove(itemNumbers);	
			}
		};
		SelectionAdapter sabAddNum = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dLEmail.add(numero);
				System.out.println(numero);
			}
		};
		SelectionAdapter sabAddApe = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dLEmail.add(apeido);
				System.out.println(apeido);
			}
		};
		SelectionAdapter sabAddDep = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				/*
				for (int i=0; i<empleadoIN.size();i++){
					for (int j=0; j<empleadoIN.get(i).getDepartamento().size();j++){
						if ( empleadoIN.get(i).getDepartamento().get(j).getName()== departamiento ){
							dLEmail.add(empleadoIN.get(i).getApellido1()+"_"+empleadoIN.get(i).getApellido2());
						}
					}
				}*/
				
				dLEmail.add(departamiento);
				System.out.println(departamiento);
			}
		};
	//
		lBApellido.addSelectionListener(sabAddApe);
		lBNumero.addSelectionListener(sabAddNum);
		lBDept.addSelectionListener(sabAddDep);
		
		bCancel.addSelectionListener(sabCancelar);
		bAdd.addSelectionListener(sabAdd);
		bRemove.addSelectionListener(sabRemove);
		
		
		
		shell.pack();
		shell.open();
	}
	public ArrayList<Empleado> dameEmpleadoOut(){
		return empleadoOUT;
	}
}
