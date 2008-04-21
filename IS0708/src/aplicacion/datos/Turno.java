package aplicacion.datos;

import java.sql.Time;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;

import aplicacion.utilidades.Util;

/**
 * Esta clase representa un turno.
 * Se corresponde con la representacion del turno en la tabla de la base de datos
 * @author Chema
 *
 */
public class Turno {
	protected int idTurno;
	protected String descripcion;
	protected Time horaEntrada;
	protected Time horaSalida;
	protected Time horaDescanso;
	protected int tDescanso; //minutos de descanso
	protected Color color;
	/**
	 * Crea una instancia nueva a partir de un turno dado
	 * @param t turno a copiar
	 */
	public Turno (Turno t){
		this.idTurno = t.getIdTurno();
		this.descripcion = t.getDescripcion();
		horaEntrada  = new Time(t.getHoraEntrada() .getTime());
		horaSalida   = new Time(t.getHoraSalida()  .getTime());
		horaDescanso = new Time(t.getHoraDescanso().getTime());
		color		 = new Color(t.getColor().getDevice(), t.getColor().getRed(), t.getColor().getGreen(), t.getColor().getBlue());
		tDescanso = t.tDescanso;
	}
	
	/**
	 * 
	 * @param idTurno  		 Identificador del turno
	 * @param descripcion    Nombre y/o datos del turno
	 * @param horaEntrada	 Hora teórica a la que debería comenzar la jornada laboral del usuario 
	 * @param horaSalida	 Hora teórica a la que debería terminar la jornada laboral del usuario
	 * @param horaDescanso	 Hora de inicio del descanso
	 * @param descanso		 Tiempo asignado a descanso (en minutos)
	 */
	public Turno(int idTurno, String descripcion, Time horaEntrada, Time horaSalida, Time horaDescanso, int descanso, Color color) {
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.horaDescanso = horaDescanso;
		tDescanso = descanso;
		this.color=color;
	}
	
	/**
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
	public Turno(int idTurno, String descripcion,String horaEntrada, String horaSalida, String horaDescanso, int descanso, Color color) {
		super();
		this.idTurno = idTurno;
		this.descripcion = descripcion;
		tDescanso = descanso;
		this.horaEntrada=Time.valueOf(horaEntrada);
		this.horaSalida=Time.valueOf(horaSalida);
		this.horaDescanso=Time.valueOf(horaDescanso);
		this.color=color;
	}
	
	public static void intercambiar(Turno t1, Turno t2){
		Turno aux = new Turno(t1);
		t1.idTurno = t2.getIdTurno();
		t1.descripcion = t2.getDescripcion();
		t1.horaEntrada  = new Time(t2.getHoraEntrada() .getTime());
		t1.horaSalida   = new Time(t2.getHoraSalida()  .getTime());
		t1.horaDescanso = new Time(t2.getHoraDescanso().getTime());
		t1.tDescanso = t2.tDescanso;
		t1.color=t2.color;
		
		t2.idTurno = aux.getIdTurno();
		t2.descripcion = aux.getDescripcion();
		t2.horaEntrada  = new Time(aux.getHoraEntrada() .getTime());
		t2.horaSalida   = new Time(aux.getHoraSalida()  .getTime());
		t2.horaDescanso = new Time(aux.getHoraDescanso().getTime());
		t2.tDescanso = aux.tDescanso;
		t2.color=aux.color;

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
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	public String getNombreColor() {
		return Util.ColorAHex(color);
	}
}