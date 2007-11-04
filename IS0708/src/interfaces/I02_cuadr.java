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
	GC gc;
	Color fg;
	ArrayList<Franja> franjas;

	public class Franja {
		int inicio;
		int fin;
		int r,g,b;
		Boolean mueve, cambiaInicio, cambiaFin;
		Boolean moviendo, cambiandoInicio, cambiandoFin;
		public Franja (int i, int f, int r, int g, int b) {
			inicio = i;
			fin = f;
			this.r = r;
			this.g = g;
			this.b = b;
			mueve = false;
			cambiaInicio = false;
			cambiaFin = false;
			moviendo = false;
			cambiandoInicio = false;
			cambiandoFin = false;
		}
		public void dibujarFranja () {
			gc.setLineWidth(1);
			cambiarRelleno(r-50,g-50,b-50);
			gc.fillRoundRectangle(inicio+2,10+2,fin-inicio,15,10,10);
			cambiarRelleno(r,g,b);
			cambiarPincel(r-100,g-100,b-100);
			gc.fillRoundRectangle(inicio,10,fin-inicio,15,8,8);
			gc.drawRoundRectangle(inicio,10,fin-inicio,15,8,8);
		}
		public Boolean contienePunto(int x, int y) {
			if (x>inicio+3 && x<fin-3) mueve = true;
			else mueve = false;
			return mueve;
		}
		public Boolean tocaLadoIzquierdo(int x, int y) {
			if (x>inicio-5 && x<inicio+5) cambiaInicio = true;
			else cambiaInicio = false;
			return cambiaInicio;
		}
		public Boolean tocaLadoDerecho(int x, int y) {
			if (x>fin-5 && x<fin+5) cambiaFin = true;
			else cambiaFin = false;
			return cambiaFin;
		}

	}
	private void calcularTama�o(){
		ancho = c.getClientArea().width;
		alto = c.getClientArea().height;	
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
		c.redraw(0, 9, ancho, 18, false);
	}
	public void cursor(int i) {
		switch (i) {
			
		case 1 :
			c.setCursor(new Cursor (c.getDisplay(), SWT.CURSOR_HAND));
			break;
		case 2 :
			c.setCursor(new Cursor (c.getDisplay(), SWT.CURSOR_SIZEE));
			break;
			
		default :
			c.setCursor(new Cursor (c.getDisplay(), SWT.CURSOR_ARROW));
			break;

		}
		
	}
	public I02_cuadr (Canvas c) {
		this.c = c;

		display = c.getDisplay();
		franjas = new ArrayList();
		Franja f1 = new Franja(20, 80, 104, 228, 85);
		Franja f2 = new Franja(150, 200, 130, 130, 225);
		Franja f3 = new Franja(220, 250, 240, 190, 150);
		franjas.add(f1);
		franjas.add(f2);
		franjas.add(f3);
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
			    // Doble buffering para evitar parpadeo
				Image bufferImage = new Image(display,ancho,alto);
			    gc = new GC(bufferImage);
				gc.setAntialias(SWT.ON);
				for (int i=0; i<franjas.size(); i++)
					franjas.get(i).dibujarFranja();
				franjas.get(1).dibujarFranja();
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
				// Comprobar si el cursor est� en alguna franja
				Franja f;
				Boolean encontrado = false;
				int i = 0;
				while (!encontrado && i<franjas.size()) {
					f = franjas.get(i);
				
					if (f.moviendo) {
						int ancho = f.fin - f.inicio;
						f.inicio = e.x-ancho/2;
						f.fin    = e.x+ancho/2;
						redibujar(f);
					}
					else if (f.cambiandoInicio) {
						f.inicio = e.x;
						redibujar(f);
					}
					else if (f.cambiandoFin) {
						f.fin = e.x;
						redibujar(f);
					}
					else
						if (f.contienePunto (e.x, e.y)) { cursor(1); encontrado=true;}
						else if (f.tocaLadoIzquierdo(e.x, e.y)) { cursor(2); encontrado=true;}
						else if (f.tocaLadoDerecho  (e.x, e.y)) { cursor(2); encontrado=true;}
						else cursor(0);
					i++;
				}
			}
		});
		c.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e){
				Franja f;
				for (int i=0; i<franjas.size(); i++) {
					f = franjas.get(i);
					if (f.contienePunto (e.x, e.y)) f.moviendo = true;
					else if (f.tocaLadoIzquierdo(e.x, e.y)) f.cambiandoInicio = true;
					else if (f.tocaLadoDerecho  (e.x, e.y)) f.cambiandoFin = true;
				}
			}
			public void mouseUp(MouseEvent e) {
				Franja f;
				for (int i=0; i<franjas.size(); i++) {
					f = franjas.get(i);
					f.moviendo = false;
					f.cambiandoInicio = false;
					f.cambiandoFin = false;
				}
			}
			public void mouseDoubleClick(MouseEvent e) {}
		});
	}
}
