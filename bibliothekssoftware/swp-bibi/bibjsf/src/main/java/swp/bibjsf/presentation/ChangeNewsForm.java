package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.News;
import swp.bibjsf.businesslogic.NewsHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;


/**
 * Ändert eine existierende Nachricht
 * 
 * @author Bernd Poppinga, Niklas Bruns
 */
@ManagedBean
@SessionScoped
public class ChangeNewsForm extends NewsForm {


	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = -9066580703860665702L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() {
		logger.debug("request to save new News "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				NewsHandler ah = NewsHandler.getInstance();
				ah.update(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								element.getTitle()
										+ " wurde erfolgreich bearbeitet"));
				return "success";
			} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "News konnten nicht bearbeitet werden"));
    			return "error";
            } catch (NoSuchBusinessObjectExistsException e2) {
            	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "News existiert nicht und konnte nicht bearbeitet werden"));
    			return "error";
            }
        } else {
        	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurden keine News übergeben."));
        	return "error";
        }
	}

	/**
	 * Setzt die geänderte Nachricht
	 * 
	 * @param newNews
	 *            geänderte Nachricht
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public String edit(News newNews) {
		element = newNews;
		return "edit";
	}
}