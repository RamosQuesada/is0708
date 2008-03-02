package aplicacion;
import java.util.ResourceBundle;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

/**
 * Este interfaz sirve para especificar objetos dibujables e imprimibles
 * @author Jakub, Daniel Dionne
 *
 */
public interface Drawable {
	/**
	 * Este m�todo devuelve un ImageData que se pueda imprimir.
	 * @param bn true si queremos una imagen en escala de grises
	 * @return el ImageData a imprimir
	 * @see #getDrawableImage()
	 */
	public ImageData getPrintableImage(Device device, ResourceBundle bundle, Rectangle size, boolean bn);
	
	/**
	 * Este m�todo devuelve un ImageData que se puede dibujar en la pantalla. 
	 * @return el ImageData a dibujar en la pantalla
	 * @see #getPrintableImage(boolean)
	 */
	public ImageData getDrawableImage();
}