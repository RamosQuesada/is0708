package aplicacion;

import java.util.ArrayList;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.*;

import java.util.Date;
import java.sql.Time;

import aplicacion.Controlador;

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
	private int idContrato;
	private Contrato contrato;
	private Date fContrato;
	private Date fAlta;
	private int felicidad;
	private ArrayList<Integer> idSubordinados;
	private ArrayList<Empleado> subordinados;
	private ArrayList<Integer> idDepartamentos;
	private ArrayList<Departamento> departamentos;
	
	// TODO Eliminar el turno, que irá en el Contrato
	// ahora sólo sirve para hacer prubas de interfaz
	/** @deprecated */
	public Turno turno;
	
/*****************************************************************************************
 * Métodos privados:
 */

	/**
	 * Actualiza la lista de departamentos cargándolos de la BD
	 * @param c
	 */
	private void actualizarDepartamentos(Controlador c) {
		// Actualizar la lista si hace falta
		if (idDepartamentos.size() > departamentos.size())
			departamentos.clear();
			for (int i = 0; i < idDepartamentos.size(); i++) {
				c.setProgreso("Cargando departamentos", (100/idDepartamentos.size())*i);
				departamentos.add(c.getDepartamento(idDepartamentos.get(i)));
			}
	}
	
	/**
	 * Actualiza la lista de subordinados cargándolos de la BD
	 * @param c
	 */
	private void actualizarSubordinados(Controlador c) {
		// Actualizar la lista si hace falta
		if (idSubordinados.size() > subordinados.size())
			subordinados.clear();
			for (int i = 0; i < idSubordinados.size(); i++) {
				c.setProgreso("Cargando empleados", (100/idSubordinados.size())*i);
				subordinados.add(c.getEmpleado(idSubordinados.get(i)));
			}
	}

	/**
	 * Actualiza el empleado superior cargándolo de la BD
	 * @param c
	 */
	private void actualizarSuperior(Controlador c) {
		// Actualizar el superior si hace falta
		if (superior==null && idSuperior!=0) {
			c.setProgreso("Cargando empleado", 50);
			superior= c.getEmpleado(idSuperior);
			c.setProgreso("",100);
		}
	}
	
	/**
	 * Comprueba si un empleado es gerente.
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
	private boolean esJefe() {
		int i = 0; boolean b = false;
		while (i<idDepartamentos.size() && !b) {
			// TODO Controlar que la lista esté inicializada
			if (this == departamentos.get(i).getJefeDepartamento()) b = true;
		}
		return b;
	}
/*****************************************************************************************
 * Constructor, getters y setters:
 */
 
	/**
	 * Constructor de un empleado. No se hace comprobación de la mayoría de los datos.
	 * @param idSuperior	el id del empleado superior. Si no tiene empleado superior
	 * 						se puede dejar como <b>null</b> o cero.
	 * @param idEmpl		el id del empleado o número de vendedor, debe ser un número 
	 * 						de 8 cifras mayor que cero.
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
	 * 
	 */
	public Empleado (Integer idSuperior, int idEmpl, String nombre, String apellido1,
			String apellido2, Date fechaNac, int sexo, String email, String password,
			int grupo, int rango, int contrato, Date fContrato, Date fAlta, Color color,
			ArrayList<Integer> idDepartamentos, ArrayList<Integer> idSubordinados) {
		if (idSuperior==null) this.idSuperior=0;
		else setIdSuperior(idSuperior);
		setIdEmpl(idEmpl);
		this.nombre		= nombre;
		this.apellido1	= apellido1;
		this.apellido2	= apellido2;
		this.fechaNac	= fechaNac;
		this.sexo		= sexo;
		this.email		= email;
		this.password	= password;
		this.grupo		= grupo;
		this.rango		= rango;
		this.idContrato	= contrato;
		this.fContrato	= fContrato;
		this.fAlta		= fAlta;
		this.color = color;
		this.idSubordinados = idSubordinados;
		this.idDepartamentos = idDepartamentos;
		superior = null;
		subordinados	= new ArrayList<Empleado>();
		departamentos	= new ArrayList<Departamento>();
		
	}

	/**
	 * Devuelve el número de vendedor del superior. Si no tiene, devuelve cero.
	 * @return
	 */
	public int getIdSuperior() {
		return idSuperior;
	}
	
	/**
	 * Asigna el id del superior al empleado actual. El número debe ser un valor
	 * mayor que 1 y debe tener menos de ocho cifras.
	 * @param id el número a asignar
	 * @return <i>true</i> si se ha realizado la asignación
	 */
	public boolean setIdSuperior(int id) {
		boolean b = false;
		if (id>1 && id<99999999) {
			idSuperior = id;
			b = true;
		}
		return b;
	}
	
	/**
	 * Devuelve el empleado superior. Si no tiene, devuelve null;
	 * @param c el controlador de la aplicación.
	 * @return
	 */
	public Empleado getSuperior(Controlador c) {
		actualizarSuperior(c);
		return superior;
	}
	
	/**
	 * Asigna un superior al empleado.
	 * @param e el empleado superior
	 */
	public void setSuperior(Empleado e) {
		superior = e;
		idSuperior = e.getIdEmpl();
	}
	
	/**
	 * Devuelve el número de vendedor de un empleado.
	 * @return	el número de vendedor del empleado.
	 */
	public int getIdEmpl() {
		return idEmpl;
	}
	
	/**
	 * Intenta asignar un número de vendedor a un empleado, que debe ser un número de 8
	 * cifras mayor que 0. Si no es correcto, no se produce ningún cambio.
	 * @param	idEmpl el número de vendedor a asignar al empleado.
	 * @return	<i>true</i> si se ha asignado correctamente el número, <i>false</i> si 
	 * 			el string no tiene longitud 8 o no es un número.
	 */
	public boolean setIdEmpl (String idEmpl){
		int n = Util.convertirNVend(idEmpl);
		if (n>0) this.idEmpl = n;
		return n>0;
	}
	
	/**
	 * Asigna el id al empleado actual. El número debe ser un valor mayor que 1 y debe
	 * tener menos de ocho cifras.
	 * @param id el número a asignar
	 * @return <i>true</i> si se ha realizado la asignación
	 */
	public boolean setIdEmpl (int id) {
		boolean b = false;
		if (id>1 && id<99999999) {
			idEmpl = id;
			b = true;
		}
		return b;
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
	 * Asigna un sexo al empleado:
	 * <ul>
	 * 		<li>0 - femenino
	 * 		<li>1 - masculino
	 * 	</ul>
	 * @param s cero para femenino, uno para masculino
	 * @see #setSexoFemenino()
	 * @see #setSexoMasculino()
	 */
	public void setSexo(int s) {
		if (s == 0 | s== 1)
			sexo = s;
	}
	/**
	 * Asigna sexo femenino al empleado.
	 * @see #setSexo
	 * @see #setSexoMasculino()
	 */
	public void setSexoFemenino() {
		sexo = 0;
	}
	
	/**
	 * Asigna sexo masculino al empleado.
	 * @see #setSexo
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
	 * @see #setGrupoPrincipiante()
	 * @see #setGrupoExperto()
	 */
	public void setGrupo(int grupo) {
		if (grupo == 0 | grupo == 1)
			this.grupo = grupo;
	}
	
	/**
	 * Pone al empleado como principiante.
	 * @see #setGrupo(int)
	 * @see #setGrupoExperto()
	 */
	public void setGrupoPrincipiante() {
		grupo = 0;
	}

	/**
	 * Pone al empleado como experto.
	 * @see #setGrupo(int)
	 * @see #setGrupoPrincipiante()()
	 */
	public void setGrupoExperto() {
		grupo = 1;
	}
	
	/**
	 * Devuelve el rango de un empleado.
	 * <ul>
	 * <li>0 - administrador
	 * <li>1 - empleado
	 * <li>2 - jefe
	 * <li>3 - gerente
	 * </ul>
	 * NO HAY setRango, el rango se ajusta automáticamente al añadir subordinados o
	 * departamentos al empleado.
	 * @return el rango del empleado
	 */
	public int getRango() {
		return rango;
	}
	
	
	
	
	/**
	 * Devuelve los departamentos a los que pertenece el empleado.
	 * @return la lista de departamentos del usuario
	 */
	public ArrayList<Departamento> getDepartamentos(Controlador c) {
		actualizarDepartamentos(c);
		return departamentos;
	}

	/**
	 * Devuelve el departamento en la posición <i>i</i> de la lista de
	 * departamentos del empleado.
	 * @return el departamento número <i>i</i> del usuario
	 */
	public Departamento getDepartamento(Controlador c, int i) {
		actualizarDepartamentos(c);
		return departamentos.get(i);
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
	public ArrayList<Empleado> getSubordinados(Controlador c) {
		actualizarSubordinados(c);
		return subordinados;
	}
		
	/**
	 * Añade un subordinado y actualiza el rango del empleado.
	 * @param empleado el empleado subordinado
	 * @return <i>true</i> si se ha añadido el Empleado, <i>false</i> si
	 * ya estaba en la lista de subordinados
	 */
	public boolean addSubordinado(Empleado empleado) {
		boolean esta = idSubordinados.contains(empleado);
		if (!esta) {
			idSubordinados.add(empleado.idEmpl);
			subordinados.add(emp);
			// Si he añadido a un jefe, el rango pasa a ser gerente
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
		if (b) idSubordinados.remove(e.getIdEmpl());
		// quitar subordinados afecta a la condición de gerente
		// hay que mirar si todavía tiene algún subordinado jefe
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
	
	public boolean estaDisponible(int dia, Time iniH, Time finH){
		boolean puede = true;
		
		//COMPROBAR SI TRABAJA UN DIA	
		//COMPROBAR SI ESTA DE VACACIONES

		if ((turno.getHoraEntrada().getTime() > iniH.getTime()) || (turno.getHoraSalida().getTime() < finH.getTime())){
			puede = false;
		} else {
			if ((iniH.getTime() >= turno.getHoraDescanso().getTime()) && (iniH.getTime() < Util.calculaFinDescanso(turno.getHoraDescanso(),turno.getTDescanso()).getTime())){
				puede = false;
			//} else {
			//	if ((finH.getTime() > turno.getHoraDescanso().getTime()) && (finH.getTime() < Util.calculaFinDescanso(turno.getHoraDescanso(),turno.getTDescanso()).getTime()))
			//		puede = false;
			//	}
			}
		}
		return puede;
	}
		
	
	
	public Color dameColor() {
		return color;
	}

	public void setDepartamento(int departamento) {
		this.departamento = departamento;
	}

	
	
	
	/**
	 * Constructor para hacer pruebas TODO
	 * @deprecated eliminar cuando ya no sea necesario
	 */
	public Empleado(int idEmpl, String nombre, Color color){
		this.idEmpl = idEmpl;
		this.nombre = nombre;
		this.color = color;
		turno = new Turno();
	}
	
	/**
	 * Constructor para hacer pruebas, Algoritmo TODO
	 * @deprecated eliminar cuando ya no sea necesario
	 */
	public Empleado(int idEmpl, String nombre, Turno tur){
		this.idEmpl = idEmpl;
		this.nombre = nombre;
		turno = tur;
	}
}