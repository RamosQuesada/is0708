/**
 * 
 */
package algoritmo;

import java.sql.Time;
import java.util.ArrayList;

/**
 * @author Alberto
 * Esta clase analiza el cuadrante creado buscando errores a la hora de asignar
 * a los empleados, creando un informe final que se le presenta al jefe de departamento
 */
public class Analisis {
	private ArrayList<Sugerencia>[] sugerencias;
	private int dias;
	
	/**
	 * Constructora con parámetros
	 * @param dias Dias a analizar
	 * @param cuadrante Cuadrante que ha sido generado
	 * @param estructura Estructura correspondiente al mes
	 */
	public Analisis (int dias, Cuadrante cuadrante, Estructura estructura){
		this.sugerencias=new ArrayList[dias];
		this.dias=dias;
		for (int i=0;i<dias;i++)
			sugerencias[i]=new ArrayList<Sugerencia>();			
		recorrido(cuadrante,estructura);
	}
	 
	/**
	 * Método para ver faltas de personal del cuadrante generado
	 * @param cuadrante Cuadrante generado
	 * @param estructura Estructura correspondiente
	 */
	public void recorrido (Cuadrante cuadrante, Estructura estructura){
		for (int i=0;i<dias;i++){
			//si el dia i no abre el centro, no se calcula el cuadrante
			if ((!estructura.getCal().diaLibre(i)) && (cuadrante.getListaTrabajaDia(i).size()!=0)) {
				Time t=new Time(8,0,0);
				for (int j=0;j<24;j++){
					t.setHours(j);
					
					for (int k=0;k<60;k=k+5){
						t.setMinutes(k);
						int minimoDia=estructura.getCal().getMinHora(i, j);
						Sugerencia sugAnterior=null;
						//si ya hay alguna sugerencias del dia, se coge la ultima, si no se queda null
						if (!sugerencias[i].isEmpty()){
							if (sugerencias[i].get(sugerencias[i].size()-1).getHoraFin().equals(new Time(t.getHours(),t.getMinutes(),0)))
								sugAnterior=sugerencias[i].get(sugerencias[i].size()-1);
						}
						
						//se comprueba cuantos trabajadores hay en un minuto concreto
						//se hacen franjas de 5 minutos
						int contador=0;
						for (int l=0;l<cuadrante.getListaTrabajaDia(i).size();l++){
							Trabaja trab=cuadrante.getListaTrabajaDia(i).get(l);
							if(compruebaHora(t,trab.getFichIni(),trab.getFichFin())){
								contador++;
							}
						}
											
						// si el contador es menor que el minimo para ese minuto, se crea una sugerencia nueva
						// solo si no existia una anterior con las mismas caracteristas
						// en caso contrario, se amplia en 5 minutos la franja de la sugerencia
						if (contador<minimoDia){
							if ((sugAnterior!=null)&&(minimoDia-contador!=sugAnterior.getFaltas()) && (sugAnterior.getTipo()!=tipoFalta(contador,minimoDia))){		
								Sugerencia sug=new Sugerencia(minimoDia-contador,minimoDia,new Time(t.getHours(),t.getMinutes(),0),new Time(t.getHours(),t.getMinutes()+5,0),i,tipoFalta(contador,minimoDia));
								sugerencias[i].add(sug);
							} else {
								if (sugAnterior==null) {
									Sugerencia sug=new Sugerencia(minimoDia-contador,minimoDia,new Time(t.getHours(),t.getMinutes(),0),new Time(t.getHours(),t.getMinutes()+5,0),i,tipoFalta(contador,minimoDia));
									sugerencias[i].add(sug);
								} else 
									sugAnterior.ampliar(0,5);
							}
						}										
					}					
				}	
			}
		}
	}
	
	/**
	 * Método para analizar las faltas de personal y generar el mensaje correspondiente
	 * @return ArrayList de comentarios con respecto a la falta de personal
	 */
	public ArrayList<String> analizarFaltas(){
		ArrayList<String> resul=new ArrayList<String>();
		int cont1=0;
		int cont2=0;
		int cont3=0;
		for (int i=0;i<dias;i++){			
			if (calcularValorFranja(getSugerenciasDia(i),1)>=0.5){cont1++;}
			if (calcularValorFranja(getSugerenciasDia(i),2)>=0.5){cont2++;}
			if (calcularValorFranja(getSugerenciasDia(i),3)>=0.5){cont3++;}
		}
		if(cont1>=5){resul.add("Se repiten faltas en el periodo de mañana (9h a 13h). Se recomienda contratar gente.");}
		if(cont2>=6){resul.add("Se repiten faltas en el periodo de mediodia (13h a 18h). Se recomienda contratar gente.");}
		if(cont3>=6){resul.add("Se repiten faltas en el periodo de tarde (18h a 23h). Se recomienda contratar gente.");}
		return resul;
	}
	
	/**
	 * Método para calcular el valor de las faltas de personal de un día
	 * @param lista Lista de las sugerencias de un día
	 * @param parte Parte del día para la que se calcula el valor
	 * @return Puntuación del día
	 */
	public double calcularValorFranja(ArrayList<Sugerencia> lista,int parte){
		double resul=0;
		for (int i=0;i<lista.size();i++){
			int horaIni=lista.get(i).getHoraIni().getHours();
			int horaFin=lista.get(i).getHoraFin().getHours();
			switch (parte){
				case 1: if (horaIni<13){resul=resul+lista.get(i).puntuacion();} break;
				case 2: if (((horaIni>=13)&&(horaIni<18))||((horaFin>=13)&&(horaFin<18))){resul=resul+lista.get(i).puntuacion();} break;
				case 3: if (horaIni>=18){resul=resul+lista.get(i).puntuacion();} break;
			}						
		}
		return resul;
	}
	
	/**
	 * Indica si una hora se encuentra incluida dentro de un perido de tiempo
	 * @param a Hora a comprobar
	 * @param ini Hora incial del periodo de tiempo
	 * @param fin Hora final del periodo de tiempo
	 * @return true si la hora está incluida dentro del periodo de tiempo
	 */
	public boolean compruebaHora(Time a, Time ini, Time fin){
		boolean resultado=true;
		if (a.before(ini)){resultado=false;}
		if (a.after(fin)){resultado=false;}
		return resultado;
	}
	
	/**
	 * Indica el tipo de falta dependiendo de la gente que falta durante una franja de datos
	 * @param contador Número de personas que faltan durante una franja de tiempo
	 * @param minimo Valor mínimo del día
	 * @return El tipo de la falta
	 */
	public int tipoFalta(int contador, int minimo){
		int tipo=0;
		if (contador==0){tipo=2;}
		else {if (contador<=minimo/2){tipo=1;}}
		return tipo;
	}
	
	/**
	 * Consulta las sugerencias de un día
	 * @param i Día del que se piden las sugerencias
	 * @return Sugerencias del día
	 */
	public ArrayList<Sugerencia> getSugerenciasDia(int i){
		return sugerencias[i];
	}
	
	/**
	 * Genera cadenas de texto de sugerencias para cada día
	 * @return Sugerencias para cada día
	 */
	public ArrayList<String>[] generarResumen(){
		ArrayList<String>[] textos = new ArrayList[dias];
		for (int i=0;i<dias;i++){
			textos[i]=new ArrayList<String>();			
		}
		for (int i=0;i<dias;i++){
			for (int j=0;j<getSugerenciasDia(i).size();j++){
				textos[i].add(getSugerenciasDia(i).get(j).toString());
			}
		}
		return textos;
	}
	
}
