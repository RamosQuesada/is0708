package aplicacion.utilidades;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.ResourceBundle;
import java.lang.String;
import java.sql.*;
import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

/**
 * El caj�n desastre de las funciones que no tienen cabida en ninguna clase,
 * pero que interesa que est�n juntas para usarse en distintos �mbitos. 
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
	 * Convierte un string a número de vendedor, comprobando que es válido. Devuelve -1
	 * si no lo es.
	 * @param	nvend el string a convertir
	 * @return	el número de vendedor, o -1 si el string es incorrecto
	 * @author	Daniel Dionne
	 */
	public static int convertirNVend(String nvend) {
		int n = -1;
		if (nvend.length()==8) {
			try {
				n = Integer.valueOf(nvend);
			}
			catch (NumberFormatException e) {
				System.err.println("aplicacion.Util: Número de vendedor incorrecto.");
			}
		}
		return n;
	}
	
	/**
	 * Convierte un entero a un String de dos cifras, con un cero delante si es necesario.
	 * @param i el n�mero a convertir
	 * @return un String con el entero en formato xx.
	 * @author David Rodilla
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
		//System.out.println(clavechar);
		clave=String.copyValueOf(clavechar);
		return clave;
	}		
	
	/**
	 * Devuelve el numero de dias
	 * @param mes
	 * @param anio
	 * @return entero que representa el numero de dias del mes
	 * @author Miguel Angel Diaz
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
	 * @author Miguel Angel Diaz
	 */
	public static Time calculaFinDescanso(Time horaInicio, int minutos){
		Time fin = new Time(horaInicio.getTime() + minutos*60000);
		return fin;		
	}
	
	/**
	 * Devuelve el numero de expertos a partir del patron.
	 * Si el patron es incorrecto devuelve "-1"
	 * @param patron String del tipo "1e5p"
	 * @return Numero de expertos del patron
	 * @author Miguel Angel Diaz
	 */	
	public static int numExpertos(String patron){
		int expertos = -1;
		int posE = patron.indexOf('e');
		int posP = patron.indexOf('p');
		if (posE<posP && posE<patron.length() && posE>0){
			// Comprobar que solo esta compuesto por digitos
			int i = 0;
			while (i<posE && patron.charAt(i)>=48 && patron.charAt(i)<=57){
				i++;
			}
			if (i == posE) expertos = Integer.valueOf(patron.substring(0,posE));
		}
		return expertos;
	}
	
	/**
	 * Devuelve el numero de principiantes a partir del patron.
	 * Si el patron es incorrecto devuelve "-1"
	 * @param patron String del tipo "1e5p"
	 * @return Numero de principiantes del patron
	 * @author Miguel Angel Diaz
	 */	
	public static int numPrincipiantes(String patron){
		int principiantes = -1;
		int posE = patron.indexOf('e');
		int posP = patron.indexOf('p');
		if (posE<posP && posP==patron.length()-1 && posE>0){
			// Comprobar que solo est� compuesto por digitos
			int i = posE+1;
			while (i<posP && patron.charAt(i)>=48 && patron.charAt(i)<=57){
				i++;
			}
			if (i == posP) principiantes = Integer.valueOf(patron.substring(posE+1,posP));
		}
		return principiantes;
	}
	
	/**
	 * Devuelve un patron a partir de expertos y principiantes
	 * @param expertos
	 * @param principiantes
	 * @return patron
	 * @author Miguel Angel Diaz
	 */
	public static String patron(int expertos, int principiantes){
		return (expertos+"e"+principiantes+"p");
	}
	
	/**
	 * Convierte un fecha de tipo Date en un String con el formato YYYY-MM-DD
	 * (que usa la BD).
	 * Si la fecha es null, devuelve 0000-00-00.
	 * @param fecha la fecha a convertir
	 * @return el String con el formato apropiado
	 * @author Jakub
	 */
	public static String dateAString(Date fecha) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(fecha);
	}

	/**
	 * Convierte un fecha de tipo Date en un String con el formato DD-MM-YYYY.
	 * Si la fecha es null, devuelve 00-00-0000.
	 * @param fecha la fecha a convertir
	 * @return el String con el formato apropiado
	 * @author Jakub
	 */
	public static String dateAString2(Date fecha) {
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(fecha);
	}
	
	/**
	 * Convierte una fecha dada en enteros separados en un String con el
	 * formato YYYY-MM-DD (que usa la BD).
	 * @param dia el día
	 * @param mes el mes
	 * @param anio el año
	 * @return el string con el formato apropiado
	 * @author Daniel Dionne
	 */
	public static String fechaAString(int dia, int mes, int anio) {
		String an = String.valueOf(anio);
		String me = String.valueOf(mes);
		String di = String.valueOf(dia);
		
		if (an.length()==1)
			an = "0" + an;
		if (me.length()==1)
			me = "0" + me;
		if (di.length()==1)
			di = "0" + di;
		return an + "-" + me + "-" + di;

	}
	/**
	 * Coje la hora y los minutos de un Date y lo pasa a String con el formato hh:mm
	 * Si la fecha es null, devuelve 00:00.
	 * @param fecha la fecha a sacar las horas
	 * @return el String con el formato apropiado
	 * @author Alberto Maqueda
	 */
	public static String dateAStringHora(java.util.Date fecha) {
		
		String time;
		
		Time t;
		t = new Time(fecha.getTime());
		
		
		int hour = t.getHours();
		int min = t.getMinutes();

		
		if(fecha==null)
			return null;
		
		String Shora = Integer.toString(hour);
		String Sminutos = Integer.toString(min);
		if (hour-10<0 && min<10)
			return "0"+Shora+":"+"0"+Sminutos;
		
		if (hour-10<0 && !(min<10))
			return "0"+Shora+":"+Sminutos;
		
		if (!(hour-10<0) && min<10)
			return Shora+":"+"0"+Sminutos;
		
		return Shora+":"+Sminutos;
		
	}

	/**
	 * Convierte un fecha de tipo long en un String con el formato YYYY-MM-DD.
	 * Si la fecha es null, devuelve 0000-00-00.
	 * @param fecha la fecha a convertir
	 * @return el String con el formato apropiado
	 * @author Jakub
	 */

	public static String longAString(long fecha) {
		Date date = new Date(fecha);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	/**
	 * Convierte en un formato para crear fechas
	 * @param anio 
	 * @param mes 
	 * @param mes
	 * @return
	 */
	public static String aFormatoDate(String anio,String mes,String dia){
		
		String year;
		//System.out.println(Integer.parseInt(año));
		
		if((Integer.parseInt(anio))<1000){
			year="20"+anio.substring(1);
		}
		else{year=anio;}
		
		String month;
		String day;
		if (mes.length()==1){month=("0"+mes);}
		else{month =mes;}
		if (dia.length()==1){day=("0"+dia);}
		else{day = dia;}
		
		return((year+"-"+month+"-"+day));
		
	}
	/**
	 * Convierte un String con formato YYYY-MM-DD en una fecha de tipo Date.
	 * @param s el String a convertir
	 * @return la fecha
	 * @author Jakub
	 */
	
	public static Date stringADate(String dateString) throws Exception{
		Date date; 		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");	
		date = (Date)formatter.parse(dateString);		
		return date;
    }
		
	
	/**
	 * 
	 * @param t  Time del que sacamos el numero de horas
	 * @return numero de horas de t
	 * @author Agustin-Daniel Delgado Muñoz
	 */
	public static int dameHoras(Time t){
		String s = t.toString();
		//s tiene el formato HH:MM:SS 
		int horas =Integer.valueOf(s.substring(0,2));
		return horas;
}	
	
	/**
	 * 
	 * @param t  Time del que sacamos el numero de minutos
	 * @return numero de minutos de t
	 * @author Agustin-Daniel Delgado Muñoz
	 */
	public static int dameMinutos(Time t){
		String s = t.toString();
		//s tiene el formato HH:MM:SS 
		int horas =Integer.valueOf(s.substring(0,2));
		int minutos =Integer.valueOf(s.substring(3,5));
		return (60*horas + minutos);
}

	/**
	 * 
	 * @param t  Time del que sacamos el numero de segundos
	 * @return numero de segundos de t
	 * @author Agustin-Daniel Delgado Muñoz
	 */
	public static int dameSegundos(Time t){
		String s = t.toString();
		//s tiene el formato HH:MM:SS 
		int horas =Integer.valueOf(s.substring(0,2));
		int minutos =Integer.valueOf(s.substring(3,5));
		int segundos = Integer.valueOf(s.substring(6,8));
		return (3600*horas + 60*minutos + segundos);
	}
	
	/**
	 * Recorta el final de una cadena de texto, añadiendo al final "..." si se ha 
	 * recortado.
	 * @param texto el texto a recortar
	 * @param x el límite de tamaño de la cadena, contando los puntos
	 * @return la cadena recortada
	 */
	public static String recortarTituloTexto(String texto, int x) {
		String s = texto;
		if (texto.length()>x) {
			s = texto.substring(0, x-3) + "...";
		}		
		return s;
	}

	/**
	 * Recorta el final de una cadena te texto, añadiendo al final "..." si se ha 
	 * recortado y sustituye los saltos de linea por espacios.
	 * @param texto el texto a recortar
	 * @param x el límite de tamaño de la cadena, contando los puntos
	 * @return la cadena recortada
	 */
	public static String recortarContenidoTexto(String texto, int x) {
		String salida;
		String aux;
		String s = recortarTituloTexto(texto,x);
				
		salida = new String();
		
		/*for (int i=0; i<s.length(); i++)
		{
			aux = s.substring(i, i+1);
			if (s.charAt(i) == '\n') 
			{
				aux = " ";
			}
			salida = salida.concat(aux);
		}*/
		
		for (int i=0; i<s.length(); i++)
		{
			aux = s.substring(i, i+1);
			//System.out.println(s.charAt(i));
 			if (s.charAt(i) == '\n' || s.charAt(i) == '\r') 
			{
				aux = " ";
			}
			salida = salida.concat(aux);
		}
		
		return salida;
	}
	
	/**
	 * Devuelve el nombre del mes i en el idioma del bundle.
	 * @param bundle paquete de idioma
	 * @param i entero del 1 al 12
	 * @return el nombre del mes, o null si el parámetro i no es correcto
	 */
	public static String mesAString(ResourceBundle bundle, int i) {
		String[] meses = { bundle.getString("enero"),
				bundle.getString("febrero"), bundle.getString("marzo"),
				bundle.getString("abril"), bundle.getString("mayo"),
				bundle.getString("junio"), bundle.getString("julio"),
				bundle.getString("agosto"),
				bundle.getString("septiembre"),
				bundle.getString("octubre"),
				bundle.getString("noviembre"),
				bundle.getString("diciembre") };
		if (i<1 || i>12) return null;
		return meses[i];
	}
	
	
	/**
	 * Añade los ceros necesarios por la izquierda a un numero de vendedor
	 * @param nv : numero de vendedor 
	 * @return numero de vendedor de 8 digitos 
	 */
	public static String completaNumVendedor(String nv) {
		while (nv.length()<8) {
			nv = "0"+nv;
		}		
		return nv;
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
	public static void cambiarRelleno(Display display, GC gc, int r, int g, int b) {
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
	public static void cambiarPincel (Display display, GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(display,r, g, b));
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
	public static void darBrillo (Display display, GC gc, int r, int g, int b,int brillo) {
		// Controlar límites de colores
		if (brillo>0) {
			if (r>255-brillo) r=255; else r+=brillo;
			if (g>255-brillo) g=255; else g+=brillo;
			if (b>255-brillo) b=255; else b+=brillo;
		} else {
			if (r<-brillo) r=0; else r+=brillo;
			if (g<-brillo) g=0; else g+=brillo;
			if (b<-brillo) b=0; else b+=brillo;
		}
		gc.setBackground(new Color(display,r, g, b));
	}
	
	/**
	 * Devuelve un color diferenciado del dado. Calcula automáticamente si debe hacer la diferencia positiva o negativa.
	 * @param color color de referencia
	 * @param cantidad valor de la diferencia
	 * @return un nuevo color que contraste con el dado
	 */
	public static Color getColorDiferenciado(Color color, int cantidad) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int c = Math.abs(cantidad);
		if ((r+g+b)/3<128) {
			if (r>255-c) r=255; else r+=c;
			if (g>255-c) g=255; else g+=c;
			if (b>255-c) b=255; else b+=c;
		} else {
			if (r<c) r=0; else r-=c;
			if (g<c) g=0; else g-=c;
			if (b<c) b=0; else b-=c;
		}
		return new Color(color.getDevice(), r, g, b);
	}
	
	/**check if the String text is integer*/
	public static boolean naturalCheck(String string){
	    try {
	    	int n = Integer.parseInt( string );
	    	if (n>=0) return true;
	    	else return false;
	    } catch (Exception e) {
	    	System.out.println("Non-integer value");
	        return false;
	    }
	}
	
	/**check if the String text is double*/
	public static boolean doubleCheck(String string){
	    try {
	    	double n = Double.parseDouble(string);
	    	return true;
	    } catch (Exception e) {
	    	System.out.println("Non-double value");
	        return false;
	    }
	}
	public static String horaAString(int hora){
		 
		if(hora<0 || hora>23){
			return null;
		}
		String Shora = Integer.toString(hora);
		if (hora-10<0){
			return "0"+Shora+":00";
		}
		return Shora+":00";
	}
	public static String horaminutosAString(int hora, int minutos){
	 
		if(hora<0 || hora>23 || minutos<0 || minutos>59){
			return null;
		}
		String Shora = Integer.toString(hora);
		String Sminutos = Integer.toString(minutos);
		if (hora-10<0){
			return "0"+Shora+":"+Sminutos+":00";
		}
		return Shora+":"+Sminutos+":00";
	}
	
	public static String intADiaSemana(int diasemana){
		
		if(diasemana==1) return "Lunes";
		if(diasemana==2) return "Martes";
		if(diasemana==3) return "Miercoles";
		if(diasemana==4) return "Jueves";
		if(diasemana==5) return "Viernes";
		if(diasemana==6) return "Sabado";
		if(diasemana==7) return "Domingo";

		return null;
		
	}
	
	/**
	 * Convierte un String en formato Hexadecimal a un objeto de
	 * tipo Color
	 * @param color
	 * @return Objeto Color
	 */
	public static Color stringAColor(String color) {
		Color c = null;
		try {
			int r,g,b;
			
			r = Integer.parseInt(color.substring(0, 2), 16);
			g = Integer.parseInt(color.substring(2, 4), 16);
			b = Integer.parseInt(color.substring(4, 6), 16);
			
			c = new Color(null, r,g,b);
		} catch(Exception e) {
			System.err.println("Util :: Formato de String incorrecto");
		}
		return c;
	}
	
	/**
	 * Calcula el hexadecimal de un objeto Color
	 * @param color Color del que se quiere saber su hexadecimal
	 * @return Un string con el color en hexadecimal
	 */
	public static String ColorAHex(Color color){
		if (color == null) return "000000";
		String r = Integer.toHexString(color.getRed()).toUpperCase();
		if (r.length()==1) r = 0 + r;
		String g = Integer.toHexString(color.getGreen()).toUpperCase();
		if (g.length()==1) g = 0 + g;
		String b = Integer.toHexString(color.getBlue()).toUpperCase();
		if (b.length()==1) b = 0 + b;
		return r+g+b;
	}
	
	/**
	 * Calcula la fecha siguiente a la fecha dada
	 * @param fecha
	 * @return la fecha siguiente
	 * @author Alberto
	 */
	public static java.sql.Date diaSiguiente(java.sql.Date fecha){
		int mes=fecha.getMonth()+1;
		int ano=fecha.getYear();
		int dia=fecha.getDate();
		
		int diaNuevo=dia;
		int mesNuevo=mes;
		int anoNuevo=ano;
		
		if ((mes==1)||(mes==3)||(mes==5)||(mes==7)||(mes==8)||(mes==10)||(mes==12)){
			if (dia==31){
				if(mes==12){
					diaNuevo=1;
					mesNuevo=1;
					anoNuevo=anoNuevo+1;
				}
				else{
					diaNuevo=1;
					mesNuevo=mesNuevo+1;
				}
				
			}
			else{
				diaNuevo=diaNuevo+1;
			}
		}
		
		if ((mes==4)||(mes==6)||(mes==9)||(mes==11)){
			if (dia==30){
				diaNuevo=1;
				mesNuevo=mesNuevo+1;
			}
			else{
				diaNuevo=diaNuevo+1;				
			}
		}
		
		if (mes==2){
			if (ano%4==0){
				if (dia==29){
					diaNuevo=1;
					mesNuevo=mesNuevo+1;
				}
				else{
					diaNuevo=diaNuevo+1;					
				}
			}
		
			else{
				if (dia==28){
					diaNuevo=1;
					mesNuevo=mesNuevo+1;
				}
				else{
					diaNuevo=diaNuevo+1;
					
				}
			}
		}
		
		java.sql.Date fechaNueva=new java.sql.Date(anoNuevo,mesNuevo-1,diaNuevo);
		return fechaNueva;
	}
	
	/**
	 * devuelve la semana a la que pertenece un dia
	 * @param fecha la fecha de la que queremos saber la semana
	 * @return un arraylist de 7 posiciones con fechas, de lunes a domingo
	 * @author Alberto
	 */
	public static ArrayList<java.sql.Date> getSemanaFecha(java.sql.Date fecha){
		ArrayList<java.sql.Date> resultado=new ArrayList<java.sql.Date>();
		java.sql.Date lunes=null,martes=null,miercoles=null,jueves=null,viernes=null,sabado=null,domingo=null;
		
		switch (fecha.getDay()){
		case 0:{domingo=fecha;
				sabado=Util.diaAnterior(domingo);
				viernes=Util.diaAnterior(sabado);
				jueves=Util.diaAnterior(viernes);
				miercoles=Util.diaAnterior(jueves);
				martes=Util.diaAnterior(miercoles);
				lunes=Util.diaAnterior(martes);
				break;}
		case 1:{lunes=fecha;
				martes=Util.diaSiguiente(lunes);
				miercoles=Util.diaSiguiente(martes);
				jueves=Util.diaSiguiente(miercoles);
				viernes=Util.diaSiguiente(jueves);
				sabado=Util.diaSiguiente(viernes);
				domingo=Util.diaSiguiente(sabado);
				break;}
		case 2:{martes=fecha;
				lunes=Util.diaAnterior(martes);
				miercoles=Util.diaSiguiente(martes);
				jueves=Util.diaSiguiente(miercoles);
				viernes=Util.diaSiguiente(jueves);
				sabado=Util.diaSiguiente(viernes);
				domingo=Util.diaSiguiente(sabado);
				break;}
		case 3:{miercoles=fecha;
				martes=Util.diaAnterior(miercoles);
				lunes=Util.diaAnterior(martes);
				jueves=Util.diaSiguiente(miercoles);
				viernes=Util.diaSiguiente(jueves);
				sabado=Util.diaSiguiente(viernes);
				domingo=Util.diaSiguiente(sabado);
				break;}
		case 4:{jueves=fecha;
				miercoles=Util.diaAnterior(jueves);
				martes=Util.diaAnterior(miercoles);
				lunes=Util.diaAnterior(martes);
				viernes=Util.diaSiguiente(jueves);
				sabado=Util.diaSiguiente(viernes);
				domingo=Util.diaSiguiente(sabado);
				break;}
		case 5:{viernes=fecha;
				jueves=Util.diaAnterior(viernes);
				miercoles=Util.diaAnterior(jueves);
				martes=Util.diaAnterior(miercoles);
				lunes=Util.diaAnterior(martes);
				sabado=Util.diaSiguiente(viernes);
				domingo=Util.diaSiguiente(sabado);
				break;}
		case 6:{sabado=fecha;
				viernes=Util.diaAnterior(sabado);
				jueves=Util.diaAnterior(viernes);
				miercoles=Util.diaAnterior(jueves);
				martes=Util.diaAnterior(miercoles);
				lunes=Util.diaAnterior(martes);
				domingo=Util.diaSiguiente(sabado);
				break;}
		default:break;
		}				
		resultado.add(lunes);
		resultado.add(martes);
		resultado.add(miercoles);
		resultado.add(jueves);
		resultado.add(viernes);
		resultado.add(sabado);
		resultado.add(domingo);
			
		return resultado;
	}
	
	
	
	
	public static java.sql.Date diaAnterior(java.sql.Date fecha){
		int mes=fecha.getMonth()+1;
		int ano=fecha.getYear();
		int dia=fecha.getDate();
		
		int diaNuevo=dia;
		int mesNuevo=mes;
		int anoNuevo=ano;
		
		if ((mes==1)||(mes==2)||(mes==4)||(mes==6)||(mes==8)||(mes==9)||(mes==11)){
			if (dia==1){
				if(mes==1){
					diaNuevo=31;
					mesNuevo=12;
					anoNuevo=anoNuevo-1;
				}
				else{
					diaNuevo=31;
					mesNuevo=mesNuevo-1;
				}
				
			}
			else{
				diaNuevo=diaNuevo-1;
			}
		}
		
		if ((mes==5)||(mes==7)||(mes==10)||(mes==12)){
			if (dia==1){
				diaNuevo=30;
				mesNuevo=mesNuevo-1;
			}
			else{
				diaNuevo=diaNuevo-1;				
			}
		}
		
		if (mes==3){
			if (ano%4==0){
				if (dia==1){
					diaNuevo=29;
					mesNuevo=2;
				}
				else{
					diaNuevo=diaNuevo-1;					
				}
			}
		
			else{
				if (dia==1){
					diaNuevo=28;
					mesNuevo=2;
				}
				else{
					diaNuevo=diaNuevo-1;
					
				}
			}
		}
		
		java.sql.Date fechaNueva=new java.sql.Date(anoNuevo,mesNuevo-1,diaNuevo);
		return fechaNueva;
	}
	
	/**
	 * Devuelve la ruta del directorio raiz de la aplicacion
	 * @return ruta de la carpeta
	 */
	public static String rutaTrabajo(){
		String ruta = System.getProperty("java.class.path");
		String separadorRutas = System.getProperty("path.separator");
		// Distinguir entre ejecucion desde eclipse y ejecucion desde el jar
		if (ruta.contains(separadorRutas))
			ruta = ruta.substring(0,ruta.indexOf(separadorRutas)-4);
		else
			ruta = ruta.substring(0, ruta.length()-16);
		return ruta;
	}
	
}