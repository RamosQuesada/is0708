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
import aplicacion.Vista;
import aplicacion.Posicion;

public class I13_Elegir_empleado {
	private Shell shell;
	private ResourceBundle bundle;
	private Vista vista;
	private Image icoPq;
	private Text tNombre,tApellido;
	private List listFiltro;
	private ArrayList<Empleado> empleadosIn, empleadosOut;
	
	public I13_Elegir_empleado(Shell padre, ResourceBundle bundle, Vista vista) {
		this.bundle   = bundle;
		this.shell = new Shell (padre, SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.shell.setLocation(padre.getBounds().width/2 + padre.getBounds().x - shell.getSize().x/2, padre.getBounds().height/2 + padre.getBounds().y - shell.getSize().y/2);
		this.vista = vista;
		// Esta búsqueda debería coger sólo un departamento, porque el número de 
		// empleados puede ser demasiado grande.
		empleadosIn = vista.getEmpleados(null, null, null, null, null, null, null);
		empleadosOut = new ArrayList<Empleado>();
		mostrarVentana();
		filtrar();
	}

	public void mostrarVentana() {
		icoPq = new Image(shell.getDisplay(), I13_Elegir_empleado.class.getResourceAsStream("icoPq.gif"));
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
		
		grupoIzq.setLayout(new GridLayout(2, false));
		grupoDer.setLayout(new GridLayout(1, false));
		
	//Establecemos el grupoIZQ
	//NUMERO
		final Label lNombre = new Label(grupoIzq, SWT.NONE);
		tNombre = new Text(grupoIzq, SWT.NONE);
		lNombre.setLayoutData(new GridData(SWT.LEFT,  SWT.CENTER, true, true, 1, 1));
		tNombre.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		lNombre.setText(bundle.getString("Nombre"));
		tNombre.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				filtrar();
			}
		});
	//APELLIDO
		final Label lApellido= new Label(grupoIzq, SWT.NONE);
		tApellido = new Text(grupoIzq, SWT.NONE);
		lApellido.setLayoutData(new GridData(SWT.LEFT,  SWT.CENTER, true, true, 1, 1));
		tApellido.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		lApellido.setText(bundle.getString("Apellido"));
		
	//Establecemos el grupoDER
		listFiltro = new List(grupoDer, SWT.MULTI | SWT.V_SCROLL);
		listFiltro.setLayoutData(new GridData(SWT.FILL,  SWT.FILL, true, true, 1, 1));	
		
		//abajo		
		Composite cButtons = new Composite(grupoDer,SWT.NONE);
		cButtons.setLayoutData(new GridData(SWT.LEFT, SWT.DOWN, true, true, 1, 1));
		cButtons.setLayout(new GridLayout(3,false));
		
		Button bFiltrar	    = new Button(cButtons, SWT.PUSH);
		Button bRemove	= new Button(cButtons, SWT.PUSH);
		Button bCancel	= new Button(cButtons, SWT.PUSH);

		bFiltrar.setText("Filtrar");
		bCancel.setText("Cancel");
		bRemove.setText("Remove");
		bFiltrar    .setLayoutData (new GridData(SWT.RIGHT,  SWT.DOWN, true, true, 1, 1));
		bRemove .setLayoutData (new GridData(SWT.RIGHT,  SWT.DOWN, true, true, 1, 1));
		bCancel .setLayoutData (new GridData(SWT.RIGHT,  SWT.DOWN, true, true, 1, 1));
	//Button Listeners
	//Definicions	
	// Un listener con lo que hace el bot�n bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shell.dispose();
			}
		};
		SelectionAdapter sabFiltrar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				filtrar();
			}
		};
		
		bCancel.addSelectionListener(sabCancelar);
		bFiltrar.addSelectionListener(sabFiltrar);
		
		shell.pack();
		shell.open();
	}	
	public void filtrar() {
		empleadosOut.clear();
		listFiltro.removeAll();
		for (int i=0; i<empleadosIn.size(); i++) {
			if (tNombre.getText().isEmpty() || empleadosIn.get(i).getNombre().toLowerCase().contains(tNombre.getText().toLowerCase()))
				empleadosOut.add(empleadosIn.get(i));
		}
		for (int i=0; i<empleadosOut.size(); i++) {
			listFiltro.add(empleadosOut.get(i).getNombreCompleto());
		}
	}
}
