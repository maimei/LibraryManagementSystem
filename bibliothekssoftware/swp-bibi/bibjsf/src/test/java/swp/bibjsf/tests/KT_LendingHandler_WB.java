package swp.bibjsf.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.mockito.internal.verification.Times;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Reader;
import swp.bibjsf.businesslogic.ClosedTimeHandler;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.OpeningTimeHandler;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;

/**
 *
 * @author
 */
public class KT_LendingHandler_WB {

    /**
     * Gemocktes Data-Objekt.
     */
    private Persistence mockedData;

    /**
     * Gemocktes ExemplarHandler-Objekt.
     */
    private ExemplarHandler eH;

    /**
     * Gemocktes ReaderHandler-Objekt.
     */
    private ReaderHandler rH;

    /**
     * Gemocktes OpeningTimeHandler-Objekt.
     */
    private OpeningTimeHandler oH;

    /**
     * Gemocktes ClosedTimeHandler-Objekt.
     */
    private ClosedTimeHandler cH;

    private LendingHandler lhandler;

    @Before
    public void setup() {
        mockedData = mock(Persistence.class);
        eH = mock(ExemplarHandler.class);
        rH = mock(ReaderHandler.class);
        oH = mock(OpeningTimeHandler.class);
        cH = mock(ClosedTimeHandler.class);
        lhandler = new LendingHandler(mockedData, eH, rH, oH, cH);
    }

    /**
     * Whitebox Test für get
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testGet() throws DataSourceException,
            NoSuchBusinessObjectExistsException {
        Lending lending = new Lending();
        lending.setExemplarID(30000);
        lending.setId(0);
        lending.setReaderID(1);

        when(mockedData.getLending(0)).thenReturn(lending);

        assertTrue(lhandler.get(0).equals(lending));
    }

    /**
     * Whitebox Test für importCSV
     *
     * @throws DataSourceException
     * @author Mathias Eggerichs
     */
    @Test(expected = NotImplementedException.class)
    public void testImportCSV() throws DataSourceException {
        lhandler.importCSV(null);
    }

    /**
     * Whitebox Test für exportCSV
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test(expected = NotImplementedException.class)
    public void testExportCSV() throws DataSourceException {
        lhandler.exportCSV(null);
    }

    /**
     * Whitebox Test für getNumber
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testGetNumber() throws DataSourceException, NamingException {

        when(mockedData.getNumberOfLendings(null)).thenReturn(1);

        assertTrue(lhandler.getNumber(null) == 1);
    }

    /**
     * Whitebox Test für update
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testUpdate() throws DataSourceException, NamingException,
            NoSuchBusinessObjectExistsException {
        Lending lending = new Lending();

        when(mockedData.updateLending(lending)).thenReturn(1);

        assertTrue(lhandler.update(lending) == 1);
    }

    /**
     * Whitebox Test für update
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testGetList() throws DataSourceException, NamingException,
            NoSuchBusinessObjectExistsException {
        Lending lending1 = new Lending();
        Lending lending2 = new Lending();
        lending1.setExemplarID(30000);
        lending1.setId(0);
        lending1.setReaderID(1);
        lending2.setExemplarID(30001);
        lending2.setId(1);
        lending2.setReaderID(2);

        List<Lending> lendingList = new ArrayList<Lending>();
        lendingList.add(lending1);
        lendingList.add(lending2);

        when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(lendingList);

        List<Lending> lendingListToCompare = new ArrayList<Lending>();
        lendingListToCompare = lhandler.get(null, 0, Integer.MAX_VALUE, null);
        assertTrue(lendingListToCompare.get(0).equals(lendingList.get(0)));
        assertTrue(lendingListToCompare.get(1).equals(lendingList.get(1)));
    }

    /**
     * Whitebox Test für update
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testGetListEmpty() throws DataSourceException, NamingException,
            NoSuchBusinessObjectExistsException {

        List<Lending> lendingList = new ArrayList<Lending>();

        when(mockedData.getLendings(null, 0, Integer.MAX_VALUE, null))
                .thenReturn(lendingList);

        lendingList = lhandler.get(null, 0, Integer.MAX_VALUE, null);
        assertTrue(lendingList.isEmpty());
    }

    /**
     * Whitebox Test für delete eine Ausleihe
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testDeleteOne() throws DataSourceException, NamingException {
        List<Lending> lendingsToDelete = new ArrayList<Lending>();
        Lending lending = new Lending();
        lendingsToDelete.add(lending);

        lhandler.delete(lendingsToDelete);
    }

    /**
     * Whitebox Test für delete von mehreren Ausleihen
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testDeleteMultiple() throws DataSourceException,
            NamingException {
        List<Lending> lendingsToDelete = new ArrayList<Lending>();
        Lending lending1 = new Lending();
        Lending lending2 = new Lending();
        Lending lending3 = new Lending();
        Lending lending4 = new Lending();
        Lending lending5 = new Lending();

        lendingsToDelete.add(lending1);
        lendingsToDelete.add(lending2);
        lendingsToDelete.add(lending3);
        lendingsToDelete.add(lending4);
        lendingsToDelete.add(lending5);

        lhandler.delete(lendingsToDelete);
     //   Mockito.verify(mockedData);
    }

    /**
     * Whitebox Test für delete von mehreren Ausleihen
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testDeleteNothing() throws DataSourceException, NamingException {
        List<Lending> lendingsToDelete = new ArrayList<Lending>();

        lhandler.delete(lendingsToDelete);
        Mockito.verifyNoMoreInteractions(mockedData);
    }

    /**
     * Whitebox Test für isOpenDay Komponententest
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testIsOpenDayOpen1() throws DataSourceException,
            NamingException, BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        Calendar cal = new GregorianCalendar();
        cal.set(2014, 2, 24);

        when(oH.isOpenDay(cal.getTime())).thenReturn(true);
        when(cH.isClosedDay(cal.getTime())).thenReturn(false);
        assertTrue(lhandler.isOpenDay(cal.getTime()));
    }

    /**
     * Whitebox Test für isOpenDay Komponententest
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testIsOpenDayOpen2() throws DataSourceException,
            NamingException, BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {

        Calendar cal = new GregorianCalendar();
        cal.set(2014, 2, 24);

        when(oH.isOpenDay(cal.getTime())).thenReturn(true);
        when(cH.isClosedDay(cal.getTime())).thenReturn(true);
        assertTrue(!(lhandler.isOpenDay(cal.getTime())));
    }

    /**
     * Whitebox Test für isOpenDay Komponententest
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     */
    @Test
    public void testIsOpenDayOpen3() throws DataSourceException,
            NamingException, BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {

        Calendar cal = new GregorianCalendar();
        cal.set(2014, 2, 24);

        when(oH.isOpenDay(cal.getTime())).thenReturn(false);
        assertTrue(!(lhandler.isOpenDay(cal.getTime())));
    }

    /**
     * Whitebox Test für CalcFeeData
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     * @throws BusinessElementAlreadyExistsException
     * NOT WORKING */
    @Test 
    public void testCalcFeeData() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        Lending lending = new Lending();

        Calendar calStart = new GregorianCalendar();
        calStart.set(2014, 2, 10);
        Calendar calTill = new GregorianCalendar();
        calTill.set(2014, 2, 11);

        lending.setStart(calStart.getTime());
        lending.setTill(calTill.getTime());

        when(cH.getClosedDays(lending.getTill(), new Date())).thenReturn(null);
        boolean[] openDays = { true, true, true, true, true, true, true };

        when(oH.getOpenDays()).thenReturn(openDays);
        int[] days = lhandler.calcFeeData(lending);

        when(mockedData.getProperty(1).getValue()).thenReturn("1");

        assertTrue(days[1] > 0);

    }

    /**
     * Whitebox Test für CalcFeeData
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     * @throws BusinessElementAlreadyExistsException
     * NOT WORKING */
    @Test 
    public void testCalcFeeData2() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        Lending lending = new Lending();
        Calendar calStart = new GregorianCalendar();
        calStart.set(2014, 2, 10);
        lending.setStart(calStart.getTime());
        lending.setTill(new Date());

        when(cH.getClosedDays(lending.getTill(), new Date())).thenReturn(null);
        boolean[] openDays = { true, true, true, true, true, true, true };

        when(oH.getOpenDays()).thenReturn(openDays);
        int[] days = lhandler.calcFeeData(lending);

        when(mockedData.getProperty(1).getValue()).thenReturn("5");

        assertTrue(days[1] == 0);

    }

    /**Whitebox Test für das Hinzufügen einer Ausleihe mit nicht bereits existierender ID
     * 
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     * @throws BusinessElementAlreadyExistsException
     * NOT WORKING */
    @Test 
    public void testAddWithoutID() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        Lending lending = new Lending();
        lending.setExemplarID(30000);
        lending.setReaderID(1);
        Reader reader = new Reader();
        reader.setUsername("TestUsername");
        Exemplar exemplar = new Exemplar();

        when(rH.get(lending.getId())).thenReturn(reader);
        when(rH.update(reader)).thenReturn(1);
        when(eH.get(exemplar.getId())).thenReturn(exemplar);
        when(eH.update(exemplar)).thenReturn(1);
        when(mockedData.addLending(lending)).thenReturn(1);
        when(mockedData.getReader(lending.getReaderID()).getUsername())
                .thenReturn("TestUsername");
        when(mockedData.getMedium(lending.getExemplarID()).getTitle())
                .thenReturn("TestTitle");

        int result = lhandler.add(lending);
        assertTrue(result == 1);

    }

    /**
     * Whitebox Test für das Hinzufügen einer Ausleihe mit bereits existierender ID
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     * @throws BusinessElementAlreadyExistsException
     * NOT WORKING */
    @SuppressWarnings("unused")
    @Test(expected = BusinessElementAlreadyExistsException.class)
  
    public void testAddWithId() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        Lending lending = new Lending();
        lending.setExemplarID(30000);
        lending.setId(20);
        
        int result = lhandler.add(lending);

    }

    /**
     * Whitebox Test für das Hinzufügen einer Ausleihe mit einem Resultat von 0
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     * @author Mathias Eggerichs
     * @throws BusinessElementAlreadyExistsException
     * NOT WORKING */
    @Test(expected = DataSourceException.class)
    public void testAddFail() throws DataSourceException,
            BusinessElementAlreadyExistsException,
            NoSuchBusinessObjectExistsException {
        Lending lending = new Lending();
        lending.setExemplarID(30000);
        Reader reader = new Reader();
        Exemplar exemplar = new Exemplar();

        when(rH.get(lending.getId())).thenReturn(reader);
        when(rH.update(reader)).thenReturn(1);
        when(eH.get(exemplar.getId())).thenReturn(exemplar);
        when(eH.update(exemplar)).thenReturn(1);
        when(mockedData.addLending(lending)).thenReturn(0);
        when(mockedData.getLending(lending.getId())).thenReturn(null);
    }

}
