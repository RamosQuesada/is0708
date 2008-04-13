package aplicacion;


import java.io.*;
import java.sql.*;
import java.util.Date;

import aplicacion.utilidades.EncriptCadena;
import aplicacion.utilidades.Util;

/**
 * Aquí se encuentran los métodos de acceso a la base de datos.
 * 
 * @author grupo bases de datos
 * 
 */
public class Database extends Thread {

	public static final String tablaContratos					= "contrato";
	public static final String tablaContratosPorDepartamento	= "contratodepartamento";
	public static final String tablaDepartamentos				= "departamento";
	public static final String tablaUsuariosPorDepartamento		= "departamentousuario";
	public static final String tablaDestinatariosMensaje		= "destinatario";
	public static final String tablaDistribucionHorarios		= "distribucion";
	public static final String tablaFestivos					= "festivos";
	public static final String tablaIncidencias					= "incidencias";
	public static final String tablaIssues						= "issues";
	public static final String tablaTurnosPorContrato			= "listaturnosporcontrato";
	public static final String tablaMensajes					= "mensaje";
	public static final String tablaNumerosPorDepartamento		= "numerosdepartamentos";
	public static final String tablaOpiniones					= "opinion";
	public static final String tablaPermisos					= "permisos";
	public static final String tablaIncidenciasPorUsuario		= "tieneincidencia";
	public static final String tablaTrabaja						= "trabaja";
	public static final String tablaTurnos						= "turnos";
	public static final String tablaUsuarios					= "usuario";
	public static final String tablaVentas						= "ventas";

	Connection con;
	Statement st;
	ResultSet rs;

	// Tras una hora de pelea con Eclipse y MySQL, por fin tenemos
	// conexion con la BD ;)
	/**
	 * Abre una conexión nueva con la base de datos
	 */
	public synchronized void abrirConexion() {
		String userName = "";
		String password = "";
		String ip = "";

		try {
			//para desencriptar
			FileInputStream is;
			is = new FileInputStream("src"+File.separator+"interfaces"+File.separator+"configBD");
			DataInputStream dis = new DataInputStream(is);
			ip= dis.readUTF();
			userName=dis.readUTF();
			password=EncriptCadena.desencripta(dis.readUTF());
			String url = "jdbc:mysql://"+ ip +"/" + "turnomat_bd";

// Descomentar este trozo para usar la base de datos local
//			userName = "root";
//			password = "";
//			url = "jdbc:mysql://localhost/turnomat_bd";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//DriverManager.setLoginTimeout(300);
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("Database :: Conexión a la BD");
		} catch (FileNotFoundException e) {
			System.err.println("Database :: No se encuentra el archivo: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Database :: Error de entrada salida: " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Database :: Error en conexión: " + e.getMessage() +
					"\n - IP:         " + ip + 
					"\n - Usuario:    " + userName + 
					"\n - Contraseña: " + password);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Database :: Error al instanciar el conector MySQL: " + e.getMessage());
		}
	}

	/**
	 * Método que elimina un contrato de la base de datos
	 * 
	 * @param id Identificador del contrato
	 * @return Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraContrato(int id) {
		boolean correcto = false;
		String q = "DELETE FROM " + tablaContratos + " WHERE IdContrato=" + id;
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar el contrato:\n\t" + q);
		}
		return correcto;
	}

	/**
	 * Metodo que borra un contrato que pertenecia a un contrato
	 * @param idContrato	el identificador del contrato a eliminar
	 * @return	si se ha realizado correctamente la eliminacion
	 */
	public boolean borraContratoConTurnos(int idContrato){
		boolean correcto = false;
		String q = "DELETE FROM " + tablaTurnosPorContrato + " WHERE IdContrato=" + idContrato;
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar el contrato y sus turnos de ListaTurnosPorContrato:\n\t" + q);
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
		String q = "DELETE FROM " + tablaDepartamentos + " WHERE Nombre='" + nombre + "'";
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar el departamento:\n\t" + q);
		}
		return correcto;
	}

	/**
	 * Borra un empleado de la BD
	 * @param NumVendedor
	 * @return True si todo va bien, false si algo ha fallado
	 * 
	 */
	public boolean borraEmpleado(int NumVendedor) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("DELETE FROM " + tablaUsuarios + " WHERE NumVendedor=" + NumVendedor);
			st.executeUpdate("DELETE FROM " + tablaUsuariosPorDepartamento + " WHERE NumVendedor=" + NumVendedor);
			st.executeUpdate("DELETE FROM " + tablaMensajes + " WHERE Remitente=" + NumVendedor);
			st.executeUpdate("DELETE FROM " + tablaDestinatariosMensaje + " WHERE NumVendedor=" + NumVendedor);
			st.executeUpdate("DELETE FROM " + tablaIncidenciasPorUsuario + " WHERE NumVendedor=" + NumVendedor);
			st.executeUpdate("DELETE FROM " + tablaTrabaja + " WHERE NumVendedor=" + NumVendedor);
			st.executeUpdate("DELETE FROM " + tablaVentas + " WHERE NumVendedor=" + NumVendedor);
			
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar el empleado");
		}
		return correcto;
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
		String q = "DELETE FROM " + tablaMensajes + " WHERE IdMensaje=" + id;
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al Borrar el mensaje");
		}
		return correcto;
	}

	/**
	 * Borra el periodo de un mes indicado por los parámetros de
	 * @param mes identifica el mes del periodo a borrar
	 * @param anio identifica el anio del periodo a borrar
	 * @param dep identifica el departamento 
	 * @return si se ha realizado correctamente la eliminacion
	 */
	public boolean borraMesTrabaja(int mes, int anio,String dep){
		String fechaIni = anio +"-"+ mes +"-"+ "01";
		String fechaFin = anio +"-"+ mes +"-"+ "31";
		String q = "DELETE FROM " + tablaTrabaja + " WHERE Fecha >='" + fechaIni + "' and Fecha <='" +
		fechaFin +"' and NumVendedor in (SELECT NumVendedor FROM " + tablaUsuariosPorDepartamento + " D where NombreDepartamento = '"+dep +"');";
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar el mes de La tabla trabaja:\n\t" + q);			
			System.err.println(e.getMessage());			
		}
		return correcto;
	}

	/**
	 * Metodo que elimina de la base de datos la fila de la tabla DepartamentoUsuario
	 * cuyo nombre de departamento coincide con el que se pasa por parametro
	 * @param nombre Nombre del departamento
	 * @return	Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraNombreDepartamentoUsuario(String nombre) {
		boolean correcto = false;
		String q = "DELETE FROM " + tablaUsuariosPorDepartamento + " WHERE NombreDepartamento='" + nombre + "'";
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar en DepartamentoUsuario:\n\t" + q);
		}
		return correcto;
	}

	/**
	 * Metodo que elimina de la base de datos la fila de la tabla NumerosDEPARTAMENTOs
	 * cuyo nombre de departamento coincide con el que se pasa por parametro
	 * @param nombre Nombre del departamento
	 * @return	Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraNumerosDepartamentos(String nombre) {
		boolean correcto = false;
		String q = "DELETE FROM " + tablaNumerosPorDepartamento + " WHERE Nombre='" + nombre + "'";
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al Borrar el departamento:\n\t" + q);
		}
		return correcto;
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
			st.executeUpdate("DELETE FROM " + tablaTurnos + " WHERE IdTurno=" + id);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar el turno");
		}
		return correcto;
	}

	/**
	 * Metodo que borra un turno que pertenecia a un contrato
	 * @param idTurno	el identificador del turno a eliminar
	 * @return	si se ha realizado correctamente la eliminacion
	 */
	public boolean borraTurnoDeContrato(int idTurno, int idContrato){
		boolean correcto = false;
		String q = "DELETE FROM " + tablaTurnosPorContrato + " WHERE IdTurno=" + idTurno 
		 + " and IdContrato= "+ idContrato;
		try {
			st = con.createStatement();
			st.executeUpdate(q);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar el turno de ListaTurnosPorContrato");
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
			st.executeUpdate("DELETE FROM " + tablaUsuarios + " WHERE NumVendedor=" + id);
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al Borrar el usuario");
		}
		return correcto;
	}

	/**
	 * Metodo que elimina de la base de datos la fila de la tabla DepartamentoUsuario
	 * cuyo numero de vendedor coincide con el que se pasa por parametro
	 * @param nv Numero de vendedor
	 * @return	Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraUsuarioDepartamentoUsuario(int nv) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("DELETE FROM " + tablaUsuariosPorDepartamento + " WHERE NumVendedor='" + nv
					+ "'");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al Borrar en DepartamentoUsuario");
		}
		return correcto;
	}

	public boolean cambiaNombreDepartamento(String NombreAntiguo, String NombreNuevo) {
		int r = 0;
		try {
			st = con.createStatement();
			r = st.executeUpdate("UPDATE " + tablaDepartamentos + " SET Nombre='"
					+ NombreNuevo + "'" + " WHERE Nombre='" + NombreAntiguo + "';");
		} catch (SQLException e) {
			System.err.println("Database :: Error Cambiar Nombre del Depto. en la BD");
			return false;
		}
		return true;
	}

	public boolean cambiaNombreDepartamentoUsuario(String NombreAntiguo, String NombreNuevo) {
		int r = 0;
		try {
			st = con.createStatement();
			r = st.executeUpdate("UPDATE " + tablaUsuariosPorDepartamento + " SET NombreDepartamento='"
					+ NombreNuevo + "'" + " WHERE NombreDepartamento='" + NombreAntiguo + "';");
		} catch (SQLException e) {
			System.err.println("Database :: Error Cambiar Nombre del Depto. en la BD");
			return false;
		}
		return true;
	}

	public boolean cambiaNombreNumerosDEPARTAMENTOs(String NombreAntiguo, String NombreNuevo) {
		int r = 0;
		try {
			st = con.createStatement();
			r = st.executeUpdate("UPDATE " + tablaNumerosPorDepartamento + " SET Nombre='"
					+ NombreNuevo + "'" + " WHERE Nombre='" + NombreAntiguo + "';");
		} catch (SQLException e) {
			System.err.println("Database :: Error Cambiar Nombre del Depto. en la BD");
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
			r = st.executeUpdate("UPDATE " + tablaContratos + " SET TurnoInicial="
					+ TurnoInicial + ", Nombre='" + Nombre + "', Patron='"
					+ Patron + "', DuracionCiclo=" + DuracionCiclo
					+ ", Salario=" + Salario + ", Tipo=" + Tipo
					+ " WHERE IdContrato=" + IdContrato + ";");
		} catch (SQLException e) {
			System.err.println("Database :: Error modificar contrato en la BD");
			return false;
		}
		return true;
	}

	/**
	 * Modifica un turno en la BD. Se le pasan todos los parametros aunque no
	 * cambien excepto el de HorasExtras ya que ese no lo debemos tocar
	 * @param idEmp
	 * @param nomb
	 * @param Ape1
	 * @param Ape2
	 * @param FNac
	 * @param sexo
	 * @param mail
	 * @param Passw
	 * @param grupo
	 * @param FCont
	 * @param Fentr
	 * @param Felic
	 * @param Idiom
	 * @param Rang
	 * @param Turn
	 * @param Contr
	 * @return Devuelve un bool que dice si todo ha ido bien.
	 */
	
	public boolean cambiarEmpleado(int idEmp, String nomb, String Ape1, String Ape2, Date FNac, int sexo, 
			String mail, String Passw, int grupo, Date FCont, Date Fentr, int Felic, int Idiom, 
			int Rang, int Turn, int Contr) {
	int r = 0;
	try {
		String Nac = Util.dateAString(FNac);
		String Cont = Util.dateAString(FCont);
		String Entr = Util.dateAString(Fentr);
		
		st = con.createStatement();
		r = st.executeUpdate("UPDATE " + tablaUsuarios + " SET Nombre='"
				+ nomb + "', Apellido1='" + Ape1 + "', Apellido2='"
				+ Ape2 + "', FechaNacimiento='" + Nac
				+ "', Sexo=" + sexo + ", Email='" + mail
				+ "', Password='" + Passw + "', IndicadorGrupo=" + grupo
				+ ", FechaContrato='" + Cont + "', FechaEntrada='" + Entr
				+ "', Felicidad=" + Felic
				+ ", Idioma=" + Idiom + ", Rango=" + Rang
				+ ", IdContrato=" + Contr + ", IdTurno=" + Turn
				+ " WHERE NumVendedor=" + idEmp + ";");
	} catch (SQLException e) {
		System.err.println("Database :: Error al modificar empleado en la BD");
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
			r = st.executeUpdate("UPDATE " + tablaTurnos + " SET Descripcion='"
					+ Descripcion + "', HoraEntrada='" + HoraEntrada
					+ "', HoraSalida='" + HoraSalida
					+ "', HoraInicioDescanso='" + HoraInicioDescanso
					+ "', DuracionDescanso='" + tdesc + "'" + " WHERE IdTurno="
					+ IdTurno + ";");
		} catch (SQLException e) {
			System.err.println("Database :: Error modificar turno en la BD");
			return false;
		}
		return true;
	}
	
	/**
	 * Cierra la conexión con la base de datos
	 * 
	 */
	public void cerrarConexion() {
		try {
			con.close();
			System.out.println("aplicacion.Database.java\t:: Conexión cerrada correctamente");
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println("aplicacion.Database.java\t:: Error cerrando la conexión");
		}
	}

	/**
	 * Comprueba si hay una conexión abierta con la base de datos
	 * 
	 * @return <i>true</i> si la conexión está abierta, <i>false</i> en caso
	 *         contrario o si hay alguna excepción
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
			r = st.executeQuery("SELECT * FROM " + tablaUsuarios + " WHERE NumVendedor = "
					+ nvend);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta del empleado ");
		}
		return r;
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
					.executeUpdate("INSERT INTO " + tablaContratos + " (TurnoInicial,Nombre,Patron,DuracionCiclo,Salario,Tipo) values ("
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
			//r = st.getGeneratedKeys();
			r = st.executeQuery("SELECT LAST_INSERT_ID()");
			if(r.next()) 
				i = r.getInt(1);
			else System.err.println("Error al obtener el identificador auto-generado");
		} catch (SQLException e) {
			i = -1;
			e.printStackTrace();
			System.err.println("Database :: Error al insertar el contrato");
		}
		return i;
	}

	/**
	 * Método que inserta en la tabla Departamento los valores correspondientes
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
		// TODO insertar horas correctamente
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO " + tablaDepartamentos + " values ('" + nombre
					+ "', '" + jefe + "', '" + "9:00:00" + "', '" + "23:00:00" + "')");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al insertar el departamento");
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Método que inserta en la tabla DepartamentoUsuario los valores
	 * correspondientes
	 * 
	 * @param nvend
	 *            Es el identificador único de cada empleado
	 * @param nombre
	 *            Nombre del departamento al que pertenece el empleado
	 * @param ultima Ejecuta las inserciones pendientes si es true, sino solo la envía
	 * @return Informa sobre si se ha podido realizar la inserción o no
	 */
	public boolean insertarDepartamentoUsuario(int nvend, String nombre, boolean ultima) {
		boolean correcto = false;
		String q = "INSERT INTO " + tablaUsuariosPorDepartamento + " values ('"
		+ nvend + "', '" + nombre + "')";
		try {
			st.addBatch(q);
			if (ultima) {
				st.executeBatch();
				st = con.createStatement();
			}
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insertar en DepartamentoUsuario");
			e.printStackTrace();
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
			int NumMax, int NumMin, String IdDepartamento, boolean ultima) {
		boolean correcto = false;
		String q = "INSERT INTO " + tablaDistribucionHorarios + " values ('" + Hora
		+ "', '" + DiaSemana + "', '" + Patron + "', '" + NumMax
		+ "', '" + NumMin + "', '" + IdDepartamento + "')";
		try {
			st = con.createStatement();
			st.addBatch(q);
			if (ultima) st.executeBatch();
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insertar Distribución:\n\t" + q);
			e.printStackTrace();
		}
		return correcto;
	}

	/**
	 * Método que inserta en la tabla Festivos los valores correspondientes a
	 * una distribución para días festivos o promociones
	 * 
	 * @param Hora
	 *            Franja horaria dividida en unidades de una hora (por ej. De
	 *            9:00 a 10:00) representado por la hora de inicio (ej. 9)
	 * 
	 * @param FechaInicio
	 *            Fecha de Inicio de la distribución especial para festivos o
	 *            promociones
	 * 
	 * @param FechaFin
	 *            Fecha de Finalizacion de la distribución especial para
	 *            festivos o promociones
	 * @param Patr�n
	 *            Nos dice cómo se distribuyen los grupos (expertos y novatos)
	 * 
	 * @param NumMax
	 *            Nos acota el número máximo de trabajadores requeridos dicho
	 *            día en una cierta franja horaria
	 * 
	 * @param NumMin
	 *            Nos acota el número mínimo de trabajadores requeridos dicho
	 *            día en una cierta franja horaria
	 * 
	 * @param IdDepartamento
	 *            identificador del dpto.
	 * 
	 * @return Informa sobre si se ha podido realizar la inserción o no
	 */
	public boolean insertarFestivo(int Hora, Date FechaInicio, Date FechaFin,
			String Patron, int NumMax, int NumMin, String IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO " + tablaFestivos + " values ('" + Hora + "', '"
					+ FechaInicio + "', '" + FechaFin + "', '" + Patron
					+ "', '" + NumMax + "', '" + NumMin + "', '"
					+ IdDepartamento + "')");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insertar Festivo");
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
			st.executeUpdate("INSERT INTO " + tablaIncidencias + " values (" + 0 + ", '"
					+ descripcion + "');");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insertar la incidencia");
		}
		return correcto;
	}
	public void insertarIssue(String texto) {
		try {
			abrirConexion();
			st = con.createStatement();
			st.executeUpdate("INSERT INTO " + tablaIssues + " (text) values ( '" + texto + "');");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Database :: Error al insertar el issue");
		}
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
			st.executeUpdate("INSERT INTO " + tablaDestinatariosMensaje + " values (" + numVendedor
					+ ", " + idMensaje + ");");
			System.out
					.println("aplicacion.Database.java\t::Destinatario insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insertar en ListaDestinatarios");
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
			st.executeUpdate("INSERT INTO " + tablaMensajes + " (Remitente, Fecha, Asunto, Texto, Marcado) values ( "
							+ remitente
							+ ", '"
							+ fecha
							+ "', '"
							+ asunto
							+ "', '" + texto + "', " + marcado + ");");
//			System.out.println("aplicacion.Database.java\t::Mensaje insertado");
			//r = st.getGeneratedKeys();
			r = st.executeQuery("SELECT LAST_INSERT_ID()");
			if(r.next()) 
				i = r.getInt(1);
			else System.err.println("Database :: Error al obtener el identificador auto-generado");
		} catch (SQLException e) {
			i = -1;
			e.printStackTrace();
			System.err.println("Database :: Error al insertar el mensaje");
		}
		return i;
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
	public boolean insertarNumerosDepartamento(int numero, String nombre) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO " + tablaNumerosPorDepartamento + " values ('"
					+ nombre + "', '" + numero + "')");
			System.out
					.println("aplicacion.Database.java\t::Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			System.err.println("Database :: Error al insertar en NumerosDepartamento");
			correcto = false;
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
			st.executeUpdate("INSERT INTO " + tablaIncidenciasPorUsuario + " values ("
					+ idIncidencia + ", " + numVendedor + ", '" + fechaInicio
					+ "','" + fechaFin + "');");
			System.out
					.println("aplicacion.Database.java\t::incidencia de una persona insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insertar la ListaTurnosPorContrato");
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
			st.addBatch("INSERT INTO " + tablaTrabaja + " values (" + numVendedor
					+ ", " + idTurno + ", '" + fecha + "', '" + horaEntrada
					+ "', '" + horaSalida + "');");
			System.out.println("aplicacion.Database.java\t::Insertado en la tabla trabaja a toda ostia");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insrtar en Trabaja");
		}
		return correcto;
	}
	
	public boolean executeBatch() {
		try {
			st = con.createStatement();
			st.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Database :: Error al ejecutar batch SQL");
		}
		return true;
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
		int i = -1;
		ResultSet r = null;
		try {
			Time tdesc = new Time(0);
			tdesc.setMinutes(Duracion);
			tdesc.setHours(0);
			st = con.createStatement();
			st
					.executeUpdate("INSERT INTO " + tablaTurnos + " (Descripcion, HoraEntrada, HoraSalida, HoraInicioDescanso, DuracionDescanso) VALUES ('"
							+ Descripcion
							+ "', '"
							+ HoraEntrada
							+ "', '"
							+ HoraSalida
							+ "', '"
							+ HoraInicioDescanso
							+ "', '"
							+ tdesc + "')");
//			System.out.println("aplicacion.Database.java\t::Turno insertado");
			//r = st.getGeneratedKeys();
			r = st.executeQuery("SELECT LAST_INSERT_ID()");
			if(r.next()) 
				i = r.getInt(1);
			else System.err.println("Database :: Error al obtener el identificador auto-generado");
		} catch (SQLException e) {
			i = -1;
			e.printStackTrace();
			System.err.println("Database :: Error al insertar el Turno");
		}
		return i;
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
			st.executeUpdate("INSERT INTO " + tablaTurnosPorContrato + " values ("
					+ idTurno + ", " + idContrato + ");");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			e.printStackTrace();
			System.err.println("Database :: Error al insertar enListaTurnosPorContrato");
		}
		return correcto;
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
			int rango, int idContrato, int idTurno, String Color, String Telefono,
			String Ssid, boolean HaEntrado, Date UltimoAcceso) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO " + tablaUsuarios + " values (" + id + ", '"
					+ nombre + "', '" + apellido1 + "' ,'" + apellido2 + "','"
					+ fechaNac + "'," + sexo + ",'" + email + "','"
					+ password + "'," + indicadorGrupo + ",'" + fechaContrato
					+ "','" + fechaEntrada + "'," + horasExtras + ","
					+ felicidad + "," + idioma + "," + rango + ","
					+ idContrato + "," + idTurno + ",'" + Color + "','"
					+ Telefono + "','" + Ssid + "'," + HaEntrado + ",'" + UltimoAcceso + "', 0)");
			correcto = true;
		} catch (SQLException e) {
			e.printStackTrace();
			correcto = false;
			System.err.println("Database :: Error al insertar Usuario");
		}
		return correcto;
	}
	
	/**
	 * Método que inserta en la tabla Ventas los valores correspondientes a cada
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
			st.executeUpdate("INSERT INTO " + tablaVentas + " values ('" + Fecha + "', '"
					+ numVentas + "', '" + idUsuario + "')");
//			System.out.println("aplicacion.Database.java\t::Ventas insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al insertar Ventas");
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
			st.executeUpdate("UPDATE " + tablaMensajes + " SET Marcado= " + marca
					+ " WHERE IdMensaje=" + id);
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.err.println("Database :: Error al Marcar el mensaje");
		}
		return correcto;
	}

	public boolean modificaDepartamento(String Nombre, int nvend) {
		int r = 0;
		try {
			st = con.createStatement();
			r = st.executeUpdate("UPDATE " + tablaDepartamentos + " SET JefeDepartamento='"
					+ nvend + "'" + "WHERE Nombre='" + Nombre + "';");
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("Database :: Error Cambiar Jefe de Dpto en la BD");
			return false;
		}
		return true;
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
					.executeQuery("SELECT * FROM " + tablaContratos + " WHERE IdContrato = "
							+ idContrato + ";");
		} catch (SQLException e) {
			System.err.println("Database :: Error de lectura de Contrato");
		}
		return result;
	}

	/**
	 * Método que lee todos los contratos de un departamento
	 * 
	 * @param departamento el identificador del departamento
	 * @return Devuelve un ResultSet con los datos leídos de la BD
	 */
	public ResultSet obtenContratosDepartamento(String departamento) {
		ResultSet r = null;
		String q = "SELECT * FROM " + tablaContratos + " WHERE IdContrato IN ("
		+ "SELECT IdContrato FROM " + tablaUsuarios + " u, " + tablaUsuariosPorDepartamento + " d "
		+ "WHERE u.NumVendedor=d.NumVendedor AND "
		+ "d.NombreDepartamento = '"+ departamento + "');";
		try {
			st = con.createStatement();
			r = st.executeQuery(q);
			
		} catch (SQLException e) {
			System.err.println("Database :: Error al realizar la consulta de contratos:\n\t" + q);
		}
		return r;
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
			r = st.executeQuery("SELECT * FROM " + tablaTrabaja + " WHERE Fecha>='" + inicio
					+ "' AND Fecha<='" + fin + "';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta en departamento ");
		}
		return r;
	}
	
	/**
	 * Método que lee los datos de un mes determinado de la tabla Trabaja 
	 * para un departamento concreto
	 * 
	 * @param mes el mes del cuadrante
	 * @param anio el año del cuadrante
	 * @param departamento el identificador del departamento
	 * @return Devuelve un ResultSet con los datos leídos de la BD
	 * 
	 * @author Daniel Dionne
	 */
	public ResultSet obtenCuadrante(int mes, int anio, String departamento) {
		ResultSet r = null;
		try {
			String inicio = anio + "-" + mes + "-" + "01";
			String fin = anio + "-" + mes + "-" + "31";
			st = con.createStatement();

			// SELECT * FROM Trabaja
			// WHERE Fecha>= 'inicio' AND Fecha<= 'fin'
			// AND NumVendedor IN (
			//		SELECT NumVendedor FROM DepartamentoUsuario
			//		WHERE NombreDepartamento = 'departamento');
			
			r = st.executeQuery("SELECT * FROM " + tablaTrabaja + " WHERE Fecha>='" + inicio
					+ "' AND Fecha<='" + fin + "' AND NumVendedor IN (" +
							"SELECT NumVendedor FROM " + tablaUsuariosPorDepartamento + " WHERE " +
							"NombreDepartamento = '"+ departamento +"');");
		} catch (SQLException e) {
			System.err.println("Database :: Error al realizar la consulta de cuadrantes");
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
			r = st.executeQuery("SELECT * FROM " + tablaDepartamentos + " WHERE Nombre='"
					+ nombre + "'");
		}

		catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta en departamento ");
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
		String q = "SELECT Nombre FROM " + tablaDepartamentos + " WHERE JefeDepartamento = " + nvend;
		try {
			st = con.createStatement();
			r = st.executeQuery(q);
		} catch (SQLException e) {
			System.err.println("Database :: Error al realizar la consulta de los ids departamentos:\n\t" + q);
		}
		return r;
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
					.executeQuery("SELECT IdMensaje from " + tablaDestinatariosMensaje + " WHERE NumVendedor = "
							+ idUsuario + ";");
		} catch (SQLException e) {
			System.err.println("Database :: Error obtenDestinatarios");
		}
		return result;

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
			rs = st.executeQuery("SELECT * FROM " + tablaDistribucionHorarios + " WHERE NombreDept ='"
							+ nombre
							+ "' AND DiaSemana="
							+ DiaSemana
							+ " ORDER BY Hora ASC");

		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta de la distribucion ");
		}
		return rs;
	}

	public ResultSet obtenDistribuciones(String nombre) {

		try {
			st = con.createStatement();
			rs = st
					.executeQuery("SELECT * FROM " + tablaDistribucionHorarios + " WHERE NombreDept ='"
							+ nombre
							+ " ORDER BY Hora ASC");

		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta de las distribuciones ");
		}
		return rs;
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
		String consulta = "SELECT * FROM " + tablaUsuarios + " WHERE ";
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
				r = st.executeQuery("SELECT * FROM " + tablaUsuarios + "");
			} else
				r = st.executeQuery(consulta);
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta del empleado a la carta");
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
					.println("Database :: Error al obtener los empleados de un departamento ");
		}
		return r;
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
			rs = st.executeQuery("SELECT * FROM " + tablaFestivos + " WHERE NombreDept ='"
					+ nombre + "' AND FechaInicio<='" + Fecha
					+ "' AND FechaFin>='" + Fecha + "' ORDER BY Hora ASC");

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err
					.println("Database :: Error al realizar la consulta de los festivos ");
		}
		return rs;
	}

	public ResultSet obtenFestivosMes(String nombre, int anio, int mes) {
		String FechaInicio = anio+mes+"1";
		String FechaFin = anio+mes+Integer.toString(Util.dameDias(mes, anio));
		
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM FESTIVOS WHERE NombreDept ='"
					+ nombre + "' AND FechaInicio>='" + FechaInicio
					+ "' AND FechaInicio<='" + FechaFin + "' ORDER BY Hora ASC");

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err
					.println("Database :: Error al realizar la consulta de los festivos ");
		}
		return rs;
	}

	public ResultSet obtenHorarioDpto(String dpto) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT HoraApertura, HoraCierre FROM DEPARTAMENTO WHERE Nombre='"+dpto+"';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta del horario de un Dpto");
		}
		return r;
	}

	public ResultSet obtenHorasTrabajoEmpleadoDia(int nv,int idturno,Date d) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT HoraEntrada, HoraSalida FROM Trabaja WHERE NumVendedor='"+nv+"' AND Fecha='"+d+"' AND IdTurno='"+idturno+"';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta del Jefe del Dpto");
		}
		return r;
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
			System.err.println("Database :: Error al obtener el maximo id de los mensajes");
		}
		return maximo;
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
		String q = "SELECT NombreDepartamento FROM " + tablaUsuariosPorDepartamento + " WHERE NumVendedor = " + nvend;
		try {
			st = con.createStatement();
			r = st.executeQuery(q);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta de los ids departamentos:\n\t" + q);
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
			r = st.executeQuery("SELECT NumVendedor FROM " + tablaUsuariosPorDepartamento + ", " + tablaDepartamentos + " WHERE JefeDepartamento = "
							+ nvend
							+ " and " + tablaDepartamentos + ".Nombre=NombreDepartamento");
		} catch (SQLException e) {
			System.err.println("Database :: Error al realizar la consulta de los subordinados ");
		}
		return r;
	}

	public ResultSet obtenJefedeDepartamento(String nombreDep) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT JefeDepartamento FROM DEPARTAMENTO WHERE Nombre='"+nombreDep+"';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta del Jefe del Dpto");
		}
		return r;
	}

	/**
	 * Método que lee los empleados un departamento concreto
	 * 
	 * @param departamento el identificador del departamento
	 * @return Devuelve un ResultSet con los datos leídos de la BD
	 * 
	 * @author Daniel Dionne
	 */
	public ResultSet obtenListaEmpleadosDepartamento(String departamento) {
		ResultSet r = null;
		try {
			st = con.createStatement();

			// SELECT * FROM " + tablaUsuarios + "
			// WHERE  NumVendedor IN (
			//		SELECT NumVendedor FROM DepartamentoUsuario
			//		WHERE NombreDepartamento = 'departamento');
			
			r = st.executeQuery("SELECT * FROM " + tablaUsuarios+ " WHERE NumVendedor IN (" +
							"SELECT NumVendedor FROM " + tablaUsuariosPorDepartamento + " WHERE " +
							"NombreDepartamento = '"+ departamento +"');");
			
		} catch (SQLException e) {
			System.err.println("Database :: Error al realizar la consulta de empleados");
		}
		return r;
	}
	/**
	 * Método que lee los empleados un departamento concreto
	 * 
	 * @param departamento el identificador del departamento
	 * @return Devuelve un ResultSet con los datos leídos de la BD
	 * 
	 * @author Daniel Dionne
	 */
	public ResultSet obtenListaEmpleadosSinJefeDepartamento(String departamento) {
		ResultSet r = null;
		try {
			st = con.createStatement();

			// SELECT * FROM " + tablaUsuarios+ "
			// WHERE  NumVendedor IN (
			//		SELECT NumVendedor FROM " + tablaUsuariosPorDepartamento + "
			//		WHERE NombreDepartamento = 'departamento');
			
			r = st.executeQuery("SELECT * FROM " + tablaUsuarios+ " WHERE NumVendedor IN (" +
							"SELECT NumVendedor FROM " + tablaUsuariosPorDepartamento + " WHERE " +
							"NombreDepartamento = '"+ departamento +"');");
			
		} catch (SQLException e) {
			System.err.println("Database :: Error al realizar la consulta de empleados");
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
					.executeQuery("SELECT ListaTurnosPorContrato.IdTurno FROM " + tablaUsuarios+ ",ListaTurnosPorContrato WHERE NumVendedor = "
							+ idEmpl
							+ " and USUARIO.IdContrato=ListaTurnosPorContrato.IdContrato");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al obtener los turnos de un contrato ");
		}
		return r;
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
			System.err.println("Database :: Error obtenMensaje ");
		}
		return result;
	}

	
	public ResultSet obtenMensajesEntrantes(int vendedor, int inicio, int desp) {
		ResultSet result = null;
		String q = "SELECT * FROM " + tablaDestinatariosMensaje + " JOIN " + tablaMensajes + " WHERE " + tablaDestinatariosMensaje + ".NumVendedor="
		+ vendedor
		+ " AND " + tablaDestinatariosMensaje + ".IdMensaje=" + tablaMensajes + ".IdMensaje LIMIT "
		+ inicio + "," + (inicio + desp) + ";";
		try {
			st = con.createStatement();
			result = st.executeQuery(q);
		} catch (SQLException e) {
			System.err.println("Database :: Error obtenMensajesEntrantes ");
		}
		return result;
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
					.executeQuery("SELECT * FROM " + tablaMensajes + " WHERE Remitente=" + id
							+ " ORDER BY Fecha LIMIT " + inicio + ","
							+ (inicio + desp));

		}

		catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta Lista de Turnos ");
		}
		return r;
	}
	
	public ResultSet obtenNombreTodosDepartamentos() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT Nombre FROM " + tablaDepartamentos);
		}
		catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta en departamento ");
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
					.executeQuery("SELECT JefeDepartamento FROM " + tablaUsuariosPorDepartamento + "," + tablaDepartamentos + " WHERE NumVendedor = "
							+ nvend
							+ " and " + tablaDepartamentos + ".Nombre=NombreDepartamento");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al obtener el superior del empleado ");
		}
		return r;
	}
	
	//Cambiar el jefe de un Dpto.
	
	/**
	 * 
	 * @return devuelve todas las filas de la tabla CONTRATO
	 */
	public ResultSet obtenTodosContratos() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM " + tablaContratos);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta en CONTRATO ");
		}
		return r;
	}	
	/**
	 * 
	 * @return devuelve todas las filas de la tabla DEPARTAMENTO
	 */
	public ResultSet obtenTodosDepartamentos() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM " + tablaDepartamentos);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta en departamento ");
		}
		return r;
	}	
	
	/**
	 * Método que obtiene el Nombre completo (Nombres + Apellidos) de todos los jefes (usuarios de rango=2)
	 * 
	 * @return Devuelve un ResultSet con los datos leídos de la BD
	 */
	public ResultSet obtenTodosNombresJefes() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT Nombre, Apellido1, Apellido2, NumVendedor FROM " + tablaUsuarios + " WHERE Rango='"+2+"';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta de nombres de (posibles) jefes de departamento ");
		}
		return r;
	}	
	
	
	public ResultSet obtenTodosNumerosDEPARTAMENTOs() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT Numero FROM " + tablaNumerosPorDepartamento + " ';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta del Jefe del Dpto");
		}
		return r;
	}		
	
	

	public ResultSet obtenTodosNumVendedoresJefes() {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT NumVendedor FROM " + tablaUsuarios + " WHERE Rango='"+2+"';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta de nombres de (posibles) jefes de departamento ");
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
			r = st.executeQuery("SELECT * FROM " + tablaTurnos);
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta en TURNOS ");
		}
		return r;
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
			result = st.executeQuery("SELECT * FROM " + tablaTurnos + " WHERE IdTurno="
					+ turno + ";");
		} catch (SQLException e) {
			System.err.println("Database :: Error obtenTurno ");
		}
		return result;
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
					.executeQuery("SELECT IdTurno FROM " + tablaTrabaja + " WHERE NumVendedor = "
							+ idEmpleado + " and Fecha ='" + dia + "'");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al obtener el turno de una fecha concreta ");
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
					.executeQuery("SELECT IdTurno FROM " + tablaUsuarios + " WHERE NumVendedor="
							+ nvend + ";");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta del turno favorito del usuario ");
		}
		return r;
	}
	
	/**
	 * Método que lee todos los turnos de los contratos de un departamento
	 * 
	 * @param departamento el identificador del departamento
	 * @return Devuelve un ResultSet con los datos leídos de la BD
	 */
	public ResultSet obtenTurnosDepartamento(String departamento) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			/**
			Otra posibilidad, un poco menos guarra
			
			SELECT * from TURNOS WHERE IdTurno IN (
				SELECT l.IdTurno FROM ListaTurnosPorContrato l, CONTRATO c, USUARIO u, DepartamentoUsuario d WHERE
				l.IdContrato = c.IdContrato AND
				c.IdContrato = u.IdContrato AND
				u.NumVendedor = d.NumVendedor AND
				d.NombreDepartamento = "DatosFijos"
			);

			 */
			
//			r = st.executeQuery("SELECT * FROM TURNOS WHERE IdTurno IN ("
//					+ "SELECT IdTurno FROM ListaTurnosPorContrato WHERE IdContrato IN ("
//					+ "SELECT IdContrato FROM CONTRATO WHERE IdContrato IN ("
//					+ "SELECT IdContrato FROM " + tablaUsuarios+ " WHERE NumVendedor IN ("
//					+ "SELECT NumVendedor FROM " + tablaUsuariosPorDepartamento + " WHERE "
//					+ "NombreDepartamento = '"+ departamento +"'))));");

			r = st.executeQuery("SELECT * FROM " + tablaTurnos + " WHERE IdTurno IN ("
					+ "SELECT l.IdTurno FROM " + tablaTurnosPorContrato + " l, " + tablaContratos + " c, " + tablaUsuarios + " u, " + tablaUsuariosPorDepartamento + " d WHERE "
					+ "l.IdContrato = c.IdContrato AND "
					+ "c.IdContrato = u.IdContrato AND "
					+ "u.NumVendedor = d.NumVendedor AND "
					+ "NombreDepartamento = '"+ departamento +"');");
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Database :: Error al realizar la consulta de turnos de contratos");
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
					.executeQuery("SELECT IdTurno FROM " + tablaTurnosPorContrato + " WHERE IdContrato="
							+ idContrato + ";");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err
					.println("Database :: Error al realizar la consulta en departamento ");
		}
		return r;
	}	
	
	
	public void run() {
		abrirConexion();
	}	
	
	public ResultSet trabajaEmpleadoDia(int nv,Date d) {
		ResultSet r = null;
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT IdTurno FROM " + tablaTrabaja + " WHERE NumVendedor='"+nv+"' AND Fecha='"+d+"';");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Database :: Error al realizar la consulta del Jefe del Dpto");
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
			System.err.println("Database :: Error al vaciar la tabla");
			return false;
		}
		return true;
	}	

	public void eliminarTodasLasTablas() {
		try {
			st = con.createStatement();
			st.addBatch("DROP TABLE " + tablaContratosPorDepartamento + ";");
			st.addBatch("DROP TABLE " + tablaDestinatariosMensaje + ";");
			st.addBatch("DROP TABLE " + tablaDistribucionHorarios + ";");
			st.addBatch("DROP TABLE " + tablaFestivos + ";");
			st.addBatch("DROP TABLE " + tablaIncidenciasPorUsuario + ";");
			st.addBatch("DROP TABLE " + tablaIssues + ";");
			st.addBatch("DROP TABLE " + tablaMensajes + ";");
			st.addBatch("DROP TABLE " + tablaOpiniones + ";");
			st.addBatch("DROP TABLE " + tablaTrabaja + ";");
			st.addBatch("DROP TABLE " + tablaTurnosPorContrato + ";");
			st.addBatch("DROP TABLE " + tablaUsuariosPorDepartamento + ";");
			st.addBatch("DROP TABLE " + tablaVentas + ";");
			st.addBatch("DROP TABLE " + tablaDepartamentos + ";");
			st.addBatch("DROP TABLE " + tablaUsuarios + ";");
//			st.addBatch("DROP TABLE " + tablaPermisos + ";");
			st.addBatch("DROP TABLE " + tablaContratos + ";");
			st.addBatch("DROP TABLE " + tablaTurnos + ";");
			st.addBatch("DROP TABLE " + tablaNumerosPorDepartamento + ";");
			st.addBatch("DROP TABLE " + tablaIncidencias + ";");
			st.executeBatch();
			System.out.println("aplicacion.Database.java\t:: Estructura de tablas y dependencias eliminadas correctamente.");

		} catch (SQLException e) {
			System.err.println("Database :: Error al borrar las tablas - ¿Quizá estén ya vacías?");
		}
	}

	public void crearTablas() {
		try {
			st = con.createStatement();
			st.addBatch("Create table contrato (" +
					"IdContrato Int NOT NULL AUTO_INCREMENT," +
					"TurnoInicial Int NOT NULL," +
					"Nombre Varchar(20) ," +
					"Patron Varchar(30) ," +
					"DuracionCiclo Int ," +
					"Salario Decimal(8,2) ," +
					"Tipo tinyint(1)," +
					"UNIQUE (Nombre)," +
					"Primary Key (IdContrato)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table contratodepartamento(" +
					"NombreDept varchar(20)," +
					"IdContrato Int NOT NULL," +
					"Primary Key (NombreDept,IdContrato)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
			
			st.addBatch("Create table departamento (" +
					"Nombre Varchar(20) ," +
					"JefeDepartamento Int NOT NULL," +
					"HoraApertura time," +
					"HoraCierre time," +
					"Primary Key (nombre)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table departamentousuario (" +
					"NumVendedor  int not null," +
					"NombreDepartamento Varchar(20)  not null," +
					"Primary Key (NumVendedor,NombreDepartamento)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");


			st.addBatch("Create table destinatario (" +
					"NumVendedor Int NOT NULL," +
					"IdMensaje Int NOT NULL," +
					"Primary Key (NumVendedor,IdMensaje)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
					
			st.addBatch("Create table distribucion (" +
					"Hora Int NOT NULL," +
					"DiaSemana int(12) NOT NULL," +
					"Patron Varchar(20) ," +
					"NumMax Int ," +
					"NumMin Int ," +
					"NombreDept varchar(20) NOT NULL," +
					"Primary Key (Hora,DiaSemana,NombreDept)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table festivos (" +
					"Hora Int NOT NULL," +
					"FechaInicio Date NOT NULL," +
					"FechaFin Date ," +
					"Patron Varchar(20) ," +
					"NumMax Int ," +
					"NumMin Int ," +
					"NombreDept varchar(20) NOT NULL," +
					"Primary Key (Hora,FechaInicio,Nombredept))" +
					" ENGINE=InnoDB DEFAULT CHARSET=latin1;");


			st.addBatch("Create table incidencias (" +
					"IdIncidencia Int NOT NULL AUTO_INCREMENT," +
					"Descripcion Varchar(40)  not null," +
					"Primary Key (IdIncidencia)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table issues (" +
					"Id Int not null," +
					"Text longtext not null," +
					"Primary Key (Id)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table listaturnosporcontrato(" +
					"IdTurno Int NOT NULL," +
					"IdContrato Int NOT NULL," +
					"Primary Key (IdTurno,IdContrato)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table mensaje (" +
					"IdMensaje Int NOT NULL AUTO_INCREMENT," +
					"Remitente Int NOT NULL," +
					"Fecha Datetime ," +
					"Asunto Varchar(100) ," +
					"Texto Text ," +
					"Marcado tinyint(1)," +
					"visto tinyint(1)," +
					"Primary Key (IdMensaje)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table numerosdepartamentos (" +
					"Nombre Varchar(20) ," +
					"Numero Int NOT NULL," +
					"Primary Key (nombre,numero)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table opinion (" +
					"Id Int NOT NULL AUTO_INCREMENT," +
					"NombreApellidos varchar(30) not null," +
					"Grupo varchar(20) ," +
					"Respuesta1 longtext," +
					"Respuesta2 longtext," +
					"Respuesta3 longtext," +
					"Respuesta4 longtext," +
					"Respuesta5 longtext," +
					"Primary Key (Id)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

/*			st.addBatch("Create table permisos (" +
					"Rango int not null," +
					"OPCIONESPORDEFINIR Char(20) ," +
					"Primary Key (Rango)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
*/

			st.addBatch("Create table tieneincidencia (" +
					"IdIncidencia Int NOT NULL," +
					"NumVendedor Int NOT NULL," +
					"FechaInicio Date NOT NULL," +
					"FechaFin Date ," +
					"check(fechainicio<fechafin)," +
					"Primary Key (IdIncidencia,NumVendedor,FechaInicio)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table trabaja (" +
					"NumVendedor Int NOT NULL," +
					"IdTurno Int NOT NULL," +
					"Fecha Date NOT NULL," +
					"HoraEntrada Time ," +
					"HoraSalida Time ," +
					"check(horaentrada<horasalida)," +
					"Primary Key (NumVendedor,IdTurno,Fecha)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table turnos (" +
					"IdTurno Int NOT NULL AUTO_INCREMENT," +
					"Descripcion Varchar(40) ," +
					"HoraEntrada Time ," +
					"HoraSalida Time ," +
					"HoraInicioDescanso time ," +
					"DuracionDescanso time," +
					"check (horaentrada<horasalida)," +
					"Primary Key (IdTurno)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table usuario (" +
					"NumVendedor Int NOT NULL," +
					"Nombre Varchar(20) ," +
					"Apellido1 Varchar(20) ," +
					"Apellido2 Varchar(20) ," +
					"FechaNacimiento Date ," +
					"Sexo int ," +
					"Email Varchar(30) ," +
					"Password Varchar(10) ," +
					"IndicadorGrupo int ," +
					"FechaContrato Date ," +
					"FechaEntrada Date ," +
					"HorasExtras Int ," +
					"Felicidad int," +
					"Idioma int," +
					"Rango int NOT NULL," +
					"IdContrato Int NOT NULL," +
					"IdTurno Int NOT NULL," +					
					"Color varchar(6)," +
					"Telefono varchar(12)," +
					"Ssid varchar(20)," +
					"HaEntrado bool," +
					"UltimoAcceso Datetime," +
					"Posicion int," +
					"check (length(numvendedor)=3)," +
					"check (sexo in(0,1))," +
					"check(idioma in(0,1,2))," +
					"check(indicadorgrupo in(0,1))," +
					"Primary Key (NumVendedor)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			st.addBatch("Create table ventas (" +
					"NumVendedor Int," +
					"Fecha Datetime ," +
					"Importe Decimal(10,2) ," +
					"Primary Key (NumVendedor,fecha)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
			st.executeBatch();
			System.out.println("aplicacion.Database.java\t:: Estructura de tablas generada correctamente.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void crearDependencias() {
		try {
			st = con.createStatement();

			st.addBatch("Alter table tieneincidencia add Index IX_TieneIncidencia (IdIncidencia);");
			st.addBatch("Alter table tieneincidencia add Foreign Key (IdIncidencia) references incidencias (IdIncidencia) on delete cascade on update cascade;");
			st.addBatch("Alter table ventas add Index IX_vende (NumVendedor);");
			st.addBatch("Alter table ventas add Foreign Key (NumVendedor) references usuario (NumVendedor) on delete restrict on update cascade;");
			st.addBatch("Alter table tieneincidencia add Index IX_TieneIncidencia2 (NumVendedor);");
			st.addBatch("Alter table tieneincidencia add Foreign Key (NumVendedor) references usuario (NumVendedor) on delete cascade on update cascade;");
			st.addBatch("Alter table trabaja add Index IX_Trabaja1 (NumVendedor);");
			st.addBatch("Alter table trabaja add Foreign Key (NumVendedor) references usuario (NumVendedor) on delete restrict on update restrict;");
			st.addBatch("Alter table destinatario add Index IX_Recibe1 (NumVendedor);");
			st.addBatch("Alter table destinatario add Foreign Key (NumVendedor) references usuario (NumVendedor) on delete restrict on update cascade;");
			st.addBatch("Alter table departamento add Index IX_jefedepartamento (JefeDepartamento);");
			st.addBatch("Alter table departamento add Foreign Key (JefeDepartamento) references usuario (NumVendedor) on delete restrict on update cascade;");
			st.addBatch("Alter table mensaje add Index IX_envia (Remitente);");
			st.addBatch("Alter table mensaje add Foreign Key (Remitente) references usuario (NumVendedor) on delete restrict on update cascade;");
			st.addBatch("Alter table listaturnosporcontrato add Index IX_TurnosPorContrato (IdContrato);");
			st.addBatch("Alter table listaturnosporcontrato add Foreign Key (IdContrato) references contrato (IdContrato) on delete cascade on update cascade;");
			st.addBatch("Alter table usuario add Index IX_TieneContrato (IdContrato);");
			st.addBatch("Alter table usuario add Foreign Key (IdContrato) references contrato (IdContrato) on delete restrict on update cascade;");
			st.addBatch("Alter table listaturnosporcontrato add Index IX_TurnosPorContratos (IdTurno);");
			st.addBatch("Alter table listaturnosporcontrato add Foreign Key (IdTurno) references turnos (IdTurno) on delete cascade on update cascade;");
			st.addBatch("Alter table trabaja add Index IX_Trabaja2 (IdTurno);");
			st.addBatch("Alter table trabaja add Foreign Key (IdTurno) references turnos (IdTurno) on delete restrict on update cascade;");
			st.addBatch("Alter table contrato add Index IX_TurnoInicial (TurnoInicial);");
			st.addBatch("Alter table contrato add Foreign Key (TurnoInicial) references turnos (IdTurno) on delete restrict on update cascade;");
			st.addBatch("Alter table usuario add Index IX_Prefiere (IdTurno);");
			st.addBatch("Alter table usuario add Foreign Key (IdTurno) references turnos (IdTurno) on delete restrict on update cascade;");
//			st.addBatch("Alter table usuario add Index IX_TienePermiso (Rango);");
//			st.addBatch("Alter table usuario add Foreign Key (Rango) references permisos (Rango) on delete restrict on update cascade;");
			
			
			st.addBatch("Alter table distribucion add Index IX_Relationship10 (NombreDept);");
			st.addBatch("Alter table distribucion add Foreign Key (NombreDept) references departamento (Nombre) on delete cascade on update cascade;");
			st.addBatch("Alter table festivos add Index IX_Relationship11 (NombreDept);");
			st.addBatch("Alter table festivos add Foreign Key (NombreDept) references departamento (Nombre) on delete cascade on update cascade;");
			st.addBatch("Alter table destinatario add Index IX_Recibe2 (IdMensaje);");
			st.addBatch("Alter table destinatario add Foreign Key (IdMensaje) references mensaje (IdMensaje) on delete cascade on update cascade;");

			
			st.addBatch("Alter table departamentousuario add Index IX_Relationship12 (NumVendedor);");
			st.addBatch("Alter table departamentousuario add Foreign Key (NumVendedor) references usuario (NumVendedor) on delete cascade on update cascade;");
			st.addBatch("Alter table departamentousuario add Index IX_Relationship13 (NombreDepartamento);");
			st.addBatch("Alter table departamentousuario add Foreign Key (NombreDepartamento) references departamento (Nombre) on delete restrict on update cascade;");

			st.addBatch("Alter table contratodepartamento add Index IX_Relationship19 (NombreDept);");
			st.addBatch("Alter table contratodepartamento add Foreign Key (NombreDept) references departamento (Nombre) on delete cascade on update cascade;");

			st.addBatch("Alter table contratodepartamento add Index IX_Relationship17 (IdContrato);");
			st.addBatch("Alter table contratodepartamento add Foreign Key (IdContrato) references contrato (IdContrato) on delete restrict on update cascade;");

			st.executeBatch();
			System.out.println("aplicacion.Database.java\t:: Dependencias generadas correctamente.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}