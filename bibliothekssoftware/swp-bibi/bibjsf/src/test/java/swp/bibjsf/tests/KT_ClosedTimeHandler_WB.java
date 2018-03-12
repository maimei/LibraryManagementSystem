package swp.bibjsf.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;

import swp.bibcommon.ClosedTime;
import swp.bibcommon.Lending;
import swp.bibjsf.businesslogic.ClosedTimeHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.OpeningTimeHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Constraint.AttributeType;

/**
 * Testklasse für ClosedTimeHandler
 *
 * @author Niklas Bruns
 *
 */
public class KT_ClosedTimeHandler_WB {

    /**
     * Gemocktes Data-Objekt.
     */
    private Persistence mockedData;

    /**
     * Gemocktes LendingHandler-Objekt.
     */
    private LendingHandler mockedHandler;
    
    private OpeningTimeHandler mockedHandler2;

    private ClosedTimeHandler ctHandler;

    @Before
    public void setup() throws DataSourceException, NamingException {
        mockedData = mock(Persistence.class);
        mockedHandler = mock(LendingHandler.class);
        mockedHandler2 = mock(OpeningTimeHandler.class);
        ctHandler = new ClosedTimeHandler(mockedData, mockedHandler,mockedHandler2);
    }

    /**
     * Returns a date in the SQL date format.
     *
     * @param date
     * @return date in SQL date format
     */
    private String toDateFormat(Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate.toString();
    }

    /**
     * Whitebox Test für getNumber
     *
     * @throws DataSourceException
     * @throws NamingException
     * @author Niklas Bruns
     */
    @Test
    public void testGetNumber() throws DataSourceException, NamingException {

        when(mockedData.getNumberOfClosedTimes(null)).thenReturn(1);

        assertTrue(ctHandler.getNumber(null) == 1);
    }

    /**
     * Whitebox Test für importCSV
     *
     * @throws DataSourceException
     * @author Niklas Bruns
     */
    @Test(expected = NotImplementedException.class)
    public void testImportCSV() throws DataSourceException {
        ctHandler.importCSV(null);
    }

    /**
     * Whitebox Test für exportCSV
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Niklas Bruns
     */
    @Test(expected = NotImplementedException.class)
    public void testExportCSV() throws DataSourceException {
        ctHandler.exportCSV(null);
    }

    /**
     * Whitebox Test für get
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Niklas Bruns
     */
    @Test
    public void testGetInt() throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        ClosedTime ct = new ClosedTime();
        ct.setId(0);
        ct.setOccasion("Grund");
        ct.setStart(new Date());

        when(mockedData.getClosedTime(0)).thenReturn(ct);

        assertTrue(ctHandler.get(0).equals(ct));
    }

    /**
     * Whitebox Test für get List
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Niklas Bruns
     */
    @Test
    public void testGetListOfConstraintIntIntListOfOrderBy()
            throws DataSourceException, NoSuchBusinessObjectExistsException {

        when(mockedData.getClosedTimes(null, 0, 0, null)).thenReturn(
                new ArrayList<ClosedTime>());

        assertTrue(ctHandler.get(null, 0, 0, null).size() == 0);
    }

    /**
     * Whiteboxtest für addClosedTime Fall 1 (ClosedTime hat ne ID und ist in
     * Datenbank vorhanden)
     *
     * @author Niklas Bruns
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @throws BusinessElementAlreadyExistsException
     */
    @Test(expected = BusinessElementAlreadyExistsException.class)
    public void testAddClosedTimeFall1() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        ClosedTime time = new ClosedTime();
        time.setId(42);

        when(mockedData.getClosedTime(42)).thenReturn(new ClosedTime());

        ctHandler.add(time);
    }

    /**
     * Whiteboxtest für addClosedTime Fall 2 (ClosedTime hat keine ID und nicht
     * in Datenbank vorhanden,kann aber auch nicht hinzugefügt werden)
     *
     * @author Niklas Bruns
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @throws BusinessElementAlreadyExistsException
     */
    @Test(expected = DataSourceException.class)
    public void testAddClosedTimeFall2() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        ClosedTime time = new ClosedTime();
        time.setOccasion("Grund");
        time.setStart(new Date());
        time.setTill(new Date());

        when(mockedData.getClosedTime(anyInt())).thenReturn(null);
        when(mockedData.addClosedTime(any(ClosedTime.class))).thenReturn(-1);

        ctHandler.add(time);
    }

    /**
     * Whiteboxtest für addClosedTime Fall 3 (ClosedTime hat keine ID und nicht
     * in Datenbank vorhanden, wird hinzugefügt, Kein Lending vorhanden)
     *
     * @author Niklas Bruns
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @throws BusinessElementAlreadyExistsException
     */
    @Test
    public void testAddClosedTimeFall3() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        ClosedTime time = new ClosedTime();
        time.setOccasion("Grund");
        time.setStart(new Date());
        time.setTill(new Date());

        LendingHandler lH = mock(LendingHandler.class);
        when(mockedData.getClosedTime(anyInt())).thenReturn(null);
        when(mockedData.addClosedTime(any(ClosedTime.class))).thenReturn(0);

        List<Constraint> c = new ArrayList<Constraint>();
        c.add(new Constraint("till", ">", toDateFormat(Calendar.getInstance()
                .getTime()), "AND", AttributeType.STRING));
        c.add(new Constraint("returned", "=", "false", "AND",
                AttributeType.STRING));

        when(lH.getNumber(c)).thenReturn(0);
        when(lH.get(c, 0, 0, null)).thenReturn(new ArrayList<Lending>());

        ctHandler.add(time);
    }

    /**
     * Whiteboxtest für addClosedTime Fall 4 (ClosedTime hat keine ID und nicht
     * in Datenbank vorhanden, wird hinzugefügt, Lending vorhanden am ClosedDay)
     *
     * @author Niklas Bruns
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @throws BusinessElementAlreadyExistsException
     */
    @Test
    public void testAddClosedTimeFall4() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        ClosedTime time = new ClosedTime();
        time.setOccasion("Grund");
        time.setStart(new Date());
        time.setTill(new Date());

        LendingHandler lH = mock(LendingHandler.class);
        when(mockedData.getClosedTime(anyInt())).thenReturn(null);
        when(mockedData.addClosedTime(any(ClosedTime.class))).thenReturn(0);

        List<Constraint> c = new ArrayList<Constraint>();
        c.add(new Constraint("till", ">", toDateFormat(Calendar.getInstance()
                .getTime()), "AND", AttributeType.STRING));
        c.add(new Constraint("returned", "=", "false", "AND",
                AttributeType.STRING));

        when(lH.getNumber(c)).thenReturn(1);
        List<Lending> list = new ArrayList<Lending>();
        Lending l = new Lending();
        l.setExemplarID(0);
        l.setExtensions(0);
        l.setFee(new BigDecimal(0));
        l.setId(0);
        l.setPaid(false);
        l.setReaderID(0);
        l.setReturned(false);
        l.setStart(new Date());
        l.setTill(new Date());

        Date d = new Date();
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));

        List<ClosedTime> returnList = new ArrayList<ClosedTime>();
        ClosedTime c1 = new ClosedTime();
        c1.setId(0);
        c1.setOccasion("Grund");
        c1.setStart(d);
        c1.setTill(d);
        returnList.add(c1);

        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
                .thenReturn(returnList);

        list.add(l);

        when(lH.get(c, 0, 1, null)).thenReturn(list);

        ctHandler.add(time);
    }

    /**
     * Whiteboxtest für addClosedTime Fall 5 (ClosedTime hat keine ID und nicht
     * in Datenbank vorhanden, wird hinzugefügt, Lending vorhanden, aber nicht
     * am ClosedDay)
     *
     * @author Niklas Bruns
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @throws BusinessElementAlreadyExistsException
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testAddClosedTimeFall5() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        ClosedTime time = new ClosedTime();
        time.setStart(new Date());
        time.setTill(new Date());

        LendingHandler lH = mock(LendingHandler.class);
        when(mockedData.getClosedTime(anyInt())).thenReturn(null);
        when(mockedData.addClosedTime(any(ClosedTime.class))).thenReturn(0);

        List<Constraint> c = new ArrayList<Constraint>();
        c.add(new Constraint("till", ">", toDateFormat(Calendar.getInstance()
                .getTime()), "AND", AttributeType.STRING));
        c.add(new Constraint("returned", "=", "false", "AND",
                AttributeType.STRING));

        when(lH.getNumber(c)).thenReturn(1);
        List<Lending> list = new ArrayList<Lending>();
        Lending l = new Lending();
        l.setExemplarID(0);
        l.setExtensions(0);
        l.setFee(new BigDecimal(0));
        l.setId(0);
        l.setPaid(false);
        l.setReaderID(0);
        l.setReturned(false);
        l.setStart(new Date());
        l.setTill(new Date());

        Date d = new Date();
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));

        List<ClosedTime> returnList = new ArrayList<ClosedTime>();
        ClosedTime c1 = new ClosedTime();
        Date d2 = new Date();
        d2.setYear(2015);
        c1.setId(0);
        c1.setOccasion("Grund");
        c1.setStart(d);
        c1.setTill(d2);
        returnList.add(c1);

        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
                .thenReturn(returnList);

        list.add(l);

        when(lH.get(c, 0, 1, null)).thenReturn(list);

        ctHandler.add(time);
    }

    /**
     * Whiteboxtest für findNextOpenDay (keine Closed Days)
     *
     * @author Niklas Bruns
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     */
    @Test
    public void testFindNextOpenDay() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        Date d = new Date();
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));
        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
                .thenReturn(new ArrayList<ClosedTime>());
        
        when(mockedHandler2.isOpenDay(d)).thenReturn(true);
        assertTrue(ctHandler.findNextOpenDay(d).equals(d));

    }

    /**
     * Whitebox Test für FindNextOpenDay mit geschlossenen Tag(Heute)
     */
    public void testFindNextOpenDayFall2() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        Date d = new Date();
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));
        List<ClosedTime> returnList = new ArrayList<ClosedTime>();

        ClosedTime c = new ClosedTime();
        c.setId(0);
        c.setOccasion("Grund");
        c.setStart(d);
        c.setTill(d);
        returnList.add(c);

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, 1);

        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
                .thenReturn(returnList);

        assertTrue(ctHandler.findNextOpenDay(d).equals(cal.getTime()));
    }

    /**
     * Whitebox Test für Update
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Niklas Bruns
     */
    @Test(expected = NotImplementedException.class)
    public void testUpdateClosedTime() throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        ctHandler.update(null);
    }

    /**
     * Whitebox Test für delete
     *
     * @throws DataSourceException
     * @author Niklas Bruns
     */
    @Test(expected = NullPointerException.class)
    public void testDeleteListOfClosedTime() throws DataSourceException {
        ctHandler.delete(null);
        verify(mockedData).deleteClosedTime(null);
    }

    /**
     * Whitebox Fall für IsClosedDay ohne eingetragene ClosedDays
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Niklas Bruns
     */
    @Test
    public void testIsClosedDayFall1() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        Date d = new Date();
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));

        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
                .thenReturn(new ArrayList<ClosedTime>());

        assertTrue(ctHandler.isClosedDay(new Date()) == false);
    }

    /**
     * Whitebox Fall für IsClosedDay mit eingetragene ClosedDays(Gleicher Tag
     * also True)
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Niklas Bruns
     */
    @Test
    public void testIsClosedDayFall2() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        Date d = new Date();
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));
        List<ClosedTime> returnList = new ArrayList<ClosedTime>();

        ClosedTime c = new ClosedTime();
        c.setId(0);
        c.setOccasion("Grund");
        c.setStart(d);
        c.setTill(d);
        returnList.add(c);

        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
                .thenReturn(returnList);

        assertTrue(ctHandler.isClosedDay(d) == true);
    }

    /**
     * Whitebox Fall für IsClosedDay mit eingetragene ClosedDays(Gleicher Tag
     * also True)
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Niklas Bruns
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testIsClosedDayFall3() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        Date d = new Date();
        List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));
        List<ClosedTime> returnList = new ArrayList<ClosedTime>();
        d.setYear(1990);
        ClosedTime c = new ClosedTime();
        c.setId(0);
        c.setOccasion("Grund");
        c.setStart(d);
        c.setTill(d);
        returnList.add(c);

        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
                .thenReturn(returnList);

        assertTrue(ctHandler.isClosedDay(new Date()) == false);
    }

    /**
     * keine ClosedDays vorhanden.
     * @author Niklas Bruns
     * @throws DataSourceException 
     * @throws NoSuchBusinessObjectExistsException 
     */
    @Test
    public void testGetClosedDays() throws DataSourceException, NoSuchBusinessObjectExistsException {
    	Date d = new Date();
    	List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));
        
        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
        .thenReturn(new ArrayList<ClosedTime>());
        assertTrue(ctHandler.getClosedDays(d, d).equals(new HashSet<>()));
        
    }
    
    /**
     * der Tag ist ein ClosedDays vorhanden.
     * @author Niklas Bruns
     * @throws DataSourceException 
     * @throws NoSuchBusinessObjectExistsException 
     */
	@Test
    public void testGetClosedDays2() throws DataSourceException, NoSuchBusinessObjectExistsException {
    	Date d = new Date();
    	List<Constraint> cons = new ArrayList<Constraint>();
        cons.add(new Constraint("start", "<=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "AND", AttributeType.STRING));
        cons.add(new Constraint("till", ">=", DateFormatUtils.format(d,
                "yyyy-MM-dd"), "OR", AttributeType.STRING));
        
        ClosedTime c = new ClosedTime();
        c.setStart(new Date());
        c.setTill(new Date());
        List<ClosedTime> l = new ArrayList<ClosedTime>();
        		l.add(c);
        when(mockedData.getClosedTimes(cons, 0, Integer.MAX_VALUE, null))
        .thenReturn(l);
        assertTrue(ctHandler.getClosedDays(d, d).size() == 1);
        
    }

}
