package swp.bibjsf.presentation;

import swp.bibcommon.Category;

/**
 * Abstrakte Oberklasse für Formen, die Kategorien hinzufügen oder ändern.
 * 
 * @author Bernd Poppinga
 */
public abstract class CategoryForm extends BusinessObjectForm<Category> {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 6826579728793183418L;

	/**
	 * Getter für den Namen der Kategorie.
	 * 
	 * @return Name der Kategorie.
	 */
	public String getName() {
		return element.getName();
	}

	public void setName(String name) {
		element.setName(name);
	}
}
