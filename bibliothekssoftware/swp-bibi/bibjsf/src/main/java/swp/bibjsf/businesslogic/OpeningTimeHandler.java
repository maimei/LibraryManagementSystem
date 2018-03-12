package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.OpeningTime;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.OrderBy;

/**
 * Handler für Öffnungszeiten.
 *
 * @author Helena Meißner, Bernd Poppinga
 *
 */
public class OpeningTimeHandler extends BusinessObjectHandler<OpeningTime> {

    /**
     * Serialisierungs-ID.
     */
    private static final long serialVersionUID = -648258085688242594L;

    /**
     * Eine Instanz einer Öffnungszeit, die als Prototyp von Objekten für diesen
     * Handler dient.
     */
    private static OpeningTime prototype = new OpeningTime();

    /**
     * Returns the only instance of ExtensionHandler (singleton).
     */
    private static volatile OpeningTimeHandler instance;

    /**
     * Konstante für die Anzahl der Wochentage.
     */
    private static int weekdays = 7;

    protected OpeningTimeHandler() throws DataSourceException, NamingException {
        super();
    }

    /**
     * Konstruktor zum Testen.
     *
     * @param testing
     *            Das gemockte Data-Objekt.
     */
    public OpeningTimeHandler(Persistence testing) {
        super(testing);
    }

    /**
     *
     * Returns the one possible instance of this class. If there is no instance,
     * a new one is created. (Singleton)
     *
     * @return the one instance of this class.
     *
     * @throws DataSourceException
     *             is thrown if there are issues with the persistence component.
     */
    public static synchronized OpeningTimeHandler getInstance()
            throws DataSourceException {
        if (instance == null) {
            try {
                instance = new OpeningTimeHandler();
            } catch (Exception e) {
                throw new DataSourceException(e.getMessage());
            }
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
     */
    @Override
    public OpeningTime get(int id) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        return persistence.getOpeningTime(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public List<OpeningTime> get(List<Constraint> constraints, int from,
            int to, List<OrderBy> order) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        return persistence.getAllOpeningTimes();
    }

    /*
     * die Methode existiert nur aufgrund von Vererbung, wird aber nicht
     * verwendet.
     */
    @Override
    public int getNumber(List<Constraint> constraints)
            throws DataSourceException {
        return weekdays;
    }

    /*
     * die Methode existiert nur aufgrund von Vererbung, wird aber nicht
     * verwendet.
     */
    @Override
    public int add(OpeningTime element) throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int,
     * java.lang.Object)
     */
    @Override
    public int update(OpeningTime openingTime) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        return persistence.updateOpeningTime(openingTime);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
     */
    @Override
    public OpeningTime getPrototype() {
        return prototype;
    }

    /*
     * die Methode existiert nur aufgrund von Vererbung, wird aber nicht
     * verwendet.
     */
    @Override
    public void delete(List<OpeningTime> elements) throws DataSourceException {
        throw new NotImplementedException();
    }

    /**
     * prüft, ob die Bibliothek am übergebenen Wochentag geöffnet ist.
     *
     * @param day
     *            der zu prüfende Wochentag.
     * @return true, falls geöffnet, sonst false.
     * @throws NoSuchBusinessObjectExistsException
     * @throws DataSourceException
     */
    public boolean isOpenDay(Date day) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        OpeningTime openingTime = get((cal.get(Calendar.DAY_OF_WEEK) + 5) % 7);
        return (openingTime.getMorningStart() != null || openingTime
                .getAfternoonStart() != null);
    }

    /**
     * Liest für jeden Wochentag aus der DB aus, ob er geöffnet ist.
     *
     * @return Array mit Boolwerten das angibt, ob geöffnet ist
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     */
    public boolean[] getOpenDays() throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        boolean[] open = new boolean[7];
        for (int i = 0; i < open.length; i++) {
            OpeningTime openingTime = get(i);
            open[i] = (openingTime.getMorningStart() != null || openingTime
                    .getAfternoonStart() != null);
        }
        return open;
    }

    /**
     * Überprüft, ob es mindestens einen geöffneten Wochentag gibt.
     *
     * @return true, falls es mindestens einen geöffneten Tag gibt, sonst false
     * @throws DataSourceException
     */
    public boolean openDaysExist() throws DataSourceException {
        List<OpeningTime> openingTimes = persistence.getAllOpeningTimes();
        for (OpeningTime ot : openingTimes) {
            if (ot.getMorningStart() != null || ot.getAfternoonStart() != null) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.businesslogic.BusinessObjectHandler#importCSV(java.io.InputStream
     * )
     */
    @Override
    public int importCSV(InputStream inputstream) throws DataSourceException {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.businesslogic.BusinessObjectHandler#exportCSV(java.io.OutputStream
     * )
     */
    @Override
    public void exportCSV(OutputStream outStream) throws DataSourceException {
        throw new NotImplementedException();
    }
}
