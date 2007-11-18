package aplicacion;

/**
 * Representa un intervalo de tiempo determinado por dos Posiciones, pinicio y pfin. También
 * almacena ambas posiciones en píxeles, que se actualizan en función del tamaño del gráfico,
 * para facilitar la comprobación del contacto con la franja.
 *  
 * @param pinicio	Posición izquierda del intervalo
 * @param pfin		Posición derecha del invervalo
 * @param inicio	Posición izquierda del intervalo en píxeles
 * @param fin		Posición derecha del intervalo en píxeles
 * @param activa	Determina si la franja está activa, para representarla con más o menos información
 * @author Daniel Dionne
 *
 */
public class Franja {
	public Posicion pinicio, pfin;
	public int inicio, fin;
	
	protected Boolean activa;
	/**
	 * Constructor de la clase. Dadas las Posiciones pinicio y pfin, actualiza automáticamente
	 * los límites en píxeles (inicio y fin) en función del tamaño del gráfico.
	 * Por defecto, al crearla, la franja no está activa.
	 * @param pinicio
	 * @param pfin
	 */
	//TODO ¿Debería comprobar si fin <= inicio?
	public Franja (Posicion pinicio, Posicion pfin) {
		this.pinicio = pinicio;
		this.pfin = pfin;
		activa = false;
		//actualizarPixeles();
	}
	/**
	 * Devuelve el string que representa al entero, con un cero delante si es menor que 10.
	 * @param i	Número a representar
	 * @return	El String del número con formato xx (ej. "15", "02", "49")
	 */
	public String aString (int i) {
		String s = String.valueOf(i);
		if (i<10) s = '0' + s;
		return s;
	}
	/**
	 * Dibuja una franja, dada la altura de la misma y su color. Además, si está activa,
	 * mostrará información adicional del intervalo representado. Se basa en la posición
	 * en píxeles para dibujar la franja, así que debe estar actualizada.
	 * 
	 * @param	gc		El contenedor gráfico donde dibujar la franja
	 * @param	despV	La altura en píxeles de la franja (0 es arriba)
	 * @param	color	El color de la franja. Normalmente dependerá del empleado.
	 */
	// TODO ...así que debe estar actualizada. Sería mejor que la propia clase controlara eso en los setters

	/**
	 * Activa esta franja.
	 */
	public void activarFranja()    {activa = true; }
	/**
	 * Desactiva esta franja.
	 */
	public void desactivarFranja() {activa = false;}
	/**
	 * Comprueba si el píxel dado está contenido en la franja, es decir, en el intervalo
	 * [inicio,fin] (nótese el intervalo cerrado).
	 * @param x	Píxel a comprobar
	 * @see #contienePixelInt(int)
	 * @see	#tocaLadoDerecho(int)
	 * @see #tocaLadoIzquierdo(int)
	 * @return Si el píxel está contenido en la franja.
	 */
}
