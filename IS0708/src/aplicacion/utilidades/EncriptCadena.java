package aplicacion.utilidades;

public class EncriptCadena

{

	public String retorno = "";

	public EncriptCadena(String c, String t, boolean a)

	{

		int i = 0, j = 0, ic = c.length(), it = t.length();

		int temp;

		int[] claveascii = new int[ic];

		int[] textoascii = new int[it];

		for (i = 0; i < ic; i++)
			claveascii[i] = c.charAt(i);

		for (i = 0; i < it; i++)
			textoascii[i] = t.charAt(i);

		if (a)

		{

			for (i = 0; i < it; i++)

			{

				j++;

				if (j >= ic)
					j = 0;

				temp = textoascii[i] + claveascii[j];

				if (temp > 255)
					temp = temp - 255;

				retorno = retorno + (char) temp;

			}

		}

		else

		{

			for (i = 0; i < it; i++)

			{

				j++;

				if (j >= ic)
					j = 0;

				temp = textoascii[i] - claveascii[j];

				if (temp < 0)
					temp = temp + 256;

				retorno = retorno + (char) temp;

			}

		}

	}
	
	public static String encripta(String cadena){
		EncriptCadena e=new EncriptCadena("pituso",cadena,true);		
		return e.getRetorno();		
	}
	
	public static String desencripta(String cadena){
		EncriptCadena e=new EncriptCadena("pituso",cadena,false);		
		return e.getRetorno();		
	}

	public String getRetorno() {
		return retorno;
	}
	
    public static void main (String[] args)

    {
    		String s="pitimazao";
    		String encript=EncriptCadena.encripta(s);
    		System.out.println(encript);
    		System.out.println(EncriptCadena.desencripta(encript));

    }


}
