package aplicacion.datos;

import java.util.ArrayList;

/**
 * Clase que representa un contrato
 * @author Miguel Angel Alonso Pajuelo
 */

public class Contrato {

/**
 * Atributos privados
 */
	
	/*Nombre del contrato*/
	private String _nombreContrato;

	/*Identificador que representa al contrato*/
	private int _numeroContrato;
	
	/*Turno inicial asignado al contrato*/
	private int _turnoInicial;
	
	/*Duracion del ciclo de cambios de turnos del contrato*/
	private int _duracionCiclo;
	
	/*Patron que representa los cambios de turno respecto a la duracion del ciclo*/
	private String _patron;
	
	/*Salario correspondiente al contrato*/
	private double _salario;
	
	/*Tipo de contrato*/
	private int _tipoContrato;
	
/**
 * Constructor de un contrato
 * @param nombreContrato Nombre del contrato
 * @param numeroContrato Identificador
 * @param turnoInicial Turno inicial asignado
 * @param duracionCiclo Duracion del ciclo
 * @param patron Patron de turnos dentro del ciclo
 * @param salario Salario del contrato
 * @param tipoContrato tipo de contrato
 */
	public Contrato(String nombreContrato,int numeroContrato,int turnoInicial,
			int duracionCiclo,String patron,double salario, int tipoContrato){
		this._nombreContrato=nombreContrato;
		this._numeroContrato=numeroContrato;
		this._turnoInicial=turnoInicial;
		this._duracionCiclo=duracionCiclo;
		this._patron=patron;
		this._salario=salario;
		this._tipoContrato=tipoContrato;
	}
	
/**
 * Metodos de acceso y modificadores
 */
	/*Nombre del contrato*/
	public String getNombreContrato(){
		return this._nombreContrato;
	}
	
	public void setNombreContrato(String nombreContrato){
		this._nombreContrato=nombreContrato;
	}
	
	/*Identificador del contrato*/
	public int getNumeroContrato(){
		return this._numeroContrato;
	}
	
	public void setNumeroContrato(int numeroContrato){
		this._numeroContrato=numeroContrato;
	}
	
	/*Turno inicial*/
	public int getTurnoInicial(){
		return this._turnoInicial;
	}
	
	public void setTurnoInicial(int turnoInicial){
		this._turnoInicial=turnoInicial;
	}
	
	/*Duracion del ciclo*/
	public int getDuracionCiclo(){
		return this._duracionCiclo;
	}
	
	public void setDuracionCiclo(int duracionCiclo){
		this._duracionCiclo=duracionCiclo;
	}
	
	/*Patron*/
	public String getPatron(){
		return this._patron;
	}
	
	public void setPatron(String patron){
		this._patron=patron;
	}
	
	/*Salario*/
	public double getSalario(){
		return this._salario;
	}
	
	public void setSalario(double salario){
		this._salario=salario;
	}
	
	public int getTipoContrato(){
		return this._tipoContrato;
	}

	public void set_tipoContrato(int contrato) {
		_tipoContrato = contrato;
	}
	/**
	 * Esta funcion devuelve un arraylist con los numeros de turnos del contrato
	 * a partir del patron
	 * @return Arraylist con los numeros de los turnos;
	 */
	public ArrayList<Integer> getNumTurnosContrato(){
		String p=this._patron;
		String numTurno="";
		boolean f=false;
		ArrayList<Integer> turnos=new ArrayList<Integer>();
		for(int i=0;i<p.length();i++){
			if(p.charAt(i)==':'){
				i++;
				//ahora empezamos a leer
				if(p.charAt(i)!='d'){
					while(f!=true && p.charAt(i)!='/'){
						numTurno+=p.charAt(i);
						if(i<p.length()-1){
							i++;
						}else{f=true;}
						if(p.charAt(i)==','){
							if(!turnos.contains(Integer.valueOf(numTurno))){
								turnos.add(Integer.valueOf(numTurno));
								numTurno="";
								if(i<p.length()-1){
									i++;
								}else{f=true;}
							}else{
								numTurno="";
								if(i<p.length()-1){
									i++;
								}else{f=true;}
								
							}
						}
					}
					if(numTurno!=""){
						if(!turnos.contains(Integer.valueOf(numTurno))){
							turnos.add(Integer.valueOf(numTurno));
							numTurno="";
							if(i<p.length()-1){
								i++;
							}else{f=true;}
						}else{
							numTurno="";
							if(i<p.length()-1){
								i++;
							}else{f=true;}
						}
					}
				}
			}
		}
		return turnos;
	}
	
	
}//fin de clase
