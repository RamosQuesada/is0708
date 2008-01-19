package paquete_pruebas;

import java.sql.Date;
import java.sql.Time;

import aplicacion.Controlador;
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
		ArrayList<String> nombres=new ArrayList<String>();//aqui almacenamos tantos nombres como indique la variable valor
		ArrayList<String> apellidos=new ArrayList<String>();//aqui almacenamos tantos apellidos como indique la variable valor
		String nombre;
		String apellido1;
		String apellido2;
		int sexo;
		String email;
		ArrayList<String> passwords=new ArrayList<String>();//aqui almacenamos tantas passwords como indique la variable valor
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
		
		//comenzamos 
		Database bd = new Database();
		Controlador c=new Controlador(bd);
		bd.run();
		Random rnd=new Random(10);
		int contratos_hechos=0;//contador del numero de contratos
		int valor=10; //Esta variable nos valdra para saber cuantos datos tenemos que generar
		
		//insercion de nombres, apellidos y passwords en ArrayList manualmente
		///////////////////////////////////////////////////////////
		nombres.add("Carlos");
		nombres.add("Roberto");
		nombres.add("Jesus");
		nombres.add("Sergio");
		nombres.add("Miguel");
		nombres.add("Antonio");
		nombres.add("Javier");
		nombres.add("Alvaro");
		nombres.add("David");
		nombres.add("Manuel");
		nombres.add("Cristina");
		nombres.add("Laura");
		nombres.add("Rosa");
		nombres.add("Lucia");
		nombres.add("Raquel");
		nombres.add("Maria");
		nombres.add("Sandra");
		nombres.add("Diana");
		nombres.add("Veronica");
		nombres.add("Carolina");
		apellidos.add("Martinez");
		apellidos.add("Gonzalez");
		apellidos.add("Lopez");
		apellidos.add("Alonso");
		apellidos.add("Ramos");
		apellidos.add("Gutierrez");
		apellidos.add("Redondo");
		apellidos.add("Garcia");
		apellidos.add("Perez");
		apellidos.add("Fernandez");
		apellidos.add("Moreno");
		apellidos.add("Jimenez");
		apellidos.add("Sanchez");
		apellidos.add("Rey");
		apellidos.add("Olivares");
		apellidos.add("De la Fuente");
		apellidos.add("Espada");
		apellidos.add("Arevalo");
		apellidos.add("Salamanca");
		apellidos.add("Carretero");
		passwords.add("w5y790");
		passwords.add("nu62t5");
		passwords.add("kf0em9");
		passwords.add("ns0mce");
		passwords.add("y0dnc7");
		passwords.add("h8fn84");
		passwords.add("bdk30c");
		passwords.add("mc09s3");
		passwords.add("lcm3lm");
		passwords.add("ks9al9");
		passwords.add("ncp3m3");
		passwords.add("ip2n32");
		passwords.add("b3k3o3");
		passwords.add("sk30cn");
		passwords.add("psdn3f");
		passwords.add("sb239c");
		passwords.add("cmc3el");
		passwords.add("mdflv9");
		passwords.add("xn3j3k");
		passwords.add("ld0mf3");
		
		
		//crear turno jefe departamento
		//Turno turno=new Turno(0,"M1","9:00:00","14:00:00","12:30:00",20);//turno por defecto para el jefe
		
		//crear contrato jefe departamento
		//Contrato contrato=new Contrato("jefe",0,1,7,"5/1/2/d",1200,0); no hace falta
		
		//crear el jefe de departamento
		//bd.insertarUsuario(12345678,"jefe","","",fechaNac,0,"Juanfran@ajandemore.es","boss",0,fechaContrato,fechaEntrada,0,0,0,0,0,0);
		
		
		//creamos un nuevo departamento
		//bd.insertarDepartamento("prueba",12345678);//se supone que el jefe será el que acabamos de crear
		
		//he comentado lo anterior porque no se si solo tenemos que generar un departamento o mas de un departamento

		//creamos la distribucion

		for (int i = 0; i < valor ; i++) {
	        	hora = (int)(rnd.nextInt(25));//con esto generamos numeros entre 0 y 24, siempre se pone limite +1
	        	diaSemana=(int)(rnd.nextInt(8));//generamos numeros entre 0 y 7
	        	patron=(int)(rnd.nextInt(4))+"e"+ (int)(rnd.nextInt(4))+"p";//supongo que lo que aqui salga dara igual,da lo mismo mismo que haya mas expertos que inexpertos y viceversa
	        	NumMin=(int)(rnd.nextInt(4));
	        	NumMax=(int)(rnd.nextInt(4))+NumMin;//para que sea coherente,no puedo ser mayor el min que el max
	        	idDepartamento=0;//ponemos de prueba este departamento
	        	System.out.println(hora);
	        	System.out.println(diaSemana);
	        	System.out.println(patron);
	        	System.out.println(NumMin);
	        	System.out.println(NumMax);
	        	System.out.println(idDepartamento);
	        	//bd.insertarDistribucion(hora,diaSemana,patron,NumMax,NumMin,idDepartamento);//aqui insertamos las distribuciones	
	    	}
		
		//rellenar los turnos
		
		for (int i = 0; i < valor ; i++) {
			if(c.getListaTurnosEmpleadosDpto("prueba").isEmpty()){//si esta vacio
				idTurno=0;
			}else{
				idTurno=c.getListaTurnosEmpleadosDpto("prueba").get((int)(rnd.nextInt(c.getListaTurnosEmpleadosDpto("prueba").size()+1))).getIdTurno();
				
			}
			Descripcion="genearcion de datos aleatorios";//¿¿??
			HoraEntrada=new Time((int)(rnd.nextInt(25)),(int)(rnd.nextInt(61)),(int)(rnd.nextInt(61)));//es asi¿¿
			HoraSalida=new Time((int)(rnd.nextInt(25)),(int)(rnd.nextInt(61)),(int)(rnd.nextInt(61)));//hay que hacer un rango
			HoraInicioDescanso=new Time((int)(rnd.nextInt(25)),(int)(rnd.nextInt(61)),(int)(rnd.nextInt(61)));
			Duracion=(int)(rnd.nextInt(31));//media hora,mas no jejjej
			//bd.insertarTurno(idTurno, Descripcion, HoraEntrada, HoraSalida, HoraInicioDescanso, Duracion);
		}
		
		//rellenar turnosPorContrato
		for (int i = 0; i < valor ; i++) {
			if(c.getListaContratosDpto("prueba").isEmpty()){
				idContrato=0;
			}else{
				idContrato=c.getListaContratosDpto("prueba").get((int)(rnd.nextInt(c.getListaContratosDpto("prueba").size()+1))).getTipoContrato();
			}
			idTurno=
			//bd.insertarTurnoPorContrato(idTurno, idContrato);
		}
		
		//rellenar contrato
		for (int i = 0; i < valor ; i++) {
			idContrato=contratos_hechos;//contador de contratos
			contratos_hechos++;
			turnoInicial=c.getTurnosDeUnContrato(idContrato).get((int)(rnd.nextInt(c.getTurnosDeUnContrato(idContrato).size()+1))).getIdTurno();
			nombre="aleatorio"+1;//creamos otro arraylist para los nombres de los contratos??
			patron=(int)(rnd.nextInt(4))+"e"+ (int)(rnd.nextInt(4))+"p";//hay que obtener el patron 
			duracionCiclo=(int)(rnd.nextInt(3));
			salario=(int)(rnd.nextInt(1500));
			tipocontrato=(int)(rnd.nextInt(5))+1;//acotamos entre 1 y 5
			//bd.insertarContrato(idContrato, turnoInicial, nombre, patron, duracionCiclo, salario, tipocontrato);
		}
		
		//rellenamos los usuarios
		for (int i = 0; i < valor ; i++) {
			id=(int)(rnd.nextInt(39999999));//se supone que tiene que tener 8 cifras creo
			nombre=nombres.get((int)(rnd.nextInt(nombres.size()+1)));
			apellido1=apellidos.get((int)(rnd.nextInt(apellidos.size()+1)));
			apellido2=apellidos.get((int)(rnd.nextInt(apellidos.size()+1)));
			sexo=(int)(rnd.nextInt(2));
			email=nombre+"@turnomatic.com";
			password=passwords.get((int)(rnd.nextInt(passwords.size()+1)));
			indicadorGrupo=(int)(rnd.nextInt(3));//¿¿que es??
			horasExtras=(int)(rnd.nextInt(3));
			felicidad=(int)(rnd.nextInt(3));//cuando sepamos los niveles de felicidad asi lo acotamos
			idioma=(int)(rnd.nextInt(3));
			rango=(int)(rnd.nextInt(3));//¿¿??
			idContrato=(int)(rnd.nextInt(3));//hay que saber cuantos contratos hay
			idTurno=(int)(rnd.nextInt(4));//hay que calcular todos los tipos de turno que existe
			//bd.insertarUsuario(id, nombre, apellido1, apellido2, fechaNac, sexo, email, password, indicadorGrupo, fechaContrato, fechaEntrada, horasExtras, felicidad, idioma, rango, idContrato, idTurno);
    	}

		bd.cerrarConexion();
	}

}
