package algoritmo;

import java.sql.Time;
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
		 * dé todos los empleados del departamento.
		 */
		ArrayList<Empleado> personal = new ArrayList<Empleado>();
		this.anio = year;
		this.mes = m;
		this.estruc = new Estructura(mes,anio,personal);
		int dias = Util.dameDias(mes,anio);
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
	
		//Colocamos a los empleados correspondientes a cada día
		
		ListasEmpleados[][] horario = estruc.getDias();
		ArrayList<Empleado> reser;
		ArrayList<Empleado> dispo;
		Empleado e;
		
		//Recorremos los dias del mes
		for(int i=0; i<Util.dameDias(mes,anio); i++){
			//dividimos en el numero de franjas de cada dia
			for(int j=0; j<estruc.getNumTrozos(); j++){
				Time inif = estruc.getTrozosHorario()[j];
				Time finf = estruc.getTrozosHorario()[j+1];
				dispo = horario[i][j].getEmpleados();
				reser = horario[i][j].getReserva();
				
				for(int k=0; k<disp.size(); k++){
					e = disp.get(k);
					if(e.estaDisponible(inif,finf)){
						dispo.add(e);						
					}else{
						reser.add(e);
					}
				}
				horario[i][j].setDisponibles(dispo);
				horario[i][j].setReserva(reser);
			}	
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
				
				/* Aquí se debe mirar el tipo de contrato de cada
				 * empleado y si este es fijo habría que insertarlo 
				 * en la estructura y en el cuadrante.
				 */
			/*	emp = disponibles.get(j);
				turno = emp.turno;
				
				//comprobamos si tiene turno fijo
				if(turno.id == 1){
					
					empleados.add(emp);
					
					
					
					
				}
				*/
							
			}
			System.out.println();

		}		
	}
		
		
}

