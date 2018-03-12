package swp.bibjsf.presentation;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Reservation;
import swp.bibjsf.businesslogic.ReservationHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Fügt eine neue Vormerkung hinzu.
 * 
 * @author Bernd Poppinga
 */
@ManagedBean
@SessionScoped
public class AddReservationForm extends ReservationForm {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1284546282220913505L;

	public AddReservationForm() {
		super();
		element = new Reservation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() {
		logger.debug("request to save new Reservation "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				ReservationHandler rh = ReservationHandler.getInstance();
				element.setReservationDate(new Date());
				try {
					element.setReaderID(new AuthBackingBean().getUserID());
				} catch (NullPointerException e) {
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_FATAL,
											"Fehler",
											"Du musst angemeldet sein, um eine Vormerkung machen zu können."));
					reset();
					return "error";
				}

				rh.add(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								"Vormerkung wurde erfolgreich hinzugefügt"));
				// reset so that the next news can be added without need to
				// press a button
				reset();
				return "success";
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Vormerkung konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (BusinessElementAlreadyExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Vormerkung existiert bereits und konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (NoSuchBusinessObjectExistsException e3) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Der Leser dieser Vormerkung existiert nicht, sie konnte daher nicht hinzugefügt werden"));
				reset();
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde keine Vormerkung übergeben."));
			return "error";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
	 */
	@Override
	public String reset() {
		element = new Reservation();
		return super.reset();
	}

	/**
	 * Setzt erst das Exemplar der Vormerkung und versucht dann, diese zu
	 * speichern.
	 * 
	 * @author Bernd Poppinga
	 * @param id
	 *            Die ID des Exemplars, das vorgemerkt werdne soll.
	 * @return Ergebnis von save
	 */
	public String setExemplarSave(int id) {
		element.setMediumID(id);
		return save();
	}
}