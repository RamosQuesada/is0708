package interfaces;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import algoritmo.Trabaja;
import aplicacion.Empleado;
import aplicacion.Posicion;
import aplicacion.Turno;
import aplicacion.Util;
import aplicacion.Vista;
//De dónde coger javadoc: http://javashoplm.sun.com/ECom/docs/Welcome.jsp?StoreId=22&PartDetailId=jdk-6u3-oth-JPR&SiteId=JSC&TransactionId=noreg

/**
 * Esta clase extiende la clase cuadrante para que se pueda:
 * 1. dibujar sobre un GC
 * 2. representar y modificar sobre un canvas
 * @author Daniel Dionne
 *
 */

public class I_Cuadrante extends algoritmo.Cuadrante { // implements aplicacion.Drawable {
	private Display display;
	private int ancho, alto, margenIzq, margenDer, margenSup, margenInf; // Tamaño y márgenes del cuadrante
	private int margenNombres; // Un margen para pintar los nombres a la izquierda
	private int alto_franjas = 15;
	private int sep_vert_franjas = 10;
	private int horaApertura, horaCierre; // Definen de qué hora a qué hora es el cuadrante
	public int tamHora, tamSubdiv;
	public int numSubdivisiones; // Cuántas subdivisiones hacer por hora (0 = sin subdivisiones)
	private Vista vista;
	private Canvas canvas;
	private boolean cacheCargada = false;
	private I_Turno turno = null;
	
	// La variable terminadoDeCrear sirve para que una franja nueva no desaparezca al crearla
	private Boolean diario = true; // 1: muestra cuadrante diario, 0: muestra cuadrante mensual
	private int empleadoActivo = -1;
	private I_Turno turnoActivo  = null; // Este turno es para el interfaz de creación de turnos

	private Label lGridCuadrante;
	private Combo cGridCuadrante;
	private Label lCuadranteTitulo;
	
	private int dia=1;
	private int mes=1;
	private int anio=1990;
	private String departamento="DatosFijos";
	private Image fondo;
	
	private int diaActVistaMes=0;
	private int empActVistaMes=0;
	private boolean diaValido=false;
	private boolean turnoPulsado=false;
	private int turnPulsX=0;
	private int turnPulsY=0;
	
	private Button bPorMes;
	private Button bPorDia;
	
	private Turno turnoSeleccionado = null;
	private Empleado empleadoSeleccionado = null;
	private int altoFila=0;
	private int anchoDia=0;
	private int margenSupVistaMes;
	
	private int meses31[]=new int[7];
	private int meses30[]=new int[4];
	
	private Point cursor = new Point(0,0);
	
	private int movimiento;	
	
	private MouseListener mouseListenerCuadrDiario;
	private MouseMoveListener mouseMoveListenerCuadrDiario;
	private MouseListener mouseListenerCuadrMensual;
	private MouseMoveListener mouseMoveListenerCuadrMensual;
	
	public class Loader extends Thread {
		public void run() {
			if (turno==null) {
				try {
					while(!vista.isCacheCargada()) {
						sleep(50);
					}
					cargarCache();
					redibujar();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public class I_Trabaja {
		private Empleado empl;//id del empleado
		private Time FichIni;//Fichaje inicial,hay que mirar bien los tipos que van a llevar las fechas
		private Time FichFin;//Fichaje final
		private I_Turno turno;//Identificador del turno
		
		public I_Trabaja (Trabaja tr) {
			this.empl=vista.getEmpleado(tr.getIdEmpl());
			this.FichIni=tr.getFichIni();
			this.FichFin=tr.getFichFin();
			// Aquí hay que hacer una copia del turno
			this.turno = new I_Turno(vista.getTurno(tr.getIdTurno()));
					
		}

		public Empleado getEmpl() {
			return empl;
		}

		public void setIdEmpl(Empleado idEmpl) {
			empl = idEmpl;
		}

		public Time getFichIni() {
			return FichIni;
		}

		public void setFichIni(Time fichIni) {
			FichIni = fichIni;
		}

		public Time getFichFin() {
			return FichFin;
		}

		public void setFichFin(Time fichFin) {
			FichFin = fichFin;
		}

		public I_Turno getTurno() {
			return turno;
		}		

		public void setIdTurno(I_Turno idTurno) {
			this.turno = idTurno;
		}
		
		
	}

	protected ArrayList<I_Trabaja> iCuad[];		//Esta matriz seria la salida del algoritmo,un vector donde en cada posicion hay una lista de los empleados que trabajan
	
	/* TODO
	 * Las barras de tamaño cero se quedan
	 * bug: al hacer muchas franjas pequeñitas, no se pegan bien (ver si sigue pasando)
	 */

	/**
	 * Constructor del cuadrante.
	 * @param d				Display sobre el que se dibujará el cuadrante
	 * @param numSubdivisiones	Número de subdivisiones que se muestran en el cuadrante.  
	 * 						<ul>
	 * 						<li>12	(cada 5 min),
	 * 						<li>6	(cada 10 min),
	 * 						<li>4	(cada 15 min),
	 * 						<li>2	(cada 30 min),
	 * 						<li>1	(sin subdivisiones)
	 * 						</ul>
	 * @param horaApertura	Hora de inicio del cuadrante
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
	public I_Cuadrante(Vista vista, int mes, int anio, String idDepartamento, int subdivisiones, int horaInicio, int horaFin) {
		super(mes, anio, idDepartamento);
		this.vista = vista;
		Thread loader = new Loader();
		loader.start();
		setConfig(subdivisiones, horaInicio, horaFin);
		margenIzq = 15;
		margenDer = 20;
		margenSup = 1;
		margenInf = 10;
		margenNombres = 90;
		margenSupVistaMes=margenSup+30;
		
		//Inicializar los arrays del calculo de los domingos
		meses31[0]=0;
		meses31[1]=1;
		meses31[2]=3;
		meses31[3]=5;
		meses31[4]=7;
		meses31[5]=8;
		meses31[6]=10;
		
		meses30[0]=4;
		meses30[1]=6;
		meses30[2]=9;
		meses30[3]=11;
	}
	
	public void setMargenes(int margenIzq, int margenDer, int margenSup, int margenInf, int margenNombres) {
		this.margenIzq  = margenIzq;
		this.margenDer  = margenDer;
		this.margenSup  = margenSup;
		this.margenInf  = margenInf;
		this.margenNombres  = margenNombres;
		this.margenSupVistaMes=margenSup+30;
	}
	
	public void setConfig(int subdivisiones, int horaInicio, int horaFin) {
		this.horaApertura = horaInicio;
		this.horaCierre = horaFin;
		this.numSubdivisiones = subdivisiones;	
	}

	public void cargarCache() {
		if (vista.isCacheCargada() && turno==null) {
			vista.setProgreso("Cargando cuadrantes", 80);
			ArrayList<Trabaja> c[] = vista.getCuadrante(mes, anio, departamento).getCuad();
			vista.setProgreso("", 100);
			iCuad = new ArrayList[c.length];
			for (int i=0; i<c.length; i++) {
				iCuad[i] = new ArrayList<I_Trabaja>();
				for (int j=0; j<c[i].size(); j++) {
					iCuad[i].add(new I_Trabaja(c[i].get(j)));
				}
			}
			cacheCargada = true;
			if (!display.isDisposed()) {
				display.asyncExec(new Runnable() {
					public void run() {
						calcularTamano();
					}
				});
			}
		}
	}
	
	public void setCompositeUnTurno(Composite cCuadrante) {
		turno = new I_Turno(new Turno(0,"","12:00:00","19:00:00","13:00:00",60));
		margenNombres=30;
		setComposite(cCuadrante, null, null);
	}
	
	/**
	 * Devuelve el turno que se ha generado. 
	 * @return el turno generado
	 */
	public Turno getTurno() {
		return turno;
	}
	
	public void setTurno(Turno t) {
		turno = new I_Turno(t);
		redibujar();
	}
	
	/**
	 * Configura un composite para mostrar un cuadrante.
	 */
	public void setComposite(Composite cCuadrante, Button bPM, Button bPD) {
		bPorMes=bPM;
		bPorDia=bPD;
		cCuadrante.setLayout(new GridLayout(3,false));
		lCuadranteTitulo = new Label (cCuadrante, SWT.LEFT);
		String fname = lCuadranteTitulo.getFont().getFontData()[0].getName();
		lCuadranteTitulo.setFont(new Font(cCuadrante.getDisplay(),fname,15,0));

		lCuadranteTitulo.setText("");
		lCuadranteTitulo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		lGridCuadrante= new Label (cCuadrante, SWT.LEFT);
		lGridCuadrante.setText("Mostrar intervalos de");
		lGridCuadrante.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		cGridCuadrante = new Combo(cCuadrante, SWT.BORDER | SWT.READ_ONLY);
		cGridCuadrante.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		cGridCuadrante.setItems(new String[] {"5 min", "10 min", "15 min", "30 min", "1 hora"});
		cGridCuadrante.select(2);

		cGridCuadrante.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e){
				switch (cGridCuadrante.getSelectionIndex()) {
				case 0 : setNumSubdivisiones(12); calcularTamano(); break;
				case 1 : setNumSubdivisiones(6); calcularTamano(); break;
				case 2 : setNumSubdivisiones(4); calcularTamano(); break;
				case 3 : setNumSubdivisiones(2); calcularTamano(); break;
				case 4 : setNumSubdivisiones(1); calcularTamano(); 
				}
			}
		});

		// Preparar el canvas
		canvas = new Canvas(cCuadrante, SWT.FILL | SWT.NO_BACKGROUND);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		// Inicializar algunas variables
//		creando = false;
//		terminadoDeCrear = true;
		movimiento = 0;
		
		calcularTamano();
		display = canvas.getDisplay();
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				dibujarCuadrante(event.gc);
			}
		});
		canvas.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {}
			public void controlResized(ControlEvent e) {
				calcularTamano();
			}
		});
		canvas.addMouseTrackListener(new MouseTrackListener(){
			public void mouseEnter(MouseEvent arg0) {}

			public void mouseExit(MouseEvent arg0) {
				if (cacheCargada) {
					empleadoActivo=-1;
					for (int i = 0; i < iCuad[dia-1].size(); i++) {
						iCuad[dia-1].get(i).getTurno().desactivarFranjas();
					}
					canvas.redraw();
				}
			}

			public void mouseHover(MouseEvent arg0) {}
			
		});
		mouseMoveListenerCuadrDiario = new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (turno!=null || cacheCargada) {
				// Si acabo de apretar el botón para crear una franja, pero
				// todavía no he movido el ratón
			/*	if (creando && empleadoActivo != -1) {
					Posicion p = cuadrante.sticky(e.x);
					FranjaDib nuevaFranja = new FranjaDib(p, p);
					cuadrante.empleados.get(empleadoActivo).turno.franjas.add(nuevaFranja);
					activarFranja(cuadrante.empleados.get(empleadoActivo).turno.franjas.size() - 1, 3);
					creando = false;
					terminadoDeCrear = false;
				}*/

				// Si estoy moviendo una franja
					if (dameMovimiento() == 2) {
						movimiento = turnoActivo.moverFranja(e.x, margenIzq, margenNombres, horaApertura, horaCierre, tamHora, tamSubdiv, numSubdivisiones);
						canvas.redraw();
					}
				// Si estoy cambiando el inicio de una franja
				else if (dameMovimiento() == 1) {
					movimiento = turnoActivo.moverLadoIzquierdo(e.x, margenIzq, margenNombres, horaApertura, tamHora, tamSubdiv, numSubdivisiones);
					canvas.redraw();
				}
				// Si estoy cambiando el fin de una franja
				else if (dameMovimiento() == 3) {
					movimiento = turnoActivo.moverLadoDerecho(e.x, margenIzq, margenNombres, horaApertura, horaCierre, tamHora, tamSubdiv, numSubdivisiones);
					canvas.redraw();
				}
				// Si no estoy moviendo ninguna franja,
				// comprobar si el cursor está en alguna franja, una por una	
					else {
						// Comprueba el empleado activo (vertical)
						
						int i = 0;
						Boolean encontrado = false;
						Boolean redibujar = false;
						if (turno==null) {
							int empleadoActivoNuevo = -1;
							// Seleccionar empleado activo

							while (!encontrado && i < iCuad[dia-1].size()) { 
								if (iCuad[dia-1].get(i).turno.contienePunto(e.y, i,margenSup,sep_vert_franjas,alto_franjas))
									empleadoActivoNuevo = i;
								i++;
							}
							
							if (empleadoActivoNuevo != empleadoActivo) {
								empleadoActivo = empleadoActivoNuevo;
								redibujar = true;
							}

							// Comprueba la franja activa (horizontal)
							i = 0;
							encontrado = false;
						}
						if (turnoActivo != null) turnoActivo.desactivarFranjas();
						I_Turno t;
						if (turno==null) {
							while (!encontrado && i < iCuad[dia-1].size()) {
								t = iCuad[dia-1].get(i).getTurno();
								if (empleadoActivo==-1) { cursor(0); t.desactivarFranjas();}
								else if (i==empleadoActivo) {
									if 		(t.contienePixelInt(e.x))	{ cursor(1); encontrado = true; turnoActivo = t; redibujar=true;}
									else if (t.tocaLadoIzquierdo(e.x))	{ cursor(2); encontrado = true; turnoActivo = t; redibujar=true;}
									else if (t.tocaLadoDerecho(e.x))	{ cursor(2); encontrado = true; turnoActivo = t; redibujar=true;}
								}
								i++;
							}
						} else {
							if 		(turno.contienePixelInt(e.x))	{ cursor(1); encontrado = true; turnoActivo = turno; redibujar=true;}
							else if (turno.tocaLadoIzquierdo(e.x))	{ cursor(2); encontrado = true; turnoActivo = turno; redibujar=true;}
							else if (turno.tocaLadoDerecho(e.x))	{ cursor(2); encontrado = true; turnoActivo = turno; redibujar=true;}
						}
						if (!encontrado && turnoActivo!=null) { cursor(0); turnoActivo=null; redibujar=true; }
						if (redibujar) canvas.redraw();
					}
				}
			}
		};
		
		mouseListenerCuadrDiario = new MouseListener() {
			public void mouseDown(MouseEvent e) {
				// Botón derecho: Borra una franja o inserta un descanso 
				// (podría mostrar un menú si hace falta)
				if (turnoActivo!=null && e.button == 3) {
					turnoActivo.botonSecundario(e.x, margenIzq, margenNombres, horaApertura, tamHora, tamSubdiv, numSubdivisiones);
					canvas.redraw();
				} else
				if (turnoActivo!=null && e.button == 1) {
					movimiento = turnoActivo.botonPrimario(e.x, margenIzq, margenNombres, horaApertura, tamHora, tamSubdiv, numSubdivisiones);
				}
			}
			
			public void mouseUp(MouseEvent e) {
				movimiento=0;
				
			}
			public void mouseDoubleClick(MouseEvent e) {
			/*	int i = 0;
				FranjaDib f;
				Boolean encontrado = false;
				while (!encontrado
						&& i < cuadrante.empleados.get(empleadoActivo).turno.franjas
								.size()) {
					f = cuadrante.empleados.get(empleadoActivo).turno.franjas.get(i);
					if (f.contienePixelInt(e.x)) {
						f = cuadrante.empleados.get(empleadoActivo).turno.franjas
								.get(i);
						// TODO que calcule el sticky en el que est�
						// Franja f2 = cuadrante.new Franja (f.inicio, e.x-10);
						// f.inicio=e.x+10;
						// cuadrante.empleados.get(empleadoActivo).franjas.add(f2);
						redibujar();
						encontrado = true;
					}
					i++;
				}*/
			}
		};
		mouseListenerCuadrMensual = new MouseListener() {
			public void mouseDown(MouseEvent e){
				if (e.button == 1 &&(diaValido) && diaActVistaMes<iCuad.length && empActVistaMes<iCuad[diaActVistaMes].size()) {
					empleadoSeleccionado = iCuad[diaActVistaMes].get(empActVistaMes).getEmpl();
					turnoSeleccionado = iCuad[diaActVistaMes].get(empActVistaMes).getTurno();
					turnPulsX=diaActVistaMes;
					turnPulsY=empActVistaMes;
					turnoPulsado=true;
				}
			};
			
			public void mouseUp(MouseEvent e){
				if (e.button == 1 &&(diaValido) && diaActVistaMes<iCuad.length && empActVistaMes<iCuad[diaActVistaMes].size()) {
					Turno turnoSeleccionado2=iCuad[diaActVistaMes].get(empActVistaMes).getTurno();
					//Si los dos empleados tienen el mismo contrato
					if (turnoSeleccionado!=null && empleadoSeleccionado.getContratoId() == iCuad[diaActVistaMes].get(empActVistaMes).getEmpl().getContratoId()) {
						// Intercambiar turnos
						Turno.intercambiar(turnoSeleccionado, turnoSeleccionado2);
						calcularTamano();						
					}
				}
				turnoSeleccionado = null;
				turnoPulsado=false;
				canvas.redraw();
			};
			
			public void mouseDoubleClick(MouseEvent e){
				//Volver a la vista diaria con el dia seleccionado
				diario=true;
				setMovCuadSemanal(true);
				cursor(0);
				bPorMes.setSelection(false);
				bPorDia.setSelection(true);
				setDia(diaActVistaMes+1);
				//System.out.println("Dia "+diaActVistaMes);
				diaValido=false;
				turnoSeleccionado=null;
			};
		};
		mouseMoveListenerCuadrMensual = new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (cacheCargada) {
				//Comprueba si el cursor esta situado sobre algun dia
				cursor.x = e.x; cursor.y = e.y;
				diaValido = false;
				int dia = 0;
				Boolean diaEncontrado = false;
				int inicioX = margenNombres + margenIzq;
				int anchoMes = ancho - margenIzq - margenDer - margenNombres;
				int anchoDia = anchoMes / iCuad.length;
				int anchoDiaCont=0;
				while (!diaEncontrado && (dia < iCuad.length)) {
					// Calculamos la franja horizontal de cada dia
					if (e.x > (inicioX + anchoDiaCont + 1)
							&& e.x < (inicioX + anchoDiaCont + anchoDia - 1)) {
						diaActVistaMes = dia;
						diaEncontrado = true;
					}
					anchoDiaCont += anchoDia;
					dia++;
				}
				//Comprueba si el cursor esta situado sobre algun empleado
				int iEmp = 0;
				Boolean empEncontrado = false;
				ArrayList<Empleado> empleados = vista.getEmpleados();
				int altoFila = 20;
				int altoFilaCont = 0;
				int inicioY = margenSupVistaMes + altoFila;
				while (!empEncontrado && (iEmp < empleados.size())) {
					// Calculamos la franja vertical de cada empleado
					if (e.y > (inicioY + altoFilaCont + 1)
							&& e.y < (inicioY + altoFilaCont + altoFila - 1)) {
						empActVistaMes = iEmp;
						empEncontrado = true;
					}
					altoFilaCont += altoFila;
					iEmp++;
				}
				if (empEncontrado && diaEncontrado) {
					if (empTrabDia(diaActVistaMes, empleados.get(empActVistaMes).getEmplId())) {
						cursor(1);
						diaValido = true;
					} else {
						cursor(0);
						diaValido = false;
					}
				} else {
					cursor(0);
					diaValido = false;
				}
					
			canvas.redraw();
			}
			}
		};

	canvas.addMouseMoveListener(mouseMoveListenerCuadrDiario);
	canvas.addMouseListener(mouseListenerCuadrDiario);
	}

	private int dameMovimiento() {
		return movimiento;
	}

	private void dibujarCuadrante(GC gc) {
		// Doble buffering para evitar parpadeo
		if (ancho != 0 && alto != 0) {
			Image bufferImage = new Image(display, ancho, alto);
			GC gc2 = new GC(bufferImage);
			try {
				gc2.setAntialias(SWT.ON);
			}
			catch (SWTException ex){
				System.err.println(ex.code);
			}
			if (diario) dibujarCuadranteDia(display, gc2, empleadoActivo);
			else dibujarCuadranteMes(gc2);
			gc.drawImage(bufferImage, 0, 0);
			bufferImage.dispose();
		}
	}
	
	private void setNumSubdivisiones(int i) {
		numSubdivisiones = i;
		tamSubdiv = tamHora/numSubdivisiones;
		redibujar();
	}

	public void setDia(int dia){
		this.dia = dia;
		if (vista.isCacheCargada()) {
			cargarCache();
			redibujar();			
		}
	};

	public void setDia(int dia, int mes, int anio) {
		this.dia = dia;
		this.mes = mes;
		this.anio = anio;
		if (vista.isCacheCargada()) {
			cargarCache();
			redibujar();			
		}
	}
	
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
		if (vista.isCacheCargada()) {
			cargarCache();
			redibujar();
		}
	}
	
	/**
	 * Calcula el tamaño de las franjas cuando se redimensiona la ventana
	 */
	private void calcularTamano() {
		ancho = canvas.getClientArea().width;
		alto = canvas.getClientArea().height;
		setTamano(ancho, alto);
		if (turno!=null)
			turno.recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
		else if (cacheCargada && diario) {
			for (int i = 0; i < iCuad[dia-1].size(); i++) {
				if (iCuad[dia-1].get(i).getTurno()==null) 
					System.out.println("Turno nulo dia " + dia + " posicion " + i);
				iCuad[dia-1]
				.get(i)
				.getTurno()
				.recalcularFranjas(margenIzq, margenNombres, horaApertura, tamHora);
			}
		}
		fondo = null; // El fondo que hay ya no vale, hay que redibujarlo
		
	}
	
	public void redibujar() {
		// Redibuja sólo las franjas que corresponden, para evitar calculos
		// innecesarios
		// TODO ¿Merece la pena? Hay que ver si hay alguna diferencia en el rendimiento.
		// c.redraw(0, margenSup+(sep_vert_franjas+alto_franjas)*(posV+1),
		// ancho, 18, false);
		// c.redraw(0, 0, ancho, alto, false);
		if (!display.isDisposed()) {
			display.asyncExec(new Runnable() {
				public void run() {
					canvas.redraw();
					if (turno==null) lCuadranteTitulo.setText(String.valueOf(dia) + " de " + aplicacion.Util.mesAString(vista.getBundle(), mes-1) + " de " + String.valueOf(anio));
				}
			});
		}
	}
	
	/**
	 * Dibuja el cuadrante, resaltando el empleado activo.
	 * @param gc				El GC del display sobre el que se dibujará el cuadrante.
	 * @param empleadoActivo	La posición del empleado a resaltar en la lista de empleados.
	 */
	// TODO Debería lanzar una excepción si empleadoActivo > empleados.size
	public void dibujarCuadranteDia(Display d, GC gc, int empleadoActivo) {
		
		Long i = new Date().getTime();
		dibujarSeleccion(gc, empleadoActivo);
		Long j = new Date().getTime();
		actualizarFondo(gc);
		Long k = new Date().getTime();
		dibujarTurnos(gc);
		Long l = new Date().getTime();
		vista.infoDebug("I_Cuadrante", "Tiempos dibujo: \n" +
				String.valueOf(j-i) + " ms. dibujar seleccion\n" +
				String.valueOf(k-j) + " ms. dibujar horas\n" + 
				String.valueOf(l-k) + " ms. dibujar turnos\n");
	}
	
	public void dibujarTurnos(GC gc) {
		if (turno!=null) {			
			turno.dibujar(display, "", gc, 0, null, margenIzq, margenNombres,margenSup,sep_vert_franjas,alto_franjas,tamHora, tamSubdiv, horaApertura, numSubdivisiones);
		}
		else if (!cacheCargada) {
			gc.setForeground(new Color(display, 0,0,0));
			gc.drawText("Cargando\ndatos...", 5, 5);
		}
		else {
			for (int i = 0; i < iCuad[dia-1].size(); i++) {
				// Dibujar el nombre del empleado y el turno
				String nombre = iCuad[dia-1].get(i).getEmpl().getNombre().charAt(0) + ". " + iCuad[dia-1].get(i).getEmpl().getApellido1();
				iCuad[dia-1].
				get(i).
				getTurno().
				dibujar(display, nombre, gc, i, vista.getEmpleados().get(i).dameColor() ,margenIzq, margenNombres,margenSup,sep_vert_franjas,alto_franjas,tamHora, tamSubdiv, horaApertura, numSubdivisiones);
			}
		}
	}
	
	public void dibujarCuadranteMes(GC gc) {
		if (fondo==null)
			actualizarFondo(gc);
		else gc.drawImage(fondo, 0, 0);
		if (diaValido && diaActVistaMes<iCuad.length && empActVistaMes<iCuad[diaActVistaMes].size()){
			int alto=39;
			//Sacamos la informacion del turno
			int idTurno=iCuad[diaActVistaMes].get(empActVistaMes).getTurno().getIdTurno();
			String idTurnoS=("Id. Turno: "+String.valueOf(idTurno));
			String descTurno=iCuad[diaActVistaMes].get(empActVistaMes).getTurno().getDescripcion();
			int ancho=Math.max(gc.textExtent(descTurno).x+15,gc.textExtent(idTurnoS).x+15);
			//Información del color
			Color color = new Color(display,120,170,120);
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			//Pintamos segun turno
			int inicioX=margenIzq+margenNombres+10-(ancho/2);
			int inicioY=margenSupVistaMes+altoFila;
			Util.cambiarRelleno(display, gc, r-50,g-50,b-50);
			gc.fillRectangle(inicioX+((diaActVistaMes)*anchoDia),
					inicioY+((empActVistaMes)*altoFila)-alto,ancho,alto);
			Util.cambiarRelleno(display, gc, r,g,b);
			Util.cambiarPincel(display, gc, r-100,g-100,b-100);
			gc.fillRectangle(inicioX+((diaActVistaMes)*anchoDia)-2,
					inicioY+((empActVistaMes)*altoFila)-(alto+2),ancho-2,alto-2);
			gc.drawRectangle(inicioX+((diaActVistaMes)*anchoDia)-2,
					inicioY+((empActVistaMes)*altoFila)-(alto+2),ancho-2,alto-2);
			gc.drawText("Id. Turno: "+String.valueOf(idTurno),
					inicioX+((diaActVistaMes)*anchoDia)+2,
					inicioY+((empActVistaMes)*altoFila)-alto+2,altoFila);
			gc.drawText(descTurno,
					inicioX+((diaActVistaMes)*anchoDia)+2,
					inicioY+((empActVistaMes)*altoFila)-(alto-(altoFila)),altoFila);
			
			//Util.darBrillo(display, gc, r, g, b,30);
			dibujaBotonPuls(gc,margenIzq+margenNombres+((diaActVistaMes)*anchoDia),
					inicioY+((empActVistaMes)*altoFila),anchoDia,altoFila);
		}
		if (turnoSeleccionado!=null) {
			gc.setBackground(new Color(display,120,170,120));
			//gc.drawRectangle(cursor.x-anchoDia/2, cursor.y-altoFila/2, anchoDia, altoFila);
			gc.fillRectangle(cursor.x-anchoDia/2, cursor.y-altoFila/2, anchoDia, altoFila);
			if (anchoDia>14)
				gc.drawText(String.valueOf(turnoSeleccionado.getIdTurno()),cursor.x-anchoDia/2 + (7/2),cursor.y - altoFila/2 + 2,altoFila);
			dibujaBoton(gc,cursor.x-anchoDia/2,cursor.y-altoFila/2,anchoDia,altoFila);
			gc.setBackground(new Color(display,255,255,255));
		}
	}
	
	/**
	 * Dibuja lineas verticales representando las horas y las subdivisiones del cuadrante.
	 * @param gc	El GC del display sobre el que se dibujará el cuadrante.
	 */
	private void actualizarFondo(GC gc) {
		int m = margenIzq + margenNombres;
		int h = horaCierre - horaApertura;
		int sep = (ancho - m - margenDer)/h;
		int subsep = sep/numSubdivisiones;
		if (fondo == null && diario) {
			fondo = new Image(display,ancho,alto);
			GC gcFondo = new GC(fondo);
			for (int i=0; i<=h; i++) {
				gcFondo.setLineStyle(SWT.LINE_SOLID);
				gcFondo.setForeground(new Color(display,40,80,40));
				if (sep>14 && sep<=20) gcFondo.drawText(String.valueOf((horaApertura+i)%24),     m+i*sep-5, margenSup, true);
				else if (sep>20)     gcFondo.drawText(String.valueOf((horaApertura+i)%24)+'h', m+i*sep-5, margenSup, true);
				gcFondo.drawLine(m+i*sep, 20+margenSup, m+i*sep, alto-margenInf);
				gcFondo.setForeground(new Color(display, 120,170,120));
				gcFondo.setLineStyle(SWT.LINE_DOT);
				if (i!=h)
					for (int j=1; j<numSubdivisiones; j++) {
						gcFondo.drawLine(m+i*sep+j*subsep, 20+margenSup+5, m+i*sep+j*subsep, alto-margenInf-5);
					}
			}
			gcFondo.setLineStyle(SWT.LINE_SOLID);
		} else if (fondo==null && !diario) {
			if (cacheCargada){
				fondo = new Image(display,ancho,alto);
				GC gcFondo = new GC(fondo);						
				Calendar c = Calendar.getInstance();
				// Esto coge el primer domingo
				boolean domingoEncontrado = false;
				int dom = 1;
				c.set(anio,mes,1);
				//System.out.println(c.get(Calendar.YEAR));
				while (!domingoEncontrado) {
					//System.out.println(c.get(Calendar.DAY_OF_WEEK));
					//System.out.println(c.get(Calendar.MONTH));
					//Dependiendo del mes en el que se encuentre el domingo tendra un valor u otro
					if (c.get(Calendar.DAY_OF_WEEK)==4 && esMes31(c.get(Calendar.MONTH))) domingoEncontrado=true;
					else if ((c.get(Calendar.DAY_OF_WEEK)==3 && esMes30(c.get(Calendar.MONTH)))) domingoEncontrado=true;
					else if ((c.get(Calendar.DAY_OF_WEEK)==2 && c.get(Calendar.MONTH)==2 && c.get(Calendar.YEAR)%4==0)) domingoEncontrado=true;
					else if ((c.get(Calendar.DAY_OF_WEEK)==1 && c.get(Calendar.MONTH)==2 && c.get(Calendar.YEAR)%4!=0)) domingoEncontrado=true;
					else {
						dom++;
						c.roll(Calendar.DATE, true);
						//c.set(anio,mes,dom);
					}
					//System.out.println("DomingoPrueba: "+c.get(Calendar.));
				}
				//System.out.println(c.get(Calendar.DAY_OF_WEEK));
				//System.out.println(c.toString());
				int anchoMes = ancho - margenIzq - margenDer - margenNombres;
				anchoDia = anchoMes/iCuad.length;
				altoFila = 20;
				// Dibujar números de los días
				if (anchoDia>14)
					for (int j=0; j < iCuad.length; j++) {
						if (((j+1)-dom)%7==0) gcFondo.setForeground(new Color(display,255,0,0));
						else if (((j+1)-dom)%7==1) gcFondo.setForeground(new Color(display,0,0,0));
						gcFondo.drawText(String.valueOf(j+1), margenIzq + margenNombres + j*anchoDia + anchoDia/2, margenSupVistaMes);
						gcFondo.setForeground(new Color(display,0,0,0));
					}
				
				ArrayList<Empleado> empleados=vista.getEmpleados();
				for (int i=0; i < empleados.size(); i++) {
					aplicacion.Empleado e=empleados.get(i);
					gcFondo.drawText(e.getNombre(), margenIzq, margenSupVistaMes + 20 + i*altoFila);
					for (int j=0; j < iCuad.length; j++) {
						//Despues calculamos el turno a visualizar
						Boolean encontrado=false;
						dibujaCasilla(gcFondo, i, j, -1, new Color(display,120,170,120));
						int k=0;
						while (!encontrado && k<iCuad[j].size()) {
							if (iCuad[j].get(k).getEmpl().getEmplId()==e.getEmplId()) {	
								dibujaCasilla(gcFondo, i, j, k, new Color(display,120,170,120));
								encontrado=true;
							}
							k++;
						}
					}
				}
			}
		}
		if (fondo!=null)
		gc.drawImage(fondo,0,0);
	}
	
	public void dibujaCasilla(GC gc, int i, int j, int empl, Color color){
		//Primero se pinta el rectangulo
		gc.setForeground(new Color(display,0,0,0));
		gc.drawRectangle(margenIzq + margenNombres + j*anchoDia, margenSupVistaMes + 20 + i*altoFila, anchoDia, altoFila);
		gc.setForeground(new Color(display,85,135,85));
		if (empl!=-1) {
			gc.setBackground(color);
			gc.fillRectangle(margenIzq + margenNombres + j*anchoDia+1, margenSupVistaMes + 20 + i*altoFila+1, anchoDia-1, altoFila-1);
			gc.setForeground(new Color(display,0,0,0));
			if (anchoDia>14)
				//gc.drawText(String.valueOf(iCuad[j].get(k).getTurno().getAbreviatura().charAt(0)),margenIzq + margenNombres + j*anchoDia + (7/2), margenSup + 20 + i*altoFila + 2,altoFila);
				gc.drawText(String.valueOf(iCuad[j].get(empl).getTurno().getIdTurno()),margenIzq + margenNombres + j*anchoDia + (7/2),margenSupVistaMes + 20 + i*altoFila + 2,altoFila);
			gc.setBackground(new Color(display,255,255,255));
		}
		gc.setForeground(new Color(display,0,0,0));
	}
	
	public void dibujaBoton(GC gc,int iX,int iY,int ancho,int alto) {
		//Lineas grises
		gc.setForeground(new Color(display,210,210,210));
		//Vertical
		gc.drawLine(iX,iY,iX,iY+alto);
		//Horizontal
		gc.drawLine(iX,iY,iX+ancho,iY);
		//Lineas negras
		gc.setForeground(new Color(display,50,50,50));
		//Vertical
		gc.drawLine(iX+ancho,iY,iX+ancho,iY+alto);
		//Horizontal
		gc.drawLine(iX+1,iY+alto,iX+ancho-1,iY+alto);
	}
	
	public void dibujaBotonPuls(GC gc,int iX,int iY,int ancho,int alto) {
		//Lineas grises
		gc.setForeground(new Color(display,50,50,50));
		//Vertical
		gc.drawLine(iX,iY,iX,iY+alto);
		//Horizontal
		gc.drawLine(iX,iY,iX+ancho,iY);
		//Lineas negras
		gc.setForeground(new Color(display,210,210,210));
		//Vertical
		gc.drawLine(iX+ancho,iY,iX+ancho,iY+alto);
		//Horizontal
		gc.drawLine(iX,iY+alto,iX+ancho,iY+alto);
		gc.setForeground(new Color(display,0,0,0));
	}
	
	/**
	 * Dibuja un fondo distinguido para el empleado seleccionado, basado en el color del empleado
	 * pero más pálido.
	 * @param gc	El GC sobre el que resaltar el empleado
	 * @param emp	La posición del empleado a resaltar en la lista de empleados. Se considera
	 * 				que -1 significa que no hay ningún empleado seleccionado.
	 */
	// TODO Lanzar excepción si emp > empleados.size
	private void dibujarSeleccion (GC gc, int emp) {
		if (emp!=-1) {
			Color color = new Color(display,220,240,220);
			if (vista.getEmpleados().get(emp).dameColor()!=null)
				color = new Color(display, 255-(255-vista.getEmpleados().get(emp).dameColor().getRed())/5, 255-(255-vista.getEmpleados().get(emp).dameColor().getGreen())/5, 255-(255-vista.getEmpleados().get(emp).dameColor().getBlue())/5);
			gc.setBackground(color);
			gc.fillRectangle(Math.max(margenIzq-2,0),margenSup+(sep_vert_franjas+alto_franjas)*(emp+1)-5,ancho-margenIzq-margenDer,alto_franjas+11);
		}
	}
	/**
	 * Pega el valor x al más cercano dentro de la rejilla. El tamaño de la rejilla está determinado
	 * por el número de subdivisiones.
	 * @param x		El valor a ajustar
	 * @return		El valor ajustado a la rejilla
	 */
	public Posicion sticky (int x) {
		int y = x - margenNombres - margenIzq + (tamHora/numSubdivisiones)/2;
		Posicion p;
		if (((y%tamHora)/(tamHora/numSubdivisiones))>=numSubdivisiones)
			// Para evitar resultados del tipo 14:60
			p = new Posicion(1+y/tamHora+horaApertura,0);
		else
			// En otro caso, hay que tener en cuenta cómo se dibuja el cuadrante para evitar
			// desfases entre las lineas horarias y las franjas.
			p = new Posicion(y/tamHora+horaApertura,((y%tamHora)/(tamHora/numSubdivisiones))*12/numSubdivisiones);
		return p;
	}
	/**
	 * Actualiza el tamaño del cuadrante, el tamaño de las horas y las subdivisiones, y para cada
	 * franja, actualiza sus píxeles inicial y final en función de sus valores pinicio y pfin.
	 * @param ancho	El ancho nuevo, en píxeles
	 * @param alto	El alto nuevo, en píxeles
	 */
	public void setTamano(int ancho, int alto) {
		this.alto = alto;
		this.ancho = ancho;
		tamHora = (ancho - margenIzq-margenDer-margenNombres)/(horaCierre-horaApertura);
		tamSubdiv = tamHora/numSubdivisiones;
	}
	
	private void cursor(int i) {
		switch (i) {
		case 1:
			canvas.setCursor(new Cursor(canvas.getDisplay(), SWT.CURSOR_HAND));
			break;
		case 2:
			canvas.setCursor(new Cursor(canvas.getDisplay(), SWT.CURSOR_SIZEE));
			break;
		default:
			canvas.setCursor(new Cursor(canvas.getDisplay(), SWT.CURSOR_ARROW));
			break;
		}

	}
/*
	public ImageData getDrawableImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageData getPrintableImage(Display display, ResourceBundle bundle,
			boolean bn) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
	
	/**
	 * Método que establece el atributo del cuadrante a diario o mensual
	 * 
	 * @param d
	 * 		booleano con el valor a establecer
	 */
	public void setDiario(boolean d) {
		diario=d;
		fondo = null;
		redibujar();
	}
	
	/**
	 * Método que activa/desactiva las acciones del raton sobre los cuadrantes diarios
	 * 
	 * @param b
	 * 		true activa, false desactiva
	 */
	public void setMovCuadSemanal(boolean b) {
		if (b) {
			canvas.removeMouseMoveListener(mouseMoveListenerCuadrMensual);
			canvas.removeMouseListener(mouseListenerCuadrMensual);
			canvas.addMouseMoveListener(mouseMoveListenerCuadrDiario);
			canvas.addMouseListener(mouseListenerCuadrDiario);
			lCuadranteTitulo.setVisible(true);
			lGridCuadrante.setVisible(true);
			cGridCuadrante.setVisible(true);
		}
		else {
			canvas.removeMouseMoveListener(mouseMoveListenerCuadrDiario);
			canvas.removeMouseListener(mouseListenerCuadrDiario);
			canvas.addMouseMoveListener(mouseMoveListenerCuadrMensual);
			canvas.addMouseListener(mouseListenerCuadrMensual);
			//Ocultamos el combo de los intervalos
			lCuadranteTitulo.setVisible(false);
			lGridCuadrante.setVisible(false);
			cGridCuadrante.setVisible(false);
		}
	}
	
	/**
	 * Método que 
	 * 
	 * @param dia
	 * 		dia en el que hay que buscar dentro de iCuad
	 * @param idEmpl
	 * 		empleado del que se desea saber si trabaja ese dia
	 */
	public boolean empTrabDia(int dia,int idEmpl){
		Boolean encontrado=false;
		int i=0;
		while (!encontrado && i<iCuad[dia].size()){
			if (iCuad[dia].get(i).getEmpl().getEmplId()==idEmpl)
				encontrado=true;
			i++;
		}
		return encontrado;
	}
	
	public boolean esMes31(int mes){
		boolean encontrado=false;
		int i=0;
		while (!encontrado && i<7){
			if (mes==meses31[i]) encontrado=true;
			else i++;
		}
		return encontrado;
	}
	
	public boolean esMes30(int mes){
		boolean encontrado=false;
		int i=0;
		while (!encontrado && i<4){
			if (mes==meses30[i]) encontrado=true;
			else i++;
		}
		return encontrado;
	}
}
