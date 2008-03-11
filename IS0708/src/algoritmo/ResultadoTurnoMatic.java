package algoritmo;


/**
 * Esta clase será el resultado del TurnoMatic, tanto el cuadrante generado 
 * como las sugerencias que se darán al usuario si el cuadrante no está completo
 * @author Ana
 *
 */

public class ResultadoTurnoMatic {

	private Cuadrante cuadrante;
	private Resumen resumen;
	
	public ResultadoTurnoMatic (){	
	}
	
	public ResultadoTurnoMatic (Cuadrante c, Resumen r) {
		this.cuadrante=c;
		this.resumen=r;
	}
	
	public Cuadrante getCuadrante () {
		return cuadrante;
	}
	
	public Resumen getResumen () {
		return resumen;
	}
	
	public void setCuadrante (Cuadrante c) {
		this.cuadrante=c;
	}
	
	public void setResumen (Resumen r) {
		this.resumen=r;
	}
	
}
