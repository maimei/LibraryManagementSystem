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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.time.DateFormatUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import swp.bibcommon.Reader;
import swp.bibjsf.businesslogic.PropertyHandler;
import swp.bibjsf.businesslogic.ReaderHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.renderer.Content;
import swp.bibjsf.renderer.IDCardPrinter;
import swp.bibjsf.renderer.IDContent;
import swp.bibjsf.renderer.Printer;
import swp.bibjsf.utils.Messages;

/**
 * A table form for readers. Supports selection, sorting, filtering, file
 * upload, CSV export, and lazy loading. Editing is not supported. There will be
 * extra forms for that.
 * 
 * @author koschke, Bernd Poppinga, Helena Meißner
 * 
 */
@ManagedBean
@SessionScoped
public class ReaderTable extends TableForm<Reader> {

	private static final long serialVersionUID = -4323158046564482542L;

	public ReaderTable() throws DataSourceException {
		super(ReaderHandler.getInstance());
		try {
			model = new TableDataModel<Reader>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Nutzer können nicht angezeigt werden"));
		}
	}

	/********************************************************************
	 * PDF generation
	 *******************************************************************/

	/**
	 * setzt die Nutzergruppe aller ausgewählten User auf 'status'.
	 * 
	 * @param groupid
	 *            Nutzergruppe, die die ausgewählten User erhalten sollen.
	 */
	public String activateSelected() {
		if (selectedElements != null) {
			for (Reader reader : selectedElements) {
				reader.setGroupid("USER");
				try {
					handler.update(reader);
					return "success";
				} catch (DataSourceException e1) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler beim Zugriff auf die Datenbank",
									"Nutzer kann nicht angezeigt werden"));
					
				} catch (NoSuchBusinessObjectExistsException e2) {
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_FATAL,
											"Fehler",
											"Nutzer existiert ncht und kann nicht bearbeitet werden"));
					
				} finally {
					selectedElements.clear();
				}
			}
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Kein Nutzer ausgewählt", "Nichts zu tun."));
		return "empty";
	}

	/**
	 * Creates a PDF printout for the ID cards of all selected readers in the
	 * table.
	 * 
	 * @return a stream containing the generated PDF
	 */
	public StreamedContent getReaderPDF() {

		if (selectedElements.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, Messages
							.get("noEntriesSelected"), Messages
							.get("nothingToDo")));
			return null;
		}
		List<Content> idcontent = new ArrayList<Content>();
		{
			for (Reader r : selectedElements) {
				idcontent.add(new IDContent(r.getFirstName(), r.getLastName(),
						((Integer) r.getId()).toString()));
			}
		}
		Printer printer = new IDCardPrinter();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		printer.printCards(idcontent, outStream);
		try {
			outStream.close();
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages
							.get("pdfFailure"), e.getLocalizedMessage()));
		}
		// Copy outStream data to inStream.
		// This is the easiest approach if your data easily fit into memory.
		// It is not recommended this approach when the data is more than 100Mb
		// (because it's not the only one object on the heap and if
		// there are many simultaneous users of our web application, it can
		// easily eat up all your Java Virtual Machine heap). Yet, we expect
		// the data to be rather limited.
		byte[] data = outStream.toByteArray();
		ByteArrayInputStream inStream = new ByteArrayInputStream(data);

		return new DefaultStreamedContent(inStream, "application/pdf",
				"data.pdf");
	}

	/**
	 * Errechnet das Datum ab dem Leser zur Löschung vorgeschlagen werden
	 * 
	 * @author Bernd Poppinga
	 * @return Datum ab dem Leser zur Löschung vorgeschlagen werden
	 */
	public String getDeletable() {
		Calendar cal = Calendar.getInstance();
		try {
			int i = Integer.parseInt(PropertyHandler.getInstance().get(2)
					.getValue());
			cal.add(Calendar.YEAR, -i);			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
							"Datum konnte nicht berechnet werden."));
			
		}
		logger.info(DateFormatUtils.format(cal,"yyyy-MM-dd"));
		return DateFormatUtils.format(cal,"yyyy-MM-dd");
	}

}