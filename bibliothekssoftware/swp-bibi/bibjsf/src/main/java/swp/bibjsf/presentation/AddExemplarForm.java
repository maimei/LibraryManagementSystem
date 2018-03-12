package swp.bibjsf.presentation;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Exemplar;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;

/**
 * @author Mathias Eggerichs, Niklas Bruns
 */
@ManagedBean
@SessionScoped
public class AddExemplarForm extends ExemplarForm {

	public AddExemplarForm() {
		super();
		element = new Exemplar();
		element.setDateOfAddition(new Date());
		element.setLendingCount(0);
	}

	private static final long serialVersionUID = -5286372129146419565L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() {
		logger.debug("request to save new exemplar "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				ExemplarHandler eh = ExemplarHandler.getInstance();
				element.setDateOfAddition(new Date());
				element.setLendingCount(0);
				int newID = eh.add(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Exemplar zu Medium " + element.getMediumID()
										+ " wurde erfolgreich hinzugefügt",
								"ID = " + newID));
				// reset so that the next exemplar can be added without need to
				// press a button
				reset();
				return "success";
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Exemplar konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (BusinessElementAlreadyExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Exemplar existiert bereits und konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde kein Exemplar übergeben."));
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
		element = new Exemplar();
		return super.reset();
	}

	/**
	 * Setzt die MediumID des Exemplar und übergibt Wert für Navigation
	 * 
	 * @author Bernd Poppinga
	 * @param mediumid
	 *            Die ID des zugehörigen Mediums
	 * @return Ausgabe für die Navigation
	 */
	public String setMedium(int mediumid) {
		element.setMediumID(mediumid);
		return "addExemplar";
	}
}