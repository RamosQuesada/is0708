package aplicacion;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import java.sql.Date;
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
 * 		sus estad?sticas e índices de rendimiento.
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
	
	// Para algunas variables, se guarda un entero al identificador de la clase 
	// y también un puntero. La idea es inicializar el puntero a null, y actualizarlo
	// en el primer acceso. Así se ahorran accesos innecesarios a la base de datos.
	// Dani (yo me entiendo...)
	
	private int idSuperior;
	private Empleado superior;
	private int idEmpl;
	private String nombre, apellido1, apellido2;
	private Color color;
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
	private int idioma;
	private ArrayList<Integer> idSubordinados;
	private ArrayList<Empleado> subordinados;
	private ArrayList<String> idDepartamentos;
	private ArrayList<Departamento> departamentos;
	private Turno turnoActual;	//No lo borreis golfos. (19-Dic)
	private int turnoFavorito;
	
	//Optimizacion Algoritmo (reducción llamadas a BBDD)
	private ArrayList<Turno> turnoE;
	private ArrayList<ArrayList<String>> turnosStr;

	
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
	 * Actualiza el contrato cargándolo de la BD
	 * @param c
	 */
	private void actualizarContrato(Controlador c) {
		// Actualizar el contrato si hace falta
		if (contrato==null && idContrato!=0) {
			c.setProgreso("Cargando contrato", 50);
			contrato= c.getContrato(idContrato);
			c.setProgreso("",100);
		}
	}
	
	/**
	 * Comprueba si un empleado es gerente.
	 * @return <i>true</i> si es gerente
	 */
	private boolean esGerente(Controlador c) {
		// Un empleado es gerente cuando subordinados que son jefes o gerentes
		int i = 0; boolean b = false;
		actualizarSubordinados(c);
		while (i<subordinados.size() && !b) {
			if (subordinados.get(i).getRango()>1) b = true;
		}
		return b;
	}

	/**
	 * Comprueba si un empleado es jefe.
	 * @return <i>true</i> si es jefe
	 */
	private boolean esJefe(Controlador c) {
		// Un empleado es jefe si tiene departamentos a su cargo
		// Dicho de otra manera, si es jefe de alguno de sus departamentos
		int i = 0; boolean b = false;
		actualizarDepartamentos(c);
		while (i<departamentos.size() && !b) {
			if (this == departamentos.get(i).getJefeDepartamento()) b = true;
		}
		return b;
	}
	
	/**
	 * Actualiza el rango de un empleado.
	 */
	private void actualizarRango(Controlador c) {
		rango = 1;
		if (esJefe(c)) rango = 2;
		else if (esGerente(c)) rango=3;
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
	 * @param color			un color con el que se representará al empleado en los cuadrantes
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
	 * @param idSubordinados
	 * @param idDepartamento
	 * @param felicidad		Grado de satisfaccion de un usuario con su horario
	 * @param idioma 		Idioma de la aplicacion para el usuario
	 * @param turnoFavorito	id del turno favorito del empleado (-1 si no se quiere asignar ninguno)
	 */
	public Empleado (Integer idSuperior, int idEmpl, String nombre, String apellido1,
			String apellido2, Date fechaNac, int sexo, String email, String password,
			int grupo, int rango, int contrato, Date fContrato, Date fAlta, Color color,
			String idDepartamento, ArrayList<Integer> idSubordinados,int felicidad, int idioma, int turnoFavorito) {
		if (idSuperior==null) this.idSuperior=0;
		else setIdSuperior(idSuperior);
		setEmplId(idEmpl);
		this.nombre		= nombre;
		this.apellido1	= apellido1;
		this.apellido2	= apellido2;
		this.color		= color;
		this.fechaNac	= fechaNac;
		this.sexo		= sexo;
		this.email		= email;
		this.password	= password;
		this.grupo		= grupo;
		this.rango		= rango;
		this.idContrato	= contrato;
		this.fContrato	= fContrato;
		this.fAlta		= fAlta;
		this.idSubordinados = idSubordinados;
		idDepartamentos = new ArrayList<String>();
		if (idDepartamento!=null) idDepartamentos.add(idDepartamento);
		superior 		= null;
		subordinados	= new ArrayList<Empleado>();
		departamentos	= new ArrayList<Departamento>();
		this.felicidad	= felicidad;
		this.idioma		= idioma;
		this.turnoActual = null;
		this.turnoE		= null;
		this.turnosStr	= null;
		this.turnoFavorito = turnoFavorito;
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
		if (id>1 && id<=99999999) {
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
		idSuperior = e.getEmplId();
	}
	
	/**
	 * Devuelve el número de vendedor de un empleado.
	 * @return	el número de vendedor del empleado.
	 */
	public int getEmplId() {
		return idEmpl;
	}
	
	/**
	 * devuelve la fecha de alta del contrato del empleado
	 * @return fecha de alta
	 * deprecated
	 *	 */
	public Date getFcontrato(){
		return fContrato;
	}
	
	/**
	 * Intenta asignar un número de vendedor a un empleado, que debe ser un número de 8
	 * cifras mayor que 0. Si no es correcto, no se produce ningún cambio.
	 * @param	idEmpl el número de vendedor a asignar al empleado.
	 * @return	<i>true</i> si se ha asignado correctamente el número, <i>false</i> si 
	 * 			el string no tiene longitud 8 o no es un número.
	 */
	public boolean setEmplId (String idEmpl){
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
	public boolean setEmplId (int id) {
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
		String ap1 = getApellido1();
		String ap2 = getApellido2();
		if (ap1==null) ap1="";
		if (ap2==null) ap2="";
		return  getNombre() + " " + ap1 + " " + ap2;
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
	 * Asigna un identificador de contrato al empleado.
	 * @param id el identificador del contrato
	 */
	public void setIdContrato(int id) {
		idContrato = id;
	}
	
	/**
	 * Asigna un contrato a un empleado.
	 * @param c el contrato a asignar
	 */
	public void setContrato(Contrato c) {
		// TODO
		//idContrato = c.getId();
		contrato = c;
	}
	
	/**
	 * Devuelve el identificador del contrato de un empleado.
	 * @return el identificador del contrato
	 */
	public int getContratoId() {
		return idContrato;
	}
	
	/**
	 * Devuelve el contrato asignado al empleado.
	 * @param c el controlador de la aplicación
	 * @return el contrato del empleado
	 */
	public Contrato getContrato(Controlador c) {
		actualizarContrato(c);
		return contrato;
	}
	
	/*
	 * TODO faltan estos getters y setters
	 * 
		Date fContrato;
		Date fAlta;
	*/

	/**
	 * Devuelve el parámetro felicidad del empleado.
	 * @return el valor de felicidad
	 */
	public int getFelicidad() {
		return felicidad;
	}
	
	
	public Turno getTurnoActual() {
		return turnoActual;
	}
	
	public void setTurnoActual(Turno t)
	{
		turnoActual = t;
	}
	public Date getFAlta(){
		return fAlta;
	}
	/**
	 * Asigna un valor de felicidad al empleado.
	 * @param felicidad el valor de felicidad a asignar
	 */
	public void setFelicidad(int felicidad) {
		this.felicidad = felicidad;
	}
	
	/**
	 * Devuelve el idioma del empleado:<ul>
	 * <li>1 - Español
	 * <li>2 - Inglés
	 * <li>3 - Polaco
	 * @return el idioma del empleado
	 */
	public int getIdioma() {
		return idioma;
	}

	/**
	 * Asigna un idioma al empleado:<ul>
	 * <li>1 - Español
	 * <li>2 - Inglés
	 * <li>3 - Polaco
	 * @param idioma el idioma a asignar
	 */
	public void setIdioma(int idioma) {
		this.idioma = idioma;
	}

	/**
	 * Asigna un arrayList de IDs de departamentos a un empleado.
	 * @param departamentos
	 */
	public void setIDDepartamentos(ArrayList<String> departamentos) {
		this.idDepartamentos = departamentos;
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
	 * Devuelve los nombres de los departamentos a los que pertenece el empleado.
	 * @return la lista de nombres de los departamentos del usuario
	 */
	public ArrayList<String> getDepartamentosId() {
		return idDepartamentos;
	}

	/**
	 * Devuelve el departamento en la posición <i>i</i> de la lista de departamentos del 
	 * empleado. 
	 * <b>NOTA</b>: Los empleados normales sólo tienen un departamento, el de la posición
	 * cero.
	 * @return el departamento número <i>i</i> del usuario
	 */
	public Departamento getDepartamento(Controlador c, int i) {
		actualizarDepartamentos(c);
		return departamentos.get(i);
	}
	
	/**
	 * Devuelve el identificador de su departamento número i
	 * @param i el departamento a coger, 0 es el principal
	 * @return el identificador del departamento
	 */
	public String getDepartamentoId(int i) {
		return idDepartamentos.get(i);
	}
	
	/**
	 * Devuelve el identificador de su departamento principal
	 * @return el identificador del departamento
	 */
	public String getDepartamentoId() {
		return idDepartamentos.get(0);
	}

	/**
	 * Añade/asigna un departamento a un usuario y actualiza su rango.
	 * @param c el controlador de la aplicación
	 * @param departamento el departamento
	 * @return <i>true</i> si el departamento se ha añadido, false en caso contrario
	 */
	public boolean addDepartamento(Departamento departamento) {
		boolean esta = idDepartamentos.contains(departamento.getNombreDepartamento());
		if (!esta) {
			idDepartamentos.add(departamento.getNombreDepartamento());
		}
		// Si era empleado y ahora tiene más de un departamento, ha pasado a jefe
		// TODO Esto no me convence nada
		if (rango==1 && idDepartamentos.size()>1) rango = 2;
		// Si era jefe o gerente no hay modificación.
		return !esta;
	}
	
	/**
	 * Quita un departamento de un empleado.
	 * @param c el controlador de la aplicación
	 * @param departamento el departamento a quitar
	 * @return
	 */
	public boolean removeDepartamento(Controlador c, Departamento departamento) {
		boolean b = idDepartamentos.remove(departamento.getNombreDepartamento());
		// Quitar departamentos afecta a la condición de jefe, pero deja intacta la de 
		// gerente. Hay que mirar si todavía tiene algún departamento.
		if (rango == 2 && !esJefe(c)) {
			rango=1;
			superior.actualizarRango(c);
		}
		return b;
	}
	
	public Color dameColor(){
		return color;
	}
	
	/**
	 * Devuelve la lista de subordinados del empleado 
	 * @return la lista de subordinados
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
		boolean esta = idSubordinados.contains(empleado.getEmplId());
		if (!esta) {
			idSubordinados.add(empleado.idEmpl);
			subordinados.add(empleado);
			// Si he añadido a un jefe, el rango pasa a ser gerente
			if (empleado.getRango()==2) rango=3;
		}
		return !esta;
	}
	
	
	/**
	 * Devuelve el turno favorito del empleado.
	 * @return el id del turno favorito, -1 si no tiene turno favorito
	 */
	public int getTurnoFavorito() {
		return turnoFavorito;
	}

	/**
	 * Asigna un turno favorito.
	 * @param turnoFavorito el id del turno a asignar como favorito
	 */
	public void setTurnoFavorito(int turnoFavorito) {
		this.turnoFavorito = turnoFavorito;
	}
	
	/**
	 * Quita el turno favorito de un empleado.
	 */
	public void removeTurnoFavorito() {
		this.turnoFavorito = -1;
	}

	/**
	 * Elimina a un Empleado de la lista de subordinados y actualiza el rango.
	 * @param e el Empleado a eliminar de la lista de subordinados
	 * @return <i>true</i> si se ha eliminado el empleado, <i>false</i>
	 * si no existía en la lista de subordinados del empleado 
	 */
	public boolean removeSubordinado (Controlador c, Empleado e) {
		boolean b = idSubordinados.remove((Integer)e.getEmplId());
		if (b) idSubordinados.remove(e.getEmplId());
		// quitar subordinados afecta a la condición de gerente
		// hay que mirar si todavía tiene algún subordinado jefe
		if (idSubordinados.isEmpty()) rango=1;
		else {
			int i = 0;
			actualizarSubordinados(c);
			while (i<idSubordinados.size() && rango==2) {
				if (subordinados.get(i).getRango()>1) rango=3;
			}
		}
		// si se ha quedado sin subordinados jefes pero tiene departamentos,
		// entonces se queda como jefe
		if (!idDepartamentos.isEmpty() && rango==1) rango=2;
		return b;
	}	
	
	public ImageData getDrawableImage() {
		// TODO
		return null;
	}
	
	public ImageData getPrintableImage(Device device, ResourceBundle bundle, Rectangle size, boolean bn) {
		int w = size.width/2;
		int h = size.height/2;
		Image image = new Image(device, w, h); 
		GC gc = new GC(image);
		int marginLeft = w/10;
		int marginLeft1 = marginLeft + w/20;
		int marginLeft2 = marginLeft + w/2;
		int marginRight = w;
		int marginTop = h/15;
		
		
		Font font =  new Font(device, new FontData("Arial",h/100,SWT.BOLD));
		gc.setFont(font);
		int marginVertical = gc.stringExtent("A").y-1;
		
		gc.setBackground(new Color(device, 50, 127, 192));
		gc.fillRectangle(marginLeft, marginTop, marginRight-marginLeft, marginVertical+marginVertical/5);
		gc.fillRectangle(marginLeft, marginTop, marginVertical/10, h);
		
		gc.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		gc.drawString(String.valueOf(idEmpl), marginLeft1*2, marginTop+marginVertical/10);
		
		marginTop = marginVertical+marginVertical/2;
		gc.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
		gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
		font =  new Font(device, new FontData("Arial",h/275,SWT.NORMAL));
		gc.setFont(font);
		marginVertical = gc.stringExtent("A").y;
		marginVertical += marginVertical/10;
		for (int i = 4; i<=13; i++ ){
			gc.drawLine(marginLeft1, marginTop+i*marginVertical-marginVertical/10, marginLeft2, marginTop+i*marginVertical-marginVertical/10);
		}
		String s;
		if(this.sexo==0){s="Mujer";}
			else s="Hombre";
		gc.drawString(bundle.getString("Nombre"),                  marginLeft1, marginTop+3*marginVertical);
		gc.drawString(nombre,                                      marginLeft2, marginTop+3*marginVertical);
		gc.drawString(bundle.getString("Apellidos"),               marginLeft1, marginTop+4*marginVertical);
		gc.drawString(apellido1 + " " + apellido2,                 marginLeft2, marginTop+4*marginVertical);
		gc.drawString(bundle.getString("EMail"),                   marginLeft1, marginTop+5*marginVertical);
		gc.drawString(email,                                       marginLeft2, marginTop+5*marginVertical);
		gc.drawString(bundle.getString("I08_2_lab_FNacimiento") ,  marginLeft1, marginTop+6*marginVertical);
		gc.drawString(Util.dateAString2(fechaNac),                 marginLeft2, marginTop+6*marginVertical);
		gc.drawString(bundle.getString("Sexo"),                    marginLeft1, marginTop+7*marginVertical);		
		gc.drawString(s,                                           marginLeft2, marginTop+7*marginVertical);
		gc.drawString(bundle.getString("I08_lab_TipoContrato"),    marginLeft1, marginTop+8*marginVertical);
		gc.drawString("",                                          marginLeft2, marginTop+8*marginVertical);
		gc.drawString(bundle.getString("Experiencia"),             marginLeft1, marginTop+9*marginVertical);
		gc.drawString("" ,                                         marginLeft2, marginTop+9*marginVertical);
		gc.drawString(bundle.getString("Departamento") ,           marginLeft1, marginTop+10*marginVertical);
		gc.drawString("",                                          marginLeft2, marginTop+10*marginVertical);
		gc.drawString(bundle.getString("I08_2_lab_FAlta"),         marginLeft1, marginTop+11*marginVertical);
		gc.drawString(Util.dateAString2(fAlta),                    marginLeft2, marginTop+11*marginVertical);
		gc.drawString(bundle.getString("I08_2_lab_FContr"),        marginLeft1, marginTop+12*marginVertical);
		gc.drawString(Util.dateAString2(fContrato),                marginLeft2, marginTop+12*marginVertical);
		Code128.pintarCode(gc, idEmpl + password, marginLeft1,     marginTop+16*marginVertical, marginVertical*4,w-marginLeft*2);
		
		gc.dispose();
		return image.getImageData();
	}
	
	/**
	 * 
	 * @param dia Dia que queremos saber si puede trabajar nuestro empleado.
	 * @param iniH Hora inicial desde la que debe estar disponible nuestro empleado para trabajar.
	 * @param finH Hora final hasta la que debe estar disponible nuestro empleado para trabajar.
	 * @param cont Controlador que nos da acceso a la base de datos.
	 * @return <i>true</i> si el empleado puede trabajar en el periodo solicitado, <i>false</i> si
	 * no puede hacerlo (libra, vacaciones, baja...).
	 */
	public boolean estaDisponible(int dia, Time iniH, Time finH, Controlador cont, ArrayList<Contrato> listaContratos, int hora, int numTrozos){
		
		Contrato contrato;
		String patron;
		ArrayList<String> turnoStr;
		java.util.Date today;
		int diaCiclo,n;
		long difFechas;
		//Turno tur;
		boolean encontrado;
		
		//cálculo del dia en el que nos encontramos dentro del ciclo.
		today = new java.util.Date();
		java.sql.Date fechaActual = new java.sql.Date(today.getTime());
		if(fContrato == null)
			fContrato = new Date(fechaActual.getTime());
		
		//ya va!!!!!!!!!!!
		long milsDia = 24*60*60*1000;
		difFechas = (fechaActual.getTime()+(dia*milsDia))-fContrato.getTime();
		diaCiclo = (int) (difFechas/(milsDia));  		
		
		//Obtencion del contrato del empleado.
		//contrato = cont.getContrato(this.getContratoId());
		encontrado = false;
		n = 0;
		contrato = null;
		while(!encontrado && n<listaContratos.size()){
			contrato = listaContratos.get(n);
			if(this.idContrato == contrato.getNumeroContrato())
				encontrado = true;			
			n++;
		}
		
		if (turnosStr == null && contrato!=null) //tratar el error en caso de que no exista el contrato.
		{
			patron = contrato.getPatron();
			turnosStr = obtenerTurnos(patron);
		}
		diaCiclo = diaCiclo%turnosStr.size();
		
	/*	if((diaCiclo == contrato.getDuracionCiclo()-1) && (hora == numTrozos-1))
				fContrato.setTime(fechaActual.getTime() + ((dia+1)*milsDia));*/
		
		//Obtencion del turno correspondiente a ese dia.
		turnoStr = turnosStr.get(diaCiclo);
		
		//COMPROBAR SI TRABAJA UN DIA	
		if (turnoStr.get(0).equals("d"))
			return false;
		else
			if ((contrato.getTipoContrato()==3) || (contrato.getTipoContrato()==4))
				return true;
		
		//COMPROBAR SI ESTA DE VACACIONES ETC
		//...
		
		//Obtencion del turno a partir de la base de datos.
		if (turnoE == null)
		{
			turnoE = cont.getListaTurnosContrato(this.idEmpl); //ponerlo al principio del alg.
		}
		
		//Obtencion del turno que le corresponde.
		boolean entrado = false;	
		//Es condicion necesaria que entre.
		for(int i=0; i<turnoE.size(); i++)
		{
			for(int j=0; j<turnoStr.size(); j++)
			{
				if(turnoE.get(i).getIdTurno() == Integer.parseInt(turnoStr.get(j)))
				{
					turnoActual = turnoE.get(i);
					if ((turnoActual.getHoraEntrada().getTime() > iniH.getTime()) || (turnoActual.getHoraSalida().getTime() < finH.getTime())){
						return false;
					} else 
					{
						if ((iniH.getTime() >= turnoActual.getHoraDescanso().getTime()) && (iniH.getTime() < Util.calculaFinDescanso(turnoActual.getHoraDescanso(),turnoActual.getTDescanso()).getTime())){
							return false;
						}
					}
					entrado = true;
					break;
				}
			}
		}
		return entrado;
	}
	
	/**
	 * Este método crea un ArrayList a partir del patrón de turnos del empleado
	 * de tal forma que cada elemento del ArrayList es cada uno de los turnos
	 * que le corresponde a cada uno de los días del ciclo.
	 * @param p patron del contrato
	 * @return lista con los turnos correspondientes a cado uno de los días.
	 */
	public ArrayList<ArrayList<String>> obtenerTurnos(String p){
		
		ArrayList<ArrayList<String>> turnos = new ArrayList<ArrayList<String>>();
		
		String tiempo;
		String tipo;
		int k = 0;
		
		for (int i=0; i<p.length(); i++)
		{
			tiempo = "";
			tipo = "";
			while (p.charAt(i) != ':')
			{
				tiempo = tiempo + p.charAt(i); 
				i++;
			}
			i++; 
			while ((i<p.length())&&(p.charAt(i) != '/'))
			{
				tipo = tipo + p.charAt(i);
				i++;
			}
			for(int t=0; t<Integer.parseInt(tiempo); t++)
			{
				turnos.add(k,obtenerTurnosAux(tipo));
				k++;
			}
		}		
		return turnos;
	}
	
	public ArrayList<String> obtenerTurnosAux(String p){
		
		ArrayList<String> turnos = new ArrayList<String>();
		String t;
		
		for (int i=0; i<p.length(); i++)
		{
			t = "";
			while ((i<p.length()) && (p.charAt(i) != ',')
					)
			{
				t = t + p.charAt(i); 
				i++;
			}
			turnos.add(t);
		}
		return turnos;
	}
	
}