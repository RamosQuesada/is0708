package algoritmo;

import java.sql.Time;

/**
 * @author DavidMartin
 * Esta clase representa el tipo de datos que llevara el cuadrante en cada posicion,es decir,
 * para cada dia y para cada hora/turno habra una idEmpleado,horaIni,HoraFin e idTurno.
 */
public class Trabaja {

	private int IdEmpl;   //id del empleado
	private Time FichIni; //fichaje inicial
	private Time FichFin; //fichaje final
	private int idTurno;  //Identificador del turno
	
	/**
	 * Constructora por defecto
	 */
	public Trabaja(){
	}
	
	/**
	 * Constructora por parámetros
	 * @param Id Identificador del empleado
	 * @param Ini Hora de inicio
	 * @param Fin Hora de fin
	 * @param Turno Turno en el que trabaja
	 */
	public Trabaja(int Id, Time Ini, Time Fin, int Turno){
		this.IdEmpl=Id;
		this.FichIni=Ini;
		this.FichFin=Fin;
		this.idTurno=Turno;
	}
	
	/**
	 * Recupera el número de identificación del empleado
	 * @return id empleado
	 */
	public int getIdEmpl() {
		return IdEmpl;
	}
	
	/**
	 * Cambia el número de identificación del empleado
	 * @param id empleado
	 */
	public void setIdEmpl(int idEmpl) {
		IdEmpl = idEmpl;
	}
	
	/**
	 * Recupera la hora inicial a la que fichó el empleado
	 * @return FichIni
	 */
	public Time getFichIni() {
		return FichIni;
	}
	
	/**
	 * Cambia la hora inicial a la que fichó el empleado
	 * @param fichIni
	 */
	public void setFichIni(Time fichIni) {
		FichIni = fichIni;
	}
	
	/**
	 * Recupera la última hora a la que fichó el empleado
	 * @return fichFin
	 */
	public Time getFichFin() {
		return FichFin;
	}
	
	/**
	 * Cambia la última hora a la que fichó el empleado
	 * @param fichFin
	 */
	public void setFichFin(Time fichFin) {
		FichFin = fichFin;
	}
	
	/**
	 * Recupera el identificador del Turno
	 * @return IdTurno
	 */
	public int getIdTurno() {
		return idTurno;
	}
	
	/**
	 * Cambia el identificador del Turno
	 * @param idTurno
	 */
	public void setIdTurno(int idTurno) {
		this.idTurno = idTurno;
	}
	
}
