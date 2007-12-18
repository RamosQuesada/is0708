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
	 * Cambia el color del pincel (foreground) sin exceder los l�mites de Color.
	 * Si se excede un l�mite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarRelleno(GC, int, int, int)
	 */
	private void cambiarPincel (Display display, GC gc, int r, int g, int b) {
		// Controlar l�mites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(display,r, g, b));
	}
	/**
	 * Cambia el color del fondo (background) sin exceder los l�mites de Color.
	 * Si se excede un l�mite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarPincel(GC, int, int, int)
	 */
	private void cambiarRelleno(Display display, GC gc, int r, int g, int b) {
		// Controlar l�mites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setBackground(new Color(display,r, g, b));
	}

	/**
	 * Dibuja una Franja. Si est� activa, coloca sobre la misma una pesta�a informativa
	 * @param display Display sobre el que se est� dibujando
	 * @param gc GC sobre el que se dibuja la Franja
	 * @param despV Desplazamiento vertical en p�xeles desde la parte superior
	 * @param color Color de la Franja
	 */
	public void dibujarFranja (Display display, GC gc, int despV, Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		// Todos los cambios de colores
		cambiarRelleno(display, gc, r-50,g-50,b-50);
		gc.fillRoundRectangle(inicio+2,despV+2,fin-inicio,15,10,10);
		cambiarRelleno(display, gc, r,g,b);
		cambiarPincel(display, gc, r-100,g-100,b-100);
		gc.fillRoundRectangle(inicio,despV,fin-inicio,15,8,8);
		gc.drawRoundRectangle(inicio,despV,fin-inicio,15,8,8);
		// Si la franja est� activa, mostrar una pesta�a con informaci�n adicional
		// TODO Si la franja est� muy a la derecha y es peque�a, la pesta�a se sale

		// Dibujar pesta�a encima de la franja, si est� activa
		if (activa) {
			int subDivs = 0;
			subDivs += (pfin.dameHora() - pinicio.dameHora())*12;
			subDivs += (pfin.dameCMin() - pinicio.dameCMin());
			// Modificar los colores teniendo siempre en cuenta los l�mites [0-255]
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio+2,despV-13,135,20,10,10);
			cambiarRelleno(display, gc, r,g,b);	
			gc.fillRoundRectangle(inicio, despV-15, 135, 20,8,8);
			gc.drawRoundRectangle(inicio, despV-15, 135, 20,8,8);
			gc.fillRectangle(inicio+1,despV+1,Math.min(fin-inicio-1,136),12);
			String s1 = "";
			if (subDivs%12*60/12 != 0) s1=' '+String.valueOf(aString(subDivs%12*60/12))+'m';
			String s  = aString(pinicio.dameHora()%24) + ":" + aString(pinicio.dameCMin()*60/12) + " - " + aString(pfin.dameHora()%24) + ":" + aString(pfin.dameCMin()*60/12) + " (" + String.valueOf(subDivs/12)+'h'+s1+')';
			gc.drawText(s, inicio+5, despV-14, true);
		}
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
		return (x>=inicio && x<=fin);// && (y>10 && y<20);
	}
	/**
	 * Comprueba si el píxel dado está contenido en el interior de la franja, sin tener en
	 * cuenta los bordes, es decir, en el intervalo abierto (inicio+d,fin-d),
	 * donde 'd' es el ancho del borde de la franja, de donde se coge para estirarla y 
	 * encogerla.
	 * @param x	Píxel a comprobar
	 * @see #contienePixel(int)
	 * @see	#tocaLadoDerecho(int)
	 * @see #tocaLadoIzquierdo(int)
	 * @return Si inicio+d < x < fin-d.
	 */
	public Boolean contienePixelInt(int x) {
		Boolean mueve = false;
		if (x>inicio+anchoLados && x<fin-anchoLados) mueve = true;
		return mueve;
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