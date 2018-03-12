package swp.bibjsf.tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import swp.bibcommon.Category;
import swp.bibcommon.ClosedTime;
import swp.bibcommon.Commentary;
import swp.bibcommon.Exemplar;
import swp.bibcommon.Extension;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.MediumType;
import swp.bibcommon.News;
import swp.bibcommon.Rating;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.persistence.Data;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Constraint.AttributeType;
import swp.bibjsf.utils.OrderBy;

/**
 * 
 * @author
 */
public class KT_Data_BB {

	private DataSource dataSource;
	private Data data;
	private static Connection conn;

	public KT_Data_BB() throws Exception {

		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		DriverManager.getConnection("jdbc:derby:libraryDB-Test;create=true",
				"bib", "bib");
		dataSource = new EmbeddedDataSource();
		((EmbeddedDataSource) dataSource).setUser("bib");
		((EmbeddedDataSource) dataSource).setPassword("bib");
		((EmbeddedDataSource) dataSource).setDatabaseName("libraryDB-Test");
		((EmbeddedDataSource) dataSource).setCreateDatabase("create");
		conn = DriverManager.getConnection("jdbc:derby:libraryDB-Test", "bib",
				"bib");
	}

	@Before
	public void setup() throws DataSourceException {
		// hand over our data source
		data = new Data(dataSource);
		data.reset();
	}

	@AfterClass
	public static void shutdown() throws SQLException {
		Statement statement = conn.createStatement();
		statement.execute("DROP TABLE OPENINGTIME");
		
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
		}
	}

	/*
	 * @Test(expected=DataSourceException.class) public void test() throws
	 * DataSourceException { data.getMedium(0); }
	 */

	/**
	 * Hilfsklasse um einen MediumType hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addMediumTypeHelp1() throws DataSourceException {
		MediumType type = new MediumType();
		type.setName("Kinderbuch");
		type.setLendingTime(1);
		type.setFee(new BigDecimal(1));
		type.setExtensionTime(1);
		type.setExtensions(1);
		type.setAttribute0("Beschreibung");
		return data.addMediumType(type);
	}

	/**
	 * BlackboxTest für die Methode addMediumType den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addMediumTypeNormalfall() throws DataSourceException {
		addMediumTypeHelp1();
	}

	/**
	 * Test für die Methode addMediumType für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addMediumTypeFehlerfall1() throws DataSourceException {
		data.addMediumType(null);
	}

	/**
	 * BlackboxTest für die Methode addMediumType für den 2. Fehlerfall (Medium
	 * doppelt hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addMediumTypeFehlerfall2() throws DataSourceException {
		addMediumTypeHelp1();
		addMediumTypeHelp1();
	}

	/**
	 * BlackboxTest für die Methode addMediumType für den 3. Fehlerfall (Medium
	 * wieder hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addMediumTypeFehlerfall3() throws DataSourceException {
		data.addMediumType(data.getMediumType(addMediumTypeHelp1()));
	}

	/**
	 * BlackboxTest für die Methode getMediumType den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getMediumTypeNormalfall() throws DataSourceException {
		MediumType type = data.getMediumType(addMediumTypeHelp1());
	}

	/**
	 * BlackboxTest für die Methode getMediumType den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getMediumTypeFehlerfall1() throws DataSourceException {
		MediumType type = data.getMediumType(-1);
	}

	/**
	 * BlackboxTest für die Methode getMediumType den 2. Fehlerfall(nicht
	 * vorhander MediumType)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getMediumTypeFehlerfall2() throws DataSourceException {
		MediumType type = data.getMediumType(42);
	}

	/**
	 * BlackboxTest für die Methode deleteMediumType für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteMediumTypeNormalfall() throws DataSourceException {
		int MediumTypeID = addMediumTypeHelp1();
		MediumType type = data.getMediumType(MediumTypeID);
		data.deleteMediumType(type);
	}

	/**
	 * BlackboxTest für die Methode deleteMediumType für den 1. Fehlerfall
	 * (Ungültiges MediumTypeObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteMediumTypeFehlerfall1() throws DataSourceException {
		data.deleteMediumType(null);
	}

	/**
	 * BlackboxTest für die Methode deleteMediumType für den 2. Fehlerfall
	 * (Nicht in Datenbank vorhandener MedienType)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void deleteMediumTypeFehlerfall2() throws DataSourceException {
		MediumType type = new MediumType();
		data.deleteMediumType(type);
	}

	/**
	 * BlackboxTest für die Methode getMediumTypes für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumTypesNormalfall() throws DataSourceException {
		int MediumTypeId = addMediumTypeHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(MediumTypeId),
				"AND", AttributeType.STRING));

		List<MediumType> list = data.getMediumTypes(constraints, 0,
				data.getNumberOfMediumType(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getMediumTypes für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumTypesNormalfall2() throws DataSourceException {
		List<MediumType> list = data.getMediumTypes(null, 0,
				data.getNumberOfMediumType(null), null);
		assertTrue(list.size() == data.getNumberOfMediumType(null));
	}

	/**
	 * BlackboxTest für die Methode getMediumTypes für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumTypesNormalfall3() throws DataSourceException {
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<MediumType> list = data.getMediumTypes(constraints, 0,
				data.getNumberOfMediumType(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == 0);
	}

	/**
	 * BlackboxTest für die Methode getMediumTypes für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumTypesFehlerfall1() throws DataSourceException {
		int MediumTypeId = addMediumTypeHelp1();
		data.deleteMediumType(data.getMediumType(MediumTypeId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(MediumTypeId),
				"AND", AttributeType.STRING));

		List<MediumType> list = data.getMediumTypes(constraints, 0,
				data.getNumberOfMediumType(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfMediumType
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfMediumTypeNormalfall() throws DataSourceException {
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfMediumType(constraints));
		assertTrue(data.getNumberOfMediumType(constraints) == 7);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfMediumType (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfMediumTypeNormalfall2() throws DataSourceException {
		int MediumTypeId = addMediumTypeHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(MediumTypeId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfMediumType(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateMediumType
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateMediumTypeNormalfall() throws DataSourceException {
		int mediumTypeId = addMediumTypeHelp1();
		MediumType type = data.getMediumType(mediumTypeId);
		type.setName("Bilderbuch");
		data.updateMediumType(type);
		assertTrue(data.getMediumType(mediumTypeId).getName()
				.equals("Bilderbuch"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateMediumType
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateMediumTypeFehlerfall1() throws DataSourceException {
		data.updateMediumType(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateMediumType
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateMediumTypeFehlerfall2() throws DataSourceException {
		MediumType type = new MediumType();
		assertTrue(0 == data.updateMediumType(type));
	}

	/**
	 * Blackbox Normalfall für addCategory
	 * 
	 * @throws DataSourceException
	 */
	@Test
	public void addCategoryNormalfall() throws DataSourceException {
		Category c = new Category();
		c.setName("Fsk6-Filme");
		assertTrue(data.addCategory(c) > -1);
	}

	/**
	 * Blackbox Fehlerfall für addCategory (schon vorhandene Kategorie)
	 * 
	 * @throws DataSourceException
	 */
	@Test(expected = DataSourceException.class)
	public void addCategoryFehlerfall1() throws DataSourceException {
		Category c = new Category();
		c.setName("Fsk6-Filme");
		assertTrue(data.addCategory(c) > -1);
		data.addCategory(c);

	}

	/**
	 * Blackbox Fehlerfall für addCategory (ungültiges Objekt)
	 * 
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addCategoryFehlerfall2() throws DataSourceException {
		data.addCategory(null);
	}

	/**
	 * BlackboxTest für die Methode addCategory für den 3. Fehlerfall (Category
	 * wieder hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addCategoryFehlerfall3() throws DataSourceException {
		data.addCategory(data.getCategory(addCategorieHelp1()));
	}

	/**
	 * Normalfalltest für getCategory
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void getCategoryNormalfall() throws DataSourceException {
		Category c = new Category();
		c.setName("Fsk6-Filme");
		int cID = data.addCategory(c);
		assertTrue(data.getCategory(cID).getName().equals("Fsk6-Filme"));
	}

	/**
	 * Fehlerfall(invalide ID) für getCategory
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = DataSourceException.class)
	public void getCategoryFehlerfall1() throws DataSourceException {
		data.getCategory(-1);
	}

	/**
	 * Fehlerfall(nicht vorhandenes Objekt) für getCategory
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = DataSourceException.class)
	public void getCategoryFehlerfall2() throws DataSourceException {
		data.getCategory(42);
	}

	/**
	 * Normalfall für deleteCategory
	 * 
	 * @throws DataSourceException
	 * 
	 */
	@Test
	public void deleteCategoryNormalfall() throws DataSourceException {
		Category c = new Category();
		c.setName("Fsk6-Filme");
		int cID = data.addCategory(c);
		data.deleteCategory(data.getCategory(cID));
	}

	/**
	 * Fehlerfall1 (ungültiges Objekt) für deleteCategory
	 * 
	 * @throws DataSourceException
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteCategoryFehlerfall22() throws DataSourceException {
		data.deleteCategory(null);
	}

	/**
	 * Hilfsklasse um einen MediumType hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addCategorieHelp1() throws DataSourceException {
		Category c = new Category();
		c.setName("Fsk6-Filme");
		return data.addCategory(c);
	}

	/**
	 * BlackboxTest für die Methode getCategories für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCategoriesNormalfall() throws DataSourceException {
		int CategorieId = addCategorieHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(CategorieId),
				"AND", AttributeType.STRING));

		List<Category> list = data.getCategories(constraints, 0,
				data.getNumberOfCategories(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getCategories für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCategoriesNormalfall2() throws DataSourceException {
		List<Category> list = data.getCategories(null, 0,
				data.getNumberOfCategories(null), null);
		assertTrue(list.size() == data.getNumberOfCategories(null));
	}

	/**
	 * BlackboxTest für die Methode getCategories für den Normalfall3 (umgekehrt
	 * sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCategoriesNormalfall3() throws DataSourceException {

		Category c = new Category();
		c.setName("Fsk6-Filme");
		data.addCategory(c);

		Category c2 = new Category();
		c2.setName("Fsk12-Filme");
		data.addCategory(c2);

		Category c3 = new Category();
		c3.setName("Fsk18-Filme");
		data.addCategory(c3);

		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Category> list = data.getCategories(constraints, 0,
				data.getNumberOfCategories(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == 0);
	}

	/**
	 * BlackboxTest für die Methode getCategories für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCategoriesFehlerfall1() throws DataSourceException {
		int CategorieId = addCategorieHelp1();
		data.deleteCategory(data.getCategory(CategorieId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(CategorieId),
				"AND", AttributeType.STRING));

		List<Category> list = data.getCategories(constraints, 0,
				data.getNumberOfCategories(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blachbox Normfallfalltest für updateCategory
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateCategoryNormalfall() throws DataSourceException {
		int CategoryId = addCategorieHelp1();
		Category type = data.getCategory(CategoryId);
		type.setName("Bilderbuch");
		data.updateCategory(type);
		assertTrue(data.getCategory(CategoryId).getName().equals("Bilderbuch"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateCategory
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateCategoryFehlerfall1() throws DataSourceException {
		data.updateCategory(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateCategory
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateCategoryFehlerfall2() throws DataSourceException {
		Category type = new Category();
		assertTrue(0 == data.updateCategory(type));
	}

	/**
	 * Blackbox Normalfall für addClosedTime
	 * 
	 * @throws DataSourceException
	 */
	@Test
	public void addClosedTimeNormalfall() throws DataSourceException {

		assertTrue(addClosedTimeHelp1() > -1);
	}

	/**
	 * Blackbox Fehlerfall für addClosedTime (ungültiges Objekt)
	 * 
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addClosedTimeFehlerfall2() throws DataSourceException {
		data.addClosedTime(null);
	}

	/**
	 * Normalfalltest für getClosedTime
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void getClosedTimeNormalfall() throws DataSourceException {
		int cID = addClosedTimeHelp1();
		assertTrue(data.getClosedTime(cID).getOccasion().equals("Grund"));
	}

	/**
	 * Fehlerfall(invalide ID) für getClosedTime
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = DataSourceException.class)
	public void getClosedTimeFehlerfall1() throws DataSourceException {
		data.getClosedTime(-1);
	}

	/**
	 * Fehlerfall(nicht vorhandenes Objekt) für getClosedTime
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = DataSourceException.class)
	public void getClosedTimeFehlerfall2() throws DataSourceException {
		data.getClosedTime(42);
	}

	/**
	 * Normalfall für deleteClosedTime
	 * 
	 * @throws DataSourceException
	 * 
	 */
	@Test
	public void deleteClosedTimeNormalfall() throws DataSourceException {
		int cID = addClosedTimeHelp1();
		data.deleteClosedTime(data.getClosedTime(cID));
	}

	/**
	 * Fehlerfall1 (ungültiges Objekt) für deleteClosedTime
	 * 
	 * @throws DataSourceException
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteClosedTimeFehlerfall2() throws DataSourceException {
		data.deleteClosedTime(null);
	}

	/**
	 * Hilfsklasse um einen MediumType hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addClosedTimeHelp1() throws DataSourceException {
		ClosedTime c = new ClosedTime();
		c.setStart(new Date());
		c.setTill(new Date());
		c.setOccasion("Grund");
		return data.addClosedTime(c);
	}

	/**
	 * BlackboxTest für die Methode getClosedTimes für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getClosedTimesNormalfall() throws DataSourceException {
		int ClosedTimeId = addClosedTimeHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ClosedTimeId),
				"AND", AttributeType.STRING));

		List<ClosedTime> list = data.getClosedTimes(constraints, 0,
				data.getNumberOfClosedTimes(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getClosedTimes für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getClosedTimesNormalfall2() throws DataSourceException {
		List<ClosedTime> list = data.getClosedTimes(null, 0,
				data.getNumberOfClosedTimes(null), null);
		assertTrue(list.size() == data.getNumberOfClosedTimes(null));
	}

	/**
	 * BlackboxTest für die Methode getClosedTimes für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getClosedTimesNormalfall3() throws DataSourceException {

		addClosedTimeHelp1();

		ClosedTime c = new ClosedTime();
		c.setOccasion("Grund2");
		c.setTill(new Date());
		c.setStart(new Date());

		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<ClosedTime> list = data.getClosedTimes(constraints, 0,
				data.getNumberOfClosedTimes(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == 0);
	}

	/**
	 * BlackboxTest für die Methode getClosedTimes für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getClosedTimesFehlerfall1() throws DataSourceException {
		int ClosedTimeId = addClosedTimeHelp1();
		data.deleteClosedTime(data.getClosedTime(ClosedTimeId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ClosedTimeId),
				"AND", AttributeType.STRING));

		List<ClosedTime> list = data.getClosedTimes(constraints, 0,
				data.getNumberOfClosedTimes(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blachbox Normfallfalltest für updateClosedTime
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateClosedTimeNormalfall() throws DataSourceException {
		int ClosedTimeId = addClosedTimeHelp1();
		ClosedTime type = data.getClosedTime(ClosedTimeId);
		type.setOccasion("Ferien");
		data.updateClosedTime(type);
		assertTrue(data.getClosedTime(ClosedTimeId).getOccasion()
				.equals("Ferien"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateClosedTime
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateClosedTimeFehlerfall1() throws DataSourceException {
		data.updateClosedTime(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateClosedTime
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateClosedTimeFehlerfall2() throws DataSourceException {
		ClosedTime type = new ClosedTime();
		assertTrue(0 == data.updateClosedTime(type));
	}
	
	/**
	 * Hilfsklasse um einen Commentary hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addCommentaryHelp1(int mediumID,int readerID) throws DataSourceException {
		Commentary type = new Commentary();
		type.setActive(true);
		type.setCommentary("Cooles Medium");
		type.setDateOfPublication(new Date());
		type.setMediumID(mediumID);
		type.setReaderID(readerID);
		return data.addCommentary(type);
	}
	
	private int addMediumHelp1() throws DataSourceException {
		Medium type = new Medium();
		type.setCategory("Medium");
		type.setMediumType(0);
		type.setTitle("Medium1");
		return data.addMedium(type);
	}
	
	private int addReaderHelp1() throws DataSourceException {
		Reader type = new Reader();
		type.setBirthday(new Date());
		type.setCity("Stadt");
		type.setEmail("email@example.com");
		type.setEntryDate(new Date());
		type.setFirstName("Hans");
		type.setGroupid("READER");
		type.setLastName("Meyer");
		type.setLastUse(new Date());
		type.setNote("");
		type.setPassword("password");
		type.setPhone("0190123456");
		type.setReminderMail(false);
		type.setReservationMail(false);
		type.setReturnMail(false);
		type.setStatus("active");
		type.setStreet("Lange Straße");
		type.setUsername("hm");
		type.setZipcode("28443");
		return data.addReader(type);
	}

	/**
	 * BlackboxTest für die Methode addCommentary den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addCommentaryNormalfall() throws DataSourceException {
		addCommentaryHelp1(addMediumHelp1(),addReaderHelp1());
	}

	/**
	 * Test für die Methode addCommentary für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addCommentaryFehlerfall1() throws DataSourceException {
		data.addCommentary(null);
	}

	/**
	 * BlackboxTest für die Methode getCommentary den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getCommentaryNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		Commentary type = data.getCommentary(addCommentaryHelp1(mediumID,readerID));
	}

	/**
	 * BlackboxTest für die Methode getCommentary den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getCommentaryFehlerfall1() throws DataSourceException {
		Commentary type = data.getCommentary(-1);
	}

	/**
	 * BlackboxTest für die Methode getCommentary den 2. Fehlerfall(nicht
	 * vorhander Commentary)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getCommentaryFehlerfall2() throws DataSourceException {
		Commentary type = data.getCommentary(42);
	}

	/**
	 * BlackboxTest für die Methode deleteCommentary für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteCommentaryNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int CommentaryID = addCommentaryHelp1(mediumID,readerID);
		Commentary type = data.getCommentary(CommentaryID);
		data.deleteCommentary(type);
	}

	/**
	 * BlackboxTest für die Methode deleteCommentary für den 1. Fehlerfall
	 * (Ungültiges CommentaryObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteCommentaryFehlerfall1() throws DataSourceException {
		data.deleteCommentary(null);
	}

	/**
	 * BlackboxTest für die Methode getCommentarys für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCommentarysNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int CommentaryId = addCommentaryHelp1(mediumID,readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(CommentaryId),
				"AND", AttributeType.STRING));

		List<Commentary> list = data.getCommentaries(constraints, 0,
				data.getNumberOfCommentaries(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getCommentarys für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCommentarysNormalfall2() throws DataSourceException {
		List<Commentary> list = data.getCommentaries(null, 0,
				data.getNumberOfCommentaries(null), null);
		assertTrue(list.size() == data.getNumberOfCommentaries(null));
	}

	/**
	 * BlackboxTest für die Methode getCommentarys für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCommentarysNormalfall3() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addCommentaryHelp1(mediumID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Commentary> list = data.getCommentaries(constraints, 0,
				data.getNumberOfCommentaries(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == 0);
	}

	/**
	 * BlackboxTest für die Methode getCommentarys für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getCommentarysFehlerfall1() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int CommentaryId = addCommentaryHelp1(mediumID,readerID);
		data.deleteCommentary(data.getCommentary(CommentaryId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(CommentaryId),
				"AND", AttributeType.STRING));

		List<Commentary> list = data.getCommentaries(constraints, 0,
				data.getNumberOfCommentaries(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfCommentary
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfCommentaryNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addCommentaryHelp1(mediumID,readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfCommentaries(constraints));
		assertTrue(data.getNumberOfCommentaries(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfCommentary (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfCommentaryNormalfall2() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int CommentaryId = addCommentaryHelp1(mediumID,readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(CommentaryId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfCommentaries(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateCommentary
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateCommentaryNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int CommentaryId = addCommentaryHelp1(mediumID,readerID);
		Commentary type = data.getCommentary(CommentaryId);
		type.setCommentary("noch cooler");
		data.updateCommentary(type);
		assertTrue(data.getCommentary(CommentaryId).getCommentary().equals("noch cooler"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateCommentary
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateCommentaryFehlerfall1() throws DataSourceException {
		data.updateCommentary(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateCommentary
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateCommentaryFehlerfall2() throws DataSourceException {
		Commentary type = new Commentary();
		assertTrue(0 == data.updateCommentary(type));
	}
	
	/**
	 * Hilfsklasse um einen Exemplar hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addExemplarHelp1(int readerID,int mediumID) throws DataSourceException {
		Exemplar type = new Exemplar();
		type.setDateOfAddition(new Date());
		type.setLatestReader(readerID);
		type.setLendingCount(1);
		type.setMediumID(mediumID);
		type.setStatus("ok");
		return data.addExemplar(type);
	}

	/**
	 * BlackboxTest für die Methode addExemplar den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addExemplarNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addExemplarHelp1(readerID, mediumID);
	}

	/**
	 * Test für die Methode addExemplar für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addExemplarFehlerfall1() throws DataSourceException {
		data.addExemplar(null);
	}


	/**
	 * BlackboxTest für die Methode getExemplar den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getExemplarNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		Exemplar type = data.getExemplar(addExemplarHelp1(readerID,mediumID));
	}

	/**
	 * BlackboxTest für die Methode getExemplar den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getExemplarFehlerfall1() throws DataSourceException {
		Exemplar type = data.getExemplar(-1);
	}

	/**
	 * BlackboxTest für die Methode getExemplar den 2. Fehlerfall(nicht
	 * vorhander Exemplar)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getExemplarFehlerfall2() throws DataSourceException {
		Exemplar type = data.getExemplar(42);
	}

	/**
	 * BlackboxTest für die Methode deleteExemplar für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteExemplarNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ExemplarID = addExemplarHelp1(readerID,mediumID);
		Exemplar type = data.getExemplar(ExemplarID);
		data.deleteExemplar(type);
	}

	/**
	 * BlackboxTest für die Methode deleteExemplar für den 1. Fehlerfall
	 * (Ungültiges ExemplarObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteExemplarFehlerfall1() throws DataSourceException {
		data.deleteExemplar(null);
	}

	/**
	 * BlackboxTest für die Methode getExemplars für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExemplarsNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ExemplarId = addExemplarHelp1(readerID,mediumID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ExemplarId),
				"AND", AttributeType.STRING));

		List<Exemplar> list = data.getExemplars(constraints, 0,
				data.getNumberOfExemplars(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getExemplars für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExemplarsNormalfall2() throws DataSourceException {
		List<Exemplar> list = data.getExemplars(null, 0,
				data.getNumberOfExemplars(null), null);
		assertTrue(list.size() == data.getNumberOfExemplars(null));
	}

	/**
	 * BlackboxTest für die Methode getExemplars für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExemplarsNormalfall3() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Exemplar> list = data.getExemplars(constraints, 0,
				data.getNumberOfExemplars(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == exemplarID);
	}

	/**
	 * BlackboxTest für die Methode getExemplars für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExemplarsFehlerfall1() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ExemplarId = addExemplarHelp1(readerID,mediumID);
		data.deleteExemplar(data.getExemplar(ExemplarId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ExemplarId),
				"AND", AttributeType.STRING));

		List<Exemplar> list = data.getExemplars(constraints, 0,
				data.getNumberOfExemplars(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfExemplar
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfExemplarNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addExemplarHelp1(readerID, mediumID);
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfExemplars(constraints));
		assertTrue(data.getNumberOfExemplars(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfExemplar (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfExemplarNormalfall2() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ExemplarId = addExemplarHelp1(readerID,mediumID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ExemplarId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfExemplars(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateExemplar
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateExemplarNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ExemplarId = addExemplarHelp1(readerID,mediumID);
		Exemplar type = data.getExemplar(ExemplarId);
		type.setNote("Kekse");
		data.updateExemplar(type);
		assertTrue(data.getExemplar(ExemplarId).getNote().equals("Kekse"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateExemplar
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateExemplarFehlerfall1() throws DataSourceException {
		data.updateExemplar(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateExemplar
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateExemplarFehlerfall2() throws DataSourceException {
		Exemplar type = new Exemplar();
		assertTrue(0 == data.updateExemplar(type));
	}
	
	/**
	 * Hilfsklasse um einen Extension hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addExtensionHelp1(int exemplarID, int readerID) throws DataSourceException {
		Extension type = new Extension();
		type.setExtensionDate(new Date());
		type.setExemplarID(exemplarID);
		type.setReaderID(readerID);

		return data.addExtension(type);
	}

	/**
	 * BlackboxTest für die Methode addExtension den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addExtensionNormalfall() throws DataSourceException {
		
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		addExtensionHelp1(exemplarID, readerID);
	}

	/**
	 * Test für die Methode addExtension für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addExtensionFehlerfall1() throws DataSourceException {
		data.addExtension(null);
	}

	/**
	 * BlackboxTest für die Methode addExtension für den 2. Fehlerfall (Medium
	 * doppelt hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addExtensionFehlerfall2() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumTypeHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		addExtensionHelp1(exemplarID, readerID);
		addExtensionHelp1(exemplarID, readerID);
	}

	/**
	 * BlackboxTest für die Methode addExtension für den 3. Fehlerfall (Medium
	 * wieder hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addExtensionFehlerfall3() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumTypeHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		
		data.addExtension(data.getExtension(addExtensionHelp1(exemplarID, readerID)));
	}

	/**
	 * BlackboxTest für die Methode getExtension den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getExtensionNormalfall() throws DataSourceException {
		
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		
		Extension type = data.getExtension(addExtensionHelp1(exemplarID, readerID));
	}

	/**
	 * BlackboxTest für die Methode getExtension den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getExtensionFehlerfall1() throws DataSourceException {
		Extension type = data.getExtension(-1);
	}

	/**
	 * BlackboxTest für die Methode getExtension den 2. Fehlerfall(nicht
	 * vorhander Extension)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getExtensionFehlerfall2() throws DataSourceException {
		Extension type = data.getExtension(42);
	}

	/**
	 * BlackboxTest für die Methode deleteExtension für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteExtensionNormalfall() throws DataSourceException {
		
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int ExtensionID = addExtensionHelp1(exemplarID, readerID);
		Extension type = data.getExtension(ExtensionID);
		data.deleteExtension(type);
	}

	/**
	 * BlackboxTest für die Methode deleteExtension für den 1. Fehlerfall
	 * (Ungültiges ExtensionObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteExtensionFehlerfall1() throws DataSourceException {
		data.deleteExtension(null);
	}

	/**
	 * BlackboxTest für die Methode getExtensions für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExtensionsNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int ExtensionId = addExtensionHelp1(exemplarID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ExtensionId),
				"AND", AttributeType.STRING));

		List<Extension> list = data.getExtensions(constraints, 0,
				data.getNumberOfExtensions(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getExtensions für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExtensionsNormalfall2() throws DataSourceException {
		List<Extension> list = data.getExtensions(null, 0,
				data.getNumberOfExtensions(null), null);
		assertTrue(list.size() == data.getNumberOfExtensions(null));
	}

	/**
	 * BlackboxTest für die Methode getExtensions für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExtensionsNormalfall3() throws DataSourceException {
		
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int id = addExtensionHelp1(exemplarID, readerID);
		
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Extension> list = data.getExtensions(constraints, 0,
				data.getNumberOfExtensions(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == id);
	}

	/**
	 * BlackboxTest für die Methode getExtensions für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getExtensionsFehlerfall1() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int ExtensionId = addExtensionHelp1(exemplarID,readerID);
		data.deleteExtension(data.getExtension(ExtensionId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ExtensionId),
				"AND", AttributeType.STRING));

		List<Extension> list = data.getExtensions(constraints, 0,
				data.getNumberOfExtensions(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfExtension
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfExtensionNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		addExtensionHelp1(exemplarID, readerID);
		
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfExtensions(constraints));
		assertTrue(data.getNumberOfExtensions(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfExtension (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfExtensionNormalfall2() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int ExtensionId = addExtensionHelp1(exemplarID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ExtensionId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfExtensions(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateExtension
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void updateExtensionNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int ExtensionId = addExtensionHelp1(exemplarID, readerID);
		Extension type = data.getExtension(ExtensionId);
		Date d = new Date();
		d.setYear(1990);
		type.setExtensionDate(d);
		data.updateExtension(type);
		assertTrue(data.getExtension(ExtensionId).getExtensionDate().getYear() == 1990);
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateExtension
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateExtensionFehlerfall1() throws DataSourceException {
		data.updateExtension(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateExtension
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateExtensionFehlerfall2() throws DataSourceException {
		Extension type = new Extension();
		assertTrue(0 == data.updateExtension(type));
	}

	/**
	 * Hilfsklasse um einen Lending hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addLendingHelp1(int exemplarID,int readerID) throws DataSourceException {
		Lending type = new Lending();
		type.setExemplarID(exemplarID);
		type.setExtensions(0);
		type.setFee(new BigDecimal(0));
		type.setPaid(false);
		type.setReaderID(readerID);
		type.setReturned(false);
		type.setStart(new Date());
		type.setTill(new Date());
		return data.addLending(type);
	}

	/**
	 * BlackboxTest für die Methode addLending den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addLendingNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		addLendingHelp1(exemplarID, readerID);
	}

	/**
	 * Test für die Methode addLending für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addLendingFehlerfall1() throws DataSourceException {
		data.addLending(null);
	}


	/**
	 * BlackboxTest für die Methode getLending den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getLendingNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		Lending type = data.getLending(addLendingHelp1(exemplarID, readerID));
	}

	/**
	 * BlackboxTest für die Methode getLending den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getLendingFehlerfall1() throws DataSourceException {
		Lending type = data.getLending(-1);
	}

	/**
	 * BlackboxTest für die Methode getLending den 2. Fehlerfall(nicht
	 * vorhander Lending)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getLendingFehlerfall2() throws DataSourceException {
		Lending type = data.getLending(42);
	}

	/**
	 * BlackboxTest für die Methode deleteLending für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteLendingNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int LendingID = addLendingHelp1(exemplarID, readerID);
		Lending type = data.getLending(LendingID);
		data.deleteLending(type);
	}

	/**
	 * BlackboxTest für die Methode deleteLending für den 1. Fehlerfall
	 * (Ungültiges LendingObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteLendingFehlerfall1() throws DataSourceException {
		data.deleteLending(null);
	}

	/**
	 * BlackboxTest für die Methode getLendings für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getLendingsNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int LendingId = addLendingHelp1(exemplarID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(LendingId),
				"AND", AttributeType.STRING));

		List<Lending> list = data.getLendings(constraints, 0,
				data.getNumberOfLendings(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getLendings für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getLendingsNormalfall2() throws DataSourceException {
		List<Lending> list = data.getLendings(null, 0,
				data.getNumberOfLendings(null), null);
		assertTrue(list.size() == data.getNumberOfLendings(null));
	}

	/**
	 * BlackboxTest für die Methode getLendings für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getLendingsNormalfall3() throws DataSourceException {
		
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int LendingId = addLendingHelp1(exemplarID, readerID);
		
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Lending> list = data.getLendings(constraints, 0,
				data.getNumberOfLendings(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == LendingId);
	}

	/**
	 * BlackboxTest für die Methode getLendings für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getLendingsFehlerfall1() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int LendingId = addLendingHelp1(exemplarID, readerID);
		data.deleteLending(data.getLending(LendingId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(LendingId),
				"AND", AttributeType.STRING));

		List<Lending> list = data.getLendings(constraints, 0,
				data.getNumberOfLendings(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfLending
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfLendingNormalfall() throws DataSourceException {
		
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		addLendingHelp1(exemplarID, readerID);
		
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfLendings(constraints));
		assertTrue(data.getNumberOfLendings(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfLending (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfLendingNormalfall2() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int LendingId = addLendingHelp1(exemplarID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(LendingId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfLendings(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateLending
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateLendingNormalfall() throws DataSourceException {
		int readerID = addReaderHelp1();
		int mediumID = addMediumHelp1();
		int exemplarID = addExemplarHelp1(readerID, mediumID);
		int LendingId = addLendingHelp1(exemplarID, readerID);
		Lending type = data.getLending(LendingId);
		type.setReturned(true);
		data.updateLending(type);
		assertTrue(data.getLending(LendingId).hasReturned());
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateLending
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateLendingFehlerfall1() throws DataSourceException {
		data.updateLending(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateLending
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateLendingFehlerfall2() throws DataSourceException {
		Lending type = new Lending();
		assertTrue(0 == data.updateLending(type));
	}

	/**
	 * BlackboxTest für die Methode addMedium den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addMediumNormalfall() throws DataSourceException {
		addMediumHelp1();
	}

	/**
	 * Test für die Methode addMedium für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addMediumFehlerfall1() throws DataSourceException {
		data.addMedium(null);
	}


	/**
	 * BlackboxTest für die Methode getMedium den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getMediumNormalfall() throws DataSourceException {
		Medium type = data.getMedium(addMediumHelp1());
	}

	/**
	 * BlackboxTest für die Methode getMedium den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getMediumFehlerfall1() throws DataSourceException {
		Medium type = data.getMedium(-1);
	}

	/**
	 * BlackboxTest für die Methode getMedium den 2. Fehlerfall(nicht
	 * vorhander Medium)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getMediumFehlerfall2() throws DataSourceException {
		Medium type = data.getMedium(42);
	}

	/**
	 * BlackboxTest für die Methode deleteMedium für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteMediumNormalfall() throws DataSourceException {
		int MediumID = addMediumHelp1();
		Medium type = data.getMedium(MediumID);
		data.deleteMedium(type);
	}

	/**
	 * BlackboxTest für die Methode deleteMedium für den 1. Fehlerfall
	 * (Ungültiges MediumObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteMediumFehlerfall1() throws DataSourceException {
		data.deleteMedium(null);
	}

	/**
	 * BlackboxTest für die Methode getMediums für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumsNormalfall() throws DataSourceException {
		int MediumId = addMediumHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(MediumId),
				"AND", AttributeType.STRING));

		List<Medium> list = data.getMediums(constraints, 0,
				data.getNumberOfMediums(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getMediums für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumsNormalfall2() throws DataSourceException {
		List<Medium> list = data.getMediums(null, 0,
				data.getNumberOfMediums(null), null);
		assertTrue(list.size() == data.getNumberOfMediums(null));
	}

	/**
	 * BlackboxTest für die Methode getMediums für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumsNormalfall3() throws DataSourceException {
		addMediumHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Medium> list = data.getMediums(constraints, 0,
				data.getNumberOfMediums(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == 0);
	}

	/**
	 * BlackboxTest für die Methode getMediums für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getMediumsFehlerfall1() throws DataSourceException {
		int MediumId = addMediumHelp1();
		data.deleteMedium(data.getMedium(MediumId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(MediumId),
				"AND", AttributeType.STRING));

		List<Medium> list = data.getMediums(constraints, 0,
				data.getNumberOfMediums(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfMedium
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfMediumNormalfall() throws DataSourceException {
		addMediumHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfMediums(constraints));
		assertTrue(data.getNumberOfMediums(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfMedium (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfMediumNormalfall2() throws DataSourceException {
		int MediumId = addMediumHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(MediumId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfMediums(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateMedium
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateMediumNormalfall() throws DataSourceException {
		int MediumId = addMediumHelp1();
		Medium type = data.getMedium(MediumId);
		type.setTitle("Test");
		data.updateMedium(type);
		assertTrue(data.getMedium(MediumId).getTitle()
				.equals("Test"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateMedium
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateMediumFehlerfall1() throws DataSourceException {
		data.updateMedium(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateMedium
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateMediumFehlerfall2() throws DataSourceException {
		Medium type = new Medium();
		assertTrue(0 == data.updateMedium(type));
	}
	
	/**
	 * Hilfsklasse um einen News hinzuzufügen
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	private int addNewsHelp1() throws DataSourceException {
		News type = new News();
		type.setContent("News sind toll!");
		type.setDateOfPublication(new Date());
		type.setTitle("Title");
		return data.addNews(type);
	}

	/**
	 * BlackboxTest für die Methode addNews den Normalfall
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	@Test
	public void addNewsNormalfall() throws DataSourceException {
		addNewsHelp1();
	}

	/**
	 * Test für die Methode addNews für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addNewsFehlerfall1() throws DataSourceException {
		data.addNews(null);
	}

	/**
	 * BlackboxTest für die Methode getNews den Normalfall
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getNewsNormalfall() throws DataSourceException {
		News type = data.getNews(addNewsHelp1());
	}

	/**
	 * BlackboxTest für die Methode getNews den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getNewsFehlerfall1() throws DataSourceException {
		News type = data.getNews(-1);
	}

	/**
	 * BlackboxTest für die Methode getNews den 2. Fehlerfall(nicht
	 * vorhander News)
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getNewsFehlerfall2() throws DataSourceException {
		News type = data.getNews(42);
	}

	/**
	 * BlackboxTest für die Methode deleteNews für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test
	public void deleteNewsNormalfall() throws DataSourceException {
		int NewsID = addNewsHelp1();
		News type = data.getNews(NewsID);
		data.deleteNews(type);
	}

	/**
	 * BlackboxTest für die Methode deleteNews für den 1. Fehlerfall
	 * (Ungültiges NewsObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteNewsFehlerfall1() throws DataSourceException {
		data.deleteNews(null);
	}

	/**
	 * BlackboxTest für die Methode getNewss für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test
	public void getNewssNormalfall() throws DataSourceException {
		int NewsId = addNewsHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(NewsId),
				"AND", AttributeType.STRING));

		List<News> list = data.getNews(constraints, 0,
				data.getNumberOfNews(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getNewss für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test
	public void getNewssNormalfall2() throws DataSourceException {
		List<News> list = data.getNews(null, 0,
				data.getNumberOfNews(null), null);
		assertTrue(list.size() == data.getNumberOfNews(null));
	}

	/**
	 * BlackboxTest für die Methode getNewss für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test
	public void getNewssNormalfall3() throws DataSourceException {
		int id = addNewsHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<News> list = data.getNews(constraints, 0,
				data.getNumberOfNews(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == id);
	}

	/**
	 * BlackboxTest für die Methode getNewss für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test
	public void getNewssFehlerfall1() throws DataSourceException {
		int NewsId = addNewsHelp1();
		data.deleteNews(data.getNews(NewsId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(NewsId),
				"AND", AttributeType.STRING));

		List<News> list = data.getNews(constraints, 0,
				data.getNumberOfNews(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfNews
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test
	public void getNumberOfNewsNormalfall() throws DataSourceException {
		addNewsHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfNews(constraints));
		assertTrue(data.getNumberOfNews(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfNews (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Mathias Eggerichs
	 */
	@Test
	public void getNumberOfNewsNormalfall2() throws DataSourceException {
		int NewsId = addNewsHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(NewsId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfNews(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateNews
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	@Test
	public void updateNewsNormalfall() throws DataSourceException {
		int NewsId = addNewsHelp1();
		News type = data.getNews(NewsId);
		type.setTitle("Test");
		data.updateNews(type);
		assertTrue(data.getNews(NewsId).getTitle()
				.equals("Test"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateNews
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateNewsFehlerfall1() throws DataSourceException {
		data.updateNews(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateNews
	 * 
	 * @author Mathias Eggerichs
	 * @throws DataSourceException
	 */
	@Test
	public void updateNewsFehlerfall2() throws DataSourceException {
		News type = new News();
		assertTrue(0 == data.updateNews(type));
	}
	
	/**
	 * Hilfsklasse um einen Rating hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addRatingHelp1(int mediumID, int readerID) throws DataSourceException {
		Rating type = new Rating();
		type.setMediumID(mediumID);
		type.setRating(1);
		type.setReaderID(readerID);
		return data.addRating(type);
	}

	/**
	 * BlackboxTest für die Methode addRating den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addRatingNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addRatingHelp1(mediumID, readerID);
	}

	/**
	 * Test für die Methode addRating für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addRatingFehlerfall1() throws DataSourceException {
		data.addRating(null);
	}

	/**
	 * BlackboxTest für die Methode addRating für den 2. Fehlerfall (Rating
	 * doppelt hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addRatingFehlerfall2() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addRatingHelp1(mediumID, readerID);
		addRatingHelp1(mediumID, readerID);
	}

	/**
	 * BlackboxTest für die Methode addRating für den 3. Fehlerfall (Rating
	 * wieder hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addRatingFehlerfall3() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		data.addRating(data.getRating(addRatingHelp1(mediumID, readerID)));
	}

	/**
	 * BlackboxTest für die Methode getRating den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getRatingNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		Rating type = data.getRating(addRatingHelp1(mediumID, readerID));
	}

	/**
	 * BlackboxTest für die Methode getRating den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getRatingFehlerfall1() throws DataSourceException {
		Rating type = data.getRating(-1);
	}

	/**
	 * BlackboxTest für die Methode getRating den 2. Fehlerfall(nicht
	 * vorhander Rating)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getRatingFehlerfall2() throws DataSourceException {
		Rating type = data.getRating(42);
	}

	/**
	 * BlackboxTest für die Methode deleteRating für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteRatingNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int RatingID = addRatingHelp1(mediumID, readerID);
		Rating type = data.getRating(RatingID);
		data.deleteRating(type);
	}

	/**
	 * BlackboxTest für die Methode deleteRating für den 1. Fehlerfall
	 * (Ungültiges RatingObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteRatingFehlerfall1() throws DataSourceException {
		data.deleteRating(null);
	}

	/**
	 * BlackboxTest für die Methode getRatings für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getRatingsNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int RatingId = addRatingHelp1(mediumID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(RatingId),
				"AND", AttributeType.STRING));

		List<Rating> list = data.getRating(constraints, 0,
				data.getNumberOfRatings(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getRatings für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getRatingsNormalfall2() throws DataSourceException {
		List<Rating> list = data.getRating(null, 0,
				data.getNumberOfRatings(null), null);
		assertTrue(list.size() == data.getNumberOfRatings(null));
	}

	/**
	 * BlackboxTest für die Methode getRatings für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getRatingsNormalfall3() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addRatingHelp1(mediumID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Rating> list = data.getRating(constraints, 0,
				data.getNumberOfRatings(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == 0);
	}

	/**
	 * BlackboxTest für die Methode getRatings für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getRatingsFehlerfall1() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int RatingId = addRatingHelp1(mediumID, readerID);
		data.deleteRating(data.getRating(RatingId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(RatingId),
				"AND", AttributeType.STRING));

		List<Rating> list = data.getRating(constraints, 0,
				data.getNumberOfRatings(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfRating
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfRatingNormalfall() throws DataSourceException {
		addRatingHelp1(addMediumHelp1(), addReaderHelp1());
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfRatings(constraints));
		assertTrue(data.getNumberOfRatings(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfRating (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfRatingNormalfall2() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int RatingId = addRatingHelp1(mediumID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(RatingId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfRatings(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateRating
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateRatingNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int RatingId = addRatingHelp1(mediumID, readerID);
		Rating type = data.getRating(RatingId);
		type.setRating(1);
		data.updateRating(type);
		assertTrue(data.getRating(RatingId).getRating() == 1);
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateRating
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateRatingFehlerfall1() throws DataSourceException {
		data.updateRating(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateRating
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateRatingFehlerfall2() throws DataSourceException {
		Rating type = new Rating();
		assertTrue(0 == data.updateRating(type));
	}

	/**
	 * BlackboxTest für die Methode addReader den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addReaderNormalfall() throws DataSourceException {
		addReaderHelp1();
	}

	/**
	 * Test für die Methode addReader für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addReaderFehlerfall1() throws DataSourceException {
		data.addReader(null);
	}

	/**
	 * BlackboxTest für die Methode addReader für den 2. Fehlerfall (Reader
	 * doppelt hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addReaderFehlerfall2() throws DataSourceException {
		addReaderHelp1();
		addReaderHelp1();
	}

	/**
	 * BlackboxTest für die Methode addReader für den 3. Fehlerfall (Reader
	 * wieder hinzufügen)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = DataSourceException.class)
	public void addReaderFehlerfall3() throws DataSourceException {
		data.addReader(data.getReader(addReaderHelp1()));
	}

	/**
	 * BlackboxTest für die Methode getReader den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getReaderNormalfall() throws DataSourceException {
		Reader type = data.getReader(addReaderHelp1());
	}

	/**
	 * BlackboxTest für die Methode getReader den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void getReaderFehlerfall1() throws DataSourceException {
		Reader type = data.getReader(-2);
	}

	/**
	 * BlackboxTest für die Methode getReader den 2. Fehlerfall(nicht
	 * vorhander Reader)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getReaderFehlerfall2() throws DataSourceException {
		Reader type = data.getReader(42);
	}

	/**
	 * BlackboxTest für die Methode deleteReader für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteReaderNormalfall() throws DataSourceException {
		int ReaderID = addReaderHelp1();
		Reader type = data.getReader(ReaderID);
		data.deleteReader(type);
	}

	/**
	 * BlackboxTest für die Methode deleteReader für den 1. Fehlerfall
	 * (Ungültiges ReaderObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteReaderFehlerfall1() throws DataSourceException {
		data.deleteReader(null);
	}

	/**
	 * BlackboxTest für die Methode getReaders für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReadersNormalfall() throws DataSourceException {
		
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("firstName", "=", "Hans",
				"AND", AttributeType.STRING));

		List<Reader> list = data.getReaders(constraints, 0,
				data.getNumberOfReaders(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getReaders für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReadersNormalfall2() throws DataSourceException {
		List<Reader> list = data.getReaders(null, 0,
				data.getNumberOfReaders(new ArrayList<Constraint>()), null);
		assertTrue(list.size() == data.getNumberOfReaders(new ArrayList<Constraint>()));
	}

	/**
	 * BlackboxTest für die Methode getReaders für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReadersNormalfall3() throws DataSourceException {
		int id = addReaderHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Reader> list = data.getReaders(constraints, 0,
				data.getNumberOfReaders(null), lO);
		assertTrue(list.get(0).getId() == id);
	}

	/**
	 * BlackboxTest für die Methode getReaders für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReadersFehlerfall1() throws DataSourceException {
		int ReaderId = addReaderHelp1();
		
		data.deleteReader(data.getReader(ReaderId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("firstName", "=", "Hans",
				"AND", AttributeType.STRING));

		List<Reader> list = data.getReaders(constraints, 0,
				data.getNumberOfReaders(constraints), null);
		assertTrue(list.size() == data.getNumberOfReaders(constraints));
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfReader
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfReaderNormalfall() throws DataSourceException {
		addReaderHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfReaders(constraints));
		assertTrue(data.getNumberOfReaders(constraints) == 3);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfReader (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfReaderNormalfall2() throws DataSourceException {
		int ReaderId = addReaderHelp1();
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ReaderId),
				"", AttributeType.INTEGER));
		assertTrue(data.getNumberOfReaders(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateReader
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateReaderNormalfall() throws DataSourceException {
		int ReaderId = addReaderHelp1();
		Reader type = data.getReader(ReaderId);
		type.setLastName("Test");
		data.updateReader(type);
		assertTrue(data.getReader(ReaderId).getLastName().equals("Test"));
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateReader
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateReaderFehlerfall1() throws DataSourceException {
		data.updateReader(null);
	}

	/**
	 * Hilfsklasse um einen Reservation hinzuzufügen
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	private int addReservationHelp1(int mediumID, int readerID) throws DataSourceException {
		Reservation type = new Reservation();
		type.setMediumID(mediumID);
		type.setReaderID(readerID);
		type.setReservationDate(new Date());
		return data.addReservation(type);
	}

	/**
	 * BlackboxTest für die Methode addReservation den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void addReservationNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addReservationHelp1(mediumID, readerID);
	}

	/**
	 * Test für die Methode addReservation für den 1. Fehlerfall (Ungültiges
	 * Objekt übergeben)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addReservationFehlerfall1() throws DataSourceException {
		data.addReservation(null);
	}

	/**
	 * BlackboxTest für die Methode getReservation den Normalfall
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test
	public void getReservationNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		Reservation type = data.getReservation(addReservationHelp1(mediumID, readerID));
	}

	/**
	 * BlackboxTest für die Methode getReservation den 1. Fehlerfall(invalide ID)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getReservationFehlerfall1() throws DataSourceException {
		Reservation type = data.getReservation(-1);
	}

	/**
	 * BlackboxTest für die Methode getReservation den 2. Fehlerfall(nicht
	 * vorhander Reservation)
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@SuppressWarnings("unused")
	@Test(expected = DataSourceException.class)
	public void getReservationFehlerfall2() throws DataSourceException {
		Reservation type = data.getReservation(42);
	}

	/**
	 * BlackboxTest für die Methode deleteReservation für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void deleteReservationNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ReservationID = addReservationHelp1(mediumID, readerID);
		Reservation type = data.getReservation(ReservationID);
		data.deleteReservation(type);
	}

	/**
	 * BlackboxTest für die Methode deleteReservation für den 1. Fehlerfall
	 * (Ungültiges ReservationObjekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteReservationFehlerfall1() throws DataSourceException {
		data.deleteReservation(null);
	}

	/**
	 * BlackboxTest für die Methode getReservations für den Normalfall
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReservationsNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ReservationId = addReservationHelp1(mediumID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ReservationId),
				"AND", AttributeType.STRING));

		List<Reservation> list = data.getReservations(constraints, 0,
				data.getNumberOfReservations(constraints), null);
		assertTrue(list.size() == 1);
	}

	/**
	 * BlackboxTest für die Methode getReservations für den Normalfall2
	 * (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReservationsNormalfall2() throws DataSourceException {
		List<Reservation> list = data.getReservations(null, 0,
				data.getNumberOfReservations(null), null);
		assertTrue(list.size() == data.getNumberOfReservations(null));
	}

	/**
	 * BlackboxTest für die Methode getReservations für den Normalfall3
	 * (umgekehrt sortieren)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReservationsNormalfall3() throws DataSourceException {
		int id = addReservationHelp1(addMediumHelp1(), addReaderHelp1());
		List<Constraint> constraints = new ArrayList<Constraint>();
		List<OrderBy> lO = new ArrayList<OrderBy>();
		lO.add(new OrderBy("id", false));
		List<Reservation> list = data.getReservations(constraints, 0,
				data.getNumberOfReservations(null), lO);
		assertTrue(list.get(list.size() - 1).getId() == id);
	}

	/**
	 * BlackboxTest für die Methode getReservations für den Fehlerfall1 (nicht
	 * vorhandenes Objekt)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getReservationsFehlerfall1() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ReservationId = addReservationHelp1(mediumID, readerID);
		data.deleteReservation(data.getReservation(ReservationId));
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ReservationId),
				"AND", AttributeType.STRING));

		List<Reservation> list = data.getReservations(constraints, 0,
				data.getNumberOfReservations(constraints), null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Blackbox Normfallfalltest für getNumberOfReservation
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfReservationNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		addReservationHelp1(mediumID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();
		System.out.printf("DATA_TEST: "
				+ data.getNumberOfReservations(constraints));
		assertTrue(data.getNumberOfReservations(constraints) == 1);
	}

	/**
	 * Blackbox Fehlerfalltest für getNumberOfReservation (ungültiger Constrain)
	 * 
	 * @throws DataSourceException
	 * @author Niklas Bruns
	 */
	@Test
	public void getNumberOfReservationNormalfall2() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ReservationId = addReservationHelp1(mediumID, readerID);
		List<Constraint> constraints = new ArrayList<Constraint>();

		constraints.add(new Constraint("id", "=", String.valueOf(ReservationId),
				"AND", AttributeType.STRING));
		assertTrue(data.getNumberOfReservations(constraints) == 1);
	}

	/**
	 * Blachbox Normfallfalltest für updateReservation
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateReservationNormalfall() throws DataSourceException {
		int mediumID = addMediumHelp1();
		int readerID = addReaderHelp1();
		int ReservationId = addReservationHelp1(mediumID, readerID);
		Reservation type = data.getReservation(ReservationId);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1900);
		type.setReservationDate(c.getTime());
		data.updateReservation(type);
		c.setTime(data.getReservation(ReservationId).getReservationDate());
		assertTrue(c.get(Calendar.YEAR) == 1900);
	}

	/**
	 * Blachbox Fehlerfall1 (ungültiges Objekt) für updateReservation
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void updateReservationFehlerfall1() throws DataSourceException {
		data.updateReservation(null);
	}

	/**
	 * Blachbox Fehlerfall2 (Objekt welches nicht vorhanden ist) für
	 * updateReservation
	 * 
	 * @author Niklas Bruns
	 * @throws DataSourceException
	 */
	@Test
	public void updateReservationFehlerfall2() throws DataSourceException {
		Reservation type = new Reservation();
		assertTrue(0 == data.updateReservation(type));
	}




}