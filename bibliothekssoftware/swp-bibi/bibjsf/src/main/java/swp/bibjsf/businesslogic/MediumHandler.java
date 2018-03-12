package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import swp.bibcommon.Medium;
import swp.bibcommon.MediumType;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;

/**
 * Handler für Books, Movies, Games und Audios.
 * 
 * @author Helena Meißner, Eike Externest, Niklas Bruns
 * 
 */
public class MediumHandler extends BusinessObjectHandler<Medium> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7090189116572552089L;

	/**
	 * Returns the only instance of CategoryHandler (singleton).
	 */
	private static volatile MediumHandler instance;

	/**
	 * Typ des Mediums
	 */
	private String type = "medium";

	/**
	 * Konstruktor.
	 */
	protected MediumHandler() throws DataSourceException, NamingException {
		super();
	}

	/**
	 * 
	 * Returns the one possible instance of this class. If there is no instance,
	 * a new one is created. (Singleton)
	 * 
	 * @return the one instance of this class.
	 * 
	 * @throws DataSourceException
	 *             is thrown if there are issues with the persistence component.
	 */
	public static synchronized MediumHandler getInstance()
			throws DataSourceException {
		if (instance == null) {
			try {
				instance = new MediumHandler();
			} catch (Exception e) {
				throw new DataSourceException(e.getMessage());
			}
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
	 */
	@Override
	public synchronized int add(Medium medium) throws DataSourceException,
			BusinessElementAlreadyExistsException {
		logger.info("add medium: " + medium);
		if (medium.hasId() && persistence.getMedium(medium.getId()) != null) {
			logger.info("medium already exists and could not be added: "
					+ medium);
			throw new BusinessElementAlreadyExistsException(
					Messages.get("mediumexists") + " " + Messages.get("id")
							+ " = " + medium.getId());
		}
		int result = persistence.addMedium(medium);
		if (result < 0) {
			throw new DataSourceException(Messages.get("mediumAdditionFailure"));
		}
		logger.info("medium added: " + medium.getTitle());
		return result;
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int,
	 * java.lang.Object)
	 */
	@Override
	public synchronized int update(Medium medium) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		logger.info("update medium " + medium.toString());
		return persistence.updateMedium(medium);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public synchronized Medium get(int id) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getMedium(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List,
	 * int, int, java.util.List)
	 */
	@Override
	public synchronized List<Medium> get(List<Constraint> constraints,
			int from, int to, List<OrderBy> order) throws DataSourceException {

		List<Medium> mediums = persistence.getMediums(constraints, from, to,
				order);
		return mediums;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
	 */
	@Override
	public synchronized int getNumber(List<Constraint> constraints)
			throws DataSourceException {

		return persistence.getNumberOfMediums(constraints);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessHandler#getPrototype() Methode, die
	 *      nur aufgrund der Vererbung bestehen muss, jedoch nicht verwendet
	 *      wird. Zur Verwendung siehe getType(Medium medium).
	 */
	@Override
	public synchronized Medium getPrototype() {
		return new Medium();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessHandler#delete(java.util.List)
	 */
	@Override
	public void delete(List<Medium> elements) throws DataSourceException {
		for (Medium medium : elements) {
			logger.info("medium deleted: " + medium.getTitle());
			persistence.deleteMedium(medium);
		}
	}

	/**
	 * @author Helena Meißner Gibt alle Exemplare zu einem Medium zurück, die
	 *         ausleihbar sind.
	 * 
	 * @param mediumID
	 *            ID des Medium, zu dem die verfügbaren Exemplare gehören.
	 * @return exemplars Liste aller verfügbaren Exemplare.
	 */
	public int getAllAvailableExemplars(int mediumID)
			throws DataSourceException {
		List<Constraint> constraints = new ArrayList<Constraint>();

		/* Constraint für Verfügbarkeit */
		constraints.add(new Constraint("status", "like", "leihbar", "AND",
				Constraint.AttributeType.STRING));

		/* Constraint für jeweiliges Medium */
		constraints.add(new Constraint("mediumid", "=", String
				.valueOf(mediumID), "OR", Constraint.AttributeType.INTEGER));

		return persistence.getNumberOfExemplars(constraints);
	}

	/**
	 * Getter für den Typ.
	 * 
	 * @return der Typ
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter für den Typ
	 * 
	 * @param type
	 *            der zu setzende Typ
	 */
	public void setType(String type) {
		logger.info(type);
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * swp.bibjsf.businesslogic.BusinessObjectHandler#importCSV(java.io.InputStream
	 * )
	 */
	@Override
	public int importCSV(InputStream inputstream) throws DataSourceException {
		return persistence.importMediums(inputstream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * swp.bibjsf.businesslogic.BusinessObjectHandler#exportCSV(java.io.OutputStream
	 * )
	 */
	@Override
	public void exportCSV(OutputStream outStream) throws DataSourceException {
		persistence.exportMediums(outStream);
	}

	/**
	 * Ermittelt den Medientyp eines Mediums anhand seiner ID.
	 * 
	 * @param mediumid
	 *            die ID des Mediums
	 * @return den Medientyp des Mediums
	 * @throws DataSourceException
	 */
	public MediumType getType(int mediumid) throws DataSourceException {
		Medium medium = persistence.getMedium(mediumid);
		return persistence.getMediumType(medium.getMediumType());
	}
}
