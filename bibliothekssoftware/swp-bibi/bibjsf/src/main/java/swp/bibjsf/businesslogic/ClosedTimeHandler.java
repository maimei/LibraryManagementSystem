package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.time.DateFormatUtils;

import swp.bibcommon.ClosedTime;
import swp.bibcommon.Lending;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.persistence.Persistence;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Constraint.AttributeType;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;
import swp.bibjsf.utils.ReaderInfo;

/**
 * Ein Business Handler für alle Geschlossenen Zeiträume. Es implementiert die
 * Geschäftsregeln, die auf geschlossene Zeiträume zutreffen.
 * 
 * @author Helena Meißner
 * 
 */
public class ClosedTimeHandler extends BusinessObjectHandler<ClosedTime> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 6061195139822360628L;

	/**
	 * Eine Instanz einer vorrübergehenden Bibliotheksschließung, die als
	 * Prototyp von Objekten für diesen Handler dient.
	 */
	private static ClosedTime prototype = new ClosedTime();

	/**
	 * Instanz eines LendingHandlers.
	 */
	private LendingHandler lH = null;

	/**
	 * Instanz eines LendingHandlers.
	 */
	private OpeningTimeHandler oH = null;

	/**
	 * Returns a date in the SQL date format.
	 * 
	 * @param date
	 * @return date in SQL date format
	 */
	private String toDateFormat(Date date) {
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate.toString();
	}

	/**
	 * Returns the only instance of ClosedTimeHandler (singleton).
	 */
	private static volatile ClosedTimeHandler instance;

	protected ClosedTimeHandler() throws DataSourceException, NamingException {
		super();
		this.lH = null;
		this.oH = null;
	}

	/**
	 * Konstruktor zum Testen.
	 * 
	 * @param testing
	 *            Das gemockte Data-Objekt.
	 * @param lH
	 */
	public ClosedTimeHandler(Persistence testing, LendingHandler lH,
			OpeningTimeHandler oh) {
		super(testing);
		this.lH = lH;
		this.oH = oh;
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
	public static synchronized ClosedTimeHandler getInstance()
			throws DataSourceException {
		if (instance == null) {
			try {
				instance = new ClosedTimeHandler();
			} catch (Exception e) {
				throw new DataSourceException(e.getMessage());
			}
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessHandler#get(int)
	 */
	@Override
	public ClosedTime get(int id) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getClosedTime(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#get(java.util.List,
	 * int, int, java.util.List)
	 */
	@Override
	public List<ClosedTime> get(List<Constraint> constraints, int from, int to,
			List<OrderBy> order) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.getClosedTimes(constraints, from, to, order);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * swp.bibjsf.businesslogic.BusinessObjectHandler#getNumber(java.util.List)
	 */
	@Override
	public int getNumber(List<Constraint> constraints)
			throws DataSourceException {
		return persistence.getNumberOfClosedTimes(constraints);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(java.lang.Object)
	 */
	@Override
	public int add(ClosedTime closedTime) throws DataSourceException,
			BusinessElementAlreadyExistsException,
			NoSuchBusinessObjectExistsException {
		logger.info("add closed time: " + closedTime);
		if (closedTime.hasId()
				&& persistence.getClosedTime(closedTime.getId()) != null) {
			logger.info("closedTime already exists and could not be added: "
					+ closedTime);
			throw new BusinessElementAlreadyExistsException(
					Messages.get("closedTime exists") + " "
							+ Messages.get("id") + " = " + closedTime.getId());
		}
		int result = persistence.addClosedTime(closedTime);
		logger.debug(closedTime + "wurde hinzugefügt");
		if (result < 0) {
			throw new DataSourceException(
					Messages.get("closedTimeAdditionFailure"));
		}
		changeInvalidEndDates(closedTime);
		return result;
	}

	/**
	 * Aktualisiert die Enddaten für Ausleihen. Wird aufgerufen, nachdem neue
	 * geschlossene Zeiträume hinzugefügt wurden.
	 * 
	 * @throws NoSuchBusinessObjectExistsException
	 * @throws DataSourceException
	 */
	private void changeInvalidEndDates(ClosedTime close)
			throws NoSuchBusinessObjectExistsException, DataSourceException {

		if (lH == null) {
			lH = LendingHandler.getInstance();
		}
		logger.debug(lH + " hat geklappt");
		List<Constraint> c = new ArrayList<Constraint>();
		c.add(new Constraint("till", ">=", toDateFormat(close.getStart()),
				"AND", null));
		c.add(new Constraint("till", "<=", toDateFormat(close.getTill()),
				"AND", null));
		c.add(new Constraint("returned", "=", "false", "AND",
				AttributeType.STRING));

		try {
			ReaderInfo rI = new ReaderInfo();
			List<Lending> ll = lH.get(c, 0, Integer.MAX_VALUE, null);
			logger.debug(ll + " hat geklappt");
			for (Lending lending : ll) {

				lending.setTill(findNextOpenDay(lending.getTill()));
				try {
					int bla = lH.update(lending);
					logger.debug(bla + " hat geklappt");
				} catch (NoSuchBusinessObjectExistsException e) {
					// Wenn Lending nicht mehr vorhanden, dann muss Datum
					// nicht mehr verändert werden.
				}
				try {
					rI.sendChangeTill(lending);
					logger.debug("sendChangeTill hat geklappt");
				} catch (MessagingException
						| NoSuchBusinessObjectExistsException e) {
					// nicht versendete E-Mail ist nicht kritisch ^^
				}
			}
		} catch (DataSourceException e) {
			logger.error(e);
		} catch (NamingException e2) {
			logger.error(e2);
		}
	}

	/**
	 * ermittelt den nächsten geöffneten Tag, ab dem übergebenen Tag.
	 * 
	 * @param d
	 *            der Tag, ab dem der nächste offene gesucht werden soll.
	 * @return den nächsten geöffneten Tag
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public Date findNextOpenDay(Date d) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		Calendar cal = Calendar.getInstance();
		if (oH == null) {
			oH = OpeningTimeHandler.getInstance();
		}
		while (isClosedDay(d) || !oH.isOpenDay(d)) {
			cal.setTime(d);
			cal.add(Calendar.DATE, 1);
			d = cal.getTime();
		}
		return d;
	}

	/*
	 * die Methode existiert nur aufgrund von Vererbung, wird aber nicht
	 * verwendet.
	 */
	@Override
	public int update(ClosedTime newValue) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#getPrototype()
	 */
	@Override
	public ClosedTime getPrototype() {
		return prototype;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * swp.bibjsf.businesslogic.BusinessObjectHandler#delete(java.util.List)
	 */
	@Override
	public void delete(List<ClosedTime> elements) throws DataSourceException {
		for (ClosedTime closedTime : elements) {
			persistence.deleteClosedTime(closedTime);
		}
	}

	/**
	 * prüft, ob die Bibliothek am übergebenen Tag geschlossen ist.
	 * 
	 * @author Helena Meißner
	 * @param day
	 *            der zu prüfende Tag.
	 * @return true, falls an dem Tag geschlossen ist, sonst false;
	 * @throws NoSuchBusinessObjectExistsException
	 * @throws DataSourceException
	 */
	public boolean isClosedDay(Date day) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		List<Constraint> cons = new ArrayList<Constraint>();
		cons.add(new Constraint("start", "<=", DateFormatUtils.format(day,
				"yyyy-MM-dd"), "AND", AttributeType.STRING));
		cons.add(new Constraint("till", ">=", DateFormatUtils.format(day,
				"yyyy-MM-dd"), "OR", AttributeType.STRING));

		List<ClosedTime> closedDays = get(cons, 0, Integer.MAX_VALUE, null);
		for (ClosedTime closedTime : closedDays) {
			boolean isStartDay = day.equals(closedTime.getStart());
			boolean isEndDay = day.equals(closedTime.getTill());
			boolean isInbetween = day.after(closedTime.getStart())
					&& day.before(closedTime.getTill());
			if (isStartDay || isEndDay || isInbetween) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Berechnet alle Tage an denen geschlossen ist, im Bereich zwischen start
	 * und end
	 * 
	 * @param start
	 *            Datum ab dem geprüft werdne soll
	 * @param end
	 *            Datum bis zu dem geprüft werden soll
	 * @return Menge aller geschl. Tage im übergebenen Bereich
	 * @throws DataSourceException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public Set<Date> getClosedDays(Date start, Date end)
			throws DataSourceException, NoSuchBusinessObjectExistsException {
		Set<Date> closed = new HashSet<>();
		List<Constraint> cons = new ArrayList<Constraint>();
		cons.add(new Constraint("start", "<=", DateFormatUtils.format(end,
				"yyyy-MM-dd"), "AND", AttributeType.STRING));
		cons.add(new Constraint("till", ">=", DateFormatUtils.format(start,
				"yyyy-MM-dd"), "OR", AttributeType.STRING));
		List<ClosedTime> closedDays = get(cons, 0, Integer.MAX_VALUE, null);
		Calendar cal = Calendar.getInstance();
		for (ClosedTime closedTime : closedDays) {
			cal.setTime(closedTime.getStart());
			while (cal.getTime().before(closedTime.getTill())
					|| cal.getTime().equals(closedTime.getTill())) {
				closed.add(cal.getTime());
				cal.add(Calendar.DATE, 1);
			}
		}
		return closed;

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
		throw new NotImplementedException();
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
		throw new NotImplementedException();
	}
}
