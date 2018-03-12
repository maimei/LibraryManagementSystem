package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Commentary;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle Commentary Daten. Es implementiert die Geschäftsregeln, die auf Kommentare zutreffen.
 * 
 * @author Helena Meißner, Niklas Bruns
 *
 */
public class CommentaryHandler extends BusinessObjectHandler<Commentary> {
	
	protected CommentaryHandler() throws DataSourceException, NamingException {
		super();
	}
	
	private static Commentary prototype = new Commentary();
	
	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7090189116572552089L;
	
    /**
     * Returns the only instance of CommentaryHandler (singleton).
     */
    private static volatile CommentaryHandler instance;
    
	/**
    *
    * Returns the one possible instance of this class. If there is no instance, a new one is created. (Singleton)
    *
    * @return the one instance of this class.
    *
    * @throws DataSourceException
    * 				is thrown if there are issues with the persistence component.
    */
	public static synchronized CommentaryHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new CommentaryHandler();
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
	public synchronized int add(Commentary commentary)
			throws DataSourceException, BusinessElementAlreadyExistsException, 
			NoSuchBusinessObjectExistsException {
		logger.info("add commentary: " + commentary);

		if (commentary.hasId()
				&& persistence.getMedium(commentary.getId()) != null) {
			logger.info("commentary already exists and could not be added: "
					+ commentary);
			throw new BusinessElementAlreadyExistsException(
					Messages.get("commentaryexists") + " " + Messages.get("id")
							+ " = " + commentary.getId());
		}
		int result = persistence.addCommentary(commentary);
		if (result < 0) {
			throw new DataSourceException(
					Messages.get("commentaryAdditionFailure"));
		} else {
			ReaderHandler rH = ReaderHandler.getInstance();
	           rH.updateLastUse(commentary.getReaderID());
		}
		logger.info("commentary added: " + persistence.getReader(commentary.getReaderID()).getUsername()
				+ "; commented medium:" + persistence.getMedium(commentary.getMediumID()).getTitle());
		return result;
	}
    	
	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
    @Override
    public synchronized int update(Commentary commentary) throws DataSourceException, NoSuchBusinessObjectExistsException {
    	logger.info("update commentary " + commentary.toString());
        return persistence.updateCommentary(commentary);
    }
    
    /* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public synchronized Commentary get(int id) throws DataSourceException, NoSuchBusinessObjectExistsException {
		return persistence.getCommentary(id);
	}
	    
    /* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
   	@Override
   	public synchronized List<Commentary> get(List<Constraint> constraints, int from, int to,
   			List<OrderBy> order) throws DataSourceException, NoSuchBusinessObjectExistsException {
   		return persistence.getCommentaries(constraints, from, to, order);
   	}
   	
   	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
   	@Override
   	public synchronized int getNumber(List<Constraint> constraints) throws DataSourceException {
   		return persistence.getNumberOfCommentaries(constraints);
   	}
   	
   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
   	@Override
   	public synchronized void delete(List<Commentary> elements) throws DataSourceException {
   		for( Commentary commentary : elements){
   			logger.info(commentary.getId());
   			persistence.deleteCommentary(commentary);
   		}
   	}

   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public synchronized Commentary getPrototype() {
		return prototype;
	}
   	
   	/**
   	 * setzt den Status aller Kommentare der Liste in der Datenbank auf aktiviert.
   	 * 
   	 * @param comments Liste der Kommentare, die aktiviert werden sollen.
   	 * @throws DataSourceException 
   	 * @throws NoSuchBusinessObjectExistsException 
   	 */
   	void activate(List<Commentary> comments) throws DataSourceException, NoSuchBusinessObjectExistsException {
   		for(Commentary comment : comments){
   			comment.setActive(true);
   			update(comment);
   		}
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
