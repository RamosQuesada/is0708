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
	/*
	 * Qué debería hacer esto:
	 * - Guardar una lista de rectángulos que correspondan con las horas
	 * - Al pasar el ratón por encima:
	 * 		- comprobar si está dentro de algún rectángulo: cursor = mano
	 * 		- comprobar si está en el borde de algún rectángulo: cursor = resize
	 * - Al arrastrar el ratón, cambiar los parámetros del rectángulo sobre el que está
	 *   según el movimiento del ratón.
	 * - Redibujar la lista de rectángulos a cada cambio.
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
				calcularTamaño();				
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
