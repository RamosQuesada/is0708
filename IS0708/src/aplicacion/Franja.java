package aplicacion;

/**
 * Representa un intervalo de tiempo determinado por dos Posiciones, pinicio y pfin. Tambi�n
 * almacena ambas posiciones en p�xeles, que se actualizan en funci�n del tama�o del gr�fico,
 * para facilitar la comprobaci�n del contacto con la franja.
 *  
 * @param pinicio	Posici�n izquierda del intervalo
 * @param pfin		Posici�n derecha del invervalo
 * @param inicio	Posici�n izquierda del intervalo en p�xeles
 * @param fin		Posici�n derecha del intervalo en p�xeles
 * @param activa	Determina si la franja est� activa, para representarla con m�s o menos informaci�n
 * @author Daniel Dionne
 *
 */
public class Franja {
	public Posicion pinicio, pfin;
	public int inicio, fin;
	
	protected Boolean activa;
	/**
	 * Constructor de la clase. Dadas las Posiciones pinicio y pfin, actualiza autom�ticamente
	 * los l�mites en p�xeles (inicio y fin) en funci�n del tama�o del gr�fico.
	 * Por defecto, al crearla, la franja no est� activa.
	 * @param pinicio
	 * @param pfin
	 */
	//TODO �Deber�a comprobar si fin <= inicio?
	public Franja (Posicion pinicio, Posicion pfin) {
		this.pinicio = pinicio;
		this.pfin = pfin;
		activa = false;
	}
	/**
	 * Devuelve el string que representa al entero, con un cero delante si es menor que 10.
	 * @param i	N�mero a representar
	 * @return	El String del n�mero con formato xx (ej. "15", "02", "49")
	 */
	public String aString (int i) {
		String s = String.valueOf(i);
		if (i<10) s = '0' + s;
		return s;
	}
	/**
	 * Dibuja una franja, dada la altura de la misma y su color. Adem�s, si est� activa,
	 * mostrar� informaci�n adicional del intervalo representado. Se basa en la posici�n
	 * en p�xeles para dibujar la franja, as� que debe estar actualizada.
	 * 
	 * @param	gc		El contenedor gr�fico donde dibujar la franja
	 * @param	despV	La altura en p�xeles de la franja (0 es arriba)
	 * @param	color	El color de la franja. Normalmente depender� del empleado.
	 */
	// TODO ...as� que debe estar actualizada. Ser�a mejor que la propia clase controlara eso en los setters

	/**
	 * Activa esta franja.
	 */
	public void activarFranja()    {activa = true; }
	/**
	 * Desactiva esta franja.
	 */
	public void desactivarFranja() {activa = false;}
	/**
	 * Comprueba si el p�xel dado est� contenido en la franja, es decir, en el intervalo
	 * [inicio,fin] (n�tese el intervalo cerrado).
	 * @param x	P�xel a comprobar
	 * @see #contienePixelInt(int)
	 * @see	#tocaLadoDerecho(int)
	 * @see #tocaLadoIzquierdo(int)
	 * @return Si el p�xel est� contenido en la franja.
	 */
}
