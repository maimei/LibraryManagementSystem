package swp.bibjsf.presentation;

import swp.bibcommon.News;
import java.util.Date;

/**
 *  Abstrakte Oberklasse für Formen, die Nachrichten hinzufügen oder ändern.
 *  
 * @author Bernd Poppinga, Niklas Bruns
 */
public abstract class NewsForm extends BusinessObjectForm<News>{

	/**
	 * einzigartige Serialisierungs-ID
	 */
		private static final long serialVersionUID = -8934654737961973037L;
		
	/**
     * Getter für das Datum der Nachricht.
     *
     * @return Datum der Nachricht.
     */
    public Date getDateOfPublication() {
		return element.getDateOfPublication();
    }
    
    /**
     * Setter für das Datum der Nachricht
     * 
     * @param date das neue Datum
     */
    public void setDateOfPublication( Date date){
    	element.setDateOfPublication(date);
    }
    
    /**
     * Setter für das Datum der Nachricht.
     *
     * @param date Datum der Nachricht.
     */
    public void setReaderID(Date date) {
    	  element.setDateOfPublication(date);
    }
    
    /**
     * Getter für den Titel der Nachricht.
     *
     * @return Titel der Nachricht
     */
    public String getTitle() {
    	return element.getTitle();
    }
    
    /**
     * Setter für den Titel der Nachricht.
     *
     * @param title Titel der Nachricht.
     */
    public void setTitle(String title) {
    	element.setTitle(title);
    }
    
    /**
     * Getter für den Inhalt der Nachricht.
     *
     * @return Inhalt der Nachricht.
     */
    public String getContent() {
		return element.getContent();
    }
    
    /**
     * Setter für den Inhalt der Nachricht.
     *
     * @param text Inhalt der Nachricht.
     */
    public void setContent(String text) {
    	  element.setContent(text);
    }
}