package swp.bibjsf.presentation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.time.DateFormatUtils;

import swp.bibcommon.ClosedTime;
import swp.bibjsf.businesslogic.ClosedTimeHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.utils.Constraint;

/**
 * Table für geschlossene Zeiträume. Unterstützt Auswählen, Sortieren, Filtern,
 * CSV-Im- & Export, Lazy Loading.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class ClosedTimeTable extends TableForm<ClosedTime> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -2880270274607049321L;

	public ClosedTimeTable() throws DataSourceException {
		super(ClosedTimeHandler.getInstance());
		try {
			model = new TableDataModel<ClosedTime>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Bibliothekspausen können nicht angezeigt werden"));
		}
	}

	/**
	 * Bestimmt die tage an denen geschlossen ist und gibt diese als Liste aus
	 * 
	 * @author Bernd Poppinga
	 * @param days
	 *            Bereich in dem die Tage bestimmt werden sollen
	 * @return Liste mit Strings die angeben wann geschlossen ist
	 */
	public final List<String> getClosedTimes(int days) {
		List<String> list = new ArrayList<String>();
		try {
			ClosedTimeHandler ch = ClosedTimeHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			Calendar cal = Calendar.getInstance();

			cons.add(new Constraint("till", ">=", DateFormatUtils.format(
					cal.getTime(), "yyyy-MM-dd"), "AND", null));
			cal.add(Calendar.DATE, days);
			cons.add(new Constraint("start", "<=", DateFormatUtils.format(
					cal.getTime(), "yyyy-MM-dd"), "AND", null));
			List<ClosedTime> closedDays = ch.get(cons, 0, Integer.MAX_VALUE,
					null);
			logger.info(closedDays);
			for (ClosedTime close : closedDays) {
				if (close.getStart().equals(close.getTill())) {
					list.add("Am "
							+ DateFormatUtils.format(close.getStart(),
									"dd.MM.yyy") + " geschlossen.");
				} else {
					list.add("Vom "
							+ DateFormatUtils.format(close.getStart(),
									"dd.MM.yyy")
							+ " bis zum "
							+ DateFormatUtils.format(close.getTill(),
									"dd.MM.yyyy") + " geschlossen.");
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

}
