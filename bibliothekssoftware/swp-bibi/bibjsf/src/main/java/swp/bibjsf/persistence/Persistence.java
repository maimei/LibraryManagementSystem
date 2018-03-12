package swp.bibjsf.persistence;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.OrderBy;

/**
 * Dieses Interface ist die Schnittstelle der Persistence-Komponente. Es handelt
 * sich hierbei um eine Weiterentwicklung der von Karsten Hölscher erstellten
 * Basis-Schnittstelle des ursprünglichen Prototypen.
 *
 * @author Eike Externest
 */
public interface Persistence {

    /**
     * Fügt der Datenbank eine neue Kategorie hinzu. Falls die Kategorie schon
     * vorhanden ist, wird eine Exception geworfen.
     *
     * @param category
     *            Die einzufügende Kategorie.
     * @return Die generierte ID des Mediums.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addCategory(final Category category) throws DataSourceException;

    /**
     * Fügt der Datenbank eine neue Schließunszeit hinzu. Falls die
     * Schließungszeit schon vorhanden ist, wird eine Exception geworfen.
     *
     * @param closedTime
     *            Die einzufügende Schließungszeit.
     * @return Die generierte ID der Schließungszeit.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addClosedTime(final ClosedTime closedTime) throws DataSourceException;

    /**
     * Fügt der Datenbank ein neues Kommentar hinzu. Falls der Kommentar schon
     * vorhanden ist wird eine Exception geworfen.
     *
     * @param commentary
     *            Der einzufügende Kommentar.
     * @return Die generierte ID des Kommentars.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addCommentary(final Commentary commentary) throws DataSourceException;

    /**
     * Fügt der Datenbank ein neues Exemplar hinzu. Falls das Exemplar schon
     * vorhanden ist wird eine Exception geworfen.
     *
     * @param exemplar
     *            Das einzufügende Exemplar.
     * @return Die generierte ID des Exemplars.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addExemplar(final Exemplar exemplar) throws DataSourceException;

    /**
     * Fügt der Datenbank eine neue Verlängerung hinzu. Falls die Verlängerung
     * schon vorhanden ist, wird eine Exception geworfen.
     *
     * @param extension
     *            Das einzufügende Medium.
     * @return Die generierte ID des Mediums.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addExtension(final Extension extension) throws DataSourceException;

    /**
     * Fügt der Datenbank eine neue Ausleihe hinzu. Falls die Ausleihe schon
     * vorhanden ist wird eine Exception geworfen.
     *
     * @param lending
     *            Die einzufügende Ausleihe.
     * @return Die generierte ID der Ausleihe.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addLending(final Lending lending) throws DataSourceException;

    /**
     * Fügt der Datenbank ein neues Medium hinzu. Falls das Medium schon
     * vorhanden ist, wird eine Exception geworfen.
     *
     * @param medium
     *            Das einzufügende Medium.
     * @return Die generierte ID des Mediums.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addMedium(final Medium medium) throws DataSourceException;

    /**
     * Fügt der Datenbank einen neuen Medientyp hinzu. Falls der Medientyp schon
     * vorhanden ist, wird eine Exception geworfen.
     *
     * @param mediumType
     *            Der einzufügende Medientyp.
     * @return Die generierte ID des Medientyps.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addMediumType(final MediumType mediumType) throws DataSourceException;

    /**
     * Fügt der Datenbank eine neue Neuigkeit hinzu. Falls die Neuigkeit schon
     * vorhanden ist wird eine Exception geworfen.
     *
     * @param news
     *            Die einzufügende Neuigkeit.
     * @return Die generierte ID der Neuigkeit.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addNews(final News news) throws DataSourceException;

    /**
     * Fügt der Datenbank eine neue Bewertung hinzu. Falls die Bewertung schon
     * vorhanden ist, wird eine Exception geworfen.
     *
     * @param rating
     *            Die einzufügende Bewertung.
     * @return Die generierte ID der Bewertung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addRating(final Rating rating) throws DataSourceException;

    /**
     * Fügt der Datenbank einen neuen Benutzer hinzu. Falls der Benutzer schon
     * vorhanden ist, wird eine Exception geworfen.
     *
     * @param reader
     *            Der einzufügende Benutzer.
     * @return Die generierte ID des Benutzers.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addReader(final Reader reader) throws DataSourceException;

    /**
     * Fügt der Datenbank eine neue Vormerkung hinzu. Falls die Vormerkung schon
     * vorhanden ist wird eine Exception geworfen.
     *
     * @param reservation
     *            Die einzufügende Vormerkung.
     * @return Die generierte ID der Vormerkung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int addReservation(final Reservation reservation)
            throws DataSourceException;

    /**
     * Erstellt eine Datenbankbackup. Das Backup wird von {@link #restore()
     * restore()} verwendet.
     *
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void backup() throws DataSourceException;

    /**
     * Löscht die übergebene Kategorie aus der Datenbank.
     *
     * @param category
     *            Die zu löschende Kategorie.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteCategory(final Category category) throws DataSourceException;

    /**
     * Löscht die übergebenen Schließungszeit aus der Datenbank.
     *
     * @param closedTime
     *            Die zu löschende Schließungszeit.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteClosedTime(final ClosedTime closedTime)
            throws DataSourceException;

    /**
     * Löscht den übergebenen Kommentar aus der Datenbank.
     *
     * @param commentary
     *            Der zu löschende Kommentar.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteCommentary(Commentary commentary) throws DataSourceException;

    /**
     * Löscht das übergebene Exemplar aus der Datenbank.
     *
     * @param exemplar
     *            Das zu löschende Exemplar.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteExemplar(final Exemplar exemplar) throws DataSourceException;

    /**
     * Löscht die übergebene Verlängerung aus der Datenbank.
     *
     * @param extension
     *            Die zu löschende Verlängerung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteExtension(final Extension extension) throws DataSourceException;

    /**
     * Löscht die übergebene Ausleihe aus der Datenbank.
     *
     * @param lending
     *            Die zu löschende Ausleihe.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteLending(final Lending lending) throws DataSourceException;

    /**
     * Löscht das übergebene Medium aus der Datenbank.
     *
     * @param medium
     *            Das zu löschende Medium.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteMedium(final Medium medium) throws DataSourceException;

    /**
     * Löscht den übergebenen Medientyp aus der Datenbank.
     *
     * @param mediumType
     *            Der zu löschende Medientyp.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteMediumType(final MediumType mediumType)
            throws DataSourceException;

    /**
     * Löscht die übergebene Neuigkeit aus der Datenbank.
     *
     * @param news
     *            Die zu löschende Neuigkeit.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteNews(final News news) throws DataSourceException;

    /**
     * Löscht die übergebene Bewertung aus der Datenbank.
     *
     * @param rating
     *            Die zu löschende Bewertung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteRating(final Rating rating) throws DataSourceException;

    /**
     * Löscht den übergebene Benutzer aus der Datenbank.
     *
     * @param reader
     *            Der zu löschende Benutzer.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteReader(final Reader reader) throws DataSourceException;

    /**
     * Löscht die übergebene Vormerkung aus der Datenbank.
     *
     * @param reservation
     *            Die zu löschende Vormerkung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void deleteReservation(final Reservation reservation)
            throws DataSourceException;

    /**
     * Exportiert alle in der Datenbank gespeicherten Medien in eine CSV-Datei.
     * Die CSV-Datei kann mit der Methode {@link #importMediums(InputStream)
     * importMediums()} aufgespielt werden.
     *
     * @param output
     *            Der ausgehende Datenstrom.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void exportMediums(final OutputStream output) throws DataSourceException;

    /**
     * Exportiert alle in der Datenbank gespeicherten Benutzer in eine
     * CSV-Datei. Die CSV-Datei kann mit der Methode
     * {@link #importReaders(InputStream) importReaders()} aufgespielt werden.
     *
     * @param output
     *            Der ausgehende Datenstrom.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void exportReaders(final OutputStream output) throws DataSourceException;

    /**
     * Gibt eine Liste aller in der Datenbank gespeicherten Medien zurück.
     *
     * @return Die Liste aller Öffnungszeiten.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<OpeningTime> getAllOpeningTimes() throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Kategorien im Bereich <code> from </code>
     * bis <code> to </code> zurück, welche die Bedingungen erfüllen. Die
     * Bereichsparameter erlauben das seitenweise Laden von Abfragen. Dabei ist
     * zu beachten, dass die erste Kategorie den Index 0 besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Kategorie aus der Liste.
     * @param to
     *            Der Index der letzten Kategorie aus der Liste.
     * @return Eine geordnete Liste aller Kategorien, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Category> getCategories(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt die Kategorie mit der entsprechenden ID zurück. Falls keine
     * Kategorie mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID der gesuchten Kategorie.
     * @return Die Kategorie mit der entsprechenden ID.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Category getCategory(final int id) throws DataSourceException;

    /**
     * Gibt die Schließungszeit mit der entsprechenden ID zurück. Falls keine
     * Schließungszeit mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID der gesuchten Schließungszeit.
     * @return Die Schließungszeit.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    ClosedTime getClosedTime(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Schließungszeiten im Bereich
     * <code> from </code> bis <code> to </code> zurück, welche die Bedingungen
     * erfüllen. Die Bereichsparameter erlauben das seitenweise Laden von
     * Abfragen. Dabei ist zu beachten, dass das erste Medium den Index 0
     * besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Schließungszeit aus der Liste.
     * @param to
     *            Der Index der letzten Schließungszeit aus der Liste.
     * @return Eine geordnete Liste aller Schließungszeiten, welche die
     *         Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<ClosedTime> getClosedTimes(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Kommentare im Bereich <code> from </code>
     * bis <code> to </code> zurück, welche die Bedingungen erfüllen. Die
     * Bereichsparameter erlauben das seitenweise Laden von Abfragen. Dabei ist
     * zu beachten, dass den ersten Kommentar den Index 0 besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index des ersten Kommentars aus der Liste.
     * @param to
     *            Der Index des letzten Kommentars aus der Liste.
     * @return Eine geordnete Liste aller Kommentare, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Commentary> getCommentaries(List<Constraint> constraints,
            final int from, final int to, List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt den Kommentar mit der entsprechenden ID zurück. Falls kein Kommentar
     * mit entsprechender ID vorhanden ist, wird eine Exception geworfen.
     *
     * @param id
     *            Die ID des gesuchten Kommentars.
     * @return Der Kommentar.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Commentary getCommentary(final int id) throws DataSourceException;

    /**
     * Gibt das Exemplar mit der entsprechenden ID zurück. Falls kein Exemplar
     * mit entsprechender ID vorhanden ist, wird eine Exception geworfen.
     *
     * @param id
     *            Die ID des gesuchten Exemplars.
     * @return Das Exemplar.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Exemplar getExemplar(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Exemplare im Bereich <code> from </code>
     * bis <code> to </code> zurück, welche die Bedingungen erfüllen. Die
     * Bereichsparameter erlauben das seitenweise Laden von Abfragen. Dabei ist
     * zu beachten, dass das erste Exemplar den Index 0 besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index des ersten Exemplars aus der Liste.
     * @param to
     *            Der Index des letzten Exemplars aus der Liste.
     * @return Eine geordnete Liste aller Exemplare, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Exemplar> getExemplars(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt die Verlängerung mit der entsprechenden ID zurück. Falls keine
     * Verlängerung mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID der gesuchten Verlängerung.
     * @return Die Verlängerung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Extension getExtension(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Verlängerungen im Bereich
     * <code> from </code> bis <code> to </code> zurück, welche die Bedingungen
     * erfüllen. Die Bereichsparameter erlauben das seitenweise Laden von
     * Abfragen. Dabei ist zu beachten, dass die erste Verlängerung den Index 0
     * besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Verlängerung aus der Liste.
     * @param to
     *            Der Index der letzten Verlängerung aus der Liste.
     * @return Eine geordnete Liste aller Verlängerungen, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Extension> getExtensions(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt die Ausleihe mit der entsprechenden ID zurück. Falls keine Ausleihe
     * mit entsprechender ID vorhanden ist, wird eine Exception geworfen.
     *
     * @param id
     *            Die ID der gesuchten Ausleihe.
     * @return Die Ausleihe.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Lending getLending(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Ausleihen im Bereich <code> from </code>
     * bis <code> to </code> zurück, welche die Bedingungen erfüllen. Die
     * Bereichsparameter erlauben das seitenweise Laden von Abfragen. Dabei ist
     * zu beachten, dass das erste Ausleihe den Index 0 besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Ausleihe aus der Liste.
     * @param to
     *            Der Index der letzten Ausleihe aus der Liste.
     * @return Eine geordnete Liste aller Ausleihen, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Lending> getLendings(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt die beliebtesten oder unbeliebtesten Medien zurück.
     * <code>order</code> gibt an, ob die Liste Aufsteigend oder Absteigend
     * sortiert werden soll.
     *
     * @param max
     *            Die maximale Anzahl Datenbankeinträge.
     * @param order
     *            <code>true</code> für absteigend, <code>false</code> für
     *            aufsteigend.
     * @return Die geordnete Liste der Medien.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Medium> getLentMedia(final int max, final boolean order)
            throws DataSourceException;

    /**
     * Gibt das Medium mit der entsprechenden ID zurück. Falls kein Medium mit
     * entsprechender ID vorhanden ist, wird eine Exception geworfen.
     *
     * @param id
     *            Die ID des gesuchten Mediums.
     * @return Das Medium.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Medium getMedium(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Medien im Bereich <code> from </code> bis
     * <code> to </code> zurück, welche die Bedingungen erfüllen. Die
     * Bereichsparameter erlauben das seitenweise Laden von Abfragen. Dabei ist
     * zu beachten, dass das erste Medium den Index 0 besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index des ersten Mediums aus der Liste.
     * @param to
     *            Der Index des letzten Mediums aus der Liste.
     * @return Eine geordnete Liste aller Medien, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Medium> getMediums(final List<Constraint> constraints, final int from,
            final int to, final List<OrderBy> order) throws DataSourceException;

    /**
     * Gibt den Medientyp mit der entsprechenden ID zurück. Falls keine
     * Medientyp mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID des gesuchten Medientyps.
     * @return Der Medientyp.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    MediumType getMediumType(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Medientypen im Bereich
     * <code> from </code> bis <code> to </code> zurück, welche die Bedingungen
     * erfüllen. Die Bereichsparameter erlauben das seitenweise Laden von
     * Abfragen. Dabei ist zu beachten, dass das erste Medium den Index 0
     * besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index des ersten Medientyps aus der Liste.
     * @param to
     *            Der Index des letzten Medientyps aus der Liste.
     * @return Eine geordnete Liste aller Medientypen, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<MediumType> getMediumTypes(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt die Neuigkeit mit der entsprechenden ID zurück. Falls keine
     * Neuigkeit mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID der gesuchten Neuigkeit.
     * @return Die Neuigkeit.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    News getNews(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Neuigkeiten im Bereich
     * <code> from </code> bis <code> to </code> zurück, welche die Bedingungen
     * erfüllen. Die Bereichsparameter erlauben das seitenweise Laden von
     * Abfragen. Dabei ist zu beachten, dass die erste Neuigkeit den Index 0
     * besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Neuigkeit aus der Liste.
     * @param to
     *            Der Index des letzten Neuigkeit aus der Liste.
     * @return Eine geordnete Liste aller Neuigkeiten, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<News> getNews(List<Constraint> constraints, final int from,
            final int to, List<OrderBy> order) throws DataSourceException;

    /**
     * Gibt die Anzahl der Kategorien zurück, welche die übergebenen Bedingungen
     * erfüllen. Falls eine Leere Liste übergeben wird, so wird die Anzahl aller
     * Kategorien zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Kategorien, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfCategories(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Schließungszeiten zurück, welche die übergebenen
     * Bedingungen erfüllen. Falls eine Leere Liste übergeben wird, so wird die
     * Anzahl aller Schließungszeiten zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Schließungszeiten, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfClosedTimes(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Kommentare zurück, welche die übergebenen Bedingungen
     * erfüllen. Falls eine Leere Liste übergeben wird, so wird die Anzahl aller
     * Kommentare zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Kommentare, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfCommentaries(List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Exemplare zurück, welche die übergebenen Bedingungen
     * erfüllen. Falls eine Leere Liste übergeben wird, so wird die Anzahl aller
     * Exemplare zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Exemplare, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfExemplars(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Verlängerungen zurück, welche die übergebenen
     * Bedingungen erfüllen. Falls eine Leere Liste übergeben wird, so wird die
     * Anzahl aller Verlängerungen zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Verlängerungen, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfExtensions(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Ausleihen zurück, welche die übergebenen Bedingungen
     * erfüllen. Falls eine Leere Liste übergeben wird, so wird die Anzahl aller
     * Ausleihen zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Ausleihen, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfLendings(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Medien zurück, welche die übergebenen Bedingungen
     * erfüllen. Falls eine Leere Liste übergeben wird, so wird die Anzahl aller
     * Medien zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Medien, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfMediums(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der MedienTypen zurück, welche die übergebenen
     * Bedingungen erfüllen. Falls eine Leere Liste übergeben wird, so wird die
     * Anzahl aller MedienTypen zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Medientypen, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfMediumType(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Neuigkeiten zurück, welche die übergebenen
     * Bedingungen erfüllen. Falls eine Leere Liste übergeben wird, so wird die
     * Anzahl aller Neuigkeiten zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Neuigkeiten, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfNews(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Eigenschaften zurück, welche die übergebenen
     * Bedingungen erfüllen. Falls eine Leere Liste übergeben wird, so wird die
     * Anzahl aller Eigenschaften zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Eigenschaften, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfProperties(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Bewertungen zurück, welche die übergebenen
     * Bedingungen erfüllen. Falls eine Leere Liste übergeben wird, so wird die
     * Anzahl aller Bewertungen zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Bewertungen, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfRatings(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Benutzer zurück, welche die übergebenen Bedingungen
     * erfüllen. Falls eine Leere Liste übergeben wird, so wird die Anzahl aller
     * Benutzer zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Benutzer, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfReaders(final List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Anzahl der Vormerkungen zurück, welche die übergebenen
     * Bedingungen erfüllen. Falls eine Leere Liste übergeben wird, so wird die
     * Anzahl aller Vormerkungen zurückgegeben.
     *
     * @param constraints
     *            Bedingungen zur Einschränkung der Abfrage.
     * @return Die Anzahl der Vormerkungen, welche die Bedingungen erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int getNumberOfReservations(List<Constraint> constraints)
            throws DataSourceException;

    /**
     * Gibt die Öffnungszeit mit der entsprechenden ID zurück. Falls keine
     * Öffnungszeit mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID des gesuchten Medientyps.
     * @return Die Öffnungszeit.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    OpeningTime getOpeningTime(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Eigenschaften im Bereich
     * <code> from </code> bis <code> to </code> zurück, welche die Bedingungen
     * erfüllen. Die Bereichsparameter erlauben das seitenweise Laden von
     * Abfragen. Dabei ist zu beachten, dass die erste Eigenschaft den Index 0
     * besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Eigenschaft aus der Liste.
     * @param to
     *            Der Index der letzten Eigenschaft aus der Liste.
     * @return Eine geordnete Liste aller Eigenschaften, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Property> getProperties(final List<Constraint> constraints,
            final int from, final int to, final List<OrderBy> order)
            throws DataSourceException;

    /**
     * Gibt die Eigenschaft mit der entsprechenden ID zurück. Falls keine
     * Eigenschaft mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID der gesuchten Eigenschaft.
     * @return Die Eigenschaft.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Property getProperty(final int id) throws DataSourceException;

    /**
     * Gibt die best- oder schlecht bewertesten Medien zurück.
     * <code>order</code> gibt an, ob die Liste Aufsteigend oder Absteigend
     * sortiert werden soll.
     *
     * @param max
     *            Die maximale Anzahl Datenbankeinträge.
     * @param order
     *            <code>true</code> für absteigend, <code>false</code> für
     *            aufsteigend.
     * @return Die geordnete Liste der Medien.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Medium> getRatedMedia(final int max, final boolean order)
            throws DataSourceException;

    /**
     * Gibt die Bewertung mit der entsprechenden ID zurück. Falls keine
     * Bewertung mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID der gesuchten Bewertung.
     * @return Die Bewertung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Rating getRating(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Bewertungen im Bereich
     * <code> from </code> bis <code> to </code> zurück, welche die Bedingungen
     * erfüllen. Die Bereichsparameter erlauben das seitenweise Laden von
     * Abfragen. Dabei ist zu beachten, dass die erste Bewertung den Index 0
     * besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Bewertung aus der Liste.
     * @param to
     *            Der Index der letzten Bewertung aus der Liste.
     * @return Eine geordnete Liste aller Bewertungen, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Rating> getRating(final List<Constraint> constraints, final int from,
            final int to, final List<OrderBy> order) throws DataSourceException;

    /**
     * Gibt einen Benutzer anhand seiner ID zurück.
     *
     * @param id
     *            Die ID des gesuchten Benutzers.
     * @return Der gefundene Benutzer oder <code>null</code>.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Reader getReader(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Benutzer im Bereich <code> from </code>
     * bis <code> to </code> zurück, welche die Bedingungen erfüllen. Die
     * Bereichsparameter erlauben das seitenweise Laden von Abfragen. Dabei ist
     * zu beachten, dass der erste Benutzer den Index 0 besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index des ersten Benutzers aus der Liste.
     * @param to
     *            Der Index des letzten Benutzers aus der Liste.
     * @return Eine geordnete Liste aller Benutzer, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Reader> getReaders(final List<Constraint> constraints, final int from,
            final int to, final List<OrderBy> order) throws DataSourceException;

    /**
     * Gibt die Vormerkung mit der entsprechenden ID zurück. Falls keine
     * Vormerkung mit entsprechender ID vorhanden ist, wird eine Exception
     * geworfen.
     *
     * @param id
     *            Die ID der gesuchten Vormerkung.
     * @return Die Vormerkung.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    Reservation getReservation(final int id) throws DataSourceException;

    /**
     * Gibt eine geordnete Liste aller Vormerkungen im Bereich
     * <code> from </code> bis <code> to </code> zurück, welche die Bedingungen
     * erfüllen. Die Bereichsparameter erlauben das seitenweise Laden von
     * Abfragen. Dabei ist zu beachten, dass das erste Medium den Index 0
     * besitzt.
     *
     * @param constraints
     *            Bedingungen um die Abfrage einzuschränken.
     * @param order
     *            Die Ordnung der Liste.
     * @param from
     *            Der Index der ersten Vormerkung aus der Liste.
     * @param to
     *            Der Index der letzten Vormerkung aus der Liste.
     * @return Eine geordnete Liste aller Vormerkung, welche die Bedingungen
     *         erfüllen.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    List<Reservation> getReservations(List<Constraint> constraints,
            final int from, final int to, List<OrderBy> order)
            throws DataSourceException;

    /**
     * Importiert die durch {@link #exportMediums(OutputStream output)
     * exportMediums()} erstellte CSV-Datei in die Datenbank.
     *
     * @param input
     *            Der eingehende Datenstrom.
     * @return Anzahl der eingelesenen Medien.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int importMediums(final InputStream input) throws DataSourceException;

    /**
     * Importiert die durch {@link #exportReaders(OutputStream output)
     * exportReaders()} erstellte CSV-Datei in die Datenbank.
     *
     * @param input
     *            Der eingehende Datenstrom.
     * @return Die Anzahl der eingelesenen Benutzer.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int importReaders(final InputStream input) throws DataSourceException;

    /**
     * Setzt die Datenbank in ihren anfänglichen Zustand zurück.
     *
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void reset() throws DataSourceException;

    /**
     * Spielt ein Backup auf, dass mit {@link #backup() backup()} erstellt
     * wurde. Vor dem Aufspielen wird die vorhandene Datenbank zurückgesetzt.
     *
     * @param path
     *            Der Pfad der Datei.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    void restore(String path) throws DataSourceException;

    /**
     * Aktualisiert die Kategorie mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Kategorie.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateCategory(final Category newValue) throws DataSourceException;

    /**
     * Aktualisiert die Schließungszeit mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Schließungszeit.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateClosedTime(final ClosedTime newValue) throws DataSourceException;

    /**
     * Aktualisiert den Kommentar mit der entsprechenden ID.
     *
     * @param newValue
     *            Der veränderte Kommentar.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateCommentary(Commentary newValue) throws DataSourceException;

    /**
     * Aktualisiert das Exemplar mit der entsprechenden ID.
     *
     * @param newValue
     *            Das veränderte Exemplar.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateExemplar(final Exemplar newValue) throws DataSourceException;

    /**
     * Aktualisiert die Verlängerung mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Verlängerung.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateExtension(final Extension newValue) throws DataSourceException;

    /**
     * Aktualisiert die Ausleihe mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Ausleihe.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateLending(final Lending newValue) throws DataSourceException;

    /**
     * Aktualisiert den Datenbankeintrag für das übergebene Medium. Die ID des
     * Mediums darf nicht verändert werden.
     *
     * @param newValue
     *            Das veränderte Medium.
     * @return Die Anzahl der veränderten Datenbankeinträge.
     * @throws DataSourceException
     *             Falls das Medium nicht verändert werden kann.
     */
    int updateMedium(final Medium newValue) throws DataSourceException;

    /**
     * Aktualisiert den Medientyp mit der entsprechenden ID.
     *
     * @param newValue
     *            Der veränderte Medientyp.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateMediumType(final MediumType newValue) throws DataSourceException;

    /**
     * Aktualisiert die Neuigkeit mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Neuigkeit.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateNews(final News newValue) throws DataSourceException;

    /**
     * Aktualisiert die Öffnungszeit mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Öffnungszeit.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateOpeningTime(final OpeningTime newValue)
            throws DataSourceException;

    /**
     * Aktualisiert die Eigenschaft mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Eigenschaft.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateProperty(final Property newValue) throws DataSourceException;

    /**
     * Aktualisiert die Bewertung mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Bewertung.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateRating(final Rating newValue) throws DataSourceException;

    /**
     * Aktualisiert den Benutzer mit der entsprechenden ID.
     *
     * @param newValue
     *            Der veränderte Benutzer.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateReader(final Reader newValue) throws DataSourceException;

    /**
     * Aktualisiert die Vormerkung mit der entsprechenden ID.
     *
     * @param newValue
     *            Die veränderte Vormerkung.
     * @return Die Anzahl veränderter Datenbankeinträge.
     * @throws DataSourceException
     *             Falls Probleme mit der Datenquelle auftreten.
     */
    int updateReservation(final Reservation newValue)
            throws DataSourceException;
}
