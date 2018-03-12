package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.MediumType;
import swp.bibcommon.Reader;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle Ausleih-Daten. Es implementiert die
 * Geschäftsregeln, die auf Ausleihen zutreffen.
 *
 * @author Helena Meißner, Bernd Poppinga
 *
 */
public class LendingHandler extends BusinessObjectHandler<Lending> {

    /**
     * Serialisierungs-ID.
     */
    private static final long serialVersionUID = 7090189116572552089L;

    /**
     * Die einzige Instanz dieser Klasse (singleton).
     */
    private static volatile LendingHandler instance;

    /**
     * An instance of Lending acting as the prototype of objects handled by this
     * handler.
     */
    private static Lending prototype = new Lending();

    /**
     * Eine Instanz eines OpeningTimeHandlers.
     */
    private final OpeningTimeHandler opTimeHandler;

    /**
     * Eine Instanz eines Reader Handlers.
     */
    private final ReaderHandler rH;

    /**
     * Eine Instanz eines Exemplar Handlers.
     */
    private final ExemplarHandler eH;

    /**
     * Eine Instanz eines ClosedTimeHandlers.
     */
    private final ClosedTimeHandler clTimeHandler;

    protected LendingHandler() throws DataSourceException, NamingException {
        super();
        eH = new ExemplarHandler();
        rH = new ReaderHandler();
        opTimeHandler = new OpeningTimeHandler();
        clTimeHandler = new ClosedTimeHandler();
    }

    /**
     * Konstruktor zum Testen.
     *
     * @param p
     * @param eH
     * @param rh
     * @param oH
     * @param cH
     * @throws DataSourceException
     * @throws NamingException
     */
    public LendingHandler(Persistence p, ExemplarHandler eH, ReaderHandler rH,
            OpeningTimeHandler oH, ClosedTimeHandler cH) {
        super(p);
        this.eH = eH;
        this.rH = rH;
        this.opTimeHandler = oH;
        this.clTimeHandler = cH;
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
    public static synchronized LendingHandler getInstance()
            throws DataSourceException {

        if (instance == null) {
            try {
                instance = new LendingHandler();
            } catch (Exception e) {
                throw new DataSourceException(e.getMessage());
            }
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
     */
    @Override
    public synchronized int add(Lending lending) throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {

        logger.info("add lending: " + lending);
        if (lending.hasId() && persistence.getLending(lending.getId()) != null) {
            logger.info("lending already exists and could not be added: "
                    + lending);
            throw new BusinessElementAlreadyExistsException(
                    Messages.get("lendingexists") + " " + Messages.get("id")
                            + " = " + lending.getId());
        }

        rH.updateLastUse(lending.getReaderID());
        Reader r = rH.get(lending.getReaderID());
        Exemplar e = eH.get(lending.getExemplarID());
        e.setLatestReader(r.getId());
        e.setLendingCount(e.getLendingCount() + 1);
        e.setStatus("verliehen");
        eH.update(e);
        int result = persistence.addLending(lending);
        if (result < 0) {
            throw new DataSourceException(
                    Messages.get("lendingAdditionFailure"));
        }
        String exId = String.valueOf(lending.getExemplarID());
        logger.info("lending added: "
                + persistence.getReader(lending.getReaderID()).getUsername()
                + "; lending exemplar:" + exId);
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int,
     * java.lang.Object)
     */
    @Override
    public synchronized int update(Lending newValue)
            throws DataSourceException, NoSuchBusinessObjectExistsException {
        return persistence.updateLending(newValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
     */
    @Override
    public synchronized Lending get(int id) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        return persistence.getLending(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public synchronized List<Lending> get(List<Constraint> constraints,
            int from, int to, List<OrderBy> order) throws DataSourceException {
        return persistence.getLendings(constraints, from, to, order);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
     */
    @Override
    public synchronized int getNumber(List<Constraint> constraints)
            throws DataSourceException {
        return persistence.getNumberOfLendings(constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
     */
    @Override
    public synchronized void delete(List<Lending> elements)
            throws DataSourceException {
        for (Lending lending : elements) {
            persistence.deleteLending(lending);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
     */
    @Override
    public synchronized Lending getPrototype() {
        return prototype;
    }

    /**
     * gibt die aktuellen Daten der Leihe zurück Orientiert sich am Enddatum der
     * Leihe und am aktuellen Datum.
     *
     * @param lendingID
     *            ID der Leihe, deren Mahngebühren errechnet werden sollen.
     * @return data 0: Überziehungstage, 1: berechnete Tage, 2: Toleranz
     * @throws NoSuchBusinessObjectExistsException
     * @throws DataSourceException
     */
    public int[] calcFeeData(Lending lending) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(lending.getTill());
        cal.add(Calendar.DATE, 1);
        Date lendingEnd = cal.getTime();
        Set<Date> feeDays = new HashSet<>();
        Set<Date> closed = clTimeHandler.getClosedDays(lendingEnd, today);
        boolean[] open = opTimeHandler.getOpenDays();
        int[] data = new int[3];
        data[0] = 0;
        cal = Calendar.getInstance();
        cal.setTime(lendingEnd);
        while (cal.getTime().before(today) || cal.getTime().equals(today)) {
            data[0]++;
            if (open[((cal.get(Calendar.DAY_OF_WEEK) + 5) % 7)]) {
                feeDays.add(cal.getTime());
            }
            cal.add(Calendar.DATE, 1);
        }
        feeDays.removeAll(closed);
        data[1] = feeDays.size();
        data[2] = Integer.parseInt(persistence.getProperty(1).getValue());
        return data;
    }

    /**
     * gibt die aktuellen Daten der Leihe zurück Orientiert sich am Enddatum der
     * Leihe und am aktuellen Datum.
     *
     * @param lendingID
     *            ID der Leihe, deren Mahngebühren errechnet werden sollen.
     * @return data 0: Überziehungstage, 1: berechnete Tage, 2: Toleranz
     * @throws NoSuchBusinessObjectExistsException
     * @throws DataSourceException
     */
    public BigDecimal calcFeeByData(Lending lending, int[] data)
            throws DataSourceException, NoSuchBusinessObjectExistsException {
        BigDecimal standardFee = getMediumTypeByExemplarID(
                lending.getExemplarID()).getFee();
        standardFee = standardFee.multiply(new BigDecimal(data[1] - data[2]));
        if (standardFee.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        } else {
            return standardFee;
        }
    }

    /**
     * gibt die aktuellen Daten der Leihe zurück Orientiert sich am Enddatum der
     * Leihe und am aktuellen Datum.
     *
     * @param lendingID
     *            ID der Leihe, deren Mahngebühren errechnet werden sollen.
     * @return data 0: Überziehungstage, 1: berechnete Tage, 2: Toleranz
     * @throws NoSuchBusinessObjectExistsException
     * @throws DataSourceException
     */
    public BigDecimal calcFee(Lending lending) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        return calcFeeByData(lending, calcFeeData(lending));
    }

    /**
     * Ermittelt den Medientyp eines Exemplars anhand seiner ID.
     *
     * @param exemplarID
     *            die ID des Exemplars
     * @return den Medientyp des Exemplars
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     */
    public MediumType getMediumTypeByExemplarID(int exemplarID)
            throws DataSourceException, NoSuchBusinessObjectExistsException {
        ExemplarHandler eh = ExemplarHandler.getInstance();
        Exemplar ex = eh.get(exemplarID);
        MediumHandler mh = MediumHandler.getInstance();
        Medium med = mh.get(ex.getMediumID());
        MediumTypeHandler mth = MediumTypeHandler.getInstance();
        return mth.get(med.getMediumType());
    }

    /**
     * Überprüft, ob der übergebene Tag geöffnet ist.
     *
     * @param date
     *            der zu prüfende Tag.
     * @return true, falls die Bibliothek an dem Tag geöffnet ist, sonst false.
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     */
    public boolean isOpenDay(Date date) throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        if (opTimeHandler.isOpenDay(date)) {
            if (!clTimeHandler.isClosedDay(date)) {
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
