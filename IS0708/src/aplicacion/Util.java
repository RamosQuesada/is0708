package aplicacion;

import java.util.Random;
import java.sql.Time;

/**
 * El caj�n desastre de las funciones que no tienen cabida en ninguna clase,
 * pero que interesa que est�n juntas para usarse en distintos �mbitos.
 * @author Daniel Dionne
 * 
 */
public class Util {
	
	/**
	 * Comprueba si una direcci�n de email dada en un String es correcta
	 * @param email	la cadena a comprobar
	 * @return		<i>true</i> si la cadena es un email, <i>false</i> en caso contrario
	 * @author Daniel Dionne
	 */
	public static boolean comprobarEmail(String email) {
		boolean valido = true;
		// Comprobar que todos los caracteres son v�lidos
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
	    // La @ no debe ser el �ltimo caracter
	    else if (email.indexOf("@") == email.length()-1) valido = false;
		// No se permiten dos puntos seguidos 
	    else if (email.indexOf("..") >=0) valido = false;
		// Y no puede haber un punto al final
	    else if (email.lastIndexOf(".") == email.length()-1) valido = false;
		return valido;
	}

	/**
	 * Convierte un string a n�mero de vendedor, comprobando que es v�lido. Devuelve -1 si no lo es.
	 * @param nvend el string a convertir
	 * @return el n�mero de vendedor, o -1 si el string es incorrecto
	 */
	public static int convertirNVend(String nvend) {
		int n = -1;
		if (nvend.length()==8) {
			try {
				n = Integer.valueOf(nvend);
			}
			catch (NumberFormatException e) {
				System.out.println("I08.1_Empleado nuevo: N�mero de vendedor incorrecto.");
			}
		}
		return n;
	}
	
	/**
	 * Convierte un entero a un String de dos cifras, con un cero delante si es necesario.
	 * @param i el n�mero a convertir
	 * @return un String con el entero en formato xx.
	 */
	static public String aString (int i) {
		String s = String.valueOf(i);
		if (i<10) s = '0' + s;
		return s;
	}

	public static String obtenerClave() {
		// TODO Auto-generated method stub
		Random randomizador=new Random();
		String clave = "";
		char[] chars = new char[]{
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
				'p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
				'P','Q','R','S','T','U','V','W','X','Y','Z',
				'1','2','3','4','5','6','7','8','9','0'
			};
		
		char[] clavechar= new char[8];
		for (int cont=0;cont<8;cont++){

			int r = randomizador.nextInt(chars.length);
			clavechar[cont]=chars[r];
		}
		System.out.println(clavechar);
		clave=String.copyValueOf(clavechar);
		return clave;
	}		
	
	/**
	 * Devuelve el numero de dias
	 * @param mes
	 * @param anio
	 * @return entero que representa el numero de dias del mes
	 */
	public static int dameDias(int mes, int anio){ //esta funcion devuelve el numero de dias del mes
		
		// Sacado de Wikipedia: http://es.wikipedia.org/wiki/Algoritmo_bisiesto
		boolean bisiesto = false;
		if ((anio % 400 == 0) || (anio % 4 == 0 && anio % 100 != 0)){
			bisiesto = true;
		}
		
		if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12){
			return 31;
		}
		else if (mes == 4 || mes == 6 || mes == 9 || mes == 10){
			return 30;
		}
		else if (bisiesto) return 29;
		else return 28;
		    
	}
	
	/**
	 * Calcula la hora del fin del descanso
	 * @param horaInicio 
	 * @param minutos duracion del descanso 
	 * @return Time que representa la hora del fin del descanso
	 */
	public static Time calculaFinDescanso(Time horaInicio, int minutos){
		Time fin = new Time(horaInicio.getTime() + minutos*60000);
		return fin;
	}
	
}
