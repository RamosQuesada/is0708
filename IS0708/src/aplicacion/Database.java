package aplicacion;

import java.sql.*;
import java.util.ArrayList;

import algoritmo.Calendario;

/**
 * Aquí se encuentran los métodos de acceso a la base de datos.
 * 
 * @author grupo bases de datos
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
			System.out.println("aplicacion.Database.java\t:: Conexión a la BD");
		} catch (Exception e) {
			e.printStackTrace();
			System.err
					.println("aplicacion.Database.java\t:: Error en conexión");
		}
	}

	/**
	 * Cierra la conexión con la base de datos
	 * 
	 */
	public void cerrarConexion() {
		try {
			con.close();
			System.out
					.println("aplicacion.Database.java\t:: Conexion Cerrada Correctamente");
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println("Error cerrando la conexión");
		}
	}

	/**
	 * Comprueba si hay una conexión abierta con la base de datos
	 * 
	 * @return <i>true</i> si la conexión está abierta, <i>false</i> en caso
	 *         contrario o si hay alguna excepci�n
	 */
	public boolean conexionAbierta() {
		boolean b = false;
		try {
			b = !con.isClosed();
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * Devuelve el empleado con identificador nvend
	 * 
	 * @param nvend
	 *            el identificador del empleado
	 * @return un ResultSet con el resultado de la consulta
	 */
	public ResultSet dameEmpleado(int nvend) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM USUARIO WHERE NumVendedor = "
					+ nvend);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error al realizar la consulta del empleado ");
		}
		return r;
	}

	/**
	 * 
	 * @param nvend
	 *            el identificador del empleado
	 * @return un ResultSet con el id del superior del empleado
	 */
	public ResultSet obtenSuperior(int nvend) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT JefeDepartamento FROM DepartamentoUsuario,DEPARTAMENTO WHERE NumVendedor = "
							+ nvend
							+ " and DEPARTAMENTO.Nombre=NombreDepartamento");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error al obtener el superior del empleado ");
		}
		return r;
	}

	/**
	 * Metodo que obtiene los subordinados del empleado si los tuviera
	 * 
	 * @param nvend
	 *            el identificador del empleado
	 * @return un ResultSet con los subordinados del empleado si los tuviera
	 */
	public ResultSet obtenIdsSubordinados(int nvend) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT NumVendedor FROM DepartamentoUsuario,DEPARTAMENTO WHERE JefeDepartamento = "
							+ nvend
							+ " and DEPARTAMENTO.Nombre=NombreDepartamento");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta de los subordinados ");
		}
		return r;
	}

	/**
	 * Metodo que devuelve de la bd los departamentos a los que pertenece el
	 * empleado
	 * 
	 * @param nvend
	 *            el identificador del empleado
	 * @return un ResultSet con los departamentos a los que pertenece el
	 *         empleado
	 */
	public ResultSet obtenIdsDepartamentos(int nvend) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT NombreDepartamento FROM DepartamentoUsuario WHERE NumVendedor = "
							+ nvend);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta de los ids departamentos");
		}
		return r;
	}

	/**
	 * Metodo que devuelve de la bd los departamentos de un jefe
	 * 
	 * @param nvend
	 *            el identificador del jefe
	 * @return un ResultSet con los departamentos
	 */
	public ResultSet obtenDepartamentosJefe(int nvend) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT Nombre FROM DEPARTAMENTO "
					+ "WHERE JefeDepartamento = " + nvend);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta de los ids departamentos");
		}
		return r;
	}

	/**
	 * Método que obtiene los empleados que pertenecen al id del departemento
	 * dado
	 * 
	 * @param idDept
	 *            identificador de departamento
	 * @return los empleados que pertenecen al id del departemento dado
	 */
	public ResultSet obtenEmpleadosDepartamento(String nombre) {
		ResultSet r = null;
		String consulta = "SELECT NumVendedor FROM DepartamentoUsuario WHERE NombreDepartamento ='"
				+ nombre + "'";
		try {
			st = con.createStatement();
			r = st.executeQuery(consulta);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener los empleados de un departamento ");
		}
		return r;
	}

	/**
	 * Listar los distintos turnos que tiene un determinado empleado.
	 * 
	 * @param idEmpl
	 *            Identificador del empleado
	 * @return Todos los identificadores de turnos que tiene un determinado
	 *         empleado.
	 */
	public ResultSet obtenListaTurnosContrato(int idEmpl) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT ListaTurnosPorContrato.IdTurno FROM USUARIO,ListaTurnosPorContrato WHERE NumVendedor = "
							+ idEmpl
							+ " and USUARIO.IdContrato=ListaTurnosPorContrato.IdContrato");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error al obtener los turnos de un contrato ");
		}
		return r;
	}

	/**
	 * Metodo que recoge el turno que le corresponde a un empleado en un dia
	 * concreto
	 * 
	 * @param dia
	 *            dia en el cual se quiere saber el turno del empleado
	 * @param idEmpleado
	 *            identificador del empleado
	 * @return ResultSet con el turno
	 */
	public ResultSet obtenTurnoEmpleadoDia(Date dia, int idEmpleado) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT IdTurno FROM Trabaja WHERE NumVendedor = "
							+ idEmpleado + " and Fecha ='" + dia + "'");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener el turno de una fecha concreta ");
		}
		return r;
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
	 * @return los empleados que coincidan con los datos dados
	 */
	public ResultSet obtenEmpleadoAlaCarta(Integer idEmpl, String idDpto,
			Integer idContrato, String nombre, String apellido1,
			String apellido2, Integer rango) {
		ResultSet r = null;
		String subconsulta;
		boolean to2null = true;
		String consulta = "SELECT * FROM USUARIO WHERE ";
		int numero = 0;
		if (idEmpl != null) {
			consulta += "NumVendedor= " + idEmpl;
			to2null = false;
			numero = 1;
		}
		if (idContrato != null) {
			if (numero == 0)
				consulta += "IdContrato= " + idContrato;
			else
				consulta += " and IdContrato= " + idContrato;
			to2null = false;
			numero = 1;
		}
		if (nombre != null) {
			if (numero == 0)
				consulta += "Nombre= '" + nombre + "'";
			else
				consulta += " and Nombre= '" + nombre + "'";
			to2null = false;
			numero = 1;
		}
		if (apellido1 != null) {
			if (numero == 0)
				consulta += "Apellido1= '" + apellido1 + "'";
			else
				consulta += " and Apellido1= '" + apellido1 + "'";
			to2null = false;
			numero = 1;
		}
		if (apellido2 != null) {
			if (numero == 0)
				consulta += "Apellido2= '" + apellido2 + "'";
			else
				consulta += " and Apellido2= '" + apellido2 + "'";
			to2null = false;
			numero = 1;
		}
		if (rango != null) {
			if (numero == 0)
				consulta += "Rango= " + rango;
			else
				consulta += " and Rango= " + rango;
			to2null = false;
			numero = 1;
		}
		if (idDpto != null) {
			subconsulta = "SELECT NumVendedor FROM DepartamentoUsuario WHERE NombreDepartamento= '"
					+ idDpto + "'";
			to2null = false;
			if (numero == 0)
				consulta += "NumVendedor IN (" + subconsulta + ")";
			else
				consulta += " and NumVendedor IN (" + subconsulta + ")";
		}
		try {
			st = con.createStatement();
			if (to2null) {
				r = st.executeQuery("SELECT * FROM USUARIO");
			} else
				r = st.executeQuery(consulta);
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta del empleado a la carta");
		}
		return r;
	}

	/**
	 * Método que inserta en la tabla usuario los valores correspondientes a un
	 * nuevo usuario
	 * 
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
	 * @param felicidad
	 *            Grado de satisfaccion de un usuario con su horario
	 * @param idioma
	 *            Idioma de la aplicacion para el usuario
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
			String apellido2, Date fechaNac, int sexo, String email,
			String password, int indicadorGrupo, Date fechaContrato,
			Date fechaEntrada, int horasExtras, int felicidad, int idioma,
			int rango, int idContrato, int idTurno) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO USUARIO values ('" + id + "', '"
					+ nombre + "', '" + apellido1 + "' ,'" + apellido2 + "','"
					+ fechaNac + "','" + sexo + "','" + email + "','"
					+ password + "','" + indicadorGrupo + "','" + fechaContrato
					+ "','" + fechaEntrada + "','" + horasExtras + "','"
					+ felicidad + "','" + idioma + "','" + rango + "','"
					+ idContrato + "','" + idTurno + "')");
			System.out.println("aplicacion.Database.java\t::Usuario insertado");
			correcto = true;
		} catch (SQLException e) {
			e.printStackTrace();
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Método que elimina de la base de datos al usuario cuyo NumVendedor se
	 * facilita
	 * 
	 * @param id
	 *            Número de vendedor del usuario
	 * @return Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraUsuario(int id) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("DELETE FROM USUARIOS WHERE NumVendedor=" + id);
			System.out.println("aplicacion.Database.java\t::Usuario Borrado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al Borrar el usuario");
		}
		return correcto;
	}

	/**
	 * M�todo que inserta en la tabla Departamento los valores correspondientes
	 * a un nuevo departamento
	 * 
	 * @param nombre
	 *            Nombre representativo de las actividades llevadas a cabo
	 *            dentro del departamento
	 * @param jefe
	 *            Persona que dirige le departamento
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarDepartamento(String nombre, int jefe) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DEPARTAMENTO values ('" + nombre
					+ "', '" + jefe + "')");
			System.out
					.println("aplicacion.Database.java\t::Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al insertar el departamento");
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Método que elimina de la base de datos al departamento cuyo nombre se
	 * facilita
	 * 
	 * @param nombre
	 *            Nombre del departamento
	 * @return Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraDepartamento(String nombre) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("DELETE FROM DEPARTAMENTO WHERE Nombre='" + nombre
					+ "'");
			System.out
					.println("aplicacion.Database.java\t::Departamento Borrado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al Borrar el departamento");
		}
		return correcto;
	}

	/**
	 * M�todo que inserta en la tabla NumerosDEPARTAMENTOS los valores
	 * correspondientes
	 * 
	 * @param numero
	 *            Numero del subdepartamento
	 * @param nombre
	 *            Nombre del departamento
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarNumerosDepartamento(String numero, String nombre) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO NumerosDEPARTAMENTOs values ('"
					+ numero + "', '" + nombre + "')");
			System.out
					.println("aplicacion.Database.java\t::Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al insertar en NumerosDepartamento");
			correcto = false;
		}
		return correcto;
	}

	/**
	 * MEtodo que inserta en la tabla DepartamentoUsuario los valores
	 * correspondientes
	 * 
	 * @param nvend
	 *            Es el identificador único de cada empleado
	 * @param nombre
	 *            Nombre del departamento al que pertenece el empleado
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarDepartamentoUsuario(int nvend, String nombre) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DepartamentoUsuario values ('"
					+ nvend + "', '" + nombre + "')");
			System.out
					.println("aplicacion.Database.java\t::Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Error al insertar en DepartamentoUsuario");
		}
		return correcto;
	}

	/**
	 * M�todo que inserta en la tabla Ventas los valores correspondientes a cada
	 * dia para un determinado usuario
	 * 
	 * @param idUsuario
	 *            Numero que identifica al usuario
	 * 
	 * @param Fecha
	 *            String que representa un dia del año concreto. El formato es
	 *            Dd/mm/aaaa
	 * 
	 * @param NumeroVentas
	 *            Entero que representa la cantidad vendida para un determinado
	 *            dia. Valor mayor o igual a 0
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */

	public boolean insertarVentas(int idUsuario, Date Fecha, int numVentas) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO VENTAS values ('" + Fecha + "', '"
					+ numVentas + "', '" + idUsuario + "')");
			System.out.println("aplicacion.Database.java\t::Ventas insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;

	}

	/**
	 * M�todo que inserta en la tabla Distribucion los valores correspondientes
	 * a una nueva distribuci�n
	 * 
	 * @param Hora
	 *            Franja horaria dividida en unidades de una hora (por ej. De
	 *            9:00 � 10:00) representado por la hora de inicio (ej. 9)
	 * 
	 * @param DiaSemana
	 *            Dia (Lunes, Martes,...,Domingo) en el que se aplica la
	 *            distribuci�n
	 * 
	 * @param Patr�n
	 *            Nos dice c�mo se distribuyen los grupos (expertos y novatos)
	 * 
	 * @param NumMax
	 *            Nos acota el n�mero m�ximo de trabajadores requeridos dicho
	 *            d�a en una cierta franja horaria
	 * 
	 * @param NumMin
	 *            Nos acota el n�mero m�nimo de trabajadores requeridos dicho
	 *            d�a en una cierta franja horaria
	 * 
	 * @param IdDepartamento
	 *            identificador del dpto.
	 * 
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarDistribucion(int Hora, int DiaSemana, String Patron,
			int NumMax, int NumMin, String IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DISTRIBUCION values ('" + Hora
					+ "', '" + DiaSemana + "', '" + Patron + "', '" + NumMax
					+ "', '" + NumMin + "', '" + IdDepartamento + "')");
			System.out
					.println("aplicacion.Database.java\t::Distribucion insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}

	/**
	 * M�todo que inserta en la tabla Festivos los valores correspondientes a
	 * una distribuci�n para d�as festivos o promociones
	 * 
	 * @param Hora
	 *            Franja horaria dividida en unidades de una hora (por ej. De
	 *            9:00 � 10:00) representado por la hora de inicio (ej. 9)
	 * 
	 * @param FechaInicio
	 *            Fecha de Inicio de la distribuci�n especial para festivos o
	 *            promociones
	 * 
	 * @param FechaFin
	 *            Fecha de Finalizacion de la distribuci�n especial para
	 *            festivos o promociones
	 * @param Patr�n
	 *            Nos dice c�mo se distribuyen los grupos (expertos y novatos)
	 * 
	 * @param NumMax
	 *            Nos acota el n�mero m�ximo de trabajadores requeridos dicho
	 *            d�a en una cierta franja horaria
	 * 
	 * @param NumMin
	 *            Nos acota el n�mero m�nimo de trabajadores requeridos dicho
	 *            d�a en una cierta franja horaria
	 * 
	 * @param IdDepartamento
	 *            identificador del dpto.
	 * 
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarFestivo(int Hora, Date FechaInicio, Date FechaFin,
			String Patron, int NumMax, int NumMin, String IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO FESTIVOS values ('" + Hora + "', '"
					+ FechaInicio + "', '" + FechaFin + "', '" + Patron
					+ "', '" + NumMax + "', '" + NumMin + "', '"
					+ IdDepartamento + "')");
			System.out.println("aplicacion.Database.java\t::Festivo insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Metodo que inserta un turno en la BD
	 * 
	 * @param Descripcion
	 *            Una breve descripcion del turno
	 * @param HoraEntrada
	 *            La hora de entrada en formato Time
	 * @param HoraSalida
	 *            La hora de salida en formato Time
	 * @param HoraInicioDescanso
	 *            La hora de inicio de descanso en formato Time
	 * @param Duracion
	 *            La duracion del descanso en formato int - Pendiente de
	 *            revision
	 * @return Informa sobre si se ha podido realizar la inserci�n(devuelve el
	 *         id asignado al turno debido al autoincremento) o no(devuelve -1)
	 */
	// como es autoincrementable he quitado el id
	public int insertarTurno(String Descripcion, Time HoraEntrada,
			Time HoraSalida, Time HoraInicioDescanso, int Duracion) {
		int i = 0;
		ResultSet r = null;
		try {
			Time tdesc = new Time(0);
			tdesc.setMinutes(Duracion);
			tdesc.setHours(0);
			st = con.createStatement();
			st
					.executeUpdate("INSERT INTO TURNOS (Descripcion, HoraEntrada, HoraSalida, HoraInicioDescanso, DuracionDescanso) VALUES ('"
							+ Descripcion
							+ "', '"
							+ HoraEntrada
							+ "', '"
							+ HoraSalida
							+ "', '"
							+ HoraInicioDescanso
							+ "', '"
							+ tdesc + "')");
			System.out.println("aplicacion.Database.java\t::Turno insertado");
			r = st.getGeneratedKeys();
			r.next();
			i = r.getInt(1);
		} catch (SQLException e) {
			i = -1;
			e.printStackTrace();
			System.err.println("Error al insertar el Turno");
		}
		return i;
	}

	/**
	 * Método que elimina un turno de la base de datos
	 * 
	 * @param id
	 *            Identificador del turno
	 * @return Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraTurno(int id) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("DELETE FROM TURNOS WHERE IdTurno=" + id);
			System.out.println("aplicacion.Database.java\t::Turno Borrado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al Borrar el turno");
		}
		return correcto;
	}

	/**
	 * Método que inserta un mensaje en la base de datos
	 * 
	 * @param remitente
	 *            representa el remitente de un mensaje
	 * @param fecha
	 *            fecha en la que se ha guardado el mensaje
	 * @param asunto
	 *            campo que indica que contiene el mensaje de forma breve
	 * @param texto
	 *            contenido de los mensajes
	 * @param marcado
	 *            indica si el mensaje está marcado o no
	 * @return Informa sobre si se ha podido realizar la insercion(devuelve el
	 *         id asignado al menasaje debido al autoincremento) o no(devuelve
	 *         -1)
	 */
	// como es autoincrementable se ha quitado el id
	public int insertarMensaje(int remitente, Date fecha, String asunto,
			String texto, boolean marcado) {
		int i = 0;
		ResultSet r = null;
		try {
			st = con.createStatement();
			st
					.executeUpdate("INSERT INTO MENSAJE (Remitente, Fecha, Asunto, Texto, Marcado) values ( "
							+ remitente
							+ ", '"
							+ fecha
							+ "', '"
							+ asunto
							+ "', '" + texto + "', " + marcado + ");");
			System.out.println("aplicacion.Database.java\t::Mensaje insertado");
			r = st.getGeneratedKeys();
			r.next();
			i = r.getInt(1);
		} catch (SQLException e) {
			i = -1;
			e.printStackTrace();
			System.err.println("Error al insertar el mensaje");
		}
		return i;
	}

	/**
	 * Método que elimina un mensaje de la base de datos
	 * 
	 * @param id
	 *            Identificador del mensaje
	 * @return Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraMensaje(int id) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("DELETE FROM MENSAJE WHERE IdMensaje=" + id);
			System.out.println("aplicacion.Database.java\t::Mensaje Borrado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al Borrar el mensaje");
		}
		return correcto;
	}

	/**
	 * 
	 * @param marca
	 *            marca del mensaje(true si marcar y false si desmarcar)
	 * @param id
	 *            identificador del mensaje a marcar
	 * @return Indica si se ha podido marcar o no el mensaje
	 */
	public boolean marcaMensaje(boolean marca, int id) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("UPDATE MENSAJE SET Marcado= " + marca
					+ " WHERE IdMensaje=" + id);
			System.out.println("aplicacion.Database.java\t::Mensaje Marcado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al Marcar el mensaje");
		}
		return correcto;
	}

	/**
	 * Método que inserta en la tabla ListaDestinatarios
	 * 
	 * @param numVendedor
	 *            representa el destinatario al que se ha enviado el mensaje
	 * @param idMensaje
	 *            representa el mensaje que se recibe
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertarListaDestinatarios(int numVendedor, int idMensaje) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DESTINATARIO values (" + numVendedor
					+ ", " + idMensaje + ");");
			System.out
					.println("aplicacion.Database.java\t::Destinatario insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Error al insertar en ListaDestinatarios");
		}
		return correcto;
	}

	// como es autoincrementable he quitado el id
	/**
	 * Método que inserta un contrato en la base de datos
	 * 
	 * @param idContrato
	 *            representa el identificador del contrato
	 * @param turnoInicial
	 *            turno con el que empieza en el contrato
	 * @param nombre
	 *            nombre del contrato
	 * @param patron
	 *            distribucion de los dias que trabaja y los que no
	 * @param duracionCiclo
	 *            periodo de tiempo que le corresponde al patron
	 * @param salario
	 *            paga del empleado
	 * @return Devuelve el idContrato del nuevo contrato o -1 en caso de error
	 */
	public int insertarContrato(int turnoInicial, String nombre, String patron,
			int duracionCiclo, double salario, int tipocontrato) {
		int i = 0;
		ResultSet r = null;
		try {
			st = con.createStatement();
			st
					.executeUpdate("INSERT INTO CONTRATO (TurnoInicial,Nombre,Patron,DuracionCiclo,Salario,Tipo) values ("
							+ turnoInicial
							+ ", '"
							+ nombre
							+ "', '"
							+ patron
							+ "', "
							+ duracionCiclo
							+ ", "
							+ salario
							+ ", "
							+ tipocontrato + ");");

			System.out
					.println("aplicacion.Database.java\t::Contrato insertado");
			r = st.getGeneratedKeys();
			r.next();
			i = r.getInt(1);
		} catch (SQLException e) {
			i = -1;
			e.printStackTrace();
			System.err.println("Error al insertar el contrato");
		}
		return i;
	}

	/**
	 * Método que elimina un contrato de la base de datos
	 * 
	 * @param id
	 *            Identificador del contrato
	 * @return Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraContrato(int id) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("DELETE FROM CONTRATO WHERE IdContrato=" + id);
			System.out.println("aplicacion.Database.java\t::Contrato Borrado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Error al Borrar el contrato");
		}
		return correcto;
	}

	/**
	 * Método que inserta en la tabla trabaja (que refleja los cuadrantes)
	 * 
	 * @param numVendedor
	 *            representa al trabajador
	 * @param idTurno
	 *            indica el turno con el que ha trabajado el empleado
	 * @param fecha
	 *            dia en el que un empleado a trabajado con un turno determinado
	 * @param horaEntrada
	 *            hora a la que entra a trabajar
	 * @param horaSalida
	 *            hora a la que sale
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertarTrabaja(int numVendedor, int idTurno, String fecha,
			Time horaEntrada, Time horaSalida) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO Trabaja values (" + numVendedor
					+ ", " + idTurno + ", '" + fecha + "', '" + horaEntrada
					+ "', '" + horaSalida + "');");
			System.out
					.println("aplicacion.Database.java\t::Insertado en la tabla trabaja");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Error al insrtar en Trabaja");
		}
		return correcto;
	}

	/**
	 * Método que inserta en ListaTurnosPorContrato
	 * 
	 * @param idTurno
	 *            identificador del turno correpondiente al contrato
	 * @param idContrato
	 *            identificador del contrato
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertarTurnoPorContrato(int idTurno, int idContrato) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO ListaTurnosPorContrato values ("
					+ idTurno + ", " + idContrato + ");");
			System.out
					.println("aplicacion.Database.java\t::turnoPorContrato insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			e.printStackTrace();
			System.err.println("Error al insertar enListaTurnosPorContrato");
		}
		return correcto;
	}

	/**
	 * Método que inserta una incidencia en la base de datos
	 * 
	 * @param idIncidencia
	 *            identificador de la incidencia
	 * @param descripcion
	 *            breve descripcion de la incidencia
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertarIncidencia(String descripcion) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO INCIDENCIAS values (" + 0 + ", '"
					+ descripcion + "');");
			System.out
					.println("aplicacion.Database.java\t::Incidencia insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Error al insertar la incidencia");
		}
		return correcto;
	}

	/**
	 * Metodo que inserta en la tabla TieneIncidencia
	 * 
	 * @param idIncidencia
	 *            identificador de la incidencia
	 * @param numVendedor
	 *            identificador del empleado
	 * @param fechaInicio
	 *            fecha de inicio de la incidencia
	 * @param fechaFin
	 *            fecha de finalizacion de la incidencia
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertarTieneIncidencia(int idIncidencia, int numVendedor,
			Time fechaInicio, Time fechaFin) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO TieneIncidencia values ("
					+ idIncidencia + ", " + numVendedor + ", '" + fechaInicio
					+ "','" + fechaFin + "');");
			System.out
					.println("aplicacion.Database.java\t::incidencia de una persona insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Error al insertar la ListaTurnosPorContrato");
		}
		return correcto;
	}

	/**
	 * Metodo que selecciona de la tabla de distribucion aquellas filas cuyo
	 * departamento y dia de la semana coincide con los pasados como parametros
	 * de la funcion
	 * 
	 * @param idDepartamento
	 * @param DiaSemana
	 *            (domingo = 0, lunes = 1....)
	 * @return ResultSet con las filas que coinciden con el departamento y el
	 *         dia de la semana
	 */
	public ResultSet obtenDistribucion(String nombre, int DiaSemana) {

		try {
			st = con.createStatement();
			rs = st
					.executeQuery("SELECT * FROM DISTRIBUCION WHERE NombreDept ='"
							+ nombre
							+ "' AND DiaSemana="
							+ DiaSemana
							+ " ORDER BY Hora ASC");

		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta de la distribucion ");
		}
		return rs;
	}

	/**
	 * Metodo que selecciona de la tabla de festivos aquellas filas cuyo
	 * departamento y fecha coinciden con los parametros
	 * 
	 * @param idDepartamento
	 * @param Fecha
	 * @return ResultSet con las filas que coinciden con el departamento y la
	 *         fecha
	 */
	public ResultSet obtenFestivos(String nombre, Date Fecha) {
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM FESTIVOS WHERE NombreDept ='"
					+ nombre + "' AND FechaInicio<='" + Fecha
					+ "' AND FechaFin>='" + Fecha + "' ORDER BY Hora ASC");

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err
					.println("Error al realizar la consulta de los festivos ");
		}
		return rs;
	}

	/**
	 * Método que devuelve los mensajes salientes de un empleado ordenados por
	 * fecha
	 * 
	 * @param id
	 *            identificador de usuario
	 * @param inicio
	 *            fecha de inicio
	 * @param desp
	 *            intervalo de fechas que se quiere consultar
	 * @return ResulSet con los mensajes salientes solicitados
	 */
	public ResultSet obtenMensajesSalientes(int id, int inicio, int desp) {
		ResultSet r = null;

		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT * FROM MENSAJE WHERE Remitente=" + id
							+ " ORDER BY Fecha LIMIT " + inicio + ","
							+ (inicio + desp));

		}

		catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta Lista de Turnos ");
		}
		return r;
	}

	/**
	 * 
	 * @param nombre
	 *            identificador del departamento
	 * @return la fila correspondiente al departamento solicitado
	 */
	public ResultSet obtenDepartamento(String nombre) {
		ResultSet r = null;

		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM DEPARTAMENTO WHERE Nombre='"
					+ nombre + "'");
		}

		catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta en departamento ");
		}
		return r;
	}

	/**
	 * 
	 * @param idContrato
	 *            identificador del contrato
	 * @return la fila corrspondiente al contrato solicitado
	 */
	public ResultSet obtenContrato(int idContrato) {
		ResultSet result = null;
		try {
			st = con.createStatement();
			result = st
					.executeQuery("SELECT * FROM CONTRATO WHERE IdContrato = "
							+ idContrato + ";");
		} catch (SQLException e) {
			System.err.println("Error de lectura de Contrato");
		}
		return result;
	}

	/**
	 * obtiene el identificador del ultimo mensaje en la base de datos
	 * 
	 * @return
	 */
	public int obtenIdMensaje() {
		ResultSet result = null;
		int maximo = -1;
		try {
			st = con.createStatement();
			result = st
					.executeQuery("SELECT Max(IdMensaje)as Maximo from MENSAJE ;");
			result.next();
			maximo = result.getInt("Maximo");
		} catch (SQLException e) {
			System.err.println("Error al obtener el maximo id de los mensajes");
		}
		return maximo;
	}

	/**
	 * lista de identificadores de mensajes que ha recibido un usuario
	 * 
	 * @param idUsuario
	 *            usuario que ha recibido los mensajes
	 * @return ResultSet con los identificadores de los mensajes
	 */
	public ResultSet obtenDestinatarios(int idUsuario) {
		ResultSet result = null;
		try {
			st = con.createStatement();
			result = st
					.executeQuery("SELECT IdMensaje from DESTINATARIO WHERE NumVendedor = "
							+ idUsuario + ";");
		} catch (SQLException e) {
			System.err.println("Error obtenDestinatarios");
		}
		return result;

	}

	/**
	 * recupera un mensaje de la base de datos indicando el numero del mismo
	 * 
	 * @param mensaje
	 *            indica el mensaje que se quiere recuperar por identificador de
	 *            mensaje
	 * @return ResulSet el mensaje indicado
	 */
	public ResultSet obtenMensaje(int mensaje) {
		ResultSet result = null;
		try {
			st = con.createStatement();
			result = st.executeQuery("SELECT * FROM MENSAJE WHERE IdMensaje="
					+ mensaje + ";");
		} catch (SQLException e) {
			System.err.println("Error obtenMensaje ");
		}
		return result;
	}

	public ResultSet obtenMensajesEntrantes(int vendedor, int inicio, int desp) {
		ResultSet result = null;
		try {
			st = con.createStatement();
			result = st
					.executeQuery("SELECT * FROM DESTINATARIO JOIN MENSAJE WHERE DESTINATARIO.NumVendedor="
							+ vendedor
							+ " AND DESTINATARIO.IdMensaje=MENSAJE.IdMensaje LIMIT "
							+ inicio + "," + (inicio + desp) + ";");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error obtenMensajesEntrantes ");
		}
		return result;
	}

	/**
	 * recupera un turno de la base de datos indicando el id del mismo
	 * 
	 * @param turno
	 *            identificador del turno
	 * @return el turno indicado
	 */
	public ResultSet obtenTurno(int turno) {
		ResultSet result = null;
		try {
			st = con.createStatement();
			result = st.executeQuery("SELECT * FROM TURNOS WHERE IdTurno="
					+ turno + ";");
		} catch (SQLException e) {
			System.err.println("Error obtenTurno ");
		}
		return result;
	}

	/**
	 * 
	 * @return devuelve todas las filas de la tabla DEPARTAMENTO
	 */
	public ResultSet obtenTodosDepartamentos() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM DEPARTAMENTO");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta en departamento ");
		}
		return r;
	}

	/**
	 * 
	 * @return devuelve todas las filas de la tabla TURNOS
	 */
	public ResultSet obtenTodosTurnos() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM TURNOS");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error al realizar la consulta en TURNOS ");
		}
		return r;
	}

	/**
	 * 
	 * @return devuelve todas las filas de la tabla CONTRATO
	 */
	public ResultSet obtenTodosContratos() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM CONTRATO");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error al realizar la consulta en CONTRATO ");
		}
		return r;
	}

	/**
	 * 
	 * @param nvend
	 *            Identificador del empleado
	 * @return devuelve la fila correspondiente al turno actual del empleado
	 */
	public ResultSet obtenTurnoFavorito(int nvend) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT IdTurno FROM USUARIO WHERE NumVendedor="
							+ nvend + ";");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta del turno favorito del usuario ");
		}
		return r;
	}

	/**
	 * 
	 * @param idContrato
	 *            identificador del contrato
	 * @return ResultSet con los turnos que pertenecen al contrato dado
	 */
	public ResultSet obtenTurnosDeUnContrato(int idContrato) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st
					.executeQuery("SELECT IdTurno FROM ListaTurnosPorContrato WHERE IdContrato="
							+ idContrato + ";");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta en departamento ");
		}
		return r;
	}

	/**
	 * Vacia los contenidos de la tabla especificada
	 * 
	 * @param nombre
	 *            El nombre de la tabla en formato String
	 * @return Devuelve un bool. True si ha ido todo bien, false en caso de
	 *         error.
	 */
	public boolean vaciarTabla(String nombre) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("TRUNCATE TABLE " + nombre);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error al vaciar la tabla");
			return false;
		}
		return true;
	}

	/**
	 * Modifica un contrato en la BD. Se le pasan todos los parametros aunque no
	 * cambien
	 * 
	 * @param IdContrato
	 * @param TurnoInicial
	 * @param Nombre
	 * @param Patron
	 * @param DuracionCiclo
	 * @param Salario
	 * @param Tipo
	 * @return Devuelve un bool que dice si todo ha ido bien.
	 */
	public boolean cambiarContrato(int IdContrato, int TurnoInicial,
			String Nombre, String Patron, int DuracionCiclo, double Salario,
			int Tipo) {
		int r = 0;
		try {
			st = con.createStatement();
			r = st.executeUpdate("UPDATE CONTRATO SET TurnoInicial="
					+ TurnoInicial + ", Nombre='" + Nombre + "', Patron='"
					+ Patron + "', DuracionCiclo=" + DuracionCiclo
					+ ", Salario=" + Salario + ", Tipo=" + Tipo
					+ " WHERE IdContrato=" + IdContrato + ";");
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("Error modificar contrato en la BD");
			return false;
		}
		return true;
	}
	/**
	 * Modifica un turno en la BD. Se le pasan todos los parametros aunque no
	 * cambien
	 * 
	 * @param IdTurno
	 * @param Descripcion
	 * @param HoraEntrada
	 * @param HoraSalida
	 * @param HoraInicioDescanso
	 * @param Duracion
	 * @return Devuelve un bool que dice si todo ha ido bien.
	 */
	public boolean cambiarTurno(int IdTurno, String Descripcion,
			Time HoraEntrada, Time HoraSalida, Time HoraInicioDescanso,
			int Duracion) {
		int r = 0;
		try {
			Time tdesc = new Time(0);
			tdesc.setMinutes(Duracion);
			tdesc.setHours(0);
			st = con.createStatement();
			r = st.executeUpdate("UPDATE TURNOS SET Descripcion='"
					+ Descripcion + "', HoraEntrada='" + HoraEntrada
					+ "', HoraSalida='" + HoraSalida
					+ "', HoraInicioDescanso='" + HoraInicioDescanso
					+ "', DuracionDescanso='" + tdesc + "'" + " WHERE IdTurno="
					+ IdTurno + ";");
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("Error modificar turno en la BD");
			return false;
		}
		return true;
	}

	/**
	 * Metodo que lee los datos de un mes determinado de la tabla Trabaja sin
	 * importar el departamento ni el vendedor
	 * 
	 * @param mes
	 * @param anio
	 * @return Devuelve un ResultSet con los datos leidos de la BD
	 */
	public ResultSet obtenCuadrante(int mes, int anio) {
		ResultSet r = null;

		try {
			String inicio = anio + "-" + mes + "-" + "1";
			String fin = anio + "-" + mes + "-" + "31";
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM Trabaja WHERE Fecha>='" + inicio
					+ "' AND Fecha<='" + fin + "';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Error al realizar la consulta en departamento ");
		}
		return r;
	}

	public ResultSet obtenNombreTodosDepartamentos() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT Nombre FROM DEPARTAMENTO");
		}
		catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error al realizar la consulta en departamento ");
		}
		return r;		
	}
	
	
	
	
}
