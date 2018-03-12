package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Category;
import swp.bibjsf.businesslogic.CategoryHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;

/**
 * Fügt eine neue Kategorie hinzu.
 * 
 * @author Bernd Poppinga
 */
@ManagedBean
@SessionScoped
public class AddCategoryForm extends CategoryForm {

	public AddCategoryForm() {
		super();
		element = new Category();
	}

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 4353253277844041969L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() {
		logger.debug("request to save new category "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				CategoryHandler ch = CategoryHandler.getInstance();
				ch.add(element);

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								element.getName()
										+ " wurde erfolgreich hinzugefügt"));
				// reset so that the next category can be added without need to
				// press a button
				reset();
				return "success";
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Kategorie konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (BusinessElementAlreadyExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Kategorie existiert bereits und konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde keine Kategorie übergeben."));
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
		element = new Category();
		return super.reset();
	}
}