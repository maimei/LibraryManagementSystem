package swp.bibjsf.businesslogic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.Reservation;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Constraint.AttributeType;

/**
 * Stellt die verschiedenen Statistiken dar, die dem Bibliothekar/Admin
 * angezeigt werden können.
 *
 * @author Helena Meißner
 */
public class Statistics extends BusinessHandler {

    /**
     * Serialisierungs-ID.
     */
    private static final long serialVersionUID = -4797842564073146511L;

    /**
     * Is a singleton.
     */
    private static volatile Statistics instance;

    /**
     * Anzahl an Wochentagen
     */
    private static final int WEEK_DAY_COUNT = 7;

    /**
     * Maximale Anzahl an Tagen im Monat
     */
    private static final int MONTH_DAY_COUNT = 31;

    /**
     * Instanz des Gregorianischen Kalenders.
     */
    Calendar cal = new GregorianCalendar();

    private Statistics() throws DataSourceException, NamingException {
        super();
    }

    /**
     * Konstruktor fürs Testen.
     *
     * @param testing
     *            Das gemockte Data-Objekt.
     */
    public Statistics(Persistence testing) {
        super(testing);
    }

    /**
     * Returns the one possible instance of this class. If there is no instance,
     * a new one is created. (Singleton)
     *
     * @return the one instance of this class.
     *
     * @throws DataSourceException
     *             is thrown if there are issues with the persistence component.
     */
    public static synchronized Statistics getInstance()
            throws DataSourceException {
        if (instance == null) {
            try {
                instance = new Statistics();
            } catch (Exception e) {
                throw new DataSourceException(e.getMessage());
            }
        }
        return instance;
    }

    /* Ausleihzeiten */

    /**
     * Gibt eine Liste aller Wochentage mit der Anzahl ihrer Ausleihen zurück.
     * Dabei werden alle Ausleihen berücksichtigt, die sich zum aktuellen
     * Zeitpunkt in der Datenbank befinden.
     *
     * @return eine Liste aller Wochentage.
     * @throws DataSourceException
     *             bei Zugriffproblemen auf die Datenbank wird eine Exception
     *             geworfen.
     */
    public int[] lendingTimesPerWeek() throws DataSourceException {
        List<Lending> lendings = persistence.getLendings(null, 0,
                Integer.MAX_VALUE, null);

        /* Liste der Wochentage beginnend bei 1=Sonntag usw. bis 7=Samstag */
        int[] daysOfWeek = new int[WEEK_DAY_COUNT + 1];

        /* Bestimmt die Anzahl an Ausleihen für jeden Wochentag */
        for (Lending lending : lendings) {
            cal.setTime(lending.getStart());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            daysOfWeek[dayOfWeek]++;
        }
        return daysOfWeek;
    }

    /**
     * Gibt eine Liste aller Tage im Monat mit der Anzahl ihrer Ausleihen
     * zurück. Dabei werden alle Ausleihen berücksichtigt, die sich zum
     * aktuellen Zeitpunkt in der Datenbank befinden.
     *
     * @return eine Liste aller Monatstage.
     * @throws DataSourceException
     *             bei Zugriffproblemen auf die Datenbank wird eine Exception
     *             geworfen.
     */
    public int[] lendingTimesPerMonth() throws DataSourceException {
        List<Lending> lendings = persistence.getLendings(null, 0,
                Integer.MAX_VALUE, null);

        /* Liste der Tage im Monat beginnend bei 0 = 1.Tag usw. */
        int[] daysOfMonth = new int[MONTH_DAY_COUNT + 1];

        for (Lending lending : lendings) {
            cal.setTime(lending.getStart());
            int monthDay = cal.get(Calendar.DAY_OF_MONTH);
            daysOfMonth[monthDay]++;
        }
        return daysOfMonth;
    }

    /* Vormerkzeiten */

    /**
     * Gibt eine Liste aller Tage im Monat mit der Anzahl ihrer Vormerkungen
     * zurück. Dabei werden alle Vormerkungen, die sich zum aktuellen Zeitpunkt
     * in der Datenbank befinden, berücksichtigt.
     *
     * @return Der Wochentag als String.
     */
    public int[] reservationTimesPerWeek() throws DataSourceException {
        List<Reservation> reservations = persistence.getReservations(null, 0,
                Integer.MAX_VALUE, null);

        /* Liste der Wochentage beginnend bei 1=Sonntag usw. bis 7=Samstag */
        int[] daysOfWeek = new int[WEEK_DAY_COUNT + 1];

        /* Bestimmt die Anzahl an Ausleihen für jeden Wochentag */
        for (Reservation reservation : reservations) {
            cal.setTime(reservation.getReservationDate());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            daysOfWeek[dayOfWeek]++;
        }
        return daysOfWeek;
    }

    /**
     * Gibt eine Liste aller Tage im Monat mit der Anzahl ihrer Vormerkungen
     * zurück. Dabei werden alle Ausleihen berücksichtigt, die sich zum
     * aktuellen Zeitpunkt in der Datenbank befinden.
     *
     * @return eine Liste aller Monatstage.
     * @throws DataSourceException
     *             bei Zugriffproblemen auf die Datenbank wird eine Exception
     *             geworfen.
     */
    public int[] reservationTimesPerMonth() throws DataSourceException {
        List<Reservation> reservations = persistence.getReservations(null, 0,
                Integer.MAX_VALUE, null);

        /* Liste der Tage im Monat beginnend bei 0 = 1.Tag usw. */
        int[] daysOfMonth = new int[MONTH_DAY_COUNT + 1];

        for (Reservation reservation : reservations) {
            cal.setTime(reservation.getReservationDate());
            int monthDay = cal.get(Calendar.DAY_OF_MONTH);
            daysOfMonth[monthDay]++;
        }
        return daysOfMonth;
    }

    /* Bibliothek in Zahlen */

    /**
     * Gibt die durchschnittliche Anzahl der Ausleihen pro Leser auf Basis des
     * aktuellen Bestands an Lesern und Ausleihen zurück.
     *
     * @return durchschnittliche Anzahl an Ausleihen als gerundeter Wert.
     *         zugegriffen werden.
     * @throws DataSourceException
     */
    public String lendingsPerReader() throws DataSourceException {
        double count;
        int lendingCount;
        int readerCount;
        lendingCount = persistence.getNumberOfLendings(null);
        readerCount = persistence.getNumberOfReaders(null);

        if (readerCount == 0) {
            return "0,00";
        }

        count = lendingCount / (double) readerCount;
        NumberFormat numberFormat = new DecimalFormat("0.00");
        return numberFormat.format(count);
    }

    /**
     * Liefert die Anzahl an Nutzern zurück.
     *
     * @return die Anzahl an Nutzern.
     * @throws DataSourceException
     */
    public int getReaderCount() throws DataSourceException {
        return persistence.getNumberOfReaders(null);
    }

    /**
     * Liefert die Anzahl an aktiven Nutzern.
     *
     * @return die Anzahl an Nutzern
     * @throws DataSourceException
     */
    public int getActiveReaderCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        Constraint con = new Constraint("status", "=", "active", "OR",
                AttributeType.STRING);
        cons.add(con);
        return persistence.getNumberOfReaders(cons);
    }

    /**
     * Liefert die Anzahl an inaktiven Nutzern.
     *
     * @return die Anzahl an Nutzern
     * @throws DataSourceException
     */
    public int getInactiveReaderCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        Constraint con = new Constraint("status", "=", "inactive", "OR",
                AttributeType.STRING);
        cons.add(con);
        return persistence.getNumberOfReaders(cons);
    }

    /**
     * Liefert die Anzahl an blockierten Nutzern.
     *
     * @return die Anzahl an Nutzern
     * @throws DataSourceException
     */
    public int getBlockedReaderCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        Constraint con = new Constraint("status", "=", "blocked", "OR",
                AttributeType.STRING);
        cons.add(con);
        return persistence.getNumberOfReaders(cons);
    }

    /**
     * Liefert die Anzahl an Ausleihen, deren Mahngebühren noch nicht beglichen
     * sind.
     *
     * @return die Anzahl an Ausleihen
     * @throws DataSourceException
     */
    public int getFeeLendingCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        Constraint con = new Constraint("paid", "=", "false", "AND",
                AttributeType.STRING);
        Constraint con2 = new Constraint("returned", "=", "true", "AND",
                AttributeType.STRING);
        cons.add(con);
        cons.add(con2);
        return persistence.getNumberOfLendings(cons);
    }

    /**
     * Liefert die Anzahl an aktuell laufenden Ausleihen.
     *
     * @return Anzahl an aktuell laufenden Ausleihen
     * @throws DataSourceException
     */
    public int getActiveLendingCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("paid", "=", "false", "AND",
                AttributeType.STRING));
        cons.add(new Constraint("returned", "=", "false", "AND",
                AttributeType.STRING));
        return persistence.getNumberOfLendings(cons);
    }

    /**
     * Liefert die Anzahl aller Medien.
     *
     * @return die Anzahl aller Medien.
     * @throws DataSourceException
     */
    public int getMediaCount() throws DataSourceException {
        return persistence.getNumberOfMediums(null);
    }

    /**
     * Liefert die Anzahl aller Exemplare.
     *
     * @return die Anzahl aller Exemplare.
     * @throws DataSourceException
     */
    public int getExemplarCount() throws DataSourceException {
        return persistence.getNumberOfExemplars(null);
    }

    /**
     * Liefert die Anzahl aller leihbaren Exemplare.
     *
     * @return Anzahl aller leihbaren Exemplare
     * @throws DataSourceException
     */
    public int getLendableExemplarCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("status", "=", "leihbar", "AND",
                AttributeType.STRING));
        return persistence.getNumberOfExemplars(cons);
    }

    /**
     * Liefert die Anzahl aller ausgeliehenen Exemplare.
     *
     * @return Anzahl aller ausgeliehenen Exemplare.
     * @throws DataSourceException
     */
    public int getLentExemplarCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("status", "=", "verliehen", "AND",
                AttributeType.STRING));
        return persistence.getNumberOfExemplars(cons);
    }

    /**
     * Liefert die Anzahl aller blockierten Exemplare.
     *
     * @return die Anzahl aller blockierten Exemplare
     * @throws DataSourceException
     */
    public int getBlockedExemplarCount() throws DataSourceException {
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("status", "=", "blockiert", "AND",
                AttributeType.STRING));
        return persistence.getNumberOfExemplars(cons);
    }

    /* Bewertungen */

    /**
     * Liefert alle Medien unter bzw. gleich oder über rating zurück.
     *
     * @param number
     *            gibt an, dass die number best/schlechtest bewerteten Medien
     *            ausgegeben werden sollen.
     * @param popularity
     *            gibt an, ob die best oder schlechtest bewerteten Medien
     *            ausgegeben werden sollen.
     * @return die Liste von gefilterten Medien.
     */
    public List<Medium> getRatedMedia(int number, boolean popularity)
            throws DataSourceException {
        if (number <= 0) {
            throw new IllegalArgumentException();
        }
        return persistence.getRatedMedia(number, popularity);
    }

    /* häufig/selten geliehene Medien */

    /**
     * Gibt die number Medien zurück, die am häufigsten bzw. am seltensten
     * ausgeliehen werden.
     *
     * @param number
     *            gibt an, dass die number häufigst/seltenst ausgeliehenen
     *            Medien ausgegeben werden sollen.
     * @param popularity
     *            gibt an, ob die häufigst oder seltenst ausgeliehenen Medien
     *            ausgegeben werden sollen.
     * @return die Liste von gefilterten Medien.
     */
    public List<Medium> getLentMedia(int number, boolean popularity)
            throws DataSourceException {
        int numberOfMedia = persistence.getNumberOfMediums(null);
        if (number <= 0) {
            throw new IllegalArgumentException();
        } else if (number > numberOfMedia) {
            number = numberOfMedia;
        }
        List<Medium> list = persistence.getLentMedia(number, popularity);
        return list;
    }

    /**
     * Liefert die Anzahl an Ausleihen zu einem Medium zurück.
     *
     * @param medium
     *            das Medium.
     * @return die Anzahl an Ausleihen.
     * @throws DataSourceException
     */
    public int getLendingCountForMedium(Medium medium)
            throws DataSourceException {
        List<Constraint> cons = new ArrayList<>();
        cons.add(new Constraint("mediumID", "=", "" + medium.getId(), "OR",
                Constraint.AttributeType.INTEGER));
        List<Exemplar> exemplars = persistence.getExemplars(cons, 0, 50000,
                null);
        int count = 0;
        for (Exemplar exemplar : exemplars) {
            count += exemplar.getLendingCount();
        }
        return count;
    }
}
