/*******************************************************************************
 * INTERFAZ I-02_cuadr :: Canvas de cuadrante
 *   por Daniel Dionne
 *   
 * Crea un canvas con un cuadrante editable.
 * Importa la clase cuadrante que es la que lo dibuja. Esta clase s�lo se encarga
 * de los movimientos y pulsaciones del rat�n.
 * 
 * ver 0.1
 *******************************************************************************/

// TODO Hacer que reduzca la resoluci�n del grid en funci�n del tama�o de la pantalla
package interfaces.empleado;


import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import aplicacion.Vista;
import aplicacion.datos.Empleado;
import aplicacion.utilidades.Util;


/**
 * Dada una instancia de Canvas, que se le pasa como par�metro al constructor,
 * crea un cuadrante sobre la misma.
 *
 */
public class Cuadrantes extends Thread{


	/**
	 * Canvas gráfico donde se dibuja el horario del empleado
	 */
	private Canvas canvas;
	
	/**
	 * Display donde vamos a dibujar 
	 */
	private Display display;
	/**
	 * Alto del cuadrado donde vamos a dibujar.
	 */
	private int alto;
	
	/**
	 * Ancho del cuadrado donde vamos a dibujar
	 */
	private int ancho;
	
	/**
	 * Bundle necesario para la traduccion de los textos
	 */
	private ResourceBundle _bundle;
	
	private MouseMoveListener mouseMoveListenerCuadrMensual;

	/**
	 * Booleando que indica si la seleccion actual es la de verlo por meses o por semanas
	 * (false == meses) (true == semanas)
	 */
	private Boolean semanal; 
	
	/**
	 * Entero que tiene el numero de empleado que tiene actualmente la sesion abierta
	 */
	private int empleadoActivo;
	
	
	/**
	 * Definen la hora de Inicio y la hora de Fin
	 */
	private int horaInicio, horaFin; 
	
	/**
	 * Contiene el empleado actual
	 */
	private Empleado empleado;

	/**
	 * Margen izquierdo de la zona de dibujo
	 */
	private int margenIzq;
	/**
	 * Margen derecho de la zona de dibujo
	 */
	private int margenDer;
	/**
	 * Margen superior de la zona de dibujo
	 */
	private int margenSup;
	/**
	 * Margen inferior de la zona de dibujo
	 */
	private int margenInf;
	/**
	 * 
	 */
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	private final Label lGridCuadrante;
	private final Combo cGridCuadrante;
	private Label lCuadranteTitulo;
	private CuadranteEmpleado cuadrante;
	private GC gc2;
	private Vista vista;


	private void calcularTamano() {
		ancho = canvas.getClientArea().width;
		alto = canvas.getClientArea().height;
		cuadrante.setTamano(ancho, alto);
	}



	/**
	 * metodo que redibuja la ventana
	 */
	public void redibujar() {
		canvas.redraw();
	}

	/**
	 * Método que inicia el thread del cuadrante del empleado
	 */
	public void run(){
		boolean run=true;
		while(run){
			if (lCuadranteTitulo.isDisposed() || vista.getEmpleadoActual().getEmplId()==0) run = false;
			else{
			display.asyncExec(new Runnable () {
				public void run() {
					redibujar();


					cuadrante.ponRepreAvance((cuadrante.dameRepreAvance()+1)%13);
				}
			});
			try {
				sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}


	public void dibujarCuadrante(GC gc) {
		// Doble buffering para evitar parpadeo
		if (ancho != 0 && alto != 0) {
			Image bufferImage = new Image(display, ancho, alto);
			gc2 = new GC(bufferImage);
			try {
				gc2.setAntialias(SWT.ON);
			}
			catch (SWTException ex){
				System.err.println(ex.code);
			}
			if (semanal) cuadrante.dibujarCuadranteDia(gc2, empleadoActivo);
			else cuadrante.dibujarCuadranteMes(gc2);
			gc.drawImage(bufferImage, 0, 0);
			
		}
	}
	
	private void setSubdivisiones(int i) {
		cuadrante.ponSubdivisiones(i);
		redibujar();
	}
	
	/**
	 * Funcion que hace que se fije el dibujado, por semanas
	 */
	public void setSemanal()  {
		semanal = true;
		lGridCuadrante.setVisible(true);
		cGridCuadrante.setVisible(true);
		redibujar();
	}
	
	/**
	 * Funcion que hace que se fije el dibujado, por meses
	 */
	public void setMensual() {
		semanal = false;
		lGridCuadrante.setVisible(false);
		cGridCuadrante.setVisible(false);
		redibujar();
	}
	
	/**
	 * Constructora que recibe como par�metro el Composite donde colocar los botones
	 * y el cuadrante.
	 * @param c	Composite sobre el que dibujar el cuadrante
	 */
	public Cuadrantes(Composite c, Boolean diario,ResourceBundle bundle,Empleado empleado,
			Date fecha,Vista vista) {
		this.semanal = diario;
		this.vista=vista;
		this._bundle = bundle;
	//	this.fecha= fecha;
		this.empleado=empleado;
			
		//a partir de aqui hay q suprimir
		final GridLayout l = new GridLayout(3,false);
		c.setLayout(l);
		
		lCuadranteTitulo= new Label (c, SWT.LEFT);
		if(fecha!=null){lCuadranteTitulo.setText("BIENVENIDO "+empleado.getNombre().toUpperCase()+"  :"+Util.dateAString(fecha));}
		else{lCuadranteTitulo.setText("BIENVENIDO "+empleado.getNombre().toUpperCase()+"  :"+Util.dateAString(new Date(System.currentTimeMillis())));}
		
		lCuadranteTitulo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		lGridCuadrante= new Label (c, SWT.LEFT);
		lGridCuadrante.setText("Mostrar intervalos de");
		lGridCuadrante.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		cGridCuadrante = new Combo(c, SWT.BORDER | SWT.READ_ONLY);
		cGridCuadrante.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		cGridCuadrante.setItems(new String[] {"15 min","30 min", "1 hora"});
		
		cGridCuadrante.select(2);
		
		cGridCuadrante.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e){
				switch (cGridCuadrante.getSelectionIndex()) {
				case 0 : setSubdivisiones(4); break;
				case 1 : setSubdivisiones(2); break;
				case 2 : setSubdivisiones(1);
				}
			}
		});

		
		this.canvas = new Canvas(c, SWT.FILL | SWT.NO_BACKGROUND);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

	//	movimiento = 0;
		margenIzq = 15;
		margenDer = 20;
		margenSup = 1;
		margenInf = 10;
		margenNombres = 0;
		empleadoActivo = -1;
		horaInicio = 9;
		horaFin = 23;
		display = canvas.getDisplay();
		cuadrante = new CuadranteEmpleado(display, 1, horaInicio, horaFin, margenIzq, margenDer, margenSup, margenInf, margenNombres,_bundle,
				empleado,fecha,vista,this);
		
		calcularTamano();
		
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				dibujarCuadrante(event.gc);
			}
		});
		canvas.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {}
			public void controlResized(ControlEvent e) {
				calcularTamano();
			}
		});		
		
		
		
		if (diario) setSemanal(); else setMensual();
		start();
	}


	public Boolean enAreaDibujo(int x, int y) {
		Boolean b = true;
		if (x < margenNombres + margenIzq)
			b = false;
		else if (x > ancho - margenDer)
			b = false;
		else if (y < margenSup)
			b = false;
		else if (y > alto - margenInf)
			b = false;
		return b;
	}
	

	public void actualizaFecha(Date fecha){
	//	this.fecha=fecha;
		lCuadranteTitulo.setText("BIENVENIDO "+empleado.getNombre().toUpperCase()+Util.dateAString(fecha));
		this.cuadrante.actualizarFecha(fecha,gc2);
		this.redibujar();
	}


}
