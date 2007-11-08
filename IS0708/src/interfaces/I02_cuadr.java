package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import java.util.ArrayList;

public class I02_cuadr {
/* Tareas:
 * - mostrar nombres de los empleados
 * - resaltar el empleado seleccionado en el modo edición
 * - bug: al hacer muchas franjas pequeñitas, no se pegan bien (y no tiene que ver con el sticky)
 */
	Composite c;
	Display display;
	int ancho;
	int alto;
	int alto_franjas;
	int sep_vert_franjas;
	int despl; // Este es para cuando movemos una barra, para saber de dónde la he cogido
	Color fg;
	Boolean creando, terminadoDeCrear;
	int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	int margenNombres; // Un margen para pintar los nombres a la izquierda
	int horaInicio,horaFin; // Definen de qué hora a qué hora es el cuadrante
	int subdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	// La variable terminadoDeCrear sirve para que una franja nueva no desaparezca al crearla
	Franja franjaActiva;
	int empleadoActivo;
	ArrayList<Empleado> empleados; 
	int movimiento;
	Boolean stickyGrid;
	Boolean subStick;
	public class Franja {
		int inicio, fin;
		public Franja (int i, int f) {
			inicio = i;
			fin = f;
		}
		public void dibujarFranja (GC gc, int despV, Color color) {
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			gc.setLineWidth(1);
			cambiarRelleno(gc, r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio+2,despV+2,fin-inicio,15,10,10);
			cambiarRelleno(gc, r,g,b);
			cambiarPincel(gc, r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio,despV,fin-inicio,15,8,8);
			gc.drawRoundRectangle(inicio,despV,fin-inicio,15,8,8);
		}
		public Boolean contienePunto(int x) {
			return x>inicio && x<fin;
		}
		public Boolean contienePuntoInt(int x, int y) {
			Boolean mueve = false;
			if (x>inicio+3 && x<fin-3) mueve = true;
			return mueve;
		}
		public Boolean tocaLadoIzquierdo(int x, int y) {
			Boolean cambiaInicio = false;
			if (x>inicio-5 && x<inicio+5) cambiaInicio = true;
			return cambiaInicio;
		}
		public Boolean tocaLadoDerecho(int x, int y) {
			Boolean cambiaFin = false;
			if (x>fin-5 && x<fin+5) cambiaFin = true;
			return cambiaFin;
		}
		public void pegarALosBordes () {
			int anchoFranja = fin - inicio;
			// Comprobar si me he salido por la izquierda
			if (inicio<margenNombres+margenIzq) {
				inicio=margenNombres+margenIzq;
				fin = inicio+anchoFranja;
			}
			// Comprobar si me he salido por la derecha
			else if (fin > ancho - margenDer) {
				fin = ancho - margenDer;
				inicio = fin-anchoFranja;
			}
		}
	}
	
	public class Empleado {
		String nombre;
		Color color;
		ArrayList<Franja> franjas;
		public Empleado (String n, Color c) {
			nombre = n;
			franjas = new ArrayList();
			color = c;
		}
		public void franjaNueva (int inicio, int fin) {
			Franja f = new Franja(inicio, fin);
			franjas.add(f);
		}
		public void quitarFranja (Franja franja) {
			franjas.remove(franja);
		}
		public void dibujarFranjas(GC gc, int posV, Color color) {
			for (int i=0; i<franjas.size(); i++)
				franjas.get(i).dibujarFranja(gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
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
	
	private void dibujarHoras(GC gc) {
		cambiarPincel(gc, 40,80,40);
		int m = margenIzq + margenNombres;
		int h = horaFin - horaInicio;
		int sep = (ancho - m - margenDer)/h;
		if (subdivisiones==0) {
			for (int i=0; i<h+1; i++) {
				gc.drawText(String.valueOf(horaInicio+i)+'h', m+i*sep-5, margenSup, true);
				gc.drawLine(m+i*sep, 20+margenSup, m+i*sep, alto-margenInf);
			}
		}
		else {
			int subsep = sep/(subdivisiones);
			int hi = horaInicio;
			for (int i=0; i<h*subdivisiones; i++) {
				gc.setLineStyle(SWT.LINE_DOT);
				if (i%subdivisiones==0) {
					gc.setLineStyle(SWT.LINE_SOLID);
					cambiarPincel(gc, 40,80,40);
					gc.drawText(String.valueOf(hi)+'h', m+i*subsep-5, margenSup, true);
					gc.drawLine(m+i*subsep, 20+margenSup, m+i*subsep, alto-margenInf);
					cambiarPincel(gc, 120,170,120);
					gc.setLineStyle(SWT.LINE_DOT);
					hi++;
				}
				else {
					gc.drawLine(m+i*subsep, 20+margenSup, m+i*subsep, alto-margenInf);
				}
			}
		}
		gc.setLineStyle(SWT.LINE_SOLID);
	}
	private void dibujarSeleccion (GC gc, int emp) {
		gc.setBackground(empleados.get(emp).dameColor());
		gc.fillRectangle(margenNombres+margenIzq,margenSup+(sep_vert_franjas+alto_franjas)*(emp+1),ancho-margenNombres-margenIzq-margenDer,alto_franjas);
	}
	public Boolean enAreaDibujo(int x, int y) {
		Boolean b = true;
		if (x<margenNombres+margenIzq) b = false;
		else if (x>ancho-margenDer)    b = false;
		else if (y<margenSup)          b = false;
		else if (y>alto-margenInf)     b = false;
		return b;
	}
	public int sticky (int x) {
		// Pegar las barras al grid si está activada la opción
		int sep = (ancho - margenIzq - margenNombres - margenDer)/(horaFin - horaInicio);
		int stick = sep / subdivisiones;
		int desp = margenNombres + margenIzq;
		if (subStick) {
			if      ((x-desp)%stick<(stick/3))         x = desp + stick * ((x-desp)/stick);
			else if ((x-desp)%stick>(stick-(stick/3))) x = desp + stick *(((x-desp)/stick)+1);
		}
		else {
			if      ((x-desp)%(stick*subdivisiones)<(stick*subdivisiones/3))         x = desp + stick * ((x-desp)/stick);
			else if ((x-desp)%(stick*subdivisiones)>(stick*subdivisiones-(stick*subdivisiones/3))) x = desp + stick *(((x-desp)/stick)+1);
		}
		return x;
	}
	private void calcularTamaño(){
		ancho = c.getClientArea().width;
		alto  = c.getClientArea().height;	
	}
	private void cambiarPincel (GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(c.getDisplay(),r, g, b));
	}
	private void cambiarRelleno(GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setBackground(new Color(c.getDisplay(),r, g, b));
	}
	public void redibujar(Franja f, int posV) {
		// Redibuja sólo las franjas que corresponden, para evitar calculos innecesarios
		c.redraw(0, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), ancho, 18, false);
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
		franjaActiva = empleados.get(empleadoActivo).franjas.get(franja);
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
		Image bufferImage = new Image(display,ancho,alto);
	    GC gc2 = new GC(bufferImage);
		gc2.setAntialias(SWT.ON);
		//dibujarSeleccion(empleadoActivo);
		dibujarHoras(gc2);
		for (int i=0; i<empleados.size(); i++) {
			empleados.get(i).dibujarFranjas(gc2, i,empleados.get(i).dameColor());
		}
		gc.drawImage(bufferImage,0,0);
		bufferImage.dispose();
	}
	
	public I02_cuadr (Canvas c) {

		this.c = c;
		creando = false;
		terminadoDeCrear = true;
		margenIzq = 20;
		margenDer=  20;
		margenSup = 20;
		margenInf = 20;
		margenNombres = 100;
		horaInicio = 9;
		horaFin = 23;
		subdivisiones = 4;
		franjaActiva = null;
		movimiento = 0;
		stickyGrid = true;
		subStick = true;
		alto_franjas = 15;
		sep_vert_franjas = 10;

		display = c.getDisplay();
		Empleado e1 = new Empleado("Pepe", new Color (display, 104, 228,  85));
		Empleado e2 = new Empleado("Pepe", new Color (display, 130, 130, 225));
		Empleado e3 = new Empleado("Pepe", new Color (display, 240, 190, 150));
		
		e1.franjaNueva(150,200);
		e1.franjaNueva(220,280);
		e1.franjaNueva(300,360);
		e2.franjaNueva(150,200);
		e2.franjaNueva(220,280);
		e2.franjaNueva(300,360);
		e3.franjaNueva(150,200);
		e3.franjaNueva(220,280);
		e3.franjaNueva(300,360);
		empleadoActivo = 2;
		empleados = new ArrayList();
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);
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
					Franja nuevaFranja = new Franja(e.x, e.x);
					if (stickyGrid){
						nuevaFranja.inicio = sticky(nuevaFranja.inicio);
					}
					empleados.get(empleadoActivo).franjas.add(nuevaFranja);
					activarFranja(empleados.get(empleadoActivo).franjas.size()-1,3);
					creando = false;
					terminadoDeCrear = false;
				}
				// Si estoy moviendo una franja
				else if (dameMovimiento()==2) {
					int anchoFranja = f.fin - f.inicio;
					f.inicio = e.x-despl;
					f.fin    = f.inicio+anchoFranja;
					if (stickyGrid) {
						f.inicio = sticky(f.inicio);
						f.fin = f.inicio+anchoFranja;
					}
					// Comprobar si me estoy pegando a los bordes
					f.pegarALosBordes();
					int j = 0;
					// Comprobar contacto con otras franjas
					Franja f2; Boolean encontrado2 = false;
					while (!encontrado2 && j<empleados.get(empleadoActivo).franjas.size()) {
						f2 = empleados.get(empleadoActivo).franjas.get(j);
						if ((f.inicio <= f2.fin && f2.contienePunto(f.inicio)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2=true;
							f.inicio = f2.inicio;
							despl += (f2.fin-f2.inicio);
							empleados.get(empleadoActivo).franjas.remove(j);								
						}
						else if ((f.fin > f2.inicio && f2.contienePunto(f.fin)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2=true;
							f.fin = f2.fin;
							empleados.get(empleadoActivo).franjas.remove(j);
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
						f.inicio = sticky(f.inicio);
					}
					// Comprobar si la barra es de tamaño menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						empleados.get(empleadoActivo).franjas.remove(f);
						cursor(0);
					}
					else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2; Boolean encontrado2 = false;
						while (!encontrado2 && j<empleados.get(empleadoActivo).franjas.size()) {
							f2 = empleados.get(empleadoActivo).franjas.get(j);
							if (f!=f2 && ((f.inicio <= f2.fin && f2.contienePunto(e.x-10)) | (f.inicio <= f2.inicio && f.fin >= f2.fin))) {
								encontrado2=true;
								f.inicio = f2.inicio;
								empleados.get(empleadoActivo).franjas.remove(j);
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
						f.fin = sticky(f.fin);
					}
					// Comprobar si la barra es de tamaño menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						empleados.get(empleadoActivo).franjas.remove(f);
						cursor(0);
					}
					else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2; Boolean encontrado2 = false;
						while (terminadoDeCrear && !encontrado2 && j<empleados.get(empleadoActivo).franjas.size()) {
							f2 = empleados.get(empleadoActivo).franjas.get(j);
							if (f!=f2 && (f.fin >= f2.inicio && f2.contienePunto(e.x+10)) | (f.inicio <= f2.inicio && f.fin >= f2.fin)) {
								encontrado2=true;
								f.fin = f2.fin;
								empleados.get(empleadoActivo).franjas.remove(j);
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
					while (!encontrado && i<empleados.size()) {
						if (empleados.get(i).contienePunto(e.y, i)) empleadoActivo = i;
						i++;
					}
					// Comprueba la franja activa (horizontal)
					i = 0; encontrado = false;
					while (!encontrado && i<empleados.get(empleadoActivo).franjas.size()) {
						f = empleados.get(empleadoActivo).franjas.get(i);
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
					while (!encontrado && i<empleados.get(empleadoActivo).franjas.size()) {
						f = empleados.get(empleadoActivo).franjas.get(i);
						if (f.contienePuntoInt (e.x, e.y)) {
							empleados.get(empleadoActivo).franjas.remove(f);
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
					while (!encontrado && i<empleados.get(empleadoActivo).franjas.size()) {
						f = empleados.get(empleadoActivo).franjas.get(i);
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
				for (int i=0; i<empleados.get(empleadoActivo).franjas.size(); i++) {
					f = empleados.get(empleadoActivo).franjas.get(i);
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
						while (!encontrado && j<empleados.get(empleadoActivo).franjas.size()) {
							f2 = empleados.get(empleadoActivo).franjas.get(j);
							if (f2.contienePuntoInt(f.inicio, e.y) || f2.contienePuntoInt(f.fin, e.y)) {
								// Juntar dos franjas que se tocan o cruzan
								if (f2.inicio < f.inicio) f.inicio = f2.inicio;
								if (f2.fin > f2.fin) f.fin = f2.fin;
								empleados.get(empleadoActivo).franjas.remove(j);
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
				while (!encontrado && i<empleados.get(empleadoActivo).franjas.size()) {
					f = empleados.get(empleadoActivo).franjas.get(i);
					if (f.contienePuntoInt (e.x, e.y)) {
						f = empleados.get(empleadoActivo).franjas.get(i);
						Franja f2 = new Franja (f.inicio, e.x-10);
						f.inicio=e.x+10;
						empleados.get(empleadoActivo).franjas.add(f2);
						redibujar(f,empleadoActivo);
						encontrado = true;
					}
					i++;
				}
			}
		});
	}
}
