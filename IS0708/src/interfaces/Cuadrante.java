/*******************************************************************************
 * Cuadrante :: 
 *   por Daniel Dionne
 *   
 * Dibuja un cuadrante.
 * 
 * ver 0.1
 *******************************************************************************/

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
	int horaInicio, horaFin; // Definen de qué hora a qué hora es el cuadrante
	int subdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	int tamHora, tamSubdiv;
	public ArrayList<Empleado> empleados; 

	public class Posicion {
		int hora, subdivision;
		public Posicion (int hora, int subdivision) {
			this.hora = hora;
			this.subdivision = subdivision;
		}
		public void suma (Posicion p, Posicion ancho) {
			int x = p.hora*12 + ancho.hora*12 + p.subdivision + ancho.subdivision;
			this.hora        = x/12;
			this.subdivision = x%12;
		}
		public void resta (Posicion p, Posicion ancho) {
			int x = p.hora*12 - ancho.hora*12 + p.subdivision - ancho.subdivision;
			this.hora        = x/12;
			this.subdivision = x%12;
		}
		public Posicion diferencia (Posicion p) {
			int x = this.hora*12 - p.hora*12 + this.subdivision - p.subdivision;
			return new Posicion(x/12, x%12);
		}
		public Boolean menorOIgualQue (Posicion p) {
			Boolean b = false;
			if (this.hora <= p.hora) b = true;
			else if (this.hora == p.hora && this.subdivision <= p.subdivision) b = true;
			return b;
		}
		public Boolean mayorQue (Posicion p) {
			Boolean b = false;
			if (this.hora > p.hora) b = true;
			else if (this.hora == p.hora && this.subdivision > p.subdivision) b = true;
			return b;
		}		
		public Boolean mayorOIgualQue (Posicion p) {
			Boolean b = false;
			if (this.hora >= p.hora) b = true;
			else if (this.hora == p.hora && this.subdivision >= p.subdivision) b = true;
			return b;
		}		
	}
	public class Franja {
		int inicio, fin;
		Posicion pinicio, pfin;
		private Boolean activa;
		public Franja (Posicion pinicio, Posicion pfin) {
			this.pinicio = pinicio;
			this.pfin = pfin;
			activa = false;
			actualizarPixeles();
		}
		private String aString (int i) {
			String s = String.valueOf(i);
			if (i<10) s = '0' + s;
			return s;
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
			if (activa) {
				int subDivs = 0;
				subDivs += (pfin.hora - pinicio.hora)*12;
				subDivs += (pfin.subdivision - pinicio.subdivision);
			
				cambiarRelleno(gc, r-50,g-50,b-50);
				gc.fillRoundRectangle(inicio+2,despV-13,135,20,10,10);
				cambiarRelleno(gc, r,g,b);	
				gc.fillRoundRectangle(inicio, despV-15, 135, 20,8,8);
				gc.drawRoundRectangle(inicio, despV-15, 135, 20,8,8);
				gc.fillRectangle(inicio+1,despV+1,Math.min(fin-inicio-1,136),12);
				String s1 = "";
				if (subDivs%12*60/12 != 0) s1=' '+String.valueOf(aString(subDivs%12*60/12))+'m';
				String s  = aString(pinicio.hora%24) + ":" + aString(pinicio.subdivision*60/12) + " - " + aString(pfin.hora%24) + ":" + aString(pfin.subdivision*60/12) + " (" + String.valueOf(subDivs/12)+'h'+s1+')';
				gc.drawText(s, inicio+5, despV-14, true);
			}
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
			Posicion ancho = pfin.diferencia(pinicio);
			// Comprobar si me he salido por la izquierda
			if (pinicio.hora < horaInicio || pinicio.subdivision<0) {
				pinicio.hora = horaInicio;
				pinicio.subdivision = 0;
				pfin.suma(pinicio, ancho);
			}
			// Comprobar si me he salido por la derecha
			else if (pfin.hora*12+pfin.subdivision >= horaFin*12) {
				Posicion fin = new Posicion(horaFin,0);
				pfin.diferencia(pinicio);
				pinicio.resta(fin, ancho);
				pfin.subdivision = 0;
				pfin.hora = horaFin;
			}
		}
		public void actualizarPixeles () {
			// Si coincide con una subdivisión representada, pagar el inicio a la subdivisión para que no quede feo
			if (pinicio.subdivision!=0 && pinicio.subdivision%(12/subdivisiones)==0) {
				inicio = margenIzq + margenNombres + tamHora*(pinicio.hora-horaInicio) + (tamHora/subdivisiones)*(pinicio.subdivision/(12/subdivisiones));
			}
			else
				inicio = margenIzq + margenNombres + tamHora*(pinicio.hora-horaInicio) + tamSubdiv*pinicio.subdivision;
			if (pfin.subdivision!=0 && pfin.subdivision%(12/subdivisiones)==0) {
				fin = margenIzq + margenNombres + tamHora*(pfin.hora-horaInicio) + (tamHora/subdivisiones)*(pfin.subdivision/(12/subdivisiones));
			}
			else
				fin    = margenIzq + margenNombres + tamHora*(pfin.hora   -horaInicio) + tamSubdiv*pfin.subdivision;
		}
		public void activarFranja()    {activa = true; }
		public void desactivarFranja() {activa = false;}
	}
	public class Empleado {
		String nombre;
		Color color;
		ArrayList<Franja> franjas;
		public Empleado (String n, Color c) {
			nombre = n;
			franjas = new ArrayList<Franja>();
			color = c;
		}
		public void franjaNueva (Posicion pinicio, Posicion pfin) {
			Franja f = new Franja(pinicio, pfin);
			franjas.add(f);
		}
		public void quitarFranja (Franja franja) {
			franjas.remove(franja);
		}
		private String aString (int i) {
			String s = String.valueOf(i);
			if (i<10) s = '0' + s;
			return s;
		}
		public void dibujarFranjas(GC gc, int posV, Color color) {
			int subDivs = 0;
			cambiarPincel(gc, 0, 0, 0);
			gc.drawText(nombre, margenIzq, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
			for (int i=0; i<franjas.size(); i++) {
				franjas.get(i).dibujarFranja(gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
				subDivs += (franjas.get(i).pfin.hora - franjas.get(i).pinicio.hora)*12;
				subDivs += (franjas.get(i).pfin.subdivision - franjas.get(i).pinicio.subdivision);
			}
			gc.drawText(String.valueOf(subDivs/12)+":"+String.valueOf(aString(subDivs%12*60/12)), margenNombres-10, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
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

	public Cuadrante(Display d, int subdivisiones, int horaInicio, int horaFin) {
		display = d;
		margenIzq = 20;
		margenDer=  20;
		margenSup = 20;
		margenInf = 20;
		margenNombres = 100;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		alto_franjas = 15;
		sep_vert_franjas = 10;
		this.subdivisiones = subdivisiones;
		
		Empleado e1 = new Empleado("Juan López", new Color (display, 104, 228,  85));
		Empleado e2 = new Empleado("Marisol Pitis", new Color (display, 130, 130, 225));
		Empleado e3 = new Empleado("Bill Gates", new Color (display, 240, 190, 150));
		
		e1.franjaNueva(new Posicion( 9, 6), new Posicion(14, 0));
		e1.franjaNueva(new Posicion(16, 0), new Posicion(18, 0));
		e2.franjaNueva(new Posicion(15, 0), new Posicion(22, 0));
		e3.franjaNueva(new Posicion(12, 3), new Posicion(16, 0));
		e3.franjaNueva(new Posicion(18, 0), new Posicion(22, 1));
		
		empleados = new ArrayList<Empleado>();
		empleados.add(e1);
		empleados.add(e2);
		empleados.add(e3);

	}
	public void dibujarCuadrante(GC gc, int empleadoActivo) {
		dibujarSeleccion(gc, empleadoActivo);
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
		int subsep = sep/subdivisiones;
		for (int i=0; i<=h; i++) {
			gc.setLineStyle(SWT.LINE_SOLID);
			cambiarPincel(gc, 40,80,40);
			if (sep>14 && sep<=20) gc.drawText(String.valueOf((horaInicio+i)%24),     m+i*sep-5, margenSup, true);
			else if (sep>20)     gc.drawText(String.valueOf((horaInicio+i)%24)+'h', m+i*sep-5, margenSup, true);
			gc.drawLine(m+i*sep, 20+margenSup, m+i*sep, alto-margenInf);
			cambiarPincel(gc, 120,170,120);
			gc.setLineStyle(SWT.LINE_DOT);
			if (i!=h)
				for (int j=1; j<subdivisiones; j++) {
					gc.drawLine(m+i*sep+j*subsep, 20+margenSup+5, m+i*sep+j*subsep, alto-margenInf-5);
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
		if (emp!=-1) {
			cambiarRelleno(gc, 255-(255-empleados.get(emp).dameColor().getRed())/5, 255-(255-empleados.get(emp).dameColor().getGreen())/5, 255-(255-empleados.get(emp).dameColor().getBlue())/5);
			gc.fillRectangle(margenNombres+margenIzq,margenSup+(sep_vert_franjas+alto_franjas)*(emp+1)-5,ancho-margenNombres-margenIzq-margenDer,alto_franjas+11);
		}
	}
	public Posicion sticky2 (int x) {
		int y = x - margenNombres - margenIzq + (tamHora/subdivisiones)/2;
		Posicion p;
		if (((y%tamHora)/(tamHora/subdivisiones))>=subdivisiones)
			p = new Posicion(1+y/tamHora+horaInicio,0);
		else
			//resto de minutos en px/2
			p = new Posicion(y/tamHora+horaInicio,((y%tamHora)/(tamHora/subdivisiones))*12/subdivisiones);
		return p;
	}	
	public void setTamaño(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaFin-horaInicio);
		tamSubdiv = tamHora/12;
		for (int i=0; i < empleados.size(); i++) {
			Empleado e = empleados.get(i);
			for (int j=0; j < e.franjas.size(); j++) {
				Franja f = e.franjas.get(j);
				f.actualizarPixeles();
			}
		}
	}
}