package aplicacion;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.*;

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
/**
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
	 * @param idDepartamentos
	 * @param felicidad		Grado de satisfaccion de un usuario con su horario
	 * @param idioma 		Idioma de la aplicacion para el usuario
	 * 
	 */
	public Empleado (Integer idSuperior, int idEmpl, String nombre, String apellido1,
			String apellido2, Date fechaNac, int sexo, String email, String password,
			int grupo, int rango, int contrato, Date fContrato, Date fAlta, Color color,
			ArrayList<String> idDepartamentos, ArrayList<Integer> idSubordinados,int felicidad, int idioma) {
		if (idSuperior==null) this.idSuperior=0;
		else setIdSuperior(idSuperior);
		setEmplId(idEmpl);
		this.nombre		= nombre;
		this.apellido1	= apellido1;
		this.apellido2	= apellido2;
		this.color = color;
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
		this.idDepartamentos = idDepartamentos;
		superior = null;
		//felicidad = 0;//se recupera de la bd muxas veces luego es la felicidad que tenga
		subordinados	= new ArrayList<Empleado>();
		departamentos	= new ArrayList<Departamento>();
		this.felicidad=felicidad;
		this.idioma=idioma;
		

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
	 * Devuelve el identificador de su departamento principal
	 * @return el identificador
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
	
	public ImageData getPrintableImage(Display display, ResourceBundle bundle, boolean bn) {
		Image image = new Image(display, 1500,1000);
		GC gc = new GC(image);
		int marginLeft = 150;
		int marginTop = 100;
		int marginVertical = 30;
		
		gc.setForeground(new Color(display, 0, 0, 200));
		Shell shell = new Shell(display);
		Font font =  shell.getFont();
		shell.dispose();
		gc.setFont(font);
		gc.drawRectangle(0, 0, 1300, 700);
		gc.drawString(bundle.getString("Vendedor")+" : "+ this.idEmpl,                marginLeft, marginTop+1*marginVertical);
		gc.drawString(bundle.getString("Contrasena")+" : "+ this.password,            marginLeft, marginTop+2*marginVertical);
		gc.drawString(bundle.getString("EMail")+" : "+ this.email,                    marginLeft, marginTop+3*marginVertical);
		gc.drawString(bundle.getString("Nombre")+" : "+ this.nombre,                  marginLeft, marginTop+4*marginVertical);
		gc.drawString(bundle.getString("I08_lab_Apellido1")+" : "+ this.apellido1,    marginLeft, marginTop+5*marginVertical);
		gc.drawString(bundle.getString("I08_lab_Apellido2")+" : "+ this.apellido2,    marginLeft, marginTop+6*marginVertical);
		gc.drawString(bundle.getString("I08_2_lab_FNacimiento")+" : "+ this.fechaNac, marginLeft, marginTop+7*marginVertical);
		gc.drawString(bundle.getString("Sexo")+" : "+ this.sexo,                      marginLeft, marginTop+8*marginVertical);
		gc.drawString(bundle.getString("I08_lab_TipoContrato")+" : "+ this.contrato,  marginLeft, marginTop+9*marginVertical);
		gc.drawString(bundle.getString("Experiencia")+" : "+ this.contrato,           marginLeft, marginTop+10*marginVertical);
		gc.drawString(bundle.getString("Departamento")+" : "+ this.departamentos,     marginLeft, marginTop+11*marginVertical);
		gc.drawString(bundle.getString("I08_2_lab_FAlta")+" : "+ this.fAlta,          marginLeft, marginTop+12*marginVertical);
		gc.drawString(bundle.getString("I08_2_lab_FContr")+" : "+ this.fContrato,     marginLeft, marginTop+13*marginVertical);
		gc.drawString(bundle.getString("I08_2_lab_SelColor")+" : "+ this.color,       marginLeft, marginTop+14*marginVertical);
		gc.drawString("Empleado", 100, 100);
		gc.dispose();
		return image.getImageData();
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