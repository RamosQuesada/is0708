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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import interfaces.Cuadrante.*;

public class I02_cuadr {
	/*
	 * Tareas: - mostrar nombres de los empleados - resaltar el empleado
	 * seleccionado en el modo edición - bug: al hacer muchas franjas
	 * pequeñitas, no se pegan bien (y no tiene que ver con el sticky)
	 */
	Composite c;
	Cuadrante cuadrante;
	int alto, ancho;
	Display display;
	int despl; // Este es para cuando movemos una barra, para saber de dónde la
				// he cogido
	Color fg;
	Boolean creando, terminadoDeCrear; // La variable terminadoDeCrear sirve
										// para que una franja nueva no
										// desaparezca al crearla
	int empleadoActivo;
	int horaInicio, horaFin; // Definen de qué hora a qué hora es el
								// cuadrante

	int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	int margenNombres; // Un margen para pintar los nombres a la izquierda
	Franja franjaActiva;
	int subdivisiones;
	int movimiento;

	private void calcularTamaño() {
		ancho = c.getClientArea().width;
		alto = c.getClientArea().height;
		cuadrante.setTamaño(ancho, alto);
	}

	public void redibujar(Franja f, int posV) {
		// Redibuja sólo las franjas que corresponden, para evitar calculos
		// innecesarios
		// TODO ¿Merece la pena? Hay que ver si hay alguna diferencia en el rendimiento.
		// c.redraw(0, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),
		// ancho, 18, false);
		c.redraw(0, 0, ancho, alto, false);
	}

	public void cursor(int i) {
		switch (i) {
		case 1:
			c.setCursor(new Cursor(c.getDisplay(), SWT.CURSOR_HAND));
			break;
		case 2:
			c.setCursor(new Cursor(c.getDisplay(), SWT.CURSOR_SIZEE));
			break;
		default:
			c.setCursor(new Cursor(c.getDisplay(), SWT.CURSOR_ARROW));
			break;
		}

	}

	public void activarFranja(int franja, int mov) {
		franjaActiva = cuadrante.empleados.get(empleadoActivo).franjas
				.get(franja);
		franjaActiva.activarFranja();
		movimiento = mov;
		// Movimientos:
		// 0: Ninguno
		// 1: Mover inicio
		// 2: Desplazar
		// 3: Mover final
	}

	public void desactivarFranja() {
		if (franjaActiva!=null)
			franjaActiva.desactivarFranja();
		franjaActiva = null;
		movimiento = 0;
	}

	public int dameMovimiento() {
		return movimiento;
	}

	public Franja dameFranjaActiva() {
		return franjaActiva;
	}

	public void dibujarCuadrante(GC gc) {
		// Doble buffering para evitar parpadeo
		if (ancho != 0 && alto != 0) {
			Image bufferImage = new Image(display, ancho, alto);
			GC gc2 = new GC(bufferImage);
			gc2.setAntialias(SWT.ON);
			cuadrante.dibujarCuadrante(gc2, empleadoActivo);
			gc.drawImage(bufferImage, 0, 0);
			bufferImage.dispose();
		}
	}

	public I02_cuadr(Canvas c) {

		this.c = c;
		creando = false;
		terminadoDeCrear = true;

		franjaActiva = null;
		movimiento = 0;

		margenIzq = 20;
		margenDer = 20;
		margenSup = 20;
		margenInf = 20;
		margenNombres = 100;
		subdivisiones = 6;
		empleadoActivo = -1;
		horaInicio = 9;
		horaFin = 23;
		cuadrante = new Cuadrante(display, subdivisiones, horaInicio, horaFin);
		calcularTamaño();
		display = c.getDisplay();
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				dibujarCuadrante(event.gc);
			}
		});
		c.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}

			public void controlResized(ControlEvent e) {
				calcularTamaño();
			}
		});
		c.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				Franja f = dameFranjaActiva();
				// Si acabo de apretar el botón para crear una franja, pero
				// todavía no he movido el ratón
				if (creando && empleadoActivo != -1) {
					Posicion p = cuadrante.sticky2(e.x);
					Franja nuevaFranja = cuadrante.new Franja(p, p);
					cuadrante.empleados.get(empleadoActivo).franjas
							.add(nuevaFranja);
					activarFranja(cuadrante.empleados.get(empleadoActivo).franjas.size() - 1, 3);
					creando = false;
					terminadoDeCrear = false;
				}
				// Si estoy moviendo una franja
				else if (dameMovimiento() == 2) {
					Posicion ancho = f.pfin.diferencia(f.pinicio);
					f.pinicio = cuadrante.sticky2(e.x - despl);
					// System.out.println(String.valueOf(f.pinicio.hora)+"-"+String.valueOf(f.pinicio.subdivision));
					f.pfin.suma(f.pinicio, ancho);
					f.pegarALosBordes();
					f.actualizarPixeles();
					int j = 0;
					Franja f2;
					Boolean encontrado2 = false;
					while (!encontrado2
							&& j < cuadrante.empleados.get(empleadoActivo).franjas
									.size()) {
						f2 = cuadrante.empleados.get(empleadoActivo).franjas
								.get(j);
						if ((f.pinicio.menorOIgualQue(f2.pfin) && f2
								.contienePunto(f.inicio - 10))
								| (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2 = true;
							Posicion ancho2 = f2.pfin.diferencia(f2.pinicio);
							f.pinicio = f2.pinicio;
							ancho2.suma(ancho, ancho2);
							f.pfin.suma(f.pinicio, ancho2);
							f.inicio = f2.inicio;
							despl += (f2.fin - f2.inicio);
							cuadrante.empleados.get(empleadoActivo).franjas
									.remove(j);
							f.actualizarPixeles();
						} else if ((f.pfin.mayorOIgualQue(f2.pinicio) && f2
								.contienePunto(f.fin + 10))
								| (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2 = true;
							f.pfin = f2.pfin;
							cuadrante.empleados.get(empleadoActivo).franjas
									.remove(j);
							f.actualizarPixeles();
						}
						j++;
					}
					redibujar(f, empleadoActivo);
				}
				// Si estoy cambiando el inicio de una franja
				else if (dameMovimiento() == 1) {
					f.inicio = e.x;
					f.pinicio = cuadrante.sticky2(f.inicio);
					// Comprobar si toco el borde izquierdo
					if (f.pinicio.hora < horaInicio
							|| f.pinicio.subdivision < 0) {
						f.pinicio.hora = horaInicio;
						f.pinicio.subdivision = 0;
					}
					f.actualizarPixeles();

					// Comprobar si la barra es de tamaño menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						cuadrante.empleados.get(empleadoActivo).franjas
								.remove(f);
						cursor(0);
					} else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2;
						Boolean encontrado2 = false;
						while (!encontrado2
								&& j < cuadrante.empleados.get(empleadoActivo).franjas
										.size()) {
							f2 = cuadrante.empleados.get(empleadoActivo).franjas
									.get(j);
							if (f != f2
									&& ((f.pinicio.menorOIgualQue(f2.pfin) && f2
											.contienePunto(e.x - 10)) | (f.inicio <= f2.inicio && f.fin >= f2.fin))) {
								encontrado2 = true;
								f.pinicio = f2.pinicio;
								cuadrante.empleados.get(empleadoActivo).franjas
										.remove(j);
								desactivarFranja();
								f.actualizarPixeles();
							}
							j++;
						}
						redibujar(f, empleadoActivo);
					}
				}
				// Si estoy cambiando el fin de una franja
				else if (dameMovimiento() == 3) {
					f.fin = e.x;
					f.pfin = cuadrante.sticky2(f.fin);
					// Comprobar si toco el borde derecho
					if (f.pfin.hora > horaFin
							|| (f.pfin.hora == horaFin && f.pfin.subdivision > 0)) {
						f.pfin.hora = horaFin;
						f.pfin.subdivision = 0;
					}
					f.actualizarPixeles();
					// Comprobar si la barra es de tamaño menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						cuadrante.empleados.get(empleadoActivo).franjas
								.remove(f);
						cursor(0);
					} else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2;
						Boolean encontrado2 = false;
						while (terminadoDeCrear
								&& !encontrado2
								&& j < cuadrante.empleados.get(empleadoActivo).franjas
										.size()) {
							f2 = cuadrante.empleados.get(empleadoActivo).franjas
									.get(j);
							if (f != f2
									&& (f.pfin.mayorOIgualQue(f2.pinicio) && f2
											.contienePunto(e.x + 10))
									| (f.inicio <= f2.inicio && f.fin >= f2.fin)) {
								encontrado2 = true;
								f.pfin = f2.pfin;
								cuadrante.empleados.get(empleadoActivo).franjas
										.remove(j);
								desactivarFranja();
								f.actualizarPixeles();
							}
							j++;
						}
					}
					redibujar(f, empleadoActivo);
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
						if (cuadrante.empleados.get(i).contienePunto(e.y, i))
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
					while (empleadoActivo != -1 && !encontrado && i < cuadrante.empleados.get(empleadoActivo).franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
						if 		(f.contienePuntoInt(e.x, e.y)) 	{ cursor(1); encontrado = true; franjaActiva = f; f.activarFranja(); redibujar=true;}
						else if (f.tocaLadoIzquierdo(e.x, e.y)) { cursor(2); encontrado = true; franjaActiva = f; f.activarFranja(); redibujar=true;}
						else if (f.tocaLadoDerecho(e.x, e.y)) 	{ cursor(2); encontrado = true; franjaActiva = f; f.activarFranja(); redibujar=true;}
						else									  cursor(0);
						i++;
					}
					if (redibujar) redibujar(f, empleadoActivo);
				}
			}
		});
		c.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				// Botón derecho: Borra una franja (podría mostrar un menú si hace falta)
				if (empleadoActivo!=-1 && e.button == 3) {
					int i = 0;
					Franja f;
					Boolean encontrado = false;
					while (!encontrado && i < cuadrante.empleados.get(empleadoActivo).franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
						if (f.contienePuntoInt(e.x, e.y)) {
							cuadrante.empleados.get(empleadoActivo).franjas.remove(f);
							redibujar(f, empleadoActivo);
							encontrado = true;
						}
						i++;
					}
				} else {
					Franja f;
					int i = 0;
					Boolean encontrado = false;
					while (empleadoActivo != -1 && !encontrado && i < cuadrante.empleados.get(empleadoActivo).franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
						if (f.contienePuntoInt(e.x, e.y)) {
							encontrado = true;
							activarFranja(i, 2);
							despl = e.x - f.inicio;
						} else if (f.tocaLadoIzquierdo(e.x, e.y)) {
							encontrado = true;
							activarFranja(i, 1);
						} else if (f.tocaLadoDerecho(e.x, e.y)) {
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
				Franja f;
				redibujar(franjaActiva,0);
				desactivarFranja();
				if (empleadoActivo != -1) {
					for (int i = 0; i < cuadrante.empleados.get(empleadoActivo).franjas
							.size(); i++) {
						f = cuadrante.empleados.get(empleadoActivo).franjas
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
							Franja f2;
							int j = 0;
							Boolean encontrado = false;
							while (!encontrado
									&& j < cuadrante.empleados
											.get(empleadoActivo).franjas.size()) {
								f2 = cuadrante.empleados.get(empleadoActivo).franjas
										.get(j);
								if (f2.contienePuntoInt(f.inicio, e.y)
										|| f2.contienePuntoInt(f.fin, e.y)) {
									// Juntar dos franjas que se tocan o cruzan
									if (f2.inicio < f.inicio)
										f.inicio = f2.inicio;
									if (f2.fin > f2.fin)
										f.fin = f2.fin;
									cuadrante.empleados.get(empleadoActivo).franjas
											.remove(j);
									redibujar(f, empleadoActivo);
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
				Franja f;
				Boolean encontrado = false;
				while (!encontrado
						&& i < cuadrante.empleados.get(empleadoActivo).franjas
								.size()) {
					f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
					if (f.contienePuntoInt(e.x, e.y)) {
						f = cuadrante.empleados.get(empleadoActivo).franjas
								.get(i);
						// TODO que calcule el sticky en el que está
						// Franja f2 = cuadrante.new Franja (f.inicio, e.x-10);
						// f.inicio=e.x+10;
						// cuadrante.empleados.get(empleadoActivo).franjas.add(f2);
						redibujar(f, empleadoActivo);
						encontrado = true;
					}
					i++;
				}
			}
		});
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

}
