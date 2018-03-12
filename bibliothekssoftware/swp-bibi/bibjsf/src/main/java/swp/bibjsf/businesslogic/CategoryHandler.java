package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.naming.NamingException;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibcommon.Category;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;
import org.apache.commons.lang.NotImplementedException;

/**
 * Ein Business Handler für alle Kategorien. Es implementiert die Geschäftsregeln, die auf Kategorien zutreffen.
 * 
 * @author Helena Meißner, Niklas Bruns
 *
 */
public class CategoryHandler extends BusinessObjectHandler<Category> {
	
	/**
	 * Konstruktor.
	 * @throws DataSourceException
	 * @throws NamingException
	 */
	protected CategoryHandler() throws DataSourceException, NamingException {
		super();
	}
	
	/**
	 * Eine Instanz eines Medientypen, der als Prototyp von Objekten für
	 * diesen Handler dient.
	 */
	private static Category prototype = new Category();
	
	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7090189116572552089L;
	
    /**
     * Returns the only instance of CategoryHandler (singleton).
     */
    private static volatile CategoryHandler instance;
    
	/**
    *
    * Returns the one possible instance of this class. If there is no instance, a new one is created. (Singleton)
    *
    * @return the one instance of this class.
    *
    * @throws DataSourceException
    * 				is thrown if there are issues with the persistence component.
    */
	public static synchronized CategoryHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new CategoryHandler();
            } catch (Exception e) {
                throw new DataSourceException(e.getMessage());
            }
        }
        return instance;
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
     */
	@Override
    public synchronized int add(Category category) throws DataSourceException, BusinessElementAlreadyExistsException {
    	logger.info("add category: " + category);
    	if (category.hasId() && persistence.getCategory(category.getId()) != null) {
                logger.info("category already exists and could not be added: "
                        + category);
                throw new BusinessElementAlreadyExistsException(Messages.get("categoryexists") + " "
                		+ Messages.get("id") + " = " + category.getId());
    	}
        int result = persistence.addCategory(category);
        if (result < 0) {
            throw new DataSourceException(Messages.get("categoryAdditionFailure"));
        }
        logger.info("category added: " + category.getName());
    	return result;
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
    @Override
    public synchronized int update(Category category) throws DataSourceException, NoSuchBusinessObjectExistsException {
    	logger.info("update category " + category.toString());
        return persistence.updateCategory(category);
    }
    
    /* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public synchronized Category get(int id) throws DataSourceException, NoSuchBusinessObjectExistsException {
		return persistence.getCategory(id);
	}	
    
    /* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
   	@Override
   	public synchronized List<Category> get(List<Constraint> constraints, int from, int to,
   			List<OrderBy> order) throws DataSourceException, NoSuchBusinessObjectExistsException {
   		return persistence.getCategories(constraints, from, to, order);
   	}
   	
   	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
   	@Override
   	public synchronized int getNumber(List<Constraint> constraints) throws DataSourceException {
   		return persistence.getNumberOfCategories(constraints);
   	}
   	
   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
   	@Override
   	public synchronized void delete(List<Category> elements) throws DataSourceException {
   		for(Category category : elements){
			logger.info("category deleted: " + category.getName());	
   			persistence.deleteCategory(category);
   		}
   	}

   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public synchronized Category getPrototype() {
		return prototype;
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#importCSV(java.io.InputStream)
     */
	@Override
	public int importCSV(InputStream inputstream) throws DataSourceException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#exportCSV(java.io.OutputStream)
     */
	@Override
	public void exportCSV(OutputStream outStream) throws DataSourceException {
		throw new NotImplementedException();	
	}
}
