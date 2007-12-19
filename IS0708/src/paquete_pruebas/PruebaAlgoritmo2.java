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
		
		TurnoMatic alg;
		java.util.Date today;
		java.sql.Date fContrato;
		long difFechas;
		int diaCiclo;
				
		Database prueba = new Database();
		prueba.abrirConexion();
		Controlador c = new Controlador(prueba);
		
		alg = new TurnoMatic(12,2007,c,"DepPrueba");
		alg.ejecutaAlgoritmo();
		System.out.println("ole");

	}
}
