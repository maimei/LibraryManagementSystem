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

package swp.bibjsf.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.naming.NamingException;

import swp.bibcommon.Lending;
import swp.bibcommon.Reader;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.PasswordSecurity;
import swp.bibjsf.utils.ReaderInfo;
import swp.bibjsf.utils.Constraint.AttributeType;

/**
 * ChangeReaderForm is a ManagedBean serving as model for reader/change.xhtml
 * that allows a user to modify existing readers.
 * 
 * @author koschke
 * 
 */
@ManagedBean
@SessionScoped
public class ChangeReaderForm extends ReaderForm {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = 376217922786376395L;

	/**
	 * alter Wert von isHistoryActivated
	 */
	boolean history;

	/**
	 * Die Email, an die bei Nachfrage ein neues Passwort gesendet werden soll.
	 */
	private String emailForPassword;

	/**
	 * Der Benutzername des Nutzers, der ein neues Passwort anfordert.
	 */
	private String userNameForPassword;

	/**
	 * Sets reader to be changed to <code>newReader</code>.
	 * 
	 * @param newReader
	 *            new reader modified by this form
	 * @return "edit" as navigation case for faces-config.xml
	 */
	public String edit(Reader newReader) {
		element = newReader;
		history = element.isHistoryActivated();
		return "edit";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.ReaderForm#save()
	 */
	@Override
	public String save() {

				try {
			// Wenn die History nicht mehr gespeichert werden soll, werden alle
			// abgelaufenden Leihen gelöscht
			if (element.isHistoryActivated() == false && history == true) {
				changeAllLendings();
			}
			
			ReaderHandler rh = ReaderHandler.getInstance();
			rh.update(element);
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Info",
									"Nutzer wurde erfolgreich bearbeitet"));
			return "success";
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Nutzer konnte nicht bearbeitet werden"));
			return "error";
		} catch (NoSuchBusinessObjectExistsException e2) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Nutzer existiert nicht und konnte nicht bearbeitet werden"));
			return "error";
		}
	}

	/**
	 * Entfernt den Leser aus seinen abgelaufenen Leihen
	 * 
	 * @author Bernd Poppinga
	 */
	private void changeAllLendings() {
		try {
			LendingHandler lh = LendingHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("readerID", "=", "" + element.getId(),
					"AND", Constraint.AttributeType.INTEGER));
			cons.add(new Constraint("paid", "=", "true", "AND",
					Constraint.AttributeType.STRING));
			List<Lending> list = lh.get(cons, 0, Integer.MAX_VALUE, null);
			for(Lending lending : list){
				lending.setReaderID(-1);
				lh.update(lending);
			}
			
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Leihen konnten nicht gelöscht werden "));
		}  catch (NoSuchBusinessObjectExistsException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler",
							"Objekt konnte nicht gefunden werden"));
		}
	}
	
	public String savePassword() {
		try {
			ReaderHandler bh = ReaderHandler.getInstance();
			PasswordSecurity pw = new PasswordSecurity();
			if	(!pw.checkPassword(element.getPassword())){
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
        				"Fehler", "Dein Password ist nicht sicher genug."));
				return "error";
			}
			
			try{
			ReaderInfo rInfo = new ReaderInfo();
			rInfo.sendNewPassword(element);
			}catch (MessagingException e) {
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
	            		"Fehler", "Das Passwort konnte nicht verschickt werden"));				
			} catch (NamingException e) {
				logger.error(e);
			}
			final String pword = bh.hashPassword(element);
			element.setPassword(pword);
			bh.update(element);
				
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
            		"Info", "Das neue Passwort wurde erfolgreich gespeichert"));
			return "success";
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
            		"Fehler beim Zugriff auf die Datenbank", "Das neue Passwort konnte nicht gespeichert werden"));
			return "error";
		} catch (NoSuchBusinessObjectExistsException e2) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
            		"Fehler", "Der Nutzer existiert nicht. Das Passwort konnte nicht gespeichert werden"));
			return "error";
		} 
	}
	
	public String editPassword(Reader reader){
		element = reader;
		element.setPassword("");
		return "edit";
	}
	
	/**
	 * Generiert ein neues Password für den Nutzer und schickt ihm eine Benachrichtigung.
	 * @param reader
	 * @return
	 */
	public String sendNewPassword() {
		Reader reader = getReaderForEmail();
		if (reader != null) {
			PasswordSecurity generator = new PasswordSecurity();
			String newPw = generator.generatePassword(1, 1, 1, 1, 8);
			reader.setPassword(newPw);
			try {
				ReaderInfo rInfo = new ReaderInfo();
				rInfo.sendNewPassword(reader);
			} catch (MessagingException e) {
				FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
					"Email konnte nicht versendet werden."));
				return "error";
			} catch (DataSourceException e2) {
				FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler beim Zugriff auf die Datenbank",
					"Email konnte nicht versendet werden."));
				return "error";
			} catch (NamingException e) {
				logger.error(e);
			}
			try {
				ReaderHandler rh = ReaderHandler.getInstance();
				final String pword = rh.hashPassword(reader);
				reader.setPassword(pword);
				rh.update(reader);
				FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
					"Dein neues Passwort wurde an deine E-Mail-Adresse gesendet."));
				return "success";
			} catch (DataSourceException e4) {
				FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler beim Zugriff auf die Datenbank",
					"Email konnte nicht versendet werden."));
				return "error";
			} catch (NoSuchBusinessObjectExistsException e5) {
				FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
					"Der Nutzer existiert nicht. Email konnte nicht versendet werden."));
				return "error";
			}
		} 
		FacesContext.getCurrentInstance()
		.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
			"Es konnte kein Nutzer mit diesen Daten gefunden werden."));
		return "error";
	}
	
	/**
	 * überprüft, ob die vom Nutzer angegebene E-Mail und der Nutzername
	 * auf einen Nutzer in der Datenbank zutriffen.
	 * @return den Nutzer, falls dieser existiert, sonst null.
	 */
	public Reader getReaderForEmail() {
		List<Reader> readers = new ArrayList<>();
		try {
			ReaderHandler rh = ReaderHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("username", "=", getUserNameForPassword(), "AND", AttributeType.STRING));
			readers = rh.get(cons, 0, 1, null);
			if (readers.size() > 0) {
				Reader reader = readers.get(0);
				if (getEmailForPassword().equals(reader.getEmail())) {
					return reader;
				}
			}
			logger.debug("liste von usern mit die daten übereinstimmen, enthält: " + readers);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler beim Zugriff auf die Datenbank",
			"Es konnte kein Nutzer mit diesen Daten ermittelt werden."));
		}
		return null;
	}

	public String getEmailForPassword() {
		return emailForPassword;
	}

	public void setEmailForPassword(String emailForPassword) {
		this.emailForPassword = emailForPassword;
	}

	public String getUserNameForPassword() {
		return userNameForPassword;
	}

	public void setUserNameForPassword(String userNameForPassword) {
		this.userNameForPassword = userNameForPassword;
	}
}