package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.naming.NamingException;

import swp.bibcommon.Property;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.OrderBy;

/**
 * Speichert Systemeinstellungen des Bibliothekars, "in Form" von Properties.
 * 
 * @author Helena Meißner
 *
 */
public class PropertyHandler extends BusinessObjectHandler<Property> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -1914755827729053146L;

	/**
	 * Eine Instanz einer Einstellung, die als Prototyp von Objekten für
	 * diesen Handler dient.
	 */
	private static Property prototype = new Property();

	/**
     * Liefert die einzige Instanz eines PropertyHandler.
     */
    private static volatile PropertyHandler instance;
	
	protected PropertyHandler() throws DataSourceException, NamingException {
		super();
	}
	
	/**
    *
    * Returns the one possible instance of this class. If there is no instance, a new one is created. (Singleton)
    *
    * @return the one instance of this class.
    *
    * @throws DataSourceException
    * 				is thrown if there are issues with the persistence component.
    */
	public static synchronized PropertyHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new PropertyHandler();
            } catch (Exception e) {
                throw new DataSourceException(e.getMessage());
            }
        }
        return instance;
	}

	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public Property get(int id) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getProperty(id);
	}

	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
	@Override
	public List<Property> get(List<Constraint> constraints, int from, int to,
			List<OrderBy> order) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getProperties(constraints, from, to, order);
	}

	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
	@Override
	public int getNumber(List<Constraint> constraints)
			throws DataSourceException {
		return persistence.getNumberOfProperties(constraints);
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
     * Methode, die nur aufgrund von Vererbung existiert, jedoch nicht benutzt wird.
     */
	@Override
	public int add(Property element) throws DataSourceException,
			BusinessElementAlreadyExistsException,
			NoSuchBusinessObjectExistsException {
		return 0;
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
	@Override
	public int update(Property property) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.updateProperty(property);
	}
	
	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public Property getPrototype() {
		return prototype;
	}

	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 * Methode, die nur aufgrund von Vererbung existiert, jedoch nicht benutzt wird.
	 */
	@Override
	public void delete(List<Property> elements) throws DataSourceException {
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#importCSV(java.io.InputStream)
     * Methode, die nur aufgrund von Vererbung existiert, jedoch nicht benutzt wird.
     */
	@Override
	public int importCSV(InputStream inputstream) throws DataSourceException {
		return 0;
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#exportCSV(java.io.OutputStream)
     * Methode, die nur aufgrund von Vererbung existiert, jedoch nicht benutzt wird.
     */
	@Override
	public void exportCSV(OutputStream outStream) throws DataSourceException {
	}
}
