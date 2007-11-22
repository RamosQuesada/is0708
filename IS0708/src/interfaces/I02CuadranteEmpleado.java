package interfaces;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class I02CuadranteEmpleado {
	private final int anchoLados = 5; // El ancho de los lados de una franja, de donde se coge para estirarla y encogerla 
	private Display display;
	private int ancho;
	private int alto;
	private int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	private int alto_franjas;
	private int tamañoFila;
	private int sep_vert_franjas;
	private int horaInicio, horaFin; // Definen de qué hora a qué hora es el cuadrante
	private int tamHora, tamSubdiv;
	public  int subdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	public Empleado empleado;
	private int tamaño =8;
	

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
		 * @param hora	La hora de la posición
		 * @param cmin	Los cminutos (intervalos de 5 minutos)
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
			//gc.fillGradientRectangle(inicio+2,despV+2,fin-inicio,15,false);
			//gc.fillRoundRectangle(inicio+2,despV+2,fin-inicio,15,10,10);
			cambiarRelleno(gc, r,g,b);
			cambiarPincel(gc, r-100,g-100,b-100);
			gc.fillGradientRectangle(inicio,despV,fin-inicio,15,false);
			//gc.fillRoundRectangle(inicio,despV,fin-inicio,15,8,8);
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
			//gc.drawText(nombre, margenIzq, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
			for (int i=0; i<franjas.size(); i++) {
				//franjas.get(i).dibujarFranja(gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
				subDivs += (franjas.get(i).pfin.hora - franjas.get(i).pinicio.hora)*12;
				subDivs += (franjas.get(i).pfin.cmin - franjas.get(i).pinicio.cmin);
			}
			//gc.drawText(String.valueOf(subDivs/12)+":"+String.valueOf(aString(subDivs%12*60/12)), margenNombres-10, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
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
	 * Clase provisional para representar un turno. A descartar.
	 * @author Daniel
	 *
	 */
	public class Turno {
		int id;
		String nombre;
		String abrev;
	}
	/**
	 * Constructor del cuadrante.
	 * @param d				Display sobre el que se dibujará el cuadrante
	 * @param subdivisiones	Número de subdivisiones que se muestran en el cuadrante.  
	 * 						<ul>
	 * 						<li>12	(cada 5 min),
	 * 						<li>6	(cada 10 min),
	 * 						<li>4	(cada 15 min),
	 * 						<li>2	(cada 30 min),
	 * 						<li>1	(sin subdivisiones)
	 * 						</ul>
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
	public I02CuadranteEmpleado(Display d, int subdivisiones, int horaInicio, int horaFin, int margenIzq, int margenDer, int margenSup, int margenInf, int margenNombres) {
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
		Empleado e1 = new Empleado("M. Jackson", new Color (display, 104, 228,  85));
		e1.franjaNueva(new Posicion( 9,  6), new Posicion(14,  0));
		e1.franjaNueva(new Posicion(16,  0), new Posicion(18,  0));
		empleado = e1;

	}
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujará el cuadrante.
	 * @param empleadoActivo	La posición del empleado a resaltar en la lista de empleados.
	 */
	// TODO Debería lanzar una excepción si empleadoActivo > empleados.size
	public void dibujarCuadranteDia(GC gc, int empleadoActivo) {
		dibujarDias(gc);
	}
	
	public void dibujarCuadranteMes(GC gc){
		Calendar c = Calendar.getInstance();
		// Esto coge el día 1 de este mes
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		// Y esto en qué día de la semana cae
		int primerDia = c.get(Calendar.DAY_OF_WEEK);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		c.roll(Calendar.DAY_OF_MONTH,false); // Pasa al último día del este mes
		int ultimoDia = c.get(Calendar.DAY_OF_MONTH);
		int anchoMes = ancho - margenIzq - margenDer - margenNombres;
		int anchoDia = anchoMes/ultimoDia;
		int altoFila = 20;
		// Dibujar números de los días
		if (anchoDia>14)
			for (int j=0; j < ultimoDia; j++) {
				gc.drawText(String.valueOf(j+1), margenIzq + margenNombres + j*anchoDia + anchoDia/2, margenSup);
			}

		gc.drawText(empleado.nombre, margenIzq, margenSup + 20 + 0*altoFila);
		for (int j=0; j < ultimoDia; j++) {
			gc.drawRectangle(margenIzq + margenNombres + j*anchoDia, margenSup + 20 + 0*altoFila, anchoDia, altoFila);
		}

		
		
		
		
		// Esto es para un calendario normal
		int altoMes = alto - margenSup - margenInf;
		int numSemanas = 5;
		int altoDia = alto/numSemanas;
		
	}
	/**
	 * Dibuja lineas verticales representando los dias.
	 * @param gc	El GC del display sobre el que se dibujará el cuadrante.
	 */
	private void dibujarDias(GC gc) {
		//cambiarPincel(gc, 40,80,40);
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep = (ancho - m - margenDer)/h;
		//int subsep = sep/subdivisiones;
		tamañoFila=(alto)/15;
		cambiarPincel(gc, 0,143,65);
		this.cambiarRelleno(gc, 180,230,180);
		gc.fillGradientRectangle(m,tamañoFila+this.margenSup,7*sep,alto-margenInf-(tamañoFila+this.margenSup),false);

		this.cambiarPincel(gc, 10, 160, 90);
		this.cambiarRelleno(gc, 0, 143, 65);
		gc.fillGradientRectangle(m,this.margenSup,7*sep,tamañoFila,true);
		
		cambiarPincel(gc, 0,0,0);
		gc.drawLine(m, this.margenSup, m+7*sep, this.margenSup);
		gc.drawLine(m, tamañoFila+this.margenSup, m+7*sep, tamañoFila+this.margenSup);
		gc.drawLine(m, alto-margenInf, m+7*sep, alto-margenInf);

		//gc.fillRoundRectangle(m,tamañoFila+this.margenSup,7*sep,alto-margenInf-(tamañoFila+this.margenSup),1,1);
		//gc.drawRoundRectangle(m,tamañoFila+this.margenSup,margenInf-margenSup,15,8,8);
		//cambiarPincel(gc,100,0,0);
		//this.cambiarRelleno(gc, 0,143,65);
		//cambiarPincel(gc, 100,100,100);
	

		//gc.fillRoundRectangle(m,this.margenSup,7*sep,tamañoFila,8,8);
		gc.drawRoundRectangle(m,this.margenSup,7*sep,tamañoFila,8,8);
		gc.drawRectangle(m, this.margenSup, 7*sep, tamañoFila);
		for (int i=0; i<=h; i++) {
			gc.setLineStyle(SWT.LINE_SOLID);
			cambiarPincel(gc, 0,0,0);
			gc.drawLine(m+i*sep, this.margenSup, m+i*sep, alto-margenInf);
			//cambiarPincel(gc, 120,170,120);
			gc.setLineStyle(SWT.LINE_DOT);
			if (i!=h)
			{	cambiarPincel(gc, 150,250,150);
				String[] diasSemana = {"     Lunes   ","   Martes"," Miercoles ","   Jueves","   Viernes ","   Sabado ","  Domingo  "};
				int tamaño1= sep/8;
				int tamaño2= tamañoFila/2;
				int desplazamiento=0;
				if (tamaño1<tamaño2){
					tamaño = tamaño1;
					}
				else{
					tamaño=tamaño2;
					desplazamiento = (tamaño1-tamaño2)*4;
				}
				
				Font fuente=gc.getFont();
				gc.setFont(new Font(display,"Verdana",tamaño,SWT.BOLD|SWT.ITALIC));
				gc.drawText(diasSemana[((i)%7)],m+i*sep+desplazamiento , margenSup+(alto/50), true);
				gc.getFont().dispose();
				gc.setFont(fuente);
			}
		}
		gc.setLineStyle(SWT.LINE_SOLID);
		dibujarTurno(gc,0,10.5f,12.5f,"INFOR.");
		dibujarTurno(gc,1,12,13,"FRUTE.");
		dibujarTurno(gc,2,15,16,"PELUQ.");
		dibujarTurno(gc,3,15,23,"FRUTE.");
		dibujarTurno(gc,4,10,12,"CAFET.");
		dibujarTurno(gc,4,15,16,"CAFET.");
		dibujarTurno(gc,5,17,18,"VIAJE.");
		dibujarTurno(gc,6,9,17, "VIAJE.");
		dibujarTurno(gc,0,16,17,"VIAJE.");
		dibujarTurno(gc,6,18,23,"VIAJE.");
		//this.dibujarLineaHorizontal(gc, 15.0f);
		int num_subdivisiones=(int)((this.subdivisiones)*(this.horaFin-this.horaInicio)+1);
		for(int cont=0;cont<num_subdivisiones;cont++){
			float fraccion = 1.0f/this.subdivisiones;
			float hora=fraccion*cont;
			hora +=this.horaInicio;
			this.dibujarLineaHorizontal(gc, hora);
		}
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
	 * Pega el valor x al más cercano dentro de la rejilla. El tamaño de la rejilla está determinado
	 * por el número de subdivisiones.
	 * @param x		El valor a ajustar
	 * @return		El valor ajustado a la rejilla
	 */

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
			Empleado e = empleado;
			for (int j=0; j < e.franjas.size(); j++) {
				Franja f = e.franjas.get(j);
				f.actualizarPixeles();
			}
	}
	
	
	public void dibujarTurno(GC gc,int dia,float horaComienzo,float horaFinal,String Departamento){
		int x_comienzo=convertirDia(dia);
		int y_comienzo=convertirHora(horaComienzo);
		int x_fin=convertirDia(dia+1);
		int y_fin=convertirHora(horaFinal);
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		this.cambiarPincel(gc, 150, 255, 150);
		this.cambiarRelleno(gc, 100, 200, 100);
		int x_comienzo_c = (x_comienzo*8+x_fin*2)/10;
		int longitud = (int)((x_fin-x_comienzo_c));
		gc.fillGradientRectangle(x_comienzo_c,y_comienzo,longitud,y_fin-y_comienzo,true);
		this.cambiarPincel(gc, 0, 0, 0);
		gc.drawRoundRectangle(x_comienzo_c,y_comienzo,longitud,y_fin-y_comienzo,8,8);
		int sep=(ancho - m - margenDer)/h;
		float tamañox= sep/12;
		float tamañoy= tamañoFila/3;
		

		int desplazamiento=0;
		if (tamañox<tamañoy){
			System.out.println("1");
			System.out.println(tamañoy-tamañox);
			tamaño = (int)(tamañox);
			desplazamiento = (int)((tamañoy-tamañox)*15);
			}
		else{
			System.out.println("2");
			System.out.println(tamañox-tamañoy);
			tamaño= (int)tamañoy;
			desplazamiento = (int)(tamañox-tamañoy)*15;
		}
		//if(desplazamiento<3){desplazamiento=20;		}
		
		Font fuente=gc.getFont();
		gc.setFont(new Font(display,"Verdana",tamaño,SWT.BOLD));
		gc.drawText(String.valueOf((int)horaComienzo),x_comienzo, (y_comienzo), true);
		gc.drawText(Departamento,m+dia*sep+desplazamiento , (y_comienzo*2+y_fin)/3, true);
		gc.drawText(String.valueOf((int)horaFinal),x_comienzo , y_fin-10, true);
		gc.getFont().dispose();
		gc.setFont(fuente);

	}
	
	public int convertirDia(int dia) {
		int x=0;
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep=(ancho - m - margenDer)/h;
		x=m+dia*sep;
		return x;

	}
	
	public int convertirHora(float hora) {
		
		Float hora_relativa = hora- horaInicio;
		int duracion = horaFin-horaInicio;
		int tamaño=alto-margenInf-(tamañoFila+this.margenSup);
		int posicion_relativa = (int)((tamaño / duracion)*hora_relativa);
		int posicion_absoluta =tamañoFila+this.margenSup+posicion_relativa;
		return posicion_absoluta;
	}
	
	public void dibujarLineaHorizontal(GC gc,float hora){
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep=(ancho - m - margenDer)/h;
		gc.setLineStyle(SWT.LINE_DOT);
		cambiarPincel(gc, 0,0,0);
		
		gc.drawLine(this.margenIzq, convertirHora(hora), this.margenIzq+7*sep,convertirHora(hora));
		gc.setLineStyle(SWT.LINE_SOLID);
	}

}
