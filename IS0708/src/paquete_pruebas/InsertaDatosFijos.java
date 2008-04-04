package paquete_pruebas;

import java.sql.Date;

import aplicacion.Controlador;
import aplicacion.Database;
import aplicacion.datos.Contrato;
import aplicacion.datos.Turno;

/**
 * Inserta datos fijos para pruebas
 * @author Miguel Angel Diaz
 */
public class InsertaDatosFijos {

	public static void insertar() {
		
		// Comenzamos 
		Database bd = new Database();
		Controlador c = new Controlador(bd,false);
		bd.run();
		String depart = "DatosFijos";
		
		Date inicio = Date.valueOf("2008-01-01");
		Date nacimiento = new Date(0);
		
		//c.vaciarTodasTablas();//borramos las tablas
		/*c.vaciarTabla("TURNOS");//VACIAMOS SOLO LAS TABLAS QUE RELLENAMOS Y LAS QUE VAN "LIGADAS"
		c.vaciarTabla("CONTRATO");
		c.vaciarTabla("DISTRIBUCION");
		c.vaciarTabla("DEPARTAMENTO");
		c.vaciarTabla("ListaTurnosPorContrato");
		c.vaciarTabla("USUARIO");
		c.vaciarTabla("MENSAJE");
		c.vaciarTabla("VENTAS");*/
				
		// Crear turno jefe departamento
		Turno turnoJefe = new Turno(0, "turnoJefeFijo", "9:00:00", "19:00:00", "13:00:00", 180);
		turnoJefe.setIdTurno(c.insertTurno(turnoJefe));
		
		// Crear contrato jefe departamento
		String patronContratoJefe = "6:" + turnoJefe.getIdTurno() + "/1:d";
		Contrato contratoJefe = new Contrato("contratoJefeFijo", 0, turnoJefe.getIdTurno() ,7, patronContratoJefe, 1200, 1);
		contratoJefe.setNumeroContrato(c.insertContrato(contratoJefe));
		//c.insertTurnoPorContrato(turnoJefe.getIdTurno(), contratoJefe.getNumeroContrato());
	
		// Crear el jefe de departamento
		c.insertUsuario(10000100, "JefeFijo", "ape1", "ape2", nacimiento, 0, "email@email.es", "10000100", 2, 
				inicio, inicio, 0, 0, 0, 2, contratoJefe.getNumeroContrato(),
				turnoJefe.getIdTurno()); //hay que cambiar el rango a 2
		c.insertDepartamentoUsuario(10000100, depart);
		
		// Creamos un nuevo departamento
	    c.insertDepartamentoPruebas(depart,10000100);
		
		// Horas en las que esta cerrado
	    for (int i=1; i<=7; i++) {
	    	for (int j=0; j<9; j++) 
				c.insertDistribucion(j, i, "1e1p", 0, 0, depart);
	    	for (int j=22; j<24; j++) 
				c.insertDistribucion(j, i, "1e1p", 0, 0, depart);
	    }
	    
	    // De lunes a viernes
		for (int i=1; i<=5; i++) {
			// Cada hora abierta
			c.insertDistribucion( 9, i, "1e1p", 6, 2, depart);
			c.insertDistribucion(10, i, "1e1p", 6, 4, depart);
			c.insertDistribucion(11, i, "1e1p", 6, 4, depart);
			c.insertDistribucion(12, i, "1e1p", 6, 4, depart);
			c.insertDistribucion(13, i, "1e1p", 4, 2, depart);
			c.insertDistribucion(14, i, "1e1p", 4, 2, depart);
			c.insertDistribucion(15, i, "1e1p", 7, 5, depart);
			c.insertDistribucion(16, i, "1e1p", 4, 2, depart);
			c.insertDistribucion(17, i, "1e1p", 4, 2, depart);
			c.insertDistribucion(18, i, "1e1p", 6, 2, depart);
			c.insertDistribucion(19, i, "1e1p", 6, 4, depart);
			c.insertDistribucion(20, i, "1e1p", 6, 4, depart);
			c.insertDistribucion(21, i, "1e1p", 6, 4, depart);
		}
		
		// Sabado
		c.insertDistribucion( 9, 6, "1e1p",  6, 2, depart);
		c.insertDistribucion(10, 6, "1e1p",  9, 7, depart);
		c.insertDistribucion(11, 6, "1e1p",  9, 7, depart);
		c.insertDistribucion(12, 6, "1e1p",  9, 7, depart);
		c.insertDistribucion(13, 6, "1e1p",  7, 5, depart);
		c.insertDistribucion(14, 6, "1e1p",  7, 5, depart);
		c.insertDistribucion(15, 6, "1e1p", 10, 8, depart);
		c.insertDistribucion(16, 6, "1e1p",  7, 5, depart);
		c.insertDistribucion(17, 6, "1e1p",  7, 5, depart);
		c.insertDistribucion(18, 6, "1e1p",  9, 4, depart);
		c.insertDistribucion(19, 6, "1e1p",  9, 7, depart);
		c.insertDistribucion(20, 6, "1e1p",  9, 7, depart);
		c.insertDistribucion(21, 6, "1e1p",  9, 7, depart);
		
		// Domingo
		c.insertDistribucion( 9, 7, "1e1p", 3, 1, depart);
		c.insertDistribucion(10, 7, "1e1p", 6, 4, depart);
		c.insertDistribucion(11, 7, "1e1p", 6, 4, depart);
		c.insertDistribucion(12, 7, "1e1p", 6, 4, depart);
		c.insertDistribucion(13, 7, "1e1p", 4, 2, depart);
		c.insertDistribucion(14, 7, "1e1p", 4, 2, depart);
		c.insertDistribucion(15, 7, "1e1p", 4, 2, depart);
		c.insertDistribucion(16, 7, "1e1p", 4, 2, depart);
		c.insertDistribucion(17, 7, "1e1p", 4, 2, depart);
		c.insertDistribucion(18, 7, "1e1p", 6, 4, depart);
		c.insertDistribucion(19, 7, "1e1p", 6, 4, depart);
		c.insertDistribucion(20, 7, "1e1p", 6, 4, depart);
		c.insertDistribucion(21, 7, "1e1p", 6, 4, depart);
		
		// Turnos
		Turno t7hM = new Turno(0, "7h Mañana", "09:00:00", "16:00:00", "09:40:00", 20);
		t7hM.setIdTurno(c.insertTurno(t7hM));
		Turno t7hT = new Turno(0, "7h Tarde", "15:00:00", "22:00:00", "18:30:00", 20);
		t7hT.setIdTurno(c.insertTurno(t7hT));
		
		Turno t4hM = new Turno(0, "4h Mañana", "09:00:00", "13:00:00", "09:00:00", 0);
		t4hM.setIdTurno(c.insertTurno(t4hM));
		Turno t4hT = new Turno(0, "4h Tarde", "18:00:00", "22:00:00", "18:00:00", 0);
		t4hT.setIdTurno(c.insertTurno(t4hT));
		
		Turno tSabM = new Turno(0, "Sab Mañana", "10:00:00", "16:00:00", "10:00:00", 0);
		tSabM.setIdTurno(c.insertTurno(tSabM));
		Turno tSabT = new Turno(0, "Sab Tarde", "16:00:00", "22:00:00", "16:00:00", 0);
		tSabT.setIdTurno(c.insertTurno(tSabT));
		
		// Contratos
		String patron7hM = "6:" + t7hM.getIdTurno() + "/1:d/" +
		                   "6:" + t7hT.getIdTurno() + "/1:d";
		Contrato c7hM = new Contrato("7 horas mañana", 0, t7hM.getIdTurno(), 14, patron7hM, 800, 1);
		c7hM.setNumeroContrato(c.insertContrato(c7hM));
		String patron7hT = "6:" + t7hT.getIdTurno() + "/1:d/" +
         				   "6:" + t7hM.getIdTurno() + "/1:d";
		Contrato c7hT = new Contrato("7 horas tarde", 0, t7hT.getIdTurno(), 14, patron7hT, 800, 1);
		c7hT.setNumeroContrato(c.insertContrato(c7hT));
		
		String patron4h = "7:" + t4hM.getIdTurno() + "," + t4hT.getIdTurno();
		Contrato c4hM = new Contrato("4 horas mañana", 0, t4hM.getIdTurno(), 7, patron4h, 600, 4);
		c4hM.setNumeroContrato(c.insertContrato(c4hM));
		Contrato c4hT = new Contrato("4 horas tarde", 0, t4hT.getIdTurno(), 7, patron4h, 600, 4);
		c4hT.setNumeroContrato(c.insertContrato(c4hT));
		
		String patronSab = "5:d/2:" + tSabM.getIdTurno() + "," + tSabT.getIdTurno();	
		Contrato cSabM = new Contrato("Sabadero mañana", 0, tSabM.getIdTurno(), 7, patronSab, 600, 4);
		cSabM.setNumeroContrato(c.insertContrato(cSabM));
		Contrato cSabT = new Contrato("Sabadero tarde", 0, tSabT.getIdTurno(), 7, patronSab, 600, 4);
		cSabT.setNumeroContrato(c.insertContrato(cSabT));
		
		// turnosPorContrato - solo hace falta insertar los que sean distintos del inicial del contrato
		//c.insertTurnoPorContrato(t7hM.getIdTurno(), c7hM.getNumeroContrato());
		c.insertTurnoPorContrato(t7hT.getIdTurno(), c7hM.getNumeroContrato());
		c.insertTurnoPorContrato(t7hM.getIdTurno(), c7hT.getNumeroContrato());
		//c.insertTurnoPorContrato(t7hT.getIdTurno(), c7hT.getNumeroContrato());
		
		//c.insertTurnoPorContrato(t4hM.getIdTurno(), c4hM.getNumeroContrato());
		c.insertTurnoPorContrato(t4hT.getIdTurno(), c4hM.getNumeroContrato());
		c.insertTurnoPorContrato(t4hM.getIdTurno(), c4hT.getNumeroContrato());
		//c.insertTurnoPorContrato(t7hT.getIdTurno(), c4hT.getNumeroContrato());
		
		//c.insertTurnoPorContrato(tSabM.getIdTurno(), cSabM.getNumeroContrato());
		c.insertTurnoPorContrato(tSabT.getIdTurno(), cSabM.getNumeroContrato());
		c.insertTurnoPorContrato(tSabM.getIdTurno(), cSabT.getNumeroContrato());
		//c.insertTurnoPorContrato(tSabT.getIdTurno(), cSabT.getNumeroContrato());
		
		// Empleados contrato "7 horas": 3 de mañana y 3 de tarde
		c.insertUsuario(10000200, "7hM1", "ape1", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c7hM.getNumeroContrato(), t7hM.getIdTurno());
		c.insertUsuario(10000201, "7hM2", "ape2", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c7hM.getNumeroContrato(), t7hM.getIdTurno());
		c.insertUsuario(10000202, "7hM3", "ape3", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c7hM.getNumeroContrato(), t7hM.getIdTurno());
		c.insertUsuario(10000300, "7hT1", "ape4", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c7hT.getNumeroContrato(), t7hT.getIdTurno());
		c.insertUsuario(10000301, "7hT2", "ape5", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c7hT.getNumeroContrato(), t7hT.getIdTurno());
		c.insertUsuario(10000302, "7hT3", "ape6", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c7hT.getNumeroContrato(), t7hT.getIdTurno());
		c.insertDepartamentoUsuario(10000200, depart);
		c.insertDepartamentoUsuario(10000201, depart);
		c.insertDepartamentoUsuario(10000202, depart);
		c.insertDepartamentoUsuario(10000300, depart);
		c.insertDepartamentoUsuario(10000301, depart);
		c.insertDepartamentoUsuario(10000302, depart);
		
		// Empleados contrato "4 horas": 2 de mañana y 2 de tarde
		c.insertUsuario(10000400, "4hM1", "ape7", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c4hM.getNumeroContrato(), t4hM.getIdTurno());
		c.insertUsuario(10000401, "4hM2", "ape8", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c4hM.getNumeroContrato(), t4hM.getIdTurno());
		c.insertUsuario(10000500, "4hT1", "ape9", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c4hT.getNumeroContrato(), t4hT.getIdTurno());
		c.insertUsuario(10000501, "4hT2", "ape10", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, c4hT.getNumeroContrato(), t4hT.getIdTurno());
		c.insertDepartamentoUsuario(10000400, depart);
		c.insertDepartamentoUsuario(10000401, depart);
		c.insertDepartamentoUsuario(10000500, depart);
		c.insertDepartamentoUsuario(10000501, depart);
		
		// Empleados contrato "Sabaderos": 3 de mañana y 3 de tarde
		c.insertUsuario(10000600, "SabM1", "ape11", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, cSabM.getNumeroContrato(), tSabM.getIdTurno());
		c.insertUsuario(10000601, "SabM2", "ape12", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, cSabM.getNumeroContrato(), tSabM.getIdTurno());
		c.insertUsuario(10000602, "SabM3", "ape13", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, cSabM.getNumeroContrato(), tSabM.getIdTurno());
		c.insertUsuario(10000700, "SabT1", "ape14", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, cSabT.getNumeroContrato(), tSabT.getIdTurno());
		c.insertUsuario(10000701, "SabT2", "ape15", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, cSabT.getNumeroContrato(), tSabT.getIdTurno());
		c.insertUsuario(10000702, "SabT3", "ape16", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
				inicio, inicio, 0, 0, 0, 1, cSabT.getNumeroContrato(), tSabT.getIdTurno());
		c.insertDepartamentoUsuario(10000600, depart);
		c.insertDepartamentoUsuario(10000601, depart);
		c.insertDepartamentoUsuario(10000602, depart);
		c.insertDepartamentoUsuario(10000700, depart);
		c.insertDepartamentoUsuario(10000701, depart);
		c.insertDepartamentoUsuario(10000702, depart);
		
		bd.cerrarConexion();
		
	}
	
	public static void insertarNdepart(int numDepartamentos) {
	
		// Comenzamos 
		Database bd = new Database();
		Controlador c = new Controlador(bd, false);
		bd.run();
		
		Date inicio = Date.valueOf("2008-01-01");
		Date nacimiento = new Date(0);
		
		//c.vaciarTodasTablas();//borramos las tablas
		/*c.vaciarTabla("TURNOS");//VACIAMOS SOLO LAS TABLAS QUE RELLENAMOS Y LAS QUE VAN "LIGADAS"
		c.vaciarTabla("CONTRATO");
		c.vaciarTabla("DISTRIBUCION");
		c.vaciarTabla("DEPARTAMENTO");
		c.vaciarTabla("ListaTurnosPorContrato");
		c.vaciarTabla("USUARIO");
		c.vaciarTabla("MENSAJE");
		c.vaciarTabla("VENTAS");*/
		
		for (int nd=1; nd<=numDepartamentos; nd++){
			
			String depart = "DatosFijos" + nd;
			
			// Crear turno jefe departamento
			Turno turnoJefe = new Turno(0, "turnoJefeFijo"+nd, "9:00:00", "19:00:00", "13:00:00", 180);
			turnoJefe.setIdTurno(c.insertTurno(turnoJefe));
			
			// Crear contrato jefe departamento
			String patronContratoJefe = "6:" + turnoJefe.getIdTurno() + "/1:d";
			Contrato contratoJefe = new Contrato("contratoJefeFijo "+nd, 0, turnoJefe.getIdTurno() ,7, patronContratoJefe, 1200, 1);
			contratoJefe.setNumeroContrato(c.insertContrato(contratoJefe));
			//c.insertTurnoPorContrato(turnoJefe.getIdTurno(), contratoJefe.getNumeroContrato());
		
			// Crear el jefe de departamento
			c.insertUsuario(10000100+nd*1000, "JefeFijo"+nd, "ape1", "ape2", nacimiento, 0, "email@email.es", Integer.toString(10000100+nd*1000), 2, 
					inicio, inicio, 0, 0, 0, 2, contratoJefe.getNumeroContrato(),
					turnoJefe.getIdTurno()); //hay que cambiar el rango a 2
			c.insertDepartamentoUsuario(10000100+nd*1000, depart);
			
			// Creamos un nuevo departamento
		    c.insertDepartamentoPruebas(depart,10000100+nd*1000);
			
			// Horas en las que esta cerrado
		    for (int i=1; i<=7; i++) {
		    	for (int j=0; j<9; j++) 
					c.insertDistribucion(j, i, "1e1p", 0, 0, depart);
		    	for (int j=22; j<24; j++) 
					c.insertDistribucion(j, i, "1e1p", 0, 0, depart);
		    }
		    
		    // De lunes a viernes
			for (int i=1; i<=5; i++) {
				// Cada hora abierta
				c.insertDistribucion( 9, i, "1e1p", 6, 2, depart);
				c.insertDistribucion(10, i, "1e1p", 6, 4, depart);
				c.insertDistribucion(11, i, "1e1p", 6, 4, depart);
				c.insertDistribucion(12, i, "1e1p", 6, 4, depart);
				c.insertDistribucion(13, i, "1e1p", 4, 2, depart);
				c.insertDistribucion(14, i, "1e1p", 4, 2, depart);
				c.insertDistribucion(15, i, "1e1p", 7, 5, depart);
				c.insertDistribucion(16, i, "1e1p", 4, 2, depart);
				c.insertDistribucion(17, i, "1e1p", 4, 2, depart);
				c.insertDistribucion(18, i, "1e1p", 6, 2, depart);
				c.insertDistribucion(19, i, "1e1p", 6, 4, depart);
				c.insertDistribucion(20, i, "1e1p", 6, 4, depart);
				c.insertDistribucion(21, i, "1e1p", 6, 4, depart);
			}
			
			// Sabado
			c.insertDistribucion( 9, 6, "1e1p",  6, 2, depart);
			c.insertDistribucion(10, 6, "1e1p",  9, 7, depart);
			c.insertDistribucion(11, 6, "1e1p",  9, 7, depart);
			c.insertDistribucion(12, 6, "1e1p",  9, 7, depart);
			c.insertDistribucion(13, 6, "1e1p",  7, 5, depart);
			c.insertDistribucion(14, 6, "1e1p",  7, 5, depart);
			c.insertDistribucion(15, 6, "1e1p", 10, 8, depart);
			c.insertDistribucion(16, 6, "1e1p",  7, 5, depart);
			c.insertDistribucion(17, 6, "1e1p",  7, 5, depart);
			c.insertDistribucion(18, 6, "1e1p",  9, 4, depart);
			c.insertDistribucion(19, 6, "1e1p",  9, 7, depart);
			c.insertDistribucion(20, 6, "1e1p",  9, 7, depart);
			c.insertDistribucion(21, 6, "1e1p",  9, 7, depart);
			
			// Domingo
			c.insertDistribucion( 9, 7, "1e1p", 3, 1, depart);
			c.insertDistribucion(10, 7, "1e1p", 6, 4, depart);
			c.insertDistribucion(11, 7, "1e1p", 6, 4, depart);
			c.insertDistribucion(12, 7, "1e1p", 6, 4, depart);
			c.insertDistribucion(13, 7, "1e1p", 4, 2, depart);
			c.insertDistribucion(14, 7, "1e1p", 4, 2, depart);
			c.insertDistribucion(15, 7, "1e1p", 4, 2, depart);
			c.insertDistribucion(16, 7, "1e1p", 4, 2, depart);
			c.insertDistribucion(17, 7, "1e1p", 4, 2, depart);
			c.insertDistribucion(18, 7, "1e1p", 6, 4, depart);
			c.insertDistribucion(19, 7, "1e1p", 6, 4, depart);
			c.insertDistribucion(20, 7, "1e1p", 6, 4, depart);
			c.insertDistribucion(21, 7, "1e1p", 6, 4, depart);
			
			// Turnos
			Turno t7hM = new Turno(0, "7h Mañana"+nd, "09:00:00", "16:00:00", "09:40:00", 20);
			t7hM.setIdTurno(c.insertTurno(t7hM));
			Turno t7hT = new Turno(0, "7h Tarde"+nd, "15:00:00", "22:00:00", "18:30:00", 20);
			t7hT.setIdTurno(c.insertTurno(t7hT));
			
			Turno t4hM = new Turno(0, "4h Mañana"+nd, "09:00:00", "13:00:00", "09:00:00", 0);
			t4hM.setIdTurno(c.insertTurno(t4hM));
			Turno t4hT = new Turno(0, "4h Tarde"+nd, "18:00:00", "22:00:00", "18:00:00", 0);
			t4hT.setIdTurno(c.insertTurno(t4hT));
			
			Turno tSabM = new Turno(0, "Sab Mañana"+nd, "10:00:00", "16:00:00", "10:00:00", 0);
			tSabM.setIdTurno(c.insertTurno(tSabM));
			Turno tSabT = new Turno(0, "Sab Tarde"+nd, "16:00:00", "22:00:00", "16:00:00", 0);
			tSabT.setIdTurno(c.insertTurno(tSabT));
			
			// Contratos
			String patron7hM = "6:" + t7hM.getIdTurno() + "/1:d/" +
			                   "6:" + t7hT.getIdTurno() + "/1:d";
			Contrato c7hM = new Contrato("7 horas mañana "+nd, 0, t7hM.getIdTurno(), 14, patron7hM, 800, 1);
			c7hM.setNumeroContrato(c.insertContrato(c7hM));
			String patron7hT = "6:" + t7hT.getIdTurno() + "/1:d/" +
	         				   "6:" + t7hM.getIdTurno() + "/1:d";
			Contrato c7hT = new Contrato("7 horas tarde "+nd, 0, t7hT.getIdTurno(), 14, patron7hT, 800, 1);
			c7hT.setNumeroContrato(c.insertContrato(c7hT));
			
			String patron4h = "7:" + t4hM.getIdTurno() + "," + t4hT.getIdTurno();
			Contrato c4hM = new Contrato("4 horas mañana "+nd, 0, t4hM.getIdTurno(), 7, patron4h, 600, 4);
			c4hM.setNumeroContrato(c.insertContrato(c4hM));
			Contrato c4hT = new Contrato("4 horas tarde "+nd, 0, t4hT.getIdTurno(), 7, patron4h, 600, 4);
			c4hT.setNumeroContrato(c.insertContrato(c4hT));
			
			String patronSab = "5:d/2:" + tSabM.getIdTurno() + "," + tSabT.getIdTurno();	
			Contrato cSabM = new Contrato("Sabadero mañana "+nd, 0, tSabM.getIdTurno(), 7, patronSab, 600, 4);
			cSabM.setNumeroContrato(c.insertContrato(cSabM));
			Contrato cSabT = new Contrato("Sabadero tarde "+nd, 0, tSabT.getIdTurno(), 7, patronSab, 600, 4);
			cSabT.setNumeroContrato(c.insertContrato(cSabT));
			
			// turnosPorContrato - solo hace falta insertar los que sean distintos del inicial del contrato
			//c.insertTurnoPorContrato(t7hM.getIdTurno(), c7hM.getNumeroContrato());
			c.insertTurnoPorContrato(t7hT.getIdTurno(), c7hM.getNumeroContrato());
			c.insertTurnoPorContrato(t7hM.getIdTurno(), c7hT.getNumeroContrato());
			//c.insertTurnoPorContrato(t7hT.getIdTurno(), c7hT.getNumeroContrato());
			
			//c.insertTurnoPorContrato(t4hM.getIdTurno(), c4hM.getNumeroContrato());
			c.insertTurnoPorContrato(t4hT.getIdTurno(), c4hM.getNumeroContrato());
			c.insertTurnoPorContrato(t4hM.getIdTurno(), c4hT.getNumeroContrato());
			//c.insertTurnoPorContrato(t7hT.getIdTurno(), c4hT.getNumeroContrato());
			
			//c.insertTurnoPorContrato(tSabM.getIdTurno(), cSabM.getNumeroContrato());
			c.insertTurnoPorContrato(tSabT.getIdTurno(), cSabM.getNumeroContrato());
			c.insertTurnoPorContrato(tSabM.getIdTurno(), cSabT.getNumeroContrato());
			//c.insertTurnoPorContrato(tSabT.getIdTurno(), cSabT.getNumeroContrato());
			
			// Empleados contrato "7 horas": 3 de mañana y 3 de tarde
			c.insertUsuario(10000200+nd*1000, "7hM1", "ape1", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c7hM.getNumeroContrato(), t7hM.getIdTurno());
			c.insertUsuario(10000201+nd*1000, "7hM2", "ape2", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c7hM.getNumeroContrato(), t7hM.getIdTurno());
			c.insertUsuario(10000202+nd*1000, "7hM3", "ape3", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c7hM.getNumeroContrato(), t7hM.getIdTurno());
			c.insertUsuario(10000300+nd*1000, "7hT1", "ape4", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c7hT.getNumeroContrato(), t7hT.getIdTurno());
			c.insertUsuario(10000301+nd*1000, "7hT2", "ape5", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c7hT.getNumeroContrato(), t7hT.getIdTurno());
			c.insertUsuario(10000302+nd*1000, "7hT3", "ape6", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c7hT.getNumeroContrato(), t7hT.getIdTurno());
			c.insertDepartamentoUsuario(10000200+nd*1000, depart);
			c.insertDepartamentoUsuario(10000201+nd*1000, depart);
			c.insertDepartamentoUsuario(10000202+nd*1000, depart);
			c.insertDepartamentoUsuario(10000300+nd*1000, depart);
			c.insertDepartamentoUsuario(10000301+nd*1000, depart);
			c.insertDepartamentoUsuario(10000302+nd*1000, depart);
			
			// Empleados contrato "4 horas": 2 de mañana y 2 de tarde
			c.insertUsuario(10000400+nd*1000, "4hM1", "ape7", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c4hM.getNumeroContrato(), t4hM.getIdTurno());
			c.insertUsuario(10000401+nd*1000, "4hM2", "ape8", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c4hM.getNumeroContrato(), t4hM.getIdTurno());
			c.insertUsuario(10000500+nd*1000, "4hT1", "ape9", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c4hT.getNumeroContrato(), t4hT.getIdTurno());
			c.insertUsuario(10000501+nd*1000, "4hT2", "ape10", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, c4hT.getNumeroContrato(), t4hT.getIdTurno());
			c.insertDepartamentoUsuario(10000400+nd*1000, depart);
			c.insertDepartamentoUsuario(10000401+nd*1000, depart);
			c.insertDepartamentoUsuario(10000500+nd*1000, depart);
			c.insertDepartamentoUsuario(10000501+nd*1000, depart);
			
			// Empleados contrato "Sabaderos": 3 de mañana y 3 de tarde
			c.insertUsuario(10000600+nd*1000, "SabM1", "ape11", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, cSabM.getNumeroContrato(), tSabM.getIdTurno());
			c.insertUsuario(10000601+nd*1000, "SabM2", "ape12", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, cSabM.getNumeroContrato(), tSabM.getIdTurno());
			c.insertUsuario(10000602+nd*1000, "SabM3", "ape13", "ape2", nacimiento, 0, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, cSabM.getNumeroContrato(), tSabM.getIdTurno());
			c.insertUsuario(10000700+nd*1000, "SabT1", "ape14", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, cSabT.getNumeroContrato(), tSabT.getIdTurno());
			c.insertUsuario(10000701+nd*1000, "SabT2", "ape15", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, cSabT.getNumeroContrato(), tSabT.getIdTurno());
			c.insertUsuario(10000702+nd*1000, "SabT3", "ape16", "ape2", nacimiento, 1, "email@email.es", "1234", 1,
					inicio, inicio, 0, 0, 0, 1, cSabT.getNumeroContrato(), tSabT.getIdTurno());
			c.insertDepartamentoUsuario(10000600+nd*1000, depart);
			c.insertDepartamentoUsuario(10000601+nd*1000, depart);
			c.insertDepartamentoUsuario(10000602+nd*1000, depart);
			c.insertDepartamentoUsuario(10000700+nd*1000, depart);
			c.insertDepartamentoUsuario(10000701+nd*1000, depart);
			c.insertDepartamentoUsuario(10000702+nd*1000, depart);
			
		}
		
		bd.cerrarConexion();
		
	}

	public static void main(String[] args){
		
		//Inserta 20 nuevos departamentos para pruebas con sus datos independientes
//		InsertaDatosFijos.insertarNdepart(20);
		
	}

}
