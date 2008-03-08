package aplicacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import org.eclipse.swt.graphics.Color;
import java.util.GregorianCalendar;

import algoritmo.*;

/**
 * Esta clase conecta el modelo (la base de datos) con la vista (los interfaces)
 * 
 * @author Todos
 */
public class Controlador {
	private Vista _vista;
	private Database _db;
	private Empleado _empleadoActual;
	private GregorianCalendar _calendario;
	final boolean _modoDebug;

	public Controlador(Database baseDatos, boolean modoDebug) {
		_modoDebug = modoDebug;
		_db = baseDatos;
		// Crea calendario con la hora actual;
		_calendario = new GregorianCalendar();
		_empleadoActual = null;
	}
	
	public Controlador(Vista vista, Database baseDatos, boolean modoDebug) {
		this._vista = vista;
		_db = baseDatos;
		_modoDebug = modoDebug;
		_empleadoActual = null;
	}

	/**
	 * Devuelve si la aplicación se ha iniciado en modo debug.
	 * 
	 * @return <i>true</i> si la aplicación se ha iniciado en modo de
	 *         corrección de errores
	 */
	public boolean getModoDebug() {
		return _modoDebug;
	}

	/**
	 * Asigna el empleado que ha iniciado sesión.
	 * 
	 * @param emp
	 *            el empleado que ha iniciado sesión
	 */
	public void setEmpleadoActual(int idEmp) {
		_empleadoActual = this.getEmpleado(idEmp);
	}

	/**
	 * Asigna el empleado que ha iniciado sesión.
	 * 
	 * @param emp
	 *            el empleado que ha iniciado sesión
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
	/**
	 * Asigna una vista al controlador 
	 * @param vista
	 */
	public void setVista(Vista vista) {
		this._vista = vista;
	}
	
	/**
	 * Abre una conexion a la base de datos si no estaba abierta
	 *
	 */
	public void abrirConexionBD(){
		if(!_db.conexionAbierta()) _db.abrirConexion();
	}
	
	/**
	 * Cierra la conexion a la base de datos
	 *
	 */
	public void cerrarConexionBD(){
		if(_db.conexionAbierta()) _db.cerrarConexion();
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
			ResultSet rs = _db.dameEmpleado(idEmpl);
			if (rs.next()) {
				rs.first();
				String nombre = rs.getString("Nombre");
				String apellido1 = rs.getString("Apellido1");
				String apellido2 = rs.getString("Apellido2");
				Date fechaNac = rs.getDate("FechaNacimiento");
				int id = rs.getInt("NumVendedor");
				String email = rs.getString("Email");
				String password = rs.getString("Password");
				int sexo = rs.getInt("Sexo");
				int grupo = rs.getInt("IndicadorGrupo");
				int rango = rs.getInt("Rango");
				int idContrato = rs.getInt("IdContrato");
				Date fechaContrato = rs.getDate("FechaContrato");
				Date fechaAlta = rs.getDate("FechaEntrada");
				Color color = null;
				int idSuperior = this.getIdSuperior(idEmpl);
				ArrayList<Integer> idSubordinados = this
						.getIdsSubordinados(idEmpl);
				ArrayList<String> idDepartamentos = this
						.getIdsDepartamentos(idEmpl);
				int felicidad = rs.getInt("Felicidad");
				int idioma = rs.getInt("Idioma");
				//TODO cargarlo de la BD
				int turnoFavorito = rs.getInt("IdTurno");
				emp = new Empleado(idSuperior, id, nombre, apellido1,
						apellido2, fechaNac, sexo, email, password, grupo,
						rango, idContrato, fechaContrato, fechaAlta, color,
						null, idSubordinados, felicidad, idioma, turnoFavorito);
				emp.setIDDepartamentos(idDepartamentos);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Controlador: Error al obtener el Empleado "
					+ idEmpl + " de la base de datos");
		}

		return emp;

	}

	/**
	 * Método que devuelve los nombres de los departamentos a los que pertenece
	 * el empleado.
	 * 
	 * @param idEmpl
	 *            identificador del empleado
	 * @return los departamentos a los que pertenece el empleado
	 */
	private ArrayList<String> getIdsDepartamentos(int idEmpl) {
		ArrayList<String> depts = new ArrayList<String>();
		ResultSet rs = _db.obtenIdsDepartamentos(idEmpl);
		try {
			while (rs.next()) {
				String idDept = rs.getString(1);
				depts.add(idDept);
			}
		} catch (Exception e) {
			System.err
					.println("Error al obtener Lista de Departamentos en la base de datos");
		}
		return depts;
	}

	// Probarlo e intentar optimizarlo cuando metamos estadisticas
	// De momento pongo una posible implementacion
	/**
	 * Carga los nombres de los departamentos de un empleado, buscando
	 * recursivamente. <br>
	 * Por ejemplo, para un gerente, devuelve los nombres de sus departamentos,
	 * y recursivamente los de los de sus subordinados. Si el parámetro es
	 * <b>null</b>, se devuelven todos los departamentos que hay en la base de
	 * datos. Si alguien se pregunta para qué leches sirve esto, sirve para las
	 * estadísticas.
	 * 
	 * @param idEmpl
	 *            el empleado del que coger recursivamente los empleados,
	 *            <b>null</b> si se quieren coger todos los departamentos
	 * @return la lista de los nombres de los departamentos
	 */
	public ArrayList<String> getIdsDepartamentosRec(Integer idEmpl) {
		ArrayList<String> depts = new ArrayList<String>();
		if (idEmpl == null) {
			ResultSet rs = _db.obtenTodosDepartamentos();
			try {
				while (rs.next()) {
					depts.add(rs.getString("Nombre"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.err
						.println("Error al obtener todos los Departamentos en la base de datos");
			}
		} else {
			ArrayList<String> aux = this.getIdsDepartamentos(idEmpl);
			for (int i = 0; i < aux.size(); i++)
				depts.add(aux.get(i));
			ArrayList<Integer> aux2 = this.getIdsSubordinados(idEmpl);
			for (int j = 0; j < aux2.size(); j++) {
				aux.clear();
				aux = this.getIdsDepartamentosRec(aux2.get(j));
				for (int k = 0; k < aux.size(); k++)
					depts.add(aux.get(k));
			}
		}

		return depts;
	}

	/**
	 * Metodo que obtiene los subordinados del empleado si los tuviera
	 * 
	 * @param idEmpl
	 *            identificador del empleado
	 * @return los subordinados del empleado en cuestion
	 */
	private ArrayList<Integer> getIdsSubordinados(int idEmpl) {
		ArrayList<Integer> subs = new ArrayList<Integer>();
		ResultSet rs = _db.obtenIdsSubordinados(idEmpl);
		try {
			while (rs.next()) {
				int idSub = rs.getInt(1);
				subs.add(idSub);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener Lista de Subordinados en la base de datos");
		}
		return subs;
	}

	/**
	 * Metodo que obtiene el superior de un empleado dado(si es 0 no tiene
	 * superior)
	 * 
	 * @param idEmpl
	 *            el identificador del empleado o número de vendedor
	 * @return el identificador del superior del empleado
	 */
	private int getIdSuperior(int idEmpl) {
		int idSup = 0;
		ResultSet rs = _db.obtenSuperior(idEmpl);
		try {
			if (rs.next()) {
				rs.first();
				idSup = rs.getInt("JefeDepartamento");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener el superior de la base de datos");
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
	public ArrayList<Empleado> getEmpleados(Integer idEmpl, String idDpto,
			Integer idContrato, String nombre, String apellido1,
			String apellido2, Integer rango) {
		ArrayList<Empleado> listaCoincidencias = new ArrayList<Empleado>();
		// TODO BD RELLENAR LISTACOINCIDENCIAS Empleado e1 = new Empleado(1, "M.
		// Jackson", new Color (shell.getDisplay(), 104, 228, 85));
		ResultSet rs = _db.obtenEmpleadoAlaCarta(idEmpl, idDpto, idContrato,
				nombre, apellido1, apellido2, rango);
		try {
			while (rs.next()) {
				int id = rs.getInt(1);
				listaCoincidencias.add(this.getEmpleado(id));
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener el empleado de la base de datos");
		}
		/*
		 * //TODO ELIMINAR HASTA FIN-ELIMINAR, HECHA PARA PRUEBAS Color color;
		 * color = new Color(null, 1,1,1); Empleado e2 = new Empleado(2, "J.
		 * Mayer", color); Empleado e3 = new Empleado(3, "B. Jovi", color);
		 * Empleado e4 = new Empleado(4, "H. Day", color); Empleado e5 = new
		 * Empleado(5, "N. Furtado", color); Empleado e6 = new Empleado(6, "L.
		 * Kravitz", color); listaCoincidencias.add(e2);
		 * listaCoincidencias.add(e3); listaCoincidencias.add(e4);
		 * listaCoincidencias.add(e5); listaCoincidencias.add(e6); //TODO
		 * FIN-ELIMINAR
		 */

		return listaCoincidencias;
	}

	/**
	 * Inserta un empleado en la base de datos.
	 * 
	 * @param empleado
	 *            el empleado a insertar
	 * @return <i>true</i> si el empleado ha sido insertado
	 */
	public boolean insertEmpleado(Empleado empleado) {
		int sexo = empleado.getSexo();
		int grupo = empleado.getGrupo();
		return _db.insertarUsuario(empleado.getEmplId(), empleado.getNombre(),
				empleado.getApellido1(), empleado.getApellido2(), empleado
						.getFechaNac(), sexo, empleado.getEmail(), empleado
						.getPassword(), grupo, Date.valueOf("0000-00-00"), Date
						.valueOf("0000-00-00"), 0, 0, empleado.getIdioma(), 1,
				empleado.getContratoId(), empleado.getTurnoFavorito());
	}

	// este metodo lo usa el programa que rellena automaticamente las tablas de
	// la base de datos
	/**
	 * Método que inserta en la base de datos los valores correspondientes a un
	 * nuevo usuario (este metodo lo usa el programa que rellena automaticamente
	 * las tablas de la base de datos)
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
	public boolean insertUsuario(int id, String nombre, String apellido1,
			String apellido2, Date fechaNac, int sexo, String email,
			String password, int indicadorGrupo, Date fechaContrato,
			Date fechaEntrada, int horasExtras, int felicidad, int idioma,
			int rango, int idContrato, int idTurno) {
		return _db.insertarUsuario(id, nombre, apellido1, apellido2, fechaNac,
				sexo, email, password, indicadorGrupo, fechaContrato,
				fechaEntrada, horasExtras, felicidad, idioma, rango,
				idContrato, idTurno);
	}

	/**
	 * Método que devuelve los nombres de los departamentos de un jefe
	 * 
	 * @param idJefe
	 *            identificador del jefe
	 * @return los departamentos que dirige el jefe
	 */
	public ArrayList<String> getDepartamentosJefe(int idJefe) {
		ArrayList<String> depts = new ArrayList<String>();
		ResultSet rs = _db.obtenDepartamentosJefe(idJefe);
		try {
			while (rs.next()) {
				String idDept = rs.getString(1);
				depts.add(idDept);
			}
		} catch (Exception e) {
			System.err
					.println("Error al obtener Lista de Departamentos en la base de datos");
		}
		return depts;
	}
	
	/***************************************************************************
	 * Métodos relacionados con departamentos
	 */

	/**
	 * Carga un departamento desde la base de datos, dado su nombre.
	 * 
	 * @param id
	 *            el nombre del departamento
	 * @return una instancia del departamento cargado
	 */
	public Departamento getDepartamento(String id) {
		try {
			ResultSet r = _db.obtenDepartamento(id);
			r.last();
			if (r.getRow() > 0) {
				r.first();
				Empleado e = getEmpleado(r.getInt("JefeDepartamento"));
				Departamento d = new Departamento(id, 0, e);
				return d;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @return devuelve un arraylist con los departamentos de la base de datos
	 *         con sus características basicas
	 */
	public ArrayList<Departamento> getTodosDepartamentos() {
		ArrayList<Departamento> departamentos = new ArrayList<Departamento>();
		ResultSet rs = _db.obtenTodosDepartamentos();
		try {
			while (rs.next()) {
				String id = rs.getString("Nombre");
				departamentos.add(this.getDepartamento(id));
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener todos los Departamentos en la base de datos");
		}

		return departamentos;

	}
	
	/**
	 * 
	 * @return devuelve un arraylist con los nombres de los departamentos de la base de datos
	 *      
	 */
	public ArrayList<String> getNombreTodosDepartamentos() {
		ArrayList<String> departamentos = new ArrayList<String>();
		ResultSet rs = _db.obtenTodosDepartamentos();
		try {
			while (rs.next()) {
				String id = rs.getString("Nombre");
				departamentos.add(id);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener todos los Departamentos en la base de datos");
		}

		return departamentos;

	}
	
	
	

	/**
	 * Guarda un departamento en la base de datos
	 * 
	 * @param departamento
	 *            el departamento a guardar
	 * @return <i>true</i> si el departamento ha sido insertado
	 */
	public boolean insertDepartamento(Departamento departamento) {
		return _db.insertarDepartamento(departamento.getNombreDepartamento(),
				departamento.getJefeDepartamento().getEmplId())
				&& _db.insertarDepartamentoUsuario(departamento
						.getJefeDepartamento().getEmplId(), departamento
						.getNombreDepartamento());
	}

	// este metodo lo usa el programa que rellena automaticamente las tablas de
	// la base de datos
	/**
	 * Metodo que inserta en la base de datos los valores correspondientes a un
	 * nuevo departamento(este metodo lo usa el programa que rellena
	 * automaticamente las tablas de la base de datos)
	 * 
	 * @param nombre
	 *            Nombre representativo de las actividades llevadas a cabo
	 *            dentro del departamento
	 * @param jefe
	 *            Persona que dirige le departamento
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertDepartamentoPruebas(String nombre, int jefe) {
		return _db.insertarDepartamento(nombre, jefe);
	}
	
	
	/**
	 * Metodo que inserta en la base de datos los valores correspondientes a un
	 * nuevo departamento(este metodo lo usa el programa que rellena
	 * automaticamente las tablas de la base de datos)
	 * 
	 * @param nombre
	 *            Nombre representativo de las actividades llevadas a cabo
	 *            dentro del departamento
	 * @param jefe
	 *            Persona que dirige le departamento
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	
	public void modificaDpto(String Nombre, int jefe) {
		 _db.modificaDepartamento(Nombre, jefe);
	}
	public void cambiaNombreDpto(String NombreAntiguo, String NombreNuevo) {
		 _db.cambiaNombreDepartamento(NombreAntiguo, NombreNuevo);
	}
	/**
	 * Metodo que a partir de un identificador de departamento y un dia de la
	 * semana (entero) nos devuelve una lista dividida en horas con sus
	 * correspondientes limites de numero de empleados maximo y minimo.
	 * 
	 * @param idDepartamento
	 * @param Fecha
	 * @return devuelve un arraylist donde cada elemento es un vector de tres
	 *         dimensiones de tal forma que vector[0]= Hora vector[1]= numero
	 *         minimo de empleados para esa hora vector[2]= numero maximo de
	 *         empleados para esa hora
	 */
	public ArrayList<Object[]> getDistribucionDia(String nombre, Date Fecha) {
		ArrayList<Object[]> lista = new ArrayList<Object[]>();
		ResultSet r;

		// se ha supuesto que fecha esta en formato string
		try {
			r = _db.obtenFestivos(nombre, Fecha);
			r.last();
			if (r.getRow() > 0) {
				r.beforeFirst();
				while (r.next()) {
					Object[] vector = new Object[4];
					vector[0] = (Integer) r.getInt("Hora");
					vector[1] = (Integer) r.getInt("NumMin");
					vector[2] = (Integer) r.getInt("NumMax");
					vector[3] = (String) r.getString("Patron");
					lista.add(vector);
				}
			} else {
				Date d = Fecha;
				int diaSemana = d.getDay();

				r = _db.obtenDistribucion(nombre, diaSemana);
				if (r.next()) {
					r.last();
					if (r.getRow() > 0) {
						r.beforeFirst();
						while (r.next()) {
							Object[] vector = new Object[4];
							vector[0] = (Integer) r.getInt("Hora");
							vector[1] = (Integer) r.getInt("NumMin");
							vector[2] = (Integer) r.getInt("NumMax");
							vector[3] = (String) r.getString("Patron");
							lista.add(vector);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err
					.println("Error al realizar la consulta de los festivos ");
		}
		return lista;
	}

	/**
	 * 
	 * @param nombre
	 * @param cal
	 */
	public void getDistribucionMes(String nombre, Calendario cal) {
		int i = 0, j = 0;
		for (i = 0; i < cal.getNumDias(); i++) {
			Date dia = Date.valueOf(cal.getAnio() + "-" + cal.getMes() + "-"
					+ (i + 1));
			ArrayList<Object[]> temp = getDistribucionDia(nombre, dia);
			j = 0;

			while (j < 24) {
				cal.actualizaHora(i, j, -1, -1, -1, -1);
				j++;
			}

			j = 0;
			if (temp.size() > 0) {
				for (j = 0; j < temp.size(); j++) {
					Object[] t = new Object[4];
					t = temp.get(j);
					cal.actualizaHora(i, (Integer) t[0], (Integer) t[2],
							(Integer) t[1], Util.numExpertos((String) t[3]),
							Util.numPrincipiantes((String) t[3]));
				}
			}
		}

	}

	/**
	 * Método para listar todos los empleados de un departamento
	 * 
	 * @param idDept
	 *            identificador del departamento
	 * @return lista de los empleados de un departamento
	 */
	public ArrayList<Empleado> getEmpleadosDepartamento(String idDept) {
		ArrayList<Empleado> emps = new ArrayList<Empleado>();
		ResultSet rs = _db.obtenEmpleadosDepartamento(idDept);
		try {
			while (rs.next()) {
				int id = rs.getInt("NumVendedor");
				emps.add(this.getEmpleado(id));
			}
		} catch (Exception e) {
			System.err
					.println("Error al obtener Lista de Departamentos en la base de datos");
		}
		return emps;
	}

	/**
	 * Método para obtener los contratos de un departamento
	 * 
	 * @param dpto
	 *            nombre del departamento
	 * @return ArrayList de los contratos que existen en en ese departamento
	 */
	public ArrayList<Contrato> getListaContratosDpto(String idDepartamento) {
		ArrayList<Contrato> contratos = new ArrayList<Contrato>();
	/*	ResultSet rs = null;
		try {
			rs = _db.obtenContratosDepartamento(idDepartamento);
			
			while (rs.next()) {
				contratos.add(new Contrato(rs.getString("Nombre"), rs.getInt("IdContrato"), rs.getInt("TurnoInicial"), rs.getInt("DuracionCiclo"), rs.getString("Patron"), rs.getDouble("Salario"), rs.getInt("Tipo")));				
			}
			return contratos;		
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Error en getListaContratosDpto");
			return null;
		}
	}*/
		
		
		ArrayList arrayIdContratos = new ArrayList();
		Contrato contrato;
		try {
			ArrayList<Empleado> e = new ArrayList<Empleado>();
			e = getEmpleadosDepartamento(idDepartamento);
			// obtener Turnos a partir de los IdTurno's

			for (int i = 0; i < e.size(); i++) {
				int idContrato = e.get(i).getContratoId();
				//contratos.add(this.getContrato(idContrato));
				contrato = this.getContrato(idContrato);
				if(!arrayIdContratos.contains(contrato.getNumeroContrato())){
					contratos.add(contrato);		
					arrayIdContratos.add(idContrato);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener Lista de Contratos del Departamento dado en la base de datos");
		}
		return contratos;
	}

	/**
	 * Metodo que asocia empleados a un departamento y los inserta en la base de
	 * datos
	 * 
	 * @param nvend
	 *            Es el identificador único de cada empleado
	 * @param nombre
	 *            Nombre del departamento al que pertenece el empleado
	 * @return Informa sobre si se ha podido realizar la inserci�n o no
	 */
	public boolean insertDepartamentoUsuario(int nvend, String nombre) {
		return _db.insertarDepartamentoUsuario(nvend, nombre);
	}

	/***************************************************************************
	 * Métodos relacionados con mensajes
	 */

	/**
	 * Obtiene una lista de <i>b</i> mensajes entrantes por orden cronológico,
	 * del más nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * 
	 * @param idEmpl
	 *            el empleado destinatario de los mensajes
	 * @param a
	 *            mensaje por el que empezar, siendo 1 el más reciente
	 * @param b
	 *            cuántos mensajes coger
	 * @return la lista de mensajes
	 */
	public ArrayList<Mensaje> getMensajesSalientes(int idEmpl, int a, int b) {
		ArrayList<Mensaje> temp = new ArrayList<Mensaje>();

		try {
			ResultSet r = _db.obtenMensajesSalientes(idEmpl, a, b);
			r.last();
			if (r.getRow() > 0) {
				r.beforeFirst();

				while (r.next()) {
					Mensaje m = new Mensaje(r.getInt("IdMensaje"), r.getInt("Remitente"), idEmpl,
							r.getDate("Fecha"), r.getString("Asunto"),
							r.getString("Texto"), r.getBoolean("Marcado"));
					temp.add(m);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Obtiene una lista de <i>b</i> mensajes salientes por orden cronológico,
	 * del más nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * 
	 * @param idEmpl
	 *            el empleado remitente de los mensajes
	 * @param a
	 *            mensaje por el que empezar, siendo 1 el más reciente
	 * @param b
	 *            cuántos mensajes coger
	 * @return la lista de mensajes
	 */
	public ArrayList<Mensaje> getMensajesEntrantes(int idEmpl, int a, int b) {
		ArrayList<Mensaje> misMensajes = new ArrayList<Mensaje>();
		try {
			
			ResultSet mensajes = _db.obtenMensajesEntrantes(idEmpl, a, b);
						
			if (mensajes!=null) {
				while (mensajes.next()) {
					Mensaje m = new Mensaje(
							mensajes.getInt("IdMensaje"),
							mensajes.getInt("Remitente"), idEmpl,
							mensajes.getDate("Fecha"),
							mensajes.getString("Asunto"),
							mensajes.getString("Texto"),
							mensajes.getBoolean("Marcado"));				
						misMensajes.add(m);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return misMensajes;
	}

	/**
	 * Inserta un mensaje en la base de datos.
	 * 
	 * @param mensaje
	 *            mensaje a insertar en la base de datos
	 * @return el id del nuevo mensaje si el mensaje se ha insertado
	 *         correctamente o -1 en caso contrario
	 */
	public int insertMensaje (Mensaje mensaje) {		
		int idMensaje= _db.insertarMensaje(mensaje.getRemitente(),mensaje.getFecha(),mensaje.getAsunto(),mensaje.getTexto(),false);
		if (idMensaje!=-1)
			_db.insertarListaDestinatarios(mensaje.getDestinatario(), idMensaje);			
		return idMensaje;
	}


	/**
	 * Elimina un mensaje en la base de datos.
	 * 
	 * @param mensaje
	 *            a borrar en la base de datos
	 * @return <i>true</i> si el mensaje se ha eliminado correctamente
	 */
	public boolean eliminaMensaje(Mensaje mensaje) {
		return _db.borraMensaje(mensaje.getIdmensaje());

	}

	/**
	 * Marca un mensaje en la base de datos.
	 * 
	 * @param mensaje
	 * @return <i>true</i> si el mensaje se ha marcado correctamente
	 */
	public boolean marcarMensaje(Mensaje mensaje) {
		return _db.marcaMensaje(true, mensaje.getIdmensaje());
	}

	/***************************************************************************
	 * Métodos relacionados con contratos
	 */

	/**
	 * Carga un contrato desde la base de datos, dado su identificador.
	 * 
	 * @param id
	 *            el identificador del contrato
	 * @return una instancia del contrato cargado
	 */
	public Contrato getContrato(int id) {
		// _db.abrirConexion();
		ResultSet result = _db.obtenContrato(id);
		int numeroContrato;
		int turnoInicial;
		String nombreContrato;
		String patron;
		int duracionCiclo;
		double salario;
		int tipoContrato;
		Contrato contrato = null;
		try {
			if (result.next()) {
				result.first();
				numeroContrato = result.getInt("IdContrato");
				turnoInicial = result.getInt("TurnoInicial");
				nombreContrato = result.getString("Nombre");
				patron = result.getString("Patron");
				duracionCiclo = result.getInt("DuracionCiclo");
				salario = result.getDouble("Salario");
				tipoContrato = result.getInt("Tipo");
				contrato = new Contrato(nombreContrato, numeroContrato,
						turnoInicial, duracionCiclo, patron, salario,
						tipoContrato);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// _db.cerrarConexion();
		return contrato;
	}

	/**
	 * Inserta un contrato en la base de datos.
	 * 
	 * @param c
	 *            el contrato a insertar
	 * @return el id del nuevo contrato si se ha insertado el contrato
	 *         correctamente o -1 en caso contrario
	 */
	public int insertContrato(Contrato c) {
		// int idContrato=c.getNumeroContrato();
		int turnoInicial = c.getTurnoInicial();
		String nombre = c.getNombreContrato();
		String patron = c.getPatron();
		int duracionCiclo = c.getDuracionCiclo();
		double salario = c.getSalario();
		int tipocontrato = c.getTipoContrato();
		int idContrato = _db.insertarContrato(turnoInicial, nombre, patron,
				duracionCiclo, salario, tipocontrato);
		_db.insertarTurnoPorContrato(turnoInicial, idContrato);
		return idContrato;
	}

	/**
	 * 
	 * @return devuelve un ArrayList con todos los identificadores de los
	 *         contratos existentes
	 */
	public ArrayList<Integer> getIdsContratos() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = _db.obtenTodosContratos();
		try {
			while (rs.next()) {
				int idContrato = rs.getInt("IdContrato");
				ids.add(idContrato);
			}
		} catch (Exception e) {
			System.err
					.println("Error al obtener los ids de los contratos en la base de datos");
		}
		return ids;
	}

	/**
	 * Modifica un contrato en la BD a partir de un objeto contrato.
	 * Deberia llamarse cada vez que se cambien cosas en un objeto de tipo contrato
	 * @param c
	 * @return
	 */
	public boolean setContrato(Contrato c) {		
		try {
			boolean b = _db.cambiarContrato(c.getNumeroContrato(), c.getTurnoInicial(), c.getNombreContrato(), c.getPatron(), c.getDuracionCiclo(), c.getSalario(), c.getTipoContrato());
		} catch (Exception e) {
			System.out.println("Error moificando Contrato");
		}
		return true;
	}
	
	/**
	 * 
	 * @param id	Identificador del contrato a eliminar 
	 * @return		si se ha eliminado el contrato correctamente o no
	 */
	public boolean eliminaContrato(int id){
		return _db.borraContrato(id);
	}
	
	/***************************************************************************
	 * Métodos relacionados con turnos
	 */

	/**
	 * Inserta un contrato en la base de datos.
	 * 
	 * @param t
	 *            el turno a insertar
	 * @return el id del turno si se ha insertado el turno correctamente o -1 en
	 *         caso contrario
	 */
	public int insertTurno(Turno t) {
		int idTurno = _db.insertarTurno(t.getDescripcion(), t.getHoraEntrada(),
				t.getHoraSalida(), t.getHoraDescanso(), t.getTDescanso());
		return idTurno;
	}

	/**
	 * Elimina un turno en la base de datos.
	 * 
	 * @param t
	 *            el turno a eliminar
	 * @return <i>true</i> si se ha eliminado el contrato correctamente
	 */
	public boolean eliminaTurno(Turno t) {
		boolean exito = _db.borraTurno(t.getIdTurno());
		return exito;
	}
	
	/**
	 * Elimina un turno en la base de datos.
	 * 
	 * @param id   el identificador del turno a eliminar
	 * @return <i>true</i> si se ha eliminado el contrato correctamente
	 */
	public boolean eliminaTurno(int id) {
		boolean exito = _db.borraTurno(id);
		return exito;
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
	public boolean modificarTurno(int IdTurno, String Descripcion,
			Time HoraEntrada, Time HoraSalida, Time HoraInicioDescanso,
			int Duracion) {
		return _db.cambiarTurno(IdTurno, Descripcion, HoraEntrada, HoraSalida, HoraInicioDescanso, Duracion);
	}
	/**
	 * 
	 * Método que devuelve TODOS los turnos de la base de datos los cuales son los
	 * que pueden tener los empleados
	 * 
	 * @return Devuelve lista de los turnos de TODOS los empleados en un ArrayList
	 */
	public ArrayList<Turno> getListaTurnosEmpleados() {
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		try {
			ResultSet rs = _db.obtenTodosTurnos();
			while (rs.next()) {
				int idTurn = rs.getInt("IdTurno");
				String descr = rs.getString("Descripcion");
				Time HoraE = rs.getTime("HoraEntrada");
				Time HoraS = rs.getTime("HoraSalida");
				Time HoraI = rs.getTime("HoraInicioDescanso");
				Time duracion = rs.getTime("DuracionDescanso");
				int descanso = aplicacion.Util.dameMinutos(duracion);
				Turno t = new Turno(idTurn, descr, HoraE, HoraS, HoraI,
						descanso);
				turnos.add(t);
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener Lista de Turnos en la base de datos");
		}
		return turnos;
	}

	/**
	 * 
	 * @param idContrato
	 *            identificador de contrato
	 * @return ArrayList de turnos pertenecientes al contrato dado
	 */
	public ArrayList<Turno> getTurnosDeUnContrato(int idContrato) {
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		try {
			ResultSet rs = _db.obtenTurnosDeUnContrato(idContrato);
			while (rs.next()) {
				int idTurn = rs.getInt("IdTurno");
				ResultSet rs2 = _db.obtenTurno(idTurn);
				while (rs2.next()) {
					String descr = rs2.getString("Descripcion");
					Time HoraE = rs2.getTime("HoraEntrada");
					Time HoraS = rs2.getTime("HoraSalida");
					Time HoraI = rs2.getTime("HoraInicioDescanso");
					Time duracion = rs2.getTime("DuracionDescanso");
					int descanso = aplicacion.Util.dameMinutos(duracion);
					Turno t = new Turno(idTurn, descr, HoraE, HoraS, HoraI,
							descanso);
					turnos.add(t);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener Lista de Turnos en la base de datos");
		}
		return turnos;
	}

	/**
	 * 
	 * @param idEmpl
	 *            identificador del empleado
	 * @return devuelve todos los turnos que tiene un empleado según su contrato
	 */
	public ArrayList<Turno> getListaTurnosContrato(int idEmpl) {
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		try {
			ResultSet rs = _db.obtenListaTurnosContrato(idEmpl);
			while (rs.next()) {
				int idTurn = rs.getInt("IdTurno");
				ResultSet rs2 = _db.obtenTurno(idTurn);
				while (rs2.next()) {
					String descr = rs2.getString("Descripcion");
					Time HoraE = rs2.getTime("HoraEntrada");
					Time HoraS = rs2.getTime("HoraSalida");
					Time HoraI = rs2.getTime("HoraInicioDescanso");
					Time duracion = rs2.getTime("DuracionDescanso");
					int descanso = aplicacion.Util.dameMinutos(duracion);
					Turno t = new Turno(idTurn, descr, HoraE, HoraS, HoraI,
							descanso);
					turnos.add(t);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener Lista de Turnos en la base de datos");
		}
		return turnos;
	}

	/**
	 * Método para obtener los contratos de un departamento
	 * 
	 * @param dpto
	 *            nombre del departamento
	 * @return ArrayList de los contratos que existen en en ese departamento
	 */
	public ArrayList<Turno> getListaTurnosEmpleadosDpto(String idDepartamento) {
	/*	ArrayList<Turno> turnos = new ArrayList<Turno>();
		ResultSet rs = null;
		try {
			rs = _db.obtenTurnosDepartamento(idDepartamento);
			
			while (rs.next()) {
				turnos.add(new Turno(rs.getInt("IdTurno"), rs.getString("Descripcion"), rs.getTime("HoraEntrada"), rs.getTime("HoraSalida"), rs.getTime("HoraInicioDescanso"), rs.getInt("DuracionDescanso")));				
			}
			return turnos;		
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Error en getListaContratosDpto");
			return null;
		}
	}*/

		ArrayList<Turno> turnos = new ArrayList<Turno>();
		ArrayList<Turno> turnosAux = new ArrayList<Turno>();
		ArrayList<String> nombres = new ArrayList<String>();
		Turno tur;
		try {
			ArrayList<Empleado> e = new ArrayList<Empleado>();
			e = getEmpleadosDepartamento(idDepartamento);
			

			for (int i = 0; i < e.size(); i++) {
				int nvend = e.get(i).getEmplId();
				//turnos=this.getListaTurnosContrato(nvend);
				turnosAux=this.getListaTurnosContrato(nvend);
				for(int j=0;j<turnosAux.size();j++){
					tur = turnosAux.get(j); 
					if(!nombres.contains(tur.getDescripcion())){
						turnos.add(tur);
						nombres.add(tur.getDescripcion());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err
					.println("Error al obtener Lista de Turnos del Departamento dado en la base de datos");
		}
		return turnos;
	}

	/**
	 * Metodo que recoge el turno que le corresponde a un empleado en un dia
	 * concreto
	 * 
	 * @param dia
	 *            dia en el cual se quiere saber el turno del empleado
	 * @param idEmpleado
	 *            identificador del empleado
	 * @return el turno del empleado en el dia concreto o 0 si no tiene turno
	 *         ese dia
	 */
	public int getTurnoEmpleadoDia(Date dia, int idEmpleado) {
		ResultSet rs = _db.obtenTurnoEmpleadoDia(dia, idEmpleado);
		int turno = 0;
		try {
			if (rs.next()) {
				rs.first();
				turno = rs.getInt("IdTurno");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err
					.println("Error al obtener el turno de un día en la base de datos");
			e.printStackTrace();
		}
		return turno;
	}

	/**
	 * Metodo que recoge el Objeto turno que le corresponde a un empleado en un
	 * dia concreto
	 * 
	 * @param dia
	 *            dia en el cual se quiere saber el turno del empleado
	 * @param idEmpleado
	 *            identificador del empleado
	 * @return el Objeto turno del empleado en el dia concreto o 0 si no tiene
	 *         turno ese dia
	 */
	public Turno getObjetoTurnoEmpleadoDia(Date dia, int idEmpleado) {
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		int turno = getTurnoEmpleadoDia(dia, idEmpleado);
		ResultSet rs = _db.obtenTurno(turno);
		try {
			if (rs.next()) {
				int idTurno = rs.getInt("IdTurno");
				String descr = rs.getString("Descripcion");
				Time hEntrada = rs.getTime("HoraEntrada");
				Time hSalida = rs.getTime("HoraSalida");
				Time hInicioDescanso = rs.getTime("HoraInicioDescanso");
				Time duracion = rs.getTime("DuracionDescanso");
				int descanso = aplicacion.Util.dameMinutos(duracion);
				Turno t = new Turno(idTurno, descr, hEntrada, hSalida,
						hInicioDescanso, descanso);
				turnos.add(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err
					.println("Error al obtener el turno de un día en la base de datos");
			e.printStackTrace();
		}
		if (turnos.size() != 0)
			return turnos.get(0);
		else {
			System.out.print("El usuario " + idEmpleado
					+ " no tiene turnos asignados");
			return null;
		}
	}

	/**
	 * Método que asocia un turno con un contrato y lo inserta en la base de
	 * datos
	 * 
	 * @param idTurno
	 *            identificador del turno correpondiente al contrato
	 * @param idContrato
	 *            identificador del contrato
	 * @return true si se ha realizado correctamente o false en caso contrario
	 */
	public boolean insertTurnoPorContrato(int idTurno, int idContrato) {
		return _db.insertarTurnoPorContrato(idTurno, idContrato);
	}

	/**
	 * 
	 * @return devuelve un ArrayList con todos los identificadores de los turnos
	 *         existentes
	 */
	public ArrayList<Integer> getIdsTurnos() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = _db.obtenTodosTurnos();
		try {
			while (rs.next()) {
				int idTurno = rs.getInt("IdTurno");
				ids.add(idTurno);
			}
		} catch (Exception e) {
			System.err
					.println("Error al obtener los ids de los turnos en la base de datos");
		}
		return ids;
	}

	/***************************************************************************
	 * Metodos relacionados con Cuadrante
	 */
	/**
	 * Guarda un cuadrante en la base de datos
	 * 
	 * @param cuadrante
	 *            cuadrante que se quiere guardar
	 */
	public void insertCuadrante(Cuadrante cuadrante) {
		for (int dia = 0; dia < cuadrante.getNumDias(); dia++) {
			ArrayList<Trabaja> cuad = cuadrante.getListaTrabajaDia(dia);
			for (int i = 0; i < cuad.size(); i++) {
				Trabaja trabaja = cuad.get(i);
				System.out.println("*************************");
				System.out.println("Vuelta " + dia + ", " + i);
				String an = "" + cuadrante.getAnio();
				if (an.length()==1)
				{
					an = "0" + an;
				}
				System.out.println(" Año: " + an);
				String me = "" + cuadrante.getMes();
				if (me.length()==1)
				{
					me = "0" + me;
				}				
				System.out.println(" Mes: " + me);
				
				int d = dia + 1;
				String di = "" + d;
				if (di.length()==1)
				{
					di = "0" + di;
				}
				System.out.println(" Dia: " + di);
			
				String fecha = an + "-" + me + "-" + di;
				System.out.println(" Fecha: " + fecha);
				System.out.println("*************************");				
				
				//String fecha = cuadrante.getAnio() + "-" + cuadrante.getMes()
				//		+ "-" + (dia + 1);
				_db.insertarTrabaja(trabaja.getIdEmpl(), trabaja.getIdTurno(),
						fecha, trabaja.getFichIni(), trabaja.getFichFin());
			}
		}
	}
	
	/**
	 * Metodo que lee un cuadrante de la BD para un mes, año y departamento determinados
	 * @param mes el mes del cuadrante
	 * @param anio el año del cuadrante
	 * @param idDepartamento el departamento del cuadrante
	 * @return el Cuadrante
	 */
	public Cuadrante getCuadrante(int mes,int anio,String idDepartamento) {
		ResultSet cuad=null;
		Cuadrante datos = new Cuadrante(mes,anio,idDepartamento);
		try {
			cuad = _db.obtenCuadrante(mes, anio, idDepartamento);
			
			while (cuad.next()) {
				Trabaja nuevo = new Trabaja(cuad.getInt("NumVendedor"), cuad.getTime("HoraEntrada"), cuad.getTime("HoraSalida"), cuad.getInt("IdTurno"));
				datos.setTrabajaDia(cuad.getDate("Fecha").getDate()-1, nuevo);
//				System.out.println("añade " + String.valueOf((cuad.getDate("Fecha").getDate()-1)));
			}
			return datos;		
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Error en getCuadrante");
			return null;
		}
	}

	/***************************************************************************
	 * Otros métodos
	 */

	/**
	 * Informa del progreso actual de la tarea en el interfaz
	 * 
	 * @param mensaje
	 *            el mensaje a mostrar
	 * @param i
	 *            el progreso, de 0 a 100
	 */
	public void setProgreso(String mensaje, int i) {
		this._vista.setProgreso(mensaje, i);
	}

	/**
	 * Devuelve la fecha y hora actuales.
	 * 
	 * @return la fecha de tipo sql.Date
	 */
	public Date getFechaActual() {
		_calendario = new GregorianCalendar();
		return new Date(_calendario.getTime().getTime());
	}

	/**
	 * M�todo que inserta en la base de datos los valores correspondientes a una
	 * nueva distribuci�n
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
	public boolean insertDistribucion(int Hora, int DiaSemana, String Patron,
			int NumMax, int NumMin, String IdDepartamento) {
		return _db.insertarDistribucion(Hora, DiaSemana, Patron, NumMax,
				NumMin, IdDepartamento);
	}

	/**
	 * * Vacia los contenidos de la tabla especificada
	 * 
	 * @param nombre
	 *            El nombre de la tabla en formato String
	 * @return Devuelve un bool. True si ha ido todo bien, false en caso de
	 *         error.
	 */
	public boolean vaciarTabla(String nombre) {
		return _db.vaciarTabla(nombre);
	}

	/**
	 * Vacia los contenidos de todas las tablas
	 * 
	 * @param
	 * @return Devuelve un bool. True si ha ido todo bien, false en caso de
	 *         error.
	 */
	public boolean vaciarTodasTablas() {
		boolean b = true;
		b = b && _db.vaciarTabla("CONTRATO");
		b = b && _db.vaciarTabla("DEPARTAMENTO");
		b = b && _db.vaciarTabla("DepartamentoUsuario");
		b = b && _db.vaciarTabla("DESTINATARIO");
		b = b && _db.vaciarTabla("DISTRIBUCION");
		b = b && _db.vaciarTabla("FESTIVOS");
		b = b && _db.vaciarTabla("INCIDENCIAS");
		b = b && _db.vaciarTabla("ListaTurnosPorContrato");
		b = b && _db.vaciarTabla("MENSAJE");
		b = b && _db.vaciarTabla("NumerosDEPARTAMENTOs");
		b = b && _db.vaciarTabla("PERMISOS");
		b = b && _db.vaciarTabla("TieneIncidencia");
		b = b && _db.vaciarTabla("Trabaja");
		b = b && _db.vaciarTabla("TURNOS");
		b = b && _db.vaciarTabla("USUARIO");
		b = b && _db.vaciarTabla("VENTAS");

		return b;
	}

	public void insertIssue (String text) {
		_db.insertarIssue(text);			
	}

}