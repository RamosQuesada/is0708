package algoritmo;

import java.util.ArrayList;
import aplicacion.*;


/**
 * Esta contiene los algoritmos que vamos a utilizar para el calculo automatico de un cuadrante
 * @author madctol
 *
 */
public class TurnoMatic {
	
	//Atributos
	private Cuadrante cuadrante;
	private Estructura estruc;
	private int mes;
	private int anio;
	
	public TurnoMatic(){
		
		
	}
	
	public TurnoMatic(int m, int year){
		
		/* Llamada a la base de datos para que nos
		 * d� todos los empleados del departamento.
		 */
		ArrayList<Empleado> personal = new ArrayList<Empleado>();
		this.anio = year;
		this.mes = m;
		this.estruc = new Estructura(mes,anio,personal);
		int dias = Util.dameDias(mes,anio);
		this.cuadrante = new Cuadrante(mes,dias);
		
	}
	
	// Aqui vendr�a el algoritmo
	
	/**
	 * M�todo que devuelve el cuadrante de ese mes
	 * @param mes Mes para el cual se quiere realizar el cuadrante
	 * @param anio A�o al que pertenece el mes.
	 * @return cuadrante Cuadrante resultante de aplicar el algoritmo
	 * El arrayList disp es solo para pruebas
	 */
	public  Cuadrante ejecutaAlgoritmo(ArrayList<Empleado> disp){
		
		//colocamos a los empleados correspondientes a cada d�a
		for(int i=0;i<Util.dameDias(mes,anio);i++){
			
			//Aqu�, actualizar la lista de empleados disponibles para cada
			//turno a partir de la lista de todos los empleados.
			
			
			//coloca s�lo a los empleados fijos.
			colocaFijos(i,disp);
			
		}
		
		return this.cuadrante;
	}
	
	/**
	 * M�todo que se encarga de colocar a los empleados fijos
	 * @param d�a Dia del mes
	 * El arrayList disp es solo para pruebas
	 */
	private void colocaFijos(int dia, ArrayList<Empleado> disp){
		
		ArrayList<Empleado> disponibles;
		ArrayList<Empleado> empleados;
		ListasEmpleados[][] listas;
		int numTrozos;
		Empleado emp;
		Turno turno;
		
		listas = estruc.getDias();
		//numTrozos = listas[0].length;
		//prueba
		numTrozos = 4;
		
		//Para cada franja horaria
		for(int i=0;i<numTrozos;i++){
			
			//disponibles = listas[dia][i].getDisponibles();
			//prueba
			disponibles = disp;
			empleados = new ArrayList<Empleado>();

			for(int j=0;j<disponibles.size();j++){
				
				/* Aqu� se debe mirar el tipo de contrato de cada
				 * empleado y si este es fijo habr�a que insertarlo 
				 * en la estructura y en el cuadrante.
				 */
				emp = disponibles.get(j);
				turno = emp.turno;
				
				//comprobamos si tiene turno fijo
				if(turno.id == 1){
					
					empleados.add(emp);
					
					
					
					
				}
				
							
			}
			System.out.println();

		}		
	}
		
		
}

