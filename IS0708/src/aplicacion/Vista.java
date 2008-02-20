package aplicacion;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

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
	private ArrayList<Empleado> empleados = new ArrayList<Empleado>();
	private ArrayList<Mensaje> mensajesEntrantes = new ArrayList<Mensaje>();
	private int num_men_hoja = 10;
	private LanguageChanger l;
	private Thread conector, loader;
	
	/**
	 * Este hilo conecta con la base de datos. 
	 * @author Daniel Dionne
	 *
	 */
	public class Conector implements Runnable {
		public void run() {
			// Conectar con la base de datos
			db.abrirConexion();
			if (!login.isDisposed())
				if (!db.conexionAbierta()) {
					shell.getDisplay().asyncExec(new Runnable () {
						public void run() {
							MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
							messageBox.setText (bundle.getString("Error"));
							messageBox.setMessage (bundle.getString("I01_err_Conexion"));
						}
					});
				}
				else {
					shell.getDisplay().asyncExec(new Runnable () {
						public void run() {
							// TODO Por alguna razón oculta de los threads, aun habiendo comprobado
							// antes si el dialog sigue presente, si no lo compruebo de nuevo
							// a veces da error.
							if (!login.isDisposed()) 
								login.setProgreso("Conectado.");
						}
					});
				}
			else {
				// En este caso se ha cerrado la aplicación antes de que termine de conectar.			
				if (db.conexionAbierta())
					db.cerrarConexion();
			}
		}
	}
	
	/**
	 * Este hilo carga empleados y mensajes de la base de datos
	 * @author Daniel Dionne
	 */
	public class Loader implements Runnable {
		public synchronized void run() {
			while (alive) {
				// Cargar empleados
				loadEmpleados();
				// Cargar mensajes
				loadMensajes();
				
				
				
				try {
					// TODO Espera 1/2 minuto (¿cómo lo dejamos?)
					wait(20000);
				} catch (Exception e) {}
			}
		}
	}

	/**
	 * Constructor de la vista:<ul>
	 * <li>Crea el display y el shell
	 * <li>Crea el gestor de idiomas
	 * <li>Conecta con la base de datos
	 * <li>Identifica al usuario
	 * @param controlador el controlador de la aplicación
	 * @param db la base de datos de la aplicación
	 */
	public Vista (Display d, Controlador controlador, Database db) {			
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
	
	public void start() {
		// Login y conexión a la base de datos
		login = new I01_Login(shell, bundle, db);
		conector = new Thread(new Conector());
		loader = new Thread(new Loader());
		conector.start();
		boolean identificado = false;
		while (!identificado) {
			if (db.conexionAbierta()) login.mostrarVentana("Conectado.");
			else login.mostrarVentana("Conectando...");
			while (!login.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
			if (login.getBotonPulsado()==1) {
				// Si llega aquí, ya hay conexión con la base de datos
				// Login de administrador
				if (login.getNumeroVendedor()==0 && login.getPassword().compareTo("admin")==0) {
					System.out.println("aplicacion.Vista.java\t::Administrador identificado");
					controlador.setEmpleadoActual(new Empleado(0,0,"Administrador","","",null,0,"","admin",0,0,0,null,null,null,null,null,0,0,0));
					identificado = true;
				}
				else {
					Empleado emp = getEmpleado(login.getNumeroVendedor());
					loader.start();
					if (emp!=null) {
						// Comprobar la clave
						if (emp.getPassword().compareTo(login.getPassword())==0) {
							controlador.setEmpleadoActual(emp);
							identificado = true;
							// Configurar idioma al del empleado
							l.cambiarLocale(controlador.getEmpleadoActual().getIdioma());
							bundle = l.getBundle();
							locale = l.getCurrentLocale();
						}
						else {
							// Si el password no coincide
							MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
							messageBox.setText (bundle.getString("Error"));
							messageBox.setMessage (bundle.getString("I01_err_Login2"));
							messageBox.open();							
						}
					}
					else {
						// Si el usuario no existe en la base de datos
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
						messageBox.setText (bundle.getString("Error"));
						messageBox.setMessage (bundle.getString("I01_err_Login1"));
						messageBox.open();
					}
				}
			}
			else {
				//Salir de la aplicación
				shell.getDisplay().dispose();
				identificado = true; // Para que salga del bucle
				if (db.conexionAbierta())
					db.cerrarConexion();
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
			i02.dispose();
			alive = false;
			loader.interrupt();
			if (!db.conexionAbierta())
				db.cerrarConexion();
		}
	}
	
	public void stop() {
		alive = false;
		shell.dispose();
		loader.interrupt();
		display.dispose();
	}
	/**
	 * Devuelve el display de la aplicación.
	 * @return el display de la aplicación
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * Muestra el parámetro en la barra de abajo de la ventana principal.
	 * @param estado el String a mostrar
	 */
	public void setTextoEstado(String estado) {
		i02.setTextoEstado(estado);
	}

	/**
	 * Devuelve el controlador de la aplicación
	 * @return el controlador de la aplicación
	 */
	public Controlador getControlador() {
		return controlador;
	}

	/**
	 * Muestra el cursor de reloj de arena
	 */
	public void setCursorEspera() {
		shell.setCursor(new Cursor(display,SWT.CURSOR_WAIT));
	}

	/**
	 * Muestra el cursor normal
	 */
	public void setCursorFlecha() {
		shell.setCursor(new Cursor(display,SWT.CURSOR_ARROW));
	}

	/**
	 * Devuelve si la aplicación se ha iniciado en modo debug
	 * @return <i>true</i> si la aplicación se ha iniciado en modo de corrección de errores
	 */
	public boolean getModoDebug() {
		return controlador.getModoDebug();
	}

	/**
	 * Muestra un mensaje de debug por consola.
	 * @param nombreClase el nombre de la clase que emite el mensaje
	 * @param mensaje el mensaje a mostrar
	 */
	public void infoDebug(String nombreClase, String mensaje) {
		if (controlador.getModoDebug()) System.out.println("Debug :: [" + nombreClase + "]\n    " + mensaje);
	}


	/*****************************************************************************************
	 * Métodos relacionados con empleados
	 */

	/**
	 * Devuelve el empleado que ha iniciado la sesión.
	 * @return el empleado que ha iniciado la sesión
	 */
	public Empleado getEmpleadoActual() {
		return controlador.getEmpleadoActual();
	}
	/**
	 * Obtiene un empleado, dado su número de vendedor o identificador.
	 * @param idEmpl el identificador del empleado o número de vendedor
	 * @return una instancia nueva del empleado
	 */
	public Empleado getEmpleado(int idEmpl) {
		return controlador.getEmpleado(idEmpl);
	}

	/**
	 * Carga la lista de empleados que trabaja en el mismo departamento que el actual
	 */
	public void loadEmpleados() {
		infoDebug("Vista", "Cargando empleados");
		empleados = getEmpleados(null, getEmpleadoActual().getDepartamentoId(), null,null, null, null, null);
		infoDebug("Vista", "Acabado de cargar empleados");
	}

	/**
	 * Carga los mensajes de la base de datos
	 */
	public void loadMensajes() {
		// Carga mensajes
		infoDebug("Vista", "Cargando mensajes");
		mensajesEntrantes = getMensajesEntrantes(getEmpleadoActual().getEmplId(), 0, num_men_hoja);
		infoDebug("Vista", "Acabado");
	}

	/**
	 * Devuelve la lista de empleados que trabaja en el mismo departamento que el actual.
	 * La carga si esta no se ha cargado todavía.
	 * @return la lista de empleados
	 */
	public ArrayList<Empleado> getEmpleados() {
		return empleados;
	}

	/**
	 * Devuelve la lista de mensajes que ha recibido el empleado actual.
	 * La carga si esta no se ha cargado todavía.
	 * @return la lista de empleados
	 */
	public ArrayList<Mensaje> getMensajesEntrantes() {
		if (mensajesEntrantes==null) {
			loadMensajes();
		}
		return mensajesEntrantes;
	}

	/**
	 * Obtiene uno o varios empleados, que coincidan con los datos
	 * dados del mismo. Los parámetros pueden ser nulos.
	 * @param idEmpl		el identificador del empleado
	 * @param idDpto		el identificador del departamento al que pertenece
	 * @param idContrato	el identificador del contrato que tiene
	 * @param nombre		el nombre del empleado
	 * @param apellido1		el primer apellido del empleado
	 * @param apellido2		el segundo apellido del empleado
	 * @return una lista de empleados que coincida con los datos dados
	 */
	public ArrayList<Empleado> getEmpleados(Integer idEmpl, String idDpto, Integer idContrato, String nombre, String apellido1, String apellido2, Integer rango) {
		return controlador.getEmpleados(idEmpl, idDpto, idContrato, nombre, apellido1, apellido2,rango);
	}

	/**
	 * Guarda un empleado.
	 * @param empleado el empleado a guardar
	 * @return <i>true</i> si el empleado ha sido guardado
	 */
	public boolean insertEmpleado(Empleado empleado) {
		setProgreso("Insertando empleado", 50);
		boolean b = controlador.insertEmpleado(empleado);
		int i = 0;
		while (i<empleado.getDepartamentosId().size() && b) {
			b = controlador.insertDepartamentoUsuario(empleado.getEmplId(), empleado.getDepartamentoId(i));
			i++;
		}
		if (b)
			setProgreso("Empleado insertado",100);
		else 
			setProgreso("No se pudo insertar el empleado", 100);
		return b;
	}

	public ArrayList<Empleado> getEmpleadosDepartamento(String idDept) {
		return controlador.getEmpleadosDepartamento(idDept);
	}

	/*****************************************************************************************
	 * Métodos relacionados con departamentos
	 */

	/**
	 * Obtiene un departamento, dado su nombre.
	 * @param id el identificador del departamento
	 * @return una instancia del departamento
	 */
	public Departamento getDepartamento(String id){
		return controlador.getDepartamento(id);
	}

	/**
	 * Guarda un departamento.
	 * @param departamento el departamento a guardar
	 * @return <i>true</i> si el departamento ha sido guardado
	 */
	public boolean insertDepartamento(Departamento departamento){
		return controlador.insertDepartamento(departamento);
	}

	/*****************************************************************************************
	 * Métodos relacionados con mensajes
	 */

	/**
	 * Obtiene una lista de <i>b</i> mensajes entrantes por orden cronológico, del más
	 * nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * @param idEmpl el empleado destinatario de los mensajes
	 * @param a mensaje por el que empezar, siendo 1 el más reciente
	 * @param b cuántos mensajes coger
	 * @return
	 */
	public ArrayList<Mensaje> getMensajesEntrantes(int idEmpl, int a, int b){
		//Esto no funciona, y no sé por qué
		//setProgreso("Cargando mensajes", 50);
		ArrayList<Mensaje> array = controlador.getMensajesEntrantes(idEmpl, a, b);
		return array;
	}

	/**
	 * Obtiene una lista de <i>b</i> mensajes salientes por orden cronológico, del más
	 * nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * @param idEmpl el empleado remitente de los mensajes
	 * @param a mensaje por el que empezar, siendo 1 el más reciente
	 * @param b cuántos mensajes coger
	 * @return
	 */
	public ArrayList<Mensaje> getMensajesSalientes(int idEmpl, int a, int b){
		return controlador.getMensajesSalientes(idEmpl, a, b);
	}

	/**
	 * Marca un mensaje
	 * @param mensaje el mensaje a marcar
	 * @return
	 */
	public boolean marcarMensaje(Mensaje mensaje) {
		return controlador.marcarMensaje(mensaje);
	}
	/*****************************************************************************************
	 * Otros métodos
	 */
	/**
	 * Ajusta la barra de progreso de la ventana principal al valor del 
	 * parámetro, y la hace desaparecer si ha terminado.
	 * @param i Un valor de 0 a 99, ó 100 para que desaparezca.
	 */
	public void setProgreso(String s, int i){
		i02.setProgreso(s,i);
	}
}
