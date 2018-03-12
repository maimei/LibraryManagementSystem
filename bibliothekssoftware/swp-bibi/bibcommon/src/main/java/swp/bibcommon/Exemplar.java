package swp.bibcommon;

import java.util.Date;

/**
 * Die Klasse Exemplar ist als Bean konzipiert und repräsentiert ein Exemplar
 * eines Mediums im System.
 *
 * @author Eike Externest, Helena Meißner
 */
public class Exemplar extends BusinessObject {

    /**
     * Der Ausleihstatus des Exemplars.
     */
    private String status;

    /**
     * Die ID des letzten Leihers.
     */
    private int latestReader;

    /**
     * Ein Kommentar des Bibliothekars.
     */
    private String note;

    /**
     * Der Ort, an dem sich das Exemplar in der Bibliothek befindet.
     */
    private String place;

    /**
     * Das Aufnahmedatum des Exemplars.
     */
    private Date dateOfAddition;

    /**
     * Die Ausleihhäufigkeit des Exemplars.
     */
    private int lendingCount;

    /**
     * Die ID des Mediums, zu dem das Exemplar gehört.
     */
    private int mediumID;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Exemplar() {

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Exemplar)) {
            return false;
        }

        Exemplar exemplar = (Exemplar) obj;
        return ((id == exemplar.id) && latestReader == exemplar.latestReader
                && dateOfAddition.equals(exemplar.dateOfAddition)
                && note.equals(exemplar.note) && status.equals(exemplar.status)
                && lendingCount == exemplar.lendingCount
                && mediumID == exemplar.mediumID && place
                    .equals(exemplar.place));
    }

    /**
     * Gibt das Aufnahmedatum des Exemplars zurück.
     *
     * @return Das Aufnahmedatum.
     */
    public final Date getDateOfAddition() {
        return dateOfAddition;
    }

    /**
     * Gibt die ID des letztens Ausleihers des Exemplars zurück.
     *
     * @return the latestReader
     */
    public final int getLatestReader() {
        return latestReader;
    }

    /**
     * Gibt die Ausleihhäufigkeit des Exemplars zurück.
     *
     * @return Die Ausleihhäufigkeit.
     */
    public final int getLendingCount() {
        return lendingCount;
    }

    /**
     * Gibt die ID des Mediums zurück.
     *
     * @return Die ID des Mediums.
     */
    public final int getMediumID() {
        return mediumID;
    }

    /**
     * Gibt die Notiz des Exemplars zurück.
     *
     * @return Die Notiz.
     */
    public final String getNote() {
        return note;
    }

    /**
     * Gibt den Aufbewahrungsort des Exemplars zurück.
     *
     * @return Der Aufbewahrungsort.
     */
    public final String getPlace() {
        return place;
    }

    /**
     * Gibt den Status des Exemplars zurück.
     *
     * @return Der Status.
     */
    public final String getStatus() {
        return status;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dateOfAddition.hashCode();
        result = prime * result + latestReader;
        result = prime * result + lendingCount;
        result = prime * result + mediumID;
        result = prime * result + note.hashCode();
        result = prime * result + place.hashCode();
        result = prime * result + status.hashCode();
        return result;
    }

    /**
     * Setzt das Aufnahmedatum des Exemplars.
     *
     * @param dateOfAddition
     *            Das zu setzende Aufnahmedatum.
     */
    public final void setDateOfAddition(final Date dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    /**
     * Setzt die ID des letzten Ausleihers des Exemplars.
     *
     * @param latestReader
     *            Der zu setzende letzte Ausleiher.
     */
    public final void setLatestReader(final int latestReader) {
        this.latestReader = latestReader;
    }

    /**
     * Setzt die Ausleihhäufigkeit des Exemplars.
     *
     * @param lendingCount
     *            Die zu setzende Ausleihhäufigkeit.
     */
    public final void setLendingCount(final int lendingCount) {
        this.lendingCount = lendingCount;
    }

    /**
     * Setzt die ID des Mediums.
     *
     * @param mediumID
     *            Die zu setzende ID.
     */
    public final void setMediumID(final int mediumID) {
        this.mediumID = mediumID;
    }

    /**
     * Setzt die Notiz des Exemplars.
     *
     * @param note
     *            Die zu setzende Notiz.
     */
    public final void setNote(final String note) {
        this.note = note;
    }

    /**
     * Setzt den Aufbewahrungsort des Exemplars.
     *
     * @param place
     *            Der zu setzende Ort.
     */
    public final void setPlace(final String place) {
        this.place = place;
    }

    /**
     * Setzt den Status des Exemplars.
     *
     * @param status
     *            Der zu setzende Status.
     */
    public final void setStatus(final String status) {
        this.status = status;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Exemplar [id=" + id + ", status=" + status + ", latestReader="
                + latestReader + ", note=" + note + ", place=" + place
                + ", dateOfAddition=" + dateOfAddition + ", lendingCount="
                + lendingCount + ", mediumID=" + mediumID + "]";
    }
}
