package swp.bibjsf.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Extension;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.Reader;
import swp.bibcommon.Reservation;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * ReaderInfo ist für die Benachrichtiung der Lesers durch E-Mails zuständig.
 * 
 * @author Hauke Olf, Niklas Bruns
 * 
 */

public class ReaderInfo {

	private InitialContext ctx;

	/**
	 * lokales Datumsformat
	 */
	DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * Session Klasse für E-Mail versand.
	 */

	private Session emailSession = null;

	/**
	 * Instanze des LendingHandlers
	 */
	LendingHandler lHandler = null;

	/**
	 * Instanze des ReaderHandlers
	 */
	ReaderHandler rHandler = null;

	/**
	 * Instanze des MediumHandlers
	 */
	MediumHandler mHandler = null;

	/**
	 * Instanze des ExemplarHandlers
	 */
	ExemplarHandler eHandler = null;

	/**
	 * Konstruktor der Klasse ReaderInfo
	 * 
	 * @throws DataSourceException
	 * @throws NamingException 
	 */
	public ReaderInfo() throws DataSourceException, NamingException {

		ctx = new InitialContext();
		emailSession = (Session) ctx.lookup("bibMail");

		lHandler = LendingHandler.getInstance();
		rHandler = ReaderHandler.getInstance();
		mHandler = MediumHandler.getInstance();
		eHandler = ExemplarHandler.getInstance();

	}

	/**
	 * Funktion dient dazu eine E-Mail zu versenden.
	 * 
	 * @param empfaenger
	 *            E-Mail adresse des Empfängers
	 * @param titel
	 *            Titel der zu versendenden E-Mail
	 * @param nachricht
	 *            Nachricht die dem Emfänger gesendet werden soll.
	 * @throws MessagingException
	 *             wird beim Fehler geworfen
	 */
	public final void sendEmail(final String empfaenger, final String titel,
			final String nachricht) throws MessagingException {
		MimeMessage message = new MimeMessage(emailSession);
		try {

			message.setFrom(new InternetAddress(emailSession
					.getProperty("mail.from")));
			System.out.println(message.getFrom());
			InternetAddress[] address = { new InternetAddress(empfaenger) };
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject(titel);
			message.setSentDate(new Date());
			message.setText(nachricht);

			Transport.send(message);
		} catch (Exception ex) {
			throw new MessagingException(ex.getMessage());
		}
	}

	/**
	 * Übersendet die Information, dass das Rückgabedatum sich verändert hat.
	 * 
	 * @param title
	 *            Titel
	 * @param Lending
	 *            lending Ausleihe die es betrifft
	 * @throws DataSourceException
	 * @throws MessagingException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public final void sendChangeTill(final Lending lending)
			throws DataSourceException, MessagingException,
			NoSuchBusinessObjectExistsException {
		Reader reader = rHandler.get(lending.getReaderID());
		Exemplar exemplar = eHandler.get(lending.getExemplarID());
		Medium medium = mHandler.get(exemplar.getMediumID());
	
		
		String nachricht = "Hallo " + reader.getFirstName() + ",\n"
				+ "Die Rückgabe des Mediums " + medium.getTitle()
				+ " hat sich verändert.\n" + "Das neue Datum ist: "
				+ df.format(lending.getTill()) + "\n";

		sendEmail(reader.getEmail(), "Rückgabe-Information", nachricht);
	}

	/**
	 * Übersendet die Information, dass eine Ausleihe heute beendet ist.
	 * 
	 * @param title
	 *            Titel
	 * @param Lending
	 *            lending Ausleihe die es betrifft
	 * @throws DataSourceException
	 * @throws MessagingException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public final void sendLendingInfo(final Lending lending)
			throws DataSourceException, MessagingException,
			NoSuchBusinessObjectExistsException {
		Reader reader = rHandler.get(lending.getReaderID());
		if (reader.hasReturnMail()) {
			Exemplar exemplar = eHandler.get(lending.getExemplarID());
			Medium medium = mHandler.get(exemplar.getMediumID());
			String nachricht = "Hallo " + reader.getFirstName() + ",\n"
					+ "Die Rückgabe des Mediums " + medium.getTitle()
					+ " ist heute fällig.\n";

			sendEmail(reader.getEmail(), "Rückgabe-Information", nachricht);
		}
	}

	/**
	 * Übersendet die Informationen über die angefallenen Gebühren.
	 * 
	 * @param email
	 *            Email-Adresse des Lesers
	 * @param lending
	 *            betroffene Ausleihe
	 * @throws DataSourceException
	 * @throws MessagingException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public final void sendFeesInfo(final Lending lending)
			throws DataSourceException, MessagingException,
			NoSuchBusinessObjectExistsException {
		Reader reader = rHandler.get(lending.getReaderID());
		
          
		if (reader.hasReminderMail()) {
			Exemplar exemplar = eHandler.get(lending.getExemplarID());
			Medium medium = mHandler.get(exemplar.getMediumID());		
			String nachricht = "Hallo " + reader.getFirstName() + ",\n"
					+ "Du hast folgenes Medium ausgeliehen: "
					+ medium.getTitle() + ".\n" + "Die Rückgabe ist seit dem "
					+ df.format(lending.getTill()) + " überfällig.\n"
					+ "Die Gebühren betragen daher "
					+ lHandler.calcFee(lending) + " €.\n";
			sendEmail(reader.getEmail(), "Gebüren-Information", nachricht);
		}
	}

	/**
	 * Übersendet die Informationen, dass ein vorgemerktes Medium verfügbar ist.
	 * 
	 * @param reservation
	 *            die betreffende Vormerkung
	 * @throws DataSourceException
	 * @throws MessagingException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public final void sendReservationInfo(final Reservation reservation)
			throws DataSourceException, MessagingException,
			NoSuchBusinessObjectExistsException {
		Reader reader = rHandler.get(reservation.getReaderID());
		if (reader.hasReservationMail()) {
			Medium medium = mHandler
					.get(reservation.getMediumID());
			String nachricht = "Hallo " + reader.getFirstName() + ",\n"
					+ "Du hast das Medium " + medium.getTitle()
					+ " vorgemerkt. "
					+ "Dieses ist ab heute wieder in unserer Bibliothek verfügbar.";
			sendEmail(reader.getEmail(), "Reservierungs-Information", nachricht);
		}
	}

	/**
	 * Übersendet die Informationen, dass ein Verlängerungswunsch vom
	 * Bibliothekar erfolgreich genehmigt wurde.
	 * 
	 * @throws DataSourceException
	 * @throws MessagingException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public final void sendExtensionInfo(final String title,
			final Extension extension) throws DataSourceException,
			MessagingException, NoSuchBusinessObjectExistsException {
		Reader reader = rHandler.get(extension.getReaderID());
		Medium medium = mHandler.get(getMediumID(extension.getExemplarID()));
		

		String nachricht = "Hallo " + reader.getFirstName() + ",\n"
				+ "Dein Verlängerungswunsch für " + medium.getTitle()
				+ "wurde erfolgreich gewährt. " + "Das Medium wurde bis zum "
				+ df.format(extension.getExtensionDate()) + " verlängert.\n";

		sendEmail(reader.getEmail(), "Verlängerungs-Information", nachricht);
	}

	/**
	 * Übersendet das vom System automatisch generierte Passwort an den neu
	 * registrierten Leser.
	 * 
	 * @throws DataSourceException
	 * @throws MessagingException
	 * @throws NoSuchBusinessObjectExistsException
	 */
	public final void sendGeneratedPassword(final Reader reader)
			throws DataSourceException, MessagingException {

		String nachricht = "Hallo "
				+ reader.getFirstName()
				+ ",\n"
				+ "Deine Registrierung als Leser bei der Schulbibliothek Rockwinkel war erfolgreich. "
				+ "Deine Anmeldedaten: \n" + "Username: "
				+ reader.getUsername() + "\n" + "Password: "
				+ reader.getPassword() + "\n"
				+ "Du kannst dich ab sofort in unserem System anmelden.";
		sendEmail(
				reader.getEmail(),
				"Registrierung Schulbibliothek Rockwinkel: Leser-Registrierung",
				nachricht);

	}

	/**
	 * Verschickt nach Änderung des Passworts eine Nachricht an den Nutzer.
	 * 
	 * @param reader
	 *            der Nutzer, der sein Passwort geändert hat.
	 * @throws MessagingException
	 */
	public final void sendNewPassword(final Reader reader)
			throws MessagingException {
		String message = "Hallo "
				+ reader.getFirstName()
				+ ",\n"
				+ "Die Änderung deines Passworts war erfolgreich.\n"
				+ "Dieses lautet ab sofort: "
				+ reader.getPassword()
				+ "\n"
				+ "Du kannst dich nun mit deinem neuen Passwort in unserem System anmelden.";
		if (reader.getEmail() != null) {
			sendEmail(reader.getEmail(),
					"Neues Passwort Schulbibliothek Rockwinkel", message);
		}
	}

	private int getMediumID(final int exemplarID) throws DataSourceException,
			NoSuchBusinessObjectExistsException {
		Exemplar e = eHandler.get(exemplarID);
		return e.getMediumID();
	}
}
