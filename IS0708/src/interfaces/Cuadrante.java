package interfaces;

import java.util.ArrayList;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import aplicacion.Posicion;
import aplicacion.FranjaDib;
import aplicacion.Empleado;
//De dónde coger javadoc: http://javashoplm.sun.com/ECom/docs/Welcome.jsp?StoreId=22&PartDetailId=jdk-6u3-oth-JPR&SiteId=JSC&TransactionId=noreg

/**
 * Dibuja un cuadrante sobre un GC.
 * 
 * @author	Daniel Dionne
 * @version	0.1
 * 
 */
public class Cuadrante { 
	private Display display;
	private int ancho;
	private int alto;
	private int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	public int alto_franjas;
	public int sep_vert_franjas;
	private int horaInicio, horaFin; // Definen de qué hora a qué hora es el cuadrante
	public int tamHora, tamSubdiv;
	public int subdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	public ArrayList<Empleado> empleados;



	/**
	 * @author Daniel
	 *
	 */
	public class Turno {
		int id;
		String nombre;
		String abrev;
	}
	/**
	 * Constructor del cuadrante.
	 * @param d				Display sobre el que se dibujará el cuadrante
	 * @param subdivisiones	Número de subdivisiones que se muestran en el cuadrante.  
	 * 						<ul>
	 * 						<li>12	(cada 5 min),
	 * 						<li>6	(cada 10 min),
	 * 						<li>4	(cada 15 min),
	 * 						<li>2	(cada 30 min),
	 * 						<li>1	(sin subdivisiones)
	 * 						</ul>
	 * @param horaInicio	Hora de inicio del cuadrante
	 * @param horaFin		Hora de fin del cuadrante. Las horas pasadas de las 24 se muestran
	 * 						como la madrugada del día siguiente.
	 * @param margenIzq		Margen izquierdo en píxeles
	 * @param margenDer		Margen derecho en píxeles
	 * @param margenSup		Margen superior en píxeles
	 * @param margenInf		Margen inferior en píxeles
	 * @param margenNombres	Margen de los nombres en píxeles (indica dónde empieza a dibujarse
	 * 						el cuadrante a partir del margen izquierdo, dejando un espacio para
	 * 						los nombres.
	 */
	public Cuadrante(Display d, int subdivisiones, int horaInicio, int horaFin, int margenIzq, int margenDer, int margenSup, int margenInf, int margenNombres, ArrayList<Empleado> empleados) {
		this.empleados = empleados;
		display = d;
		this.margenIzq  = margenIzq;
		this.margenDer  = margenDer;
		this.margenSup  = margenSup;
		this.margenInf  = margenInf;
		this.margenNombres  = margenNombres;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		alto_franjas = 15;
		sep_vert_franjas = 10;
		this.subdivisiones = subdivisiones;
		
		// TODO Borrar esto cuando se importen los empleados

	}
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujará el cuadrante.
	 * @param empleadoActivo	La posición del empleado a resaltar en la lista de empleados.
	 */
	// TODO Debería lanzar una excepción si empleadoActivo > empleados.size
	public void dibujarCuadranteDia(GC gc, int empleadoActivo) {
		dibujarSeleccion(gc, empleadoActivo);
		dibujarHoras(gc);
		for (int i=0; i<empleados.size(); i++) {
			empleados.get(i).dibujarFranjas(display, gc, i, empleados.get(i).dameColor(),margenIzq, margenNombres,margenSup,sep_vert_franjas,alto_franjas);
		}
	}
	
	public void dibujarCuadranteMes(GC gc){
		Calendar c = Calendar.getInstance();
		// Esto coge el día 1 de este mes
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		// Y esto en qué día de la semana cae
		int primerDia = c.get(Calendar.DAY_OF_WEEK);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		c.roll(Calendar.DAY_OF_MONTH,false); // Pasa al último día del este mes
		int ultimoDia = c.get(Calendar.DAY_OF_MONTH);
		int anchoMes = ancho - margenIzq - margenDer - margenNombres;
		int anchoDia = anchoMes/ultimoDia;
		int altoFila = 20;
		// Dibujar números de los días
		if (anchoDia>14)
			for (int j=0; j < ultimoDia; j++) {
				gc.drawText(String.valueOf(j+1), margenIzq + margenNombres + j*anchoDia + anchoDia/2, margenSup);
			}

		for (int i=0; i < empleados.size(); i++) {
			gc.drawText(empleados.get(i).dameNombre(), margenIzq, margenSup + 20 + i*altoFila);
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
	 * @param gc	El GC del display sobre el que se dibujará el cuadrante.
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
	 * pero más pálido.
	 * @param gc	El GC sobre el que resaltar el empleado
	 * @param emp	La posición del empleado a resaltar en la lista de empleados. Se considera
	 * 				que -1 significa que no hay ningún empleado seleccionado.
	 */
	// TODO Lanzar excepción si emp > empleados.size
	private void dibujarSeleccion (GC gc, int emp) {
		if (emp!=-1) {
			gc.setForeground(new Color(display, 255-(255-empleados.get(emp).dameColor().getRed())/5, 255-(255-empleados.get(emp).dameColor().getGreen())/5, 255-(255-empleados.get(emp).dameColor().getBlue())/5));
			gc.fillRectangle(margenNombres+margenIzq,margenSup+(sep_vert_franjas+alto_franjas)*(emp+1)-5,ancho-margenNombres-margenIzq-margenDer,alto_franjas+11);
		}
	}
	/**
	 * Pega el valor x al más cercano dentro de la rejilla. El tamaño de la rejilla está determinado
	 * por el número de subdivisiones.
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
			// En otro caso, hay que tener en cuenta cómo se dibuja el cuadrante para evitar
			// desfases entre las lineas horarias y las franjas.
			p = new Posicion(y/tamHora+horaInicio,((y%tamHora)/(tamHora/subdivisiones))*12/subdivisiones);
		return p;
	}
	/**
	 * Actualiza el tamaño del cuadrante, el tamaño de las horas y las subdivisiones, y para cada
	 * franja, actualiza sus píxeles inicial y final en función de sus valores pinicio y pfin.
	 * @param ancho	El ancho nuevo, en píxeles
	 * @param alto	El alto nuevo, en píxeles
	 */
	public void setTamaño(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaFin-horaInicio);
		tamSubdiv = tamHora/12;
		for (int i=0; i < empleados.size(); i++) {
			Empleado e = empleados.get(i);
			for (int j=0; j < e.franjas.size(); j++) {
				FranjaDib f = e.franjas.get(j);
				f.actualizarPixeles(margenIzq, margenNombres, tamHora, tamSubdiv, subdivisiones, horaInicio);
			}
		}
	}
}