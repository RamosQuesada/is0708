package aplicacion;

/**
 * El cajón desastre de las funciones que no tienen cabida en ninguna clase, pero que interesa
 * que estén fuera para usarse en distintos ámbitos.
 * @author Daniel
 *
 */
public class General {
	
	/**
	 * Comprueba si una dirección de email dada en un String es correcta
	 * @param email	la cadena a comprobar
	 * @return		<i>true</i> si la cadena es un email, <i>false</i> en caso contrario
	 */
	public static boolean comprobarEmail(String email) {
		boolean valido = true;
		// Comprobar que todos los caracteres son válidos
		String caracteresValidos = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@.-_";
		int i=0;
		while  (i < email.length() && valido) {
			char letter = email.charAt(i);
			if (caracteresValidos.indexOf(letter) == -1) valido = false;
			i++;
		}
		// Debe contener @, y no debe ser el primer caracter
		if (email.indexOf("@") < 1) valido = false;
	    // Debe haber un punto tras la @
	    else if (email.lastIndexOf(".") <= email.indexOf("@")) valido = false;
	    // La @ no debe ser el último caracter
	    else if (email.indexOf("@") == email.length()-1) valido = false;
		// No se permiten dos puntos seguidos 
	    else if (email.indexOf("..") >=0) valido = false;
		// Y no puede haber un punto al final
	    else if (email.lastIndexOf(".") == email.length()-1) valido = false;
		return valido;
	}

	/**
	 * Convierte un string a número de vendedor, comprobando que es válido. Devuelve -1 si no lo es.
	 * @param nvend el string a convertir
	 * @return el número de vendedor, o -1 si el string es incorrecto
	 */
	public static int convertirNVend(String nvend) {
		int n = -1;
		if (nvend.length()==8) {
			try {
				n = Integer.valueOf(nvend);
			}
			catch (NumberFormatException e) {
				System.out.println("I08.1_Empleado nuevo: Número de vendedor incorrecto.");
			}
		}
		return n;
	}
	
	/**
	 * Convierte un entero a un String de dos cifras, con un cero delante si es necesario.
	 * @param i el número a convertir
	 * @return un String con el entero en formato xx.
	 */
	static public String aString (int i) {
		String s = String.valueOf(i);
		if (i<10) s = '0' + s;
		return s;
	}

}
