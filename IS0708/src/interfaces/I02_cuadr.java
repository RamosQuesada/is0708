/*******************************************************************************
 * INTERFAZ I-02_cuadr :: Canvas de cuadrante
 *   por Daniel Dionne
 *   
 * Crea un canvas con un cuadrante editable.
 * Importa la clase cuadrante que es la que lo dibuja. Esta clase s�lo se encarga
 * de los movimientos y pulsaciones del rat�n.
 * 
 * ver 0.1
 *******************************************************************************/

// TODO Hacer que reduzca la resoluci�n del grid en funci�n del tama�o de la pantalla
package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import java.sql.Date;

import aplicacion.Empleado;
import aplicacion.Posicion;
import aplicacion.FranjaDib;

/**
 * Dada una instancia de Canvas, que se le pasa como parámetro al constructor,
 * crea un cuadrante sobre la misma.
 * @author Daniel
 *
 */
public class I02_cuadr {
	


	




	private void desactivarFranja() {
		if (franjaActiva!=null)
			franjaActiva.desactivarFranja();
		franjaActiva = null;
		movimiento = 0;
	}

	private int dameMovimiento() {
		return movimiento;
	}

	private FranjaDib dameFranjaActiva() {
		return franjaActiva;
	}

	public void setDiario()  {
		diario = true;
		lGridCuadrante.setVisible(true);
		cGridCuadrante.setVisible(true);
		canvas.addMouseListener(mouseListenerCuadrSemanal);
		canvas.addMouseMoveListener(mouseMoveListenerCuadrSemanal);
		canvas.removeMouseListener(mouseListenerCuadrMensual);
		canvas.removeMouseMoveListener(mouseMoveListenerCuadrMensual);
		redibujar();}
	public void setMensual() {
		diario = false;
		lGridCuadrante.setVisible(false);
		cGridCuadrante.setVisible(false);
		canvas.addMouseListener(mouseListenerCuadrMensual);
		canvas.addMouseMoveListener(mouseMoveListenerCuadrMensual);
		canvas.removeMouseListener(mouseListenerCuadrSemanal);
		canvas.removeMouseMoveListener(mouseMoveListenerCuadrSemanal);
		redibujar();
	}
	/**
	 * Constructora que recibe como par�metro el Composite donde colocar los botones
	 * y el cuadrante.
	 * @param c	Composite sobre el que dibujar el cuadrante
	 */
	private void activarFranja(int franja, int mov) {
		//franjaActiva = franjas.get(franja);
		franjaActiva.activarFranja();
		movimiento = mov;
		// Movimientos:
		// 0: Ninguno
		// 1: Mover inicio
		// 2: Desplazar
		// 3: Mover final
	}
	

	public Boolean enAreaDibujo(int x, int y) {
		Boolean b = true;
		if (x < margenNombres + margenIzq)
			b = false;
		else if (x > ancho - margenDer)
			b = false;
		else if (y < margenSup)
			b = false;
		else if (y > alto - margenInf)
			b = false;
		return b;
	}
	/**
	 *  method sets printable image ( day view )
	 *  size of drowen cuadrante is set 
	 *  by imgSize variable
	 */
	
	public void ponImageDia(){
		cuadranteImg.dispose();
		cuadranteImg = new Image(this.display, imgSize.x, imgSize.y);
		GC gc1 = new GC(cuadranteImg);
		Cuadrante c = new Cuadrante(display,4,9,23,0,0,0,0,0,empleados);
		c.setTamano(imgSize.x-10, imgSize.y-10);
		c.dibujarCuadranteDia(gc1,-1);	
		calcularTamano();
		gc1.dispose();
	}
	
	/**
	 *  method sets printable image ( month view )
	 *  size of drowen cuadrante is set 
	 *  by imgSize variable
	 */
	
	public void ponImageMes(){
		cuadranteImg.dispose();
		cuadranteImg = new Image(this.display, imgSize.x, imgSize.y);
		GC gc2 = new GC(cuadranteImg);
		Cuadrante c = new Cuadrante(display,4,9,23,0,0,0,0,0,empleados);
		c.setTamano(imgSize.x-10, imgSize.y-10);
		c.dibujarCuadranteMes(gc2);
		calcularTamano();
		gc2.dispose();
		
	}
	
	/**
	 *  implementation of interface "impresion.Imprimible"
	 *  returns class private variable cuadranteImg - that
	 *  MUST be set by ponImageDia or ponImageMes
	 */
	
	public ImageData dameImageImprimible(){
		return this.cuadranteImg.getImageData();
	}
			
}