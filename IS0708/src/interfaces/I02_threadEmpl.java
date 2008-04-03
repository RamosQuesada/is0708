package interfaces;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import algoritmo.Trabaja;
import aplicacion.datos.Turno;
import aplicacion.utilidades.Util;

/**
 * Clase que implementa el thread que dado una vista de cuadrante empleado, le introduce
 * los valores a mostrar.
 */
public class I02_threadEmpl extends Thread{
	
	/**
	 * indica si debemos finalizar la ejecucion del thread lo antes posible
	 */
	private boolean finalizar=false;
	

	
	/**
	 * Cuadrante al que le vamos a pasar
	 */
	private I02CuadranteEmpleado cuadrante;
	
	/**
	 * Indica si hay algún otro thread corriendo 
	 */
	private static boolean corriendo=false;
	
	/**
	 * Constructor del thread de Empleado, que pide el los horarios de un empleado
	 * @param cuadrante
	 */
	public I02_threadEmpl(I02CuadranteEmpleado cuadrante){
		this.cuadrante=cuadrante;
	}
	

	/**
	 * Método que sobreescribe el metodo run que se ejecuta al iniciar el thread
	 */
	public synchronized void run(){
		/*
		 * mientras este corriendo otro thread hacemos q el nuevo espere 
		 */
		while(corriendo){
			try {
				wait(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Si ha llegado aqui, es que es el único thread que esta ejecutando, con lo 
		 * cual corriendo == false, luego lo ponemos a true
		 */
		corriendo=true;
		
		/*
		 * No redibujar, hasta que este completado
		 */
		this.cuadrante.ponRedibujar(false);
		
		
		/*
		 * ponemos la fecha actual
		 */
		Date fecha=cuadrante.dameFecha();
		if(fecha==null){
		fecha=new Date(System.currentTimeMillis());}
			
			
		/*
		 * cogemos la fecha seleccionada
		 */
		GregorianCalendar calendario = new GregorianCalendar();	
		calendario.set(GregorianCalendar.DAY_OF_MONTH, fecha.getDate());
		calendario.set(GregorianCalendar.MONTH, convertir(fecha.getMonth()));
		calendario.set(GregorianCalendar.YEAR, fecha.getYear());
			
		int numDias=0;
		int mes_inicial=calendario.get(GregorianCalendar.MONTH);
		calendario.setFirstDayOfWeek(GregorianCalendar.SUNDAY);
		while((calendario.get(GregorianCalendar.DAY_OF_WEEK)!=GregorianCalendar.FRIDAY)&&(!finalizar)){
			if(calendario.get(GregorianCalendar.DATE)==1){
				calendario.roll(GregorianCalendar.MONTH, -1);
			}
			calendario.roll(GregorianCalendar.DATE, -1);
			numDias++;
		}

		int mes_final=calendario.get(GregorianCalendar.MONTH);
		boolean cambio=false;
		if(mes_inicial!=mes_final){
			cambio=true;
		}
			
		cuadrante.ponAvance(4);

		//esperando 3...
		cuadrante.ponAvance(3);
		int cont=0;
		ArrayList<Float> HorasFinAux=new ArrayList<Float>();
		ArrayList<Float> HorasInicioAux=new ArrayList<Float>();
		ArrayList<Float> HorasComienzoDescansoAux=new ArrayList<Float>();
		ArrayList<Float> HorasFinDescansoAux=new ArrayList<Float>();
		while (cont < 7 && (! finalizar)){
			//si cont > 2 esperando 2..
			if((cont>2)&&(cont<5)){
				cuadrante.ponAvance(2);
			}
			//si cont > 5 esperando 1..
			if(cont>=5){
				cuadrante.ponAvance(1);
			}
				
			/* Mantenerse esperando hasta que Esperar a que se cargue la cache */
			while((!(cuadrante.dameVista()).isCacheCargada())&&(!finalizar)){
				cuadrante.ponAvance(-1);
				try {
					 sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if((cuadrante.dameVista()).isCacheCargada()){
				
			ArrayList<Trabaja> lista_trabaja=new ArrayList<Trabaja>();
			try{
				int dia=calendario.get(GregorianCalendar.DAY_OF_MONTH);
				int ano=calendario.get(GregorianCalendar.YEAR)+1900;
				int mes=calendario.get(GregorianCalendar.MONTH);
				lista_trabaja=(cuadrante.dameVista()).getListaTrabajaDia(dia, mes+1, ano, cuadrante.dameEmpleado().getDepartamentoId());
			}
			catch(Exception e){
					
			}
				
			Trabaja trabaja=null;	
			int contador=0;
			boolean fin=false;
			while((!fin)&&(contador<lista_trabaja.size())){
				if((lista_trabaja.get(contador).getIdEmpl()==cuadrante.dameEmpleado().getEmplId())){
					trabaja=lista_trabaja.get(contador);
					fin=true;
				}
				contador++;
				}
				
			int turno=0;
			
			/* si el turno no es vacio, vemos que id tiene */
			if(trabaja!=null){
				turno= trabaja.getIdTurno();	
			}
			

			Turno tturno= (cuadrante.dameVista()).getTurno(turno);
				
			//__fin boss
			Time horaEntrada,horaSalida,horaDescanso;
			int duracionDescanso;
			Float horaEntradaFloat=0.0f;
			Float horaSalidaFloat=0.0f;
			Float horaDescansoFloat = 0.0f;
			Float finHoraDescansoFloat = 0.0f;
			if((turno!=0)&&(!finalizar)){
				int actual=0;
					
				horaEntrada=tturno.getHoraEntrada();
				horaSalida=tturno.getHoraSalida();
				horaDescanso=tturno.getHoraDescanso();
				duracionDescanso=tturno.getTDescanso();
						
				horaEntradaFloat=(float)(horaEntrada.getHours()+horaEntrada.getMinutes()/60.0f);
				horaSalidaFloat=(float)(horaSalida.getHours()+horaSalida.getMinutes()/60.0f);
				horaDescansoFloat=(float)(horaDescanso.getHours()+horaDescanso.getMinutes()/60.0f);
				finHoraDescansoFloat = (float)(horaDescansoFloat + ((float)(duracionDescanso)/60));
									
				HorasFinAux.add(cont,horaSalidaFloat);
				HorasInicioAux.add(cont,horaEntradaFloat);
				HorasComienzoDescansoAux.add(cont,horaDescansoFloat);
				HorasFinDescansoAux.add(cont,finHoraDescansoFloat);

				}
				else{
					HorasFinAux.add(cont,0.0f);
					HorasInicioAux.add(cont,0.0f);
					HorasComienzoDescansoAux.add(cont,0.0f);
					HorasFinDescansoAux.add(cont,0.0f);
				}
			
		}
				cont++;
				
				calendario.add(GregorianCalendar.DATE, 1);
		}
			corriendo=false;
			
			if(!finalizar){
				cuadrante.ponAvance(0);
				cuadrante.ponHorasComienzoDescanso(HorasComienzoDescansoAux);
				cuadrante.ponHorasFin(HorasFinAux);
				cuadrante.ponHorasFinDescanso(HorasFinDescansoAux);
				cuadrante.ponHorasInicio(HorasInicioAux);
				cuadrante.ponRedibujar(true);
			}
			
	}

	/**
	 * Clase que dado un mes en numero, devuelve el correspondiente para un gregorian calendar
	 * @param mes mes que queremos convertir a gregorian calendar
	 * @return mes en gregorian calendar
	 */
	public int convertir(int mes){
		
		int devolver=0;
		if(mes==0){devolver= GregorianCalendar.JANUARY;}
		if(mes==1){devolver= GregorianCalendar.FEBRUARY;}
		if(mes==2){devolver= GregorianCalendar.MARCH;}
		if(mes==3){devolver= GregorianCalendar.APRIL;}
		if(mes==4){devolver= GregorianCalendar.MAY;}
		if(mes==5){devolver= GregorianCalendar.JUNE;}
		if(mes==6){devolver= GregorianCalendar.JULY;}
		if(mes==7){devolver= GregorianCalendar.AUGUST;}
		if(mes==8){devolver= GregorianCalendar.SEPTEMBER;}
		if(mes==9){devolver= GregorianCalendar.OCTOBER;}
		if(mes==10){devolver= GregorianCalendar.NOVEMBER;}
		if(mes==11){devolver= GregorianCalendar.DECEMBER;}
		
		return devolver;
	}
	
	/**
	 * metodo que indica que se deve finalizar la ejecucion de un determinado thread, esto es, 
	 * termina la ejecución tratando de no calcular nada mas, y liberando para que se puede crear otro
	 */
	public void ponFinalizar(){
		finalizar=true;
	}
}
