package swp.bibjsf.presentation;

import swp.bibcommon.Medium;
import swp.bibcommon.Reservation;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.businesslogic.ReservationHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Table für Vormerkungen. Unterstützt Auswählen, Sortieren, Filtern, CSV-Im- &
 * Export, Lazy Loading.
 * 
 * @author Bernd Poppinga, Helena Meißner
 * 
 */
@ManagedBean
@SessionScoped
public class ReservationTable extends TableForm<Reservation> {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1L;

	public ReservationTable() throws DataSourceException {
		super(ReservationHandler.getInstance());
		try {
			model = new TableDataModel<Reservation>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Vormerkungen können nicht angezeigt werden"));
		}
	}

	/**
	 * Gibt den Status der Vormerkung als String zurück.
	 * 
	 * @author Bernd Poppinga
	 * @param reservation
	 *            Die Vormerkung dessen Status bestimmt werden soll
	 * @return Status der Vormerkung als String
	 */
	public String getReservationStatus(Reservation reservation) {
		try {
			ExemplarHandler lh = ExemplarHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("mediumID", "=", ""
					+ reservation.getMediumID(), "AND",
					Constraint.AttributeType.INTEGER));
			cons.add(new Constraint("status", "=", "leihbar", "AND",
					Constraint.AttributeType.INTEGER));
			int count = lh.getNumber(cons);
			return "" + count + " Exemplar(e) verfügbar.";

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Berechnet wie viele frühere Vormerkungen anderer Leser es gibt.
	 * 
	 * @author Bernd Poppinga
	 * @param reservation
	 *            Vormerkung, von der aus berechnet werden soll
	 * @return Anzahl der früheren Vormerkugen
	 */
	public int getEarlierReservationCount(Reservation reservation) {
		int count = 0;
		try {
			ReservationHandler rh = ReservationHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("mediumID", "=", ""
					+ reservation.getMediumID(), "AND",
					Constraint.AttributeType.INTEGER));
			List<Reservation> list = rh.get(cons, 0, Integer.MAX_VALUE, null);
			for (Reservation r : list) {
				if (r.getReservationDate().before(
						reservation.getReservationDate()))
					count++;
			}
		} catch (Exception e) {
			logger.debug(e);
			return 0;
		}
		return count;
	}

	/**
	 * Ermittelt den Titel des Mediums.
	 * 
	 * @param mediumID
	 *            ID des Mediums
	 * @return der Titel
	 */
	public String getTitle(int mediumID) {
		try {
			MediumHandler mh = MediumHandler.getInstance();
			Medium medium = mh.get(mediumID);
			if (medium != null) {
				return medium.getTitle();
			}
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank", ""));
			return "";
		} catch (NoSuchBusinessObjectExistsException e2) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Es konnte kein passendes Objekt gefunden werden"));
			return "";
		}
		return null;
	}
}
