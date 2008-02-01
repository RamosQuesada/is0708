package paquete_pruebas;

import java.sql.Date;
import java.sql.Time;

import aplicacion.Controlador;
import aplicacion.Database;
import aplicacion.Empleado;
import aplicacion.Turno;
import aplicacion.Contrato;
import aplicacion.Departamento;
import aplicacion.Util;

import java.util.ArrayList;
import java.util.Random;

public class GeneraDatos {

	// Os he quitado el main y he puesto un botón en la aplicación que resetea la base de datos.
	// También he añadido el departamento al jefe.
	// Dani
	
	/**
	 * @param args
	 */
	public static void reset() {
		////////////////////////////////////////////////
		//atributos de usuario
		////////////////////////////////////////////////
		int id;
		ArrayList<String> nombres=new ArrayList<String>();//aqui almacenamos tantos nombres como indique la variable valor
		ArrayList<String> apellidos=new ArrayList<String>();//aqui almacenamos tantos apellidos como indique la variable valor
		ArrayList<Integer> ids=new ArrayList<Integer>();
		String nombre;
		String apellido1;
		String apellido2;
		int sexo;
		String email;
		ArrayList<String> passwords=new ArrayList<String>();//aqui almacenamos tantas passwords como indique la variable valor
		String password;
		int indicadorGrupo; 
		Date fechaNac=new Date(1975-01-01);
		Date fechaContrato=new Date(1975-01-01);
		Date fechaEntrada=new Date(1975-01-01);
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
		String idDepartamento;
		
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
		int tipoContrato;
		////////////////////////////////////////////////
		
		//comenzamos 
		Database bd = new Database();
		Controlador c=new Controlador(bd,false);
		bd.run();
		c.vaciarTodasTablas();//borramos las tablas
		/*c.vaciarTabla("TURNOS");//VACIAMOS SOLO LAS TABLAS QUE RELLENAMOS Y LAS QUE VAN "LIGADAS"
		c.vaciarTabla("CONTRATO");
		c.vaciarTabla("DISTRIBUCION");
		c.vaciarTabla("DEPARTAMENTO");
		c.vaciarTabla("ListaTurnosPorContrato");
		c.vaciarTabla("USUARIO");
		c.vaciarTabla("MENSAJE");
		c.vaciarTabla("VENTAS");*/
		Random rnd=new Random(10);
		//int valor=15; //Esta variable nos valdra para saber cuantos datos tenemos que generar
		int numero_de_contratos=15;
		int numero_de_turnos=15;
		int numero_de_turnos_contrato=15;
		int numero_usuarios=15;
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
		
		Turno turno;
		Contrato contrato;
		Empleado empleado;
		//crear turno jefe departamento
		turno=new Turno(0,"M1","9:00:00","14:00:00","12:30:00",20);//turno por defecto para el jefe
		
		//crear contrato jefe departamento
		contrato=new Contrato("jefe",0,1,7,"5T2D",1200,0); //no hace falta
	
		//crear el jefe de departamento
		c.insertUsuario(12345678,"jefe","","",fechaNac,0,"Juanfran@ajandemore.es","boss",0,fechaContrato,fechaEntrada,0,0,0,2,contrato.getNumeroContrato(),turno.getIdTurno());//hay que cambiar el rango a 2, 
		c.insertDepartamentoUsuario(12345678, "prueba");
		
		//creamos un nuevo departamento
	    c.insertDepartamentoPruebas("prueba",12345678);//se supone que el jefe será el que acabamos de crear
		
		//he comentado lo anterior porque no se si solo tenemos que generar un departamento o mas de un departamento

		//creamos la distribucion
	    System.out.println("INSERTAMOS DISTRIBUCION");
		for (int i=1; i<=7; i++) {
			for (int j=9; j<23; j++) {
				c.insertDistribucion(j, i, "1e1p", 5, 3, "prueba");
			}	
		}
		/*for (int i = 0; i < valor ; i++) {
	        	hora = (int)(rnd.nextInt(25));//con esto generamos numeros entre 0 y 24, siempre se pone limite +1
	        	diaSemana=(int)(rnd.nextInt(8));//generamos numeros entre 0 y 7
	        	patron=(int)(rnd.nextInt(4))+"e"+ (int)(rnd.nextInt(4))+"p";//supongo que lo que aqui salga dara igual,da lo mismo mismo que haya mas expertos que inexpertos y viceversa
	        	NumMin=(int)(rnd.nextInt(4));
	        	NumMax=(int)(rnd.nextInt(4))+NumMin;//para que sea coherente,no puedo ser mayor el min que el max
	        	idDepartamento="prueba";//ponemos de prueba este departamento
	        	System.out.println("DISTRIBUCION");
	        	System.out.println("//////////////////////////");
	        	System.out.println("hora: "+hora);
	        	System.out.println("Dia semana "+diaSemana);
	        	System.out.println("Patron: "+patron);
	        	System.out.println("NumMin: "+NumMin);
	        	System.out.println("Nummax: "+NumMax);
	        	System.out.println("Id deparatmento: "+idDepartamento);
	        	System.out.println("//////////////////////////");
	        	System.out.println();
	        	
	        	c.insertDistribucion(hora, diaSemana, patron, NumMax, NumMin,"prueba");//aqui insertamos las distribuciones	
	    	}*/
		
		//rellenar los turnos
		
		int ha,hb,hc,hd,he,hf,hg,hh;
		for (int i = 0; i < numero_de_turnos ; i++) {
			idTurno=0;//la base de datos genera automaticamente la id del turno
			Descripcion="genearcion de datos aleatorios";
			HoraEntrada=new Time((int)((rnd.nextInt(9)+8)),(int)(rnd.nextInt(60)),(int)(rnd.nextInt(60)));//es asi¿¿
			HoraSalida=new Time((int)(rnd.nextInt(4)+HoraEntrada.getHours()+4),(int)(rnd.nextInt(60)),(int)(rnd.nextInt(60)));//hay que hacer un rango
			HoraInicioDescanso=new Time((int)(((HoraSalida.getHours()-HoraEntrada.getHours())/2)+HoraEntrada.getHours()),(int)(rnd.nextInt(60)),(int)(rnd.nextInt(60)));
			Duracion=(int)(rnd.nextInt(31));//media hora,mas no jejjej
			turno=new Turno(idTurno,Descripcion,HoraEntrada,HoraSalida,HoraInicioDescanso,Duracion);
			System.out.println("TURNOS");
        	System.out.println("//////////////////////////");
        	System.out.println("Descripcion: "+Descripcion);
        	System.out.println("Hora entrada: "+HoraEntrada);
        	System.out.println("HoraSalida: "+HoraSalida);
        	System.out.println("Hora inicio descanso: "+HoraInicioDescanso);
        	System.out.println("Duracion del descansito: "+Duracion);
        	System.out.println("//////////////////////////");
        	System.out.println();
        	c.insertTurno(turno);
		}
		
		//rellenar contrato
		for (int i = 0; i < numero_de_contratos ; i++) {
			
			turnoInicial=c.getIdsTurnos().get((int)(rnd.nextInt(c.getIdsTurnos().size())));
			nombre="aleatorio"+i;//creamos otro arraylist para los nombres de los contratos??
			int dias_trabaja=(int)(rnd.nextInt(7))+1;//acotamos entre 1 y 7
			patron=dias_trabaja+":"+turnoInicial; //  dias_trabaja/turnoInicial, falta añadir que descanse aleatoriamente
			duracionCiclo=dias_trabaja;
			salario=(int)(rnd.nextInt(1500));
			tipoContrato=(int)(rnd.nextInt(4))+1;//acotamos entre 1 y 4 segun me han comentado
			contrato=new Contrato(nombre,0,turnoInicial,duracionCiclo,patron,salario,tipoContrato);
			System.out.println("CONTRATOS");
        	System.out.println("//////////////////////////");
        	System.out.println("idContrato: "+0);
        	System.out.println("turnoInicial: "+turnoInicial);
        	System.out.println("nombre: "+nombre);
        	System.out.println("patron: "+patron);
        	System.out.println("Duracion ciclo: "+duracionCiclo);
        	System.out.println("salario: "+salario);
        	System.out.println("Tipo de Contrato: "+tipoContrato);
        	System.out.println("//////////////////////////");
        	System.out.println();
        	
			idContrato = c.insertContrato(contrato);
		}
		//rellenar turnosPorContrato
		for (int i = 0; i < numero_de_turnos_contrato ; i++) {
			
			idContrato=c.getIdsContratos().get((int)(rnd.nextInt(c.getIdsContratos().size())));//selecciono contrato al azar
			idTurno=c.getIdsTurnos().get((int)(rnd.nextInt(c.getIdsTurnos().size())));//selecciono turno al azar
				
			System.out.println("TURNOS POR CONTRATO");
        	System.out.println("//////////////////////////");
        	System.out.println("idContrato: "+idContrato);
        	System.out.println("idTurno: "+idTurno);
          	System.out.println("//////////////////////////");
        	System.out.println();
        	c.insertTurnoPorContrato(idTurno, idContrato);
        	//despues de esta llamada la base de datos tiene que actualizar patron,duracion ciclo,
        	Contrato ct=c.getContrato(idContrato);
        	int dias_trabaja1=(int)(rnd.nextInt(7))+1;//acotamos entre 1 y 7
        	String pat=ct.getPatron()+"/"+dias_trabaja1+":"+idTurno;//debemos añadir al opcion de meter mas un turno o que descansa
        	int dur_ciclo=ct.getDuracionCiclo()+dias_trabaja1;//Actulizamos la duracion del ciclo
        	System.out.println("nuevo patron: "+pat+" duracion: "+dur_ciclo);
        	ct.setPatron(pat);
        	ct.setDuracionCiclo(dur_ciclo);
        	c.setContrato(ct);
        	//llamar a la base de datos con un metodo que se le meta (patron,duracion ciclo,idContrato)
		}
		//insertar usuarios en departamento
		for (int i = 0; i < numero_usuarios ; i++) {
			ids.add((int)(rnd.nextInt(39999999)));//añado los id de los usuarios al arraylist
			c.insertDepartamentoUsuario(ids.get(i),"prueba");
			System.out.println("Id de usuarios: "+ids.get(i));
		}
		//rellenamos los usuarios
		for (int i = 0; i < numero_usuarios ; i++) {
			id=ids.get(i);//se supone que tiene que tener 8 cifras creo
			nombre=nombres.get((int)(rnd.nextInt(nombres.size())));
			apellido1=apellidos.get((int)(rnd.nextInt(apellidos.size())));
			apellido2=apellidos.get((int)(rnd.nextInt(apellidos.size())));
			sexo=(int)(rnd.nextInt(2));
			email=id+"@turnomatic.es";
			password=passwords.get((int)(rnd.nextInt(passwords.size())));
			indicadorGrupo=(int)(rnd.nextInt(3));//¿¿que es??
			hh=(int)rnd.nextInt(50);
			ha=1992-hh;
			hb=2008-((int)rnd.nextInt(2008-ha));
			if (hb==2008) {
				hb--;
			}
			hc=2008-((int)rnd.nextInt(2008-hb));
			hd=(int) rnd.nextInt(13);
			he=(int) rnd.nextInt(13);
			hf=(int) rnd.nextInt(13);
			fechaNac=new Date(ha-1900,hd,Util.dameDias(hd, ha));//año,mes,dia lo suyo para los dias seria llamar a la funcion que hicimos que te decia el numero de dias de un mes dado
			fechaContrato=new Date(hb-1900,he,Util.dameDias(he,hb));
			fechaEntrada=new Date(hc-1900,hf,Util.dameDias(hf,hc));
			horasExtras=(int)(rnd.nextInt(3));
			felicidad=(int)(rnd.nextInt(3));//cuando sepamos los niveles de felicidad asi lo acotamos
			idioma=(int)(rnd.nextInt(3));
			rango=1;//rango de empleado
			idContrato=c.getIdsContratos().get((int)(rnd.nextInt(c.getIdsContratos().size())));
			System.out.println("idContrato: "+idContrato);
			System.out.println("Longitud el arrayList: "+c.getTurnosDeUnContrato(idContrato).size());
			idTurno=c.getTurnosDeUnContrato(idContrato).get((int)(rnd.nextInt(c.getTurnosDeUnContrato(idContrato).size()))).getIdTurno();
        	System.out.println("//////////////////////////");
        	System.out.println("id: "+id);
        	System.out.println("nombre: "+nombre);
        	System.out.println("apellido1: "+apellido1);
        	System.out.println("apellido2: "+apellido2);
        	System.out.println("sexo: "+sexo);
        	System.out.println("email: "+email);
        	System.out.println("password: "+password);
        	System.out.println("indicadorGrupo: "+indicadorGrupo);
        	System.out.println("Fecha Nacimiento: "+fechaNac);
        	System.out.println("Fecha Contrato: "+fechaContrato);
        	System.out.println("Fecha Entrada: "+fechaEntrada);
        	System.out.println("horasExtras: "+horasExtras);
        	System.out.println("Felicidad: "+felicidad);
        	System.out.println("idioma: "+idioma);
        	System.out.println("rango: "+rango);
        	System.out.println("idContrato: "+idContrato);
        	System.out.println("idTurno: "+idTurno);
        	System.out.println("//////////////////////////");
        	System.out.println();
			c.insertUsuario(id, nombre, apellido1, apellido2, fechaNac, sexo, email, password, indicadorGrupo, fechaContrato, fechaEntrada, horasExtras, felicidad, idioma, rango, idContrato, idTurno);
    	}

		bd.cerrarConexion();
	}

}
