package swp.bibjsf.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.ReservationHandler;
import swp.bibjsf.utils.Constraint;

/**
 * @author Mathias Eggerichs, Hauke Olf
 */
public abstract class MediumForm extends BusinessObjectForm<Medium> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 3949684770083455670L;

	/**
	 * Language of a book. A two-letter ISO 639-1 code such as 'fr', 'en', etc.
	 */
	private String[] languages = { "de", "en", "fr", "es", "it", "la", "nl",
			"pl", "ru", "tr" };

	/**
	 * Getter for title.
	 * 
	 * @return title.
	 */
	public String getTitle() {
		return element.getTitle();
	}

	/**
	 * Setter for title.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		element.setTitle(title);
	}

	/**
	 * Getter for subtitle.
	 * 
	 * @return subtitle.
	 */
	public String getSubtitle() {
		return element.getSubtitle();
	}

	/**
	 * Setter for subtitle.
	 * 
	 * @param subtitle
	 *            .
	 */
	public void setSubtitle(String subtitle) {
		element.setSubtitle(subtitle);
	}

	/**
	 * Getter for categories.
	 * 
	 * @return String of categories.
	 */
	public String getCategory() {
		return element.getCategory();
	}

	/**
	 * Setter for categories.
	 * 
	 * @param string
	 *            new categories.
	 */
	public void setCategory(String category) {
		element.setCategory(category);
	}

	/**
	 * Getter for categories.
	 * 
	 * @return String of categories.
	 */
	public String[] getCat() {
		String cat = element.getCategory();
		if (cat != null)
			return cat.split(", ");
		else
			return new String[0];
	}

	/**
	 * Setter for categories.
	 * 
	 * @param string
	 *            new categories.
	 */
	public void setCat(String[] category) {
		String categories = "";
		if (category != null && category.length > 0) {
			categories = category[0];
			for (int i = 1; i < category.length; i++) {
				categories += ", " + category[i];
			}
		}
		element.setCategory(categories);
	}

	/**
	 * Getter for Date of Publication.
	 * 
	 * @return String of Date.
	 */
	public Date getDateOfPublication() {
		return element.getDateOfPublication();
	}

	/**
	 * Setter for Date of Publication.
	 * 
	 * @param Date
	 *            new date of publication.
	 */
	public void setDateOfPublication(Date date) {
		element.setDateOfPublication(date);
	}

	/**
	 * Getter for description.
	 * 
	 * @return String of description.
	 */
	public String getDescription() {
		return element.getDescription();
	}

	/**
	 * Setter for description.
	 * 
	 * @param String
	 *            new description.
	 */
	public void setDescription(String description) {
		element.setDescription(description);
	}

	/**
	 * Getter for language.
	 * 
	 * @return String language.
	 */
	public String getLanguage() {
		return element.getLanguage();
	}

	/**
	 * Setter for language.
	 * 
	 * @param subtitle
	 *            new language.
	 */
	public void setLanguage(String language) {
		element.setLanguage(language);
	}

	/**
	 * Getter for location.
	 * 
	 * @return String location.
	 */
	public String getLocation() {
		return element.getLocation();
	}

	/**
	 * Setter for location.
	 * 
	 * @param String
	 *            new location.
	 */
	public void setLocation(String location) {
		element.setLocation(location);
	}

	/**
	 * Getter for price.
	 * 
	 * @return BigDecimal price.
	 */
	public double getPrice() {
		return element.getPrice();
	}

	/**
	 * Setter for price.
	 * 
	 * @param BigDecimal
	 *            new price.
	 */
	public void setPrice(double price) {
		element.setPrice(price);
	}

	/**
	 * Getter for Rating.
	 * 
	 * @return average rating.
	 */
	public double getRating() {
		return element.getRating();
	}

	/**
	 * Setter for Rating.
	 * 
	 * @param rating
	 *            new rating.
	 */
	public void setRating(double rating) {
		element.setRating(rating);
	}

	/**
	 * Gibt die möglichen Sprachen eines Mediums zurück.
	 * 
	 * @return die Sprache.
	 */
	public String[] getLanguages() {
		return languages.clone();
	}

	/**
	 * Setzt die möglichen Sprachen eines Mediums.
	 * 
	 * @param languages
	 *            mögliche Sprachen.
	 */
	public void setLanguages(String[] languages) {
		this.languages = languages.clone();
	}

	/**
	 * Getter für den Medientyp.
	 * 
	 * @return the mediumType
	 */
	public int getMediumType() {
		return element.getMediumType();
	}

	/**
	 * Setter für den Medientyp
	 * 
	 * @param mediumType
	 *            the mediumType to set
	 */
	public void setMediumType(int mediumType) {
		element.setMediumType(mediumType);
	}

	/**
	 * Gibt die URL zur Bilddatei des Mediums zurück.
	 * 
	 * @return Die URL.
	 */
	public String getImageURL() {
		return element.getImageURL();
	}

	/**
	 * Setzt die URL zur Bilddatei des Mediums.
	 * 
	 * @param imageURL
	 *            Die zu setzende URL.
	 */
	public void setImageURL(String imageURL) {
		element.setImageURL(imageURL);
	}

	/**
	 * Gibt die Anzahl der Bewertungen zurück.
	 * 
	 * @return Die Anzahl der Bewertungen.
	 */
	public int getRatingCount() {
		return element.getRatingCount();
	}

	/**
	 * Setzt die Anzahl der Bewertungen.
	 * 
	 * @param ratingCount
	 *            Die zu setzende Anzahl.
	 */
	public void setRatingCount(int ratingCount) {
		element.setRatingCount(ratingCount);
	}

	/**
	 * @return the attribute0
	 */
	public String getAttribute0() {
		return element.getAttribute0();
	}

	/**
	 * @param attribute0
	 *            the attribute0 to set
	 */
	public void setAttribute0(String attribute0) {
		element.setAttribute0(attribute0);
	}

	/**
	 * @return the attribute1
	 */
	public String getAttribute1() {
		return element.getAttribute1();
	}

	/**
	 * @param attribute1
	 *            the attribute1 to set
	 */
	public void setAttribute1(String attribute1) {
		element.setAttribute1(attribute1);
	}

	/**
	 * @return the attribute2
	 */
	public String getAttribute2() {
		return element.getAttribute2();
	}

	/**
	 * @param attribute2
	 *            the attribute2 to set
	 */
	public void setAttribute2(String attribute2) {
		element.setAttribute2(attribute2);
	}

	/**
	 * @return the attribute3
	 */
	public String getAttribute3() {
		return element.getAttribute3();
	}

	/**
	 * @param attribute3
	 *            the attribute3 to set
	 */
	public void setAttribute3(String attribute3) {
		element.setAttribute3(attribute3);
	}

	/**
	 * @return the attribute4
	 */
	public String getAttribute4() {
		return element.getAttribute4();
	}

	/**
	 * @param attribute4
	 *            the attribute4 to set
	 */
	public void setAttribute4(String attribute4) {
		element.setAttribute4(attribute4);
	}

	/**
	 * @return the attribute5
	 */
	public String getAttribute5() {
		return element.getAttribute5();
	}

	/**
	 * @param attribute5
	 *            the attribute5 to set
	 */
	public void setAttribute5(String attribute5) {
		element.setAttribute5(attribute5);
	}

	/**
	 * @return the attribute6
	 */
	public String getAttribute6() {
		return element.getAttribute6();
	}

	/**
	 * @param attribute6
	 *            the attribute6 to set
	 */
	public void setAttribute6(String attribute6) {
		element.setAttribute6(attribute6);
	}

	/**
	 * @return the attribute7
	 */
	public String getAttribute7() {
		return element.getAttribute7();
	}

	/**
	 * @param attribute7
	 *            the attribute7 to set
	 */
	public void setAttribute7(String attribute7) {
		element.setAttribute7(attribute7);
	}

	/**
	 * @return the attribute8
	 */
	public String getAttribute8() {
		return element.getAttribute8();
	}

	/**
	 * @param attribute8
	 *            the attribute8 to set
	 */
	public void setAttribute8(String attribute8) {
		element.setAttribute8(attribute8);
	}

	/**
	 * @return the attribute9
	 */
	public String getAttribute9() {
		return element.getAttribute9();
	}

	/**
	 * @param attribute9
	 *            the attribute9 to set
	 */
	public void setAttribute9(String attribute9) {
		element.setAttribute9(attribute9);
	}

	public String getReservationCount() {
		String out = "";
		try {
			ReservationHandler rh = ReservationHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("mediumID", "=", "" + getId(), "AND",
					Constraint.AttributeType.INTEGER));
			out += rh.getNumber(cons);
		} catch (Exception e) {
			logger.debug(e);
		}
		return out;
	}
}