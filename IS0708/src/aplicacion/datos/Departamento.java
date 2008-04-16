package aplicacion.datos;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Clase que representa un departamento
 * @author David Rodilla Rodríguez
 */
public class Departamento {

	/** Nombre del departamento */
	private String _nombreDepartamento;
	/** Numero asociado del departamento */
	private ArrayList<Integer> _numerosDepartamento;
	/** Jefe del departamento */
	private Empleado _jefeDepartamento;
	/** Conjunto de empleados que trabajan para el departamento */
	private ArrayList<Empleado> _listaEmpleados;
	/** Hora en la que abre el departamento */
	private Time _horaApertura;
	/** Hora en la que cierra el departamento */
	private Time _horaCierre;
	
	/**
	 * Constructor que se obtiene cuando se carga de la base de datos
	 * con todos los datos del departamento
	 * @param nombreDepartamento Nombre del departamento
	 * @param jefeDepartamento Jefe del departamento
	 * @param listaEmpleados lista de empleados del departamento
	 */
	public Departamento(String nombreDepartamento, int numeroDepartamento,
			Empleado jefeDepartamento, ArrayList<Empleado> listaEmpleados,
			Time horaApertura, Time horaCierre){
		_numerosDepartamento = new ArrayList<Integer>();
		_numerosDepartamento.add(numeroDepartamento);
		_jefeDepartamento = jefeDepartamento;
		_nombreDepartamento = nombreDepartamento;
		_listaEmpleados = listaEmpleados;
		_horaApertura = horaApertura;
		_horaCierre = horaCierre;
	}
	
	/**
	 * Constructor en el que no importan los empleados, se usa cuando se crea un 
	 * departamento
	 * @param nombreDepartamento Nombre del departamento
	 * @param numeroDepartamento Numero del departamento
	 * @param jefeDepartamento Jefe del departamento
	 */
	public Departamento(String nombreDepartamento, int numeroDepartamento,
			Empleado jefeDepartamento,
			Time horaApertura, Time horaCierre){
		_numerosDepartamento = new ArrayList<Integer>();
		_numerosDepartamento.add(numeroDepartamento);
		_jefeDepartamento=jefeDepartamento;
		_nombreDepartamento=nombreDepartamento;
		_listaEmpleados=null;
		_horaApertura = horaApertura;
		_horaCierre = horaCierre;
	}
	
	/**
	 * Cambia el jefe del departamento
	 * @param id Nuevo jefe del departamento
	 */
	public void setJefeDepartamento(Empleado empl){
		_jefeDepartamento = empl;
		 
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
	public ArrayList<Empleado> getlistaEmpleados(){
		return _listaEmpleados;
	}
	
	/**
	 * Getter del numero asociado del departamento
	 * @return Integer numero asociado
	 */
	public ArrayList<Integer> getNumerosDepartamento(){
		return _numerosDepartamento;
	}

	/**
	 * Añade un número al departamento 
	 * @param Entero con el numero del departamento a añadir
	 */
	public void addNumeroDepartamento(int num){
		_numerosDepartamento.add(num);
	}

	/**
	 * Getter de la hora de apertura
	 * @return la hora de apertura
	 */
	public Time getHoraApertura() {
		return _horaApertura;
	}

	/**
	 * Modifica la hora de apertura
	 * @param apertura La nueva hora de apertura
	 */
	public void setHoraApertura(Time apertura) {
		_horaApertura = apertura;
	}

	/**
	 * Consulta la hora de cierre
	 * @return la hora de cierre
	 */
	public Time getHoraCierre() {
		return _horaCierre;
	}

	/**
	 * Modifica la hora de cierre
	 * @param cierre La nueva hora de cierre
	 */
	public void setHoraCierre(Time cierre) {
		_horaCierre = cierre;
	}

}
