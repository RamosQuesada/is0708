/*******************************************************************************
 * INTERFAZ I-02 :: Ventana principal Gerente
 *   por Carlos Sánchez
 *   
 * Interfaz principal de la aplicación para un gerente
 * ver 0.1
 *******************************************************************************/
package interfaces;
import idiomas.LanguageChanger;
import java.util.Locale;
import java.util.ResourceBundle;
import org.eclipse.swt.*;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Interfaz de un gerente
 * @author Carlos Sánchez
 *
 */
public class I02Gerente {
	Shell _shell;
	Display _display;
	ResourceBundle _bundle;
	Locale _locale;
	
	/**
	 * Constructor de la clase
	 * @param _shell 
	 * @param _display
	 * @param _bundle
	 * @param _locale
	 */
	public I02Gerente(Shell _shell, Display _display, ResourceBundle _bundle, Locale _locale) {
		super();
		this._shell = _shell;
		this._display = _display;
		this._bundle = _bundle;
		this._locale = _locale;
	}
	
	

}
