package aplicacion;
import org.eclipse.swt.graphics.*;

/**
 * Este interfaz sirve para especificar objetos dibujables e imprimibles
 * @author Jakub, Daniel Dionne
 *
 */
public interface Drawable {
	/**
	 * Este método devuelve un ImageData que se pueda imprimir
	 * @param bn true si queremos una imagen en escala de grises
	 * @return el ImageData a imprimir
	 */
	public ImageData getPrintableImage(boolean bn);
	
	/**
	 * Este método devuelve un ImageData que se puede dibujar en la pantalla 
	 * @return el ImageData a dibujar en la pantalla
	 */
	public ImageData getDrawableImage();
}