package interfaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import aplicacion.Empleado;
import aplicacion.FranjaDib;
import aplicacion.Posicion;
import aplicacion.Turno;
import aplicacion.Vista;

/**
 * Esta clase extiende la clase cuadrante para que se pueda:
 * 1. dibujar sobre un GC
 * 2. representar y modificar sobre un canvas
 * @author Daniel Dionne
 *
 */
public class I_Cuadrante extends algoritmo.Cuadrante { // implements aplicacion.Drawable {
	private Display display;
	private int ancho, alto;
	private int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	public int alto_franjas = 15;
	public int sep_vert_franjas = 10;
	private int horaApertura, horaFin; // Definen de qué hora a qué hora es el cuadrante
	public int tamHora, tamSubdiv;
	public int subdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	private Vista vista;

	/* TODO
	 * Las barras de tamaño cero se quedan
	 * bug: al hacer muchas franjas pequeñitas, no se pegan bien (ver si sigue pasando)
	 */
//	private Canvas canvas;
	private Composite c;
	// La variable terminadoDeCrear sirve para que una franja nueva no desaparezca al crearla
	private Boolean diario = true; // 1: muestra cuadrante diario, 0: muestra cuadrante mensual
	private int empleadoActivo;

	private Point imgSize = new Point(800,800);
	private  Label lGridCuadrante;
	private Combo cGridCuadrante;
	
	private int dia = 1;
	
	
	private Image cuadranteImg;
	private FranjaDib franjaActiva;
	private int movimiento;	
	private int despl; // Este es para cuando movemos una barra, para saber de d�nde la
	// he cogido
	private Boolean creando, terminadoDeCrear;

	private MouseListener mouseListenerCuadrSemanal;
	private MouseListener mouseListenerCuadrMensual;
	private MouseMoveListener mouseMoveListenerCuadrSemanal;
	private MouseMoveListener mouseMoveListenerCuadrMensual;
	
	public I_Cuadrante(Vista vista, int mes, int anio, String idDepartamento) {
		super(mes, anio, idDepartamento);
		this.vista = vista;
	}
	
	public void setComposite(Composite c) {
		// Preparar el canvas
		this.c = c;
//		this.canvas = new Canvas(c, SWT.FILL | SWT.NO_BACKGROUND);
//		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		final GridLayout l = new GridLayout(3,false);
		c.setLayout(l);
		cuadranteImg = new Image(display, imgSize.x, imgSize.y);
		final Label lCuadranteTitulo= new Label (c, SWT.LEFT);
		String fname = lCuadranteTitulo.getFont().getFontData()[0].getName();
		lCuadranteTitulo.setFont(new Font(c.getDisplay(),fname,15,0));
		//String sFecha = fecha.getDate() + " de " + fecha.getMonth() + " de " + fecha.getYear(); 
		//lCuadranteTitulo.setText(sFecha);
		lCuadranteTitulo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		lGridCuadrante= new Label (c, SWT.LEFT);
		lGridCuadrante.setText("Mostrar intervalos de");
		lGridCuadrante.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		cGridCuadrante = new Combo(c, SWT.BORDER | SWT.READ_ONLY);
		cGridCuadrante.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		cGridCuadrante.setItems(new String[] {"5 min", "10 min", "15 min", "30 min", "1 hora"});
		cGridCuadrante.select(2);
		
		cGridCuadrante.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e){
				switch (cGridCuadrante.getSelectionIndex()) {
				case 0 : setSubdivisiones(12); break;
				case 1 : setSubdivisiones(6); break;
				case 2 : setSubdivisiones(4); break;
				case 3 : setSubdivisiones(2); break;
				case 4 : setSubdivisiones(1);
				}
			}
		});

		// Inicializar algunas variables
		creando = false;
		terminadoDeCrear = true;
		franjaActiva = null;
		movimiento = 0;
		margenIzq = 15;
		margenDer = 20;
		margenSup = 1;
		margenInf = 10;
		margenNombres = 90;
		empleadoActivo = -1;
		horaApertura = 9;
		horaFin = 23;
		
		calcularTamano();
//		display = canvas.getDisplay();
//		canvas.addPaintListener(new PaintListener() {
//			public void paintControl(PaintEvent event) {
//				dibujarCuadrante(event.gc);
//			}
//		});
//		canvas.addControlListener(new ControlListener() {
//			public void controlMoved(ControlEvent e) {}
//			public void controlResized(ControlEvent e) {
//				calcularTamano();
//			}
//		});
	}
	
	private void dibujarCuadrante(GC gc) {
		// Doble buffering para evitar parpadeo
		if (ancho != 0 && alto != 0) {
			Image bufferImage = new Image(display, ancho, alto);
			GC gc2 = new GC(bufferImage);
			// TODO Probar la siguiente linea en el laboratorio
			try {
				gc2.setAntialias(SWT.ON);
			}
			catch (SWTException ex){
				System.err.println(ex.code);
			}
			if (diario) dibujarCuadranteDia(display, gc2, empleadoActivo);
			else dibujarCuadranteMes(gc2);
			gc.drawImage(bufferImage, 0, 0);
			bufferImage.dispose();
		}
	}
	
	private void setSubdivisiones(int i) {
		subdivisiones = i;
		//redibujar();
	}
	
	public void setDia(int i) {
		dia = i;
	}
	
	private void calcularTamano() {
		ancho = c.getClientArea().width;
		alto = c.getClientArea().height;
	}
	
	private void redibujar() {
		// Redibuja sólo las franjas que corresponden, para evitar calculos
		// innecesarios
		// TODO ¿Merece la pena? Hay que ver si hay alguna diferencia en el rendimiento.
		// c.redraw(0, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),
		// ancho, 18, false);
		// c.redraw(0, 0, ancho, alto, false);
		//canvas.redraw();
	}


	/*
	public I02_cuadr(Composite c, Boolean diario, ArrayList<Empleado> empleados, Cuadrante cuadrante) {
		this.diario = diario;
		this.empleados = empleados;
		

		
			
				
		mouseMoveListenerCuadrSemanal = new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				FranjaDib f = dameFranjaActiva();
				// Si acabo de apretar el bot�n para crear una franja, pero
				// todav�a no he movido el rat�n
				if (creando && empleadoActivo != -1) {
					Posicion p = cuadrante.sticky(e.x);
					FranjaDib nuevaFranja = new FranjaDib(p, p);
					cuadrante.empleados.get(empleadoActivo).turno.franjas.add(nuevaFranja);
					activarFranja(cuadrante.empleados.get(empleadoActivo).turno.franjas.size() - 1, 3);
					creando = false;
					terminadoDeCrear = false;
				}
				// Si estoy moviendo una franja
				else if (dameMovimiento() == 2) {
					Posicion ancho = f.pfin.diferencia(f.pinicio);
					f.pinicio = cuadrante.sticky(e.x - despl);
					// System.out.println(String.valueOf(f.pinicio.hora)+"-"+String.valueOf(f.pinicio.cmin));
					f.pfin.suma(f.pinicio, ancho);
					f.pegarALosBordes(horaInicio, horaFin);
					f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
					int j = 0;
					FranjaDib f2;
					Boolean encontrado2 = false;
					while (!encontrado2	&& j < cuadrante.empleados.get(empleadoActivo).turno.franjas.size()) {
						f2 = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(j);
						if ((f.pinicio.menorOIgualQue(f2.pfin) && f2.contienePixel(f.inicio - 10,0)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2 = true;
							Posicion ancho2 = f2.pfin.diferencia(f2.pinicio);
							f.pinicio = f2.pinicio;
							ancho2.suma(ancho, ancho2);
							f.pfin.suma(f.pinicio, ancho2);
							f.inicio = f2.inicio;
							despl += (f2.fin - f2.inicio);
							cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(j);
							f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
						} else if ((f.pfin.mayorOIgualQue(f2.pinicio) && f2.contienePixel(f.fin + 10,0))	| (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2 = true;
							f.pfin = f2.pfin;
							cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(j);
							f.actualizarPixeles(margenIzq, margenNombres, cuadrante.tamHora, cuadrante.tamSubdiv, cuadrante.subdivisiones, horaInicio);
						}
						j++;
					}
					redibujar();
				}
				// Si estoy cambiando el inicio de una franja
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
							if (f != f2 && ((f.pinicio.menorOIgualQue(f2.pfin) && f2.contienePixel(e.x - 10,0)) | (f.inicio <= f2.inicio && f.fin >= f2.fin))) {
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
							if (f != f2	&& (f.pfin.mayorOIgualQue(f2.pinicio) && f2.contienePixel(e.x + 10,0)) | (f.inicio <= f2.inicio && f.fin >= f2.fin)) {
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
			}
		};
		
		mouseListenerCuadrSemanal = new MouseListener() {
			public void mouseDown(MouseEvent e) {
				// Bot�n derecho: Borra una franja (podr�a mostrar un men� si hace falta)
				if (empleadoActivo!=-1 && e.button == 3) {
					int i = 0;
					FranjaDib f;
					Boolean encontrado = false;
					while (!encontrado && i < cuadrante.empleados.get(empleadoActivo).turno.franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(i);
						if (f.contienePixelInt(e.x)) {
							cuadrante.empleados.get(empleadoActivo).turno.franjas.remove(f);
							redibujar();
							encontrado = true;
						}
						i++;
					}
				} else {
					FranjaDib f;
					int i = 0;
					Boolean encontrado = false;
					while (empleadoActivo != -1 && !encontrado && i < cuadrante.empleados.get(empleadoActivo).turno.franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(i);
						if (f.contienePixelInt(e.x)) {
							encontrado = true;
							activarFranja(i, 2);
							despl = e.x - f.inicio;
						} else if (f.tocaLadoIzquierdo(e.x)) {
							encontrado = true;
							activarFranja(i, 1);
						} else if (f.tocaLadoDerecho(e.x)) {
							encontrado = true;
							activarFranja(i, 3);
						}
						i++;
					}
					if (!encontrado && enAreaDibujo(e.x, e.y) && empleadoActivo != -1)
						creando = true;
				}
			}
			public void mouseUp(MouseEvent e) {
				FranjaDib f;
				redibujar();
				desactivarFranja();
				if (empleadoActivo != -1) {
					for (int i = 0; i < cuadrante.empleados.get(empleadoActivo).turno.franjas
							.size(); i++) {
						f = cuadrante.empleados.get(empleadoActivo).turno.franjas
								.get(i);
						// Si acabo de crear una franja, comprobar que no est�
						// del rev�s, y si lo est�, darle la vuelta
						// Comprobar tambi�n si se cruza con otra.
						if (!terminadoDeCrear) {
							if (f.inicio > f.fin) {
								int aux = f.inicio;
								f.inicio = f.fin;
								f.fin = aux;
							}
							FranjaDib f2;
							int j = 0;
							Boolean encontrado = false;
							while (!encontrado
									&& j < cuadrante.empleados
											.get(empleadoActivo).turno.franjas.size()) {
								f2 = cuadrante.empleados.get(empleadoActivo).turno.franjas
										.get(j);
								if (f2.contienePixelInt(f.inicio)
										|| f2.contienePixelInt(f.fin)) {
									// Juntar dos franjas que se tocan o cruzan
									if (f2.inicio < f.inicio)
										f.inicio = f2.inicio;
									if (f2.fin > f2.fin)
										f.fin = f2.fin;
									cuadrante.empleados.get(empleadoActivo).turno.franjas
											.remove(j);
									redibujar();
								}
								j++;
							}
						}
					}
				}
				terminadoDeCrear = true;
			}
			public void mouseDoubleClick(MouseEvent e) {
				int i = 0;
				FranjaDib f;
				Boolean encontrado = false;
				while (!encontrado
						&& i < cuadrante.empleados.get(empleadoActivo).turno.franjas
								.size()) {
					f = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(i);
					if (f.contienePixelInt(e.x)) {
						f = cuadrante.empleados.get(empleadoActivo).turno.franjas
								.get(i);
						// TODO que calcule el sticky en el que est�
						// Franja f2 = cuadrante.new Franja (f.inicio, e.x-10);
						// f.inicio=e.x+10;
						// cuadrante.empleados.get(empleadoActivo).franjas.add(f2);
						redibujar();
						encontrado = true;
					}
					i++;
				}
			}
		};
		mouseListenerCuadrMensual = new MouseListener() {
			public void mouseDown(MouseEvent e){};
			public void mouseUp(MouseEvent e){};
			public void mouseDoubleClick(MouseEvent e){};
		};
		mouseMoveListenerCuadrMensual = new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				
			}
		};
		if (diario) setDiario(); else setMensual();
	}
	
	*/
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
	 * @param horaApertura	Hora de inicio del cuadrante
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
	
	public void setMargenes(int margenIzq, int margenDer, int margenSup, int margenInf, int margenNombres) {
		this.margenIzq  = margenIzq;
		this.margenDer  = margenDer;
		this.margenSup  = margenSup;
		this.margenInf  = margenInf;
		this.margenNombres  = margenNombres;	
	}
	
	public void setConfig(int subdivisiones, int horaInicio, int horaFin) {
		this.horaApertura = horaInicio;
		this.horaFin = horaFin;
		this.subdivisiones = subdivisiones;	
	}
	

	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujará el cuadrante.
	 * @param empleadoActivo	La posición del empleado a resaltar en la lista de empleados.
	 */
	// TODO Debería lanzar una excepción si empleadoActivo > empleados.size
	public void dibujarCuadranteDia(Display d, GC gc, int empleadoActivo) {
		dibujarSeleccion(gc, empleadoActivo);
		dibujarHoras(gc);
		dibujarTurnos(gc);
	}
	
	public void dibujarTurnos(GC gc) {
		if (vista.getEmpleados().size()==0) {
			gc.drawText("Cargando...", 5, 5);
		}
		else {
			int idTurno;
			Turno turno = new Turno(1,"pi","14:00:00","19:00:00","16:00:00",1);
			
			for (int i=0; i<cuad[dia].size(); i++) {
				idTurno = cuad[dia].get(i).getIdTurno();
				//turno = vista.getTurno(idTurno);
				turno.anadeGUI(c,horaApertura,horaFin,subdivisiones,false,new Color(display,200,200,200));//vista.getEmpleados().get(i).dameColor());
			}
			
			//for (int i=0; i<vista.getEmpleados().size(); i++) {
			//	vista.getEmpleados().get(i).dibujarTurno(display, gc, i, vista.getEmpleados().get(i).dameColor(),margenIzq, margenNombres,margenSup,sep_vert_franjas,alto_franjas);
			//}
		}
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

		for (int i=0; i < vista.getEmpleados().size(); i++) {
			gc.drawText(vista.getEmpleados().get(i).getNombre(), margenIzq, margenSup + 20 + i*altoFila);
			for (int j=0; j < ultimoDia; j++) {
				gc.drawRectangle(margenIzq + margenNombres + j*anchoDia, margenSup + 20 + i*altoFila, anchoDia, altoFila);
			}
		}
			
		// Esto es para un calendario normal
		int altoMes = alto - margenSup - margenInf;
		int numSemanas = 5;
		int altoDia = alto/numSemanas;
		
	}
	
	/**
	 * Dibuja lineas verticales representando las horas y las subdivisiones del cuadrante.
	 * @param gc	El GC del display sobre el que se dibujar� el cuadrante.
	 */
	private void dibujarHoras(GC gc) {
		gc.setForeground(new Color(display, 40,80,40));
		int m = margenIzq + margenNombres;
		int h = horaFin - horaApertura;
		int sep = (ancho - m - margenDer)/h;
		int subsep = sep/subdivisiones;
		for (int i=0; i<=h; i++) {
			gc.setLineStyle(SWT.LINE_SOLID);
			gc.setForeground(new Color(display,40,80,40));
			if (sep>14 && sep<=20) gc.drawText(String.valueOf((horaApertura+i)%24),     m+i*sep-5, margenSup, true);
			else if (sep>20)     gc.drawText(String.valueOf((horaApertura+i)%24)+'h', m+i*sep-5, margenSup, true);
			gc.drawLine(m+i*sep, 20+margenSup, m+i*sep, alto-margenInf);
			gc.setForeground(new Color(display, 120,170,120));
			gc.setLineStyle(SWT.LINE_DOT);
			if (i!=h)
				for (int j=1; j<subdivisiones; j++) {
					gc.drawLine(m+i*sep+j*subsep, 20+margenSup+5, m+i*sep+j*subsep, alto-margenInf-5);
			}
		}
		gc.setLineStyle(SWT.LINE_SOLID);
	}
	/**
	 * Dibuja un fondo distinguido para el empleado seleccionado, basado en el color del empleado
	 * pero m�s p�lido.
	 * @param gc	El GC sobre el que resaltar el empleado
	 * @param emp	La posici�n del empleado a resaltar en la lista de empleados. Se considera
	 * 				que -1 significa que no hay ning�n empleado seleccionado.
	 */
	// TODO Lanzar excepción si emp > empleados.size
	private void dibujarSeleccion (GC gc, int emp) {
		if (emp!=-1) {
			gc.setForeground(new Color(display, 255-(255-vista.getEmpleados().get(emp).dameColor().getRed())/5, 255-(255-vista.getEmpleados().get(emp).dameColor().getGreen())/5, 255-(255-vista.getEmpleados().get(emp).dameColor().getBlue())/5));
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
			p = new Posicion(1+y/tamHora+horaApertura,0);
		else
			// En otro caso, hay que tener en cuenta c�mo se dibuja el cuadrante para evitar
			// desfases entre las lineas horarias y las franjas.
			p = new Posicion(y/tamHora+horaApertura,((y%tamHora)/(tamHora/subdivisiones))*12/subdivisiones);
		return p;
	}
	/**
	 * Actualiza el tamaño del cuadrante, el tamaño de las horas y las subdivisiones, y para cada
	 * franja, actualiza sus píxeles inicial y final en función de sus valores pinicio y pfin.
	 * @param ancho	El ancho nuevo, en píxeles
	 * @param alto	El alto nuevo, en píxeles
	 */
	public void setTamano(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaFin-horaApertura);
		tamSubdiv = tamHora/12;
//		for (int i=0; i < vista.getEmpleados().size(); i++) {
//			Empleado e = vista.getEmpleados().get(i);
//			for (int j=0; j < e.turno.franjas.size(); j++) {
//				FranjaDib f = e.turno.franjas.get(j);
//				f.actualizarPixeles(margenIzq, margenNombres, tamHora, tamSubdiv, subdivisiones, horaInicio);
//			}
//		}
	}

/*
	public ImageData getDrawableImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageData getPrintableImage(Display display, ResourceBundle bundle,
			boolean bn) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
}
