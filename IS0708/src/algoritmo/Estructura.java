package algoritmo;

import java.util.ArrayList;
import aplicacion.Empleado;
import aplicacion.Util;

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
		numDias=Util.dameDias(mes,anio);//calculamos el numero de dias
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
	
	public ListasEmpleados[][] getDias(){
		return dias;
	}


}
