package interfaces;

import java.util.Calendar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import aplicacion.Empleado;
import aplicacion.Posicion;

public class I02CuadranteEmpleado {
	//private final int anchoLados = 5; // El ancho de los lados de una franja, de donde se coge para estirarla y encogerla 
	private Display display;
	private int ancho;
	private int alto;
	private int margenIzq, margenDer, margenSup, margenInf; // M�rgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	//private int alto_franjas;
	private int tama�oFila;
	//private int sep_vert_franjas;
	private int horaInicio, horaFin; // Definen de qu� hora a qu� hora es el cuadrante
	private int tamHora;
	//, tamSubdiv;
	public  int subdivisiones; // Cu�ntas subdivisiones hacer por hora (0 = sin subdivisiones)
	public Empleado empleado;
	private int tama�o =8;
	


	/**
	 * Constructor del cuadrante.
	 * @param d				Display sobre el que se dibujar� el cuadrante
	 * @param subdivisiones	N�mero de subdivisiones que se muestran en el cuadrante.  
	 * 						<ul>
	 * 						<li>12	(cada 5 min),
	 * 						<li>6	(cada 10 min),
	 * 						<li>4	(cada 15 min),
	 * 						<li>2	(cada 30 min),
	 * 						<li>1	(sin subdivisiones)
	 * 						</ul>
	 * @param horaInicio	Hora de inicio del cuadrante
	 * @param horaFin		Hora de fin del cuadrante. Las horas pasadas de las 24 se muestran
	 * 						como la madrugada del d�a siguiente.
	 * @param margenIzq		Margen izquierdo en p�xeles
	 * @param margenDer		Margen derecho en p�xeles
	 * @param margenSup		Margen superior en p�xeles
	 * @param margenInf		Margen inferior en p�xeles
	 * @param margenNombres	Margen de los nombres en p�xeles (indica d�nde empieza a dibujarse
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
		//alto_franjas = 15;
		//sep_vert_franjas = 10;
		this.subdivisiones = subdivisiones;
		
		// TODO Borrar esto cuando se importen los empleados
		Empleado e1 = new Empleado(2, "M. Jackson", new Color (display, 104, 228,  85));
		e1.franjaNueva(new Posicion( 9,  6), new Posicion(14,  0));
		e1.franjaNueva(new Posicion(16,  0), new Posicion(18,  0));
		empleado = e1;

	}
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujar� el cuadrante.
	 * @param empleadoActivo	La posici�n del empleado a resaltar en la lista de empleados.
	 */
	// TODO Deber�a lanzar una excepci�n si empleadoActivo > empleados.size
	public void dibujarCuadranteDia(GC gc, int empleadoActivo) {
		dibujarDias(gc);
	}
	
	public void dibujarCuadranteMes(GC gc){
		Calendar c = Calendar.getInstance();
		// Esto coge el d�a 1 de este mes
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		// Y esto en qu� d�a de la semana cae
		int primerDia = c.get(Calendar.DAY_OF_WEEK);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		c.roll(Calendar.DAY_OF_MONTH,false); // Pasa al �ltimo d�a del este mes
		int ultimoDia = c.get(Calendar.DAY_OF_MONTH);
		int anchoMes = ancho - margenIzq - margenDer - margenNombres;
		int anchoDia = anchoMes/ultimoDia;
		int altoFila = 20;
		// Dibujar n�meros de los d�as
		if (anchoDia>14)
			for (int j=0; j < ultimoDia; j++) {
				gc.drawText(String.valueOf(j+1), margenIzq + margenNombres + j*anchoDia + anchoDia/2, margenSup);
			}

		gc.drawText(empleado.getNombre(), margenIzq, margenSup + 20 + 0*altoFila);
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
	 * @param gc	El GC del display sobre el que se dibujar� el cuadrante.
	 */
	private void dibujarDias(GC gc) {
		//cambiarPincel(gc, 40,80,40);
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep = (ancho - m - margenDer)/h;
		//int subsep = sep/subdivisiones;
		tama�oFila=(alto)/15;
		cambiarPincel(gc, 0,143,65);
		this.cambiarRelleno(gc, 180,230,180);
		int inferior=((int)((alto-margenInf-(tama�oFila+this.margenSup))
				/(horaFin-horaInicio)))*(horaFin-horaInicio);
		gc.fillGradientRectangle(m,tama�oFila+this.margenSup,7*sep,inferior,false);
		int inferior_total=tama�oFila+margenSup+inferior;
		this.cambiarPincel(gc, 10, 160, 90);
		this.cambiarRelleno(gc, 0, 143, 65);
		gc.fillGradientRectangle(m,this.margenSup,7*sep,tama�oFila,true);
		
		cambiarPincel(gc, 0,0,0);
		gc.drawLine(m, this.margenSup, m+7*sep, this.margenSup);
		gc.drawLine(m, tama�oFila+this.margenSup, m+7*sep, tama�oFila+this.margenSup);
		gc.drawLine(m, inferior_total, m+7*sep, inferior_total);
		gc.drawRoundRectangle(m,this.margenSup,7*sep,tama�oFila,8,8);
		gc.drawRectangle(m, this.margenSup, 7*sep, tama�oFila);
		for (int i=0; i<=h; i++) {
			gc.setLineStyle(SWT.LINE_SOLID);
			cambiarPincel(gc, 0,0,0);
			gc.drawLine(m+i*sep, this.margenSup, m+i*sep, inferior_total);
			gc.setLineStyle(SWT.LINE_DOT);
			if (i!=h)
			{	cambiarPincel(gc, 150,250,150);
				String[] diasSemana = {"Lunes","Martes","Miercoles","Jueves","Viernes ","Sabado ","Domingo"};
				int tama�o1= sep/8;
				int tama�o2= tama�oFila/2;
				if (tama�o1<tama�o2){
					tama�o = tama�o1;
					}
				else{
					tama�o=tama�o2;
				}

				Font fuente=gc.getFont();
				gc.setFont(new Font(display,"Verdana",tama�o,SWT.BOLD|SWT.ITALIC));
				String text = diasSemana[((i)%7)];
		        Point textSize = gc.textExtent(text);
		        gc.drawText(diasSemana[((i)%7)],((m+i*sep)+((m+(i+1)*sep)))/2-textSize.x/2, margenSup+(alto/50), true);
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
	 * Cambia el color del pincel (foreground) sin exceder los l�mites de Color.
	 * Si se excede un l�mite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarRelleno(GC, int, int, int)
	 */
	private void cambiarPincel (GC gc, int r, int g, int b) {
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
	private void cambiarRelleno(GC gc, int r, int g, int b) {
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
	 * Pega el valor x al m�s cercano dentro de la rejilla. El tama�o de la rejilla est� determinado
	 * por el n�mero de subdivisiones.
	 * @param x		El valor a ajustar
	 * @return		El valor ajustado a la rejilla
	 */

	/**
	 * Actualiza el tama�o del cuadrante, el tama�o de las horas y las subdivisiones, y para cada
	 * franja, actualiza sus p�xeles inicial y final en funci�n de sus valores pinicio y pfin.
	 * @param ancho	El ancho nuevo, en p�xeles
	 * @param alto	El alto nuevo, en p�xeles
	 */
	public void setTama�o(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaFin-horaInicio);
		Empleado e = empleado;
	}
	
	
	/**
	 * Metodo que dibuja un turno de trabajo de un empleado, esto es desde que entra hasta que sale
	 * sin interrupciones
	 * @param gc
	 * @param dia dia de la semana del que vamos a dibujar el turno
	 * @param horaComienzo hora del comienzo del turno
	 * @param horaFinal	hora del fin del turno
	 * @param Departamento	Nombre del departamento en el que va a trabajar
	 */
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
		int x_comienzo_t = ((x_comienzo_c+x_fin)/2);
		int longitud = (int)((x_fin-x_comienzo_c));
		gc.fillGradientRectangle(x_comienzo_c,y_comienzo,longitud,y_fin-y_comienzo,true);
		this.cambiarPincel(gc, 0, 0, 0);
		gc.drawRoundRectangle(x_comienzo_c,y_comienzo,longitud,y_fin-y_comienzo,8,8);
		int sep=(ancho - m - margenDer)/h;
		float tama�ox= sep/12;
		float tama�oy= tama�oFila/3;
		

		if (tama�ox<tama�oy){
			System.out.println("1");
			System.out.println(tama�oy-tama�ox);
			tama�o = (int)(tama�ox);
			}
		else{
			System.out.println("2");
			System.out.println(tama�ox-tama�oy);
			tama�o= (int)tama�oy;
		}
		
		Font fuente=gc.getFont();
		gc.setFont(new Font(display,"Verdana",tama�o,SWT.BOLD));
		gc.drawText(String.valueOf((int)horaComienzo),x_comienzo, (y_comienzo), true);
		String text = Departamento;
        Point textSize = gc.textExtent(text);
        gc.drawText(Departamento,x_comienzo_t-textSize.x/2, (y_comienzo*2+y_fin)/3, true);
		String text2 = (String.valueOf((int)horaFinal));
        Point textSize2 = gc.textExtent(text2);
		gc.drawText((String.valueOf((int)horaFinal)),x_comienzo , y_fin-textSize2.y, true);
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

	/**
	 * Metodo que convierte una hora determinada a una coordenada  y
	 */
	public int convertirHora(float hora) {
		
		Float hora_relativa = hora- horaInicio;
		int duracion = horaFin-horaInicio;
		int tama�o=alto-margenInf-(tama�oFila+this.margenSup);
		int posicion_relativa = (int)((tama�o / duracion)*hora_relativa);
		int posicion_absoluta =tama�oFila+this.margenSup+posicion_relativa;
		return posicion_absoluta;
	}
	
	/**
	 * Metodo que dibuja una linea horizontal a una hora dada
	 * @param gc
	 * @param hora hora a la que queremos dibujar la hora
	 */
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
