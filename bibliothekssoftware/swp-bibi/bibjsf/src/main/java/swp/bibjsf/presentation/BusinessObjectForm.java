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

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import swp.bibcommon.BusinessObject;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Superclass for all forms adding or changing business objects (Reader, Book).
 *
 * @author koschke
 *
 * @param <Element>
 */
public abstract class BusinessObjectForm<Element extends BusinessObject> implements Serializable {

    /**
     * Unique serial number.
     */
    private static final long serialVersionUID = -4002906520241794791L;
    /**
     * Logger of this class
     */
    protected static final Logger logger = Logger.getLogger(BusinessObjectForm.class);

    /**
     * Constructor.
     */
    public BusinessObjectForm() {
        super();
    }

    /**
     * The new reader that is to be added or the reader to be modified.
     * This form fills the data of this reader and then adds it to the database once save()
     * is executed.
     */
    protected Element element;

    /*
     * IMPORTANT NOTE FOR ALL STRING FIELDS:
     * An input string may be null if the user did not enter anything. Use trim() for
     * user inputs.
     */

    /**
     * Returns the number of character per input line.
     *
     * @return number of character per input line
     */
    public int getTextWidth() {
    	return 50;
    }

    /**
     * Remove trailing whitespace in user input.
     *
     * @param value string to be cleared
     * @return value without trailing blanks at the start and end or null if value is null
     */
    protected static String trim(String value) {
    	if (value == null) {
    		return value;
    	} else {
    		return value.trim();
    	}
    }

    /*********************************************************************************
     * actions
     ********************************************************************************/

    /**
     * Called in case of successful addition of an element.
     *
     * @param result the number of added elements for the report
     * @return "success" for navigation
     */
    protected String success(int result) {
    	String objectS;
    	if (result > 1) {
    		objectS = "Objekte";
    	} else {
    		objectS = "Objekt";
    	}
    	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Info", result + " " + objectS + " wurden erfolgreich hinzugefügt"));
        logger.debug("saved " + element);
        return "success";
    }

    /**
     * For failure of addition.
     *
     * @param e thrown exception
     * @return "error" for navigation
     */
    protected String failure(Exception e) {
        logger.debug("could not save: " + element + ": " + e.getClass().getCanonicalName() + " " + e.getMessage());
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
				"Fehler", element + " konnte nicht hinzugefügt werden"));
        return "error";
    }

    /**
     * For failure of addition.
     *
     * @param message error message to be emitted
     * @return "error" for navigation
     */
    protected String failure(final String message) {
        logger.debug("could not save: " + element + ": " + message);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
				"Fehler", element + " konnte nicht hinzugefügt werden"));
        return "error";
    }

    /**
     * Saves an element. Called when 'save' button is clicked.
     *
     * @return "success" or "error" as a string. Is used for
     *         navigation. See faces-config.xml.
     * @throws NoSuchBusinessObjectExistsException 
     */
    public abstract String save() throws NoSuchBusinessObjectExistsException;

    /**
     * Cancels addition of an element. Navigation continues with main page.
     *
     * @return returns "cancel" as string. Is used for navigation.
     * See faces-config.xml.
     */
    public String cancel() {
        logger.debug("adding element canceled");
        return "cancel";
    }

    /**
     * Resets the form to a new empty element.
     *
     * @return navigation token interpreted by faces-config.xml
     */
    public String reset() {
    	logger.debug("element reset");
        return "reset";
    }

    /**
     * Returns of the element ID.
     *
     * @return ID
     */
    public int getId() {
        return element.getId();
    }

    /**
     * Sets the ID of the element.
     *
     * @param id new element ID
     */
    public void setId(final int id) {
        element.setId(id);
    }
}