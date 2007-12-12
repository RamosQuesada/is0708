package aplicacion;

import java.sql.*;


/**
 * Aqu� se encuentran los m�todos de acceso a la base de datos.
 * 
 * @author 
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

	/**
	 * Comprueba si hay una conexi�n abierta con la base de datos
	 * @return	<i>true</i> si la conexi�n est� abierta, <i>false</i> en caso
	 * 			contrario o si hay alguna excepci�n
	 * @author Daniel Dionne
	 */
	public boolean conexionAbierta() {
		boolean b = false;
		try {
			b = !con.isClosed();
		} catch (Exception e) {}
		return b;
	}
	
	/**
	 * Devuelve el empleado con identificador nvend
	 * @param nvend el identificador del empleado
	 * @return una instancia del empleado
	 * @author Daniel Dionne
	 */
	public Empleado dameEmpleado(int nvend) {
		ResultSet r;
		String nombre = "";
		String apellido1 = "";
		String apellido2 = "";
		String fechaNac = "";
		try {			
			st = con.createStatement();
			r = st.executeQuery(
					"SELECT * FROM USUARIO WHERE NumVendedor = '" + String.valueOf(nvend) + "'");
			r.first();
			nombre		= r.getString("Nombre");
			apellido1	= r.getString("Apellido1");
			apellido2	= r.getString("Apellido2");
			fechaNac	= r.getString("FechaNacimiento");
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return new Empleado(nvend, nombre, (Turno)null);
		//return new Empleado(0, nvend, nombre, apellido1, apellido2, 
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
	 *            Representa el correo electr�nico del empleado
	 * @param password
	 *            Se corresponde con la contraseña para autenticarse en el
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
			String apellido2, String fechaNac, String sexo, String email,
			String password, String indicadorGrupo, String fechaContrato,
			String fechaEntrada, int horasExtras, int idDept, String rango,
			int idContrato, int idTurno) {
		boolean correcto = false;
		try {			
			st = con.createStatement();
			st.executeUpdate("INSERT INTO USUARIO values ('" + id + "', '"
					+ nombre + "', '" + apellido1 + "' ,'" + apellido2 + "','"
					+ fechaNac + "','" + sexo + "','" + email + "','"
					+ password + "','" + indicadorGrupo + "','" + fechaContrato + "','"
					+ fechaEntrada + "','" + horasExtras + "','" + idDept
					+ "','" + rango + "','" + idContrato + "','" + idTurno
					+ "')");
			System.out.println("Usuario insertado");
			correcto = true;
		} catch (SQLException e) {
			e.printStackTrace();
			correcto = false;
		}
		return correcto;
	}

	/**
	 * M�todo que inserta en la tabla Departamento los valores correspondientes a un nuevo departamento
	 * @param nombre	Nombre representativo de las actividades llevadas a cabo dentro del departamento 
	 * @param jefe		Persona que dirige le departamento
	 * @return			Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarDepartamento(String nombre,int jefe) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DEPARTAMENTO values ('" + nombre + "', '" + jefe + "')");
			System.out.println("Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	
	/**
	 * M�todo que inserta en la tabla numerosDpto los valores correspondientes
	 * @param numero	Numero del subdepartamento
	 * @param nombre	Nombre del departamento
	 * @return			Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarNumerosDepartamento(String numero,String nombre) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO NumerosDpto values ('" + numero + "', '" + nombre + "')");
			System.out.println("Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	/**
	 * M�todo que inserta en la tabla Distribucion los valores correspondientes a una nueva distribuci�n
	 * @param Hora    Franja horaria  dividida en unidades de una hora (por ej. De 9:00 � 10:00) representado por la hora de inicio (ej. 9)
	 *            
	 * @param DiaSemana  Dia (Lunes, Martes,...,Domingo) en el que se aplica la distribuci�n
	 *            
	 * @param Patr�n   Nos dice c�mo se distribuyen los grupos (expertos y novatos)
	 *           
	 * @param NumMax   Nos acota el n�mero m�ximo de trabajadores requeridos dicho d�a en una cierta franja horaria 

	 * @param NumMin  Nos acota el n�mero m�nimo de trabajadores requeridos dicho d�a en una cierta franja horaria 
	 * 
	 * @param IdDepartamento  identificador del dpto.
	
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
public boolean insertarDistribucion(int Hora,String DiaSemana,String Patron,int NumMax, int NumMin,int IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DISTRIBUCION values ('" + Hora + "', '" + DiaSemana + "', '" + Patron + "', '" +NumMax+ "', '" +NumMin+ "', '" +IdDepartamento+ "')");
			System.out.println("Distribucion insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}

	/**
	 * M�todo que inserta en la tabla Festivos los valores correspondientes a una distribuci�n para d�as festivos o promociones
	 * @param Hora    Franja horaria  dividida en unidades de una hora (por ej. De 9:00 � 10:00) representado por la hora de inicio (ej. 9)
	 *            
	 * @param FechaInicio   Fecha de Inicio de la distribuci�n especial para festivos o promociones
	 * 
	 * @param FechaFin   Fecha de Finalizacion de la distribuci�n especial para festivos o promociones
	 * @param Patr�n   Nos dice c�mo se distribuyen los grupos (expertos y novatos)
	 *           
	 * @param NumMax   Nos acota el n�mero m�ximo de trabajadores requeridos dicho d�a en una cierta franja horaria 
	
	 * @param NumMin  Nos acota el n�mero m�nimo de trabajadores requeridos dicho d�a en una cierta franja horaria 
	 * 
	 * @param IdDepartamento  identificador del dpto.
	
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarFestivo(int Hora,String FechaInicio,String FechaFin,String Patron,int NumMax, int NumMin,int IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO FESTIVOS values ('" + Hora + "', '" + FechaInicio + "', '" + FechaFin+ "', '" + Patron + "', '" +NumMax+ "', '" +NumMin+ "', '" +IdDepartamento+ "')");
			System.out.println("Festivo insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Metodo que inserta un turno en la BD
	 * @param idTurno Pendiente de revision
	 * @param Descripcion Una breve descripcion del turno
	 * @param HoraEntrada La hora de entrada en formato Time
	 * @param HoraSalida La hora de salida en formato Time
	 * @param HoraInicioDescanso La hora de inicio de descanso en formato Time
	 * @param Duracion La duracion del descanso en formato int - Pendiente de revision
	 * @return
	 */
	public boolean insertarTurno(int idTurno,String Descripcion,Time HoraEntrada,Time HoraSalida,Time HoraInicioDescanso, int Duracion) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO TURNOS values ('" + idTurno + "', '" + Descripcion + "', '" + HoraEntrada + "', '" + HoraSalida + "', '" + HoraInicioDescanso + "', '" + Duracion + "')");
			System.out.println("Turno insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	/**
	 * 
	 * @param remitente representa el remitente de un mensaje
	 * @param fecha fecha en la que se ha guardado el mensaje
	 * @param asunto campo que indica que contiene el mensaje de forma breve
	 * @param texto contenido de los mensajes
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	
	public boolean insertarMensaje(int remitente,Time fecha,String asunto,String texto) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO MENSAJE values ( 0 , " + remitente 
					+  ", '" + fecha + "', '" + asunto + "', '" + texto + "');");
			System.out.println("Mesaje insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	
	
	/**
	 * 
	 * @param numVendedor representa el destinatario al que se ha enviado el mensaje
	 * @param idMensaje representa el mensaje que se recibe
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertarListaDestinatarios(int numVendedor,int idMensaje) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DESTINATARIO values (" + numVendedor + ", " + idMensaje + ");");
			System.out.println("Destinatario insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	
	
	
	
	
	public static void main(String[] IS0708) {
		Database prueba = new Database();
		prueba.abrirConexion();
		
		Time h = new Time(10000000);
					
		
		prueba.insertarTurno(1, "prueba", h, h, h, 10);
		prueba.insertarDistribucion(9, "Martes", "1e5p", 10, 7, 1);
		prueba.insertarMensaje(1,new Time(0),"prueba","que gran prueba");
		prueba.insertarListaDestinatarios(1, 1);
		//prueba.insertarDepartamento("is", Integer.);
		//prueba.insertarUsuario(1235, "Pablo", "Gervas", "Gomez_Navarro","2007-12-11" , "m", "gervas@is.com", "ballenas", "p","2007-12-11" , "2007-12-11", 0, 123, "empleado", 1, 1);
		//prueba.insertarUsuario(1234, "Pablo", "Gervas", "Gomez_Navarro","2007-12-11" , "m", "gervas@is.com", "ballenas", "p","2007-12-11" , "2007-12-11", 0, 123, "empleado", 1, 1);
		prueba.insertarDepartamento("is", 123);
		prueba.insertarNumerosDepartamento("123", "is");
		
	  //  Date d = new Date(10000000);
	    prueba.insertarFestivo(9, "2007-12-11", "2007-12-11","2e7p", 15, 10, 1);
		prueba.cerrarConexion();
	}
}
