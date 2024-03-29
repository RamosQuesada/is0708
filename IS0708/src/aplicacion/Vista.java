package aplicacion;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import algoritmo.Cuadrante;
import algoritmo.Resumen;
import algoritmo.Sugerencia;
import algoritmo.Trabaja;

import aplicacion.datos.Contrato;
import aplicacion.datos.Departamento;
import aplicacion.datos.Empleado;
import aplicacion.datos.Turno;
import aplicacion.mensajeria.Mensaje;

import idiomas.LanguageChanger;

import interfaces.general.ShellLogin;
import interfaces.general.ShellPrincipal;
import interfaces.imagenes.CargadorImagenes;

public class Vista {
	private Controlador controlador;
	private Database db;
	private Shell shell;
	private Display display;
	private ResourceBundle bundle;
	private Locale locale;
	private boolean pidiendoCuadrante=false;
	private ShellLogin login;
	private ShellPrincipal shellPrincipal;
	private boolean alive = true;
	private LanguageChanger l;
	private Thread conector, loader, cacheUploader;
	private boolean cacheCargada = false;
	private int num_men_hoja = 0;
	CargadorImagenes imagenes;
	
	/** Lista de tareas que se están llevando a cabo para mostrar en la barra inferior */
	private ArrayList<Tarea> tareas= new ArrayList<Tarea>();
	
	/**
	 * Caché local: Lista de empleados que trabajan en el mismo departamento que
	 * el usuario actual
	 */
	private ArrayList<Empleado> empleados = new ArrayList<Empleado>();

	/** Caché local: Lista de mensajes entrantes del usuario actual */
	private ArrayList<Mensaje> mensajesEntrantes = new ArrayList<Mensaje>();
	
	/** Caché local: Lista de mensajes salientes del usuario actual */
	private ArrayList<Mensaje> mensajesSalientes = new ArrayList<Mensaje>();

	/** Caché local: Lista de contratos disponibles para este departamento */
	private ArrayList<Contrato> contratos = new ArrayList<Contrato>();

	/** Caché local: Lista de turnos en los contratos de este departamento */
	private ArrayList<Turno> turnos = new ArrayList<Turno>();

	/** Caché local: Lista de departamentos de un jefe */
	private ArrayList<Departamento> departamentosJefe = new ArrayList<Departamento>();
	
	/** Caché local: Cuadrantes del departamento actual */
	private ArrayList<Cuadrante> cuadrantes = new ArrayList<Cuadrante>();
	
	/** Caché local: Sugerencias del departamento actual */
	private ArrayList<Sugerencia> sugerencias = new ArrayList<Sugerencia>();
	
	/** Caché local: Ventas del departamento actual */
	private ArrayList<ArrayList<Object[]>> vector_ventas = new ArrayList<ArrayList<Object[]>>();
	
	
	/**
	 * cola FIFO de inserciones/actualizaciones a realizar en la BD
	 */
	private java.util.Queue<ElementoCache> colaEscritura;
	
	private int INSERTAR  = 0;
	private int MODIFICAR = 1;
	private int ELIMINAR  = 2;
	private class ElementoCache {
		public ArrayList<Object> o; // El objeto a insertar
		public int i;    // INSERTAR, ACTUALIZAR o ELIMINAR
		String tipo;     // El tipo de objeto
		public ElementoCache(Object o, int i, String tipo) {
			this.o= new ArrayList<Object>();
			this.o.add(o);
			this.i=i;
			this.tipo = tipo;
		}
		public ElementoCache(Object o1, Object o2, int i, String tipo) {
			this.o= new ArrayList<Object>();
			this.o.add(o1);
			this.o.add(o2);
			this.i=i;
			this.tipo = tipo;
		}
	}
	public class Tarea {
		public String s;
		public int p;
		public Tarea(String s, int p) {
			this.s = s;
			this.p = p;
		}
		
	}
	
	private void insertCache(Object o, String tipo) {
		colaEscritura.add(new ElementoCache(o, INSERTAR, tipo));
	}
	
	private void insertCache(int id1, int id2, String tipo) {		
		colaEscritura.add(new ElementoCache(id1, id2, INSERTAR, tipo));
	}

	private void modifyCache(Object o, String tipo) {
		colaEscritura.add(new ElementoCache(o, MODIFICAR, tipo));
	}
	
	private void modifyCache(Object o, String fecha, String tipo) {
		colaEscritura.add(new ElementoCache(o, fecha, MODIFICAR, tipo));
	}

	private void deleteCache(Object o, String tipo) {
		colaEscritura.add(new ElementoCache(o, ELIMINAR, tipo));
	}
	
	private class CacheUploaderDaemon extends Thread {
		// TODO usar batches para upload
		public void run() {
			this.setName("Vista.CacheUploaderDaemon");
			int frac = 0;
			int prog = 0; // Progreso de la cola
			while (alive) {
				if (!colaEscritura.isEmpty()) {
					frac = 100/colaEscritura.size();
					prog = 0;
				}
				while (!colaEscritura.isEmpty()) {
					setProgreso("Actualizando base de datos", prog);
					prog+=frac;
					ElementoCache e = colaEscritura.poll();
					if (e.i==INSERTAR) {
						if      (e.tipo.equals("Empleado" ))		insertEmpleadoBD((Empleado) e.o.get(0));
						else if (e.tipo.equals("Cuadrante"))		controlador.insertCuadrante((Cuadrante) e.o.get(0)); 			
						else if (e.tipo.equals("TurnoContrato"))	controlador.insertTurnoPorContrato((Integer)e.o.get(0), (Integer)e.o.get(1));
						else if (e.tipo.equals("Sugerencia"))		controlador.insertSugerencia((Sugerencia)e.o.get(0));
//						else if (e.tipo.equals("Contrato"))			controlador.insertContrato((Contrato) e.o.get(0));
						/*else if (e.tipo.equals("Departamento"))		controlador.insertDepartamento((Departamento)e.o.get(0));
						else if (e.tipo.equals("CrearDepartamento")){ 	controlador.insertDepartamentoUsuario((Integer)e.o.get(2),e.o.get(0).toString()); //tabla DepartamentoUsuario
																		controlador.insertNumerosDepartamento((Integer)e.o.get(1),e.o.get(0).toString()); //tabla NumerosDEPARTAMENTOs
																		controlador.insertDepartamentoPruebas(e.o.get(0).toString(),(Integer)e.o.get(2)); //tabla DEPARTAMENTO
						}*/
						//TODO quitar esto
						else System.err.println("Error en cache - Insertar "+e.tipo+" no existe");
					}
					else if (e.i==ELIMINAR) {
						if      (e.tipo.equals("Contrato"))			controlador.eliminaContrato((Integer) e.o.get(0));
						else if (e.tipo.equals("Turno"))			controlador.eliminaTurno((Integer) e.o.get(0));
						else if (e.tipo.equals("ContratoConTurnos"))	controlador.eliminaContratoConTurnos((Integer) e.o.get(0));
						else if (e.tipo.equals("Empleado"))			controlador.eliminaEmpleado((Integer) e.o.get(0));
						else if (e.tipo.equals("eliminaMesTrabaja")) {
							ArrayList<Object> ar = (ArrayList<Object>)e.o.get(0);
							controlador.eliminaMesTrabaja((Integer)ar.get(0),(Integer)ar.get(1),(Integer)ar.get(2),ar.get(3).toString());
						}else if (e.tipo.equals("eliminaTurnoDeContrato")){
							ArrayList<Object> aux = (ArrayList<Object>)e.o.get(0);
							controlador.eliminaTurnoDeContrato((Integer)aux.get(0),(Integer)aux.get(1));
						}
						//TODO quitar esto
						else System.err.println("Error en cache - Eliminar "+e.tipo+" no existe");
					}
					else if(e.i==MODIFICAR) {
						if      (e.tipo.equals("Contrato"))			controlador.modificarContrato(((Contrato)e.o.get(0)).getNumeroContrato(), ((Contrato)e.o.get(0)).getTurnoInicial(), ((Contrato)e.o.get(0)).getNombreContrato(), ((Contrato)e.o.get(0)).getPatron() , ((Contrato)e.o.get(0)).getDuracionCiclo(), ((Contrato)e.o.get(0)).getSalario(), ((Contrato)e.o.get(0)).getTipoContrato());
						else if (e.tipo.equals("Turno"))			controlador.modificarTurno(((Turno)e.o.get(0)).getIdTurno(), ((Turno)e.o.get(0)).getDescripcion(), ((Turno)e.o.get(0)).getHoraEntrada(), ((Turno)e.o.get(0)).getHoraSalida(), ((Turno)e.o.get(0)).getHoraDescanso(), ((Turno)e.o.get(0)).getTDescanso(),((Turno)e.o.get(0)).getColor());
						else if (e.tipo.equals("Empleado"))			controlador.cambiarEmpleado(((Empleado)e.o.get(0)).getEmplId(), ((Empleado)e.o.get(0)).getNombre(), ((Empleado)e.o.get(0)).getApellido1(), ((Empleado)e.o.get(0)).getApellido2(), ((Empleado)e.o.get(0)).getFechaNac(), ((Empleado)e.o.get(0)).getSexo(), ((Empleado)e.o.get(0)).getEmail(), ((Empleado)e.o.get(0)).getPassword(), ((Empleado)e.o.get(0)).getGrupo(), ((Empleado)e.o.get(0)).getFcontrato(), ((Empleado)e.o.get(0)).getFAlta(), ((Empleado)e.o.get(0)).getFelicidad(), ((Empleado)e.o.get(0)).getIdioma(), ((Empleado)e.o.get(0)).getRango(), ((Empleado)e.o.get(0)).getTurnoFavorito(), ((Empleado)e.o.get(0)).getColor(),((Empleado)e.o.get(0)).getContratoId(), ((Empleado)e.o.get(0)).getPosicion());
						else if (e.tipo.equals("Mensaje"))			controlador.marcarMensaje((Mensaje)e.o.get(0));
						else if (e.tipo.equals("MensajeLeido"))		controlador.setLeido((Mensaje)e.o.get(0));
						//else if (e.tipo.equals("JefeDepartamento"))	controlador.modificaDpto(((Departamento)e.o.get(0)).getNombreDepartamento(), ((Departamento)e.o.get(0)).getJefeDepartamento().getEmplId()); 
						else if (e.tipo.equals("NombreDepartamento")){controlador.cambiaNombreDpto(((ArrayList<String>)e.o.get(0)).get(0),((ArrayList<String>)e.o.get(0)).get(1));}//nombre antiguo,nombre nuevo
//																		controlador.cambiaNombreDepartamentoUsuario(e.o.get(0).toString(),e.o.get(1).toString());
//																		controlador.cambiaNombreNumerosDEPARTAMENTOs(e.o.get(0).toString(),e.o.get(1).toString());
//																		} 
						else if (e.tipo.equals("Trabaja"))			controlador.modificaTrabaja(((Trabaja)e.o.get(0)).getIdEmpl(), ((Trabaja)e.o.get(0)).getIdTurno(), (String)e.o.get(1));
						//TODO quitar esto
						else System.err.println("Error en cache - Modificar "+e.tipo+" no existe");
					}
				}
				setProgreso("Actualizando base de datos", 100);
				try {
					sleep(1000);
				} catch (Exception e) {};
			}
		}
	}
	
	
	/**
	 * Inserta un empleado en la base de datos
	 * @param e el empleado a insertar
	 * @return false si el empleado ya existe
	 */
	public boolean insertEmpleado(Empleado e) {
		if (getEmpleado(e.getEmplId())!=null) return false;
		empleados.add(e);
		insertCache(e, "Empleado");
		return true;
	}
	
	
	/**
	 * Inserta un turno en la base de datos
	 * @param t el turno a insertar
	 * @return el id, -1 si el turno ya existe
	 */
	public int insertTurno(Turno t) {
		// TODO He quitado esta comprobación porque si metes un turno, se le asigna un id nuevo, ¿no? - Dani
//		if (getTurno(t.getIdTurno())!=null) return -1;
		int i = controlador.insertTurno(t);
		t.setIdTurno(i);
		turnos.add(t);
		return i;
	}
	
	/**
	 * Inserta un cuadrante en la base de datos
	 * @param c el cuadrante a insertar
	 * @return false si el cuadrante ya existe
	 */
	public boolean insertCuadrante(Cuadrante c) {
		int i=0;
		while (i<cuadrantes.size()){
			if (cuadrantes.get(i).getMes()==c.getMes() && cuadrantes.get(i).getAnio()==c.getAnio())
				cuadrantes.remove(i);
			else
				i++;
		}
		cuadrantes.add(c);
		insertCache(c, "Cuadrante");
		return true;
	}
	
	/**
	 * Inserta una sugerencia en la base de datos
	 * @param s la sugerencia a insertar
	 */
	public void insertSugerencias (Resumen r){
		Sugerencia sugAux = null;
		for (int i=0;i<r.getSugerencias().length;i++) {
			for (int j=0;j<r.leerDia(i).size();j++) {
				sugAux = r.leerDia(i).get(j);
				sugerencias.add(sugAux);
				insertCache(sugAux, "Sugerencia");
			}
		}
	}
	
	/**
	 * Inserta un contrato en la base de datos
	 * @param c el contrato a insertar
	 * @return el id, o -1 si el contrato ya existe
	 */
	public int insertContrato(Contrato c) {
		if (getContrato(c.getNumeroContrato())!=null) return -1;
//		insertCache(c, "Contrato");
		int i = controlador.insertContrato(c, this.getEmpleadoActual().getDepartamentoId());		
		c.setNumeroContrato(i);
		
		contratos.add(c);
		return i;
	}
	
	/**
	 * Inserta una relación turno-contrato en la base de datos
	 * @param idTurno el turno contenido en el contrato
	 * @param idContrato el contrato que contiene al turno
	 * @return siempre true
	 */
	public boolean insertTurnoPorContrato(int idTurno, int idContrato) {
		insertCache(idTurno, idContrato, "TurnoContrato");
		return true;
	}
	
	/**
	 * Inserta ventas para un empleado y dia determinado
	 * @param idVend Vendedor al que se le insertan las ventas
	 * @param ventas Ventas a insertar
	 * @param fecha Fecha en la que se insertan las ventas
	 * @return <i>true</i> si se ha insertado correctamente
	 */
	public boolean insertVentas (int idVend, float ventas, Date fecha){
		return controlador.insertVentas(idVend, ventas, fecha);
	}
	
	/**
	 * Elimina un contrato de la base de datos
	 * @param idContrato El contrato a eliminar
	 * @return <i>false</i> si el contrato no existe
	 */
	public boolean eliminaContrato(int idContrato) {
		Contrato c = getContrato(idContrato);
		if (c==null) return false;
		//contratos.remove(idContrato);
		contratos.remove(c);
		deleteCache(idContrato, "Contrato");
		return true;
	}
	
	/**
	 * Elimina un contrato con todos sus turnos
	 * @param idContrato El contrato a eliminar
	 * @return <i>false</i> si el contrato no existe
	 */
	public boolean eliminaContratoConTurnos(int idContrato) {
		Contrato c = getContrato(idContrato);
		if (c==null) return false;
//		contratos.remove(idContrato);
		deleteCache(idContrato, "ContratoConTurnos");
		return true;
	}
	
	/**
	 * Elimina un empleado de la base de datos
	 * @param idEmpl
	 * @return
	 */
	public boolean eliminaEmpleado(int idEmpl) {
		if (getEmpleado(idEmpl)==null) return false;
		empleados.remove(getEmpleado(idEmpl));
		deleteCache(idEmpl, "Empleado");
		return true;
	}
	
	/**
	 * Elimina un turno de la base de datos
	 * @param idTurno el turno a eliminar
	 * @return false si el turno no existe
	 */
	public boolean eliminaTurno(int idTurno) {
		if (getTurno(idTurno)==null) return false;
		turnos.remove(getTurno(idTurno));		
		deleteCache(idTurno, "Turno");
		return true;
	}
	/**
	 * Esta funcion elimina un turno de un contrato
	 * @param idTurno
	 * @param idContrato
	 * @return boolean que nos confirma exito ne la operacion
	 */
	public boolean eliminaTurnoDeContrato(int idTurno, int idContrato){
		//modificamos bbdd
		ArrayList<Object> aux=new ArrayList<Object>();
		aux.add(idTurno);
		aux.add(idContrato);
		deleteCache(aux,"eliminaTurnoDeContrato");//borramos de la bbdd
		return true;
		
	}
	
	/**
	 * Elimina un mensaje
	 * @param m Mensaje a borrar
	 * @return <i>true</i> si se ha borrado correctamente
	 */
	public boolean eliminaMensaje(Mensaje m){
		return this.controlador.eliminaMensaje(m);		
	}
	
	/**
	 * Consulta si existen ventas para un empleado y dia determinado
	 * @param idVend Vendedor para el que se buscan ventas
	 * @param fecha Fecha para la que se buscan ventas
	 * @return <i>true</i> si existen ventas para el empleado y fecha
	 */
	public boolean existeVentas (int idVend,Date fecha){
		return this.controlador.existeVentas(idVend, fecha);
	}
	
	/**
	 * Cambia las ventas de un empleado de un dia determinado
	 * @param idVend Vendedor para el que se quieren cambiar las ventas
	 * @param fecha Fecha en la que se quieren cambiar las ventas
	 * @param ventas Nuevas ventas
	 */
	public void cambiarVentas(int idVend, Date fecha, float ventas){
		this.controlador.cambiarVentas(idVend, fecha, ventas);
	}
	
	/**
	 * Modifica un contrato en la base de datos
	 * @param idContrato Contrato a modificar
	 * @param turnoInicial Nuevo turno inicial
	 * @param nombre Nuevo nombre
	 * @param patron Nuevo patron
	 * @param duracionCiclo Nueva duracion del ciclo
	 * @param salario Nuevo salario
	 * @param tipo Nuevo tipo
	 * @return <i>true</i> si se ha modificado correctamente
	 */
	public boolean modificarContrato(int idContrato, int turnoInicial, String nombre, String patron, int duracionCiclo, double salario, int tipo) {
		Contrato c = getContrato(idContrato);
		if (c==null) return false;
		c.setTurnoInicial(turnoInicial);
		c.setNombreContrato(nombre);
		c.setPatron(patron);
		c.setDuracionCiclo(duracionCiclo);
		c.setSalario(salario);
		c.set_tipoContrato(tipo);
		modifyCache(c, "Contrato");
		return true;
	}
	
	/**
	 * Modifica la información de un empleado
	 * @param idEmp Nueva idEmpleado
	 * @param nomb Nuevo nombre
	 * @param Ape1 Nuevo primer apellido
	 * @param Ape2 Nuevo segundo apellido
	 * @param FNac Nueva fecha de nacimiento
	 * @param sexo Nuevo sexo
	 * @param mail Nuevo email
	 * @param Passw Nueva contraseña
	 * @param grupo Nuevo grupo
	 * @param FCont Nueva fecha de contratacion
	 * @param Fentr Nueva fecha de entrada
	 * @param Felic Nueva felicidad
	 * @param Idiom Nuevo idioma
	 * @param Rang Nuevo rango
	 * @param Turn Nuevo turno favorito
	 * @param Contr Nuevo contrato
	 * @return <i>cierto</i> si se ha podido modificar
	 */
	public boolean modificarEmpleado(int idEmp, String nomb, String Ape1, String Ape2, Date FNac, int sexo, 
			String mail, String Passw, int grupo, Date FCont, Date Fentr, int Felic, int Idiom, 
			int Rang, int Turn, int Contr, Color color){
		Empleado e = getEmpleado(idEmp);
		Contrato c = getContrato(Contr);
		
		if (e==null) return false;
		e.setNombre(nomb);
		e.setApellido1(Ape1);
		e.setApellido2(Ape2);
		e.setFechaNac(FNac);
		e.setSexo(sexo);
		e.setEmail(mail);
		e.setPassword(Passw);
		e.setGrupo(grupo);
		e.setFcontrato(FCont);
		e.setFAlta(Fentr);
		e.setFelicidad(Felic);
		e.setIdioma(Idiom);
		e.setRango(Rang);
		if (Contr != e.getContratoId())
			e.setTurnoFavorito(c.getTurnoInicial());
		e.setContrato(c);
		e.setIdContrato(Contr);
		e.setColor(color);
		
		modifyCache(e, "Empleado");
		return true;
	}
	
	/**
	 * Modifica la información de un empleado
	 * @param emp el empleado modificado
	 * @return <i>cierto</i> si se ha podido modificar
	 */
	public boolean modificarEmpleado(Empleado emp){
		Empleado eCache = getEmpleado(emp.getEmplId());
		Contrato c = getContrato(emp.getContratoId());
		
		if (eCache==null) return false;
		eCache.setNombre(emp.getNombre());
		eCache.setApellido1(emp.getApellido1());
		eCache.setApellido2(emp.getApellido2());
		eCache.setFechaNac(emp.getFechaNac());
		eCache.setSexo(emp.getSexo());
		eCache.setEmail(emp.getEmail());
		eCache.setPassword(emp.getPassword());
		eCache.setGrupo(emp.getGrupo());
		eCache.setFcontrato(emp.getFcontrato());
		eCache.setFAlta(emp.getFAlta());
		eCache.setFelicidad(emp.getFelicidad());
		eCache.setIdioma(emp.getIdioma());
		eCache.setRango(emp.getRango());
		if (emp.getContratoId() != eCache.getContratoId())
			eCache.setTurnoFavorito(c.getTurnoInicial());
		eCache.setContrato(c);
		eCache.setIdContrato(emp.getContratoId());
		eCache.setColor(emp.getColor());
		eCache.setPosicion(emp.getPosicion());
		
		modifyCache(eCache, "Empleado");
		return true;
	}
	
	public boolean modificarTrabaja(int idTurno, int idEmpl, int dia, int mes, int anio, String dep) {
		// Coger Trabaja del Cuadrante
		Cuadrante c = getCuadrante(mes, anio, dep);
		Trabaja t = null;
		int i=0; boolean encontrado = false;
		while (!encontrado && i<c.getListaTrabajaDia(dia-1).size()) {	// Coger trabaja correspondiente al empleado
			if (c.getListaTrabajaDia(dia-1).get(i).getIdEmpl()==idEmpl) {
				t = c.getListaTrabajaDia(dia-1).get(i);
				encontrado = true;
			}
			else i++;
		}
		if (t==null) return false;
		
		t.setIdTurno(idTurno);
		modifyCache(t, aplicacion.utilidades.Util.fechaAString(dia, mes, anio), "Trabaja");
		return true;
	}

	/**
	 * Modifica un turno en la base de datos
	 * @param idTurno Turno a modificar
	 * @param descripcion Nueva descripcion
	 * @param horaEntrada Nueva hora de entrada
	 * @param horaSalida Nueva hora de salida
	 * @param horaInicioDescanso Nueva hora inicio descanso
	 * @param duracion Nueva duracion
	 * @return <i>true</i> si se ha modificado correctamente
	 */
	public boolean modificarTurno(int idTurno, String descripcion, Time horaEntrada, Time horaSalida, Time horaInicioDescanso, int duracion, Color color) {
		Turno t = getTurno(idTurno);
		if (t==null) return false;
		t.setDescripcion(descripcion);
		t.setHoraEntrada(new Time(horaEntrada.getTime()));
		t.setHoraSalida(new Time(horaSalida.getTime()));
		t.setHoraDescanso(new Time(horaInicioDescanso.getTime()));
		t.setTDescanso(duracion);
		t.setColor(color);
		modifyCache(t, "Turno");
		return true;
	}
	
	/**
	 * Modifica un cuadrante a partir de un dia
	 * @param primerDia Primer dia a modificar
	 * @param mes Mes del cuadrante a modificar
	 * @param anio Anio del cuadrante a modificar
	 * @param idDepartamento Departamento del cuadrante a modificar
	 * @param c Cuadrante a modificar
	 */
	public void modificarCuadrante (int primerDia, int mes, int anio, String idDepartamento, Cuadrante c) {
		int i = 0;
		boolean encontrado = false;
		while (i<cuadrantes.size() && !encontrado) {
			if (cuadrantes.get(i).getAnio()!=anio || cuadrantes.get(i).getMes()!=mes || !cuadrantes.get(i).getIdDepartamento().equals(idDepartamento)) 
				i++;
			else
				encontrado = true;
		}
		for (int j=primerDia-1;j<c.getNumDias();j++)
			cuadrantes.get(i).getCuad()[j]=c.getCuad()[j];
	}
	
	/**
	 * Modifica sugerencias a partir de un dia
	 * @param primerDia Primer dia a modificar
	 * @param mes Mes de las sugerencias a modificar
	 * @param anio Anio de las sugerencias a modificar
	 * @param idDepartamento Departamento de las sugerencias a modificar
	 * @param r Sugerencias a modificar
	 */
	public void modificarSugerencias (int primerDia, int mes, int anio, String idDepartamento, Resumen r) {
		int aux=0;
		for (int i=0;i<sugerencias.size();i++){
			if (sugerencias.get(i).getDept().equals(idDepartamento) && sugerencias.get(i).getFecha().getMonth()==mes  && sugerencias.get(i).getFecha().getYear()==anio
					&& sugerencias.get(i).getFecha().getDay()>=primerDia-1 && sugerencias.get(i).getFecha().getDay()<r.getDias()) {
				sugerencias.remove(i);
				i--;
			}
		}
		for (int j=primerDia-1;j<r.getSugerencias().length;j++)
			for (int k=0;k<r.leerDia(j).size();k++)
				sugerencias.add(r.leerDia(j).get(k));
	}
 	
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
	public class Loader extends Thread {
		public synchronized void run() {
			this.setName("Loader");
			// Si hay un usuario logueado
			if (controlador.getEmpleadoActual() != null){
				loadCache();
				cacheCargada = true;
				// Se queda consultando los mensajes periódicamente
				while (alive) {
					loadTodosMensajes();
					cacheCargada = true;
					try {
						// TODO Espera 20 segundos (¿cómo lo dejamos?)
						wait(20000);
					} catch (InterruptedException e) {}
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
	 * <li>Inicia la caché y carga los datos
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

		// Crear display y shell
		display = d;
		shell = new Shell(display);

		// Cargar imágenes de la aplicación
		imagenes = new CargadorImagenes(d);

		// Crear gestor de idiomas
		l = new LanguageChanger();
		bundle = l.getBundle();
		locale = l.getCurrentLocale();
		
		// Instanciar cola de escritura de caché
		colaEscritura = new java.util.LinkedList<ElementoCache>();
		
		// Instanciar lista de tareas
		tareas = new ArrayList<Tarea>();
		
		// Lo normal ahora es llamar al método start()
	}

	/**
	 * Intenta realizar el login e inicia la aplicación.
	 */
	public void start() {
		// Iniciar conexión a la base de datos, iniciar caché
		conector = new Thread(new Conector());
		loader = new Thread(new Loader());
		cacheUploader = new CacheUploaderDaemon();
		conector.start();
		cacheUploader.start();

		// Login 
		login();
		// Si no he cerrado el shell, ya he hecho login correctamente
		if (!shell.isDisposed()) {
			shellPrincipal.mostrarVentana();
		}
	}
	
	/**
	 * Este método realiza la identificación del usuario.
	 * @author Daniel Dionne
	 */
	public void login() {
		// Si la ventana de aplicación está abierta, ocultarla
		if (shellPrincipal!=null && !shellPrincipal.getShell().isDisposed()) shellPrincipal.getShell().setVisible(false);
		
		login = new ShellLogin(shell, bundle, db, imagenes);
		boolean identificadoOCancelado = false;
		Empleado emp = null;
		// Intenta identificar al usuario hasta que lo consigue o hasta que se pulsa el botón cancelar
		while (!identificadoOCancelado) {
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
				if (login.getNumeroVendedor() == 0 && login.getPassword().equals("admin")) {
					controlador.setEmpleadoActual(new Empleado(0, 0,
							"Administrador", "", "", null, 0, "", "admin", 0,
							0, 0, null, null, null, null, null, 0, 0, 0, 0));
					identificadoOCancelado = true;
					shellPrincipal = new ShellPrincipal(shell, shell.getDisplay(), bundle, locale, this);
				// Login normal
				} else {
					emp = getEmpleado(login.getNumeroVendedor());
					if (emp != null) {
						// Comprobar la clave
						// Si la clave es correcta:
						// - asignar idioma a la aplicación
						// - empezar a cargar los datos
						if (emp.getPassword().equals(login.getPassword())) {
							controlador.setEmpleadoActual(emp);
							identificadoOCancelado = true;
							// Configurar idioma al del empleado
							l.cambiarLocale(controlador.getEmpleadoActual()
									.getIdioma());
							bundle = l.getBundle();
							locale = l.getCurrentLocale();

							if (!loader.isAlive())
								loader.start();
							shellPrincipal = new ShellPrincipal(shell, shell.getDisplay(), bundle, locale, this);
							
						} else {
							// Si el password no coincide
							// Se muestra un mensaje en caso de que no se haya detectado el lector
							// (ya que si el lector lee mal, es un rollo tener que estar cogiendo el ratón para
							// cerrar la ventana)
							if (!login.detectadoLector()) {
								MessageBox messageBox = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
								messageBox.setText(bundle.getString("Error"));
								messageBox.setMessage(bundle.getString("I01_err_Login2"));
								messageBox.open();
							} else {
								display.beep();
							}
						}
					} else {
						// Si el usuario no existe en la base de datos, mostrar mensaje
						MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
						messageBox.setText(bundle.getString("Error"));
						messageBox.setMessage(bundle.getString("I01_err_Login1"));
						messageBox.open();
					}
				}
//				if (i02!=null) i02.dispose();
//				i02 = new I02_Principal(shell, shell.getDisplay(), bundle, locale, this);
			} else {
				// Se ha pulsado el botón Cancelar o cerrar, por tanto hay que salir de la aplicación
				shell.getDisplay().dispose();
				identificadoOCancelado = true; // Para que salga del bucle
				stop();
			}
		}
	}

	/**
	 * Cierra la aplicación
	 */
	public void stop() {
		try {
			alive = false;
			loader.interrupt();
			if (shellPrincipal!=null) shellPrincipal.dispose();
			display.dispose();
		}
		catch(Exception e) {
			String error = "Vista ::\tError al cerrar la aplicación:\n";
			if (e.getMessage()!=null) error += e.getMessage();
			else e.printStackTrace();
			System.err.println(error);
		};
	}

	/**
	 * Devuelve el display de la aplicación.
	 * @return el display de la aplicación
	 */
	public Display getDisplay() {
		return display;
	}
	
	/**
	 * Devuelve el cargador de imagenes
	 * @return el cargador de imagenes
	 */
	public CargadorImagenes getImagenes() {
		return imagenes;
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
		shellPrincipal.setTextoEstado(estado);
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
	 * 
	 * Establece información de la distribucion de un departamento para un determinado dia. 
	 * @param depart Nombre del departamento
	 * @param tipoDia Tipo de dia (1-7) : (lunes-domingo)
	 * @param datos arraylist donde cada elemento es un vector de tres
	 *        dimensiones de tal forma que vector[0]= Hora vector[1]= numero
	 *        minimo de empleados para esa hora vector[2]= numero maximo de
	 *        mpleados para esa hora
	 */
	public void setDistribucionDiaSemana(String depart, int tipoDia, ArrayList<Object[]> datos){
		controlador.setDistribucionDiaSemana(depart, tipoDia, datos);
	}
	
	/**
	 * Funcion que establece la hora de entrada y salida de un departamento
	 * @param dpto: Id del Dpto.
	 * @param entrada: hora de apertura del departamento
	 * @return salida: hora de cierre del departamento
	 */
	public void setHorarioDpto(String dpto, Time entrada, Time salida) {
		controlador.setHorarioDpto(dpto, entrada, salida);
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
	 * Si no lo encuentra, devuelve <b>null</b>.
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
	
	/**
	 * Consulta un turno
	 * @param idTurno Turno que se consulta
	 * @return Turno consultado
	 */
	public Turno getTurno (int idTurno) {
		// Buscar en cache
		int i = 0;
		while (i<turnos.size()) {
			if (turnos.get(i).getIdTurno()==idTurno) {
				return turnos.get(i);
			}
			i++;
		}
		infoDebug("Vista", "Fallo al coger el turno " + idTurno + " de caché.");
		// Si no, buscar en BD
		Turno t =  controlador.getTurno(idTurno);
		if (t!=null) turnos.add(t);
		return t;
		
	}
	
	/**
	 * Consulta un contrato
	 * @param idContrato Contrato a consultar
	 * @return Contrato consultado
	 */
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
	
	/**
	 * Consulta las sugerencias de un mes
	 * @param mes Mes de las sugerencias a consultar
	 * @param anio Anio de las sugerencias a consultar
	 * @param idDepartamento Departamento de las sugerencias a consultar
	 * @return Cuadrante consultado
	 */
	public ArrayList<Sugerencia> getSugerencias(int mes, int anio, String idDepartamento) {
		if (!alive) return null;
		ArrayList<Sugerencia> aux = new ArrayList<Sugerencia>();
		for (int i=0;i<sugerencias.size();i++) {
			if (sugerencias.get(i).getFecha().getYear()+1900==anio && 
				sugerencias.get(i).getFecha().getMonth()+1==mes && 
				sugerencias.get(i).getDept().equals(idDepartamento))
				aux.add(sugerencias.get(i));	
		}
		if (aux.size() != 0) return aux;
		// Si no, buscar en BD
		for (int i=1;i<=aplicacion.utilidades.Util.dameDias(mes, anio);i++) {
			Date fecha = new Date(anio,mes,i);
			ArrayList<Sugerencia> aux2 = controlador.getSugerenciasDia(idDepartamento, fecha);
			for (int j=0;j<aux2.size();j++)
				sugerencias.add(aux2.get(j));
		}
		return sugerencias;
	}
	
	/**
	 * Consulta un cuadrante
	 * @param mes Mes del cuadrante a consultar
	 * @param anio Anio del cuadrante a consultar
	 * @param idDepartamento Departamento del cuadrante a consultar
	 * @return Cuadrante consultado
	 */
	public Cuadrante getCuadrante(int mes, int anio, String idDepartamento) {
		while (pidiendoCuadrante){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pidiendoCuadrante=true;
		if (!alive) return null;
		//TODO corregir llamadas a este metodo con idDepartamento = ""
		String idDpto = idDepartamento;
		if (idDpto.equals(""))
			idDpto = getEmpleadoActual().getDepartamentoId();
		int i = 0;
		while (i<cuadrantes.size()) {

			if ((cuadrantes.get(i).getAnio()==anio) && (cuadrantes.get(i).getMes()==mes) && ((cuadrantes.get(i).getIdDepartamento()).compareTo(idDpto)==0)){
				pidiendoCuadrante=false;
				return cuadrantes.get(i);
				
			}
				
			i++;
		}
		// Si no, buscar en BD
		Cuadrante c = controlador.getCuadrante(mes, anio, idDpto);
		cuadrantes.add(c);
		getSugerencias(mes, anio, idDpto);
		pidiendoCuadrante=false;
		return c;
	}
	
	/**
	 * Elimina un cuadrante de la base de datos
	 * @param mes Mes del cuadrante a eliminar
	 * @param anio Anio del cuadrante a eliminar
	 * @param idDepartamento Departamento del cuadrante a eliminar
	 */
	public void eliminaCuadrante(int mes, int anio, String idDepartamento) {
		if (!alive) return;
		int i = 0;
		while (i<cuadrantes.size()) {
			if (cuadrantes.get(i).getIdDepartamento().equals(idDepartamento) && cuadrantes.get(i).getMes()==mes && cuadrantes.get(i).getAnio()==anio) {
				cuadrantes.remove(i);
			}
			else
				i++;
		}
	}
	
	/**
	 * Elimina las sugerencias de un mes de la base de datos
	 * @param mes Mes de las sugerencias a eliminar
	 * @param anio Anio de las sugerencias a eliminar
	 * @param idDepartamento Departamento de las sugerencias a eliminar
	 */
	public void eliminaSugerencias(int mes, int anio, String idDepartamento) {
		if (!alive) return;
		int i = 0;
		while (i<sugerencias.size()) {
			if (sugerencias.get(i).getDept().equals(idDepartamento) && sugerencias.get(i).getFecha().getMonth()==mes && sugerencias.get(i).getFecha().getYear()==anio) {
				sugerencias.remove(i);
			}
			else
				i++;
		}
	}
	
	/**
	 * Consulta la lista de trabaja para un departamento y dia determinado
	 * @param dia Dia a consultar
	 * @param mes Mes a consultar
	 * @param anio Anio a consultar
	 * @param idDepartamento Departamento a consultar
	 * @return Lista con los turnos que cumplen los empleados que trabajan
	 */
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
	 * Esta función devuele un arraylist con los turnos de un contrato especifico
	 * @param idContrato
	 * @return ArrayList con los turnos de ese contratos
	 */
	public ArrayList<Turno> getTurnosDeUnContrato(int idContrato){
		
		int i=0;
		boolean encontrado=false;
		ArrayList<Turno> turnosContrato=new ArrayList<Turno>();
		ArrayList<Integer> numTurnos=new ArrayList<Integer>();
		while(i<contratos.size()&&!encontrado){//miramos en la cache
			if(contratos.get(i).getNumeroContrato()==idContrato){
				encontrado=true;
			}else{
				i++;
			}
		}
		if(encontrado){//si esta en la cache
			numTurnos=contratos.get(i).getNumTurnosContrato();
			for(int j=0;j<numTurnos.size();j++){
				turnosContrato.add(getTurno(numTurnos.get(j)));
			}
			return turnosContrato;
		}else{//si no esta devolvemos null, aunk deberiamos generar error
			return null;
		}
		
	}
	
	/**
	 * Consulta los turnos cargados en cache
	 * @return los turnos cargados de la base de datos
	 */
	public ArrayList<Turno> getTurnos(){
		return turnos;
	}
	/**
	 * Funcion que devuelve un arrayList donde se especifican las ventas del año actual
	 * @return ArrayList<ArrayList<Object[]>>
	 * 		   Cada arraylist dentro del arraylist global representa un mes.
	 * 		   Para cada mes hay un array de 2 elementos:
	 * 				- Array[0] contiene la fecha
	 * 				- Array[1] contiene las ventas en esa fecha
	 */
	public ArrayList<ArrayList<Object[]>> getVentas(){
		return vector_ventas;
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
	 * Carga los mensajes de la base de datos
	 */
	public void loadTodosMensajes() {
		// Carga mensajes
		infoDebug("Vista", "Cargando mensajes");
		mensajesEntrantes = getTodosMensajesEntrantes(getEmpleadoActual().getEmplId());
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
	/*public ArrayList<Mensaje> getMensajesEntrantes() {
		if (mensajesEntrantes == null) {
			loadMensajes();
		}
		return mensajesEntrantes;
	}*/
	
	/**
	 * Devuelve la lista COMPLETA de mensajes que ha recibido el empleado actual. La
	 * carga si esta no se ha cargado todavía.
	 * 
	 * @return la lista de empleados
	 */
	public ArrayList<Mensaje> getTodosMensajesEntrantes() {
		if (mensajesEntrantes == null) {
			loadTodosMensajes();
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
		
		ArrayList<Empleado> datos = (ArrayList<Empleado>) empleados.clone();
		ArrayList<Empleado> sol = new ArrayList<Empleado>();
		
		if (idEmpl != null)
			for (int i=0; i<datos.size(); i++) {
				Empleado e = datos.get(i);
				
				if (e.getEmplId() == idEmpl) {
						sol.add(e);						
				}
					 
			}
		
		if (idDpto != null)
			for (int i=0; i<datos.size(); i++) {
				Empleado e = datos.get(i);
				
				if (e.getDepartamentoId() == idDpto) {
					sol.add(e);							
				}
					 
			}
		
		if (idContrato != null)
			for (int i=0; i<datos.size(); i++) {
				Empleado e = datos.get(i);
				
				if (e.getContratoId() == idContrato) {
					sol.add(e);							
				}
					 
			}
		
		if (nombre != null)
			for (int i=0; i<datos.size(); i++) {
				Empleado e = datos.get(i);
				
				if (e.getNombre() == nombre) {
					sol.add(e);							
				}
					 
			}
		
		if (apellido1 != null)
			for (int i=0; i<datos.size(); i++) {
				Empleado e = datos.get(i);
				
				if (e.getApellido1() == apellido1) {
					sol.add(e);							
				}
					 
			}
		
		if (apellido2 != null)
			for (int i=0; i<datos.size(); i++) {
				Empleado e = datos.get(i);
				
				if (e.getApellido2() == apellido2) {
					sol.add(e);							
				}
					 
			}
		
		if (rango != null)
			for (int i=0; i<datos.size(); i++) {
				Empleado e = datos.get(i);
				
				if (e.getRango() == rango) {
					sol.add(e);							
				}
					 
			}
		
		if (sol.size() > 0)
			return sol;
		else
			return controlador.getEmpleados(idEmpl, idDpto, idContrato, nombre,
				apellido1, apellido2, rango);
		
		//TODO
	}

	/**
	 * Guarda un empleado.
	 * 
	 * @param empleado
	 *            el empleado a guardar
	 * @return <i>true</i> si el empleado ha sido guardado
	 */
	private boolean insertEmpleadoBD(Empleado empleado) {
		boolean b = controlador.insertEmpleado(empleado);
		int i = 0;
		while (i < empleado.getDepartamentosId().size() && b) {
			b = controlador.insertDepartamentoUsuario(empleado.getEmplId(),
					empleado.getDepartamentoId(i));
			i++;
		}
		return b;
	}

	/**
	 * Consulta los empleados de un departamento
	 * @param idDept
	 * @return Lista de empleados del departamento
	 */
	public ArrayList<Empleado> getEmpleadosDepartamento(String idDept) {
		ArrayList<Empleado> e = new ArrayList<Empleado>();
		for (int i=0; i<empleados.size(); i++) {
			if (empleados.get(i).getDepartamentoId().equals(idDept))
				e.add(empleados.get(i));
		}
		if (e.size()>0) return e;
		return controlador.getEmpleadosDepartamento(getEmpleadoActual().getEmplId(),idDept);
	}
	
	/**
	 * Funcion que mira si un empleado trabaja un determinado dia.
	 * @param nv Numero de vendedor.
	 * @param d Dia
	 * @return Turno que trabaja, si no trabaja, null.
	 */
	@SuppressWarnings("deprecation")
	public Turno dimeSitrabajaEmpleadoDia(int nv, Date d) {
		Cuadrante cuad = getCuadrante(d.getMonth()+1, d.getYear()+1900, getEmpleadoActual().getDepartamentoId());
		if (cuad != null){
			ArrayList<Trabaja> dia = cuad.getListaTrabajaDia(d.getDate());
			for (int j=0; j<dia.size(); j++){
				if (dia.get(j).getIdEmpl() == nv){
					return getTurno(dia.get(j).getIdTurno());
				}
			}
		}
		return null;
	}
	/**
	 * Funcion que mira si un empleado trabaja un determinado dia.
	 * @param nv Numero de vendedor.
	 * @param d Dia
	 * @return Turno que trabaja, si no trabaja, null.
	 */
	@SuppressWarnings("deprecation")
	public Turno trabajaEmpleadoDia(int nv, Date d) {
		Cuadrante cuad = getCuadrante(d.getMonth()+1, d.getYear()+1900, getEmpleadoActual().getDepartamentoId());
		if (cuad != null){
			ArrayList<Trabaja> dia = cuad.getListaTrabajaDia(d.getDate()-1);
			for (int j=0; j<dia.size(); j++){
				if (dia.get(j).getIdEmpl() == nv){
					return getTurno(dia.get(j).getIdTurno());
				}
			}
		}
		return null;
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
    	/*String[] array=new String[departamentosJefe.size()];
        for(int i=0;i<departamentosJefe.size();i++){
        	array[i]=departamentosJefe.get(i).getNombreDepartamento();
        }
        return array;*/
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
		ArrayList<Mensaje> array = controlador.getMensajesEntrantes(idEmpl,a,b);
		return array;
		/*if(mensajesEntrantes.size()==0){//si esta vacio, rellenamos cache
			ArrayList<Mensaje> array = controlador.getMensajesEntrantes(idEmpl,a,b);
			for(int i=0;i<array.size();i++){
				mensajesEntrantes.add(array.get(i));
			}
			return mensajesEntrantes;
		}else{
			return mensajesEntrantes;
		}*/
		
	}
    
	/**
	 * Obtiene una lista de <i>b</i> mensajes entrantes por orden cronológico,
	 * del más nuevo al más antiguo, empezando desde el mensaje <i>a</i>.
	 * 
	 * @param idEmpl
	 *            el empleado destinatario de los mensajes
	 * @return
	 */
	public ArrayList<Mensaje> getTodosMensajesEntrantes(int idEmpl) {
		// Esto no funciona, y no sé por qué
		// setProgreso("Cargando mensajes", 50);
		ArrayList<Mensaje> array = controlador.getTodosMensajesEntrantes(idEmpl);
		return array;
		/*if(mensajesEntrantes.size()==0){//si esta vacio, rellenamos cache
			ArrayList<Mensaje> array = controlador.getMensajesEntrantes(idEmpl,a,b);
			for(int i=0;i<array.size();i++){
				mensajesEntrantes.add(array.get(i));
			}
			return mensajesEntrantes;
		}else{
			return mensajesEntrantes;
		}*/
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
		/*if(mensajesSalientes.size()==0){//si esta vacio, rellenamos cache
			ArrayList<Mensaje> array = controlador.getMensajesSalientes(idEmpl, a, b);
			for(int i=0;i<array.size();i++){
				mensajesEntrantes.add(array.get(i));
			}
			return mensajesEntrantes;
		}else{
			return mensajesEntrantes;
		}*/ //no se si estara bien asi
	}

	/**
	 * Invierte el marcado del mensaje
	 * 
	 * @param mensaje
	 *            el mensaje a marcar/desmarcar
	 * @return <i>true</i> si el marcado del mensaje se ha invertido correctamente
	 */
	public boolean marcarMensaje(Mensaje mensaje) {
		modifyCache(mensaje, "Mensaje");
		return true;
	}
	
	/**
	 * Pone un mensaje como visto
	 * 
	 * @param mensaje
	 *            el mensaje a marcar como visto
	 * @return <i>true</i> si el marcado del mensaje se ha visto correctamente
	 */
	public boolean marcarMensajeVisto(Mensaje mensaje) {
		modifyCache(mensaje, "MensajeLeido");
		return true;
	}


		
	
	
	/***************************************************************************
	 * Otros métodos
	 */
//	
	/**
	 * @param dia
	 */
	public void eliminaMesTrabaja(int dia, int mes, int anio, String departamento) {
//		controlador.eliminaMesTrabaja(dia, mes, anio, departamento);
		//borramos de la cache
		boolean encontrado=false;
		int i=0;
		while (i<cuadrantes.size() && !encontrado){
			Cuadrante cu = cuadrantes.get(i);
			if (cu.getIdDepartamento().equals(departamento) && cu.getMes()==mes && cu.getAnio()==anio){
				if (dia == 1)
					cuadrantes.remove(i);
				else 
					cuadrantes.get(i).eliminaTrabajaDesdeDia(dia);
				encontrado=true;
			}
			i++;
		}
		ArrayList<Object> aux=new ArrayList<Object>();
		aux.add(dia);
		aux.add(mes);
		aux.add(anio);
		aux.add(departamento);
		deleteCache(aux,"eliminaMesTrabaja");
	}
	
	/**
	 * Ajusta la barra de progreso de la ventana principal al valor del
	 * parámetro, y la hace desaparecer si ha terminado.
	 * 
	 * @param i
	 *            un valor de 0 a 99, ó 100 para que desaparezca.
	 */
	public void setProgreso(String s, int prog) {
		// Si la tarea no está, se añade
		// Si está, se actualiza su progreso
		// El progreso mostrado es el promedio de todas
		int p = 0;
		boolean encontrada = false;
		// Si hay tareas
		if (tareas.size()>0) {
			for (int i=0; i<tareas.size(); i++) {
				if (tareas.get(i).s.equals(s)) {
					tareas.get(i).p = prog;
					encontrada = true;
				}
				// Si el progreso es 100, se elimina
				if (prog>=100) tareas.remove(i);
				// Si no, se suma su progreso para calcular la media
				else p+= tareas.get(i).p;
			}
			p/=tareas.size();
			// Si no se encuentra, y el progreso es menor que 100,
			// se añade
			if (!encontrada && prog<100) tareas.add(new Tarea(s,prog));
			// Muestra la última en la lista
			if (shellPrincipal!=null) shellPrincipal.setProgreso(tareas.get(tareas.size()).s, p);
		} else {
		// Si no hay tareas
			if (shellPrincipal!=null) shellPrincipal.setProgreso("", 100);
		}
		
//		if (shellPrincipal!=null) shellPrincipal.setProgreso(s, prog);
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
		int rango = getEmpleadoActual().getRango();
		ArrayList<String> idsDptos = getEmpleadoActual().getDepartamentosId();
		
		// Para todos los departamentos a los que pertenezca el empleado
		for (int nd=0; nd<idsDptos.size(); nd++){
			String dep = idsDptos.get(nd);
			int numvendedor = getEmpleadoActual().getEmplId();

			if (!alive) return;
			setProgreso("Cargando contratos dpto "+dep, 50);
			contratos = controlador.getListaContratosDpto(dep);
			setProgreso("Cargando contratos dpto "+dep, 100);

			if (!alive) return;
			setProgreso("Cargando empleados dpto "+dep, 25);
			empleados = controlador.getEmpleadosDepartamento(getEmpleadoActual().getEmplId(),dep);
			setProgreso("Cargando empleados dpto "+dep, 100);
			
			//Prueba ordenación empleados
//			for (int i = 0; i < empleados.size(); i++) {
//				System.out.println(empleados.get(i).getPosicion());
//			}
			ordenaEmpleados();
//			for (int i = 0; i < empleados.size(); i++) {
//				System.out.println(empleados.get(i).getPosicion());
//			}
			//Fin PRueba
			
			
			if (!alive) return;
			if (rango == 1) { // Si es un empleado, coger turnos de su departamento
				setProgreso("Cargando turnos dpto "+dep, 70);
				turnos = controlador.getListaTurnosEmpleadosDpto(dep);
				setProgreso("Cargando turnos dpto "+dep, 100);
			} else if (rango == 2) { // Si es un jefe, coger turnos de todos los departamentos
				ArrayList<String> temp = new ArrayList<String>();
				temp = controlador.getDepartamentosJefe(numvendedor);
				for (int i=0; i<temp.size(); i++)
					departamentosJefe.add(controlador.getDepartamento(temp.get(i)));
				//TODO borrar si al final no se usa
	//			setProgreso("Cargando jefes de departamento", 60);
	//			numeroJefesDepartamento = controlador.getNumVendedorTodosJefes();
	//			nombreJefesDepartamento = controlador.getNombreTodosJefes();
				setProgreso("Cargando turnos dpto "+dep, 70);
				ArrayList<Turno> turnosDep = new ArrayList<Turno>();
				for (int i=0; i<departamentosJefe.size(); i++) {
					turnosDep = controlador.getListaTurnosEmpleadosDpto(departamentosJefe.get(i).getNombreDepartamento());
					for (int j=0; j<turnosDep.size(); j++) {
						turnos.add(turnosDep.get(j));
					}				
				}
				setProgreso("Cargando turnos dpto "+dep, 100);
				
			} else {
				System.err.println("Vista\t:: Tipo de empleado inválido para cargar la cache.");
			}
		}
		if(rango==1){//si es un empleado guardamos las ventas que éste ha hecho en un año
			java.util.Date fecha = new java.util.Date();//cogemos la fecha del sistema
			vector_ventas=controlador.getVentas(this.getEmpleadoActual().getEmplId(),fecha.getYear()+1900);
		}else if(rango==2){//si es un jefe almacenamos la suma de las ventas de todos los empleados de este departamento durante un año
			java.util.Date fecha = new java.util.Date();//cogemos la fecha del sistema
			vector_ventas=controlador.getVentasJefe(this.empleados,fecha.getYear()+1900);
		}
		System.out.println("Cache cargada");
	}

	/**
	 * Cambia el nombre de un departamento
	 * @param NombreAntiguo Nombre antiguo
	 * @param NombreNuevo Nombre nuevo
	 */
	public void cambiarNombreDepartamento(String NombreAntiguo, String NombreNuevo) {
		// TODO Auto-generated method stub
//se modifica el nombre del Dpto. en las 3 tablas de Departamentos
		
//		this.controlador.cambiaNombreDpto(NombreAntiguo, NombreNuevo);
//		//this.controlador.cambiaNombreDepartamentoUsuario(NombreAntiguo, NombreNuevo);
//		//this.controlador.cambiaNombreNumerosDEPARTAMENTOs(NombreAntiguo, NombreNuevo);
		boolean esta=false;
		ArrayList <String> aux=new ArrayList<String>();//Aqui meto los dos nombres que necesito, de esta forma no hace falta 
		aux.add(NombreAntiguo);
		aux.add(NombreNuevo);
		int i=0;
		while (i<departamentosJefe.size() && !esta){
			if (departamentosJefe.get(i).getNombreDepartamento().equals(NombreAntiguo)){
				departamentosJefe.get(i).setNombreDepartamento(NombreNuevo);
				modifyCache(aux,"NombreDepartamento");
				return;
			}
			i++;
		}
		if (getEmpleadoActual().getRango() == 0){
			controlador.cambiaNombreDpto(NombreAntiguo, NombreNuevo);
		}
	}
	
	/**
	 * @return Nombres de todos los departamentos
	 */
	public ArrayList<String> getNombreTodosDepartamentos() {
		return this.controlador.getNombreTodosDepartamentos();
		/*leemos la cache, preguntar si leer los que tenemos en cache o miramos por otro lado
		 * ArrayList <String> aux=new ArrayList<String>();
		for(int i=0;i<departamentosJefe.size();i++){//no se si mirar en departamento jefe o meter otro arraylist
			aux.add(departamentosJefe.get(i).getNombreDepartamento());		
		}
		return aux;*/
	}

	/**
	 * Consulta los contrato del departamento actual (cargados en cache)
	 * @return Lista de contratos del departamento
	 */
	public ArrayList<Contrato> getListaContratosDepartamento() {
		return contratos;
	}
	
	/**
	 * Funcion que devuelve todos los nombres completos (nombre+apellidos)
	 *  de los empleados que pueden ser jefes de departamento y su numero de vendedor
	 * @return Nombres completos de los jefes y sus numeros de vendedores
	 */
	public ArrayList<String> getNombreTodosJefes() {
		return this.controlador.getNombreTodosJefes();
//		return nombreJefesDepartamento;
	}

	/**
	 * @return numero de vendedores de todos los jefes
	 */
	public ArrayList<Integer> getNumVendedorTodosJefes() {
		return this.controlador.getNumVendedorTodosJefes();
//		return numeroJefesDepartamento;
	}	
	
	/**
	 * Funcion que incluye en la BBDD un nuevo departamento
	 * @param nombredep Nombre del departamento
	 * @param num Numero de departamento
	 * @param minCi  minutos cierre
	 * @param horCi  hora Cierre
	 * @param minIn  minuto apertura
	 * @param horIn  hora apertura
	 * @param nomJefe nombre del Jefe de departamento
	 */
	public void crearDepartamento(String nombredep, String num, int nvJefe, int horIn, int minIn, int horCi, int minCi) {
		// TODO Auto-generated method stub by carlos Sánchez
		//Agustin deberia devolverme algo q me indique si hay un fallo alcrearlo cual es
		//hazlo como mas facil te sea.Yo no se cuantos distintos puede haber.
	
	//para añadir el Dpto en la BBDD no se necesita el Nombre del Jefe
	//pero si su numero de vendedor (por eso lo he puesto como int) que forma parte de la PK 
		
		int n= Integer.parseInt(num);
		String horaapertura=aplicacion.utilidades.Util.horaminutosAString(horIn, minIn);
		String horacierre=aplicacion.utilidades.Util.horaminutosAString(horCi, minCi);
		Time thI= Time.valueOf(horaapertura);
		Time thC=Time.valueOf(horacierre);
		this.controlador.insertDepartamentoPruebas(nombredep, nvJefe,thI,thC); //tabla DEPARTAMENTO
		this.controlador.insertNumerosDepartamento(n, nombredep); //tabla NumerosDEPARTAMENTOs
		this.controlador.insertDepartamentoUsuario(nvJefe, nombredep); //tabla DepartamentoUsuario

		//insertamos en la cache
		//Departamento d=new Departamento(nombredep,Integer.parseInt(num),getEmpleado(nvJefe),null,null);
		//departamentosJefe.add(d);
		/*ArrayList<Object> aux=new ArrayList<Object>();
		aux.add(nombredep);
		aux.add(n);
		aux.add(nvJefe);
		insertCache(aux,"crearDepartamento");*///no quitar el parseInt
	}
	
	/**
	 * Función que nos dice si ya existe ese nombre de departamento
	 * @param nombre nombre a comparar
	 * @return
	 */
	public boolean existeNombreDepartamento(String nombre) {
//		ArrayList<String> departamentos = this.getNombreTodosDepartamentos();
//		return departamentos.contains(text);
		//miramos en la cache
		int i = 0;
		while (i<departamentosJefe.size()) {
			if (departamentosJefe.get(i).getNombreDepartamento()==nombre){
				return true;
			}
			i++;
		}
		//si no esta miramos en la BD
		Departamento d = controlador.getDepartamento(nombre);
		if (d == null)
			return false;
		//departamentosJefe.add(d);
		return true;
	}
	
	/**
	 * Función que nos dice si ya existe ese número de departamento
	 * @param text Numero a comparar
	 * @return
	 */
	public boolean existeNumDepartamento(String text) {
		
		int nd = Integer.valueOf(text);
		if (getEmpleadoActual().getRango() == 2){
			//miramos en la cache
			int i = 0;
			while (i<departamentosJefe.size()) {
				for (int j=0; j<departamentosJefe.get(i).getNumerosDepartamento().size();j++){
					if (departamentosJefe.get(i).getNumerosDepartamento().get(j)==nd){
						return true;
					}
				}
				i++;
			}
			
		}
		//Si no es jefe o si no esta miramos en la BD
		return controlador.existeNumDepartamento(nd);

	}
	
	/**
	 * Consulta si existe un cuadrante en la cache
	 * @param mes Mes del cuadrante
	 * @param anio Anio del cuadrante
	 * @param idDepartamento Departamento del cuadrante
	 * @return <i>true</i> si existe
	 */
	public boolean existeCuadranteCache (int mes, int anio, String idDepartamento) {
		int i = 0;
		while (i<cuadrantes.size()) {
			if (cuadrantes.get(i).getAnio()==anio && cuadrantes.get(i).getMes()==mes && cuadrantes.get(i).getIdDepartamento().equals(idDepartamento)) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	/**
	 * Función que cambia el jefe de un departamento
	 * @param text nombre del departamento
	 * @param numjefe numero del nuevo jefe
	 */
	public boolean cambiarJefeDepartamento(String text, String numjefe) {
		// TODO Auto-generated method stub by Carlos Sanchez
		return this.controlador.modificaDpto(text, Integer.valueOf(numjefe));
		//buscamos en la cache
		/*Departamento d=this.getDepartamento(text);
		for(int i=0;i<departamentosJefe.size();i++){
			if(departamentosJefe.get(i).getNombreDepartamento()==text){
				departamentosJefe.get(i).set_JefeDepartamento(Integer.valueOf(numjefe));
				d.set_JefeDepartamento(Integer.valueOf(numjefe));
				modifyCache(d,"JefeDepartamento");
				return true;
			}
		}
		//si no esta
		d.set_JefeDepartamento(Integer.valueOf(numjefe));//modifico el jefe del departamento,()
		departamentosJefe.add(d);
		modifyCache(d,"JefeDepartamento");
		return true; //poner la funcion a void*/
	}
	
	/**
	 * Función que devuelve Info de un Dpto. (empleados y horario del dia actual)
	 * @param dpto nombre del departamento
	 * @return Info de "dpto" (empleados y horario del dia actual)
	 */	
	public String infoDpto(String dpto) {
		String info = "Horarios Empleados:"+"\n";
		for (int i=0; i<empleados.size(); i++){
			Turno t = trabajaEmpleadoDia(empleados.get(i).getEmplId(), getFechaActual());
			String nombreempleado = empleados.get(i).getNombreCompleto();
			boolean b=controlador.trabajaEmpleadoDia(empleados.get(i).getEmplId(), this.getFechaActual());
			if (t != null && b){
				String horaentrada = t.getHoraEntrada().toString();
				String horasalida = t.getHoraSalida().toString();
				if (horaentrada!=null || horasalida!=null)
					info += "\n"+ nombreempleado+ ": " + horaentrada+ " - "+ horasalida;
				else
				    info += "\n"+ nombreempleado+ ": No tiene turnos asignados";
			}
			else
				info += "\n"+ nombreempleado+ ": No tiene turnos asignados";
		}
		return info;
	}
	
	/**
	 * Función que devuelve Info de la distribucion de un Dpto dado un dia de la semana
	 * @param dpto nombre del departamento
	 * @param diaSemana entero que representa dia de la semana
	 * 		0->Domingo, 1->Lunes,...,6->Sabado
	 * @return Info de la distribucion de "dpto" (numeros
	 * 	maximos y minimos de empleados por franjas horarias) en "diaSemana"
	 */	
    /* TODO no se usa. Solo imprime por pantalla!!! */
	public void infoDistribucionDpto(String dpto, int diaSemana) {
			ArrayList<Integer[]> a = new ArrayList<Integer[]>();
			a=this.controlador.getInfoDistribucionDpto(dpto, diaSemana);
			for(int i=0; i<a.size();i++){
				Integer[] vector=a.get(i);
				int hora=vector[0];
				int nummin=vector[1];
				int nummax=vector[2];
				String Shora=aplicacion.utilidades.Util.horaAString(hora);
				String SdiaSemana = aplicacion.utilidades.Util.intADiaSemana(diaSemana);
				System.out.print("Distribucion para el " + SdiaSemana);
				System.out.println();
				System.out.println();
				System.out.println(Shora+" : "+nummin+" (numero minimo de empleados) / "
						+ nummax+" (numero maximo de empleados)");
			}
   
	}
	
	/**
	 * Funcion que dado un objeto empleado te devuelve de que departamento es jefe
	 * @param empleado
	 * @return
	 */
	public ArrayList<String> getNombreDepartamentosJefe(Empleado empleado) {
		ArrayList<String> dptos = new ArrayList<String>();
		if (getEmpleadoActual().getRango() == 2) // Solo para jefes
			for (int i=0; i<departamentosJefe.size(); i++)
				dptos.add(departamentosJefe.get(i).getNombreDepartamento());
		return dptos;
	}
	
	/**
	 * Funcion que devuelve true si tiene algun empleado el departamento
	 * @param text
	 * @return
	 */
	public boolean tieneEmpleados(String text) {
		// TODO Auto-generated method stub
		//Se usa para ver cuando se puede eliminar un Dpto.
		//esto es, cuando solo queda su jefe (que siempre
		//está ahí)
		return this.getEmpleadosDepartamento(text).size()>1;
	}
	
	/**
	 * Función que elimina un departamento de las
	 * 3 tablas de departamentos
	 * @param text Nombre del departamento
	 */
	public boolean eliminaDepartamento(String text) {
		
		if (!this.tieneEmpleados(text)){
		return this.controlador.eliminaDepartamento(text);
		}
		return false;
	}
	
	/**
	 * Devuelve información de la distribucion de un departamento para un determinado dia. 
	 * @param depart Nombre del departamento
	 * @param tipoDia Tipo de dia (1-7) : (lunes-domingo)
	 * @return devuelve un arraylist donde cada elemento es un vector de tres
	 *         dimensiones de tal forma que vector[0]= Hora vector[1]= numero
	 *         minimo de empleados para esa hora vector[2]= numero maximo de
	 *         empleados para esa hora
	 */
	public ArrayList<Object[]> getDistribucionDepartamentoDiaSemana(String depart, int tipoDia){
		return controlador.getDistribucionDiaSemana(depart, tipoDia);
	}	
	 
	/**
	 * Funcion que dado un Dpto, te devuelve 
	 * un ArrayList de dos componentes en la que
	 * la primera(horas.get(0)) es la hora de apertura
	 * y la segunda(horas.get(1)) la hora de cierre
	 * @param dpto: Id del Dpto.
	 * @return ArrayList<String>: [0: horaApertura, 1: horaCierre]
	 */
	public ArrayList<String> getHorarioDpto(String dpto) {
		ArrayList<String> horas;
		if (getEmpleadoActual().getRango()==2){
			for (int i=0; i<departamentosJefe.size(); i++)
				if (departamentosJefe.get(i).getNombreDepartamento().equals(dpto)){
					horas = new ArrayList<String>();
					horas.add(departamentosJefe.get(i).getHoraApertura().toString());
					horas.add(departamentosJefe.get(i).getHoraCierre().toString());
					return horas;
				}
		}
		horas = this.controlador.getHorarioDpto(dpto);
		return horas;
	}

	/**
	 * Marca como leido un mensaje
	 * @param m Mensaje
	 * @return <i>true</i> si se ha marcado correctamente
	 */
	public boolean setLeido(Mensaje m) {
		modifyCache(m, "MensajeLeido");
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public int getNum_men_hoja() {
		return num_men_hoja;
	}

	/**
	 * 
	 * @param num_men_hoja
	 */
	public void setNum_men_hoja(int num_men_hoja) {
		this.num_men_hoja = num_men_hoja;
	}
	
	/**
	 * Funcion que cambia el horario de un departamento
	 * @param i horaInicio
	 * @param j minutoInicio
	 * @param k horaCierre
	 * @param m	minutoCierre	
	 * @param nombre nombre departamento
	 */
	public void cambiarHorarioDepartamento(int i, int j,
			int k, int m, String nombre) {
		// TODO Auto-generated method stub
		String horaapertura=aplicacion.utilidades.Util.horaminutosAString(i, j);
		String horacierre=aplicacion.utilidades.Util.horaminutosAString(k, m);
		Time thI= Time.valueOf(horaapertura);
		Time thC=Time.valueOf(horacierre);
		this.controlador.cambiaHorarioDpto(nombre, thI, thC);
	}
	
	/**
	 * Función que ordena el ArrayList de los empleados
	 */
	public void ordenaEmpleados(){
		int i=0;
		ArrayList<Empleado> aux=new ArrayList<Empleado>();
		//Busco el jefe
		while(i<empleados.size()&& empleados.get(i).getRango()!=2){
			i++;
		}
		if(i<empleados.size()){
			aux.add(empleados.get(i));
			empleados.remove(i);
		}
		//Jefe ya añadido al auxiliar
		boolean fin=false;
		int j=0;
		int lon=empleados.size();
		while(j < lon) {
			fin=false;
			for (int j2 = 1; j2 < aux.size() && !fin; j2++) {
				if(aux.get(j2).getPosicion()>=empleados.get(0).getPosicion()){
					aux.add(j2, empleados.get(0));
					empleados.remove(0);
					fin=true;
				}
			}
			if(!fin){
				aux.add(empleados.get(0));
				empleados.remove(0);
			}
			j++;
		}
		empleados=aux;
	}
	
	/**
	 * Informa de si la aplicación sigue ejecutándose
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}
}