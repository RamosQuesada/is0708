package interfaces.graficos;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import aplicacion.Vista;

public class TablaSemanal implements Tabla{
	private ArrayList <String> fechas;
	private ArrayList <String> ventas;
	
	public TablaSemanal(Date fecha,Vista vista){
		fechas=new ArrayList<String>();
		ventas=new ArrayList<String>();
		obtenFechasYVentas(fecha,vista);
	}

	/**
	 * Obtienen las fechas y las ventas de la semana de la fecha dada
	 */
	public void obtenFechasYVentas(Date fecha,Vista vista) {
		ArrayList <Date> fechasSemana=aplicacion.utilidades.Util.getSemanaFecha(fecha);
		ArrayList<ArrayList<Object[]>> vectorVentas = vista.getVentas();
		for (int i=0;i<fechasSemana.size();i++){
			Date f=fechasSemana.get(i);
			int dia=f.getDate();
			int mes=f.getMonth()+1;
			ArrayList<Object[]> ventasMes = vectorVentas.get(mes-1);
			Object[] ventasDia=ventasMes.get(dia-1);
			double cantidad=(Double) ventasDia[1];
			ventas.add(Double.toString(cantidad));
			Date f2=(Date) ventasDia[0];
			int dia2=f2.getDate();
			int mes2=f2.getMonth()+1;
			int año=f2.getYear()+1900;
			if ((dia2<10)&&(mes2<10)) fechas.add(año+"-0"+mes2+"-0"+dia2);
			else if(mes2<10) fechas.add(año+"-0"+mes2+"-"+dia2);
				 else if (dia2<10) fechas.add(año+"-"+mes2+"-0"+dia2);
				 	  else fechas.add(año+"-"+mes2+"-"+dia2);
		}
	}

	/**
	 * Rellena la tabla semanal
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
