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

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Reader;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.exception.BusinessElementAlreadyExistsException;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.PasswordSecurity;

/**
 * AddReaderForm is a ManagedBean serving as model for reader/add.xhtml that
 * allows a user to create new readers.
 * 
 * @author koschke, Bernd Poppinga
 * 
 */
@ManagedBean
@SessionScoped
public class AddReaderForm extends ReaderForm {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = -494454216495818377L;

	public AddReaderForm() {
		super();
		element = new Reader();
	}

	/**
	 * Saves a reader.
	 * 
	 * @return "success" or "error" as a string. Is used for navigation. See
	 *         faces-config.xml.
	 */
	@Override
	public String save() {
		logger.debug("request to save reader "
				+ ((element == null) ? "NULL" : element.toString()));

		if (element != null) {
			element.setHistoryActivated(false);
			element.setStatus("active");
			element.setEntryDate(new Date());
			element.setLastUse(new Date());
			element.setReturnMail(true);
			element.setReminderMail(true);
			element.setReservationMail(true);

			if ((new AuthBackingBean().isNotRole("NEW"))) {
				if (element.getPassword() != null) {
					if (!(new PasswordSecurity()).checkPassword(element
							.getPassword())) {
						FacesContext
								.getCurrentInstance()
								.addMessage(
										null,
										new FacesMessage(
												FacesMessage.SEVERITY_FATAL,
												"Fehler",
												"Dein Password ist nicht sicher genug. Das Passwort muss mindestens 8 Zeichen lang sein und Kleinbuchstaben, Großbuchstaben, Zahlen, Sonderzeichen enthalten."));
						return "error";
					}
				}
			}
			try {
				ReaderHandler bh = ReaderHandler.getInstance();
				int id = -1;
				id = bh.add(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, element
								.getFirstName()
								+ " "
								+ element.getLastName()
								+ " wurde erfolgreich hinzugefügt", "ID = "
								+ id));
				reset();
				return "success";
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Nuzter konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (BusinessElementAlreadyExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Nutzer existiert bereits und konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			} catch (NoSuchBusinessObjectExistsException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
								"Nutzer konnte nicht hinzugefügt werden"));
				reset();
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde kein Nutzer übergeben."));
			return "error";
		}

	}

	@Override
	public String reset() {
		element = new Reader();
		return super.reset();
	}
}