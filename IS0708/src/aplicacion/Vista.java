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
	public Vista (Shell shell, ResourceBundle bundle, Locale locale, Controlador controlador, Database db) {			
		this.shell = shell;
		this.bundle = bundle;
		this.locale = locale;
		this.controlador = controlador;
		this.db = db;


		I02_Principal i02 = new I02_Principal(shell, shell.getDisplay(), bundle, locale, this);
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
		public ArrayList<Empleado> getEmpleado(Integer idEmpl, Integer idDpto, Integer idContrato, String nombre, String apellido1, String apellido2) {
			return controlador.getEmpleado(idEmpl, idDpto, idContrato, nombre, apellido1, apellido2);
		}

		/**
		 * Guarda un empleado.
		 * @param empleado el empleado a guardar
		 * @return <i>true</i> si el empleado ha sido guardado
		 */
		public boolean insertEmpleado(Empleado empleado) {
			return controlador.insertEmpleado(empleado);
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
