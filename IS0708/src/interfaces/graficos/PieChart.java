package interfaces.graficos;

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
 * @author Carlos Sanchez Garcia
 *
 */
public class PieChart {
	/**
	 * Constructor de la clase
	 *
	 */
	public PieChart(){
		
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public BufferedImage creaImagen()
    {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Enero", new Integer(75));
		pieDataset.setValue("Febrero", new Integer(10));
		pieDataset.setValue("Marzo", new Integer(10));
		pieDataset.setValue("Resto", new Integer(5));
		JFreeChart chart = ChartFactory.createPieChart("Ventas realizadas",
				pieDataset, true, true, false);

         BufferedImage image = chart.createBufferedImage(300,300);
        return image;
    }
	/**
	 * Convierte de BufferedImage a ImageData
	 * @param bufferedImage imagen a convertir
	 * @return
	 */
	public static ImageData convertToSWT(BufferedImage bufferedImage) {
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
	
	public void creaVentana(){
		final Shell shell = new Shell();
		shell.setText("SWT Image");
		shell.setLayout(new GridLayout(1,true));
		Label c = new Label(shell, SWT.CENTER);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		c.setImage(dameImagen(convertToSWT(creaImagen()),shell.getDisplay()));

		shell.setSize(350,350);
		// Mostrar ventana centrada en la pantalla
		shell.setLocation(
				shell.getDisplay().getBounds().width  / 2 - shell.getSize().x / 2, 
				shell.getDisplay().getBounds().height / 2 - shell.getSize().y / 2  );
		shell.open();
		
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				shell.dispose();
			}
		});
		
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}
	
}
