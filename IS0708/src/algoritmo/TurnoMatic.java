package algoritmo;

import java.util.ArrayList;
import aplicacion.*;

public class TurnoMatic {
	
	private ListasEmpleados[][] dias;//Este sera el cuadrante
	private ArrayList<Empleado> personal;//aqui estaran todos los empleados
	
	public TurnoMatic(int numDias, ArrayList<Empleado> personal){
		this.personal = personal;
		// Calcular el numero de trozos en que se divide el horario
		// hace falta saber como se van a representar los turnos
		int numTrozos = 0; // Llamaría a la función para calcular el numero de trozos
		dias = new ListasEmpleados[numDias][numTrozos];
		for (int i=0; i<numDias; i++)
			for (int j=0; j<numTrozos; j++)
				dias[i][j] = new ListasEmpleados();
	}
	
}
