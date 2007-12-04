package algoritmo;

import java.util.ArrayList;
import aplicacion.*;

public class ListasEmpleados {
	private ArrayList<Empleado> empleados;
    private ArrayList<Empleado> disponibles;
    private ArrayList<Empleado> reserva;
    
    public ListasEmpleados(){
    	empleados = new ArrayList<Empleado>();
    	disponibles = new ArrayList<Empleado>();
    	// Inicializar disponibles en cada turno
    	reserva = new ArrayList<Empleado>();
    }

	public ArrayList<Empleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(ArrayList<Empleado> empleados) {
		this.empleados = empleados;
	}

	public ArrayList<Empleado> getDisponibles() {
		return disponibles;
	}

	public void setDisponibles(ArrayList<Empleado> disponibles) {
		this.disponibles = disponibles;
	}

	public ArrayList<Empleado> getReserva() {
		return reserva;
	}

	public void setReserva(ArrayList<Empleado> reserva) {
		this.reserva = reserva;
	}
    
    
    
}
