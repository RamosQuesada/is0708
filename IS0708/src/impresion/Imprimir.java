/**
 * 
 */
package impresion;

/**
 * @author Kuba Chudzinski
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.printing.*;

/*import interfaces.Cuadrante;
import aplicacion.Empleado;
import java.util.ArrayList;
import org.eclipse.swt.graphics.Point;
*/

public class Imprimir {
	
	private PrintDialog printDialog;
	private PrinterData printerData;
	private Shell imShell;
	private Display imDisplay;
	private Font font;
	private Printer printer;
	
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
	public void setPrinter(PrinterData printerData){
		if (!(printerData == null)) {
	          printer = new Printer(printerData);
	          //System.out.print(printer.getClientArea());
		}
	}
	
	public void imprimirImage(ImageData imageData){	
		try{printDialog.setText("PrintDialog");
		printDialog.setScope(PrinterData.ALL_PAGES);
		//printDialog.setPrintToFile(true);
		
		printerData = printDialog.open();
		setPrinter(printerData);
		Image image = new Image(printer, imageData);
		font = new Font(printer,"courier", 8, 0);
	    printer.startJob("PrintJob");
	    printer.startPage();
	          //Point size = imShell.getSize();    
	    GC gc = new GC(printer);
	    if (!image.isDisposed()){
	    	//System.out.println(p.getClientArea().width);
	        gc.setFont(font);
	        gc.drawString("Datos personales", 100, 100, true);
	        gc.drawImage(image, 200, 200);
	    }

	    printer.endPage();
	    gc.dispose();
	    printer.endJob();
	    printer.dispose();}
		catch (Exception e){
			System.out.println("excepcion al cerrar porque hay argumentos nulos");
		}
	}

	public void imprimirComposite(Composite composite){	
		printDialog.setText("PrintDialog");
		printDialog.setScope(PrinterData.ALL_PAGES);
		//printDialog.setPrintToFile(true);
		
		printerData = printDialog.open();
		if (!(printerData == null)) {
	          Printer p = new Printer(printerData);
	          font = new Font(p,"courier", 10, 0);
	          p.startJob("PrintJob");
	          p.startPage();
	          //Point size = imShell.getSize(); 
	          //Image image = new Image(imDisplay, composite.getClientArea().height, composite.getClientArea().width);
   
	          Rectangle rect = composite.getClientArea();
	          // Create buffer for double buffering
	          Image image = new Image(imDisplay, 800,800);
	          
	          GC gc1 = new GC(composite);
	         
	          gc1.copyArea(image, rect.x,rect.y);
	          
	          GC gc = new GC(p);         
	          Image printImage = new Image(p, image.getImageData());
	          if (!printImage.isDisposed()){
	        	  //System.out.println(p.getClientArea().width);
	        	  gc.setFont(font);
	        	  gc.drawString("Imprimir Composite Ejemplo", 50, 50, true);
	        	  gc.drawImage(printImage, 750, 150);
	          }

	          p.endPage();
	          gc.dispose();
	          p.endJob();
	          p.dispose();
	        }
	}
}

