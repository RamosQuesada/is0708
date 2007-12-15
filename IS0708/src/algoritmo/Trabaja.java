package algoritmo;

import java.util.ArrayList;
import java.sql.Time;
import aplicacion.*;

/**
 * Esta clase representa el tipo de datos que llevara el cuadrante en cada posicion,es decir,
 * para cada dia y para cada hora/turno habra una idEmpleado,horaIni,HoraFin e idTurno.
 * @author DavidMartin
 *
 */
public class Trabaja {

	private int IdEmpl;//id del empleado
	private Time FichIni;//Fichaje inicial,hay que mirar bien los tipos que van a llevar las fechas
	private Time FichFin;//Fichaje final
	private int idTurno;//Identificador del turno
	
	public Trabaja(){//constructora por defecto
	}
	public Trabaja(int Id,Time Ini,Time Fin,int Turno){//Otra constructora
		this.IdEmpl=Id;
		this.FichIni=Ini;
		this.FichFin=Fin;
		this.idTurno=Turno;
		
	}
	/**
	 * Recupera el numero de identificacion del empleado
	 * @return id empleado
	 */
	public int getIdEmpl() {
		return IdEmpl;
	}
	/**
	 * Cambia el numero de identificacion del empleado
	 * @param id empleado
	 */
	public void setIdEmpl(int idEmpl) {
		IdEmpl = idEmpl;
	}
	/**
	 * Recupera la hora inicial a la que ficho el empleado
	 * @return FichIni
	 */
	public Time getFichIni() {
		return FichIni;
	}
	/**Cambia la hora inicial a la que ficho el empleado
	 * @param fichIni
	 */
	public void setFichIni(Time fichIni) {
		FichIni = fichIni;
	}
	/**
	 * Recupera la ultima hora a la que ficho el empleado
	 * @return fichFin
	 */
	public Time getFichFin() {
		return FichFin;
	}
	/**Cambia la ultima hora a la que ficho el empleado
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
	/**Cambia el identificador del Turno
	 * @param idTurno
	 */
	public void setIdTurno(int idTurno) {
		this.idTurno = idTurno;
	}
	
	
}
