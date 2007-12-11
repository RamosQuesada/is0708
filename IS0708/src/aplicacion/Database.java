package aplicacion;

import java.sql.*;

/**
 * Aquí se encuentran los métodos de acceso a la base de datos.
 * 
 * @author Camilo & chema & Agustín
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
	 * Abre una conexión nueva con la base de datos
	 */
	public synchronized void abrirConexion() {
		try {
			String userName = "turnomat_user";
			String password = "is0708";
			String bd = "turnomat_bd";
			String url = "jdbc:mysql://72.34.56.241:3306/" + bd;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("Conexión a la BD");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en conexión");
		}
	}

	/**
	 * Cierra la conexión con la base de datos
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

	/**
	 * Comprueba si hay una conexión abierta con la base de datos
	 * @return	<i>true</i> si la conexión está abierta, <i>false</i> en caso
	 * 			contrario o si hay alguna excepción
	 * @author Daniel Dionne
	 */
	public boolean conexionAbierta() {
		boolean b = false;
		try {
			b = !con.isClosed();
		} catch (Exception e) {}
		return b;
	}
	
	public Empleado dameEmpleado(int nvend) {

		// Pongo return null para que no diga que hay un error
		return null;

	}

	/**
	 * Método que inserta en la tabla usuario los valores correspondientes a un nuevo usuario
	 * @param id
	 *            Es el identificador único de cada empleado, que se corresponde
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
	 *            Representa el correo electrónico del empleado
	 * @param password
	 *            Se corresponde con la contraseña para autenticarse en el
	 *            sistema
	 * @param indicadorGrupo
	 *            Clasifica al usuario según su antigüedad en la empresa
	 * @param fechaContrato
	 *            Fecha del primer dia en la que el usuario trabaja con el tipo
	 *            de contrato actual
	 * @param fechaEntrada
	 *            Fecha de alta en la empresa
	 * @param horasExtras
	 *            Lleva en cuenta el numero de horas extras. Debe ser un entero
	 *            para saber cuando debe horas o le deben horas
	 * @param idDept
	 *            Hace referencia a la sección dentro del departamento en
	 *            cuestión
	 * @param rango
	 *            Posición del empleado en la empresa
	 * @param idContrato
	 *            Hace referencia al tipo de contrato
	 * @param idTurno
	 *            Turno preferido por el empleado
	 * @return Informa sobre si se ha podido realizar la inserción o no
	 */
	public boolean insertarUsuario(int id, String nombre, String apellido1,
			String apellido2, Date fechaNac, String sexo, String email,
			String password, String indicadorGrupo, Date fechaContrato,
			Date fechaEntrada, int horasExtras, int idDept, String rango,
			int idContrato, int idTurno) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO USUARIO values ('" + id + "', '"
					+ nombre + "', '" + apellido1 + "' ,'" + apellido2 + "','"
					+ fechaNac + "','" + sexo + "','" + email + "','"
					+ password + "','" + indicadorGrupo + fechaContrato + "','"
					+ fechaEntrada + "','" + horasExtras + "','" + idDept
					+ "','" + rango + "','" + idContrato + "','" + idTurno
					+ "')");
			System.out.println("Usuario insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Método que inserta en la tabla Departamento los valores correspondientes a un nuevo departamento
	 * @param nombre	Nombre representativo de las actividades llevadas a cabo dentro del departamento 
	 * @param jefe		Persona que dirige le departamento
	 * @return			Informa sobre si se ha podido realizar la inserción o no
	 */
	public boolean insertarDepartamento(String nombre,int jefe) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO departamento values ('" + nombre + "', '" + jefe + "')");
			System.out.println("Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	
	/**
	 * Método que inserta en la tabla numerosDpto los valores correspondientes
	 * @param numero	Numero del subdepartamento
	 * @param nombre	Nombre del departamento
	 * @return			Informa sobre si se ha podido realizar la inserción o no
	 */
	public boolean insertarNumerosDepartamento(String numero,String nombre) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO numerosDpto values ('" + numero + "', '" + nombre + "')");
			System.out.println("Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	/**
	 * Método que inserta en la tabla Distribucion los valores correspondientes a una nueva distribución
	 * @param Hora    Franja horaria  dividida en unidades de una hora (por ej. De 9:00 – 10:00) representado por la hora de inicio (ej. 9)
	 *            
	 * @param DiaSemana  Dia (Lunes, Martes,...,Domingo) en el que se aplica la distribución
	 *            
	 * @param Patrón   Nos dice cómo se distribuyen los grupos (expertos y novatos)
	 *           
	 * @param NumMax   Nos acota el número máximo de trabajadores requeridos dicho día en una cierta franja horaria 

	 * @param NumMin  Nos acota el número mínimo de trabajadores requeridos dicho día en una cierta franja horaria 
	 * 
	 * @param IdDepartamento  identificador del dpto.
	
	 * @return Informa sobre si se ha podido realizar la inserción o no
	 */
public boolean insertarDistribucion(int Hora,String DiaSemana,String Patron,int NumMax, int NumMin,int IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO distribucion values ('" + Hora + "', '" + DiaSemana + "', '" + Patron + "', '" +NumMax+ "', '" +NumMin+ "', '" +IdDepartamento+ "')");
			System.out.println("Distribucion insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Método que inserta en la tabla Distribucion los valores correspondientes a una nueva distribución
	 * @param Hora    Franja horaria  dividida en unidades de una hora (por ej. De 9:00 – 10:00) representado por la hora de inicio (ej. 9)
	 *            
	 * @param FechaInicio   Fecha de Inicio de la distribución especial para festivos o promociones
	 * 
	 * @param FechaFin   Fecha de Finalizacion de la distribución especial para festivos o promociones
	 * @param Patrón   Nos dice cómo se distribuyen los grupos (expertos y novatos)
	 *           
	 * @param NumMax   Nos acota el número máximo de trabajadores requeridos dicho día en una cierta franja horaria 
	
	 * @param NumMin  Nos acota el número mínimo de trabajadores requeridos dicho día en una cierta franja horaria 
	 * 
	 * @param IdDepartamento  identificador del dpto.
	
	 * @return Informa sobre si se ha podido realizar la inserción o no
	 */
	public boolean insertarFestivo(int Hora,Date FechaInicio,Date FechaFin,String Patron,int NumMax, int NumMin,int IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO festivos values ('" + Hora + "', '" + FechaInicio + "', '" + FechaFin+ "', '" + Patron + "', '" +NumMax+ "', '" +NumMin+ "', '" +IdDepartamento+ "')");
			System.out.println("Festivo insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
}
