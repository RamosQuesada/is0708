package algoritmo;

import java.util.ArrayList;
import aplicacion.Empleado;
import aplicacion.Turno;

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
		 * dé todos los empleados del departamento.
		 */
		ArrayList<Empleado> personal = new ArrayList<Empleado>();
		this.anio = year;
		this.mes = m;
		this.estruc = new Estructura(mes,anio,personal);
		int dias = estruc.dameDias(mes);
		this.cuadrante = new Cuadrante(mes,dias);
		
	}
	
	// Aqui vendría el algoritmo
	
	/**
	 * Método que devuelve el cuadrante de ese mes
	 * @param mes Mes para el cual se quiere realizar el cuadrante
	 * @param anio Año al que pertenece el mes.
	 * @return cuadrante Cuadrante resultante de aplicar el algoritmo
	 * El arrayList disp es solo para pruebas
	 */
	public  Cuadrante ejecutaAlgoritmo(ArrayList<Empleado> disp){
		
		//colocamos a los empleados correspondientes a cada día
		for(int i=0;i<estruc.dameDias(mes);i++){
			
			//Aquí, actualizar la lista de empleados disponibles para cada
			//turno a partir de la lista de todos los empleados.
			
			//coloca sólo a los empleados fijos.
			colocaFijos(i,disp);
			
		}
		
		return this.cuadrante;
	}
	
	/**
	 * Método que se encarga de colocar a los empleados fijos
	 * @param día Dia del mes
	 * El arrayList disp es solo para pruebas
	 */
	private void colocaFijos(int dia, ArrayList<Empleado> disp){
		
		ArrayList<Empleado> disponibles;
		ListasEmpleados[][] listas;
		int numTrozos;
		
		listas = estruc.getDias();
		numTrozos = listas[0].length;
		
		//Para cada franja horaria
		for(int i=0;i<numTrozos;i++){
			
			disponibles = listas[dia][i].getDisponibles();

			for(int j=0;i<disponibles.size();j++){
				
				/* Aquí se debe mirar el tipo de contrato de cada
				 * empleado y si este es fijo habría que insertarlo 
				 * en la estructura y en el cuadrante.
				 */
							
			}		
		}		
	}
		
		
}

