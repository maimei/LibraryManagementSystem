package swp.bibjsf.presentation;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Extension;
import swp.bibjsf.businesslogic.ExtensionHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Fügt einen neuen Verlängerungwunsch hinzu 
 * 
 * @author Bernd Poppinga
 */
@ManagedBean
@SessionScoped
public class AddExtensionForm extends ExtensionForm{
	
	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 7715582275434004391L;
	
	public AddExtensionForm(){
		super();
		element = new Extension();
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#save()
     */
	@Override
	public String save() {
    	logger.debug("request to save new extension " + ((element == null) ? "NULL" : element.toString()));
    	if (element != null) {
    		try {
    			ExtensionHandler eh = ExtensionHandler.getInstance();
    			element.setExtensionDate(new Date());
    			eh.add(element);
    			 
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
    					"Info", "Verlägerungswunsch bis zum " + element.getExtensionDate() + " wurde erfolgreich hinzugefügt"));
    			// reset so that the next extension can be added without need to press a button
    			reset();
    			return "success";

    		} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "Verlängerung konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		} catch (BusinessElementAlreadyExistsException e2) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Verlängerung existiert bereits und konnte nicht hinzugefügt werden"));
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
    				"Achtung", "Es wurde keine Verlängerung übergeben."));
    		return "error";
    	}
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
     */
	@Override
	public String reset() {
		element = new Extension();
		return super.reset();
	}
}