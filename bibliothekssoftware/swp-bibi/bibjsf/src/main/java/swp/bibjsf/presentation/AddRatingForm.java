package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Rating;
import swp.bibjsf.businesslogic.RatingHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Fügt eine neue Bewertung hinzu. 
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class AddRatingForm extends RatingForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -6522338462837943498L;

	/**
	 * Konstruktor.
	 */
	public AddRatingForm() {
		super();
	    element = new Rating();
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#save()
     */
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new rating " + ((element == null) ? "NULL" : element.toString()));
    	if (element != null) {
    		try {
    			RatingHandler rh = RatingHandler.getInstance();
    			rh.add(element);
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
    					"Info", "Das Medium wurde von dir mit " + element.getRating() + " Sternen bewertet."));
    			// reset so that the next news can be added without need to press a button
    			reset();
    			return "success";
    		} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "Die Berwertung konnte nicht abgegeben werden."));
        		reset();
    			return "error";
    		} catch (BusinessElementAlreadyExistsException e2) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Du hast dieses Medium bereits bewertet und kannst es nicht erneut bewerten."));
        		reset();
    			return "error";
    		}
       	} else {
    		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurde keine Bewertung übergeben."));
    		return "error";
    	}    	
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
     */
	@Override
	public String reset() {
		element = new Rating();
		return super.reset();
	}
}
