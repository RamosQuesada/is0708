package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class I02_cuadr {
	Composite c;
	int ancho;
	int alto;
	GC gc;
	Color fg;
	
	private void calcularTama�o(){
		ancho = c.getClientArea().width;
		alto = c.getClientArea().height;	
	}
	private void cambiarPincel(int r, int g, int b) {
		gc.setForeground(new Color(c.getDisplay(),r, g, b));
	}
	private void cambiarRelleno(int r, int g, int b) {
		gc.setBackground(new Color(c.getDisplay(),r, g, b));
	}
	/*
	 * Qu� deber�a hacer esto:
	 * - Guardar una lista de rect�ngulos que correspondan con las horas
	 * - Al pasar el rat�n por encima:
	 * 		- comprobar si est� dentro de alg�n rect�ngulo: cursor = mano
	 * 		- comprobar si est� en el borde de alg�n rect�ngulo: cursor = resize
	 * - Al arrastrar el rat�n, cambiar los par�metros del rect�ngulo sobre el que est�
	 *   seg�n el movimiento del rat�n.
	 * - Redibujar la lista de rect�ngulos a cada cambio.
	 */
	
	public I02_cuadr (Composite c) {
		this.c = c;
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				gc = event.gc;
				dibujaCuadrado(0,0,ancho-1,alto-1,5);
			}
		});
		c.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) { }
			public void controlResized(ControlEvent e) {
				calcularTama�o();				
			}
		});
		
	}
	public void dibujaCuadrado (int x0, int y0, int x1, int y1, int borde) {
		gc.setLineWidth(borde);
		cambiarRelleno(190,234,140);
		cambiarPincel(200,150,65);
		gc.fillRectangle(x0,y0,x1,y1);
		gc.drawRectangle(x0,y0,x1,y1);
	}
}
