package swp.bibjsf.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import swp.bibcommon.ClosedTime;
import swp.bibcommon.Lending;
import swp.bibcommon.OpeningTime;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;

public class IT_LendingHandler_WB {
	/**
	 * 
	 * @author Mathias Eggerichs
	 */
	
	private LendingHandler lhandler;
	
	public void setup() throws DataSourceException {
		lhandler = LendingHandler.getInstance();
		MockitoAnnotations.initMocks(this);
	}  

	/**
	 * Whitebox Integration Test für CalcFee
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 * @author Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 */
	@Test 
	public void testCalcFeeData() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
		Persistence data = mock(Persistence.class);
		
		when(data.getProperty(1).getValue()).thenReturn("1");
		
		Lending lending = new Lending();
		
		Calendar calStart = new GregorianCalendar();
		calStart.set(2014,2,10);
		Calendar calTill = new GregorianCalendar();
		calTill.set(2014,2,11);
		lending.setStart(calStart.getTime());
		lending.setTill(calTill.getTime());
		OpeningTime opTime = new OpeningTime();
		opTime.setDay("Montag");
		opTime.setMorningStart("12:00");
		opTime.setMorningEnd("20:00");
		
		when(data.updateOpeningTime(opTime)).thenReturn(1);
		int[] days = lhandler.calcFeeData(lending);
		
		when(data.getClosedTimes(null, 0, Integer.MAX_VALUE, null)).thenReturn(null);
		when(data.getOpeningTime(Mockito.anyInt())).thenReturn(opTime);
		when(data.getProperty(1).getValue()).thenReturn("5");
		
		assertTrue(days[1] > 0);
	}
	
	/**
	 * Whitebox Integration Test für CalcFee
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 * @author Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 */
	@Test 
	public void testCalcFeeDataWithClosedDays() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
		Persistence data = mock(Persistence.class);
		when(data.getProperty(1).getValue()).thenReturn("2");
		Lending lending = new Lending();
		Calendar calStart = new GregorianCalendar();
		calStart.set(2014,2,10);
		Calendar calTill = new GregorianCalendar();
		calStart.set(2014,2,15);
		lending.setStart(calStart.getTime());
		lending.setTill(calTill.getTime());
		OpeningTime opTime = new OpeningTime();
		opTime.setDay("Montag");
		opTime.setMorningStart("12:00");
		opTime.setMorningEnd("20:00");
		ClosedTime clTime = new ClosedTime();
		Calendar calClosed = new GregorianCalendar();
		calStart.set(2014,2,14);
		clTime.setStart(calClosed.getTime());
		clTime.setTill(new Date());
		List<ClosedTime> closedTimes = new ArrayList<ClosedTime>();
		closedTimes.add(clTime);
		when(data.getClosedTimes(null, 0, Integer.MAX_VALUE, null)).thenReturn(closedTimes);
		when(data.getOpeningTime(Mockito.anyInt())).thenReturn(opTime);
		when(data.getProperty(1).getValue()).thenReturn("5");
		
		int[] days = lhandler.calcFeeData(lending);
		
		
		assertTrue(days[1] == 0);
		
	}
	
	/**
	 * Whitebox IntegrationsTest für das Hinzufügen einer Ausleihe mit einem Resultat von 0
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 * @author Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 */
	@Test (expected = DataSourceException.class)
	public void testAddFail() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
		Lending lending = new Lending();
		lending.setExemplarID(30000);
		
		Persistence data = mock(Persistence.class);
		
		
		when(data.addLending(lending)).thenReturn(0);
		when(data.getLending(lending.getId())).thenReturn(null);
		
	}

}
