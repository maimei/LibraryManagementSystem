package swp.bibcommon;

import java.util.Date;

/**
 * Die Klasse Extension ist als Bean konzipiert und repräsentiert eine
 * Verlängerung im System.
 *
 * @author Eike Externest
 */
public class Extension extends BusinessObject {

    /**
     * Die ID des Benutzers.
     */
    private int readerID;

    /**
     * Die ID des Exemplars.
     */
    private int exemplarID;

    /**
     * Das Eingangsdatum des Verlängerungswunsches.
     */
    private Date extensionDate;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Extension() {

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
        if (obj == null || !(obj instanceof Extension)) {
            return false;
        }

        Extension extension = (Extension) obj;
        return ((id == extension.id) && readerID == extension.readerID
                && exemplarID == extension.exemplarID && extensionDate
                    .equals(extension.extensionDate));
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
     * Gibt das Datum des Verlängerungswunsches zurück.
     *
     * @return Das Datum des Verlängerungswunsches.
     */
    public final Date getExtensionDate() {
        return extensionDate;
    }

    /**
     * Gibt die ID des Benutzers zurück.
     *
     * @return Die ID des Benutzers.
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
        result = prime * result + exemplarID;
        result = prime * result + extensionDate.hashCode();
        result = prime * result + readerID;
        return result;
    }

    /**
     * Setzt die ID des Mediums.
     *
     * @param exemplarID
     *            Die zu setzende ID des Mediums.
     */
    public final void setExemplarID(final int exemplarID) {
        this.exemplarID = exemplarID;
    }

    /**
     * Setzt das Datum des Verlängerungswunsches.
     *
     * @param extensionDate
     *            Das zu setzende Datum.
     */
    public final void setExtensionDate(final Date extensionDate) {
        this.extensionDate = extensionDate;
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
        return "Extension [id=" + id + ", readerID=" + readerID
                + ", exemplarID=" + exemplarID + ", extensionDate="
                + extensionDate + "]";
    }
}
