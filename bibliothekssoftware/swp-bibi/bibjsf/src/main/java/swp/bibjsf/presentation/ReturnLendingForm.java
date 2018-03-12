package swp.bibjsf.presentation;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Ändert eine existierende Leihe
 * 
 * @author Bernd Poppinga, Niklas Bruns
 */
@ManagedBean
@SessionScoped
public class ReturnLendingForm extends LendingForm {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 6558447953420364011L;

	/**
	 * Überziehungstage
	 */
	private int days = 0;

	/**
	 * berechnete Überziehungstage
	 */
	private int feedays = 0;

	/**
	 * Toleranz
	 */
	private int tolerance = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() {
		logger.debug("request to save new Lending "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				LendingHandler ah = LendingHandler.getInstance();
				element.setReturned(true);
				ExemplarHandler eh = ExemplarHandler.getInstance();
				Exemplar ex = eh.get(element.getExemplarID());
				ex.setStatus("leihbar");
				eh.update(ex);
				if (element.getFee().compareTo(BigDecimal.ZERO) == 0)
					element.setPaid(true);
				else
					element.setPaid(false);

				if (element.isPaid() == true
						&& (!(new AuthBackingBean()).getReaderByID(
								element.getReaderID()).isHistoryActivated())) {
				 element.setReaderID(-1);

				} 
					ah.update(element);

					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Info",
									"Leihe wurde erfolgreich bearbeitet"));
					reset();
					return "success";
				
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Leihe konnte nicht bearbeitet werden"));
				return "error";
			} catch (NoSuchBusinessObjectExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Leihe existiert nicht und konnte nicht bearbeitet werden"));
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde keine Leihe übergeben."));
			return "error";
		}
	}

	/**
	 * Setzt die geänderte Leihe
	 * 
	 * @param newLending
	 *            geänderte Leihe
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public String setLending(Lending newLending) {
		element = newLending;
		calcFee();
		return "return";
	}

	/**
	 * Setzt die bezahlte Leihe
	 * 
	 * @param newLending
	 *            geänderte Leihe
	 * @return Navigationsfall für faces-config.xml
	 */
	public String payLending(Lending newLending) {
		element = newLending;
		element.setFee(BigDecimal.ZERO);
		return save();

	}

	/**
	 * Setzt die Werte aus denen sich die Mahngebühren zusammensetzen
	 * 
	 * @author Bernd Poppinga
	 * 
	 */
	private void calcFee() {
		try {
			LendingHandler lh = LendingHandler.getInstance();
			int[] data = lh.calcFeeData(element);
			logger.info(data);
			days = data[0];
			feedays = data[1];
			tolerance = data[2];
			element.setFee(lh.calcFeeByData(element, data));

		} catch (Exception e) {
			logger.debug(e);

		}
	}
	
	/**
	 * Gibt die Überziehungstage zurück
	 * 
	 * @return Überziehungstage
	 */
	public int getDays() {
		return days;
	}
	
	/**
	 * Setzt die Überziehungstage
	 * 
	 * @param tolerance Überziehungstage
	 */
	public void setDays(int days) {
		this.days = days;
	}
	
	/**
	 * Gibt die angerechneten Überziehungstage zurück
	 * 
	 * @return angerechnete Überziehungstage
	 */
	public int getFeedays() {
		return feedays;
	}
	
	/**
	 * Setzt die angerechneten Überziehungstage
	 * 
	 * @param tolerance angerechnete Überziehungstage
	 */
	public void setFeedays(int feedays) {
		this.feedays = feedays;
	}
	
	/**
	 * Gibt die Toleranztage zurück
	 * 
	 * @return Toleranztage
	 */
	public int getTolerance() {
		return tolerance;
	}
	/**
	 * Setzt die Toleranztage
	 * 
	 * @param tolerance Toleranztage
	 */
	public void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}

}