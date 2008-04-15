package interfaces.general;

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

import aplicacion.Vista;
import aplicacion.datos.Empleado;

public class DialogElegirEmpleado extends Thread {
	private Composite padre;
	private Text tNombre;
	private List listFiltro;
	private ArrayList<Empleado> empleadosOut;
	private Shell listShell;
	private int idEmpl = 0;
	private Vista vista;
	private ResourceBundle bundle;
	
	
	public DialogElegirEmpleado (Composite padre, Vista vista, ResourceBundle bundle) {
		this.padre = padre;
		this.vista = vista;
		this.bundle = bundle;
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
				if (e.keyCode==13) {				
					idEmpl = empleadosOut.get(listFiltro.getSelectionIndex()).getEmplId();
					tNombre.setText(listFiltro.getItem(listFiltro.getSelectionIndex()));
					listShell.setVisible(false);
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
		listFiltro.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				if (listFiltro.getSelectionIndex()!=-1) {
					idEmpl = empleadosOut.get(listFiltro.getSelectionIndex()).getEmplId();
					tNombre.setText(listFiltro.getItem(listFiltro.getSelectionIndex()));
					listShell.setVisible(false);
				}
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
		if (tNombre.getText()!="") {
			for (int i=0; i<vista.getEmpleados().size(); i++) {
				if (tNombre.getText()=="" || vista.getEmpleados().get(i).getNombreCompleto().toLowerCase().contains(tNombre.getText().toLowerCase()))
					empleadosOut.add(vista.getEmpleados().get(i));
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
	
	public int getIdEmpl() {
		return idEmpl;
	}
	
	public String getNombreEmpl() {
		return tNombre.getText();
	}
}
