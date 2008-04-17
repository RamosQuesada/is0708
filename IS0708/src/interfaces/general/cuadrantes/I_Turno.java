package interfaces.general.cuadrantes;

import java.sql.Time;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;

import aplicacion.datos.Turno;
import aplicacion.utilidades.Util;

/**
 * Esta clase extiende la clase turno para añadir las funcionalidades de dibujo.
 * @author Daniel Dionne
 *
 */
public class I_Turno extends aplicacion.datos.Turno {
	/**
	 * Crea una instancia nueva a partir de un turno dado
	 * @param t turno a copiar
	 */
	public I_Turno (Turno t){
		super(t);
	}
	
	public I_Turno(int idTurno, String descripcion, Time horaEntrada, Time horaSalida, Time horaDescanso, int descanso, Color color) {
		super(idTurno, descripcion, horaEntrada, horaSalida, horaDescanso, descanso, color);
	}
	
	public I_Turno(int idTurno, String descripcion,String horaEntrada, String horaSalida, String horaDescanso, int descanso, Color color) {
		super(idTurno, descripcion, horaEntrada, horaSalida, horaDescanso, descanso,color);
	}
		
	
/**
 * 	Estos métodos sirven para representar un turno en un interfaz
 */	
	/** Horas y minutos que el empleado está trabajando en este turno */
	private int horas1,minutos1,horas2,minutos2;
	/** Valores que indican si el cursor está encima de una de las dos franjas */
	private boolean activa1 = false;
	private boolean activa2 = false;
	private int anchoLados = 4;
	int inicio1, inicio2, fin1, fin2;
	int despl = 0; // Para saber de dónde la he cogido
	private boolean modificado; // indica si ha sido modificado

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
	
	public void recalcularFranjas(int margenIzq, int margenNombres, int horaApertura, int tamHora) {
		inicio1 = margenIzq + margenNombres + (horaEntrada.getHours()-horaApertura) * tamHora + (tamHora*horaEntrada.getMinutes())/60;
		if (tDescanso==0) {
			fin1 = margenIzq + margenNombres + (horaSalida.getHours()-horaApertura) * tamHora + (tamHora*horaSalida.getMinutes())/60;
		}
		else {
			fin1    = margenIzq + margenNombres + (horaDescanso.getHours()-horaApertura) * tamHora + (tamHora*horaDescanso.getMinutes())/60;
			inicio2 = margenIzq + margenNombres + (horaDescanso.getHours()-horaApertura+tDescanso/60) * tamHora  + (tamHora*(horaDescanso.getMinutes()+(tDescanso%60)))/60;
			fin2    = margenIzq + margenNombres + (horaSalida.getHours()-horaApertura) * tamHora + (tamHora*horaSalida.getMinutes())/60;
		}
	}
	
	public void dibujar(Display display, String nombre, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas, int tamHora, int tamSubdiv, int horaApertura, int numSubdiv, int y) {
		calculaTiempoTrabajado();
		// Si el empleado no tiene color, asignarle un color verde
		if (color==null)
			color = new Color(display,120,170,120);
		gc.setBackground(new Color(display, 0,0,0));
		gc.setForeground(new Color(display, 0,0,0));
		int despV = margenSup+(posV+1)*(alto_franjas+sep_vert_franjas);
		if (y!=0) despV = y;
		if (margenNombres > 0) {
			gc.drawText(nombre, margenIzq, despV, true);
			gc.drawText(horas1+horas2+(minutos1+minutos2)/60 + ":" + Util.aString((minutos1+minutos2)%60), margenNombres-10, despV, true);
		}
		// Si tDescanso > 0, hay que pintar 2 trozos. Si no, solo 1.
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		// Dibujar franja
		Util.cambiarRelleno(display, gc, r-50,g-50,b-50);
		gc.fillRoundRectangle(inicio1+2,despV+2,fin1-inicio1,15,10,10);
		Util.cambiarRelleno(display, gc, r,g,b);
		Util.cambiarPincel(display, gc, r-100,g-100,b-100);
		gc.fillRoundRectangle(inicio1,despV,fin1-inicio1,15,8,8);
		gc.drawRoundRectangle(inicio1,despV,fin1-inicio1,15,8,8);
		if (tDescanso!=0) {
			// Dibujar segunda franja
			Util.cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio2+2,despV+2,fin2-inicio2,15,10,10);
			Util.cambiarRelleno(display, gc, r,g,b);
			Util.cambiarPincel(display, gc, r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio2,despV,fin2-inicio2,15,8,8);
			gc.drawRoundRectangle(inicio2,despV,fin2-inicio2,15,8,8);
		}
		
		// Dibujar pestaña encima de la franja, si está activa
		if (activa1) {
			// Modificar los colores teniendo siempre en cuenta los límites [0-255]
			Util.cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio1+2,despV-13,135,20,10,10);
			Util.cambiarRelleno(display, gc, r,g,b);	
			gc.fillRoundRectangle(inicio1, despV-15, 135, 20,8,8);
			gc.drawRoundRectangle(inicio1, despV-15, 135, 20,8,8);
			gc.fillRectangle(inicio1+1,despV+1,Math.min(fin1-inicio1-1,136),12);
			String s1 = "";
			if (minutos1 != 0) s1=' '+ String.valueOf(minutos1) +'m';
			String s;
			if (tDescanso==0)
				s =  Util.aString(horaEntrada.getHours()) + ":" + Util.aString(horaEntrada.getMinutes()) + " - " + Util.aString(horaSalida.getHours()) + ":" + Util.aString(horaSalida.getMinutes()) + " (" + String.valueOf(horas1)+'h'+s1+')';
			else
				s = Util.aString(horaEntrada.getHours()) + ":" + Util.aString(horaEntrada.getMinutes()) + " - " + Util.aString(horaDescanso.getHours()) + ":" + Util.aString(horaDescanso.getMinutes()) + " (" + String.valueOf(horas1)+'h'+s1+')';
			gc.drawText(s, inicio1+5, despV-14, true);
		}
		else if (activa2) {
			// Modificar los colores teniendo siempre en cuenta los límites [0-255]
			Util.cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio2+2,despV-13,135,20,10,10);
			Util.cambiarRelleno(display, gc, r,g,b);	
			gc.fillRoundRectangle(inicio2, despV-15, 135, 20,8,8);
			gc.drawRoundRectangle(inicio2, despV-15, 135, 20,8,8);
			gc.fillRectangle(inicio2+1,despV+1,Math.min(fin2-inicio2-1,136),12);
			String s1 = "";
			if (minutos2 != 0) s1=' '+ String.valueOf(minutos2) +'m';
			int hd = horaDescanso.getHours()*60+horaDescanso.getMinutes()+tDescanso;
			String s = Util.aString(hd/60) + ":" + Util.aString(hd%60) + " - " + Util.aString(horaSalida.getHours()) + ":" + Util.aString(horaSalida.getMinutes()) + " (" + String.valueOf(horas2)+'h'+s1+')';
			gc.drawText(s, inicio2+5, despV-14, true);
		}
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
	public boolean contienePixelInt(int x) {
		return (contienePixelInt1(x, anchoLados) ||
				contienePixelInt2(x, anchoLados));
	}
	
	private boolean contienePixelInt1(int x, int ancho ) {
		boolean b = false;
		if (x>inicio1+ancho && x<fin1-ancho) {
			activa1 = true; b = true;
		}
		return b;
	}
	
	private boolean contienePixelInt2(int x, int ancho ) {
		boolean b = false;
		if (tDescanso!=0) {			
			if (x>inicio2+ancho && x<fin2-ancho) {
				activa2 = true; b = true;
			}
		}
		return b;
	}

	/**
	 * Comprueba si el píxel dado está contenido en el lado izquierdo de la franja, es decir,
	 * en el intervalo cerrado [inicio-d,inicio+d], donde 'd' es el ancho del borde de la franja,
	 * de donde se coge para estirarla y encogerla.
	 * @param x P?xel a comprobar
	 * @see #contienePixel(int)
	 * @see #contienePixelInt(int)
	 * @see	#tocaLadoDerecho(int)
	 * @return Si inicio-d <= x <= inicio+d
	 */
	public boolean tocaLadoIzquierdo(int x) {
		return (tocaLadoIzquierdo1(x) || 
				tocaLadoIzquierdo2(x));
	}

	private boolean tocaLadoIzquierdo1(int x) {
		boolean cambiaInicio = false;
		if (x>=inicio1-anchoLados && x<=inicio1+anchoLados)                 { activa1 = true; activa2=false; cambiaInicio = true; }
		return cambiaInicio;
	}
	
	private boolean tocaLadoIzquierdo2(int x) {
		boolean cambiaInicio = false;
		if (tDescanso!=0) {
			if (x>=inicio2-anchoLados && x<=inicio2+anchoLados) { activa2 = true; activa1=false; cambiaInicio = true; }
		}
		return cambiaInicio;
	}

	/**
	 * Comprueba si el píxel dado está contenido en el lado izquierdo de la franja, es decir,
	 * en el intervalo cerrado [inicio-d,inicio+d], donde 'd' es el ancho del borde de la franja,
	 * de donde se coge para estirarla y encogerla. 
	 * @param x
	 * @see #contienePixel(int)
	 * @see #contienePixelInt(int)
	 * @see #tocaLadoIzquierdo(int)
	 * @return Si fin-d <= x <= fin+d
	 */
	public boolean tocaLadoDerecho(int x) {
		return (tocaLadoDerecho1(x) || 
				tocaLadoDerecho2(x));
	}
	
	private boolean tocaLadoDerecho1(int x) {
		boolean cambiaFin = false;
		if (x>=fin1-anchoLados && x<=fin1+anchoLados)                 { activa1 = true; activa2=false; cambiaFin = true; }
		return cambiaFin;
	}

	private boolean tocaLadoDerecho2(int x) {
		boolean cambiaFin = false;
		if (tDescanso!=0) {
			if (tDescanso!=0 && x>=fin2-anchoLados && x<=fin2+anchoLados) { activa2 = true; activa1=false; cambiaFin = true; }
		}
		return cambiaFin;
	}

	public void activarFranja1() {
		activa1 = true;
	}
	
	public void activarFranja2() {
		activa2 = true;
	}
	
	/**
	 * Realiza las acciones pertinentes al pulsar el botón secundario del ratón
	 * @param x la posición horizontal del cursor
	 * @param margenIzq el margen izquierdo
	 * @param margenNombres el margen para los nombres
	 * @param horaApertura la hora de apertura
	 * @param tamHora el tamaño de una hora
	 * @param tamSubdiv el tamaño de una subdivision
	 */
	public void botonSecundario (int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv) {
		modificado = true;
		// Si hay descanso, se elimina la franja seleccionada
		if (tDescanso!=0) {
			// Si estoy borrando la primera franja, tengo que copiar la segunda a la primera
			if (contienePixelInt1(x, 0)) {
				horaEntrada.setHours(horaDescanso.getHours()+(tDescanso/60));
				horaEntrada.setMinutes(horaDescanso.getMinutes()+(tDescanso%60));
				quitarDescanso();
				activa1 = false;
				recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
			}
			// Si estoy borrando la segunda franja, tengo que adelantar la salida
			else if (contienePixelInt2(x, 0)) {
				horaSalida.setHours(horaDescanso.getHours());
				horaSalida.setMinutes(horaDescanso.getMinutes());
				quitarDescanso();
				activa2 = false;
				recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
			}
		}
		// Si no hay descanso, se añade 
		else {
			if (contienePixelInt1(x, 0)) {
				//Insertar un descanso predeterminado de 30 minutos en donde se ha pinchado
				tDescanso = 30;
				horaDescanso.setHours(dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora));
				horaDescanso.setMinutes(dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv));
				// Ahora falta comprobar que la vuelta del descanso no sea posterior 
				// a la salida
				recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
				if (fin1>=fin2) {
					horaSalida.setHours(horaDescanso.getHours()+tDescanso/60+1);
				}
			}
		}
	}
	
	public int botonPrimario (int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv) {
		// Movimiento:
		//  1 - Moviendo inicio franja
		//  2 - Moviendo toda la franja
		//  3 - Moviendo fin franja
		int m = 0;
		if      (tocaLadoIzquierdo(x)) m = 1;
		else if (contienePixelInt(x)) { m = 2; 
			//Calcular minutos al inicio de la franja
			calcularDespl(x, margenIzq, margenNombres, horaApertura, tamHora, tamSubdiv, numSubdiv);
		}
		else if (tocaLadoDerecho(x)) m = 3;
		return m;
		
	};
	
	private void calcularDespl(int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv) {
		if (activa1) {
			despl = 
				dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora)*numSubdiv +
				dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv) -
				horaEntrada.getHours()*numSubdiv -
				horaEntrada.getMinutes()*numSubdiv/60;
		}
		else {
			despl = 
				dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora)*numSubdiv +
				dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv) -
				horaDescanso.getHours()*numSubdiv -
				horaDescanso.getMinutes()*numSubdiv/60 -
				tDescanso*numSubdiv/60;
		}

	}
	
	public int moverLadoIzquierdo(int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv){
		modificado = true;
		int mov = 1; // Esta variable indica si sigo moviendo el lado izquierdo
		int h = dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora);
		int m = dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv);
		int t = h*60+m;
		if (activa1) {
			// Si me paso por la izquierda, pegar al borde
			if (h<horaApertura) {
				horaEntrada.setHours(horaApertura);
				horaEntrada.setMinutes(0);
			}
			// Si me paso por la derecha, y hay descanso, eliminar franja
			else if (tDescanso!=0 && t>=horaDescanso.getHours()*60 + horaDescanso.getMinutes()) {
				horaEntrada.setHours(horaDescanso.getHours()+tDescanso/60);
				horaEntrada.setMinutes(horaDescanso.getMinutes()+tDescanso%60);
				quitarDescanso();
				activa1=false;
				mov = 0;
			}
			// Si me paso por la derecha, y no hay descanso
			else if (tDescanso==0 && t>=horaSalida.getHours()*60 + horaSalida.getMinutes()) {
				// no hace nada		
			}
			// Desplazamiento normal
			else {
				horaEntrada.setHours(h); 
				horaEntrada.setMinutes(m);
			}
		}
		else if (activa2) {
			// Si me paso por la izquierda, juntar franjas
			if (t<=horaDescanso.getHours()*60 + horaDescanso.getMinutes()) {
				quitarDescanso();
				activa2 = false;
				mov = 0;
			}
			//Si me paso por la derecha, eliminar franja
			else if (t>=horaSalida.getHours()*60+ horaSalida.getMinutes()) {
				horaSalida.setHours(horaDescanso.getHours());
				horaSalida.setMinutes(horaDescanso.getMinutes());
				quitarDescanso();
				activa2 = false;
				mov = 0;
			}
			// Desplazamiento normal
			else {
				tDescanso = (h - horaDescanso.getHours())*60 + (m-horaDescanso.getMinutes());
			}
		}
		recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
		return mov;
	}
	
	public int moverLadoDerecho(int x, int margenIzq, int margenNombres, int horaApertura, int horaCierre, int tamHora, int tamSubdiv, int numSubdiv){
		modificado = true;
		int mov = 3;
		int h = dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora);
		int m = dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv);
		int t = h*60+m;

		if (activa1) {
			// Si me paso por la izquierda 
			if (t<=horaEntrada.getHours()*60 + horaEntrada.getMinutes()) {
				// si hay descanso, eliminar franja
				if (tDescanso!=0) { 
					horaEntrada.setHours(horaDescanso.getHours()+tDescanso/60);
					horaEntrada.setMinutes(horaDescanso.getMinutes()+tDescanso%60);
					quitarDescanso();
					activa1 = false;
					mov = 0;
				}
				// si no hay descanso, no hace nada
			}
			// Si me paso por la derecha y hay descanso, juntar las dos barras
			else if (tDescanso!= 0 && (h>horaDescanso.getHours()+tDescanso/60 ||
				h==horaDescanso.getHours()+tDescanso/60 &&
				m>horaDescanso.getMinutes()+tDescanso%60)) {
				quitarDescanso();
				activa1 = false;
				mov = 0;
			}
			// Si me paso por la derecha y no hay descanso, pegar al borde
			else if (tDescanso == 0 && (h>=horaCierre)) {
				horaSalida.setHours(horaCierre);
				horaSalida.setMinutes(0);
			}
			// Movimiento normal
			else {
				// Si hay descanso
				if (tDescanso!=0) {
				int dif = (horaDescanso.getHours()-h)*60 + horaDescanso.getMinutes()-m;
				tDescanso+=dif;
				horaDescanso.setHours(h);
				horaDescanso.setMinutes(m);
				}
				else {
					horaSalida.setHours(h);
					horaSalida.setMinutes(m);					
				}
			}
		}
		else if (activa2) {
			// Si me paso por la derecha, ajustar al borde
			if (h>=horaCierre) {
				horaSalida.setHours(horaCierre);
				horaSalida.setMinutes(0);
			}
			// Si me paso por la izquierda, eliminar franja
			else if (tDescanso!=0 && (
					t<=horaDescanso.getHours()*60 + horaDescanso.getMinutes()+tDescanso)) {
				horaSalida.setHours(horaDescanso.getHours());
				horaSalida.setMinutes(horaDescanso.getMinutes());
				quitarDescanso();
				activa2 = false;
				mov = 0;
			}
			// Movimiento normal
			else {
				horaSalida.setHours(h);
				horaSalida.setMinutes(m);
			}
		}
		recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
		return mov;
	}
	
	public int moverFranja(int x, int margenIzq, int margenNombres, int horaApertura, int horaCierre, int tamHora, int tamSubdiv, int numSubdiv) {
		modificado = true;
		int mov = 2;
		int h = dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora) - (despl*(60/numSubdiv))/60;
		int m = dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv) - (despl*(60/numSubdiv))%60;
		if (activa1) {
			// Despl es el número de subdivs desde horaEntrada hasta el cursor
			// Si hay descanso
			if (tDescanso!=0) {
				int tVuelta = horaDescanso.getHours()*60+ horaDescanso.getMinutes() + tDescanso;
				int tamFranja = horaDescanso.getHours()*60 + horaDescanso.getMinutes() - (
						horaEntrada.getHours()*60 + horaEntrada.getMinutes());
				// Me paso por la izquierda
				if (h*60+m<=horaApertura*60) {
					horaEntrada.setHours(horaApertura);
					horaEntrada.setMinutes(0);
					horaDescanso.setHours(horaApertura + tamFranja/60);
					horaDescanso.setMinutes(tamFranja%60);
					tDescanso = tVuelta - (horaDescanso.getHours()*60+ horaDescanso.getMinutes());
				// Movimiento normal
				} else {
					horaEntrada.setHours(h);
					horaEntrada.setMinutes(m);
					horaDescanso.setHours(h + tamFranja/60);
					horaDescanso.setMinutes(m + tamFranja%60);
					tDescanso = tVuelta - (horaDescanso.getHours()*60+ horaDescanso.getMinutes());
				}
				// Si tDescanso resulta negativo, juntar las dos franjas
				if (tDescanso<=0) quitarDescanso();

			}
			// Si no hay descanso
			else {
				int tamFranja = horaSalida.getHours()*60 + horaSalida.getMinutes() - (
						horaEntrada.getHours()*60 + horaEntrada.getMinutes());
				if (h*60+m<=horaApertura*60) {
					horaEntrada.setHours(horaApertura);
					horaEntrada.setMinutes(0);
					horaSalida.setHours(horaApertura + tamFranja/60);
					horaSalida.setMinutes(tamFranja%60);		
				// Me paso por la derecha (falta)
				} else if (h*60+m+tamFranja > horaCierre*60) {
					horaSalida.setHours(horaCierre);
					horaSalida.setMinutes(0);
					int j = horaCierre*60-tamFranja;
					horaEntrada.setHours(j/60);
					horaEntrada.setMinutes(j%60);
					
				// Movimiento normal
				} else {
					horaEntrada.setHours(h);
					horaEntrada.setMinutes(m);
					horaSalida.setHours(h + tamFranja/60);
					horaSalida.setMinutes(m + tamFranja%60);
				}
			}
		}
		else if (activa2) {
			// Despl es el número de subdivs desde horaVuelta hasta el cursor
			int tamFranja = horaSalida.getHours()*60 + horaSalida.getMinutes() - (
					horaDescanso.getHours()*60 + horaDescanso.getMinutes()+tDescanso);
			tDescanso = (h - horaDescanso.getHours())*60 + m - horaDescanso.getMinutes();
			// Si tDescanso resulta negativo, juntar las dos franjas
			horaSalida.setHours(horaDescanso.getHours() + tDescanso/60 + tamFranja/60);
			horaSalida.setMinutes(horaDescanso.getMinutes() + tDescanso%60 + tamFranja%60);
			if (tDescanso<=0) {
				quitarDescanso();
				activa2=false;
				activa1=true;
				calcularDespl(x, margenIzq, margenNombres, horaApertura, tamHora, tamSubdiv, numSubdiv);
			} else if (h*60+m+tamFranja > horaCierre*60) {
				horaSalida.setHours(horaCierre);
				horaSalida.setMinutes(0);
				int vuelta = horaCierre*60-tamFranja;
				tDescanso=vuelta-(horaDescanso.getHours()*60+horaDescanso.getMinutes());
				
			}
		}
		recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
		return mov;
	}
	
	public void quitarDescanso() {
		tDescanso=0;
		horaDescanso.setMinutes(0);
		horaDescanso.setHours(0);
	}
	
	public int dameHoraCursor(int x, int margenIzq, int margenNombres, int horaApertura, int tamHora) {
		int h = ((x-margenIzq-margenNombres)/tamHora)+horaApertura;
		if (h<0) h=0;
		return h;
	}
	
	public int dameSubdivCursor(int x, int margenIzq, int margenNombres, int tamHora, int tamSubdiv) {
		int m = ((x-margenIzq-margenNombres)%tamHora)/tamSubdiv;
		if (m<0) m=0;
		return m;
	}

	public boolean isModificado() {
		return modificado;
	}

	public void setModificado(boolean modificado) {
		this.modificado = modificado;
	}
	
	
}