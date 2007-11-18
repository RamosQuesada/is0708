package aplicacion;

import java.util.ArrayList;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

/**
 * Representa a un empleado.
 * @author Daniel
 *
 */
public class Empleado {
	private String nombre;
	private Color color;
	public ArrayList<FranjaDib> franjas;
	public Empleado (String n, Color c) {
		nombre = n;
		franjas = new ArrayList<FranjaDib>();
		color = c;
	}
	public String dameNombre() {
		return nombre;
	}
	public void franjaNueva (Posicion pinicio, Posicion pfin) {
		FranjaDib f = new FranjaDib(pinicio, pfin);
		franjas.add(f);
	}
	public void quitarFranja (FranjaDib franja) {
		franjas.remove(franja);
	}
	protected String aString (int i) {
		String s = String.valueOf(i);
		if (i<10) s = '0' + s;
		return s;
	}
	public Color dameColor() {
		return color;
	}
	public void dibujarFranjas(Display display, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas) {
		int subDivs = 0;
		gc.setBackground(new Color(display, 0,0,0));
		gc.drawText(nombre, margenIzq, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
		for (int i=0; i<franjas.size(); i++) {
			franjas.get(i).dibujarFranja(display, gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
			subDivs += (franjas.get(i).pfin.dameHora() - franjas.get(i).pinicio.dameHora())*12;
			subDivs += (franjas.get(i).pfin.dameCMin() - franjas.get(i).pinicio.dameCMin());
		}
		gc.drawText(String.valueOf(subDivs/12)+":"+String.valueOf(aString(subDivs%12*60/12)), margenNombres-10, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
	}
	public Boolean contienePunto (int y, int posV, int margenSup, int sep_vert_franjas, int alto_franjas) {
		Boolean b = false;
		if (y > margenSup+(sep_vert_franjas+alto_franjas)*(posV+1) && y<=margenSup+(sep_vert_franjas+alto_franjas)*(posV+2)) b = true;
		return b;
	}
}