package swp.bibjsf.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.Rating;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.businesslogic.Statistics;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Constraint.AttributeType;

/**
 * Testklasse für die Klasse Statistics in der BusinessLogic.
 * 
 * @author Helena Meißner, Eike Externest
 */
public class KT_Statistics_BB {

	/**
	 * Gemocktes Data Objekt.
	 */
	private Persistence mockedData;

	/**
	 * Das Statistics-Objekt.
	 */
	private Statistics stats;

	/**
	 * Die Wochentage.
	 */
	private final List<String> days;

	/**
	 * Kalender-Instanz
	 */
	private Calendar cal;

	/**
	 * Anzahl an Wochentagen
	 */
	private static final int WEEK_DAY_COUNT = 7;

	/**
	 * Maximale Anzahl an Tagen im Monat
	 */
	private static final int MONTH_DAY_COUNT = 31;

	/**
	 * Bereitet die Tests vor.
	 * 
	 * @author Eike Externest
	 */
	public KT_Statistics_BB() throws DataSourceException {
		days = new ArrayList<String>();
		days.add("Montag");
		days.add("Dienstag");
		days.add("Mittwoch");
		days.add("Donnerstag");
		days.add("Freitag");
		days.add("Samstag");
		days.add("Sonntag");
	}

	@Before
	public void setup() throws DataSourceException, NamingException {
		mockedData = mock(Persistence.class);
		stats = new Statistics(mockedData);
	}

	/**
	 * Testet den Normalfall für die Methode lendingTimesPerWeek.
	 * 
	 * @author Helena Meißner
	 * @DataSourceException
	 */
	@Test
	public void testLendingTimesPerWeek() throws DataSourceException {

		cal = Calendar.getInstance();
		List<Lending> lendings = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Lending lending = new Lending();
			cal.set(2014, 2, 22 + i * 2); // Samstag, Montag, Mittwoch, Freitag,
											// Sonntag
			lending.setStart(cal.getTime());
			lendings.add(lending);
		}

		int[] weekdays = new int[WEEK_DAY_COUNT + 1];
		weekdays[7]++;
		weekdays[2]++;
		weekdays[4]++;
		weekdays[6]++;
		weekdays[1]++;

		when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(lendings);

		assertTrue(stats.lendingTimesPerWeek().equals(weekdays));
	}

	/**
	 * Testet den Grenzfall der Methode lendingTimesPerWeek. Es wird davon
	 * ausgegangen, dass keine Ausleihen im System vorhanden sind.
	 * 
	 * @author Eike Externest
	 */
	@Test
	public void testLendingTimesPerWeekBorder() throws DataSourceException {

		List<Lending> lendings = new ArrayList<>();

		int[] weekdays = new int[WEEK_DAY_COUNT + 1];

		when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(lendings);

		assertTrue(stats.lendingTimesPerWeek().equals(weekdays));
	}

	/**
	 * Testet den Normalfall für die Methode lendingTimesPerMonth.
	 * 
	 * @author Helena Meißner
	 * @DataSourceException
	 */
	@Test
	public void testLendingTimesPerMonth() throws DataSourceException {

		cal = Calendar.getInstance();
		List<Lending> lendings = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Lending lending = new Lending();
			cal.set(2014, 2, 22 + i * 2); // 22., 24., 26., 28., 2.
			lending.setStart(cal.getTime());
			lendings.add(lending);
		}

		int[] monthdays = new int[MONTH_DAY_COUNT + 1];
		monthdays[22]++;
		monthdays[24]++;
		monthdays[26]++;
		monthdays[28]++;
		monthdays[2]++;

		when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(lendings);

		assertTrue(stats.lendingTimesPerWeek().equals(monthdays));
	}

	/* Vormerkzeiten */

	/**
	 * Testet den Normalfall für die Methode reservationTimesPerMonth. Es wird
	 * davon ausgegangen, dass Vormerkungen im System vorhanden sind und keine
	 * DataSourceException gefangen werden musste.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testReservationTimesPerWeek() throws DataSourceException {

		cal = Calendar.getInstance();
		List<Reservation> reservations = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Reservation reservation = new Reservation();
			cal.set(2014, 2, 22 + i * 2); // Samstag, Montag, Mittwoch, Freitag,
											// Sonntag
			reservation.setReservationDate(cal.getTime());
			reservations.add(reservation);
		}

		int[] weekdays = new int[WEEK_DAY_COUNT + 1];
		weekdays[7]++;
		weekdays[2]++;
		weekdays[4]++;
		weekdays[6]++;
		weekdays[1]++;

		when(mockedData.getReservations(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(reservations);

		assertTrue(stats.reservationTimesPerWeek().equals(weekdays));
	}

	/**
	 * Testet den Grenzfall der Methode reservationTimesPerWeek. Es wird davon
	 * ausgegangen, dass keine Vormerkungen im System vorhanden sind.
	 * 
	 * @author Eike Externest
	 */
	@Test
	public void testReservationTimesPerWeekBorder() throws DataSourceException {

		List<Reservation> reservations = new ArrayList<>();

		int[] weekdays = new int[WEEK_DAY_COUNT + 1];

		when(mockedData.getReservations(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(reservations);

		assertTrue(stats.reservationTimesPerWeek().equals(weekdays));
	}

	/**
	 * Testet den Normalfall für die Methode reservationTimesPerMonth.
	 * 
	 * @author Helena Meißner
	 * @DataSourceException
	 */
	@Test
	public void testReservationTimesPerMonth() throws DataSourceException {

		cal = Calendar.getInstance();
		List<Reservation> reservations = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Reservation reservation = new Reservation();
			cal.set(2014, 2, 22 + i * 2); // 22., 24., 26., 28., 2.
			reservation.setReservationDate(cal.getTime());
			reservations.add(reservation);
		}

		int[] monthdays = new int[MONTH_DAY_COUNT + 1];
		monthdays[22]++;
		monthdays[24]++;
		monthdays[26]++;
		monthdays[28]++;
		monthdays[2]++;

		when(mockedData.getReservations(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(reservations);

		assertTrue(stats.reservationTimesPerWeek().equals(monthdays));
	}

	/* Bibliothek in Zahlen */

	/**
	 * Testet den Normalfall für die Methode lendingsPerReader.
	 * 
	 * @author Helena Meißner
	 * @throws DataSourceException
	 */
	@Test
	public void lendingsPerReader() throws DataSourceException {

		List<Lending> lendings = new ArrayList<>();
		lendings.add(new Lending());
		lendings.add(new Lending());
		lendings.add(new Lending());

		List<Reader> readers = new ArrayList<>();
		readers.add(new Reader());

		when(mockedData.getNumberOfLendings(null)).thenReturn(lendings.size());
		when(mockedData.getNumberOfReaders(null)).thenReturn(readers.size());

		double count = lendings.size() / (double) readers.size();
		NumberFormat numberFormat = new DecimalFormat("0.00");
		String avgCount = numberFormat.format(count);

		assertTrue(stats.lendingsPerReader().equals(avgCount));

	}

	/**
	 * Testet den Normalfall für die Methode getReaderCount.
	 * 
	 * @author Helena Meißner
	 * @throws DataSourceException
	 */
	@Test
	public void testGetReaderCount() throws DataSourceException {

		List<Reader> readers = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			readers.add(new Reader());
		}

		when(mockedData.getNumberOfReaders(null)).thenReturn(readers.size());

		assertTrue(stats.getReaderCount() == readers.size());
	}

	/**
	 * Testet den Normalfall für die Methode getActiveReaderCount.
	 * 
	 * @author Helena Meißner
	 * @DataSourceException
	 */
	@Test
	public void getActiveReaderCount() throws DataSourceException {

		List<Reader> readers = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Reader r = new Reader();
			if (i % 2 == 0) {
				r.setStatus("active"); // 0, 2, 4 = 3 Mal aktiv
			} else {
				r.setStatus("blocked"); // 1, 3 Mal blockiert
			}
			readers.add(r);
		}
		int count = 0;
		for (Reader r : readers) {
			if (r.getStatus().equals("active")) {
				count++;
			}
		}

		List<Constraint> cons = new ArrayList<>();
		cons.add(new Constraint("status", "=", "inactive", "OR",
                AttributeType.STRING));
		when(mockedData.getNumberOfReaders(cons)).thenReturn(count);

		assertTrue(stats.getInactiveReaderCount() == count);
	}

	/**
	 * Testet den Normalfall für die Methode getInactiveReaderCount.
	 * 
	 * @author Helena Meißner
	 * @DataSourceException
	 */
	@Test
	public void getInactiveReaderCountNormal() throws DataSourceException {
		List<Reader> readers = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Reader r = new Reader();
			if (i % 2 == 1) {
				r.setStatus("inactive"); // 1, 3 = 2 Mal inaktiv
			} else {
				r.setStatus("blocked"); // 0, 2, 4 Mal blockiert
			}
			readers.add(r);
		}
		int count = 0;
		for (Reader r : readers) {
			if (r.getStatus().equals("inactive")) {
				count++;
			}
		}

		when(mockedData.getNumberOfReaders(null)).thenReturn(readers.size());

		assertTrue(stats.getInactiveReaderCount() == count);
	}

	/**
	 * Testet den Normalfall für die Methode getBlockedReaderCount.
	 * 
	 * @author Helena Meißner
	 * @DataSourceException
	 */
	@Test
	public void getBlockedReaderCountNormal() throws DataSourceException {

		List<Reader> readers = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Reader r = new Reader();
			if (i % 2 == 0) {
				r.setStatus("blocked"); // 0, 2, 4 = 3 Mal gesperrt
			} else {
				r.setStatus("active"); // 1, 3 Mal aktiv
			}
			readers.add(r);
		}
		int count = 0;
		for (Reader r : readers) {
			if (r.getStatus().equals("blocked")) {
				count++;
			}
		}

		when(mockedData.getNumberOfReaders(null)).thenReturn(readers.size());

		assertTrue(stats.getBlockedReaderCount() == count);
	}

	/**
	 * Testet den Normalfall für die Methode getFeeLendingCount.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetFeeLendingCount() throws DataSourceException {

		List<Lending> lendings = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Lending lending = new Lending();
			lending.setPaid(false);
			lending.setReturned(true);
		}
		Lending lend = new Lending();
		lend.setReturned(false);
		lendings.add(lend);

		int count = 0;
		for (Lending lending : lendings) {
			if (!lending.isPaid() && lending.hasReturned()) {
				count++;
			}
		}

		when(mockedData.getNumberOfLendings(null)).thenReturn(lendings.size());

		assertTrue(stats.getFeeLendingCount() == count);
	}

	/**
	 * Testet den Normalfall für die Methode getActiveLendingCount.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetActiveLendingCount() throws DataSourceException {

		List<Lending> lendings = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Lending lending = new Lending();
			lending.setPaid(false);
			lending.setReturned(false);
		}
		Lending lend = new Lending();
		lend.setPaid(false);
		lend.setReturned(true);

		int count = 0;
		for (Lending lending : lendings) {
			if (!lending.isPaid() && !lending.hasReturned()) {
				count++;
			}
		}

		when(mockedData.getNumberOfReaders(null)).thenReturn(lendings.size());

		assertTrue(stats.getActiveLendingCount() == count);

	}

	/**
	 * Testet den Normalfall für die Methode getMediaCount.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetMediaCount() throws DataSourceException {
		List<Medium> media = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			media.add(new Medium());
		}

		when(mockedData.getNumberOfMediums(null)).thenReturn(media.size());

		assertTrue(stats.getActiveLendingCount() == media.size());
	}

	/**
	 * Testet den Normalfall für die Methode getExemplarCount.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void getExemplarCount() throws DataSourceException {
		List<Exemplar> exemplars = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			exemplars.add(new Exemplar());
		}

		when(mockedData.getNumberOfExemplars(null))
				.thenReturn(exemplars.size());

		assertTrue(stats.getActiveLendingCount() == exemplars.size());
	}

	/**
	 * Testet den Normalfall für die Methode getLendableExemplarCount.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetLendableExemplarCount() throws DataSourceException {
		List<Exemplar> exemplars = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Exemplar ex = new Exemplar();
			if (i % 2 == 0) {
				ex.setStatus("leihbar");
			} else {
				ex.setStatus("verliehen");
			}
			exemplars.add(ex);
		}

		int count = 0;
		for (Exemplar ex : exemplars) {
			if (ex.getStatus().equals("leihbar")) {
				count++;
			}
		}

		when(mockedData.getNumberOfExemplars(null))
				.thenReturn(exemplars.size());

		assertTrue(stats.getLendableExemplarCount() == count);
	}

	/**
	 * Testet den Normalfall für die Methode getLentExemplarCount.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetLentExemplarCount() throws DataSourceException {
		List<Exemplar> exemplars = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Exemplar ex = new Exemplar();
			if (i % 2 == 0) {
				ex.setStatus("leihbar");
			} else {
				ex.setStatus("verliehen");
			}
			exemplars.add(ex);
		}

		int count = 0;
		for (Exemplar ex : exemplars) {
			if (ex.getStatus().equals("verliehen")) {
				count++;
			}
		}

		when(mockedData.getNumberOfExemplars(null))
				.thenReturn(exemplars.size());

		assertTrue(stats.getLendableExemplarCount() == count);
	}

	/**
	 * Testet den Normalfall für die Methode getBlockedExemplarCount.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetBlockedExemplarCount() throws DataSourceException {
		List<Exemplar> exemplars = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Exemplar ex = new Exemplar();
			if (i % 2 == 0) {
				ex.setStatus("blockiert");
			} else {
				ex.setStatus("verliehen");
			}
			exemplars.add(ex);
		}

		int count = 0;
		for (Exemplar ex : exemplars) {
			if (ex.getStatus().equals("blockiert")) {
				count++;
			}
		}

		when(mockedData.getNumberOfExemplars(null))
				.thenReturn(exemplars.size());

		assertTrue(stats.getLendableExemplarCount() == count);
	}

	/**
	 * Testet den Normalfall für die Methode getRatedMedia.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetRatedMediaNormal() throws DataSourceException {

		List<Rating> ratings = new ArrayList<>();
		for (int i = 1; i < 6; i++) {
			Rating rat = new Rating();
			rat.setRating(i);
			rat.setMediumID(i);
			rat.setReaderID(0);
		}

		List<Medium> media = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Medium med = new Medium();
			med.setId(i);
		}

		List<Reader> readers = new ArrayList<>();
		readers.add(new Reader());

		when(mockedData.getRating(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(ratings);
		when(mockedData.getMediums(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(media);
		when(mockedData.getReaders(null, 0, Integer.MAX_VALUE, null))
				.thenReturn(readers);

		assertTrue(stats.getRatedMedia(5, true).size() == 5);
	}

	/**
	 * Testet den Fehlerfall für die Methode getRatedMedia.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetRatedMediaError() throws DataSourceException {
		stats.getRatedMedia(-1, true);
	}

	/**
	 * Testet den Trivialfall für die Methode getLentMedia.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void testGetLentMedia()
			throws DataSourceException {

		when(mockedData.getNumberOfMediums(null)).thenReturn(0);
		when(mockedData.getLentMedia(5, true)).thenReturn(
				new ArrayList<Medium>());

		assertTrue(stats.getLentMedia(5, true).isEmpty());

	}
	
	/**
	 * Testet den Fehlerfall für die Methode getLentMedia.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetLentMediaError() throws DataSourceException {
		stats.getLentMedia(-1, true);
	}
	
	/**
	 * Testet den Normalfall für die Methode getLendingCountForMedium.
	 * 
	 * @author Eike Externest
	 * @throws DataSourceException
	 */
	@Test
	public void getLendingCountForMedium() throws DataSourceException {
		
		ArrayList<Exemplar> exemplars = new ArrayList<>();
		for (int i =0; i<3; i++) {
			Exemplar ex = new Exemplar();
			ex.setLendingCount(1);
			exemplars.add(ex);
		}
		
		when(mockedData.getExemplars(null, 0, Integer.MAX_VALUE, null)).thenReturn(exemplars);

        assertTrue(stats.getLendingCountForMedium(new Medium()) == 3);
	}
}