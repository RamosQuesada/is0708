package aplicacion;

import java.util.ArrayList;

/**
 * Esta clase conecta el modelo (la base de datos) con la vista (los interfaces)
 * @author David Rodilla y Daniel Dionne
 */
public class Controlador {
	private Vista _vista;
	private Database _baseDatos;
	private Empleado empleadoActual;
	
	public Controlador(Database baseDatos, int idEmp){
		this._baseDatos=baseDatos;
		setEmpleadoActual(idEmp);
	}
	
	/**
	 * Asigna el empleado que ha iniciado sesión.
	 * @param emp el empleado que ha iniciado sesión.
	 */
	public void setEmpleadoActual(int idEmp) {
		empleadoActual = _baseDatos.dameEmpleado(idEmp);
	}
	
	/**
	 * Devuelve el empleado que ha iniciado sesión.
	 * @return el empleado que ha iniciado sesión.
	 */
	public Empleado getEmpleadoActual() {
		return empleadoActual;
	}
	public void incluyeVista(Vista vista){
		this._vista=vista;
	}
	
	public Controlador(Vista vista,Database baseDatos){
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
		_baseDatos.dameEmpleado(idEmpl);
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
	public ArrayList<Empleado> getEmpleado(Integer idEmpl, Integer idDpto, Integer idContrato, String nombre, String apellido1, String apellido2) {
		return null;
	}

	/**
	 * Inserta un empleado en la base de datos.
	 * @param empleado el empleado a insertar
	 * @return <i>true</i> si el empleado ha sido insertado
	 */
	public boolean insertEmpleado(Empleado empleado) {
		return false;
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
	 * @return <i>true</i> si el departamento ha sido insertado
	 */
	public boolean insertDepartamento(Departamento departamento){
		return false;
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
