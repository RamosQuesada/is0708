package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Cuadrante {
	Display display;
	int ancho;
	int alto;
	int margenIzq, margenDer, margenSup, margenInf; // Márgenes del cuadrante
	int margenNombres; // Un margen para pintar los nombres a la izquierda
	int alto_franjas;
	int sep_vert_franjas;
	int horaInicio,horaFin; // Definen de qué hora a qué hora es el cuadrante
	int subdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	
	public ArrayList<Empleado> empleados; 

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

	public Cuadrante(Display d) {
		display = d;
		margenIzq = 20;
		margenDer=  20;
		margenSup = 20;
		margenInf = 20;
		margenNombres = 100;
		horaInicio = 9;
		horaFin = 23;
		alto_franjas = 15;
		sep_vert_franjas = 10;
		subdivisiones = 4;
		
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
		
		empleados = new ArrayList();
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);

	}

	public void dibujarCuadrante(GC gc) {
		//dibujarSeleccion(empleadoActivo);
		dibujarHoras(gc);
		for (int i=0; i<empleados.size(); i++) {
			empleados.get(i).dibujarFranjas(gc, i, empleados.get(i).dameColor());
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
	private void cambiarPincel (GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(display,r, g, b));
	}
	private void cambiarRelleno(GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setBackground(new Color(display,r, g, b));
	}
	private void dibujarSeleccion (GC gc, int emp) {
		gc.setBackground(empleados.get(emp).dameColor());
		gc.fillRectangle(margenNombres+margenIzq,margenSup+(sep_vert_franjas+alto_franjas)*(emp+1),ancho-margenNombres-margenIzq-margenDer,alto_franjas);
	}

	public int sticky (int x, Boolean subStick) {
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
	public void setTamaño(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
	}

}
