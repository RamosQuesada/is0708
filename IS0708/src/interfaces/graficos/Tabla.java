package interfaces.graficos;

import java.sql.Date;

import org.eclipse.swt.widgets.Table;

import aplicacion.Vista;

public interface Tabla {
	
	/**
	 * Obtiene las fechas y sus ventas a partir de la que se pasa por parámetro
	 * @param fecha a partir de la cual se calculan el resto de fechas y las ventas
	 * @param vista la vista de la aplicación
	 */
	public abstract void obtenFechasYVentas(Date fecha,Vista vista);
	
	/**
	 * Rellena la tabal con lo que se obtiene en obtenFechasYVentas
	 * @return la tabla rellena
	 */
	public abstract void rellenaTabla(Table tabla);
}
