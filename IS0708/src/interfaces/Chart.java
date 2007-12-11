package interfaces;

import java.awt.image.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
/**
 * Clase que genera graficos
 * @author Carlos Sánchez Garcia
 *
 */
public class Chart {
	/**
	 * Constructor de la clase
	 *
	 */
	public Chart(){
		
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
    	DefaultPieDataset data = new DefaultPieDataset();
    	data.setValue("Category 1", 401.);
    	data.setValue("Category 2", 227.);
    	data.setValue("Category 3", 211.);
    	
		JFreeChart chart = ChartFactory.createPieChart
        ("Sample Pie Chart",   // Title
         data,           // Dataset
         true                  // Show legend 
         , true,
         false
        );

         BufferedImage image = chart.createBufferedImage(300,300);
        return image;
    }
	/**
	 * Convierte de BufferedImage a ImageData
	 * @param bufferedImage imagen a convertir
	 * @return
	 */
	static ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}		
			return data;		
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}
	
	public Image dameImagen(ImageData img,Display display){
		final Image swtImage = new Image(display, img);
		return swtImage;
	}
	
	public static void main(String args[]){
		Chart a=new Chart();
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("SWT Image");
		shell.setLayout(new GridLayout(1,false));
		Canvas c = new Canvas(shell,SWT.NONE);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		c.setBackgroundImage(a.dameImagen(convertToSWT(a.creaImagen()),display));

		shell.setSize(700,500);
		// Mostrar ventana centrada en la pantalla
		shell.setLocation(
				display.getBounds().width  / 2 - shell.getSize().x / 2, 
				display.getBounds().height / 2 - shell.getSize().y / 2  );
		shell.open();
		
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.ICON_WARNING);
				messageBox.setText("Mensaje");
				// Diferentes iconos:
				// http://www.developer.com/java/other/article.php/10936_3330861_2
				messageBox.setMessage("I02_dlg_CerrarAp");
				e.doit = messageBox.open() == SWT.YES;
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
}
