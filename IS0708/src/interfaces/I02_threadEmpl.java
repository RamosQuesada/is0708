package interfaces;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.graphics.GC;

import aplicacion.Util;

public class I02_threadEmpl extends Thread{
	
	private I02CuadranteEmpleado cuadrante;
	private I02_cuadrEmpl cuadranteSup;
	private GC gc;
	private static boolean corriendo=false;
	public I02_threadEmpl(I02CuadranteEmpleado cuadrante,I02_cuadrEmpl cuadrSup,GC gc){
		this.cuadrante=cuadrante;
		this.cuadranteSup=cuadrSup;
		this.gc=gc;
	}
	
	public synchronized void run(){
		System.out.println("thread");
		
		while(corriendo){try {
			sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		corriendo=true;
			Date fechaActual;
			cuadrante.horasFin= new ArrayList<Float>();
			cuadrante.horasInicio = new ArrayList<Float>();
			cuadrante.horaFinDescanso = new ArrayList<Float>();
			cuadrante.horaComienzoDescanso =  new ArrayList<Float>();
			if(cuadrante.fecha==null){
			cuadrante.fecha=new Date(System.currentTimeMillis());}
			GregorianCalendar calendario = new GregorianCalendar();
			//System.out.println(ahoraCal.getClass());
			//calendario.setFirstDayOfWeek(calendario.MONDAY);
			//calendario.set(fecha.getYear(),fecha.getMonth(),fecha.getDate());
			//calendario.setGregorianChange(fecha);
		//	System.out.println("pruebasel" +Util.dateAString(fecha));
			
	
			calendario.set(GregorianCalendar.DAY_OF_MONTH, cuadrante.fecha.getDate());
			calendario.set(GregorianCalendar.MONTH, cuadrante.fecha.getMonth());
			calendario.set(GregorianCalendar.YEAR, cuadrante.fecha.getYear());
	//		System.out.println(calendario.get(GregorianCalendar.DAY_OF_WEEK));
			int numDias=0;
			while(calendario.get(GregorianCalendar.DAY_OF_WEEK)!=6){
				calendario.add(Calendar.DATE, -1);
				numDias++;
			}
			cuadrante.tiposTurno= cuadrante.vista.getControlador().getListaTurnosEmpleados();
			for(int cont=0;cont<7;cont++){
				cuadrante.fecha= Date.valueOf(Util.aFormatoDate(Integer.toString(
					calendario.get(GregorianCalendar.YEAR)),
					Integer.toString(
						calendario.get(GregorianCalendar.MONTH)+1),
					Integer.toString(
						calendario.get(GregorianCalendar.DATE)+cont)
					));
	
			//	System.out.println("FECHA REAL:"+fecha);
				//System.out.println(Util.dateAString(fecha));
				int turno = cuadrante.vista.getControlador().getTurnoEmpleadoDia(cuadrante.fecha, cuadrante.empleado.getEmplId());
				
				Time horaEntrada,horaSalida,horaDescanso;
				int duracionDescanso;
				Float horaEntradaFloat=0.0f;
				Float horaSalidaFloat=0.0f;
				Float horaDescansoFloat = 0.0f;
				Float finHoraDescansoFloat = 0.0f;
				if(turno==0){System.out.println("vacio");}
				if(turno!=0){
					
					System.out.println("turno no vacio");
					int actual=0;
					
					while (turno!=cuadrante.tiposTurno.get(actual).getIdTurno())actual++;
					if(cuadrante.tiposTurno.get(actual).getIdTurno()==turno){
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
					}
				}
				else{
					cuadrante.horasInicio.add(cont,0.0f);
					cuadrante.horasFin.add(cont,0.0f);
					cuadrante.horaComienzoDescanso.add(cont,0.0f);
					cuadrante.horaFinDescanso.add(cont,0.0f);
				}
		}
			
			this.cuadrante.redibujar=true;
		corriendo=false;
	}

}
