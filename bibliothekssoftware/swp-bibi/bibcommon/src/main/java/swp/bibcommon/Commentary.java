package swp.bibcommon;

import java.util.Date;

/**
 * Die Klasse Commentary ist als Bean konzipiert und repräsentiert ein Kommentar
 * im System.
 *
 * @author Eike Externest
 */
public class Commentary extends BusinessObject {

    /**
     * Das Veröffentlichungsdatum des Kommentars.
     */
    private Date dateOfPublication;

    /**
     * Der Text des Kommentars.
     */
    private String commentary;

    /**
     * Der Status des Kommentars.
     */
    private boolean active;

    /**
     * Die ID des Mediums.
     */
    private int mediumID;

    /**
     * Die ID des Verfassers.
     */
    private int readerID;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Commentary() {

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof Commentary)) {
            return false;
        }

        Commentary comm = (Commentary) other;
        return ((id == comm.id)
                && commentary.equals(comm.commentary)
                && dateOfPublication.equals(comm.dateOfPublication)
                && active == comm.active
                && mediumID == comm.mediumID
                && readerID == comm.readerID);
    }

    /**
     * Gibt den Text des Kommentars zurück.
     *
     * @return Der Text.
     */
    public final String getCommentary() {
        return commentary;
    }

    /**
     * Gibt das Veröffentlichungsdatum des Kommentars zurück.
     *
     * @return Das Veröffentlichungsdatum.
     */
    public final Date getDateOfPublication() {
        return dateOfPublication;
    }

    /**
     * Gibt die ID des kommentierten Mediums zurück.
     *
     * @return Die ID des Mediums.
     */
    public final int getMediumID() {
        return mediumID;
    }

    /**
     * Gibt die ID des Autors zurück.
     *
     * @return Die ID des Autors
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
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + commentary.hashCode();
        result = prime * result + dateOfPublication.hashCode();
        result = prime * result + mediumID;
        result = prime * result + readerID;
        return result;
    }

    /**
     * Gibt den Status des Kommentars zurück.
     *
     * @return Der Status.
     */
    public final boolean isActive() {
        return active;
    }

    /**
     * Setzt den Status des Kommentars.
     *
     * @param active
     *            Der zu setzende Status.
     */
    public final void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * Setzt den Text des Kommentars.
     *
     * @param commentary
     *            Der zu setzende Text.
     */
    public final void setCommentary(final String commentary) {
        this.commentary = commentary;
    }

    /**
     * Setzt das Veröffentlichungsdatum des Kommentars.
     *
     * @param dateOfPublication
     *            Das zu setzende Veröffentlichungsdatum.
     */
    public final void setDateOfPublication(final Date dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    /**
     * Setzt die ID des kommentierten Mediums.
     *
     * @param mediumID
     *            Die zu setzende ID.
     */
    public final void setMediumID(final int mediumID) {
        this.mediumID = mediumID;
    }

    /**
     * Setzt die ID des Autors.
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
        return "Commentary [id=" + id + "dateOfPublication="
                + dateOfPublication + ", commentary=" + commentary
                + ", active=" + active + ", mediumID=" + mediumID
                + ", readerID=" + readerID + "]";
    }
}
