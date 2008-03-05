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
	private Cuadrante cuadrante;
	
	public Analisis(int dias, Cuadrante cuadrante){
		this.sugerencias =new ArrayList[dias];
		this.cuadrante=cuadrante;
		this.dias=dias;
		for (int i=0;i<dias;i++){
			sugerencias[i]=new ArrayList<Sugerencia>();			
		}
		recorrido(cuadrante);
		
	}
	
	public void recorrido (Cuadrante cuadrante){
		for (int i=0;i<dias;i++){
			Time t=new Time(8,0,0);
			for (int j=0;j<24;j++){
				t.setHours(j);
				
				for (int k=-5;k<60;k=k+5){
					t.setMinutes(k);
					int minimoDia=0;//TODO buscar como se saca el minimo de trabajadores en un dia
					Sugerencia sugAnterior=null;
					//si ya hay alguna sugerencias del dia, se coge la ultima, si no se queda null
					if (!sugerencias[i].isEmpty()){
						sugAnterior=sugerencias[i].get(sugerencias[i].size());
					}
					
					//se comprueba cuantos trabajadores hay en un minuto concreto
					//se hacen franjas de 5 minutos
					int contador=0;
					for (int l=0;l<cuadrante.getListaTrabajaDia(i).size();l++){
						Trabaja trab=cuadrante.getListaTrabajaDia(i).get(l);
						if(compruebaHora(t,trab.getFichIni(),trab.getFichFin())){
							contador++;
						}
					}
										
					// si el contador es menor que el minimo para ese minuto, se crea una sugerencia nueva
					// solo si no existia una anterior con las mismas caracteristas
					// en caso contrario, se amplia en 5 minutos la franja de la sugerencia
					if (contador<minimoDia){
						if((sugAnterior!=null)&&(minimoDia-contador!=sugAnterior.getFaltas())){							
							Sugerencia sug=new Sugerencia(minimoDia-contador, t,new Time(t.getHours(),k,0));
							sugerencias[i].add(sug);
						}
						else{
							sugAnterior.ampliar(0,5);
						}
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
	
	public ArrayList<Sugerencia> getSugerenciasDia(int i){
		return sugerencias[i];
	}
}
