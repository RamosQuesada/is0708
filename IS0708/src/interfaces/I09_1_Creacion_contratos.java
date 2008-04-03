package interfaces;

/*******************************************************************************
 * INTERFAZ I-09.1 :: Creaci�n de un contrato
 *   por Daniel Dionne & Jose  Maria Martin
 *   
 * Interfaz para crear un nuevo contrato.
 * ver 1.0
 *******************************************************************************/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.ScrolledComposite;

import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.eclipse.swt.graphics.*;

import aplicacion.Vista;
import aplicacion.datos.Contrato;
import aplicacion.datos.Empleado;
import aplicacion.datos.Turno;
import aplicacion.utilidades.Util;

public class I09_1_Creacion_contratos {
	private Shell padre = null;

	private Shell shell = null;

	private ResourceBundle bundle;

	private Vista vista;

	private int modo;

	private int idContrato; // id del contrato a modificar o -1 en caso de nuevo
							// contrato

	private String patron;

	private List listaTurnosContrato;

	private ArrayList<Turno> turnos;

	private Contrato contratoInsertado;

	private Contrato contratoModificado;

	private ArrayList<Integer> idsTurnosInsertados;

	private ArrayList<Integer> idsTurnosEliminados;
	
	private ArrayList<Integer> idsTurnosAñadidos;

	private int turnoInicial;

	private final int longCicloDefault = 14;
	
	/**
	 * Constructor. Crea una nueva ventana para la creación/modificacion de un nuevo contrato segun indique el falg modo
	 * @param padre ventana desde la que se realiza la llamada
	 * @param bundle herramienta de idiomas
	 * @param vista vista de la aplicacion
	 * @param modo indica la funcionalidad que se le dara a la ventana,
	 * 	<i>0</i> para la creación de un nuevo contrato
	 * 	<i>otro</i> para la modificación de un contrato selecionado anteriormente en la ventana padre. 
	 * @param id identificador del contrato actual que mostrara en esta ventana
	 * 	<i>-1</i> esta ventana no contendra ningún valor, ya que es para la creacion de un nuevo contrato
	 * 	<i>otro</i> identifica el contrato que se mostrara en la ventana para modificar 
	 * @param cm contrato para mostrar en la ventana en caso de que <b>id</b> sea distinto de <i>-1</i>
	 */
	public I09_1_Creacion_contratos(Shell padre, ResourceBundle bundle,
			Vista vista, int modo, int id, Contrato cm) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;
		this.modo = modo;
		this.idContrato = id;
		if (modo == 0) {
			patron = "";
			turnoInicial = 0;
		} else {
			patron = cm.getPatron();
			turnoInicial = cm.getTurnoInicial();
		}

		contratoModificado = cm;
		// CAMBIAR
		if (idContrato != -1)
			turnos = vista.getControlador().getTurnosDeUnContrato(idContrato);
		else
			turnos = new ArrayList<Turno>();
		idsTurnosInsertados = new ArrayList<Integer>();
		idsTurnosEliminados = new ArrayList<Integer>();
		idsTurnosAñadidos = new ArrayList<Integer>();
		// contratos=vista.getControlador().getListaContratosDpto("DatosFijos");
		crearVentana();

	}

	/**
	 * Esta clase muestra una ventana con casillas para marcar los turnos.
	 * Dibuja tantas filas como turnos, y tantas columnas como días haya en el
	 * ciclo.
	 * 
	 * @author Daniel Dionne
	 * 
	 */
	private class CheckBoxes {
		private Button[][] cbs;

		private Shell shell;

		private Shell padre;

		public CheckBoxes(Shell padre, final int longCiclo, final int numTurnos) {
			this.padre = padre;
			shell = new Shell(SWT.APPLICATION_MODAL | SWT.CLOSE);
			shell.setLayout(new GridLayout(2, false));
			shell.setText(bundle.getString("I09_lab_ConfigurarCiclo"));
			// Permite cerrar la ventana pulsando ESC
			shell.addListener(SWT.Traverse, new Listener() {
				public void handleEvent(Event event) {
					switch (event.detail) {
					case SWT.TRAVERSE_ESCAPE:
						shell.close();
						event.detail = SWT.TRAVERSE_NONE;
						event.doit = false;
						break;
					}
				}
			});
			//Informacion sobre el turno inicial en uso
			final Composite cti = new Composite(shell, SWT.NONE);
			cti.setLayout(new GridLayout(1, false));
			final Label lTurnoIni = new Label(shell, SWT.CENTER);
			lTurnoIni.setText(bundle.getString("I09_info_TurnoInicial") + turnoInicial);
			lTurnoIni.setForeground(new Color(cti.getShell().getDisplay(), 255, 0, 0));
			// Este tipo de composite permite que la ventana pueda ser m�s
			// estrecha
			// que el contenido pero que se siga pudiendo ver todo
			final ScrolledComposite sc = new ScrolledComposite(shell,
					SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			// c1 contiene las casillas, y est� dentro del scroll
			final Composite c1 = new Composite(sc, SWT.NONE);
			// Label de ayuda
			final Label lInfo = new Label(shell, SWT.WRAP);
			// c2 tiene los botones Aceptar y Cancelar
			final Composite c2 = new Composite(shell, SWT.NONE);
			c1.setLayout(new GridLayout(longCiclo + 1, false));
			c2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			lInfo.setText(bundle.getString("I09_info_AyudaCiclos"));
			lInfo.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true,
					1, 2));
			lInfo.setSize(200, 200);
			c2.setLayout(new GridLayout(2, true));
			c2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			// Creaci�n de las casillas. Se guardan en una matriz para poder
			// recuperar
			// sus valores
			cbs = new Button[numTurnos][longCiclo];
			Label l;
			for (int i = -1; i < numTurnos; i++) {
				if (i == -1)
					l = new Label(c1, SWT.NONE);
				else {
					l = new Label(c1, SWT.NONE);
					l.setText(listaTurnosContrato.getItem(i));
				}
				for (int j = 1; j <= longCiclo; j++) {
					if (i == -1) {
						l = new Label(c1, SWT.NONE);
						l.setText(String.valueOf(j));
						if (j % 7 == 0)
							l.setForeground(new Color(c1.getShell()
									.getDisplay(), 255, 0, 0));
						l.setLayoutData(new GridData(SWT.CENTER, SWT.FILL,
								true, true, 1, 1));
					} else {
						cbs[i][j - 1] = new Button(c1, SWT.CHECK);
						cbs[i][j - 1].setLayoutData(new GridData(SWT.FILL,
								SWT.FILL, true, true, 1, 1));
					}
				}
			}

			c1.pack();
			sc.setContent(c1);
			final Button bAceptar = new Button(c2, SWT.PUSH);
			final Button bCancelar = new Button(c2, SWT.PUSH);
			bAceptar.setText(bundle.getString("Aceptar"));
			bCancelar.setText(bundle.getString("Cancelar"));
			bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
					1, 1));
			bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1));
			// Un listener con lo que hace el bot�n bCancelar
			SelectionAdapter sabCancelar = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();
				}
			};
			bCancelar.addSelectionListener(sabCancelar);

			// Un listener con lo que hace el bot�n bCancelar
			SelectionAdapter sabAceptar = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int nDiasSeguidos = 1;
					String turnos1 = "";
					String turnos2 = "";
					String aux = "";
					for (int j = 0; j < longCiclo; j++) {
						turnos2 = "";
						for (int i = 0; i < numTurnos; i++) {
							if (cbs[i][j].getSelection()) {
								if (turnos2 == "")
									turnos2 += turnos.get(i).getIdTurno();
								else
									turnos2 += "," + turnos.get(i).getIdTurno();
							}
						}
						if (turnos2 == "")
							turnos2 += "d";
						if (turnos1.equals(turnos2))
							nDiasSeguidos++;
						else {
							if (turnos1 != "")
								aux += nDiasSeguidos + ":" + turnos1 + "/";
							turnos1 = turnos2;
							nDiasSeguidos = 1;
						}
					}
					aux += nDiasSeguidos + ":" + turnos1 + "/";
					patron = aux.substring(0, aux.length() - 1);
					System.out.println(patron);
					shell.dispose();
				}
			};
			bAceptar.addSelectionListener(sabAceptar);

			// TODO La acci�n del bot�n Aceptar
			shell.setDefaultButton(bAceptar);
			shell.pack();

			// Evitar que la ventana se salga de la pantalla
			if (shell.getSize().x > shell.getDisplay().getClientArea().width - 100)
				shell.setSize(shell.getDisplay().getClientArea().width - 100,
						shell.getSize().y);

			// Mostrar centrada sobre el padre
			shell.setLocation(this.padre.getBounds().width / 2
					+ this.padre.getBounds().x - shell.getSize().x / 2,
					this.padre.getBounds().height / 2
							+ this.padre.getBounds().y - shell.getSize().y / 2);
			shell.open();
		}
	}

	/**
	 * Esta clase muestra una ventana con los contratos existentes para elegir
	 * uno nuevo y añadirlo al contrato.
	 * 
	 * @author Jose Maria Martin
	 * 
	 */
	private class ElegirTurno extends Thread{
		private Shell padre = null;

		private Shell shell = null;
		
		private Turno turnoElegido=null;
		
		private boolean datosInterfazCargados = false;
		
		private List listaTurnos;
		
		public Turno getTurnoElegido(){
			return turnoElegido;
		}
		
		/**
		 * Implementa un hilo que coge los turnos de la vista.
		 */
		public void run() {
			// boolean run = true;
			try {
				while (!vista.isCacheCargada()) {
					sleep(5000);
				}
			} catch (Exception e) {
			}			
			datosInterfazCargados = true;
			// while (run) {

			if (listaTurnos.isDisposed()) {
			} // run = false;
			else {
				if (!listaTurnos.isDisposed()) {
					// Actualizar tabla
					if (!listaTurnos.isDisposed()) {
						listaTurnos.getDisplay().asyncExec(new Runnable() {
							public void run() {
								mostrarTurnos();
							}
						});
					}
				}
				try {
					// TODO Espera 10 segundos (¿cómo lo dejamos?)
					sleep(10000);
				} catch (Exception e) {
				}
			}
		}
		
		private void mostrarTurnos() {
			if (vista.isCacheCargada() && datosInterfazCargados) {
				// listaTurnos.removeAll();
				for (int i = 0; i < vista.getTurnos().size(); i++)
					listaTurnos.add(vista.getTurnos().get(i).getIdTurno() + " " + vista.getTurnos().get(i).getDescripcion());
			}
		}

		public ElegirTurno(final Shell padre) {
			this.padre = padre;
			shell = new Shell(SWT.APPLICATION_MODAL | SWT.CLOSE);
			shell.setLayout(new GridLayout(2, false));
			shell.setText(bundle.getString("I09_Elegir_turno"));
			// Permite cerrar la ventana pulsando ESC
			shell.addListener(SWT.Traverse, new Listener() {
				public void handleEvent(Event event) {
					switch (event.detail) {
					case SWT.TRAVERSE_ESCAPE:
						shell.close();
						event.detail = SWT.TRAVERSE_NONE;
						event.doit = false;
						break;
					}
				}
			});
			final Label lElegir = new Label(shell, SWT.RIGHT);
			lElegir.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
					1, 1));
			lElegir.setText(bundle.getString("I09_lab_elegir"));
			//final List list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
			listaTurnos=new List(shell, SWT.BORDER | SWT.V_SCROLL);
			listaTurnos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
					4));
			start();
			//CAMBIAR esto por el array de turnos de la vista directamente????
			//pensar si hacer que herede d thread
			//final ArrayList<Turno> turnos = vista.getTurnos();
			/*for (int i = 0; i < turnos.size(); i++)
				listaTurnos.add(turnos.get(i).getIdTurno() + " "
						+ turnos.get(i).getDescripcion());*/
			final Composite grupo1 = new Composite(shell, SWT.NONE);
			grupo1.setLayout(new GridLayout(2, false));
			grupo1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
					1, 1));
			final Button bAceptar = new Button(grupo1, SWT.PUSH);
			final Button bCancelar = new Button(grupo1, SWT.PUSH);
			bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
					1, 1));
			bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1));
			bAceptar.setText(bundle.getString("Aceptar"));
			bCancelar.setText(bundle.getString("Cancelar"));
			// Listener para el bot�n bCancelar
			SelectionAdapter sabCancelar = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();
				}
			};
			bCancelar.addSelectionListener(sabCancelar);
			// Listener para el bot�n bAceptar
			SelectionAdapter sabAceptar = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (listaTurnos.getSelectionIndex() > -1) {
						turnoElegido=vista.getTurnos().get(listaTurnos.getSelectionIndex());
						System.out.println(turnoElegido.getIdTurno());
						shell.dispose();
					} else {
						MessageBox messageBox = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION
										| SWT.OK | SWT.CANCEL);
						messageBox.setMessage(bundle
								.getString("I09_lab_elegir"));
						messageBox.open();
					}
				}
			};
			bAceptar.addSelectionListener(sabAceptar);
			grupo1.pack();
			shell.setDefaultButton(bAceptar);
			shell.pack();
			// Mostrar centrada sobre el padre
			shell.setLocation(this.padre.getBounds().width / 2
					+ this.padre.getBounds().x - shell.getSize().x / 2,
					this.padre.getBounds().height / 2
							+ this.padre.getBounds().y - shell.getSize().y / 2);
			shell.open();
		}

		public Shell getShell() {
			return shell;
		}
	}

	/**
	 * Crea el contenido de la ventana
	 */
	public void crearVentana() {
		shell = new Shell(padre, SWT.CLOSE | SWT.APPLICATION_MODAL);
		if (modo == 0)
			shell.setText(bundle.getString("I09_lab_NuevoContrato"));
		else
			shell.setText(bundle.getString("I09_lab_ModifTurno"));
		shell.setLayout(new GridLayout(2, true));

		// Permite cerrar la ventana pulsando ESC
		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
					shell.close();
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = false;
					break;
				}
			}
		});

		final Group grupo1 = new Group(shell, SWT.NONE);
		final Group grupo2 = new Group(shell, SWT.NONE);
		final Group grupo3 = new Group(shell, SWT.NONE);

		grupo1.setText(bundle.getString("Contrato"));
		grupo2.setText(bundle.getString("I09_lab_Turnos"));
		grupo3.setText(bundle.getString("I09_lab_Ciclo"));

		// Grupo1 - Contrato
		grupo1.setLayout(new GridLayout(2, false));
		final Label lNombre = new Label(grupo1, SWT.LEFT);
		final Text tContrato = new Text(grupo1, SWT.BORDER);
		lNombre.setText(bundle.getString("I09_lab_NombreContrato"));
		lNombre.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true,
				1, 1));
		tContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		tContrato.setToolTipText(bundle.getString("I09_tip_NombreContrato"));
		if (modo == 0)
			tContrato.setText("");
		else
			tContrato.setText(contratoModificado.getNombreContrato());

		// Grupo 2 - Turnos
		grupo2.setLayout(new GridLayout(2, false));
		listaTurnosContrato = new List(grupo2, SWT.BORDER | SWT.V_SCROLL);
		if (idContrato != -1) {
			for (int i = 0; i < turnos.size(); i++) {
				listaTurnosContrato.add(turnos.get(i).getIdTurno() + " "
						+ turnos.get(i).getDescripcion());
			}
		}
		listaTurnosContrato.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 5));

		final Button bNuevoTurno = new Button(grupo2, SWT.PUSH);
		final Button bModificarTurno = new Button(grupo2, SWT.PUSH);
		final Button bEliminarTurno = new Button(grupo2, SWT.PUSH);
		final Button bElegirTurno = new Button(grupo2, SWT.PUSH);
		final Button bTurnoInicial = new Button(grupo2, SWT.PUSH);

		bNuevoTurno.setText(bundle.getString("I09_Nuevo_turno"));
		bModificarTurno.setText(bundle.getString("I09_Modif_turno"));
		bEliminarTurno.setText(bundle.getString("I09_Eliminar_turno"));
		bElegirTurno.setText(bundle.getString("I09_Elegir_turno"));
		bTurnoInicial.setText(bundle.getString("I09_turno_inicial"));
		bNuevoTurno.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1));
		bModificarTurno.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		bEliminarTurno.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		bElegirTurno.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		bTurnoInicial.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));

		if (turnos.isEmpty())
			bTurnoInicial.setEnabled(false);
		//bElegirTurno.setEnabled(false);

		// Grupo 3 - Ciclo
		grupo3.setLayout(new GridLayout(4, false));
		final Label lLongCiclo = new Label(grupo3, SWT.LEFT);
		final Text tLongCiclo = new Text(grupo3, SWT.BORDER);
		final Button bLCCambiar = new Button(grupo3, SWT.PUSH);
		final Button bAyuda = new Button(grupo3, SWT.PUSH);
		lLongCiclo.setText(bundle.getString("I09_lab_LongitudCiclo"));
		lLongCiclo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				true, 1, 1));
		tLongCiclo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1));
		tLongCiclo.setTextLimit(2);
		tLongCiclo.setToolTipText(bundle.getString("I09_tip_LongCiclo"));
		if (modo == 0)
			tLongCiclo.setText(String.valueOf(longCicloDefault));
		else
			tLongCiclo.setText(String.valueOf(contratoModificado
					.getDuracionCiclo()));
		bLCCambiar.setText(bundle.getString("I09_but_Configurar"));
		bLCCambiar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1));

		bAyuda.setText(bundle.getString("Ayuda"));
		bAyuda
				.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true, 1,
						1));
		// Salario y Tipo
		final Label lSalario = new Label(shell, SWT.LEFT);
		final Text tSalario = new Text(shell, SWT.BORDER);
		if (modo == 0)
			tSalario.setText(Integer.toString(0));
		else
			tSalario.setText(Double.toString(contratoModificado.getSalario()));

		final Label lTipo = new Label(shell, SWT.LEFT);
		final Text tTipo = new Text(shell, SWT.BORDER);
		if (modo == 0)
			tTipo.setText(Integer.toString(1));
		else
			tTipo.setText(Integer
					.toString(contratoModificado.getTipoContrato()));

		lSalario.setText(bundle.getString("I09_lab_salario"));
		lSalario.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true,
				1, 1));
		tSalario.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		tSalario.setToolTipText(bundle.getString("I09_tip_NombreTurno"));
		lTipo.setText(bundle.getString("I09_lab_tipo"));
		lTipo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				1));
		tTipo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tTipo.setToolTipText(bundle.getString("I09_tip_IdTurno"));
		tTipo.setTextLimit(1);

		// Parte inferior de la ventana
		final Button bAceptar = new Button(shell, SWT.PUSH);
		final Button bCancelar = new Button(shell, SWT.PUSH);

		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
				1, 1));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
				1, 1));

		grupo1
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
						1));
		grupo2
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
						1));
		grupo3
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
						1));

		bAceptar.setText(bundle.getString("Aceptar"));
		bCancelar.setText(bundle.getString("Cancelar"));
		bAceptar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true,
				1, 1));
		bCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true,
				1, 1));

		// Listener para el bot�n de configurar ciclo
		SelectionAdapter sabLCCambiar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					new CheckBoxes(shell,
							Integer.valueOf(tLongCiclo.getText()),
							listaTurnosContrato.getItemCount());
				} catch (Exception ex) {
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.OK | SWT.ICON_WARNING);
					messageBox.setText(bundle.getString("Error"));
					messageBox
							.setMessage(bundle.getString("I09_err_LongCiclo"));
					messageBox.open();
					tLongCiclo.setFocus();
					tLongCiclo.setSelection(0, 2);
				}
			}
		};
		bLCCambiar.addSelectionListener(sabLCCambiar);

		// Listener para el bot�n de nuevo turno
		SelectionAdapter sabNuevoTurno = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				I09_1_1_Creacion_turnos i09 = new I09_1_1_Creacion_turnos(
						shell, vista, bundle, 0, -1, idContrato, null);
				while (!i09.getShell().isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				Turno t = i09.getTurnoInsertado();
				if (t != null) {
					if (!bTurnoInicial.isEnabled())
						bTurnoInicial.setEnabled(true);
					idsTurnosInsertados.add(t.getIdTurno());
					//listaTurnosContrato.removeAll();
					turnos.add(t);
					/*for (int i = 0; i < turnos.size(); i++)
						listaTurnosContrato.add(turnos.get(i).getIdTurno()
								+ " " + turnos.get(i).getDescripcion());
					listaTurnosContrato.redraw();*/
					listaTurnosContrato.add(t.getIdTurno()+ " " + t.getDescripcion());
				}
			}
		};
		bNuevoTurno.addSelectionListener(sabNuevoTurno);

		// Listener para el bot�n de modificar turno
		SelectionAdapter sabModificarTurno = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox msgBox = new MessageBox(shell,
						SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.OK
								| SWT.CANCEL);
				msgBox.setMessage(bundle.getString("I09_aviso_inconsistencias"));
				msgBox.setText("Warning");
				int resp = msgBox.open();
				if (resp == SWT.OK) {
				if (listaTurnosContrato.getSelectionIndex() > -1) {
					int index = listaTurnosContrato.getSelectionIndex();
					int id = turnos.get(index).getIdTurno();
					I09_1_1_Creacion_turnos i091 = new I09_1_1_Creacion_turnos(
							shell, vista, bundle, 1, id, idContrato, turnos
									.get(index));
					while (!i091.getShell().isDisposed()) {
						if (!shell.getDisplay().readAndDispatch()) {
							shell.getDisplay().sleep();
						}
					}
					Turno taux = i091.getTurnoModificado();
					if (taux != null) {
						turnos.remove(index);
						listaTurnosContrato.remove(index);
						listaTurnosContrato.add(taux.getIdTurno() + " "
								+ taux.getDescripcion(), index);
						turnos.add(index, taux);
					}
				} else {
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION
									| SWT.OK | SWT.CANCEL);
					messageBox.setMessage(bundle
							.getString("I09_bot_modif_turno_no_select"));
					messageBox.open();
				}
				}
			}
		};
		bModificarTurno.addSelectionListener(sabModificarTurno);

		// Listener para el bot�n de eliminar turno
		SelectionAdapter sabEliminarTurno = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox msgBox = new MessageBox(shell,
						SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.OK
								| SWT.CANCEL);
				msgBox.setMessage(bundle.getString("I09_aviso_inconsistencias"));
				msgBox.setText("Warning");
				int resp = msgBox.open();
				if (resp == SWT.OK) {
				if (listaTurnosContrato.getSelectionIndex() > -1) {
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | SWT.OK
									| SWT.CANCEL);
					messageBox.setMessage(bundle
							.getString("I09_bot_elim_turno"));
					int response = messageBox.open();
					if (response == SWT.OK) {
						Turno t = turnos.get(listaTurnosContrato
								.getSelectionIndex());
						int idEliminado = t.getIdTurno();
						boolean cond = false;
						if (modo == 0)
							cond = idEliminado == turnoInicial;
						else
							cond = idEliminado == contratoModificado
									.getTurnoInicial();
						if (cond) {
							MessageBox messageBox2 = new MessageBox(shell,
									SWT.APPLICATION_MODAL | SWT.OK
											| SWT.ICON_WARNING);
							messageBox2.setText("Info");
							messageBox2.setMessage(bundle
									.getString("I09_warn_elim_Turno_ini"));
							messageBox2.open();
						} else {
							ArrayList<Contrato> auxContratos=vista.getListaContratosDepartamento();
							int apariciones=0;
							for (int i=0;i<auxContratos.size();i++){
								Contrato aux=auxContratos.get(i);
								if (aux.getNumeroContrato()!=idContrato){
									ArrayList<Turno> auxTurnos=vista.getControlador().getTurnosDeUnContrato(aux.getNumeroContrato());
									for(int j=0;j<auxTurnos.size();j++){
										if (idEliminado==auxTurnos.get(j).getIdTurno()) apariciones++;
									}									
								}
							}
							boolean okis;
							if (apariciones>0) okis=true;
							else okis = vista.eliminaTurno(t.getIdTurno());
							// CAMBIAR
							//boolean okis = vista.eliminaTurno(t.getIdTurno());
							//boolean okis = vista.getControlador().eliminaTurno(t);
							if (okis) {
								int index = listaTurnosContrato
										.getSelectionIndex();
								idsTurnosEliminados.add(idEliminado);
								turnos.remove(index);
								listaTurnosContrato.remove(index);
								MessageBox messageBox2 = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.OK
												| SWT.ICON_INFORMATION);
								messageBox2.setText("Info");
								messageBox2.setMessage(bundle
										.getString("I09_elim_Turno"));
								messageBox2.open();
							} else {
								MessageBox messageBox2 = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.OK
												| SWT.ICON_ERROR);
								messageBox2.setText(bundle.getString("Error"));
								messageBox2.setMessage(bundle
										.getString("I09_err_elim_Turno"));
								messageBox2.open();
							}
						}
					}
				} else {
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION
									| SWT.OK | SWT.CANCEL);
					messageBox.setMessage(bundle
							.getString("I09_bot_elim_turno_no_select"));
					messageBox.open();
				}
				}
			}
		};
		bEliminarTurno.addSelectionListener(sabEliminarTurno);
		// Listener para el bot�n de elegir turno
		SelectionAdapter sabElegirTurno = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ElegirTurno iet=new ElegirTurno(shell);
				while (!iet.getShell().isDisposed()) {
					if (!shell.getDisplay().readAndDispatch()) {
						shell.getDisplay().sleep();
					}
				}
				Turno tElegido=iet.getTurnoElegido();
				if (tElegido!=null){
					idsTurnosAñadidos.add(tElegido.getIdTurno());
					listaTurnosContrato.removeAll();
					turnos.add(tElegido);
					for (int i = 0; i < turnos.size(); i++)
						listaTurnosContrato.add(turnos.get(i).getIdTurno()
							+ " " + turnos.get(i).getDescripcion());
					listaTurnosContrato.redraw();
				}
				if (!bTurnoInicial.isEnabled())
					bTurnoInicial.setEnabled(true);
			}
		};
		bElegirTurno.addSelectionListener(sabElegirTurno);

		// Listener para el bot�n de turno inicial
		SelectionAdapter sabTurnoInicial = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (listaTurnosContrato.getSelectionIndex() > -1) {
					turnoInicial = turnos.get(
							listaTurnosContrato.getSelectionIndex())
							.getIdTurno();
					System.out.println(turnoInicial);
				} else {
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION
									| SWT.OK | SWT.CANCEL);
					messageBox.setMessage(bundle
							.getString("I09_bot_turno_ini_no_select"));
					messageBox.open();
				}
			}
		};
		bTurnoInicial.addSelectionListener(sabTurnoInicial);
		// TODO Listener para el bot�n bAceptar
		SelectionAdapter sabAceptar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// modo = 0 es nuevo turno y modo =1 es modificar
				String nombre = tContrato.getText();
				if (nombre.equals("") || (turnos.size() == 0) || (patron=="")) {
					MessageBox messageBox = new MessageBox(shell,
							SWT.APPLICATION_MODAL | SWT.OK
									| SWT.ICON_INFORMATION);
					messageBox.setText("Info");
					messageBox.setMessage(bundle
							.getString("I09_descrip_Contrato"));
					messageBox.open();
				} else {
					if (turnoInicial == 0) {
						MessageBox messageBox = new MessageBox(shell,
								SWT.APPLICATION_MODAL | SWT.OK
										| SWT.ICON_INFORMATION);
						messageBox.setText("Info");
						messageBox.setMessage(bundle
								.getString("I09_descrip_Turno_inicial"));
						messageBox.open();
					} else {
						if (Util.naturalCheck(tLongCiclo.getText())&&Util.doubleCheck(tSalario.getText())
								&&Util.naturalCheck(tTipo.getText())){
						int longCiclo = Integer.valueOf(tLongCiclo.getText());
						double sueldo = Double.valueOf(tSalario.getText());
						int tipo = Integer.valueOf(tTipo.getText());
						if (modo == 0) {
							contratoInsertado = new Contrato(nombre, 0,
									turnoInicial, longCiclo, patron, sueldo,
									tipo);
							// CAMBIAR
							int id = vista.insertContrato(contratoInsertado);
							contratoInsertado = new Contrato(nombre, id,
									turnoInicial, longCiclo, patron, sueldo,
									tipo);
							//vista.getListaContratosDepartamento().add(contratoInsertado);
							if (id != -1) {
								MessageBox messageBox = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.OK
												| SWT.ICON_INFORMATION);
								messageBox.setText("Info");
								messageBox.setMessage(bundle
										.getString("I09_insert_Contrato"));
								messageBox.open();
							} else {
								contratoInsertado = null;
								MessageBox messageBox = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.OK
												| SWT.ICON_ERROR);
								messageBox.setText(bundle.getString("Error"));
								messageBox.setMessage(bundle
										.getString("I09_err_insert_Contrato"));
								messageBox.open();
							}
							shell.dispose();
						} else {
							// CAMBIAR
							boolean okis = vista.modificarContrato(idContrato,
									turnoInicial, nombre, patron,
									longCiclo, sueldo, tipo);
//							boolean okis = vista.getControlador()
//									.modificarContrato(idContrato,
//											turnoInicial, nombre, patron,
//											longCiclo, sueldo, tipo);
							contratoModificado = new Contrato(nombre,
									idContrato, turnoInicial, longCiclo,
									patron, sueldo, tipo);
							for (int i = 0; i < idsTurnosEliminados.size(); i++) {
								int aux = idsTurnosEliminados.get(i);
								// CAMBIAR
								okis = okis
										&& vista.getControlador()
												.eliminaTurnoDeContrato(aux,
														idContrato);
							}
							if (okis) {
								MessageBox messageBox = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.OK
												| SWT.ICON_INFORMATION);
								messageBox.setText("Info");
								messageBox.setMessage(bundle
										.getString("I09_modif_Contrato"));
								messageBox.open();
							} else {
								contratoModificado = null;
								MessageBox messageBox = new MessageBox(shell,
										SWT.APPLICATION_MODAL | SWT.OK
												| SWT.ICON_ERROR);
								messageBox.setText(bundle.getString("Error"));
								messageBox.setMessage(bundle
										.getString("I09_err_modif_Contrato"));
								messageBox.open();
							}
							shell.dispose();
						}
					}
						else{
							MessageBox messageBox = new MessageBox(shell,
									SWT.APPLICATION_MODAL | SWT.OK
											| SWT.ICON_ERROR);
							messageBox.setText("Error");
							messageBox.setMessage(bundle
									.getString("I09_error_numerico"));
							messageBox.open();
							
						}
					}
					
				}
			}
		};
		bAceptar.addSelectionListener(sabAceptar);

		// Listener para el bot�n bCancelar
		SelectionAdapter sabCancelar = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				turnos.clear();
				for (int i = 0; i < idsTurnosInsertados.size(); i++) {
//					vista.getControlador().eliminaTurno(
//							idsTurnosInsertados.get(i));
					vista.eliminaTurno(idsTurnosInsertados.get(i));
				}
				shell.dispose();
			}
		};
		bCancelar.addSelectionListener(sabCancelar);

		shell.setDefaultButton(bAceptar);
		shell.pack();
		// Mostrar ventana centrada sobre el padre
		shell.setLocation(padre.getBounds().width / 2 + padre.getBounds().x
				- shell.getSize().x / 2, padre.getBounds().height / 2
				+ padre.getBounds().y - shell.getSize().y / 2);
		shell.open();
	}
	
	/**
	 * Devuelve la lista de los identificadores de turnos insertados para configurar el contrato
	 * @return <i>idsTurnosInsertados</i> que contiene la lista de los identificadores de los turnos. 
	 */
	public ArrayList<Integer> getTurnosInsertados() {
		return idsTurnosInsertados;
	}
	
	/**
	 * Devuelve la lista de los identificadores de turnos eliminados del contrato
	 * @return <i>idsTurnosEliminados</i> que contiene la lista de los identificadores de los turnos.
	 */
	public ArrayList<Integer> getTurnosEliminados() {
		return idsTurnosEliminados;
	}
	
	/**
	 * Devuelve la lista de los identificadores de turnos existentes añadidos al contrato
	 * @return <i>idsTurnosAñadidos</i> que contiene la lista de los identificadores de los turnos.
	 */
	public ArrayList<Integer> getTurnosAñadidos() {
		return idsTurnosAñadidos;
	}
	
	/**
	 * Devuelve el contrato creado
	 * @return <i>contratoInsertado</i> el nuevo contrato creado
	 */
	public Contrato getContratoInsertado() {
		return contratoInsertado;
	}
	
	/**
	 * Devuelve el contrato modificado
	 * @return <i>contratoModificado</i> el contrato con las modificaciones correspondientes
	 */
	public Contrato getContratoModificado() {
		return contratoModificado;
	}
	
	/**
	 * Devuelve la ventana 
	 * @return <i>shell</i> la ventana
	 */
	public Shell getShell() {
		return shell;
	}

}