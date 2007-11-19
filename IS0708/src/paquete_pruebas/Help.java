package paquete_pruebas;
import java.io.File;

import interfaces.I02;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;


public class Help {
	private Image icoPq;
	private Shell shell;
	private Display display;
	private String html;
	
	public Help(){

		display = new Display();
		shell = new Shell(display,SWT.DIALOG_TRIM);
		icoPq = new Image(display, I02.class.getResourceAsStream("icoPq.gif"));	
		
		html = "<html> \n <head> \n <script type=\"text/javascript\">\n"+
		"<!-- \n"+
		"function redir(){\n"+
		"window.location =\"http://help.eclipse.org/help33/index.jsp\" \n}\n"+
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
			Browser browser;
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
		Help help = new Help();
		help.abrirHelp();
	}

}


