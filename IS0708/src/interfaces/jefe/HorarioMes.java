/**
 * 
 */
package interfaces.jefe;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import aplicacion.utilidades.Util;


/**
 * @author Alberto
 *
 */
public class HorarioMes {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="236,67"
	private Composite composite = null;
	private Composite padre;
	private Color[] colores;
	private CuadroDia[] cuadros;
	private Composite[] dias;
	private Date fecha;  //  @jve:decl-index=0:
	private int primero;
	private int mes;
	private int anio;

	/**
	 * This method initializes sShell	
	 *
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setLayout(new GridLayout());
		createComposite();
		sShell.setSize(new Point(379, 183));
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		
		composite = new Composite(padre, SWT.NONE);		
		composite.setLayout(gridLayout);		
		composite.setLayoutData(gridData);
		
		for (int i=0;i<42;i++){
			this.cuadros[i]=new CuadroDia(this.composite, this.sShell,null);
		}
		this.cuadros[6].setDomingo();
		this.cuadros[13].setDomingo();
		this.cuadros[20].setDomingo();
		this.cuadros[27].setDomingo();
		this.cuadros[34].setDomingo();
		this.cuadros[41].setDomingo();
		
		this.dias=new Composite[42];
		for (int i=0;i<42;i++){
			dias[i]=cuadros[i].getComposite();
		}
		
	}
	
	public HorarioMes(Composite padre, Shell shellClinica, int mes, int anio) {
		this.padre = padre;
		this.sShell = shellClinica;
		this.mes=mes;
		this.anio=anio;
		
		this.colores = new Color[5];
		this.colores[0] = new Color(Display.getCurrent(), 17, 247, 20);
		this.colores[1] = new Color(Display.getCurrent(), 128, 207, 39);
		this.colores[2] = new Color(Display.getCurrent(), 238, 222, 30);
		this.colores[3] = new Color(Display.getCurrent(), 251, 152, 19);
		this.colores[4] = new Color(Display.getCurrent(), 247, 13, 19);
		
		this.cuadros = new CuadroDia[42];
		
		
		createComposite();
	//	actualizar();
	}

	public Control getComposite() {
		return this.composite;
	}
	
	public void setMes(int mes, int anio){
		this.mes=mes;
		this.anio=anio;
		Date fecha=new Date(this.anio-1900,this.mes-1,1);
		
		switch (fecha.getDay()){
			case 0:this.primero=6;this.cuadros[0].desactivar();this.cuadros[1].desactivar();this.cuadros[2].desactivar();this.cuadros[3].desactivar();this.cuadros[4].desactivar();this.cuadros[5].desactivar();break;
			case 1:this.primero=0;break;
			case 2:this.primero=1;this.cuadros[0].desactivar();break;
			case 3:this.primero=2;this.cuadros[0].desactivar();this.cuadros[1].desactivar();break;
			case 4:this.primero=3;this.cuadros[0].desactivar();this.cuadros[1].desactivar();this.cuadros[2].desactivar();break;
			case 5:this.primero=4;this.cuadros[0].desactivar();this.cuadros[1].desactivar();this.cuadros[2].desactivar();this.cuadros[3].desactivar();break;
			case 6:this.primero=5;this.cuadros[0].desactivar();this.cuadros[1].desactivar();this.cuadros[2].desactivar();this.cuadros[3].desactivar();this.cuadros[4].desactivar();break;
			default:break;
		}
		Date fechaAux=fecha;
		for (int i=this.primero;i<this.primero+Util.dameDias(this.mes,this.anio);i++){			
			cuadros[i].setFecha(fechaAux);
			
			switch (i%7){
			case 0:this.cuadros[i].activar();this.cuadros[i].setFecha(fechaAux);break;
			case 1:this.cuadros[i].activar();this.cuadros[i].setFecha(fechaAux);break;
			case 2:this.cuadros[i].activar();this.cuadros[i].setFecha(fechaAux);break;
			case 3:this.cuadros[i].activar();this.cuadros[i].setFecha(fechaAux);break;
			case 4:this.cuadros[i].activar();this.cuadros[i].setFecha(fechaAux);break;
			case 5:this.cuadros[i].activar();this.cuadros[i].setFecha(fechaAux);break;
			case 6:this.cuadros[i].activar();this.cuadros[i].setFecha(fechaAux);break;
			default:break;
			}
			fechaAux=(Date) Util.diaSiguiente(fechaAux);
		}
		
		for (int i=this.primero+Util.dameDias(this.mes,this.anio);i<42;i++){
			cuadros[i].desactivar();
		}
	}

}
