package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * ManagedBean zur Anzeige von Medien.
 * 
 * @author Bernd Poppinga
 *
 */
@ManagedBean
@SessionScoped
public class ShowMediumForm extends MediumForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -4807678360247093664L;

	/**
	 * Setzt das Medium
	 * 
	 * @param id
	 *            ID des Mediums
	 * @return war der Vorgang erfolgreich
	 */
	public String setMedium(int id) {
		try {
			MediumHandler mh = MediumHandler.getInstance();
			element = mh.get(id);
			return "show";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Medium konnte nicht geladen werden"));
			return "error";
		}
	}

	/**
	 * Gibt das Medium zurück.
	 * 
	 * @return das Medium
	 */
	public Medium getMedium() {
		return element;
	}

	/**
	 * Methode, die lediglich aufgrund von Vererbung existiert.
	 */
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		throw new NotImplementedException();
	}

	/**
	 * Setzt das Medium erneut mit den Werten aus der Datenbank.
	 */
	public void reload() {
		try {
			MediumHandler mh = MediumHandler.getInstance();
			element = mh.get(element.getId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Medium konnte nicht geladen werden"));
		}
	}
}