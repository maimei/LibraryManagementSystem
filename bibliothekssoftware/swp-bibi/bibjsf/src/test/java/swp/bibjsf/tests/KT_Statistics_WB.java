package swp.bibjsf.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
import org.mockito.Matchers;
import org.mockito.Mockito;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.businesslogic.Statistics;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;

/**
 * Whitebox-Testklasse für die Klasse Statistics der BusinessLogic.
 *
 * @author Bernd Poppinga, Eike Externest
 */
public class KT_Statistics_WB {

    /**
     * Gemocktes Data-Objekt.
     */
    private Persistence mockedData;

    /**
     * Instanz der Statistics
     */
    private Statistics stats;

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

    @Before
    public void setup() throws DataSourceException, NamingException {
        mockedData = mock(Persistence.class);
        stats = new Statistics(mockedData);
    }

    /* Ausleihzeiten */

    /**
     * Whiteboxtest für lendingTimesPerWeek() für den Fall, dass Ausleihen in
     * der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testLendingTimesPerWeekNormal() throws DataSourceException {

        cal = Calendar.getInstance();
        List<Lending> lendings = new ArrayList<>();
        Lending lend1 = new Lending();
        cal.set(2014, 2, 22); // Samstag
        lend1.setStart(cal.getTime());
        Lending lend2 = new Lending();
        cal.set(2014, 2, 23); // Sonntag
        lend1.setStart(cal.getTime());
        Lending lend3 = new Lending();
        cal.set(2014, 2, 25); // Dienstag
        lend1.setStart(cal.getTime());
        lendings.add(lend1);
        lendings.add(lend2);
        lendings.add(lend3);

        int[] weekdays = new int[WEEK_DAY_COUNT + 1];
        weekdays[7]++;
        weekdays[1]++;
        weekdays[3]++;

        when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(lendings);

        assertArrayEquals(stats.lendingTimesPerWeek(), weekdays);
    }

    /**
     * Whiteboxtest für lendingTimesPerWeek() für den Fall, dass keine Ausleihen
     * in der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testLendingTimesPerWeekTrivial() throws DataSourceException {
        List<Lending> lendings = new ArrayList<>();
        int[] weekdays = new int[WEEK_DAY_COUNT + 1];

        when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(lendings);
        assertArrayEquals(stats.lendingTimesPerWeek(), weekdays);
    }

    /**
     * Whiteboxtest für lendingTimesPerMonth() für den Fall, dass Ausleihen in
     * der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testLendingTimesPerMonthNormal() throws DataSourceException {

        cal = Calendar.getInstance();
        List<Lending> lendings = new ArrayList<>();
        int[] daysOfMonth = new int[MONTH_DAY_COUNT + 1];

        /* Erzeugt Ausleihen an geraden Tagen in Monat: 2, 4, 6 etc. */
        for (int i = 1; i < MONTH_DAY_COUNT + 1; i++) {
            if (i % 2 == 0) {
                Lending lending = new Lending();
                cal.set(2014, 2, i);
                lending.setStart(cal.getTime());
                lendings.add(lending);
            }
        }

        for (int i = 1; i < MONTH_DAY_COUNT + 1; i++) {
            if (i % 2 == 0) {
                daysOfMonth[i]++;
            }
        }

        when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(lendings);
        assertArrayEquals(stats.lendingTimesPerMonth(), daysOfMonth);
    }

    /**
     * Whiteboxtest für lendingTimesPerMonth() für den Fall, dass keine
     * Ausleihen in der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testLendingTimesPerMonthTrivial() throws DataSourceException {

        List<Lending> lendings = new ArrayList<>();
        int[] daysOfMonth = new int[MONTH_DAY_COUNT + 1];

        when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(lendings);

        assertArrayEquals(stats.lendingTimesPerMonth(), daysOfMonth);
    }

    /* Vormerkzeiten */

    /**
     * Whiteboxtest für reservationTimesPerWeek() für den Fall, dass
     * Vormerkungen in der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testReservationTimesPerWeekNormal() throws DataSourceException {

        cal = Calendar.getInstance();
        List<Reservation> reservations = new ArrayList<>();
        Reservation res1 = new Reservation();
        cal.set(2014, 2, 22); // Samstag
        res1.setReservationDate(cal.getTime());
        Reservation res2 = new Reservation();
        cal.set(2014, 2, 23); // Sonntag
        res2.setReservationDate(cal.getTime());
        Reservation res3 = new Reservation();
        cal.set(2014, 2, 25); // Dienstag
        res3.setReservationDate(cal.getTime());
        reservations.add(res1);
        reservations.add(res2);
        reservations.add(res3);

        int[] weekdays = new int[WEEK_DAY_COUNT + 1];
        weekdays[7]++;
        weekdays[1]++;
        weekdays[3]++;

        Mockito.doReturn(reservations).when(
                mockedData.getReservations(null, 0, Integer.MAX_VALUE, null));

        assertArrayEquals(stats.reservationTimesPerWeek(), weekdays);
    }

    /**
     * Whiteboxtest für reservationTimesPerWeek() für den Fall, dass keine
     * Vormerkungen in der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testReservationTimesPerWeekTrivial() throws DataSourceException {
        List<Reservation> reservations = new ArrayList<>();
        int[] weekdays = new int[WEEK_DAY_COUNT + 1];

        when(mockedData.getReservations(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(reservations);
        assertArrayEquals(stats.reservationTimesPerWeek(), weekdays);
    }

    /**
     * Whiteboxtest für reservationTimesPerMonth() für den Fall, dass
     * Vormerkungen in der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testReservationTimesPerMonthNormal() throws DataSourceException {
        cal = Calendar.getInstance();
        List<Reservation> reservations = new ArrayList<>();
        int[] daysOfMonth = new int[MONTH_DAY_COUNT + 1];

        /* Erzeugt Ausleihen an geraden Tagen in Monat: 2, 4, 6 etc. */
        for (int i = 1; i < MONTH_DAY_COUNT + 1; i++) {
            if (i % 2 == 0) {
                Reservation reservation = new Reservation();
                cal.set(2014, 2, i);
                reservation.setReservationDate(cal.getTime());
                reservations.add(reservation);
            }
        }

        for (int i = 1; i < MONTH_DAY_COUNT + 1; i++) {
            if (i % 2 == 0) {
                daysOfMonth[i]++;
            }
        }

        when(mockedData.getReservations(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(reservations);

        assertArrayEquals(stats.reservationTimesPerMonth(), daysOfMonth);
    }

    /**
     * Whiteboxtest für reservationTimesPerMonth() für den Fall, dass keine
     * Vormerkungen in der Datenbank existieren.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testReservationTimesPerMonthTrivial()
            throws DataSourceException {

        List<Reservation> reservations = new ArrayList<>();
        int[] daysOfMonth = new int[MONTH_DAY_COUNT + 1];

        when(mockedData.getReservations(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(reservations);

        assertArrayEquals(stats.reservationTimesPerMonth(), daysOfMonth);
    }

    /* Bibliothek in Zahlen */

    /**
     * Whiteboxtest für lendingsPerReader() für den Fall, dass sich sowohl
     * Ausleihen als auch Leser in der Datenbank befinden.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testLendingsPerReaderNormal() throws DataSourceException {

        List<Lending> lendings = new ArrayList<>();
        lendings.add(new Lending());
        lendings.add(new Lending());
        lendings.add(new Lending());

        List<Reader> readers = new ArrayList<>();
        readers.add(new Reader());
        readers.add(new Reader());

        when(mockedData.getNumberOfLendings(null)).thenReturn(lendings.size());
        when(mockedData.getNumberOfReaders(null)).thenReturn(readers.size());

        double count = lendings.size() / (double) readers.size();
        NumberFormat numberFormat = new DecimalFormat("0.00");
        String avgCount = numberFormat.format(count);

        assertTrue(stats.lendingsPerReader().equals(avgCount));
    }

    /**
     * Whiteboxtest für lendingsPerReader() für den Fall, dass sich weder
     * Ausleihen noch Leser in der Datenbank befinden.
     *
     * @throws DataSourceException
     */
    @Test
    public void testLendingsPerReaderTrivial() throws DataSourceException {
        when(mockedData.getNumberOfLendings(null)).thenReturn(0);
        when(mockedData.getNumberOfReaders(null)).thenReturn(0);

        assertTrue(stats.lendingsPerReader().equals("0,00"));
    }

    /**
     * Whiteboxtest für getReaderCount() für den Fall, dass sich Leser in der
     * Datenbank befinden.
     *
     * @throws DataSourceException
     */
    @Test
    public void testGetReaderCountNormal() throws DataSourceException {

        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            readers.add(new Reader());
        }

        when(mockedData.getNumberOfReaders(null)).thenReturn(readers.size());

        assertTrue(stats.getReaderCount() == readers.size());
    }

    /**
     * Whiteboxtest für getActiveReaderCount() für den Fall, dass sich 3 aktive
     * Leser in der Datenbank befinden.
     *
     * @throws DataSourceException
     */
    @Test
    public void testGetActiveReaderCountNormal() throws DataSourceException {
        Mockito.doReturn(3).when(mockedData)
                .getNumberOfReaders(Matchers.anyListOf(Constraint.class));

        assertTrue(stats.getInactiveReaderCount() == 3);
    }

    /**
     * Whiteboxtest für getInactiveReaderCount() für den Fall, dass sich 3
     * inaktive Leser in der Datenbank befinden.
     *
     * @throws DataSourceException
     */
    @Test
    public void testGetInactiveReaderCountNormal() throws DataSourceException {
        Mockito.doReturn(3).when(mockedData)
                .getNumberOfReaders(Matchers.anyListOf(Constraint.class));

        assertEquals(stats.getInactiveReaderCount(), 3);
    }

    /**
     * Whiteboxtest für getBlockedReaderCount() für den Fall, dass sich Leser in
     * der Datenbank befinden.
     *
     * @author Bernd Poppinga
     * @throws DataSourceException
     */
    @Test
    public void testGetBlockedReaderCountNormal() throws DataSourceException {
        Mockito.doReturn(3).when(mockedData)
                .getNumberOfReaders(Matchers.anyListOf(Constraint.class));
        assertTrue(stats.getBlockedReaderCount() == 3);
    }

    /**
     * Whiteboxtest für getFeeLendingCount() für den Fall, dass sich keine
     * offenen Mahngebühren der Datenbank befinden. Dieser Fall ist gleichzeitig
     * ein Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetFeeLendingCountNormal() throws DataSourceException {
        Mockito.doReturn(0).when(mockedData)
                .getNumberOfLendings(Matchers.anyListOf(Constraint.class));

        assertTrue(stats.getFeeLendingCount() == 0);
    }

    /**
     * Whiteboxtest für getActiveLendingCount() für den Fall, dass sich keine
     * Ausleihen der Datenbank befinden. Dieser Fall ist gleichzeitig ein
     * Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetActiveLendingCountNormal() throws DataSourceException {
        when(mockedData.getNumberOfLendings(null)).thenReturn(0);

        assertTrue(stats.getActiveLendingCount() == 0);
    }

    /**
     * Whiteboxtest für getMediaCount() für den Fall, dass sich keine Medien in
     * der Datenbank befinden. Dieser Fall ist gleichzeitig ein Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetMediaCountNormal() throws DataSourceException {
        when(mockedData.getNumberOfMediums(null)).thenReturn(0);

        assertTrue(stats.getActiveLendingCount() == 0);
    }

    /**
     * Whiteboxtest für getExemplarCount() für den Fall, dass sich keine
     * Exemplare in der Datenbank befinden. Dieser Fall ist gleichzeitig ein
     * Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetExemplarCountNormal() throws DataSourceException {
        when(mockedData.getNumberOfExemplars(null)).thenReturn(0);

        assertTrue(stats.getExemplarCount() == 0);
    }

    /**
     * Whiteboxtest für getLendableExemplarCount() für den Fall, dass sich keine
     * Exemplare in der Datenbank befinden. Dieser Fall ist gleichzeitig ein
     * Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetLendableExemplarCountNormal() throws DataSourceException {
        when(mockedData.getNumberOfExemplars(null)).thenReturn(0);

        assertTrue(stats.getLendableExemplarCount() == 0);
    }

    /**
     * Whiteboxtest für getLentExemplarCount() für den Fall, dass sich keine
     * Exemplare in der Datenbank befinden. Dieser Fall ist gleichzeitig ein
     * Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetLentExemplarCountNormal() throws DataSourceException {
        Mockito.doReturn(0).when(mockedData)
                .getNumberOfExemplars(Matchers.anyListOf(Constraint.class));

        assertTrue(stats.getLentExemplarCount() == 0);
    }

    /**
     * Whiteboxtest für getBlockedExemplarCount() für den Fall, dass sich keine
     * Exemplare in der Datenbank befinden. Dieser Fall ist gleichzeitig ein
     * Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetBlockedExemplarCount() throws DataSourceException {
        when(mockedData.getNumberOfExemplars(null)).thenReturn(0);

        assertTrue(stats.getBlockedExemplarCount() == 0);
    }

    /**
     * Whiteboxtest für getRatedMedia() für den Fall, dass eine negative Zahl
     * übergeben wird.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRatedMediaError() throws DataSourceException {
        stats.getRatedMedia(-1, true);
    }

    /**
     * Whiteboxtest für getRatedMedia() für den Fall, dass sich keine
     * Bewertungen in der Datenbank befinden. Dieser Fall ist gleichzeitig ein
     * Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetRatedMediaNormal() throws DataSourceException {
        when(mockedData.getRatedMedia(1, true)).thenReturn(
                new ArrayList<Medium>());

        assertTrue(stats.getRatedMedia(1, true).isEmpty());
    }

    /**
     * Whiteboxtest für getLentMedia() für den Fall, dass sich keine Medien in
     * der Datenbank befinden. Dieser Fall ist gleichzeitig ein Trivialfall.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetLentMedia() throws DataSourceException {

        when(mockedData.getNumberOfMediums(null)).thenReturn(0);
        when(mockedData.getLentMedia(1, true)).thenReturn(
                new ArrayList<Medium>());

        assertTrue(stats.getLentMedia(1, true).isEmpty());
    }

    /**
     * Whiteboxtest für testGetLentMedia() für den Fall, dass eine negative Zahl
     * übergeben wird.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLentMediaError() throws DataSourceException {
        stats.getRatedMedia(-1, true);
    }

    /**
     * Whiteboxtest für testGetLentMedia() für den Fall, dass eine negative Zahl
     * übergeben wird.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetLendingCountForMediumTrivial()
            throws DataSourceException {
        when(mockedData.getExemplars(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(new ArrayList<Exemplar>());

        assertTrue(stats.getLendingCountForMedium(new Medium()) == 0);
    }

    /**
     * Whiteboxtest für testGetLentMedia() für den Fall, dass eine negative Zahl
     * übergeben wird.
     *
     * @author Eike Externest
     * @throws DataSourceException
     */
    @Test
    public void testGetLendingCountForMediumNormal() throws DataSourceException {
        List<Exemplar> list = new ArrayList<Exemplar>();
        Exemplar e = new Exemplar();
        e.setLendingCount(1);
        list.add(e);

        Mockito.doReturn(list)
                .when(mockedData)
                .getExemplars(Matchers.anyListOf(Constraint.class), 0, 50000,
                        null);

        assertTrue(stats.getLendingCountForMedium(new Medium()) == 1);
    }
}
