package interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class I12_Ayuda {private Image icoPq;
private Shell shell;
private Display display;
private String html;

public I12_Ayuda(){

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
	I12_Ayuda help = new I12_Ayuda();
	help.abrirHelp();
}

}
