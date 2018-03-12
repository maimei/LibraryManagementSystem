package swp.bibjsf.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import swp.bibcommon.Commentary;

import swp.bibjsf.businesslogic.CommentaryHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.naming.NamingException;

/**
 * Testklasse hinsichtlich der Komponententests für die Funktionsfähigkeit
 * der Klasse CommentaryHandler
 * 
 * @author Hauke Olf
 *
 */


 //Funktionstest für die Methode getInstance().
 
public class KT_CommentaryHandler_BB {
	
	

 //Notwendige Test-Variablen	
 
	
private Date testDate = new Date();	
	
@Test
public void testGetInstance() throws DataSourceException, NamingException {
	
	CommentaryHandler ch = null;
	
	try {
		ch = CommentaryHandler.getInstance();
	} catch (DataSourceException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	try {
		assertEquals(CommentaryHandler.getInstance(), ch);
	} catch (DataSourceException e) {
		fail("Datasource error");
	}
}

/**
 *  Funktionstest für die Methode getInstance().
 *   Getestet wird, ob getInstance() tatsächlich eine Instanz liefert.
 */
@Test (expected = DataSourceException.class)
public void testGetInstanceNotNull() throws DataSourceException, NamingException {
	
	CommentaryHandler ch = null;

		ch = CommentaryHandler.getInstance();

		assertNotNull(ch);
}


/**
 *  Funktionstest für die Methode add(Commentary).
 * @throws DataSourceException
 * @throws NamingException
 * @throws NoSuchBusinessObjectExistsException 
 * @throws BusinessElementAlreadyExistsException 
 */
 
@Test
public void testAddCommentary() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
	
	CommentaryHandler ch = CommentaryHandler.getInstance();
	int commentaryID;
	String commentary = "Dies ist ein Testkommentar";
	int testMediumID = 35000;
	int testReaderID = 12;
	
	Commentary testCommentary = new Commentary();
	testCommentary.setDateOfPublication(testDate);
	testCommentary.setCommentary(commentary);
	testCommentary.setMediumID(testMediumID);
	testCommentary.setReaderID(testReaderID);
	
	commentaryID = ch.add(testCommentary);
	assertFalse(-1 == commentaryID);
	assertTrue(1 <= commentaryID);
} 


 /**
  *  Funktionstest für die Methode add(Commentary).
  *  Getestet wird der Fall eines bereits identisch vorhandenen Commentary.
 * @throws NoSuchBusinessObjectExistsException 
 * @throws BusinessElementAlreadyExistsException 
  */
 @Test
public void testAddCommentaryAlreadyExist() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
	
	testAddCommentary();
	
	CommentaryHandler ch = CommentaryHandler.getInstance();
	int commentaryID;
	String commentary = "Dies ist ein Testkommentar";
	int testMediumID = 35000;
	int testReaderID = 12;
	
	Commentary testCommentary = new Commentary();
	testCommentary.setDateOfPublication(testDate);
	testCommentary.setCommentary(commentary);
	testCommentary.setMediumID(testMediumID);
	testCommentary.setReaderID(testReaderID);
	
	commentaryID = ch.add(testCommentary);
	assertFalse(-1 == commentaryID);
	assertTrue(1 <= commentaryID);
} 


 /**
  *  Funktionstest für die Methode update(int i, Commentary c).
 * @throws DataSourceException 
 * @throws NoSuchBusinessObjectExistsException 
 * @throws BusinessElementAlreadyExistsException 
  */
@Test
public void testUpdateCommentary() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
	
	CommentaryHandler ch = CommentaryHandler.getInstance();
	int commentaryID;
	String commentary = "Dies ist ein Testkommentar";
	int testMediumID = 35002;
	int testReaderID = 13;
	
	int commentaryIDUpdate; 
	String commentaryUpdate = "Dies ist ein upgedateter Testkommentar";
	int updateMediumID = 35004;
	int updateReaderID = 15;
	
	Commentary testCommentary = new Commentary();
	testCommentary.setDateOfPublication(new Date());
	testCommentary.setCommentary(commentary);
	testCommentary.setMediumID(testMediumID);
	testCommentary.setReaderID(testReaderID);
	
	commentaryID = ch.add(testCommentary);
	assertFalse(-1 == commentaryID);
	assertTrue(1 <= commentaryID);
	
	Commentary updateCommentary = new Commentary();
	updateCommentary.setDateOfPublication(new Date());
	updateCommentary.setCommentary(commentaryUpdate);
	updateCommentary.setMediumID(updateMediumID);
	updateCommentary.setReaderID(updateReaderID);
	
	commentaryIDUpdate = ch.update(updateCommentary);
	assertFalse(-1 == commentaryIDUpdate);
	assertTrue(1 <= commentaryIDUpdate);
	
}


/**
 * Funktionstest für die Methode get() per ID.
 * @throws DataSourceException 
 * @throws NoSuchBusinessObjectExistsException 
 * @throws BusinessElementAlreadyExistsException 
 */
 
@Test
public void testGetCommentaryById() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
	
	CommentaryHandler ch = CommentaryHandler.getInstance();
	Commentary getC;
	int commentaryID;
	String commentary = "Dies ist ein Testkommentar";
	int testMediumID = 35002;
	int testReaderID = 13;
	
	Commentary testCommentary = new Commentary();
	testCommentary.setDateOfPublication(new Date());
	testCommentary.setCommentary(commentary);
	testCommentary.setMediumID(testMediumID);
	testCommentary.setReaderID(testReaderID);
	
	commentaryID = ch.add(testCommentary);
	assertFalse(-1 == commentaryID);
	assertTrue(1 <= commentaryID);
	
	getC = ch.get(commentaryID);
	assertEquals(testCommentary, getC);
}


/**
 * Funktionstest für die Methode get() mit einer Liste.
 * @throws DataSourceException 
 * @throws NoSuchBusinessObjectExistsException 
 * @throws BusinessElementAlreadyExistsException 
 */
 
@Test
public void testGetByList() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
	
	CommentaryHandler ch = CommentaryHandler.getInstance();
	Commentary getC;
	int commentaryID;
	String commentary = "Dies ist ein Testkommentar";
	int testMediumID = 35002;
	int testReaderID = 13;
	
	Commentary testCommentary = new Commentary();
	testCommentary.setDateOfPublication(new Date());
	testCommentary.setCommentary(commentary);
	testCommentary.setMediumID(testMediumID);
	testCommentary.setReaderID(testReaderID);
	
	commentaryID = ch.add(testCommentary);
	List<Constraint> conList = new ArrayList<Constraint>();
    List<Commentary> cList = ch.get(conList, 0, Integer.MAX_VALUE, null);
    assertFalse(cList.isEmpty());
    assertTrue(cList.size() == 1);
}


 /**
  * Funktionstest für die Methode delete().
 * @throws DataSourceException 
 * @throws NoSuchBusinessObjectExistsException 
 * @throws BusinessElementAlreadyExistsException 
  */
 
@Test
public void testDelete() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {

	CommentaryHandler ch = CommentaryHandler.getInstance();
	Commentary getC;
	int commentaryID;
	String commentary = "Dies ist ein Testkommentar";
	int testMediumID = 35002;
	int testReaderID = 13;
	
	Commentary testCommentary = new Commentary();
	testCommentary.setDateOfPublication(new Date());
	testCommentary.setCommentary(commentary);
	testCommentary.setMediumID(testMediumID);
	testCommentary.setReaderID(testReaderID);
	
	commentaryID = ch.add(testCommentary);
	List<Commentary> cList = new ArrayList<Commentary>();
	cList.add(testCommentary);
	
	ch.delete(cList);
	List<Constraint> conList = new ArrayList<Constraint>();
    List<Commentary> emptyList = ch.get(conList, 0, Integer.MAX_VALUE, null);
	assertTrue(emptyList.isEmpty());
	
}


/**
 * Funktionstest für die Methode activate(List<Commentary>).
 * @throws DataSourceException 
 * @throws NoSuchBusinessObjectExistsException 
 * @throws BusinessElementAlreadyExistsException 
 */
 
@Test
public void testActivate() throws DataSourceException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
	
	CommentaryHandler ch = CommentaryHandler.getInstance();
	Commentary C1;
	int commentaryID;
	String commentary = "Dies ist ein Testkommentar";
	int testMediumID = 35002;
	int testReaderID = 13;
	
	Commentary C2;
	int commentaryID2;
	String commentary2 = "Dies ist auch ein Testkommentar";
	int testMediumID2 = 35005;
	int testReaderID2 = 15;
	
	Commentary testCommentary1 = new Commentary();
	testCommentary1.setDateOfPublication(new Date());
	testCommentary1.setCommentary(commentary);
	testCommentary1.setMediumID(testMediumID);
	testCommentary1.setReaderID(testReaderID);

	Commentary testCommentary2 = new Commentary();
	testCommentary2.setDateOfPublication(new Date());
	testCommentary2.setCommentary(commentary2);
	testCommentary2.setMediumID(testMediumID2);
	testCommentary2.setReaderID(testReaderID2);

	commentaryID = ch.add(testCommentary1);
	commentaryID2 = ch.add(testCommentary2);
	
	List<Commentary> cList = new ArrayList<Commentary>();
	cList.add(testCommentary1);
	cList.add(testCommentary2);
	
	assertFalse(testCommentary1.isActive());
	assertFalse(testCommentary2.isActive());
}

}
