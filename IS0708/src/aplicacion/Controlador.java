package aplicacion;

import interfaces.I02_Menu_principal;
import java.util.ArrayList;

/**
 * Esta clase conecta el modelo (la base de datos) con la vista (los interfaces)
 * @author David Rodilla y Daniel Dionne
 *
 */
public class Controlador {
	private I02_Menu_principal _vista;
	private Database _baseDatos;
	
	public Controlador(Database baseDatos){
		this._baseDatos=baseDatos;
	}
	
	public void incluyeVista(I02_Menu_principal vista){
		this._vista=vista;
	}
	
	public Controlador(I02_Menu_principal vista,Database baseDatos){
		this._vista=vista;
		this._baseDatos=baseDatos;
	}
/****************************************************************************
 * Métodos relacionados con empleados
 */
	
	/**
	 * Carga un empleado desde la base de datos, dado su número de vendedor o identificador.
	 * @param idEmpl el identificador del empleado o número de vendedor
	 * @return una instancia nueva del empleado
	 */
	public Empleado getEmpleado(int idEmpl) {
		return null;
	}

	/**
	 * Carga uno o varios empleados desde la base de datos, que coincidan con los datos
	 * dados del mismo. Los parámetros pueden ser nulos.
	 * @param idEmpl		el identificador del empleado
	 * @param idDpto		el identificador del departamento al que pertenece
	 * @param idContrato	el identificador del contrato que tiene
	 * @param nombre		el nombre del empleado
	 * @param apellido1		el primer apellido del empleado
	 * @param apellido2		el segundo apellido del empleado
	 * @return una lista de empleados que coincida con los datos dados
	 */
	public ArrayList<Empleado> getEmpleado(int idEmpl, int idDpto, int idContrato, String nombre, String apellido1, String apellido2) {
		return null;
	}
	

/****************************************************************************
 * Métodos relacionados con departamentos
 */
	
	/**
	 * Carga un departamento desde la base de datos, dado su identificador.
	 * @param id el identificador del departamento
	 * @return una instancia del departamento cargado
	 */
	public Departamento getDepartamento(int id){
		return null;
	}
	
	/**
	 * Guarda un departamento en la base de datos
	 * @param departamento el departamento a guardar
	 */
	public void insertDepartamento(Departamento departamento){
		
	}
	
/****************************************************************************
 * Métodos relacionados con contratos
 */
	
	/**
	 * Carga un contrato desde la base de datos, dado su identificador.
	 * @param id el identificador del contrato
	 * @return uan instancia del contrato cargado
	 */
	//public Contrato getContrato(int id) {
	//	return null;
	//}
	
	// TODO hacer insertarContrato


}
