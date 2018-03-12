package swp.bibjsf.tests;


import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;


import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Data;
import swp.bibcommon.Lending;

import java.util.*;

import org.junit.Test;

/**
 * @author Bernd Poppinga
 */

public class KT_LendingHandler_BB {
	
	Lending lending = new Lending();
	Lending lending2 = new Lending();
	
	public  KT_LendingHandler_BB(){
      Calendar cal = Calendar.getInstance();
      cal.set(2012, 12, 1);
	  lending.setReaderID(3442);
	  lending.setExemplarID(49494);
	  lending.setStart(cal.getTime());
	  cal.set(2012, 12, 24);
	  lending.setTill(cal.getTime());
	  
	  lending2.setReaderID(3432);
	  lending2.setExemplarID(43144);
	  cal.set(2013, 12, 02);
	  lending2.setStart(cal.getTime());
	  lending2.setTill(new Date());
	}
    
	
	@Test
	public void testGetInstance() {
		
		LendingHandler lendingHandler1;
		LendingHandler lendingHandler2;
		try{
			
			lendingHandler1 = LendingHandler.getInstance();
		    lendingHandler2 = LendingHandler.getInstance();
		    assertNotNull(lendingHandler1);
		   assertEquals(lendingHandler1, lendingHandler2);
		}
		catch(DataSourceException e )
		{
			fail("Datasource error");
		}
		
	}
	
	@Test
	public void testAddNormalfall() {
		LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
		    assertEquals(data.getLending(lendingHandler.add(lending)), lending);
	    }
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
	
	/*@Test(expected = BusinessElementAlreadyExistsException.class)
	public void testAddFehlerfall() throws BusinessElementAlreadyExistsException {
		LendingHandler lendingHandler;
		try{
			
			lendingHandler = LendingHandler.getInstance();
			lendingHandler.
			lendingHandler.add(lending);
			//lendingHandler.add(lending);
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }	
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}*/
	
	@Test
	public void testUpdateNormalfall() {
		LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
			int id = lendingHandler.add(lending);
			assertEquals(1,lendingHandler.update(lending2));
			assertEquals(lending2, data.getLending(id));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
		
	}
	
	@Test
	public void testUpdateFehlerfall() {
		LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
			int id = lendingHandler.add(lending);
			assertEquals(0,lendingHandler.update(lending2));
			assertEquals(lending, data.getLending(id));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }	
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
	
	@Test
	public void testGetNormalfall() {
		LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
			int id = lendingHandler.add(lending);
			assertEquals(lending,lendingHandler.get(id));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }	
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
	
    @Test
    public void testGetListNormalfall() {
    	LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
		    lendingHandler.add(lending);
		    lendingHandler.add(lending2);
		    List<Lending> list = lendingHandler.get(null,0,2,null);
 			assertTrue(list.contains(lending));
 			assertTrue(list.contains(lending2));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }	
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
    
    
    @Test
    public void testGetNumberNormalfall() { 
    	LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
		    lendingHandler.add(lending);
		    lendingHandler.add(lending2);
		    assertTrue(2 == lendingHandler.getNumber(null));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }	
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
    
    @Test
    public void testDeleteNormalfall() {
    	LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
		    lendingHandler.add(lending);
		    lendingHandler.add(lending2);
		    List<Lending> list = new ArrayList<Lending>();
		    list.add(lending);
		    list.add(lending2);
		    lendingHandler.delete(list);
 			assertTrue(0 == lendingHandler.getNumber(null));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }	
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
	
    @Test
    public void testDeleteFehlerfall() {
    	LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
		    lendingHandler.add(lending);
		    List<Lending> list = new ArrayList<Lending>();
		    list.add(lending2);
		    lendingHandler.delete(list);
 			assertTrue(1 == lendingHandler.getNumber(null));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }	
		catch(BusinessElementAlreadyExistsException b) { fail("AlreadyExsists error");}
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
    
    @Test
    public void testCalcFeeNormalFall() {
    	LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
		   
		    int days = lending.getTill().compareTo(new Date());
		    assertEquals(((days-3)*0.3),lendingHandler.calcFee(lending));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }
		
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
		
	}
    
    @Test
    public void testCalcFeeGrenzfal () throws ParseException {
    	LendingHandler lendingHandler;
		try{
			Data data = new Data();
			data.reset();
			lendingHandler = LendingHandler.getInstance();
		    
		    assertEquals(0,lendingHandler.calcFee(lending2));
		}
		catch(DataSourceException e ){ fail("Datasource error"); }
		catch(NamingException n) { fail("Naming error"); }
		
		catch(NoSuchBusinessObjectExistsException e2) { fail("NoSuchBusinessObjectExists"); }
	}
}
    
    
	

