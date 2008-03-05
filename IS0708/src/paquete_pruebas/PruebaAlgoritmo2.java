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
		TurnoMatic alg = new TurnoMatic(12,2007,c,"DatosFijos");
		//TurnoMatic alg = new TurnoMatic(12,2007,c,"pruebaAlg");
		alg.ejecutaAlgoritmo();
		alg.imprimeEstructura();
		alg.imprimeCuadrante(); 

	}
}
