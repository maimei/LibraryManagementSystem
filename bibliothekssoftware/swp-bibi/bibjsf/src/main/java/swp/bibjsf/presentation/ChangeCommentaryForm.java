package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Commentary;
import swp.bibjsf.businesslogic.CommentaryHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;


/**
 * Ändert ein existierendes Kommentar.
 * 
 * @author Bernd Poppinga, Niklas Bruns
 */
@ManagedBean
@SessionScoped
public class ChangeCommentaryForm extends CommentaryForm {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 8293810163627138348L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() {
		logger.debug("request to save new Commentary "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				CommentaryHandler ah = CommentaryHandler.getInstance();
				ah.update(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								"Kommentar wurde erfolgreich bearbeitet"));
				return "success";

			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Kommentar konnte nicht bearbeitet werden"));
				return "error";
			} catch (NoSuchBusinessObjectExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Kommentar existiert nicht und konnte nicht bearbeitet werden"));
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde kein Kommentar übergeben."));
			return "error";
		}
	}

	/**
	 * Setzt das geänderte Kommentar
	 * 
	 * @param newCommentary
	 *            geändertes Kommentar
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public String edit(Commentary newCommentary) {
		element = newCommentary;
		return "edit";
	}
}