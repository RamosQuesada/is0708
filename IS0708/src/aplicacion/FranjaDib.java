package aplicacion;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;

/**
 * Extiende la clase Franja, que representa un intervalo de tiempo determinado por dos Posiciones,
 * pinicio y pfin, con funciones para poder dibujar y manipular una Franja.
 *  
 * @param pinicio	Posici�n izquierda del intervalo
 * @param pfin		Posici�n derecha del invervalo
 * @param inicio	Posici�n izquierda del intervalo en p�xeles
 * @param fin		Posici�n derecha del intervalo en p�xeles
 * @param activa	Determina si la franja est� activa, para representarla con m�s o menos informaci�n
 * @author Daniel Dionne
 *
 */
public class FranjaDib extends Franja {
	private final int anchoLados = 5; // El ancho de los lados de una franja, de donde se coge para estirarla y encogerla
	public FranjaDib (Posicion pinicio, Posicion pfin) {
		super(pinicio, pfin);
	}


	
	/**
	 * Dibuja una Franja. Si est� activa, coloca sobre la misma una pesta�a informativa
	 * @param display Display sobre el que se est� dibujando
	 * @param gc GC sobre el que se dibuja la Franja
	 * @param despV Desplazamiento vertical en p�xeles desde la parte superior
	 * @param color Color de la Franja
	 */
	public void dibujarFranja (Display display, GC gc, int despV, Color color) {
		


	}
	/**
	 * Evita que la barra se salga por los extremos. Si se sale, la coloca en el extremo.
	 */
	public void pegarALosBordes (int horaInicio, int horaFin) {
		Posicion ancho = pfin.diferencia(pinicio);
		// Comprobar si me he salido por la izquierda
		if (pinicio.dameHora() < horaInicio || pinicio.dameCMin()<0) {
			pinicio.ponHora(horaInicio);
			pinicio.ponCMin(0);
			pfin.suma(pinicio, ancho);
		}
		// Comprobar si me he salido por la derecha
		else if (pfin.dameHora()*12+pfin.dameCMin() >= horaFin*12) {
			Posicion fin = new Posicion(horaFin,0);
			pfin.diferencia(pinicio);
			pinicio.resta(fin, ancho);
			pfin.ponCMin(0);
			pfin.ponHora(horaFin);
		}
	}
	public void actualizarPixeles (int margenIzq, int margenNombres, int tamHora, int tamSubdiv, int subdivisiones, int horaInicio) {
		// Si coincide con una subdivisi�n representada, pagar el inicio a la subdivisi�n para que no quede feo
		if (pinicio.dameCMin()!=0 && pinicio.dameCMin()%(12/subdivisiones)==0) {
			inicio = margenIzq + margenNombres + tamHora*(pinicio.dameHora()-horaInicio) + (tamHora/subdivisiones)*(pinicio.dameCMin()/(12/subdivisiones));
		}
		else
			inicio = margenIzq + margenNombres + tamHora*(pinicio.dameHora()-horaInicio) + tamSubdiv*pinicio.dameCMin();
		if (pfin.dameCMin()!=0 && pfin.dameCMin()%(12/subdivisiones)==0) {
			fin = margenIzq + margenNombres + tamHora*(pfin.dameHora()-horaInicio) + (tamHora/subdivisiones)*(pfin.dameCMin()/(12/subdivisiones));
		}
		else
			fin    = margenIzq + margenNombres + tamHora*(pfin.dameHora()   -horaInicio) + tamSubdiv*pfin.dameCMin();
	}

	public Boolean contienePixel(int x, int y) {
		return (x>=inicio && x<=fin) && (y>10 && y<20);
	}
	
	/**
	 * Comprueba si el p�xel dado est� contenido en el lado izquierdo de la franja, es decir,
	 * en el intervalo cerrado [inicio-d,inicio+d], donde 'd' es el ancho del borde de la franja,
	 * de donde se coge para estirarla y encogerla.
	 * @param x P�xel a comprobar
	 * @see #contienePixel(int)
	 * @see #contienePixelInt(int)
	 * @see	#tocaLadoDerecho(int)
	 * @return Si inicio-d <= x <= inicio+d
	 */
	public Boolean tocaLadoIzquierdo(int x) {
		Boolean cambiaInicio = false;
		if (x>=inicio-anchoLados && x<=inicio+anchoLados) cambiaInicio = true;
		return cambiaInicio;
	}
	/**
	 * Comprueba si el p�xel dado est� contenido en el lado izquierdo de la franja, es decir,
	 * en el intervalo cerrado [inicio-d,inicio+d], donde 'd' es el ancho del borde de la franja,
	 * de donde se coge para estirarla y encogerla. 
	 * @param x
	 * @see #contienePixel(int)
	 * @see #contienePixelInt(int)
	 * @see #tocaLadoIzquierdo(int)
	 * @return Si fin-d <= x <= fin+d
	 */
	public Boolean tocaLadoDerecho(int x) {
		Boolean cambiaFin = false;
		if (x>=fin-anchoLados && x<=fin+anchoLados) cambiaFin = true;
		return cambiaFin;
	}

}