package interfaces;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.graphics.GC;

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
			Date fechaActual;
			cuadrante.ponHorasFin(new ArrayList<Float>());
			cuadrante.ponHorasInicio(new ArrayList<Float>());
			//cuadrante.horaFinDescanso = new ArrayList<Float>();
			cuadrante.ponHorasFinDescanso(new ArrayList<Float>());
			cuadrante.ponHorasComienzoDescanso(new ArrayList<Float>());
			if(cuadrante.fecha==null){
			cuadrante.fecha=new Date(System.currentTimeMillis());}
			GregorianCalendar calendario = new GregorianCalendar();	
			calendario.set(GregorianCalendar.DAY_OF_MONTH, cuadrante.fecha.getDate());
			calendario.set(GregorianCalendar.MONTH, cuadrante.fecha.getMonth());
			calendario.set(GregorianCalendar.YEAR, cuadrante.fecha.getYear());
			//System.out.println("dia "+cuadrante.fecha.getDate()+"mes "+cuadrante.fecha.getMonth()+"año "+cuadrante.fecha.getYear());
			int numDias=0;
			while((calendario.get(GregorianCalendar.DAY_OF_WEEK)!=6)&&(!finalizar)){
				calendario.add(Calendar.DATE, -1);
				numDias++;
			}
			cuadrante.avance=4;
			//__yo cuadrante.tiposTurno= cuadrante.vista.getControlador().getListaTurnosEmpleados();
			//cuadrante.tiposTurno=cuadrante.vista.getTurnos();
			//esperando 3...
			cuadrante.avance=3;
			int cont=0;
			while (cont < 7 && (! finalizar)){
				//si cont > 2 esperando 2..
				if((cont>2)&&(cont<5)){
					cuadrante.avance=2;
				}
				//si cont > 5 esperando 1..
				if(cont>=5){
					cuadrante.avance=1;
				}
				cuadrante.fecha= Date.valueOf(Util.aFormatoDate(Integer.toString(
					calendario.get(GregorianCalendar.YEAR)),
					Integer.toString(
						calendario.get(GregorianCalendar.MONTH)),
					Integer.toString(
						calendario.get(GregorianCalendar.DATE)+cont)
					));

				
				//ESPERA A QUE SE CARGUE LA CACHE
				while((!(cuadrante.dameVista()).isCacheCargada())&&(!finalizar)){
					
					cuadrante.avance=-1;
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
					
				// lista_trabaja=cuadrante.vista.getListaTrabajaDia(cuadrante.fecha.getDate(), cuadrante.fecha.getMonth()+2, 2008, cuadrante.empleado.getDepartamentoId());
					 lista_trabaja=(cuadrante.dameVista()).getListaTrabajaDia(cuadrante.fecha.getDate(), cuadrante.fecha.getMonth()+2, cuadrante.fecha.getYear()+1900, cuadrante.empleado.getDepartamentoId());
				}
				catch(Exception e){
			//		System.out.println("fecha "+cuadrante.fecha.getDate()+" "+cuadrante.fecha.getMonth()+" "+"2008");	
				}
				
			//	System.out.println("fecha "+cuadrante.fecha.getDate()+" "+cuadrante.fecha.getMonth()+" "+cuadrante.fecha.getYear());
				
				Trabaja trabaja=null;	
				int contador=0;
				boolean fin=false;
				while((!fin)&&(contador<lista_trabaja.size())){
					if((lista_trabaja.get(contador).getIdEmpl()==cuadrante.empleado.getEmplId())){
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
						
						cuadrante.anadeHoraInicio(cont, horaEntradaFloat);
						cuadrante.anadeHoraFin(cont, horaSalidaFloat);
						cuadrante.anadeHoraComienzoDescanso(cont, horaDescansoFloat);
						cuadrante.anadeHoraFinDescanso(cont, finHoraDescansoFloat);
						//cuadrante.horaComienzoDescanso.add(cont,horaDescansoFloat);
						//cuadrante.horaFinDescanso.add(cont,finHoraDescansoFloat);
				//	}
				}
				else{
					cuadrante.anadeHoraInicio(cont, 0.0f);
					cuadrante.anadeHoraFin(cont, 0.0f);
					cuadrante.anadeHoraComienzoDescanso(cont, 0.0f);
					cuadrante.anadeHoraFinDescanso(cont, 0.0f);
//					cuadrante.horaFinDescanso.add(cont,0.0f);
				}
			cont++;
		}
		}
			corriendo=false;
			
			if(!finalizar){
				cuadrante.avance=0;
				cuadrante.ponRedibujar(true);
			}
			
	}

}
