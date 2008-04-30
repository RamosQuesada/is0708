package interfaces.graficos;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;

public class TablaMensual implements Tabla{
	private ArrayList <String> fechas;
	private ArrayList <String> ventas;
	
	public TablaMensual(Date fecha){
		obtenFechasYVentas(fecha);
	}

	/**
	 * Obtienen las fechas y las ventas del mes de la fecha dada
	 */
	public void obtenFechasYVentas(Date fecha) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Rellena la tabla mensual
	 */
	public Table rellenaTabla() {
		// TODO Auto-generated method stub
		return null;
	}
}
