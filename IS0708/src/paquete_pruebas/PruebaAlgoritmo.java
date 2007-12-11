package paquete_pruebas;

import algoritmo.*;
import aplicacion.Util;
import aplicacion.Empleado;
//import aplicacion.Turno; // Utilizamos la clase turno de nuestro "gran paquete"
import java.util.ArrayList;
import java.sql.Time;

public class PruebaAlgoritmo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TurnoMatic alg;
		Cuadrante cuad;
		Empleado emp1,emp2,emp3;
		aplicacion.Turno t1,t2;
		ArrayList<Empleado> disponibles;
		
		disponibles = new ArrayList<Empleado>();
		//public turnoString idTurno, String descripcion,String horaEntrada, String horaSalida, String horaDescanso, int descanso
		//t1 = new Turno("1","M1","9:00:00","14:00:00","12:30:00",20);
		//t2 = new Turno("2","T1","9:00:00","17:00:00","14:30:00",25);
		t1=new aplicacion.Turno(1,"23","2");
		t2=new aplicacion.Turno(2,"12","21");
		emp1 = new Empleado(1,"emp1",t1);
		emp2 = new Empleado(2,"emp2",t2);
		emp3 = new Empleado(3,"emp3",t1);
		disponibles.add(emp1);
		disponibles.add(emp2);
		disponibles.add(emp3);
		cuad = new Cuadrante(12,2007);
		System.out.println(cuad.getNumDias());
		
		alg = new TurnoMatic(6,2007);
		//alg.ejecutaAlgoritmo(disponibles);
		//alg.imprimeCuadrante();
		//alg.imprimeEstructura();
		/*Time pr1 = new Time(19,49,00);
		System.out.println(pr1.toString());
		Time pr2 = Util.calculaFinDescanso(pr1, 20);
		System.out.println(pr2.toString());
		*/
		

		// Prueba de inicializaTrozos
		/*ArrayList<Turno> turnos = new ArrayList<Turno>();
		//turnos.add(t1);
		//turnos.add(t2);
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
		System.out.println(horas.toString()); // Sin ordenar
		// Ordenar la lista
		int numTrozos = horas.size();
		ArrayList<Time> trozosHorario = new ArrayList<Time>();
		for (int i=0; i<numTrozos; i++){
			int j=0;
			while (j<trozosHorario.size() && trozosHorario.get(j).getTime()<horas.get(i).getTime())
				j++;
			trozosHorario.add(j,horas.get(i));
		}
		System.out.println(trozosHorario.toString()); // Ordenada
		*/
		
		/*
		// Prueba de la funcion Util.numExpertos(patron)
		System.out.println("Expertos: "+Util.numExpertos("3e5p"));
		System.out.println("Expertos: "+Util.numExpertos("re5p"));
		*/
		
		System.out.println();
		
		
		
	}

}
