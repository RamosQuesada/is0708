package algoritmo;

import java.util.ArrayList;
import aplicacion.*;

public class ListasEmpleados {
	
	/**
	 * Empleados que trabajan
	 */
	private ArrayList<Empleado> empleados;
	
	/**
	 * Empleados disponibles
	 */
    private ArrayList<Empleado> disponibles;
    
    /**
     * Empleados en reserva (se calculará al finalizar)
     */
    private ArrayList<Empleado> reserva;
    
    /**
     * Constructora de ListasEmpleados
     */
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
