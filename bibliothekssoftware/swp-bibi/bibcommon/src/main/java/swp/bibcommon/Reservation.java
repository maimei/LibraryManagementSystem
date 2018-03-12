package swp.bibcommon;

import java.util.Date;

/**
 * Die Klasse Reservation ist als Bean konzipiert und repräsentiert eine
 * Vormerkung im System.
 *
 * @author Eike Externest
 */
public class Reservation extends BusinessObject {

    /**
     * Die ID des reservierenden Ausleihers.
     */
    private int readerID;

    /**
     * Die ID des reservierten Mediums.
     */
    private int mediumID;

    /**
     * Das Datum der Reservierung.
     */
    private Date reservationDate;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Reservation() {

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

        if (obj == null || !(obj instanceof Reservation)) {
            return false;
        }

        Reservation res = (Reservation) obj;
        return ((id == res.id) && readerID == res.readerID
                && mediumID == res.mediumID && reservationDate
                    .equals(res.reservationDate));
    }

    /**
     * Gibt die ID des vorgemerkten Mediums zurück.
     *
     * @return Die ID des vorgemerkten Mediums.
     */
    public final int getMediumID() {
        return mediumID;
    }

    /**
     * Gibt die ID des vormerkenden Benutzers zurück.
     *
     * @return Die ID des vormerkenden Benutzers.
     */
    public final int getReaderID() {
        return readerID;
    }

    /**
     * Gibt das Datum der Vormerkung zurück.
     *
     * @return Das Datum der Vormerkung.
     */
    public final Date getReservationDate() {
        return reservationDate;
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
        result = prime * result + mediumID;
        result = prime * result + readerID;
        result = prime * result + reservationDate.hashCode();
        return result;
    }

    /**
     * Setzt die ID des vorgemerkten Mediums zurück.
     *
     * @param mediumID
     *            Die zu setzende ID des vorgemerkten Mediums.
     */
    public final void setMediumID(final int mediumID) {
        this.mediumID = mediumID;
    }

    /**
     * Setzt die ID des vormerkenden Benutzers.
     *
     * @param readerID
     *            Die ID des vormerkenden Benutzers.
     */
    public final void setReaderID(final int readerID) {
        this.readerID = readerID;
    }

    /**
     * Setzt das Datum der Vormerkung.
     *
     * @param reservationDate
     *            Das zu setzende Datum.
     */
    public final void setReservationDate(final Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Reservation [readerID=" + readerID + ", mediumID=" + mediumID
                + ", reservationDate=" + reservationDate + "]";
    }
}
