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
package interfaces;

import java.sql.Date;
import java.util.ResourceBundle;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import aplicacion.Empleado;
import aplicacion.Util;
import aplicacion.Vista;


/**
 * Dada una instancia de Canvas, que se le pasa como par�metro al constructor,
 * crea un cuadrante sobre la misma.
 * @author Daniel
 *
 */
public class I02_cuadrEmpl extends Thread{
	/* TODO
	 * Las barras de tama�o cero se quedan
	 * bug: al hacer muchas franjas peque�itas, no se pegan bien (ver si sigue pasando)
	 */
	private Canvas canvas;
	//public boolean redibujar;
	private int alto, ancho;
	private Display display;
	private ResourceBundle _bundle;
	//private int despl; // Este es para cuando movemos una barra, para saber de d�nde la
				// he cogido
	//private Boolean creando, terminadoDeCrear;
	// La variable terminadoDeCrear sirve para que una franja nueva no desaparezca al crearla
	private Boolean semanal; // 1: muestra cuadrante diario, 0: muestra cuadrante mensual
	private int empleadoActivo;
	private int horaInicio, horaFin; // Definen de qu� hora a qu� hora es el
								// cuadrante
	private Empleado empleado;

	private int margenIzq, margenDer, margenSup, margenInf; // M�rgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	private int movimiento;
	private final Label lGridCuadrante;
	private final Combo cGridCuadrante;
	private Date fecha;
	private Label lCuadranteTitulo;
	private I02CuadranteEmpleado cuadrante;
	private GC gc2;
	private Vista vista;


	private void calcularTamano() {
		ancho = canvas.getClientArea().width;
		alto = canvas.getClientArea().height;
		cuadrante.setTamano(ancho, alto);
	}



	public void redibujar() {
		canvas.redraw();
	}

	public void run(){
		boolean run=true;
		while(run){
			if (lCuadranteTitulo.isDisposed() || vista.getEmpleadoActual().getEmplId()==0) run = false;
			else{
			display.asyncExec(new Runnable () {
				public void run() {
					if(cuadrante.dameRedibujar()){
						
						
						redibujar();
						cuadrante.ponRedibujar(false);
					}
					else{
					//	System.out.println("Esperando"+cuadrante.avance);
					//	redibujar();
						//cuadrante.
						
					}
				}
			});
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
			// TODO Probar la siguiente linea en el laboratorio
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
	public void setSemanal()  {
		semanal = true;
		lGridCuadrante.setVisible(true);
		cGridCuadrante.setVisible(true);
	//	canvas.removeMouseListener(mouseListenerCuadrMensual);
	//	canvas.removeMouseMoveListener(mouseMoveListenerCuadrMensual);
		redibujar();}
	public void setMensual() {
		semanal = false;
		lGridCuadrante.setVisible(false);
		cGridCuadrante.setVisible(false);
	//	canvas.addMouseListener(mouseListenerCuadrMensual);
	//	canvas.addMouseMoveListener(mouseMoveListenerCuadrMensual);
		redibujar();
	}
	/**
	 * Constructora que recibe como par�metro el Composite donde colocar los botones
	 * y el cuadrante.
	 * @param c	Composite sobre el que dibujar el cuadrante
	 */
	public I02_cuadrEmpl(Composite c, Boolean diario,ResourceBundle bundle,Empleado empleado,
			Date fecha,Vista vista) {
		this.semanal = diario;
		this.vista=vista;
		this._bundle = bundle;
		this.fecha= fecha;
		this.empleado=empleado;
		
		
		
		
		//a partir de aqui hay q suprimir
		final GridLayout l = new GridLayout(3,false);
		c.setLayout(l);
		
		
		lCuadranteTitulo= new Label (c, SWT.LEFT);
		String fname = lCuadranteTitulo.getFont().getFontData()[0].getName();
		lCuadranteTitulo.setFont(new Font(c.getDisplay(),fname,10,0));
		// TODO Esto tendr� que cambiarse por la fecha elegida en el calendario
		if(fecha!=null){
			lCuadranteTitulo.setText("BIENVENIDO "+empleado.getNombre().toUpperCase()+"  :"+Util.dateAString(fecha));
		}
		else{
			lCuadranteTitulo.setText("BIENVENIDO "+empleado.getNombre().toUpperCase()+"  :"+Util.dateAString(new Date(System.currentTimeMillis())));
		}
		
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
				//case 0 : setSubdivisiones(12); break;
				case 0 : setSubdivisiones(4); break;
				case 1 : setSubdivisiones(2); break;
				case 2 : setSubdivisiones(1);
				}
			}
		});

		
		this.canvas = new Canvas(c, SWT.FILL | SWT.NO_BACKGROUND);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
	//	creando = false;
	//	terminadoDeCrear = true;

		movimiento = 0;
		margenIzq = 15;
		margenDer = 20;
		margenSup = 1;
		margenInf = 10;
		margenNombres = 90;
		empleadoActivo = -1;
		horaInicio = 9;
		horaFin = 23;
		display = canvas.getDisplay();
		cuadrante = new I02CuadranteEmpleado(display, 1, horaInicio, horaFin, margenIzq, margenDer, margenSup, margenInf, margenNombres,_bundle,
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
		
		this.fecha=fecha;
		//"BIENVENIDO "+empleado.getNombre().toUpperCase()+"  :"+Util.dateAString(new Date(System.currentTimeMillis()))

		lCuadranteTitulo.setText("BIENVENIDO "+empleado.getNombre().toUpperCase()+Util.dateAString(fecha));
		this.cuadrante.actualizarFecha(fecha,gc2);
		this.redibujar();
	}

}
