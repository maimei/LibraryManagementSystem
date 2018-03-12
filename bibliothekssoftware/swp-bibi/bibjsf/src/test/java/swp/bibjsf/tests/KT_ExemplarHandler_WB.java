package swp.bibjsf.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;

/**
 * White-Box-Testklasse für ExemplarHandler aus der BusinessLogic.
 *
 * @author Hauke Olf
 *
 */
public class KT_ExemplarHandler_WB {

    /**
     * Gemocktes Data-Objekt.
     */
    private Persistence mockedData;

    private ExemplarHandler eHandler;

    @Before
    public void setup() throws DataSourceException, NamingException {
        mockedData = mock(Persistence.class);
        eHandler = new ExemplarHandler(mockedData);
    }

    /**
     * Test für get(int id)
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     */
    @Test
    public void testGet() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        Exemplar ex = new Exemplar();
        String place = "Hinten links";
        ex.setMediumID(5);
        ex.setPlace(place);
        ex.setId(0);

        when(mockedData.getExemplar(0)).thenReturn(ex);

        assertTrue(eHandler.get(0).equals(ex));
    }

    /**
     * Test für get(List<Constraint> constraints, int from, int to,
     * List<OrderBy> order)
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     */
    @Test
    public void testGetList() throws DataSourceException,
            NoSuchBusinessObjectExistsException {


        Exemplar ex1 = new Exemplar();
        Exemplar ex2 = new Exemplar();
        Exemplar ex3 = new Exemplar();

        String place1 = "Hinten links";
        ex1.setMediumID(5);
        ex1.setPlace(place1);
        ex1.setId(0);

        String place2 = "Vorne links";
        ex2.setMediumID(5);
        ex2.setPlace(place2);
        ex2.setId(1);

        String place3 = "Ganz weit weg";
        ex2.setMediumID(5);
        ex2.setPlace(place3);
        ex2.setId(2);

        List<Exemplar> exList = new ArrayList<Exemplar>();
        exList.add(ex1);
        exList.add(ex2);
        exList.add(ex3);

        when(mockedData.getExemplars(null, 0, Integer.MAX_VALUE, null)).thenReturn(
                exList);
        List<Exemplar> compareList = new ArrayList<Exemplar>();

        compareList = eHandler.get(null, 0, Integer.MAX_VALUE, null);
        assertTrue(compareList.get(0).equals(exList.get(0)));
        assertTrue(compareList.get(1).equals(exList.get(1)));
        assertTrue(compareList.get(2).equals(exList.get(2)));
    }

    /**
     * Test für get(List<Constraint> constraints, int from, int to,
     * List<OrderBy> order) ABER EMPTY
     *
     * @throws DataSourceException
     * @throws NoSuchBusinessObjectExistsException
     */
    @Test
    public void testGetEmptyList() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        List<Exemplar> exList = new ArrayList<Exemplar>();
        when(mockedData.getExemplars(null, 0, Integer.MAX_VALUE, null)).thenReturn(
                exList);

        exList = eHandler.get(null, 0, Integer.MAX_VALUE, null);
        assertTrue(exList.isEmpty());
    }

    /**
     * Test für getNumber(List<Constraint> constraints)
     *
     * @throws DataSourceException
     */
    @Test
    public void testGetNumber() throws DataSourceException {

        when(mockedData.getNumberOfExemplars(null)).thenReturn(1);
        assertTrue(eHandler.getNumber(null) == 1);
    }

    /**
     * Test für add(Exemplar exemplar) bei nicht bestehender ID
     *
     * @throws DataSourceException
     * @throws BusinessElementAlreadyExistsException
     */
    @Test
    public void testAddNoId() throws DataSourceException,
            BusinessElementAlreadyExistsException {

        Exemplar ex1 = new Exemplar();
        int addResult;

        Medium med = new Medium();
        
        med.setId(5);
        med.setTitle("Test");
        
        when(mockedData.getMedium(5)).thenReturn(med);
        
        String place1 = "Hinten links";
        ex1.setMediumID(5);
        ex1.setPlace(place1);
        ex1.setId(0);

        when(mockedData.addExemplar(ex1)).thenReturn(0);
        addResult = eHandler.add(ex1);

        assertTrue(addResult == 0);
    }

    /**
     * Test für add(Exemplar exemplar) bei erwartetem Rückgabewert 0
     *
     * @throws DataSourceException
     * @throws BusinessElementAlreadyExistsException
     */
    @Test(expected = DataSourceException.class)
    public void testAddWithId() throws DataSourceException,
            BusinessElementAlreadyExistsException {

        Exemplar ex = new Exemplar();
        int addResult;

        String place = "Hinten links";
        ex.setMediumID(5);
        ex.setPlace(place);
        ex.setId(0);

        when(mockedData.addExemplar(ex)).thenReturn(-1);
        addResult = eHandler.add(ex);

        assertTrue(addResult == -1);

    }

    /**
     * Test für erfolgreiches update(Exemplar exemplar)
     *
     * @throws DataSourceException
     * @throws BusinessElementAlreadyExistsException
     */
    @Test
    public void testUpdate() throws DataSourceException,
            NoSuchBusinessObjectExistsException {

        Exemplar ex = new Exemplar();
        int updateResult;

        String place = "Hinten links";
        ex.setMediumID(5);
        ex.setPlace(place);
        ex.setId(14);

        when(mockedData.updateExemplar(ex)).thenReturn(1);

        updateResult = eHandler.update(ex);
        assertTrue(updateResult == 1);
    }

    /**
     * Test für Löschen eines Exemplars mit delete(List<Exemplar> elements)
     *
     * @throws DataSourceException
     */
    @Test
    public void testSingleDeletion() throws DataSourceException {

        Exemplar ex = new Exemplar();
        Medium med = new Medium();
        
        med.setId(5);
        med.setTitle("Test");
        
        String place = "Hinten links";
        ex.setMediumID(5);
        ex.setPlace(place);
        ex.setId(14);
        
        when(mockedData.getMedium(5)).thenReturn(med);

        List<Exemplar> delList = new ArrayList<Exemplar>();
        delList.add(ex);

        eHandler.delete(delList);
        Mockito.verify(mockedData, Mockito.times(1));
    }

    /**
     * Test für Löschen mehrerer Exemplare mit delete(List<Exemplar> elements)
     *
     * @throws DataSourceException
     */
    @Test
    public void testSeveralDeletions() throws DataSourceException {
        Medium med = new Medium();
        
        med.setId(5);
        med.setTitle("Test");
    	
        Exemplar ex1 = new Exemplar();
        Exemplar ex2 = new Exemplar();
        Exemplar ex3 = new Exemplar();
        Exemplar ex4 = new Exemplar();
        
        ex1.setMediumID(5);
        ex1.setId(14);
        
        ex2.setMediumID(5);
        ex2.setId(15);
        
        ex3.setMediumID(5);
        ex3.setId(16);
        
        ex4.setMediumID(5);
        ex4.setId(17);

        List<Exemplar> delList = new ArrayList<Exemplar>();
        delList.add(ex1);
        delList.add(ex2);
        delList.add(ex3);
        delList.add(ex4);
        
        when(mockedData.getMedium(5)).thenReturn(med);

        eHandler.delete(delList);
        Mockito.verify(mockedData, Mockito.times(4));
    }

    /**
     * Test für importCSV(InputStream inputstream)
     *
     * @throws DataSourceException
     */
    @Test(expected = NotImplementedException.class)
    public void testImportCSV() throws DataSourceException {
        eHandler.importCSV(null);
    }

    /**
     * Test für exportCSV(OutputStream outStream)
     *
     * @throws DataSourceException
     */
    @Test(expected = NotImplementedException.class)
    public void testExportCSV() throws DataSourceException {
        eHandler.exportCSV(null);
    }
}
