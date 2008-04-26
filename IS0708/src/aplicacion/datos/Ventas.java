package aplicacion.datos;

import java.sql.Date;
/**
 * 
 * @author Martinete
 * Esta clase especifica las ventas del departamento actual en una fecha concreta
 */

public class Ventas {

	private int ventas;
	private Date fecha;
	
	/**
	 * Contructora por defecto
	 */
	public void ventas(){
		ventas=0;
		fecha=null;
	}
	/**
	 * Constructora con dos parametros
	 * @param ventas
	 * @param fecha
	 */
	public void ventas(int ventas,Date fecha,int anio){
		this.ventas=ventas;
		this.fecha=fecha;
	}
	/**
	 * Devuelve las ventas
	 * @return
	 */
	public int getVentas() {
		return ventas;
	}
	/**
	 * Establece las ventas
	 * @param ventas
	 */
	public void setVentas(int ventas) {
		this.ventas = ventas;
	}
	/**
	 * Devuelve la fecha
	 * @return
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * Establece la fecha
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * Devuelve el año
	 * @return
	 */
}

