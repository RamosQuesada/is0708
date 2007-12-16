package algoritmo;

import aplicacion.Util;

/**
 * Esta clase representa a un calendario donde vamos a almacenar los dias festivos, el
 * max/min numero de empleados y el numero de expertos e inexpertos
 * @author DavidMartin & Miguel Angel Diaz
 */
public class Calendario {
	
	private String idDepartamento;
	private int mes;
	private int anio;
	private int numDias; //numero de dias del mes
	private HoraCalendario cal[][]; //esta matriz contiene para cada dia y para cada hora un objeto de la clase Horacalendario
	
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
	 * Constructora de un objeto Calendario
	 * @param mes
	 * @param anio
	 */
	public Calendario(int mes,int anio, String idDepartamento){
		this.mes = mes;
		this.anio = anio;
		numDias = Util.dameDias(mes,anio); 
		this.idDepartamento = idDepartamento;
		//el calendario tiene un numero de dias segun el mes y de 0 a 23 horas
		cal = new HoraCalendario[numDias][24]; 
		//TODO borrar para ahorrar memoria cuando no se utilicen "funciones guarras" de calendario y cuadrante
		for (int i=0;i<numDias;i++){
			for (int j=0;j<24;j++){
				cal[i][j]=new HoraCalendario(0,0,0,0);
				//PRUEBA
				//cal[i][j]=new HoraCalendario(4,0,0,0);
			}
		}
		
	}
	
	/**
	 * Consulta el mes del calendario
	 * @return mes
	 */
	public int getMes() {
		return mes;
	}
	
	/**
	 * Modificadora del mes del calendario
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
	 * Consulta el año del calendario
	 * @return Año
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
	 * 
	 * @return estructura del calendario
	 * @deprecated  "funcion guarra": se pierde la ocultacion
	 */
	public HoraCalendario[][] getCal() {
		return cal;
	}
	
	
	/**
	 * 
	 * @param cal
	 * @deprecated "funcion guarra": se pierde la ocultacion
	 */
	public void setCal(HoraCalendario[][] cal) {
		this.cal = cal;
	}
	
	
	/**
	 * Actualiza la información de una hora concreta del calendario
	 * @param dia
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
	 * @return Numero maximo de empleados
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
	 * @return Numero minimo de empleados
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
		return (dia>=1 && dia<=numDias && hora>=0 && hora<24);
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

	public String getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	
}
