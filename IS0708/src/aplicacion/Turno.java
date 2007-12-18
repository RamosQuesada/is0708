package aplicacion;

import java.sql.Time;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import aplicacion.FranjaDib;
import aplicacion.Posicion;

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
	private int tDescanso;//minutos de descanso
	public ArrayList<FranjaDib> franjas;
	
	
	/**
	 * Constructor por defecto
	 * @deprecated
	 */
	public Turno() {
		franjas = new ArrayList<FranjaDib>();	
	}
	/**
	 * 
	 * @param idTurno  		 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno 
	 * @param horaEntrada	 Hora te�rica a la que deber�a comenzar la jornada laboral del usuario 
	 * @param horaSalida	 Hora te�rica a la que deber�a terminar la jornada laboral del usuario
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
//	this.idTurno = idTurno;
//	this.descripcion = descripcion;
//	this.horaEntrada = horaEntrada;
//	this.horaSalida = horaSalida;
//	this.horaDescanso = horaDescanso;

	public void franjaNueva (Posicion pinicio, Posicion pfin) {
		FranjaDib f = new FranjaDib(pinicio, pfin);
		franjas.add(f);
	}
	public void quitarFranja (FranjaDib franja) {
		franjas.remove(franja);
	}
	public Boolean contienePunto (int y, int posV, int margenSup, int sep_vert_franjas, int alto_franjas) {
		Boolean b = false;
		if (y > margenSup+(sep_vert_franjas+alto_franjas)*(posV+1) && y<=margenSup+(sep_vert_franjas+alto_franjas)*(posV+2)) b = true;
		return b;
	}
	public void dibujarTurnoCuadranteSemanalJefe(Display display, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas) {
		int subDivs = 0;
		for (int i=0; i<franjas.size(); i++) {
			franjas.get(i).dibujarFranja(display, gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
			subDivs += (franjas.get(i).pfin.dameHora() - franjas.get(i).pinicio.dameHora())*12;
			subDivs += (franjas.get(i).pfin.dameCMin() - franjas.get(i).pinicio.dameCMin());
		}
	}
	/**
	 * Pega el valor x al m�s cercano dentro de la rejilla. El tama�o de la rejilla est� determinado
	 * por el n�mero de subdivisiones.
	 * @param x		El valor a ajustar
	 * @return		El valor ajustado a la rejilla
	 */
	public Posicion sticky (int x, int margenIzq, int tamHora, int subdivisiones, int horaInicio) {
		int y = x - margenIzq + (tamHora/subdivisiones)/2;
		Posicion p;
		if (((y%tamHora)/(tamHora/subdivisiones))>=subdivisiones)
			// Para evitar resultados del tipo 14:60
			p = new Posicion(1+y/tamHora+horaInicio,0);
		else
			// En otro caso, hay que tener en cuenta c�mo se dibuja el cuadrante para evitar
			// desfases entre las lineas horarias y las franjas.
			p = new Posicion(y/tamHora+horaInicio,((y%tamHora)/(tamHora/subdivisiones))*12/subdivisiones);
		return p;
	}

	private int alto;
	private int ancho;
	private int tamSep;
	private int tamSubsep;
	private Color color;
	/**
	 * Esta clase añade el interfaz gráfico de un turno a la representación del
	 * cuadrante.
	 * @param c			el composite donde va
	 * @param hApertura	cuándo se abre
	 * @param hCierre	cuándo se cierra
	 * @param s			el número de subdivisiones		
	 * @param p			si es la primera fila (entonces sólo pinta las horas)
	 */
	public void anadeGUI(Composite c, int hApertura, int hCierre, int s, boolean p, Color col) {
		color = col;
		final boolean primero = p;
		final Display display = c.getDisplay();
		final int horaApertura = hApertura;
		final int horaCierre = hCierre;
		final int margenSup;
		final int subdivisiones = s;
		final int margenDer = 5;
		final int margenIzq = 5;
		final Canvas cTurno = new Canvas(c,SWT.NONE | SWT.NO_BACKGROUND);
		final int h = horaCierre - horaApertura;
		final FranjaDib f1 = new FranjaDib(new Posicion(horaEntrada.getHours(),horaEntrada.getMinutes()),new Posicion(horaDescanso.getHours(),horaDescanso.getMinutes()));
		final FranjaDib f2 = new FranjaDib(new Posicion(horaDescanso.getHours()+tDescanso,horaDescanso.getMinutes()),new Posicion(horaSalida.getHours(),horaSalida.getMinutes()));
		
		if (primero) margenSup = 20;
		else margenSup = 0;
		cTurno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		cTurno.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				alto = cTurno.getSize().y;
				ancho = cTurno.getSize().x;
				tamSep = (ancho-10 - margenIzq - margenDer)/h;
				tamSubsep = tamSep/subdivisiones;
				Image bufferImage = new Image(display, cTurno.getSize().x, cTurno.getSize().y);
				GC gc2 = new GC(bufferImage);
				// TODO Probar la siguiente linea en el laboratorio
				try {
					gc2.setAntialias(SWT.ON);
				}
				catch (SWTException ex){
					System.out.println(ex.code);
				}
				gc2.setForeground(new Color(display, 40,80,40));
				
				for (int i=0; i<=h; i++) {
					// dibujar las lineas verticales para las horas
					gc2.setLineStyle(SWT.LINE_SOLID);
					gc2.setForeground(new Color(display,40,80,40));
					// si es el primero, pone los nombres de las horas arriba
					if (primero) {
						if (tamSep>14 && tamSep<=20) gc2.drawText(String.valueOf((horaApertura+i)%24),     margenIzq+i*tamSep-5, 0, true);
						else if (tamSep>20)     gc2.drawText(String.valueOf((horaApertura+i)%24)+'h', margenIzq+i*tamSep-5, 0, true);
					}
					else {
						gc2.drawLine(margenIzq+i*tamSep, margenSup, margenIzq+i*tamSep, alto);
						gc2.setForeground(new Color(display, 120,170,120));
						gc2.setLineStyle(SWT.LINE_DOT);
						if (i!=h)
							for (int j=1; j<subdivisiones; j++) {
								gc2.drawLine(margenIzq+i*tamSep+j*tamSubsep, margenSup, margenIzq+i*tamSep+j*tamSubsep, alto);
						}
						gc2.setLineStyle(SWT.LINE_SOLID);
						// Dibujar la franja 1
						
						f1.actualizarPixeles(margenIzq, 0, tamSep, tamSubsep, subdivisiones, horaApertura);
						f1.dibujarFranja(display, gc2, 5, color);					
						// Dibujar la franja 2
						
						f2.actualizarPixeles(margenIzq, 0, tamSep, tamSubsep, subdivisiones, horaApertura);
						f2.dibujarFranja(display, gc2, 5, new Color(display, 80,180,80));					
					}
				}
				gc2.setLineStyle(SWT.LINE_SOLID);
				e.gc.drawImage(bufferImage, 0, 0);
				bufferImage.dispose();
			}
		});
		// Añadir listener
		cTurno.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				FranjaDib f = f1;//dameFranjaActiva();
				// Si acabo de apretar el botón para crear una franja, pero
				// todavía no he movido el ratón
				/*
				if (creando) {
					Posicion p = cuadrante.sticky(e.x);
					FranjaDib nuevaFranja = new FranjaDib(p, p);
					cuadrante.empleados.get(empleadoActivo).turno.franjas.add(nuevaFranja);
					activarFranja(cuadrante.empleados.get(empleadoActivo).turno.franjas.size() - 1, 3);
					creando = false;
					terminadoDeCrear = false;
				}
				// Si estoy moviendo una franja
				else*/ //if (dameMovimiento() == 2) {
					
					Posicion ancho = f.pfin.diferencia(f.pinicio);
					f.pinicio = sticky(e.x, margenIzq, tamSep, subdivisiones,horaApertura);
					// System.out.println(String.valueOf(f.pinicio.hora)+"-"+String.valueOf(f.pinicio.cmin));
					f.pfin.suma(f.pinicio, ancho);
					f.pegarALosBordes(horaApertura, horaCierre);
					f.actualizarPixeles(margenIzq, 0, tamSep, tamSubsep, subdivisiones, horaApertura);
					int j = 0;
					FranjaDib f2;
					Boolean encontrado2 = false;
					
					/*
					while (!encontrado2	&& j < cuadrante.empleados.get(empleadoActivo).turno.franjas.size()) {
						f2 = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(j);
						if ((f.pinicio.menorOIgualQue(f2.pfin) && f2.contienePixel(f.inicio - 10)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2 = true;
							Posicion ancho2 = f2.pfin.diferencia(f2.pinicio);
							f.pinicio = f2.pinicio;
							ancho2.suma(ancho, ancho2);
							f.pfin.suma(f.pinicio, ancho2);
							f.inicio = f2.inicio;
							despl += (f2.fin - f2.inicio);
							cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(j);
							f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
						} else if ((f.pfin.mayorOIgualQue(f2.pinicio) && f2.contienePixel(f.fin + 10))	| (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2 = true;
							f.pfin = f2.pfin;
							cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(j);
							f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
						}
						j++;
					}
					redibujar();
					
				}*/
				// Si estoy cambiando el inicio de una franja
				/*
				else if (dameMovimiento() == 1) {
					f.inicio = e.x;
					f.pinicio = cuadrante.sticky(f.inicio);
					// Comprobar si toco el borde izquierdo
					if (f.pinicio.dameHora() < horaInicio	|| f.pinicio.dameCMin() < 0) {
						f.pinicio.ponHora(horaInicio);
						f.pinicio.ponCMin(0);
					}
					f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);

					// Comprobar si la barra es de tama�o menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(f);
						cursor(0);
					} else {
						// Comprobar contacto con otras franjas
						int j = 0;
						FranjaDib f2;
						Boolean encontrado2 = false;
						while (!encontrado2 && j < cuadrante.empleados.get(empleadoActivo).turno.franjas.size()) {
							f2 = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(j);
							if (f != f2 && ((f.pinicio.menorOIgualQue(f2.pfin) && f2.contienePixel(e.x - 10)) | (f.inicio <= f2.inicio && f.fin >= f2.fin))) {
								encontrado2 = true;
								f.pinicio = f2.pinicio;
								cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(j);
								desactivarFranja();
								f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
							}
							j++;
						}
						redibujar();
					}
				}
				
				// Si estoy cambiando el fin de una franja
				else if (dameMovimiento() == 3) {
					f.fin = e.x;
					f.pfin = cuadrante.sticky(f.fin);
					// Comprobar si toco el borde derecho
					if (f.pfin.dameHora() > horaFin || (f.pfin.dameHora() == horaFin && f.pfin.dameCMin() > 0)) {
						f.pfin.ponHora(horaFin);
						f.pfin.ponCMin(0);
					}
					f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
					// Comprobar si la barra es de tama�o menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						cuadrante.empleados.get(empleadoActivo).turno.franjas
								.remove(f);
						cursor(0);
					} else {
						// Comprobar contacto con otras franjas
						int j = 0;
						FranjaDib f2;
						Boolean encontrado2 = false;
						while (terminadoDeCrear	&& !encontrado2	&& j < cuadrante.empleados.get(empleadoActivo).turno.franjas.size()) {
							f2 = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(j);
							if (f != f2	&& (f.pfin.mayorOIgualQue(f2.pinicio) && f2.contienePixel(e.x + 10)) | (f.inicio <= f2.inicio && f.fin >= f2.fin)) {
								encontrado2 = true;
								f.pfin = f2.pfin;
								cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(j);
								desactivarFranja();
								f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
							}
							j++;
						}
					}
					redibujar();
				}
				// Si no estoy moviendo ninguna franja,
				// comprobar si el cursor est� en alguna franja, una por una
				else {
					// Comprueba el empleado activo (vertical)
					int i = 0;
					Boolean encontrado = false;
					int empleadoActivoNuevo = -1;
					// Seleccionar empleado activo
					while (!encontrado && i < cuadrante.empleados.size()) { 
						if (cuadrante.empleados.get(i).turno.contienePunto(e.y, i,margenSup,cuadrante.sep_vert_franjas,cuadrante.alto_franjas))
							empleadoActivoNuevo = i;
						i++;
					}
					Boolean redibujar = false;
					if (empleadoActivoNuevo != empleadoActivo) {
						empleadoActivo = empleadoActivoNuevo;
						redibujar = true;

					}
					// Comprueba la franja activa (horizontal)
					i = 0;
					encontrado = false;
					if (franjaActiva!=null)	franjaActiva.desactivarFranja();
					franjaActiva = null;
					while (empleadoActivo != -1 && !encontrado && i < cuadrante.empleados.get(empleadoActivo).turno.franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(i);
						if 		(f.contienePixelInt(e.x)) 	{ cursor(1); encontrado = true; franjaActiva = f; f.activarFranja(); redibujar=true;}
						else if (f.tocaLadoIzquierdo(e.x)) { cursor(2); encontrado = true; franjaActiva = f; f.activarFranja(); redibujar=true;}
						else if (f.tocaLadoDerecho(e.x)) 	{ cursor(2); encontrado = true; franjaActiva = f; f.activarFranja(); redibujar=true;}
						else									  cursor(0);
						i++;
					}
					if (redibujar) redibujar();
				
				}
				*/
				cTurno.redraw();
			}
		});
	}
	
	private void cursor(Canvas canvas, int i) {
		switch (i) {
		case 1:
			canvas.setCursor(new Cursor(canvas.getDisplay(), SWT.CURSOR_HAND));
			break;
		case 2:
			canvas.setCursor(new Cursor(canvas.getDisplay(), SWT.CURSOR_SIZEE));
			break;
		default:
			canvas.setCursor(new Cursor(canvas.getDisplay(), SWT.CURSOR_ARROW));
			break;
		}

	}

}