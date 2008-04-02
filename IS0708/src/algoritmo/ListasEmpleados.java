package algoritmo;

import java.util.ArrayList;
import aplicacion.*;
import aplicacion.datos.Empleado;

/**
 * @author madctol
 * Listas utilizadas en la clase Estructura
 */
public class ListasEmpleados {

	private ArrayList<Empleado> empleados;   //empleados que trabajan
    private ArrayList<Empleado> disponibles; //empleados disponibles
    private ArrayList<Empleado> reserva;     //empleados de reserva
    private String idDepartamento;           //identificador de departamento
    
    /**
     * Constructora por parámetros
     * @param id Identificador del departamento
     */
    public ListasEmpleados(String id){
    	this.empleados = new ArrayList<Empleado>();
    	this.disponibles = new ArrayList<Empleado>();
    	this.reserva = new ArrayList<Empleado>();
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
