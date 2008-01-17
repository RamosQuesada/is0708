package paquete_pruebas;

import java.sql.Date;
import java.sql.Time;

import aplicacion.Database;
import aplicacion.Empleado;
import aplicacion.Turno;
import aplicacion.Contrato;
import aplicacion.Departamento;

import java.util.ArrayList;
import java.util.Random;

public class GeneraDatos {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		////////////////////////////////////////////////
		//atributos de usuario
		////////////////////////////////////////////////
		int id;
		String nombre;
		String apellido1;
		String apellido2;
		int sexo;
		String email;
		String password; 
		int indicadorGrupo; 
		Date fechaNac=new Date(1970-01-01);
		Date fechaContrato=new Date(1975-01-01);
		Date fechaEntrada=new Date(1970-02-01);
		int horasExtras;
		int felicidad;
		int idioma;
		int rango; 
		int idContrato; 
		int idTurno;
		
		////////////////////////////////////////////////
		//atributos de departamento
		////////////////////////////////////////////////
		int hora;
		int diaSemana;
		String patron;
		int NumMax;
		int NumMin;
		int idDepartamento;
		////////////////////////////////////////////////
		//atributos Turno
		////////////////////////////////////////////////
		String Descripcion;
		Time HoraEntrada;
		Time HoraSalida;
		Time HoraInicioDescanso;
		int Duracion;
		////////////////////////////////////////////////
		//atributos contrato
		////////////////////////////////////////////////
		//int idContrato;
		int turnoInicial;
		//String nombre;
		//String patron;
		int duracionCiclo;
		double salario;
		int tipocontrato;
		////////////////////////////////////////////////
		Database bd = new Database();
		bd.run();
		Random rnd=new Random(10);
		int valor=10; //Esta variable nos valdra para saber cuantos datos tenemos que generar
		
		//crear turno jefe departamento
		//Turno turno=new Turno(0,"M1","9:00:00","14:00:00","12:30:00",20);//turno por defecto para el jefe
		
		//crear contrato jefe departamento
		//Contrato contrato=new Contrato("jefe",0,1,7,"5/1/2/d",1200,0); no hace falta
		
		//crear el jefe de departamento
		//bd.insertarUsuario(12345678,"jefe","","",fechaNac,0,"Juanfran@ajandemore.es","boss",0,fechaContrato,fechaEntrada,0,0,0,0,0,0);
		
		
		//creamos un nuevo departamento
		//bd.insertarDepartamento("prueba",12345678);//se supone que el jefe será el que acabamos de crear
		
		
		//creamos la distribucion

		for (int i = 0; i < valor ; i++) {
	        	hora = (int)(rnd.nextInt(25));//con esto generamos numeros entre 0 y 24, siempre se pone limite +1
	        	diaSemana=(int)(rnd.nextInt(8));//generamos numeros entre 0 y 7
	        	patron=(int)(rnd.nextInt(4))+"e"+ (int)(rnd.nextInt(4))+"p";//supongo que lo que aqui salga dara igual,da lo mismo mismo que haya mas expertos que inexpertos y viceversa
	        	NumMin=(int)(rnd.nextInt(4));
	        	NumMax=(int)(rnd.nextInt(4))+NumMin;//para que sea coherente,no puedo ser mayor el min que el max
	        	idDepartamento=0;//que ponemos aqui??
	        	System.out.println(hora);
	        	System.out.println(diaSemana);
	        	System.out.println(patron);
	        	System.out.println(NumMin);
	        	System.out.println(NumMax);
	        	System.out.println(idDepartamento);
	        	bd.insertarDistribucion(hora,diaSemana,patron,NumMax,NumMin,idDepartamento);//aqui insertamos las distribuciones	
	    	}
		//rellenar los turnos
		//int idTurno;
		
		for (int i = 0; i < valor ; i++) {
			
			idTurno=(int)(rnd.nextInt(3));//¿¿como lo hacemos??
			Descripcion="";//¿¿??
			HoraEntrada=new Time((int)(rnd.nextInt(25)),(int)(rnd.nextInt(61)),(int)(rnd.nextInt(61)));//es asi¿¿
			HoraSalida=new Time((int)(rnd.nextInt(25)),(int)(rnd.nextInt(61)),(int)(rnd.nextInt(61)));//hay que hacer un rango
			HoraInicioDescanso=new Time((int)(rnd.nextInt(25)),(int)(rnd.nextInt(61)),(int)(rnd.nextInt(61)));
			Duracion=(int)(rnd.nextInt(31));//media hora,mas no jejjej
		}
		//rellenar turnosPorContrato
		for (int i = 0; i < valor ; i++) {
			idTurno=(int)(rnd.nextInt(3));//¿¿??
			idContrato=idTurno=(int)(rnd.nextInt(3));//hay que mirar cuantos tipos de contrato existen
			bd.insertarTurnoPorContrato(idTurno, idContrato);
		}
		
		//rellenar contrato
		for (int i = 0; i < valor ; i++) {
			idContrato=idTurno=(int)(rnd.nextInt(3));//hay que mirar cuantos tipos de contrato existen
			turnoInicial=(int)(rnd.nextInt(5));//hay que mirar tipo de turnos
			nombre="";
			patron="";//hay que obtener el patron 
			duracionCiclo=(int)(rnd.nextInt(3));
			salario=(double)(rnd.nextInt(1500));
			tipocontrato=(int)(rnd.nextInt(3));//hay que mirar tipos de contratos
			bd.insertarContrato(idContrato, turnoInicial, nombre, patron, duracionCiclo, salario, tipocontrato);
		}
		
		//rellenamos los usuarios
		
		for (int i = 0; i < valor ; i++) {
			id=(int)(rnd.nextInt(8));
			nombre="";
			apellido1="";
			apellido2="";
			sexo=(int)(rnd.nextInt(2));
			email="";
			password="";
			indicadorGrupo=(int)(rnd.nextInt(3));//¿¿que es??
			horasExtras=(int)(rnd.nextInt(3));
			felicidad=(int)(rnd.nextInt(3));//cuando sepamos los niveles de felicidad asi lo acotamos
			idioma=(int)(rnd.nextInt(3));
			rango=(int)(rnd.nextInt(3));//¿¿??
			idContrato=(int)(rnd.nextInt(3));//hay que saber cuantos contratos hay
			idTurno=(int)(rnd.nextInt(4));//hay que calcular todos los tipos de turno que existe
			bd.insertarUsuario(id, nombre, apellido1, apellido2, fechaNac, sexo, email, password, indicadorGrupo, fechaContrato, fechaEntrada, horasExtras, felicidad, idioma, rango, idContrato, idTurno);
    	}

		bd.cerrarConexion();
	}

}
