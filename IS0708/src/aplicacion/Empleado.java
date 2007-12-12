package aplicacion;

import java.util.ArrayList;
import aplicacion.Util.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import java.util.Date;

import javax.print.attribute.SetOfIntegerSyntax;
import org.eclipse.swt.graphics.*;

/**
 * Esta clase representa a un empleado.
 * <h1>Rango</h1>
 * <ul>
 * <li>	0 - Administrador<br>
 * 		El administrador es un superusuario que tiene 
 * 		permiso para crear gerentes y configurar la aplicación.
 * 		No es considerado un empleado, y no se le pueden asignar
 * 		departamentos. Por tanto tampoco puede consultar un cuadrante
 * 		ni su rendimiento.
 * <li>	1 - Empleado<br>
 * 		Un empleado es un usuario básico. Puede consultar su horario
 * 		y el de su departamento. Puede utilizar la mensajería, y
 * 		comprobar su rendimiento personal comparado al de otros
 * 		vendedores de su departamento.
 * <li> 2 - Jefe<br>
 * 		Un jefe es un empleado que tiene a su cargo uno o más
 * 		departamentos. Además de lo que puede hacer un empleado,
 * 		puede cambiar los cuadrantes de sus departamentos y mirar
 * 		sus estad�sticas e índices de rendimiento.
 * <li> 3 - Gerente<br>
 * 		El gerente es un empleado que tiene a su cargo empleados
 * 		que son jefes. Puede tener también la función de jefe si
 * 		tiene algún departamento a su cargo, y además puede crear
 * 		jefes, departamentos y asignar jefes a departamentos.
 * 
 * @author Daniel Dionne
 *
 */
public class Empleado implements Drawable {	
	/** @deprecated */
	// TODO quitar esta clase
	private class Contrato {};
	
	// TODO Para algunas variables, se guarda un entero al identificador de la clase 
	// y también un puntero. La idea es inicializar el puntero a null, y actualizarlo
	// en el primer acceso. Así se ahorran accesos innecesarios a la base de datos.
	// Hay que hacer los getters y setters con cuidado.
	// 
	// Dani (yo me entiendo...)
	
	private int idSuperior;
	private Empleado superior;
	private String nombre, apellido1, apellido2;
	private Color color;
	private int idEmpl;
	private Date fechaNac;
	private int sexo; // 0 femenino, 1 masculino
	private String email;
	private String password;
	private int grupo; // 0 principiante, 1 experto
	private int rango; // 0 administrador, 1 empleado, 2 jefe, 3 gerente
	private int departamento;
	private int idContrato;
	private Contrato contrato;
	private Date fContrato;
	private Date fAlta;
	private ArrayList<Integer> idSubordinados;
	private ArrayList<Empleado> subordinados;
	private ArrayList<Integer> idDepartamentos;
	private ArrayList<Departamento> departamentos;
	
	// TODO Eliminar el turno, que ir� en el Contrato
	// ahora s�lo sirve para hacer prubas de interfaz
	public Turno turno;
	
 
	/**
	 * Constructor de un empleado. No se hace ninguna comprobaci�n de ninguno de los datos.
	 * @param idSuperior	el id del empleado superior. Normalmente, el que llama al constructor
	 * @param idEmpl			el id del empleado o número de vendedor, debe ser un número de 8 cifras
	 * @param nombre		el nombre del empleado
	 * @param apellido1		el primer apellido del empleado
	 * @param apellido2		el segundo apellido del empleado
	 * @param fechaNac		la fecha de nacimiento del empleado
	 * @param sexo			el sexo del vendedor:	<ul>
	 * 												<li>0 - femenino
	 * 												<li>1 - masculino
	 * 												</ul>
	 * @param email			la dirección de email del empleado
	 * @param password		la contraseña de acceso del empleado
	 * @param grupo			la experiencia del empleado:	<ul>
	 * 														<li>0 - principiante
	 * 														<li>1 - experto
	 * 														</ul>
	 * @param departamento	el nombre del departamento al que pertenece	
	 * @param rango			el rango del empleado:	<ul>
	 * 												<li>0 - administrador
	 * 												<li>1 - empleado
	 * 												<li>2 - jefe
	 * 												<li>3 - gerente
	 * 												</ul>
	 * @param contrato		el contrato del empleado
	 * @param fContrato		fecha en que el empleado empieza a aparecer en los cuadrantes de este departamento
	 * @param fAlta			fecha en que el empleado empieza a trabajar en la empresa
	 * @param color			un color con el que se representará al empleado en los cuadrantes
	 */
	public Empleado (int idSuperior, int idEmpl, String nombre, String apellido1, String apellido2, 
			Date fechaNac, int sexo, String email, String password, int grupo,
			int departamento,int rango, int contrato, Date fContrato, Date fAlta, Color color,
			ArrayList<Integer> idDepartamentos, ArrayList<Integer> idSubordinados) {
		this.idSuperior	= idSuperior;
		this.idEmpl		= idEmpl;
		this.nombre		= nombre;
		this.apellido1	= apellido1;
		this.apellido2	= apellido2;
		this.fechaNac	= fechaNac;
		this.sexo		= sexo;
		this.email		= email;
		this.password	= password;
		this.grupo		= grupo;
		this.departamento=departamento;
		this.rango		= rango;
		this.idContrato	= contrato;
		this.fContrato	= fContrato;
		this.fAlta		= fAlta;
		this.color = color;
		// TODO Rellenar la lista de subordinados y de departamentos
		this.idSubordinados = idSubordinados;
		this.idDepartamentos = idDepartamentos;
	}

	/**
	 * Constructor para hacer pruebas
	 * @deprecated eliminar cuando ya no sea necesario
	 */
	public Empleado(int idEmpl, String nombre, Color color){
		this.idEmpl = idEmpl;
		this.nombre = nombre;
		this.color = color;
		turno = new Turno();
	}
	
	/**
	 * Constructor para hacer pruebas, Algoritmo
	 * @deprecated eliminar cuando ya no sea necesario
	 */
	public Empleado(int idEmpl, String nombre, Turno tur){
		this.idEmpl = idEmpl;
		this.nombre = nombre;
		turno = tur;
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
	 * Devuelve el nombre completo de un empleado.
	 * @return el nombre completo del empleado
	 */
	public String getNombreCompleto() {
		return getNombre() + getApellido1() + getApellido2();
	}

	/**
	 * Devuelve el número de vendedor de un empleado.
	 * @return	el número de vendedor del empleado.
	 */
	public int getIdEmpl() {
		return idEmpl;
	}
	
	/**
	 * Intenta asignar un número de vendedor a un empleado, que debe ser un número de 8 cifras.
	 * Si no es correcto, no produce ningún cambio.
	 * @param	idEmpl el número de vendedor a asignar al empleado.
	 * @return	<i>true</i> si se ha asignado correctamente el número, <i>false</i> si 
	 * 			el string no tiene longitud 8 o no es un número.
	 */
	public boolean setIdEmpl(String idEmpl){
		int n = Util.convertirNVend(idEmpl);
		if (n>0) this.idEmpl = n;
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
	 * @param fechaNac la fecha de nacimiento a asignar
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
	 * @see #setSexoMasculino()
	 */
	public void setSexoFemenino() {
		sexo = 0;
	}
	
	/**
	 * Asigna sexo masculino al empleado.
	 * @see #setSexoFemenino()
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
	
	/**
	 * Devuelve la contraseña de un empleado.
	 * @return la contraseña del empleado
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * Asigna una contraseña a un empleado.
	 * @param password la contraseña
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Devuelve el grupo de experiencia al que pertenece el empleado.
	 * @return 0 - principiante, 1 - experto
	 */
	public int getGrupo() {
		return grupo;
	}

	/**
	 * Asigna un grupo de experiencia al empleado. El parámetro debe ser 0 ó 1,
	 * o no se hará ninguna modificación.
	 * @param grupo 0 si es principiante, 1 si es experto
	 */
	public void setGrupo(int grupo) {
		if (grupo == 0 | grupo == 1)
			this.grupo = grupo;
	}
	
	/**
	 * Devuelve los departamentos los que pertenece el empleado.
	 * @return la lista de departamentos del usuario
	 */
	public ArrayList<Departamento> getDepartamento() {
		//TODO Controlar que la lista esté inicializada
		return departamentos;
	}

	/**
	 * Devuelve el departamento en la posición <i>i</i> de la lista de
	 * departamentos del empleado.
	 * @return el departamento número <i>i</i> del usuario
	 */
	public Departamento getDepartamento(int i) {
		//TODO Controlar que la lista esté inicializada
		return departamentos.get(i);
	}
	
	/**
	 * Devuelve el rango de un usuario.
	 * NO HAY setRango, el rango se ajusta automáticamente al añadir
	 * subordinados o departamentos
	 * @return el rango del empleado
	 */
	public int getRango() {
		return rango;
	}
	
	/**
	 * Comprueba si un empleado es gerente
	 * @return <i>true</i> si es gerente
	 */
	private boolean esGerente() {
		int i = 0; boolean b = false;
		while (i<idSubordinados.size() && !b) {
			// TODO
			//if (subordinados.get(i).getRango()>1) b = true;
		}
		return b;
	}

	/**
	 * Comprueba si un empleado es jefe
	 * @return <i>true</i> si es jefe
	 */
	public boolean esJefe() {
		int i = 0; boolean b = false;
		while (i<idDepartamentos.size() && !b) {
			// TODO Controlar que la lista esté inicializada
			if (this == departamentos.get(i).getJefeDepartamento()) b = true;
		}
		return b;
	}
	
	/**
	 * Actualiza el rango de un empleado, según los departamentos
	 * que tenga y si tiene algún subordinado jefe
	 */
	public void actualizarRango() {
		rango = 1;
		// Si tiene departamentos a su cargo, es jefe
		if (!idDepartamentos.isEmpty()) rango = 2;
		// Si alguno de sus empleados es jefe, entonces es gerente
		if (esGerente()) rango=3;
	}

	/**
	 * Asigna un departamento a un usuario y actualiza su rango.
	 * @param departamento el nombre del departamento
	 * @return <i>true</i> si el departamento se ha a�adido, false en caso contrario
	 */
	public boolean addDepartamento(Departamento departamento) {
		boolean esta = idDepartamentos.contains(departamento);
		if (!esta) {
			// TODO Controlar que la lista esté inicializada
			departamentos.add(departamento);
		}
		// Si era un empleado, ha pasado a ser jefe.
		// Si era gerente no hay modificaci�n.
		if (rango==1) rango = 2;
		return !esta;
	}
	
	public boolean removeDepartamento(Departamento departamento) {
		boolean b = idDepartamentos.remove(departamento);
		// quitar departamentos afecta a la condici�n de jefe,
		// pero deja intacta la de gerente
		// hay que mirar si todav�a tiene alg�n departamento
		if (rango == 2 && !esJefe()) {
			rango=1;
			// TODO
			//superior.actualizarRango();
		}
		return b;
	}
	
	/**
	 * Devuelve la lista de subordinados del empleado 
	 * @return la lista  de subordinados
	 */
	public ArrayList<Empleado> getSubordinados() {
		// TODO controlar que la lista esté actualizada
		return subordinados;
	}
		
	/**
	 * A�ade un subordinado y actualiza el rango del empleado.
	 * @param empleado el empleado subordinado
	 * @return <i>true</i> si se ha añadido el Empleado, <i>false</i> si
	 * ya estaba en la lista de subordinados
	 */
	public boolean addSubordinado(Empleado empleado) {
		boolean esta = idSubordinados.contains(empleado);
		if (!esta) {
			idSubordinados.add(empleado.idEmpl);
			// Si he a�adido a un jefe, el rango pasa a ser gerente
			if (empleado.getRango()==2) rango=3;
		}
		return !esta;
	}

	/**
	 * Elimina a un Empleado de la lista de subordinados y actualiza el rango.
	 * @param e el Empleado a eliminar de la lista de subordinados
	 * @return <i>true</i> si se ha eliminado el empleado, <i>false</i>
	 * si no existía en la lista de subordinados del empleado 
	 */
	public boolean removeSubordinado (Empleado e) {
		// TODO Controlar que la lista esté actualizada
		boolean b = subordinados.remove(e);

		// quitar subordinados afecta a la condici�n de gerente
		// hay que mirar si todav�a tiene alg�n subordinado jefe
		if (idSubordinados.isEmpty()) rango=1;
		else {
			int i = 0;
			while (i<idSubordinados.size() && rango==2) {
				// TODO
				//if (subordinados.get(i).getRango()>1) rango=3;
			}
		}
		// si se ha quedado sin subordinados jefes pero tiene departamentos,
		// entonces se queda como jefe
		if (!idDepartamentos.isEmpty() && rango==1) rango=2;
		return b;
	}
	
	/**
	 * Devuelve el identificador de su departamento principal
	 * @return el identificador
	 */
	public int getIdDepartamento() {
		return idDepartamentos.get(0);
	}
	
/*
 * TODO faltan estos getters y setters
 * 
	Contrato contrato;
	Date fContrato;
	Date fAlta;
*/
	
	public ImageData getDrawableImage() {
		// TODO
		return null;
	}
	
	public ImageData getPrintableImage(boolean bn) {
		// TODO
		return null;
	}
	
	
	public void dibujarTurno(Display display, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas) {
		// Un entero para sumar el tiempo que trabaja un empleado y mostrarlo a la izquierda
		int subDivs = 0;
		gc.setBackground(new Color(display, 0,0,0));
		if (margenNombres > 0) {
			gc.drawText(nombre, margenIzq, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
			gc.drawText(String.valueOf(subDivs/12)+":"+String.valueOf(Util.aString(subDivs%12*60/12)), margenNombres-10, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1), true);
		}
		turno.dibujarFranjas(display, gc, posV, color, margenIzq, margenNombres, margenSup, sep_vert_franjas, alto_franjas);
	}
	
	public Color dameColor() {
		return color;
	}

	public void setDepartamento(int departamento) {
		this.departamento = departamento;
	}

}