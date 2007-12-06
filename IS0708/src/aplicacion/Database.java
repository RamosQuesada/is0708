package aplicacion;

import java.sql.*;
/**
 * Aquí se encuentran los métodos de acceso a la base de datos.
 * @author Camilo
 *
 */
public class Database {	
	Connection con;	
	Statement st;
	ResultSet rs;
	
	// Tras una hora de pelea con Eclipse y MySQL, por fin tenemos
	// conexion con la BD ;)
	public void abrirConexion() {
		try { 
			String userName="turnomat_user";
	        String password="is0708";
	        String bd = "turnomat_bd";
	        String url="jdbc:mysql://72.34.56.241:3306/"+bd; 
	        Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	        con = DriverManager.getConnection(url, userName, password); 
	        System.out.println("Conexión a la BD"); 
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println("Error en conexión"); 
		} 
	}
	
	public void cerrarConexion() {
		try {
			con.close();
			System.out.println("Conexion Cerrada Correctamente");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error cerrando la conexion");
		}
	}
	public Empleado dameEmpleado(int nvend) {
		
		// Pongo return null para que no diga que hay un error
		return null;
		
	}
	
	public static void main(String[] IS0708) {
		@SuppressWarnings("unused")
		Database prueba = new Database();
		prueba.abrirConexion();
		prueba.cerrarConexion();
	}
}
