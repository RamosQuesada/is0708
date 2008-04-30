package interfaces.graficos;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;

public class TablaAnual implements Tabla{
	private ArrayList <String> fechas;
	private ArrayList <String> ventas;
	
	public TablaAnual(Date fecha){
		obtenFechasYVentas(fecha);
	}

	/**
	 * Obtienen las fechas y las ventas del año de la fecha dada
	 */
	public void obtenFechasYVentas(Date fecha) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Rellena la tabla anual
	 */
	public Table rellenaTabla() {
		// TODO Auto-generated method stub
		return null;
	}
}
