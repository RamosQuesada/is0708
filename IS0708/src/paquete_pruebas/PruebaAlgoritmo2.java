package paquete_pruebas;

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
		
		Database prueba = new Database();
		prueba.abrirConexion();
		Controlador c = new Controlador(prueba);
		
		alg = new TurnoMatic(12,2007,c,"is");
		System.out.println();

	}
}
