package swp.bibjsf.presentation;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Commentary;
import swp.bibjsf.businesslogic.CommentaryHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Fügt ein neues Kommentar hinzu.
 * 
 * @author Bernd Poppinga
 */
@ManagedBean
@SessionScoped
public class AddCommentaryForm extends CommentaryForm{
	
	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 2841785597737269142L;
	
	public AddCommentaryForm(){
		element = new Commentary();
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#save()
     */
    @Override
    public String save() {
    	logger.debug("request to save new book " + ((element == null) ? "NULL" : element.toString()));
    	if (element != null) {
    		try {
    			CommentaryHandler ch = CommentaryHandler.getInstance();
    			element.setDateOfPublication(new Date());
    			element.setActive(false);
    			ch.add(element);
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
    					"Info", "Rezension wurde erfolgreich hinzugefügt, muss jedoch noch freigeschaltet werden"));
    			// reset so that the next Commentary can be added without need to press a button
    			reset();
    			return "success";
    		} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "Rezension konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		} catch (BusinessElementAlreadyExistsException e2) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Rezension existiert bereits und konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		} catch (NoSuchBusinessObjectExistsException e3) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Der Verfasser dieser Rezension existiert nicht, sie konnte daher nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		}
    	} else {
    		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurde keine Rezension übergeben."));
    		return "error";
    	}
    }
    
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
     */
	@Override
	public String reset() {
		element = new Commentary();
		return super.reset();
	}
}