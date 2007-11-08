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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.*;
import interfaces.I02_cuadr;

public class Imprimir {
	private PrintDialog printDialog;
	private PrinterData printerData;
	private Shell imShell;
	private Display imDisplay;
	public Imprimir(Shell shell){
		imShell = shell;
		printDialog = new PrintDialog(imShell, SWT.NONE);
		printerData = new PrinterData();
	}
	public Imprimir(Shell shell, Display display){
		imShell = shell;
		imDisplay = display;
		printDialog = new PrintDialog(imShell, SWT.NONE);
		printerData = new PrinterData();
		 
	}
	
	public void imprimirImage(ImageData image){
		System.out.println("Pulsado Imprimir");	
		printDialog.setText("PrintDialogDemo");
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
	public void abrirDialogBox(){
		System.out.println("Pulsado Imprimir");	
		printDialog.setText("PrintDialogDemo");
		printDialog.setScope(PrinterData.ALL_PAGES);
		//printDialog.setPrintToFile(true);
		printerData = printDialog.open();
		if (!(printerData == null)) {
	          Printer p = new Printer(printerData);
	          p.startJob("PrintJob");
	          p.startPage();
	          Point size = imShell.getSize();
	          Image image = new Image(imDisplay, size.x, size.y);
	          GC gc = new GC(imDisplay);
	          imShell.update();
	          gc.copyArea(image, imShell.getLocation().x, imShell.getLocation().y);
	          gc.dispose(); 
	          // Get the ImageData and create a new printer Image from it
	          GC gc2 = new GC(p);
	          ImageData imageData = image.getImageData();
	          Image printImage = new Image(p, imageData);
	          gc2.drawImage(printImage, size.x, size.y);
	          
	          p.endPage();
	          gc2.dispose();
	          p.endJob();
	          p.dispose();
	        }
	};
	public void imprimirEstado(PrinterData printerData){
		if (printerData != null){
			switch(printerData.scope){
			case PrinterData.ALL_PAGES:
			System.out.println("Printing all pages.");
			break;
			case PrinterData.SELECTION:
			System.out.println("Printing selected page.");
			break;
			case PrinterData.PAGE_RANGE:
			System.out.print("Printing page range. ");
			System.out.print("From:"+printerData.startPage);
			System.out.println(" to:"+printerData.endPage);
			break;
			}
			if (printerData.printToFile){
			System.out.println("Printing to file.");
			//SafeSaveDialog ssDialog = new SafeSaveDialog(imShell);
			//ssDialog.open();
			
			
			}
			else
			System.out.println("Not printing to file.");
			if (printerData.collate)
			System.out.println("Collating.");
			else
			System.out.println("Not collating.");
			System.out.println("Number of copies:"+printerData.copyCount);
			System.out.println("Printer Name:"+printerData.name);
			}
		
	}

}

