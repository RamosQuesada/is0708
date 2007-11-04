package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import java.util.ArrayList;

public class I02_cuadr {

	Composite c;
	int ancho;
	int alto;
	GC gc;
	Color fg;
	ArrayList<Franja> franjas;

	public class Franja {
		int inicio;
		int fin;
		Boolean mueve, cambiaInicio, cambiaFin;
		Boolean moviendo, cambiandoInicio, cambiandoFin;
		public Franja (int i, int f) {
			inicio = i;
			fin = f;
			mueve = false;
			cambiaInicio = false;
			cambiaFin = false;
			moviendo = false;
			cambiandoInicio = false;
			cambiandoFin = false;
		}
		public void dibujarFranja () {
			gc.setLineWidth(1);
			cambiarPincel(0,145,3);
			cambiarRelleno(100,100,100);
			gc.fillRoundRectangle(inicio+2,10+2,fin-inicio,15,10,10);
			cambiarRelleno(104,228,85);
			gc.fillRoundRectangle(inicio,10,fin-inicio,15,8,8);
			gc.drawRoundRectangle(inicio,10,fin-inicio,15,8,8);
		}
		public Boolean contienePunto(int x, int y) {
			if (x>inicio+3 && x<fin-3) mueve = true;
			else mueve = false;
			return mueve;
		}
		public Boolean tocaLadoIzquierdo(int x, int y) {
			if (x>inicio-2 && x<inicio+4) cambiaInicio = true;
			else cambiaInicio = false;
			return cambiaInicio;
		}
		public Boolean tocaLadoDerecho(int x, int y) {
			if (x>fin-4 && x<fin+2) cambiaFin = true;
			else cambiaFin = false;
			return cambiaFin;
		}

	}

	private void calcularTamaño(){
		ancho = c.getClientArea().width;
		alto = c.getClientArea().height;	
	}
	private void cambiarPincel(int r, int g, int b) {
		gc.setForeground(new Color(c.getDisplay(),r, g, b));
	}
	private void cambiarRelleno(int r, int g, int b) {
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
	public I02_cuadr (Composite c) {
		this.c = c;
		franjas = new ArrayList();
		Franja f1 = new Franja(20, 80);
		Franja f2 = new Franja(150, 200);
		franjas.add(f1);
		franjas.add(f2);
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				gc = event.gc;
				gc.setAntialias(SWT.ON);
				for (int i=0; i<franjas.size(); i++)
				  franjas.get(i).dibujarFranja();
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
				// Comprobar si el cursor está en alguna franja
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
