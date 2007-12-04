package algoritmo;

import java.util.ArrayList;
import aplicacion.Empleado;

/**
 * 
 * @author madctol
 *
 */
public class Estructura {
	
	/**La primera dimensi�n representa el mes en cuesti�n (una posici�n por cada dia del mes)
	 * la segunda dimensi�n representa el horario de cada dia (los turnos)
	 */
	private ListasEmpleados[][] dias;
	
	/**
	 * Vector que guarda el horario de los turnos (uno m�s para saber el fin del �ltimo)
	 */
	//private Hora[numTrozos+1] horas;  
	
	/**
	 *  Referencia local a la lista de empleados
	 */
	private ArrayList<Empleado> personal;//aqui estar�n todos los empleados
	
	// lista con min/max de cada dia y cada hora
	//private Calendario cal 
	
	/**
	 *  Constructora de Estructura
	 */
	public Estructura(int numDias, ArrayList<Empleado> personal){
		this.personal = personal;
		// Calcular el numero de trozos en que se divide el horario
		// Recuperar de la base de datos la lista de todos los turnos del departamento
		// y ver en cuantos trozos vas a partir cada dia
		int numTrozos = 0;  
		dias = new ListasEmpleados[numDias][numTrozos];
		// Se podr�a no asignar listas a los dias que no se trabaja
		for (int i=0; i<numDias; i++)
			for (int j=0; j<numTrozos; j++)
				dias[i][j] = new ListasEmpleados();
	}

}
