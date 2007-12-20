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
		Controlador c = new Controlador(prueba);
		
		TurnoMatic alg = new TurnoMatic(12,2007,c,"DepPrueba");
		alg.ejecutaAlgoritmo();
		alg.imprimeEstructura();
		alg.imprimeCuadrante(); 
	}
}
