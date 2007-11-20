package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.ResourceBundle;

public class I12_Ayuda  {private Image icoPq;
private Shell shell;
private Display display;
private String html;

public I12_Ayuda(){


	display = new Display();
	shell = new Shell(display,SWT.DIALOG_TRIM);
	icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));		
	
	//works under WIN 
	String localpath = (new File("").getAbsolutePath());
	String helppath = "/Ayuda/index_es.html";
	localpath = localpath.replaceAll("\\\\", "/");
	localpath = localpath.replaceAll(" ", "%20");
	

	System.out.print("file://localhost/"+localpath+helppath);
	
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
public I12_Ayuda(ResourceBundle bundle){

	display = new Display();
	shell = new Shell(display,SWT.DIALOG_TRIM);
	icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));	
	String locale = bundle.getLocale().getCountry();
	html = "<html> \n <head> \n <script type=\"text/javascript\">\n"+
	"<!-- \n"+
	"function redir(){\n"+
	"window.location =\"file://localhost/C:/Documents%20and%20Settings/SzaryMysz/Pulpit/eclipse/IS_PRO/IS0708/Ayuda/index_es.html\" \n}\n"+
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
	I12_Ayuda help = new I12_Ayuda();
	help.abrirHelp();
}

}
