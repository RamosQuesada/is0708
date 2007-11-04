package interfaces;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class I02_cuadr {
	Composite c;
	GC gc;
	
	public I02_cuadr (Composite c) {
		this.c = c;
	}
	public void dibujaCuadrado () {
		
	}
	
	public void franja () {
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				event.gc.drawOval(0, 0, 600, 600);
			}
		});
	}
}
