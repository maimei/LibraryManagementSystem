package swp.bibjsf.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

import swp.bibcommon.OpeningTime;
import swp.bibjsf.businesslogic.OpeningTimeHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;

/**
 * Testklasse für die Klasse OpeningTimeHandler der Businesslogic
 * in Form von Whiteboxtests.
 *
 * @author Helena Meißner
 *
 */
public class KT_OpeningTimeHandler_WB {

    /**
     * Gemocktes Data-Objekt.
     */
    private Persistence mockedData;

	/**
	 * Instanz eines OpeningTimeHandlers.
	 */
	OpeningTimeHandler oth;

    /**
     * Array mit den Strings aller Wochentage.
     */
    private final String[] weekdays = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};

    @Before
    public void setup() throws DataSourceException, NamingException {
        mockedData = mock(Persistence.class);
        oth = new OpeningTimeHandler(mockedData);
    }

	/**
	 * Whitebox-Test für die Methode get(int id)
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test
	public void testGet() throws DataSourceException,
	NoSuchBusinessObjectExistsException {
		OpeningTime ot = new OpeningTime();
		ot.setDay("Freitag");
		ot.setId(4);


		when(mockedData.getOpeningTime(4)).thenReturn(ot);


		assertTrue(oth.get(4).equals(ot));
	}

	/**
	 * Whitebox-Test für die Methode get(List<Constraint> constraints, int from,
			int to, List<OrderBy> order)
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test
	public void testGet2() throws DataSourceException,
	NoSuchBusinessObjectExistsException {
		List<OpeningTime> ots = new ArrayList<>();
		for (int i = 0; i<weekdays.length; i++) {
			OpeningTime ot = new OpeningTime();
			ot.setDay(weekdays[i]);
			ot.setId(i);
			ots.add(ot);
			if (i % 2 == 0) {
				ot.setMorningStart(String.valueOf(6+i));
				ot.setMorningEnd(String.valueOf(8+i));
			} else {
				ot.setAfternoonStart(String.valueOf(12+i));
				ot.setAfternoonEnd(String.valueOf(14+i));
			}
		}


		when(mockedData.getAllOpeningTimes()).thenReturn(ots);


		List<OpeningTime> ots2 = oth.get(null, 0, Integer.MAX_VALUE, null);
		for (int i = 0; i<weekdays.length; i++) {
			assertTrue(ots2.get(i).equals(ots.get(i)));
		}
	}

	/**
	 * Whitebox-Test für die Methode getNumber(List<Constraint> constraints)
	 * Hier wird der Wert der Konstante weekdays erwartet.
	 * @throws DataSourceException
	 */
	@Test
	public void testGetNumber() throws DataSourceException {
		assertTrue(weekdays.length == (oth.getNumber(null)));
	}

	/**
	 * Whitebox-Test für die Methode add(OpeningTime element)
	 * Da diese nicht implementiert ist, wird hier eine NotImplementedException erwartet.
	 * @throws DataSourceException
	 * @throws BusinessElementAlreadyExistsException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test(expected = NotImplementedException.class)
	public void testAdd() throws DataSourceException,
	BusinessElementAlreadyExistsException,
	NoSuchBusinessObjectExistsException {
		oth.add(null);
	}

	/**
	 * Whitebox-Test für die Methode update(OpeningTime openingTime)
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test
	public void testUpdate() throws DataSourceException,
	NoSuchBusinessObjectExistsException {

		OpeningTime ot = new OpeningTime();
		ot.setId(2);
		ot.setDay("Mittwoch");

		when(mockedData.updateOpeningTime(ot)).thenReturn(1);
		when(mockedData.getOpeningTime(2)).thenReturn(ot);


		assertTrue(oth.update(ot)==1 || oth.get(2).equals(ot));
	}

	/**
	 * Whitebox-Test für die Methode delete(List<OpeningTime> elements)
	 * Da Öffnungszeiten-Objekte nicht gelöscht werden können,
	 * wird hier eine NotImplementedException erwartet.
	 * @throws DataSourceException
	 */
	@Test(expected = NotImplementedException.class)
	public void testDelete() throws DataSourceException {
		oth.delete(null);
	}

	/**
	 * Whitebox-Test für die Methode isOpenDay(Date day)
	 * für den Fall, dass nur vormittags geöffnet ist.
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test
	public void testIsOpenDay() throws DataSourceException,
	NoSuchBusinessObjectExistsException {
		/* Wochentag Dienstag in der Datenbank */
		OpeningTime ot = new OpeningTime();
		ot.setId(1);
		ot.setDay("Dienstag");
		ot.setMorningStart(String.valueOf(9));
		ot.setMorningEnd(String.valueOf(13));

		/* Übergebener Testtag (ein Dienstag) */
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 2, 25);




		OpeningTime ot2 = oth.get((cal.get(Calendar.DAY_OF_WEEK) + 5) % 7);
		when(mockedData.getOpeningTime(1)).thenReturn(ot);
		when(ot2.getMorningStart() != null).thenReturn(true);
		when(ot2.getAfternoonStart() != null).thenReturn(true);
		assertTrue(oth.isOpenDay(cal.getTime()));
	}

	/**
	 * Whitebox-Test für die Methode isOpenDay(Date day)
	 * für den Fall, dass nur nachmittags geöffnet ist.
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test
	public void testIsOpenDay2() throws DataSourceException,
	NoSuchBusinessObjectExistsException {
		/* Wochentag Mittwoch in der Datenbank */
		OpeningTime ot = new OpeningTime();
		ot.setId(2);
		ot.setDay("Mittwoch");
		ot.setAfternoonStart(String.valueOf(14));
		ot.setAfternoonEnd(String.valueOf(17));

		/* Übergebener Testtag (ein Mittwoch) */
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 2, 26);




		OpeningTime ot2 = oth.get((cal.get(Calendar.DAY_OF_WEEK) + 5) % 7);
		when(mockedData.getOpeningTime(2)).thenReturn(ot);
		when(ot2.getMorningStart() != null).thenReturn(true);
		when(ot2.getAfternoonStart() != null).thenReturn(true);
		assertTrue(oth.isOpenDay(cal.getTime()));
	}

	/**
	 * Whitebox-Test für die Methode isOpenDay(Date day)
	 * für den Fall, dass an dem Tag nicht geöffnet ist.
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test
	public void testIsOpenDay3() throws DataSourceException,
	NoSuchBusinessObjectExistsException {
		/* Wochentag Samstag in der Datenbank */
		OpeningTime ot = new OpeningTime();
		ot.setId(5);
		ot.setDay("Samstag");

		/* Übergebener Testtag (ein Samstag) */
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 2, 22);




		OpeningTime ot2 = oth.get((cal.get(Calendar.DAY_OF_WEEK) + 5) % 7);
		when(mockedData.getOpeningTime(5)).thenReturn(ot);
		when(ot2.getMorningStart() != null).thenReturn(true);
		when(ot2.getAfternoonStart() != null).thenReturn(true);
		assertTrue(!oth.isOpenDay(cal.getTime()));
	}

	/**
	 * Whitebox-Test für die Methode getOpenDays()
	 * für den Fall, dass mindestens ein geöffneter Tag existiert.
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	@Test
	public void testGetOpenDays() throws DataSourceException,
	NoSuchBusinessObjectExistsException {

		/* Öffnungstage in der Datenbank */
		List<OpeningTime> otimes = new ArrayList<>();
		for (int i = 0; i<weekdays.length; i++) {
			OpeningTime ot = new OpeningTime();
			ot.setId(i);
			ot.setDay(weekdays[i]);
			if (i % 2 == 0) { // nur Montag, Mittwoch, Freitag und Sonntag haben geöffnet
				ot.setMorningStart(String.valueOf(9));
				ot.setMorningEnd(String.valueOf(13));
			}
			otimes.add(ot);
		}




		/* Returnangaben bei Methodenaufruf im OpeningTimeHandler */
		for (int i = 0; i<weekdays.length; i++) {
			when(mockedData.getOpeningTime(i)).thenReturn(otimes.get(i));
			when(mockedData.getOpeningTime(i).getMorningStart() != null).thenReturn(true);
			when(mockedData.getOpeningTime(i).getAfternoonStart() != null).thenReturn(true);
		}

		boolean[] open = new boolean[7];
		for (int i = 0; i<weekdays.length; i++) {
			open[i] = otimes.get(i).getMorningStart() != null || otimes.get(i).getAfternoonStart() != null;
		}
		assertTrue(oth.getOpenDays() == open);
	}

	/**
	 * Whitebox-Test für die Methode openDaysExist()
	 * für den Fall, dass kein geöffneter Tag existiert.
	 * @throws DataSourceException
	 */
	@Test
	public void testOpenDaysExist() throws DataSourceException {

		/* Öffnungstage in der Datenbank */
		List<OpeningTime> otimes = new ArrayList<>();
		for (int i = 0; i<weekdays.length; i++) {
			OpeningTime ot = new OpeningTime();
			ot.setId(i);
			ot.setDay(weekdays[i]);
			otimes.add(ot);
		}




		/* Returnangaben bei Methodenaufruf im OpeningTimeHandler */
		when(mockedData.getAllOpeningTimes()).thenReturn(otimes);

		for (OpeningTime ot : otimes) {
			when(ot.getMorningStart() != null).thenReturn(true);
			when(ot.getAfternoonStart() != null).thenReturn(true);
		}

		assertTrue(oth.openDaysExist() == false);
	}

	/**
	 * Whitebox-Test für die Methode openDaysExist()
	 * für den Fall, dass mindestens ein geöffneter Tag existiert.
	 * @throws DataSourceException
	 */
	@Test
	public void testOpenDaysExist2() throws DataSourceException {

		/* Öffnungstage in der Datenbank */
		List<OpeningTime> otimes = new ArrayList<>();
		for (int i = 0; i<weekdays.length; i++) {
			OpeningTime ot = new OpeningTime();
			ot.setId(i);
			ot.setDay(weekdays[i]);
			if (i % 2 == 0) { // nur Montag, Mittwoch, Freitag und Sonntag haben geöffnet
				ot.setAfternoonStart(String.valueOf(14));
				ot.setAfternoonEnd(String.valueOf(17));
			}
			otimes.add(ot);
		}




		/* Returnangaben bei Methodenaufruf im OpeningTimeHandler */
		when(mockedData.getAllOpeningTimes()).thenReturn(otimes);

		for (OpeningTime ot : otimes) {
			when(ot.getMorningStart() != null).thenReturn(true);
			when(ot.getAfternoonStart() != null).thenReturn(true);
		}

		assertTrue(oth.openDaysExist() == true);
	}


	/**
	 * Whitebox-Test für die Methode importCSV(InputStream inputstream).
	 * Da CSV-Datein von Öffnungszeiten-Objekten nicht importiert werden können,
	 * wird hier eine NotImplementedException erwartet.
	 * @throws DataSourceException
	 */
	@Test(expected = NotImplementedException.class)
	public void testImportCSV() throws DataSourceException {
		oth.importCSV(null);
	}

	/**
	 * Whitebox-Test für die Methode exportCSV(OutputStream outStream).
	 * Da CSV-Datein von Öffnungszeiten-Objekten nicht exportiert werden können,
	 * wird hier eine NotImplementedException erwartet.
	 * @throws DataSourceException
	 */
	@Test(expected = NotImplementedException.class)
	public void testExportCSV() throws DataSourceException {
		oth.exportCSV(null);
	}
}
