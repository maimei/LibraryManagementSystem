package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Extension;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle Verlängerungs-Daten. Es implementiert die Geschäftsregeln, die auf Verlängerungen zutreffen.
 * 
 * @author Helena Meißner, Niklas Bruns
 *
 */
public class ExtensionHandler extends BusinessObjectHandler<Extension> {
	
	protected ExtensionHandler() throws DataSourceException, NamingException {
		super();
	}
	
	private static Extension prototype = new Extension();
	
	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7090189116572552089L;
	
    /**
     * Returns the only instance of ExtensionHandler (singleton).
     */
    private static volatile ExtensionHandler instance;
    
	/**
    *
    * Returns the one possible instance of this class. If there is no instance, a new one is created. (Singleton)
    *
    * @return the one instance of this class.
    *
    * @throws DataSourceException
    * 				is thrown if there are issues with the persistence component.
    */
	public static synchronized ExtensionHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new ExtensionHandler();
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
    public synchronized int add(Extension extension) throws DataSourceException, BusinessElementAlreadyExistsException,
    NoSuchBusinessObjectExistsException {
    	logger.info("add extension: " + extension);
    	if (extension.hasId() && persistence.getExtension(extension.getId()) != null) {
                logger.info("extension already exists and could not be added: "
                        + extension);
                throw new BusinessElementAlreadyExistsException(Messages.get("extensionexists") + " "
                		+ Messages.get("id") + " = " + extension.getId());
        }
        int result = persistence.addExtension(extension);
        if (result < 0) {
            throw new DataSourceException(Messages.get("extensionAdditionFailure"));
        } else {
        	ReaderHandler rH = ReaderHandler.getInstance();
            rH.updateLastUse(extension.getReaderID());        	
        }
        logger.info("extension added: " + persistence.getMedium(persistence.getExemplar(extension.getExemplarID()).getMediumID()).getTitle()
        		+ "; extension reader:" + persistence.getReader(extension.getReaderID()).getUsername());
        return result;
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
    @Override
    public synchronized int update(Extension extension) throws DataSourceException, NoSuchBusinessObjectExistsException {
    	logger.info("update extension " + extension.toString());
        return persistence.updateExtension(extension);
    }
    
    /* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public synchronized Extension get(int id) throws DataSourceException {
		return persistence.getExtension(id);
	}
	
    /* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
   	@Override
   	public synchronized List<Extension> get(List<Constraint> constraints, int from, int to,
   			List<OrderBy> order) throws DataSourceException {
   		return persistence.getExtensions(constraints, from, to, order);
   	}
   	
   	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
   	@Override
   	public synchronized int getNumber(List<Constraint> constraints) throws DataSourceException {
   		return persistence.getNumberOfExtensions(constraints);
   	}
   	
   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
   	@Override
   	public synchronized void delete(List<Extension> elements) throws DataSourceException 
   	{
   	  	for( Extension extension : elements)
   	  	{
   	   		persistence.deleteExtension(extension);
   	  	}
   	}

   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public synchronized Extension getPrototype() {
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