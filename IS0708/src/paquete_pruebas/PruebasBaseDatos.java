package paquete_pruebas;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import algoritmo.Calendario;
import algoritmo.Cuadrante;
import algoritmo.Trabaja;
import aplicacion.Contrato;
import aplicacion.Controlador;
import aplicacion.Database;
import aplicacion.Turno;

public class PruebasBaseDatos {

	/**
	 * @param args
	 */
	//jh
	//CUIDADO!!!!!!!!!!!!
	//NO inserteis en las tablas un string cuando en la base de datos del servidor 
	//esta como date porque luego peta al insertar asi q por favor cambiad las pruebas
	public static void main(String[] args) {
		Database prueba = new Database();
		prueba.abrirConexion();
		//Controlador c = new Controlador(prueba);
		//c.getListaTurnosContrato(2);
		prueba.insertarContrato(5, 2, "cap", "patron", 2, 222, 1);
		//int x=c.getTurnoEmpleadoDia(Date.valueOf("2007-12-21"),71449215);
		//System.out.println(x);
		
				
		//Calendario cal = new Calendario(12, 2007, "DepPrueba");
		//Controlador c1 = new Controlador(prueba);
		//c1.getDistribucionMes("DepPrueba", cal);
		//c.getEmpleados(null, "dep", null, null, null, null, 1);
		/*
		ArrayList a = new ArrayList();
		
		Time h = new Time(10000000);
		//Calendario cal = new Calendario(12, 2007);
		Controlador c1 = new Controlador(prueba);
		//c1.getDistribucionMes(3, cal);
		Calendario cal = new Calendario(12, 2007, 3);
		Controlador c1 = new Controlador(prueba);
		c1.getDistribucionMes(3, cal);
		a = c1.getMensajesEntrantes(1,0,30);
		prueba.obtenFestivos(3, "2007-12-12");
		prueba.insertarTurno(1, "prueba", h, h, h, 10);
		prueba.insertarTurno(4, "prueba", h, h, h, 14500);

		prueba.insertarDistribucion(9, "Martes", "1e5p", 10, 7, 1);*/
		//prueba.insertarMensaje(1,Date.valueOf("2007-12-11"), "prueba", "que gran prueba",false);
		//prueba.marcaMensaje(true, 1);
		/*prueba.insertarListaDestinatarios(1, 1);
		// prueba.insertarUsuario(1235, "Pablo", "Gervas",
		// "Gomez_Navarro","2007-12-11" , "m", "gervas@is.com", "ballenas",
		// "p","2007-12-11" , "2007-12-11", 0, 123, "empleado", 1, 1);
		// prueba.insertarUsuario(1234, "Pablo", "Gervas",
		// "Gomez_Navarro","2007-12-11" , "m", "gervas@is.com", "ballenas",
		// "p","2007-12-11" , "2007-12-11", 0, 123, "empleado", 1, 1);
		prueba.insertarDepartamento("is", 123);
		prueba.insertarNumerosDepartamento("123", "is");

		prueba.insertarContrato(1, 1, "turnoUno", "3t4d", 7, 123.123);
		prueba.insertarIncidencia("preñadasssssss");
		prueba.insertarTieneIncidencia(1, 1, new Time(0), new Time(10));
		// prueba.insertarTrabaja(2, 2, new Time(0), new Time(0), new Time(0));
		prueba.insertarTurnoPorContrato(1, 1);
		prueba.insertarVenta(1, new Time(0), 123.43);
		prueba.insertarFestivo(9, "2007-12-11", "2007-12-11", "2e7p", 15, 10,1);

		// Prueba método ObtenerLista de Turnos

		// Prueba método ObtenerLista de Turnos

		Controlador c = new Controlador(prueba);

		ArrayList<Empleado> emps=c.getEmpleadosDepartamento(1);
		ArrayList<Turno> t = new ArrayList<Turno>();

		t = c.getTurnosEmpleados();
		for (int i = 0; i < t.size(); i++) {
			System.out.print(t.get(i).getIdTurno() + "  ");
			System.out.print(t.get(i).getDescripcion() + "  ");
			System.out.print(t.get(i).getHoraEntrada() + "  ");
			System.out.print(t.get(i).getHoraSalida() + "  ");
			System.out.print(t.get(i).getHoraDescanso() + "  ");
			System.out.print(t.get(i).getTDescanso() + "  ");
			System.out.println();
		}
		
		
		//prueba insertCuadrante
		Controlador c = new Controlador(prueba);
		Cuadrante cuad=new Cuadrante(12,2007,1);
		Trabaja trab=new Trabaja(2,null,null,1);
		for(int i=0;i<cuad.getNumDias();i++){
			cuad.setTrabajaDia(i, trab);
		}
		c.insertCuadrante(cuad); */
		
		/*
		//prueba insertContrato
		int idContrato=2;
		int turnoInicial=1;
		String nombre= "Tarde";
		String patron= "2d3f";
		int duracionCiclo=5;
		double salario=1200.0;
		Contrato contrato=new Contrato(nombre,idContrato,turnoInicial,duracionCiclo,patron,salario);
		c.insertContrato(contrato);
		prueba.cerrarConexion();
		
		//leer contrato
		Contrato contrato=c.getContrato(1);*/
		//prueba.borraMensaje(2);
		//prueba.insertarMensaje(2,12121212,Date.valueOf("2007-12-11"), "asunto", "texto", false);

			
	}

}

