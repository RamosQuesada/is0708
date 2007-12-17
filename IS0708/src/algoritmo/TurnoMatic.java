package algoritmo;

import java.sql.Time;
import java.util.ArrayList;
import aplicacion.*;


/**
 * Esta contiene los algoritmos que vamos a utilizar para el calculo automatico de un cuadrante
 * @author Alberto Maqueda & Javier Doria & Carlos Gil 
 *
 */
public class TurnoMatic {
	
	//Atributos
	private Cuadrante cuadrante;
	private Estructura estruc;
	private int mes;
	private int anio;
	private String idDepartamento;
	private Controlador controlador;
	
	
	public TurnoMatic(){
		
		
	}
	
	//El array list turnos es de prueba.
	public TurnoMatic(int m, int year,ArrayList<Turno> turnos,String idDepartamento){
		
		/* Llamada a la base de datos para que nos
		 * dé todos los empleados del departamento.
		 */
		ArrayList<Empleado> personal = new ArrayList<Empleado>();
		this.idDepartamento=idDepartamento;
		this.anio = year;
		this.mes = m;
		this.estruc = new Estructura(mes,year,personal, turnos, idDepartamento);
		this.cuadrante = new Cuadrante(mes,year, idDepartamento);
		
	}
	 
	/**
	 * Constructora del algoritmo, se encarga de crear la estructura
	 * el cuadrante y recibe el controlador del programa
	 * @param m mes para el cual se realiza el cuadrante
	 * @param year año al que pertenece el mes
	 * @param cont controlador de la aplicacion
	 */
	public TurnoMatic(int m, int year, Controlador cont, String idDepartamento){
		
		/* Llamada a la base de datos para que nos
		 * dé todos los empleados del departamento.
		 */
		ArrayList<Empleado> personal = new ArrayList<Empleado>();
		this.idDepartamento=idDepartamento;
		this.anio = year;
		this.mes = m;
		this.estruc = new Estructura(mes,year,personal, cont.getListaTurnosEmpleados(), idDepartamento);
		this.cuadrante = new Cuadrante(mes,year, idDepartamento);
		this.controlador = cont;
		
	}

	/**
	 * Método que devuelve el cuadrante de ese mes
	 * @param mes Mes para el cual se quiere realizar el cuadrante
	 * @param anio Año al que pertenece el mes.
	 * @return cuadrante Cuadrante resultante de aplicar el algoritmo
	 * El arrayList disp es solo para pruebas.
	 */
	public Cuadrante ejecutaAlgoritmo(){
	
		int id = Integer.parseInt(idDepartamento);
		ArrayList<Empleado> listaE = controlador.getEmpleadosDepartamento(id);
		
		//Colocamos a los empleados correspondientes a cada día
		
		ListasEmpleados[][] horario = estruc.getDias();
		ArrayList<Empleado> reser;
		ArrayList<Empleado> dispo;
		Empleado e;
		Calendario calendario;
		
		calendario = estruc.getCalendario();

		//Recorremos los dias del mes
		for(int i=0; i<Util.dameDias(mes,anio); i++){
			
			//dividimos en el numero de franjas de cada dia
			for(int j=0; j<estruc.getNumTrozos(); j++){
			//for(int j=0; j<disp.size(); j++){
				Time inif = estruc.getTrozosHorario().get(j); 
				Time finf = estruc.getTrozosHorario().get(j+1);
				dispo = horario[i][j].getEmpleados();
				reser = horario[i][j].getReserva();
				
				//Comprobamos la disponibilidad de cada empleado
				for(int k=0; k<listaE.size(); k++){
					e = listaE.get(k);
					if(e.estaDisponible(i,inif,finf)){
						dispo.add(e);						
					}else{
						reser.add(e);
					}
				}
				horario[i][j].setDisponibles(dispo);
				horario[i][j].setReserva(reser);
				
				//coloca sólo a los empleados fijos.
				colocaFijos(i,listaE,i,j);
			}	
		}		
		return this.cuadrante;
}
	
	/**
	 * Método que se encarga de colocar a los empleados fijos
	 * @param día Dia del mes
	 * El arrayList disp es solo para pruebas
	 */
	private void colocaFijos(int dia, ArrayList<Empleado> disp, int p, int q){
		
		ArrayList<Trabaja>[] cu = cuadrante.getCuad();
		ArrayList<Empleado> disponibles;
		ArrayList<Empleado> empleados;
		ListasEmpleados[][] listas;
		int numTrozos;
		Empleado emp;
		Turno turno;
		Trabaja trab;
		int max;
		
		listas = estruc.getDias();
		numTrozos = estruc.getNumTrozos();
		//prueba
		//numTrozos = 4;
		Calendario calendario = estruc.getCalendario();;
		max = calendario.getMaxHora(p,q);
		
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
				emp = disp.get(j);
				turno = emp.turno;
				
				//comprobamos si tiene turno fijo
				
				if(turno.getIdTurno() == 1 && empleados.size()<max){
					
					empleados.add(emp);
					Time pr1 = new Time(19,49,00);
					Time pr2 = new Time(19,49,00);
					trab = new Trabaja(emp.getEmplId(),pr1,pr2,0);//tenemos que meterle el turno que le corresponda
					cu[i].add(trab);
						
				}				
			}
			listas[dia][i].setEmpleados(empleados);  
			cuadrante.setCuad(cu);
		}
		this.estruc.setDias(listas);
	}
	
	/**
	 * Método de prueba que imprime el cuadrante
	  */
	public void imprimeCuadrante(){
	 
		System.out.println("Mes: "+cuadrante.getMes());
		System.out.println("Año: "+cuadrante.getAnio());
		System.out.println("Número de días: "+cuadrante.getNumDias());
		
		ArrayList<Trabaja> cuad[];
		ArrayList<Trabaja> trabajadores;
		Trabaja trab;
		
		cuad=cuadrante.getCuad();
		
		for(int i=0;i<cuad.length;i++){
			trabajadores=cuad[i];
			for(int j=0;j<trabajadores.size();j++){
				trab = trabajadores.get(j);
				System.out.println("Empleado: "+trab.getIdEmpl());
				
			}
		}	
	}
	
	public void imprimeEstructura(){
		
		ArrayList<Empleado> emp;
		ArrayList<Empleado> disp;
		ArrayList<Empleado> reser;
		ListasEmpleados[][] listas;
		Empleado e;
		
		listas = estruc.getDias();
		
		
		for(int i=0;i<listas.length;i++){
			System.out.println("Dia: "+i);
			for(int j=0;j<listas[i].length;j++){
				emp=listas[i][j].getEmpleados();
				disp=listas[i][j].getDisponibles();
				reser=listas[i][j].getReserva();
				System.out.println("Turno: "+j);
				System.out.println("Lista de empleados: ");
				for(int k=0;k<emp.size();k++){
					e=emp.get(k);
					System.out.println(e.getEmplId());
				}
				
				System.out.println("Lista de empleados disponibles: ");
				for(int k=0;k<disp.size();k++){
					e=disp.get(k);
					System.out.println(e.getEmplId());
				}
				
				System.out.println("Lista de empleados reserva: ");
				for(int k=0;k<reser.size();k++){
					e=reser.get(k);
					System.out.println(e.getEmplId());
				}
			}
		}
	}

	public Cuadrante getCuadrante() {
		return cuadrante;
	}

	public void setCuadrante(Cuadrante cuadrante) {
		this.cuadrante = cuadrante;
	}

	public Estructura getEstruc() {
		return estruc;
	}

	public void setEstruc(Estructura estruc) {
		this.estruc = estruc;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}


		
}

