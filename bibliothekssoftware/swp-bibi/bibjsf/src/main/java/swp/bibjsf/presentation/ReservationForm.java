package swp.bibjsf.presentation;

import swp.bibcommon.Reservation;
import java.util.Date;

/**
 *  Abstrakte Oberklasse für Formen, die Vormerkungen hinzufügen oder ändern.
 *  
 * @author Bernd Poppinga
 */
public abstract class ReservationForm extends BusinessObjectForm<Reservation>{

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 3477646614118898451L;
	
	/**
     * Getter für die an der Leihe beteiligte Person.
     *
     * @return ID der an der Leihe beteiligten Person.
     */
    public int getReaderID() {
		return element.getReaderID();
    }
    
    /**
     * Setter für die an der Vormerkung beteiligte Person.
     *
     * @param readerID ID der an der Vormerkung beteiligten Person.
     */
    public void setReaderID(int readerID) {
    	  element.setReaderID(readerID);
    }
    
    /**
     * Getter für das an der Vormerkung beteiligte Medium.
     *
     * @return ID des an der Vormerkung beteiligten Mediums.
     */
    public int getMediumID() {
		return element.getMediumID();
    }
    
    /**
     * Setter für das an der Vormerkung beteiligte Medium.
     *
     * @param mediumID ID des an der Vormerkung beteiligten Mediums.
     */
    public void setMediumID(int mediumID) {
    	  element.setMediumID(mediumID);
    }
    
    /**
     * Getter für das Datum, an dem die Vormerkung gemacht worden ist.
     *
     * @return Datum der Vormerkung.
     */
    public Date getDate() {
		return element.getReservationDate();
    }
    
    /**
     * Setter für das Datum, an dem die Vormerkung gemacht worden ist.
     *
     * @param date Datum der Vormerkung.
     */
    public void setDate(Date date) {
    	 element.setReservationDate(date);
    }
}