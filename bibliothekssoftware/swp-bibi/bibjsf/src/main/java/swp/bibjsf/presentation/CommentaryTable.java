package swp.bibjsf.presentation;

import swp.bibcommon.Commentary;
import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.CommentaryHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.OrderBy;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * A table form for commentaries. Supports selection, sorting, filtering, file
 * upload, CSV export, and lazy loading. Editing is not supported.
 * 
 * @author Bernd Poppinga, Helena Meißner
 * 
 */
@ManagedBean
@SessionScoped
public class CommentaryTable extends TableForm<Commentary> {

	public CommentaryTable() throws DataSourceException {
		super(CommentaryHandler.getInstance());
		try {
			model = new TableDataModel<Commentary>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Kommentare können nicht angezeigt werden"));
		}
	}

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Aktiviert die ausgewählten Kommentare.
	 */
	public String activateSelected() {
		for (Commentary commentary : selectedElements) {
			commentary.setActive(true);
			try {
				handler.update(commentary);
				} catch (DataSourceException e1) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Kommentare können nicht aktiviert werden"));
				return "error";
			} catch (NoSuchBusinessObjectExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Kommentar"
												+ commentary.getId()
												+ "existiert nicht und kann nicht aktiviert werden"));
				return "error";
			}

		}
		selectedElements.clear();
		return "success";

	}

	/**
	 * Löscht die übergebene Rezension
	 * 
	 * @author Bernd Poppinga
	 * @param commentary Rezensionen die gelöscht werden soll
	 * @return Navigationsfall für die Faces
	 */
	public String delete(Commentary commentary) {
		try {
			logger.info("Löschen");
			List<Commentary> list = new ArrayList<>();
			list.add(commentary);
			handler.delete(list);
			return "success";
		} catch (Exception e) {
			return "error";
		}

	}
	
	/**
	 * Gibt die X neuesten Rezensionen zurück.
	 * 
	 * @author Bernd Poppinga
	 * @param count Anzahl der Rezensionen
	 * @return Liste der Rezensionen
	 */
	public List<Commentary> getNewest(int count) {
		List<Constraint> cons = new ArrayList<>();
		cons.add(new Constraint("active", "=", "true", "AND",
				Constraint.AttributeType.STRING));
		List<OrderBy> list = new ArrayList<>();
		list.add(new OrderBy("dateOfPublication"));
		try {
			return handler.get(cons, 0, 100, list);
		} catch (DataSourceException e) {
			logger.error(e);
			return null;
		} catch (NoSuchBusinessObjectExistsException e) {
			logger.info(e);
			return null;
		}
	}

	/**
	 * Gibt das zum Kommentar gehörende Medium zurück.
	 * 
	 * @author Bernd Poppinga
	 * @param Kommentar
	 *            des Mediums, dass zurückgegeben werden soll.
	 * @return Medium des Kommentars
	 */
	public Medium getMedium(Commentary commentary) {
		try {
			MediumHandler mh = MediumHandler.getInstance();
			return mh.get(commentary.getMediumID());

		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Überprüfung konnte nicht durchgeführt werden"));
			return null;
		} catch (NoSuchBusinessObjectExistsException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Objekt konnte nicht gefunden werden."));
			return null;
		}
	}
}