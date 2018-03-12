/*package swp.bibjsf.tests;

import static org.junit.Assert.*;
import swp.bibjsf.persistence.Data;

import org.junit.Test;

import swp.bibcommon.Book;
import swp.bibcommon.Category;
import swp.bibcommon.Commentary;
import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.News;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.naming.NamingException;
import javax.validation.constraints.AssertTrue;
public class KTDataTest {
	*//**
	 * Testklasse für Komponententests für die Kommunikation mit der Datenbank
	 * 
	 * @author Niklas Bruns, Mathias Eggerichs
	 *
	 *//*
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	@Test
	public void testGetAllMediumsGrenzfall() throws DataSourceException, NamingException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    List<Medium> list = persistence.getAllMediums();
	    assertTrue(list.isEmpty());
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 *//*
	@Test
	public void testGetAllMediumsNormallfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    book.setTitle("Test");
	    persistence.addMedium(book);
	    List<Medium> list = persistence.getAllMediums();
	    assertTrue(list.size() == 1);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testAddGetMediumNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    book.setTitle("Test");
	    persistence.addMedium(book);
	    Book book2 = (Book) persistence.getMedium(persistence.addMedium(book));
	    assertTrue(book.getTitle() == book2.getTitle());
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test(expected = DataSourceException.class)
	public void testGetMediumFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException {
		Persistence persistence = new Data();
	    persistence.reset();
	    persistence.getMedium(42);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	@Test
	public void testUpdateMediumFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    assertTrue(persistence.updateMedium(book) == 0);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateMediumNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
{
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    book.setTitle("Test");
	    int mediumID = persistence.addMedium(book);
	    book = (Book) persistence.getMedium(mediumID);
	    book.setTitle("Test2");
	    assertTrue(persistence.updateMedium(book) == 1);
	    book = (Book) persistence.getMedium(mediumID);
	    assertTrue(book.getTitle() == "Test2");
	}
	   
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateMediumbyIDNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "9999");
	    book.setTitle("Test");
	    int mediumID = persistence.addMedium(book);
	    book = (Book) persistence.getMedium(mediumID);
	    book.setTitle("Test2");
	    assertTrue(persistence.updateMedium(mediumID, book) > 0);
	    book = (Book) persistence.getMedium(mediumID);
	    assertTrue(book.getTitle() == "Test2");
	}
	
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateMediumbyIDFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    assertTrue(persistence.updateMedium(42, book) == 0);
	}
	
	
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateMediumbyIDGrenzfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    book.setTitle("Test");
	    int mediumID = persistence.addMedium(book);
	    book = (Book) persistence.getMedium(mediumID);
	    book.setTitle("Test2");
	    assertTrue(persistence.updateMedium(book) == 1);
	    book = (Book) persistence.getMedium(mediumID);
	    assertTrue(book.getTitle() == "Test2");
	}

	@Test(expected = DataSourceException.class)
	public void testDeleteMediumFehlerfall() throws DataSourceException, NamingException {
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    persistence.deleteMedium(book);  
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	public void testDeleteMediumNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException {
		Persistence persistence = new Data();
	    persistence.reset();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    book.setTitle("Test");
	    book = (Book) persistence.getMedium(persistence.addMedium(book));
	    persistence.deleteMedium(book);  
	}

	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	@Test
	public void testGetAllLendingsGrenzfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    List<Lending> list = persistence.getAllLendings();
	    assertTrue(list.isEmpty());
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 *//*	
	@Test
	public void testGetAllLendingsNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Lending lending = new Lending();
	    lendingFillOne(lending, 42, 42);
	    persistence.addLending(lending);
	    List<Lending> list = persistence.getAllLendings();
	    assertTrue(list.size() == 1);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	@Test
	public void testAddLendingNormallFall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Lending lending = new Lending();
	    Book book = new Book();
	    bookFillOne(book, "42");
	    Reader reader = new Reader();
	    int mediumID = persistence.addMedium(book);
	    int readerID = persistence.addReader("ADMIN",reader);
	    lendingFillOne(lending, mediumID, readerID);
		persistence.addLending(lending);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test(expected = DataSourceException.class)
	public void testGetLendingFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    persistence.getLending(42);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	public void testGetLendingNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Lending lending = new Lending();
	    lendingFillOne(lending, 42, 42);
	    assertTrue(persistence.getLending(persistence.addLending(lending)).getExemplarID() == 42);
	    
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateLendingFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Lending lending = new Lending();
	    lendingFillOne(lending, 42, 42);
		assertTrue(persistence.updateLending(42,lending) == 0);	
	}
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	@Test(expected = DataSourceException.class)
	public void testDeleteLendingFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Lending lending = new Lending();
	    lendingFillOne(lending, 42, 42);
	    persistence.deleteLending(lending);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	public void testDeleteLendingNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Lending lending = new Lending();
	    lendingFillOne(lending, 42, 42);
	    persistence.deleteLending(persistence.getLending(persistence.addLending(lending)));
	}

	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	@Test
	public void testGetAllExemplarsGrenzfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    List<Exemplar> list = persistence.getAllExemplars();
	    assertTrue(list.isEmpty());
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 *//*
	@Test
	public void testGetAllExemplarsNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Exemplar exemplar = new Exemplar();
	    exemplarFillOne(exemplar, 100);
	    exemplar.setNote("Test");
	    persistence.addExemplar(exemplar);
	    List<Exemplar> list = persistence.getAllExemplars();
	    assertTrue(list.size() == 1);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testAddGetExemplarNormallFall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Exemplar exemplar = new Exemplar();
	    exemplarFillOne(exemplar, 100);
	    exemplar.setNote("Test");
	    persistence.addExemplar(exemplar);
	    Exemplar exemplar2 = (Exemplar) persistence.getExemplar(persistence.addExemplar(exemplar));
	    assertTrue(exemplar.getNote() == exemplar2.getNote());
	    
	    
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test(expected = DataSourceException.class)
	public void testGetExemplarsFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    persistence.getExemplar(42);
	}
	
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateExemplarsFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Exemplar exemplar = new Exemplar();
		assertTrue(persistence.updateExemplar(42,exemplar) == 0);	
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws NoSuchBusinessObjectExistsException 
	 * @throws BusinessElementAlreadyExistsException 
	 *//*
	public void testUpdateExemplarNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Exemplar exemplar = new Exemplar();
	    exemplarFillOne(exemplar, 100);
	    exemplar.setNote("Test");
	    int exemplarID = persistence.addExemplar(exemplar);
	    exemplar = (Exemplar) persistence.getExemplar(exemplarID);
	    exemplar.setNote("Test2");
	    assertTrue(persistence.updateExemplar(exemplarID,exemplar) == 1);
	    exemplar = (Exemplar) persistence.getExemplar(exemplarID);
    assertTrue(exemplar.getNote() == "Test2");
	}

	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	@Test(expected = DataSourceException.class)
	public void testDeleteExemplarFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Exemplar exemplar = new Exemplar();
	    persistence.deleteExemplar(exemplar);
	}
	*//**
	 * Autor: Mathias Eggerichs
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	public void testDeleteExemplarNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Exemplar exemplar = new Exemplar();
	    exemplarFillOne(exemplar, 42);
	    persistence.deleteExemplar(persistence.getExemplar(persistence.addExemplar(exemplar)));
	}
	
	*//**
	 * GrenzfallTest für GetAllReservations. Type: Leere Liste
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testGetAllReservationsGrenzfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    List<Reservation> list = persistence.getAllReservations();
	    assertTrue(list.isEmpty());
	}
	
	*//**
	 * Dient zum Füllen eines Reader Objektes
	 * @param reader der zu füllende
	 * Autor: Niklas Bruns
	 *//*
	public void fillReader(Reader reader)
	{
	    reader.setUsername("Hans");
	    reader.setPassword("123456");
	    reader.setEmail("Hans@example.com");
	    reader.setFirstName("Hans");
	    reader.setLastName("Meyer");
	    reader.setBirthday(new Date(-259804800));
	    reader.setStreet("Langestrasse");
	    reader.setCity("Bremen");
	    reader.setZipcode("28199");
	    reader.setStatus("active");
	    reader.setHistoryActivated(false);
	}
	
	*//**
	 * Dient dazu ein Buch zu befüllen
	 * @param book
	 * Autor: Niklas Bruns
	 *//*
	public void fillBook(Book book)
	{
	    book.setTitle("Der Herr der Ringe - Die Gefährten Neuausgabe 2012");
	    book.setDateOfPublication(new Date(1357516800));
	    book.setLocation("Regal 1");
	    book.setIndustrialIdentifier("3608939814");
	    book.setPageCount(608);
	    book.setAuthors("John Ronald Reuel Tolkien");
	}
	
	*//**
	 * Füllt eine Gültige Reservierung sammt Nutzer und Medium in die Datenbank
	 * Autor: Niklas Bruns
	 * @return ID der neuen Reservierung
	 *//*
	public int createReservation() throws DataSourceException, BusinessElementAlreadyExistsException, NamingException
	{
		Persistence persistence = new Data();
	    Book book = new Book();
	    fillBook(book);
	    int bookID = persistence.addBook(book);
	    Reader reader = new Reader();
	    fillReader(reader);
	    int readerID = persistence.addReader("ADMIN",reader);
	    
	    Reservation reservation = new Reservation();
	    reservation.setMediumID(bookID);
	    reservation.setReaderID(readerID);
	    return persistence.addReservation(reservation);
	}
	
	*//**
	 * NormfallTest für getAllReservations. Eine Reservierung.
	 * Autor: Niklas Bruns 
	 *//*
	@Test
	public void testGetAllReservationsNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();  
	    int reservationID = createReservation();
	    List<Reservation> list = persistence.getAllReservations();
	    assertTrue(list.size() == 1);
	    assertTrue(list.get(0).getId() == reservationID);
	}
	
	*//**
	 * NormfallTest für getAllReservations. Zwei Reservierungen für 1 Medium von 2 Lesern.
	 * Autor: Niklas Bruns 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testGetAllReservationsNormalfall2() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    
	    int reservationID = createReservation();
	    Reservation reservation = persistence.getReservation(reservationID);
	    
	    Reader reader2 = new Reader();
	    fillReader(reader2);
	    int reader2ID = persistence.addReader("ADMIN",reader2);
	    
	    Reservation reservation2 = new Reservation();
	    reservation2.setMediumID(reservation.getMediumID());
	    reservation2.setReaderID(reader2ID);
	    
	    int reservation2ID = persistence.addReservation(reservation2);
	    
	    List<Reservation> list = persistence.getAllReservations();
	    assertTrue(list.size() == 2);
	    assertTrue(list.get(0).getId() == reservationID || list.get(0).getId() == reservation2ID );
	    assertTrue(list.get(1).getId() == reservationID || list.get(1).getId() == reservation2ID);
	}
	
	*//**
	 * NormfallTest für getAllReservations. Zwei Reservierungen für 2 Bücher von 2 Lesern.
	 * Autor: Niklas Bruns 
	 *//*
	@Test
	public void testGetAllReservationsNormalfall3() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    
	    int reservationID = createReservation();
	    int reservation2ID = createReservation(); 
	    
	    List<Reservation> list = persistence.getAllReservations();
	    assertTrue(list.size() == 2);
	    assertTrue(list.get(0).getId() == reservationID || list.get(0).getId() == reservation2ID );
	    assertTrue(list.get(1).getId() == reservationID || list.get(1).getId() == reservation2ID);
	}
	
	*//**
	 * NormfallTest fürs Hinzufügen und Auslesen einer Reservierung. Andere Fälle durch
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testAddundGetReservationNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException
	{	    
		Persistence persistence = new Data();
	    persistence.reset();
	    int reservationID = createReservation();
	    assertTrue(persistence.getReservation(reservationID).getMediumID() == reservationID);
	}
	
	*//**
	 * FehlerfallTest für GetReservation, nicht vorhanderes Element wird angefordert.
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testGetReservationFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    	persistence.getReservation(42);
	    	assertTrue(false);
	    }
	    catch(DataSourceException | NoSuchBusinessObjectExistsException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfalltest für AddReservation, ungültiges Objekt wird übergeben.
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testAddReservationFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    	persistence.addReservation(null);
	    	assertTrue(false);
	    }
	    catch(DataSourceException | BusinessElementAlreadyExistsException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfall für UpdateReservation, nicht vorhandenes Objekt
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateReservationFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Reservation reservation = new Reservation();
	    reservationFillOne(reservation, 42, 42);
		assertTrue(persistence.updateReservation(42,reservation) == 0); 
	}
	
	*//**
	 * Fehlerfall für UpdateReservation, ungültiges Objekt
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateReservationFehlerfall2() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
		assertTrue(persistence.updateReservation(42,null) == 0); 
	}
	
	*//**
	 * Normfalltest für UpdateReservation für eine Reservierung
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testUpdateReservationNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    
	    int reservationID = createReservation();
	    Reservation reservation = persistence.getReservation(reservationID);
	    
	    reservation.setDate(new Date(1357516801));
	    assertTrue(persistence.updateReservation(reservationID,reservation) == 1);
	    
	    reservation = persistence.getReservation(reservationID);
	    assertTrue(reservation.getDate().equals(new Date(1357516801)));
	}
	*//**
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testGetReservationsFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    	persistence.getReservation(42);
	    	assertTrue(false);
	    }
	    catch(DataSourceException | NoSuchBusinessObjectExistsException e)
	    {
	    	assertTrue(true);
	    }
	}

	*//**
	 * Fehlerfalltest fuer DeleteReservation, nicht vorhandes Objekt soll gelöscht werden
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testDeleteReservationFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Reservation reservation = new Reservation();
	    reservationFillOne(reservation, 42, 42);
	    try
	    {
	    	persistence.deleteReservation(reservation);
	    	assertTrue(false);
	    }
	    catch(DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfalltest fuer DeleteReservation, ungültiges Objekt soll gelöscht werden
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testDeleteReservationFehlerfall2() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    	persistence.deleteReservation(null);
	    	assertTrue(false);
	    }
	    catch(DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Normalfall Test für DeleteReservation, 1 Objekt wird gelöscht
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testDeleteReservationNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();

	    int reservationID = createReservation();
	    Reservation reservation = persistence.getReservation(reservationID);
	    
	    reservation = persistence.getReservation(reservationID);
		persistence.deleteReservation(reservation);
		try
		{
			persistence.getReservation(reservationID);
			assertTrue(false);
		}
		catch(DataSourceException | NoSuchBusinessObjectExistsException e)
		{
			assertTrue(true);
		}
	}
	
	*//**
	 * Normfall für AddundGetNews, eine News wird benutzt
	 * Autor: Niklas Bruns
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testAddundGetNewsNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    News news = new News();
	    news.setDateOfPublication(new Date());
		news.setTitle("Toller Titel");
	    assertTrue(persistence.getNews(persistence.addNews(news)).getDescription() == "Test");
	}
	
	*//**
	 * Normfall für AddundGetNews, zwei News werden benutzt
	 * Autor: Niklas Bruns
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testAddundGetNewsNormalfall2() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    News news = new News();
	    news.setDateOfPublication(new Date());
		news.setTitle("Toller Titel");
	    News news2 = new News();
	    news2.setDateOfPublication(new Date());
	    news2.setTitle("News1");
	    news2.setDescription("Ich mag Kekse!");
	    assertTrue(persistence.getNews(persistence.addNews(news)).getDescription() == "Test");
	    assertTrue(persistence.getNews(persistence.addNews(news2)).getDescription() == "Ich mag Kekse!");
	}
	
	*//**
	 * Normfallfall Test für UpdateNewsNormal 1 Objekt
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 * @throws BusinessElementAlreadyExistsException 
	 *//*
	@Test
	public void testUpdateNewsNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    News news = new News();
	    news.setDateOfPublication(new Date());
		news.setTitle("Toller Titel");
	    int nID = persistence.addNews(news);
	    news = persistence.getNews(nID);
	    news.setDescription("Test2");
	    persistence.updateNews(news.getId(),news);
	    assertTrue(persistence.getNews(nID).getDescription() == "Test2");
	}
	*//**
	 * Fehlerfalltest für UpdateNews, nicht vorhandes Objekt soll geupdatet werden
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	    
	@Test
	public void testUpdateNewsFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException {
		Persistence persistence = new Data();
	    persistence.reset();
	    News news = new News();
	    news.setDateOfPublication(new Date());
		news.setTitle("Toller Titel");
		assertTrue(persistence.updateNews(news.getId(),news) == 0);
	}
	
	*//**
	 * Fehlerfalltest für UpdateNews, ungültiges Objekt soll geupdatet werden
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	    
	@Test
	public void testUpdateNewsFehlerfall2() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException {
		Persistence persistence = new Data();
	    persistence.reset();
		assertTrue(persistence.updateNews(0,null) == 0);
	}
	
	*//**
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testGetNewsFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    persistence.getNews(42);
	    assertTrue(false);
	    }
	    catch (DataSourceException | NoSuchBusinessObjectExistsException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfall für AddNews mit ungültigen Objekt
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testAddNewsFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    persistence.addNews(null);
	    assertTrue(false);
	    }
	    catch (DataSourceException | BusinessElementAlreadyExistsException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	
	*//**
	 * Fehlerfalltest für DeleteNews, nicht vorhandenes Objekt
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testDeleteNewsFehlerfall() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    News news = new News();
	    news.setDateOfPublication(new Date());
		news.setTitle("Toller Titel");
	    try
	    {
	    persistence.deleteNews(news);
	    assertTrue(false);
	    }
	    catch(DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfalltest für DeleteNews, ungültiges Objekt
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testDeleteNewsFehlerfall2() throws DataSourceException, NamingException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    persistence.deleteNews(null);
	    assertTrue(false);
	    }
	    catch(DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Normfalltest für delete News, 1 Objekt
	 * Autor: Niklas Bruns
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testDeleteNewsNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    News news = new News();
	    news.setDateOfPublication(new Date());
		news.setTitle("Toller Titel");
	    int nID = persistence.addNews(news);
	    news = persistence.getNews(nID);
	    persistence.deleteNews(news);
	    try
	    {
	    	persistence.getNews(nID);
	    }
	    catch(DataSourceException | NoSuchBusinessObjectExistsException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Erstellt einen Commentar samt Medium und Reader
	 * Autor: Niklas Bruns
	 * @return ID des neuen Commentary
	 *//*
	int createCommentary() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    Book book = new Book();
	    fillBook(book);
	    int bookID = persistence.addBook(book);
	    Reader reader = new Reader();
	    fillReader(reader);
	    int readerID = persistence.addReader("ADMIN",reader);
	    Commentary commentary = new Commentary();
	    commentary.setActive(true);
	    commentary.setMediumID(bookID);
	    commentary.setReaderID(readerID);
	    commentary.setCommentary("Tolles Buch!");
		return persistence.addCommentary(commentary);
	}
	
	*//**
	 * Normalfalltest für Add und Get Commentary
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testAddundGetCommentaryNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    int commentaryID = createCommentary();
	    assertTrue(persistence.getCommentary(commentaryID).getCommentary() == "Tolles Buch!");
	}
	
	*//**
	 * Fehlerfalltest für GetCommentary, nicht vorhandenes Objekt
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testGetCommentaryFehlerfall() throws DataSourceException, NamingException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    persistence.getCommentary(42);
	    assertTrue(false);
	    }
	    catch(DataSourceException | NoSuchBusinessObjectExistsException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfalltest für AddCommentary, ungültiges Objekt
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testAddCommentaryFehlerfall() throws DataSourceException, NamingException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
	    persistence.addCommentary(null);
	    assertTrue(false);
	    }
	    catch(DataSourceException | BusinessElementAlreadyExistsException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Normalfalltest für deleteCommentary 1 Objekt.
	 * Autor: Niklas Bruns 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testDeleteCommentaryNormalfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    int commentaryID = createCommentary();
		persistence.deleteCommentary(persistence.getCommentary(commentaryID));
		try
		{
			persistence.getCommentary(commentaryID);
			assertTrue(false);
		}
		catch(DataSourceException e)
		{
			assertTrue(true);
		}
	}
	
	*//**
	 * Normalfalltest für  Add und Get Category mit einem Objekt
	 * Autor: Niklas Bruns
	 * @throws BusinessElementAlreadyExistsException 
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testAddUndGetCategoryNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Category category = new Category();
	    category.setName("Kinderbücher");
	    assertTrue(persistence.getCategory(persistence.addCategory(category)).getName() == "Kinderbücher");
	}
	
	
	*//**
	 * Normalfalltest für  Add und Get Category mit zwei Objekt
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 * @throws BusinessElementAlreadyExistsException 
	 *//*
	@Test
	public void testAddUndGetCategoryNormalfall2() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    
	    Category category = new Category();
	    category.setName("Kat1");
	    Category category2 = new Category();
	    category.setName("Kat2");
	    int catID1 = persistence.addCategory(category);
	    int catID2 = persistence.addCategory(category2);
	    assertTrue(persistence.getCategory(catID1).getName() == "Kat1");
	    assertTrue(persistence.getCategory(catID2).getName() == "Kat2");
	}
	
	*//**
	 * Fehlerfalltest für getCategory, nicht vorhandene Kategorie
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 *//*
	@Test
	public void testGetCategoryFehlerfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
		persistence.getCategory(42);
		assertTrue(false);
	    }
	    catch (DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfalltest für addCategory, nicht vorhandene Kategorie
	 * Autor: Niklas Bruns
	 * @throws BusinessElementAlreadyExistsException 
	 *//*
	@Test
	public void testAddCategoryFehlerfall() throws DataSourceException, NamingException, BusinessElementAlreadyExistsException 
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
		persistence.addCategory(null);
		assertTrue(false);
	    }
	    catch (DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	
	*//**
	 * Fehlerfalltest für deleteCategory, nicht vorhandenes Objekt
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testDeleteCategoryFehlerfall() throws DataSourceException, NamingException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Category category = new Category();
	    category.setName("Kinderbücher");
	    try
	    {
		persistence.deleteCategory(category);
		assertTrue(false);
	    }
	    catch (DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Fehlerfalltest für deleteCategory, ungültiges Objekt
	 * Autor: Niklas Bruns
	 *//*
	@Test
	public void testDeleteCategoryFehlerfall2() throws DataSourceException, NamingException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    try
	    {
		persistence.deleteCategory(null);
		assertTrue(false);
	    }
	    catch (DataSourceException e)
	    {
	    	assertTrue(true);
	    }
	}
	
	*//**
	 * Normalfalltest für deleteCategory mit einem Objekt
	 * Autor: Niklas Bruns
	 * @throws NoSuchBusinessObjectExistsException 
	 * @throws BusinessElementAlreadyExistsException 
	 *//*
	@Test
	public void testDeleteCategoryNormalfall() throws DataSourceException, NamingException, NoSuchBusinessObjectExistsException, BusinessElementAlreadyExistsException
	{
		Persistence persistence = new Data();
	    persistence.reset();
	    Category category = new Category();
	    category.setName("Kinderbücher");
	    int cID = persistence.addCategory(category);
		persistence.deleteCategory(persistence.getCategory(cID));
		try
		{
			persistence.getCategory(cID);
			assertTrue(false);
		}
		catch(DataSourceException e)
		{
			assertTrue(true);
		}
	}
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	public void bookFillOne(Book book, String isbn) {
			
		book.setPageCount(9999);
		book.setPrintType("TestType");
		book.setPublisher("TestPublisher");
		book.setPreviewLink("TestPreviewLink");
		book.setAuthors("TestAuthor");
		book.setIndustrialIdentifier(isbn);
		book.setImageURL("TestImageLink");
	}
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	public void lendingFillOne(Lending lending, int exemplarID, int readerID) {
		
		lending.setExemplarID(exemplarID);
		lending.setReaderID(readerID);
		Date date = new Date();
		lending.setFrom(date);
		date = org.apache.commons.lang.time.DateUtils.addDays(date, 1);
		lending.setTill(date);
		
	}
	
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	public void reservationFillOne(Reservation reservation, int mediumID, int readerID) {
		
		reservation.setReaderID(readerID);
		reservation.setMediumID(mediumID);
	}

	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	public List<Book> generateBooks(int count) {
		
		int isbnVariation = 200000;
		List<Book> books = new ArrayList<Book>();
		
		for (int i = 0; i <= count; i++) {

			if (isbnVariation < 100000) fail("Test not applicable");
			Book book = new Book();
			bookFillOne(book, "" + isbnVariation + "99999999");
			isbnVariation--;
			books.add(book);
		}
		return books;
	
	}
	
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	public void exemplarFillOne(Exemplar exemplar, int id) {
		
		exemplar.setLatestReader(9999);
		Date date = new Date();
		exemplar.setDateOfAddition(date);
		exemplar.setNote("TestNote");
		exemplar.setStatus("Ausgeliehen");
		exemplar.setLendingCount(9999);
		exemplar.setMediumID(id);
		exemplar.setPlace("TestPlace");
		
	}
	*//**
	 * Autor: Mathias Eggerichs
	 *//*
	public void generateExemplars(int count) {
		int idRange = 50000;
		List<Exemplar> exemplars = new ArrayList<Exemplar>();
		
		for (int i = 0; i <= count; i++) {
			Exemplar exemplar = new Exemplar();
			exemplarFillOne(exemplar, idRange);
			idRange--;
			exemplars.add(exemplar);
		}
	
	}
//	---------------------------------- Work in Progress - Leistungstests --------------------------------------------
//	@Test
//	public void testAddGetMediumHuge() throws DataSourceException, NamingException
//	{
//		Persistence persistence = new Data();
//		persistence.reset();
//		try {
//		    for(int i = 0; i <30000; i++) {
//		    	Book book = new Book();
//		    	book.setTitle(String.valueOf(i));
//		    	persistence.addMedium(book);
//		    }
//		}
//		catch(DataSourceException e1) {
//		    fail("Datasource Error");
//		}
//	}
//	/**
//	 * Autor: Mathias Eggerichs
//	 */
//	@Test
//	public void testAddGetMediumTimeCheck() throws DataSourceException, NamingException
//	{
//		Persistence persistence = new Data();
//	    persistence.reset();
//	    long startTime = System.currentTimeMillis();
//	    
//	    Book book = new Book();
//    	book.setTitle("Test");
//    	persistence.addMedium(book);
//	    
//	    long endTime = System.currentTimeMillis();    	
//	    long diffTime = endTime - startTime;		
//	    
//	    assertTrue(diffTime < 3.0);
//	}
//
//	@Test
//	public void testUpdateNewsHuge() throws DataSourceException, NamingException
//	{
//		Persistence persistence = new Data();
//	    persistence.reset();
//	    News news = new News();
//	    news.setDescription("Test");
//	    int nID = persistence.addNews(news);
//	    try {
//	    	for(int i = 0; i <= 1000; i++) {
//	    		news.setDescription(String.valueOf(i));
//	    		persistence.updateNews(news);
//	    		news = persistence.getNews(nID);
//	    	}
//	    }
//	    catch(DataSourceException e1) {
//		    fail("Datasource Error");
//		}
//
//	    assertTrue(persistence.getNews(nID).getDescription() == String.valueOf(1000));
//	}
//
//	@Test public void testSimultanousUpdateMedium() throws DataSourceException, NamingException 
//	{
//		Thread user1 = new Thread() {
//			public void run() {
//				Persistence persistence;
//				try {
//					persistence = new Data();
//					persistence.reset();
//					Book book = new Book();
//					assertTrue(persistence.updateMedium(book) == 0);
//				
//				} catch (DataSourceException e1) {
//					fail("Datasource error");
//				} catch (NamingException e1) {
//					fail("Object already in Use");
//				}
//			}
//		};
//		
//		Thread user2 = new Thread() {
//			public void run() {
//				Persistence persistence;
//				try {
//					persistence = new Data();
//					persistence.reset();
//					Book book = new Book();
//					assertTrue(persistence.updateMedium(book) == 0);
//				
//				} catch (DataSourceException e1) {
//					fail("Datasource error");
//				} catch (NamingException e1) {
//					fail("Object already in Use");
//				}
//			}
//		};
//		
//		user1.start();
//		user2.start();
//	}
//	
//	@Test public void testRecoverAddGetMedium() throws DataSourceException, NamingException {
//		Persistence persistence = new Data();
//	    persistence.reset();
//	    Book book = new Book();
//	    book.setTitle("Test");
//	    persistence.addMedium(book);
//	   
//	    try {
//	    	persistence.addMedium(book);
//	    } catch(DataSourceException e) {
//	    	
//	    }
//	    Book book2 = new Book();
//	    book2.setTitle("NewBook");
//	    persistence.addMedium(book2);
//	}
		
	
//}*/