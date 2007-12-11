package algoritmo;
import java.util.ArrayList;

/**
 * Esta clase representa a un calendario donde vamos a almacenar los dias festivos,el
 * max/min numero de empleados y el numero de expertos e inexpertos
 * @author DavidMartin
 */
public class Calendario {
	
	private int mes;
	private int anio;
	private int numDias;//numero de dias del mes
	private HoraCalendario cal[][];//esta matriz contiene para cada dia y para cada hora un objeto de la clase Horacalendario
	
	public Calendario(){//constructora por defecto
		
	}
	public Calendario(int numDias,int mes,int anio){
		this.mes=mes;
		this.anio=anio;
		this.numDias=numDias;
		cal=new HoraCalendario[numDias][24];//el calenadario tendra un numero de dias dependiendo del mes y de 0 a 23 horas
		//ahora rellenamos el calenadrio
		for(int i=0;i<numDias;i++){
			for(int j=0;j<24;j++){
				//Aqui debemos rellenar los datos accediendo a las base de datos
				//PRUEBA
				cal[i][j]=new HoraCalendario(4,0,0,0);
			}
		}
		
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
