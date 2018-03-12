package swp.bibjsf.presentation;

import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * A table form for lendings. Supports selection, sorting, filtering, file
 * upload, CSV export, and lazy loading. Editing is not supported.
 * 
 * @author Bernd Poppinga, Helena Meißner
 */
@ManagedBean
@SessionScoped
public class LendingTable extends TableForm<Lending> {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1L;

	public LendingTable() throws DataSourceException {
		super(LendingHandler.getInstance());
		try {
			model = new TableDataModel<Lending>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Leihen können nicht angezeigt werden"));
		}
	}

	/**
	 * Die Methode gibt die Mahngebühren zurück. Falls das Exemplar noch nicht
	 * zurückgegeben worden ist, werden die errechneten Mahngebühren
	 * zurückgegeben, sonst die festgeschriebenen.
	 * 
	 * @author Bernd Poppinga
	 * @param lending
	 *            Die Leihe deren Mahngebühren errechnet werden sollen.
	 * @return Mahngebühren
	 */
	public BigDecimal getFee(Lending lending) {

		if (lending != null) {
			if (lending.hasReturned()) {
				return lending.getFee();
			}
			try {
				return ((LendingHandler) handler).calcFee(lending);
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank", ""));
				return new BigDecimal(0);
			} catch (NoSuchBusinessObjectExistsException e) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Es konnte kein passendes Objekt gefunden werden"));
				return new BigDecimal(0);
			}
		} else {
			return new BigDecimal(0);
		}
	}
	

	/**
	 * ermittelt die Überziehungstage einer Ausleihe.
	 * 
	 * @author Helena Meißner
	 * @param lending
	 * 				die Ausleihe
	 * @return die Überziehungstage
	 */
	public int getOverdrawnDays(Lending lending) {
		if (lending != null) {
			try {
				int[] data = ((LendingHandler) handler).calcFeeData(lending);
				return data[0];
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank", ""));
			} catch (NoSuchBusinessObjectExistsException e) {
				FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
					"Es konnte kein passendes Objekt gefunden werden"));
			}
		}		
		return -1;
	}
	
	/**
	 * Gibt den heutigen Tag im SQL Format zurück
	 * @return den heutigen Tag
	 */
	public String getToday() {
		return DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd");
	}
	
	/**
	 * Ermittelt den Titel des Mediums zum zugehörigen Exemplar
	 * 
	 * @author Helena Meißner
	 * @param exemplarID
	 * 				ID des Exemplars
	 * @return der Titel
	 */
	public String getMediumTitle(int exemplarID) {
		try {
			ExemplarHandler eh = ExemplarHandler.getInstance();
			MediumHandler mh = MediumHandler.getInstance();
			Medium medium = mh.get(eh.get(exemplarID).getMediumID());
			return medium.getTitle();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_FATAL,
				"Fehler beim Zugriff auf die Datenbank", ""));
			return "";
		} catch (NoSuchBusinessObjectExistsException e2) {
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
				"Es konnte kein passendes Objekt gefunden werden"));
			return "";
		}
	}	
	
	
}