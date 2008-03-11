package interfaces;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import algoritmo.Trabaja;
import aplicacion.Turno;
import aplicacion.Util;

public class I02_threadEmpl extends Thread{
	
	private boolean finalizar=false;
	public void ponFinalizar(){
		finalizar=true;
	}
	private I02CuadranteEmpleado cuadrante;
	private static boolean corriendo=false;
	public I02_threadEmpl(I02CuadranteEmpleado cuadrante){
		this.cuadrante=cuadrante;
	}
	
	public synchronized void run(){
		while(corriendo){try {
			
			wait(100);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		corriendo=true;
		this.cuadrante.ponRedibujar(true);
			//Date fechaActual;

			Date fecha=cuadrante.dameFecha();
			if(fecha==null){
			fecha=new Date(System.currentTimeMillis());}
			
			
			GregorianCalendar calendario = new GregorianCalendar();	
			calendario.set(GregorianCalendar.DAY_OF_MONTH, fecha.getDate());
			calendario.set(GregorianCalendar.MONTH, convertir(fecha.getMonth()));
			calendario.set(GregorianCalendar.YEAR, fecha.getYear());
			

			
			
			//System.out.println("dia "+cuadrante.fecha.getDate()+"mes "+cuadrante.fecha.getMonth()+"año "+cuadrante.fecha.getYear());
			int numDias=0;
			int mes_inicial=calendario.get(GregorianCalendar.MONTH);
			calendario.setFirstDayOfWeek(GregorianCalendar.SUNDAY);
			while((calendario.get(GregorianCalendar.DAY_OF_WEEK)!=GregorianCalendar.FRIDAY)&&(!finalizar)){
			//	calendario.add(GregorianCalendar.DATE, -1);
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
			//__yo cuadrante.tiposTurno= cuadrante.vista.getControlador().getListaTurnosEmpleados();
			//cuadrante.tiposTurno=cuadrante.vista.getTurnos();
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
//				if(mes_inicial<mes_final){
//					cont2=cont+1;
//				}else
//				if(mes_final>mes_inicial){
//					cont2=cont+1;
//				}else{
//					cont2=cont;
//				}
				
				
		//		fecha= Date.valueOf(Util.aFormatoDate(Integer.toString(
		//			calendario.get(GregorianCalendar.YEAR)),
		//			Integer.toString(
		//				calendario.get(GregorianCalendar.MONTH)),
		//			Integer.toString(
		//				calendario.get(GregorianCalendar.DATE))
		//			));
			
				
				//ESPERA A QUE SE CARGUE LA CACHE
				while((!(cuadrante.dameVista()).isCacheCargada())&&(!finalizar)){
					
					cuadrante.ponAvance(-1);
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if((cuadrante.dameVista()).isCacheCargada()){

	//CACHE NO QUITAR MAS UNO DE MES ARRIBA...
				
				ArrayList<Trabaja> lista_trabaja=new ArrayList<Trabaja>();
				try{
					int dia=calendario.get(GregorianCalendar.DAY_OF_MONTH);
					int ano=calendario.get(GregorianCalendar.YEAR)+1900;
					int mes=calendario.get(GregorianCalendar.MONTH);
				// lista_trabaja=cuadrante.vista.getListaTrabajaDia(cuadrante.fecha.getDate(), cuadrante.fecha.getMonth()+2, 2008, cuadrante.empleado.getDepartamentoId());
				//	 lista_trabaja=(cuadrante.dameVista()).getListaTrabajaDia(fecha.getDate(), mes, ano, cuadrante.dameEmpleado().getDepartamentoId());
					 lista_trabaja=(cuadrante.dameVista()).getListaTrabajaDia(dia, mes+1, ano, cuadrante.dameEmpleado().getDepartamentoId());
					 
					
				}
				catch(Exception e){
			//		System.out.println("fecha "+cuadrante.fecha.getDate()+" "+cuadrante.fecha.getMonth()+" "+"2008");	
				}
				
			//	System.out.println("fecha "+cuadrante.fecha.getDate()+" "+cuadrante.fecha.getMonth()+" "+cuadrante.fecha.getYear());
				
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
				if(trabaja!=null){
					turno= trabaja.getIdTurno();
					
				}
				else{
					//turno vacio
				}
    
			//mio poner mas uno
		//		int turno = cuadrante.vista.getControlador().getTurnoEmpleadoDia(cuadrante.fecha, cuadrante.empleado.getEmplId());
				//__ boss
				

				
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
					
				//__yo	while ((turno!=cuadrante.tiposTurno.get(actual).getIdTurno())&&(!finalizar))actual++;
				//	cuadrante.tiposTurno.set(0, tturno);
				//	if(cuadrante.tiposTurno.get(actual).getIdTurno()==turno){
						/*
						horaEntrada=cuadrante.tiposTurno.get(actual).getHoraEntrada();
						horaSalida=cuadrante.tiposTurno.get(actual).getHoraSalida();
						horaDescanso=cuadrante.tiposTurno.get(actual).getHoraDescanso();
						duracionDescanso=cuadrante.tiposTurno.get(actual).getTDescanso();
						
						horaEntradaFloat=(float)(horaEntrada.getHours()+horaEntrada.getMinutes()/60.0f);
						horaSalidaFloat=(float)(horaSalida.getHours()+horaSalida.getMinutes()/60.0f);
						horaDescansoFloat=(float)(horaDescanso.getHours()+horaDescanso.getMinutes()/60.0f);
						finHoraDescansoFloat = (float)(horaDescansoFloat + ((float)(duracionDescanso)/60));
						cuadrante.horasInicio.add(cont,horaEntradaFloat);
						cuadrante.horasFin.add(cont,horaSalidaFloat);
						cuadrante.horaComienzoDescanso.add(cont,horaDescansoFloat);
						cuadrante.horaFinDescanso.add(cont,finHoraDescansoFloat);
						*/
						
						horaEntrada=tturno.getHoraEntrada();
						horaSalida=tturno.getHoraSalida();
						horaDescanso=tturno.getHoraDescanso();
						duracionDescanso=tturno.getTDescanso();
						
						horaEntradaFloat=(float)(horaEntrada.getHours()+horaEntrada.getMinutes()/60.0f);
						horaSalidaFloat=(float)(horaSalida.getHours()+horaSalida.getMinutes()/60.0f);
						horaDescansoFloat=(float)(horaDescanso.getHours()+horaDescanso.getMinutes()/60.0f);
						finHoraDescansoFloat = (float)(horaDescansoFloat + ((float)(duracionDescanso)/60));
						//cuadrante.horasInicio.add(cont,horaEntradaFloat);
						
						HorasFinAux.add(cont,horaSalidaFloat);
						HorasInicioAux.add(cont,horaEntradaFloat);
						HorasComienzoDescansoAux.add(cont,horaDescansoFloat);
						HorasFinDescansoAux.add(cont,finHoraDescansoFloat);

						//cuadrante.horaComienzoDescanso.add(cont,horaDescansoFloat);
						//cuadrante.horaFinDescanso.add(cont,finHoraDescansoFloat);
				//	}
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
}
