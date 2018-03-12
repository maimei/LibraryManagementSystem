package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.ClosedTime;
import swp.bibjsf.businesslogic.ClosedTimeHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Form zum Hinzufügen von Zeiträumen, in denen die Bibliothek geschlossen ist.
 * 
 * @author Helena Meißner
 * 
 */
@ManagedBean
@SessionScoped
public class AddClosedTimeForm extends ClosedTimeForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -8853370698934898047L;

	/**
	 * Konstruktor.
	 */
	public AddClosedTimeForm() {
		super();
		element = new ClosedTime();
	}

	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new closed time "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				ClosedTimeHandler cth = ClosedTimeHandler.getInstance();
				if (element.getStart().before(element.getTill())
						|| element.getStart().equals(element.getTill())) {
					cth.add(element);
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_INFO,
											"Info",
											"Die Bibliothekspause wurde erfolgreich hinzugefügt"));
					reset();
					return "success";
				} else {
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_INFO,
											"Fehler",
											"Das Startdatum muss vor dem"
													+ " Enddatum liegen. Es wurde keine Bibliothekspause hinzugefügt"));
					reset();
					return "error";
				}
			} catch (DataSourceException e) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_FATAL,
										"Fehler beim Zugriff auf die Datenbank",
										"Die Bibliothekspause konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (BusinessElementAlreadyExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Die Bibliothekspause existiert bereits und konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde keine Bibliothekspause übergeben."));
			return "error";
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
	 */
	@Override
	public final String reset() {
		element = new ClosedTime();
		return super.reset();
	}
}
