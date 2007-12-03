package algoritmo;

import java.util.ArrayList;
import aplicacion.*;

public class TurnoMatic {
	
	private Horario[] dias;//Este sera el cuadrante
	private ArrayList<Empleado> personal;//aqui estaran todos los empleados
	
	public TurnoMatic(int numDias, ArrayList<Empleado> personal){
		this.personal = personal;
		dias = new Horario[numDias];
		// Calcular el numero de trozos en que se divide el horario
		// hace falta saber como se van a representar los turnos
		int numTrozos = 0; // Llamaría a la función para calcular el numero de trozos
		for (int i=0; i<numDias; i++)
			dias[i] = new Horario(numTrozos, personal);
	}
	
}
