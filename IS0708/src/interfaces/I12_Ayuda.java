package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import idiomas.LanguageChanger;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class I12_Ayuda  {
	private Image icoPq;
	private Shell shell;
	private Display display;
	/**
	 *  String variable with the source code  of a web page that  redirects user to local help file ( writen as HTML ).  
	 */
	private String html;
	/**
	 *  Keeps the Absolute Path to the folder with a project
	 */
	private String localpath;
	/**
	 *  Keeps the Path inside a project folder to a HTML Help File
	 */
	private String helppath;
	/**
	 *  Class Constructor
	 *  This constructor will open default language User Help File ( ES )
	 */
public I12_Ayuda(Display d){
	
	display = d;
	shell = new Shell(display,SWT.DIALOG_TRIM);
	icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));		
	
	/*
	 *  A place where variables localpath, helppath are initialized and prepared to by used as a part of URL address
	 *  	Important 
	 *   	it is designed to work under Windows system file
	 */

	localpath = (new File("").getAbsolutePath());
	helppath = "/Ayuda/index_es.html";
	localpath = localpath.replaceAll("\\\\", "/");
	localpath = localpath.replaceAll(" ", "%20");
	System.out.print("file://localhost/"+localpath+helppath);
	
	
	// Source Code of a Web Page that redirects user to a locally kept Help Web Page
	
	html = "<html> \n <head> \n <script type=\"text/javascript\">\n"+
	"<!-- \n"+
	"function redir(){\n"+
	""+
	"window.location =\"file://localhost/"+localpath+helppath+"\"\n}\n"+
	"//-->\n"+
	"</script>"+
	"</head>"+
		"<body onLoad=\"redir()\">"+
	
		"</body>" +
	"</html>";
	
}
/**
 *  Class Constructor
 *  @param bundle keeps information about User Country of Origin 
 *  This constructor will open User Help File in the lounge depending on bungle variable
 */
public I12_Ayuda(ResourceBundle bundle){

	
	display = new Display();
	shell = new Shell(display,SWT.DIALOG_TRIM);
	icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));		
	
	/*
	 *  A place where variables localpath, helppath are initialized and prepared to by used as a part of URL address
	 *  	Important 
	 *   	it is designed to work under Windows system file
	 */

	localpath = (new File("").getAbsolutePath());
	
	helppath = "/Ayuda/index_" + bundle.getLocale().getCountry() + ".html";
	localpath = localpath.replaceAll("\\\\", "/");
	localpath = localpath.replaceAll(" ", "%20");
	System.out.print("file://localhost/"+localpath+helppath);
	
	
	// Source Code of a Web Page that redirects user to a locally kept Help Web Page
	
	html = "<html> \n <head> \n <script type=\"text/javascript\">\n"+
	"<!-- \n"+
	"function redir(){\n"+
	""+
	"window.location =\"file://localhost/"+localpath+helppath+"\"\n}\n"+
	"//-->\n"+
	"</script>"+
	"</head>"+
		"<body onLoad=\"redir()\">"+
	
		"</body>" +
	"</html>";
	

}
/**
 *  Class Constructor
 *  @param locale keeps information about User Country  
 *  This constructor will open User Help File in the lounge depending on locale variable
 */
public I12_Ayuda(Locale locale){

	
	display = new Display();
	shell = new Shell(display,SWT.DIALOG_TRIM);
	icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));		
	
	/*
	 *  A place where variables localpath, helppath are initialized and prepared to by used as a part of URL address
	 *  	Important 
	 *   	it is designed to work under Windows system file
	 */

	localpath = (new File("").getAbsolutePath());
	
	helppath = "/Ayuda/index_" + locale.getCountry() + ".html";
	localpath = localpath.replaceAll("\\\\", "/");
	localpath = localpath.replaceAll(" ", "%20");
	System.out.print("file://localhost/"+localpath+helppath);
	
	
	// Source Code of a Web Page that redirects user to a locally kept Help Web Page
	
	html = "<html> \n <head> \n <script type=\"text/javascript\">\n"+
	"<!-- \n"+
	"function redir(){\n"+
	""+
	"window.location =\"file://localhost/"+localpath+helppath+"\"\n}\n"+
	"//-->\n"+
	"</script>"+
	"</head>"+
		"<body onLoad=\"redir()\">"+
	
		"</body>" +
	"</html>";
	

}
public void abrirHelp(){
		shell.setLayout(new FillLayout());
		shell.setImage(icoPq);
		shell.setText("Turno Matic");
		shell.setSize(700, 500);
		Browser browser ;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			return;
		}
		browser.setText(html);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

public static void main(String [] args) {
	LanguageChanger lch = new LanguageChanger();
	//System.out.println(lch.getCurrentLocale().getCountry());
	I12_Ayuda help = new I12_Ayuda(lch.getCurrentLocale());
	help.abrirHelp();
}

}
