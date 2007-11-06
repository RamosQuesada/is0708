package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import java.util.ArrayList;

public class I02_cuadr {

	Composite c;
	Display display;
	int ancho;
	int alto;
	int despl; // Este es para cuando movemos una barra, para saber de d�nde la he cogido
	GC gc;
	Color fg;
	Boolean creando, terminadoDeCrear;
	int margenIzq, margenDer, margenSup, margenInf; // M�rgenes del cuadrante
	int margenNombres; // Un margen para pintar los nombres a la izquierda
	int horaInicio,horaFin; // Definen de qu� hora a qu� hora es el cuadrante
	int subdivisiones; // Cu�ntas subdivisiones hacer por hora (0 = sin subdivisiones)
	// La variable terminadoDeCrear sirve para que una franja nueva no desaparezca al crearla
	Franja franjaActiva;
	Empleado empleadoActivo;
	ArrayList<Empleado> empleados; 
	int movimiento;
	Boolean stickyGrid;
	Boolean subStick;
	public class Franja {
		int inicio, fin;
		int r,g,b;
		public Franja (int i, int f, int r, int g, int b) {
			inicio = i;
			fin = f;
			this.r = r;
			this.g = g;
			this.b = b;
		}
		public void dibujarFranja (int despV) {
			gc.setLineWidth(1);
			cambiarRelleno(r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio+2,despV+2,fin-inicio,15,10,10);
			cambiarRelleno(r,g,b);
			cambiarPincel(r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio,despV,fin-inicio,15,8,8);
			gc.drawRoundRectangle(inicio,despV,fin-inicio,15,8,8);
		}
		public Boolean contienePunto(int x, int y) {
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
		int r,g,b;
		int posV;
		ArrayList<Franja> franjas;
		public Empleado (String n, int r, int g, int b, int posV) {
			nombre = n;
			franjas = new ArrayList();
			this.r = r;
			this.g = g;
			this.b = b;
			this.posV = posV;
		}
		public void franjaNueva (int inicio, int fin) {
			Franja f = new Franja(inicio, fin, r, g, b);
			franjas.add(f);
		}
		public void quitarFranja (Franja franja) {
			franjas.remove(franja);
		}
		public void dibujarFranjas() {
			for (int i=0; i<franjas.size(); i++)
				franjas.get(i).dibujarFranja(posV);
		}
	}
	
	private void dibujarHoras() {
		cambiarPincel(40,80,40);
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
					cambiarPincel(40,80,40);
					gc.drawText(String.valueOf(hi)+'h', m+i*subsep-5, margenSup, true);
					gc.drawLine(m+i*subsep, 20+margenSup, m+i*subsep, alto-margenInf);
					cambiarPincel(120,170,120);
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
	public Boolean enAreaDibujo(int x, int y) {
		Boolean b = true;
		if (x<margenNombres+margenIzq) b = false;
		else if (x>ancho-margenDer)    b = false;
		else if (y<margenSup)          b = false;
		else if (y>alto-margenInf)     b = false;
		return b;
	}
	
	public int sticky (int x) {
		// Pegar las barras al grid si est� activada la opci�n
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
	
	private void calcularTama�o(){
		ancho = c.getClientArea().width;
		alto  = c.getClientArea().height;	
	}
	private void cambiarPincel (int r, int g, int b) {
		// Controlar l�mites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(c.getDisplay(),r, g, b));
	}
	private void cambiarRelleno(int r, int g, int b) {
		// Controlar l�mites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setBackground(new Color(c.getDisplay(),r, g, b));
	}
	
	public void redibujar(Franja f) {
		c.redraw(0, 49, ancho, 18, false);
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
		franjaActiva = empleadoActivo.franjas.get(franja);
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

		display = c.getDisplay();
		Empleado e1 = new Empleado("Pepe", 104, 228,  85, 50);
		Empleado e2 = new Empleado("Pepe", 130, 130, 225, 100);
		Empleado e3 = new Empleado("Pepe", 240, 190, 150, 150);
		
		e1.franjaNueva(150,200);
		e1.franjaNueva(220,280);
		e1.franjaNueva(300,360);
		e2.franjaNueva(150,200);
		e2.franjaNueva(220,280);
		e2.franjaNueva(300,360);
		e3.franjaNueva(150,200);
		e3.franjaNueva(220,280);
		e3.franjaNueva(300,360);
		empleadoActivo = e1;
		empleados = new ArrayList();
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
			    // Doble buffering para evitar parpadeo
				Image bufferImage = new Image(display,ancho,alto);
			    gc = new GC(bufferImage);
				gc.setAntialias(SWT.ON);
				dibujarHoras();
				for (int i=0; i<empleados.size(); i++) {
					empleadoActivo = empleados.get(i);
					empleadoActivo.dibujarFranjas();
				}
				event.gc.drawImage(bufferImage,0,0);
				bufferImage.dispose();
			}
		});
		c.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) { }
			public void controlResized(ControlEvent e) {
				calcularTama�o();
			}
		});
		c.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e){
				Franja f = dameFranjaActiva();
				// Si acabo de apretar el bot�n para crear una franja, pero todav�a no he movido el rat�n
				if (creando) {
					Franja nuevaFranja = new Franja(e.x, e.x,104,228,85);
					if (stickyGrid){
						nuevaFranja.inicio = sticky(nuevaFranja.inicio);
					}
					empleadoActivo.franjas.add(nuevaFranja);
					activarFranja(empleadoActivo.franjas.size()-1,3);
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
					while (!encontrado2 && j<empleadoActivo.franjas.size()) {
						f2 = empleadoActivo.franjas.get(j);
						if ((f.inicio < f2.fin && f2.contienePunto(f.inicio, e.y)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2=true;
							f.inicio = f2.inicio;
							despl += (f2.fin-f2.inicio);
							empleadoActivo.franjas.remove(j);								
						}
						else if ((f.fin > f2.inicio && f2.contienePunto(f.fin, e.y)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
							encontrado2=true;
							f.fin = f2.fin;
							empleadoActivo.franjas.remove(j);
						}
						j++;
					}						
					redibujar(f);
				}
				// Si estoy cambiando el inicio de una franja
				else if (dameMovimiento()==1) {
					f.inicio = e.x;
					// Comprobar si toco el borde izquierdo
					if (f.inicio < margenNombres+margenIzq) f.inicio=margenNombres+margenIzq;
					// Comprobar si la barra es de tama�o menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						empleadoActivo.franjas.remove(f);
						cursor(0);
					}
					else if (stickyGrid) {
						f.inicio = sticky(f.inicio);
					}
					else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2; Boolean encontrado2 = false;
						while (terminadoDeCrear && !encontrado2 && j<empleadoActivo.franjas.size()) {
							f2 = empleadoActivo.franjas.get(j);
							if ((f.inicio < f2.fin && f2.contienePunto(e.x, e.y)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
								encontrado2=true;
								f.inicio = f2.inicio;
								empleadoActivo.franjas.remove(j);
							}
							j++;
						}
					}
					redibujar(f);
				}
				// Si estoy cambiando el fin de una franja
				else if (dameMovimiento()==3) {
					f.fin = e.x;
					// Comprobar si toco el borde derecho
					if (f.fin > ancho-margenDer) f.fin=ancho-margenDer;
					// Comprobar si la barra es de tama�o menor o igual que 0
					if (f.inicio > f.fin) {
						desactivarFranja();
						empleadoActivo.franjas.remove(f);
						cursor(0);
					}
					else if (stickyGrid) {
						f.fin = sticky(f.fin);
					}
					else {
						// Comprobar contacto con otras franjas
						int j = 0;
						Franja f2; Boolean encontrado2 = false;
						while (terminadoDeCrear && !encontrado2 && j<empleadoActivo.franjas.size()) {
							f2 = empleadoActivo.franjas.get(j);
							if ((f.fin > f2.inicio && f2.contienePunto(e.x, e.y)) | (f.inicio < f2.inicio && f.fin > f2.fin)) {
								encontrado2=true;
								f.fin = f2.fin;
								empleadoActivo.franjas.remove(j);
								desactivarFranja();
							}
							j++;
						}					
					}
					redibujar(f);
				}
				// Si no estoy moviendo ninguna franja,
				// comprobar si el cursor est� en alguna franja, una por una
				else {
					int i=0;
					Boolean encontrado = false;
					while (!encontrado && i<empleadoActivo.franjas.size()) {
						f = empleadoActivo.franjas.get(i);
						if (f.contienePuntoInt (e.x, e.y)) { cursor(1); encontrado=true;}
						else if (f.tocaLadoIzquierdo(e.x, e.y)) { cursor(2); encontrado=true;}
						else if (f.tocaLadoDerecho  (e.x, e.y)) { cursor(2); encontrado=true;}
						else cursor(0);
						i++;
					}
				}
			}
		});
		c.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e){
// COMPROBAR que no queda una fracci�n tonta
				if (e.button==3) {
					int i=0; Franja f; Boolean encontrado = false;
					while (!encontrado && i<empleadoActivo.franjas.size()) {
						f = empleadoActivo.franjas.get(i);
						if (f.contienePuntoInt (e.x, e.y)) {
							empleadoActivo.franjas.remove(f);
							redibujar(f);
							encontrado = true;
						}
						i++;
					}
				}
				else {
					Franja f;
					int i = 0;
					Boolean encontrado = false;
					while (!encontrado && i<empleadoActivo.franjas.size()) {
						f = empleadoActivo.franjas.get(i);
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
				for (int i=0; i<empleadoActivo.franjas.size(); i++) {
					f = empleadoActivo.franjas.get(i);
					// Si acabo de crear una franja, comprobar que no est� del rev�s, y si lo est�, darle la vuelta
					// Comprobar tambi�n si se cruza con otra.
					if (!terminadoDeCrear) {
						if (f.inicio > f.fin) {
							int aux = f.inicio;
							f.inicio = f.fin;
							f.fin = aux;
						}
						Franja f2;
						int j = 0;
						Boolean encontrado = false;
						while (!encontrado && j<empleadoActivo.franjas.size()) {
							f2 = empleadoActivo.franjas.get(j);
							if (f2.contienePuntoInt(f.inicio, e.y) || f2.contienePuntoInt(f.fin, e.y)) {
								// Juntar dos franjas que se tocan o cruzan
								if (f2.inicio < f.inicio) f.inicio = f2.inicio;
								if (f2.fin > f2.fin) f.fin = f2.fin;
								empleadoActivo.franjas.remove(j);
								redibujar(f);
							}
							j++;
						}
					}
				}
				
				terminadoDeCrear = true;
			}
			public void mouseDoubleClick(MouseEvent e) {
				int i=0; Franja f; Boolean encontrado = false;
				while (!encontrado && i<empleadoActivo.franjas.size()) {
					f = empleadoActivo.franjas.get(i);
					if (f.contienePuntoInt (e.x, e.y)) {
						f = empleadoActivo.franjas.get(i);
						Franja f2 = new Franja (f.inicio, e.x-10, 104, 228, 85);
						f.inicio=e.x+10;
						empleadoActivo.franjas.add(f2);
						redibujar(f);
						encontrado = true;
					}
					i++;
				}
			}
		});
	}
}
