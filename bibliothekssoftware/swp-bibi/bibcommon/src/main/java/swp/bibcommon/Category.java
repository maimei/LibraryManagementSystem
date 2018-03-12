package swp.bibcommon;

/**
 * Die Klasse Category ist als Bean konzipierte und repräsentiert eine Kategorie
 * im System.
 *
 * @author Eike Externest
 */

public class Category extends BusinessObject {

    /**
     * Der Name der Kategorie.
     */
    private String name;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Category() {

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
        if (obj == null || !(obj instanceof Category)) {
            return false;
        }

        Category category = (Category) obj;
        return ((id == category.id) && name.equals(category.name));
    }

    /**
     * Gibt den Namen der Kategorie zurück.
     *
     * @return Der Name.
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
        result = prime * result + name.hashCode();
        return result;
    }

    /**
     * Setzt den Namen der Kategorie.
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
        return "Category [id=" + id + ", name=" + name + "]";
    }
}
