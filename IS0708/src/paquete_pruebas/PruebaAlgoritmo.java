package paquete_pruebas;
import algoritmo.*;
import aplicacion.Empleado;
import aplicacion.Turno;
import java.util.ArrayList;

public class PruebaAlgoritmo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TurnoMatic alg;
		Cuadrante cuad;
		Empleado emp1,emp2,emp3;
		Turno t1,t2;
		ArrayList<Empleado> disponibles;
		
		disponibles = new ArrayList<Empleado>();
		t1 = new Turno(1,"9","14");
		t2 = new Turno(2,"12","17");
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
		
		System.out.println();
		
	}

}
