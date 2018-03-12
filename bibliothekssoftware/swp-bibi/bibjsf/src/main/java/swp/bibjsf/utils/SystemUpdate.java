package swp.bibjsf.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.apache.commons.lang.time.DateFormatUtils;

import swp.bibcommon.ClosedTime;
import swp.bibcommon.Lending;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.businesslogic.ClosedTimeHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.businesslogic.PropertyHandler;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.businesslogic.ReservationHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint.AttributeType;

/**
 * SystemUpdate beinhaltet die notwendigen Implementierungen, um System-Updates
 * durchzuführen.
 * 
 * @author Hauke Olf, Niklas Bruns
 * 
 */
@Singleton
@Startup
public class SystemUpdate {
	/**
	 * Returns a date in the SQL date format.
	 * 
	 * @param date
	 * @return date in SQL date format
	 */
	private String toDateFormat(final Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}

	@PostConstruct
	public void onStartup() {
		System.out.println("Initialization success. HIER HIER");
		updateOnceADay();
	}

	/**
	 * Ruft die Update-Methoden für geendete Ausleihen, ausstehende Gebühren und
	 * inaktive Leser einmal am Tag um 00.00 Uhr auf.
	 * 
	 * @throws DataSourceException
	 *             is thrown if there are issues with the persistence component.
	 */
	@Schedule(dayOfMonth = "*", hour = "0")
	public void updateOnceADay() {
		try {
			System.out.println("SystemUpdate startet...");
			updateFee();
			updateInactiveReaders();
			updateReservations();
			updateClosedTimes();
		} catch (Exception e) {
			System.out.println("SystemUpdate error..." + e.getMessage());
			// throw new DataSourceException(e.getMessage());
		}
	}

	/**
	 * Löscht Closed Times die bereits verstichen sind.
	 * 
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	private void updateClosedTimes() {
		try {
			System.out.println("updateClosedTimes startet...");
			ClosedTimeHandler cH;

			cH = ClosedTimeHandler.getInstance();
			List<Constraint> constraints = new ArrayList<Constraint>();

			constraints.add(new Constraint("till", "<", toDateFormat(Calendar
					.getInstance().getTime()), "", AttributeType.STRING));
			List<ClosedTime> cl = cH.get(constraints, 0,
					cH.getNumber(constraints), null);
			if (cl.size() != 0) {
				System.out
						.println("WARNUNG: updateClosedTimes Wert/e werden gelöscht");
				cH.delete(cl);
			}
		} catch (DataSourceException | NoSuchBusinessObjectExistsException e) {
			System.out.println("ERROR at updateClosedTimes " + e.getMessage());
		}
	}

	/**
	 * Mit dieser Methode werden die überzogenen Ausleihen festgestellt und die
	 * anfallenden Gebühren berechnet und gespeichert.
	 * 
	 * @throws DataSourceException
	 *             is thrown if there are issues with the persistence component.
	 * @throws MessagingException
	 */
	private void updateFee() {

		try {
			System.out.println("updateFee startet...");
			LendingHandler lH = null;
			lH = LendingHandler.getInstance();
			ReaderInfo rI = new ReaderInfo();

			// Sendet erinnerung, dass Medium heute zurück gegeben werden muss.
			// DateFormat dateFormat = new
			// SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			List<Constraint> constraints = new ArrayList<Constraint>();

			/*
			 * constraints.add(new Constraint("till", "=", toDateFormat(Calendar
			 * .getInstance().getTime()), "AND", AttributeType.STRING));
			 * constraints.add(new Constraint("returned", "=", "false", "AND",
			 * AttributeType.STRING));
			 */

			constraints.add(new Constraint("till", "=", toDateFormat(Calendar
					.getInstance().getTime()), "AND", AttributeType.STRING));
			constraints.add(new Constraint("returned", "=", "false", "",
					AttributeType.STRING));
			List<Lending> ll = lH.get(constraints, 0,
					lH.getNumber(constraints), null);
			for (Lending lending : ll) {
				try {
					rI.sendLendingInfo(lending);
				} catch (NoSuchBusinessObjectExistsException e) {
					// Wenn es das Objekt nicht gibt, ist es auch egal.
				}
			}
			constraints.clear();
			ll.clear();
			/*
			 * constraints.add(new Constraint("till", "<", toDateFormat(Calendar
			 * .getInstance().getTime()), "AND", AttributeType.STRING));
			 * constraints.add(new Constraint("returned", "=", "false", "AND",
			 * AttributeType.STRING));
			 */

			constraints.add(new Constraint("till", "<", toDateFormat(Calendar
					.getInstance().getTime()), "AND", AttributeType.STRING));
			constraints.add(new Constraint("returned", "=", "false", "",
					AttributeType.STRING));
			ll = lH.get(constraints, 0, Integer.MAX_VALUE, null);
			for (Lending lending : ll) {
				try {
					rI.sendFeesInfo(lending);
				} catch (NoSuchBusinessObjectExistsException e) {
					// Wenn es das Objekt nicht gibt, muss auch nichts versendet
					// werden!
				}
			}

		} catch (DataSourceException | MessagingException e) {
			System.out.println("ERROR at updateFee " + e.getMessage());
		} catch (NamingException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Mit dieser Methode werden die inaktiven Leser festgestellt und als solche
	 * gespeichert und entsprechend weiter behandelt.
	 * 
	 * @throws DataSourceException
	 *             is thrown if there are issues with the persistence component.
	 * @throws NamingException
	 * @throws NumberFormatException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	private void updateInactiveReaders() {
		try {
			System.out.println("updateInactiveReaders startet...");
			ReaderHandler rH = ReaderHandler.getInstance();
			PropertyHandler pH = PropertyHandler.getInstance();
			List<Constraint> cons = new ArrayList<Constraint>();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -Integer.parseInt(pH.get(0).getValue()));

			cons.add(new Constraint("lastuse", "<=",
					toDateFormat(cal.getTime()), "AND", AttributeType.STRING));

			List<Reader> rl = rH.get(cons, 0, Integer.MAX_VALUE, null);
			for (Reader r : rl) {
				r.setStatus("inactive");
				try {
					rH.update(r);

				} catch (NoSuchBusinessObjectExistsException e) {
					// Dann ist der User auch egal!
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR at updateInactiveReaders "
					+ e.getMessage());
		}

	}

	private void updateReservations() {
		try {
			System.out.println("updateReservations startet...");
			ReservationHandler reH = ReservationHandler.getInstance();
			MediumHandler mh = MediumHandler.getInstance();
			ReaderInfo rI = new ReaderInfo();

			List<Reservation> lr = reH.get(null, 0, Integer.MAX_VALUE, null);
			for (Reservation res : lr) {
				if (mh.getAllAvailableExemplars(res.getMediumID()) > 0) {
					rI.sendReservationInfo(res);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR at updateReservations " + e.getMessage());
		}
	}

	// List<Constraint> contrain = new ArrayList<Constraint>();

	// EmailExemplarHelper helper = new EmailExemplarHelper(1);

	// Sort by Date
	/*
	 * for (Reservation r : lr) { List<Constraint> constraint2 = new
	 * ArrayList<Constraint>(); // Exemplare für MediumID finden und nicht
	 * ausgeliehen. constraint2.add(new Constraint("MediumID", "=", String
	 * .valueOf(r.getMediumID()), "AND", AttributeType.INTEGER));
	 */

	/*
	 * contrain.add(new Constraint("status", "=", "Leihbar", "AND",
	 * AttributeType.STRING));
	 */

	/*
	 * constraint2.add(new Constraint("status", "=", "Leihbar", "",
	 * AttributeType.STRING));
	 */

	// List<Exemplar> exemplare = eH.get(constraint2, 0,
	// eH.getNumber(constraint2), null);

	// Bei Konstruktor von EmailExemplarHelper, Parameter aus
	// Properties abfragen.

	/*
	 * List<Exemplar> eListe = eH.get(constraint2, 0, eH.getNumber(constraint2),
	 * null); if (eListe.size() != 0) { for (Exemplar ex : eListe) { if
	 * (helper.wantLoan(ex.getId())) { try { rI.sendReservationInfo(r); break; }
	 * catch (MessagingException e) { e.printStackTrace(); break; // kann ich
	 * auch nix machen! } catch (NoSuchBusinessObjectExistsException e) {
	 * e.printStackTrace(); break; // dann halt nicht ;) } } }
	 */

	/**
	 * Methode dient dazu, die MediumID zu ener ExemplarID zu ermitteln.
	 * 
	 * @param exemplarID
	 *            Exemplar ID zu der ermittelt werden soll.
	 * @return MediumID
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	/*
	 * private int getMediumID(final int exemplarID) throws DataSourceException,
	 * NoSuchBusinessObjectExistsException { ExemplarHandler eH =
	 * ExemplarHandler.getInstance(); Exemplar e = eH.get(exemplarID); return
	 * e.getMediumID(); }
	 */

}
