package algoritmo;

import aplicacion.Controlador;
import aplicacion.Util;

/**
 * @author DavidMartin & Miguel Angel Diaz
 * Esta clase representa a un calendario donde vamos a almacenar los dias festivos, el
 * max/min numero de empleados y el numero de expertos e inexpertos
 */
public class Calendario {
	
	private String idDepartamento;
	private int mes;
	private int anio;
	private int numDias; 			//numero de dias del mes
	private HoraCalendario cal[][]; //esta matriz contiene para cada dia y para cada hora un objeto de la clase Horacalendario
	private Controlador cont;
	
	/**
	 * Constructora por defecto
	 */
	public Calendario(){
		mes = 0;
		anio = 0;
		numDias = 0;
		cal = null;
	}
	
	/**
	 * Constructora por parámetros
	 * @param mes Mes para el calendario
	 * @param anio Año para el calendario
	 * @param cont Controlador de la aplicación
	 * @param idDepartamento Identificador del departamento
	 */
	public Calendario(int mes, int anio, Controlador cont, String idDepartamento){
		this.mes = mes;
		this.cont = cont;
		this.anio = anio;
		numDias = Util.dameDias(mes,anio); 
		this.idDepartamento = idDepartamento;
		cal = new HoraCalendario[numDias][24];
		cont.getDistribucionMes(this.idDepartamento, this);
	}
	
	/**
	 * Consulta el mes del calendario
	 * @return mes
	 */
	public int getMes() {
		return mes;
	}
	
	/**
	 * Modifica el mes del calendario
	 * @param mes Nuevo valor para mes
	 */
	public void setMes(int mes) {
		this.mes = mes;
	}
	
	/**
	 * Consulta el año del calendario
	 * @return año
	 */
	public int getAnio() {
		return anio;
	}
	
	/**
	 * Modifica el año del calendario
	 * @param Nuevo valor para el año
	 */
	public void setAnio(int anio) {
		this.anio = anio;
	}
	
	/**
	 * Consulta el numero de dias del mes del calendario
	 * @return Numero de dias
	 */
	public int getNumDias() {
		return numDias;
	}
	
	/**
	 * Modifica el numero de dias del calendario
	 * @param numDias Nuevo numero de dias
	 */
	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}
	
	/**
	 * Devuelve el calendario
	 * @return cal
	 */
	public HoraCalendario[][] getCal() {
		return cal;
	}
	
	/**
	 * Modifica el calendario
	 * @param cal Nuevo calendario
	 */
	public void setCal(HoraCalendario[][] cal) {
		this.cal = cal;
	}
	
	/**
	 * Actualiza la información de una hora concreta del calendario
	 * @param dia Día qie se qio
	 * @param hora
	 * @param max
	 * @param min
	 * @param exp
	 * @param princ
	 */
	public void actualizaHora(int dia, int hora, int max, int min, int exp, int princ){
		if (esCorrecto(dia, hora)){
			if (!existeHora(dia, hora))
				cal[dia][hora] = new HoraCalendario(max,min,exp,princ);
			else {
				cal[dia][hora].setMax(max);
				cal[dia][hora].setMin(min);
				cal[dia][hora].setExpertos(exp);
				cal[dia][hora].setPrincipiantes(princ);
			}
		}
	}
	
	/**
	 * Consulta el numero maximo de empleados para un dia y una hora del calendario
	 * Si el dia o la hora son incorrectos devuelve "-1"
	 * @return numero maximo de empleados
	 */
	public int getMaxHora(int dia, int hora){
		if (esCorrecto(dia, hora)){
			if (existeHora(dia, hora))
				return cal[dia][hora].getMax();
			else
				return 0;
		}
		else return -1;
	}
	
	/**
	 * Consulta el numero minimo de empleados para un dia y una hora del calendario
	 * Si el dia o la hora son incorrectos devuelve "-1"
	 * @return numero minimo de empleados
	 */
	public int getMinHora(int dia, int hora){
		if (esCorrecto(dia, hora)){
			if (existeHora(dia, hora))
				return cal[dia][hora].getMin();
			else
				return 0;
		}
		else return -1;
	}
	
	/**
	 * Consulta el numero de expertos del patron para un dia y una hora del calendario
	 * Si el dia o la hora son incorrectos devuelve "-1"
	 * @return numero de expertos
	 */
	public int getExpHora(int dia, int hora){
		if (esCorrecto(dia, hora)){
			if (existeHora(dia, hora))
				return cal[dia][hora].getExpertos();
			else
				return 1;
		}
		else return -1;
	}
	
	/**
	 * Consulta el numero de principiantes del patron para un dia y una hora del calendario
	 * Si el dia o la hora son incorrectos devuelve "-1"
	 * @return numero de principiantes
	 */
	public int getPrincHora(int dia, int hora){
		if (esCorrecto(dia, hora)){
			if (existeHora(dia, hora))
				return cal[dia][hora].getPrincipiantes();
			else return 1;
		}
		else return -1;
	}
	
	/**
	 * Metodo para comprobar si los indices con los que se accede al calendario son correctos
	 * @param dia
	 * @param hora
	 * @return true si 1<=dia<=numDias y 0<=hora<24
	 */
	private boolean esCorrecto(int dia, int hora){
		return (dia>=0 && dia<numDias && hora>=0 && hora<24);
	}
	
	/**
	 * Metodo para comprobar si una hora ya esta creada
	 * @param dia
	 * @param hora
	 * @return true si la hora estaba creada
	 */
	private boolean existeHora(int dia, int hora){
		return (cal[dia][hora] != null);
	}

	/**
	 * Consulta el identificador del departamento
	 * @return Identificador
	 */
	public String getIdDepartamento() {
		return idDepartamento;
	}

	/**
	 * Modifica el identificador del departamento
	 * @param Nuevo valor para el identificador
	 */
	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	
	/**
	 * Metodo para comprobar si el departamento cierra un dia
	 * @param dia
	 * @return true si el departamento cierra
	 */
	public boolean diaLibre(int dia) {
		boolean libre = true;
		int n = 0;
		while ((libre) && (n<cal[dia].length)) {
			if (cal[dia][n].horaLibre())
				n++;
			else
				libre = false;
		}
		return libre;
	}
	
}
