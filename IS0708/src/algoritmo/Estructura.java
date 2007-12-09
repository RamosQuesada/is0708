package algoritmo;

import java.util.ArrayList;
import aplicacion.Empleado;

/**
 * La primera dimensión representa el mes en cuestión (una posición por cada dia del mes)
 * la segunda dimensión representa el horario de cada dia (los turnos)
 * @author madctol
 *
 */
public class Estructura {
	

	private ListasEmpleados[][] dias;
	private ArrayList<Empleado> personal;//aqui estarán todos los empleados
	private Calendario cal;//calendario donde se almacena min/max perso,exp/inexp... 
	

	public Estructura(int mes,int anio,ArrayList<Empleado> personal){//constructora de la estructura
		this.personal = personal;
		int numTrozos = 0;  
		// Calcular el numero de trozos en que se divide el horario
		// Recuperar de la base de datos la lista de todos los turnos del departamento
		// y ver en cuantos trozos vas a partir cada dia
		int numDias=0;
		numDias=dameDias(mes);//calculamos el numero de dias
		dias = new ListasEmpleados[numDias][numTrozos];
		// Se podría no asignar listas a los dias que no se trabaja
		for (int i=0; i<numDias; i++){
			for (int j=0; j<numTrozos; j++){
				dias[i][j] = new ListasEmpleados();
			}
		}
		personal=new ArrayList<Empleado>();
		//rellenar lista empleados
		cal=new Calendario(numDias,mes,anio);//creamos calendario
	}
	public int dameDias(int mes){//esta funcion devuelve el numero de dias del mes
		if(mes == (1|3|5|7|8|10|12)){
			return 31;
		}
		if(mes == (4|6|9|11)){
			return 30;
		}else return 28;
	}
	
	public ListasEmpleados[][] getDias(){
		return dias;
	}


}
