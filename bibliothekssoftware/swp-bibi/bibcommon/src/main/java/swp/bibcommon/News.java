package swp.bibcommon;

import java.util.Date;

/**
 * Die Klasse News ist als Bean konzipierte und repräsentiert eine Neuigkeit im
 * System.
 *
 * @author Eike Externest
 */
public class News extends BusinessObject {

    /**
     * Das Veröffentlichungsdatum der Neuigkeit.
     */
    private Date dateOfPublication;

    /**
     * Der Titel der Neuigkeit.
     */
    private String title;

    /**
     * Der Text der Neuigkeit.
     */
    private String content;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public News() {

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

        if (obj == null || !(obj instanceof News)) {
            return false;
        }

        News news = (News) obj;
        return ((id == news.id)
                && dateOfPublication.equals(news.dateOfPublication)
                && title.equals(news.title) && content.equals(news.content));
    }

    /**
     * Gibt den Inhalt der Neuigkeit zurück.
     *
     * @return Der Inhalt.
     */
    public final String getContent() {
        return content;
    }

    /**
     * Gibt das Veröffentlichungsdatum der Neuigkeit zurück.
     *
     * @return Das Veröffentlichungsdatum.
     */
    public final Date getDateOfPublication() {
        return dateOfPublication;
    }

    /**
     * Gibt den Titel der Neuigkeit zurück.
     *
     * @return Der Titel.
     */
    public final String getTitle() {
        return title;
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
        result = prime * result + content.hashCode();
        result = prime * result + dateOfPublication.hashCode();
        result = prime * result + title.hashCode();
        return result;
    }

    /**
     * Setzt den Inhalt der Neuigkeit.
     *
     * @param content
     *            Der zu setzende Inhalt.
     */
    public final void setContent(final String content) {
        this.content = content;
    }

    /**
     * Setzt das Veröffentlichungsdatum der Neuigkeit.
     *
     * @param dateOfPublication
     *            Das zu setzende Veröffentlichungsdatum.
     */
    public final void setDateOfPublication(final Date dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    /**
     * Setzt den Titel der Neuigkeit.
     *
     * @param title
     *            Der zu setzende Titel.
     */
    public final void setTitle(final String title) {
        this.title = title;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "News [id=" + id + ", dateOfPublication=" + dateOfPublication
                + ", title=" + title + ", content=" + content + "]";
    }
}
