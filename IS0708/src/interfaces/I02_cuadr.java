/*******************************************************************************
 * INTERFAZ I-02_cuadr :: Canvas de cuadrante
 *   por Daniel Dionne
 *   
 * Crea un canvas con un cuadrante editable.
 * Importa la clase cuadrante que es la que lo dibuja. Esta clase sólo se encarga
 * de los movimientos y pulsaciones del ratón.
 * 
 * ver 0.1
 *******************************************************************************/

// TODO Hacer que reduzca la resolución del grid en función del tamaño de la pantalla
package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import aplicacion.Empleado;
import aplicacion.Posicion;
import aplicacion.FranjaDib;

/**
 * Dada una instancia de Canvas, que se le pasa como parámetro al constructor,
 * crea un cuadrante sobre la misma.
 * @author Daniel
 *
 */
public class I02_cuadr {
	/* TODO
	 * Las barras de tamaño cero se quedan
	 * bug: al hacer muchas franjas pequeñitas, no se pegan bien (ver si sigue pasando)
	 */
	private Canvas canvas;
	private Cuadrante cuadrante;
	private int alto, ancho;
	private Display display;
	private ArrayList<Empleado> empleados;
	private int despl; // Este es para cuando movemos una barra, para saber de dónde la
				// he cogido
	private Boolean creando, terminadoDeCrear;
	// La variable terminadoDeCrear sirve para que una franja nueva no desaparezca al crearla
	private Boolean diario; // 1: muestra cuadrante diario, 0: muestra cuadrante mensual
	private int empleadoActivo;
	private int horaInicio, horaFin; // Definen de qué hora a qué hora es el
								// cuadrante

	private Image cuadranteImg;
	private final Point imgSize = new Point(800,800);
	private int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	private FranjaDib franjaActiva;
	private int movimiento;
	private final Label lGridCuadrante;
	private final Combo cGridCuadrante;
	
	private MouseListener mouseListenerCuadrSemanal;
	private MouseListener mouseListenerCuadrMensual;
	private MouseMoveListener mouseMoveListenerCuadrSemanal;
	private MouseMoveListener mouseMoveListenerCuadrMensual;

	private void calcularTamaño() {
		ancho = canvas.getClientArea().width;
		alto = canvas.getClientArea().height;
		cuadrante.setTamaño(ancho, alto);
	}

	private void redibujar() {
		// Redibuja sólo las franjas que corresponden, para evitar calculos
		// innecesarios
		// TODO ¿Merece la pena? Hay que ver si hay alguna diferencia en el rendimiento.
		// c.redraw(0, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),
		// ancho, 18, false);
		//c.redraw(0, 0, ancho, alto, false);
		canvas.redraw();
	}

	private void cursor(int i) {
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

	private void activarFranja(int franja, int mov) {
		franjaActiva = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(franja);
		franjaActiva.activarFranja();
		movimiento = mov;
		// Movimientos:
		// 0: Ninguno
		// 1: Mover inicio
		// 2: Desplazar
		// 3: Mover final
	}

	private void desactivarFranja() {
		if (franjaActiva!=null)
			franjaActiva.desactivarFranja();
		franjaActiva = null;
		movimiento = 0;
	}

	private int dameMovimiento() {
		return movimiento;
	}

	private FranjaDib dameFranjaActiva() {
		return franjaActiva;
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
				System.out.println(ex.code);
			}
			if (diario) cuadrante.dibujarCuadranteDia(gc2, empleadoActivo);
			else cuadrante.dibujarCuadranteMes(gc2);
			gc.drawImage(bufferImage, 0, 0);
			bufferImage.dispose();
		}
	}
	private void setSubdivisiones(int i) {
		cuadrante.subdivisiones = i;
		redibujar();
	}
	public void setDiario()  {
		diario = true;
		lGridCuadrante.setVisible(true);
		cGridCuadrante.setVisible(true);
		canvas.addMouseListener(mouseListenerCuadrSemanal);
		canvas.addMouseMoveListener(mouseMoveListenerCuadrSemanal);
		canvas.removeMouseListener(mouseListenerCuadrMensual);
		canvas.removeMouseMoveListener(mouseMoveListenerCuadrMensual);
		redibujar();}
	public void setMensual() {
		diario = false;
		lGridCuadrante.setVisible(false);
		cGridCuadrante.setVisible(false);
		canvas.addMouseListener(mouseListenerCuadrMensual);
		canvas.addMouseMoveListener(mouseMoveListenerCuadrMensual);
		canvas.removeMouseListener(mouseListenerCuadrSemanal);
		canvas.removeMouseMoveListener(mouseMoveListenerCuadrSemanal);
		redibujar();
	}
	/**
	 * Constructora que recibe como parámetro el Composite donde colocar los botones
	 * y el cuadrante.
	 * @param c	Composite sobre el que dibujar el cuadrante
	 */
	public I02_cuadr(Composite c, Boolean diario, ArrayList<Empleado> empleados) {
		this.diario = diario;
		this.empleados = empleados;
		final GridLayout l = new GridLayout(3,false);
		c.setLayout(l);
		
		final Label lCuadranteTitulo= new Label (c, SWT.LEFT);
		String fname = lCuadranteTitulo.getFont().getFontData()[0].getName();
		lCuadranteTitulo.setFont(new Font(c.getDisplay(),fname,15,0));
		// TODO Esto tendrá que cambiarse por la fecha elegida en el calendario
		lCuadranteTitulo.setText("12 de noviembre de 2007");
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

		this.canvas = new Canvas(c, SWT.FILL | SWT.NO_BACKGROUND);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
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
		horaInicio = 9;
		horaFin = 23;
		cuadrante = new Cuadrante(display, 4, horaInicio, horaFin, margenIzq, margenDer, margenSup, margenInf, margenNombres, empleados);
		calcularTamaño();
		display = canvas.getDisplay();
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				dibujarCuadrante(event.gc);
			}
		});
		canvas.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {}
			public void controlResized(ControlEvent e) {
				calcularTamaño();
			}
		});			
				
		mouseMoveListenerCuadrSemanal = new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				FranjaDib f = dameFranjaActiva();
				// Si acabo de apretar el botón para crear una franja, pero
				// todavía no he movido el ratón
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

					// Comprobar si la barra es de tamaño menor o igual que 0
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
					// Comprobar si la barra es de tamaño menor o igual que 0
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
				// comprobar si el cursor está en alguna franja, una por una
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
				// Botón derecho: Borra una franja (podría mostrar un menú si hace falta)
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
						// Si acabo de crear una franja, comprobar que no está
						// del revés, y si lo está, darle la vuelta
						// Comprobar también si se cruza con otra.
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
						// TODO que calcule el sticky en el que está
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

	public Boolean enAreaDibujo(int x, int y) {
		Boolean b = true;
		if (x < margenNombres + margenIzq)
			b = false;
		else if (x > ancho - margenDer)
			b = false;
		else if (y < margenSup)
			b = false;
		else if (y > alto - margenInf)
			b = false;
		return b;
	}
	/**
	 *  method sets printable image ( day view )
	 *  size of drowen cuadrante is set 
	 *  by imgSize variable
	 */
	
	public void ponImageDia(){
		cuadranteImg.dispose();
		cuadranteImg = new Image(this.display, imgSize.x, imgSize.y);
		GC gc1 = new GC(cuadranteImg);
		Cuadrante c = new Cuadrante(display,4,9,23,0,0,0,0,0,empleados);
		c.setTamaño(imgSize.x-10, imgSize.y-10);
		c.dibujarCuadranteDia(gc1,-1);	
		calcularTamaño();
		gc1.dispose();
	}
	
	/**
	 *  method sets printable image ( month view )
	 *  size of drowen cuadrante is set 
	 *  by imgSize variable
	 */
	
	public void ponImageMes(){
		cuadranteImg.dispose();
		cuadranteImg = new Image(this.display, imgSize.x, imgSize.y);
		GC gc2 = new GC(cuadranteImg);
		Cuadrante c = new Cuadrante(display,4,9,23,0,0,0,0,0,empleados);
		c.setTamaño(imgSize.x-10, imgSize.y-10);
		c.dibujarCuadranteMes(gc2);
		calcularTamaño();
		gc2.dispose();
		
	}
	
	/**
	 *  implementation of interface "impresion.Imprimible"
	 *  returns class private variable cuadranteImg - that
	 *  MUST be set by ponImageDia or ponImageMes
	 */
	
	public ImageData dameImageImprimible(){
		return this.cuadranteImg.getImageData();
	}
		
	
}
