package swp.bibjsf.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 * Die Klasse PasswordSecurity dient dazu, die sicherheit(Komplexität) der
 * Passwörter zu gewährleisten.
 * 
 * @author (Niklas Bruns)
 * @version (21.01.2014)
 */
public class PasswordSecurity {
	/**
	 * Liste in der die verbotenen Passwörter stehen.
	 */
	private List<String> weakPasswords = null;

	/**
	 * Instanz eines Zufallsnummersgegerators
	 */
	private Random generator = null;

	/**
	 * String in dem alle kleinen Buchstaben stehen.
	 */
	private String lowerLetters;

	/**
	 * String in dem alle großen Buchstaben stehen.
	 */
	private String upperLetters;
	/**
	 * String in dem alle Sonderzeichen stehen.
	 */
	private String specialLetters;

	/**
	 * Constructor for objects of class PasswordSecurity.
	 */
	public PasswordSecurity() {
		weakPasswords = new ArrayList<String>();
		generator = new Random(System.currentTimeMillis());
		lowerLetters = "abcdefghijklmnopqrstuvwxyz";
		upperLetters = lowerLetters.toUpperCase();
		specialLetters = "!§$%&/()=?{[]}\\";
		fillWeakPasswords();
	}

	/**
	 * Diese Funktion dient dazu, die Liste weakPasswords zu füllen.
	 */
	private void fillWeakPasswords() {
		weakPasswords.add("12345678");
		weakPasswords.add("87654321");
		weakPasswords.add("password");
		weakPasswords.add("passwort");
		weakPasswords.add("abcd1234");
		weakPasswords.add("qwertzui");
	}

	/**
	 * Zählt ob genügend Zeichen im String vorhanden sind.
	 * 
	 * @param anzahlZahl
	 *            Anzahl Zahlen
	 * @param anzahlLower
	 *            Anzahl Kleinbuchstaben
	 * @param anzahlUpper
	 *            Anzahl Großbuchstaben
	 * @param anzahlSpezial
	 *            Anzahl Sonderzeichen
	 * @return ob mindestens die geforderte Anzahl vorhanden ist.
	 */
	private boolean countLetter(final String password, final int anzahlZahl,
			final int anzahlLower, final int anzahlUpper,
			final int anzahlSpezial) {

		
		
		int anzahlZahlCounter = anzahlZahl;
		int anzahlLowerCounter = anzahlLower;
		int anzahlUpperCounter = anzahlUpper;
		int anzahlSpezialCounter = anzahlSpezial;
		char[] list = password.toCharArray();
		for (char c : list) {
			if (Character.isDigit(c)) {
				anzahlZahlCounter--;
			} else if (Character.isLowerCase(c)) {
				anzahlLowerCounter--;
			} else if (Character.isUpperCase(c)) {
				anzahlUpperCounter--;
			} else {
				anzahlSpezialCounter--;
			}
		}
		return (anzahlZahlCounter <= 0 && anzahlLowerCounter <= 0
				&& anzahlUpperCounter <= 0 && anzahlSpezialCounter <= 0);
	}

	/**
	 * Funktion prüft, ob das übergebene Passwort in der Liste mit schwachen
	 * Passwörtern befindet.
	 * @param password Passwort welches überprüft werden soll.
	 * 
	 * @return true falls Passwort sicher, sonst false.
	 */
	private boolean checkIfPasswordIsInList(final String password) {
		for (String value : weakPasswords) {
			if (0 == password.compareToIgnoreCase(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Funktion dient dazu, ein Passwort auf dessen Sicherheit zu prüfen.
	 * 
	 * @param password
	 *            Passwort welches überprüft werden soll
	 * @return true wenn Passwort sicher, sonst falsch.
	 */
	public final boolean checkPassword(final String password) {
		if (password.length() < 8) {
			return false;
		}
		if (!countLetter(password, 1, 1, 1, 1)) {
			return false;
		}
		if (!checkIfPasswordIsInList(password)) {
			return false;
		}
		return true;
	}

	/**
	 * Funktion gibt übergeben Anzahl von Zahlen zurück.
	 * 
	 * @param number
	 *            Anzahl der Zeichen die zurück gegeben werden sollen.
	 * @return Zeichenkette
	 */
	private String getNumbers(final int number) {
		String returnValue = new String();
		for (int i = 0; i < number; i++) {
			returnValue += String.valueOf(generator.nextInt(10));
		}
		return returnValue;
	}

	/**
	 * Funktion gibt übergeben Anzahl von Buchstaben zurück.
	 * 
	 * @param number
	 *            Anzahl der Buchstaben
	 * @param lower
	 *            ob die Zeichen lowercase sein sollen, wenn nicht uppercase.
	 * @return gewünschte Anzahl von Buchstaben
	 */
	private String getLetters(final int number, final boolean lower) {
		String returnValue = new String();
		for (int i = 0; i < number; i++) {
			if (lower) {
				returnValue += lowerLetters.charAt(generator
						.nextInt(lowerLetters.length()));
			} else {
				returnValue += upperLetters.charAt(generator
						.nextInt(upperLetters.length()));
			}
		}
		return returnValue;
	}

	/**
	 * Funktion gibt übergeben Anzahl von Sonderezeichen zurück
	 * 
	 * @param number
	 *            Anzahl der Buchstaben
	 * @return gewünschte Anzahl von Buchstaben
	 */
	private String getSpecialLetters(final int number) {
		String returnValue = new String();
		for (int i = 0; i < number; i++) {
			returnValue += specialLetters.charAt(generator
					.nextInt(specialLetters.length()));
		}
		return returnValue;
	}

	/**
	 * Funktion um verbotene Passwörter hinzuzufügen.
	 * @param password schlechtes Passwort welches hinzugefügt werden soll.
	 */
	public final void addWeakPassword(final String password) {
		weakPasswords.add(password);
	}

	/**
	 * Generiert ein Passwort.
	 * 
	 * @param lower
	 *            wie viele lowercase Buchstaben mindestens enthalten sein
	 *            sollen.
	 * @param higher
	 *            wie viele highercase Buchstaben mindestens enthalten sein
	 *            sollen.
	 * @param special
	 *            wie viele Sonderzeichen mindestens enthalten sein sollen.
	 * @param size
	 *            länge des gewünschten Passworts.
	 * @return generiertes Passwort.
	 */
	public final String generatePassword(final int numbers, final int lower,
			final int higher, final int special, final int size) {
		
		int numberCounter = numbers;
		int lowerCounter = lower;
		int higherCounter = higher;
		int specialCounter = special;
		
		String returnValue = new String();
		while ((numberCounter + higherCounter + specialCounter + lowerCounter) < size) {
			switch (generator.nextInt(4)) {
			case 0:
				lowerCounter++;
				break;
			case 1:
				higherCounter++;
				break;
			case 2:
				specialCounter++;
				break;
			case 3:
				numberCounter++;
				break;
			default:
				break;
			}
		}
		returnValue += getLetters(lowerCounter, true);
		returnValue += getLetters(higherCounter, false);
		returnValue += getSpecialLetters(specialCounter);
		returnValue += getNumbers(numberCounter);
		returnValue = shuffleString(returnValue);
		return returnValue;
	}

	/**
	 * Funktion mischt den übergeben String.
	 * 
	 * @param value
	 *            String welcher gemischt werden soll
	 * @return gemischter String
	 */
	private String shuffleString(final String value) {
		ArrayList<Character> liste = new ArrayList<Character>();
		String returnValue = new String();
		for (Character c : value.toCharArray()) {
			liste.add(c);
		}
		Collections.shuffle(liste);
		for (Character c : liste) {
			returnValue += c;
		}
		return returnValue;
	}

}
