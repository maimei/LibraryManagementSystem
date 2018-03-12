package swp.bibjsf.presentation;

import java.util.Date;

import swp.bibcommon.ClosedTime;

/**
 *  Abstrakte Oberklasse für Formen, die geschlossene Zeiträume hinzufügen.
 *  
 * @author Helena Meißner
 */
public abstract class ClosedTimeForm extends BusinessObjectForm<ClosedTime> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 6649673148439063393L;

	/**
	 * Getter für das Datum, ab dem die Bibliothek geschlossen ist.
	 * @return Datum, ab dem die Bibliothek geschlossen ist.
	 */
	public Date getStart() {
		return element.getStart();
	}
	
	/**
	 * Setter für das Datum, ab dem die Bibliothek geschlossen ist.
	 * @param start Datum, ab dem die Bibliothek geschlossen ist.
	 */
	public void setStart(Date start) {
		element.setStart(start);
	}
	
	/**
	 * Getter für den letzen Tag, bevor die Bibliothek wieder geöffnet wird.
	 * @return Letzer Tag, bevor die Bibliothek wieder geöffnet wird.
	 */
	public Date getTill() {
		return element.getTill();
	}
	
	/**
	 * Setter für den letzen Tag, bevor die Bibliothek wieder geöffnet wird.
	 * @param end
	 * 			Letzer Tag, bevor die Bibliothek wieder geöffnet wird.
	 */
	public void setTill(Date till) {
		element.setTill(till);
	}
	
	/**
	 * Getter für den Anlass der vorrübergehenden Bibliotheksschliessung.
	 * @return Anlass der Bibliotheksschliessung.
	 */
	public String getOccasion() {
		return element.getOccasion();
	}
	
	/**
	 * Setter für den Anlass der vorrübergehenden Bibliotheksschliessung.
	 * @param occasion
	 * 			Anlass der Bibliotheksschliessung.
	 */
	public void setOccasion(String occasion) {
		element.setOccasion(occasion);
	}
}
