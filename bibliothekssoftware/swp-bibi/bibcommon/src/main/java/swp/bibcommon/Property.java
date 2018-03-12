package swp.bibcommon;

/**
 * Die Klasse Property ist als Bean konzipiert und repräsentiert eine
 * Einstellung im System.
 *
 * @author Eike Externest
 */
public class Property extends BusinessObject {

    /**
     * Der Name der Einstellung.
     */
    private String name;

    /**
     * Der Wert der Einstellung.
     */
    private String value;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Property() {

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Property)) {
            return false;
        }

        Property prop = (Property) obj;
        return ((id == prop.id) && name.equals(prop.name) && value
                .equals(prop.value));
    }

    /**
     * Gibt den Namen der Einstellung zurück.
     *
     * @return Der Name der Einstellung.
     */
    public final String getName() {
        return name;
    }

    /**
     * Gibt den Wert der Einstellung zurück.
     *
     * @return Der Wert der Einstellung.
     */
    public final String getValue() {
        return value;
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
        result = prime * result + name.hashCode();
        result = prime * result + value.hashCode();
        return result;
    }

    /**
     * Setzt den Namen der Einstellung.
     *
     * @param name
     *            Der zu setzende Name.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Setzt den Wert der Einstellung.
     *
     * @param value
     *            Der zu setzende Wert.
     */
    public final void setValue(final String value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Property [name=" + name + ", value=" + value + "]";
    }
}
