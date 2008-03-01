package aplicacion;

import java.sql.Time;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import aplicacion.FranjaDib;
import aplicacion.Posicion;
import aplicacion.Util;

/**
 * Esta clase representa un turno.
 * Se corresponde con la representacion del turno en la tabla de la base de datos
 * @author Chema
 *
 */
public class Turno {
	private int idTurno;
	private String descripcion;
	private Time horaEntrada;
	private Time horaSalida;
	private Time horaDescanso;
	private int tDescanso; //minutos de descanso
	
	
	/**
	 * 
	 * @param idTurno  		 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno
	 * @param horaEntrada	 Hora teórica a la que debería comenzar la jornada laboral del usuario 
	 * @param horaSalida	 Hora teórica a la que debería terminar la jornada laboral del usuario
	 * @param horaDescanso	 Hora de inicio del descanso
	 * @param descanso		 Tiempo asignado a descanso (en minutos)

	 */
	public Turno(int idTurno, String descripcion, Time horaEntrada, Time horaSalida, Time horaDescanso, int descanso) {
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.horaDescanso = horaDescanso;
		tDescanso = descanso;

	}
	
	/**
	 * 
	 * @param idTurno  	 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno 
	 * @param horaEntrada	 Hora te�rica a la que deber�a comenzar la jornada laboral del usuario 
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param horaSalida	 Hora te�rica a la que deber�a terminar la jornada laboral del usuario
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param horaDescanso	 Hora de inicio del descanso
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param descanso		 Tiempo asignado a descanso (en minutos)
	 */
	public Turno(int idTurno, String descripcion,String horaEntrada, String horaSalida, String horaDescanso, int descanso) {
		super();
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		tDescanso = descanso;
		this.horaEntrada=Time.valueOf(horaEntrada);
		this.horaSalida=Time.valueOf(horaSalida);
		this.horaDescanso=Time.valueOf(horaDescanso);

	}
	
	
	/**
	 * Prueba, algoritmo
	 * @param id
	 * @deprecared
	 */
	public Turno(int id){
		this.idTurno = id;
		
	}
	
	/**
	 * 
	 * @param t	horas y minutos
	 * @return  devuelve la hora del tiempo introducido
	 */
	public long obtenHoras(Time t){
		   //obtenemos los segundos
		   long segundos = t.getTime() / 1000;		 
		   //obtenemos las horas
		   long horas = segundos / 3600;		 
		   //restamos las horas para continuar con minutos
		   segundos -= horas*3600;		 
		   //igual que el paso anterior
		   long minutos = segundos /60;
		   segundos -= minutos*60;
		   return horas;		
	}
	/**
	 * 
	 * @param t  horas y minutos en formato Time
	 * @return	 devuelve los minutos de la hora introducida
	 */
	public long obtenMinutos(Time t){
		   //obtenemos los segundos
		   long segundos = t.getTime() / 1000;		 
		   //obtenemos las horas
		   long horas = segundos / 3600;		 
		   //restamos las horas para continuar con minutos
		   segundos -= horas*3600;		 
		   //igual que el paso anterior
		   long minutos = segundos /60;
		   segundos -= minutos*60;
		   return minutos;		
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Time getHoraDescanso() {
		return horaDescanso;
	}
	public void setHoraDescanso(Time horaDescanso) {
		this.horaDescanso = horaDescanso;
	}
	public Time getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(Time horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public Time getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(Time horaSalida) {
		this.horaSalida = horaSalida;
	}
	public int getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(int idTurno) {
		this.idTurno = idTurno;
	}
	public int getTDescanso() {
		return tDescanso;
	}
	public void setTDescanso(int descanso) {
		tDescanso = descanso;
	}
	
	
/**
 * 	Estos métodos sirven para representar un turno en un interfaz
 */	
	/** Horas y minutos que el empleado está trabajando en este turno */
	private int horas1,minutos1,horas2,minutos2;
	/** Estos indican si el cursor está encima de una de las dos franjas */
	private boolean activa1 = false;
	private boolean activa2 = false;
	private int anchoLados = 4;

	public void calculaTiempoTrabajado() {
		int franja1, franja2;
		if (tDescanso!=0) {
			franja1 =  horaDescanso.getHours()*60 + horaDescanso.getMinutes() - horaEntrada.getHours()*60 - horaEntrada.getMinutes();		 
			minutos1 = franja1%60;
			horas1 = franja1/60;
			franja2 = horaSalida.getHours()*60 + horaSalida.getMinutes() - horaDescanso.getHours()*60 - horaDescanso.getMinutes() -tDescanso;
			minutos2 = franja2%60;
			horas2 = franja2/60;
		}
		else {
			franja1 = horaSalida.getHours()*60 + horaSalida.getMinutes() - horaEntrada.getHours()*60 - horaEntrada.getMinutes();
			minutos1 = franja1%60;
			horas1 = franja1/60;
			minutos2 = 0;
			horas2 = 0;
		}
	}
	
	
	public Boolean contienePunto (int y, int posV, int margenSup, int sep_vert_franjas, int alto_franjas) {
		Boolean b = false;
		if (y > margenSup+(sep_vert_franjas+alto_franjas)*(posV+1) && y<=margenSup+(sep_vert_franjas+alto_franjas)*(posV+2)) b = true;
		return b;
	}
	

	public void dibujar(Display display, String nombre, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas, int tamHora, int tamSubdiv, int horaInicio, int numSubdiv) {
		calculaTiempoTrabajado();
		// Si el empleado no tiene color, asignarle un color gris
		if (color==null)
			color = new Color(display,120,170,120);
		gc.setBackground(new Color(display, 0,0,0));
		gc.setForeground(new Color(display, 0,0,0));
		int despV = margenSup+(posV+1)*(alto_franjas+sep_vert_franjas);
		if (margenNombres > 0) {
			gc.drawText(nombre, margenIzq, despV, true);
			gc.drawText(horas1+horas2 + ":" + Util.aString(minutos1+minutos2), margenNombres-10, despV, true);
		}
		// Si tDescanso > 0, hay que pintar 2 trozos. Si no, solo 1.
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int inicio1, inicio2, fin1, fin2;
		inicio1 = margenIzq + margenNombres + (horaEntrada.getHours()-horaInicio) * tamHora + (tamHora*horaEntrada.getMinutes())/60;
		fin1    = margenIzq + margenNombres + (horaDescanso.getHours()-horaInicio) * tamHora + (tamHora*horaDescanso.getMinutes())/60;
		inicio2 = margenIzq + margenNombres + (horaDescanso.getHours()-horaInicio+tDescanso/60) * tamHora  + (tamHora*(horaDescanso.getMinutes()+(tDescanso%60)))/60;
		fin2    = margenIzq + margenNombres + (horaSalida.getHours()-horaInicio) * tamHora + (tamHora*horaSalida.getMinutes())/60;
		
		if (tDescanso==0) {
			// Dibujar franja
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio1+2,despV+2,fin2-inicio1,15,10,10);
			cambiarRelleno(display, gc, r,g,b);
			cambiarPincel(display, gc, r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio1,despV,fin2-inicio1,15,8,8);
			gc.drawRoundRectangle(inicio1,despV,fin2-inicio1,15,8,8);
		}
		else {
			// Dibujar primera franja
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio1+2,despV+2,fin1-inicio1,15,10,10);
			cambiarRelleno(display, gc, r,g,b);
			cambiarPincel(display, gc, r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio1,despV,fin1-inicio1,15,8,8);
			gc.drawRoundRectangle(inicio1,despV,fin1-inicio1,15,8,8);
			// Dibujar segunda franja
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio2+2,despV+2,fin2-inicio2,15,10,10);
			cambiarRelleno(display, gc, r,g,b);
			cambiarPincel(display, gc, r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio2,despV,fin2-inicio2,15,8,8);
			gc.drawRoundRectangle(inicio2,despV,fin2-inicio2,15,8,8);
		}
		
		// Dibujar pestaña encima de la franja, si está activa
		if (activa1) {
			// Modificar los colores teniendo siempre en cuenta los límites [0-255]
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio1+2,despV-13,135,20,10,10);
			cambiarRelleno(display, gc, r,g,b);	
			gc.fillRoundRectangle(inicio1, despV-15, 135, 20,8,8);
			gc.drawRoundRectangle(inicio1, despV-15, 135, 20,8,8);
			gc.fillRectangle(inicio1+1,despV+1,Math.min(fin1-inicio1-1,136),12);
			String s1 = "";
			if (minutos1 != 0) s1=' '+ String.valueOf(minutos1) +'m';
			String s  = Util.aString(horaEntrada.getHours()) + ":" + Util.aString(horaEntrada.getMinutes()) + " - " + Util.aString(horaDescanso.getHours()) + ":" + Util.aString(horaDescanso.getMinutes()) + " (" + String.valueOf(horas1)+'h'+s1+')';
			gc.drawText(s, inicio1+5, despV-14, true);
		}
		else if (activa2) {
			// Modificar los colores teniendo siempre en cuenta los límites [0-255]
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio2+2,despV-13,135,20,10,10);
			cambiarRelleno(display, gc, r,g,b);	
			gc.fillRoundRectangle(inicio2, despV-15, 135, 20,8,8);
			gc.drawRoundRectangle(inicio2, despV-15, 135, 20,8,8);
			gc.fillRectangle(inicio2+1,despV+1,Math.min(fin2-inicio2-1,136),12);
			String s1 = "";
			if (minutos2 != 0) s1=' '+ String.valueOf(minutos2) +'m';
			String s = Util.aString(horaDescanso.getHours()+tDescanso/60) + ":" + Util.aString(horaDescanso.getMinutes()+tDescanso%60) + " - " + Util.aString(horaSalida.getHours()) + ":" + Util.aString(horaSalida.getMinutes()) + " (" + String.valueOf(horas2)+'h'+s1+')';
			gc.drawText(s, inicio2+5, despV-14, true);
		}

		
	}
	
	/**
	 * Cambia el color del fondo (background) sin exceder los límites de Color.
	 * Si se excede un límite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarPincel(GC, int, int, int)
	 */
	private void cambiarRelleno(Display display, GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setBackground(new Color(display,r, g, b));
	}

	/**
	 * Cambia el color del pincel (foreground) sin exceder los límites de Color.
	 * Si se excede un límite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarRelleno(GC, int, int, int)
	 */
	private void cambiarPincel (Display display, GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(display,r, g, b));
	}
	
	public void desactivarFranjas() {
		activa1 = false;
		activa2 = false;
	}
	
	/**
	 * Comprueba si el píxel dado está contenido en el interior de la alguna franja
	 * del turno, sin tener en cuenta los bordes, es decir, en el intervalo abierto
	 * (inicio+d,fin-d), donde 'd' es el ancho del borde de la franja, de donde se
	 * coge para estirarla y encogerla.
	 * @param x	Píxel a comprobar
	 * @see #contienePixel(int)
	 * @see	#tocaLadoDerecho(int)
	 * @see #tocaLadoIzquierdo(int)
	 * @return Si inicio+d < x < fin-d.
	 */
	public Boolean contienePixelInt(int x, int margenIzq, int margenNombres, int horaInicio, int tamHora ) {
		Boolean b = false;
		int inicio1 = margenIzq + margenNombres + (horaEntrada.getHours()-horaInicio) * tamHora + (tamHora*horaEntrada.getMinutes())/60;
		int fin1    = margenIzq + margenNombres + (horaDescanso.getHours()-horaInicio) * tamHora + (tamHora*horaDescanso.getMinutes())/60;
		int inicio2 = margenIzq + margenNombres + (horaDescanso.getHours()-horaInicio+tDescanso/60) * tamHora  + (tamHora*(horaDescanso.getMinutes()+(tDescanso%60)))/60;
		int fin2    = margenIzq + margenNombres + (horaSalida.getHours()-horaInicio) * tamHora + (tamHora*horaSalida.getMinutes())/60;
		
		if (x>inicio1+anchoLados && x<fin1-anchoLados) {
			activa1 = true; b = true;
		}
		else if (x>inicio2+anchoLados && x<fin2-anchoLados) {
			activa2 = true; b = true;
		}
		return b;
	}
	

	public void activarFranja1() {
		activa1 = true;
	}
	
	public void activarFranja2() {
		activa2 = true;
	}
}