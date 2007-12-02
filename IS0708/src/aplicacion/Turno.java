package aplicacion;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

/**
 * Esta clase representa un turno.
 * @author Daniel
 *
 */
public class Turno {
	public ArrayList<FranjaDib> franjas;
	public Turno() {
		franjas = new ArrayList<FranjaDib>();	
	}
	public void franjaNueva (Posicion pinicio, Posicion pfin) {
		FranjaDib f = new FranjaDib(pinicio, pfin);
		franjas.add(f);
	}
	public void quitarFranja (FranjaDib franja) {
		franjas.remove(franja);
	}
	public Boolean contienePunto (int y, int posV, int margenSup, int sep_vert_franjas, int alto_franjas) {
		Boolean b = false;
		if (y > margenSup+(sep_vert_franjas+alto_franjas)*(posV+1) && y<=margenSup+(sep_vert_franjas+alto_franjas)*(posV+2)) b = true;
		return b;
	}
	public void dibujarFranjas(Display display, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas) {
		int subDivs = 0;
		for (int i=0; i<franjas.size(); i++) {
			franjas.get(i).dibujarFranja(display, gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
			subDivs += (franjas.get(i).pfin.dameHora() - franjas.get(i).pinicio.dameHora())*12;
			subDivs += (franjas.get(i).pfin.dameCMin() - franjas.get(i).pinicio.dameCMin());
		}
	}
	
}
