package algoritmo;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import aplicacion.*;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;
import aplicacion.datos.Turno;
import aplicacion.utilidades.Util;


/**
 * Esta clase contiene los algoritmos que vamos a utilizar para el calculo automatico de un cuadrante
 * @author grupoAlgoritmo 
 *
 */
public class TurnoMatic {
	
	private Cuadrante cuadrante;
	private Vista vista;
	private Estructura estruc;
	//private int diaIni;
	private int mes;
	private int anio;
	private String idDepartamento;
	private Controlador controlador;
	private ArrayList<Empleado> listaE;
	private ArrayList<Contrato> contratosDep;
	private ArrayList<Turno> turnosDep;
	
	/**
	 * Constructora por defecto
	 */
	public TurnoMatic(){		
	}
		 
	/**
	 * Constructora del algoritmo, se encarga de crear la estructura
	 * el cuadrante y recibe el controlador del programa
	 * @param d Dia inicial a partir del cual se realiza el cuadrante
	 * @param m Mes para el que se realiza el cuadrante
	 * @param year Año al que pertenece el mes
	 * @param vis Cache de la aplicacion
	 * @param idDepartamento Departamento para el que se genera el cuadrante
	 */
	public TurnoMatic(/*int d, */int m, int year, Vista vis, String idDepartamento){
		this.vista = vis;
		this.controlador = vista.getControlador();
		this.idDepartamento = idDepartamento;
		this.anio = year;
		this.mes = m;
		//this.diaIni = d;
	    this.listaE = this.vista.getEmpleadosDepartamento(idDepartamento);
	    //this.listaE = this.controlador.getEmpleadosDepartamentoPruebasAlg(idDepartamento);		
		this.contratosDep = this.controlador.getListaContratosDpto(this.idDepartamento);
		this.turnosDep = this.controlador.getListaTurnosEmpleadosDpto(this.idDepartamento);		
		this.estruc = new Estructura(mes, year, controlador, idDepartamento, listaE);
		this.cuadrante = new Cuadrante(mes, year, idDepartamento);		
	}

	/**
	 * Método que ejecuta el algoritmo colocando primero los empleados con un turno fijo para el dia 
	 * para el que se genera el cuadrante, y mediante vuelta atrás coloca a los empleados que pueden trabajar 
	 * ese día con diferentes turnos 
	 * @return cuadrante deseado y las sugerencias en cuanto a 
	 * faltas de personal y necesidad de contratar más empleados
	 */
	public ResultadoTurnoMatic ejecutaAlgoritmo(){	
		ListasEmpleados[][] horario = estruc.getDias();
		ArrayList<Empleado> reser;
		ArrayList<Empleado> dispo;
		ArrayList<Empleado> empl;
		Turno turno;
		Trabaja trab;
		Contrato contAux;
		Empleado e;

		//recorremos los dias del mes
		for(int i=0/*diaIni-1*/; i<Util.dameDias(mes,anio); i++){ //FOR1
			//si el dia i no abre el centro, no se calcula el cuadrante
			if (!estruc.getCal().diaLibre(i)) {
				System.out.println(i);
				
				//dividimos en el numero de franjas de cada dia
				for(int j=0; j<estruc.getNumTrozos(); j++){ //FOR2
		
					Time inif = estruc.getTrozosHorario().get(j); 
					System.out.println(inif);
					System.out.println(inif.toString());
					Time finf = estruc.getTrozosHorario().get(j+1);
					dispo = horario[i][j].getDisponibles();
					reser = horario[i][j].getReserva();
					empl = horario[i][j].getEmpleados();
					
					//comprobamos la disponibilidad de cada empleado
					for(int k=0; k<listaE.size(); k++){ //FOR3 
						e = listaE.get(k);
						int id = e.getContratoId();
						contAux = buscaContrato(id, contratosDep);
						
						if(e.estaDisponible(i,inif,finf,controlador,contratosDep,j,estruc.getNumTrozos(),mes,anio)){
							
							if(contAux.getTipoContrato()==1 || contAux.getTipoContrato()==2){
								empl.add(e);
								turno = e.getTurnoActual();
								if (!contiene(i, e.getEmplId())) {
									trab = new Trabaja(e.getEmplId(),turno.getHoraEntrada(),turno.getHoraSalida(),turno.getIdTurno());
									cuadrante.setTrabajaDia(i, trab);
								}
							
							} else
								//(contAux.getTipoContrato()==3 || contAux.getTipoContrato()==4)
								dispo.add(e);
							
						} else
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
				colocaNoFijos(dispoDia, reserDia, emplDia, i); 
			}
		}
		vista.insertCuadrante(cuadrante);
		Resumen resumen = new Resumen(Util.dameDias(mes,anio), cuadrante, estruc);
		ResultadoTurnoMatic resultado = new ResultadoTurnoMatic(cuadrante, resumen);
		return resultado;
	}

	/**
	 * Método para colocar los empleados de tipo 3 y 4 con el algoritmo vuelta atrás
	 * @param dispo Lista de empleados disponibles que vienen del método ejecutaAlgoritmo
	 * @param reser Lista de empleados de reserva que vienen del método ejecutaAlgoritmo
	 * @param empl Lista de empleados que vienen del método ejecutaAlgoritmo
	 * @param dia Dia para el que se ejecuta el vuelta atrás
	 */
	private void colocaNoFijos (ArrayList<Empleado> dispo, ArrayList<Empleado> reser, ArrayList<Empleado> empl, int dia){
		//se ordenan las listas de disponibles y reserva segun la felicidad de los empleados		
		ArrayList<Empleado> e1=ordenarLista(dispo,1);
		ArrayList<Empleado> e2=ordenarLista(reser,2);
		
		/*hacemos unas comprobaciones iniciales que nos permiten saber si hay 
		alguna posibilidad de que haya solucion satisfactoria*/
		if (comprobaciones(dispo,dia)) {
			if (!vueltaAtrasMarcaje(e1,e2,0,dia))
				/*en caso de que la solucion no sea satisfactoria, se coloca 
				a los empleados en su turno preferido para que, al menos, aumenten su felicidad*/
				colocarPreferidos(dispo,dia);
		} else
			/*en caso de que no haya solucion posible, se coloca 
			a los empleados en su turno preferido para que, al menos, aumenten su felicidad*/
			colocarPreferidos(dispo,dia);
	}
	
	/**
	 * Método para comprobar, sin ejecutar ningún algoritmo, si es posible generar un 
	 * cuadrante con los empleados de que se dispone
	 * @param dispoDia ArrayList de empleados que trabajan el dia para el que se genera 
	 * el cuadrante y que no están todavía insertados 
	 * @param dia Dia para el que se esta generando el cuadrante
	 * @return true si hay alguna posibilidad de generar el cuadrante
	 */
	private boolean comprobaciones (ArrayList<Empleado> dispoDia, int dia) {
		Empleado empleado;
		/*compruebaNumEmpleados sera false si para alguna franja horaria se necesita un minimo de 
		empleados superior al numero de empleados de que disponemos.*/  
		boolean compruebaNumEmpleados=true;
		
		//minHorasDia es el array de los minimos para cada hora del departamento
		int[] minHorasDia = new int[24];
		for (int i=0;i<24;i++)
			//minHorasDia[i] = estruc.getCalendario().getMinHora(dia, i);
			minHorasDia[i] = estruc.getCal().getMinHora(dia, i);
		
		//tam es el numero de horas en las que el departamento esta abierto
		int tam=0, num=0;
		for (int i=0;i<24;i++){
			//num=estruc.getCalendario().getMinHora(dia, i);
			num=estruc.getCal().getMinHora(dia, i);
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
		
		/*comprueba si el numero de empleados fijos y rotatorios (ya incluidos en el cuadrante) 
		es suficiente para cubrir las necesidades de los minimos*/ 
		for (int i=0;i<div;i++) {
			//min fijado para cada 5min
			empleadosFranja[i]=minMinutos[i];
			//comprueba si el numero de empleados del departamento es mayor que el minimo de cada franja.
			if (empleadosFranja[i]-contarEmpleadosMin(cuadrante.getListaTrabajaDia(dia),i,minHorasDia,dia)>listaE.size()) 
				compruebaNumEmpleados=false;
			else {
				//al minimo necesario para cada 5min se restan los empleados fijos y rotatorios ya incluidos en el cuadrante
				empleadosFranja[i]=empleadosFranja[i]-contarEmpleadosMin(cuadrante.getListaTrabajaDia(dia),i,minHorasDia,dia);
				//si el valor obtenido menor o igual que 0 es que ya hemos completado la franja con los fijos y rotatorios
				if (empleadosFranja[i]>0) {
					//se resta cada empleado en cada una de las divisiones de 5min en las que hay posibilidad de que trabaje en cualquiera de sus turnos
					for (int k=0;k<dispoDia.size();k++) {
						if (empleadosFranja[i]>0) {
							empleado=dispoDia.get(k);	
							Contrato c = buscaContrato(empleado.getContratoId(), contratosDep);
							ArrayList<Integer> turnosEmpl = obtenerTurnosContrato(c.getPatron());
							ArrayList<Turno> turnosEmpleado = new ArrayList<Turno>();
							for (int l=0; l<turnosEmpl.size(); l++)
								turnosEmpleado.add(buscaTurno(turnosEmpl.get(l), turnosDep));
							for (int l=0;l<turnosEmpleado.size();l++) {
								if (empleadosFranja[i]>0) {
									Turno turnoEmpl=turnosEmpleado.get(l);
									int h=i/12; //h nos permite utilizar el array minHoras, es la hora "en punto" a la que pertenece el minuto que buscamos
									int min=(i-h*12)*5; //min es el minuto dentro de la hora h que buscamos
									int hora=0, aux=0;
									boolean enc=false;
									while (aux<24 && !enc) {
										if (minHorasDia[aux]>0) {
											hora=aux+h;
											enc=true;
										}
										aux++;
									}
									if (trabajaTurno(turnoEmpl, hora, min)) 
										empleadosFranja[i]--;
								}
							}
						}
					}	
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
	 * Método para ordenar los ArrayList por felicidad y convertirlos en array
	 * @param lista ArrayList de empleados para ordenar y convertir en Array
	 * @param criterio Criterio de ordenacion: 1 = de menor a mayor, 2= de mayor a menor
	 * @return ArrayList ordenado de los empleados según su felicidad 
	 */
	private ArrayList<Empleado> ordenarLista(ArrayList<Empleado> lista, int criterio) {
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
		for (int k=0; k<e1.length;k++)
			resultado.add(e1[k]);
		return resultado;
	}
	
	/**
	 * Método basado en el algoritmo de vuelta atrás con marcaje en el que, una vez colocada la 
	 * lista de empleados disponibles en el orden deseado, se prueba si colocando a los empleados 
	 * en los turnos que prefieren se puede generar un cuadrante que cumpla los requisitos pedidos 
	 * en cuanto al mínimo y máximo de empleados por hora en el departamento
	 * @param dispo Lista de empleados disponibles
	 * @param reser Lista de empleados de reserva
	 * @param k Parametro para la recursion
	 * @param dia Dia para el que estamos generando el cuadrante
	 * @return true si se ha generado un cuadrante que cumple los requisitos de minimo y maximo de personal
	 */
	private boolean vueltaAtrasMarcaje (ArrayList<Empleado> dispo, ArrayList<Empleado> reser, int k, int dia){
		/*fHoraria es un ArrayList con todos los turnos en los que puede trabajar el empleado situado en la 
		 posición k de disponibles*/
		Contrato c = buscaContrato(dispo.get(k).getContratoId(), contratosDep);
		ArrayList<Integer> turnosEmpl = obtenerTurnosContrato(c.getPatron());
		ArrayList<Turno> fHoraria = new ArrayList<Turno>();
		for (int j=0; j<turnosEmpl.size(); j++)
			fHoraria.add(buscaTurno(turnosEmpl.get(j), turnosDep));
		int tFavorito = dispo.get(k).getTurnoFavorito(); //turno favorito del empleado
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
		while (fHoraria.size()!=0) {
			franjaHoraria = fHoraria.get(0);
			ponerEmpleado (dispo.get(k), franjaHoraria.getHoraEntrada(), franjaHoraria.getHoraSalida(), franjaHoraria, dia);
			//si el turno en el que se incluye al empleado en el cuadrante es el que él prefiere, aumenta su felicidad
			if (franjaHoraria.getIdTurno()==tFavorito) 
				dispo.get(k).setFelicidad(dispo.get(k).getFelicidad()+1);
			k=k+1;      
			if  (k==dispo.size()) {
				if (comprobarFranjasCompletas(dia, fHorariasDpto))
					 return true;
			} else {
				if (k<dispo.size()) {
					if (vueltaAtrasMarcaje(dispo, reser,k,dia))
						return true;
				}
			}
			k=k-1;
			quitarEmpleado(dispo.get(k),dia);
			/*si al recolocar a un empleado en un turno diferente, el turno del que se le quita es el que él prefiere, 
			su felicidad queda igual que estaba antes de ejecutar el algoritmo*/
			if (franjaHoraria.getIdTurno()==tFavorito)
				dispo.get(k).setFelicidad(dispo.get(k).getFelicidad()-1);
			fHoraria.remove(0);
		}
		return false;
	}
	
	/**
	 * Método que coloca a los empleados en el cuadrante con su turno preferido porque el algoritmo de 
	 * vueltas atrás no encuentra solución satisfactoria
	 * @param dispo ArrayList de empleados que trabajan el dia para el que se genera el cuadrante 
	 * @param dia Dia para el que se esta generando el cuadrante
	 */
	public void colocarPreferidos(ArrayList<Empleado> dispo, int dia){
		for (int i=0;i<dispo.size();i++){
			Contrato c = buscaContrato(dispo.get(i).getContratoId(), contratosDep);
			ArrayList<Integer> turnosEmpl = obtenerTurnosContrato(c.getPatron());
			ArrayList<Turno> fHoraria = new ArrayList<Turno>();
			for (int j=0; j<turnosEmpl.size(); j++)
				fHoraria.add(buscaTurno(turnosEmpl.get(j), turnosDep));
			int tFavorito = dispo.get(i).getTurnoFavorito(); //turno favorito del empleado
			int n=0;
			boolean enc=false;
			while (n<fHoraria.size() && !enc) {
				if (fHoraria.get(n).getIdTurno()==tFavorito) enc=true;
				else n++;
			}
			Turno franjaHoraria=fHoraria.get(n);
			ponerEmpleado (dispo.get(i), franjaHoraria.getHoraEntrada(), franjaHoraria.getHoraSalida(), franjaHoraria, dia);
			dispo.get(i).setFelicidad(dispo.get(i).getFelicidad()+1);
		}
	}

	/**
	 * Método para comprobar que todas las franjas horarias de un dia cumplen los requisito de personal
	 * @param dia Dia en el que queremos hacer la comprobacion
	 * @param fHoraria Lista de franjas en que se divide un dia
	 * @return true si todas las franjas están completas
	 */
	private boolean comprobarFranjasCompletas(int dia, ArrayList<Time> fHoraria){
		boolean valido=true;
		//bucle que recorre todas las franjas horarias de este dia
		for (int i=0;(i<(fHoraria.size()-1))&&(valido);i++){
			Time timeIni=fHoraria.get(i);
			Time timeFin=fHoraria.get(i+1);
			int horaIni=timeIni.getHours();
			//si la hora final es una hora en punto, no es necesario redondear con +1
			int horaFin=timeFin.getHours();
			if (horaFin*60<timeFin.getHours()*60+timeFin.getMinutes())
				horaFin++;
			Date fecha=new Date(anio, mes, dia);
			//bucle que recorre todas las horas de una franja
			for (int j=horaIni;(j<horaFin) && valido;j++){
				for (int min=0;(min<60) && valido;min++){
					if (estruc.getCal().getMinHora(dia,j)>contarEmpleadosHora(cuadrante.getListaTrabajaDia(dia),fecha,j,min))
						valido=false;
				}				
			}
		}
		return valido;
	}
	
	/**
	 * Método para contar el numero de empleados que trabajan en 5min concretos indicados por el minuto de inicio 
	 * contabilizando sólo las horas en las que el departamento esta abierto
	 * @param lista Lista de trabajadores de un dia
	 * @param div Division a comprobar
	 * @param minHorasDia Mínimo de empleados por hora en el departamento
	 * @param dia Dia para el que se genera el cuadrante
	 * @return número de empleados mínimo en una franja de 5min
	 */
	private int contarEmpleadosMin (ArrayList<Trabaja> lista, int div, int[] minHorasDia,int dia) {		
		int h=div/12; //h nos permite utilizar el array minHoras, es la hora "en punto" a la que pertenece el minuto que buscamos
		int min=(div-h*12)*5; //min es el minuto dentro de la hora h que buscamos
		int hora=0, aux=0;
		boolean enc=false;
		while (aux<24 && !enc) {
			if (minHorasDia[aux]>0) {
				enc=true;
				hora=aux+h;
			}
			aux++;
		}		
		Date fecha=new Date(anio, mes, dia);
		return contarEmpleadosHora(lista,fecha,hora,min);
	}
	
	/**
	 * Método para contar el número de empleados que trabajan a una hora concreta
	 * @param lista Lista de trabajadores de un dia
	 * @param dia Dia para el que se genera el cuadrante
	 * @param hora Hora a comprobar
	 * @param minuto Minuto a comprobar
	 */
	private int contarEmpleadosHora(ArrayList<Trabaja> lista, Date dia, int hora, int minuto){
		int contador=0;
		int momento=hora*60+minuto;
		
		for(int i=0;i<lista.size();i++){
			
			int minIni=lista.get(i).getFichIni().getMinutes()+(lista.get(i).getFichIni().getHours()*60);
			int minFin=lista.get(i).getFichFin().getMinutes()+(lista.get(i).getFichFin().getHours()*60);
			
			//t es el identificador del turno que tiene el empleado en el cuadrante
			int t = lista.get(i).getIdTurno();
			Turno turno = buscaTurno(t, turnosDep);
			
			int minIniDescanso=(turno.getHoraDescanso().getHours()*60)+turno.getHoraDescanso().getMinutes();
			int minFinDescanso=minIniDescanso+turno.getTDescanso();
			
			//comprobar si en ese minuto esta currando y no esta descansando
			if (((minIni<=momento)&&(minFin>momento))&&((minIniDescanso>momento)||(minFinDescanso<=momento)||(turno.getTDescanso()==0))){
				contador++;
			}
		}
		return contador;
	}
	 /**
	  * Método que comprueba si un momento concreto está comprendido entre el inicio y el fin de un turno
	  * @param turnoEmpl Turno a comprobar
	  * @param hora Hora a comprobar
	  * @param minuto Minuto a comprobar
	  * @return true si la hora está dentro de los límites del turno
	  */
	private boolean trabajaTurno(Turno turnoEmpl, int hora, int minuto) {
		int momento=hora*60+minuto;
		
		int minIni = turnoEmpl.getHoraEntrada().getHours()*60+turnoEmpl.getHoraEntrada().getMinutes();
		int minFin = turnoEmpl.getHoraSalida().getHours()*60+turnoEmpl.getHoraSalida().getMinutes();
		
		int minIniDescanso = (turnoEmpl.getHoraDescanso().getHours()*60)+turnoEmpl.getHoraDescanso().getMinutes();
		int minFinDescanso = minIniDescanso+turnoEmpl.getTDescanso();
		
		if ((minIni<=momento && minFin>momento) && (minIniDescanso>momento || minFinDescanso<=momento || turnoEmpl.getTDescanso()==0)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Método para eliminar un empleado de un cuadrante de un dia
	 * @param emp Empleado a eliminar del cuadrante
	 * @param dia Dia para el que estamos generando el cuadrante
	 */
	private void quitarEmpleado (Empleado emp, int dia){
		boolean enc=false;
		int k=0;
		ArrayList<Trabaja> cuadDia = cuadrante.getListaTrabajaDia(dia);
		while ((!enc) && (k<cuadDia.size())){
			if (cuadDia.get(k).getIdEmpl()==emp.getEmplId()) {
				cuadDia.remove(k);
				enc=true;
			}
			else k++;
		}				
	}
	
	/**
	 * Método para poner un empleado en un cuadrante de un dia
	 * @param emp Empleado a colocar
	 * @param ini Inicio de su turno de trabajo
	 * @param fin Fin de su turno de trabajo
	 * @param turno Turno en el que el empleado trabajará el día indicado
	 * @param dia Dia de su turno d trabajo
	 */
	private void ponerEmpleado (Empleado emp, Time ini, Time fin, Turno turno, int dia){
		Trabaja trabaja = new Trabaja(emp.getEmplId(),ini,fin,turno.getIdTurno());
		cuadrante.setTrabajaDia(dia, trabaja);
		vista.setProgreso("Generando cuadrante, por favor espere", dia*3);
	}
	
	/**
	 * Método para comprobar si un empleado está incluido en el cuadrante un dia concreto
	 * @param dia Dia deseado
	 * @param idEmpl Empleado a comprobar
	 * @return true si el empleado trabaja el dia indicado
	 */
	private boolean contiene(int dia, int idEmpl) {
		boolean encontrado = false;
		int n = 0;
		int e = 0;
		ArrayList<Trabaja> cuadDia = cuadrante.getListaTrabajaDia(dia);
		while(!encontrado && n<cuadDia.size()){
			e = cuadDia.get(n).getIdEmpl();
			if(idEmpl == e)
				encontrado = true;			
			n++;
		}
		return encontrado;
	}

	/**
	 * Método que devuelve el contrato correspondiente al idContrato indicado
	 * @param idContrato Identificador del contrato deseado
	 * @param listaContratos Lista de todos los contratos del departamento
	 * @return el contrato buscado
	 */
	private Contrato buscaContrato(int idContrato, ArrayList<Contrato> listaContratos) {
		boolean encontrado = false;
		int n = 0;
		Contrato c = null;
		while(!encontrado && n<listaContratos.size()){
			c = listaContratos.get(n);
			if(idContrato == c.getNumeroContrato())
				encontrado = true;			
			n++;
		}
		return c;
	}

	/**
	 * Método que devuelve el turno correspondiente al idTurno indicado
	 * @param idTurno Identificador del turno deseado
	 * @param listaTurnos Lista de todos los turnos del departamento
	 * @return el turno buscado
	 */
	private Turno buscaTurno(int idTurno, ArrayList<Turno> listaTurnos) {
		boolean encontrado = false;
		int n = 0;
		Turno t = null;
		while(!encontrado && n<listaTurnos.size()){
			t = listaTurnos.get(n);
			if(idTurno == t.getIdTurno())
				encontrado = true;			
			n++;
		}
		return t;
	}
	 
	/**
	 * Método para obtener todos los turnos posibles de un contrato teniendo el patrón del contrato
	 * @param p Patrón del contrato
	 * @return ArrayList de los identificadores de los turnos del contrato
	 */
	private ArrayList<Integer> obtenerTurnosContrato(String p){
		
		ArrayList<Integer> turnos = new ArrayList<Integer>();
		String tipo;
		
		for (int i=0; i<p.length(); i++) {
			tipo = "";
			while (p.charAt(i) != ':') { 
				i++;
			}
			i++; 
			while ((i<p.length())&&(p.charAt(i) != '/')) {
				tipo = tipo + p.charAt(i);
				i++;
			}
			obtenerTurnosContratoAux(turnos, tipo);
		}		
		return turnos;
	}
	
	/**
	 * Método para obtener todos los turnos posibles de un contrato teniendo el patrón del contrato
	 * @param p Patrón del contrato
	 * @param turnos ArrayList de los identificadores de los turnos del contrato
	 */
	private void obtenerTurnosContratoAux(ArrayList<Integer> turnos, String p){
		
		String t;
		
		for (int i=0; i<p.length(); i++)
		{
			t = "";
			while ((i<p.length()) && (p.charAt(i) != ',')
					)
			{
				t = t + p.charAt(i); 
				i++;
			}
			if (!t.equals("d"))
				if (!turnos.contains(Integer.parseInt(t)))
					turnos.add(Integer.parseInt(t));
		}
	}

//---------------------------------------------------------------------------------
	
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
	
	/**
	 * Método de prueba que imprime la estructura
	 */
	public void imprimeEstructura(){
		ArrayList<Empleado> emp;
		ListasEmpleados[][] listas;
		Empleado e;
		
		listas = estruc.getDias();
		
		for(int i=0; i<listas.length; i++){
			System.out.println("Dia: " + i);
			for(int j=0;j<listas[i].length;j++){
				emp = listas[i][j].getEmpleados();
				System.out.println("Turno: " + j);
				System.out.println("Lista de empleados: ");
				for(int k=0;k<emp.size();k++){
					e = emp.get(k);
					System.out.println(e.getEmplId());
				}
			}
		}
	}

//---------------------------------------------------------------------------------

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
