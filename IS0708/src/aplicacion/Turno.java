package aplicacion;

import java.sql.Time;
import java.util.ArrayList;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import aplicacion.FranjaDib;
import aplicacion.Posicion;

/**
 * Esta clase representa un turno.
 * Se corresponde con la representaci�n del turno en la tabla de la base de datos
 * @author Chema
 *
 */
public class Turno {
	private int idTurno;
	private String descripcion;
	private Time horaEntrada;
	private Time horaSalida;
	private Time horaDescanso;
	private int tDescanso;//minutos de descanso
	public ArrayList<FranjaDib> franjas;
	
	/**
	 * Constructor por defecto
	 *
	 */
	public Turno() {
		franjas = new ArrayList<FranjaDib>();	
	}
	
	/**
	 * 
	 * @param idTurno  		 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno 
	 * @param horaEntrada	 Hora te�rica a la que deber�a comenzar la jornada laboral del usuario 
	 * @param horaSalida	 Hora te�rica a la que deber�a terminar la jornada laboral del usuario
	 * @param horaDescanso	 Hora de inicio del descanso
	 * @param descanso		 Tiempo asignado a descanso (en minutos)

	 */
	public Turno(int idTurno, String descripcion, Time horaEntrada, Time horaSalida, Time horaDescanso, int descanso) {
		super();
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.horaDescanso = horaDescanso;
		tDescanso = descanso;
		
	}
	
	/**
	 * 
	 * @param idTurno  	 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno 
	 * @param horaEntrada	 Hora te�rica a la que deber�a comenzar la jornada laboral del usuario 
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param horaSalida	 Hora te�rica a la que deber�a terminar la jornada laboral del usuario
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param horaDescanso	 Hora de inicio del descanso
	 * 						 (en String recuperado de la BBDD con JDBC)
	 * @param descanso		 Tiempo asignado a descanso (en minutos)
	 */
	public Turno(int idTurno, String descripcion,String horaEntrada, String horaSalida, String horaDescanso, int descanso) {
		super();
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		tDescanso = descanso;
		this.horaEntrada=Time.valueOf(horaEntrada);
		this.horaSalida=Time.valueOf(horaSalida);
		this.horaDescanso=Time.valueOf(horaDescanso);
	}
	/**
	 * Prueba, algoritmo
	 * @param id
	 * @deprecared
	 */
	public Turno(int id){
		this.idTurno = id;
		
	}
	
	/**
	 * 
	 * @param t	horas y minutos
	 * @return  devuelve la hora del tiempo introducido
	 */
	public long obtenHoras(Time t){
		   //obtenemos los segundos
		   long segundos = t.getTime() / 1000;		 
		   //obtenemos las horas
		   long horas = segundos / 3600;		 
		   //restamos las horas para continuar con minutos
		   segundos -= horas*3600;		 
		   //igual que el paso anterior
		   long minutos = segundos /60;
		   segundos -= minutos*60;
		   return horas;		
	}
	/**
	 * 
	 * @param t  horas y minutos en formato Time
	 * @return	 devuelve los minutos de la hora introducida
	 */
	public long obtenMinutos(Time t){
		   //obtenemos los segundos
		   long segundos = t.getTime() / 1000;		 
		   //obtenemos las horas
		   long horas = segundos / 3600;		 
		   //restamos las horas para continuar con minutos
		   segundos -= horas*3600;		 
		   //igual que el paso anterior
		   long minutos = segundos /60;
		   segundos -= minutos*60;
		   return minutos;		
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Time getHoraDescanso() {
		return horaDescanso;
	}
	public void setHoraDescanso(Time horaDescanso) {
		this.horaDescanso = horaDescanso;
	}
	public Time getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(Time horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public Time getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(Time horaSalida) {
		this.horaSalida = horaSalida;
	}
	public int getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(int idTurno) {
		this.idTurno = idTurno;
	}
	public int getTDescanso() {
		return tDescanso;
	}
	public void setTDescanso(int descanso) {
		tDescanso = descanso;
	}
	public void franjaNueva (Posicion pinicio, Posicion pfin) {
		FranjaDib f = new FranjaDib(pinicio, pfin);
		franjas.add(f);
	}
	public void quitarFranja (FranjaDib franja) {
		franjas.remove(franja);
	}
	public Boolean contienePunto (int y, int posV, int margenSup, int sep_vert_franjas, int alto_franjas) {
		Boolean b = false;
		if (y > margenSup+(sep_vert_franjas+alto_franjas)*(posV+1) && y<=margenSup+(sep_vert_franjas+alto_franjas)*(posV+2)) b = true;
		return b;
	}
	public void dibujarFranjas(Display display, GC gc, int posV, Color color, int margenIzq, int margenNombres, int margenSup, int sep_vert_franjas, int alto_franjas) {
		int subDivs = 0;
		for (int i=0; i<franjas.size(); i++) {
			franjas.get(i).dibujarFranja(display, gc, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),color);
			subDivs += (franjas.get(i).pfin.dameHora() - franjas.get(i).pinicio.dameHora())*12;
			subDivs += (franjas.get(i).pfin.dameCMin() - franjas.get(i).pinicio.dameCMin());
		}
	}
}

