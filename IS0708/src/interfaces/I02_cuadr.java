/*******************************************************************************
 * INTERFAZ I-02_cuadr :: Canvas de cuadrante
 *   por Daniel Dionne
 *   
 * Crea un canvas con un cuadrante editable
 * ver 0.1
 *******************************************************************************/

package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import java.util.ArrayList;
import interfaces.Cuadrante.*;

public class I02_cuadr {
/* Tareas:
 * - mostrar nombres de los empleados
 * - resaltar el empleado seleccionado en el modo edición
 * - bug: al hacer muchas franjas pequeñitas, no se pegan bien (y no tiene que ver con el sticky)
 */
	Composite c;
	Cuadrante cuadrante;
	int alto, ancho;
	Display display;
	int despl; // Este es para cuando movemos una barra, para saber de dónde la he cogido
	Color fg;
	Boolean creando, terminadoDeCrear; 	// La variable terminadoDeCrear sirve para que una franja nueva no desaparezca al crearla
	int empleadoActivo;
	
	int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	int margenNombres; // Un margen para pintar los nombres a la izquierda
	Franja franjaActiva;

	int movimiento;
	Boolean stickyGrid;
	Boolean subStick;
	
	
	private void calcularTamaño(){
		ancho = c.getClientArea().width;
		alto  = c.getClientArea().height;
		cuadrante.setTamaño(ancho, alto);
	}
	public void redibujar(Franja f, int posV) {
		// Redibuja sólo las franjas que corresponden, para evitar calculos innecesarios
		//TODO
		//c.redraw(0, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), ancho, 18, false);
		c.redraw(0, 0, ancho, alto, false);
	}
	public void cursor(int i) {
		switch (i) {			
		case 1 :
			c.setCursor(new Cursor (c.getDisplay(), SWT.CURSOR_HAND));	break;
		case 2 :
			c.setCursor(new Cursor (c.getDisplay(), SWT.CURSOR_SIZEE));	break;
		default :
			c.setCursor(new Cursor (c.getDisplay(), SWT.CURSOR_ARROW));	break;
		}
		
	}
	public void activarFranja(int franja, int mov) {
		franjaActiva = cuadrante.empleados.get(empleadoActivo).franjas.get(franja);
		movimiento = mov;
		// Movimientos:
		//		0: Ninguno
		//		1: Mover inicio
		//		2: Desplazar
		//		3: Mover final
	}
	public void desactivarFranja() {
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
		if (ancho !=0 && alto !=0) {
		Image bufferImage = new Image(display,ancho,alto);
	    GC gc2 = new GC(bufferImage);
		gc2.setAntialias(SWT.ON);
		cuadrante.dibujarCuadrante(gc2);
		gc.drawImage(bufferImage,0,0);
		bufferImage.dispose();
		}
	}
	
	public I02_cuadr (Canvas c) {

		this.c = c;
		creando = false;
		terminadoDeCrear = true;
		
		franjaActiva = null;
		movimiento = 0;
		stickyGrid = true;
		subStick = true;
		
		margenIzq = 20;
		margenDer=  20;
		margenSup = 20;
		margenInf = 20;
		margenNombres = 100;

		empleadoActivo = 2;
		cuadrante = new Cuadrante(display);
		calcularTamaño();
		display = c.getDisplay();
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				dibujarCuadrante(event.gc);
			}
		});
		c.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) { }
			public void controlResized(ControlEvent e) {
				calcularTamaño();
			}
		});
		c.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e){
				Franja f = dameFranjaActiva();
				// Si acabo de apretar el botón para crear una franja, pero todavía no he movido el ratón
				if (creando) {
					Franja nuevaFranja = cuadrante.new Franja(e.x, e.x);
					if (stickyGrid){
						nuevaFranja.inicio = cuadrante.sticky(nuevaFranja.inicio, subStick);
					}
					cuadrante.empleados.get(empleadoActivo).franjas.add(nuevaFranja);
					activarFranja(cuadrante.empleados.get(empleadoActivo).franjas.size()-1,3);
					creando = false;
					terminadoDeCrear = false;
				}
				// Si estoy moviendo una franja
				else if (dameMovimiento()==2) {
					int anchoFranja = f.fin - f.inicio;
					f.inicio = e.x-despl;
					f.fin    = f.inicio+anchoFranja;
					if (stickyGrid) {
						f.inicio = cuadrante.sticky(f.inicio, subStick);
						f.fin = f.inicio+anchoFranja;
					}
					// Comprobar si me estoy pegando a los bordes
					f.pegarALosBordes();
					int j = 0;
					// Comprobar contacto con otras franjas
					Franja f2; Boolean encontrado2 = false;
					while (!encontrado2 && j<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
						f2 = cuadrante.empleados.get(empleadoActivo).franjas.get(j);
						if ((f.inicio <= f2.fin && f2.contienePunto(f.inicio)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2=true;
							f.inicio = f2.inicio;
							despl += (f2.fin-f2.inicio);
							cuadrante.empleados.get(empleadoActivo).franjas.remove(j);								
						}
						else if ((f.fin > f2.inicio && f2.contienePunto(f.fin)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2=true;
							f.fin = f2.fin;
							cuadrante.empleados.get(empleadoActivo).franjas.remove(j);
						}
						j++;
					}						
					redibujar(f,empleadoActivo);
				}
				// Si estoy cambiando el inicio de una franja
				else if (dameMovimiento()==1) {
					f.inicio = e.x;
					// Comprobar si toco el borde izquierdo
					if (f.inicio < margenNombres+margenIzq) f.inicio=margenNombres+margenIzq;
					// Activar sticky-grid
					if (stickyGrid) {
						f.inicio = cuadrante.sticky(f.inicio, subStick);
					}
					// Comprobar si la barra es de tamaño menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						cuadrante.empleados.get(empleadoActivo).franjas.remove(f);
						cursor(0);
					}
					else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2; Boolean encontrado2 = false;
						while (!encontrado2 && j<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
							f2 = cuadrante.empleados.get(empleadoActivo).franjas.get(j);
							if (f!=f2 && ((f.inicio <= f2.fin && f2.contienePunto(e.x-10)) | (f.inicio <= f2.inicio && f.fin >= f2.fin))) {
								encontrado2=true;
								f.inicio = f2.inicio;
								cuadrante.empleados.get(empleadoActivo).franjas.remove(j);
								desactivarFranja();
							}
							j++;
						}
					}
					redibujar(f,empleadoActivo);
				}
				// Si estoy cambiando el fin de una franja
				else if (dameMovimiento()==3) {
					f.fin = e.x;
					// Comprobar si toco el borde derecho
					if (f.fin > ancho-margenDer) f.fin=ancho-margenDer;
					// Activar sticky-grid
					if (stickyGrid) {
						f.fin = cuadrante.sticky(f.fin, subStick);
					}
					// Comprobar si la barra es de tamaño menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						cuadrante.empleados.get(empleadoActivo).franjas.remove(f);
						cursor(0);
					}
					else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2; Boolean encontrado2 = false;
						while (terminadoDeCrear && !encontrado2 && j<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
							f2 = cuadrante.empleados.get(empleadoActivo).franjas.get(j);
							if (f!=f2 && (f.fin >= f2.inicio && f2.contienePunto(e.x+10)) | (f.inicio <= f2.inicio && f.fin >= f2.fin)) {
								encontrado2=true;
								f.fin = f2.fin;
								cuadrante.empleados.get(empleadoActivo).franjas.remove(j);
								desactivarFranja();
							}
							j++;
						}					
					}
					redibujar(f,empleadoActivo);
				}
				// Si no estoy moviendo ninguna franja,
				// comprobar si el cursor está en alguna franja, una por una
				else {
					// Comprueba el empleado activo (vertical)
					int i=0; Boolean encontrado = false;
					while (!encontrado && i<cuadrante.empleados.size()) {
						if (cuadrante.empleados.get(i).contienePunto(e.y, i)) empleadoActivo = i;
						i++;
					}
					// Comprueba la franja activa (horizontal)
					i = 0; encontrado = false;
					while (!encontrado && i<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
						if (f.contienePuntoInt (e.x, e.y)) { cursor(1); encontrado=true;}
						else if (f.tocaLadoIzquierdo(e.x, e.y)) { cursor(2); encontrado=true;}
						else if (f.tocaLadoDerecho  (e.x, e.y)) { cursor(2); encontrado=true;}
						else cursor(0);
						i++;
						//System.out.print(empleadoActivo);						
					}
				}
			}
		});
		c.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e){
// COMPROBAR que no queda una fracción tonta
				if (e.button==3) {
					int i=0; Franja f; Boolean encontrado = false;
					while (!encontrado && i<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
						if (f.contienePuntoInt (e.x, e.y)) {
							cuadrante.empleados.get(empleadoActivo).franjas.remove(f);
							redibujar(f,empleadoActivo);
							encontrado = true;
						}
						i++;
					}
				}
				else {
					Franja f;
					int i = 0;
					Boolean encontrado = false;
					while (!encontrado && i<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
						f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
						if (f.contienePuntoInt (e.x, e.y))		{
							encontrado = true;
							activarFranja(i,2);
							despl = e.x - f.inicio;
						}
						else if (f.tocaLadoIzquierdo(e.x, e.y)) { encontrado = true; activarFranja(i,1);}
						else if (f.tocaLadoDerecho  (e.x, e.y)) { encontrado = true; activarFranja(i,3);}					
						i++;
					}
					if (!encontrado && enAreaDibujo(e.x,e.y)) creando = true;
				}
			}
			public void mouseUp(MouseEvent e) {
				Franja f;
				desactivarFranja();
				for (int i=0; i<cuadrante.empleados.get(empleadoActivo).franjas.size(); i++) {
					f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
					// Si acabo de crear una franja, comprobar que no está del revés, y si lo está, darle la vuelta
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
						while (!encontrado && j<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
							f2 = cuadrante.empleados.get(empleadoActivo).franjas.get(j);
							if (f2.contienePuntoInt(f.inicio, e.y) || f2.contienePuntoInt(f.fin, e.y)) {
								// Juntar dos franjas que se tocan o cruzan
								if (f2.inicio < f.inicio) f.inicio = f2.inicio;
								if (f2.fin > f2.fin) f.fin = f2.fin;
								cuadrante.empleados.get(empleadoActivo).franjas.remove(j);
								redibujar(f,empleadoActivo);
							}
							j++;
						}
					}
				}
				
				terminadoDeCrear = true;
			}
			public void mouseDoubleClick(MouseEvent e) {
				int i=0; Franja f; Boolean encontrado = false;
				while (!encontrado && i<cuadrante.empleados.get(empleadoActivo).franjas.size()) {
					f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
					if (f.contienePuntoInt (e.x, e.y)) {
						f = cuadrante.empleados.get(empleadoActivo).franjas.get(i);
						Franja f2 = cuadrante.new Franja (f.inicio, e.x-10);
						f.inicio=e.x+10;
						cuadrante.empleados.get(empleadoActivo).franjas.add(f2);
						redibujar(f,empleadoActivo);
						encontrado = true;
					}
					i++;
				}
			}
		});
	}
	
	public Boolean enAreaDibujo(int x, int y) {
		Boolean b = true;
		if (x<margenNombres+margenIzq) b = false;
		else if (x>ancho-margenDer)    b = false;
		else if (y<margenSup)          b = false;
		else if (y>alto-margenInf)     b = false;
		return b;
	}

}
