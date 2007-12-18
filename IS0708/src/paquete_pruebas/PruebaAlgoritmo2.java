package paquete_pruebas;

import java.sql.Date;
import java.util.ArrayList;

import algoritmo.*;
import aplicacion.*;

public class PruebaAlgoritmo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TurnoMatic alg;
		java.util.Date today;
		java.sql.Date fContrato;
		long difFechas;
		int diaCiclo;
		
		
		today = new java.util.Date();
		java.sql.Date fechaActual = new java.sql.Date(today.getTime());
		//YEAR: year - 1900
		//MES: de 0 a 11 (enero 0, diciembre 11)
		fContrato = new java.sql.Date(107,11,8);
		if(fContrato == null)
			fContrato = new Date(today.getTime());
		difFechas = today.getTime()-fContrato.getTime();
		diaCiclo = (int)difFechas/(24*60*60*1000);
		System.out.println("diferencia: " + diaCiclo);
		
		
		
		Database prueba = new Database();
		prueba.abrirConexion();
		Controlador c = new Controlador(prueba);
		
		alg = new TurnoMatic(12,2007,c,"dep");
		alg.ejecutaAlgoritmo();
		System.out.println();

	}
}
