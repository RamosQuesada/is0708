package idiomas;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageChanger {
	 private Locale currentLocale;
	 private ResourceBundle labels;
	 private Locale[] supportedLocales = {
			new Locale("es","ES"),
			new Locale("pl","POL"),
			new Locale("en","US")
		};
	public LanguageChanger(){		
		currentLocale = supportedLocales[0];
		labels = ResourceBundle.getBundle("idiomas.Idioma",currentLocale);
	}
			 
	public void cambiarLocale(int locale){
		currentLocale=supportedLocales[locale];
		labels=ResourceBundle.getBundle("idiomas.Idioma",currentLocale);
	}
	
	public ResourceBundle getBundle(){
		return labels;
	}
	public Locale getCurrentLocale(){
		return currentLocale;
	}
 }
