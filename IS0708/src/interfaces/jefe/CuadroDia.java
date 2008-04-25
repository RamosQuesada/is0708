/**
 * 
 */
package interfaces.jefe;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;


/**
 * @author Alberto
 *
 */
public class CuadroDia {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="288,65"
	private Composite composite = null;  //  @jve:decl-index=0:
	private Label lDia = null;
	private Color[] colores = null;
	private Color colorGris=null;


	private Composite padre;

	private Date fecha;  //  @jve:decl-index=0:
	/**
	 * This method initializes sShell	
	 *
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setLayout(new GridLayout());
		createComposite();
		sShell.setSize(new Point(224, 185));
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.CENTER;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.verticalAlignment = GridData.CENTER;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.CENTER;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.CENTER;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.verticalAlignment = GridData.CENTER;
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 4;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalSpan = 5;
		gridData1.verticalAlignment = GridData.CENTER;
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		composite = new Composite(padre, SWT.BORDER);
		composite.setLayoutData(gridData);
		composite.setLayout(gridLayout);
		composite.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				setMarcado();
			}
		});
		lDia = new Label(composite, SWT.NONE);
		if (fecha!=null){
		lDia.setText(String.valueOf(fecha.getDate()));}
		lDia.setLayoutData(gridData1);
		lDia.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				setMarcado();
			}
		});
	}
	
	public CuadroDia(Composite padre, Shell shell, Date fecha) {
		this.padre = padre;
		this.sShell = shell;
		this.fecha = fecha;
		this.colores = new Color[5];
		this.colores[0] = new Color(Display.getCurrent(), 17, 247, 20);
		this.colores[1] = new Color(Display.getCurrent(), 128, 207, 39);
		this.colores[2] = new Color(Display.getCurrent(), 238, 222, 30);
		this.colores[3] = new Color(Display.getCurrent(), 251, 152, 19);
		this.colores[4] = new Color(Display.getCurrent(), 247, 13, 19);
		this.colorGris  = new Color(Display.getCurrent(), 128,128,128);
		createComposite();
	}





	public Composite getComposite() {
		return composite;
	}
	
	public void setDomingo(){
		composite.setBackground(new Color(Display.getCurrent(),255,150,150));
		lDia.setBackground(new Color (Display.getCurrent(),255,150,150));
	}
	
	public void setMarcado(){
		composite.setBackground(new Color(Display.getCurrent(),255,128,0));
		lDia.setBackground(new Color(Display.getCurrent(),255,128,0));
	}
	
	
	public void desactivar(){
		//composite.setBackground(new Color(Display.getCurrent(),192,192,192));
		composite.setVisible(false);
		lDia.setVisible(false);
	}
	
	public void activar(){
		//composite.setBackground(new Color(Display.getCurrent(),236,233,216));
		composite.setVisible(true);
		lDia.setVisible(true);
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
		lDia.setText(String.valueOf(fecha.getDate()));
	}
	
	

}
