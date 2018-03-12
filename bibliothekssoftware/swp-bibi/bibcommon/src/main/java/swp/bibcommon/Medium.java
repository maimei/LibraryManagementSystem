package swp.bibcommon;

import java.util.Date;

/**
 * Die Klasse Medium ist eine als Bean konzipierte Klasse, die die Grundlage für
 * alle Meidentypen der Bibliothek bietet.
 *
 * @author Eike Externest
 */
public class Medium extends BusinessObject {

    /**
     * Der Titel des Mediums.
     */
    private String title;

    /**
     * Der Untertitel des Mediums.
     */
    private String subtitle;

    /**
     * Der Veröffentlichungsort des Mediums.
     */
    private String location;

    /**
     * Der Preis des Mediums.
     */
    private double price;

    /**
     * Die Durchschnittliche Bewertung des Mediums.
     */
    private double rating;

    /**
     * Die Beschreibung des Mediums.
     */
    private String description;

    /**
     * Die Kategorien des Mediums.
     */
    private String category;

    /**
     * Die Sprache des Mediums.
     */
    private String language;

    /**
     * Das Erschenungsdatum des Mediums.
     */
    private Date dateOfPublication;

    /**
     * Der Typ des Mediums.
     */
    private int mediumType;

    /**
     * Die URL zur Bilddatei des Mediums.
     */
    private String imageURL;

    /**
     * Der Wert des ersten spezifischen Attributs.
     */
    private String attribute0;

    /**
     * Der Wert des zweiten spezifischen Attributs.
     */
    private String attribute1;

    /**
     * Der Wert des dritten spezifischen Attributs.
     */
    private String attribute2;

    /**
     * Der Wert des vierten spezifischen Attributs.
     */
    private String attribute3;

    /**
     * Der Wert des fünften spezifischen Attributs.
     */
    private String attribute4;

    /**
     * Der Wert des sechsten spezifischen Attributs.
     */
    private String attribute5;

    /**
     * Der Wert des siebten spezifischen Attributs.
     */
    private String attribute6;

    /**
     * Der Wert des achten spezifischen Attributs.
     */
    private String attribute7;

    /**
     * Der Wert des neunten spezifischen Attributs.
     */
    private String attribute8;

    /**
     * Der Wert des zehnten spezifischen Attributs.
     */
    private String attribute9;

    /**
     * Die Anzahl der Bewertungen.
     */
    private int ratingCount;

    /**
     * Leerer Konstruktor.
     */
    public Medium() {

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

        if (obj == null || !(obj instanceof Medium)) {
            return false;
        }

        Medium medium = (Medium) obj;
        return ((id == medium.id) && attribute0.equals(medium.attribute0)
                && attribute1.equals(medium.attribute1)
                && attribute2.equals(medium.attribute2)
                && attribute3.equals(medium.attribute3)
                && attribute4.equals(medium.attribute4)
                && attribute5.equals(medium.attribute5)
                && attribute6.equals(medium.attribute6)
                && attribute7.equals(medium.attribute7)
                && attribute8.equals(medium.attribute8)
                && attribute9.equals(medium.attribute9)
                && category.equals(medium.category)
                && dateOfPublication.equals(medium.dateOfPublication)
                && description.equals(medium.description)
                && imageURL.equals(medium.imageURL)
                && language.equals(medium.language)
                && location.equals(medium.location)
                && price == price && rating == medium.rating
                && ratingCount == medium.ratingCount && subtitle
                    .equals(medium.subtitle));
    }

    /**
     * Gibt den Wert des ersten spezifischen Attributs zurück.
     *
     * @return Der Wert des ersten spezifischen Attributs.
     */
    public final String getAttribute0() {
        return attribute0;
    }

    /**
     * Gibt den Wert des zweiten spezifischen Attributs zurück.
     *
     * @return Der Wert des zweiten spezifischen Attributs.
     */
    public final String getAttribute1() {
        return attribute1;
    }

    /**
     * Gibt den Wert des dritten spezifischen Attributs zurück.
     *
     * @return Der Wert des dritten spezifischen Attributs.
     */
    public final String getAttribute2() {
        return attribute2;
    }

    /**
     * Gibt den Wert des vierten spezifischen Attributs zurück.
     *
     * @return Der Wert des vierten spezifischen Attributs.
     */
    public final String getAttribute3() {
        return attribute3;
    }

    /**
     * Gibt den Wert des fünften spezifischen Attributs zurück.
     *
     * @return Der Wert des fünften spezifischen Attributs.
     */
    public final String getAttribute4() {
        return attribute4;
    }

    /**
     * Gibt den Wert des sechsten spezifischen Attributs zurück.
     *
     * @return Der Wert des sechsten spezifischen Attributs.
     */
    public final String getAttribute5() {
        return attribute5;
    }

    /**
     * Gibt den Wert des siebten spezifischen Attributs zurück.
     *
     * @return Der Wert des siebten spezifischen Attributs.
     */
    public final String getAttribute6() {
        return attribute6;
    }

    /**
     * Gibt den Wert des achten spezifischen Attributs zurück.
     *
     * @return Der Wert des achten spezifischen Attributs.
     */
    public final String getAttribute7() {
        return attribute7;
    }

    /**
     * Gibt den Wert des neunten spezifischen Attributs zurück.
     *
     * @return Der Wert des neunten spezifischen Attributs.
     */
    public final String getAttribute8() {
        return attribute8;
    }

    /**
     * Gibt den Wert des zehnten spezifischen Attributs zurück.
     *
     * @return Der Wert des zehnten spezifischen Attributs.
     */
    public final String getAttribute9() {
        return attribute9;
    }

    /**
     * Gibt die Kategorie des Mediums zurück.
     *
     * @return Die Kategorie.
     */
    public final String getCategory() {
        return category;
    }

    /**
     * Gibt das Erscheinungsdatum des Mediums zurück.
     *
     * @return Das Erscheinungsdatum.
     */
    public final Date getDateOfPublication() {
        return dateOfPublication;
    }

    /**
     * Gibt die Beschreibung des Mediums zurück.
     *
     * @return Die Beschreibung.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Gibt die URL zur Bilddatei des Mediums zurück.
     *
     * @return Die URL.
     */
    public final String getImageURL() {
        return imageURL;
    }

    /**
     * Gibt die Sprache des Mediums zurück.
     *
     * @return Die Sprache.
     */
    public final String getLanguage() {
        return language;
    }

    /**
     * Gibt den Standort des Mediums zurück.
     *
     * @return Der Standort.
     */
    public final String getLocation() {
        return location;
    }

    /**
     * Gibt die ID des Medientyps zurück.
     *
     * @return Die ID des Medientyps.
     */
    public final int getMediumType() {
        return mediumType;
    }

    /**
     * Gibt dem Preis des Mediums zurück.
     *
     * @return Der Preis.
     */
    public final double getPrice() {
        return price;
    }

    /**
     * Gibt die durchschnittliche Bewertung des Mediums zurück.
     *
     * @return Die durchschnittliche Bewertung.
     */
    public final double getRating() {
        return rating;
    }

    /**
     * Gibt die Anzahl der Bewertungen zurück.
     *
     * @return Die Anzahl der Bewertungen.
     */
    public final int getRatingCount() {
        return ratingCount;
    }

    /**
     * Gibt den Untertitel des Mediums zurück.
     *
     * @return Der Untertitel
     */
    public final String getSubtitle() {
        return subtitle;
    }

    /**
     * Gibt den Titel des Mediums zurück.
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
        result = prime * result + attribute0.hashCode();
        result = prime * result + attribute1.hashCode();
        result = prime * result + attribute2.hashCode();
        result = prime * result + attribute3.hashCode();
        result = prime * result + attribute4.hashCode();
        result = prime * result + attribute5.hashCode();
        result = prime * result + attribute6.hashCode();
        result = prime * result + attribute7.hashCode();
        result = prime * result + attribute8.hashCode();
        result = prime * result + attribute9.hashCode();
        result = prime * result + category.hashCode();
        result = prime * result + dateOfPublication.hashCode();
        result = prime * result + description.hashCode();
        result = prime * result + imageURL.hashCode();
        result = prime * result + language.hashCode();
        result = prime * result + location.hashCode();
        result = prime * result + mediumType;
        result = prime * result + (int) price;
        result = prime * result + (int) rating;
        result = prime * result + ratingCount;
        result = prime * result + subtitle.hashCode();
        result = prime * result + title.hashCode();
        return result;
    }

    /**
     * Setzt den Wert des ersten spezifischen Attributs.
     *
     * @param attribute0
     *            Der zu setzende Wert.
     */
    public final void setAttribute0(final String attribute0) {
        this.attribute0 = attribute0;
    }

    /**
     * Setzt den Wert des zweiten spezifischen Attributs.
     *
     * @param attribute1
     *            Der zu setzende Wert.
     */
    public final void setAttribute1(final String attribute1) {
        this.attribute1 = attribute1;
    }

    /**
     * Setzt den Wert des dritten spezifischen Attributs.
     *
     * @param attribute2
     *            Der zu setzende Wert.
     */
    public final void setAttribute2(final String attribute2) {
        this.attribute2 = attribute2;
    }

    /**
     * Setzt den Wert des vierten spezifischen Attributs.
     *
     * @param attribute3
     *            Der zu setzende Wert.
     */
    public final void setAttribute3(final String attribute3) {
        this.attribute3 = attribute3;
    }

    /**
     * Setzt den Wert des fünften spezifischen Attributs.
     *
     * @param attribute4
     *            Der zu setzende Wert.
     */
    public final void setAttribute4(final String attribute4) {
        this.attribute4 = attribute4;
    }

    /**
     * Setzt den Wert des sechsten spezifischen Attributs.
     *
     * @param attribute5
     *            Der zu setzende Wert.
     */
    public final void setAttribute5(final String attribute5) {
        this.attribute5 = attribute5;
    }

    /**
     * Setzt den Wert des siebten spezifischen Attributs.
     *
     * @param attribute6
     *            Der zu setzende Wert.
     */
    public final void setAttribute6(final String attribute6) {
        this.attribute6 = attribute6;
    }

    /**
     * Setzt den Wert des achten spezifischen Attributs.
     *
     * @param attribute7
     *            Der zu setzende Wert.
     */
    public final void setAttribute7(final String attribute7) {
        this.attribute7 = attribute7;
    }

    /**
     * Setzt den Wert des neunten spezifischen Attributs.
     *
     * @param attribute8
     *            Der zu setzende Wert.
     */
    public final void setAttribute8(final String attribute8) {
        this.attribute8 = attribute8;
    }

    /**
     * Setzt den Wert des zehnten spezifischen Attributs.
     *
     * @param attribute9
     *            Der zu setzende Wert.
     */
    public final void setAttribute9(final String attribute9) {
        this.attribute9 = attribute9;
    }

    /**
     * Setzt die Kategorie des Mediums.
     *
     * @param category
     *            Die zu setzende Kategorie.
     */
    public final void setCategory(final String category) {
        this.category = category;
    }

    /**
     * Setzt das Erscheinungsdatum des Mediums.
     *
     * @param dateOfPublication
     *            Das zu setzende Erscheinungsdatum.
     */
    public final void setDateOfPublication(final Date dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    /**
     * Setzt die Beschreibung des Mediums.
     *
     * @param description
     *            Die zu setzende Beschreibung.
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Setzt die URL zur Bilddatei des Mediums.
     *
     * @param imageURL
     *            Die zu setzende URL.
     */
    public final void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Setzt die Sprache des Mediums.
     *
     * @param language
     *            Die zu setzende Sprache.
     */
    public final void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Setzt den Standort des Mediums.
     *
     * @param location
     *            Der zu setzende Standort.
     */
    public final void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Setzt die ID des Medientyps.
     *
     * @param mediumType
     *            Die zu setzende ID.
     */
    public final void setMediumType(final int mediumType) {
        this.mediumType = mediumType;
    }

    /**
     * Setzt den Preis des Mediums.
     *
     * @param price
     *            Der zu setzende Preis.
     */
    public final void setPrice(final double price) {
        this.price = price;
    }

    /**
     * Setzt die durchschnittliche Bewertung des Mediums.
     *
     * @param rating
     *            Die zusetzende Bewertung.
     */
    public final void setRating(final double rating) {
        this.rating = rating;
    }

    /**
     * Setzt die Anzahl der Bewertungen.
     *
     * @param ratingCount
     *            Die zu setzende Anzahl.
     */
    public final void setRatingCount(final int ratingCount) {
        this.ratingCount = ratingCount;
    }

    /**
     * Setzt den Untertitel des Mediums.
     *
     * @param subtitle
     *            Der zu setzende Untertitel.
     */
    public final void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Setzt den Titel des Mediums.
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
        return "Medium [id=" + id + ", title=" + title + ", subtitle="
                + subtitle + ", location=" + location + ", price=" + price
                + ", rating=" + rating + ", description=" + description
                + ", category=" + category + ", language=" + language
                + ", dateOfPublication=" + dateOfPublication + ", mediumType="
                + mediumType + ", imageURL=" + imageURL + ", attribute0="
                + attribute0 + ", attribute1=" + attribute1 + ", attribute2="
                + attribute2 + ", attribute3=" + attribute3 + ", attribute4="
                + attribute4 + ", attribute5=" + attribute5 + ", attribute6="
                + attribute6 + ", attribute7=" + attribute7 + ", attribute8="
                + attribute8 + ", attribute9=" + attribute9 + ", ratingCount="
                + ratingCount + "]";
    }
}
