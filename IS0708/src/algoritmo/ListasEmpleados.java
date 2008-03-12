package algoritmo;

import java.util.ArrayList;
import aplicacion.*;
/**
 * En esta clase estan las listas que utilizamos en la clase estructura
 * @author madctol
 */
public class ListasEmpleados {

	private ArrayList<Empleado> empleados;   //empleados que trabajan
    private ArrayList<Empleado> disponibles; //empleados disponibles
    private ArrayList<Empleado> reserva;     //empleados de reserva
    private String idDepartamento;           //identificador de departamento
    

    public ListasEmpleados(String id){
    	empleados = new ArrayList<Empleado>();
    	disponibles = new ArrayList<Empleado>();
    	// inicializar disponibles en cada turno
    	reserva = new ArrayList<Empleado>();
    	this.idDepartamento=id;
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

	public String getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	
}
