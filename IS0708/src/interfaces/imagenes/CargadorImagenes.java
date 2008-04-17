package interfaces.imagenes;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class CargadorImagenes {
	private Image icoGr, icoPq, ico_imprimir, ico_cuadrante, ico_chico,
	ico_chica, ico_chicos, fondoLogin, ico_mens_l, ico_mens, fondoAdmin, grafica,carga;		
	
	public CargadorImagenes(Display display) {
		icoGr				= new Image(display, CargadorImagenes.class.getResourceAsStream("icoGr.gif"));
		icoPq				= new Image(display, CargadorImagenes.class.getResourceAsStream("icoPq.gif"));
		ico_imprimir		= new Image(display, CargadorImagenes.class.getResourceAsStream("ico_imprimir.gif"));
		ico_cuadrante		= new Image(display, CargadorImagenes.class.getResourceAsStream("ico_cuadrante.gif"));
		ico_chico			= new Image(display, CargadorImagenes.class.getResourceAsStream("ico_chico.gif"));
		ico_chica			= new Image(display, CargadorImagenes.class.getResourceAsStream("ico_chica.gif"));
		ico_chicos			= new Image(display, CargadorImagenes.class.getResourceAsStream("ico_chicos.gif"));
		fondoLogin			= new Image(display, CargadorImagenes.class.getResourceAsStream("intro.png"));
		ico_mens_l			= new Image(display, CargadorImagenes.class.getResourceAsStream("ico_mens1_v.gif"));
		ico_mens			= new Image(display, CargadorImagenes.class.getResourceAsStream("ico_mens2_v.gif"));
		fondoAdmin			= new Image(display, CargadorImagenes.class.getResourceAsStream("admin.png"));
		grafica				= new Image(display, CargadorImagenes.class.getResourceAsStream("grafica_1.ico"));
		carga				= new Image(display, CargadorImagenes.class.getResourceAsStream("turnomatic.gif"));
	}
	
	public void dispose() {
		icoGr.dispose();
		icoPq.dispose();
		ico_imprimir.dispose();
		ico_cuadrante.dispose();
		ico_chico.dispose();
		ico_chica.dispose();
		ico_chicos.dispose();
		fondoLogin.dispose();
		ico_mens_l.dispose();
		ico_mens.dispose();
		fondoAdmin.dispose();
		carga.dispose();
		
	}


	
	public Image getIcoGr() {
		return icoGr;
	}

	public Image getIcoPq() {
		return icoPq;
	}

	public Image getIco_imprimir() {
		return ico_imprimir;
	}

	public Image getIco_cuadrante() {
		return ico_cuadrante;
	}

	public Image getIco_chico() {
		return ico_chico;
	}

	public Image getIco_chica() {
		return ico_chica;
	}

	public Image getIco_chicos() {
		return ico_chicos;
	}

	public Image getFondoLogin() {
		return fondoLogin;
	}

	public Image getIco_mens_l() {
		return ico_mens_l;
	}

	public Image getIco_mens() {
		return ico_mens;
	}

	public Image getFondoAdmin() {
		return fondoAdmin;
	}

	public Image getGrafica() {
		return grafica;
	};
	
	public Image getCarga(){
		return carga;
	}
}
