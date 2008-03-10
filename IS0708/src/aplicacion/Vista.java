package aplicacion;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import algoritmo.Cuadrante;
import algoritmo.Trabaja;

import idiomas.LanguageChanger;
import interfaces.*;

public class Vista {
	private Controlador controlador;
	private Database db;
	private Shell shell;
	private Display display;
	private ResourceBundle bundle;
	private Locale locale;

	private I01_Login login;
	private I02_Principal i02;
	private boolean alive = true;
	private LanguageChanger l;
	private Thread conector, loader;
	private boolean cacheCargada = false;
	private int num_men_hoja = 10;
	
	/**
	 * Caché local: Lista de empleados que trabajan en el mismo departamento que
	 * el usuario actual
	 */
	private ArrayList<Empleado> empleados = new ArrayList<Empleado>();

	/** Caché local: Lista de mensajes entrantes del usuario actual */
	private ArrayList<Mensaje> mensajesEntrantes = new ArrayList<Mensaje>();

	/** Caché local: Lista de contratos disponibles para este departamento */
	private ArrayList<Contrato> contratos = new ArrayList<Contrato>();

	/** Caché local: Lista de turnos en los contratos de este departamento */
	private ArrayList<Turno> turnos = new ArrayList<Turno>();

	/** Caché local: Lista de departamentos de un jefe*/
	private ArrayList<Departamento> departamentosJefe = new ArrayList<Departamento>();
	
	/** Caché local: Cuadrantes del departamento actual */
	private ArrayList<Cuadrante> cuadrantes = new ArrayList<Cuadrante>();
	
	/**
	 * Este hilo conecta con la base de datos.
	 * 
	 * @author Daniel Dionne
	 */
	public class Conector implements Runnable {
		public void run() {
			// Conectar con la base de datos
			db.abrirConexion();
			if (!login.isDisposed())
				if (!db.conexionAbierta()) {
					shell.getDisplay().asyncExec(new Runnable() {
						public void run() {
							MessageBox messageBox = new MessageBox(shell,
									SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
							messageBox.setText(bundle.getString("Error"));
							messageBox.setMessage(bundle
									.getString("I01_err_Conexion"));
						}
					});
				} else {
					shell.getDisplay().asyncExec(new Runnable() {
						public void run() {
							// TODO Por alguna razón oculta de los threads, aun
							// habiendo comprobado
							// antes si el dialog sigue presente, si no lo
							// compruebo de nuevo
							// a veces da error.
							if (!login.isDisposed())
								login.setProgreso("Conectado.");
						}
					});
				}
			else {
				// En este caso se ha cerrado la aplicación antes de que termine
				// de conectar.
				if (db.conexionAbierta())
					System.out.println("Vista-Conector cerrando conexion");
					db.cerrarConexion();
			}
		}
	}

	/**
	 * Este hilo carga la caché
	 * 
	 * @author Daniel Dionne
	 */
	public class Loader implements Runnable {
		public synchronized void run() {
			// Si hay un usuario logueado
			if (controlador.getEmpleadoActual() != null){
				loadCache();
				cacheCargada = true;
				// Se queda consultando los mensajes periódicamente
				while (alive) {
					loadMensajes();
					cacheCargada = true;
					try {
						// TODO Espera 20 segundos (¿cómo lo dejamos?)
						wait(20000);
					} catch (Exception e) {}
				}
				db.cerrarConexion();
			}
		}
	}


	/**
	 * Constructor de la vista:
	 * <ul>
	 * <li>Crea el display y el shell
	 * <li>Crea el gestor de idiomas
	 * <li>Conecta con la base de datos
	 * <li>Identifica al usuario
	 * </ul>
	 * 
	 * @param controlador
	 *            el controlador de la aplicación
	 * @param db
	 *            la base de datos de la aplicación
	 * 
	 * @author Daniel Dionne
	 */
	public Vista(Display d, Controlador controlador, Database db) {
		// Inicializar variables
		this.controlador = controlador;
		this.db = db;
		controlador.setVista(this);

		// Creación del display y el shell
		display = d;
		shell = new Shell(display);

		// Creación del gestor de idiomas
		l = new LanguageChanger();
		bundle = l.getBundle();
		locale = l.getCurrentLocale();
	}

	/**
	 * Este método realiza el login.
	 */
	public void start() {
		// Login y conexión a la base de datos
		login = new I01_Login(shell, bundle, db);
		conector = new Thread(new Conector());
		loader = new Thread(new Loader());
		conector.start();
		boolean identificado = false;
		while (!identificado) {
			if (db.conexionAbierta())
				login.mostrarVentana("Conectado.");
			else
				login.mostrarVentana("Conectando...");
			// Espera hasta que se cierre la ventana de login
			while (!login.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
			// Una vez cerrada la ventana de login
			if (login.getBotonPulsado() == 1) {
				// Login de administrador
				if (login.getNumeroVendedor() == 0
						&& login.getPassword().compareTo("admin") == 0) {
					System.out.println("aplicacion.Vista\t::Administrador identificado");
					controlador.setEmpleadoActual(new Empleado(0, 0,
							"Administrador", "", "", null, 0, "", "admin", 0,
							0, 0, null, null, null, null, null, 0, 0, 0));
					identificado = true;
				// Login normal
				} else {
					Empleado emp = getEmpleado(login.getNumeroVendedor());
					if (emp != null) {
						// Comprobar la clave
						if (emp.getPassword().compareTo(login.getPassword()) == 0) {
							controlador.setEmpleadoActual(emp);
							identificado = true;
							// Configurar idioma al del empleado
							l.cambiarLocale(controlador.getEmpleadoActual()
									.getIdioma());
							bundle = l.getBundle();
							locale = l.getCurrentLocale();
							loader.start();
						} else {
							// Si el password no coincide
							if (!login.detectadoLector()) {
								MessageBox messageBox = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
								messageBox.setText(bundle.getString("Error"));
								messageBox.setMessage(bundle.getString("I01_err_Login2"));
								messageBox.open();
							} else {
								display.beep();
							}
							// TODO else mostrar un aviso pero que no haya que cerrarlo
						}
					} else {
						// Si el usuario no existe en la base de datos
						MessageBox messageBox = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
						messageBox.setText(bundle.getString("Error"));
						messageBox.setMessage(bundle
								.getString("I01_err_Login1"));
						messageBox.open();
					}
				}
			} else {
				// Salir de la aplicación
				shell.getDisplay().dispose();
				identificado = true; // Para que salga del bucle
				if (db.conexionAbierta()) {
					System.out.println("Vista-start cerrando conexion. Si ves este mensaje sin venir a cuento, avísanos.");
					db.cerrarConexion();
				}
			}
		}

		// Si todavía no he cerrado el display, ya he hecho login correctamente
		if (!shell.isDisposed()) {
			i02 = new I02_Principal(shell, shell.getDisplay(), bundle, locale, this);
			// Este bucle mantiene la ventana abierta
			while (!shell.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
			stop();
		}
	}

	public void stop() {
		try {
			alive = false;
			loader.interrupt();
			i02.dispose();
			shell.dispose();
			display.dispose();
		}
		catch(Exception e) {
			System.err.println("Error al parar la Vista.");
		};
	}

	/**
	 * Devuelve el display de la aplicación.
	 * 
	 * @return el display de la aplicación
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * Devuelve si la caché ha terminado de cargarse.
	 * 
	 * @return <i>true</i> si la caché ha terminado de cargarse
	 */
	public boolean isCacheCargada() {
		return cacheCargada;
	}

	/**
	 * Muestra el parámetro en la barra de abajo de la ventana principal.
	 * 
	 * @param estado
	 *            el String a mostrar
	 */
	public void setTextoEstado(String estado) {
		i02.setTextoEstado(estado);
	}

	/**
	 * Devuelve el controlador de la aplicación
	 * 
	 * @return el controlador de la aplicación
	 */
	public Controlador getControlador() {
		return controlador;
	}

	/**
	 * Muestra el cursor de reloj de arena
	 */
	public void setCursorEspera() {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
	}

	/**
	 * Muestra el cursor normal
	 */
	public void setCursorFlecha() {
		shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
	}

	/**
	 * Devuelve true si la aplicación se ha iniciado en modo debug
	 * 
	 * @return <i>true</i> si la aplicación se ha iniciado en modo de
	 *         corrección de errores
	 */
	public boolean getModoDebug() {
		return controlador.getModoDebug();
	}

	/**
	 * Muestra un mensaje de debug por consola.
	 * 
	 * @param nombreClase
	 *            el nombre de la clase que emite el mensaje
	 * @param mensaje
	 *            el mensaje a mostrar
	 */
	public void infoDebug(String nombreClase, String mensaje) {
		if (controlador.getModoDebug())
			System.out
					.println("Debug :: [" + nombreClase + "]\n    " + mensaje);
	}

	/***************************************************************************
	 * Métodos relacionados con empleados
	 */

	/**
	 * Devuelve el empleado que ha iniciado la sesión.
	 * 
	 * @return el empleado que ha iniciado la sesión
	 */
	public Empleado getEmpleadoActual() {
		return controlador.getEmpleadoActual();
	}

	/**
	 * Obtiene un empleado, dado su número de vendedor o identificador. Primero
	 * mira en la caché, y si no está, lo coge de la base de datos.
	 * 
	 * @param idEmpl
	 *            el identificador del empleado o número de vendedor
	 * @return una instancia nueva del empleado
	 */
	public Empleado getEmpleado(int idEmpl) {
		// Buscar en cache
		int i = 0;
		while (i<empleados.size()) {
			if (empleados.get(i).getEmplId()==idEmpl) {
				return empleados.get(i);
			}
			i++;
		}
		// Si no, buscar en BD
		return controlador.getEmpleado(idEmpl);
	}
	
	public Turno getTurno (int idTurno) {
		// Buscar en cache
		int i = 0;
		while (i<turnos.size()) {
			if (turnos.get(i).getIdTurno()==idTurno) {
				return turnos.get(i);
			}
			i++;
		}
		// Si no, buscar en BD
		//return controlador.getTurno(idTurno);
		return null;
	}
	
	public Contrato getContrato (int idContrato) {
		// Buscar en cache
		int i = 0;
		while (i<contratos.size()) {
			if (contratos.get(i).getNumeroContrato()==idContrato) {
				return contratos.get(i);
			}
			i++;
		}
		// Si no, buscar en BD
		return controlador.getContrato(idContrato);
	}
	
	public Cuadrante getCuadrante(int mes, int anio, String idDepartamento) {
		if (!alive) return null;
		int i = 0;
		while (i<cuadrantes.size()) {
			if (cuadrantes.get(i).getAnio()==anio && cuadrantes.get(i).getMes()==mes && cuadrantes.get(i).getIdDepartamento().equals(idDepartamento)) {
				return cuadrantes.get(i);
			}
			i++;
		}
		// Si no, buscar en BD
		Cuadrante c = controlador.getCuadrante(mes, anio, idDepartamento);
		cuadrantes.add(c);
		return c;
	}
	
	public ArrayList<Trabaja> getListaTrabajaDia(int dia, int mes, int anio, String idDepartamento) {
		Cuadrante c = getCuadrante(mes, anio, idDepartamento);
		return c.getListaTrabajaDia(dia-1);
	}
	
	/**
	 * @deprecated
	 * @return el numero de turnos de la base de datos
	 */
	public int getTurnoSize(){
		return turnos.size();
	}
	/**
	 * 
	 * @return los turnos cargados de la base de datos
	 */
	public ArrayList <Turno> getTurnos(){
		return turnos;
	}


	/**
	 * Carga los mensajes de la base de datos
	 */
	public void loadMensajes() {
		// Carga mensajes
		infoDebug("Vista", "Cargando mensajes");
		mensajesEntrantes = getMensajesEntrantes(getEmpleadoActual()
				.getEmplId(), 0, num_men_hoja);
		infoDebug("Vista", "Acabado");
	}
	
	/**
	 * Devuelve la lista de empleados que trabaja en el mismo departamento que
	 * el actual. La carga si esta no se ha cargado todavía.
	 * 
	 * @return la lista de empleados
	 */
	public ArrayList<Empleado> getEmpleados() {
		return empleados;
	}

	/**
	 * Devuelve la lista de mensajes que ha recibido el empleado actual. La
	 * carga si esta no se ha cargado todavía.
	 * 
	 * @return la lista de empleados
	 */
	public ArrayList<Mensaje> getMensajesEntrantes() {
		if (mensajesEntrantes == null) {
			loadMensajes();
		}
		return mensajesEntrantes;
	}

	/**
	 * Obtiene uno o varios empleados, que coincidan con los datos dados del
	 * mismo. Los parámetros pueden ser nulos.
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
		return controlador.getEmpleados(idEmpl, idDpto, idContrato, nombre,
				apellido1, apellido2, rango);
	}

	/**
	 * Guarda un empleado.
	 * 
	 * @param empleado
	 *            el empleado a guardar
	 * @return <i>true</i> si el empleado ha sido guardado
	 */
	public boolean insertEmpleado(Empleado empleado) {
		setProgreso("Insertando empleado", 50);
		boolean b = controlador.insertEmpleado(empleado);
		int i = 0;
		while (i < empleado.getDepartamentosId().size() && b) {
			b = controlador.insertDepartamentoUsuario(empleado.getEmplId(),
					empleado.getDepartamentoId(i));
			i++;
		}
		if (b)
			setProgreso("Empleado insertado", 100);
		else
			setProgreso("No se pudo insertar el empleado", 100);
		return b;
	}

	public ArrayList<Empleado> getEmpleadosDepartamento(String idDept) {
		return controlador.getEmpleadosDepartamento(getEmpleadoActual().getEmplId(),idDept);
	}

	/***************************************************************************
	 * Métodos relacionados con departamentos
	 */

	/**
	 * Obtiene un departamento, dado su nombre.
	 * 
	 * @param id
	 *            el identificador del departamento
	 * @return una instancia del departamento
	 */
	public Departamento getDepartamento(String id) {
		return controlador.getDepartamento(id);
	}

	/**
	 * Guarda un departamento.
	 * 
	 * @param departamento
	 *            el departamento a guardar
	 * @return <i>true</i> si el departamento ha sido guardado
	 */
	public boolean insertDepartamento(Departamento departamento) {
		return controlador.insertDepartamento(departamento);
	}

	/**
     * Devuelve los nombres de todos los departamentos de un jefe.
     *
     * @return un array con los nombres
     */
    public String[] getNombresDepartamentosJefe(){
        //Primero obtenemos el arrayList con los departamentos
        ArrayList<String> dpts=controlador.getDepartamentosJefe(getEmpleadoActual().getEmplId());
        //Devolvemos como array
        String[] array=new String[dpts.size()];
        dpts.toArray(array);
        return array;
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
	 * @return
	 */
	public ArrayList<Mensaje> getMensajesEntrantes(int idEmpl, int a, int b) {
		// Esto no funciona, y no sé por qué
		// setProgreso("Cargando mensajes", 50);
		ArrayList<Mensaje> array = controlador.getMensajesEntrantes(idEmpl, a,
				b);
		return array;
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
	 * @return
	 */
	public ArrayList<Mensaje> getMensajesSalientes(int idEmpl, int a, int b) {
		return controlador.getMensajesSalientes(idEmpl, a, b);
	}

	/**
	 * Marca un mensaje
	 * 
	 * @param mensaje
	 *            el mensaje a marcar
	 * @return
	 */
	public boolean marcarMensaje(Mensaje mensaje) {
		return controlador.marcarMensaje(mensaje);
	}


		
	
	
	/***************************************************************************
	 * Otros métodos
	 */
	
	/**
	 * Ajusta la barra de progreso de la ventana principal al valor del
	 * parámetro, y la hace desaparecer si ha terminado.
	 * 
	 * @param i
	 *            Un valor de 0 a 99, ó 100 para que desaparezca.
	 */
	public void setProgreso(String s, int i) {
		if (i02!=null) i02.setProgreso(s, i);
	}

	/**
	 * Devuelve la fecha actual
	 * 
	 * @return la fecha actual
	 */
	public Date getFechaActual() {
		return controlador.getFechaActual();
	}
	
	/**
	 * Devuelve el paquete de idioma
	 * @return el paquete de idioma actual
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	/**
	 * Carga la cache con los datos de la BD segun el tipo de usuario
	 * que hace login 
	 * @param tipo: Tipo de usuario: 0 empleado, 1 jefe
	 * @param numvendedor: El numero de vendedor del usuario para el
	 * que se van a cargar los datos
	 */
	public void loadCache() {
		int tipo = getEmpleadoActual().getRango();
		String dep = getEmpleadoActual().getDepartamentoId();
		int numvendedor = getEmpleadoActual().getEmplId();
		if (!alive) return;
		setProgreso("Cargando empleados", 25);
		empleados = controlador.getEmpleadosDepartamento(getEmpleadoActual().getEmplId(),dep);
		if (!alive) return;
		setProgreso("Cargando contratos", 50);
		contratos = controlador.getListaContratosDpto(dep);
		if (!alive) return;
		if (!alive) return;
		if (tipo == 1) {
			setProgreso("Cargando turnos", 70);
			turnos = controlador.getListaTurnosEmpleadosDpto(dep);
			setProgreso("", 100);
		} else if (tipo == 2) {
			ArrayList<String> temp = new ArrayList<String>();
			temp = controlador.getDepartamentosJefe(numvendedor);
			for (int i=0; i<temp.size(); i++)
				departamentosJefe.add(controlador.getDepartamento(temp.get(i)));
			setProgreso("Cargando turnos", 70);
			for (int i=0; i<departamentosJefe.size(); i++) {
				turnos = controlador.getListaTurnosEmpleadosDpto(departamentosJefe.get(i).getNombreDepartamento());
			}
			setProgreso("", 100);
			
		} else {
			System.err.println("Vista\t:: Tipo de empleado inválido para cargar la cache.");
		}
	}

	public void cambiarNombreDepartamento(String NombreAntiguo, String NombreNuevo) {
		// TODO Auto-generated method stub
//se modifica el nombre del Dpto. en las 3 tablas de Departamentos
		this.controlador.cambiaNombreDpto(NombreAntiguo, NombreNuevo);
		this.controlador.cambiaNombreDepartamentoUsuario(NombreAntiguo, NombreNuevo);
		this.controlador.cambiaNombreNumerosDEPARTAMENTOs(NombreAntiguo, NombreNuevo);
	}
	
	
	public ArrayList<String> getNombreTodosDepartamentos() {
		return this.controlador.getNombreTodosDepartamentos();
	}

	public ArrayList<Contrato> getListaContratosDepartamento() {
		return contratos;
	}
	/**
	 * Funcion que devuelve todos los nombres completos (nombre+apellidos)
	 *  de los empleados que pueden ser jefes de departamento
	 * @return Nombres de los jefes
	 */
	public ArrayList<String> getNombreTodosJefes() {
		return this.controlador.getNombreTodosJefes();
	}


	public ArrayList<Integer> getNumVendedorTodosJefes() {
		return this.controlador.getNumVendedorTodosJefes();
	}
	
	
	
	/**
	 * Funcion que incluye en la BBDD un nuevo departamento
	 * @param nombredep Nombre del departamento
	 * @param num Numero de departamento
	 * @param nomJefe nombre del Jefe de departamento
	 */
	public void crearDepartamento(String nombredep, String num, int nvJefe) {
		// TODO Auto-generated method stub by carlos Sánchez
		//Agustin deberia devolverme algo q me indique si hay un fallo alcrearlo cual es
		//hazlo como mas facil te sea.Yo no se cuantos distintos puede haber.
	
	//para añadir el Dpto en la BBDD no se necesita el Nombre del Jefe
	//pero si su numero de vendedor (por eso lo he puesto como int) que forma parte de la PK 
		
		int n= Integer.parseInt(num);
		this.controlador.insertDepartamentoUsuario(nvJefe, nombredep); //tabla DepartamentoUsuario
		this.controlador.insertNumerosDepartamento(n, nombredep); //tabla NumerosDEPARTAMENTOs
		this.controlador.insertDepartamentoPruebas(nombredep, nvJefe); //tabla DEPARTAMENTO
	}
	/**
	 * Función que nos dice si ya existe ese nombre de departament
	 * @param text nombre a comparar
	 * @return
	 */
	public boolean existeNombreDepartamento(String text) {
		// TODO Auto-generated method stub by Carlos Sánchez
		ArrayList<String> departamentos = this.getNombreTodosDepartamentos();
		return departamentos.contains(text);
	}
	/**
	 * Función que nos dice si ya existe ese número de departamento
	 * @param text Numero a comparar
	 * @return
	 */
	public boolean existeNumDepartamento(String text) {
		// TODO Auto-generated method stub by Carlos Sánchez
		ArrayList<String> numeros = this.controlador.getTodosNumerosDEPARTAMENTOs();
		return numeros.contains(text);

	}
	
	
	
	
	
	
}