package interfaces;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import aplicacion.Empleado;
import aplicacion.Vista;

public class I13_Elegir_empleado {
	private Composite padre;
	private Text tNombre;
	private List listFiltro;
	private ArrayList<Empleado> empleadosIn, empleadosOut;
	private Shell listShell;
	public I13_Elegir_empleado(Composite padre, Vista vista) {
		this.padre = padre;
		// Esta búsqueda debería coger sólo un departamento, porque el número de 
		// empleados puede ser demasiado grande.
		empleadosIn = vista.getEmpleados(null, null, null, null, null, null, null);
		empleadosOut = new ArrayList<Empleado>();
		listShell = new Shell (padre.getShell(), SWT.NONE);
		mostrarVentana();
	}

	public void mostrarVentana() {
		GridLayout lShell = new GridLayout(2,false);
		padre.setLayout(lShell);

		tNombre = new Text(padre, SWT.BORDER);
		tNombre.setLayoutData(new GridData(SWT.FILL,  SWT.CENTER, true, true, 1, 1));
		tNombre.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				filtrar();
			}
		});
		tNombre.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode==16777218 && listShell.isVisible()) {
					listFiltro.setFocus();
					listFiltro.setSelection(0);
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
		listShell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
					listShell.setVisible(false);
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = false;
					break;
				}
			}
		});
		listFiltro = new List(listShell, SWT.MULTI | SWT.V_SCROLL);
		listFiltro.setSize(200,50);
		listShell.setSize(200,50);
		listShell.open();
		listShell.setVisible(false);
		listFiltro.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode==13)				
					tNombre.setText(listFiltro.getItem(listFiltro.getSelectionIndex()));		
			}
			public void keyReleased(KeyEvent e) {}
		});
		listFiltro.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				tNombre.setText(listFiltro.getItem(listFiltro.getSelectionIndex()));
			}
			public void mouseUp(MouseEvent e) {}
			
		});
		
		// Un listener para esconder la ventana si el padre cambia de posición
		padre.getShell().addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
				listShell.setVisible(false);
			}
			public void controlResized(ControlEvent e) {
				listShell.setVisible(false);		
			}
		});
		
	}	
	public void filtrar() {
		empleadosOut.clear();
		listFiltro.removeAll();
		if (!tNombre.getText().isEmpty()) {
			for (int i=0; i<empleadosIn.size(); i++) {
				if (tNombre.getText().isEmpty() || empleadosIn.get(i).getNombre().toLowerCase().contains(tNombre.getText().toLowerCase()))
					empleadosOut.add(empleadosIn.get(i));
			}
			for (int i=0; i<empleadosOut.size(); i++) {
				listFiltro.add(empleadosOut.get(i).getNombreCompleto());
			}
			if (empleadosOut.size()>0) {
				listShell.setVisible(true);
				listShell.setLocation(padre.getShell().getLocation().x+10 + tNombre.getLocation().x,padre.getShell().getLocation().y + tNombre.getLocation().y+ 50);
				tNombre.setFocus();
			}
			else
				listShell.setVisible(false);
		}
		else listShell.setVisible(false);
	}
}
