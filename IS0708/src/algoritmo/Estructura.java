package algoritmo;

import java.util.ArrayList;
import java.sql.Time;
import aplicacion.*;

/**
 * La primera dimensi�n representa el mes en cuesti�n (una posici�n por cada dia del mes)
 * la segunda dimensi�n representa el horario de cada dia (los turnos)
 * @author DavidMartin & Miguel Angel Diaz
 *
 */
public class Estructura {
	

	private ListasEmpleados[][] dias;
	private ArrayList<Empleado> personal;//aqui estar�n todos los empleados
	private Calendario cal;//calendario donde se almacena min/max perso,exp/inexp... 
	private ArrayList<Time> trozosHorario; // Lista con el inicio de cada turno y el fin del ultimo
	                                       // tamanio: nTrozos+1;
	private int numTrozos;
	private int idDepartamento;
	//Atributo de prueba turnos
	ArrayList<Turno> turnos;
	

	
	public Estructura(int mes,int anio,ArrayList<Empleado> personal,int idDepartamento){//constructora de la estructura
		this.personal = personal;
		this.idDepartamento=idDepartamento;
		// Calcular el numero de trozos en que se divide el horario
		// Recuperar de la base de datos la lista de todos los turnos del departamento
		// y ver en cuantos trozos vas a partir cada dia
		inicializaTrozos(); 
		
		int numDias = Util.dameDias(mes, anio);//calculamos el numero de dias
		dias = new ListasEmpleados[numDias][numTrozos];
		// Se podr�a no asignar listas a los dias que no se trabaja
		for (int i=0; i<numDias; i++){
			for (int j=0; j<numTrozos; j++){
				dias[i][j] = new ListasEmpleados();
			}
		}
		personal = new ArrayList<Empleado>();
		//rellenar lista empleados
		cal=new Calendario(mes, anio, idDepartamento);//creamos calendario
	}
	
	/**
	 * Esta constructora es de prueba, sirve para pasar un array de turnos
	 * y poder hacer pruebas.
	 * @param mes
	 * @param anio
	 * @param personal
	 * @deprecated borrar
	 */
	public Estructura(int mes,int anio,ArrayList<Empleado> personal, ArrayList<Turno> t,int idDepartamento){//constructora de la estructura
		this.personal = personal;
		// Calcular el numero de trozos en que se divide el horario
		// Recuperar de la base de datos la lista de todos los turnos del departamento
		// y ver en cuantos trozos vas a partir cada dia
		this.idDepartamento=idDepartamento;
		this.turnos = t;
		inicializaTrozos(); 
		
		this.turnos = t;
		
		int numDias = Util.dameDias(mes,anio);//calculamos el numero de dias
		dias = new ListasEmpleados[numDias][numTrozos];
		// Se podr�a no asignar listas a los dias que no se trabaja
		for (int i=0; i<numDias; i++){
			for (int j=0; j<numTrozos; j++){
				dias[i][j] = new ListasEmpleados();
			}
		}
		personal = new ArrayList<Empleado>();
		//rellenar lista empleados
		cal=new Calendario(mes, anio, idDepartamento);//creamos calendario
	}
	
	public ListasEmpleados[][] getDias(){
		return dias;
	}
	
	public void setDias(ListasEmpleados[][] l){
		
		dias = l;
	}
	
	private void inicializaTrozos(){
		// ArrayList<Turno> turnos = Database.getTurnos(idDepartamento) // M�todo en proceso
		//ArrayList<Turno> turnos = new ArrayList<Turno>();	Comentado para prueba	
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
		// Ordenar la lista
		numTrozos = horas.size()-1;
		trozosHorario = new ArrayList<Time>();
		for (int i=0; i<numTrozos+1; i++){
			int j=0;
			while (j<trozosHorario.size() && trozosHorario.get(j).getTime()<horas.get(i).getTime())
				j++;
			trozosHorario.add(j,horas.get(i));
		}
	}
	
	/**
	 * Devuelve el numero de trozos en los que se divide el dia del mes.
	 * @return	numero de trozos/dia.
	 */
	public int getNumTrozos() {
		return numTrozos;
	}
	
	/**
	 * Devuelve las franjas horarias en las que se divide el dia del mes.
	 * @return	franjas horarias.
	 */
	public ArrayList<Time> getTrozosHorario() {
		return trozosHorario;
	}	
	
	public Calendario getCalendario(){
		return cal;
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

	public int getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(int idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public ArrayList<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(ArrayList<Turno> turnos) {
		this.turnos = turnos;
	}

	public void setTrozosHorario(ArrayList<Time> trozosHorario) {
		this.trozosHorario = trozosHorario;
	}

	public void setNumTrozos(int numTrozos) {
		this.numTrozos = numTrozos;
	}

	
}
