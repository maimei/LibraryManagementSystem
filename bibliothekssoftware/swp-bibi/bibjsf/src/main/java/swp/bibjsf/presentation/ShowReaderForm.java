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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Reader;
import swp.bibjsf.businesslogic.ReaderHandler;

/**
 * showReaderInfo is a ManagedBean serving as model for reader/readerInfo.xhtml
 * that allows a user to modify existing readers.
 * 
 * @author Mathias Eggerichs
 * 
 */
@ManagedBean
@SessionScoped
public class ShowReaderForm extends ReaderForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 6181384137651440367L;

	/**
	 * Setzt den Leser
	 * 
	 * @param id
	 *            ID des Lesers der gesetzt werden soll
	 * @return Navigationsfall für die Faces
	 */
	public String setReaderByID(int id) {
		try {
			ReaderHandler rh = ReaderHandler.getInstance();
			element = rh.get(id);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler",
							"Nutzer konnte nicht geladen werden"));
		}
		return "details";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.ReaderForm#save()
	 * Methode, die aufgrund von Vererbung besteht, aber nicht verwendet wird.
	 */
	@Override
	public String save() {
		throw new NotImplementedException();
	}

	/**
	 * Getter für den Nutzer.
	 * @return den Nutzer
	 */
	public Reader getElement() {
		return element;
	}

	/**
	 * Setter für den Nutzer.
	 * @param reader
	 * 			der zu setzende Nutzer
	 */
	public void setReader(Reader reader) {
		element = reader;
	}
}
