package algoritmo;
import java.util.ArrayList;

/**
 * 
 * @author DavidMartin
 *
 */
/**
 * Esta clase representa a un calendario donde vamos a almacenar los dias festivos,el
 * max/min numero de empleados y el numero de expertos e inexpertos
 */
public class Calendario {
	
	/**
	 *Mes
	 */
	private int mes;
	/**
	 *Año
	 */
	private int anio;
	/**
	 *Numero de dias del mes
	 */
	private int numDias;
	/**La primera dimensión representa el mes en cuestión (una posición por cada dia del mes)
	 * la segunda dimensión representa las horas de cada dia
	 */
	private HoraCalendario cal[][];
	
	public Calendario(int mes,int anio){
		this.mes=mes;
		this.anio=anio;
		numDias=dameDias(mes); 
		cal=new HoraCalendario[numDias][24];//el calenadario tendra un numero de dias dependiendo del mes y de 0 a 23 horas
		//ahora rellenamos el calenadrio
		for(int i=0;i<numDias;i++){
			for(int j=0;j<24;j++){
				//Aqui debemos rellenar los datos accediendo a las base de datos
			}
		}
		
	}
	public int dameDias(int mes){//esta funcion devuelve el numero de dias del mes
		if(mes == (1|3|5|7|8|10|12)){
			return 31;
		}
		if(mes == (4|6|9|11)){
			return 30;
		}else return 28;
		
		
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public int getNumDias() {
		return numDias;
	}
	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}
	public HoraCalendario[][] getCal() {
		return cal;
	}
	public void setCal(HoraCalendario[][] cal) {
		this.cal = cal;
	}
}
