package swp.bibjsf.presentation;

import swp.bibcommon.OpeningTime;

/**
 * Abstrakte Oberklasse für die ChangeOpeningTimeForm.
 *  
 * @author Helena Meißner
 */
public abstract class OpeningTimeForm extends BusinessObjectForm<OpeningTime> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -621095634934896988L;
	
	/**
	 * Getter für den Wochentag der Öffnungszeit.
	 * @return der Wochentag
	 */
	public String getDay() {
		return element.getDay();
	}
	
	/**
	 * Setter für den Wochentag der Öffnungszeit.
	 * @param weekday
	 */
	public void setDay(String day) {
		element.setDay(day);
	}
	
	/**
	 * Getter für den Beginn der Öffnungszeiten vormittags.
	 * @return Beginn der Öffnungszeiten vormittags
	 */
	public String getMorningStart() {
		return element.getMorningStart();
	}
	
	/**
	 * Setter für den Beginn der Öffnungszeiten vormittags.
	 * @param morningStart
	 * 				Beginn der Öffnungszeiten vormittags
	 */
	public void setMorningStart(String morningStart) {
		element.setMorningStart(morningStart);
	}
	
	/**
	 * Getter für das Ende der Öffnungszeiten vormittags.
	 * @return Ende der Öffnungszeiten vormittags
	 */
	public String getMorningEnd() {
		return element.getMorningEnd();
	}
	
	/**
	 * Setter für das Ende der Öffnungszeiten vormittags.
	 * @param morningEnd
	 * 				Ende der Öffnungszeiten vormittags
	 */
	public void setMorningEnd(String morningEnd) {
		element.setMorningEnd(morningEnd);
	}
	
	/**
	 * Getter für den Beginn der Öffnungszeiten nachmittags.
	 * @return Beginn der Öffnungszeiten nachmittags
	 */
	public String getAfternoonStart() {
		return element.getAfternoonStart();
	}
	
	/**
	 * Setter für den Beginn der Öffnungszeiten nachmittags.
	 * @param afternoonStart
	 * 				 Beginn der Öffnungszeiten nachmittags
	 */
	public void setAfternoonStart(String afternoonStart) {
		element.setAfternoonStart(afternoonStart);
	}
	
	/**
	 * Getter für das Ende der Öffnungszeiten nachmittags.
	 * @return Ende der Öffnungszeiten nachmittags
	 */
	public String getAfternoonEnd() {
		return element.getAfternoonEnd();
	}
	
	/**
	 * Setter für das Ende der Öffnungszeiten nachmittags.
	 * @param afternoonEnd
	 * 				Ende der Öffnungszeiten nachmittags
	 */
	public void setAfternoonEnd(String afternoonEnd) {
		element.setAfternoonEnd(afternoonEnd);
	}
}
