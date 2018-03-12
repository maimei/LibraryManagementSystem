/**
 * 
 */
package swp.bibcommon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Die Klasse Lending ist als Bean konzipiert und repräsentiert eine Ausleihe im
 * System.
 * 
 * @author Eike Externest
 */
public class Lending extends BusinessObject implements Serializable {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 5174559719352910585L;

	/**
	 * Die ID des ausgeliehenen Mediums.
	 */
	private int exemplarID;

	/**
	 * Die ID des Leihers.
	 */
	private int readerID;

	/**
	 * Das Start-Datum der Ausleihe.
	 */
	private Date start;

	/**
	 * Das End-Datum der Ausleihe.
	 */
	private Date till;

	/**
	 * Gibt an, ob die Ausleihe zurückgegeben wurde.
	 */
	private boolean returned;

	/**
	 * Gibt an, ob ggf. anfallende Gebühren bezahl wurde.
	 */
	private boolean paid;

	/**
	 * Die Gebühren für die Ausleihe.
	 */
	private BigDecimal fee;

	/**
	 * Die Anzahl der Verlängerungen.
	 */
	private int extensions;

	/**
	 * Leerer Konstruktor für DBUtils.
	 */
	public Lending() {

	}

	/**
	 * Gibt die ID des Mediums zurück.
	 * 
	 * @return Die ID des Mediums.
	 */
	public final int getExemplarID() {
		return exemplarID;
	}

	/**
	 * Setzt die ID des Mediums.
	 * 
	 * @param mediumID
	 *            die zu setzende ID.
	 */
	public final void setExemplarID(final int exemplarID) {
		this.exemplarID = exemplarID;
	}

	/**
	 * Gibt die ID des Ausleihers zurück.
	 * 
	 * @return Die ID des Ausleihers.
	 */
	public final int getReaderID() {
		return readerID;
	}

	/**
	 * Setzt die ID des Ausleihers.
	 * 
	 * @param readerID
	 *            Die zu setzende ID.
	 */
	public final void setReaderID(final int readerID) {
		this.readerID = readerID;
	}

	/**
	 * Gibt das Startdatum der Ausleihe zurück.
	 * 
	 * @return Das Startdatum.
	 */
	public final Date getStart() {
		return start;
	}

	/**
	 * Setzt das Startdatum der Ausleihe.
	 * 
	 * @param from
	 *            Das zu setzende Startdatum.
	 */
	public final void setStart(final Date start) {
		this.start = start;
	}

	/**
	 * Gibt das Enddatum der Ausleihe zurück.
	 * 
	 * @return Das Enddatum.
	 */
	public final Date getTill() {
		return till;
	}

	/**
	 * Setzt das Enddatum der Ausleihe.
	 * 
	 * @param till
	 *            Das zu setzende Enddatum.
	 */
	public final void setTill(final Date till) {
		this.till = till;
	}

	/**
	 * Gibt zurück, ob die Ausleihe beendet ist.
	 * 
	 * @return Der Status der Ausleihe.
	 */
	public final boolean hasReturned() {
		return returned;
	}

	/**
	 * Setzt den Status der Ausleihe.
	 * 
	 * @param returned
	 *            Der zu setzende Status.
	 */
	public final void setReturned(boolean returned) {
		this.returned = returned;
	}

	/**
	 * Gibt zurück, ob die Gebühren bezahlt wurden.
	 * 
	 * @return Wurden die Gebühren bezahlt?
	 */
	public final boolean isPaid() {
		return paid;
	}

	/**
	 * Setzt den Bezahlzustand der Gebühren.
	 * 
	 * @param paid
	 *            Der zu setzende Zustand.
	 */
	public final void setPaid(boolean paid) {
		this.paid = paid;
	}

	/**
	 * Gibt die angefallenen Gebühren zurück.
	 * 
	 * @return Die Gebühren.
	 */
	public final BigDecimal getFee() {
		return fee;
	}

	/**
	 * Setzt die angefallenen Gebühren.
	 * 
	 * @param fee
	 *            Die zu setzenden Gebühren.
	 */
	public final void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	/**
	 * Gibt die Anzahl der Verlängerungen zurück.
	 * 
	 * @return Die Anzahl der Verlängerungen.
	 */
	public final int getExtensions() {
		return extensions;
	}

	/**
	 * Setzt die Anzahl der Verlängerungen.
	 * 
	 * @param extensions
	 *            Die zu setzende Anzahl.
	 */
	public final void setExtensions(int extensions) {
		this.extensions = extensions;
	}
}