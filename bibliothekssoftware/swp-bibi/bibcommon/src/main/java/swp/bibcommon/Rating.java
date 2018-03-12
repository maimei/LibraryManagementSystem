package swp.bibcommon;

/**
 * Die Klasse Rating ist als Bean konzipiert und repräsentiert ein Bewertung im
 * System.
 *
 * @author Eike Externest
 */
public class Rating extends BusinessObject {

    /**
     * Die ID des Mediums.
     */
    private int mediumID;

    /**
     * Die ID des Benutzers.
     */
    private int readerID;

    /**
     * Die Bewertung.
     */
    private int rating;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Rating() {

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

        if (obj == null || !(obj instanceof Rating)) {
            return false;
        }

        Rating rate = (Rating) obj;
        return ((id == rate.id) && mediumID == rate.mediumID
                && readerID == rate.readerID && rating == rate.getRating());
    }

    /**
     * Gibt die ID des bewerteten Mediums zurück.
     *
     * @return Die ID des Mediums.
     */
    public final int getMediumID() {
        return mediumID;
    }

    /**
     * Gibt die Bewertung zurück.
     *
     * @return Die Bewertung.
     */
    public final int getRating() {
        return rating;
    }

    /**
     * Gibt die ID des Benutzers zurück.
     *
     * @return Die ID des Nutzers.
     */
    public final int getReaderID() {
        return readerID;
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
        result = prime * result + rating;
        result = prime * result + readerID;
        return result;
    }

    /**
     * Setzt die ID des bewerteten Mediums.
     *
     * @param mediumID
     *            Die zu setzende ID.
     */
    public final void setMediumID(final int mediumID) {
        this.mediumID = mediumID;
    }

    /**
     * Setzt die Bewertung.
     *
     * @param rating
     *            Die zu setzende Bewertung.
     */
    public final void setRating(final int rating) {
        this.rating = rating;
    }

    /**
     * Setzt die ID des Benutzers.
     *
     * @param readerID
     *            Die zu setzende ID.
     */
    public final void setReaderID(final int readerID) {
        this.readerID = readerID;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Rating [mediumID=" + mediumID + ", readerID=" + readerID
                + ", rating=" + rating + "]";
    }
}
