package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;


import swp.bibcommon.Reservation;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle Vormerk-Daten. Es implementiert die Geschäftsregeln, die auf Vormerkungen zutreffen.
 * 
 * @author Helena Meißner
 *
 */
public class ReservationHandler extends BusinessObjectHandler<Reservation> {
	
	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7090189116572552089L;
	
    /**
     * Returns the only instance of ReservationHandler (singleton).
     */
    private static volatile ReservationHandler instance;
    
    /**
     * An instance of Lending acting as the prototype of objects handled by this handler.
     */
    private static Reservation prototype = new Reservation();	
    
    protected ReservationHandler() throws DataSourceException, NamingException {
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
	public static synchronized ReservationHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new ReservationHandler();
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
    public synchronized int add(Reservation reservation) throws DataSourceException, 
    		BusinessElementAlreadyExistsException, NoSuchBusinessObjectExistsException {
    	logger.info("add reservation: " + reservation);
    	if (reservation.hasId() && persistence.getCategory(reservation.getId()) != null) {
                logger.info("reservation already exists and could not be added: "
                        + reservation);
                throw new BusinessElementAlreadyExistsException(Messages.get("lendingexists") + " "
                		+ Messages.get("id") + " = " + reservation.getId());
        }
        int result = persistence.addReservation(reservation);
        if (result < 0) {
            throw new DataSourceException(Messages.get("lendingAdditionFailure"));
        } else {
        	ReaderHandler rH = ReaderHandler.getInstance();
           rH.updateLastUse(reservation.getReaderID());
        }        
        logger.info("reservation added: " + persistence.getMedium(reservation.getMediumID()).getTitle()
        		+ "; reservation reader:" + persistence.getReader(reservation.getReaderID()).getUsername());
        return result;
	}
	
	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
    @Override
    public synchronized int update(Reservation reservation) throws DataSourceException, NoSuchBusinessObjectExistsException {
    	logger.info("update lending " + reservation.toString());
        return persistence.updateReservation(reservation);
    }
    
    /* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public synchronized Reservation get(int id) throws DataSourceException, NoSuchBusinessObjectExistsException {
	return persistence.getReservation(id);
	}
	
	
    
    /* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
   	@Override
   	public synchronized List<Reservation> get(List<Constraint> constraints, int from, int to,
   			List<OrderBy> order) throws DataSourceException {
   		return persistence.getReservations(constraints, from, to, order);
   	}
   	
   	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
   	@Override
   	public synchronized int getNumber(List<Constraint> constraints) throws DataSourceException {
   		return persistence.getNumberOfReservations(constraints);
   	}
   	
   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
   	@Override
   	public synchronized void delete(List<Reservation> elements) throws DataSourceException {
   	for(Reservation reservation : elements)
   	{
   		persistence.deleteReservation(reservation);
   	}
   	}

   	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public synchronized Reservation getPrototype() {
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