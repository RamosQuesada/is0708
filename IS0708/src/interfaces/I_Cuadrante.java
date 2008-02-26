package interfaces;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import aplicacion.Empleado;
import aplicacion.FranjaDib;
import aplicacion.Posicion;

/**
 * Esta clase extiende la clase cuadrante para que se pueda:
 * 1. dibujar sobre un GC
 * 2. representar y modificar sobre un canvas
 * @author Daniel D
 *
 */
public class I_Cuadrante extends algoritmo.Cuadrante {
	private Display display;
	private int ancho;
	private int alto;
	private int margenIzq, margenDer, margenSup, margenInf; // M�rgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	public int alto_franjas = 15;
	public int sep_vert_franjas = 10;
	private int horaInicio, horaFin; // Definen de qué hora a qué hora es el cuadrante
	public int tamHora, tamSubdiv;
	public int subdivisiones; // Cu�ntas subdivisiones hacer por hora (0 = sin subdivisiones)
	public ArrayList<Empleado> empleados;

	public I_Cuadrante(int mes, int anio, String idDepartamento) {
		super(mes, anio, idDepartamento);
	}
	
	
	/**
	 * Constructor del cuadrante.
	 * @param d				Display sobre el que se dibujar� el cuadrante
	 * @param subdivisiones	N�mero de subdivisiones que se muestran en el cuadrante.  
	 * 						<ul>
	 * 						<li>12	(cada 5 min),
	 * 						<li>6	(cada 10 min),
	 * 						<li>4	(cada 15 min),
	 * 						<li>2	(cada 30 min),
	 * 						<li>1	(sin subdivisiones)
	 * 						</ul>
	 * @param horaInicio	Hora de inicio del cuadrante
	 * @param horaFin		Hora de fin del cuadrante. Las horas pasadas de las 24 se muestran
	 * 						como la madrugada del d�a siguiente.
	 * @param margenIzq		Margen izquierdo en p�xeles
	 * @param margenDer		Margen derecho en p�xeles
	 * @param margenSup		Margen superior en p�xeles
	 * @param margenInf		Margen inferior en p�xeles
	 * @param margenNombres	Margen de los nombres en p�xeles (indica d�nde empieza a dibujarse
	 * 						el cuadrante a partir del margen izquierdo, dejando un espacio para
	 * 						los nombres.
	 */
	
	public void setMargenes(int margenIzq, int margenDer, int margenSup, int margenInf, int margenNombres) {
		this.margenIzq  = margenIzq;
		this.margenDer  = margenDer;
		this.margenSup  = margenSup;
		this.margenInf  = margenInf;
		this.margenNombres  = margenNombres;	
	}
	
	public void setConfig(int subdivisiones, int horaInicio, int horaFin) {
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.subdivisiones = subdivisiones;	
	}
	
	public void setEmpleados(ArrayList<Empleado> empleados) {
		this.empleados = empleados;
	}
	
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujará el cuadrante.
	 * @param empleadoActivo	La posición del empleado a resaltar en la lista de empleados.
	 */
	// TODO Debería lanzar una excepción si empleadoActivo > empleados.size
	public void dibujarCuadranteDia(Display d, GC gc, int empleadoActivo) {
		dibujarSeleccion(gc, empleadoActivo);
		dibujarHoras(gc);
		dibujarTurnos(gc);
	}
	
	public void dibujarTurnos(GC gc) {
		for (int i=0; i<empleados.size(); i++) {
			empleados.get(i).dibujarTurno(display, gc, i, empleados.get(i).dameColor(),margenIzq, margenNombres,margenSup,sep_vert_franjas,alto_franjas);
		}
	}
	
	public void dibujarCuadranteMes(GC gc){
		Calendar c = Calendar.getInstance();
		// Esto coge el d�a 1 de este mes
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		// Y esto en qu� d�a de la semana cae
		int primerDia = c.get(Calendar.DAY_OF_WEEK);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		c.roll(Calendar.DAY_OF_MONTH,false); // Pasa al �ltimo d�a del este mes
		int ultimoDia = c.get(Calendar.DAY_OF_MONTH);
		int anchoMes = ancho - margenIzq - margenDer - margenNombres;
		int anchoDia = anchoMes/ultimoDia;
		int altoFila = 20;
		// Dibujar n�meros de los d�as
		if (anchoDia>14)
			for (int j=0; j < ultimoDia; j++) {
				gc.drawText(String.valueOf(j+1), margenIzq + margenNombres + j*anchoDia + anchoDia/2, margenSup);
			}

		for (int i=0; i < empleados.size(); i++) {
			gc.drawText(empleados.get(i).getNombre(), margenIzq, margenSup + 20 + i*altoFila);
			for (int j=0; j < ultimoDia; j++) {
				gc.drawRectangle(margenIzq + margenNombres + j*anchoDia, margenSup + 20 + i*altoFila, anchoDia, altoFila);
			}
		}
			
		// Esto es para un calendario normal
		int altoMes = alto - margenSup - margenInf;
		int numSemanas = 5;
		int altoDia = alto/numSemanas;
		
	}
	
	/**
	 * Dibuja lineas verticales representando las horas y las subdivisiones del cuadrante.
	 * @param gc	El GC del display sobre el que se dibujar� el cuadrante.
	 */
	private void dibujarHoras(GC gc) {
		gc.setForeground(new Color(display, 40,80,40));
		int m = margenIzq + margenNombres;
		int h = horaFin - horaInicio;
		int sep = (ancho - m - margenDer)/h;
		int subsep = sep/subdivisiones;
		for (int i=0; i<=h; i++) {
			gc.setLineStyle(SWT.LINE_SOLID);
			gc.setForeground(new Color(display,40,80,40));
			if (sep>14 && sep<=20) gc.drawText(String.valueOf((horaInicio+i)%24),     m+i*sep-5, margenSup, true);
			else if (sep>20)     gc.drawText(String.valueOf((horaInicio+i)%24)+'h', m+i*sep-5, margenSup, true);
			gc.drawLine(m+i*sep, 20+margenSup, m+i*sep, alto-margenInf);
			gc.setForeground(new Color(display, 120,170,120));
			gc.setLineStyle(SWT.LINE_DOT);
			if (i!=h)
				for (int j=1; j<subdivisiones; j++) {
					gc.drawLine(m+i*sep+j*subsep, 20+margenSup+5, m+i*sep+j*subsep, alto-margenInf-5);
			}
		}
		gc.setLineStyle(SWT.LINE_SOLID);
	}
	/**
	 * Dibuja un fondo distinguido para el empleado seleccionado, basado en el color del empleado
	 * pero m�s p�lido.
	 * @param gc	El GC sobre el que resaltar el empleado
	 * @param emp	La posici�n del empleado a resaltar en la lista de empleados. Se considera
	 * 				que -1 significa que no hay ning�n empleado seleccionado.
	 */
	// TODO Lanzar excepci�n si emp > empleados.size
	private void dibujarSeleccion (GC gc, int emp) {
		if (emp!=-1) {
			gc.setForeground(new Color(display, 255-(255-empleados.get(emp).dameColor().getRed())/5, 255-(255-empleados.get(emp).dameColor().getGreen())/5, 255-(255-empleados.get(emp).dameColor().getBlue())/5));
			gc.fillRectangle(margenNombres+margenIzq,margenSup+(sep_vert_franjas+alto_franjas)*(emp+1)-5,ancho-margenNombres-margenIzq-margenDer,alto_franjas+11);
		}
	}
	/**
	 * Pega el valor x al m�s cercano dentro de la rejilla. El tama�o de la rejilla est� determinado
	 * por el n�mero de subdivisiones.
	 * @param x		El valor a ajustar
	 * @return		El valor ajustado a la rejilla
	 */
	public Posicion sticky (int x) {
		int y = x - margenNombres - margenIzq + (tamHora/subdivisiones)/2;
		Posicion p;
		if (((y%tamHora)/(tamHora/subdivisiones))>=subdivisiones)
			// Para evitar resultados del tipo 14:60
			p = new Posicion(1+y/tamHora+horaInicio,0);
		else
			// En otro caso, hay que tener en cuenta c�mo se dibuja el cuadrante para evitar
			// desfases entre las lineas horarias y las franjas.
			p = new Posicion(y/tamHora+horaInicio,((y%tamHora)/(tamHora/subdivisiones))*12/subdivisiones);
		return p;
	}
	/**
	 * Actualiza el tama�o del cuadrante, el tama�o de las horas y las subdivisiones, y para cada
	 * franja, actualiza sus p�xeles inicial y final en funci�n de sus valores pinicio y pfin.
	 * @param ancho	El ancho nuevo, en p�xeles
	 * @param alto	El alto nuevo, en p�xeles
	 */
	public void setTamano(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaFin-horaInicio);
		tamSubdiv = tamHora/12;
		for (int i=0; i < empleados.size(); i++) {
			Empleado e = empleados.get(i);
			for (int j=0; j < e.turno.franjas.size(); j++) {
				FranjaDib f = e.turno.franjas.get(j);
				f.actualizarPixeles(margenIzq, margenNombres, tamHora, tamSubdiv, subdivisiones, horaInicio);
			}
		}
	}

}
