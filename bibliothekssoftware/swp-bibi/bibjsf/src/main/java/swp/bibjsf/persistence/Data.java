package swp.bibjsf.persistence;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import swp.bibcommon.BusinessObject;
import swp.bibcommon.Category;
import swp.bibcommon.ClosedTime;
import swp.bibcommon.Commentary;
import swp.bibcommon.Exemplar;
import swp.bibcommon.Extension;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.MediumType;
import swp.bibcommon.News;
import swp.bibcommon.OpeningTime;
import swp.bibcommon.Property;
import swp.bibcommon.Rating;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.utils.CSVReader;
import swp.bibjsf.utils.CSVReader.CorruptInput;
import swp.bibjsf.utils.CSVReader.UnknownColumn;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;
import swp.bibjsf.utils.Reflection;

/**
 * Data ist die Implementierung der Persistence-Komponente und zuständig für die
 * Kommunikation mit der Datenbank. Die Implementierung setzt auf dem Prototypen
 * von K. Hölscher, D. Lüdemann und R. Koschke auf.
 *
 * @author Eike Externest
 */
public class Data implements Persistence {

    /**
     * Informationen über Tabellenspalten.
     */
    private static class ColumnDescriptor {

        /**
         * Die Spaltenüberschrift.
         */
        public String label;

        /**
         * Der Typ der Spalte.
         */
        public int type;
    }

    /**
     * Die Tabelle, in der Kategorien gespeichert werden.
     */
    private static final String CATEGORY = "CATEGORY";

    /**
     * Die Tabelle, in der Kommentare gespeichert werden.
     */
    private static final String COMMENTARY = "COMMENTARY";

    /**
     * Die Tabelle, in der Schließungszeiten gespeichert werden.
     */
    private static final String CLOSEDTIME = "CLOSEDTIME";

    /**
     * Die Tabelle, in der Exemplare gespeichert werden.
     */
    private static final String EXEMPLAR = "EXEMPLAR";

    /**
     * Die Tabelle, in der Verlängerungsanfragen gespeichert werden.
     */
    private static final String EXTENSION = "EXTENSION";

    /**
     * Die Tabelle, in der Ausleihen gespeichert werden.
     */
    private static final String LENDING = "LENDING";

    /**
     * Die Tabelle, in der Medien gespeichert werden.
     */
    private static final String MEDIUM = "MEDIUM";

    /**
     * Die Tabelle, in der Medientypen gespeichert werden.
     */
    private static final String MEDIUMTYPE = "MEDIUMTYPE";

    /**
     * Die Tabelle, in der Neuigkeiten gespeichert werden.
     */
    private static final String NEWS = "NEWS";

    /**
     * Die Tabelle, in der Öffnungszeiten gespeichert werden.
     */
    private static final String OPENINGTIME = "OPENINGTIME";

    /**
     * Die Tabelle, in der Eigenschaften gespeichert werden.
     */
    private static final String PROPERTY = "PROPERTY";

    /**
     * Die Tabelle, in der Bewertungen gespeichert werden.
     */
    private static final String RATING = "RATING";

    /**
     * Die Tabelle, in der Benutzer gespeichert werden.
     */
    private static final String READER = "READER";

    /**
     * Die Tabelle, in der Vormerkungen gespeichert werden.
     */
    private static final String RESERVATION = "RESERVATION";

    /**
     * The minimal ID a book can have. We are using different ranges for book
     * and reader IDs to avoid user input mistakes. By making sure the ID ranges
     * do not overlap, we can check whether a user inputs a book ID for a reader
     * or vice versa.
     */
    private static final int BOOK_MIN_ID = 30000;

    /**
     * Die Mininmale ID, die ein Reader besitzten darf.
     */
    private static final int READER_MIN_ID = 0;

    /**
     * The ID of the admin in reader table. Admin must always exist.
     */
    private static final int ADMIN_ID = 0;

    /**
     * Name of admin.
     */
    private static final String ADMIN = "admin";

    /**
     * Die Datenquelle zur Persistierung der Daten.
     */
    private final DataSource dataSource;

    /**
     * Basis JNDI Pfad.
     */
    private final String databaselookup = "java:comp/env";

    /**
     * JNDI Name der Ressource für die Datenbank.
     */
    private final String databasename = "jdbc/libraryDB";

    /**
     * Der Query Runner. Vereinfacht den Umgang mit der Datenbank.
     */
    private final QueryRunner run;

    /**
     * Logger für Log-Ausgaben.
     */
    private static final Logger LOGGER = Logger.getLogger(Data.class);

    /**
     * The format for imported/exported dates.
     */
    private static final String DATEFORMAT = "dd.MM.yyyy";

    /**
     * Das Trennsymbol in der CSV-Datei.
     */
    private static final char DEFAULT_SEPARATOR = ';';

    /**
     * Das Kommentarsymbol in der CSV-Datei.
     */
    private static final String DEFAULT_QUOTE = "\"";

    /**
     * Reduces <code>columns</code> to the list of table contained therein.
     *
     * @param columns
     *            columns whose labels are to be gathered
     * @return only the labels in columns
     */
    private static String[] toLabels(final ColumnDescriptor[] columns) {
        String[] result = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            result[i] = columns[i].label;
        }
        return result;
    }

    /**
     * Returns a concatenation of the strings in columns separated by
     * DEFAULT_SEPARATOR.
     *
     * @param columns
     *            the strings to be concatenated
     * @return concatenation of the strings in columns separated by
     *         DEFAULT_SEPARATOR
     */
    private static String toString(final String[] columns) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            s.append(columns[i]);
            s.append(DEFAULT_SEPARATOR);
        }
        return s.toString();
    }

    /**
     * Creates a new instance of this class. It is checked, whether the
     * datasource can be provided by the application container and whether the
     * database has the right structure.
     *
     * @throws DataSourceException
     *             Falls keine Datenquelle gefunden wird.
     * @throws NamingException
     *             is thrown if there are problems during the JNDI-name look-up.
     */
    public Data() throws DataSourceException, NamingException {
        LOGGER.debug("create new Data object");
        Context envCtx;
        Context initCtx = new InitialContext();
        LOGGER.debug("lookup database: " + databaselookup + ", " + databasename);

        envCtx = (Context) initCtx.lookup(databaselookup);
        dataSource = (DataSource) envCtx.lookup(databasename);
        run = new QueryRunner(dataSource);
        try {
            checkDatabaseStructure(true);
        } catch (SQLException e) {
            LOGGER.error("check database structure failure: " + e.getMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Konstruktor zum Testen.
     *
     * @param dataSrc
     *            Die Datenquelle.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenqulle auftreten.
     */
    public Data(final DataSource dataSrc) throws DataSourceException {
        LOGGER.addAppender(new ConsoleAppender(new PatternLayout()));
        LOGGER.debug("create new Data object for testing");
        dataSource = dataSrc;

        run = new QueryRunner(dataSource);

        try {
            checkDatabaseStructure(true);
        } catch (SQLException e) {
            LOGGER.error("check database structure failure: " + e.getMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#addCategory(swp.bibcommon.Category)
     */
    @Override
    public final int addCategory(final Category category)
            throws DataSourceException {
        LOGGER.debug("add category " + category);

        if (category == null) {
            LOGGER.error("category must not be null");
            throw new IllegalArgumentException("category must not be null");
        }

        try {
            return insertByID(category, CATEGORY, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add category failure" + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#addClosedTime(swp.bibcommon.ClosedTime
     * )
     */
    @Override
    public final int addClosedTime(final ClosedTime closedTime)
            throws DataSourceException {
        LOGGER.debug("add closedtime " + closedTime);

        if (closedTime == null) {
            LOGGER.error("closedTime must not be null");
            throw new IllegalArgumentException("closedTime must not be null");
        }

        try {
            return insertByID(closedTime, CLOSEDTIME, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add closedtime failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#addCommentary(swp.bibcommon.Commentary
     * )
     */
    @Override
    public final int addCommentary(final Commentary commentary)
            throws DataSourceException {
        LOGGER.debug("add commentary " + commentary);

        if (commentary == null) {
            LOGGER.error("commentary must not be null");
            throw new IllegalArgumentException("commentary must not be null");
        }

        try {
            return insertByID(commentary, COMMENTARY, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add commentary failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#addExemplar(swp.bibcommon.Exemplar)
     */
    @Override
    public final int addExemplar(final Exemplar exemplar)
            throws DataSourceException {
        LOGGER.debug("add exemplar " + exemplar);

        if (exemplar == null) {
            LOGGER.error("exemplar must not be null");
            throw new IllegalArgumentException("exemplar must not be null");
        }

        try {
            return insertByID(exemplar, EXEMPLAR, BOOK_MIN_ID, null, null);
        } catch (SQLException e) {
            LOGGER.error("add exemplar failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#addExtension(swp.bibcommon.Extension)
     */
    @Override
    public final int addExtension(final Extension extension)
            throws DataSourceException {
        LOGGER.debug("add extension " + extension);

        if (extension == null) {
            LOGGER.error("extension must not be null");
            throw new IllegalArgumentException("extension must not be null");
        }

        try {
            return insertByID(extension, EXTENSION, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add extension failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#addLending(swp.bibcommon.Lending)
     */
    @Override
    public final int addLending(final Lending lending)
            throws DataSourceException {
        LOGGER.debug("add lending " + lending);

        if (lending == null) {
            LOGGER.error("lending must not be null");
            throw new IllegalArgumentException("lending must not be null");
        }

        try {
            return insertByID(lending, LENDING, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add lending failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#addMedium(swp.bibcommon.Medium)
     */
    @Override
    public final int addMedium(final Medium medium) throws DataSourceException {
        LOGGER.debug("add medium " + medium);

        if (medium == null) {
            LOGGER.error("medium must not be null");
            throw new IllegalArgumentException("medium must not be null");
        }

        try {
            Set<String> toIgnore = new HashSet<String>();
            toIgnore.add("rating");
            toIgnore.add("ratingCount");
            return insertByID(medium, MEDIUM, 0, toIgnore, null);
        } catch (SQLException e) {
            LOGGER.error("add medium failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#addMediumType(swp.bibcommon.MediumType
     * )
     */
    @Override
    public final int addMediumType(final MediumType mediumType)
            throws DataSourceException {
        LOGGER.debug("add mediumtype " + mediumType);

        if (mediumType == null) {
            LOGGER.error("mediumtype must not be null");
            throw new IllegalArgumentException("mediumtype must not be null");
        }

        try {
            return insertByID(mediumType, MEDIUMTYPE, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add mediumtype failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#addNews(swp.bibcommon.News)
     */
    @Override
    public final int addNews(final News news) throws DataSourceException {
        LOGGER.debug("add news " + news);

        if (news == null) {
            LOGGER.error("news must not be null");
            throw new IllegalArgumentException("news must not be null");
        }

        try {
            return insertByID(news, NEWS, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add news failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#addRating(swp.bibcommon.Rating)
     */
    @Override
    public final int addRating(final Rating rating) throws DataSourceException {
        LOGGER.debug("add rating " + rating);

        if (rating == null) {
            LOGGER.error("rating must not be null");
            throw new IllegalArgumentException("rating must not be null");
        }

        try {
            return insertByID(rating, RATING, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add rating failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#addReader(swp.bibcommon.Reader)
     */
    @Override
    public final int addReader(final Reader reader) throws DataSourceException {
        LOGGER.debug("add reader " + reader);

        if (reader == null) {
            LOGGER.error("reader must not be null");
            throw new IllegalArgumentException("reader must not be null");
        }

        try {
            return insertByID(reader, READER, READER_MIN_ID, null, null);
        } catch (SQLException e) {
            LOGGER.error("add reader failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#addReservation(swp.bibcommon.Reservation
     * )
     */
    @Override
    public final int addReservation(final Reservation reservation)
            throws DataSourceException {
        LOGGER.debug("add reservation " + reservation);

        if (reservation == null) {
            LOGGER.error("reservation must not be null");
            throw new IllegalArgumentException("reservation must not be null");
        }

        try {
            return insertByID(reservation, RESERVATION, 0, null, null);
        } catch (SQLException e) {
            LOGGER.error("add reservation failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#backup()
     */
    @Override
    public final void backup() throws DataSourceException {
        LOGGER.debug("preparing backup");

        // Trennzeichen ermitteln
        String del = System.getProperty("file.separator");

        // Verzeichnisname
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

        // Pfad
        File dir = new File(System.getProperty("user.dir") + del + "backup"
                + del + df.format(now));
        String path = dir.getAbsolutePath() + del;

        // Ordner erstellen, falls nötig.
        LOGGER.debug("create directory: " + path + " status: " + dir.mkdirs());

        List<String> tables;

        try {
            tables = getTableNames();
        } catch (SQLException e) {
            LOGGER.debug("unable to get tablenames");
            throw new DataSourceException(e.getLocalizedMessage());
        }

        try {
            Connection conn = dataSource.getConnection();
            try {
                conn.setAutoCommit(true);
                Statement s = conn.createStatement();

                try {
                    LOGGER.debug("freezing db");
                    s.executeUpdate("CALL SYSCS_UTIL.SYSCS_FREEZE_DATABASE()");
                    LOGGER.debug("starting backup");

                    for (String t : tables) {
                        LOGGER.debug("creating backup for table " + t);
                        s.execute("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE(null, '"
                                + t.toUpperCase() + "', '" + path
                                + t.toUpperCase()
                                + ".backup', ';', null, null)");
                    }

                } catch (SQLException e) {
                    LOGGER.error("export problem " + e.getLocalizedMessage());
                    throw new DataSourceException(e.getLocalizedMessage());
                } finally {
                    LOGGER.debug("unfreezing db");
                    s.executeUpdate("CALL SYSCS_UTIL.SYSCS_UNFREEZE_DATABASE()");
                    s.close();
                }
            } catch (SQLException e) {
                LOGGER.error("statement problem " + e.getLocalizedMessage());
                throw new DataSourceException(e.getLocalizedMessage());
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error("connection problem " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Prüft die Struktur der Datenbank. Falls Tabellen fehlen, werden diese
     * erstellt. Weiterhin kann der Standard-Administratoraccount erstellt
     * werden.
     *
     * @param standard
     *            Sollen Standard-Werte erstellt werden?
     * @throws SQLException
     *             Falls ein SQL-Fehler auftritt.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    private synchronized void checkDatabaseStructure(final boolean standard)
            throws SQLException, DataSourceException {
        LOGGER.debug("check database structure");

        List<String> tableNames = getTableNames();
        for (String name : tableNames) {
            LOGGER.debug("database table " + name + " exists");
        }
        // PROPERTY
        if (!tableExists(tableNames, PROPERTY)) {
            LOGGER.debug("database table " + PROPERTY
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + PROPERTY + " (id INT, "
                    + "name VARCHAR(256) NOT NULL UNIQUE, "
                    + "value LONG VARCHAR NOT NULL, "
                    + "CONSTRAINT PROPERTY_PK PRIMARY KEY(id))");

            if (standard) {
                insertProperties();
            }
        }
        // READER
        if (!tableExists(tableNames, READER)) {
            LOGGER.debug("database table " + READER
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + READER
                    + " (id INT, username VARCHAR(128) NOT NULL UNIQUE, "
                    + "password varchar(128), "
                    + "firstname VARCHAR(256) NOT NULL, "
                    + "lastname VARCHAR(256) NOT NULL, birthday DATE, "
                    + "street VARCHAR(128), zipcode VARCHAR(12), "
                    + "city VARCHAR(50), phone VARCHAR(30), "
                    + "email VARCHAR(128) UNIQUE, entrydate DATE, "
                    + "lastuse DATE, note LONG VARCHAR, "
                    + "status VARCHAR(10), historyActivated BOOLEAN, "
                    + "groupid VARCHAR(128) NOT NULL, returnMail BOOLEAN, "
                    + "reminderMail BOOLEAN, reservationMail BOOLEAN, "
                    + "CONSTRAINT READER_PK PRIMARY KEY(id))");

            if (standard) {
                insertAdmin();
                insertDummyUser();
            }
        }
        // CATEGORY
        if (!tableExists(tableNames, CATEGORY)) {
            LOGGER.debug("database table " + CATEGORY
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + CATEGORY + " (id INT, "
                    + "name VARCHAR(256) NOT NULL UNIQUE, "
                    + "CONSTRAINT CATEGORY_PK PRIMARY KEY(id))");
        }
        // NEWS
        if (!tableExists(tableNames, NEWS)) {
            LOGGER.debug("database table " + NEWS
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + NEWS + " (id INT, "
                    + "dateofpublication DATE, " + "title VARCHAR(256), "
                    + "content LONG VARCHAR, "
                    + "CONSTRAINT NEWS_PK PRIMARY KEY(id))");
        }
        // MEDIUMTYPE
        if (!tableExists(tableNames, MEDIUMTYPE)) {
            LOGGER.debug("database table " + MEDIUMTYPE
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + MEDIUMTYPE + " (id INT, "
                    + "name VARCHAR(256) UNIQUE, lendingtime INT, "
                    + "fee DECIMAL(10,2), extensions INT, "
                    + "extensionTime INT, "
                    + "attribute0 VARCHAR(256), attribute1 VARCHAR(256), "
                    + "attribute2 VARCHAR(256), attribute3 VARCHAR(256), "
                    + "attribute4 VARCHAR(256), attribute5 VARCHAR(256), "
                    + "attribute6 VARCHAR(256), attribute7 VARCHAR(256), "
                    + "attribute8 VARCHAR(256), attribute9 VARCHAR(256), "
                    + "CONSTRAINT MEDIUMTYPE_PK PRIMARY KEY(id))");

            if (standard) {
                insertMediaTypes();
            }
        }
        // MEDIUM
        if (!tableExists(tableNames, MEDIUM)) {
            LOGGER.debug("database table " + MEDIUM
                    + " does not exist, creating new one");
            run.update("CREATE TABLE "
                    + MEDIUM
                    + " (id INT, "
                    + "title VARCHAR(256), location VARCHAR(256), "
                    + "dateofpublication DATE, category LONG VARCHAR, "
                    + "subtitle VARCHAR(256), imageurl VARCHAR(256), "
                    + "language VARCHAR(256), price DECIMAL(10,2), "
                    + "description LONG VARCHAR, dateofaddition DATE, "
                    + "mediumtype INT, attribute0 VARCHAR(1000), "
                    + "attribute1 VARCHAR(1000), attribute2 VARCHAR(1000), "
                    + "attribute3 VARCHAR(1000), attribute4 VARCHAR(1000), "
                    + "attribute5 VARCHAR(1000), attribute6 VARCHAR(1000), "
                    + "attribute7 VARCHAR(1000), attribute8 VARCHAR(1000), "
                    + "attribute9 VARCHAR(1000), "
                    + "CONSTRAINT MEDIUM_PK PRIMARY KEY(id), "
                    + "CONSTRAINT MEDIUM_UQ UNIQUE(dateofpublication, "
                    + "language, mediumtype), "
                    + "CONSTRAINT MEDIUM_FK FOREIGN KEY(mediumtype) REFERENCES "
                    + MEDIUMTYPE
                    + "(id) ON DELETE SET NULL ON UPDATE RESTRICT)");
        }
        // RATING
        if (!tableExists(tableNames, RATING)) {
            LOGGER.debug("database table " + RATING
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + RATING + " (id INT, readerid INT, "
                    + "mediumid INT, " + "rating INT, "
                    + "CONSTRAINT RATING_PK PRIMARY KEY(readerid, mediumid), "
                    + "CONSTRAINT RATING1_FK FOREIGN KEY(readerid) REFERENCES "
                    + READER + "(id) ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "CONSTRAINT RATING2_FK FOREIGN KEY(mediumid) REFERENCES "
                    + MEDIUM + "(id) ON DELETE CASCADE ON UPDATE RESTRICT)");
        }
        // EXEMPLAR
        if (!tableExists(tableNames, EXEMPLAR)) {
            LOGGER.debug("database table " + EXEMPLAR
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + EXEMPLAR
                    + " (id INT, mediumID INT, status VARCHAR(256), "
                    + "latestReader INT, "
                    + "note LONG VARCHAR, dateofaddition DATE, "
                    + "lendingcount INT, place VARCHAR(256), "
                    + "CONSTRAINT EXEMPLAR_PK PRIMARY KEY(id), "
                    + "CONSTRAINT EXEMPLAR_CHECK CHECK (id >= " + BOOK_MIN_ID
                    + "), CONSTRAINT EXEMPLAR1_FK FOREIGN KEY(mediumID) "
                    + "REFERENCES " + MEDIUM
                    + "(ID) ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "CONSTRAINT EXEMPLAR2_FK FOREIGN KEY(latestreader) "
                    + "REFERENCES " + READER
                    + "(ID) ON DELETE SET NULL ON UPDATE RESTRICT)");
        }
        // LENDING
        if (!tableExists(tableNames, LENDING)) {
            LOGGER.debug("database table " + LENDING
                    + " does not exist, creating new one");
            run.update("CREATE TABLE "
                    + LENDING
                    + " (id INT, readerid INT, exemplarid INT, start DATE, "
                    + "till DATE, returned BOOLEAN, fee DECIMAL(10,2), "
                    + "paid BOOLEAN, extensions INT, "
                    + "CONSTRAINT LENDING_PK PRIMARY KEY(id), "
                    + "CONSTRAINT LENDINGS1_FK FOREIGN KEY(readerid) REFERENCES "
                    + READER
                    + "(id) ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "CONSTRAINT LENDINGS2_FK FOREIGN KEY(exemplarid) REFERENCES "
                    + EXEMPLAR + "(id) ON DELETE CASCADE ON UPDATE RESTRICT)");
        }
        // OPENINGTIME
        if (!tableExists(tableNames, OPENINGTIME)) {
            LOGGER.debug("database table " + OPENINGTIME
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + OPENINGTIME
                    + " (id INT, day VARCHAR(256) NOT NULL UNIQUE, "
                    + "morningstart VARCHAR(5), "
                    + "morningend VARCHAR(5), afternoonstart VARCHAR(5), "
                    + "afternoonend VARCHAR(5), "
                    + "CONSTRAINT OPENINGTIME_PK PRIMARY KEY(id))");
            if (standard) {
                insertOpeningTimes();
            }
        }
        // CLOSINGTIME
        if (!tableExists(tableNames, CLOSEDTIME)) {
            LOGGER.debug("database table " + CLOSEDTIME
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + CLOSEDTIME
                    + " (id INT, start DATE, till DATE, "
                    + "occasion LONG VARCHAR, "
                    + "CONSTRAINT CLOSEDTIME_PK PRIMARY KEY(id))");
        }
        // COMMENTARY
        if (!tableExists(tableNames, COMMENTARY)) {
            LOGGER.debug("database table " + COMMENTARY
                    + " does not exist, creating new one");
            run.update("CREATE TABLE "
                    + COMMENTARY
                    + " (id INT, commentary LONG VARCHAR, dateofpublication DATE, "
                    + "mediumid INT, readerid INT, active BOOLEAN, "
                    + "CONSTRAINT COMMENTARY_PK PRIMARY KEY(id), "
                    + "CONSTRAINT COMMENTARY1_FK FOREIGN KEY(readerid) REFERENCES "
                    + READER
                    + "(id) ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "CONSTRAINT COMMENTARY2_FK FOREIGN KEY(mediumid) REFERENCES "
                    + MEDIUM + "(id) ON DELETE CASCADE ON UPDATE RESTRICT)");
        }
        // RESERVATION
        if (!tableExists(tableNames, RESERVATION)) {
            LOGGER.debug("database table " + RESERVATION
                    + " does not exist, creating new one");
            run.update("CREATE TABLE " + RESERVATION
                    + " (id INT, readerid INT, mediumid INT, "
                    + "reservationdate DATE, "
                    + "CONSTRAINT RESERVATION_PK PRIMARY KEY(id), "
                    + "CONSTRAINT RESERVATION1_FK FOREIGN KEY(readerid) "
                    + "REFERENCES " + READER
                    + "(id) ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "CONSTRAINT RESERVATION2_FK FOREIGN KEY(mediumid) "
                    + "REFERENCES " + MEDIUM
                    + "(id) ON DELETE CASCADE ON UPDATE RESTRICT)");
        }
        // EXTENSION
        if (!tableExists(tableNames, EXTENSION)) {
            LOGGER.debug("database table " + EXTENSION
                    + " does not exist, creating new one");
            run.update("CREATE TABLE "
                    + EXTENSION
                    + " (id INT, readerid INT, exemplarid INT, extensiondate DATE, "
                    + "CONSTRAINT EXTENSION_PK PRIMARY KEY(id), "
                    + "CONSTRAINT EXTENSION1_FK FOREIGN KEY(readerid) REFERENCES "
                    + READER
                    + "(id) ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "CONSTRAINT EXTENSION2_FK FOREIGN KEY(exemplarid) REFERENCES "
                    + EXEMPLAR + "(id) ON DELETE CASCADE ON UPDATE RESTRICT)");
        }
    }

    /**
     * Disables auto-commit and closes {@code con}.
     *
     * @param con
     *            Die zu schließende Verbindung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    protected final void closeConnection(final Connection con)
            throws DataSourceException {
        if (con != null) {
            try {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new DataSourceException(e.getLocalizedMessage());
                } finally {
                    con.close();
                }
            } catch (SQLException e) {
                throw new DataSourceException(e.getLocalizedMessage());
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteCategory(swp.bibcommon.Category)
     */
    @Override
    public final void deleteCategory(final Category category)
            throws DataSourceException {
        LOGGER.info("deleting category " + category);

        if (category == null) {
            LOGGER.error("category must not be null");
            throw new IllegalArgumentException("category must not be null");
        }

        try {
            run.update("DELETE FROM " + CATEGORY + " WHERE ID = ?",
                    category.getId());
        } catch (SQLException e) {
            LOGGER.error("failure in deleting category "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteClosedTime(swp.bibcommon.ClosedTime
     * )
     */
    @Override
    public final void deleteClosedTime(final ClosedTime closedTime)
            throws DataSourceException {
        LOGGER.info("deleting closedTime " + closedTime);

        if (closedTime == null) {
            LOGGER.error("closedtime must not be null");
            throw new IllegalArgumentException("closedtime must not be null");
        }

        try {
            run.update("DELETE FROM " + CLOSEDTIME + " WHERE ID = ?",
                    closedTime.getId());
        } catch (SQLException e) {
            LOGGER.error("delete closedtime failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteCommentary(swp.bibcommon.Commentary
     * )
     */
    @Override
    public final void deleteCommentary(final Commentary commentary)
            throws DataSourceException {
        LOGGER.info("deleting commentary " + commentary);

        if (commentary == null) {
            LOGGER.error("commentary must not be null");
            throw new IllegalArgumentException("commentary must not be null");
        }

        try {
            run.update("DELETE FROM " + COMMENTARY + " WHERE ID = ?",
                    commentary.getId());
        } catch (SQLException e) {
            LOGGER.error("failure in deleting commentary "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteExemplar(swp.bibcommon.Exemplar)
     */
    @Override
    public final void deleteExemplar(final Exemplar exemplar)
            throws DataSourceException {
        LOGGER.info("deleting exemplar " + exemplar);

        if (exemplar == null) {
            LOGGER.error("exemplar must not be null");
            throw new IllegalArgumentException("exemplar must not be null");
        }

        try {
            run.update("DELETE FROM " + EXEMPLAR + " WHERE ID = ?",
                    exemplar.getId());
        } catch (SQLException e) {
            LOGGER.error("delete exemplar failure: " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteExtension(swp.bibcommon.Extension
     * )
     */
    @Override
    public final void deleteExtension(final Extension extension)
            throws DataSourceException {
        LOGGER.info("deleting extension " + extension);

        if (extension == null) {
            LOGGER.error("extension must not be null");
            throw new IllegalArgumentException("extension must not be null");
        }

        try {
            run.update("DELETE FROM " + EXTENSION + " WHERE ID = ?",
                    extension.getId());
        } catch (SQLException e) {
            LOGGER.error("failure in deleting extension "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteLending(swp.bibcommon.Lending)
     */
    @Override
    public final void deleteLending(final Lending lending)
            throws DataSourceException {
        LOGGER.info("deleting lending " + lending);

        if (lending == null) {
            LOGGER.error("lending must not be null");
            throw new IllegalArgumentException("lending must not be null");
        }

        try {
            run.update("DELETE FROM " + LENDING + " WHERE ID = ?",
                    lending.getId());
        } catch (SQLException e) {
            LOGGER.error("failure in deleting lending "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteMedium(swp.bibcommon.Medium)
     */
    @Override
    public final void deleteMedium(final Medium medium)
            throws DataSourceException {
        LOGGER.info("deleting medium " + medium);

        if (medium == null) {
            LOGGER.error("medium must not be null");
            throw new IllegalArgumentException("medium must not be null");
        }

        try {
            run.update("DELETE FROM " + MEDIUM + " WHERE ID = ?",
                    medium.getId());
        } catch (SQLException e) {
            LOGGER.error("failure in deleting medium "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteMediumType(swp.bibcommon.MediumType
     * )
     */
    @Override
    public final void deleteMediumType(final MediumType mediumType)
            throws DataSourceException {
        LOGGER.info("deleting mediumtype " + mediumType);

        if (mediumType == null) {
            LOGGER.error("mediumtype must not be null");
            throw new IllegalArgumentException("mediumtype must not be null");
        }

        // Verhindern, dass Standard-Medientypen gelöscht werden
        if (mediumType.getId() < 7) {
            throw new DataSourceException("Cannot delete standard Mediumtype");
        }

        try {
            run.update("DELETE FROM " + MEDIUMTYPE + " WHERE ID = ?",
                    mediumType.getId());
        } catch (SQLException e) {
            LOGGER.error("failure in deleting mediumtype "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#deleteNews(swp.bibcommon.News)
     */
    @Override
    public final void deleteNews(final News news) throws DataSourceException {
        LOGGER.info("deleting news " + news);

        if (news == null) {
            LOGGER.error("news must not be null");
            throw new IllegalArgumentException("news must not be null");
        }

        try {
            run.update("DELETE FROM " + NEWS + " WHERE ID = ?", news.getId());
        } catch (SQLException e) {
            LOGGER.error("delete news failure:" + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteRating(swp.bibcommon.Rating)
     */
    @Override
    public final void deleteRating(final Rating rating)
            throws DataSourceException {
        LOGGER.info("deleting closedTime " + rating);

        if (rating == null) {
            LOGGER.error("rating must not be null");
            throw new IllegalArgumentException("rating must not be null");
        }

        try {
            run.update("DELETE FROM " + RATING + " WHERE ID = ?",
                    rating.getId());
        } catch (SQLException e) {
            LOGGER.error("delete rating failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#deleteReader(swp.bibcommon.Reader)
     */
    @Override
    public final void deleteReader(final Reader reader)
            throws DataSourceException {
        LOGGER.debug("delete reader " + reader);

        if (reader == null) {
            LOGGER.error("reader must not be null");
            throw new IllegalArgumentException("reader must not be null");
        }

        if (reader.getId() == ADMIN_ID) {
            LOGGER.info("attempt to delete admin " + reader + ": ignored");
            throw new DataSourceException(Messages.get("adminmustnotbechanged"));
        }

        try {
            run.update("DELETE FROM " + READER + " WHERE ID = ?",
                    reader.getId());
        } catch (SQLException e) {
            LOGGER.error("failure in deleting reader "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#deleteReservation(swp.bibcommon.
     * Reservation)
     */
    @Override
    public final void deleteReservation(final Reservation reservation)
            throws DataSourceException {
        LOGGER.info("deleting reservation " + reservation);

        if (reservation == null) {
            LOGGER.error("reservation must not be null");
            throw new IllegalArgumentException("reservation must not be null");
        }

        try {
            run.update("DELETE FROM " + RESERVATION + " WHERE ID = ?",
                    reservation.getId());
        } catch (SQLException e) {
            LOGGER.error("delete reservation failure: "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Exports all rows in <code>table</code> to <code>out</code> in CSV format.
     *
     * @param out
     *            the output stream
     * @param table
     *            the name of the table to be exported
     * @throws DataSourceException
     *             thrown in case of problems with the data source
     */
    public final void export(final OutputStream out, final String table)
            throws DataSourceException {
        final String query = "SELECT * from " + table;
        try {
            Connection connection = dataSource.getConnection();
            try {
                LOGGER.debug("export " + query);
                Statement stmt = connection.createStatement();
                try {
                    ResultSet set = stmt.executeQuery(query);
                    try {
                        final PrintStream printer = newPrintStream(out);
                        final SimpleDateFormat df = new SimpleDateFormat(
                                DATEFORMAT);
                        final int numberOfColumns = set.getMetaData()
                                .getColumnCount();

                        ResultSetMetaData metaData = set.getMetaData();
                        for (int column = 1; column <= numberOfColumns; column++) {
                            printer.print(quote(metaData.getColumnLabel(column)));
                            if (column < numberOfColumns) {
                                printer.print(DEFAULT_SEPARATOR);
                            }
                        }
                        printer.println();

                        // print data rows
                        while (set.next()) {
                            for (int column = 1; column <= numberOfColumns; column++) {
                                Object value = set.getObject(column);
                                if (value != null) {
                                    // null should appear as empty string
                                    if (value instanceof Date) {
                                        printer.print(quote(df
                                                .format((Date) value)));
                                    } else {
                                        printer.print(quote(value.toString()));
                                    }
                                }
                                if (column < numberOfColumns) {
                                    printer.print(DEFAULT_SEPARATOR);
                                }
                            }
                            printer.println();
                        }
                    } finally {
                        set.close();
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#exportMediums(java.io.OutputStream)
     */
    @Override
    public final void exportMediums(final OutputStream output)
            throws DataSourceException {
        LOGGER.debug("export mediums");
        export(output, MEDIUM);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#exportReaders(java.io.OutputStream)
     */
    @Override
    public final void exportReaders(final OutputStream output)
            throws DataSourceException {
        LOGGER.debug("export readers");
        export(output, READER);
    }

    /**
     * Befüllt das SQL-Statement <code>stmt</code> mit den Bedingungen aus
     * <code>constraints</code>.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param stmt
     *            Das SQL-Statement in welches die Bedingungen eingefügt werden
     *            sollen.
     * @throws SQLException
     *             Falls ein SQL-Fehler auftritt.
     */
    private void fillInArguments(final List<Constraint> constraints,
            final PreparedStatement stmt) throws SQLException {
        if (constraints != null) {
            int parameterIndex = 1;
            for (Constraint constraint : constraints) {
                stmt.setString(parameterIndex, constraint.getProperty());
                parameterIndex++;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getAllOpeningTimes()
     */
    @Override
    public final List<OpeningTime> getAllOpeningTimes()
            throws DataSourceException {
        LOGGER.debug("get all openingtimes");
        try {
            ResultSetHandler<List<OpeningTime>> resultSetHandler = new BeanListHandler<OpeningTime>(
                    OpeningTime.class);

            List<OpeningTime> openingTimes = run.query("SELECT * FROM "
                    + OPENINGTIME, resultSetHandler);
            return openingTimes;
        } catch (SQLException e) {
            LOGGER.error("list reservations failure");
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getCategories(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public final List<Category> getCategories(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get books");
        return getElements(constraints, from, to, order, CATEGORY,
                Category.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getCategory(int)
     */
    @Override
    public final Category getCategory(final int id) throws DataSourceException {
        LOGGER.debug("get category");
        try {
            ResultSetHandler<List<Category>> resultSetHandler = new BeanListHandler<Category>(
                    Category.class);

            List<Category> categories = run.query("SELECT * FROM " + CATEGORY
                    + " WHERE id=" + id, resultSetHandler);

            if (categories == null || categories.size() == 0) {
                LOGGER.error("no category for id " + id);
                throw new DataSourceException("Keine Kategorie mit der ID "
                        + id + " vorhanden.");
            }

            return categories.get(0);
        } catch (SQLException e) {
            LOGGER.error("error getting category for id "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getClosedTime(int)
     */
    @Override
    public final ClosedTime getClosedTime(final int id)
            throws DataSourceException {
        LOGGER.debug("get closedtime for id " + id);
        try {
            ResultSetHandler<List<ClosedTime>> resultSetHandler = new BeanListHandler<ClosedTime>(
                    ClosedTime.class);

            List<ClosedTime> closedTimes = run.query("SELECT * FROM "
                    + CLOSEDTIME + " WHERE id=" + id, resultSetHandler);

            if (closedTimes == null || closedTimes.size() == 0) {
                LOGGER.error("no closedtime for id " + id);
                throw new DataSourceException(
                        "Keine Schließungszeit mit der ID " + id
                                + " vorhanden.");
            }

            return closedTimes.get(0);
        } catch (SQLException e) {
            LOGGER.error("get closedtime for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getClosedTimes(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public final List<ClosedTime> getClosedTimes(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get closedtime");
        return getElements(constraints, from, to, order, CLOSEDTIME,
                ClosedTime.class);
    }

    /**
     * Retrieves the column information from <code>table</code>.
     *
     * @param table
     *            the name of the table whose columns need to be known
     * @return descriptors for each table column
     * @throws DataSourceException
     *             thrown in case of problems with the data source
     */
    private ColumnDescriptor[] getColumns(final String table)
            throws DataSourceException {
        final String query = "SELECT * from " + table;
        try {
            Connection connection = dataSource.getConnection();
            try {
                LOGGER.debug("getColumns " + query);
                Statement stmt = connection.createStatement();
                try {
                    ResultSet set = stmt.executeQuery(query);
                    try {
                        final int numberOfColumns = set.getMetaData()
                                .getColumnCount();
                        ColumnDescriptor[] result = new ColumnDescriptor[numberOfColumns];

                        ResultSetMetaData metaData = set.getMetaData();
                        for (int column = 1; column <= numberOfColumns; column++) {
                            result[column - 1] = new ColumnDescriptor();
                            result[column - 1].type = metaData
                                    .getColumnType(column);
                            result[column - 1].label = metaData
                                    .getColumnLabel(column);
                        }

                        return result;
                    } finally {
                        set.close();
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getCommentaries(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public final List<Commentary> getCommentaries(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get readers");
        return getElements(constraints, from, to, order, COMMENTARY,
                Commentary.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getCommentary(int)
     */
    @Override
    public final Commentary getCommentary(final int id)
            throws DataSourceException {
        LOGGER.debug("get commentary for id " + id);
        try {
            ResultSetHandler<List<Commentary>> resultSetHandler = new BeanListHandler<Commentary>(
                    Commentary.class);

            List<Commentary> commentaries = run.query("SELECT * FROM "
                    + COMMENTARY + " WHERE ID=" + id, resultSetHandler);

            if (commentaries == null || commentaries.size() == 0) {
                LOGGER.error("no commentary for id " + id);
                throw new DataSourceException("Kein Kommentar mit der ID " + id
                        + " vorhanden.");
            }

            return commentaries.get(0);
        } catch (SQLException e) {
            LOGGER.error("get commentary for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Gibt alle Elemente vom Typ <code>clazz</code> im Bereich <code>to</code>
     * bis <code>from</code> zurück, welche die Bedingungen erfüllen.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param from
     *            Der Index des ersten Elements.
     * @param to
     *            Der Index des letzten Elements.
     * @param order
     *            Die Ordnung der Liste.
     * @param table
     *            Der Name der zu durchsuchenden Tabelle.
     * @param clazz
     *            Die Klasse der Elemente.
     * @param Element
     *            Ein Objekt einer Klasse die von BusinessObject erbt.
     * @return Die Liste der Elemente, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    public final <Element extends BusinessObject> List<Element> getElements(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order, final String table,
            final Class<Element> clazz) throws DataSourceException {
        LOGGER.debug("get elements for table " + table);

        ArrayList<Element> allResults = new ArrayList<Element>();

        try {
            Connection connection = dataSource.getConnection();
            try {
                String query = "SELECT * FROM " + table + toQuery(constraints)
                        + toOrderByClause(order);
                LOGGER.debug("getElements " + query);

                PreparedStatement stmt = connection.prepareStatement(query);
                try {
                    try {
                        stmt.setMaxRows(to + 1);
                    } catch (SQLException e) {
                        // ignore this exception and try to run the query anyway
                    }

                    fillInArguments(constraints, stmt);

                    ResultSet rs = stmt.executeQuery();

                    try {
                        // Use the BeanHandler implementation to convert the
                        // first
                        // ResultSet row into a Reader JavaBean.
                        ResultSetHandler<Element> handler = new BeanHandler<Element>(
                                clazz);

                        int i = 0;
                        Element reader;

                        while ((reader = handler.handle(rs)) != null) {
                            if (from <= i && i <= to) {
                                allResults.add(reader);
                            } else if (i > to) {
                                break;
                            }
                            i++;
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
        return allResults;
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getExemplar(int)
     */
    @Override
    public final Exemplar getExemplar(final int id) throws DataSourceException {
        LOGGER.debug("get exemplar for id " + id);
        try {
            ResultSetHandler<List<Exemplar>> resultSetHandler = new BeanListHandler<Exemplar>(
                    Exemplar.class);

            List<Exemplar> exemplars = run.query("SELECT * FROM " + EXEMPLAR
                    + " WHERE ID=" + id, resultSetHandler);

            if (exemplars == null || exemplars.size() == 0) {
                LOGGER.error("no exemplar for id " + id);
                throw new DataSourceException("Kein Exemplar mit der ID " + id
                        + " vorhanden.");
            }

            return exemplars.get(0);
        } catch (SQLException e) {
            LOGGER.error("get exemplar for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getExemplars(java.util.List, int,
     * int, java.util.List)
     */
    @Override
    public final List<Exemplar> getExemplars(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get exemplars");
        return getElements(constraints, from, to, order, EXEMPLAR,
                Exemplar.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getExtension(int)
     */
    @Override
    public final Extension getExtension(final int id)
            throws DataSourceException {
        LOGGER.debug("get extension for id " + id);
        try {
            ResultSetHandler<List<Extension>> resultSetHandler = new BeanListHandler<Extension>(
                    Extension.class);

            List<Extension> extensions = run.query("SELECT * FROM " + EXTENSION
                    + " WHERE ID=" + id, resultSetHandler);

            if (extensions == null || extensions.size() == 0) {
                LOGGER.error("no extension for id " + id);
                throw new DataSourceException("Keine Verlängerung mit der ID "
                        + id + " vorhanden.");
            }
            return extensions.get(0);
        } catch (SQLException e) {
            LOGGER.error("get extension for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getExtensions(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public final List<Extension> getExtensions(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get extensions");
        return getElements(constraints, from, to, order, EXTENSION,
                Extension.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getLending(int)
     */
    @Override
    public final Lending getLending(final int id) throws DataSourceException {
        LOGGER.debug("get lending for id " + id);
        try {
            ResultSetHandler<List<Lending>> resultSetHandler = new BeanListHandler<Lending>(
                    Lending.class);

            List<Lending> lendings = run.query("SELECT * FROM " + LENDING
                    + " WHERE ID = " + id, resultSetHandler);

            if (lendings == null || lendings.size() == 0) {
                LOGGER.error("no lending for id " + id);
                throw new DataSourceException("Keine Ausleihe mit der ID " + id
                        + " vorhanden.");
            }

            return lendings.get(0);
        } catch (SQLException e) {
            LOGGER.error("get lending for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getLendings(java.util.List, int,
     * int, java.util.List)
     */
    @Override
    public final List<Lending> getLendings(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException {
        LOGGER.debug("get lendings");
        return getElements(constraints, from, to, order, LENDING, Lending.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getLentMedia(int, boolean)
     */
    @Override
    public final List<Medium> getLentMedia(final int max, final boolean order)
            throws DataSourceException {
        LOGGER.info("get lent media");
        try {

            String query = "SELECT "
                    + MEDIUM
                    + ".id, "
                    + MEDIUM
                    + ".title, tmp.rating, tmp2.lendingcount FROM "
                    + MEDIUM
                    + " LEFT JOIN (SELECT mediumid as id, CAST(AVG(rating) AS DECIMAL(3,2)) as rating FROM "
                    + RATING
                    + " GROUP BY mediumid) AS tmp ON "
                    + MEDIUM
                    + ".id = tmp.id "
                    + "LEFT JOIN (SELECT mediumid as id, SUM(lendingcount) as lendingcount "
                    + "FROM " + EXEMPLAR + " GROUP BY mediumid) AS tmp2 ON "
                    + MEDIUM + ".id=tmp2.id " + "ORDER BY tmp2.lendingcount ";

            if (order) {
                query += "DESC NULLS LAST";
            } else {
                query += "ASC NULLS FIRST";
            }

            ResultSetHandler<List<Medium>> resultSetHandler = new BeanListHandler<Medium>(
                    Medium.class);

            List<Medium> mediums = run.query(query, resultSetHandler);

            LOGGER.debug("mediums " + mediums.size());
            if (max >= mediums.size()) {
                return mediums;
            }

            return mediums.subList(0, max);
        } catch (SQLException e) {
            LOGGER.error("get lent media error " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getMedium(int)
     */
    @Override
    public final Medium getMedium(final int id) throws DataSourceException {
        LOGGER.debug("get medium for id " + id);
        try {
            ResultSetHandler<List<Medium>> resultSetHandler = new BeanListHandler<Medium>(
                    Medium.class);

            List<Medium> mediums = run.query("SELECT * FROM " + MEDIUM
                    + " WHERE ID = " + id, resultSetHandler);

            if (mediums == null || mediums.size() == 0) {
                LOGGER.error("no medium for id " + id);
                throw new DataSourceException("Keine Medium mit der ID " + id
                        + " vorhanden.");
            }

            Medium m = mediums.get(0);

            try {
                final long sum = singleResultQuery("SELECT SUM(rating) FROM "
                        + RATING + " WHERE mediumid=" + id
                        + " GROUP BY mediumid");

                final long count = singleResultQuery("SELECT COUNT(rating) FROM "
                        + RATING
                        + " WHERE mediumid="
                        + id
                        + " GROUP BY mediumid");

                double rating = 0;

                if (count != 0) {
                    rating = (double) sum / (double) count;
                }

                m.setRating(rating);
                m.setRatingCount((int) count);
            } catch (SQLException e) {
                LOGGER.error("get rating/ratingcount failure "
                        + e.getLocalizedMessage());
                throw new DataSourceException(e.getLocalizedMessage());
            }

            return m;
        } catch (SQLException e) {
            LOGGER.error("get medium for id failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getMediums(java.util.List, int,
     * int, java.util.List)
     */
    @Override
    public final List<Medium> getMediums(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException {
        LOGGER.debug("get mediums");

        List<Medium> mediums = getElements(constraints, from, to, order,
                MEDIUM, Medium.class);

        for (Medium m : mediums) {
            int i = m.getId();

            try {
                final long sum = singleResultQuery("SELECT SUM(rating) FROM "
                        + RATING + " WHERE mediumid=" + i
                        + " GROUP BY mediumid");

                final long count = singleResultQuery("SELECT COUNT(rating) FROM "
                        + RATING
                        + " WHERE mediumid="
                        + i
                        + " GROUP BY mediumid");

                double rating = 0;

                if (count != 0) {
                    rating = (double) sum / (double) count;
                }

                m.setRating(rating);
                m.setRatingCount((int) count);
            } catch (SQLException e) {
                LOGGER.error("get rating/ratingcount failure: "
                        + e.getLocalizedMessage());
                throw new DataSourceException(e.getLocalizedMessage());
            }
        }
        return mediums;
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getMediumType(int)
     */
    @Override
    public final MediumType getMediumType(final int id)
            throws DataSourceException {
        LOGGER.debug("get mediumtype for id " + id);
        try {
            ResultSetHandler<List<MediumType>> resultSetHandler = new BeanListHandler<MediumType>(
                    MediumType.class);

            List<MediumType> mediumTypes = run.query("SELECT * FROM "
                    + MEDIUMTYPE + " WHERE ID = " + id, resultSetHandler);

            if (mediumTypes == null || mediumTypes.size() == 0) {
                LOGGER.error("no mediumtype for id " + id);
                throw new DataSourceException("Kein Medientyp mit der ID " + id
                        + " vorhanden.");
            }

            return mediumTypes.get(0);
        } catch (SQLException e) {
            LOGGER.error("get mediumtype for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getMediumTypes(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public final List<MediumType> getMediumTypes(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get mediumtypes");
        return getElements(constraints, from, to, order, MEDIUMTYPE,
                MediumType.class);
    }

    /**
     * Ermittelt einen Primärschlüssel im Bereich <code>minID</code> bis
     * java.lang.Integer.MAX_VALUE für die übergebene Tabelle.
     *
     * @param table
     *            Die entsprechende Tabelle.
     * @param minID
     *            Die minimale ID, die der Schlüssel haben darf.
     * @return Der neue Primärschlüssel.
     * @throws SQLException
     *             Falls kein Primärschlüssel gefunden werden kann.
     * @throws DataSourceException
     *             Falls kein Schlüssel mehr frei ist.
     */
    private int getNewId(final String table, final int minID)
            throws SQLException, DataSourceException {
        final String query = "SELECT MIN(tid) FROM "
                + "(SELECT (id + 1) as tid FROM " + table
                + ") AS t1 LEFT JOIN " + "(SELECT id FROM " + table
                + ") AS t2 ON t1.tid = t2.id "
                + "WHERE t2.id IS NULL AND t1.tid >= " + minID
                + " AND t1.tid <= " + Integer.MAX_VALUE;

        Connection connection = dataSource.getConnection();
        try {
            Statement stmt = connection.createStatement();
            try {
                ResultSet rs = stmt.executeQuery(query);
                try {
                    if (rs.next()) {
                        Long l = rs.getLong(1);
                        if (rs.wasNull()) {
                            int i = getNumberOfElements(table, null);

                            if (i == 0) {
                                return minID;
                            }

                        } else {
                            return l.intValue();
                        }
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stmt.close();
            }
        } finally {
            connection.close();
        }
        LOGGER.error("Keine freie ID verfügbar!");
        throw new DataSourceException("Keine freie ID verfügbar!");
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getNews(int)
     */
    @Override
    public final News getNews(final int id) throws DataSourceException {
        LOGGER.debug("get reservation for id " + id);
        try {
            ResultSetHandler<List<News>> resultSetHandler = new BeanListHandler<News>(
                    News.class);

            List<News> news = run.query("SELECT * FROM " + NEWS + " WHERE id="
                    + id, resultSetHandler);

            if (news == null || news.size() == 0) {
                throw new DataSourceException("Keine Neuigkeit mit der ID "
                        + id + " vorhanden.");
            }

            return news.get(0);
        } catch (SQLException e) {
            LOGGER.error("get news for id failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getNews(java.util.List, int, int,
     * java.util.List)
     */
    @Override
    public final List<News> getNews(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException {
        LOGGER.debug("get news");
        return getElements(constraints, from, to, order, NEWS, News.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfCategories(java.util.List)
     */
    @Override
    public final int getNumberOfCategories(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of categories");
        return getNumberOfElements(CATEGORY, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfClosedTimes(java.util.List)
     */
    @Override
    public final int getNumberOfClosedTimes(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of closedtime");
        return getNumberOfElements(CLOSEDTIME, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfCommentaries(java.util.
     * List)
     */
    @Override
    public final int getNumberOfCommentaries(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of commentaries");
        return getNumberOfElements(COMMENTARY, constraints);
    }

    /**
     * Gibt die Anzahl der Elemente aus der entsprechenden Tabelle zurück,
     * welche die Bedingungen erffüllen.
     *
     * @param table
     *            Die zu durchsuchende Datenbanktabelle.
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @return Die Anzahl der Elemente.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    public final int getNumberOfElements(final String table,
            final List<Constraint> constraints) throws DataSourceException {
        LOGGER.debug("get number of elements for table " + table);
        try {
            Connection connection = dataSource.getConnection();
            try {
                String query = "SELECT COUNT(*) FROM " + table
                        + toQuery(constraints);
                LOGGER.debug("get number of elements: " + query);

                PreparedStatement stmt = connection.prepareStatement(query);
                try {
                    fillInArguments(constraints, stmt);
                    ResultSet rs = stmt.executeQuery();
                    try {
                        // go to first row
                        if (rs.next()) {
                            int count = rs.getInt(1);
                            return count;
                        } else {
                            LOGGER.error("SQL result has no row");
                            return 0;
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfExemplar(java.util.List)
     */
    @Override
    public final int getNumberOfExemplars(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of exemplars");
        return getNumberOfElements(EXEMPLAR, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfExtensions(java.util.List)
     */
    @Override
    public final int getNumberOfExtensions(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of extensions");
        return getNumberOfElements(EXTENSION, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfLendings(java.util.List)
     */
    @Override
    public final int getNumberOfLendings(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of lendings");
        return getNumberOfElements(LENDING, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfMediums(java.util.List)
     */
    @Override
    public final int getNumberOfMediums(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of mediums");
        return getNumberOfElements(MEDIUM, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfMediumType(java.util.List)
     */
    @Override
    public final int getNumberOfMediumType(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of mediumtypes");
        return getNumberOfElements(MEDIUMTYPE, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getNumberOfNews(java.util.List)
     */
    @Override
    public final int getNumberOfNews(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of news");
        return getNumberOfElements(NEWS, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfProperties(java.util.List)
     */
    @Override
    public final int getNumberOfProperties(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of property");
        return getNumberOfElements(PROPERTY, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfRatings(java.util.List)
     */
    @Override
    public final int getNumberOfRatings(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of rating");
        return getNumberOfElements(RATING, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfReaders(java.util.List)
     */
    @Override
    public final int getNumberOfReaders(List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of readers");
        return getNumberOfElements(READER, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#getNumberOfReservations(java.util.
     * List)
     */
    @Override
    public final int getNumberOfReservations(final List<Constraint> constraints)
            throws DataSourceException {
        LOGGER.debug("get number of reservations");
        return getNumberOfElements(RESERVATION, constraints);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getOpeningTime(int)
     */
    @Override
    public final OpeningTime getOpeningTime(final int id)
            throws DataSourceException {
        LOGGER.debug("get openingtime for id " + id);
        try {
            ResultSetHandler<List<OpeningTime>> resultSetHandler = new BeanListHandler<OpeningTime>(
                    OpeningTime.class);

            List<OpeningTime> openingTime = run.query("SELECT * FROM "
                    + OPENINGTIME + " WHERE ID = " + id, resultSetHandler);

            if (openingTime == null || openingTime.size() == 0) {
                LOGGER.error("no openingtime for id " + id);
                throw new DataSourceException("Keine Öffnungszeit mit der ID "
                        + id + " vorhanden.");
            }

            return openingTime.get(0);
        } catch (SQLException e) {
            LOGGER.error("get openingtime for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getProperties(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public final List<Property> getProperties(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get properties");
        return getElements(constraints, from, to, order, PROPERTY,
                Property.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getProperty(int)
     */
    @Override
    public final Property getProperty(final int id) throws DataSourceException {
        LOGGER.debug("get property for id " + id);
        try {
            ResultSetHandler<List<Property>> resultSetHandler = new BeanListHandler<Property>(
                    Property.class);

            List<Property> properties = run.query("SELECT * FROM " + PROPERTY
                    + " WHERE id=" + id, resultSetHandler);

            if (properties == null || properties.size() == 0) {
                LOGGER.error("no property for id " + id);
                throw new DataSourceException("Keine Eigenschaft mit der id "
                        + id + "vorhanden.");
            }

            return properties.get(0);
        } catch (SQLException e) {
            LOGGER.error("get property for id failure: "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getRatedMedia(int, boolean)
     */
    @Override
    public final List<Medium> getRatedMedia(final int max, final boolean order)
            throws DataSourceException {
        LOGGER.info("get rated media ");
        try {
            String query = "SELECT " + MEDIUM + ".id, " + MEDIUM + ".title, "
                    + MEDIUM + ".mediumtype, tmp.rating, tmp.ratingcount "
                    + "FROM " + MEDIUM + " INNER JOIN (SELECT " + MEDIUM
                    + ".id AS id, CAST(AVG(" + RATING + ".rating "
                    + ") AS DECIMAL(3,2)) AS " + RATING + ", COUNT(" + RATING
                    + ") AS " + RATING + "count FROM " + MEDIUM
                    + " INNER JOIN " + RATING + " ON " + MEDIUM + ".id = "
                    + RATING + ".mediumid GROUP BY " + MEDIUM
                    + ".id) AS tmp ON " + MEDIUM
                    + ".id = tmp.id ORDER BY rating ";

            if (order) {
                query += "DESC NULLS LAST, ratingcount DESC NULLS LAST";
            } else {
                query += "ASC NULLS FIRST, ratingcount ASC NULLS FIRST";
            }

            ResultSetHandler<List<Medium>> resultSetHandler = new BeanListHandler<Medium>(
                    Medium.class);

            List<Medium> mediums = run.query(query, resultSetHandler);

            if (max >= mediums.size()) {
                return mediums;
            }

            return mediums.subList(0, max + 1);
        } catch (SQLException e) {
            LOGGER.error("get rated media error " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getRating(int)
     */
    @Override
    public final Rating getRating(final int id) throws DataSourceException {
        LOGGER.debug("get rating for id " + id);
        try {
            ResultSetHandler<List<Rating>> resultSetHandler = new BeanListHandler<Rating>(
                    Rating.class);

            List<Rating> ratings = run.query("SELECT * FROM " + RATING
                    + " WHERE id=" + id, resultSetHandler);

            if (ratings == null || ratings.size() == 0) {
                LOGGER.error("no rating for id " + id);
                throw new DataSourceException("Keine Bewertung mit der id "
                        + id + "vorhanden.");
            }

            return ratings.get(0);
        } catch (SQLException e) {
            LOGGER.error("get rating for id failure: "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getRating(java.util.List, int,
     * int, java.util.List)
     */
    @Override
    public final List<Rating> getRating(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException {
        LOGGER.debug("get rating");
        return getElements(constraints, from, to, order, RATING, Rating.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getReader(int)
     */
    @Override
    public final Reader getReader(final int id) throws DataSourceException {
        LOGGER.debug("get reader for id " + id);

        if (id < -1) {
            LOGGER.error("id must be greater or equal then -1");
            throw new IllegalArgumentException(
                    "id must be greater or equal then -1");
        }

        try {
            ResultSetHandler<List<Reader>> resultSetHandler = new BeanListHandler<Reader>(
                    Reader.class);

            List<Reader> readers = run.query("SELECT * FROM " + READER
                    + " WHERE ID = " + id, resultSetHandler);

            if (readers == null || readers.size() == 0) {
                LOGGER.error("no reader for id " + id);
                throw new DataSourceException("Kein Benutzer mit der ID " + id
                        + " vorhanden.");
            }

            return readers.get(0);
        } catch (SQLException e) {
            LOGGER.error("get reader for id failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getReaders(java.util.List)
     */
    @Override
    public final List<Reader> getReaders(List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException {
        LOGGER.debug("get readers");

        return getElements(constraints, from, to, order, READER, Reader.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getReservation(int)
     */
    @Override
    public final Reservation getReservation(final int id)
            throws DataSourceException {
        LOGGER.debug("get reservation for id " + id);
        try {
            ResultSetHandler<List<Reservation>> resultSetHandler = new BeanListHandler<Reservation>(
                    Reservation.class);

            List<Reservation> reservations = run.query("SELECT * FROM "
                    + RESERVATION + " WHERE id=" + id, resultSetHandler);

            if (reservations == null || reservations.size() == 0) {
                LOGGER.error("no reservation for id " + id);
                throw new DataSourceException("Keine Vormerkung mit der ID "
                        + id + " vorhanden.");
            }

            return reservations.get(0);
        } catch (SQLException e) {
            LOGGER.error("get reservation for id failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#getReservations(java.util.List,
     * int, int, java.util.List)
     */
    @Override
    public final List<Reservation> getReservations(
            final List<Constraint> constraints, final int from, final int to,
            final List<OrderBy> order) throws DataSourceException {
        LOGGER.debug("get reservations");
        return getElements(constraints, from, to, order, RESERVATION,
                Reservation.class);
    }

    /**
     * Gibt die Namen der Datenbanktabellen in kleinbuchstaben zurück.
     *
     * @return Eine Liste mit Namen aller Datenbanktabellen.
     * @throws SQLException
     *             Falls SQL-Fehler auftreten.
     */
    private List<String> getTableNames() throws SQLException {
        LOGGER.debug("get table names");
        DatabaseMetaData dbMeta;
        List<String> result = new ArrayList<String>();
        Connection dbConnection = dataSource.getConnection();
        try {
            dbMeta = dbConnection.getMetaData();
            LOGGER.debug("URL of database " + dbMeta.getURL());
            LOGGER.debug("database version: major="
                    + dbMeta.getDatabaseMajorVersion() + " minor="
                    + dbMeta.getDatabaseMinorVersion() + " product_version="
                    + dbMeta.getDatabaseProductVersion() + " product_name="
                    + dbMeta.getDatabaseProductName());

            ResultSet rs = dbMeta.getTables(null, null, null,
                    new String[] { "TABLE" });
            try {
                while (rs.next()) {
                    String theTableName = rs.getString("TABLE_NAME");
                    result.add(theTableName.toLowerCase());
                }
            } finally {
                rs.close();
            }
            return result;
        } finally {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                LOGGER.debug("error closing database connection.");
                // ignore, nothing to be done here anyway
            }
        }
    }

    /**
     * Returns the values of all non-static fields of <code>object</code> not
     * contained in <code>toIgnore</code>. If a field is found in
     * <code>replace</code>, the corresponding replacement value in
     * <code>replace</code> is used instead of the actual value of the field.
     * Otherwise the field's value is used.
     *
     * @param object
     *            the object whose fields are to be returned
     * @param toIgnore
     *            field names that should not be stored
     * @param replace
     *            values to be replaced for storing
     * @param numberOfFields
     *            the number of fields returned
     * @return the list of field values (has length numberOfFields)
     * @throws SQLException
     *             thrown in case the object cannot be inserted
     */
    private Object[] getValues(final Object object, final Set<String> toIgnore,
            final HashMap<String, Object> replace, final int numberOfFields)
            throws SQLException {
        Object[] values = new Object[numberOfFields];
        int i = 0;

        HashMap<String, Field> fields = Reflection.getTransitiveFields(
                new HashMap<String, Field>(), object.getClass());

        for (Field f : fields.values()) {
            String fieldName = f.getName();
            if (relevantField(toIgnore, f)) {
                try {
                    f.setAccessible(true);
                    values[i] = f.get(object);
                    if (replace != null) {
                        final Object replacement = replace.get(fieldName);
                        if (replacement != null) {
                            values[i] = replacement;
                        }
                    }
                    if (values[i] instanceof Date) {
                        // Date must be converted
                        values[i] = toDateFormat((Date) values[i]);
                    }
                    i++;
                } catch (IllegalArgumentException e) {
                    // internal error message, hence, we are not using
                    // Messages.get(...)
                    throw new SQLException(fieldName
                            + " is an illegal argument: " + e.getMessage());
                } catch (IllegalAccessException e) {
                    // internal error message, hence, we are not using
                    // Messages.get(...)
                    throw new SQLException("cannot access field " + fieldName
                            + ": " + e.getMessage());
                }
            }
        }
        return values;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * swp.bibjsf.persistence.Persistence#importMediums(java.io.InputStream)
	 */
	@Override
    public final int importMediums(final InputStream input)
            throws DataSourceException {
        LOGGER.info("import mediums");

        final String[] expectedColumns = { "ID", "AUTHORS", "AVGRATING",
                "CATEGORIES", "DATEOFADDITION", "DATEOFPUBLICATION",
                "DESCRIPTION", "NOTE", "IMAGEURL", "INDUSTRIALIDENTIFIER",
                "LANGUAGE", "LOCATION", "PAGECOUNT", "PREVIEWLINK", "PRICE",
                "PRINTTYPE", "PUBLISHER", "SUBTITLE", "TITLE", "VOTES" };

        try {
            if (input.markSupported()) {
                LOGGER.debug("mark placed");
                input.mark(0);
            }

            CSVReader csvReader = new CSVReader(input, DEFAULT_SEPARATOR,
                    DEFAULT_QUOTE);

            if (!csvReader.hasColumns(expectedColumns)) {
                if (input.markSupported()) {
                    LOGGER.debug("reset inputstream");
                    input.reset();
                    return importTable(input, READER);
                } else {
                    throw new DataSourceException(
                            "Die Datei konnte nicht eingelesen werden.");
                }
            }

            if (csvReader.numberOfRows() == 0) {
                LOGGER.error("CSV file is empty");
                throw new DataSourceException("Die CSV-Datei ist leer.");
            }

            Connection con = dataSource.getConnection();
            try {
                con.setAutoCommit(true);
                HashSet<String> cat = new HashSet<String>();
                // Fehlerhafte medien
                StringBuilder error = new StringBuilder("");

                // SQL-Statements erstellen
                StringBuilder query = new StringBuilder("INSERT INTO " + MEDIUM
                        + " (ID, TITLE, LOCATION, "
                        + "DATEOFPUBLICATION, CATEGORY, "
                        + "SUBTITLE, IMAGEURL, LANGUAGE, PRICE, "
                        + "DESCRIPTION, DATEOFADDITION, "
                        + "MEDIUMTYPE, ATTRIBUTE0, "
                        + "ATTRIBUTE1, ATTRIBUTE2, "
                        + "ATTRIBUTE3, ATTRIBUTE4, "
                        + "ATTRIBUTE5, ATTRIBUTE6, "
                        + "ATTRIBUTE7, ATTRIBUTE8, ATTRIBUTE9) VALUES ( "
                        + "?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                StringBuilder query2 = new StringBuilder("INSERT INTO "
                        + EXEMPLAR + " (ID, STATUS, LATESTREADER, NOTE, "
                        + "PLACE, DATEOFADDITION, "
                        + "LENDINGCOUNT, MEDIUMID) VALUES ( "
                        + "?, 'leihbar', NULL, ?, ?, ?, 0, ?)");

                StringBuilder query3 = new StringBuilder("INSERT INTO "
                        + CATEGORY + " (ID, NAME) VALUES ( " + "?, ?)");

                final DateFormat formatter = new SimpleDateFormat(DATEFORMAT);

                PreparedStatement stmt = con.prepareStatement(query.toString());
                PreparedStatement stmt2 = con.prepareStatement(query2
                        .toString());
                PreparedStatement stmt3 = con.prepareStatement(query3
                        .toString());

                try {
                    int i = 0;
                    
                    for (int row = 0; row < csvReader.numberOfRows(); row++) {
                        LOGGER.debug("processing line " + (row + 1));
                        try {
                            String id = csvReader.get("ID", row);
                            String title = csvReader.get("TITLE", row);
                            String location = csvReader.get("LOCATION", row);
                            String dateOfAddition = csvReader.get(
                                    "DATEOFADDITION", row);
                            String dateOfPublication = csvReader.get(
                                    "DATEOFPUBLICATION", row);
                            String categories = csvReader
                                    .get("CATEGORIES", row);

                            String[] cats = categories.split(",");

                            for (String s : cats) {
                                cat.add(s.trim());                                
                            }

                            String subtitle = csvReader.get("SUBTITLE", row);
                            String imageUrl = csvReader.get("IMAGEURL", row);
                            String language = csvReader.get("LANGUAGE", row);
                            String price = csvReader.get("PRICE", row);
                            String description = csvReader.get("DESCRIPTION",
                                    row);
                           
                            String authors = csvReader.get("AUTHORS", row);
                            String note = csvReader.get("NOTE", row);
                            String isbn = csvReader.get("INDUSTRIALIDENTIFIER",
                                    row);
                            String pagecount = csvReader.get("PAGECOUNT", row);
                            String preview = csvReader.get("PREVIEWLINK", row);
                            String publisher = csvReader.get("PUBLISHER", row);

                            if (id.isEmpty() || title.isEmpty()) {
                                error.append((row + 1) + ", ");
                                continue;
                            }

                            i = getNewId(MEDIUM, i);

                            stmt.setInt(1, i);
                            stmt.setString(2, title);
                            stmt.setNull(3, java.sql.Types.VARCHAR);

                            if (dateOfPublication.isEmpty()) {
                                stmt.setNull(4, java.sql.Types.DATE);
                            } else {
                                try {
                                    java.sql.Date sqlDate = new java.sql.Date(
                                            formatter.parse(dateOfPublication)
                                                    .getTime());
                                    stmt.setDate(4, sqlDate);
                                } catch (ParseException e) {
                                    throw new DataSourceException(
                                            "Falsches Datumsformat in Zeile "
                                                    + (row + 1));
                                }
                            }

                            stmt.setString(5, categories);
                            stmt.setString(6, subtitle);
                            stmt.setString(7, imageUrl);
                            stmt.setString(8, language);
                            if (price.isEmpty()) {
                                stmt.setNull(9, java.sql.Types.DECIMAL);
                            } else {
                                stmt.setBigDecimal(9, new BigDecimal(price));
                            }
                            stmt.setString(10, description);
                        
                            if (dateOfAddition.isEmpty()) {
                                stmt.setNull(11, java.sql.Types.DATE);
                            } else {
                                try {
                                    java.sql.Date sqlDate = new java.sql.Date(
                                            formatter.parse(dateOfAddition)
                                                    .getTime());
                                    stmt.setDate(11, sqlDate);
                                } catch (ParseException e) {
                                    throw new DataSourceException(
                                            "Falsches Datumsformat in Zeile "
                                                    + (row + 1));
                                }
                            }

                            stmt.setInt(12, 0);
                            stmt.setString(13, authors);
                            stmt.setString(14, isbn);
                            stmt.setNull(15, java.sql.Types.VARCHAR);
                            stmt.setString(16, pagecount);
                            stmt.setString(17, publisher);
                            stmt.setString(18, preview);
                            stmt.setNull(19, java.sql.Types.VARCHAR);
                            stmt.setNull(20, java.sql.Types.VARCHAR);
                            stmt.setNull(21, java.sql.Types.VARCHAR);
                            stmt.setNull(22, java.sql.Types.VARCHAR);

                            // EXEMPLAR
                            stmt2.setInt(1, Integer.parseInt(id));
                            stmt2.setString(2, note);
                            stmt2.setString(3, location);

                            if (dateOfAddition.isEmpty()) {
                                stmt2.setNull(4, java.sql.Types.DATE);
                            } else {
                                try {
                                    java.sql.Date sqlDate = new java.sql.Date(
                                            formatter.parse(dateOfAddition)
                                                    .getTime());
                                    stmt2.setDate(4, sqlDate);
                                } catch (ParseException e) {
                                    throw new DataSourceException(
                                            "Falsches Datumsformat in Zeile "
                                                    + (row + 1));
                                }
                            }

                            stmt2.setInt(5, i);
                           
                         
                    
                        

                                    List<Constraint> cons = new ArrayList<Constraint>();
                                    if(title != null && !title.equals("")){
                                    cons.add(new Constraint("title", "LIKE",
                                            title, "AND",
                                            Constraint.AttributeType.STRING));
                                    }
                                    if(language != null && !language.equals("")){
                                    cons.add(new Constraint("language", "=",
                                            language, "AND",
                                            Constraint.AttributeType.STRING));
                                    }
                                    if(dateOfPublication != null ){
                                    cons.add(new Constraint("dateofpublication", "=",
                                            dateOfPublication, "AND",
                                            Constraint.AttributeType.INTEGER));
                                    }
                                    cons.add(new Constraint("mediumType", "=",
                                            "0", "AND",
                                            Constraint.AttributeType.INTEGER));
                                    
                                    LOGGER.info("Test for " + title);
                                    
                                    Medium medium;
                                    List<Medium> mediums = new ArrayList<>();
                                    if( cons.size() > 1 ){
                                    mediums = getMediums(cons, 0, 1, null);
                                    }
                                    if( mediums.size() > 0){
                                    	medium = mediums.get(0);                                    

                                    LOGGER.debug("found medium " + medium.getTitle() + " " + medium.getId() ) ;

                                    stmt2.setInt(5, medium.getId());
                                    }
                            	else{
                            		   try {
                                           LOGGER.debug("import medium " +title);
                                           stmt.execute();
                                       } catch (SQLException e) {
                                           if (e.getSQLState().startsWith("23")) {
                                               LOGGER.debug("medium already exists");
                                           }else {
                                   LOGGER.error("import error "
                                           + e.getLocalizedMessage()
                                           + e.getSQLState());
                                   throw new DataSourceException(
                                           e.getLocalizedMessage());
                                           }
                                       }
                            	}

                                      
                           
                    
                                LOGGER.debug("import exemplar " + stmt2);
                                try {
                                    stmt2.execute();
                                } catch (SQLException e) {
                                    LOGGER.debug("import exemplar error "
                                            + e.getLocalizedMessage() + " "
                                            + e.getSQLState());
                                    
                                }
                            
                                    }
                        
                        catch (UnknownColumn e) {
                            // Kann nicht passieren.
                        }
                    }
                    int j = 0;
                    for (String str : cat) {
                    	if(str != null && !str.equals("")){
                        i = getNewId(CATEGORY, j);
                        stmt3.setInt(1, i);
                        stmt3.setString(2, str);

                        try {
                            stmt3.execute();
                        } catch (SQLException e) {
                            if (e.getSQLState().startsWith("23")) {
                                LOGGER.debug("category already exists");
                                // Do nothing here.
                            } else {
                                LOGGER.error("error adding category");
                                throw new DataSourceException(
                                        "Kategorie konnte nicht eingeügt werden.");
                            }

                        }

                    }

                    if (!error.toString().equals("")) {
                        error.deleteCharAt(error.length() - 1);
                        throw new DataSourceException(
                                "Folgende Zeilen konnten nicht importiert werden: "
                                        + error.toString());
                    }
                    }
                } finally {
                    stmt.close();
                }
                con.commit();
                LOGGER.debug("inserts are committed");
            } catch (RuntimeException e) {
                LOGGER.debug("inserts are rolled back");
                con.rollback();
                throw e;
            } catch (Exception e) {
                LOGGER.debug("inserts are rolled back");
                con.rollback();
                throw e;
            } finally {
                con.close();
            }
            return csvReader.numberOfRows();
        } catch (CorruptInput e) {
            throw new DataSourceException(e.getLocalizedMessage());
        } catch (SQLException e) {
            throw new DataSourceException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#importReaders(java.io.InputStream)
     */
    @Override
    public final int importReaders(final InputStream input)
            throws DataSourceException {
        LOGGER.debug("import readers");

        final String[] expectedColumns = { "ID", "USERNAME", "PASSWORD",
                "FIRSTNAME", "LASTNAME", "BIRTHDAY", "STREET", "ZIPCODE",
                "CITY", "PHONE", "EMAIL", "ENTRYDATE", "LASTUSE", "NOTE" };

        try {
            if (input.markSupported()) {
                LOGGER.debug("mark placed");
                input.mark(0);
            }

            CSVReader csvReader = new CSVReader(input, DEFAULT_SEPARATOR,
                    DEFAULT_QUOTE);

            if (!csvReader.hasColumns(expectedColumns)) {
                LOGGER.debug("reset inputstream");
                input.reset();
                return importTable(input, READER);
            }

            if (csvReader.numberOfRows() == 0) {
                LOGGER.error("Die CSV-Datei ist leer.");
                throw new DataSourceException("Die CSV-Datei ist leer.");
            }

            Connection con = dataSource.getConnection();
            try {
                con.setAutoCommit(false);

                // Fehlerhafte reader
                StringBuilder error = new StringBuilder("");

                // SQL-Statement erstellen
                StringBuilder query = new StringBuilder(
                        "INSERT INTO "
                                + READER
                                + " (STATUS, HISTORYACTIVATED, GROUPID, ID, USERNAME, PASSWORD, "
                                + "FIRSTNAME, LASTNAME, BIRTHDAY, STREET, ZIPCODE, CITY, PHONE, "
                                + "EMAIL, ENTRYDATE, LASTUSE, NOTE) VALUES ('active', false, 'USER', ");

                for (int i = 0; i < expectedColumns.length; i++) {
                    query.append("?,");
                }

                // Komma am ende entfernen
                query.deleteCharAt(query.length() - 1);

                // Statement schließen
                query.append(")");

                final DateFormat formatter = new SimpleDateFormat(DATEFORMAT);

                PreparedStatement stmt = con.prepareStatement(query.toString());

                try {
                    for (int row = 0; row < csvReader.numberOfRows(); row++) {
                        LOGGER.debug("processing line " + (row + 1));
                        try {
                            String id = csvReader.get("ID", row);
                            String username = csvReader.get("USERNAME", row);
                            String password = csvReader.get("PASSWORD", row);
                            String firstname = csvReader.get("FIRSTNAME", row);
                            String lastname = csvReader.get("LASTNAME", row);
                            String birthday = csvReader.get("BIRTHDAY", row);
                            String street = csvReader.get("STREET", row);
                            String zipcode = csvReader.get("ZIPCODE", row);
                            String city = csvReader.get("CITY", row);
                            String phone = csvReader.get("PHONE", row);
                            String email = csvReader.get("EMAIL", row);
                            String entryDate = csvReader.get("ENTRYDATE", row);
                            String lastUse = csvReader.get("LASTUSE", row);
                            String note = csvReader.get("NOTE", row);

                            if (id.isEmpty() || username.isEmpty()
                                    || password.isEmpty() || email.isEmpty()) {
                                LOGGER.debug("illegal data in row " + (row + 1));
                                error.append((row + 1) + ", ");
                                continue;
                            }

                            stmt.setInt(1, Integer.parseInt(id));
                            stmt.setString(2, username);
                            stmt.setString(3, password);
                            stmt.setString(4, firstname);
                            stmt.setString(5, lastname);

                            if (birthday.isEmpty()) {
                                stmt.setNull(6, java.sql.Types.DATE);
                            } else {
                                try {
                                    java.sql.Date sqlDate = new java.sql.Date(
                                            formatter.parse(birthday).getTime());
                                    stmt.setDate(6, sqlDate);
                                } catch (ParseException e) {
                                    throw new DataSourceException(
                                            "Falsches Datumsformat in Zeile "
                                                    + (row + 1));
                                }
                            }

                            stmt.setString(7, street);
                            stmt.setString(8, zipcode);
                            stmt.setString(9, city);
                            stmt.setString(10, phone);
                            stmt.setString(11, email);
                            if (entryDate.isEmpty()) {
                                stmt.setNull(12, java.sql.Types.DATE);
                            } else {
                                try {
                                    java.sql.Date sqlDate = new java.sql.Date(
                                            formatter.parse(entryDate)
                                                    .getTime());
                                    stmt.setDate(12, sqlDate);
                                } catch (ParseException e) {
                                    throw new DataSourceException(
                                            "Falsches Datumsformat in Zeile "
                                                    + (row + 1));
                                }
                            }
                            if (lastUse.isEmpty()) {
                                stmt.setNull(13, java.sql.Types.DATE);
                            } else {
                                try {
                                    java.sql.Date sqlDate = new java.sql.Date(
                                            formatter.parse(lastUse).getTime());
                                    stmt.setDate(13, sqlDate);
                                } catch (ParseException e) {
                                    throw new DataSourceException(
                                            "Falsches Datumsformat in Zeile "
                                                    + row + " Spalte 13");
                                }
                            }
                            stmt.setString(14, note);

                        } catch (UnknownColumn e) {
                            // Kann nicht passieren
                            LOGGER.error("Critical Error " + e);
                            throw new DataSourceException(
                                    "Ein unerwarteter Fehler ist aufgetreten");
                        }

                        try {
                            LOGGER.debug("inserting reader " + stmt);
                            stmt.execute();
                        } catch (SQLException e) {
                            if (e.getSQLState().startsWith("23")) {
                                LOGGER.error("Ignoring Reader because "
                                        + e.getLocalizedMessage());
                            } else {
                                LOGGER.error("Import Error "
                                        + e.getLocalizedMessage());
                                throw new DataSourceException(
                                        e.getLocalizedMessage());
                            }
                        }
                    }

                    if (!error.toString().equals("")) {
                        error.deleteCharAt(error.length() - 1);
                        throw new DataSourceException(
                                "Folgende Zeilen konnten nicht importiert werden: "
                                        + error.toString());
                    }
                } finally {
                    stmt.close();
                }

                con.commit();
                LOGGER.debug("inserts are committed");
            } catch (RuntimeException e) {
                LOGGER.debug("inserts are rolled back");
                con.rollback();
                throw e;
            } catch (Exception e) {
                LOGGER.debug("inserts are rolled back");
                con.rollback();
                throw e;
            } finally {
                con.close();
            }

            return csvReader.numberOfRows();
        } catch (CorruptInput e) {
            throw new DataSourceException(e.getLocalizedMessage());
        } catch (SQLException e) {
            throw new DataSourceException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Reads CSV data from <code>input</code> and inserts them into
     * <code>table</code>.
     *
     * @param input
     *            input stream with CSV data to be imported
     * @param table
     *            Der Name der zu befüllenden Tabelle.
     * @return Die Anzahl eingefügter Tabelleneinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    public final int importTable(final InputStream input, final String table)
            throws DataSourceException {
        final ColumnDescriptor[] columns = getColumns(table);
        final String[] expectedColumns = toLabels(columns);
        if (expectedColumns.length == 0) {
            LOGGER.debug("table with no columns");
            return 0;
        }
        try {
            CSVReader csvReader = new CSVReader(input, DEFAULT_SEPARATOR,
                    DEFAULT_QUOTE);
            if (!csvReader.hasColumns(expectedColumns)) {
                throw new DataSourceException(Messages.get("unexpectedHeader")
                        + ": " + toString(expectedColumns));
            }

            if (csvReader.numberOfRows() == 0) {
                LOGGER.debug("CSV file is empty");
                return 0;
            }
            Connection con = dataSource.getConnection();
            try {
                con.setAutoCommit(false);
                // construct SQL statement
                StringBuilder fields = new StringBuilder("INSERT INTO " + table
                        + " (");
                StringBuilder placeholders = new StringBuilder("VALUES (");

                // append fields by "field1, field2, ..., fieldN"
                // append placeholders by "?, ?, ..., ?"
                // where field1 is a non-static field
                // count the number of such fields in numberOfFields
                for (int i = 0; i < expectedColumns.length; i++) {
                    fields.append(expectedColumns[i]);
                    fields.append(",");
                    placeholders.append("?,");
                }
                // fields = "INSERT INTO <table> (field1, field2, ..., fieldN,"
                // placeholders = "VALUES (?, ?, ..., ?,"

                // remove last commas
                fields.deleteCharAt(fields.length() - 1);
                placeholders.deleteCharAt(placeholders.length() - 1);

                // close statement parts
                fields.append(")");
                placeholders.append(")");

                // fields = "INSERT INTO <table> (field1, field2, ..., fieldN)"
                // placeholders = "VALUES (?, ?, ..., ?)"

                final DateFormat formatter = new SimpleDateFormat(DATEFORMAT);
                // create prepared statement
                final String query = fields.toString()
                        + placeholders.toString();
                // query =
                // "INSERT INTO <table> (field1, field2, ..., fieldN)
                // VALUES (?, ?, ..., ?)"
                // The field names are read from an input file and, hence,
                // represented
                // potentially tainted values. Yet, the fields must correspond
                // to the
                // field names of <table>. Otherwise the SQL statement is
                // invalid. Hence,
                // if any of the fields were tainted, the query would fail.
                // Furthermore,
                // the values read from the input file are added via setX
                // statements to this
                // prepared statement with place holders. Consequently, the
                // query is save.
                PreparedStatement stmt = con.prepareStatement(query);
                try {
                    for (int row = 0; row < csvReader.numberOfRows(); row++) {
                        LOGGER.debug("processing line " + (row + 1));
                        // collect values
                        for (int col = 0; col < expectedColumns.length; col++) {
                            String value;
                            try {
                                value = csvReader
                                        .get(expectedColumns[col], row);
                            } catch (UnknownColumn e) {
                                throw new DataSourceException(
                                        Messages.get("unexpectedColumn") + " '"
                                                + expectedColumns[col] + "' "
                                                + Messages.get("inLine") + " "
                                                + (row + 2) + ": "
                                                + e.getLocalizedMessage());
                            }
                            if (value.isEmpty()) {
                                stmt.setNull(col + 1, columns[col].type);
                            } else if (columns[col].type == java.sql.Types.DATE) {
                                Date date;
                                try {
                                    date = formatter.parse(value);
                                    java.sql.Date sqlDate = new java.sql.Date(
                                            date.getTime());
                                    stmt.setDate(col + 1, sqlDate);
                                } catch (ParseException e) {
                                    throw new DataSourceException(
                                            Messages.get("unexpectedDateFormat")
                                                    + " '"
                                                    + value
                                                    + "' "
                                                    + Messages.get("inLine")
                                                    + " "
                                                    + (row + 2)
                                                    + "; "
                                                    + Messages.get("expected")
                                                    + " "
                                                    + DATEFORMAT
                                                    + ": "
                                                    + e.getLocalizedMessage());
                                }
                            } else {
                                stmt.setString(col + 1, value);
                            }
                        }

                        try {
                            stmt.execute();
                        } catch (SQLException e) {
                            if (e.getSQLState().startsWith("23")) {
                                LOGGER.debug("ignoring element because "
                                        + e.getLocalizedMessage());
                                // Ignoriere Datensatz (Datenbank wahrscheinlich
                                // aktueller)
                            } else {
                                LOGGER.error("Import Error "
                                        + e.getLocalizedMessage());
                                throw new DataSourceException(
                                        e.getLocalizedMessage());
                            }
                        }
                    }
                } finally {
                    stmt.close();
                }
                con.commit();
                LOGGER.debug("inserts are committed");
            } catch (RuntimeException e) {
                // This exception handler is subsumed by the following on
                // Exception.
                // It is there to please findbugs, which otherwise complains.
                LOGGER.debug("inserts are rolled back");
                con.rollback();
                throw e;
            } catch (Exception e) {
                LOGGER.debug("inserts are rolled back");
                con.rollback();
                throw e;
            } finally {
                con.close();
            }
            return csvReader.numberOfRows();
        } catch (CorruptInput e) {
            throw new DataSourceException(e.getLocalizedMessage());
        } catch (SQLException e) {
            throw new DataSourceException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Inserts <code>object</code> into <code>table</code> retrieving all fields
     * of the object. Only those objects are actually stored that are not listed
     * in <code>toIgnore</code>. If a field is found in <code>replace</code>,
     * the corresponding replacement value in <code>replace</code> is used
     * instead of the actual value of the field. Otherwise the field's value is
     * stored.
     *
     * @param table
     *            name of the table in which <code>object</code> is inserted
     * @param object
     *            the object to be stored
     * @param toIgnore
     *            field names that should not be stored
     * @param replace
     *            values to be replaced for storing
     * @throws SQLException
     *             thrown in case the object cannot be inserted
     */
    private void insert(final String table, final Object object,
            final Set<String> toIgnore, final HashMap<String, Object> replace)
            throws SQLException {
        LOGGER.debug("insert into table " + table);

        StringBuilder fields = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder placeholders = new StringBuilder("VALUES (");
        int numberOfFields = 0;

        // append fields by "field1, field2, ..., fieldN"
        // append placeholders by "?, ?, ..., ?"
        // where field1 is a non-static field
        // count the number of such fields in numberOfFields
        HashMap<String, Field> fieldsOfObject = Reflection.getTransitiveFields(
                new HashMap<String, Field>(), object.getClass());
        for (Field f : fieldsOfObject.values()) {
            if (relevantField(toIgnore, f)) {
                fields.append(f.getName());
                fields.append(",");
                placeholders.append("?,");
                numberOfFields++;
            }
        }

        // fields = "INSERT INTO <table> (field1, field2, ..., fieldN,"
        // placeholders = "VALUES (?, ?, ..., ?,"
        if (numberOfFields > 0) {
            // remove last commas
            fields.deleteCharAt(fields.length() - 1);
            placeholders.deleteCharAt(placeholders.length() - 1);

            // close statement parts
            fields.append(")");
            placeholders.append(")");

            // fields = "INSERT INTO <table> (field1, field2, ..., fieldN)"
            // placeholders = "VALUES (?, ?, ..., ?)"
            String sqlInsert = fields.toString() + placeholders.toString();
            // sqlInsert = "INSERT INTO <table> (field1, field2, ..., fieldN)
            // VALUES (?, ?, ..., ?)"

            // now collect the values of object's fields
            Object[] values = getValues(object, toIgnore, replace,
                    numberOfFields);
            // sqlInsert is built from fields collected from object, hence,
            // cannot be tainted and sqlInsert is safe.
            Connection connection = dataSource.getConnection();
            try {
                PreparedStatement stmt = connection.prepareStatement(sqlInsert);
                try {
                    // fill parameters of the statement
                    for (int i = 0; i < values.length; i++) {
                        if (values[i] == null) {
                            stmt.setNull(i + 1, java.sql.Types.VARCHAR);
                        }

                        /*
                         * We are currently not using enums. They would need to
                         * be mapped onto int or strings. Here is the code for
                         * the int mapping:
                         *
                         * else if (values[i] instanceof Enum) { // enums are
                         * mapped onto integer Enum e = (Enum)values[i];
                         * //stmt.setInt(i + 1, e.ordinal()); stmt.setString(i +
                         * 1, values[i].toString());
                         *
                         * Yet, the conversion back from the database to the
                         * bean is more complicated. We would need to provide
                         * our own BeanHandler for this conversion. }
                         */
                        else {
                            stmt.setObject(i + 1, values[i]);
                        }
                        LOGGER.debug("parameter "
                                + i
                                + ": "
                                + ((values[i] == null) ? "NULL" : values[i]
                                        .toString()));
                    }
                    LOGGER.debug("stmt = " + stmt.toString());
                    stmt.executeUpdate();
                } finally {
                    stmt.close();
                }
            } finally {
                connection.close();
            }
        } else {
            // we treat this as an error; it does not make sense to insert an
            // object with no fields
            throw new SQLException("entity " + object + " has no fields");
        }
    }

    /**
     * Erstellt den Administrator-Account, vorrausgesetzt dieser existiert nicht
     * bereits in der Datenbank.
     *
     * @throws SQLException
     *             Falls Probleme mit SQL auftreten.
     */
    private void insertAdmin() throws SQLException {
        LOGGER.debug("inserting admin");
        try {
            Reader admin = getReader(0);
            LOGGER.debug("admin already exists " + admin);
            admin = null;
            // Do nothing here
        } catch (DataSourceException e) {
            LOGGER.debug("insert admin");
            run.update("insert into " + READER + "(id, username, "
                    + " password, firstname, lastname, birthday, "
                    + "groupid, status) values (" + ADMIN_ID + ", '" + ADMIN
                    + "', '21232f297a57a5a743894a0e4a801fc3', '" + ADMIN
                    + "','" + ADMIN + "','"
                    + new java.sql.Date(System.currentTimeMillis())
                    + "', 'ADMIN', 'active')");
        }
    }

    /**
     * Erstellt den Administrator-Account, vorrausgesetzt dieser existiert nicht
     * bereits in der Datenbank.
     *
     * @throws SQLException
     *             Falls Probleme mit SQL auftreten.
     */
    private void insertDummyUser() throws SQLException {
        LOGGER.debug("inserting dummy");
        try {
            Reader dummy = getReader(-1);
            LOGGER.debug("dummy already exists " + dummy);
            dummy = null;
            // Do nothing here
        } catch (DataSourceException e) {
            LOGGER.debug("insert admin");
            run.update("insert into " + READER + "(id, username, "
                    + " password, firstname, lastname, birthday, "
                    + "groupid, status) values (-1, 'Anonym', '-1', "
                    + "'Anonym', 'Anonym', '"
                    + new java.sql.Date(System.currentTimeMillis())
                    + "', 'USER', 'active')");
        }
    }

    /**
     * Fügt ein Element in eine Tabelle ein. Falls dieses Element keine ID
     * besitzt, so wird eine generiert.
     *
     * @param element
     *            Das hinzuzufügende Element.
     * @param table
     *            Die Tabelle, in welche das Element eingefügt werden soll.
     * @param minID
     *            Die minimale ID, die ein Element besitzen darf.
     * @param toIgnore
     *            Attribute, die ignoriert werden sollen.
     * @param replace
     *            Werte, die ausgetauscht werden sollen.
     * @return Die generierte ID.
     * @throws SQLException
     *             Falls SQL-Fehler auftreten.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    private int insertByID(final BusinessObject element, final String table,
            final int minID, final Set<String> toIgnore,
            final HashMap<String, Object> replace) throws SQLException,
            DataSourceException {

        int id = getNewId(table, minID);

        element.setId(id);

        insert(table, element, toIgnore, replace);
        LOGGER.debug("element " + element + " added with given ID " + id);

        return element.getId();
    }

    /**
     * Erstellt die Wochentage für die Öffnungszeiten-Tabelle.
     *
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    private void insertOpeningTimes() throws DataSourceException {
        try {
            LOGGER.debug("inserting montag");
            run.update("insert into " + OPENINGTIME
                    + "(id, day) VALUES (0, 'Montag')");
            LOGGER.debug("inserting dienstag");
            run.update("insert into " + OPENINGTIME
                    + "(id, day) VALUES (1, 'Dienstag')");
            LOGGER.debug("inserting mittwoch");
            run.update("insert into " + OPENINGTIME
                    + "(id, day) VALUES (2, 'Mittwoch')");
            LOGGER.debug("inserting donnerstag");
            run.update("insert into " + OPENINGTIME
                    + "(id, day) VALUES (3, 'Donnerstag')");
            LOGGER.debug("inserting freitag");
            run.update("insert into " + OPENINGTIME
                    + "(id, day) VALUES (4, 'Freitag')");
            LOGGER.debug("inserting samstag");
            run.update("insert into " + OPENINGTIME
                    + "(id, day) VALUES (5, 'Samstag')");
            LOGGER.debug("inserting sonntag");
            run.update("insert into " + OPENINGTIME
                    + "(id, day) VALUES (6, 'Sonntag')");
        } catch (SQLException e) {
            LOGGER.error("create openingTimes error " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Erstellt die standard Medientypen.
     *
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    private void insertMediaTypes() throws DataSourceException {
        try {
            try {
                MediumType book = getMediumType(0);
                LOGGER.debug("mediumtype buch already exists " + book);
                book = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting buch");
                run.update("insert into "
                        + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (0, 'Buch', 2, 0.10, 1, 1, 'Autor(en)', "
                        + "'ISBN', 'Band', 'Seitenzahl', 'Verlag', 'Vorschau', "
                        + "NULL, NULL, NULL, NULL)");
            }
            try {
                MediumType magazine = getMediumType(1);
                LOGGER.debug("mediumtype zeitschrift already exists "
                        + magazine);
                magazine = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting zeitschrift");
                run.update("insert into " + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (1, 'Zeitschrift', 2, 0.10, 1, 1, "
                        + "'Editor(en)', "
                        + "'ISSN', 'Ausgabe', 'Seitenzahl', 'Verlag', "
                        + "'Vorschau', NULL, NULL, NULL, NULL)");
            }
            try {
                MediumType soft = getMediumType(2);
                LOGGER.debug("mediumtype software already exists " + soft);
                soft = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting software");
                run.update("insert into " + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (2, 'Software', 2, 0.10, 1, 1, 'Verlag', "
                        + "'Anzahl der Datenträger', NULL, NULL, NULL, NULL, "
                        + "NULL, NULL, NULL, NULL)");
            }
            try {
                MediumType audioBook = getMediumType(3);
                LOGGER.debug("mediumtype hörbuch already exists " + audioBook);
                audioBook = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting hörbuch");
                run.update("insert into " + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (3, 'Hörbuch', 2, 0.10, 1, 1, 'Verlag', "
                        + "'Anzahl der Datenträger', 'Spieldauer', NULL, "
                        + "NULL, NULL, NULL, NULL, NULL, NULL)");
            }
            try {
                MediumType music = getMediumType(4);
                LOGGER.debug("mediumtype music already exists " + music);
                music = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting musik");
                run.update("insert into " + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (4, 'Musik', 2, 0.10, 1, 1, 'Künstler', "
                        + "'Label', 'Spieldauer', 'Anzahl der Titel', "
                        + "'Anzahl der Datenträger', NULL, "
                        + "NULL, NULL, NULL, NULL)");
            }
            try {
                MediumType film = getMediumType(5);
                LOGGER.debug("mediumtype book already exists " + film);
                film = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting film");
                run.update("insert into " + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (5, 'Film', 2, 0.10, 1, 1, 'Regisseur(e)', "
                        + "'Filmvertreiber', 'Spieldauer', "
                        + "'Anzahl der Datenträger', "
                        + "'Altersfreigabe', NULL, NULL, NULL, NULL, NULL)");
            }
            try {
                MediumType cassette = getMediumType(6);
                LOGGER.debug("mediumtype cassette already exists " + cassette);
                cassette = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting cassette");
                run.update("insert into "
                        + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (6, 'Kassette', 2, 0.10, 1, 1, 'Spieldauer', "
                        + "'Verlag', NULL, NULL, NULL, NULL, "
                        + "NULL, NULL, NULL, NULL)");
            }
            try {
                MediumType other = getMediumType(7);
                LOGGER.debug("mediumtype sonstiges already exists " + other);
                other = null;
                // Do nothing here
            } catch (DataSourceException e) {
                LOGGER.debug("inserting sonstiges");
                run.update("insert into "
                        + MEDIUMTYPE
                        + "(id, name, lendingtime, fee, extensions, "
                        + "extensiontime, attribute0, attribute1, "
                        + "attribute2, attribute3, attribute4, attribute5, "
                        + "attribute6, attribute7, attribute8, attribute9) "
                        + "VALUES (7, 'Sonstiges', 2, 0.10, 1, 1, 'Attribut1', "
                        + "'Attribut2', 'Attribut3', 'Attribut4', 'Attribut5', "
                        + "'Attribut6', 'Attribut7', 'Attribut8', 'Attribut9', "
                        + "'Attribut10')");
            }
        } catch (SQLException e) {
            LOGGER.error("create openingTimes error " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Erstellt vorgegebene Eigenschaften, vorrausgesetzt diese existiert nicht
     * bereits in der Datenbank.
     *
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    private void insertProperties() throws DataSourceException {
        LOGGER.debug("inserting properties");
        try {
            try {
                Property p = getProperty(0);
                LOGGER.debug("property inactive time already exists " + p);
                p = null;
                // Do nothing here
            } catch (DataSourceException e) {
                run.update("insert into " + PROPERTY
                        + "(id, name, value) values (0,'inactiveTime', '12')");
            }
            try {
                MediumType p1 = getMediumType(1);
                LOGGER.debug("property tolerance already exists " + p1);
                p1 = null;
                // Do nothing here
            } catch (DataSourceException e) {
                run.update("insert into " + PROPERTY
                        + "(id, name, value) values (1,'tolerance', '2')");
            }
            try {
                MediumType p2 = getMediumType(2);
                LOGGER.debug("property delete time already exists " + p2);
                p2 = null;
                // Do nothing here
            } catch (DataSourceException e) {
                run.update("insert into " + PROPERTY
                        + "(id, name, value) values (2,'deleteTime', '8')");
            }
        } catch (SQLException e) {
            LOGGER.error("create properties error " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * Returns a new print stream for out.
     *
     * @param out
     *            output stream for which to create the print stream
     * @return new print stream or null if there is no UTF-8 or ISO-8859-1
     *         encoding.
     */
    protected final PrintStream newPrintStream(final OutputStream out) {
        try {
            return new PrintStream(out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should not occur; we will try ISO-8859-1 instead
            try {
                return new PrintStream(out, true, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                return null;
            }
        }
    }

    /**
     * Quotes every quote in string and surrounds the whole string by quotes.
     *
     * @param string
     *            input string
     * @return "string"
     */
    private String quote(final String string) {
        return DEFAULT_QUOTE
                + string.replace(DEFAULT_QUOTE, DEFAULT_QUOTE + DEFAULT_QUOTE)
                + DEFAULT_QUOTE;
    }

    /**
     * True if <code>field</code> is relevant for update or insert: neither a
     * static field nor a field to be ignored.
     *
     * @param toIgnore
     *            field names that should not be stored (may be null)
     * @param field
     *            Das zu prüfende Feld.
     * @return if <code>field</code> is relevant
     */
    private boolean relevantField(final Set<String> toIgnore, final Field field) {
        return !Modifier.isStatic(field.getModifiers())
                && !shouldBeIgnored(toIgnore, field.getName());
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#reset()
     */
    @Override
    public final void reset() throws DataSourceException {
        LOGGER.debug("drop all tables in database");
        reset(true);
    }

    /**
     * Setzt die Datenbank auf ihren ursprünglichen Zustand zurück.
     * <code>createAdmin</code> legt fest, ob der Standard-Administrator
     * erstellt werden soll.
     *
     * @param createAdmin
     *            Standard-Administrator anlegen?
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    private void reset(final boolean createAdmin) throws DataSourceException {
        try {
            List<String> tableNames = getTableNames();

            while (tableNames.size() != 0) {
                String tmp = tableNames.get(0);
                try {
                    LOGGER.debug("drop table " + tmp);
                    run.update("DROP TABLE " + tmp);
                    tableNames.remove(tmp);
                } catch (SQLException e) {
                    tableNames.remove(tmp);
                    tableNames.add(tmp);
                }
            }
            checkDatabaseStructure(createAdmin);
        } catch (SQLException e) {
            LOGGER.error("drop table error " + e.getLocalizedMessage());
            throw new DataSourceException("reset failed with: "
                    + e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#restore(java.lang.String)
     */
    @Override
    public final void restore(final String path) throws DataSourceException {
        LOGGER.debug("preparing restore");

        // Backup
        File backup = new File(path);

        if (!backup.isDirectory()) {
            LOGGER.error(path + " is no directory");
            throw new DataSourceException(path + " ist kein Verzeichnis.");
        }

        try {
            Connection conn = dataSource.getConnection();
            try {
                conn.setAutoCommit(true);
                Statement s = conn.createStatement();

                try {
                    LOGGER.debug("resetting db");
                    reset(false);

                    // logger.debug("freezing db");
                    // s.executeUpdate("CALL SYSCS_UTIL.SYSCS_FREEZE_DATABASE()");

                    LOGGER.debug("restoring db");

                    // READER
                    LOGGER.debug("restoring table " + READER);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + READER + "', '" + path + READER
                            + ".backup', ';', null, null, 0)");
                    // CATEGORY
                    LOGGER.debug("restoring table " + CATEGORY);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + CATEGORY + "', '" + path + CATEGORY
                            + ".backup', ';', null, null, 0)");
                    // PROPERTY
                    LOGGER.debug("restoring table " + PROPERTY);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + PROPERTY + "', '" + path + PROPERTY
                            + ".backup', ';', null, null, 0)");
                    // NEWS
                    LOGGER.debug("restoring table " + NEWS);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + NEWS + "', '" + path + NEWS
                            + ".backup', ';', null, null, 0)");
                    // OPENINGTIME
                    LOGGER.debug("restoring table " + OPENINGTIME);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + OPENINGTIME + "', '" + path + OPENINGTIME
                            + ".backup', ';', null, null, 0)");
                    // MEDIUMTYPE
                    LOGGER.debug("restoring table " + MEDIUMTYPE);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + MEDIUMTYPE + "', '" + path + MEDIUMTYPE
                            + ".backup', ';', null, null, 0)");
                    // MEDIUM
                    LOGGER.debug("restoring table " + MEDIUM);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + MEDIUM + "', '" + path + MEDIUM
                            + ".backup', ';', null, null, 0)");
                    // CLOSEDTIME
                    LOGGER.debug("restoring table " + CLOSEDTIME);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + CLOSEDTIME + "', '" + path + CLOSEDTIME
                            + ".backup', ';', null, null, 0)");
                    // RATING
                    LOGGER.debug("restoring table " + RATING);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + RATING + "', '" + path + RATING
                            + ".backup', ';', null, null, 0)");
                    // EXEMPLAR
                    LOGGER.debug("restoring table " + EXEMPLAR);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + EXEMPLAR + "', '" + path + EXEMPLAR
                            + ".backup', ';', null, null, 0)");
                    // LENDING
                    LOGGER.debug("restoring table " + LENDING);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + LENDING + "', '" + path + LENDING
                            + ".backup', ';', null, null, 0)");
                    // COMMENTARY
                    LOGGER.debug("restoring table " + COMMENTARY);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + COMMENTARY + "', '" + path + COMMENTARY
                            + ".backup', ';', null, null, 0)");
                    // RESERVATION
                    LOGGER.debug("restoring table " + RESERVATION);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + RESERVATION + "', '" + path + RESERVATION
                            + ".backup', ';', null, null, 0)");
                    // EXTENSION
                    LOGGER.debug("restoring table " + EXTENSION);
                    s.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
                            + EXTENSION + "', '" + path + EXTENSION
                            + ".backup', ';', null, null, 0)");
                } catch (SQLException e) {
                    LOGGER.error("import problem " + e.getLocalizedMessage());
                    throw new DataSourceException(e.getLocalizedMessage());
                } finally {
                    // logger.debug("unfreezing db");
                    // s.executeUpdate("CALL SYSCS_UTIL.SYSCS_UNFREEZE_DATABASE()");
                    s.close();
                }
            } catch (SQLException e) {
                LOGGER.error("statement problem " + e.getLocalizedMessage());
                throw new DataSourceException(e.getLocalizedMessage());
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error("connection problem " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /**
     * True if <code>field</code> should be ignored.
     *
     * @param toIgnore
     *            field names that should not be stored (may be null)
     * @param field
     *            field examined
     * @return if <code>field</code> should be ignored
     */
    private boolean shouldBeIgnored(final Set<String> toIgnore,
            final String field) {
        return toIgnore != null && toIgnore.contains(field);
    }

    /**
     * Führt eine einfache SQL-Abfrage aus, die höchstens ein Ergebnis liefert.
     *
     * @param query
     *            Die SQL-Abfrage.
     * @return Das Ergebnis der SQL-Abfrage
     * @throws SQLException
     *             Falls SQL-Fehler auftreten.
     */
    private long singleResultQuery(final String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            Statement stmt = connection.createStatement();
            try {
                ResultSet rs = stmt.executeQuery(query);
                try {
                    // go to first row
                    if (rs.next()) {
                        return rs.getLong(1);
                    } else {
                        return 0;
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stmt.close();
            }
        } finally {
            connection.close();
        }
    }

    /**
     * Überprüft ob die Tabelle in der Liste vorhanden ist.
     *
     * @param tableNames
     *            Eine Liste mit Tabellennamen.
     * @param tableName
     *            Der Name der zu überprüfenden Tabelle.
     * @return <code>true</code> falls die Tabelle vorhanden ist,
     *         <code>false</code> andernfalls.
     */
    private boolean tableExists(final List<String> tableNames,
            final String tableName) {
        return tableNames.contains(tableName.toLowerCase());
    }

    /**
     * Wandelt ein Datum in das SQL Date-Format.
     *
     * @param date
     *            Das zu wandelnde Datum.
     * @return Das Datum im SQL format.
     */
    private String toDateFormat(final Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate.toString();
    }

    /**
     * Returns an ORDER BY clause for an sql select statement for the given
     * <code>order</code>.
     *
     * Note: The fields in <code>order</code> are data that are derived from
     * field names in the facelets and, hence, cannot be influenced by the user,
     * can it? For this reason, they need to be sanitized for potential SQL
     * injections, do they?
     *
     * @param order
     *            the order specification
     * @return ORDER BY clause if <code>order</code> specifies an order or empty
     *         string if <code>order</code> is null or empty.
     *
     */
    private String toOrderByClause(final List<OrderBy> order) {
        if (order == null || order.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" ORDER BY ");
        for (OrderBy o : order) {
            sb.append(o.getAttribute());
            if (o.isAscending()) {
                sb.append(" ASC,");
            } else {
                sb.append(" DESC,");
            }
        }
        // remove last comma
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Erstellt die WHERE-Klausel anhand der übergebenen Bedingungen.
     *
     * @param constraints
     *            Bedingungen, die in SQL-Syntax gewandelt werden sollen.
     * @return Die Where-Klausel
     * @see Constraint
     */
    private String toQuery(final List<Constraint> constraints) {
        if (constraints == null || constraints.size() == 0) {
            return "";
        } else {
            // remove last logicalOperator
            if (!constraints.get(constraints.size() - 1).getLogicalOperator()
                    .equals(")")) {
                constraints.get(constraints.size() - 1).setLogicalOperator("");
            }

            StringBuilder sb = new StringBuilder(" WHERE ");
            for (Constraint constraint : constraints) {
                sb.append(constraint.getAttribute() + " "
                        + constraint.getOperator() + " ? "
                        + constraint.getLogicalOperator() + " ");
            }
            return sb.toString();
        }
    }

    /**
     * Gibt die Werte als Strings zurück.
     *
     * @param values
     *            Die Werte.
     * @return Die Werte als String.
     */
    private Object toString(final Object[] values) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                result.append("<NULL>");
            } else {
                result.append(values[i].toString());
            }
            result.append(" | ");
        }
        return result.toString();
    }

    /**
     * Updates the row in <code>table</code> identified by <code>key</code> by
     * the values given in <code>object</code> excluding those listed in
     * <code>toIgnore</code>. The row is identified by looking up all rows whose
     * value of column <code>key</code> equals <code>keyValue</code>.
     *
     * If a field of <code>object</code> is found in <code>replace</code>, the
     * corresponding replacement value in <code>replace</code> is used instead
     * of the actual value of the field. Otherwise the field's value is stored.
     *
     * @param table
     *            name of the table in which <code>object</code> is inserted
     * @param object
     *            the object to be stored
     * @param key
     *            key to identify the row to be updated
     * @param keyValue
     *            required value for the key
     * @param toIgnore
     *            field names that should not be stored
     * @param replace
     *            values to be replaced for storing
     * @return the number of inserted rows (0 or 1)
     * @throws SQLException
     *             thrown in case the object cannot be inserted
     */
    private int update(final String table, final Object object,
            final String key, final Object keyValue,
            final Set<String> toIgnore, final HashMap<String, Object> replace)
            throws SQLException {
        LOGGER.debug("update table " + table);
        StringBuilder stmt = new StringBuilder("UPDATE " + table + " SET ");
        int numberOfFields = 0;

        // append stmt by "field1 = ?, field2 = ?, ..., fieldN = ?,"
        // where field<i> is a non-static field not to be ignored
        // count the number of such fields in numberOfFields
        HashMap<String, Field> fieldsOfObject = Reflection.getTransitiveFields(
                new HashMap<String, Field>(), object.getClass());
        for (Field f : fieldsOfObject.values()) {
            if (relevantField(toIgnore, f)) {
                stmt.append(f.getName());
                stmt.append(" = ?,");
                numberOfFields++;
            }
        }

        // fields =
        // "UPDATE <table> SET field1 = ?, field2 = ?, ..., fieldN = ?,"
        if (numberOfFields > 0) {
            // remove last comma
            stmt.deleteCharAt(stmt.length() - 1);

            // append where clause
            stmt.append(" WHERE " + key + " = ?");

            try {
                // create the list of values to be filled in;
                // the length of this argument list is numberOfFields + 1

                // other arguments
                Object[] collectedValues = getValues(object, toIgnore, replace,
                        numberOfFields);

                // add key value at the end
                Object[] values = new Object[collectedValues.length + 1];
                for (int i = 0; i < collectedValues.length; i++) {
                    values[i] = collectedValues[i];
                }
                values[collectedValues.length] = keyValue;
                LOGGER.debug(stmt.toString());
                LOGGER.debug(toString(values));
                // finally run query
                return run.update(stmt.toString(), values);
            } catch (IllegalArgumentException e) {
                // internal error message, hence, we are not using
                // Messages.get(...)
                throw new SQLException(key + " is an illegal argument: "
                        + e.getMessage());
            } catch (SecurityException e) {
                // internal error message, hence, we are not using
                // Messages.get(...)
                throw new SQLException("cannot access field " + key + ": "
                        + e.getMessage());
            }
        } else {
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateCategory(swp.bibcommon.Category)
     */
    @Override
    public final int updateCategory(final Category newValue)
            throws DataSourceException {
        LOGGER.error("updating category " + newValue);

        if (newValue == null) {
            LOGGER.error("category must not be null");
            throw new IllegalArgumentException("category must not be null");
        }

        try {
            return update(CATEGORY, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("failure in updating category"
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateClosedTime(swp.bibcommon.ClosedTime
     * )
     */
    @Override
    public final int updateClosedTime(final ClosedTime newValue)
            throws DataSourceException {
        LOGGER.error("updating news " + newValue);

        if (newValue == null) {
            LOGGER.error("closedtime must not be null");
            throw new IllegalArgumentException("closedtime must not be null");
        }

        try {
            return update(CLOSEDTIME, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("failure in updating closedtime "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateCommentary(swp.bibcommon.Commentary
     * )
     */
    @Override
    public final int updateCommentary(final Commentary newValue)
            throws DataSourceException {
        LOGGER.debug("update commentary " + newValue);

        if (newValue == null) {
            LOGGER.error("commentary must not be null");
            throw new IllegalArgumentException("commentary must not be null");
        }

        try {
            return update(COMMENTARY, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("update commentary failure: "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateExemplar(swp.bibcommon.Exemplar)
     */
    @Override
    public final int updateExemplar(final Exemplar newValue)
            throws DataSourceException {
        LOGGER.debug("update exemplar " + newValue);

        if (newValue == null) {
            LOGGER.error("exemplar must not be null");
            throw new IllegalArgumentException("exemplar must not be null");
        }

        try {
            return update(EXEMPLAR, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("update exemplar failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateExtension(swp.bibcommon.Extension
     * )
     */
    @Override
    public final int updateExtension(final Extension newValue)
            throws DataSourceException {
        LOGGER.debug("update extension " + newValue);

        if (newValue == null) {
            LOGGER.error("extension must not be null");
            throw new IllegalArgumentException("extension must not be null");
        }

        try {
            return update(EXTENSION, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("failure in updating extension "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateLending(swp.bibcommon.Lending)
     */
    @Override
    public final int updateLending(final Lending newValue)
            throws DataSourceException {
        LOGGER.debug("update lending " + newValue);

        if (newValue == null) {
            LOGGER.error("lending must not be null");
            throw new IllegalArgumentException("lending must not be null");
        }

        try {
            return update(LENDING, newValue, "id", newValue.getId(), null, null);
        } catch (SQLException e) {
            LOGGER.error("update lending failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateMedium(swp.bibcommon.Medium)
     */
    @Override
    public final int updateMedium(final Medium newValue)
            throws DataSourceException {
        LOGGER.debug("update medium " + newValue);

        if (newValue == null) {
            LOGGER.error("medium must not be null");
            throw new IllegalArgumentException("medium must not be null");
        }

        try {
            Set<String> toIgnore = new HashSet<String>();
            toIgnore.add("rating");
            toIgnore.add("ratingCount");
            return update(MEDIUM, newValue, "id", newValue.getId(), toIgnore,
                    null);
        } catch (SQLException e) {
            LOGGER.error("update medium failures " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateMediumType(swp.bibcommon.MediumType
     * )
     */
    @Override
    public final int updateMediumType(final MediumType newValue)
            throws DataSourceException {
        LOGGER.debug("update mediumType " + newValue);

        if (newValue == null) {
            LOGGER.error("mediumtype must not be null");
            throw new IllegalArgumentException("madiumtype must not be null");
        }

        try {
            return update(MEDIUMTYPE, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("update mediumtype failure " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#updateNews(swp.bibcommon.News)
     */
    @Override
    public final int updateNews(final News newValue) throws DataSourceException {
        LOGGER.error("updating news " + newValue);

        if (newValue == null) {
            LOGGER.error("news must not be null");
            throw new IllegalArgumentException("news must not be null");
        }

        try {
            return update(NEWS, newValue, "id", newValue.getId(), null, null);
        } catch (SQLException e) {
            LOGGER.error("failure in updating news " + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#updateOpeningTime(swp.bibcommon.
     * OpeningTime)
     */
    @Override
    public final int updateOpeningTime(final OpeningTime newValue)
            throws DataSourceException {
        LOGGER.debug("update openingtime " + newValue);

        if (newValue == null) {
            LOGGER.error("openingtime must not be null");
            throw new IllegalArgumentException("openingtime must not be null");
        }

        try {
            return update(OPENINGTIME, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("update openingtime failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateProperty(swp.bibcommon.Property)
     */
    @Override
    public final int updateProperty(final Property newValue)
            throws DataSourceException {
        LOGGER.error("updating property " + newValue);

        if (newValue == null) {
            LOGGER.error("property must not be null");
            throw new IllegalArgumentException("property must not be null");
        }

        try {
            return update(PROPERTY, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("failure in updating property "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateRating(swp.bibcommon.Rating)
     */
    @Override
    public final int updateRating(final Rating newValue)
            throws DataSourceException {
        LOGGER.error("updating rating " + newValue);

        if (newValue == null) {
            LOGGER.error("rating must not be null");
            throw new IllegalArgumentException("rating must not be null");
        }

        try {
            return update(RATING, newValue, "id", newValue.getId(), null, null);
        } catch (SQLException e) {
            LOGGER.error("failure in updating rating "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * swp.bibjsf.persistence.Persistence#updateReader(swp.bibcommon.Reader,
     * swp.bibcommon.Reader)
     */
    @Override
    public final int updateReader(final Reader newValue)
            throws DataSourceException {
        LOGGER.debug("update reader " + newValue);

        if (newValue == null) {
            LOGGER.error("reader must not be null");
            throw new IllegalArgumentException("reader must not be null");
        }

        try {
            return update(READER, newValue, "id", newValue.getId(), null, null);
        } catch (SQLException e) {
            LOGGER.error("failure in updating reader "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see swp.bibjsf.persistence.Persistence#updateReservation(swp.bibcommon.
     * Reservation)
     */
    @Override
    public final int updateReservation(final Reservation newValue)
            throws DataSourceException {
        LOGGER.debug("update reservation " + newValue);

        if (newValue == null) {
            LOGGER.error("reservation must not be null");
            throw new IllegalArgumentException("reservation must not be null");
        }

        try {
            return update(RESERVATION, newValue, "id", newValue.getId(), null,
                    null);
        } catch (SQLException e) {
            LOGGER.error("update reservation failure "
                    + e.getLocalizedMessage());
            throw new DataSourceException(e.getLocalizedMessage());
        }
    }
}
