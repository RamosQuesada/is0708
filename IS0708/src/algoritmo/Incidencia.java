package algoritmo;
import java.util.Date;

/**
 * Representación de posibles incidencias de los trabajadores
 * @author Agustín Daniel Delgado Muñoz
 */
public class Incidencia {
	
	private int nvend;
	private int idIncidencia;
	private String descripcion;
	private Date fechainicio= new Date();
	private Date fechafin=new Date();
	
	/**
	 * 
	 * @param nvend  		 Numero de vendedor
	 * @param idIncidencia   Identificador de la incidencia
	 * @param descripcion	 Descripcion de la incidencia
	 * @param fechainicio	 Fecha de inicio de la incidencia
	 * @param fechafin	     Fecha de finalizacion de la incidencia
	 

	 */
	public Incidencia(){ //constructora por defecto
		
	}
	
	//constructora 
	public Incidencia(int user, int id, String descr,Date finicio, Date ffin){
		this.nvend=user;
		this.idIncidencia=id;
		this.descripcion=descr;
		this.fechainicio=finicio;
		this.fechafin=ffin;
	}
	
	//getters & setters de la clase Incidencias
	
	public int getnvend() {
		return this.nvend;
	}
	public void setNvend(int user) {
		this.nvend = user;
	}
	
	public int getIDincidencia() {
		return this.idIncidencia;
	}
	public void setIDincidencia(int id) {
		this.idIncidencia = id;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
	public void setDescripcion(String descr) {
		this.descripcion=descr;
	}
	
	
	public Date getFechaInicio() {
		return this.fechainicio;
	}
	public void setFechaInicio(Date finicio) {
		this.fechainicio=finicio;
	}
	
	
	public Date getFechaFin() {
		return this.fechafin;
	}
	public void setFechaFin(Date ffin) {
		this.fechafin=ffin;
	}
	
	
	
	
	

}
