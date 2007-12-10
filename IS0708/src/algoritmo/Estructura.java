package algoritmo;

import java.util.ArrayList;
import java.sql.Time;
import aplicacion.Empleado;
import aplicacion.Util;

/**
 * La primera dimensi�n representa el mes en cuesti�n (una posici�n por cada dia del mes)
 * la segunda dimensi�n representa el horario de cada dia (los turnos)
 * @author madctol
 *
 */
public class Estructura {
	

	private ListasEmpleados[][] dias;
	private ArrayList<Empleado> personal;//aqui estar�n todos los empleados
	private Calendario cal;//calendario donde se almacena min/max perso,exp/inexp... 
	private Time[] trozosHorario; // Lista con el inicio de cada turno y el fin del ultimo
	                              // tamanio: nTrozos+1;
	private int numTrozos;

	public Estructura(int mes,int anio,ArrayList<Empleado> personal){//constructora de la estructura
		this.personal = personal;
		
		// Calcular el numero de trozos en que se divide el horario
		// Recuperar de la base de datos la lista de todos los turnos del departamento
		// y ver en cuantos trozos vas a partir cada dia
		inicializaTrozos(); 
		
		int numDias = Util.dameDias(mes,anio);//calculamos el numero de dias
		dias = new ListasEmpleados[numDias][numTrozos];
		// Se podr�a no asignar listas a los dias que no se trabaja
		for (int i=0; i<numDias; i++){
			for (int j=0; j<numTrozos; j++){
				dias[i][j] = new ListasEmpleados();
			}
		}
		personal=new ArrayList<Empleado>();
		//rellenar lista empleados
		cal=new Calendario(numDias,mes,anio);//creamos calendario
	}
	
	public ListasEmpleados[][] getDias(){
		return dias;
	}
	
	private void inicializaTrozos(){
		// ArrayList<Turno> turnos = Database.getTurnos(idDepartamento) // M�todo en proceso
		ArrayList<Turno> turnos = new ArrayList<Turno>();		
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
		numTrozos = horas.size();
		ArrayList<Time> orden = new ArrayList<Time>();
		for (int i=0; i<numTrozos; i++){
			int j=0;
			while (j<orden.size() && orden.get(j).getTime()<horas.get(i).getTime())
				j++;
			orden.add(j,horas.get(i));
		}
		// Guarda lo calculado
		trozosHorario = new Time[numTrozos];
		for (int i=0; i<numTrozos; i++)
			trozosHorario[i] = orden.get(i);
	}
	
}
