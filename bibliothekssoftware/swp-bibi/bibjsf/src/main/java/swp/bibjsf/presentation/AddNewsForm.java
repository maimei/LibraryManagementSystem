package swp.bibjsf.presentation;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.News;
import swp.bibjsf.businesslogic.NewsHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;

/**
 * Fügt eine neue Nachricht hinzu. 
 * 
 * @author Bernd Poppinga
 */
@ManagedBean
@SessionScoped
public class AddNewsForm extends NewsForm{
	
	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = -4684305859349177551L;
	
	public AddNewsForm (){
		super();
		element = new News();
		element.setDateOfPublication(new Date());
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#save()
     */
	@Override
	public String save() {
    	logger.debug("request to save new News " + ((element == null) ? "NULL" : element.toString()));
    	if (element != null) {
    		try {
    			NewsHandler nh = NewsHandler.getInstance();
    			nh.add(element);
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
    					"Info", element.getTitle() + " wurde erfolgreich hinzugefügt"));
    			// reset so that the next news can be added without need to press a button
    			reset();
    			return "success";
    		} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "News konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		} catch (BusinessElementAlreadyExistsException e2) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "News existiert bereits und konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		}
       	} else {
    		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurden keine News übergeben."));
    		return "error";
    	}    	
	}
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
     */
	@Override
	public String reset() {
		element = new News();
		element.setDateOfPublication(new Date());
		return super.reset();
	}
}
