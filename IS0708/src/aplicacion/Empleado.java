package aplicacion;

import java.util.ArrayList;
import aplicacion.General.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import java.util.Date;

/**
 * Representa a un empleado.
 * @author Daniel
 *
 */
public class Empleado {
	private String nombre, apellido1, apellido2;
	private Color color;
	private int nvend;
	private Date fechaNac;
	private int sexo; // 0 femenino, 1 masculino
	private String email;
	private String contraseña;
	private int grupo; // 0 principiante, 1 experto
	private String departamento;
	private int rango;
	private int contrato;
	private Date fContrato;
	private Date fAlta;
	// Eliminar esto, que irá en el cuadrante
	// ahora sólo sirve para hacer prubas de interfaz
	public Turno turno;
	
 
	/**
	 * Constructor de un empleado. No se hace ninguna comprobación de ninguno de los datos.
	 * @param nvend			el número de empleado, debe ser un número de 8 cifras
	 * @param nombre		el nombre del empleado
	 * @param apellido1		el primer apellido del empleado
	 * @param apellido2		el segundo apellido del empleado
	 * @param fechaNac		la fecha de nacimiento del empleado
	 * @param sexo			el sexo del vendedor: 0 femenino, 1 masculino
	 * @param email			la dirección de email del empleado
	 * @param contraseña	la contraseña de acceso del empleado
	 * @param grupo			la experiencia del empleado: 0 principiante, 1 experto
	 * @param departamento	el nombre del departamento al que pertenece	
	 * @param rango			POR DETERMINAR SI SE QUEDARÁ COMO STRING O INT
	 * @param contrato		POR DETERMINAR
	 * @param fContrato		fecha en que el empleado empieza a aparecer en los cuadrantes de este departamento
	 * @param fAlta			fecha en que el empleado empieza a trabajar en la empresa
	 * @param color			un color con el que se representará al empleado en los cuadrantes
	 */
	public Empleado (int nvend, String nombre, String apellido1, String apellido2, 
			Date fechaNac, int sexo, String email, String contraseña, int grupo,
			String departamento, int rango, int contrato, Date fContrato, Date fAlta, Color color) {
		this.nvend		= nvend;
		this.nombre		= nombre;
		this.apellido1	= apellido1;
		this.apellido2	= apellido2;
		this.fechaNac	= fechaNac;
		this.sexo		= sexo;
		this.email		= email;
		this.contraseña	= contraseña;
		this.grupo		= grupo;
		this.departamento	= departamento;
		this.rango		= rango;
		this.contrato	= contrato;
		this.fContrato	= fContrato;
		this.fAlta		= fAlta;
		this.color = color;
		// TODO Esto debería quitarse cuando no haga falta
	}

	/**
	 * Constructor para hacer pruebas
	 * @deprecated eliminar cuando ya no sea necesaria
	 */
	public Empleado(int nvend, String nombre, Color color){
		this.nvend = nvend;
		this.nombre = nombre;
		this.color = color;
		turno = new Turno();
	}
	
	/**
	 * Devuelve el nombre de un empleado, sin apellidos.
	 * @return	el nombre del empleado.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Asigna un nombre al empleado.
	 * @param nombre	el String a asignar al nombre del empleado.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve el primer apellido de un empleado.
	 * @return	el primer apellido del empleado.
	 */
	public String getApellido1() {
		return apellido1;
	}

	/**
	 * Asigna un primer apellido al empleado.
	 * @param nombre	el String a asignar al primer apellido del empleado.
	 */
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	/**
	 * Devuelve el segundo apellido de un empleado.
	 * @return	el segundo apellido del empleado.
	 */
	public String getApellido2() {
		return apellido2;
	}

	/**
	 * Asigna un segundo apellido al empleado.
	 * @param nombre	el String a asignar al segundo apellido del empleado.
	 */
	public void setApellido2(String apellido2) {
		this.apellido1 = apellido2;
	}

	/**
	 * Devuelve el número de vendedor de un empleado.
	 * @return	el número de vendedor del empleado.
	 */
	public int getNVend() {
		return nvend;
	}
	
	/**
	 * Intenta asignar un número de vendedor a un empleado, que debe ser un número de 8 cifras.
	 * Si no es correcto, no produce ningún cambio.
	 * @param	nvend el número de vendedor a asignar al empleado.
	 * @return	<i>true</i> si se ha asignado correctamente el número, <i>false</i> si 
	 * 			el string no tiene longitud 8 o no es un número.
	 */
	public boolean setNVend(String nvend){
		int n = General.convertirNVend(nvend);
		if (n>0) this.nvend = n;
		return n>0;
	}

	/**
	 * Devuelve el color que representa al empleado.
	 * @return el color que represena al empleado
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Asigna un color a un empleado.
	 * @param color el color nuevo
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Devuelve la fecha de nacimiento de un empleado.
	 * @return la fecha de nacimiento del empleado
	 */
	public Date getFechaNac() {
		return fechaNac;
	}
	
	/**
	 * Asigna una fecha de nacimiento a un empleado.
	 */
	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}
	
	/**
	 * Devuelve el sexo del empleado, siendo 0 femenino y 1 masculino.
	 * @return el sexo del empleado
	 */
	public int getSexo() {
		return sexo;
	}
	
	/**
	 * Asigna sexo femenino al empleado.
	 */
	public void setSexoFemenino() {
		sexo = 0;
	}
	
	/**
	 * Asigna sexo masculino al empleado.
	 */
	public void setSexoMasculino() {
		sexo = 1;
	}
	
	/**
	 * Devuelve la dirección de email del empleado.
	 * @return la dirección de email del empleado.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Asigna una dirección de email a un empleado. No hace ninguna comprobación.
	 * @param email la dirección de email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
/*
 faltan estos getters y setters
	String contraseña;
	int grupo; // 0 principiante, 1 experto
	String departamento;
	int rango;
	int contrato;
	Date fContrato;
	Date fAlta;
*/
	
	public void dibujarTurno(Display display, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas) {
		// Un entero para sumar el tiempo que trabaja un empleado y mostrarlo a la izquierda
		int subDivs = 0;
		gc.setBackground(new Color(display, 0,0,0));
		if (margenNombres > 0) {
			gc.drawText(nombre, margenIzq, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
			gc.drawText(String.valueOf(subDivs/12)+":"+String.valueOf(General.aString(subDivs%12*60/12)), margenNombres-10, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
		}
		turno.dibujarFranjas(display, gc, posV, color, margenIzq, margenNombres, margenSup, sep_vert_franjas, alto_franjas);
	}
	
	public Color dameColor() {
		return color;
	}

}