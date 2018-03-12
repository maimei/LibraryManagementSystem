package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.MediumType;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Ein Business Handler für alle Medientypen. Er implementiert die Geschäftsregeln, die auf Medientypen zutreffen.
 * 
 * @author Helena Meißner
 *
 */
public class MediumTypeHandler extends BusinessObjectHandler<MediumType> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 9157584881468204580L;

	/**
     * Returns the only instance of MediumTypeHandler (singleton).
     */
    private static volatile MediumTypeHandler instance;
	
    private static MediumType prototype = new MediumType();
    
    /**
     * Konstruktor.
     * @throws DataSourceException
     * @throws NamingException
     */
	protected MediumTypeHandler() throws DataSourceException, NamingException {
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
	public static synchronized MediumTypeHandler getInstance() throws DataSourceException {
        if (instance == null) {
            try {
                instance = new MediumTypeHandler();
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
	public MediumType get(int id) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getMediumType(id);
	}

	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List, int, int, java.util.List)
   	 */
	@Override
	public List<MediumType> get(List<Constraint> constraints, int from, int to,
			List<OrderBy> order) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getMediumTypes(constraints, from, to, order);
	}

	/* (non-Javadoc)
   	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
   	 */
	@Override
	public int getNumber(List<Constraint> constraints)
			throws DataSourceException {
		return persistence.getNumberOfMediumType(constraints);
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
     */
	@Override
	public int add(MediumType mediumType) throws DataSourceException,
			BusinessElementAlreadyExistsException,
			NoSuchBusinessObjectExistsException {
		logger.info("add medium type: " + mediumType);
    	if (mediumType.hasId() && persistence.getExtension(mediumType.getId()) != null) {
                logger.info("mediumType already exists and could not be added: "
                        + mediumType);
                throw new BusinessElementAlreadyExistsException(Messages.get("mediumType exists") + " "
                		+ Messages.get("id") + " = " + mediumType.getId());
        }
        int result = persistence.addMediumType(mediumType);
        if (result < 0) {
            throw new DataSourceException(Messages.get("mediumTypeAdditionFailure"));
        }
        return result;
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int, java.lang.Object)
     */
	@Override
	public int update(MediumType mediumType) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.updateMediumType(mediumType);
	}

	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public MediumType getPrototype() {
		return prototype;
	}

	/* (non-Javadoc)
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
	@Override
	public void delete(List<MediumType> elements) throws DataSourceException {
		for(MediumType mediumType : elements)
   	   		persistence.deleteMediumType(mediumType);		
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
