package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Ändert ein existierndes Medium.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class ChangeMediumForm extends MediumForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 3675929371691880580L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new Medium "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				MediumHandler mh = MediumHandler.getInstance();
				mh.update(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								element.getTitle()
										+ " wurde erfolgreich bearbeitet"));
				return "success";
			} catch (DataSourceException e) {
    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler beim Zugriff auf die Datenbank", "Medium konnte nicht bearbeitet werden"));
    			return "error";
            } catch (NoSuchBusinessObjectExistsException e2) {
            	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Medium existiert nicht und konnte nicht bearbeitet werden"));
    			return "error";
            }
        } else {
        	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
    				"Achtung", "Es wurden kein Medium übergeben."));
        	return "error";
        }
	}

	/**
	 * Setzt das geänderte Medium.
	 * 
	 * @param newMedium
	 *            geändertes Medium
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public String edit(Medium newMedium) {
		element = newMedium;
		return "edit";
	}
}
