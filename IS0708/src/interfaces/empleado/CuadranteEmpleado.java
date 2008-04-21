package interfaces.empleado;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import aplicacion.Vista;
import aplicacion.datos.Empleado;
import aplicacion.datos.Turno;
import aplicacion.utilidades.Util;

public class CuadranteEmpleado {
	
	private int repreAvance=0;
	private Vista vista;
	private boolean redibujar;
	private boolean finSemana;
	private Display display;
	private int ancho;
	private int alto=100;
	private int margenIzq, margenDer, margenSup, margenInf; // M�rgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	
	private int tamanoFila;
	
	private int horaInicio, horaFin; // Definen de qu� hora a qu� hora es el cuadrante
	private int tamHora;
	
	private  int subdivisiones; // Cu�ntas subdivisiones hacer por hora (0 = sin subdivisiones)
	private Empleado empleado;
	private int tamano =8;
	private ResourceBundle _bundle;
	private Date fecha;
	
	private int avance=4;
	//private ArrayList<Integer> turnos;//SE USA?
	private ArrayList<Float> horasInicio;
	private ArrayList<Float> horasFin;
	private ArrayList<Float> horaComienzoDescanso;
	private ArrayList<Float> horaFinDescanso;
	private ArrayList<Time> horaInicioMes;
	private ArrayList<Time> horaFinMes;
	private int primerDiaMes;
	
	public void ponPrimerDiaMes(int dia){
		this.primerDiaMes=dia;
	}
	public void ponHoraInicioMes(ArrayList<Time> inicio){
		this.horaInicioMes=inicio;
	}
	
	public void ponRepreAvance(int i){
		this.repreAvance=i;
	}
	
	public int dameRepreAvance(){
		return this.repreAvance;
	}
	public void ponHoraFinMes(ArrayList<Time> fin){
		this.horaFinMes=fin;
	}
	
	//private ArrayList<Turno> tiposTurno;
	private ThreadsCuadrantes thread;
	//private I02_cuadrEmpl superior;
	private GC gc;

	public Empleado dameEmpleado(){
		return empleado;
	}
	public void ponSubdivisiones(int subdivisiones){
		this.subdivisiones=subdivisiones;
	}
	public void ponAvance(int avance){
		this.avance=avance;
	}
	public Date dameFecha(){
		return fecha;
	}
	public void ponFecha(Date fecha){
		this.fecha=fecha;
	}
	public void ponHorasFinDescanso(ArrayList<Float> horaFinDescanso){
		this.horaFinDescanso=horaFinDescanso;
	}
	
	public ArrayList<Float> dameHoraFinDescanso(){
		return this.horaFinDescanso;
	}
	
	public void anadeHoraFinDescanso(int posicion,Float horaFin){
		this.horaFinDescanso.add(posicion,horaFin);
	}
	
	public void ponHorasComienzoDescanso(ArrayList<Float> horaComienzoDescanso){
		this.horaComienzoDescanso=horaComienzoDescanso;
	}
	
	public ArrayList<Float> dameHoraComienzoDescanso(){
		return this.horaComienzoDescanso;
	}
	
	public void anadeHoraComienzoDescanso(int posicion,Float horaFin){
		this.horaComienzoDescanso.add(posicion,horaFin);
	}
	
	public void ponHorasFin(ArrayList<Float> horasFin){
		this.horasFin=horasFin;
	}
	
	public ArrayList<Float> dameHorasFin(){
		return this.horasFin;
	}
	
	public void anadeHoraFin(int posicion,Float horaFin){
		this.horasFin.add(posicion,horaFin);
	}
	
	public void ponHorasInicio(ArrayList<Float> horasInicio){
		this.horasInicio=horasInicio;
	}
	
	public ArrayList<Float> dameHorasInicio(){
		return this.horasInicio;
	}
	
	public void anadeHoraInicio(int posicion,Float horaInicio){
		this.horasInicio.add(posicion,horaInicio);
	}
	
	

	


	/**
	 * Constructor del cuadrante.
	 * @param d				Display sobre el que se dibujar� el cuadrante
	 * @param subdivisiones	N�mero de subdivisiones que se muestran en el cuadrante.  
	 * 						<ul>
	 * 						<li>12	(cada 5 min),
	 * 						<li>6	(cada 10 min),
	 * 						<li>4	(cada 15 min),
	 * 						<li>2	(cada 30 min),
	 * 						<li>1	(sin subdivisiones)
	 * 						</ul>
	 * @param horaInicio	Hora de inicio del cuadrante
	 * @param horaFin		Hora de fin del cuadrante. Las horas pasadas de las 24 se muestran
	 * 						como la madrugada del d�a siguiente.
	 * @param margenIzq		Margen izquierdo en p�xeles
	 * @param margenDer		Margen derecho en p�xeles
	 * @param margenSup		Margen superior en p�xeles
	 * @param margenInf		Margen inferior en p�xeles
	 * @param margenNombres	Margen de los nombres en p�xeles (indica d�nde empieza a dibujarse
	 * 						el cuadrante a partir del margen izquierdo, dejando un espacio para
	 * 						los nombres.
	 */
	public CuadranteEmpleado(Display d, int subdivisiones, int horaInicio, int horaFin, int margenIzq, 
			int margenDer, int margenSup, int margenInf, int margenNombres,ResourceBundle bundle,Empleado empleado,
			Date fecha,Vista vista,Cuadrantes sup) {
		display = d;
		_bundle=bundle;
		this.vista=vista;
	//	this.superior=sup;
		this.empleado=empleado;
		this.fecha=fecha;
		this.horasFin= new ArrayList<Float>();
		this.horasInicio = new ArrayList<Float>();
		this.horaFinDescanso = new ArrayList<Float>();
		this.horaComienzoDescanso =  new ArrayList<Float>();
		this.margenIzq  = margenIzq;
		this.margenDer  = margenDer;
		this.margenSup  = margenSup+20;
		this.margenInf  = margenInf+10;
		this.margenNombres  = margenNombres;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.subdivisiones = subdivisiones;
		

	}
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujar� el cuadrante.
	 * @param empleadoActivo	La posici�n del empleado a resaltar en la lista de empleados.
	 */
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujar� el cuadrante.
	 * @param empleadoActivo	La posici�n del empleado a resaltar en la lista de empleados.
	 */
	// TODO Deber�a lanzar una excepci�n si empleadoActivo > empleados.size
	public void dibujarCuadranteDia(GC gc, int empleadoActivo) {
		dibujarDias(gc);
	}
	
	public void dibujarCuadranteMes(GC gc){
		Calendar c = Calendar.getInstance();
		// Esto coge el d�a 1 de este mes
		final String[] diasSemana={
				"L","M","X","J","V","S","D"
			};
		if(this.avance!=0){
			boolean izq=false,der=false,arr=false,abajo=false;
			cambiarPincel(gc, 90,140,90);
			this.cambiarRelleno(gc, 90, 140, 90);
			gc.fillGradientRectangle(0,0,this.ancho,this.alto,true);
			
			for(int cont2=0;cont2<6;cont2++){
				for(int cont=0;cont<7;cont++){
					if(cont==0){izq=true;}
					if(cont==6){der=true;}
					if(cont2==0){arr=true;}
					if(cont2==5){abajo=true;}
					if(cont2==0){
					dibujarDiaMes(gc,true,cont,1,null,null,diasSemana[cont],izq,der,arr,abajo,1,1);
					}
					else{
						dibujarDiaMes(gc,false,cont,cont2+1,null,null,"",izq,der,arr,abajo,1,1);	
					}
					izq=der=arr=abajo=false;
				}
				
				}
			dibujarDiaMes(gc,false,0,1,null,null,"",false,false,false,false,7,1);
			dibujarDiaMes(gc,false,0,1,null,null,"",false,false,false,false,7,6);
			
			dibujarCarga(gc," CARGANDO BASE DATOS ");
			/*cambiarRelleno(gc,100,200,100);
			cambiarPincel(gc,100,200,100);
			gc.fillGradientRectangle(ancho/4, alto/4, this.ancho/2, this.alto/2, true);
			cambiarPincel(gc,0,0,0);
			gc.drawRectangle(ancho/3, alto/3, ancho/2, alto/2);
			int tamanoFuente=alto/20;
			Font fuente=gc.getFont();
			gc.setFont(new Font(display,"Times",tamanoFuente,SWT.BOLD));
			String text = " CARGANDO CUADRANTE";
			String text2 = "MENSUAL";
	        Point textSize = gc.textExtent(text);
	        Point textSize2 = gc.textExtent(text2);
	        gc.drawText(text,0+ancho/2-textSize.x/2, 0+alto/2+textSize.y/2,false);
	        gc.drawText(text2,0+ancho/2-textSize.x/2, 0+alto/2+textSize2.y/2+textSize.y,false);
	        cambiarPincel(gc,0,0,0);
			gc.getFont().dispose();
			gc.setFont(fuente);*/
		}
		else{
			c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		
		// Y esto en qu� d�a de la semana cae
		int primerDia = c.get(Calendar.DAY_OF_WEEK);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		c.roll(Calendar.DAY_OF_MONTH,false); // Pasa al �ltimo d�a del este mes
		int ultimoDia = c.get(Calendar.DAY_OF_MONTH);
		int anchoMes = ancho - margenIzq - margenDer - margenNombres;
		int anchoDia = anchoMes/ultimoDia;
		int altoFila = 20;
		// Dibujar n�meros de los d�as
		if (anchoDia>14)
			for (int j=0; j < ultimoDia; j++) {
		//		gc.drawText(String.valueOf(j+1), margenIzq + margenNombres + j*anchoDia + anchoDia/2, margenSup);
			}

		//gc.drawText(empleado.getNombre(), margenIzq, margenSup + 20 + 0*altoFila);
		//for (int j=0; j < ultimoDia; j++) {
		//	gc.drawRectangle(margenIzq + margenNombres + j*anchoDia, margenSup + 20 + 0*altoFila, anchoDia, altoFila);

		// Esto es para un calendario normal
		int altoMes = alto - margenSup - margenInf;
		int numSemanas = 5;
		int altoDia = alto/numSemanas;
		int diaSemana=this.primerDiaMes;
		int semana=1;
		if(horaInicioMes!=null){

		boolean izq=false,der=false,arr=false,abajo=false;
		
		cambiarPincel(gc, 90,140,90);
		this.cambiarRelleno(gc, 90, 140, 90);
		
		gc.fillGradientRectangle(0,0,this.ancho,this.alto,true);
		for(int cont2=0;cont2<6;cont2++){
		for(int cont=0;cont<7;cont++){
			if(cont==0){izq=true;}
			if(cont==6){der=true;}
			if(cont2==0){arr=true;}
			if(cont2==5){abajo=true;}
			if(cont2==0){
			dibujarDiaMes(gc,true,cont,1,null,null,diasSemana[cont],izq,der,arr,abajo,1,1);
			}
			else{
				dibujarDiaMes(gc,false,cont,cont2+1,null,null,"",izq,der,arr,abajo,1,1);	
			}
			izq=der=arr=abajo=false;
		}
		
		}
		for(int cont=0;cont<this.horaInicioMes.size();cont++){
			if(cont==0){izq=true;}
			if(cont==6){der=true;}
			dibujarDiaMes(gc,false,diaSemana-1,semana+1,horaInicioMes.get(cont),this.horaFinMes.get(cont),new Integer(cont+1).toString(),izq,der,false,false,1,1);
			diaSemana++;
			if(diaSemana==8){
				diaSemana=1;
				semana++;
			}
			izq=der=arr=abajo=false;
		}
		}
		dibujarDiaMes(gc,false,0,1,null,null,"",false,false,false,false,7,1);
		dibujarDiaMes(gc,false,0,1,null,null,"",false,false,false,false,7,6);
		}
		/*
		this.dibujarDiaMes(gc, 3, 1);
		this.dibujarDiaMes(gc, 4, 1);
		this.dibujarDiaMes(gc, 5, 1);
		this.dibujarDiaMes(gc, 6, 1);
		this.dibujarDiaMes(gc, 0, 1);
		this.dibujarDiaMes(gc, 1, 1);
		this.dibujarDiaMes(gc, 2, 1);
		this.dibujarDiaMes(gc, 2, 2);
		this.dibujarDiaMes(gc, 2, 3);
		this.dibujarDiaMes(gc, 2, 4);
		this.dibujarDiaMes(gc, 2, 5);*/
		
	}
	/**
	 * Dibuja lineas verticales representando los dias.
	 * @param gc	El GC del display sobre el que se dibujar� el cuadrante.
	 */
	private void dibujarDias(GC gc) {
		//cambiarPincel(gc, 40,80,40);
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep = (ancho - m - margenDer)/h;
		//int subsep = sep/subdivisiones;
		tamanoFila=(alto)/15;
		cambiarPincel(gc, 90,140,90);
		this.cambiarRelleno(gc, 90, 140, 90);
		gc.fillGradientRectangle(0,0,this.ancho,this.alto,true);
		
		cambiarPincel(gc, 0,143,65);
		this.cambiarRelleno(gc, 180,230,180);
		
		int inferior=((int)((alto-margenInf-(tamanoFila+this.margenSup))
				/(horaFin-horaInicio)))*(horaFin-horaInicio);

		gc.fillGradientRectangle(m,tamanoFila+this.margenSup,7*sep,inferior,false);
		int inferior_total=tamanoFila+margenSup+inferior;
		this.cambiarPincel(gc, 10, 160, 90);
		this.cambiarRelleno(gc, 0, 143, 65);
		gc.fillGradientRectangle(m,this.margenSup,7*sep,tamanoFila,true);

		
		cambiarPincel(gc, 0,0,0);
		gc.setLineWidth(3);
		gc.drawRectangle(m,tamanoFila+this.margenSup,7*sep,inferior);
		gc.setLineWidth(3);
		gc.drawLine(m, this.margenSup, m+7*sep, this.margenSup);
		gc.drawLine(m, tamanoFila+this.margenSup, m+7*sep, tamanoFila+this.margenSup);
		gc.drawLine(m, inferior_total, m+7*sep, inferior_total);
		gc.setLineWidth(3);
		gc.drawRoundRectangle(m,this.margenSup,7*sep,tamanoFila,8,8);
		gc.drawRectangle(m, this.margenSup, 7*sep, tamanoFila);
		gc.setLineWidth(2);
		for (int i=0; i<=h; i++) {
			gc.setLineStyle(SWT.LINE_SOLID);
			cambiarPincel(gc, 0,0,0);
			gc.drawLine(m+i*sep, this.margenSup, m+i*sep, inferior_total);
			gc.setLineStyle(SWT.LINE_DOT);
			if (i!=h)
			{	cambiarPincel(gc, 150,250,150);
				final String[] diasSemana={
						"L","M","X","J","V","S","D"
					};
				int tamano1= sep/10;
				int tamano2= tamanoFila/2;
				if (tamano1<tamano2){
					tamano = tamano1;
					}
				else{
					tamano=tamano2;
				}

				Font fuente=gc.getFont();
				gc.setFont(new Font(display,"Verdana",tamano+2,SWT.BOLD));
				String text = diasSemana[((i)%7)];
		        Point textSize = gc.textExtent(text);
		        gc.drawText(diasSemana[((i)%7)],((m+i*sep)+((m+(i+1)*sep)))/2-textSize.x/2, margenSup+(alto/50), true);
				gc.getFont().dispose();
				gc.setFont(fuente);
			}
		}
		gc.setLineStyle(SWT.LINE_SOLID);
		int num_subdivisiones=(int)((this.subdivisiones)*(this.horaFin-this.horaInicio)+1);
		for(int cont=0;cont<num_subdivisiones;cont++){
			float fraccion = 1.0f/this.subdivisiones;
			float hora=fraccion*cont;
			hora +=this.horaInicio;
			this.dibujarLineaHorizontal(gc, hora);
		}
		dibujarTurnos(gc);
		

	}
	
	public void actualizarFecha(Date fecha,GC gc){
		this.fecha=fecha;
		actualizarTurnos(gc);
	}

	public void actualizarTurnos(GC gc){
		if(thread!=null){
			if(thread.isAlive()){
				thread.ponFinalizar();
				
			}
		}
		thread = new ThreadsCuadrantes(this);
		
		thread.start();
	
	}
	
	public void finalizaThread(){
		thread.ponFinalizar();
	}
	
	public void dibujarCarga(GC gc,String texto){
		/*cambiarPincel(gc, 100, 200, 100);
		cambiarRelleno(gc, 100, 200, 100);
		gc.fillRectangle((ancho-margenIzq-margenDer)/2-130,
				(alto-margenInf-margenSup)/2-40,300,100);
		cambiarPincel(gc, 0, 0, 0);
		gc.drawRectangle((ancho-margenIzq-margenDer)/2-130,
				(alto-margenInf-margenSup)/2-40,300,100);
		Font fuente=gc.getFont();
		cambiarPincel(gc, 0, 0, 0);
		gc.setFont(new Font(display,"Times",10,SWT.BOLD));
		*/
		org.eclipse.swt.graphics.Image carga= this.dameVista().getImagenes().getCarga(this.repreAvance);
		//carga.getImageData().scaledTo(10, 100);
		//gc.drawImage(carga, 0, 0);
		int factor=ancho;
		if(alto<ancho){
			factor=alto;
		}
		float prod=((float)factor)/500;
		int x=(int)(ancho-margenIzq)/2-((int)(carga.getImageData().width*prod)/2);
		int y=(int)(alto-margenInf-margenSup)/2-((int)(carga.getImageData().height*prod)/2);

		gc.drawImage(carga,0,0,carga.getImageData().width, carga.getImageData().height,	x,y,
				((int)(carga.getImageData().width*prod)), ((int)(carga.getImageData().height*prod)));
		//----------------RELOJITO ANTERIOR....
		//, arg5, arg6, arg7, arg8)
		//org.eclipse.swt.graphics.Image carga = this.dameVista().getImagenes().getCarga();
//		Image cargaRed = carga.getScaledInstance(
//				(int)(d.getWidth()/2), (int)(d.getHeight()/2),
//				Image.SCALE_FAST); 
	//	gc.drawImage(carga, 0, 0);
	/*	for(int cont=0;cont<7;cont++){
			int ang_aux =(int)(((360+(360/8))/360)*cont);
			int yaux = (int)(10*Math.sin(ang_aux));
			if(cont==6){yaux-=3;}
			if(cont==0){yaux+=3;}
			int xaux = (int)(10*Math.cos(ang_aux));
			if(this.repreAvance<7){
				if((cont>(this.repreAvance))){
					cambiarPincel(gc, 0, 0, 0);
					cambiarRelleno(gc, 0, 0, 0);
				}
				else{
					cambiarPincel(gc, 249, 244, 153);
					cambiarRelleno(gc, 249, 244, 153);
				}
			}
			else{
				int aux=cont+7;
				if(aux<((this.repreAvance))){
					cambiarPincel(gc, 0, 0, 0);
					cambiarRelleno(gc, 0, 0, 0);
				}
				else{
					cambiarPincel(gc, 249, 244, 153);
					cambiarRelleno(gc, 249, 244, 153);
				}
			}
			
			gc.fillOval((ancho-margenIzq-margenDer)/2+xaux+20, (alto-margenInf-margenSup)/2+30+yaux, 5, 5);
			//gc.drawOval;
		}
		/*System.out.println("avance"+repreAvance);
		double angulo =(int)(((360+(360/8))/360)*repreAvance);
		int x= (int)(10*Math.cos(angulo));
		int y= (int)(10*Math.sin(angulo));
		gc.setLineWidth(3);
		if((repreAvance==6)||(repreAvance==13)){y-=3;}
		if((repreAvance==0)||(repreAvance==7)){y+=3;}
		cambiarPincel(gc, 249, 244, 153);
		cambiarRelleno(gc, 249, 244, 153);
		gc.fillOval((ancho-margenIzq-margenDer)/2+x+20, (alto-margenInf-margenSup)/2+30+y, 5, 5);*/
		//gc.drawOval((ancho-margenIzq-margenDer)/2+140+x, (alto-margenInf-margenSup)/2+5+y, 5, 5);
		/*cambiarPincel(gc, 0, 0, 0);
		gc.drawText(texto,(ancho-margenIzq-margenDer)/2-55, (alto-margenInf-margenSup)/2,true);
		gc.getFont().dispose();
		gc.setFont(fuente);*/
		
        
	}
	
	public void dibujaTurnosCargados(GC gc){	
		if(this.avance==-1){
			dibujarCarga(gc," CARGANDO BASE DATOS ");
		}
		else
		if(this.avance>1){
			dibujarCarga(gc," CARGANDO CUADRANTES ");
		}
		else{
			
		for(int cont=0;cont<7;cont++){
			if(horasInicio.size()>cont){
				Font fuente=gc.getFont();
				gc.setFont(new Font(display,"Verdana",tamano+5,SWT.BOLD|SWT.ITALIC));
				int tamano2= (int) ((-horasInicio.get(cont)+horaComienzoDescanso.get(cont))*10);
				if(tamano2>40){tamano2=40;}
				tamano2= (int) ((-horaFinDescanso.get(cont)+horasFin.get(cont))*10);
				if(tamano2>40){tamano2=40;}
				String horaUno = convertirString(horasInicio.get(cont),tamano2,1)+convertirString(horaComienzoDescanso.get(cont),tamano2,2);
				String horaDos = convertirString(horaFinDescanso.get(cont),tamano2,1)+convertirString(horasFin.get(cont),tamano2,2);
				String hora = horaUno + '\n'+"-----" +'\n'+'\n' +horaDos;
				dibujarTurno(gc,cont,this.horasInicio.get(cont),this.horaComienzoDescanso.get(cont),
						this.horaFinDescanso.get(cont),this.horasFin.get(cont),hora,tamano2);
				gc.getFont().dispose();
				gc.setFont(fuente);
			}
			
		}
	//	redibujar=false;
		
		}
	}
	
	public String convertirString(double hora, int tamano2,int posicion){

		Double Dhora = Math.floor(hora);
		double DMinutos= (hora-Dhora)*60;
		//DMinutos=DMinutos-Math.floor(DMinutos);
		int ihora= (int) (hora);
		int iminutos = (int)(DMinutos);
		String sminutos;
		String shora;
		if(iminutos<10){
			sminutos="0"+iminutos;
			}
		else{
			sminutos=""+iminutos;
			}
		if(ihora<10){
			shora="0"+ihora;
			}
		else{
			shora=""+ihora;
			}
		
		String devolver;
		if(tamano2>tamano+15){
			devolver= ihora+":"+sminutos+'\n';
		}
		else 
		if(posicion==1){
			devolver= shora+":"+sminutos+'-';
		}
		else{
			devolver= shora+":"+sminutos;
		}
		return devolver;
	}

	public void dibujarTurnos(GC gc){
		//GregorianCalendar calendario=new GregorianCalendar();
		if(fecha==null){ actualizarTurnos(gc);
		fecha=new Date(System.currentTimeMillis());}
		else{
			dibujaTurnosCargados(gc);
		}
	}
	
	
	public Vista dameVista(){
		return vista;
	}
	
	public void ponVista(Vista vista){
		this.vista=vista;
	}
	/**
	 * Cambia el color del pincel (foreground) sin exceder los l�mites de Color.
	 * Si se excede un l�mite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarRelleno(GC, int, int, int)
	 */
	private void cambiarPincel (GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setForeground(new Color(display,r, g, b));
	}
	/**
	 * Cambia el color del fondo (background) sin exceder los l�mites de Color.
	 * Si se excede un l�mite, se pone a 0 o 255, respectivamente.
	 * @param gc	El GC del que cambiar el color
	 * @param r		Valor del componente rojo
	 * @param g		Valor del componente verde
	 * @param b		Valor del componente azul
	 * @see #cambiarPincel(GC, int, int, int)
	 */
	private void cambiarRelleno(GC gc, int r, int g, int b) {
		// Controlar límites de colores
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;		
		gc.setBackground(new Color(display,r, g, b));
	}


	/**
	 * Actualiza el tama�o del cuadrante, el tama�o de las horas y las subdivisiones, y para cada
	 * franja, actualiza sus p�xeles inicial y final en funci�n de sus valores pinicio y pfin.
	 * @param ancho	El ancho nuevo, en p�xeles
	 * @param alto	El alto nuevo, en p�xeles
	 */
	public void setTamano(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaFin-horaInicio);
		Empleado e = empleado;
	}
	
	
	/**
	 * Metodo que dibuja un turno de trabajo de un empleado, esto es desde que entra hasta que sale
	 * sin interrupciones
	 * @param gc
	 * @param dia dia de la semana del que vamos a dibujar el turno
	 * @param horaComienzo hora del comienzo del turno
	 * @param horaFinal	hora del fin del turno
	 * @param float2 
	 * @param float1 
	 * @param Departamento	Nombre del departamento en el que va a trabajar
	 * @param tamano2 
	 */
	public void dibujarTurno(GC gc,int dia,float horaComienzo,float horaDescanso,float horaFinDescanso, float horaFinal, String Departamento, int tamano2){
		if (gc!=null) {
			int x_comienzo=convertirDia(dia);
			int y_comienzo=convertirHora(horaComienzo);
			int x_fin=convertirDia(dia+1);
			int y_fin=convertirHora(horaFinal);
			int x_com_des=convertirDia(dia);
			int y_com_des=convertirHora(horaDescanso);
			int x_fin_des=convertirDia(dia+1);
			int y_fin_des=convertirHora(horaFinDescanso);
			int m = margenIzq + margenNombres;
			m=margenIzq;
			int h = horaFin - horaInicio;
			h=7;
			cambiarPincel(gc, 100, 200, 100);
			cambiarRelleno(gc, 130, 200, 130);
			int x_comienzo_c=x_comienzo;
			int x_comienzo_t = (x_comienzo + x_fin)/2;
			int longitud = (int)((x_fin-x_comienzo_c));
			
			gc.fillGradientRectangle(x_comienzo_c,y_comienzo,longitud,y_fin-y_comienzo,true);
			this.cambiarPincel(gc, 50, 150, 100);
			this.cambiarRelleno(gc, 00, 150, 000);
			gc.fillGradientRectangle(x_com_des, y_com_des, longitud, y_fin_des-y_com_des, true);
			this.cambiarPincel(gc, 0, 0, 0);
			gc.setLineStyle(SWT.LINE_CUSTOM);
			gc.drawRoundRectangle(x_com_des,y_com_des,longitud,y_fin_des-y_com_des,8,8);

			gc.drawRoundRectangle(x_comienzo_c,y_comienzo,longitud,y_fin-y_comienzo,8,8);
			int sep=(ancho - m - margenDer)/h;
			float tamanox= sep/12;
			float tamanoy= tamanoFila/3;
			
			if (tamanox<tamanoy){
				tamano = (int)(tamanox);
				}
			else{
				tamano= (int)tamanoy;
			}
			
			Font fuente=gc.getFont();
			gc.setFont(new Font(display,"Verdana",tamano+tamano2/15,SWT.BOLD));
			//gc.drawText(String.valueOf((int)horaComienzo),x_comienzo, (y_comienzo), true);
			String text = Departamento;
	        Point textSize = gc.textExtent(text);
	        gc.drawText(Departamento,x_comienzo_t-textSize.x/2, (y_comienzo*2+y_fin)/3, true);
			String text2 = (String.valueOf((int)horaFinal));
	        Point textSize2 = gc.textExtent(text2);
			//gc.drawText((String.valueOf((int)horaFinal)),x_comienzo , y_fin-textSize2.y, true);
			gc.getFont().dispose();
			gc.setFont(fuente);
		}
	}
	
	public int convertirDia(int dia) {
		int x=0;
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep=(ancho - m - margenDer)/h;
		x=m+dia*sep;
		return x;

	}

	/**
	 * Metodo que convierte una hora determinada a una coordenada  y
	 */
	public int convertirHora(float hora) {
		
		Float hora_relativa = hora- horaInicio;
		int duracion = horaFin-horaInicio;
		int tamano=alto-margenInf-(tamanoFila+this.margenSup);
		int posicion_relativa = (int)((tamano / duracion)*hora_relativa);
		int posicion_absoluta =tamanoFila+this.margenSup+posicion_relativa;
		return posicion_absoluta;
	}
	
	public void dibujarDiaMes(GC gc,boolean activar,int dia,int semana,Time entrada, Time Fin, String texto, boolean izq, boolean der, boolean arr, boolean abajo,int num_filas,int num_columnas){
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep=(ancho - m - margenDer)/h;
		int sep2=(alto - margenInf - margenSup )/h;
		if((num_filas==1)&&(num_columnas==1)){
			gc.setLineStyle(SWT.LINE_SOLID);
			cambiarPincel(gc, 100,200,100);
			this.cambiarRelleno(gc, 100, 200, 100);
			gc.setLineWidth(2);
		//	if(!activado){
			if(activar){
				cambiarPincel(gc, 200,250,200);
				this.cambiarRelleno(gc, 200, 250, 200);

			}
			if(!activar){
			if(((texto==null)||(texto==""))&&(entrada==null)&&(Fin==null)){
				cambiarPincel(gc, 90,140,90);
				this.cambiarRelleno(gc, 90, 140, 90);	
			}
			else if(((entrada==null)||(Fin==null))){
				cambiarPincel(gc, 30,50,30);
				this.cambiarRelleno(gc, 30, 50, 30);
			}
			}
			
			gc.fillGradientRectangle(this.margenIzq+dia*sep, h+semana*sep2, sep, sep2,true);
			cambiarPincel(gc, 0,0,0);

			if(texto!=null){
				int tamanoFuente=0;
				if(!activar){
					if((entrada!=null)&&(Fin!=null)){
						tamanoFuente=sep/8;
						cambiarPincel(gc,0,150,0);
					}
					else{
					tamanoFuente=sep/5;
					cambiarPincel(gc,250,250,250);
					}
				}
				else{
					tamanoFuente=sep/3;
				}
				Font fuente=gc.getFont();
				gc.setFont(new Font(display,"Times",tamanoFuente,SWT.BOLD));
				String text = texto;
		        Point textSize = gc.textExtent(text);
		        gc.drawText(text,margenIzq+dia*sep+sep/2-textSize.x/2, h+semana*sep2+textSize.y/100,false);
		        cambiarPincel(gc,0,0,0);
				gc.getFont().dispose();
				gc.setFont(fuente);
			}

			if((entrada!=null)&&(Fin!=null)){
				Util util;
				String inicio =Util.aString(entrada.getHours())+":"+Util.aString(entrada.getMinutes());
				String fin =Util.aString(Fin.getHours())+":"+Util.aString(Fin.getMinutes());
				int tamanoFuente=sep/8;
				Font fuente=gc.getFont();
				gc.setFont(new Font(display,"Verdana",tamanoFuente,SWT.NORMAL));
				String text1 = inicio;
				String text2 = fin;
		        Point textSize = gc.textExtent(text1);
		        Point textSize2 = gc.textExtent(text2);
		        gc.drawText(text1,margenIzq+dia*sep+sep/2-textSize.x/2, h+semana*sep2+sep2-textSize2.y-textSize.y,false);
		        gc.drawText(text2,margenIzq+dia*sep+sep/2-textSize2.x/2, h+semana*sep2+sep2-textSize2.y,false);
				gc.getFont().dispose();
				gc.setFont(fuente);
			}
			else{
				gc.drawText("", margenIzq+dia*sep, h+semana*sep2+sep2/2,false);
			}

	/*	if(texto==null){*/
		}
		if((num_filas>1)||(num_columnas>1)){
			gc.setLineWidth(3);
			gc.drawRectangle(this.margenIzq+dia*sep, h+semana*sep2, sep*num_filas, sep2*num_columnas);
	//	gc.drawLine(this.margenIzq, convertirHora(hora), this.margenIzq+7*sep,convertirHora(hora));
	//	gc.setLineStyle(SWT.LINE_SOLID);
		}
		//}

		if(activar){
			gc.setLineWidth(3);
			gc.drawRectangle(this.margenIzq+dia*sep, h+semana*sep2, sep, sep2);
		}
		if((entrada!=null)||(Fin!=null)||((texto!="")&&(texto!=null))){
			gc.setLineWidth(1);
			gc.drawRectangle(this.margenIzq+dia*sep, h+semana*sep2, sep, sep2);
		}
	//	if(izq){gc.drawLine(this.margenIzq+dia*sep-sep, h+semana*sep2, this.margenIzq+dia*sep-sep, h+semana*sep2*2);}
	//	if(der){{gc.drawLine(this.margenIzq+dia*sep, h+semana*sep2, this.margenIzq+dia*sep, h+semana*sep2*2);}}
	//	if(arr){{gc.drawLine(this.margenIzq+dia*sep-sep, h+semana*sep2, this.margenIzq+dia*sep, h+semana*sep2);}}
	//	if(abajo){{gc.drawLine(this.margenIzq+dia*sep-sep, h+semana*sep2*2, this.margenIzq+dia*sep, h+semana*sep2*2);}}
		gc.setLineWidth(1);
	}
	/**
	 * Metodo que dibuja una linea horizontal a una hora dada
	 * @param gc
	 * @param hora hora a la que queremos dibujar la hora
	 */
	public void dibujarLineaHorizontal(GC gc,float hora){
		int m = margenIzq + margenNombres;
		m = margenIzq;
		int h = horaFin - horaInicio;
		h=7;
		int sep=(ancho - m - margenDer)/h;
		gc.setLineStyle(SWT.LINE_DOT);
		cambiarPincel(gc, 0,0,0);
		gc.setLineWidth(2);
		gc.drawLine(this.margenIzq, convertirHora(hora), this.margenIzq+7*sep,convertirHora(hora));
		gc.setLineStyle(SWT.LINE_SOLID);
	}

	public void ponRedibujar(boolean redibujar){
		this.redibujar=redibujar;
	}
	
	public boolean dameRedibujar(){
		return redibujar;
	}
	public void ponRedibujarSemana(boolean b) {
		// TODO Auto-generated method stub
		this.finSemana=b;
		
	}
	public boolean dameRedibujarSemana(){
		return finSemana;
	}}
