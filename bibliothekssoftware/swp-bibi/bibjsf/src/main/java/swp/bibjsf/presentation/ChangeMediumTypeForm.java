package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.MediumType;
import swp.bibjsf.businesslogic.MediumTypeHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Ändert einen existierenden Medientyp.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class ChangeMediumTypeForm extends MediumTypeForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 8941964460426499373L;

	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new MediumType "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				MediumTypeHandler mth = MediumTypeHandler.getInstance();
				mth.update(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								"Mediumtyp wurde erfolgreich bearbeitet"));
				return "success";

			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Mediumtyp konnte nicht bearbeitet werden"));
				return "error";
			} catch (NoSuchBusinessObjectExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Mediumtyp existiert nicht und konnte nicht bearbeitet werden"));
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde kein Mediumtyp übergeben."));
			return "error";
		}
	}

	/**
	 * Setzt den geänderten Medientyp.
	 * 
	 * @param newMediumType
	 *            geänderter Medientyp
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public String edit(MediumType mediumtype) {
		element = mediumtype;
		return "edit";
	}
}
