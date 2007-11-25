/**
 * 
 */
package impresion;

/**
 * @author Kuba Chudzinski
 *
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.printing.*;
import interfaces.Cuadrante;
import aplicacion.Empleado;
import java.util.ArrayList;

public class Imprimir {
	private PrintDialog printDialog;
	private PrinterData printerData;
	private Shell imShell;
	private Display imDisplay;
	   /** 
	    * Class constructor. 
	    * @param display 
	    */
	public Imprimir(Display display){		
		imDisplay = display;
		imShell = new Shell(imDisplay);
		printDialog = new PrintDialog(imShell, SWT.NONE);
		printerData = new PrinterData();
		 
	}
	public PrinterData abrirDialogBox(){
		printDialog.setText("PrintDialog");
		printDialog.setScope(PrinterData.ALL_PAGES);
		//printDialog.setPrintToFile(true);
		return printerData = printDialog.open();
	}

	public void imprimirIO2(PrinterData pData, ArrayList<Empleado> empleados){
		
		if (!(pData == null)) {
			Printer p = new Printer(pData);
			p.startJob("PrintJob");
			p.startPage();

			GC gc2 = new GC(p);
			Cuadrante c = new Cuadrante(imDisplay,4,9,23,0,0,0,0,0,empleados);
			c.setTamaño(p.getClientArea().width, p.getClientArea().height);
			c.dibujarCuadranteDia(gc2,-1);

			p.endPage();
			gc2.dispose();
			p.endJob();
			p.dispose();
		}
	}
	
	public void imprimirImage(ImageData image){	
		printDialog.setText("PrintDialog");
		printDialog.setScope(PrinterData.ALL_PAGES);
		//printDialog.setPrintToFile(true);
		printerData = printDialog.open();
		if (!(printerData == null)) {
	          Printer p = new Printer(printerData);
	          p.startJob("PrintJob");
	          p.startPage();
	          Point size = imShell.getSize();    
	          GC gc = new GC(p);

	          Image printImage = new Image(p, image);
	          if (!printImage.isDisposed()){
	        	  gc.drawImage(printImage, size.x, size.y);
	          }

	          p.endPage();
	          gc.dispose();
	          p.endJob();
	          p.dispose();
	        }
	}
}

