package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import aplicacion.Vista;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class I12_Ayuda {

	private String htmlDefault;
	private Browser browser;
	private Image icoPq, icoPrnt, icoBk,icoFw,icoHome;
	private Shell shell;
	/**
	 * display as help is normally displayed after a user call this atribut will
	 * by passed to class constructor
	 */
	private Display display;
	/**
	 * String variable with the source code of a web page that redirects user to
	 * local help file ( writen as HTML ).
	 */
	final private String html;
	/**
	 * Keeps the Absolute Path to the folder with a project
	 */
	private String localpath;
	/**
	 * Keeps the Path inside a project folder to a HTML Help File
	 */
	private String helppath;

	private String filePath;


	/**
	 * Class Constructor
	 * 
	 * @param locale
	 *            keeps information about User Country This constructor will
	 *            open User Help File in the lounge depending on locale variable
	 * @param _display
	 *            class uses main window display atribut to show help window
	 */
	public I12_Ayuda(Display d, Locale locale, ResourceBundle bundle) {

		display = d;
		shell = new Shell(display);
		icoPq = new Image(display, Vista.class.getResourceAsStream("icoPq.gif"));

		htmlDefault = "<html> \n <head> \n " + "</head>"
			+ "<body>" + bundle.getString("I12_err_fileNotFound")
			 + "</body>" + "</html>";
		/*
		 * A place where variables localpath, helppath are initialized and
		 * prepared to by used as a part of URL address Important it is designed
		 * to work under Windows system file
		 */

		localpath = (new File("").getAbsolutePath());

		helppath = "/Ayuda/" + locale.getCountry() + "/index.html";
		localpath = localpath.replaceAll("\\\\", "/");
		filePath = localpath + helppath;
		localpath = localpath.replaceAll(" ", "%20");
		System.out.print("file://localhost/" + localpath + helppath);

		// Source Code of a Web Page that redirects user to a locally kept Help
		// Web Page

		html = "<html> \n <head> \n <script type=\"text/javascript\">\n"
				+ "<!-- \n" + "function redir(){\n" + ""
				+ "window.location =\"file://localhost/" + localpath + helppath
				+ "\"\n}\n" + "//-->\n" + "</script>" + "</head>"
				+ "<body onLoad=\"redir()\">" +

				"</body>" + "</html>";
		abrirHelp();
	}

	public void abrirHelp() {
		shell.setLayout(new FillLayout());
		shell.setImage(icoPq);
		shell.setText("Turno Matic");
		shell.setSize(700, 500);

		icoBk	= new Image(display, I12_Ayuda.class.getResourceAsStream("ico_Bk.gif"));
		icoFw	= new Image(display, I12_Ayuda.class.getResourceAsStream("ico_Fw.gif"));
		icoHome = new Image(display, I12_Ayuda.class.getResourceAsStream("ico_Home.gif"));
		icoPrnt = new Image(display, I12_Ayuda.class.getResourceAsStream("ico_Imprimir.gif"));
		
		Composite c = new Composite(shell,SWT.NONE);
		c.setLayout(new GridLayout(1,true));
		
		Composite cButtons = new Composite(c,SWT.NONE);
		cButtons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		cButtons.setLayout(new GridLayout(4,false));

		Button bBack	= new Button(cButtons, SWT.PUSH);
		Button bForward	= new Button(cButtons, SWT.PUSH);
		Button bHome	= new Button(cButtons, SWT.PUSH);
		Button bPrint	= new Button(cButtons, SWT.PUSH);
		
		bBack.setLayoutData		(new GridData(SWT.LEFT,  SWT.TOP, false, true, 1, 1));
		bHome.setLayoutData		(new GridData(SWT.FILL,  SWT.TOP, false, true, 1, 1));
		bForward.setLayoutData	(new GridData(SWT.LEFT,  SWT.TOP, false, true, 1, 1));
		bPrint.setLayoutData	(new GridData(SWT.RIGHT, SWT.TOP, true,  true, 1, 1));
		
		bBack.setImage(icoBk);
		bHome.setImage(icoHome);
		bForward.setImage(icoFw);
		bPrint.setImage(icoPrnt);
		bPrint.setToolTipText("I12_tip_imprimir");
		
		browser = new Browser(c, SWT.NONE);
		
		bBack.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				browser.back();
				
			}
			
		});
		bForward.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				browser.forward();				
			}		
		});
		bHome.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				browser.setText(I12_Ayuda.this.html);				
			}
			
		});
		bPrint.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				browser.execute("window.print();");
				
			}
		});
		try {
			//browser = new Browser(c, SWT.NONE);
			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			return;
		}
		
		if ((new File(this.filePath).exists())) {
			browser.setText(html);
		} else {
			browser.setText(this.htmlDefault);
		}

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();
	}
}