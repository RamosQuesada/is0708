package interfaces;

/*******************************************************************************
 * INTERFACE :: ManageDepartment
 *   by Aneta Mironska
 *   
 * Management (adding and configurate) department.
 * ver 1.0
 *******************************************************************************/
 /**draws window for to add/edid department*/


import java.util.ArrayList;
import java.util.ResourceBundle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import aplicacion.Database;
import aplicacion.Departamento;
import aplicacion.Empleado;
import aplicacion.Vista;
public class I10_Config_departamento {
	private Database db;
	private Shell padre = null;
	private Vista vista=null;
	private Shell shellWindow =null;
	private String[] arrayDB = {"","",""};
	
	//List of Atributes of Departament in DB 
	private Label labName = null;
	private Label labNumber = null;
	private Label labBoss = null;
	
	private Button butNewBoss = null;
	
	private Button bAccept = null;
	private Button bCancel = null;
	
	private Text tName = null;
	private Text tNumber = null;
	private Combo comboBoss = null;

	private ResourceBundle bundle;

	//private aplicacion.Turno turno;
	
	private Image iconDep;
	
	/**constructor for editing department*/
	public I10_Config_departamento(Shell padre, ResourceBundle bundle,
			String windowName, String[] newArrayDB, Vista vista,
			Database db) {
		this.padre = padre;
		this.bundle = bundle;
		this.db = db;
		this.vista=vista;
		//String[] newArrayLabel = {bundle.getString("Nombre"), bundle.getString("I10_dep_num"), bundle.getString("I10_dep_jefe")};
		
		arrayDB = newArrayDB;
		createWindow(windowName);
	}
	
	/**constructor for new department*/
	public I10_Config_departamento(Shell padre, ResourceBundle bundle,
			String windowName,Vista vista) {
		this.padre = padre;
		this.bundle = bundle;
		this.vista = vista;

		//String[] newArrayLabel = {bundle.getString("Nombre"), bundle.getString("I10_dep_num"), bundle.getString("I10_dep_jefe")};

		createWindow(windowName);
	}

	/**create window for department*/
	private void createWindow(String windowName) {
		//final Shell shellWindow = new Shell(padre, SWT.CLOSE | SWT.RESIZE
		shellWindow = new Shell(padre, SWT.CLOSE | SWT.CLOSE
				| SWT.APPLICATION_MODAL);
		shellWindow.setText(windowName);
		shellWindow.setLayout(new GridLayout(2,true));
		
		closeOnECS(shellWindow);
		
		//adding icon to window
		iconDep = new Image(padre.getDisplay(), Vista.class.getResourceAsStream("ico_chicos.gif"));
		shellWindow.setImage(iconDep);

		//add Components into Window
		this.addComponents();
		
		//Adding listeners to Buttons	
		// on action "Cancel"
		SelectionAdapter onCancel = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					shellWindow.dispose();	
			}
		};

		// on action "Accept"
		SelectionAdapter onAccept = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(integerCheck(tNumber.getText())==true){
/*TODO BD reference to a class which connects DB and takes the values of 
  labels texts and combo (labName, labNumber ,labBoss, tName, tNumber, comboBoss ) and sends it to DB
  DB check if this department exists if not add new, if yes edid with new datas*/
					shellWindow.dispose();
				}else{
					//show message for user
					MessageBox messageBox = new MessageBox (padre, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.ICON_INFORMATION);
					messageBox.setText (bundle.getString("Mensaje"));
					messageBox.setMessage (bundle.getString("I10_int_check_mess"));
					e.doit = messageBox.open () == SWT.CLOSE;
					
					System.out.println("Non-integer value in Number field: "+tNumber.getText());
				}		
			}
		};
		
		// on action "New Boss"
		SelectionAdapter onNewBoss = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				I08_1_Anadir_empleado I08_1_instance = new I08_1_Anadir_empleado(padre, bundle, vista);
			}
		};
		
		bCancel.addSelectionListener(onCancel);
		bAccept.addSelectionListener(onAccept);
		butNewBoss.addSelectionListener(onNewBoss);

		//default button is "Accept"
		shellWindow.setDefaultButton(bAccept);
//*************************************************************************************************************		
		shellWindow.pack();
		shellWindow.setSize(new Point(350, shellWindow.getSize().y));
		shellWindow.setLocation(padre.getBounds().width / 2 + padre.getBounds().x
				- shellWindow.getSize().x / 2, padre.getBounds().height / 2
				+ padre.getBounds().y - shellWindow.getSize().y / 2);
		shellWindow.open();

	}
	/**add components into window*/
	public void addComponents(){
//		******Adding Components into Window*********************************************************************
		final Group group = new Group(shellWindow, SWT.NONE);
		group.setText(bundle.getString("Departamento"));
		group.setLayout(new GridLayout(1,true));
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		

		//Group - Keeps Variables of Department 
		group.setLayout(new GridLayout(2,true));
		group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		
		//components inside Group
		this.addComponentInGroup(group);
		
		//Buttons "Accept" and "Cancel"
		this.addComponentOutGroup();

	}
	/**add components which are in group*/
	public void addComponentInGroup(Group group){

		labName = new Label (group, SWT.NONE);
		labName.setText(bundle.getString("Nombre"));
		labName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	
		
		tName = new Text  (group, SWT.BORDER);	
		tName.setText(this.getArrayDB()[0]);	
		tName.setSize(100,20);
		tName.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));
		
		labNumber = new Label (group, SWT.NONE);
		labNumber.setText(bundle.getString("I10_dep_num"));
		labNumber.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	
	
		tNumber = new Text  (group, SWT.BORDER);	
		tNumber.setText(this.getArrayDB()[1]);	
		tNumber.setSize(100,20);
		tNumber.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));
		
		labBoss = new Label (group, SWT.NONE);
		labBoss.setText(bundle.getString("I10_dep_jefe"));
		labBoss.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));	
		
		comboBoss = new Combo  (group, SWT.DROP_DOWN );	
//TODO combo gets items from DB (about possible boss)
		ArrayList<Empleado> listaCoincidencias= this.vista.getEmpleado(null, null, null, null, null, null,2);
		String[] nombresEmpleados= new String[listaCoincidencias.size()];
		for(int cont=0;cont<listaCoincidencias.size();cont++){
			
			nombresEmpleados[cont]=(listaCoincidencias.get(cont).getNombre()+" "
			+listaCoincidencias.get(cont).getApellido1()+" "+listaCoincidencias.get(cont).getApellido2());
		}
		comboBoss.setItems(nombresEmpleados);

		comboBoss.setLayoutData	(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));
		
		
		
		
		//CREACION DEL JEFE Y DEL DEPARTAMENTO
		
		String nombreJefe = comboBoss.getText();
		//TODO separar nombres y apellidos y ¿coger primer empleado?
		int numeroDepartamento = Integer.valueOf(tNumber.getText());
		Empleado jefe=listaCoincidencias.get(comboBoss.getSelectionIndex());
		String nombreDepartamento = tName.getText();
		
		Departamento departamento = new Departamento(nombreDepartamento,
				numeroDepartamento, jefe);
		
		this.vista.insertDepartamento(departamento);
		
		butNewBoss		= new Button(group, SWT.PUSH);
		//butNewBoss	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		butNewBoss	.setText(bundle.getString("I10_dep_nuevo_jefe"));
		butNewBoss	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
	}
	
	/**add components out of group (button Accept and Cancel)*/
	public void addComponentOutGroup(){
		//Buttons "Accept" and "Cancel"
		
		bAccept		= new Button(shellWindow, SWT.PUSH);
		bAccept	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bAccept	.setText(bundle.getString("Aceptar"));
		bAccept	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		bCancel		= new Button(shellWindow, SWT.PUSH);		
		bCancel	.setLayoutData	(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		bCancel	.setText(bundle.getString("Cancelar"));
		bCancel	.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	/**allow to close window on ECS key*/
	public void closeOnECS(final Shell newShell) {
		
		newShell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
					newShell.close();
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = false;
					break;
				}
			}
		});
	}
	/**check if the String text is interger*/
	public boolean integerCheck(String string){
	    try {
	    	int n = Integer.parseInt( string );
	    	return true;
	    } catch (Exception e) {
	    	System.out.println("Non-integer value");
	        return false;
	    }
	}
	
/**GET methods*/
	public ResourceBundle getBundle(){
		return bundle;
	}
	public Shell getPadre(){
		return padre;
	}
	public Image getIconDep(){
		return iconDep;
	}
	public String[] getArrayDB(){
		return arrayDB;
	}
	public Label getLabName(){
		return labName;
	}
	public Label getLabNumber(){
		return labNumber;
	}
	public Label getLabBoss(){
		return labBoss;
	}
	public Button getButNewBoss(){
		return butNewBoss;
	}
	public Button getBAccept(){
		return bAccept;
	}
	public Button getBCancel(){
		return bCancel;
	}
	public Text getTName(){
		return tName;
	}
	public Text getTNumber(){
		return tNumber;
	}
	public Combo getComboBoss(){
		return comboBoss;
	}
/**SET methods*/
	public void setIconDep(Image newImage){
		iconDep = newImage;
	}
	public void setBundle(ResourceBundle newBundle){
		bundle = newBundle;
	}
	public void setArrayDB(String[]newArrayDB){
		arrayDB = newArrayDB;
	}
	public void setLabName(Label newLabel){
		labName = newLabel;
	}
	public void setLabNumber(Label newLabel){
		labNumber = newLabel;
	}
	public void setLabBoss(Label newLabel){
		labBoss = newLabel;
	}
	public void setButNewBoss(Button newButNewBoss){
		butNewBoss = newButNewBoss;
	}
	public void setBAccept(Button newBAccept){
		bAccept = newBAccept;
	}
	public void setBCancel(Button newBCancel){
		bCancel = newBCancel;
	}
	public void setTName(Text newTName){
		tName = newTName;
	}
	public void setTNumber(Text newTNumber){
		tNumber = newTNumber;
	}
	public void setComboBoss(Combo newComboBoss){
		comboBoss = newComboBoss;
	}
	
}
