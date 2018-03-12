package swp.bibjsf.presentation;

import swp.bibcommon.Commentary;
import java.util.Date;

/**
 *  Abstrakte Oberklasse für Formen, die Kommentare hinzufügen oder ändern.
 *  
 * @author Bernd Poppinga
 */

public abstract class CommentaryForm extends BusinessObjectForm<Commentary>{

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 6775807508382579051L;
	
	/**
     * Getter für die an dem Kommentar beteiligte Person.
     *
     * @return ID der an dem Kommentar beteiligten Person.
     */
    public int getReaderID() {
		return element.getReaderID();
    }
    /**
     * Setter für die an dem Kommentar beteiligte Person.
     *
     * @param readerID ID der an dem Kommentar beteiligten Person.
     */
    public void setReaderID(int readerID) {
    	  element.setReaderID(readerID);
    }
    /**
     * Getter für das an dem Kommentar beteiligte Medium.
     *
     * @return ID des an dem Kommentar beteiligten Mediums.
     */
    public int getMediumID() {
		return element.getMediumID();
    }
    /**
     * Setter für das an dem Kommentar beteiligte Medium.
     *
     * @param mediumID ID des an dem Kommentar beteiligten Mediums.
     */
    public void setMediumID(int mediumID) {
    	  element.setMediumID(mediumID);
    }
    
    /**
     * Getter für das Abgabedatum des Kommentars.
     *
     * @return Abgabedatum des Kommentars.
     */
    public Date getDate() {
		return element.getDateOfPublication();
    }
    /**
     * Setter für das Abgabedatum des Kommentars.
     *
     * @param date Abgabedatum des Kommentars.
     */
    public void setFrom(Date date) {
    	  element.setDateOfPublication(date);
    }
    
    /**
     * Getter für den Inhalt des Kommentars.
     **
     * @return Inhalt des Kommentars.
     */
    public String getText() {
		return element.getCommentary();
    }
    
    /**
     * Setter für den Inhalt des Kommentars.
     **
     * @param text Inhalt des Kommentars.
     */
    public void setText(String text) {
		element.setCommentary(text);
    }
    
    /**
     * Gibt zurück, ob das Kommentar freigegeben worden ist.
     *
     * @return ob das Kommentar freigegeben worden ist.
     */
    public boolean isActivated() {
		return element.isActive();
    }
    /**
     * Setter für den Wert, der angibt ob das Kommentar freigegeben worden ist.
     *
     * @param isActivated ob das Kommentar freigegeben worden ist.
     */
    public void setActivated(boolean isActivated) {
    	  element.setActive(isActivated);
    }    
}
