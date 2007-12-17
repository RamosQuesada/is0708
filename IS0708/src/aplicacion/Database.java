package aplicacion;

import java.sql.*;
import java.util.ArrayList;

import algoritmo.Calendario;

/**
 * Aqu� se encuentran los m�todos de acceso a la base de datos.
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
	 * 
	 * @return <i>true</i> si la conexi�n est� abierta, <i>false</i> en caso
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
			System.out.println("Error al realizar la consulta del empleado ");
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
			r = st.executeQuery("SELECT JefeDepartamento FROM DepartamentoUsuario,DEPARTAMENTO WHERE NumVendedor = "
							+ nvend
							+ " and DEPARTAMENTO.Nombre=NombreDepartamento");
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error al realizar la consulta del empleado ");
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
			System.out.println("Error al realizar la consulta del empleado ");
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
			r = st.executeQuery("SELECT NombreDepartamento FROM DepartamentoUsuario WHERE NumVendedor = "
							+ nvend);
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error al realizar la consulta del empleado ");
		}
		return r;
	}
	
	/**
	 * Método que obtiene los empleados que pertenecen al id del departemento dado
	 * @param idDept	identificador de departamento
	 * @return			los empleados que pertenecen al id del departemento dado
	 */
	public ResultSet obtenEmpleadosDepartamento(int idDept){
		ResultSet r = null;
		String subconsulta="SELECT NumVendedor FROM DepartamentoUsuario,NumerosDEPARTAMENTOs WHERE Numero ="+idDept+
							" and NombreDepartamento=Nombre";
		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM USUARIO WHERE NumVendedor IN ("+subconsulta+") ORDER BY IdContrato");
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error al realizar la consulta del empleado ");
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
		boolean to2null=true;		
		String consulta="SELECT * FROM USUARIO WHERE ";
		int numero=0;
		if (idEmpl!=null) {			
			consulta+="NumVendedor= "+idEmpl;
			to2null=false;
			numero=1;
		}
		if (idContrato!=null) {
			if (numero==0) consulta+="IdContrato= "+idContrato;
			else consulta+=" and IdContrato= "+idContrato; 
			to2null=false;
			numero=1;
		}
		if (nombre!=null) {
			if (numero==0) consulta+="Nombre= '"+nombre+"'";
			else consulta+=" and Nombre= '"+nombre+"'"; 
			to2null=false;
			numero=1;
		}
		if (apellido1!=null) {
			if (numero==0) consulta+="Apellido1= '"+apellido1+"'";
			else consulta+=" and Apellido1= '"+apellido1+"'"; 
			to2null=false;
			numero=1;
		}
		if (apellido2!=null) {
			if (numero==0) consulta+="Apellido2= '"+apellido2+"'";
			else consulta+=" and Apellido2= '"+apellido2+"'"; 
			to2null=false;
			numero=1;
		}
		if (rango!=null) {
			if (numero==0) consulta+="Rango= "+rango;
			else consulta+=" and Rango= "+rango; 
			to2null=false;
			numero=1;			
		}
		if (idDpto!=null) {
			subconsulta="SELECT NumVendedor FROM DepartamentoUsuario WHERE NombreDepartamento= '"+idDpto+"'";
			to2null=false;
			if (numero==0) consulta+="NumVendedor IN ("+subconsulta+")";
			else consulta+=" and NumVendedor IN ("+subconsulta+")";
		}
		try {
			st = con.createStatement();
			if (to2null) {
				r = st.executeQuery("SELECT * FROM USUARIO");
			}
			else r = st.executeQuery(consulta);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al realizar la consulta del empleado a la carta");
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
			System.out.println("Usuario insertado");
			correcto = true;
		} catch (SQLException e) {
			e.printStackTrace();
			correcto = false;
		}
		return correcto;
	}
	
	/**
	 * Método que elimina de la base de datos al usuario cuyo NumVendedor se facilita
	 * @param id	Número de vendedor del usuario
	 * @return		Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraUsuario(int id){
		boolean correcto = false;
	    try{
	        st=con.createStatement();
	        st.executeUpdate( "DELETE FROM USUARIOS WHERE NumVendedor="+id);
	        System.out.println("Usuario Borrado");
	        correcto=true;
	    }catch(SQLException e) {
	        System.out.println("Error al Borrar el usuario");
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
			System.out.println("Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			System.out.println("Error al insertar el departamento");
			correcto = false;
		}
		return correcto;
	}
	
	/**
	 * Método que elimina de la base de datos al departamento cuyo nombre se facilita
	 * @param nombre	Nombre del departamento
	 * @return		Informa sobre si se ha podido realizar el borrado o no
	 */
	public boolean borraDepartamento(String nombre){
		boolean correcto = false;
	    try{
	        st=con.createStatement();
	        st.executeUpdate( "DELETE FROM DEPARTAMENTO WHERE Nombre="+nombre);
	        System.out.println("Departamento Borrado");
	        correcto=true;
	    }catch(SQLException e) {
	        System.out.println("Error al Borrar el departamento");
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
			System.out.println("Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			System.out.println("Error al insertar en NumerosDepartamento");
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
			System.out.println("Departamento insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insertar en DepartamentoUsuario");
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
			System.out.println("Ventas insertada");
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
	public boolean insertarDistribucion(int Hora, int DiaSemana,
			String Patron, int NumMax, int NumMin, int IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO DISTRIBUCION values ('" + Hora
					+ "', '" + DiaSemana + "', '" + Patron + "', '" + NumMax
					+ "', '" + NumMin + "', '" + IdDepartamento + "')");
			System.out.println("Distribucion insertada");
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
	public boolean insertarFestivo(int Hora, Date FechaInicio,
			Date FechaFin, String Patron, int NumMax, int NumMin,
			int IdDepartamento) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO FESTIVOS values ('" + Hora + "', '"
					+ FechaInicio + "', '" + FechaFin + "', '" + Patron
					+ "', '" + NumMax + "', '" + NumMin + "', '"
					+ IdDepartamento + "')");
			System.out.println("Festivo insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}

	/**
	 * Metodo que inserta un turno en la BD
	 * 
	 * @param idTurno
	 *            Pendiente de revision
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
	 * @return	Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertarTurno(int idTurno, String Descripcion,
			Time HoraEntrada, Time HoraSalida, Time HoraInicioDescanso,
			int Duracion) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO TURNOS values ('" + idTurno + "', '"
					+ Descripcion + "', '" + HoraEntrada + "', '" + HoraSalida
					+ "', '" + HoraInicioDescanso + "', '" + Duracion + "')");
			System.out.println("Turno insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insertar el Turno");
		}
		return correcto;
	}
	
	/**
	 * Método que elimina un turno de la base de datos
	 * @param id	Identificador del turno
	 * @return		Informa sobre si se ha podido realizar el borrado o no	
	 */
	public boolean borraTurno(int id){
		boolean correcto = false;
	    try{
	        st=con.createStatement();
	        st.executeUpdate( "DELETE FROM TURNOS WHERE IdTurno="+id);
	        System.out.println("Turno Borrado");
	        correcto=true;
	    }catch(SQLException e) {
	        System.out.println("Error al Borrar el turno");
	    }
	    return correcto;
	}
	
	/**
	 * Método que inserta un mensaje en la base de datos
	 * @param remitente
	 *            representa el remitente de un mensaje
	 * @param fecha
	 *            fecha en la que se ha guardado el mensaje
	 * @param asunto
	 *            campo que indica que contiene el mensaje de forma breve
	 * @param texto
	 *            contenido de los mensajes
	 * @param marcado
	 * 			  indica si el mensaje está marcado o no
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */

	public boolean insertarMensaje(int remitente, Date fecha, String asunto,
			String texto,boolean marcado) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO MENSAJE values ( 0 , " + remitente
					+ ", '" + fecha + "', '" + asunto + "', '" + texto + "', " +marcado+");");
			System.out.println("Mensaje insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			e.printStackTrace();
			System.out.println("Error al insertar el mensaje");
		}
		return correcto;
	}

	/**
	 * Método que elimina un mensaje de la base de datos
	 * @param id	Identificador del mensaje
	 * @return		Informa sobre si se ha podido realizar el borrado o no	
	 */
	public boolean borraMensaje(int id){
		boolean correcto = false;
	    try{
	        st=con.createStatement();
	        st.executeUpdate( "DELETE FROM MENSAJE WHERE IdMensaje="+id);
	        System.out.println("Mensaje Borrado");
	        correcto=true;
	    }catch(SQLException e) {
	        System.out.println("Error al Borrar el mensaje");
	    }
	    return correcto;
	}
	
	/**
	 * 
	 * @param marca marca del mensaje(true si marcar y false si desmarcar)
	 * @param id	identificador del mensaje a marcar
	 * @return		Indica si se ha podido marcar o no el mensaje	
	 */
	public boolean marcaMensaje(boolean marca,int id){
		boolean correcto = false;
	    try{
	        st=con.createStatement();
	        st.executeUpdate("UPDATE MENSAJE SET Marcado= "+marca+ " WHERE IdMensaje="+id);	        
	        System.out.println("Mensaje Marcado");
	        correcto=true;
	    }catch(SQLException e) {
	        System.out.println("Error al Marcar el mensaje");
	    }
	    return correcto;
	}
	
	/**
	 * Método que inserta en la tabla ListaDestinatarios
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
			System.out.println("Destinatario insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insertar en ListaDestinatarios");
		}
		return correcto;
	}

	/**
	 * Método que inserta un contrato en la base de datos
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
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertarContrato(int idContrato, int turnoInicial,
			String nombre, String patron, int duracionCiclo, double salario) {
		boolean correcto = false;
		try {
			st = con.createStatement();
			st.executeUpdate("INSERT INTO CONTRATO values (" + idContrato
					+ ", " + turnoInicial + ", '" + nombre + "', '" + patron
					+ "', " + duracionCiclo + ", " + salario + ");");
			System.out.println("Contrato insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insertar el contrato");
		}
		return correcto;
	}
	
	/**
	 * Método que elimina un contrato de la base de datos
	 * @param id	Identificador del contrato
	 * @return		Informa sobre si se ha podido realizar el borrado o no	
	 */
	public boolean borraContrato(int id){
		boolean correcto = false;
	    try{
	        st=con.createStatement();
	        st.executeUpdate( "DELETE FROM CONTRATO WHERE IdContrato="+id);
	        System.out.println("Contrato Borrado");
	        correcto=true;
	    }catch(SQLException e) {
	        System.out.println("Error al Borrar el contrato");
	    }
	    return correcto;
	}

	/**
	 * Método que inserta en la tabla trabaja (que refleja los cuadrantes)
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
			System.out.println("Insertado en la tabla trabaja");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insrtar en Trabaja");
		}
		return correcto;
	}


	/**
	 * Método que inserta en ListaTurnosPorContrato
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
			System.out.println("turnoPorContrato insertado");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insertar enListaTurnosPorContrato");
		}
		return correcto;
	}

	/**
	 * Método que inserta una incidencia en la base de datos
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
			System.out.println("Incidencia insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insertar la incidencia");
		}
		return correcto;
	}

	/**
	 * Metodo que inserta en la tabla TieneIncidencia
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
			System.out.println("incidencia de una persona insertada");
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
			System.out.println("Error al insertar la ListaTurnosPorContrato");
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
					.executeQuery("SELECT * FROM DISTRIBUCION WHERE NombreDept ="
							+ nombre + " AND DiaSemana=" + DiaSemana);

		} catch (SQLException e) {
			// TODO: handle exception
			System.out
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
			rs = st
					.executeQuery("SELECT * FROM FESTIVOS WHERE NombreDept ="
							+ nombre
							+ " AND FechaInicio<='"
							+ Fecha
							+ "' AND FechaFin>='" + Fecha + "' ORDER BY Hora ASC");

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error al realizar la consulta de los festivos ");
		}
		return rs;
	}

	/**
	 * 
	 * 
	 * @return Devuelve un ResultSet con los turnos de los empleados
	 */

	public ResultSet obtenListaTurnosEmpleados() {
		ResultSet r = null;

		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM TURNOS ");

		}

		catch (SQLException e) {
			// TODO: handle exception
			System.out
					.println("Error al realizar la consulta Lista de Turnos ");
		}
		return r;
	}

	public ResultSet obtenMensajesSalientes(int id, int inicio, int desp) {
		ResultSet r = null;

		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM MENSAJE WHERE Remitente="+id+" ORDER BY Fecha LIMIT "+inicio+","+(inicio+desp));

		}

		catch (SQLException e) {
			// TODO: handle exception
			System.out
					.println("Error al realizar la consulta Lista de Turnos ");
		}
		return r;
	}
	
	public ResultSet obtenDepartamento(String nombre) {
		ResultSet r = null;

		try {
			st = con.createStatement();
			r = st.executeQuery("SELECT * FROM DEPARTAMENTO WHERE Nombre='"+nombre+"'");
		}

		catch (SQLException e) {
			// TODO: handle exception
			System.out
					.println("Error al realizar la consulta en departamento ");
		}
		return r;
	}
	
	public ResultSet obtenContrato(int idContrato){
		ResultSet result=null;
		try {
			st = con.createStatement();
			result = st.executeQuery("SELECT * FROM CONTRATO WHERE IdContrato = "+ idContrato+";");
		} catch (SQLException e) {
			System.out.println("Error de lectura de Contrato");
		}
		return result;
	}
	/**
	 *obtiene el identificador del ultimo mensaje enviado 
	 * @return
	 */
	public int obtenIdMensaje(){
		ResultSet result=null;
		int maximo=-1;
		try {
			st = con.createStatement();
			result = st.executeQuery("SELECT Max(IdMensaje)as Maximo from MENSAJE ;");
			result.next();
			maximo=result.getInt("Maximo");
		} catch (SQLException e) {
			System.out.println("Error al obtener el maximo id de los mensajes");
		}
		return maximo;
	}
	/**
	 * lista de identificadores de mensajes que ha recibido un usuario 
	 * @param idUsuario usuario que ha recibido los mensajes
	 * @return ResultSet con los identificadores de los mensajes
	 */
	public ResultSet obtenDestinatarios(int idUsuario){
		ResultSet result=null;
		try {
			st = con.createStatement();
			result = st.executeQuery("SELECT IdMensaje from DESTINATARIO WHERE NumVendedor = " + idUsuario + ";");			
		} catch (SQLException e) {
			System.out.println("Error obtenDestinatarios");
		}
		return result;
		
	}
	/**
	 * recupera un mensaje de la base de datos indicando el numero del mismo
	 * @param mensaje indica el mensaje que se quiere recuperar por identificador de mensaje
	 * @return ResulSet el mensaje indicado 
	 */
	public ResultSet obtenMensaje(int mensaje) {
		ResultSet result = null;
		try {
			st = con.createStatement();
			result = st.executeQuery("SELECT * FROM MENSAJE WHERE IdMensaje="+mensaje+";");
		}
		catch (SQLException e) {
			System.out.println("Error obtenMensajesEntrantes ");
		}
		return result;
	}
}
