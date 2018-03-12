package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.MediumType;
import swp.bibjsf.businesslogic.MediumTypeHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.presentation.MediumTypeForm;

/**
 * Fügt einen neuen Medientyp hinzu.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class AddMediumTypeForm extends MediumTypeForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 3712679349453336411L;
	
	public AddMediumTypeForm(){
		super();
		element = new MediumType();
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#save()
     */
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new category " + ((element == null) ? "NULL" : element.toString()));
    	if (element != null) {
    		try {
    			MediumTypeHandler mth = MediumTypeHandler.getInstance();
    			mth.add(element);
    			// reset so that the next category can be added without need to press a button
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
    					"Info", element.getName() + " wurde erfolgreich hinzugefügt"));
    			reset();
    			return "success";
    		} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "MediumTyp konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		} catch (BusinessElementAlreadyExistsException e2) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "MediumTyp existiert bereits und konnte nicht hinzugefügt werden"));
        		reset();
    			return "error";
    		}
       	} else {
    		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurde keine MediumTyp übergeben."));
    		return "error";
    	}    	
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
     */
	@Override
	public String reset() {
		element = new MediumType();
		return super.reset();
	}
}
