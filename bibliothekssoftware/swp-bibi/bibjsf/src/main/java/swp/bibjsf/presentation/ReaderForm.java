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
import javax.faces.context.FacesContext;

import swp.bibcommon.Reader;

/**
 * ReaderForm is an abstract superclass of forms that add new or change existing
 * readers.
 * 
 * @author koschke , Niklas Bruns
 * 
 */
public abstract class ReaderForm extends BusinessObjectForm<Reader> {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = 380665113958410405L;

	/*********************************************************************************
	 * Input fields that are not attributes of the element
	 ********************************************************************************/

	/**
	 * The password needs to be repeated to make sure the user did not
	 * misspelled it (it cannot be seen). This field stores the second edit of
	 * the password. reader.getPassword() and passwordCheck must match.
	 */
	private String passwordCheck = "";

	/**
	 * @return the passwordCheck
	 */
	public String getPasswordCheck() {
		return passwordCheck;
	}

	/**
	 * @param passwordCheck
	 *            the passwordCheck to set
	 */
	public void setPasswordCheck(String passwordCheck) {
		// passwords are not trimmed
		this.passwordCheck = passwordCheck;
	}

	/* **************************************
	 * wrappers to setters/getters of element
	 * ************************************
	 */

	/**
	 * Returns the first name.
	 * 
	 * @return first name.
	 */
	public String getFirstName() {
		return element.getFirstName();
	}

	/**
	 * Sets first name.
	 * 
	 * @param firstName
	 *            new first name
	 */
	public void setFirstName(final String firstName) {
		element.setFirstName(trim(firstName));
	}

	/**
	 * Returns last name.
	 * 
	 * @return last name.
	 */
	public String getLastName() {
		return element.getLastName();
	}

	/**
	 * Sets last name.
	 * 
	 * @param lastName
	 *            new last name
	 */
	public void setLastName(final String lastName) {
		element.setLastName(trim(lastName));
	}

	/**
	 * Returns the birthday of a reader.
	 * 
	 * @return birthday of reader
	 */
	public Date getBirthday() {
		return element.getBirthday();
	}

	/**
	 * Sets birthday of a reader.
	 * 
	 * @param birthday
	 *            new birthday
	 */
	public void setBirthday(Date birthday) {
		element.setBirthday(birthday);
	}

	public String getUserName() {
		return element.getUsername();
	}

	public void setUserName(String userName) {
		element.setUsername(trim(userName));
	}

	public String getPassword() {
		return element.getPassword();
	}

	public void setPassword(String password) {
		// passwords are not trimmed
		element.setPassword(password);
	}

	public String getEmail() {
		return element.getEmail();
	}

	public void setEmail(String email) {
		element.setEmail(trim(email));
	}

	public String getStreet() {
		return element.getStreet();
	}

	public void setStreet(String street) {
		element.setStreet(trim(street));
	}

	public String getZipcode() {
		return element.getZipcode();
	}

	public void setZipcode(String zipcode) {
		element.setZipcode(trim(zipcode));
	}

	public String getCity() {
		return element.getCity();
	}

	public void setCity(String city) {
		element.setCity(trim(city));
	}

	public String getPhone() {
		return element.getPhone();
	}

	public void setPhone(String phone) {
		element.setPhone(trim(phone));
	}

	public String getNote() {
		return element.getNote();
	}

	public void setNote(String note) {
		element.setNote(note);
	}

	/**
	 * Gibt zurück, ob die Ausleihhistorie gespeichert werden soll.
	 * 
	 * @return ob die Ausleihhistorie gespeichert werden soll.
	 */
	public boolean getIsSettingsOn() {
		return element.isHistoryActivated();
	}

	/**
	 * Setter für den Wert,der angibt, ob die Ausleihhistorie gespeichert werden
	 * soll.
	 * 
	 * @param isSettingsOn
	 *            ob die Ausleihhistorie gespeichert werden soll.
	 */
	public void setIsSettingsOn(boolean isSettingsOn) {
		element.setHistoryActivated(isSettingsOn);
	}

	/**
	 * Getter für den Status.
	 * 
	 * @return Status
	 */
	public String getStatus() {
		String status = element.getStatus();
		switch (status) {
		case "active":
			return "aktiv";
		case "inactive":
			return "inaktiv";
		case "blocked":
			return "gesperrt";
		}
		FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
								"Der Status des Nutzers konnte nicht ermittelt werden"));
		return "";
	}

	/**
	 * Setter für den Status.
	 * 
	 * @param status
	 *            Status
	 */
	public void setStatus(String status) {
		switch (status) {
		case "aktiv":
			status = "active";
			break;
		case "inaktiv":
			status = "inactive";
			break;
		case "gesperrt":
			status = "blocked";
			break;
		}
		element.setStatus(status);
	}

	public Date getLastUse() {
		return element.getLastUse();
	}

	public void setLastUse(Date lastUse) {
		element.setLastUse(lastUse);
	}

	public Date getEntryDate() {
		return element.getEntryDate();
	}

	public void setEntryDate(Date entryDate) {
		element.setEntryDate(entryDate);
	}

	public String getGroupid() {
		return element.getGroupid();
	}

	public void setGroupid(String groupid) {
		element.setGroupid(groupid);
	}
	
	public boolean getReturnMailOn() {
		return element.hasReturnMail();
	}
	
	public void setReturnMailOn(boolean returnMail) {
		element.setReturnMail(returnMail);
	}
	
	public boolean getReminderMailOn() {
		return element.hasReminderMail();
	}
	
	public void setReminderMailOn(boolean reminderMail) {
		element.setReminderMail(reminderMail);
	}
	
	public boolean getReservationMailOn() {
		return element.hasReservationMail();
	}
	
	public void setReservationMailOn(boolean reservationMail) {
		element.setReservationMail(reservationMail);
	}
}
