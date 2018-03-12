package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Exemplar;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * @author Mathias Eggerichs, Niklas Bruns
 */
@ManagedBean
@SessionScoped
public class ChangeExemplarForm extends ExemplarForm {
    
	private static final long serialVersionUID = 3125986486398605107L;
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#save()
     */
    @Override
	public String save() {
        logger.debug("request to save new exemplar " + ((element == null) ? "NULL" : element.toString()));
        if (element != null) {
            try {
                ExemplarHandler eh = ExemplarHandler.getInstance();
                eh.update(element);
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
                		"Info", "Exemplar wurde erfolgreich bearbeite"));
                return "success";

            } catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "Exemplar konnte nicht bearbeitet werden"));
    			return "error";
            } catch (NoSuchBusinessObjectExistsException e2) {
            	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Exemplar existiert nicht und konnte nicht bearbeitet werden"));
    			return "error";
            }
        } else {
        	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurde kein Exemplar übergeben."));
        	return "error";
        }
    }
    
	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#edit()
     */
    public String edit(Exemplar newExemplar) {
        element = newExemplar;
        return "edit";
    }    
}
