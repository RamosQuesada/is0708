package paquete_pruebas;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import algoritmo.*;
import aplicacion.*;

public class PruebaAlgoritmo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
						
		Database prueba = new Database();
		prueba.abrirConexion();
		Controlador c = new Controlador(prueba, true);
		String me = "1";
		if (me.length()==1)
		{
			me = "0" + me;
		}	
		System.out.println(me);
		//hay q meter meses siguientes al dia en el que se genera el cuadrante
		//TurnoMatic alg = new TurnoMatic(4,2008,c,"DatosFijos");
		//TurnoMatic alg = new TurnoMatic(6,2008,c,"DatosFijos");
		//TurnoMatic alg = new TurnoMatic(12,2007,c,"pruebaAlg");
 		ResultadoTurnoMatic resultado = alg.ejecutaAlgoritmo();
 		alg.imprimeEstructura();
		alg.imprimeCuadrante(); 

	}
}
