package algoritmo;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import aplicacion.*;


/**
 * Esta contiene los algoritmos que vamos a utilizar para el calculo automatico de un cuadrante
 * @author grupoAlgoritmo 
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
	 * @param m Mes para el cual se realiza el cuadrante
	 * @param year Año al que pertenece el mes
	 * @param cont Controlador de la aplicacion
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
	 * @param anio Año al que pertenece el mes
	 * @return cuadrante Cuadrante resultante de aplicar el algoritmo
	 * El arrayList disp es solo para pruebas
	 */
	public Cuadrante ejecutaAlgoritmo(){	
		//Colocamos a los empleados correspondientes a cada día
		ListasEmpleados[][] horario = estruc.getDias();
		//ListasEmpleados[] horarioDia =new ListasEmpleados[Util.dameDias(mes, anio)];
		ArrayList<Trabaja>[] cu = cuadrante.getCuad();
		ArrayList<Contrato> contratosDep = this.controlador.getListaContratosDpto(this.idDepartamento);
		ArrayList<Empleado> reser;
		ArrayList<Empleado> dispo;
		ArrayList<Empleado> empl;
		Turno turno;
		Trabaja trab;
		Contrato contAux;
		
		Empleado e;

		//Recorremos los dias del mes
		for(int i=0; i<Util.dameDias(mes,anio); i++){ //FOR1
			
			//horarioDia[i] = new ListasEmpleados(idDepartamento);
			
			//dividimos en el numero de franjas de cada dia
			for(int j=0; j<estruc.getNumTrozos(); j++){ //FOR2

				Time inif = estruc.getTrozosHorario().get(j); 
				Time finf = estruc.getTrozosHorario().get(j+1);
				dispo = horario[i][j].getDisponibles();
				reser = horario[i][j].getReserva();
				empl = horario[i][j].getEmpleados();
				
				//Comprobamos la disponibilidad de cada empleado
				for(int k=0; k<listaE.size(); k++){ //FOR3 
					e = listaE.get(k);
					int id = e.getContratoId();
					contAux = this.controlador.getContrato(id);
					/*if(contAux.getTipoContrato()==1 || contAux.getTipoContrato()==2){
							
						if(e.estaDisponible(i,inif,finf,controlador,contratosDep,j,estruc.getNumTrozos())){
					
							empl.add(e);
							turno = e.getTurnoActual();
							Time pr1 = new Time(19,49,00);
							Time pr2 = new Time(19,49,00);
							trab = new Trabaja(e.getEmplId(),inif,finf,turno.getIdTurno());
							cu[i].add(trab);
						
							}else{
								if (contAux.getTipoContrato()==1 )
									reser.add(e);
							}
					}*/
					if(e.estaDisponible(i,inif,finf,controlador,contratosDep,j,estruc.getNumTrozos())){
						
						if(contAux.getTipoContrato()==1 || contAux.getTipoContrato()==2){
							
							empl.add(e);
							turno = e.getTurnoActual();
							trab = new Trabaja(e.getEmplId(),inif,finf,turno.getIdTurno());
							cu[i].add(trab);
							
						}else
							//(contAux.getTipoContrato()==3 || contAux.getTipoContrato()==4)
							dispo.add(e);
						
					}else
							reser.add(e);

				} //ENDFOR3
						
				horario[i][j].setEmpleados(empl);
				horario[i][j].setDisponibles(dispo);
				horario[i][j].setReserva(reser);
				
			} //ENDFOR2	
			
			ArrayList<Empleado> reserDia = new ArrayList<Empleado>();
			ArrayList<Empleado> dispoDia = new ArrayList<Empleado>();
			ArrayList<Empleado> emplDia = new ArrayList<Empleado>();
			Empleado aux;
			
			for(int j=0; j<estruc.getNumTrozos(); j++){

				int n=0;
				while (n<horario[i][j].getDisponibles().size()) {
					aux = horario[i][j].getDisponibles().get(n);
					if(!dispoDia.contains(aux))
						dispoDia.add(aux);
					n++;
				}		
				

				n=0;
				while (n<horario[i][j].getEmpleados().size()) {
					aux = horario[i][j].getEmpleados().get(n);
					if(!emplDia.contains(aux))
						emplDia.add(aux);
					n++;
				}

			}
			
			for(int j=0; j<estruc.getNumTrozos(); j++){
				
				int n=0;
				while (n<horario[i][j].getReserva().size()) {
					aux = horario[i][j].getReserva().get(n);
					if((!reserDia.contains(aux)) && (!emplDia.contains(aux)))
						reserDia.add(aux);
					n++;
				}
			
			}
			
			//metemos en dispoDia a todos los empleados con contrato 3 o 4 y rango 1
			/*for(int j=0;j<listaE.size();j++){
				aux = listaE.get(j);
				if(aux.getRango()==1){
					int id = aux.getContratoId();
					contAux = this.controlador.getContrato(id);
					if(contAux.getTipoContrato()==3 || contAux.getTipoContrato()==4){
						dispoDia.add(aux);
					}
				}
			}*/
	
			colocaNoFijos(dispoDia, reserDia, emplDia, i, cu);//se colocan para cada dia i del mes 
		}
		
		cuadrante.setCuad(cu);
		controlador.insertCuadrante(cuadrante);
		return this.cuadrante;
	}
	
	/**
	 * Método para colocar los empleados no fijos con el algoritmo vuelta atrás
	 * @param dispo Lista de empleados disponibles que vienen del método ejecutaAlgoritmo
	 * @param reser Lista de empleados de reserva que vienen del método ejecutaAlgoritmo
	 * @param empl Lista de empleados que vienen del método ejecutaAlgoritmo
	 */
	private void colocaNoFijos (ArrayList<Empleado> dispo, ArrayList<Empleado> reser, ArrayList<Empleado> empl, int dia, ArrayList<Trabaja>[] cuadAux){
		//ordeno las 2 listas segun la felicidad de los empleados		
		ArrayList<Empleado> e1=ordenarLista(dispo,1);
		ArrayList<Empleado> e2=ordenarLista(reser,2);
		
		boolean hecho=false;
		
		/*hacemos unas comprobaciones básicas que permiten saber si, antes de ejecutar 
		  el algoritmo de vuelta atrás, hay posibilidad de que se pueda generar un cuadrante.
		  Si con estas primeras comprobaciones parece posible la generación, se ejecuta el algoritmo*/
	//las comprobaciones no son necesarias
		//if (comprobaciones(cuadAux, e1, dia))
			hecho=vueltaAtrasMarcaje(e1,e2,0,dia,cuadAux);
		
		//no ha habido solucion con los disponibles, avisamos al usuario del problema surgido
		//if (!hecho) 
			//avisarUsuario (cuadAux, e2, dia);
	}
	
	/**
	 * Metodo para avisar al usuario de que no se ha podido generar un cuadrante con los fijos, rotatorios y disponibles.
	 * El usuario deberá elegir entre modificar el horario manualmente, llamar a alguien de reserva, o contratar a alguien.
	 * @param cuadAux el cuadrante con los fijos, rotatorios y disponibles ya incluidos.
	 * @param reserDia es el ArrayList de los empleados que no trabajan el día para el que se genera el cuadrante
	 * @param dia es el dia para el que se esta generando el cuadrante.
	 */
	/*private void avisarUsuario (ArrayList<Trabaja>[] cuadAux, ArrayList<Empleado> reserDia, int dia){
		
	}*/
	
	/**
	 * Metodo para comprobar, sin ejecutar ningún algoritmo, si es posible generar un cuadrante con los empleados de que se dispone
	 * @param cuadAux Cuadrante con los fijos y rotatorios ya incluidos
	 * @param dispoDia ArrayList de empleados que trabajan el dia para el que se genera el cuadrante 
	 * @param dia Dia para el que se esta generando el cuadrante
	 */
	private boolean comprobaciones (ArrayList<Trabaja>[]cuadAux, ArrayList<Empleado> dispoDia, int dia) {
		Empleado empleado;
		/*compruebaNumEmpleados sera false si para alguna franja horaria se necesita un minimo de 
		empleados superior al numero de empleados de que disponemos.*/  
		boolean compruebaNumEmpleados=true;
		
		//minHorasDia es el array de los minimos para cada hora del departamento
		int[] minHorasDia = new int[24];
		for (int i=0;i<24;i++)
			minHorasDia[i] = estruc.getCalendario().getMinHora(dia, i);
		
		//tam es el numero de horas en las que el departamento esta abierto
		int tam=0, num=0;
		for (int i=0;i<24;i++){
			num=estruc.getCalendario().getMinHora(dia, i);
			if (num>0) tam++;
		}
		
		/*minHoras es el array donde se guarda el minimo de cada hora en las que el departamento esta abierto,
		  es igual que minHorasDia suprimiendo las horas para las que min=0*/
		int[] minHoras=new int[tam];
		int j=0;
		for (int i=0;i<tam;i++){
			boolean enc=false;
			while (j<24 && !enc) {
				if (minHorasDia[j]>0) {
					minHoras[i]=minHorasDia[j];
					enc=true;
				}
				j++;
			}
		}	
		
		/*div es el numero de posiciones de los arrays que utilizaremos en esta funcion.
		  div equivale al numero de horas en las que el departamento esta abierto contabilizadas de 5 en 5min.*/
		int div=tam*12;
		
		/*minMinutos es el array donde se guarca el minimo de cada division de 5min en las que el departamento esta abierto,
		  es igual que minHora repetida cada posicion 12 veces (12 es el numero de divisiones de 1hora en 5min)*/
		int[] minMinutos=new int[div];
		for (int i=0;i<tam;i++)
			for (int k=0;k<12;k++)
				minMinutos[i*12+k]=minHoras[i];
		
		/*empleadosFranja permite conocer el numero de empleados necesarios cada 5min teniendo en 
		cuenta que ya han sido incluidos los fijos y rotatorios en el cuadrante.*/
		int[] empleadosFranja=new int[div]; 
		
		//empleadoMin guarda el numero de posibilidades que hay de que un empleado trabaje a cada de las divisiones de 5min
		int[] empleadoMin=new int[div]; 
		for (int i=0;i<div;i++)
			empleadoMin[i]=0;
		
		ArrayList<Turno> turnosEmpleado;
		Turno turnoEmpl;
		/*comprueba si el numero de empleados fijos y rotatorios (ya incluidos en el cuadrante) 
		es suficiente para cubrir las necesidades de los minimos*/ 
		for (int i=0;i<div;i++) {
			//min fijado para cada 5min
			empleadosFranja[i]=minMinutos[i];
			//comprueba si el numero de empleados del departamento es mayor que el minimo de cada franja.
			if (empleadosFranja[i]-contarEmpleadosMin(cuadAux[dia],i,minHorasDia,dia)<listaE.size()) 
				compruebaNumEmpleados=false;
			else {
				//al minimo necesario para cada 5min se restan los empleados fijos y rotatorios ya incluidos en el cuadrante
				empleadosFranja[i]=empleadosFranja[i]-contarEmpleadosMin(cuadAux[dia],i,minHorasDia,dia);
				//se resta cada empleado en cada una de las divisiones de 5min en las que hay posibilidad de que trabaje en cualquiera de sus turnos
				for (int k=0;k<dispoDia.size();k++) {
					empleado=dispoDia.get(k);	
					turnosEmpleado=controlador.getListaTurnosContrato(empleado.getEmplId());
					for (int l=0;l<turnosEmpleado.size();l++) {
						turnoEmpl=turnosEmpleado.get(l);
						int h=i/12; //h nos permite utilizar el array minHoras, es la hora "en punto" a la que pertenece el minuto que buscamos
						int m=i-h*12; //m es el minuto dentro de la hora h que buscamos
						int hora=0, aux=0;
						boolean enc=false;
						while (aux<24 && !enc) {
							if (minHorasDia[aux]>0) hora=aux+h;
							aux++;
						}
						if (turnoEmpl.getHoraEntrada().getHours()<=hora && turnoEmpl.getHoraSalida().getHours()>hora &&
							turnoEmpl.getHoraEntrada().getMinutes()<=m && turnoEmpl.getHoraSalida().getMinutes()>m)
							empleadoMin[i]++;
					}
					/*si en algun turno el empleado puede trabajar a la hora i, se resta de empleadosFranja[i] 
					indicando que al menos él puede trabajar a esa hora*/
					for (int l=0;l<div;l++) 
						if (empleadoMin[l]>0)
							empleadosFranja[l]--;
				}
			}
		}
		int i=0;
		while (compruebaNumEmpleados && i<div) {
			if (empleadosFranja[i]>0) compruebaNumEmpleados=false;
			i++;
		}
		return compruebaNumEmpleados;
	}
	
	/**
	 * Metodo para ordenar los ArrayList por orden de felicidad y convertirlas en array
	 * @param lista ArrayList de empleados para ordenar y convertir en Array
	 * @param criterio Criterio de ordenacion: 1 = de menor a mayor, 2= de mayor a menor
	 */
	private ArrayList<Empleado> ordenarLista(ArrayList<Empleado> lista,int criterio) {
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
	 * Metodo basado en el algoritmo de vuelta atrás con marcaje en el que, una vez colocadas la lista de empleados 
	 * disponibles en el orden deseado, se prueba si colocando a los empleados en los turnos que prefieren se puede 
	 * generar un cuadrante que cumpla los requisitos pedidos en cuanto al mínimo y máximo de empleados por hora en 
	 * el departamento
	 * @param dispo Lista de empleados disponibles
	 * @param reser Lista de empleados de reserva
	 * @param k Parametro para la recursion
	 * @param dia Dia para el que estamos generando el cuadrante
	 */
	private boolean vueltaAtrasMarcaje (ArrayList<Empleado> dispo, ArrayList<Empleado> reser, int k, int dia, ArrayList<Trabaja>[] cuadAux){
		/*fHoraria es un ArrayList con todos los turnos en los que puede trabajar el empleado situado en la 
		 posición k de disponibles*/ 
		ArrayList<Turno> fHoraria = controlador.getListaTurnosContrato (dispo.get(k).getEmplId());
		// turno favorito del empleado
		int tFavorito = dispo.get(k).getTurnoFavorito();
		int i=0;
		boolean enc=false;
		while (i<fHoraria.size() && !enc) {
			if (fHoraria.get(i).getIdTurno()==tFavorito) enc=true;
			else i++;
		}
		fHoraria.add(0, fHoraria.get(i)); //inserta el turno favorito en la primera posicion de la lista
		fHoraria.remove(i+1); //elimina el turno favorito de la posicion inicial en la que estaba (i+1 porque ha quedado desplazado)
		ArrayList<Time> fHorariasDpto = estruc.getTrozosHorario();
		Turno franjaHoraria;
		boolean hecho=false;
		while (fHoraria.size()!=0) {
			franjaHoraria = fHoraria.get(0);
			ponerEmpleado (dispo.get(k), franjaHoraria.getHoraEntrada(), franjaHoraria.getHoraSalida(), cuadAux[dia]);
			//si el turno en el que se incluye al empleado en el cuadrante es el que él prefiere, aumenta su felicidad
			if (franjaHoraria.getIdTurno()==tFavorito) 
				dispo.get(k).setFelicidad(dispo.get(k).getFelicidad()+1);
			k=k+1;      
			if  (k==dispo.size())
				if  (comprobarFranjasCompletas(cuadAux, dia, fHorariasDpto))
					hecho=true;
			else
				if (k<dispo.size())
					hecho=vueltaAtrasMarcaje(dispo, reser,k,dia,cuadAux);
			k=k-1;
			if (fHoraria.size()>1) {
				quitarEmpleado(dispo.get(k),cuadAux[dia]);
				/*si al recolocar a un empleado en un turno diferente, el turno del que se le quita es el que él prefiere, 
				su felicidad queda igual que estaba antes de ejecutar el algoritmo*/
				if (franjaHoraria.getIdTurno()==tFavorito)
					dispo.get(k).setFelicidad(dispo.get(k).getFelicidad()-1);
			}
			fHoraria.remove(0);
		}
		return hecho;
	}

	/**
	 * Metodo para comprobar que todas las franjas horarias de un dia cumplen los requisito de personal
	 * @param cuadDia Cuadrante
	 * @param dia Dia en el que queremos hacer la comprobacion
	 * @param fHoraria Lista de franjas en que se divide un dia
	 */
	private boolean comprobarFranjasCompletas(ArrayList<Trabaja>[] cuadAux,int dia, ArrayList<Time> fHoraria){
		boolean valido=true;
		//bucle que recorre todas las franjas horarias de este dia
		for (int i=0;(i<(fHoraria.size()-1))&&(valido);i++){
			Time timeIni=fHoraria.get(i);
			Time timeFin=fHoraria.get(i+1);
			int horaIni=timeIni.getHours();
			int horaFin=timeFin.getHours()+1;
			Date fecha=new Date(anio, mes, dia);
			//bucle que recorre todas las horas de una franja
			for (int j=horaIni;j<=horaFin;j++){
				for (int min=0;min<60;min++){
					if (estruc.getCalendario().getMinHora(dia,j)>contarEmpleadosHora(cuadAux[dia],fecha,j,min)){
						valido=false;
					}	
				}				
			}
		}
		return valido;
	}
	
	/**
	 * Metodo para contar el numero de empleados que trabajan en 5min concretos indicados por el minuto de inicio 
	 * contabilizando sólo las horas en las que el departamento esta abierto
	 * @param lista Lista de trabajadores de un dia
	 * @param min Minuto de inicio a comprobar
	 */
	private int contarEmpleadosMin (ArrayList<Trabaja> lista, int min, int[] minHorasDia,int dia) {
		int h=min/12; //h nos permite utilizar el array minHoras, es la hora "en punto" a la que pertenece el minuto que buscamos
		int m=min-h*12; //m es el minuto dentro de la hora h que buscamos
		int hora=0, aux=0;
		boolean enc=false;
		while (aux<24 && !enc) {
			if (minHorasDia[aux]>0) hora=aux+h;
			aux++;
		}
		Date fecha=new Date(anio, mes, dia);
		return contarEmpleadosHora(lista,fecha,hora,m);
	}
	
	/**
	 * Metodo para contar el numero de empleados que trabajan a una hora concreta
	 * @param lista Lista de trabajadores de un dia
	 * @param hora Hora a comprobar
	 */
	private int contarEmpleadosHora(ArrayList<Trabaja> lista, Date dia, int hora, int minuto){
		int contador=0;
		int momento=hora*60+minuto;
		
		for(int i=0;i<lista.size();i++){
			
			
			int minIni=lista.get(i).getFichIni().getMinutes()+(lista.get(i).getFichIni().getHours()*60);
			int minFin=lista.get(i).getFichFin().getMinutes()+(lista.get(i).getFichFin().getHours()*60);
			
			//el metodo controlador.getTurnoEmpleadoDia DEBERIA devolver un TURNO en lugar de un INT
			Turno t=controlador.getObjetoTurnoEmpleadoDia(dia, lista.get(i).getIdEmpl());
			int minIniDescanso=(t.getHoraDescanso().getHours()*60)+t.getHoraDescanso().getMinutes();
			int minFinDescanso=minIniDescanso+t.getTDescanso();
			
			
			//comprobar si en ese minuto esta currando y no esta descansando
			if (((minIni<=momento)&&(minFin>momento))&&((minIniDescanso>momento)||(minFinDescanso<momento))){
				contador++;
			}
		}
		return contador;
	}
	
	/**
	 * Metodo para eliminar un empleado de una cuadrante de un cuadrante de un dia
	 * @param e Empleado a eliminar del cuadrante
	 * @param dia Dia para el que estamos generando el cuadrante
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
	 * @param e Cmpleado a colocar
	 * @param ini Inicio de su turno de trabajo
	 * @param fin Fin de su turno de trabajo
	 * @param dia Dia de su turno d trabajo
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

