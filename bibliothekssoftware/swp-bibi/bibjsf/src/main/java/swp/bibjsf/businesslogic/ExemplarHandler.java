package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Exemplar;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle Exemplare. Es implementiert die Geschäftsregeln, die auf Exemplare zutreffen.
 *
 * @author Helena Meißner, Niklas Bruns
 *
 */
public class ExemplarHandler extends BusinessObjectHandler<Exemplar> {

	protected ExemplarHandler() throws DataSourceException, NamingException {
		super();
	}

	public ExemplarHandler(Persistence testing) {
        super(testing);
    }

	private static Exemplar prototype = new Exemplar();

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 1L;

    /**
     * Returns the only instance of CommentaryHandler (singleton).
     */
    private static volatile ExemplarHandler instance;

	/**
    *
    * Returns the one possible instance of this class. If there is no instance, a new one is created. (Singleton)
    *
    * @return the one instance of this class.
    *
    * @throws DataSourceException
    * 				is thrown if there are issues with the persistence component.
    */
	public static synchronized ExemplarHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new ExemplarHandler();
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
	public synchronized Exemplar get(int id) throws DataSourceException, NoSuchBusinessObjectExistsException {
		return persistence.getExemplar(id);
	}

	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
   	@Override
   	public synchronized List<Exemplar> get(List<Constraint> constraints, int from, int to,
   			List<OrderBy> order) throws DataSourceException, NoSuchBusinessObjectExistsException {
   		return  persistence.getExemplars(constraints, from, to, order);
   	}

   	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
   	@Override
   	public synchronized int getNumber(List<Constraint> constraints) throws DataSourceException {
   		return persistence.getNumberOfExemplars(constraints);
   	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
     */
	@Override
    public synchronized int add(Exemplar exemplar) throws DataSourceException,
    		BusinessElementAlreadyExistsException {
    	logger.info("add exemplar: " + exemplar);
    	if (exemplar.hasId() && persistence.getCategory(exemplar.getId()) != null) {
               logger.info("exemplar already exists and could not be added: "
                       + exemplar);
               throw new BusinessElementAlreadyExistsException(Messages.get("exemplarexists") + " "
                	+ Messages.get("id") + " = " + exemplar.getId());
        }
        int result = persistence.addExemplar(exemplar);
        if (result < 0) {
            throw new DataSourceException(Messages.get("exemplarAdditionFailure"));
        }
        logger.info("exemplar added: " + persistence.getMedium(exemplar.getMediumID()).getTitle());
        return result;
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
    @Override
    public synchronized int update(Exemplar exemplar) throws DataSourceException, NoSuchBusinessObjectExistsException {
    	logger.info("update exemplar " + exemplar.toString());
        return persistence.updateExemplar(exemplar);
    }

    /* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public synchronized Exemplar getPrototype() {
		return prototype;
	}

	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
   	@Override
   	public synchronized void delete(List<Exemplar> elements) throws DataSourceException {
   		for(Exemplar exemplar : elements){
			logger.info("exemplar deleted: " + persistence.getMedium(exemplar.getMediumID()).getTitle());
   			persistence.deleteExemplar(exemplar);
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
