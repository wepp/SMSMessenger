package ua.com.qbee.smscrypt;

import java.util.HashMap;

public class Transliterate {

	private static HashMap<String, String> arrayTransliterate;
	private static HashMap<String, String> arrayUnTransliterate;

	// transliterate to latin
	public static String transliterate(String strStart) {
		initilizeTransliterate();
		String res = "";
		for (int i = 0; i < strStart.length(); i++) {
			if (arrayTransliterate.get("" + strStart.charAt(i)) != null) {
				res += arrayTransliterate.get("" + strStart.charAt(i));
			} else {
				res += strStart.charAt(i);
			}
		}
		return res;
	}

	// UNtransliterate from latin
	public static String unTransliterate(String strStart) {
		initilizeUnTransliterate();
		String res = "";
		for (int i = 0; i < strStart.length(); i++) {
			if(("" + strStart.charAt(i)).equals("ä")) {
				i++;
				res += arrayUnTransliterate.get("ä" + strStart.charAt(i));
			} else {
				if (arrayUnTransliterate.get("" + strStart.charAt(i)) != null) {
					res += arrayUnTransliterate.get("" + strStart.charAt(i));
				} else {
					res += strStart.charAt(i);
				}
			}
		}
		return res;
	}

	public static boolean needToTransliterate(String strStart) {
		initilizeTransliterate();
		for (int i = 0; i < strStart.length(); i++) {
			if (arrayTransliterate.get("" + strStart.charAt(i)) != null) {
				return true;
			}
		}
		return false;
	}

	public static boolean needToUntransliterate(String strStart) {
		initilizeUnTransliterate();
		for (int i = 0; i < strStart.length(); i++) {
			if (arrayUnTransliterate.get("" + strStart.charAt(i)) != null) {
				return true;
			}
		}
		return false;
	}

	private static void initilizeTransliterate() {
		arrayTransliterate = new HashMap<String, String>();
		arrayTransliterate.put("а", "£");
		arrayTransliterate.put("б", "à");
		arrayTransliterate.put("в", "¥");
		arrayTransliterate.put("г", "è");
		arrayTransliterate.put("д", "é");
		arrayTransliterate.put("е", "ù");
		arrayTransliterate.put("є", "ì");
		arrayTransliterate.put("ж", "ò");
		arrayTransliterate.put("з", "~");
		arrayTransliterate.put("и", "Ø");
		arrayTransliterate.put("і", "ø");
		arrayTransliterate.put("ї", "Å");
		arrayTransliterate.put("й", "å");
		arrayTransliterate.put("к", "Δ");
		arrayTransliterate.put("л", "Φ");
		arrayTransliterate.put("м", "Γ");
		arrayTransliterate.put("н", "Λ");
		arrayTransliterate.put("о", "Ω");
		arrayTransliterate.put("п", "Π");
		arrayTransliterate.put("р", "Ψ");
		arrayTransliterate.put("с", "Σ");
		arrayTransliterate.put("т", "Θ");
		arrayTransliterate.put("у", "Ξ");
		arrayTransliterate.put("ф", "Æ");
		arrayTransliterate.put("х", "æ");
		arrayTransliterate.put("ц", "ß");
		arrayTransliterate.put("ч", "É");
		arrayTransliterate.put("ш", "¤");
		arrayTransliterate.put("щ", "Ä");
		arrayTransliterate.put("ь", "Ö");
		arrayTransliterate.put("ю", "Ñ");
		arrayTransliterate.put("я", "Ü");
		arrayTransliterate.put("ґ", "¿");
		// Big ARTS
		arrayTransliterate.put("А", "ä£");
		arrayTransliterate.put("Б", "äà");
		arrayTransliterate.put("В", "ä¥");
		arrayTransliterate.put("Г", "äè");
		arrayTransliterate.put("Д", "äé");
		arrayTransliterate.put("Е", "äù");
		arrayTransliterate.put("Є", "äì");
		arrayTransliterate.put("Ж", "äò");
		arrayTransliterate.put("З", "ä~");
		arrayTransliterate.put("И", "äØ");
		arrayTransliterate.put("І", "äø");
		arrayTransliterate.put("Ї", "äÅ");
		arrayTransliterate.put("Й", "äå");
		arrayTransliterate.put("К", "äΔ");
		arrayTransliterate.put("Л", "äΦ");
		arrayTransliterate.put("М", "äΓ");
		arrayTransliterate.put("Н", "äΛ");
		arrayTransliterate.put("О", "äΩ");
		arrayTransliterate.put("П", "äΠ");
		arrayTransliterate.put("Р", "äΨ");
		arrayTransliterate.put("С", "äΣ");
		arrayTransliterate.put("Т", "äΘ");
		arrayTransliterate.put("У", "äΞ");
		arrayTransliterate.put("Ф", "äÆ");
		arrayTransliterate.put("Х", "äæ");
		arrayTransliterate.put("Ц", "äß");
		arrayTransliterate.put("Ч", "äÉ");
		arrayTransliterate.put("Ш", "ä¤");
		arrayTransliterate.put("Щ", "äÄ");
		arrayTransliterate.put("Ь", "äÖ");
		arrayTransliterate.put("Ю", "äÑ");
		arrayTransliterate.put("Я", "äÜ");
		arrayTransliterate.put("Ґ", "ä¿");
		// Russian
		arrayTransliterate.put("ё", "à");
		arrayTransliterate.put("ъ", "ü");
		arrayTransliterate.put("ы", "ñ");
		arrayTransliterate.put("э", "ö");

		arrayTransliterate.put("Ё", "äà");
		arrayTransliterate.put("Ъ", "äü");
		arrayTransliterate.put("Ы", "äñ");
		arrayTransliterate.put("Э", "äö");
	}

	private static void initilizeUnTransliterate() {
		arrayUnTransliterate = new HashMap<String, String>();
		arrayUnTransliterate.put("£", "а");
		arrayUnTransliterate.put("à", "б");
		arrayUnTransliterate.put("¥", "в");
		arrayUnTransliterate.put("è", "г");
		arrayUnTransliterate.put("é", "д");
		arrayUnTransliterate.put("ù", "е");
		arrayUnTransliterate.put("ì", "є");
		arrayUnTransliterate.put("ò", "ж");
		arrayUnTransliterate.put("~", "з");
		arrayUnTransliterate.put("Ø", "и");
		arrayUnTransliterate.put("ø", "і");
		arrayUnTransliterate.put("Å", "ї");
		arrayUnTransliterate.put("å", "й");
		arrayUnTransliterate.put("Δ", "к");
		arrayUnTransliterate.put("Φ", "л");
		arrayUnTransliterate.put("Γ", "м");
		arrayUnTransliterate.put("Λ", "н");
		arrayUnTransliterate.put("Ω", "о");
		arrayUnTransliterate.put("Π", "п");
		arrayUnTransliterate.put("Ψ", "р");
		arrayUnTransliterate.put("Σ", "с");
		arrayUnTransliterate.put("Θ", "т");
		arrayUnTransliterate.put("Ξ", "у");
		arrayUnTransliterate.put("Æ", "ф");
		arrayUnTransliterate.put("æ", "х");
		arrayUnTransliterate.put("ß", "ц");
		arrayUnTransliterate.put("É", "ч");
		arrayUnTransliterate.put("¤", "ш");
		arrayUnTransliterate.put("Ä", "щ");
		arrayUnTransliterate.put("Ö", "ь");
		arrayUnTransliterate.put("Ñ", "ю");
		arrayUnTransliterate.put("Ü", "я");
		arrayUnTransliterate.put("¿", "ґ");
		// Big ARTS
		arrayUnTransliterate.put("ä£", "А");
		arrayUnTransliterate.put("äà", "Б");
		arrayUnTransliterate.put("ä¥", "В");
		arrayUnTransliterate.put("äè", "Г");
		arrayUnTransliterate.put("äé", "Д");
		arrayUnTransliterate.put("äù", "Е");
		arrayUnTransliterate.put("äì", "Є");
		arrayUnTransliterate.put("äò", "Ж");
		arrayUnTransliterate.put("ä~", "З");
		arrayUnTransliterate.put("äØ", "И");
		arrayUnTransliterate.put("äø", "І");
		arrayUnTransliterate.put("äÅ", "Ї");
		arrayUnTransliterate.put("äå", "Й");
		arrayUnTransliterate.put("äΔ", "К");
		arrayUnTransliterate.put("äΦ", "Л");
		arrayUnTransliterate.put("äΓ", "М");
		arrayUnTransliterate.put("äΛ", "Н");
		arrayUnTransliterate.put("äΩ", "О");
		arrayUnTransliterate.put("äΠ", "П");
		arrayUnTransliterate.put("äΨ", "Р");
		arrayUnTransliterate.put("äΣ", "С");
		arrayUnTransliterate.put("äΘ", "Т");
		arrayUnTransliterate.put("äΞ", "У");
		arrayUnTransliterate.put("äÆ", "Ф");
		arrayUnTransliterate.put("äæ", "Х");
		arrayUnTransliterate.put("äß", "Ц");
		arrayUnTransliterate.put("äÉ", "Ч");
		arrayUnTransliterate.put("ä¤", "Ш");
		arrayUnTransliterate.put("äÄ", "Щ");
		arrayUnTransliterate.put("äÖ", "Ь");
		arrayUnTransliterate.put("äÑ", "Ю");
		arrayUnTransliterate.put("äÜ", "Я");
		arrayUnTransliterate.put("ä¿", "Ґ");
		// Russian
		arrayUnTransliterate.put("à", "ё");
		arrayUnTransliterate.put("ü", "ъ");
		arrayUnTransliterate.put("ñ", "ы");
		arrayUnTransliterate.put("ö", "э");

		arrayUnTransliterate.put("äà", "Ё");
		arrayUnTransliterate.put("äü", "Ъ");
		arrayUnTransliterate.put("äñ", "Ы");
		arrayUnTransliterate.put("äö", "Э");
	}

}
