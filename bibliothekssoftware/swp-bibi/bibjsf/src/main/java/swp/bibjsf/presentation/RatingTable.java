package swp.bibjsf.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Rating;
import swp.bibjsf.businesslogic.RatingHandler;
import swp.bibjsf.exception.DataSourceException;

import swp.bibjsf.utils.Constraint;

/**
 * Table für Berwertungen. Unterstützt Auswählen, Sortieren, Filtern, CSV-Im- &
 * Export, Lazy Loading.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class RatingTable extends TableForm<Rating> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -993450795103015710L;

	public RatingTable() throws DataSourceException {
		super(RatingHandler.getInstance());
		try {
			model = new TableDataModel<Rating>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Bewertungen können nicht angezeigt werden"));
		}
	}

	/**
	 * Ermittelt anhand von Leser ID und Medium ID die Bewertung des Lesers für
	 * das Medium
	 * 
	 * @author Bernd Poppinga
	 * @param readerid
	 *            ID des Lesers der bewertet hat
	 * @param mediumid
	 *            ID des Mediums das bewertet wurde
	 * @return Die Bewertung des Lesers für das Medium
	 */
	public int getRating(int readerid, int mediumid) {
		try {
			RatingHandler rh = RatingHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("readerID", "=", "" + readerid, "AND",
					Constraint.AttributeType.INTEGER));
			cons.add(new Constraint("mediumID", "=", "" + mediumid, "AND",
					Constraint.AttributeType.INTEGER));
			List<Rating> ratings = rh.get(cons, 0, 1, null);
			Rating rating = ratings.get(0);
			return rating.getRating();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Bewertung konnte nicht ermittelt werden"));
			return -1;
		}
	}
}
