package aplicacion;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.graphics.Color;

/**
 * Esta clase conecta el modelo (la base de datos) con la vista (los interfaces)
 * 
 * @author Todos
 */
public class Controlador {
	private Vista _vista;

	private Database db;

	private Empleado empleadoActual;

	public Controlador(Database baseDatos) {
		this.db = baseDatos;
	}

	/**
	 * Asigna el empleado que ha iniciado sesión.
	 * 
	 * @param emp el empleado que ha iniciado sesión
	 */
	public void setEmpleadoActual(int idEmp) {
		empleadoActual = this.getEmpleado(idEmp);
	}
	
	/**
	 * Asigna el empleado que ha iniciado sesión.
	 * 
	 * @param emp el empleado que ha iniciado sesión
	 */	
	public void setEmpleadoActual(Empleado emp) {
		empleadoActual = emp;
	}

	/**
	 * Devuelve el empleado que ha iniciado sesión.
	 * 
	 * @return el empleado que ha iniciado sesión.
	 */
	public Empleado getEmpleadoActual() {
		return empleadoActual;
	}

	public void incluyeVista(Vista vista) {
		this._vista = vista;
	}

	public Controlador(Vista vista, Database baseDatos) {
		this._vista = vista;
		this.db = baseDatos;
	}

/***************************************************************************
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
			ResultSet rs = db.dameEmpleado(idEmpl);
			rs.first();
			String nombre = rs.getString("Nombre");
			String apellido1 = rs.getString("Apellido1");
			String apellido2 = rs.getString("Apellido2");
			Date fechaNac = Util.stringADate(rs.getString("FechaNacimiento"));
			int id = rs.getInt("NumVendedor");
			String email = rs.getString("Email");
			String password = rs.getString("Password");
			String sex = rs.getString("Sexo");
			int sexo;
			if (sex.equalsIgnoreCase("femenino"))
				sexo = 0;
			else
				sexo = 1;
			String g = rs.getString("IndicadorGrupo");
			int grupo;
			if (g.equalsIgnoreCase("principiante"))
				grupo = 0;
			else
				grupo = 1;
			String rg = rs.getString("Rango");
			int rango;
			if (rg.equalsIgnoreCase("empleado"))
				rango = 1;
			else if (rg.equalsIgnoreCase("jefe"))
				rango = 2;
			else if (rg.equalsIgnoreCase("gerente"))
				rango = 3;
			else
				rango = 0;
			int idContrato=rs.getInt("IdContrato");
			Date fechaContrato = Util.stringADate(rs.getString("FechaContrato"));
			Date fechaAlta = Util.stringADate(rs.getString("FechaEntrada"));
			int dept=rs.getInt("IdDepartamento");
			Color color=null;
			int idSuperior=this.getIdSuperior(idEmpl);
			ArrayList<Integer> idSubordinados=this.getIdsSubordinados(idEmpl);
			ArrayList<Integer> idDepartamentos=this.getIdsDepartamentos(idEmpl);
			emp=new Empleado(idSuperior,id,nombre,apellido1,apellido2,fechaNac,sexo,
							 email,password,grupo,dept,rango,idContrato,fechaContrato,
							 fechaAlta,color,idSubordinados,idDepartamentos);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener el Empleado de la base de datos");
		}

		return emp;
	}
	
	/**
	 * 
	 * @param idEmpl	identificador del empleado
	 * @return
	 */
	private ArrayList<Integer> getIdsDepartamentos(int idEmpl) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param idEmpl identificador del empleado 
	 * @return		 los subordinados del empleado en cuestion
	 */
	private ArrayList<Integer> getIdsSubordinados(int idEmpl) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param idEmpl  el identificador del empleado o número de vendedor
	 * @return		  el identificador del superior del empleado
	 */
	private int getIdSuperior(int idEmpl) {
		// TODO Auto-generated method stub
		int idSup=0;
		ResultSet rs=db.obtenSuperior(idEmpl);
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
		String sexo = "Femenino";
		if (empleado.getSexo() == 1)
			sexo = "Masculino";
		String grupo = "Principiante";
		if (empleado.getGrupo() == 1)
			grupo = "Experto";
		return db.insertarUsuario(empleado.getIdEmpl(), empleado.getNombre(),
				empleado.getApellido1(), empleado.getApellido2(),
				aplicacion.Util.dateAString(empleado.getFechaNac()), sexo,
				empleado.getEmail(), empleado.getPassword(), grupo,
				"0000-00-00", "0000-00-00", 0, 0, "Empleado", 0, 0);
	}

/***************************************************************************
 * Métodos relacionados con departamentos
 */

	/**
	 * Carga un departamento desde la base de datos, dado su identificador.
	 * 
	 * @param id
	 *            el identificador del departamento
	 * @return una instancia del departamento cargado
	 */
	public Departamento getDepartamento(int id) {
		return null;
	}

	/**
	 * Guarda un departamento en la base de datos
	 * 
	 * @param departamento
	 *            el departamento a guardar
	 * @return <i>true</i> si el departamento ha sido insertado
	 */
	public boolean insertDepartamento(Departamento departamento) {
		return false;
	}

/***************************************************************************
 * Métodos relacionados con contratos
 */

	/**
	 * Carga un contrato desde la base de datos, dado su identificador.
	 * 
	 * @param id
	 *            el identificador del contrato
	 * @return uan instancia del contrato cargado
	 */
	// public Contrato getContrato(int id) {
	// return null;
	// }
	// TODO hacer insertarContrato

}
