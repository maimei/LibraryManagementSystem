package swp.bibjsf.presentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.businesslogic.ReservationHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;

/**
 * Fügt eine neue Ausleihe hinzu.
 * 
 * @author Bernd Poppinga
 */
@ManagedBean
@SessionScoped
public class AddLendingForm extends LendingForm {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 3533191489675286712L;

	/**
	 * Leser der Ausleihe
	 */
	private Reader reader;

	/**
	 * Exemplar der Ausleihe
	 */
	private Exemplar exemplar;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	public AddLendingForm() {
		super();
		element = new Lending();
	}

	@Override
	public String save() {
		logger.debug("request to save new Lending "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {

				LendingHandler lh = LendingHandler.getInstance();
				element.setStart(new Date());
				element.setExtensions(0);
				element.setPaid(false);
				element.setReturned(false);
				element.setFee(BigDecimal.valueOf(0));
				if (!checkLending()) {
					return "error";
				}

				lh.add(element);

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								"Auseihe wurde erfolgreich hinzugefügt"));

				// Reservierungen des Readers für dieses Exemplar löschen
				ReservationHandler rh = ReservationHandler.getInstance();
				List<Constraint> cons = new ArrayList<Constraint>();
				cons.add(new Constraint("readerid", "=", ""
						+ element.getReaderID(), "AND", null));
				cons.add(new Constraint("mediumid", "=", ""
						+ exemplar.getMediumID(), "AND", null));
				rh.delete(rh.get(cons, 0, Integer.MAX_VALUE, null));

				// reset so that the next Lending can be added without need to
				// press a button
				reset();
				return "success";
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Ausleihe konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (BusinessElementAlreadyExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Ausleihe existiert bereits und konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (NoSuchBusinessObjectExistsException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
								"Kein solches Objekt ist vorhanden"));
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde keine Ausleihe übergeben."));
			return "error";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#reset()
	 */
	@Override
	public String reset() {
		element = new Lending();
		return super.reset();
	}

	/**
	 * Prüft, ob der Leser, validate ist, d.h. ob er existiert, freigeschaltet
	 * ist und nicht blockiert ist.
	 * 
	 * @author Bernd Poppinga
	 * @return Ob ein Leser valide ist.
	 */
	private boolean validateReader() {
		try {
			ReaderHandler rh = ReaderHandler.getInstance();
			reader = rh.get(element.getReaderID());
			if (reader == null) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_WARN,
										"Fehler",
										"Es konnte kein Leser mit passender ID gefunden werden."));
				return false;
			} else {
				if (reader.getGroupid().equals("NEW")) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Fehler",
									"Leser muss freigeschaltet werden."));
					return false;
				} else {
					if (reader.getStatus().equals("blocked"))
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_WARN,
										"Achtung", "Leser ist gesperrt."));
					return true;
				}
			}
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Es konnte kein Leser mit passender ID gefunden werden."));
			return false;
		}
	}

	/**
	 * Prüft, ob das Exemplar, validate ist, d.h. ob er existiert, leihbar ist
	 * und nicht gesperrt ist.
	 * 
	 * @author Bernd Poppinga
	 * @return Ob ein Exemplar validate ist.
	 */
	private boolean validateExemplar() {
		try {
			ExemplarHandler eh = ExemplarHandler.getInstance();
			exemplar = eh.get(element.getExemplarID());
			if (exemplar == null) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Es konnte kein Exemplar mit passender ID gefunden werden."));
				return false;

			} else {
				if (exemplar.getStatus().equals("verliehen")) {
					FacesContext.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_FATAL,
											"Fehler",
											"Exemplar ist bereits verliehen."));
					return false;
				} else {
					if (reader.getStatus().equals("blockiert")) {
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Achtung", "Exemplar ist gesperrt."));
					}
					return true;
				}
			}
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Es konnte kein Exemplar mit passender ID gefunden werden."));
			return false;
		} catch (NoSuchBusinessObjectExistsException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Fehler",
									"Es konnte kein Exemplar mit passender ID gefunden werden."));
			return false;
		}
	}

	/**
	 * Prüft, ob das Exemplar von einem anderen Leser vorgemerkt worden ist und
	 * gibt ggf. eine Nachricht aus.
	 * 
	 * @author Bernd Poppinga
	 * 
	 */
	public void isReservated() {
		try {
			ReservationHandler rh = ReservationHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("mediumID", "=", ""
					+ exemplar.getMediumID(), "AND",
					Constraint.AttributeType.INTEGER));
			List<Reservation> list = rh.get(cons, 0, Integer.MAX_VALUE, null);
			if (list.size() != 0) {
				MediumHandler mh = MediumHandler.getInstance();
				int exemplars = mh.getAllAvailableExemplars(exemplar
						.getMediumID());
				logger.info(list.size() + "fawg4egagrbvsuibviosuc");
				logger.info(exemplars + "ovnuoibvsuibviosuc");
				if (exemplars <= list.size()) {
					list = sortReservationByDate(list);
					int n = -1;
					for (int i = 0; i < list.size(); i++) {
						logger.info("disukbficvasbi<" + i);
						if (list.get(i).getReaderID() == reader.getId()) {
							n = i;
							logger.info("disukbfidbviucafovnisucvasbi<" + i);
							break;
						}
					}
					if (n == -1) {
						FacesContext
								.getCurrentInstance()
								.addMessage(
										null,
										new FacesMessage(
												FacesMessage.SEVERITY_WARN,
												"Achtung",
												"Andere Leser haben dieses Medium vorgemerkt, dieser jedoch nicht."));
					} else if (n >= exemplars) {
						FacesContext
								.getCurrentInstance()
								.addMessage(
										null,
										new FacesMessage(
												FacesMessage.SEVERITY_WARN,
												"Achtung",
												"Es gibt noch "
														+ exemplars
														+ " leihbare Exemplare. Jedoch steht dieser Leser erst an Position "
														+ (n + 1)
														+ " der Vormerkungsliste."));
					}

				}
			}

		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Überprüfung konnte nicht durchgeführt werden"));
		}
	}

	/**
	 * Führt alle Validierungen aus.
	 * 
	 * @author Bernd Poppinga
	 * @return Navigationsfall für die Faces ( Wurde die Validierung bestanden ?
	 *         )
	 */
	public String validate() {
		if (validateReader())
			if (validateExemplar()) {
				isReservated();
				element.setTill(getLendingEnd(element.getExemplarID()));
				return "passed";
			}
		return "notpassed";
	}

	/**
	 * Gibt den Leser der Ausleihe zurück.
	 * 
	 * @return Leser der Ausleihe
	 */
	public Reader getReader() {
		return reader;
	}

	/**
	 * Gibt das Exemplar der Ausleihe zurück.
	 * 
	 * @return Exemplar der Ausleihe
	 */
	public Exemplar getExemplar() {
		return exemplar;
	}

	/**
	 * Sortiert eine Liste von Rezensionen nach dem Datum
	 * 
	 * @author Bernd Poppinga
	 * @param list
	 *            Liste der zu sortierenden Rezensionen
	 * @return sortierte Liste
	 */
	private List<Reservation> sortReservationByDate(List<Reservation> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i; j < list.size() - 1; j++) {
				logger.info("j =" + j + "und i = " + i);
				if (list.get(j).getReservationDate()
						.after(list.get(j + 1).getReservationDate())) {
					Reservation n = list.get(j);
					list.add(j, list.get(j + 1));
					list.add(j + 1, n);
				}
			}
		}
		return list;
	}

}