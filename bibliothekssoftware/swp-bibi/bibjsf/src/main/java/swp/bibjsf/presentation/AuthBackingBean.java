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

import javax.annotation.security.DeclareRoles;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import swp.bibcommon.Reader;
import swp.bibjsf.businesslogic.RatingHandler;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.businesslogic.ReservationHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.utils.Constraint;

/**
 * Bean to handle authentication.
 * 
 * @author D. Lüdemann, Bernd Poppinga
 * 
 */
@ManagedBean
@RequestScoped
@DeclareRoles({ "ADMIN", "USER", "BIB", "NEW" })
public class AuthBackingBean {

	private static Logger log = Logger.getLogger(AuthBackingBean.class
			.getName());

	public String logout() {
		String result = "/index?faces-redirect=true";
		// String result="index";
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		try {
			request.logout();
		} catch (ServletException e) {
			log.error("ERROR in logout: " + e.getLocalizedMessage());
			result = "/loginError?faces-redirect=true";
		}

		return result;
	}

	/**
	 * Prüft, ob ein Nutzer zu einer bestimmten Rolle gehört. Rollen könenn
	 * "NEW", "USER", "BIB" oder "ADMIN" sein.
	 * 
	 * @param role
	 *            die Rolle, die überprüft werden soll.
	 * @return true, falls der Nutzer zur übergebenen Rolle gehört, sonst false.
	 */
	public boolean isRole(String role) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		if (role.equals("NEW"))
			return request.isUserInRole("NEW") || request.isUserInRole("USER")
					|| request.isUserInRole("BIB")
					|| request.isUserInRole("ADMIN");
		else if (role.equals("USER"))
			return request.isUserInRole("USER") || request.isUserInRole("BIB")
					|| request.isUserInRole("ADMIN");

		else if (role.equals("BIB"))
			return request.isUserInRole("BIB") || request.isUserInRole("ADMIN");
		else if (role.equals("ADMIN"))
			return request.isUserInRole("ADMIN");
		else
			return false;
	}

	/**
	 * Prüft, ob ein Nutzer nicht zu einer bestimmten Rolle gehört. Rollen
	 * könenn "NEW", "USER", "BIB" oder "ADMIN" sein.
	 * 
	 * @param role
	 *            die Rolle, die überprüft werden soll.
	 * @return true, falls der Nutzer nicht zur übergebenen Rolle gehört, sonst
	 *         false.
	 */
	public boolean isNotRole(String role) {
		return !isRole(role);
	}

	/**
	 * Ermittelt den Username des aktuell eingeloggten Nutzers.
	 * 
	 * @return den Nutzernamen des Nutzers
	 */
	public String getUsername() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		try {
			return request.getUserPrincipal().getName();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Ermittelt die User ID des aktuell eingeloggten Nutzers.
	 * 
	 * @return die User ID.
	 */
	public int getUserID() {
		try {
			ReaderHandler rh = ReaderHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("username", "=", getUsername(), "AND",
					Constraint.AttributeType.STRING));
			List<Reader> readers = rh.get(cons, 0, 1, null);
			return readers.get(0).getId();
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Überprüft, ob der eingeloggte Nutzer ein Medium noch nicht bewertet hat.
	 * 
	 * @param id
	 *            ID des Mediums.
	 * @return true, falls das Medium schon bewertet wurde von dem Nutzer, sonst
	 *         false.
	 */
	public boolean hasNotRated(int id) {
		try {
			RatingHandler rh = RatingHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			int userid = getUserID();
			if (userid == -1)
				return false;
			cons.add(new Constraint("readerID", "=", "" + getUserID(), "AND",
					Constraint.AttributeType.INTEGER));
			cons.add(new Constraint("mediumID", "=", "" + id, "AND",
					Constraint.AttributeType.INTEGER));
			int i = rh.getNumber(cons);
			return (i == 0);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Überprüft, ob der eingeloggte Nutzer ein Medium vorgemerkt hat.
	 * 
	 * @param id
	 *            ID des Mediums.
	 * @return false, falls das Medium schon vorgemerkt wurde von dem Nutzer,
	 *         sonst true.
	 */
	public boolean hasReservated(int id) {
		log.info("sdfiuafiurbuivwviub");
		try {
			ReservationHandler rh = ReservationHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			int userid = getUserID();
			log.info(userid);
			if (userid == -1)
				return true;
			cons.add(new Constraint("readerID", "=", "" + getUserID(), "AND",
					Constraint.AttributeType.INTEGER));
			cons.add(new Constraint("mediumID", "=", "" + id, "AND",
					Constraint.AttributeType.INTEGER));
			int i = rh.getNumber(cons);
			log.info(i);
			return (i != 0);
		} catch (Exception e) {
			return true;
		}
	}
	
	/**
	 * Überprüft, ob der eingeloggte Nutzer ein Medium nicht vorgemerkt hat.
	 * 
	 * @param id
	 *            ID des Mediums.
	 * @return false, falls das Medium schon vorgemerkt wurde von dem Nutzer,
	 *         sonst true.
	 */
	public boolean hasNotReservated(int id) {
		return !hasReservated(id);
	}

	/**
	 * Prüft, ob der Nutzer mit der übergebenen id blockiert ist.
	 * 
	 * @param id
	 *            die ID der Nutzers.
	 * @return true, falls der Nutzer blockiert ist, sonst false.
	 */
	public boolean isBlocked(int id) {
		Reader reader = getReaderByID(id);
		if (reader == null)
			return false;
		return reader.getStatus().equals("blocked");
	}
	
	/**
	 * Prüft, ob der Nutzer mit der übergebenen id nicht blockiert ist.
	 * 
	 * @param id
	 *            die ID der Nutzers.
	 * @return true, falls der Nutzer blockiert ist, sonst false.
	 */
	public boolean isNotBlocked(int id) {
		return !isBlocked(id);
	}

	/**
	 * Ermittelt den Nutzer anhand der ID.
	 * 
	 * @param id
	 *            die ID des gewünschten Nutzers.
	 * @return der Nutzer
	 */
	public Reader getReaderByID(int id) {
		try {
			ReaderHandler rh = ReaderHandler.getInstance();
			return rh.get(id);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Nutzer konnte nicht ermittelt werden"));
			return null;
		}
	}
}