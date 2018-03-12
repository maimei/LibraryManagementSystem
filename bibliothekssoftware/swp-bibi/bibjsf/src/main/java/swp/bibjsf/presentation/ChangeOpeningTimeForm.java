package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.OpeningTime;
import swp.bibjsf.businesslogic.OpeningTimeHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Ändert eine existierende Öffnungszeit
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class ChangeOpeningTimeForm extends OpeningTimeForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 2941103575925131466L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new opening time "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				OpeningTimeHandler oth = OpeningTimeHandler.getInstance();
				if (checkMorningTime() && checkAfternoonTime()) {
					oth.update(element);
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Öffnungszeit wurde erfolgreich bearbeitet"));
					return "success";
				} else {
					return "error";
				}
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Öffnungszeit konnte nicht bearbeitet werden"));
				return "error";
			} catch (NoSuchBusinessObjectExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Öffnungszeit existiert nicht und konnte nicht bearbeitet werden"));
				return "error";
			} catch (NumberFormatException e3) {
				FacesContext.getCurrentInstance().addMessage(null, 	new FacesMessage(FacesMessage.SEVERITY_WARN, 
						"Fehler", "Die Uhrzeit war ungültig."));
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde keine Öffnungszeit übergeben."));
			return "error";
		}
	}
	
	/**
	 * Setzt das geänderte Kommentar
	 * 
	 * @param newCommentary
	 *            geändertes Kommentar
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public String edit(OpeningTime newOpeningTime) {
		element = newOpeningTime;
		return "edit";
	}
	
	private boolean checkMorningTime() {
		if (getMorningStart() == null && getMorningEnd() == null) {
			return true;
		} else if (getMorningStart() == null || getMorningEnd() == null) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Achtung", "Es wird Start- und Endzeitpunkt der Uhrzeit vormittags benötigt."));
			return false;
		} else {
			int hourS = Integer.parseInt(getMorningStart().substring(0, 2)); //Stunde des Starts vormittags
			int hourE = Integer.parseInt(getMorningEnd().substring(0, 2)); //Stunde des Endes vormittags
			boolean validHS = hourS >= 0 && hourS < 24; // Stunde des Starts vormittags im gültigen Bereich
			boolean validHE = hourE >= 0 && hourE < 24; // Stunde des Endes vormittags im gültigen Bereich
			
			int minuteS = Integer.parseInt(getMorningStart().substring(3, 5));
			int minuteE = Integer.parseInt(getMorningEnd().substring(3, 5));
			boolean validMS = minuteS >= 0 && minuteS < 60;
			boolean validME = minuteE >= 0 && minuteE < 60;
			
			if (validHS && validMS) {
				if (validHE && validME) {
					if (hourS < hourE || (hourE == hourS && minuteS < minuteE)) {
						return true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
			            		"Fehler", "Die Uhrzeiten " + getMorningStart() + " und " + getMorningEnd() + " vormittags sind nicht kompatibel und wurden nicht bearbeitet."));
						return false;
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
		            		"Fehler", "Die Enduhrzeit " + getMorningEnd() + " vormittags ist ungültig und wurde nicht bearbeitet."));
					return false;
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
	            	"Fehler", "Die Startuhrzeit " + getMorningStart() + " vormittags ist ungültig und wurde nicht bearbeitet."));
				return false;
			}
		}
	}
	
	private boolean checkAfternoonTime() {
		if (getAfternoonStart() == null && getAfternoonEnd() == null) {
			return true;
		} else if (getAfternoonStart() == null || getAfternoonEnd() == null) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
	            "Achtung", "Es wird Start- und Endzeitpunkt der Uhrzeit nachmittags benötigt."));
			return false;
		} else {
			int hourS = Integer.parseInt(getAfternoonStart().substring(0, 2)); //Stunde des Starts nachmittags
			int hourE = Integer.parseInt(getAfternoonEnd().substring(0, 2)); //Stunde des Endes nachmittags
			boolean validHS = hourS >= 0 && hourS < 24; // Stunde des Starts nachmittags im gültigen Bereich
			boolean validHE = hourE >= 0 && hourE < 24; // Stunde des Endes nachmittags im gültigen Bereich
			
			int minuteS = Integer.parseInt(getAfternoonStart().substring(3, 5));
			int minuteE = Integer.parseInt(getAfternoonEnd().substring(3, 5));
			boolean validMS = minuteS >= 0 && minuteS < 60;
			boolean validME = minuteE >= 0 && minuteE < 60;
			
			int hourEM, minuteEM;
			if (getMorningEnd() != null) {
				hourEM = Integer.parseInt(getMorningEnd().substring(0, 2));
				minuteEM = Integer.parseInt(getMorningEnd().substring(3, 5));
			} else {
				hourEM = 0;
				minuteEM = 0;
			}
			
			if (validHS && validMS) {
				if (validHE && validME) {
					if (hourS < hourE || (hourE == hourS && minuteS < minuteE)) {
						if (hourEM < hourS || (hourEM == hourS && minuteEM < minuteS)) {
							return true;
						} else {
							FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
				            	"Fehler", "Die Uhrzeiten vormittags und nachmittags sind nicht kompatibel."));
							return false;
						}
					} else {
						FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
			            	"Fehler", "Die Uhrzeiten " + getAfternoonStart() + " und " + getAfternoonEnd() + " nachmittags sind nicht kompatibel und wurden nicht bearbeitet."));
						return false;
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
		            		"Fehler", "Die Enduhrzeit " + getAfternoonEnd() + " nachmittags ist ungültig und wurde nicht bearbeitet."));
					return false;
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
	            	"Fehler", "Die Startuhrzeit " + getAfternoonStart() + " nachmittags ist ungültig und wurde nicht bearbeitet."));
				return false;
			}
		}
	}
}
