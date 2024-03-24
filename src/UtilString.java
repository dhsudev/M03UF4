
/* Utilitats de confirmació
 *
 * Aquest mòdul conté diferents utilitats per gestionar strings
 */
public class UtilString {
	/*
	 * Donat un caràcter, retorna si és vocal
	 */
	public static boolean esVocal(char ch) {
		String vowels = "aàáäeèéëiìíïoòóöuúùü";
		vowels += vowels.toUpperCase();
		// Search if the char is a vowel
		for (int chIndex = 0; chIndex < vowels.length(); chIndex++) {
			if (ch == vowels.charAt(chIndex)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Donat un string, retorna el string separant les lletres amb coma espai
	 * (expecte l'última)
	 */
	public static String lletresSeparades(String word) {
		int size = word.length();
		String separated = "";
		char ch;
		if (size > 0) {
			for (int i = 0; i < size - 1; i++) {
				ch = word.charAt(i);
				if (Character.isLetter(ch)) {
					separated += word.charAt(i) + ", ";
				} else {
					separated += ch;
				}
			}
			separated += word.charAt(size - 1);
		}
		return (separated);
	}

	/*
	 * Donat un string, retorna el string treient tots els caràcters que no siguin
	 * lletres
	 */
	public static String nomesLletres(String word) {
		int size = word.length();
		String onlyLetters = "";
		char ch;
		for (int i = 0; i < size; i++) {
			ch = word.charAt(i);
			if (Character.isLetter(ch)) {
				onlyLetters += word.charAt(i);
			}
		}
		return (onlyLetters);
	}

	public static String seccioString(int numInici, int numFinal, String text) {
		String seccio = "";
		if (numFinal > numInici) {
			for (int i = numInici; i < numFinal; i++) {
				seccio += text.charAt(i);
			}
		} else if (numFinal < numInici) {
			for (int i = numInici; i > numFinal; i--) {
				seccio += text.charAt(i);
			}
		} else if (numFinal == 0 && numInici == 0) {
			seccio += text.charAt(0);
		}
		return (seccio);
	}

	/*
	 * Donat un string, retorn si pot ser convertit a int
	 * També podem definir si volem una funcionalitat estricte o no
	 * amb el parámetre bool estricte
	 */
	public static boolean esEnter(String text, boolean estricte) {
		if (!estricte) {
			text = text.strip();
		}
		if (text.isEmpty()) {
			return (false);
		}
		boolean esEnter = true;
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.charAt(i);
			if (!Character.isDigit(ch)) {
				esEnter = false;
			}
			if (i == 0 && (ch.equals('-') || ch.equals('+'))) {
				esEnter = true;
			}
		}
		return (esEnter);
	}

	public static boolean esEnter(String text) {
		return (esEnter(text, true));
	}

	/*
	 * Donat un string el converteix a int
	 * En cas d'especificar que no es vol estricte, omet els espais
	 */
	public static int aEnter(String text, boolean estricte) {
		if (!estricte) {
			text = text.strip();
		}
		int num = Integer.parseInt(text);
		return (num);
	}

	public static int aEnter(String text) {
		return (aEnter(text, true));
	}

	/*
	 * Donat un string i un size repeteix els caracters del string
	 * fins arribar al size donat
	 */
	public static String cadenaContinua(String text, int size) {
		String cadena = "";
		for (int i = 0; i < size; i++) {
			int index;
			if (i > text.length() - 1) {
				index = i % text.length();
			} else {
				index = i;
			}
			cadena += text.charAt(index);
		}
		return cadena;
	}

	/*
	 * Donada dos strings, compara si la primera conté la segona
	 * S'utilitza el paràmetre "estricte" per especificar
	 * si volem ometre accents i chars especials (ñ,Æ...)
	 */
	public static boolean esSubstring(String text, String subtext, boolean estricte) {
		if (!estricte) {
			text = text.toLowerCase();
			subtext = subtext.toLowerCase();
			text = normalize(text);
			subtext = normalize(subtext);
		}
		if (subtext.isEmpty()) {
			return true;
		}
		boolean contains = false;
		for (int i = 0; i <= text.length() - 1; i++) {
			int j = 0;
			int k = i;
			boolean equals = false;
			if (equalChars(text.charAt(i), subtext.charAt(j))) {
				equals = true;
				while (equals) {
					if (!equalChars(text.charAt(k), subtext.charAt(j))) {
						equals = false;
						// System.out.println("diff found");
						break;
					}
					if (j >= subtext.length() - 1 || k >= text.length() - 1) {
						equals = false;
						// System.out.println("Size overload");
						break;
					}
					j++;
					k++;
					// System.out.println("IguaALLSL round " + j);

				}
				if (j >= subtext.length() - 1) {
					contains = true;
				}
			}
			if (contains) {
				// System.out.println("Son igualss");
				break;
			}
		}
		return (contains);

	}

	public static boolean equalChars(Character ch1, Character ch2) {
		return (ch1.equals(ch2));
	}

	public static boolean esSubstring(String text, String subtext) {
		return (esSubstring(text, subtext, true));
	}

	public static String normalize(String text) {
		text = text.toLowerCase();
		String accentedChars = "àáäèéëìíïòóöúùüñẅẗÿḧæłøç";
		String unaccentedChars = "aaaeeeiiiooouuunwtyhaloc";

		for (int i = 0; i < accentedChars.length(); i++) {
			text = text.replace(accentedChars.charAt(i), unaccentedChars.charAt(i));
		}
		return (text);
	}

	// retorna cert quan text comença amb prefix, considerant si ha de ser o no
	// estricte
	public static boolean esPrefix(String text, String prefix, boolean estricte) {
		if (!estricte) {
			text = text.toLowerCase();
			prefix = prefix.toLowerCase();
			text = normalize(text);
			prefix = normalize(prefix);
		}
		int size = prefix.length();
		if (size > text.length()) {
			return (false);
		}
		String prefixText = "";
		for (int i = 0; i < size; i++) {
			prefixText += text.charAt(i);
		}
		if (size == text.length()) {
			prefixText = text;
		}
		return (equalstr(prefix, prefixText));
	}

	public static boolean esPrefix(String text, String prefix) {
		return (esPrefix(text, prefix, true));
	}

	// retorna cert quan text finalitza amb sufix, considerant si ha de ser o no
	// extricte
	public static boolean esSufix(String text, String sufix, boolean estricte) {
		if (!estricte) {
			text = text.toLowerCase();
			sufix = sufix.toLowerCase();
			text = normalize(text);
			sufix = normalize(sufix);
		}
		int size = sufix.length();
		if (size > text.length()) {
			return (false);
		}
		String sufixText = "";
		for (int i = text.length() - size; i < text.length(); i++) {
			sufixText += text.charAt(i);
		}
		if (size == text.length()) {
			sufixText = text;
		}
		return (equalstr(sufix, sufixText));
	}

	public static boolean esSufix(String text, String sufix) {
		return (esSufix(text, sufix, true));
	}

	public static boolean equalstr(String txt1, String txt2) {
		boolean equal = true;
		if (txt1.length() != txt2.length()) {
			return false;
		}
		for (int i = 0; i < txt1.length(); i++) {
			if (!equalChars(txt1.charAt(i), txt2.charAt(i))) {
				equal = false;
			}
		}
		return (equal);
	}

	// retorna el nombre d'aparicions del subtext dins del text, considerant si
	// ha de ser o no estricte
	public static int quants(String text, String subtext, boolean estricte) {
		if (!estricte) {
			text = normalize(text);
			subtext = normalize(subtext);
		}
		if (subtext.isEmpty()) {
			return 0;
		}
		if (esSubstring(text, subtext)) {
			int count = 0;
			int index = 0;
			while (index != text.length()) {
				// System.out.println("Search init");
				int index_final = index + subtext.length();
				if (index_final > text.length()) {
					index_final = text.length();
				}
				if (equalstr(seccioString(index, index_final, text), subtext)) {
					count++;
					// System.out.println("----------- Equal");
				} else {
					// System.out.println("----------- Not equal");
				}
				// System.out.println("SUBTEXT: " + seccioString(index, index_final, text));
				// System.out.println("BUSCANT: " + subtext);
				index++;
			}
			return (count);
		} else {
			return 0;
		}

	}

	public static int quants(String text, String subtext) {
		return (quants(text, subtext, true));
	}

	public static String entreComes(int[] array, char sp){
		if (array.length < 1){
			return "";
		}
		String out = "";
		out += array[0];
		for (int i = 1; i < array.length; i++) {
			out += sp + " " + array[i];
		}
		return (out);
	}

	// retorna la seqüència de subcadenes de text separades entre caràcters en
	// blanc, incloent els caracters en blanc quan inclouBlancs és cert
	public static String[] separa(String text, boolean inclouBlancs){
		String arrBlankChars = " \t\n";
		int wordsCount = 0;
		String[] emptyWord = new String[0];
		if(text.isEmpty()){return emptyWord;}
		// Comptem les paraules per poder crear l'array:
		for (int i = 1; i < text.length(); i++){
			// Comprovar que ha cambiar de un caracter a l'anterior és un "final paraula" :
			// espai + lletra || lletra + espai
			if(checkifContains(arrBlankChars, text.charAt(i)) != checkifContains(arrBlankChars, text.charAt(i-1))){
				// Si ha de incloure blancs i estem comprovant una paraula de blancs
				if(inclouBlancs && checkifContains(arrBlankChars, text.charAt(i -1))){
					wordsCount++;
				// Si és una paraula de lletres
				} else if(!checkifContains(arrBlankChars, text.charAt(i - 1))){
					wordsCount ++;
				}
			}
		}
		// Afegir Última paraula si hem de incloure espais o si la última es de lletres:
		if ((inclouBlancs || !checkifContains(arrBlankChars, text.charAt(text.length()-1)))){
			wordsCount ++;
		}
		//System.out.println("Num de paraules: " + wordsCount);
		// En cas de no trobar paraules retornem array buit
		if (wordsCount == 0){return emptyWord;}

		// Si no comançcem a a afegir paraules a l'array
		String[] words = new String[wordsCount];
		int startWord = 0;
		int iWord = 0;
		// Comptem les paraules per poder crear l'array:
		for (int i = 1; i < text.length(); i++){
			//System.out.printf("s{%d} - i{%d} -> %s\n", startWord, i, text.substring(startWord, i));
			// Comprovar que ha cambiar de un caracter a l'anterior és un "final paraula" :
			// espai + lletra || lletra + espai
			if(checkifContains(arrBlankChars, text.charAt(i)) != checkifContains(arrBlankChars, text.charAt(i-1))){
				// Si ha de incloure blancs i estem comprovant una paraula de blancs
				if(inclouBlancs && checkifContains(arrBlankChars, text.charAt(i -1))){
					words[iWord] = text.substring(startWord, i);
					//System.out.printf("Found word! (spaces) -> '%s'\n", text.substring(startWord, i));
					iWord ++;
				// Si és una paraula de lletres
				} else if(!checkifContains(arrBlankChars, text.charAt(i - 1))){
					words[iWord] = text.substring(startWord, i);
					//System.out.printf("Found word! -> '%s'\n", text.substring(startWord, i));
					iWord ++;
				}
				startWord = i;
			}
		}
		System.out.println("End loop with sIndex at: " + startWord);
		// Afegir Última paraula si hem de incloure espais o si la última es de lletres:
		if ((inclouBlancs || !checkifContains(arrBlankChars, text.charAt(text.length() -1 )))){
			//System.out.printf("End word! s{%d} -> '%s'\n",startWord, text.substring(startWord));
			words[iWord] = text.substring(startWord);
		}
		return (words);
	}
	// equival a separa(text, false)
	public static String[] separa(String text){
		return(separa(text, false));
	}
	public static boolean checkifContains(String searchList, char ch){
		for(int i = 0; i < searchList.length(); i++){
			if(equalChars(searchList.charAt(i), ch)) {return true;}
		}
		return false;
	}
	// retorna un String format per les cadenes de text separades pel separador amb darrer separador
	public static String junta(String[] cadenes, String separador, String darrerSeparador){
		if (cadenes.length == 0) {
			return "";
		}
		if (cadenes.length == 1) {
			return cadenes[0];
		}
		String str = "";
		for (int i = 0; i < cadenes.length - 1; i++){
			str += cadenes[i] + separador;
		}
		str = seccioString(0, str.length() - separador.length(), str);
		str += darrerSeparador + cadenes[cadenes.length -1];
		return (str);
	}
	// equivalent a junta(cadenes, separador, separador)
	public static String junta(String[] cadenes, String separador){
		return (junta(cadenes, separador, separador));
	}
}