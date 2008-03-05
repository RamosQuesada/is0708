package aplicacion;

import java.sql.Time;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;

import aplicacion.Util;

/**
 * Esta clase representa un turno.
 * Se corresponde con la representacion del turno en la tabla de la base de datos
 * @author Chema
 *
 */
public class Turno {
	private int idTurno;
	private String descripcion;
	private Time horaEntrada;
	private Time horaSalida;
	private Time horaDescanso;
	private int tDescanso; //minutos de descanso
	
	
	/**
	 * 
	 * @param idTurno  		 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno
	 * @param horaEntrada	 Hora teórica a la que debería comenzar la jornada laboral del usuario 
	 * @param horaSalida	 Hora teórica a la que debería terminar la jornada laboral del usuario
	 * @param horaDescanso	 Hora de inicio del descanso
	 * @param descanso		 Tiempo asignado a descanso (en minutos)

	 */
	public Turno(int idTurno, String descripcion, Time horaEntrada, Time horaSalida, Time horaDescanso, int descanso) {
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.horaDescanso = horaDescanso;
		tDescanso = descanso;
	}
	
	/**
	 * 
	 * @param idTurno  	 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno 
	 * @param horaEntrada	 Hora te�rica a la que deber�a comenzar la jornada laboral del usuario 
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param horaSalida	 Hora te�rica a la que deber�a terminar la jornada laboral del usuario
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param horaDescanso	 Hora de inicio del descanso
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param descanso		 Tiempo asignado a descanso (en minutos)
	 */
	public Turno(int idTurno, String descripcion,String horaEntrada, String horaSalida, String horaDescanso, int descanso) {
		super();
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		tDescanso = descanso;
		this.horaEntrada=Time.valueOf(horaEntrada);
		this.horaSalida=Time.valueOf(horaSalida);
		this.horaDescanso=Time.valueOf(horaDescanso);

	}
	
	
	/**
	 * Prueba, algoritmo
	 * @param id
	 * @deprecared
	 */
	public Turno(int id){
		this.idTurno = id;
		
	}
	
	/**
	 * 
	 * @param t	horas y minutos
	 * @return  devuelve la hora del tiempo introducido
	 */
	public long obtenHoras(Time t){
		   //obtenemos los segundos
		   long segundos = t.getTime() / 1000;		 
		   //obtenemos las horas
		   long horas = segundos / 3600;		 
		   //restamos las horas para continuar con minutos
		   segundos -= horas*3600;		 
		   //igual que el paso anterior
		   long minutos = segundos /60;
		   segundos -= minutos*60;
		   return horas;		
	}
	/**
	 * 
	 * @param t  horas y minutos en formato Time
	 * @return	 devuelve los minutos de la hora introducida
	 */
	public long obtenMinutos(Time t){
		   //obtenemos los segundos
		   long segundos = t.getTime() / 1000;		 
		   //obtenemos las horas
		   long horas = segundos / 3600;		 
		   //restamos las horas para continuar con minutos
		   segundos -= horas*3600;		 
		   //igual que el paso anterior
		   long minutos = segundos /60;
		   segundos -= minutos*60;
		   return minutos;		
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Time getHoraDescanso() {
		return horaDescanso;
	}
	public void setHoraDescanso(Time horaDescanso) {
		this.horaDescanso = horaDescanso;
	}
	public Time getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(Time horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public Time getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(Time horaSalida) {
		this.horaSalida = horaSalida;
	}
	public int getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(int idTurno) {
		this.idTurno = idTurno;
	}
	public int getTDescanso() {
		return tDescanso;
	}
	public void setTDescanso(int descanso) {
		tDescanso = descanso;
	}
	
	
/**
 * 	Estos métodos sirven para representar un turno en un interfaz
 */	
	/** Horas y minutos que el empleado está trabajando en este turno */
	private int horas1,minutos1,horas2,minutos2;
	/** Estos indican si el cursor está encima de una de las dos franjas */
	private boolean activa1 = false;
	private boolean activa2 = false;
	private int anchoLados = 4;
	int inicio1, inicio2, fin1, fin2;
	int despl = 0; // Para saber de dónde la he cogido

	public void calculaTiempoTrabajado() {
		int franja1, franja2;
		if (tDescanso!=0) {
			franja1 =  horaDescanso.getHours()*60 + horaDescanso.getMinutes() - horaEntrada.getHours()*60 - horaEntrada.getMinutes();		 
			minutos1 = franja1%60;
			horas1 = franja1/60;
			franja2 = horaSalida.getHours()*60 + horaSalida.getMinutes() - horaDescanso.getHours()*60 - horaDescanso.getMinutes() -tDescanso;
			minutos2 = franja2%60;
			horas2 = franja2/60;
		}
		else {
			franja1 = horaSalida.getHours()*60 + horaSalida.getMinutes() - horaEntrada.getHours()*60 - horaEntrada.getMinutes();
			minutos1 = franja1%60;
			horas1 = franja1/60;
			minutos2 = 0;
			horas2 = 0;
		}
	}
	
	
	public Boolean contienePunto (int y, int posV, int margenSup, int sep_vert_franjas, int alto_franjas) {
		Boolean b = false;
		if (y > margenSup+(sep_vert_franjas+alto_franjas)*(posV+1) && y<=margenSup+(sep_vert_franjas+alto_franjas)*(posV+2)) b = true;
		return b;
	}
	
	public void recalcularFranjas(int margenIzq, int margenNombres, int horaApertura, int tamHora) {
		inicio1 = margenIzq + margenNombres + (horaEntrada.getHours()-horaApertura) * tamHora + (tamHora*horaEntrada.getMinutes())/60;
		if (tDescanso==0) {
			fin1 = margenIzq + margenNombres + (horaSalida.getHours()-horaApertura) * tamHora + (tamHora*horaSalida.getMinutes())/60;
		}
		else {
			fin1    = margenIzq + margenNombres + (horaDescanso.getHours()-horaApertura) * tamHora + (tamHora*horaDescanso.getMinutes())/60;
			inicio2 = margenIzq + margenNombres + (horaDescanso.getHours()-horaApertura+tDescanso/60) * tamHora  + (tamHora*(horaDescanso.getMinutes()+(tDescanso%60)))/60;
			fin2    = margenIzq + margenNombres + (horaSalida.getHours()-horaApertura) * tamHora + (tamHora*horaSalida.getMinutes())/60;
		}
	}
	
	public void dibujar(Display display, String nombre, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas, int tamHora, int tamSubdiv, int horaApertura, int numSubdiv) {
		calculaTiempoTrabajado();
		// Si el empleado no tiene color, asignarle un color gris
		if (color==null)
			color = new Color(display,120,170,120);
		gc.setBackground(new Color(display, 0,0,0));
		gc.setForeground(new Color(display, 0,0,0));
		int despV = margenSup+(posV+1)*(alto_franjas+sep_vert_franjas);
		if (margenNombres > 0) {
			gc.drawText(nombre, margenIzq, despV, true);
			gc.drawText(horas1+horas2+(minutos1+minutos2)/60 + ":" + Util.aString((minutos1+minutos2)%60), margenNombres-10, despV, true);
		}
		// Si tDescanso > 0, hay que pintar 2 trozos. Si no, solo 1.
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		// Dibujar franja
		cambiarRelleno(display, gc, r-50,g-50,b-50);
		gc.fillRoundRectangle(inicio1+2,despV+2,fin1-inicio1,15,10,10);
		cambiarRelleno(display, gc, r,g,b);
		cambiarPincel(display, gc, r-100,g-100,b-100);
		gc.fillRoundRectangle(inicio1,despV,fin1-inicio1,15,8,8);
		gc.drawRoundRectangle(inicio1,despV,fin1-inicio1,15,8,8);
		if (tDescanso!=0) {
			// Dibujar segunda franja
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio2+2,despV+2,fin2-inicio2,15,10,10);
			cambiarRelleno(display, gc, r,g,b);
			cambiarPincel(display, gc, r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio2,despV,fin2-inicio2,15,8,8);
			gc.drawRoundRectangle(inicio2,despV,fin2-inicio2,15,8,8);
		}
		
		// Dibujar pestaña encima de la franja, si está activa
		if (activa1) {
			// Modificar los colores teniendo siempre en cuenta los límites [0-255]
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio1+2,despV-13,135,20,10,10);
			cambiarRelleno(display, gc, r,g,b);	
			gc.fillRoundRectangle(inicio1, despV-15, 135, 20,8,8);
			gc.drawRoundRectangle(inicio1, despV-15, 135, 20,8,8);
			gc.fillRectangle(inicio1+1,despV+1,Math.min(fin1-inicio1-1,136),12);
			String s1 = "";
			if (minutos1 != 0) s1=' '+ String.valueOf(minutos1) +'m';
			String s;
			if (tDescanso==0)
				s =  Util.aString(horaEntrada.getHours()) + ":" + Util.aString(horaEntrada.getMinutes()) + " - " + Util.aString(horaSalida.getHours()) + ":" + Util.aString(horaSalida.getMinutes()) + " (" + String.valueOf(horas1)+'h'+s1+')';
			else
				s = Util.aString(horaEntrada.getHours()) + ":" + Util.aString(horaEntrada.getMinutes()) + " - " + Util.aString(horaDescanso.getHours()) + ":" + Util.aString(horaDescanso.getMinutes()) + " (" + String.valueOf(horas1)+'h'+s1+')';
			gc.drawText(s, inicio1+5, despV-14, true);
		}
		else if (activa2) {
			// Modificar los colores teniendo siempre en cuenta los límites [0-255]
			cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio2+2,despV-13,135,20,10,10);
			cambiarRelleno(display, gc, r,g,b);	
			gc.fillRoundRectangle(inicio2, despV-15, 135, 20,8,8);
			gc.drawRoundRectangle(inicio2, despV-15, 135, 20,8,8);
			gc.fillRectangle(inicio2+1,despV+1,Math.min(fin2-inicio2-1,136),12);
			String s1 = "";
			if (minutos2 != 0) s1=' '+ String.valueOf(minutos2) +'m';
			String s = Util.aString(horaDescanso.getHours()+tDescanso/60) + ":" + Util.aString(horaDescanso.getMinutes()+tDescanso%60) + " - " + Util.aString(horaSalida.getHours()) + ":" + Util.aString(horaSalida.getMinutes()) + " (" + String.valueOf(horas2)+'h'+s1+')';
			gc.drawText(s, inicio2+5, despV-14, true);
		}

		
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
	private void cambiarRelleno(Display display, GC gc, int r, int g, int b) {
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
	private void cambiarPincel (Display display, GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(display,r, g, b));
	}
	
	public void desactivarFranjas() {
		activa1 = false;
		activa2 = false;
	}
	
	/**
	 * Comprueba si el píxel dado está contenido en el interior de la alguna franja
	 * del turno, sin tener en cuenta los bordes, es decir, en el intervalo abierto
	 * (inicio+d,fin-d), donde 'd' es el ancho del borde de la franja, de donde se
	 * coge para estirarla y encogerla.
	 * @param x	Píxel a comprobar
	 * @see #contienePixel(int)
	 * @see	#tocaLadoDerecho(int)
	 * @see #tocaLadoIzquierdo(int)
	 * @return Si inicio+d < x < fin-d.
	 */
	public boolean contienePixelInt(int x) {
		return (contienePixelInt1(x, anchoLados) ||
				contienePixelInt2(x, anchoLados));
	}
	
	private boolean contienePixelInt1(int x, int ancho ) {
		boolean b = false;
		if (x>inicio1+ancho && x<fin1-ancho) {
			activa1 = true; b = true;
		}
		return b;
	}
	
	private boolean contienePixelInt2(int x, int ancho ) {
		boolean b = false;
		if (tDescanso!=0) {			
			if (x>inicio2+ancho && x<fin2-ancho) {
				activa2 = true; b = true;
			}
		}
		return b;
	}

	/**
	 * Comprueba si el píxel dado está contenido en el lado izquierdo de la franja, es decir,
	 * en el intervalo cerrado [inicio-d,inicio+d], donde 'd' es el ancho del borde de la franja,
	 * de donde se coge para estirarla y encogerla.
	 * @param x P�xel a comprobar
	 * @see #contienePixel(int)
	 * @see #contienePixelInt(int)
	 * @see	#tocaLadoDerecho(int)
	 * @return Si inicio-d <= x <= inicio+d
	 */
	public boolean tocaLadoIzquierdo(int x) {
		return (tocaLadoIzquierdo1(x) || 
				tocaLadoIzquierdo2(x));
	}

	private boolean tocaLadoIzquierdo1(int x) {
		boolean cambiaInicio = false;
		if (x>=inicio1-anchoLados && x<=inicio1+anchoLados)                 { activa1 = true; activa2=false; cambiaInicio = true; }
		return cambiaInicio;
	}
	
	private boolean tocaLadoIzquierdo2(int x) {
		boolean cambiaInicio = false;
		if (tDescanso!=0) {
			if (x>=inicio2-anchoLados && x<=inicio2+anchoLados) { activa2 = true; activa1=false; cambiaInicio = true; }
		}
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
	public boolean tocaLadoDerecho(int x) {
		return (tocaLadoDerecho1(x) || 
				tocaLadoDerecho2(x));
	}
	
	private boolean tocaLadoDerecho1(int x) {
		boolean cambiaFin = false;
		if (x>=fin1-anchoLados && x<=fin1+anchoLados)                 { activa1 = true; activa2=false; cambiaFin = true; }
		return cambiaFin;
	}

	private boolean tocaLadoDerecho2(int x) {
		boolean cambiaFin = false;
		if (tDescanso!=0) {
			if (tDescanso!=0 && x>=fin2-anchoLados && x<=fin2+anchoLados) { activa2 = true; activa1=false; cambiaFin = true; }
		}
		return cambiaFin;
	}


	public void activarFranja1() {
		activa1 = true;
	}
	
	public void activarFranja2() {
		activa2 = true;
	}
	
	/**
	 * Realiza las acciones pertinentes al pulsar el botón secundario del ratón
	 * @param x la posición horizontal del cursor
	 * @param margenIzq el margen izquierdo
	 * @param margenNombres el margen para los nombres
	 * @param horaApertura la hora de apertura
	 * @param tamHora el tamaño de una hora
	 * @param tamSubdiv el tamaño de una subdivision
	 */
	public void botonSecundario (int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv) {
		// Si hay descanso, se elimina la franja seleccionada
		if (tDescanso!=0) {
			// Si estoy borrando la primera franja, tengo que copiar la segunda a la primera
			if (contienePixelInt1(x, 0)) {
				horaEntrada.setHours(horaDescanso.getHours()+(tDescanso/60));
				horaEntrada.setMinutes(horaDescanso.getMinutes()+(tDescanso%60));
				quitarDescanso();
				activa1 = false;
				recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
			}
			// Si estoy borrando la segunda franja, tengo que adelantar la salida
			else if (contienePixelInt2(x, 0)) {
				horaSalida.setHours(horaDescanso.getHours());
				horaSalida.setMinutes(horaDescanso.getMinutes());
				quitarDescanso();
				activa2 = false;
				recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
			}
		}
		// Si no hay descanso, se añade 
		else {
			if (contienePixelInt1(x, 0)) {
				//Insertar un descanso predeterminado de 30 minutos en donde se ha pinchado
				tDescanso = 30;
				horaDescanso.setHours(dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora));
				horaDescanso.setMinutes(dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv));
				// Ahora falta comprobar que la vuelta del descanso no sea posterior 
				// a la salida
				recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
				if (fin1>=fin2) {
					horaSalida.setHours(horaDescanso.getHours()+tDescanso/60+1);
				}
			}
		}
	}
	
	public int botonPrimario (int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv) {
		// Movimiento:
		//  1 - Moviendo inicio franja
		//  2 - Moviendo toda la franja
		//  3 - Moviendo fin franja
		int m = 0;
		if      (tocaLadoIzquierdo(x)) m = 1;
		else if (contienePixelInt(x)) { m = 2; 
			//Calcular minutos al inicio de la franja
			calcularDespl(x, margenIzq, margenNombres, horaApertura, tamHora, tamSubdiv, numSubdiv);
		}
		else if (tocaLadoDerecho(x)) m = 3;
		return m;
		
	};
	
	private void calcularDespl(int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv) {
		if (activa1) {
			despl = 
				dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora)*numSubdiv +
				dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv) -
				horaEntrada.getHours()*numSubdiv -
				horaEntrada.getMinutes()*numSubdiv/60;
		}
		else {
			despl = 
				dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora)*numSubdiv +
				dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv) -
				horaDescanso.getHours()*numSubdiv -
				horaDescanso.getMinutes()*numSubdiv/60 -
				tDescanso*numSubdiv/60;
		}

	}
	public int moverLadoIzquierdo(int x, int margenIzq, int margenNombres, int horaApertura, int tamHora, int tamSubdiv, int numSubdiv){
		int mov = 1; // Esta variable indica si sigo moviendo el lado izquierdo
		int h = dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora);
		int m = dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv);
		int t = h*60+m;
		if (activa1) {
			// Si me paso por la izquierda, pegar al borde
			if (h<horaApertura) {
				horaEntrada.setHours(horaApertura);
				horaEntrada.setMinutes(0);
			}
			// Si me paso por la derecha, y hay descanso, eliminar franja
			else if (tDescanso!=0 && t>=horaDescanso.getHours()*60 + horaDescanso.getMinutes()) {
				horaEntrada.setHours(horaDescanso.getHours()+tDescanso/60);
				horaEntrada.setMinutes(horaDescanso.getMinutes()+tDescanso%60);
				quitarDescanso();
				activa1=false;
				mov = 0;
				System.out.println("pasa derecha con descanso");
			}
			// Si me paso por la derecha, y no hay descanso
			else if (tDescanso==0 && t>=horaSalida.getHours()*60 + horaSalida.getMinutes()) {
				// no hace nada		
			}
			// Desplazamiento normal
			else {
				horaEntrada.setHours(h); 
				horaEntrada.setMinutes(m);
			}
		}
		else if (activa2) {
			// Si me paso por la izquierda, juntar franjas
			if (t<=horaDescanso.getHours()*60 + horaDescanso.getMinutes()) {
				quitarDescanso();
				activa2 = false;
				mov = 0;
			}
			//Si me paso por la derecha, eliminar franja
			else if (t>=horaSalida.getHours()*60+ horaSalida.getMinutes()) {
				horaSalida.setHours(horaDescanso.getHours());
				horaSalida.setMinutes(horaDescanso.getMinutes());
				quitarDescanso();
				activa2 = false;
				mov = 0;
			}
			// Desplazamiento normal
			else {
				tDescanso = (h - horaDescanso.getHours())*60 + (m-horaDescanso.getMinutes());
			}
		}
		recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
		return mov;
	}
	
	public int moverLadoDerecho(int x, int margenIzq, int margenNombres, int horaApertura, int horaCierre, int tamHora, int tamSubdiv, int numSubdiv){
		int mov = 3;
		int h = dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora);
		int m = dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv);
		int t = h*60+m;

		if (activa1) {
			// Si me paso por la izquierda 
			if (t<=horaEntrada.getHours()*60 + horaEntrada.getMinutes()) {
				// si hay descanso, eliminar franja
				if (tDescanso!=0) { 
					horaEntrada.setHours(horaDescanso.getHours()+tDescanso/60);
					horaEntrada.setMinutes(horaDescanso.getMinutes()+tDescanso%60);
					quitarDescanso();
					activa1 = false;
					mov = 0;
				}
				// si no hay descanso, no hace nada
			}
			// Si me paso por la derecha y hay descanso, juntar las dos barras
			else if (tDescanso!= 0 && (h>horaDescanso.getHours()+tDescanso/60 ||
				h==horaDescanso.getHours()+tDescanso/60 &&
				m>horaDescanso.getMinutes()+tDescanso%60)) {
				quitarDescanso();
				activa1 = false;
				mov = 0;
			}
			// Si me paso por la derecha y no hay descanso, pegar al borde
			else if (tDescanso == 0 && (h>=horaCierre)) {
				horaSalida.setHours(horaCierre);
				horaSalida.setMinutes(0);
			}
			// Movimiento normal
			else {
				// Si hay descanso
				if (tDescanso!=0) {
				int dif = (horaDescanso.getHours()-h)*60 + horaDescanso.getMinutes()-m;
				tDescanso+=dif;
				horaDescanso.setHours(h);
				horaDescanso.setMinutes(m);
				}
				else {
					horaSalida.setHours(h);
					horaSalida.setMinutes(m);					
				}
			}
		}
		else if (activa2) {
			// Si me paso por la derecha, ajustar al borde
			if (h>=horaCierre) {
				horaSalida.setHours(horaCierre);
				horaSalida.setMinutes(0);
			}
			// Si me paso por la izquierda, eliminar franja
			else if (tDescanso!=0 && (
					t<=horaDescanso.getHours()*60 + horaDescanso.getMinutes()+tDescanso)) {
				horaSalida.setHours(horaDescanso.getHours());
				horaSalida.setMinutes(horaDescanso.getMinutes());
				quitarDescanso();
				activa2 = false;
				mov = 0;
			}
			// Movimiento normal
			else {
				horaSalida.setHours(h);
				horaSalida.setMinutes(m);
			}
		}
		recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
		return mov;
	}
	
	public int moverFranja(int x, int margenIzq, int margenNombres, int horaApertura, int horaCierre, int tamHora, int tamSubdiv, int numSubdiv) {
		int mov = 2;
		int h = dameHoraCursor(x, margenIzq, margenNombres, horaApertura, tamHora) - (despl*(60/numSubdiv))/60;
		int m = dameSubdivCursor(x, margenIzq, margenNombres, tamHora, tamSubdiv)*(60/numSubdiv) - (despl*(60/numSubdiv))%60;
		if (activa1) {
			// Despl es el número de subdivs desde horaEntrada hasta el cursor
			// Si hay descanso
			if (tDescanso!=0) {
				int tVuelta = horaDescanso.getHours()*60+ horaDescanso.getMinutes() + tDescanso;
				int tamFranja = horaDescanso.getHours()*60 + horaDescanso.getMinutes() - (
						horaEntrada.getHours()*60 + horaEntrada.getMinutes());
				// Me paso por la izquierda
				if (h<horaApertura) {
					horaEntrada.setHours(horaApertura);
					horaEntrada.setMinutes(0);
					horaDescanso.setHours(horaApertura + tamFranja/60);
					horaDescanso.setMinutes(tamFranja%60);
					tDescanso = tVuelta - (horaDescanso.getHours()*60+ horaDescanso.getMinutes());
				// Me paso por la derecha (falta)
				// Movimiento normal
				} else {
					horaEntrada.setHours(h);
					horaEntrada.setMinutes(m);
					horaDescanso.setHours(h + tamFranja/60);
					horaDescanso.setMinutes(m + tamFranja%60);
					tDescanso = tVuelta - (horaDescanso.getHours()*60+ horaDescanso.getMinutes());
				}
				// Si tDescanso resulta negativo, juntar las dos franjas
				if (tDescanso<=0) quitarDescanso();

			}
			// Si no hay descanso
			else {
				int tamFranja = horaSalida.getHours()*60 + horaSalida.getMinutes() - (
						horaEntrada.getHours()*60 + horaEntrada.getMinutes());
				horaSalida.setHours(h + tamFranja/60);
				horaSalida.setMinutes(m + tamFranja%60);
			}
		}
		else if (activa2) {
			// Despl es el número de subdivs desde horaVuelta hasta el cursor
			int tamFranja = horaSalida.getHours()*60 + horaSalida.getMinutes() - (
					horaDescanso.getHours()*60 + horaDescanso.getMinutes()+tDescanso);
			tDescanso = (h - horaDescanso.getHours())*60 + m - horaDescanso.getMinutes();
			// Si tDescanso resulta negativo, juntar las dos franjas
			horaSalida.setHours(horaDescanso.getHours() + tDescanso/60 + tamFranja/60);
			horaSalida.setMinutes(horaDescanso.getMinutes() + tDescanso%60 + tamFranja%60);
			if (tDescanso<=0) {
				quitarDescanso();
				activa2=false;
				activa1=true;
				calcularDespl(x, margenIzq, margenNombres, horaApertura, tamHora, tamSubdiv, numSubdiv);
			}
		}
		recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
		return mov;
	}
	
	public void quitarDescanso() {
		tDescanso=0;
		horaDescanso.setMinutes(0);
		horaDescanso.setHours(0);
	}
	
	public int dameHoraCursor(int x, int margenIzq, int margenNombres, int horaApertura, int tamHora) {
		int h = ((x-margenIzq-margenNombres)/tamHora)+horaApertura;
		if (h<0) h=0;
		return h;
	}
	
	public int dameSubdivCursor(int x, int margenIzq, int margenNombres, int tamHora, int tamSubdiv) {
		int m = ((x-margenIzq-margenNombres)%tamHora)/tamSubdiv;
		if (m<0) m=0;
		return m;
	}
}