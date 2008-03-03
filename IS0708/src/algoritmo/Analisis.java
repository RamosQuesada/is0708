/**
 * 
 */
package algoritmo;

import java.sql.Time;
import java.util.ArrayList;

/**
 * @author Alberto
 * Esta clase analiza el cuadrante creado buscando errores a la hora de asignar
 * a los empleados, creando un informe final que se le presenta al jefe de departamento
 */
public class Analisis {
	private ArrayList<Sugerencia>[] sugerencias;
	private int dias;
	
	public Analisis(int dias){
		sugerencias =new ArrayList[dias];
		this.dias=dias;
		for (int i=0;i<dias;i++){
			sugerencias[i]=new ArrayList<Sugerencia>();			
		}
		
	}
	
	public void recorrido (Cuadrante cuadrante){
		for (int i=0;i<dias;i++){
			Time t=new Time(8,0,0);
			for (int j=0;j<24;j++){
				t.setHours(j);
				
				for (int k=-5;k<60;k=k+5){
					t.setMinutes(k);
					
					for (int l=0;l<cuadrante.getListaTrabajaDia(i).size();l++){
						Trabaja trab=cuadrante.getListaTrabajaDia(i).get(l);
						if(compruebaHora(t,trab.getFichIni(),trab.getFichFin())){
							contador++;
						}
					}
					
					if (contador<minimoDia){
						Sugerencia sug=new Sugerencia(minimoDia-contador, t,new Time(t.getHours(),k))
					}
					
					
				}
					
			}
						
		}
	}
	
	public boolean compruebaHora(Time a, Time ini, Time fin){
		boolean resultado=true;
		if (a.before(ini)){resultado=false;}
		if (a.after(fin)){resultado=false;}
		return resultado;
	}
}
