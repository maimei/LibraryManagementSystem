package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Fügt ein neues Medium hinzu.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class AddMediumForm extends MediumForm {

	/**
	 * Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1L;

	public AddMediumForm(){
		super();
		element = new Medium();
	}
	
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new Medium " + ((element == null) ? "NULL" : element.toString()));
    	if (element != null) {
    		try {
    			
    			MediumHandler mh = MediumHandler.getInstance();
    			element.setRatingCount(0);
    			element.setId(mh.add(element));
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
    					"Info", "Medium wurde erfolgreich hinzugefügt"));
    			return "success";
    		} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "Medium konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		} catch (BusinessElementAlreadyExistsException e2) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Medium existiert bereits und konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		}
    	} else {
    		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurde kein Medium übergeben."));
    		return "error";
    	}
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
     */
	@Override
	public String reset() {
		element = new Medium();
		return super.reset();
	}
}
