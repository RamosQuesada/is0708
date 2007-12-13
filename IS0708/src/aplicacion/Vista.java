package aplicacion;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import interfaces.*;

public class Vista {
	private Controlador controlador;
	private Database db;
	private Shell shell;
	private ResourceBundle bundle;
	private Locale locale;
	private I02_Principal i02;
	public Vista (Shell shell, ResourceBundle bundle, Locale locale, Controlador controlador, Database db) {			
		this.shell = shell;
		this.bundle = bundle;
		this.locale = locale;
		this.controlador = controlador;
		this.db = db;

		// Login y conexión a la base de datos
		I01_Login login = new I01_Login(shell, bundle, db);
		boolean identificado = false;
		while (!identificado) {
			login.mostrarVentana();
			while (!login.dialog.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
			if (login.getBotonPulsado()==1) {
				// Si llega aquí, ya ha conexión con la base de datos
				if (login.getNumeroVendedor()==0) {
					if (login.getPassword()=="admin")
						System.out.println("Administrador identificado");
				}
				else {
					controlador.setEmpleadoActual(getEmpleado(login.getNumeroVendedor()));
					System.out.println("Empleado identificado: " + controlador.getEmpleadoActual().getNombreCompleto());
				}
				identificado = true;
				// si no, mostrar mensaje de error
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
		if (!shell.getDisplay().isDisposed()) {
			i02 = new I02_Principal(shell, shell.getDisplay(), bundle, locale, this);	
			// Este bucle mantiene la ventana abierta
			while (!shell.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
			if (!shell.getDisplay().isDisposed())
				shell.getDisplay().dispose();
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
	 * Ajusta la barra de progreso de la ventana principal al valor del 
	 * parámetro, y la hace desaparecer si ha terminado.
	 * @param i Un valor de 0 a 99, ó 100 para que desaparezca.
	 */
	public void setProgreso(int i){
		i02.setProgreso(i);
	}

	
/****************************************************************************
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
		setProgreso(50);
		setTextoEstado("Insertando empleado");
		boolean b = controlador.insertEmpleado(empleado);
		setProgreso(100);
		if (b)
			setTextoEstado("Empleado insertado");
		else 
			setTextoEstado("No se pudo insertar el empleado");
		return b;
	}
	

/****************************************************************************
 * Métodos relacionados con departamentos
 */
	
	/**
	 * Obtiene un departamento, dado su identificador.
	 * @param id el identificador del departamento
	 * @return una instancia del departamento
	 */
	public Departamento getDepartamento(int id){
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
}
