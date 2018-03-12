package swp.bibjsf.presentation;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibjsf.businesslogic.Statistics;
import swp.bibjsf.exception.DataSourceException;

/**
 * ManagedBean, die die allgemeine Daten für die Ausleihzahlen-Statistik bereithält.
 * 
 * @author Helena Meißner
 *
 */
@ManagedBean
@SessionScoped
public class CommonStatsForm implements Serializable {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -6942615582013699369L;

	/* Statistics-Instanz */
	private Statistics stats;
	
	/** 
	 * Konstruktor.
	 * @throws DataSourceException 
	 */
	public CommonStatsForm() throws DataSourceException {
		stats = Statistics.getInstance();
	}
	
	/**
	 * Liefert die Anzahl an Nuztern (Leser, Bibliothekare, Admins).
	 * @return die Anzahl an Nutzern
	 */
	public int getReaderCount() {
		try {
			return stats.getReaderCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Nutzern konnte nicht ermittelt werden"));
		}
		return -1;
	}
	
	/**
	 * Liefert die Anzahl an aktiven Nutzern.
	 * @return die Anzahl an Nutzern
	 */
	public int getActiveReaderCount() {
		try {
			return stats.getActiveReaderCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an aktiven Nutzern konnte nicht ermittelt werden"));
		}
		return -1;
	}
	
	/**
	 * Liefert die Anzahl an inaktiven Nutzern.
	 * @return die Anzahl an Nutzern
	 */
	public int getInactiveReaderCount() {
		try {
			return stats.getInactiveReaderCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an inaktiven Nutzern konnte nicht ermittelt werden"));
		}
		return -1;
	}
	
	/**
	 * Liefert die Anzahl an gesperrten Nutzern.
	 * @return die Anzahl an Nutzern
	 */
	public int getBlockedReaderCount() {
		try {
			return stats.getBlockedReaderCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an blockierten Nutzern konnte nicht ermittelt werden"));
		}
		return -1;
	}
	
	/**
	 * Liefert die Anzahl an Ausleihen, deren Mahngebühren noch nicht beglichen sind.
	 * @return die Anzahl an Ausleihen
	 */
	public int getFeeLendingCount() {
		try {
			return stats.getFeeLendingCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Ausleihen mit unbeglichenen Mahngebühren konnte nicht ermittelt werden"));
		}
		return -1;
	}
	
	/**
	 * Liefert die Anzahl an aktuell laufenden Ausleihen.
	 * @return Anzahl an aktuell laufenden Ausleihen
	 */
	public int getActiveLendingCount() {
		try {
			return stats.getActiveLendingCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an aktuellen Ausleihen konnte nicht ermittelt werden"));
		}
		return -1;
	}
	
	/**
	 * Liefert die durchschnittliche Anzahl an Ausleihen pro Nutzer.
	 * @return durchschnittliche Anzahl an Ausleihen pro Nutzer
	 */
	public String getLendingsPerReader() {
		try {
			return stats.lendingsPerReader();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Ausleihen pro Nutzer konnte nicht ermittelt werden"));
		}
		return "-1";
	}
	
	/**
	 * Liefert die Anzahl aller Medien.
	 * @return die Anzahl aller Medien.
	 * @throws DataSourceException
	 */
	public int getMediaCount() {
		try {
			return stats.getMediaCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Medien konnte nicht ermittelt werden"));
			return -1;
		}
	}
	
	/**
	 * Liefert die Anzahl aller Exemplare.
	 * @return die Anzahl aller Exemplare.
	 * @throws DataSourceException 
	 */
	public int getExemplarCount() {
		try {
			return stats.getExemplarCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Medien konnte nicht ermittelt werden"));
			return -1;
		}
	}
	
	/**
	 * Liefert die Anzahl aller leihbaren Exemplare.
	 * @return Anzahl aller leihbaren Exemplare
	 * @throws DataSourceException
	 */
	public int getLendableExemplarCount() {
		try {
			return stats.getLendableExemplarCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Medien konnte nicht ermittelt werden"));
			return -1;
		}
	}
	
	/**
	 * Liefert die Anzahl aller ausgeliehenen Exemplare.
	 * @return Anzahl aller ausgeliehenen Exemplare.
	 * @throws DataSourceException
	 */
	public int getLentExemplarCount() {
		try {
			return stats.getLentExemplarCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Medien konnte nicht ermittelt werden"));
			return -1;
		}
	}
	
	/**
	 * Liefert die Anzahl aller blockierten Exemplare.
	 * @return die Anzahl aller blockierten Exemplare
	 * @throws DataSourceException
	 */
	public int getBlockedExemplarCount() {
		try {
			return stats.getBlockedExemplarCount();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Anzahl an Medien konnte nicht ermittelt werden"));
			return -1;
		}
	}
}