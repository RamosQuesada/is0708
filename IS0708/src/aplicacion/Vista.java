package aplicacion;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import idiomas.LanguageChanger;
import interfaces.*;

public class Vista extends Thread {
	private Controlador controlador;
	private Database db;
	private Shell shell;
	private Display display;
	private ResourceBundle bundle;
	private Locale locale;
	private I01_Login login; 
	private I02_Principal i02;
	
	public void run() {
		setName("Conexión base de datos");
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
						// Rellenar la barra de progreso
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

	/**
	 * Constructor de la vista:<ul>
	 * <li>Crea el display y el shell
	 * <li>Crea el gestor de idiomas
	 * <li>Conecta con la base de datos
	 * <li>Identifica al usuario
	 * @param controlador el controlador de la aplicación
	 * @param db la base de datos de la aplicación
	 */
	public Vista (Controlador controlador, Database db) {			
		this.controlador = controlador;
		this.db = db;

		// Creación del display y el shell
		display = new Display ();
		shell = new Shell(display);
		
		// Creación del gestor de idiomas
		LanguageChanger l = new LanguageChanger();
		// TODO Poner esto después del login
		// 0 español
		// 1 polaco
		// 2 inglés
		l.cambiarLocale(0);
		
		bundle = l.getBundle();
		locale = l.getCurrentLocale();
		
		// Login y conexión a la base de datos
		login = new I01_Login(shell, bundle, db);
		start();
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
						System.out.println("Administrador identificado");
						controlador.setEmpleadoActual(new Empleado(0,0,"Administrador","","",null,0,"","admin",0,0,0,null,null,null,null,null,0,0));
						identificado = true;
				}
				else {
					Empleado emp = getEmpleado(login.getNumeroVendedor());
					if (emp!=null) {
						// Comprobar la clave
						if (emp.getPassword().compareTo(login.getPassword())==0) {
							controlador.setEmpleadoActual(emp);
							identificado = true;
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
			if (!db.conexionAbierta())
				db.cerrarConexion();
		}
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
	public ArrayList<Empleado> getEmpleado(Integer idEmpl, Integer idDpto, Integer idContrato, String nombre, String apellido1, String apellido2, int rango) {
		return controlador.getEmpleado(idEmpl, idDpto, idContrato, nombre, apellido1, apellido2,rango);
	}

	/**
	 * Guarda un empleado.
	 * @param empleado el empleado a guardar
	 * @return <i>true</i> si el empleado ha sido guardado
	 */
	public boolean insertEmpleado(Empleado empleado) {
		setProgreso("Insertando empleado", 50);
		boolean b = controlador.insertEmpleado(empleado);
		if (b)
			setProgreso("Empleado insertado",100);
		else 
			setProgreso("No se pudo insertar el empleado", 100);
		return b;
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
		return controlador.getMensajesEntrantes(idEmpl, a, b);
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
