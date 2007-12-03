package algoritmo;

import java.util.ArrayList;
import aplicacion.*;

public class Horario {
    private ListasEmpleados[] horario;
    private ArrayList<Empleado> personal;
    
    public Horario(int npartes, ArrayList<Empleado> personal){
    	horario = new ListasEmpleados[npartes];
    	for (int i=0; i<npartes; i++)
    		horario[i] = new ListasEmpleados();
    		
    }
}
