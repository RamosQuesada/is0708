package aplicacion;

import interfaces.I02_Menu_principal;

public class Controlador {
	private I02_Menu_principal _vista;
	private Database _baseDatos;
	
	public Controlador(Database baseDatos){
		this._baseDatos=baseDatos;
	}
	
	public void incluyeVista(I02_Menu_principal vista){
		this._vista=vista;
	}
	
	public Controlador(I02_Menu_principal vista,Database baseDatos){
		this._vista=vista;
		this._baseDatos=baseDatos;
	}
	
	
	//TODO BD
	public void guardaDepartamento(Departamento departamento){
		
	}
	
	//TODO BD
	public Empleado obtenEmpleado(String nombreEmpleado) {
		// TODO Auto-generated method stub
		return null;
	} 
	
}
