package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.News;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle News-Daten. Es implementiert die Geschäftsregeln, die auf News zutreffen.
 * 
 * @author Helena Meißner
 *
 */
public class NewsHandler extends BusinessObjectHandler<News> {
	
	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7090189116572552089L;
	
    /**
     * Returns the only instance of CategoryHandler (singleton).
     */
    private static volatile NewsHandler instance;
    
    /**
     * An instance of Lending acting as the prototype of objects handled by this handler.
     */
    private static News prototype = new News();
    
	protected NewsHandler() throws DataSourceException, NamingException {
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
	public static synchronized NewsHandler getInstance() 
			throws DataSourceException {
        
		if (instance == null) {
            try {
                instance = new NewsHandler();
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
    public synchronized int add(News news) throws DataSourceException, 
    	BusinessElementAlreadyExistsException {
    	
		logger.info("add news: " + news);
        if (news.hasId() && persistence.getCategory(news.getId()) != null) {
            logger.info("news already exists and could not be added: "
                    + news);
            throw new BusinessElementAlreadyExistsException(Messages.get("newsexists") + " "
            		+ Messages.get("id") + " = " + news.getId());
        }
        int result = persistence.addNews(news);
        if (result < 0) {
            throw new DataSourceException(Messages.get("newsAdditionFailure"));
        }
        return result;
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
    @Override
    public synchronized int update(News news) throws DataSourceException, NoSuchBusinessObjectExistsException {
    	return persistence.updateNews(news);
    }
    
    /* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public synchronized News get(int id) throws DataSourceException, NoSuchBusinessObjectExistsException {
		return persistence.getNews(id);
	}
	
	
    
    /* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
   	@Override
   	public synchronized List<News> get(List<Constraint> constraints, int from, int to,
   			List<OrderBy> order) throws DataSourceException {
   		List<OrderBy> list = new ArrayList<>();
   		list.add(new OrderBy("dateOfPublication", false));
   		return persistence.getNews(constraints, from, to, list);
   	}
   	
   	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 * is never used, only existent because of inheritance
   	 */
   	@Override
   	public synchronized int getNumber(List<Constraint> constraints) throws DataSourceException {
   		return persistence.getNumberOfNews(constraints);
   	}
   	
   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
   	@Override
   	public synchronized void delete(List<News> elements) throws DataSourceException {
   		for(News news : elements)
   			persistence.deleteNews(news);
   	}

   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public synchronized News getPrototype() {
		return prototype;
	}

	@Override
	public int importCSV(InputStream inputstream) throws DataSourceException {
		throw new NotImplementedException();
	}

	@Override
	public void exportCSV(OutputStream outStream) throws DataSourceException {
		throw new NotImplementedException();
	}
}