package swp.bibjsf.presentation;

import swp.bibcommon.Extension;
import java.util.Date;

/**
 *  Abstrakte Oberklasse für Formen, die Verlängerungswünsche hinzufügen oder ändern.
 *  
 * @author Bernd Poppinga, Niklas Bruns
 */
public abstract class ExtensionForm extends BusinessObjectForm<Extension>{

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 6794095869108415854L;
	
	
    /**
     * Getter für die an dem Verlängerungswunsch beteiligte Person.
     *
     * @return ID der an dem Verlängerungswunsch beteiligten Person.
     */
    public int getReaderID() {
		return element.getReaderID();
    }
    
    /**
     * Setter für die an dem Verlängerungswunsch beteiligte Person.
     *
     * @param readerID ID der an dem Verlängerungswunsch beteiligten Person.
     */
    public void setReaderID(int readerID) {
    	  element.setReaderID(readerID);
    }
    
    /**
     * Getter für das an dem Verlängerungswunsch beteiligte Medium.
     *
     * @return ID des an dem Verlängerungswunsch beteiligten Mediums.
     */
    public int getExemplarID() {
		return element.getExemplarID();
    }
    
    /**
     * Setter für das an dem Verlängerungswunsch beteiligte Medium.
     *
     * @param mediumID ID des an dem Verlängerungswunsch beteiligten Mediums.
     */
    public void setExemplarID(int exemplarID) {
    	  element.setExemplarID(exemplarID);
    }
    
    /**
     * Getter für das Datum, an dem der Verlängerungswunsch abgegeben worden ist.
     *
     * @return Datum des Verlängerungswunsches.
     */
    public Date getExtensionDate() {
		return element.getExtensionDate();
    }
    
    /**
     * Setter für das Datum, an dem der Verlängerungswunsch abgegeben worden ist.
     *
     * @param date Datum des Verlängerungswunsches.
     */
    public void setExtensionDate(Date date) {
    	  element.setExtensionDate(date);
    }
 }