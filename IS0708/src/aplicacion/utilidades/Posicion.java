package aplicacion.utilidades;

/**
 * Representa una hora del cuadrante mediante dos valores:
 * <p>
 * <ul>
 * <li>hora: La hora, que admite valores mayores que 24, que representan la madrugada del d�a siguiente (i.e. 26 = 2 a.m.)
 * <li>cmin: Intervalo de 5 minutos, es el intervalo m�s peque�o representable. Una hora tiene 12 cmins, [0-11].
 * </ul>
 * @author Daniel Dionne
 *
 */
public class Posicion {
	private int hora, cmin;
	/**
	 * El constructor de la clase. Si cmin es mayor que 11, se deja en 11.
	 * @param hora	La hora de la posici�n
	 * @param cmin	Los cminutos (intervalos de 5 minutos)
	 */
	public Posicion (int hora, int cmin) {
		this.hora = hora;
		this.cmin = cmin;
		if (this.cmin > 11) this.cmin = 11;
	}
	/**
	 * Devuelve la hora.
	 * @return Un valor positivo
	 * @see #dameCMin()
	 * @see #ponHora(int)
	 * @see #ponCMin(int)
	 */
	public int dameHora() {
		return hora;
	}
	/**
	 * Devuelve el n�mero de intervalos de 5 minutos.
	 * Por ejemplo, las 12:15 devuelve 3.
	 * @return Un valor de 0 a 11
	 * @see #dameHora()
	 * @see #ponHora(int)
	 * @see #ponCMin(int)
	 */
	public int dameCMin() {
		return cmin;
	}
	/**
	 * Asigna la hora que se pasa por par�metro.
	 * @param h Un entero que representa la hora. Pasadas las 24 se consideran horas del d�a 
	 * siguiente. No se admiten horas negativas.
	 * @return Devuelve <i>false</i> si el par�metro es negativo, <i>true</i> en cualquier otro caso.
	 * @see #dameHora()
	 * @see #dameCMin()
	 * @see #ponCMin(int)
	 */
	public boolean ponHora(int h) {
		boolean b = true;
		if (h<0) b = false;
		else hora = h;
		return b;
	}
	/**
	 * Asigna los intervalos de 5 minutos que se pasan por par�metro.
	 * @param m Un entero que representa el n�mero de intervalos de 5 minutos. Debe estar en el rango [0,11].
	 * @return Devuelve <i>false</i> si el par�metro se sale del rango [0,11]. <i>True</i> en cualquier otro caso.
	 * @see #dameHora()
	 * @see #dameCMin()
	 * @see #ponHora(int)
	 */
	public boolean ponCMin(int m) {
		boolean b = true;
		if (m<0 || m>11) b = false;
		else cmin = m;
		return b;
	}
	/**
	 * Asigna a esta instancia la suma de una posici�n menos un ancho, definidos por dos posiciones
	 * @param p		La posici�n inicial
	 * @param ancho	La cantidad de tiempo a sumar a esa posici�n
	 */
	public void suma (Posicion p, Posicion ancho) {
		int x = p.hora*12 + ancho.hora*12 + p.cmin + ancho.cmin;
		this.hora = x/12;
		this.cmin = x%12;

	}
	/**
	 * Asigna a esta instancia la resta de una posici�n menos un ancho, definidos por dos posiciones
	 * @param p		La posici�n inicial
	 * @param ancho	La cantidad de tiempo a restar a esa posici�n
	 */
	public void resta (Posicion p, Posicion ancho) {
		int x = p.hora*12 - ancho.hora*12 + p.cmin - ancho.cmin;
		this.hora        = x/12;
		this.cmin = x%12;
	}
	/**
	 * Calcula la diferencia de tiempo (en valor absoluto) entre esta y una posici�n dada. 
	 * @param p		La posici�n a la que calcular la distancia
	 * @return		La distancia en valor absoluto
	 */
	public Posicion diferencia (Posicion p) {
		int x = Math.abs(this.hora*12 - p.hora*12 + this.cmin - p.cmin);
		return new Posicion(x/12, x%12);
	}
	/**
	 * Compara esta posici�n con la Posici�n p
	 * @param p		Posici�n que comparar
	 * @return		this <= p
	 */
	public Boolean menorOIgualQue (Posicion p) {
		Boolean b = false;
		if (this.hora <= p.hora) b = true;
		else if (this.hora == p.hora && this.cmin <= p.cmin) b = true;
		return b;
	}
	/**
	 * Compara esta posici�n con la Posici�n p
	 * @param p		Posici�n que comparar
	 * @return		this > p
	 */
	public Boolean mayorQue (Posicion p) {
		Boolean b = false;
		if (this.hora > p.hora) b = true;
		else if (this.hora == p.hora && this.cmin > p.cmin) b = true;
		return b;
	}
	/**
	 * Compara esta posici�n con la Posici�n p
	 * @param p		Posici�n que comparar
	 * @return		this >= p
	 */
	public Boolean mayorOIgualQue (Posicion p) {
		Boolean b = false;
		if (this.hora >= p.hora) b = true;
		else if (this.hora == p.hora && this.cmin >= p.cmin) b = true;
		return b;
	}		
}
