package swp.bibjsf.presentation;

import swp.bibcommon.Rating;

/**
 * Abstrakte Oberklasse für die AddRatingForm.
 *  
 * @author Helena Meißner
 */
public abstract class RatingForm extends BusinessObjectForm<Rating> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7588225216110697987L;

	/**
	 * Gibt die ID des bewerteten Mediums zurück.
	 * 
	 * @return Die ID des Mediums.
	 */
	public int getMediumID() {
		return element.getMediumID();
	}
	
	/**
	 * Setzt die ID des bewerteten Mediums.
	 * 
	 * @param mediumID Die zu setzende ID.
	 */
	public void setMediumID(int mediumID) {
		element.setMediumID(mediumID);
	}
	
	/**
	 * Gibt die ID des Benutzers zurück.
	 * 
	 * @return Die ID des Nutzers.
	 */
	public int getReaderID() {
		return element.getReaderID();
	}
	
	/**
	 * Setzt die ID des Benutzers.
	 * 
	 * @param readerID Die zu setzende ID.
	 */
	public void setReaderID(int readerID) {
		element.setReaderID(readerID);
	}
	
	/**
	 * Gibt die Bewertung zurück.
	 * 
	 * @return Die Bewertung.
	 */
	public int getRating() {
		return element.getRating();
	}
	
	/**
	 * Setzt die Bewertung.
	 * 
	 * @param rating Die zu setzende Bewertung.
	 */
	public void setRating(int rating) {
		element.setRating(rating);
	}	
}
