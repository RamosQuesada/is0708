package aplicacion;

import org.eclipse.swt.graphics.GC;


public class Code128 {
	final static int A = 203;
	final static int B = 204;
	final static int C = 205;
	
	static int ToA         = 201;
	static int ToC         = 199;
	static int codeTable   = 204;

	
	

	/**
	 * Convierte una cadena de texto en una cadena binaria, donde los 1's son barras
	 * y los 0's separaciones.
	 * Cambia automáticamente de modo si es necesario.
	 * @param input la cadena a convertir
	 * @return la cadena codificada como 1's y 0's
	 */
	public static String code128(String input) {
		String encoded = "";
		String decoded = "";
		if (input.length() == 0) return "";
		char chr = 0;
		int cValue = 0;
		String[] charSet = new String[106];
		int curCodeTable = 0;
		int contador = 1;
		String startChar = "";
		String stopChar  = "1100011101011";
		int checkDigit   = 0;
		// Tabla de Code-128
		charSet[0]       = "11011001100";
		charSet[1]       = "11001101100";
		charSet[2]       = "11001100110";
		charSet[3]       = "10010011000";
		charSet[4]       = "10010001100";
		charSet[5]       = "10001001100";
		charSet[6]       = "10011001000";
		charSet[7]       = "10011000100";
		charSet[8]       = "10001100100";
		charSet[9]       = "11001001000";
		charSet[10]      = "11001000100";
		charSet[11]      = "11000100100";
		charSet[12]      = "10110011100";
		charSet[13]      = "10011011100";
		charSet[14]      = "10011001110";
		charSet[15]      = "10111001100";
		charSet[16]      = "10011101100";
		charSet[17]      = "10011100110";
		charSet[18]      = "11001110010";
		charSet[19]      = "11001011100";
		charSet[20]      = "11001001110";
		charSet[21]      = "11011100100";
		charSet[22]      = "11001110100";
		charSet[23]      = "11101101110";
		charSet[24]      = "11101001100";
		charSet[25]      = "11100101100";
		charSet[26]      = "11100100110";
		charSet[27]      = "11101100100";
		charSet[28]      = "11100110100";
		charSet[29]      = "11100110010";
		charSet[30]      = "11011011000";
		charSet[31]      = "11011000110";
		charSet[32]      = "11000110110";
		charSet[33]      = "10100011000";
		charSet[34]      = "10001011000";
		charSet[35]      = "10001000110";
		charSet[36]      = "10110001000";
		charSet[37]      = "10001101000";
		charSet[38]      = "10001100010";
		charSet[39]      = "11010001000";
		charSet[40]      = "11000101000";
		charSet[41]      = "11000100010";
		charSet[42]      = "10110111000";
		charSet[43]      = "10110001110";
		charSet[44]      = "10001101110";
		charSet[45]      = "10111011000";
		charSet[46]      = "10111000110";
		charSet[47]      = "10001110110";
		charSet[48]      = "11101110110";
		charSet[49]      = "11010001110";
		charSet[50]      = "11000101110";
		charSet[51]      = "11011101000";
		charSet[52]      = "11011100010";
		charSet[53]      = "11011101110";
		charSet[54]      = "11101011000";
		charSet[55]      = "11101000110";
		charSet[56]      = "11100010110";
		charSet[57]      = "11101101000";
		charSet[58]      = "11101100010";
		charSet[59]      = "11100011010";
		charSet[60]      = "11101111010";
		charSet[61]      = "11001000010";
		charSet[62]      = "11110001010";
		charSet[63]      = "10100110000";
		charSet[64]      = "11100001100";
		charSet[65]      = "10010110000";
		charSet[66]      = "10010000110";
		charSet[67]      = "10000101100";
		charSet[68]      = "10000100110";
		charSet[69]      = "10110010000";
		charSet[70]      = "10110000100";
		charSet[71]      = "10011010000";
		charSet[72]      = "10011000010";
		charSet[73]      = "10000110100";
		charSet[74]      = "10000110010";
		charSet[75]      = "11000010010";
		charSet[76]      = "11001010000";
		charSet[77]      = "11110111010";
		charSet[78]      = "11000010100";
		charSet[79]      = "10001111010";
		charSet[80]      = "10100111100";
		charSet[81]      = "10010111100";
		charSet[82]      = "10010011110";
		charSet[83]      = "10111100100";
		charSet[84]      = "10011110100";
		charSet[85]      = "10011110010";
		charSet[86]      = "11110100100";
		charSet[87]      = "11110010100";
		charSet[88]      = "11110010010";
		charSet[89]      = "11011011110";
		charSet[90]      = "11011110110";
		charSet[91]      = "11110110110";
		charSet[92]      = "10101111000";
		charSet[93]      = "10100011110";
		charSet[94]      = "10001011110";
		charSet[95]      = "10111101000";
		charSet[96]      = "10111100010";
		charSet[97]      = "11110101000";
		charSet[98]      = "11110100010";
		charSet[99]      = "10111011110";
		charSet[100]     = "10111101110";
		charSet[101]     = "11101011110";
		charSet[102]     = "11110101110";
		charSet[103]     = "11010000100";
		charSet[104]     = "11010010000";
		charSet[105]     = "11010011100";
		curCodeTable     = B;
		checkDigit       = B - 100;
		startChar        = charSet[checkDigit];
		//Añade un cero al principio si el modo es C y la longitud es impar
		if (curCodeTable == C) input += ((input.length() % 2 != 0) ? "0" : "");
		for (int i = 0; i < input.length(); i++){
			// Coger entrada
			chr = input.charAt(i);

			// Si los siguientes 4 caracteres son números, cambiar a modo C
			if (curCodeTable == B) {
				try {
					Integer.valueOf(input.substring(i, i+4));
					// Insertar cambio a modo C y actualizar checkDigit
					curCodeTable = C;
					encoded += charSet[99];
					decoded += 99 + " ";
					checkDigit += 99 * contador++;
				}
				catch (Exception e) {
					
				};
			}
			// Si estoy en modo C y me viene algo que no es un número
			else if (curCodeTable == C) {
				try {
					Integer.valueOf(input.substring(i, i+2));
				}
				catch (Exception e) {
					// Insertar cambio a modo B y actualizar checkDigit
					curCodeTable = B;
					encoded += charSet[100];
					decoded += 100 + " ";
					checkDigit += 100 * contador++;
				}
			}
	
			// Procesar entrada en modo A o B (modo texto)
			if (curCodeTable == A || curCodeTable == B){
				if      (chr < 32)              chr += 64;
				else if (chr > 31 && chr < 128) chr -= 32;
				else if (chr > 127)             chr -= 100;
				checkDigit += chr * contador++;
				encoded += charSet[chr];
				decoded += (int)chr + " ";

			}
			// Procesar entrada en modo C (modo numérico)
			else {
				// En codificación C se cogen los dígitos de 2 en 2
				cValue = Integer.valueOf(input.substring(i, i+2));
				checkDigit += cValue*contador++;
				i++;
				encoded += charSet[cValue];
				decoded += cValue + " ";
				cValue = 0;
			}			
		}
		// Calcular checkDigit y concatenar resultado.
		checkDigit %= 103;
		decoded += checkDigit;
		encoded = startChar + encoded + charSet[checkDigit] + stopChar;
		//System.out.println(decoded);
		return encoded;
	}
	
	/**
	 * Convierte una cadena a code128 y la dibuja sobre un GC.
	 * @param gc el GC sobre el que pintar
	 * @param input la cadena a convertir
	 */
	public static void pintarCode(GC gc, String input) {
		String encoded = code128(input);
		for (int i = 0; i < encoded.length(); i++) {
			if (encoded.charAt(i)=='1') gc.drawLine(i, 0, i, 10);
		}
	}
}