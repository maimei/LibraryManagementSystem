package swp.bibjsf.presentation;

import java.util.Date;

import swp.bibcommon.Exemplar;

/**
 * an abstract superclass of forms that add new or change existing exemplars.
 * 
 * @author Mathias Eggerichs, Niklas Bruns
 */
public abstract class ExemplarForm extends BusinessObjectForm<Exemplar> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -7195779536092113528L;

	/**
	 * Getter für den letzten Leser des Exemplars.
	 * 
	 * @return letzter Leser des Exemplars.
	 */
	public int getLatestReader() {
		return element.getLatestReader();
	}

	/**
	 * Setter für den letzten Leser des Exemplars.
	 * 
	 * @param latestReader
	 *            neuer letzter Leser des Exemplars.
	 */
	public void setLatestReader(int latestReader) {
		element.setLatestReader(latestReader);
	}

	/**
	 * Getter für das Datum der Hinzufügung des Exemplars.
	 * 
	 * @return Datum der Hinzufügung des Exemplars.
	 */
	public Date getDateOfAddition() {
		return element.getDateOfAddition();
	}

	/**
	 * Setter für das Datum der Hinzufügung des Exemplars.
	 * 
	 * @param date
	 *            das neue Datum der Hinzufügung des Exemplars.
	 */
	public void setDateOfAddition(Date date) {
		element.setDateOfAddition(date);
	}

	/**
	 * Getter für Notiz des Exemplars.
	 * 
	 * @return Notiz des Exemplars.
	 */
	public String getNote() {
		return element.getNote();
	}

	/**
	 * Setter für die Notiz des Exemplars.
	 * 
	 * @param note
	 *            neue Notiz des Exemplars.
	 */
	public void setNote(String note) {
		element.setNote(note);
	}

	/**
	 * Getter für den aktuellen Ausleihstatus des Exemplars.
	 * 
	 * @return Ausleihstatus des Exemplars.
	 */
	public String getStatus() {
		return element.getStatus();
	}

	/**
	 * Setter für den aktuellen Ausleihstatus des Exemplars.
	 * 
	 * @param status
	 *            neuer aktueller Ausleihstatus des Exemplars.
	 */
	public void setStatus(String status) {
		element.setStatus(status);
	}

	/**
	 * Getter für Anzahl der Ausleihen des Exemplars.
	 * 
	 * @return Anzahl der Ausleihen des Exemplars.
	 */
	public int getLendingCount() {
		return element.getLendingCount();
	}

	/**
	 * Setter für die Anzahl der Ausleihen des Exemplars.
	 * 
	 * @param lendingCount
	 *            neue Anzahl der Ausleihen des Exemplars
	 */
	public void setLendingCount(int lendingCount) {
		element.setLendingCount(lendingCount);
	}

	/**
	 * Getter für die ID des zugehörigen Mediums.
	 * 
	 * @return zugehörige ID des Mediums.
	 */
	public int getMediumID() {
		return element.getMediumID();
	}

	/**
	 * Setter für die zugehörige ID des Mediums.
	 * 
	 * @param mediumID
	 *            neue zugehörige ID des Mediums.
	 */
	public void setMediumID(int mediumID) {
		element.setMediumID(mediumID);
	}

	/**
	 * Getter für den Standort des Exemplars.
	 * 
	 * @return Standort des Exemplars.
	 */
	public String getPlace() {
		return element.getPlace();
	}

	/**
	 * Setter für den Standort des Exemplars.
	 * 
	 * @param place
	 *            neuer Standort des Exemplars..
	 */
	public void setPlace(String place) {
		element.setPlace(place);
	}

}
