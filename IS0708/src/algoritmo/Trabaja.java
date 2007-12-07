package algoritmo;

import java.util.ArrayList;

/**
 * 
 * @author DavidMartin
 *
 */
/**
 * Esta clase representa el tipo de datos que llevara el cuadrante en cada posicion,es decir,
 * para cada dia y para cada hora/turno habra una idEmpleado,horaIni y HoraFin.
 */
public class Trabaja {

	/**
	 *Id del empleado
	 */
	private int IdEmpl;
	/**
	 *Fichaje inicial
	 */
	private int FichIni;//hay que mirar bien los tipos que van a llevar las fechas
	/**
	 *Fichaje final
	 */
	private int FichFin;
	
	public Trabaja(){//constructora por defecto
		IdEmpl=0;
		FichIni=0;
		FichFin=0;
	}
	public Trabaja(int Id,int Ini,int Fin){//Otra constructora
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
	public int getFichIni() {
		return FichIni;
	}
	public void setFichIni(int fichIni) {
		FichIni = fichIni;
	}
	public int getFichFin() {
		return FichFin;
	}
	public void setFichFin(int fichFin) {
		FichFin = fichFin;
	}
	
}
