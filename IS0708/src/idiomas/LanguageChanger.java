package idiomas;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageChanger {
	
	Locale[] supportedLocales = {
		new Locale("es","ES"),
		new Locale("pl","POL"),
		new Locale("en","US")
	};
	Locale currentLocale = supportedLocales[0];
	ResourceBundle labels = ResourceBundle.getBundle("paquete.Fichero",currentLocale);
				 
	public void cambiarLocale(int locale){
		currentLocale=supportedLocales[locale];
		labels=ResourceBundle.getBundle("paquete.Fichero",currentLocale);
	}
	
	public ResourceBundle getBundle(){
		return labels;
	}
	
 }
