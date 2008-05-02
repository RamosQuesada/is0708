package interfaces.graficos;

import java.awt.image.*;
import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
/**
 * Clase plantilla para aplicar el patrón Template Method 
 * a la generación de gráficos de distintos tipos
 * @author Carlos Sanchez Garcia
 *
 */
public abstract class Chart {
	protected ArrayList <String> fechas;
	protected ArrayList <Double> cantidades;
	/**
	 * Constructor de la clase
	 *
	 */
	public Chart(ArrayList<String> fechas,ArrayList<Double> cantidades){
		this.fechas=fechas;
		this.cantidades=cantidades;
	}
	/**
	 * Metodo que crea el grafico
	 * @return imagen en formato BufferedImage
	 */
	public abstract BufferedImage creaImagen();
	
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
	
	/**
	 * 
	 * @param img	imagen a convertir
	 * @param display display
	 * @return imagen convertida para swt
	 */
	public Image dameImagen(ImageData img,Display display){
		final Image swtImage = new Image(display, img);
		return swtImage;
	}
	
	/**
	 * Crea una ventana con el gráfico
	 *
	 */
	
	public void creaVentana(){
		final Shell shell = new Shell();
		shell.setText("SWT Image");
		shell.setLayout(new GridLayout(1,true));
		Label c = new Label(shell, SWT.CENTER);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		c.setImage(dameImagen(convertToSWT(creaImagen()),shell.getDisplay()));

		shell.setSize(800,800);
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
