package algoritmo;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

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
	
	//Optimizacion Algoritmo (reduccion llamadas a BBDD)
	private ArrayList<Empleado> listaE;
	
	
	public TurnoMatic(){		
	}
		 
	/**
	 * Constructora del algoritmo, se encarga de crear la estructura
	 * el cuadrante y recibe el controlador del programa
	 * @param m mes para el cual se realiza el cuadrante
	 * @param year año al que pertenece el mes
	 * @param cont controlador de la aplicacion
	 * El arrayList turnos es de prueba
	 */
	public TurnoMatic(int m, int year, Controlador cont, String idDepartamento){
		
		/* Llamada a la base de datos para que nos
		 * dé todos los empleados del departamento.
		 */
		this.controlador = cont;
		this.idDepartamento = idDepartamento;
		this.anio = year;
		this.mes = m;
	    this.listaE = controlador.getEmpleadosDepartamento(idDepartamento);		
		this.estruc = new Estructura(mes, year, cont, idDepartamento, listaE);
		this.cuadrante = new Cuadrante(mes, year, idDepartamento);		
	}

	/**
	 * Método que devuelve el cuadrante de ese mes
	 * @param mes Mes para el cual se quiere realizar el cuadrante
	 * @param anio Año al que pertenece el mes.
	 * @return cuadrante Cuadrante resultante de aplicar el algoritmo
	 * El arrayList disp es solo para pruebas.
	 */
	public Cuadrante ejecutaAlgoritmo(){	
		//Colocamos a los empleados correspondientes a cada día
		ListasEmpleados[][] horario = estruc.getDias();
		ListasEmpleados[] horarioDia =new ListasEmpleados[Util.dameDias(mes, anio)];
		ArrayList<Trabaja>[] cu = cuadrante.getCuad();
		ArrayList<Empleado> reser;
		ArrayList<Empleado> dispo;
		ArrayList<Empleado> empl;
		Turno turno;
		Trabaja trab;
		
		Empleado e;

		//Recorremos los dias del mes
		for(int i=0; i<Util.dameDias(mes,anio); i++){
			
			horarioDia[i]=new ListasEmpleados("aux");
			//dividimos en el numero de franjas de cada dia
			for(int j=0; j<estruc.getNumTrozos(); j++){
			//for(int j=0; j<disp.size(); j++){
				Time inif = estruc.getTrozosHorario().get(j); 
				Time finf = estruc.getTrozosHorario().get(j+1);
				dispo = horario[i][j].getDisponibles();
				reser = horario[i][j].getReserva();
				empl = horario[i][j].getEmpleados();
				
				//Comprobamos la disponibilidad de cada empleado
				for(int k=0; k<listaE.size(); k++){
					e = listaE.get(k);
					
					//creacionListas(e,i,inif,finf);
					if(e.estaDisponible(i,inif,finf,controlador,j,estruc.getNumTrozos())){
						dispo.add(e);		
						empl.add(e);
						turno = e.getTurnoActual();
						Time pr1 = new Time(19,49,00);
						Time pr2 = new Time(19,49,00);
						trab = new Trabaja(e.getEmplId(),pr1,pr2,turno.getIdTurno());
						cu[i].add(trab);
						
					}else{
						reser.add(e);
					}
				}
				
				horario[i][j].setDisponibles(dispo);
				horario[i][j].setReserva(reser);
				horario[i][j].setEmpleados(empl);
				
				//coloca sólo a los empleados fijos.
				//colocaFijos(i,dispo,i,j);
				
			}	
			
			ArrayList<Empleado> reserDia = new ArrayList<Empleado>();
			ArrayList<Empleado> dispoDia = new ArrayList<Empleado>();
			ArrayList<Empleado> emplDia = new ArrayList<Empleado>();
			Empleado aux;
			
			for(int j=0; j<estruc.getNumTrozos(); j++){
				int n=0;
				while (n<horario[i][j].getDisponibles().size()) {
					aux = horario[i][j].getDisponibles().get(n);
					dispoDia.add(aux);
					n++;
				}
				while (n<horario[i][j].getReserva().size()) {
					aux = horario[i][j].getReserva().get(n);
					reserDia.add(aux);
					n++;
				}
				while (n<horario[i][j].getEmpleados().size()) {
					aux = horario[i][j].getEmpleados().get(n);
					emplDia.add(aux);
					n++;
				}
			}
			
			colocaNoFijos(dispoDia, reserDia, emplDia, i, cu);//se colocan para cada dia i del mes
		}		

		cuadrante.setCuad(cu);
		//controlador.insertCuadrante(cuadrante);
		return this.cuadrante;
	}
	
	/**
	 * Método para colocar los empleados no fijos con el algoritmo vuelta-atrás
	 * @param dispo Lista de empleados disponibles que vienen del método ejecutaAlgoritmo
	 * @param reser Lista de empleados de reserva que vienen del método ejecutaAlgoritmo
	 * @param empl Lista de empleados que vienen del método ejecutaAlgoritmo
	 */
	private void colocaNoFijos (ArrayList<Empleado> dispo, ArrayList<Empleado> reser, ArrayList<Empleado> empl, int dia, ArrayList<Trabaja>[] cuadrante){
		Calendario cal=this.estruc.getCalendario();
		int tipoc;
		
		/*separo de las 3 listas los empleados que ya estan colocados en el cuadrante,
	      esto es, los que su tipo de contrato es 1 o 2 (fijo o rotatorio)*/
		for (int k=0;k<dispo.size();k++){
			tipoc=dispo.get(k).getContrato(controlador).getTipoContrato();
			if (tipoc==1 || tipoc==2){
				dispo.remove(k);
				k--;
			}
		}
		for (int k=0;k<reser.size();k++){
			tipoc=reser.get(k).getContrato(controlador).getTipoContrato();
			if (tipoc==1 || tipoc==2){
				reser.remove(k);
				k--;
			}
		}
		for (int k=0;k<empl.size();k++){
			tipoc=empl.get(k).getContrato(controlador).getTipoContrato();
			if (tipoc==1 || tipoc==2){
				empl.remove(k);
				k--;
			}
		}
		
		//ordeno las 3 listas segun la felicidad de los empleados		

		ArrayList<Empleado> e1=ordenarLista(dispo,1);
		ArrayList<Empleado> e2=ordenarLista(reser,2);
		ArrayList<Empleado> e3=ordenarLista(empl,1);
		
		//llamo al algoritmo vueltra atras
		
		boolean hecho=vueltaAtrasMarcaje(e1,e2,0,dia,cuadrante);
				
	}
	
	/**
	 * Metodo para ordenar los ArrayList por orden de felicidad y convertirlas en array
	 * @param lista es el ArrayList de empleados para ordenar y convertir en Array
	 * @param criterio es el criterio de ordenacion: 1 = de menor a mayor, 2= de mayor a menor
	 */
	private ArrayList<Empleado> ordenarLista(ArrayList<Empleado> lista,int criterio){
		Empleado[] e1=new Empleado[lista.size()];
		e1=lista.toArray(e1);
		Empleado aux;
		
		for (int k=0; k<e1.length;k++){
			for(int i=0;i<e1.length-1;i++){
				switch (criterio){
				case 1:if (e1[i].getFelicidad()>e1[i+1].getFelicidad()) {
					   		aux=e1[i+1];
					   		e1[i+1]=e1[i];
					   		e1[i]=aux;}
						break;
				case 2:if (e1[i].getFelicidad()<e1[i+1].getFelicidad()) {
							aux=e1[i+1];
							e1[i+1]=e1[i];
							e1[i]=aux;}
						break;
				default:break;
				}				
			}
		}
		
		ArrayList<Empleado> resultado=new ArrayList<Empleado>();
		for (int k=0; k<e1.length;k++){
			resultado.add(e1[k]);
		}
		return resultado;
	}
	
	/**
	 * Metodo recursivo para colocar los empleados segun las franjas horarias
	 * @param dispo Lista de empleados disponibles
	 * @param reser Lista de empleados de reserva
	 * @param k parametro para la recursion
	 * @param dia es el dia para el que estamos trabajando
	 */
	private boolean vueltaAtrasMarcaje (ArrayList<Empleado> dispo, ArrayList<Empleado> reser, int k, int dia, ArrayList<Trabaja>[] cuadrante){
		/*fHoraria es un ArrayList con todos los turnos en los que puede trabajar el empleado situado en la 
		 posición k de disponibles*/ 
		ArrayList<Turno> fHoraria = controlador.getListaTurnosContrato (k);
		Turno franjaHoraria;
		boolean hecho=false;
		while (fHoraria.size()!=0) {
			franjaHoraria = fHoraria.get(1);
			ponerEmpleado (dispo.get(k), franjaHoraria.getHoraEntrada(), franjaHoraria.getHoraSalida(), cuadrante[dia]);
			k=k+1;      
			if  (k==dispo.size()+1){
				if  (comprobarFranjasCompletas(cuadrante, dia, fHoraria)){
					hecho=true;
				/*	guardarCuadrante (cuadrante);
					guardarDatosEmpleados (cuadrante);
					mostrarCuadrante(cuadrante);*/ //esto se hace solo, no hace falta llamar a nada (creo)
				}
			}
			else{
				if (k<dispo.size()){
					hecho=vueltaAtrasMarcaje(dispo, reser,k,dia,cuadrante);
				}
			}
			k=k-1;
			quitarEmpleado(dispo.get(k),cuadrante[dia]);   
			fHoraria.remove(1);
		}
		return hecho;
	}

	
	/**
	 * metodo para comprobar que todas las franjas horarias de un dia cumplen los requisito de personal
	 * @param cuadDia es el cuadrante
	 * @param dia es el dia en el que queremos hacer la comprobacion
	 * @param fHoraria es la lista de franjas que se divide un dia
	 * @return
	 */
	
	private boolean comprobarFranjasCompletas(ArrayList<Trabaja>[] cuadDia,int dia, ArrayList<Turno> fHoraria){
		boolean valido=true;
		
		//bucle que recorre todas las franjas horarias de este dia
		for (int i=0;(i<fHoraria.size())&&(valido);i++){
			Turno turno=fHoraria.get(i);
			int horaIni=turno.getHoraEntrada().getHours();
			int horaFin=turno.getHoraSalida().getHours()+1;
			
			//bucle que recorre todas las horas de una franja
			for (int j=horaIni;j<=horaFin;j++){
				if (estruc.getCal().getMinHora(dia,j)>contarEmpleadosHora(cuadDia[dia],j)){
					valido=false;
				}
			}
		}
		
		return valido;
	}
	/**
	 * metodo para contar el numero de empleados que trabajan a una hora concreta
	 * @param lista la lista de trabajadores de un dia
	 * @param hora la hora a comprobar
	 * @return
	 */
	private int contarEmpleadosHora(ArrayList<Trabaja> lista, int hora){
		int contador=0;
		for(int i=0;i<lista.size();i++){
			if ((lista.get(i).getFichIni().getHours()<=hora)&&(lista.get(i).getFichFin().getHours()>=hora)){
				contador++;
			}
		}
		return contador;
	}
	
	/**
	 * Metodo para eliminar un empleado de una cuadrante de un cuadrante de un dia
	 * @param e el empleado a eliminar del cuadrante
	 * @param dia el cuadrante de tal dia
	 */
	private void quitarEmpleado (Empleado e, ArrayList<Trabaja> cuadDia){
		boolean enc=false;
		int k=0;
		
		while ((!enc) && (k<cuadDia.size())){
			if (cuadDia.get(k).getIdEmpl()==e.getEmplId()) {
				cuadDia.remove(k);
				enc=true;
			}
		}				
	}
	
	/**
	 * Metodo para poner un empleado en un cuadrante de un dia
	 * @param e el empleado a colocar
	 * @param ini el inicio de su turno de trabajo
	 * @param fin el fin de su turno de trabajo
	 * @param dia el dia de su turno d trabajo
	 * 
	 */
	private void ponerEmpleado (Empleado e, Time ini, Time fin, ArrayList<Trabaja> cuadDia){
		Trabaja trabaja = new Trabaja(e.getEmplId(),ini,fin,e.getTurnoActual().getIdTurno());
		cuadDia.add(trabaja);
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
		Calendario calendario = estruc.getCalendario();
		
		//Para evitar el deprecado usar Calendar
		//Calendar hora = new Calendar();
		Time hora;
		hora = estruc.getTrozosHorario().get(q);
		int gHora = hora.getHours();
		max = calendario.getMaxHora(dia,gHora);
		
		//Para cada franja horaria
		for(int i=0;i<numTrozos;i++){
			
			disponibles = listas[dia][i].getDisponibles();
			empleados = new ArrayList<Empleado>();

			for(int j=0;j<disponibles.size();j++){
				
				/* Aquí se debe mirar el tipo de contrato de cada
				 * empleado y si este es fijo habría que insertarlo 
				 * en la estructura y en el cuadrante.
				 */
				emp = disponibles.get(j);
				turno = emp.getTurnoActual();
				
				//comprobamos si tiene turno fijo
				
				//if(turno.getIdTurno() == 1){
					
					empleados.add(emp);
					Time pr1 = new Time(19,49,00);
					Time pr2 = new Time(19,49,00);
					trab = new Trabaja(emp.getEmplId(),pr1,pr2,turno.getIdTurno());//tenemos que meterle el turno que le corresponda
					cu[i].add(trab);
						
				//}				
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
			System.out.println("Dia "+i);
			trabajadores=cuad[i];
			for(int j=0;j<trabajadores.size();j++){
				System.out.println("Turno "+j);
				trab = trabajadores.get(j);
				System.out.println("Empleado: " + trab.getIdEmpl());
				
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
		
		
		for(int i=0; i<listas.length; i++){
			System.out.println("Dia: " + i);
			for(int j=0;j<listas[i].length;j++){
				emp = listas[i][j].getEmpleados();
				//disp=listas[i][j].getDisponibles();
				//reser=listas[i][j].getReserva();
				System.out.println("Turno: " + j);
				System.out.println("Lista de empleados: ");
				for(int k=0;k<emp.size();k++){
					e = emp.get(k);
					System.out.println(e.getEmplId());
				}
				
			/*	System.out.println("Lista de empleados disponibles: ");
				for(int k=0;k<disp.size();k++){
					e=disp.get(k);
					System.out.println(e.getEmplId());
				}
				
				System.out.println("Lista de empleados reserva: ");
				for(int k=0;k<reser.size();k++){
					e=reser.get(k);
					System.out.println(e.getEmplId());
				}*/
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

