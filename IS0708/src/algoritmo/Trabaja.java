package algoritmo;

import java.util.ArrayList;
import java.sql.Time;
import aplicacion.*;

/**
 * Esta clase representa el tipo de datos que llevara el cuadrante en cada posicion,es decir,
 * para cada dia y para cada hora/turno habra una idEmpleado,horaIni y HoraFin.
 * @author DavidMartin
 *
 */
public class Trabaja {

	private int IdEmpl;//id del empleado
	private Time FichIni;//Fichaje inicial,hay que mirar bien los tipos que van a llevar las fechas
	private Time FichFin;//Fichaje final
	
	public Trabaja(){//constructora por defecto
	}
	public Trabaja(int Id,Time Ini,Time Fin){//Otra constructora
		this.IdEmpl=Id;
		this.FichIni=Ini;
		this.FichFin=Fin;
		
	}
	public int getIdEmpl() {
		return IdEmpl;
	}
	public void setIdEmpl(int idEmpl) {
		IdEmpl = idEmpl;
	}
	public Time getFichIni() {
		return FichIni;
	}
	public void setFichIni(Time fichIni) {
		FichIni = fichIni;
	}
	public Time getFichFin() {
		return FichFin;
	}
	public void setFichFin(Time fichFin) {
		FichFin = fichFin;
	}
	
	
}
