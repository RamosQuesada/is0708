package aplicacion;

import java.util.ArrayList;

/**
 * Clase que representa un departamento
 * @author David Rodilla Rodríguez
 */
public class Departamento {
	/** @deprecated */
	// TODO quitar esta clase
	private class Calendario {
		
	};
	
	/**
	 * Nombre del departamento
	 */
	private String _nombreDepartamento;
	/**
	 * Numero asociado del departamento
	 */
	private int _numeroDepartamento;
	/**
	 * Jefe del departamento
	 */
	private Empleado _jefeDepartamento;
	
	/**
	 * Conjunto de empleados que trabajan para el departamento
	 */
	private ArrayList<Empleado> _listaEmpleados;
	private Calendario _calendario;
	
	/**
	 * Constructor que se obtiene cuando se carga de la base de datos
	 * con todos los datos del departamento
	 * @param nombreDepartamento Nombre del departamento
	 * @param numeroDepartamento Numero del departamento
	 * @param jefeDepartamento Jefe del departamento
	 * @param listaEmpleados lista de empleados del departamento
	 * @param calendario calendario de festivos del departamento
	 */
	public Departamento(String nombreDepartamento, int numeroDepartamento,
			Empleado jefeDepartamento,ArrayList<Empleado> listaEmpleados,Calendario calendario){
		this._numeroDepartamento=numeroDepartamento;
		this._jefeDepartamento=jefeDepartamento;
		this._nombreDepartamento=nombreDepartamento;
		this._listaEmpleados=listaEmpleados;
		this._calendario=calendario;
	}
	
	
	/**
	 * Constructor en el que no importan los empleados, se usa cuando se crea un 
	 * departamento
	 * @param nombreDepartamento Nombre del departamento
	 * @param numeroDepartamento Numero del departamento
	 * @param jefeDepartamento Jefe del departamento
	 */
	public Departamento(String nombreDepartamento, int numeroDepartamento,
			Empleado jefeDepartamento){
		this._numeroDepartamento=numeroDepartamento;
		this._jefeDepartamento=jefeDepartamento;
		this._nombreDepartamento=nombreDepartamento;
		this._listaEmpleados=null;
	}
	
	/**
	 * Getter del nombre del departamento 
	 * @return nombre del departamento
	 */
	public String getNombreDepartamento(){
		return _nombreDepartamento;
	}
	/**
	 * Setter del nombre del departamento 
	 * @param String con el nombre del departamento
	 */
	public void setNombreDepartamento(String nombreDep){
		this._nombreDepartamento=nombreDep;
	}
	
	/**
	 * Getter del jefe de departamento
	 * @return 
	 */
	public Empleado getJefeDepartamento(){
		return _jefeDepartamento;
	}
	

	
	/**
	 * Getter de la lista de empleados
	 * @return ArrayList<Empleado> lista de empleados del departamento
	 */
	public ArrayList<Empleado> _listaEmpleados(){
		return _listaEmpleados;
	}
	
	/**
	 * Getter del numero asociado del departamento
	 * @return Integer numero asociado
	 */
	public int getNumeroDepartamento(){
		return _numeroDepartamento;
	}
	/**
	 * Setter del numero del departamento 
	 * @param Entero con el numero del departamento
	 */
	public void setNumeroDepartamento(int num){
		this._numeroDepartamento=num;
	}
	
	
}
