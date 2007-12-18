package aplicacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.Time;
import org.eclipse.swt.graphics.Color;
import java.util.GregorianCalendar;

import algoritmo.*;

/**
 * Esta clase  conecta el modelo (la base de datos) con la vista (los interfaces)
 * Resumen de los métodos que hay: (si añadís alguno, por favor, añadidlo aquí también)
 * 
 * - Métodos relacionados con empleados: (P = pendiente)
 * 		getEmpleado(int)			Carga un empleado
 *	P	getEmpleados(...)			Carga uno o varios empleados que coincidan con los 
 * 									parámetros dados
 * 		getIdsDepartamentos(int)	Carga los nombres de sus departamentos
 *	P	getIdsDepartamentosRec(int)	Lo mismo pero recursivamente
 * 		getIdsSubordinados(int)		Carga los subordinados
 * 		getIdSuperior(int)			Carga el identificador del superior
 *		insertEmpleado(Empleado)	Inserta un empleado en la base de datos
 * 
 * - Métodos relacionados con departamentos:
 * 	P	getDepartamento(String)		Carga un departamento
 * 	P	insertDepartamento(Dep)		Inserta un departamento
 * 		getDistribucionDia(int, String)	Carga la distribución de un departamento para
 * 									un día concreto.
 * 									NOTA: El idDepartamento debería ser un string - Dani
 * 		getDistribucionMes
 * 		getEmpleadosDepartamento	listar todos los empleados de un departamento
 * 
 * - Métodos relacionados con mensajes:
 * 	P	getMensajesEntrantes(...)	Carga un número determinado de mensajes entrantes
 *	P	getMensajesSalientes(...)	Carga un número determinado de mensajes salientes
 *	?	getMensajes(int)			Carga todos los mensajes (¿necesario?)
 *		insertMensaje(Mensaje)		Inserta un mensaje
 *		eliminaMensaje				Elimina un mensaje
 *		marcarMensaje							Marca un mensaje en la bd
 * 
 * - Métodos relacionados con contratos
 *	P	getContrato(int)			Carga un contrato dado su id
 *	P	insertContrato(Contrato)	Inserta un contrato
 *
 * - Métodos relacionados con turnos
 * 		getListaTurnosEmpleados()		Carga una lista de turnos
 * 
 * - Métodos relacionados con cuadrantes
 * 		insertCuadrante(Cuadrante)	Guarda un cuadrante en la base de datos 
 * 
 * @author Todos
 */
public class Controlador {
	private Vista _vista;
	private Database _db;
	private Empleado _empleadoActual;
	private GregorianCalendar calendario;

	
	public Controlador(Database baseDatos) {
		this._db = baseDatos;
		// Crea calendario con la hora actual;
		calendario = new GregorianCalendar();
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

	public void setVista(Vista vista) {
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
			int grupo=rs.getInt("IndicadorGrupo");
			int rango=rs.getInt("Rango");
			int idContrato=rs.getInt("IdContrato");
			Date fechaContrato = rs.getDate("FechaContrato");
			Date fechaAlta = rs.getDate("FechaEntrada");
			Color color=null;
			int idSuperior=this.getIdSuperior(idEmpl);
			ArrayList<Integer> idSubordinados=this.getIdsSubordinados(idEmpl);
			ArrayList<String> idDepartamentos=this.getIdsDepartamentos(idEmpl);
			int felicidad=rs.getInt("Felicidad");
			int idioma=rs.getInt("Idioma");
			emp=new Empleado(idSuperior,id,nombre,apellido1,apellido2,fechaNac,sexo,
							 email,password,grupo,rango,idContrato,fechaContrato,
							 fechaAlta,color,idDepartamentos,idSubordinados,felicidad,idioma);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Controlador: Error al obtener el Empleado " + idEmpl + " de la base de datos");
		}

		return emp;
	
	}
		
	/**
	 * Método que devuelve los nombres de los departamentos a los que pertenece el 
	 * empleado.
	 * @param idEmpl	identificador del empleado
	 * @return			los departamentos a los que pertenece el empleado
	 */
	private ArrayList<String> getIdsDepartamentos(int idEmpl) {
		ArrayList<String> depts=new ArrayList<String>();
		ResultSet rs=_db.obtenIdsDepartamentos(idEmpl);
		try {
			while (rs.next()) {
				String idDept = rs.getString(1);
				depts.add(idDept);
			}
		} catch (Exception e) {
			System.out.println("Error al obtener Lista de Departamentos en la base de datos");
		}		
		return depts;
	}

	/**
	 * Carga los nombres de los departamentos de un empleado, buscando recursivamente.
	 * <br>Por ejemplo, para un gerente, devuelve los nombres de sus departamentos,
	 * y recursivamente los de los de sus subordinados. Si el parámetro es <b>null</b>,
	 * se devuelven todos los departamentos que hay en la base de datos.
	 * Si alguien se pregunta para qué leches sirve esto, sirve para las estadísticas.
	 * @param idEmpl	el empleado del que coger recursivamente los empleados, 
	 * 					<b>null</b> si se quieren coger todos los departamentos
	 * @return			la lista de los nombres de los departamentos
	 */
	public ArrayList<String> getIdsDepartamentosRec(Integer idEmpl) {
		ArrayList<String> depts = new ArrayList<String>();
		//TODO
		return depts;
	}

	/**
	 * Metodo que obtiene los subordinados del empleado si los tuviera
	 * @param idEmpl identificador del empleado 
	 * @return		 los subordinados del empleado en cuestion
	 */
	private ArrayList<Integer> getIdsSubordinados(int idEmpl) {
		ArrayList<Integer> subs=new ArrayList<Integer>();
		ResultSet rs=_db.obtenIdsSubordinados(idEmpl);
		try {
			while (rs.next()) {
				int idSub = rs.getInt(1);
				subs.add(idSub);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener Lista de Subordinados en la base de datos");
		}		
		return subs;
	}

	/**
	 * Metodo que obtiene el superior de un empleado dado(si es 0 no tiene superior)
	 * @param idEmpl  el identificador del empleado o número de vendedor
	 * @return		  el identificador del superior del empleado
	 */
	private int getIdSuperior(int idEmpl) {
		int idSup=0;
		ResultSet rs=_db.obtenSuperior(idEmpl);
		try {
			if(rs.next()){
				rs.first();			
				idSup = rs.getInt("JefeDepartamento");
			}
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
	public ArrayList<Empleado> getEmpleados(Integer idEmpl, String idDpto,
			Integer idContrato, String nombre, String apellido1,
			String apellido2, Integer rango) {
		ArrayList<Empleado> listaCoincidencias=new ArrayList<Empleado>();
		//TODO BD RELLENAR LISTACOINCIDENCIAS	Empleado e1 = new Empleado(1, "M. Jackson", new Color (shell.getDisplay(), 104, 228,  85));
		ResultSet rs=_db.obtenEmpleadoAlaCarta(idEmpl, idDpto, idContrato, nombre, apellido1, apellido2, rango); 
		try {
			while (rs.next()) {
				int id = rs.getInt(1);
				listaCoincidencias.add(this.getEmpleado(id));
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener el empleado de la base de datos");
		}		
		/*//TODO ELIMINAR HASTA FIN-ELIMINAR, HECHA PARA PRUEBAS
		Color color;
		color		= new Color(null, 1,1,1);
		Empleado e2 = new Empleado(2, "J. Mayer",   color);
		Empleado e3 = new Empleado(3, "B. Jovi",    color);
		Empleado e4 = new Empleado(4, "H. Day",     color);
		Empleado e5 = new Empleado(5, "N. Furtado", color);
		Empleado e6 = new Empleado(6, "L. Kravitz", color);
		listaCoincidencias.add(e2);
		listaCoincidencias.add(e3);
		listaCoincidencias.add(e4);
		listaCoincidencias.add(e5);
		listaCoincidencias.add(e6);
		//TODO FIN-ELIMINAR */
		
		
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
		int sexo=empleado.getSexo();
		int grupo=empleado.getGrupo();
		return _db.insertarUsuario(empleado.getEmplId(), empleado.getNombre(),
				empleado.getApellido1(), empleado.getApellido2(),
				empleado.getFechaNac(), sexo,
				empleado.getEmail(), empleado.getPassword(), grupo,
				Date.valueOf("0000-00-00"), Date.valueOf("0000-00-00"), 0,0,empleado.getIdioma(),1,0,0);
	}

/******************************************************************************************
 * Métodos relacionados con departamentos
 */

	/**
	 * Carga un departamento desde la base de datos, dado su nombre.
	 * @param id	el nombre del departamento
	 * @return		una instancia del departamento cargado
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
		
	/**
	 * Guarda un departamento en la base de datos
	 * 
	 * @param departamento el departamento a guardar
	 * @return <i>true</i> si el departamento ha sido insertado
	 */
	public boolean insertDepartamento(Departamento departamento) {
		return _db.insertarDepartamento(departamento.getNombreDepartamento(), departamento.getJefeDepartamento().getEmplId());		
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
	public ArrayList<Object[]> getDistribucionDia (String nombre, Date Fecha){
		ArrayList lista= new ArrayList();		
		ResultSet r;
		Object[] vector = new Object[4];
		
		//se ha supuesto que fecha esta en formato string
		try{
			r=_db.obtenFestivos(nombre, Fecha);
			r.last();
			if (r.getRow()>0){
				r.beforeFirst();
				while (r.next()){					
					vector[0]=(Integer)r.getInt("Hora");
					vector[1]=(Integer)r.getInt("NumMin");
					vector[2]=(Integer)r.getInt("NumMax");
					vector[3]=(String)r.getString("Patron");
					lista.add(vector);
				}
			}else{
				
				
				Date d=Fecha;
				int diaSemana=d.getDay();
				
				r=_db.obtenDistribucion(nombre, diaSemana);
				r.last();
				if (r.getRow()>0){
					r.beforeFirst();
					while (r.next()){
						vector[0]=(Integer)r.getInt("Hora");
						vector[1]=(Integer)r.getInt("NumMin");
						vector[2]=(Integer)r.getInt("NumMax");
						vector[3]=(String)r.getString("Patron");
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

	public void getDistribucionMes(String nombre, Calendario cal) {
		int i=0,j=0;
		for (i=0; i<cal.getNumDias(); i++) {
			Date dia = Date.valueOf(cal.getAnio()+"-"+cal.getMes()+"-"+(i+1));
			ArrayList<Object[]> temp = getDistribucionDia(nombre,dia);			
			for (j=0; j<temp.size(); j++) {
				Object[] t = new Object[4];
				t = temp.get(j);
				cal.actualizaHora(i, (Integer)t[0], (Integer)t[2], (Integer)t[1], Util.numExpertos((String)t[3]), Util.numPrincipiantes((String)t[3]));
			}
			
			while (j<24) {
				cal.actualizaHora(i, j, -1, -1, -1, -1);
				j++;
			}
				
		}
		
	}
	
	/**
	 * Método para listar todos los empleados de un departamento
	 * @param idDept	identificador del departamento
	 * @return			lista de los empleados de un departamento
	 */
	public ArrayList<Empleado> getEmpleadosDepartamento(String idDept){
		ArrayList<Empleado> emps=new ArrayList<Empleado>();
		ResultSet rs=_db.obtenEmpleadosDepartamento(idDept);
		try {
			while (rs.next()) {
				int id=rs.getInt("NumVendedor");
				emps.add(this.getEmpleado(id));
			}
		} catch (Exception e) {
			System.out.println("Error al obtener Lista de Departamentos en la base de datos");
		}		
		return emps;
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
	public ArrayList<Mensaje> getMensajesSalientes(int idEmpl, int a, int b){
		ArrayList<Mensaje> temp = new ArrayList<Mensaje>();
		
		try {
			ResultSet r = _db.obtenMensajesSalientes(idEmpl, a, b);
			r.last();
			if (r.getRow()>0){
				r.beforeFirst();
				
				while (r.next()) {
					Mensaje m = new Mensaje(r.getInt("Remitente"), idEmpl, r.getDate("Fecha"), r.getString("Asunto"), r.getString("Texto"));					
					temp.add(m);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Obtiene una lista de <i>b</i> mensajes salientes por orden cronológico, del más
	 * nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * @param idEmpl el empleado remitente de los mensajes
	 * @param a mensaje por el que empezar, siendo 1 el más reciente
	 * @param b cuántos mensajes coger
	 * @return la lista de mensajes
	 */
	public ArrayList<Mensaje> getMensajesEntrantes (int idEmpl, int a, int b){
		ArrayList<Mensaje> misMensajes = new ArrayList<Mensaje>();		
		try {
			int contador=0;
			ResultSet idMensajes= _db.obtenDestinatarios(idEmpl);
			if(idMensajes!=null){
				while(idMensajes.next()){
					int idMensaje=idMensajes.getInt("IdMensaje");
					if(idMensaje!=-1){
						ResultSet mensaje= _db.obtenMensaje(idMensaje);
						while(mensaje.next()){
							contador++;
							Mensaje m = new Mensaje(mensaje.getInt("Remitente"), idEmpl, mensaje.getDate("Fecha"),
								mensaje.getString("Asunto"), mensaje.getString("Texto"));
							if(contador>=0 && contador<(a+b))
								misMensajes.add(m);
						}
					}
						
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return misMensajes;
	}

	/**
	 * Inserta un mensaje en la base de datos.
	 * @param mensaje	mensaje a insertar en la base de datos
	 * @return <i>true</i> si el mensaje se ha insertado correctamente
	 */
	public boolean insertMensaje (Mensaje mensaje) {		
		int idMensaje=_db.obtenIdMensaje();
		boolean correcto=false;
		if (idMensaje!=-1){
			correcto=_db.insertarMensaje(idMensaje+1,mensaje.getRemitente(),mensaje.getFecha(),mensaje.getAsunto(),mensaje.getTexto(),false);
			if(correcto)
				correcto= correcto &&_db.insertarListaDestinatarios(mensaje.getDestinatario(), idMensaje+1);		
		}	
		return correcto;
	}
	
	/**
	 * Elimina un mensaje en la base de datos.
	 * @param mensaje a borrar en la base de datos
	 * @return <i>true</i> si el mensaje se ha eliminado correctamente
	 */
	public boolean eliminaMensaje (Mensaje mensaje) {
		return _db.borraMensaje(mensaje.getIdmensaje());
		
	}
	
	/**
	 * Marca un mensaje en la base de datos.
	 * @param mensaje
	 * @return <i>true</i> si el mensaje se ha marcado correctamente
	 */
	public boolean marcarMensaje (Mensaje mensaje) {
		return _db.marcaMensaje(true, mensaje.getIdmensaje());
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
		//_db.abrirConexion();
		ResultSet result = _db.obtenContrato(id);		
		int numeroContrato;
		int turnoInicial;
		String nombreContrato;
		String patron;
		int duracionCiclo;
		double salario;
		Contrato contrato=null;
		try {
			result.next();
			numeroContrato = result.getInt("IdContrato");
			turnoInicial = result.getInt("TurnoInicial");
			nombreContrato = result.getString("Nombre");
			patron = result.getString("Patron");
			duracionCiclo = result.getInt("DuracionCiclo");
			salario = result.getDouble("Salario");
			contrato=new Contrato(nombreContrato,numeroContrato , turnoInicial, duracionCiclo, patron, salario);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//_db.cerrarConexion();
		return contrato;
	}
	
	
	/**
	 * Inserta un contrato en la base de datos.
	 * @param c		el contrato a insertar
	 * @return		<i>true</i> si se ha insertado el contrato correctamente
	 */	
	public boolean insertContrato(Contrato c) {
		int idContrato=c.getNumeroContrato();
		int turnoInicial=c.getTurnoInicial();
		String nombre=c.getNombreContrato();
		String patron=c.getPatron();
		int duracionCiclo=c.getDuracionCiclo();
		double salario=c.getSalario();
		_db.abrirConexion();
		boolean exito=_db.insertarContrato(idContrato, turnoInicial, nombre, patron, duracionCiclo, salario);
		_db.cerrarConexion();
		return exito;
	}

/******************************************************************************************
 * Métodos relacionados con turnos 
 */
	

	/**
	 * Inserta un contrato en la base de datos.
	 * @param t	el turno a insertar
	 * @return		<i>true</i> si se ha insertado el contrato correctamente
	 */
	public boolean insertTurno(Turno t) {
		_db.abrirConexion();
		boolean exito=_db.insertarTurno(t.getIdTurno(), t.getDescripcion(), t.getHoraEntrada(), t.getHoraSalida(), t.getHoraDescanso(), t.getTDescanso());
		_db.cerrarConexion();
		return exito;
	}
	/**
	 * Elimina un turno en la base de datos.
	 * @param t	el turno a eliminar
	 * @return		<i>true</i> si se ha eliminado el contrato correctamente
	 */
	public boolean eliminaTurno(Turno t) {
		_db.abrirConexion();
		boolean exito=_db.borraTurno(t.getIdTurno());
		_db.cerrarConexion();
		return exito;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return Devuelve lista de los turnos de los empleados en un ArrayList
	 */
	public ArrayList<Turno> getListaTurnosEmpleados() {
        ArrayList<Turno> turnos= new ArrayList<Turno>();
		try {
			ResultSet rs = _db.obtenListaTurnosEmpleados();
			while(rs.next()){
				int idTurn = rs.getInt("IdTurno");
				String descr = rs.getString("Descripcion");
				Time HoraE = rs.getTime("HoraEntrada");
				Time HoraS = rs.getTime("HoraSalida");
				Time HoraI = rs.getTime("HoraInicioDescanso");
				Time duracion = rs.getTime("DuracionDescanso");
				int descanso = aplicacion.Util.dameMinutos(duracion);
				Turno t = new Turno(idTurn,descr,HoraE,HoraS,HoraI,descanso);
				turnos.add(t);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener Lista de Turnos en la base de datos");
		}		
		return  turnos;
	}
	
	public ArrayList<Turno> getListaTurnosContrato(int idEmpl){
		ArrayList<Turno> turnos= new ArrayList<Turno>();
		try {
			ResultSet rs = _db.obtenListaTurnosContrato(idEmpl);
			while(rs.next()){
				int idTurn = rs.getInt("IdTurno");
				ResultSet rs2 = _db.obtenTurno(idTurn);
				while (rs2.next()){
					String descr = rs2.getString("Descripcion");
					Time HoraE = rs2.getTime("HoraEntrada");
					Time HoraS = rs2.getTime("HoraSalida");
					Time HoraI = rs2.getTime("HoraInicioDescanso");
					Time duracion = rs2.getTime("DuracionDescanso");
					int descanso = aplicacion.Util.dameMinutos(duracion);
					Turno t = new Turno(idTurn,descr,HoraE,HoraS,HoraI,descanso);
					turnos.add(t);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al obtener Lista de Turnos en la base de datos");
		}		
		return  turnos;
	}
	
	/**
	 * Metodo que recoge el turno que le corresponde a un empleado en un dia concreto
	 * @param dia			dia en el cual se quiere saber el turno del empleado
	 * @param idEmpleado	identificador del empleado
	 * @return	el turno del empleado en el dia concreto o 0 si no tiene turno ese dia
	 */
	public int getTurnoEmpleadoDia(Date dia, int idEmpleado){
		ResultSet rs=_db.obtenTurnoEmpleadoDia(dia,idEmpleado);
		int turno=0;
		try {
			if(rs.next()) {
				rs.first();			
				turno=rs.getInt("IdTurno");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el turno de un día en la base de datos");
			e.printStackTrace();
		}
		return turno;
	}
	

/******************************************************************************************
 * Metodos relacionados con Cuadrante
 */	
	/**
	 * Guarda un cuadrante en la base de datos
	 * @param cuadrante cuadrante que se quiere guardar
	 */
	public void insertCuadrante(Cuadrante cuadrante){
		_db.abrirConexion();
		for(int dia=0;dia<cuadrante.getNumDias();dia++){
			ArrayList<Trabaja> cuad=cuadrante.getListaTrabajaDia(dia);
			for(int i=0;i<cuad.size();i++){
				Trabaja trabaja=cuad.get(i);
				String fecha=cuadrante.getAnio()+"-"+cuadrante.getMes()+"-"+(dia+1);
				_db.insertarTrabaja(trabaja.getIdEmpl(), trabaja.getIdTurno(), fecha, trabaja.getFichIni(),
						trabaja.getFichFin());
			}
		}
		_db.cerrarConexion();
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
	
	/**
	 * Devuelve la fecha y hora actuales.
	 * @return la fecha de tipo sql.Date
	 */
	public Date getFechaActual(){
		calendario = new GregorianCalendar();
		return new Date(calendario.getTime().getTime());
	}
}