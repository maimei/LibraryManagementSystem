package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Rating;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle Bewertungs-Daten. Es implementiert die Geschäftsregeln, die auf Bewertungen zutreffen.
 * 
 * @author Helena Meißner
 *
 */
public class RatingHandler extends BusinessObjectHandler<Rating> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -2258051125833660278L;

	/**
	 * Eine Instanz einer Medieneinstellung, die als Prototyp von Objekten für
	 * diesen Handler dient.
	 */
	private static Rating prototype = new Rating();
	
	/**
     * Speichert die einzige Instanz eines RatingsHandler.
     */
    private static volatile RatingHandler instance;
	
	/**
	 * Konstruktor.
	 * @throws DataSourceException
	 * @throws NamingException
	 */
	protected RatingHandler() throws DataSourceException, NamingException {
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
	public static synchronized RatingHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new RatingHandler();
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
	public Rating get(int id) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getRating(id);
	}

	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
	@Override
	public List<Rating> get(List<Constraint> constraints, int from, int to,
			List<OrderBy> order) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getRating(constraints, from, to, order);
	}

	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
	@Override
	public int getNumber(List<Constraint> constraints)
			throws DataSourceException {
		return persistence.getNumberOfRatings(constraints);
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
     */
	@Override
	public int add(Rating rating) throws DataSourceException,
			BusinessElementAlreadyExistsException,
			NoSuchBusinessObjectExistsException {
		logger.info("add rating: " + rating);

		if (rating.hasId()
				&& persistence.getMedium(rating.getId()) != null) {
			logger.info("rating already exists and could not be added: "
					+ rating);
			throw new BusinessElementAlreadyExistsException(
					Messages.get("ratingexists") + " " + Messages.get("id")
							+ " = " + rating.getId());
		}

		int result = persistence.addRating(rating);
		if (result < 0) {
			throw new DataSourceException(
					Messages.get("ratingAdditionFailure"));
		} else {			
			ReaderHandler rH = ReaderHandler.getInstance();
	           rH.updateLastUse(rating.getReaderID());
		}
		return result;
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
	@Override
	public int update(Rating rating) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.updateRating(rating);
	}

	@Override
	public Rating getPrototype() {
		return prototype;
	}

	@Override
	public void delete(List<Rating> elements) throws DataSourceException {
		for(Rating rating : elements) {
   	   		persistence.deleteRating(rating);
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
