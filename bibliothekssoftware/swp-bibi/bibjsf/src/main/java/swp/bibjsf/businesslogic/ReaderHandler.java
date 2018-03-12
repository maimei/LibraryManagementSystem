/*
 * Copyright (c) 2013 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package swp.bibjsf.businesslogic;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.naming.NamingException;

import swp.bibcommon.Reader;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;
import swp.bibjsf.utils.PasswordSecurity;
import swp.bibjsf.utils.ReaderInfo;

/**
 * A business handler for all reader data. It implements the business rules that
 * apply to readers.
 * 
 * @author koschke, Niklas Bruns
 */
public class ReaderHandler extends BusinessObjectHandler<Reader> {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = -5653849921779676759L;

	/**
	 * The only instance of this class (singleton).
	 */
	private static volatile ReaderHandler instance;

	protected ReaderHandler() throws DataSourceException, NamingException {
		super();
	}

	/**
	 * Returns unique instance of this class.
	 * 
	 * @return unique instance of this class
	 * 
	 * @throws DataSourceException
	 *             thrown in case of problems with the data source
	 */
	public static synchronized ReaderHandler getInstance()
			throws DataSourceException {

		if (instance == null) {
			try {
				instance = new ReaderHandler();
			} catch (Exception e) {
				throw new DataSourceException(e.getMessage());
			}
		}
		return instance;
	}

	/**
	 * Returns a list of readers of the library fulfilling the given
	 * constraints.
	 * 
	 * @param constraints
	 *            a list of the constraints for this query
	 * @param order
	 * @return list of all readers of the library fulfilling all constraints
	 * @throws DataSourceException
	 *             thrown if there is any problem with the data source
	 */
	@Override
	public synchronized List<Reader> get(List<Constraint> constraints,
			final int from, final int to, List<OrderBy> order)
			throws DataSourceException {
		if (constraints == null) {
			constraints = new ArrayList<Constraint>();
		}
		constraints.add(0, new Constraint("id", "<>",
				"-1", "AND", null));
		return persistence.getReaders(constraints, from, to, order);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#add(reader)
	 */
	@Override
	public synchronized int add(Reader reader) throws DataSourceException,
			BusinessElementAlreadyExistsException,
			NoSuchBusinessObjectExistsException {
		logger.info("add reader: " + reader);
		try {
			if (reader.hasId() && persistence.getReader(reader.getId()) != null) {
				logger.info("reader already exists and could not be added: "
						+ reader);
				throw new BusinessElementAlreadyExistsException(
						Messages.get("readerexists") + " " + Messages.get("id")
								+ " = " + reader.getId());
			}
		} catch (BusinessElementAlreadyExistsException e) {
			logger.error(e);
		}

		// if reader has no assigned user name, he/she gets a default user name
		String username = defaultUsername(reader);

		if (reader.getPassword() == null || reader.getPassword().equals("")) {
			PasswordSecurity generator = new PasswordSecurity();
			String password = generator.generatePassword(1, 1, 1, 1, 8);

			reader.setPassword(password);

		}

		Constraint c = new Constraint("username", "LIKE", username + "%",
				"AND", Constraint.AttributeType.STRING);

		List<Constraint> l = new ArrayList<Constraint>();
		l.add(c);

		List<Reader> tmp = get(l, 0, 9999999, null);
		if (tmp.size() != 0) {
			Reader r = tmp.get(tmp.size() - 1);

			String name = (r.getUsername().substring(username.length()));
			if (name.equals("")) {
				username += "1";
			} else {
				username += (Integer.parseInt(name) + 1);
			}

		}
		reader.setUsername(username);

		try {
			ReaderInfo rInfo = new ReaderInfo();
			rInfo.sendGeneratedPassword(reader);
		} catch (MessagingException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Email konnte nicht versendet werden.  \n"
									+ "Der Benutzername lautet: "
									+ reader.getUsername() + "\n "
									+ "Das Passwort lautet "
									+ reader.getPassword()));
		} catch (NamingException e) {
			logger.error(e);
		}

		reader.setPassword(hashPassword(reader));

		int result = persistence.addReader(reader);
		if (result < 0) {
			throw new DataSourceException(Messages.get("readernotadded"));
		}
		return result;
	}

	/**
	 * Creates a default username as a concatenation of the the user's first
	 * letter of his/her first name and the last name all in lower case. If a
	 * user has neither first nor last name, a random name is generated.
	 * 
	 * @param reader
	 *            reader whose user name is to be created
	 * @param Zahl
	 *            mit der Username endet.
	 * @return default user name
	 */
	private String defaultUsername(Reader reader) {
		final String firstname = reader.getFirstName();
		final String lastname = reader.getLastName();
		if (firstname != null && !firstname.isEmpty()) {
			if (lastname != null && !lastname.isEmpty()) {
				return (firstname.charAt(0) + lastname).toLowerCase();
			} else {
				return firstname.toLowerCase();
			}
		} else {
			if (lastname != null && !lastname.isEmpty()) {
				return lastname.toLowerCase();
			} else {
				// neither first nor last name given
				return Messages.get("user")
						+ ((Integer) (int) Math.abs(Math.random() * 1000 + 1))
								.toString();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessObjectHandler#update(int,
	 * java.lang.Object)
	 */
	@Override
	public synchronized int update(Reader newValue) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		return persistence.updateReader(newValue);
	}

	/**
	 * Deletes all elements in the data source.
	 * 
	 * @param elements
	 *            the elements to be deleted
	 * @throws DataSourceException
	 */
	@Override
	public synchronized void delete(List<Reader> elements)
			throws DataSourceException {
		for (Reader reader : elements) {
			logger.info("reader deleted: " + reader.getUsername());
			persistence.deleteReader(reader);
		}
	}

	/**
	 * The number of elements fulfilling given constraints.
	 * 
	 * @param constraints
	 *            the set of constraints to be fulfilled to be counted
	 * @return number of elements fulfilling given constraints.
	 * @throws DataSourceException
	 */
	@Override
	public synchronized int getNumber(List<Constraint> constraints)
			throws DataSourceException {
		logger.info("ajbiuvasbviuboius<bviuabiuvb");
		if (constraints == null) {
			constraints = new ArrayList<Constraint>();
		}
		constraints.add(0, new Constraint("id", "<>",
				"-1", "AND", null));
		return persistence.getNumberOfReaders(constraints);
	}

	/**
	 * Returns the element with the given id.
	 * 
	 * @param id
	 *            ID of the element to be retrieved
	 * @return element with the given id
	 * @throws DataSourceException
	 */
	@Override
	public synchronized Reader get(int id) throws DataSourceException {
		return persistence.getReader(id);
	}

	/**
	 * An instance of Reader acting as the prototype of objects handled by this
	 * handler.
	 */
	private static Reader prototype = new Reader();

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.businesslogic.BusinessHandler#getPrototype()
	 */
	@Override
	public synchronized Reader getPrototype() {
		return prototype;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * swp.bibjsf.businesslogic.BusinessObjectHandler#export(java.io.OutputStream
	 * )
	 */
	@Override
	public synchronized void exportCSV(OutputStream outStream)
			throws DataSourceException {
		persistence.exportReaders(outStream);
	}

	/**
	 * Reads all CSV data from input and stores them into the reader table.
	 * 
	 * @param input
	 *            the stream from where to read the CSV data
	 * @throws InputException
	 *             thrown in case the input data cannot be processed completely
	 * @throws DataSourceException
	 *             thrown if the data cannot be stored in the data source
	 * @return the number of read and inserted readers
	 */
	@Override
	public synchronized int importCSV(InputStream input)
			throws DataSourceException {
		return persistence.importReaders(input);
	}

	/**
	 * Returns an MD5 hash of reader's password.
	 * 
	 * @param reader
	 *            reader whose password is to be hashed
	 * @return MD5 hash of reader's password
	 * @throws NoSuchAlgorithmException
	 *             thrown if there is no MD5 algorithm
	 */
	public String hashPassword(final Reader reader) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			String readerPassword = reader.getPassword();
			if (readerPassword == null) {
				readerPassword = "";
			}
			byte[] bpassword;
			String pw = "";
			try {
				bpassword = md.digest(readerPassword.getBytes("UTF-8"));
				StringBuffer password = new StringBuffer();
				for (int i = 0; i < bpassword.length; i++) {
					password.append(Integer.toString(
							(bpassword[i] & 0xff) + 0x100, 16).substring(1));
				}
				pw = password.toString();
			} catch (UnsupportedEncodingException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
								"Das Password kann nicht kodiert werden."));
			}
			return pw;
		} catch (NoSuchAlgorithmException e2) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Der Hash-Algorithmus existiert nicht."));
		}
		return "error";
	}

	public void updateLastUse(int readerid) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		Reader r = get(readerid);
		r.setLastUse(new Date());
		if (r.getStatus().equals("inactive"))
			r.setStatus("active");
		update(r);
	}
}
