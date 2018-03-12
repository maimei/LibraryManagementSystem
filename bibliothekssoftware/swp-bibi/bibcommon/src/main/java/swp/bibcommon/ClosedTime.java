package swp.bibcommon;

import java.util.Date;

/**
 * Die Klasse ClosedTime ist als Bean konzipiert und repräsentiert
 * Schließungszeiten im System.
 *
 * @author Helena Meißner, Eike Externest
 */
public class ClosedTime extends BusinessObject {

    /**
     * Das Startdatum der Schließungszeit.
     */
    private Date start;

    /**
     * Das Enddatum der Schießungszeit.
     */
    private Date till;

    /**
     * Der Anlass der vorrübergehenden Schließung.
     */
    private String occasion;

    /**
     * Leerer Konstrutkor für DBUtils.
     */
    public ClosedTime() {

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
        if (obj == null || !(obj instanceof ClosedTime)) {
            return false;
        }

        ClosedTime close = (ClosedTime) obj;
        return ((id == close.id)
                && start.equals(close.start)
                && till.equals(close.till)
                && occasion.equals(close.occasion));
    }

    /**
     * Gibt den Anlass der Schließung zurück.
     *
     * @return Der Anlass.
     */
    public final String getOccasion() {
        return occasion;
    }

    /**
     * Gibt das Startdatum der Schließung zurück.
     *
     * @return Das Startdatum.
     */
    public final Date getStart() {
        return start;
    }

    /**
     * Gibt das Enddatum der Schließung zurück.
     *
     * @return Das Enddatum.
     */
    public final Date getTill() {
        return till;
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
        result = prime * result + occasion.hashCode();
        result = prime * result + start.hashCode();
        result = prime * result + till.hashCode();
        return result;
    }

    /**
     * Setzt den Anlass der Schließung.
     *
     * @param occasion
     *            Der zu setzende Anlass.
     */
    public final void setOccasion(final String occasion) {
        this.occasion = occasion;
    }

    /**
     * Setzt das Startdatum der Schließung.
     *
     * @param start
     *            Das zu setzende Startdatum.
     */
    public final void setStart(final Date start) {
        this.start = start;
    }

    /**
     * Setzt das Enddatum der Schließung.
     *
     * @param till
     *            Das zu setzende Enddatum.
     */
    public final void setTill(final Date till) {
        this.till = till;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "ClosedTime [id=" + id + ", start=" + start + ", till=" + till
                + ", occasion=" + occasion + "]";
    }
}
