package interfaces.graficos;

import java.sql.Date;

import org.eclipse.swt.widgets.Table;

public interface Tabla {
	
	/**
	 * Obtiene las fechas y sus ventas a partir de la que se pasa por parámetro
	 * @param fecha a partir de la cual se calculan el resto de fechas y las ventas
	 */
	public abstract void obtenFechasYVentas(Date fecha);
	
	/**
	 * Rellena la tabal con lo que se obtiene en obtenFechasYVentas
	 * @return la tabla rellena
	 */
	public abstract Table rellenaTabla();
}
