package algoritmo;

import java.util.ArrayList;
import aplicacion.*;

public class TurnoMatic {
	
	private ListasEmpleados[][] dias;//Este sera el cuadrante
	//private Hora[numTrozos+1] horas;  // Vector guarda el horario de los turnos
	private ArrayList<Empleado> personal;//aqui estaran todos los empleados
	// lista con min/max de cada dia y cada hora
	
	public TurnoMatic(int numDias, ArrayList<Empleado> personal){
		this.personal = personal;
		// Calcular el numero de trozos en que se divide el horario
		// Recuperar de la base de datos la lista de todos los turnos del departamento
		// y ver en cuantos trozos vas a partir cada dia
		int numTrozos = 0; 
		dias = new ListasEmpleados[numDias][numTrozos];
		// Se podría no asignar listas a los dias que no se trabaja
		for (int i=0; i<numDias; i++)
			for (int j=0; j<numTrozos; j++)
				dias[i][j] = new ListasEmpleados();
	}
	
}
