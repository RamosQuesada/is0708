package interfaces;

import java.util.ArrayList;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

//De dónde coger javadoc: http://javashoplm.sun.com/ECom/docs/Welcome.jsp?StoreId=22&PartDetailId=jdk-6u3-oth-JPR&SiteId=JSC&TransactionId=noreg
/*TODO	Si hago la ventana muy muy pequeña, el cuadrante se da la vuelta
 *		
 */		

/**
 * Dibuja un cuadrante sobre un GC.
 * 
 * @author	Daniel Dionne
 * @version	0.1
 * 
 */
public class Cuadrante {
	private final int anchoLados = 5; // El ancho de los lados de una franja, de donde se coge para estirarla y encogerla 
	private Display display;
	private int ancho;
	private int alto;
	private int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	private int alto_franjas;
	private int sep_vert_franjas;
	private int horaInicio, horaFin; // Definen de qué hora a qué hora es el cuadrante
	private int tamHora, tamSubdiv;
	public  int subdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	public ArrayList<Empleado> empleados; 

	/**
	 * Representa una hora del cuadrante mediante dos valores:
	 * <p>
	 * <ul>
	 * <li>hora: La hora, que admite valores mayores que 24, que representan la madrugada del día siguiente (i.e. 26 = 2 a.m.)
	 * <li>cmin: Intervalo de 5 minutos, es el intervalo más pequeño representable. Una hora tiene 12 cmins, [0-11].
	 * </ul>
	 * @author Daniel Dionne
	 *
	 */
	public class Posicion {
		int hora, cmin;
		/**
		 * El constructor de la clase. Si cmin es mayor que 11, se deja en 11.
		 * @param hora
		 * @param cmin
		 */
		public Posicion (int hora, int cmin) {
			this.hora = hora;
			this.cmin = cmin;
			if (this.cmin > 11) this.cmin = 11;
		}
		/**
		 * Asigna a esta instancia la suma de una posición menos un ancho, definidos por dos posiciones
		 * @param p		La posición inicial
		 * @param ancho	La cantidad de tiempo a sumar a esa posición
		 */
		public void suma (Posicion p, Posicion ancho) {
			int x = p.hora*12 + ancho.hora*12 + p.cmin + ancho.cmin;
			this.hora = x/12;
			this.cmin = x%12;

		}
		/**
		 * Asigna a esta instancia la resta de una posición menos un ancho, definidos por dos posiciones
		 * @param p		La posición inicial
		 * @param ancho	La cantidad de tiempo a restar a esa posición
		 */
		public void resta (Posicion p, Posicion ancho) {
			int x = p.hora*12 - ancho.hora*12 + p.cmin - ancho.cmin;
			this.hora        = x/12;
			this.cmin = x%12;
		}
		/**
		 * Calcula la diferencia de tiempo (en valor absoluto) entre esta y una posición dada. 
		 * @param p		La posición a la que calcular la distancia
		 * @return		La distancia en valor absoluto
		 */
		public Posicion diferencia (Posicion p) {
			int x = Math.abs(this.hora*12 - p.hora*12 + this.cmin - p.cmin);
			return new Posicion(x/12, x%12);
		}
		/**
		 * Compara esta posición con la Posición p
		 * @param p		Posición que comparar
		 * @return		this <= p
		 */
		public Boolean menorOIgualQue (Posicion p) {
			Boolean b = false;
			if (this.hora <= p.hora) b = true;
			else if (this.hora == p.hora && this.cmin <= p.cmin) b = true;
			return b;
		}
		/**
		 * Compara esta posición con la Posición p
		 * @param p		Posición que comparar
		 * @return		this > p
		 */
		public Boolean mayorQue (Posicion p) {
			Boolean b = false;
			if (this.hora > p.hora) b = true;
			else if (this.hora == p.hora && this.cmin > p.cmin) b = true;
			return b;
		}
		/**
		 * Compara esta posición con la Posición p
		 * @param p		Posición que comparar
		 * @return		this >= p
		 */
		public Boolean mayorOIgualQue (Posicion p) {
			Boolean b = false;
			if (this.hora >= p.hora) b = true;
			else if (this.hora == p.hora && this.cmin >= p.cmin) b = true;
			return b;
		}		
	}
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
		Posicion pinicio, pfin;
		int inicio, fin;
		private Boolean activa;
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
			actualizarPixeles();
		}
		/**
		 * Devuelve el string que representa al entero, con un cero delante si es menor que 10.
		 * @param i	Número a representar
		 * @return	El String del número con formato xx (ej. "15", "02", "49")
		 */
		private String aString (int i) {
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
		public void dibujarFranja (GC gc, int despV, Color color) {
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			// Todos los cambios de colores
			cambiarRelleno(gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio+2,despV+2,fin-inicio,15,10,10);
			cambiarRelleno(gc, r,g,b);
			cambiarPincel(gc, r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio,despV,fin-inicio,15,8,8);
			gc.drawRoundRectangle(inicio,despV,fin-inicio,15,8,8);
			// Si la franja está activa, mostrar una pestaña con información adicional
			// TODO Si la franja está muy a la derecha y es pequeña, la pestaña se sale
			if (activa) {
				int subDivs = 0;
				subDivs += (pfin.hora - pinicio.hora)*12;
				subDivs += (pfin.cmin - pinicio.cmin);
				// Modificar los colores teniendo siempre en cuenta los límites [0-255]
				cambiarRelleno(gc, r-50,g-50,b-50);
				gc.fillRoundRectangle(inicio+2,despV-13,135,20,10,10);
				cambiarRelleno(gc, r,g,b);	
				gc.fillRoundRectangle(inicio, despV-15, 135, 20,8,8);
				gc.drawRoundRectangle(inicio, despV-15, 135, 20,8,8);
				gc.fillRectangle(inicio+1,despV+1,Math.min(fin-inicio-1,136),12);
				String s1 = "";
				if (subDivs%12*60/12 != 0) s1=' '+String.valueOf(aString(subDivs%12*60/12))+'m';
				String s  = aString(pinicio.hora%24) + ":" + aString(pinicio.cmin*60/12) + " - " + aString(pfin.hora%24) + ":" + aString(pfin.cmin*60/12) + " (" + String.valueOf(subDivs/12)+'h'+s1+')';
				gc.drawText(s, inicio+5, despV-14, true);
			}
		}
		/**
		 * Comprueba si el píxel dado está contenido en la franja, es decir, en el intervalo
		 * [inicio,fin] (nótese el intervalo cerrado).
		 * @param x	Píxel a comprobar
		 * @see #contienePixelInt(int)
		 * @see	#tocaLadoDerecho(int)
		 * @see #tocaLadoIzquierdo(int)
		 * @return Si el píxel está contenido en la franja.
		 */
		public Boolean contienePixel(int x) {
			return x>=inicio && x<=fin;
		}
		/**
		 * Comprueba si el píxel dado está contenido en el interior de la franja, sin tener en
		 * cuenta los bordes, es decir, en el intervalo abierto (inicio+d,fin-d),
		 * donde 'd' es el ancho del borde de la franja, de donde se coge para estirarla y encogerla.
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
		 * Comprueba si el píxel dado está contenido en el lado izquierdo de la franja, es decir,
		 * en el intervalo cerrado [inicio-d,inicio+d], donde 'd' es el ancho del borde de la franja,
		 * de donde se coge para estirarla y encogerla.
		 * @param x Píxel a comprobar
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
		 * Comprueba si el píxel dado está contenido en el lado izquierdo de la franja, es decir,
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
		/**
		 * Evita que la barra se salga por los extremos. Si se sale, la coloca en el extremo.
		 */
		public void pegarALosBordes () {
			Posicion ancho = pfin.diferencia(pinicio);
			// Comprobar si me he salido por la izquierda
			if (pinicio.hora < horaInicio || pinicio.cmin<0) {
				pinicio.hora = horaInicio;
				pinicio.cmin = 0;
				pfin.suma(pinicio, ancho);
			}
			// Comprobar si me he salido por la derecha
			else if (pfin.hora*12+pfin.cmin >= horaFin*12) {
				Posicion fin = new Posicion(horaFin,0);
				pfin.diferencia(pinicio);
				pinicio.resta(fin, ancho);
				pfin.cmin = 0;
				pfin.hora = horaFin;
			}
		}
		/**
		 * Actualiza los límites en píxeles de la franja a partir de los valores de pinicio y pfin,
		 * teniendo en cuenta el tamaño del gráfico y los márgenes.
		 */
		public void actualizarPixeles () {
			// Si coincide con una subdivisión representada, pagar el inicio a la subdivisión para que no quede feo
			if (pinicio.cmin!=0 && pinicio.cmin%(12/subdivisiones)==0) {
				inicio = margenIzq + margenNombres + tamHora*(pinicio.hora-horaInicio) + (tamHora/subdivisiones)*(pinicio.cmin/(12/subdivisiones));
			}
			else
				inicio = margenIzq + margenNombres + tamHora*(pinicio.hora-horaInicio) + tamSubdiv*pinicio.cmin;
			if (pfin.cmin!=0 && pfin.cmin%(12/subdivisiones)==0) {
				fin = margenIzq + margenNombres + tamHora*(pfin.hora-horaInicio) + (tamHora/subdivisiones)*(pfin.cmin/(12/subdivisiones));
			}
			else
				fin    = margenIzq + margenNombres + tamHora*(pfin.hora   -horaInicio) + tamSubdiv*pfin.cmin;
		}
		/**
		 * Activa esta franja.
		 */
		public void activarFranja()    {activa = true; }
		/**
		 * Desactiva esta franja.
		 */
		public void desactivarFranja() {activa = false;}
	}
	/**
	 * Una clase provisional para representar a un empleado. A descartar.
	 * @author Daniel
	 *
	 */
	public class Empleado {
		String nombre;
		Color color;
		ArrayList<Franja> franjas;
		public Empleado (String n, Color c) {
			nombre = n;
			franjas = new ArrayList<Franja>();
			color = c;
		}
		public void franjaNueva (Posicion pinicio, Posicion pfin) {
			Franja f = new Franja(pinicio, pfin);
			franjas.add(f);
		}
		public void quitarFranja (Franja franja) {
			franjas.remove(franja);
		}
		private String aString (int i) {
			String s = String.valueOf(i);
			if (i<10) s = '0' + s;
			return s;
		}
		public void dibujarFranjas(GC gc, int posV, Color color) {
			int subDivs = 0;
			cambiarPincel(gc, 0, 0, 0);
			gc.drawText(nombre, margenIzq, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
			for (int i=0; i<franjas.size(); i++) {
				franjas.get(i).dibujarFranja(gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
				subDivs += (franjas.get(i).pfin.hora - franjas.get(i).pinicio.hora)*12;
				subDivs += (franjas.get(i).pfin.cmin - franjas.get(i).pinicio.cmin);
			}
			gc.drawText(String.valueOf(subDivs/12)+":"+String.valueOf(aString(subDivs%12*60/12)), margenNombres-10, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
		}
		public Boolean contienePunto (int y, int posV) {
			Boolean b = false;
			if (y > margenSup+(sep_vert_franjas+alto_franjas)*(posV+1) && y<=margenSup+(sep_vert_franjas+alto_franjas)*(posV+2)) b = true;
			return b;
		}
		public Color dameColor() {
			return color;
		}
	}

	/**
	 * Constructor del cuadrante
	 * @param d				Display sobre el que se dibujará el cuadrante
	 * @param subdivisiones	Número de subdivisiones que se muestran en el cuadrante.
	 * 						Los valores habituales de este parámetro son:
	 * 						<ul>
	 * 						<il> 12	(cada 5 min),
	 *  					<il> 6	(cada 10 min),
	 *   					<il> 4	(cada 15 min),
	 *    					<il> 2	(cada 30 min),
	 *    					<il> 1	(sin subdivisiones)
	 *     					</ul>  
	 * @param horaInicio	Hora de inicio del cuadrante
	 * @param horaFin		Hora de fin del cuadrante. Las horas pasadas de las 24 se muestran
	 * 						como la madrugada del día siguiente.
	 * @param margenIzq		Margen izquierdo en píxeles
	 * @param margenDer		Margen derecho en píxeles
	 * @param margenSup		Margen superior en píxeles
	 * @param margenInf		Margen inferior en píxeles
	 * @param margenNombres	Margen de los nombres en píxeles (indica dónde empieza a dibujarse
	 * 						el cuadrante a partir del margen izquierdo, dejando un espacio para
	 * 						los nombres.
	 */
	public Cuadrante(Display d, int subdivisiones, int horaInicio, int horaFin, int margenIzq, int margenDer, int margenSup, int margenInf, int margenNombres) {
		display = d;
		this.margenIzq  = margenIzq;
		this.margenDer  = margenDer;
		this.margenSup  = margenSup;
		this.margenInf  = margenInf;
		this.margenNombres  = margenNombres;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		alto_franjas = 15;
		sep_vert_franjas = 10;
		this.subdivisiones = subdivisiones;
		
		// TODO Borrar esto cuando se importen los empleados
		Empleado e1 = new Empleado("Juan López", new Color (display, 104, 228,  85));
		Empleado e2 = new Empleado("Marisol Pitis", new Color (display, 130, 130, 225));
		Empleado e3 = new Empleado("Bill Gates", new Color (display, 240, 190, 150));
		
		e1.franjaNueva(new Posicion( 9, 6), new Posicion(14, 0));
		e1.franjaNueva(new Posicion(16, 0), new Posicion(18, 0));
		e2.franjaNueva(new Posicion(15, 0), new Posicion(22, 0));
		e3.franjaNueva(new Posicion(12, 3), new Posicion(16, 0));
		e3.franjaNueva(new Posicion(18, 0), new Posicion(22, 1));
		
		empleados = new ArrayList<Empleado>();
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);

	}
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujará el cuadrante.
	 * @param empleadoActivo	La posición del empleado a resaltar en la lista de empleados.
	 */
	// TODO Debería lanzar una excepción si empleadoActivo > empleados.size
	public void dibujarCuadrante(GC gc, int empleadoActivo) {
		dibujarSeleccion(gc, empleadoActivo);
		dibujarHoras(gc);
		for (int i=0; i<empleados.size(); i++) {
			empleados.get(i).dibujarFranjas(gc, i, empleados.get(i).dameColor());
		}
	}
	/**
	 * Dibuja lineas verticales representando las horas y las subdivisiones del cuadrante.
	 * @param gc	El GC del display sobre el que se dibujará el cuadrante.
	 */
	private void dibujarHoras(GC gc) {
		cambiarPincel(gc, 40,80,40);
		int m = margenIzq + margenNombres;
		int h = horaFin - horaInicio;
		int sep = (ancho - m - margenDer)/h;
		int subsep = sep/subdivisiones;
		for (int i=0; i<=h; i++) {
			gc.setLineStyle(SWT.LINE_SOLID);
			cambiarPincel(gc, 40,80,40);
			if (sep>14 && sep<=20) gc.drawText(String.valueOf((horaInicio+i)%24),     m+i*sep-5, margenSup, true);
			else if (sep>20)     gc.drawText(String.valueOf((horaInicio+i)%24)+'h', m+i*sep-5, margenSup, true);
			gc.drawLine(m+i*sep, 20+margenSup, m+i*sep, alto-margenInf);
			cambiarPincel(gc, 120,170,120);
			gc.setLineStyle(SWT.LINE_DOT);
			if (i!=h)
				for (int j=1; j<subdivisiones; j++) {
					gc.drawLine(m+i*sep+j*subsep, 20+margenSup+5, m+i*sep+j*subsep, alto-margenInf-5);
			}
		}
		gc.setLineStyle(SWT.LINE_SOLID);
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
	private void cambiarPincel (GC gc, int r, int g, int b) {
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
	 * Cambia el color del fondo (background) sin exceder los límites de Color.
	 * Si se excede un límite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarPincel(GC, int, int, int)
	 */
	private void cambiarRelleno(GC gc, int r, int g, int b) {
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
	 * Dibuja un fondo distinguido para el empleado seleccionado, basado en el color del empleado
	 * pero más pálido.
	 * @param gc	El GC sobre el que resaltar el empleado
	 * @param emp	La posición del empleado a resaltar en la lista de empleados. Se considera
	 * 				que -1 significa que no hay ningún empleado seleccionado.
	 */
	// TODO Lanzar excepción si emp > empleados.size
	private void dibujarSeleccion (GC gc, int emp) {
		if (emp!=-1) {
			cambiarRelleno(gc, 255-(255-empleados.get(emp).dameColor().getRed())/5, 255-(255-empleados.get(emp).dameColor().getGreen())/5, 255-(255-empleados.get(emp).dameColor().getBlue())/5);
			gc.fillRectangle(margenNombres+margenIzq,margenSup+(sep_vert_franjas+alto_franjas)*(emp+1)-5,ancho-margenNombres-margenIzq-margenDer,alto_franjas+11);
		}
	}
	/**
	 * Pega el valor x al más cercano dentro de la rejilla. El tamaño de la rejilla está determinado
	 * por el número de subdivisiones.
	 * @param x		El valor a ajustar
	 * @return		El valor ajustado a la rejilla
	 */
	public Posicion sticky (int x) {
		int y = x - margenNombres - margenIzq + (tamHora/subdivisiones)/2;
		Posicion p;
		if (((y%tamHora)/(tamHora/subdivisiones))>=subdivisiones)
			// Para evitar resultados del tipo 14:60
			p = new Posicion(1+y/tamHora+horaInicio,0);
		else
			// En otro caso, hay que tener en cuenta cómo se dibuja el cuadrante para evitar
			// desfases entre las lineas horarias y las franjas.
			p = new Posicion(y/tamHora+horaInicio,((y%tamHora)/(tamHora/subdivisiones))*12/subdivisiones);
		return p;
	}
	/**
	 * Actualiza el tamaño del cuadrante, el tamaño de las horas y las subdivisiones, y para cada
	 * franja, actualiza sus píxeles inicial y final en función de sus valores pinicio y pfin.
	 * @param ancho	El ancho nuevo, en píxeles
	 * @param alto	El alto nuevo, en píxeles
	 */
	public void setTamaño(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaFin-horaInicio);
		tamSubdiv = tamHora/12;
		for (int i=0; i < empleados.size(); i++) {
			Empleado e = empleados.get(i);
			for (int j=0; j < e.franjas.size(); j++) {
				Franja f = e.franjas.get(j);
				f.actualizarPixeles();
			}
		}
	}
}