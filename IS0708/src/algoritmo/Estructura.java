package algoritmo;

import java.util.ArrayList;
import java.sql.Time;
import aplicacion.*;
import aplicacion.datos.Empleado;
import aplicacion.datos.Turno;
import aplicacion.utilidades.Util;

/**
 * La primera dimension representa el mes en cuestion (una posicion por cada dia del mes)
 * la segunda dimension representa el horario de cada dia (los turnos)
 * @author DavidMartin & Miguel Angel Diaz
 *
 */
public class Estructura {
	
	private ListasEmpleados[][] dias;
	private ArrayList<Empleado> personal;  // todos los empleados
	private Calendario cal;				   // calendario donde se almacena min/max de personal, exp/inexp... 
	private ArrayList<Time> trozosHorario; // lista con el inicio de cada turno y el fin del ultimo
	                                       // tamanio: nTrozos+1;
	private int numTrozos;
	private String idDepartamento;
	private Controlador controlador;		
	
	/**
	 * Constructora con parámetros
	 * @param mes Mes
	 * @param anio Año
	 * @param cont Controlador
	 * @param idDepartamento Identificador del departamento
	 * @param listaE Lista de empleados
	 */
	public Estructura(int mes, int anio, Controlador cont, String idDepartamento, ArrayList<Empleado> listaE){
		this.idDepartamento = idDepartamento;
		this.controlador = cont;
		this.personal = listaE;
		
		// calculamos el numero de trozos en que se divide el horario
		/* recuperamos de la base de datos la lista de todos los turnos del departamento 
		 * y vemos en cuantos trozos vas a partir cada dia*/
		inicializaTrozos(); 
		
		//calculamos el numero de dias
		int numDias = Util.dameDias(mes, anio);					
		dias = new ListasEmpleados[numDias][numTrozos];
		// se podria no asignar listas a los dias que no se trabaja
		for (int i=0; i<numDias; i++){
			for (int j=0; j<numTrozos; j++){
				dias[i][j] = new ListasEmpleados(idDepartamento);
			}
		}
		//rellenamos la lista de empleados
		cal = new Calendario(mes, anio, controlador, idDepartamento);//creamos calendario
	}
	
	/**
	 * Consulta las listas de empleados
	 * @return Listas de Empleados 
	 */
	public ListasEmpleados[][] getDias(){
		return dias;
	}
	
	/**
	 * Modifica las listas de empleados
	 * @param Listas de Empleados
	 */
	public void setDias(ListasEmpleados[][] l){
		dias = l;
	}
	
	/**
	 * Método para dividir el horario de un empleado según las franjas que forman 
	 * los turnos de los empleados y sus tiempos de descanso
	 */
	private void inicializaTrozos(){
		ArrayList<Turno> turnos = controlador.getListaTurnosEmpleadosDpto(this.idDepartamento);
		ArrayList<Time> horas = new ArrayList<Time>();
		for (int i=0; i<turnos.size(); i++){
			if (!horas.contains(turnos.get(i).getHoraEntrada()))
				horas.add(turnos.get(i).getHoraEntrada());
			if (!horas.contains(turnos.get(i).getHoraSalida()))
				horas.add(turnos.get(i).getHoraSalida());
			if (!horas.contains(turnos.get(i).getHoraDescanso()))
				horas.add(turnos.get(i).getHoraDescanso());
			Time finDescanso = Util.calculaFinDescanso(turnos.get(i).getHoraDescanso(), turnos.get(i).getTDescanso());
			if (!horas.contains(finDescanso))
				horas.add(finDescanso);
		}
		// ordenar la lista
		numTrozos = horas.size()-1;
		trozosHorario = new ArrayList<Time>();
		for (int i=0; i<numTrozos+1; i++){
			int j=0;
			while (j<trozosHorario.size() && trozosHorario.get(j).getTime()<horas.get(i).getTime())
				j++;
			trozosHorario.add(j,horas.get(i));
		}
	}
	
	public int getNumTrozos() {
		return numTrozos;
	}
	
	public ArrayList<Time> getTrozosHorario() {
		return trozosHorario;
	}	

	public ArrayList<Empleado> getPersonal() {
		return personal;
	}

	public void setPersonal(ArrayList<Empleado> personal) {
		this.personal = personal;
	}

	public Calendario getCal() {
		return cal;
	}

	public void setCal(Calendario cal) {
		this.cal = cal;
	}

	public String getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public void setTrozosHorario(ArrayList<Time> trozosHorario) {
		this.trozosHorario = trozosHorario;
	}

	public void setNumTrozos(int numTrozos) {
		this.numTrozos = numTrozos;
	}
	
}
