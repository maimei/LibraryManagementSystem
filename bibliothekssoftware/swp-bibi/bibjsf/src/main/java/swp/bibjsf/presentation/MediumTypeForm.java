package swp.bibjsf.presentation;

import java.math.BigDecimal;

import swp.bibcommon.MediumType;

/**
 * Abstrakte Oberklasse für Formen, die Medientypen hinzufügen oder ändern.
 * 
 * @author Helena Meißner
 */
public abstract class MediumTypeForm extends BusinessObjectForm<MediumType> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -2821954354996224963L;

	/**
	 * Getter für den Namen des Medientyps.
	 * 
	 * @return der Name
	 */
	public String getName() {
		return element.getName();
	}

	/**
	 * Setter für den Namen des Medientyps.
	 * 
	 * @param name
	 *            der Name
	 */
	public void setName(String name) {
		element.setName(name);
	}

	/**
	 * Getter für die Standardausleihdauer.
	 * 
	 * @return die Standardausleihdauer
	 */
	public int getLendingTime() {
		return element.getLendingTime();
	}

	/**
	 * Setter für die Standardausleihdauer.
	 * 
	 * @param lendingTime
	 *            die Standardausleihdauer
	 */
	public void setLendingTime(int lendingTime) {
		element.setLendingTime(lendingTime);
	}

	/**
	 * Getter für die Standardmahngebühr.
	 * 
	 * @return die Standardmahngebühr
	 */
	public BigDecimal getFee() {
		return element.getFee();
	}

	/**
	 * Setter für die Standardmahngebühr.
	 * 
	 * @param fee
	 *            die Standardmahngebühr
	 */
	public void setFee(BigDecimal fee) {
		element.setFee(fee);
	}

	/**
	 * Getter für die Anzahl an möglichen Verlängerungen durch den Leser selbst.
	 * 
	 * @return die Anzahl der Verlängerungen
	 */
	public int getExtensions() {
		return element.getExtensions();
	}

	/**
	 * Setter für die Anzahl an möglichen Verlängerungen durch den Leser selbst.
	 * 
	 * @param extensions
	 *            die Anzahl an Verlängerungen
	 */
	public void setExtensions(int extensions) {
		element.setExtensions(extensions);
	}

	/**
	 * Getter für die Standardverlängerungszeit.
	 * 
	 * @return die Standardverlängerungszeit
	 */
	public int getExtensionTime() {
		return element.getExtensionTime();
	}

	/**
	 * Setter für die Standardverlängerungszeit.
	 * 
	 * @param die
	 *            Standardverlängerungszeit
	 */
	public void setExtensionTime(int extensionTime) {
		element.setExtensionTime(extensionTime);
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

	/**
	 * Prüft ob ein Attribut gesetzt ist
	 * 
	 * @author Bernd Poppinga
	 * @param i
	 *            das Attribut, das geprüft werden soll
	 * @return Ob das Attribut gesetzt ist.
	 */
	public boolean checkAttribute(int i) {
		String attribute = null;
		switch (i) {
		case 0:
			attribute = element.getAttribute0();
			break;
		case 1:
			attribute = element.getAttribute1();
			break;
		case 2:
			attribute = element.getAttribute2();
			break;
		case 3:
			attribute = element.getAttribute3();
			break;
		case 4:
			attribute = element.getAttribute4();
			break;
		case 5:
			attribute = element.getAttribute5();
			break;
		case 6:
			attribute = element.getAttribute6();
			break;
		case 7:
			attribute = element.getAttribute7();
			break;
		case 8:
			attribute = element.getAttribute8();
			break;
		case 9:
			attribute = element.getAttribute9();
			break;
		}
		if (attribute != null) {
			if (!attribute.equals("")) {
				return true;
			}
		}
		return false;

	}

}
