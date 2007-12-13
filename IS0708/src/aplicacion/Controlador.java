package aplicacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import org.eclipse.swt.graphics.Color;

import algoritmo.HoraCalendario;

/**
 * Esta clase conecta el modelo (la base de datos) con la vista (los interfaces)
 * 
 * @author Todos
 */
public class Controlador {
	private Vista _vista;
	private Database _db;
	private Empleado _empleadoActual;

	
	public Controlador(Database baseDatos) {
		this._db = baseDatos;
	}

	/**
	 * Asigna el empleado que ha iniciado sesión.
	 * 
	 * @param emp el empleado que ha iniciado sesión
	 */
	public void setEmpleadoActual(int idEmp) {
		_empleadoActual = this.getEmpleado(idEmp);
	}
	
	/**
	 * Asigna el empleado que ha iniciado sesión.
	 * 
	 * @param emp el empleado que ha iniciado sesión
	 */	
	public void setEmpleadoActual(Empleado emp) {
		_empleadoActual = emp;
	}

	/**
	 * Devuelve el empleado que ha iniciado sesión.
	 * 
	 * @return el empleado que ha iniciado sesión.
	 */
	public Empleado getEmpleadoActual() {
		return _empleadoActual;
	}

	public void incluyeVista(Vista vista) {
		this._vista = vista;
	}

	public Controlador(Vista vista, Database baseDatos) {
		this._vista = vista;
		this._db = baseDatos;
	}

	/******************************************************************************************
 * Métodos relacionados con empleados
 */

	/**
	 * Carga un empleado desde la base de datos, dado su número de vendedor o
	 * identificador.
	 * 
	 * @param idEmpl
	 *            el identificador del empleado o número de vendedor
	 * @return una instancia nueva del empleado
	 */
	public Empleado getEmpleado(int idEmpl) {
		Empleado emp = null;
		try {
			ResultSet rs = _db.dameEmpleado(idEmpl);
			rs.first();
			String nombre = rs.getString("Nombre");
			String apellido1 = rs.getString("Apellido1");
			String apellido2 = rs.getString("Apellido2");
			Date fechaNac = rs.getDate("FechaNacimiento");
			int id = rs.getInt("NumVendedor");
			String email = rs.getString("Email");
			String password = rs.getString("Password");
			int sexo = rs.getInt("Sexo");			
			int grupo=rs.getInt("IndicadorGrupo");;			
			String rg = rs.getString("Rango");
			int rango=rs.getInt("Rango");			
			int idContrato=rs.getInt("IdContrato");
			Date fechaContrato = rs.getDate("FechaContrato");
			Date fechaAlta = rs.getDate("FechaEntrada");
			Color color=null;
			int idSuperior=this.getIdSuperior(idEmpl);
			ArrayList<Integer> idSubordinados=this.getIdsSubordinados(idEmpl);
			ArrayList<Integer> idDepartamentos=this.getIdsDepartamentos(idEmpl);
			int felicidad=rs.getInt("Felicidad");
			int idioma=rs.getInt("Idioma");
			emp=new Empleado(idSuperior,id,nombre,apellido1,apellido2,fechaNac,sexo,
							 email,password,grupo,rango,idContrato,fechaContrato,
							 fechaAlta,color,idSubordinados,idDepartamentos,felicidad,idioma);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener el Empleado de la base de datos");
		}

		return emp;
	
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @return Devuelve lista de los turnos de los empleados en un ArrayList
	 */
	public ArrayList<Turno> getTurnosEmpleados() {
        ArrayList<Turno> turnos= new ArrayList<Turno>();
		try {
			ResultSet rs = _db.dameListaTurnosEmpleados();
			while(rs.next()){
			String idTurn = rs.getString("IdTurno");
			String descr = rs.getString("Descripcion");
			Time HoraE = rs.getTime("HoraEntrada");
			Time HoraS = rs.getTime("HoraSalida");
			Time HoraI = rs.getTime("HoraInicioDescanso");
			//duracion del descanso como Time en la BBDD???
			//las funciones que calculan la hora, minutos y segundos estan depreciadas aqui en JAVA
			Time duracion = rs.getTime("DuracionDescanso");
			//paso el Time a String y me quedo con el substring de la franja de los minutos HH:MM:SS
			String duracionStr = duracion.toString().substring(3,5);
			//lo paso a int (que es como esta duracionDescanso en JAVA)
			Integer duracionInt= (int)Integer.valueOf(duracionStr);
			Turno t = new Turno(idTurn,descr,HoraE,HoraS,HoraI,duracionInt);
			turnos.add(t);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener Lista de Turnos en la base de datos");
		}
		
	return  turnos;
	}
	
	/**
	 * Metodo que devuelve los departamentos a los que pertenece el empleado
	 * @param idEmpl	identificador del empleado
	 * @return			los departamentos a los que pertenece el empleado
	 */
	private ArrayList<Integer> getIdsDepartamentos(int idEmpl) {
		// TODO Auto-generated method stub
		ArrayList<Integer> depts=new ArrayList<Integer>();
		ResultSet rs=db.obtenIdsDepartamentos(idEmpl);
		try {
			while (rs.next()) {
				int idDept = rs.getInt(0);
				depts.add(idDept);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener Lista de Departamentos en la base de datos");
		}		
		return depts;
	}

	/**
	 * Metodo que obtiene los subordinados del empleado si los tuviera
	 * @param idEmpl identificador del empleado 
	 * @return		 los subordinados del empleado en cuestion
	 */
	private ArrayList<Integer> getIdsSubordinados(int idEmpl) {
		// TODO Auto-generated method stub
		ArrayList<Integer> subs=new ArrayList<Integer>();
		ResultSet rs=db.obtenIdsSubordinados(idEmpl);
		try {
			while (rs.next()) {
				int idSub = rs.getInt(0);
				subs.add(idSub);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener Lista de Departamentos en la base de datos");
		}		
		return subs;
	}

	/**
	 * Metodo que obtiene el superior de un empleado dado(si es 0 no tiene superior)
	 * @param idEmpl  el identificador del empleado o número de vendedor
	 * @return		  el identificador del superior del empleado
	 */
	private int getIdSuperior(int idEmpl) {
		// TODO Auto-generated method stub
		int idSup=0;
		ResultSet rs=_db.obtenSuperior(idEmpl);
		try {
			idSup = rs.getInt("JefeEmpleado");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener el superior de la base de datos");
		}
		return idSup;
	}

	
	/**
	 * Carga uno o varios empleados desde la base de datos, que coincidan con
	 * los datos dados. Los parámetros pueden ser nulos.
	 * 
	 * @param idEmpl
	 *            el identificador del empleado
	 * @param idDpto
	 *            el identificador del departamento al que pertenece
	 * @param idContrato
	 *            el identificador del contrato que tiene
	 * @param nombre
	 *            el nombre del empleado
	 * @param apellido1
	 *            el primer apellido del empleado
	 * @param apellido2
	 *            el segundo apellido del empleado
	 * @return una lista de empleados que coincida con los datos dados
	 */
	public ArrayList<Empleado> getEmpleado(Integer idEmpl, Integer idDpto,
			Integer idContrato, String nombre, String apellido1,
			String apellido2, Integer rango) {
		return null;
	}

	/**
	 * Inserta un empleado en la base de datos.
	 * 
	 * @param empleado
	 *            el empleado a insertar
	 * @return <i>true</i> si el empleado ha sido insertado
	 */
	public boolean insertEmpleado(Empleado empleado) {		
		int sexo=empleado.getSexo();
		int grupo=empleado.getGrupo();
		return _db.insertarUsuario(empleado.getIdEmpl(), empleado.getNombre(),
				empleado.getApellido1(), empleado.getApellido2(),
				empleado.getFechaNac(), sexo,
				empleado.getEmail(), empleado.getPassword(), grupo,
				Date.valueOf("0000-00-00"), Date.valueOf("0000-00-00"), 0,0,0,1, 0, 0);
	}

/******************************************************************************************
 * Métodos relacionados con departamentos
 */

	/**
	 * Carga un departamento desde la base de datos, dado su nombre.
	 * 
	 * @param id	el nombre del departamento
	 * @return		una instancia del departamento cargado
	 */
	public Departamento getDepartamento(String id) {
		return null;
	}

	/**
	 * Guarda un departamento en la base de datos
	 * 
	 * @param departamento el departamento a guardar
	 * @return <i>true</i> si el departamento ha sido insertado
	 */
	public boolean insertDepartamento(Departamento departamento) {
		return false;
	}
	/**
	 * Metodo que a partir de un identificador de departamento y un dia de la semana (entero) nos devuelve 
	 * una lista dividida en horas con sus correspondientes limites de numero de empleados maximo y minimo. 
	 * @param idDepartamento
	 * @param Fecha
	 * @return devuelve un arraylist donde cada elemento es un vector de tres dimensiones de tal forma que
	 * vector[0]= Hora
	 * vector[1]= numero minimo de empleados para esa hora
	 * vector[2]= numero maximo de empleados para esa hora
	 */
	public ArrayList<Integer[]> getDistribucion (int idDepartamento, String Fecha){
		ArrayList lista= new ArrayList();		
		ResultSet r;
		int[] vector = new int[3];
		r=_db.obtenFestivos(idDepartamento, Fecha);
		//se ha supuesto que fecha esta en formato string
		try{
			r.last();
			if (r.getRow()>0){
				r.beforeFirst();
				while (r.next()){					
					vector[0]=r.getInt("Hora");
					vector[1]=r.getInt("NumMin");
					vector[2]=r.getInt("NumMax");
					lista.add(vector);
				}
			}else{
				
				
				Date d=Date.valueOf(Fecha);
				int diaSemana=d.getDay();
				
				r=_db.obtenDistribucion(idDepartamento, diaSemana);
				r.last();
				if (r.getRow()>0){
					r.beforeFirst();
					while (r.next()){
						vector[0]=r.getInt("Hora");
						vector[1]=r.getInt("NumMin");
						vector[2]=r.getInt("NumMax");
						lista.add(vector);
					}
				}
			}
		
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error al realizar la consulta de los festivos ");
		}
		return lista;
	}


/******************************************************************************************
 * Métodos relacionados con mensajes
 */
	
	/**
	 * Obtiene una lista de <i>b</i> mensajes entrantes por orden cronológico, del más
	 * nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * @param idEmpl el empleado destinatario de los mensajes
	 * @param a mensaje por el que empezar, siendo 1 el más reciente
	 * @param b cuántos mensajes coger
	 * @return la lista de mensajes
	 */
	public ArrayList<Mensaje> getMensajesEntrantes(int idEmpl, int a, int b){
		return null;
	}

	/**
	 * Obtiene una lista de <i>b</i> mensajes salientes por orden cronológico, del más
	 * nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * @param idEmpl el empleado remitente de los mensajes
	 * @param a mensaje por el que empezar, siendo 1 el más reciente
	 * @param b cuántos mensajes coger
	 * @return la lista de mensajes
	 */
	public ArrayList<Mensaje> getMensajesSalientes(int idEmpl, int a, int b){
		return null;
	}
	
	public ArrayList<Mensaje> getMensajes(int id) {
		// TODO BD Funcion q devuelva todos los mensajes de este usuario en un ArrayList
		// Dani: ¿De verdad hacer falta recuperar todos todos los mensajes a la vez?
		return null;
	}

/******************************************************************************************
 * Métodos relacionados con contratos
 */

	/**
	 * Carga un contrato desde la base de datos, dado su identificador.
	 * @param id	el identificador del contrato
	 * @return		una instancia del contrato cargado
	 */
	public Contrato getContrato(int id) {
		// TODO BD
	  return null;
	}
	
	/**
	 * Inserta un contrato en la base de datos.
	 * @param c		el contrato a insertar
	 * @return		<i>true</i> si se ha insertado el contrato correctamente
	 */
	public boolean insertContrato(Contrato c) {
		// TODO BD
		return false;
	}

	
/******************************************************************************************
 * Otros métodos 
 */

	/**
	 * Informa del progreso actual de la tarea en el interfaz
	 * @param mensaje el mensaje a mostrar
	 * @param i el progreso, de 0 a 100
	 */
	public void setProgreso(String mensaje, int i) {
		_vista.setProgreso(mensaje, i);
	}
}