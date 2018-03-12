package swp.bibjsf.presentation;

import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.MediumType;
import swp.bibjsf.businesslogic.ClosedTimeHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.businesslogic.MediumTypeHandler;
import swp.bibjsf.businesslogic.OpeningTimeHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

import java.math.BigDecimal;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Abstrakte Oberklasse für Formen, die Leihen hinzufügen oder ändern.
 * 
 * @author Bernd Poppinga
 */
public abstract class LendingForm extends BusinessObjectForm<Lending> {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1327580147191235987L;

	/**
	 * Getter für die an der Leihe beteiligte Person.
	 * 
	 * @return ID der an der Leihe beteiligten Person.
	 */
	public int getReaderID() {
		return element.getReaderID();
	}

	/**
	 * Setter für die an der Leihe beteiligte Person.
	 * 
	 * @param readerID
	 *            ID der an der Leihe beteiligten Person.
	 */
	public void setReaderID(int readerID) {
		element.setReaderID(readerID);
	}

	/**
	 * Getter für das an der Leihe beteiligte Medium.
	 * 
	 * @return ID des an der Leihe beteiligten Mediums.
	 */
	public int getExemplarID() {
		return element.getExemplarID();
	}

	/**
	 * Setter für das an der Leihe beteiligte Medium.
	 * 
	 * @param mediumID
	 *            ID des an der Leihe beteiligten Mediums.
	 */
	public void setExemplarID(int exemplarID) {
		element.setExemplarID(exemplarID);
	}

	/**
	 * Getter für das Startdatum der Leihe.
	 * 
	 * @return Startdatum der Leihe.
	 */
	public Date getStart() {
		return element.getStart();
	}

	/**
	 * Setter für das Startdatum der Leihe
	 * 
	 * @param from
	 *            Startdatum der Leihe.
	 */
	public void setStart(Date start) {
		element.setStart(start);
	}

	/**
	 * Getter für das Enddatum der Leihe.
	 ** 
	 * @return Enddatum der Leihe.
	 */
	public Date getTill() {
		return (element.getTill());
	}

	/**
	 * Setter für das Enddatum der Leihe
	 * 
	 * @param till
	 *            Enddatum der Leihe.
	 */
	public void setTill(Date till) {
		element.setTill(till);
	}

	/**
	 * Gibt zurück, ob die Leihe vollständig beendet worden ist.
	 * 
	 * @return ob die Leihe vollständig beendet worden ist.
	 */
	public boolean getReturned() {
		return element.hasReturned();
	}

	/**
	 * Setter für den Wert, der angibt ob die Leihe vollständig beendet worden
	 * ist.
	 * 
	 * @param hasEndet
	 *            ob die Leihe vollständig beendet worden ist.
	 */
	public void setReturned(boolean returned) {
		element.setReturned(returned);
	}

	/**
	 * Getter für paid.
	 * 
	 * @return true, falls paid = true, sonst false
	 */
	public boolean getPaid() {
		return element.isPaid();
	}

	/**
	 * Setter für paid.
	 * 
	 * @param paid
	 *            bezahlt: true oder false
	 */
	public void setPaid(boolean paid) {
		element.setPaid(paid);
	}

	/**
	 * Getter für die Mahngebühr
	 * 
	 * @return die Mahngebühr
	 */
	public BigDecimal getFee() {
		return element.getFee();
	}

	/**
	 * Setter für die Mahngebühr
	 * 
	 * @param fee
	 *            die zu setzende Mahngebühr
	 */
	public void setFee(BigDecimal fee) {
		element.setFee(fee);
	}

	/**
	 * Getter für die Anzahl an Verlängerungen durch den Leser selbst.
	 * 
	 * @return die Anzahl an Verlängerungen
	 */
	public int getExtensions() {
		return element.getExtensions();
	}

	/**
	 * Getter für die Anzahl an Verlängerungen durch den Leser selbst.
	 * 
	 * @param die
	 *            Anzahl an Verlängerungen
	 */
	public void setExtensions(int extensions) {
		element.setExtensions(extensions);
	}

	/**
	 * Berechnet mithilfe des Mediumtypes das Enddatum für eine Leihe und gibt
	 * den nächsten offenen Tag ab diesem Datum zurück.
	 * 
	 * @author Bernd Poppinga
	 * @param die
	 *            ID der Leihe
	 * @return nächster offener Tag ab Enddatum der Leihe.
	 */

	public Date getLendingEnd(int id) {
		try {
			LendingHandler lh = LendingHandler.getInstance();
			MediumType type = lh.getMediumTypeByExemplarID(id);
			Date date = (new Date((new Date()).getTime()
					+ (1000 * 60 * 60 * 24 * type.getLendingTime())));
			return getNextOpenDay(date);
		} catch (DataSourceException e) {

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank", ""));
			return new Date();
		} catch (NoSuchBusinessObjectExistsException e) {

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Es konnte kein passendes Objekt gefunden werden"));
			return new Date();
		}
	}

	/**
	 * Berechnet mithilfe des Mediumtypes das Enddatum für eine Verlängerung und
	 * gibt den nächsten offenen Tag ab diesem Datum zurück.
	 * 
	 * @author Bernd Poppinga
	 * @param die
	 *            ID des Mediums
	 * @return nächster offener Tag ab Enddatum der Verlängerung.
	 */
	public Date getExtensionEnd(int id) {
		try {
			MediumHandler mh = MediumHandler.getInstance();
			Medium medium = mh.get(id);
			MediumTypeHandler mth = MediumTypeHandler.getInstance();
			MediumType type = mth.get(medium.getMediumType());
			Date date = (new Date((new Date()).getTime()
					+ (1000 * 60 * 60 * 24 * type.getLendingTime())));
			return getNextOpenDay(date);
		} catch (DataSourceException e) {

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank", ""));
			return new Date();
		} catch (NoSuchBusinessObjectExistsException e) {

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Es konnte kein passendes Objekt gefunden werden"));
			return new Date();
		}
	}

	/**
	 * Berechnet den nächsten offenen Tag ab dem übergebenen Datum
	 * 
	 * @author Bernd Poppinga
	 * @param date
	 *            Datum ab dem berechnet werden soll.
	 * @return Tag an dem die Bib offen hat.
	 */
	public Date getNextOpenDay(Date date) {
		try {

			OpeningTimeHandler oh = OpeningTimeHandler.getInstance();
			if (!oh.openDaysExist()) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
								"Die Bibliothek hat nie geöffnet."));
				return new Date();
			}
			ClosedTimeHandler ch = ClosedTimeHandler.getInstance();
			while (!(oh.isOpenDay(date) && !ch.isClosedDay(date))) {
				date.setTime(date.getTime() + (1000 * 60 * 60 * 24));
				logger.info(date);
			}
			return date;
		} catch (DataSourceException e) {

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank", ""));
			return new Date();
		} catch (NoSuchBusinessObjectExistsException e) {

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Es konnte kein passendes Objekt gefunden werden"));
			return new Date();
		}
	}

	/**
	 * Überprüft, ob die Bibliothek am Enddatum geschlossen ist und ob das
	 * Startdatum vor dem Enddatum der Ausleihe liegt.
	 * 
	 * @return true, falls die Start- und Enddatum der Leihe gültig sind, sonst
	 *         false.
	 */
	public boolean checkLending() {
		try {
			LendingHandler lh = LendingHandler.getInstance();
			if (!lh.isOpenDay(element.getTill())) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Achtung",
										"Die Auseihe endet an einem Tag, an dem die Bibliothek geschlossen hat."));
				return false;
			}
			if (!element.getTill().after(getStart())) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Achtung",
								"Die Ausleihe endet vor dem Startdatum."));
				return false;
			}
			return true;
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler beim Zugriff auf die Datenbank",
									"Die Daten der Ausleihe konnten nicht überprüft werden"));
			return false;
		} catch (NoSuchBusinessObjectExistsException e2) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Kein solches Objekt ist vorhanden"));
			return false;
		} catch (NullPointerException e3) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Enddatum könnte nicht überprüft werden"));
			return false;
		}
	}

}