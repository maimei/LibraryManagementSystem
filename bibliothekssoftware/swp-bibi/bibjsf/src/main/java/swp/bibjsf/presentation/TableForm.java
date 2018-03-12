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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import swp.bibcommon.BusinessObject;
import swp.bibjsf.businesslogic.BusinessObjectHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.utils.Messages;

/**
 * A table form for Elements. Supports selection, sorting, filtering, CSV
 * import/export, and lazy loading. Editing is not supported. There will be
 * extra forms for that.
 * 
 * Why is editing not supported? Editing turned out to be too difficult to get
 * right. Here is a summary of my attempts. PrimeFaces offers row editors, which
 * can be used to edit rows. They don't allow you to modify expanded rows,
 * however. You could allow modifying expanded rows by conditional rendering
 * input or output text fields. Yet, all these attempts finally failed if a user
 * is allowed to modify the identifying attribute (the ID). That ID is needed to
 * identify the database entry. If it can be changed, we need to have the ID
 * before and after the modification. We could track the old ID by saving it
 * during the OnEditInit event by storing it in a map <object reference -> ID>.
 * The problem is that the row editor seems to create copies of the row element
 * changed. This is another object and the object reference is different from
 * the original object reference stored in the map upon OnEditInit. So we cannot
 * lookup the element in the map anymore. I finally gave up here.
 * 
 * @author koschke
 * 
 */

public abstract class TableForm<Element extends BusinessObject> implements
		Serializable {

	private static final long serialVersionUID = -4323158046564482542L;

	protected static final Logger logger = Logger.getLogger(TableForm.class);

	/**
	 * The business handler that takes care of the service requests.
	 */
	protected BusinessObjectHandler<Element> handler;

	/**
    * 
    */
	private int rowMax = Integer.MAX_VALUE;

	/**
	 * Constructor.
	 * 
	 * @param handler
	 *            handling the services
	 */
	public TableForm(BusinessObjectHandler<Element> handler) {
		this.handler = handler;
	}

	/**************************************************************************
	 * Table settings
	 *************************************************************************/

	/**
	 * True if the table may be modified.
	 * 
	 * @return true if the table may be modified
	 */
	public boolean getModifiable() {
		return false;
	}

	/**************************************************************************
	 * Filtering
	 *************************************************************************/

	/**
	 * List of all selected rows.
	 */
	protected List<Element> filteredElements;

	/**
	 * Returns a list of all selected rows.
	 * 
	 * @return list of all selected rows.
	 */
	public List<Element> getFilteredElements() {
		return filteredElements;
	}

	/**
	 * Sets the list of selected rows. Called from the Facelet.
	 * 
	 * @param filteredElements
	 *            the new filtered elements.
	 */
	public void setFilteredElements(List<Element> filteredElements) {
		this.filteredElements = filteredElements;
	}

	/**************************************************************************
	 * Selection
	 *************************************************************************/

	/**
	 * All rows currently selected.
	 */
	protected List<Element> selectedElements;

	/**
	 * Returns all rows currently selected.
	 * 
	 * @return all rows currently selected or null if none are selected
	 */
	public List<Element> getSelectedElements() {
		if (selectedElements == null) {
			return null;
		} else {
			return new ArrayList<Element>(selectedElements);
		}
	}

	/**
	 * Sets the rows currently selected. Called by the Facelet.
	 * 
	 * @param selectedElements
	 *            new selected Elements
	 */
	public void setSelectedElements(List<Element> selectedElements) {
		this.selectedElements = new ArrayList<Element>(selectedElements);
	}

	/**
	 * Deletes all selected elements from the data model.
	 */
	public void deleteSelected() {
		if (selectedElements.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es sind keine Einträge ausgewählt"));
		} else {
			try {
				String objectS;
				if (selectedElements.size() > 1) {
					objectS = "Objekte";
				} else {
					objectS = "Objekt";
				}
				handler.delete(selectedElements);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								selectedElements.size() + " " + objectS
										+ " erfolgreich gelöscht"));
				// populateModel();
				selectedElements = null;
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Objekte konnten nicht gelöscht werden"));
			}
		}
	}

	/**************************************************************************
	 * Lazy loading data model.
	 *************************************************************************/

	/**
	 * The lazy loading data model.
	 */
	protected LazyDataModel<Element> model;

	/**
	 * Returns the data model.
	 * 
	 * @return the data model
	 */
	public LazyDataModel<Element> getModel() {
		return model;
	}

	/**
	 * Sets the data model. (Is this ever being called?)
	 * 
	 * @param model
	 *            new data model
	 */
	public void setModel(LazyDataModel<Element> model) {
		this.model = model;
	}

	/********************************************************************
	 * Upload / Download
	 *******************************************************************/

	/**
	 * Handles the upload request by the user to upload a CSV file of business
	 * objects to be stored in the database.
	 * 
	 * @param event
	 *            the event that gives us the uploaded file
	 */
	public void handleFileUpload(FileUploadEvent event) {
		final UploadedFile uploadedFile = event.getFile();
		String fileName = uploadedFile.getFileName();
		try {
			final int entries = handler
					.importCSV(uploadedFile.getInputstream());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					fileName + " " + Messages.get("uploadSuccess"), entries
							+ " " + Messages.get("elements"));
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, fileName
							+ " " + Messages.get("uploadFailure"), e
							.getLocalizedMessage()));
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, fileName
							+ " " + Messages.get("uploadFailure"), e
							.getLocalizedMessage()));
		}
	}

	/**
	 * Creates a CSV export file containing all business objects in the table.
	 * 
	 * @return a stream containing the generated CSV
	 */
	public StreamedContent getcSV() {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			handler.exportCSV(outStream);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, Messages
							.get("csvFailure"), e.getLocalizedMessage()));
			return null;
		}

		try {
			outStream.close();
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages
							.get("csvFailure"), e.getLocalizedMessage()));
		}

		byte[] data = outStream.toByteArray();
		ByteArrayInputStream inStream = new ByteArrayInputStream(data);
		return new DefaultStreamedContent(inStream, "application/csv",
				"Data.csv");
	}

	public int getRowMax() {
		return rowMax;
	}

	public void setRowMax(int rowMax) {
		this.rowMax = rowMax;
	}

	/**
	 * Setzt sowohl die ausgewählten Elemente, als auch die Filter zurück
	 * 
	 * @author Bernd Poppinga
	 */
	public void reset() {
		if (selectedElements != null)
			selectedElements.clear();
		((TableDataModel<Element>) model).reset();
	}

}