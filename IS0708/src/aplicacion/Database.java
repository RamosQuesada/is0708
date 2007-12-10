package aplicacion;

import java.sql.*;

/**
 * Aqu� se encuentran los m�todos de acceso a la base de datos.
 * 
 * @author Camilo & chema
 * 
 */
public class Database extends Thread {
	Connection con;

	Statement st;

	ResultSet rs;

	public void run() {
		abrirConexion();
	}

	// Tras una hora de pelea con Eclipse y MySQL, por fin tenemos
	// conexion con la BD ;)
	/**
	 * Abre una conexi�n nueva con la base de datos
	 */
	public synchronized void abrirConexion() {
		try {
			String userName = "turnomat_user";
			String password = "is0708";
			String bd = "turnomat_bd";
			String url = "jdbc:mysql://72.34.56.241:3306/" + bd;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("Conexi�n a la BD");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en conexi�n");
		}
	}

	/**
	 * Cierra la conexi�n con la base de datos
	 * 
	 */
	public void cerrarConexion() {
		try {
			con.close();
			System.out.println("Conexion Cerrada Correctamente");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error cerrando la conexion");
		}
	}

	public Empleado dameEmpleado(int nvend) {

		// Pongo return null para que no diga que hay un error
		return null;

	}

	/**
	 * M�todo que inserta en la tabla usuario los valores correspondientes a un nuevo usuario
	 * @param id
	 *            Es el identificador �nico de cada empleado, que se corresponde
	 *            con la clave primaria en la tabla.
	 * @param nombre
	 *            Nombre del empleado
	 * @param apellido1
	 *            Primer apellido
	 * @param apellido2
	 *            Segundo apellido
	 * @param fechaNac
	 *            Fecha de nacimiento
	 * @param sexo
	 *            Sexo del empleado
	 * @param email
	 *            Representa el correo electr�nico del empleado
	 * @param password
	 *            Se corresponde con la contrase�a para autenticarse en el
	 *            sistema
	 * @param indicadorGrupo
	 *            Clasifica al usuario seg�n su antig�edad en la empresa
	 * @param fechaContrato
	 *            Fecha del primer dia en la que el usuario trabaja con el tipo
	 *            de contrato actual
	 * @param fechaEntrada
	 *            Fecha de alta en la empresa
	 * @param horasExtras
	 *            Lleva en cuenta el numero de horas extras. Debe ser un entero
	 *            para saber cuando debe horas o le deben horas
	 * @param idDept
	 *            Hace referencia a la secci�n dentro del departamento en
	 *            cuesti�n
	 * @param rango
	 *            Posici�n del empleado en la empresa
	 * @param idContrato
	 *            Hace referencia al tipo de contrato
	 * @param idTurno
	 *            Turno preferido por el empleado
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarUsuario(int id, String nombre, String apellido1,
			String apellido2, Date fechaNac, String sexo, String email,
			String password, String indicadorGrupo, Date fechaContrato,
			Date fechaEntrada, int horasExtras, int idDept, String rango,
			int idContrato, int idTurno) {
		boolean error = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO usuario values ('" + id + "', '"
					+ nombre + "', '" + apellido1 + "' ,'" + apellido2 + "','"
					+ fechaNac + "','" + sexo + "','" + email + "','"
					+ password + "','" + indicadorGrupo + fechaContrato + "','"
					+ fechaEntrada + "','" + horasExtras + "','" + idDept
					+ "','" + rango + "','" + idContrato + "','" + idTurno
					+ "')");
			System.out.println("Usuario insertado");
			error = true;
		} catch (SQLException e) {
			error = false;
		}
		return error;
	}

	/**
	 * M�todo que inserta en la tabla Departamento los valores correspondientes a un nuevo departamento
	 * @param nombre	Nombre representativo de las actividades llevadas a cabo dentro del departamento 
	 * @param jefe		Persona que dirige le departamento
	 * @return			Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarDepartamento(String nombre,int jefe) {
		boolean error = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO departamento values ('" + nombre + "', '" + jefe + "')");
			System.out.println("Departamento insertado");
			error = true;
		} catch (SQLException e) {
			error = false;
		}
		return error;
	}
	
	/**
	 * M�todo que inserta en la tabla numerosDpto los valores correspondientes
	 * @param numero	Numero del subdepartamento
	 * @param nombre	Nombre del departamento
	 * @return			Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarNumerosDepartamento(String numero,String nombre) {
		boolean error = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO numerosDpto values ('" + numero + "', '" + nombre + "')");
			System.out.println("Departamento insertado");
			error = true;
		} catch (SQLException e) {
			error = false;
		}
		return error;
	}

	public static void main(String[] IS0708) {
		// @SuppressWarnings("unused")
		Database prueba = new Database();
		prueba.abrirConexion();
		prueba.cerrarConexion();
	}
}
