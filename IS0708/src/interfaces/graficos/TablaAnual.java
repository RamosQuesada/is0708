package interfaces.graficos;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import aplicacion.Vista;

public class TablaAnual implements Tabla{
	private ArrayList <String> fechas;
	private ArrayList <String> ventas;
	
	public TablaAnual(Date fecha,Vista vista){
		fechas=new ArrayList<String>();
		ventas=new ArrayList<String>();
		obtenFechasYVentas(fecha,vista);
	}

	/**
	 * Obtienen las fechas y las ventas del año de la fecha dada
	 */

	public void obtenFechasYVentas(Date fecha, Vista vista) {
		ArrayList<ArrayList<Object[]>> vectorVentas = vista.getVentas();
		int año=fecha.getYear()+1900;
		for(int i=0;i<vectorVentas.size();i++){
			ArrayList<Object[]> ventasMes = vectorVentas.get(i);
			String mes="";
			switch (i) {
				case 0 : mes="Enero/January "+año;break;
				case 1 : mes="Febero/February "+año;break;
				case 2 : mes="Marzo/March "+año;break;
				case 3 : mes="Abril/April "+año;break;
				case 4 : mes="Mayo/May "+año;break;
				case 5 : mes="Junio/June "+año;break;
				case 6 : mes="Julio/July "+año;break;
				case 7 : mes="Agosto/August "+año;break;
				case 8 : mes="Septiembre/September "+año;break;
				case 9 : mes="Octubre/October "+año;break;
				case 10 : mes="Noviembre/November "+año;break;
				case 11 : mes="Diciembre/December "+año;break;				
			}
			fechas.add(mes);
			double total=0;
			for (int j=0;j<ventasMes.size();j++){
				Object[] ventasDia=ventasMes.get(j);
				double cantidad=(Double) ventasDia[1];
				total+=cantidad;
			}
			ventas.add(Double.toString(total));		
		}		
	}

	/**
	 * Rellena la tabla anual
	 */

	public void rellenaTabla(Table tabla) {
		tabla.removeAll();
		for (int i=0;i<fechas.size();i++){
			TableItem tItem = new TableItem(tabla, SWT.NONE);
			tItem.setText(0, fechas.get(i));
			tItem.setText(1, ventas.get(i));
		}
	}
}
