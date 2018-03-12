package swp.bibjsf.presentation;

import swp.bibcommon.Property;

/**
 *  Abstrakte Oberklasse für die ChangePropertyForm.
 *  
 * @author Helena Meißner
 */
public abstract class PropertyForm extends BusinessObjectForm<Property> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 3502738193886136660L;

	/**
	 * Gibt den Namen der Einstellung zurück.
	 * name
	 * @return Der Name der Einstellung.
	 */
	public String getName() {
		return element.getName();
	}
	
	/**
	 * Setzt den Namen der Einstellung.
	 * 
	 * @param name Der zu setzende Name.
	 */
	public void setName(String name) {
		element.setName(name);
	}
	
	/**
	 * Gibt den Wert der Einstellung zurück.
	 * 
	 * @return Der Wert der Einstellung.
	 */
	public String getValue() {
		return element.getValue();
	}
	
	/**
	 * Setzt den Wert der Einstellung.
	 * 
	 * @param value Der zu setzende Wert.
	 */
	public void setValue(String value) {
		element.getValue();
	}
}
