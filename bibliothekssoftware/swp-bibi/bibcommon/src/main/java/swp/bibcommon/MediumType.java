package swp.bibcommon;

import java.math.BigDecimal;

/**
 * Die Klasse MediumType ist als Bean konzipiert und repräsentiert Mediumtypen
 * im System.
 *
 * @author Eike Externest
 */
public class MediumType extends BusinessObject {

    /**
     * Der Name des Medientyps.
     */
    private String name;

    /**
     * Die Ausleihzeit für Medien dieses Typs.
     */
    private int lendingTime;

    /**
     * Die Gebühren für Medien dieses Typs.
     */
    private BigDecimal fee;

    /**
     * Die Anzahl der möglichen Verlängerungen für Medien dieses Typs.
     */
    private int extensions;

    /**
     * Die Verlängerungszeit für Medien dieses Typs.
     */
    private int extensionTime;

    /**
     * Der Name des ersten spezifischen Attributs.
     */
    private String attribute0;

    /**
     * Der Name des zweiten spezifischen Attributs.
     */
    private String attribute1;

    /**
     * Der Name des dritten spezifischen Attributs.
     */
    private String attribute2;

    /**
     * Der Name des vierten spezifischen Attributs.
     */
    private String attribute3;

    /**
     * Der Name des fünften spezifischen Attributs.
     */
    private String attribute4;

    /**
     * Der Name des sechsten spezifischen Attributs.
     */
    private String attribute5;

    /**
     * Der Name des siebten spezifischen Attributs.
     */
    private String attribute6;

    /**
     * Der Name des achten spezifischen Attributs.
     */
    private String attribute7;

    /**
     * Der Name des neunten spezifischen Attributs.
     */
    private String attribute8;

    /**
     * Der Name des zehnten spezifischen Attributs.
     */
    private String attribute9;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public MediumType() {

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

        if (obj == null || !(obj instanceof MediumType)) {
            return false;
        }

        MediumType type = (MediumType) obj;
        return ((id == type.id) && attribute0.equals(type.attribute0)
                && attribute1.equals(type.attribute1)
                && attribute2.equals(type.attribute0)
                && attribute3.equals(type.attribute0)
                && attribute4.equals(type.attribute0)
                && attribute5.equals(type.attribute0)
                && attribute6.equals(type.attribute0)
                && attribute7.equals(type.attribute0)
                && attribute8.equals(type.attribute0)
                && attribute9.equals(type.attribute0)
                && extensionTime == type.extensionTime
                && extensions == type.extensions && fee.equals(type.fee)
                && lendingTime == type.lendingTime && name.equals(type.name));
    }

    /**
     * Gibt den Name des ersten spezifischen Attributs zurück.
     *
     * @return Der Name des ersten spezifischen Attributs.
     */
    public final String getAttribute0() {
        return attribute0;
    }

    /**
     * Gibt den Name des zweiten spezifischen Attributs zurück.
     *
     * @return Der Name des zweiten spezifischen Attributs.
     */
    public final String getAttribute1() {
        return attribute1;
    }

    /**
     * Gibt den Name des dritten spezifischen Attributs zurück.
     *
     * @return Der Name des dritten spezifischen Attributs.
     */
    public final String getAttribute2() {
        return attribute2;
    }

    /**
     * Gibt den Name des vierten spezifischen Attributs zurück.
     *
     * @return Der Name des vierten spezifischen Attributs.
     */
    public final String getAttribute3() {
        return attribute3;
    }

    /**
     * Gibt den Name des fünften spezifischen Attributs zurück.
     *
     * @return Der Name des fünften spezifischen Attributs.
     */
    public final String getAttribute4() {
        return attribute4;
    }

    /**
     * Gibt den Name des sechsten spezifischen Attributs zurück.
     *
     * @return Der Name des sechsten spezifischen Attributs.
     */
    public final String getAttribute5() {
        return attribute5;
    }

    /**
     * Gibt den Name des siebten spezifischen Attributs zurück.
     *
     * @return Der Name des siebten spezifischen Attributs.
     */
    public final String getAttribute6() {
        return attribute6;
    }

    /**
     * Gibt den Name des achten spezifischen Attributs zurück.
     *
     * @return Der Name des achten spezifischen Attributs.
     */
    public final String getAttribute7() {
        return attribute7;
    }

    /**
     * Gibt den Name des neunten spezifischen Attributs zurück.
     *
     * @return Der Name des neunten spezifischen Attributs.
     */
    public final String getAttribute8() {
        return attribute8;
    }

    /**
     * Gibt den Name des zehnten spezifischen Attributs zurück.
     *
     * @return Der Name des zehnten spezifischen Attributs.
     */
    public final String getAttribute9() {
        return attribute9;
    }

    /**
     * Gibt die Anzahl möglicher Verlängerungen zurück.
     *
     * @return Die Anzahl der möglichen Verlängerungen.
     */
    public final int getExtensions() {
        return extensions;
    }

    /**
     * Gibt die Verlängerungszeit des Medientyps zurück.
     *
     * @return Die Verlängerungszeit.
     */
    public final int getExtensionTime() {
        return extensionTime;
    }

    /**
     * Gibt die Gebühren des Medientyps zurück.
     *
     * @return Die Gebühren.
     */
    public final BigDecimal getFee() {
        return fee;
    }

    /**
     * Gibt die Ausleihzeit des Medientyps zurück.
     *
     * @return Die Ausleihzeit.
     */
    public final int getLendingTime() {
        return lendingTime;
    }

    /**
     * Gibt den Namen des Medientyps zurück.
     *
     * @return Der Name des Medientyps.
     */
    public final String getName() {
        return name;
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
        result = prime * result + extensionTime;
        result = prime * result + extensions;
        result = prime * result + fee.hashCode();
        result = prime * result + lendingTime;
        result = prime * result + name.hashCode();
        return result;
    }

    /**
     * Setzt den Name des ersten spezifischen Attributs.
     *
     * @param attribute0
     *            Der zu setzende Name.
     */
    public final void setAttribute0(final String attribute0) {
        this.attribute0 = attribute0;
    }

    /**
     * Setzt den Name des zweiten spezifischen Attributs.
     *
     * @param attribute1
     *            Der zu setzende Name.
     */
    public final void setAttribute1(final String attribute1) {
        this.attribute1 = attribute1;
    }

    /**
     * Setzt den Name des dritten spezifischen Attributs.
     *
     * @param attribute2
     *            Der zu setzende Name.
     */
    public final void setAttribute2(final String attribute2) {
        this.attribute2 = attribute2;
    }

    /**
     * Setzt den Name des vierten spezifischen Attributs.
     *
     * @param attribute3
     *            Der zu setzende Name.
     */
    public final void setAttribute3(final String attribute3) {
        this.attribute3 = attribute3;
    }

    /**
     * Setzt den Name des fünften spezifischen Attributs.
     *
     * @param attribute4
     *            Der zu setzende Name.
     */
    public final void setAttribute4(final String attribute4) {
        this.attribute4 = attribute4;
    }

    /**
     * Setzt den Name des sechsten spezifischen Attributs.
     *
     * @param attribute5
     *            Der zu setzende Name.
     */
    public final void setAttribute5(final String attribute5) {
        this.attribute5 = attribute5;
    }

    /**
     * Setzt den Name des siebten spezifischen Attributs.
     *
     * @param attribute6
     *            Der zu setzende Name.
     */
    public final void setAttribute6(final String attribute6) {
        this.attribute6 = attribute6;
    }

    /**
     * Setzt den Name des achten spezifischen Attributs.
     *
     * @param attribute7
     *            Der zu setzende Name.
     */
    public final void setAttribute7(final String attribute7) {
        this.attribute7 = attribute7;
    }

    /**
     * Setzt den Name des neunten spezifischen Attributs.
     *
     * @param attribute8
     *            Der zu setzende Name.
     */
    public final void setAttribute8(final String attribute8) {
        this.attribute8 = attribute8;
    }

    /**
     * Setzt den Name des zehnten spezifischen Attributs.
     *
     * @param attribute9
     *            Der zu setzende Name.
     */
    public final void setAttribute9(final String attribute9) {
        this.attribute9 = attribute9;
    }

    /**
     * Setzt die Anzahl möglicher Verlängerungen.
     *
     * @param extensions
     *            Die zu setzende Anzahl.
     */
    public final void setExtensions(final int extensions) {
        this.extensions = extensions;
    }

    /**
     * Setzt die Verlängerungszeit des Medientyps.
     *
     * @param extensionTime
     *            Die zu setzende Verlängerungszeit.
     */
    public final void setExtensionTime(final int extensionTime) {
        this.extensionTime = extensionTime;
    }

    /**
     * Setzt die Gebühren des Medientyps.
     *
     * @param fee
     *            Die zu setzenden Gebühren.
     */
    public final void setFee(final BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * Setzt die Ausleihzeit des Medientyps.
     *
     * @param lendingTime
     *            Die zu setzende Ausleihzeit.
     */
    public final void setLendingTime(final int lendingTime) {
        this.lendingTime = lendingTime;
    }

    /**
     * Setzt den Namen des Medientyps.
     *
     * @param name
     *            Der zu setzende Name.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "MediumType [id=" + id + ", name=" + name + ", lendingTime="
                + lendingTime + ", fee=" + fee + ", extensions=" + extensions
                + ", extensionTime=" + extensionTime + ", attribute0="
                + attribute0 + ", attribute1=" + attribute1 + ", attribute2="
                + attribute2 + ", attribute3=" + attribute3 + ", attribute4="
                + attribute4 + ", attribute5=" + attribute5 + ", attribute6="
                + attribute6 + ", attribute7=" + attribute7 + ", attribute8="
                + attribute8 + ", attribute9=" + attribute9 + "]";
    }
}
