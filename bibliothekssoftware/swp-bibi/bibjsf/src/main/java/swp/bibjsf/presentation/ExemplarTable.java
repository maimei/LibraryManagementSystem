package swp.bibjsf.presentation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Medium;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.renderer.Content;
import swp.bibjsf.renderer.MediumTagContent;
import swp.bibjsf.renderer.MediumTagPrinter;
import swp.bibjsf.renderer.Printer;
import swp.bibjsf.utils.Messages;

/**
 * A table form for commentaries. Supports selection, sorting, filtering, file
 * upload, CSV export, and lazy loading. Editing is not supported.
 *
 * @author Bernd Poppinga, Helena Meißner
 */
@ManagedBean
@SessionScoped
public class ExemplarTable extends TableForm<Exemplar> {


	private static final long serialVersionUID = 5493006722201749914L;




	public ExemplarTable() throws DataSourceException {
		super(ExemplarHandler.getInstance());
		try {
			model = new TableDataModel<Exemplar>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
	    		"Fehler beim Zugriff auf die Datenbank", "Exemplare können nicht angezeigt werden"));
		}
	}




	/**
     * Creates a PDF printout for the ID cards of all selected readers in the table.
     *
     * @return a stream containing the generated PDF
     */
    public StreamedContent getExemplarPDF(Medium medium) {

        if (selectedElements.size() == 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.get("noEntriesSelected"), Messages.get("nothingToDo")));
            return null;
        }
        List<Content> idcontent = new ArrayList<Content>();
        // Wenn sich um ein Buch handelt sollen andere Attribute gedruckt werden
       /* if(medium.getMediumType() == 0){
        	for (Exemplar r : selectedElements) {
    			idcontent.add(new IDContent(medium.getAttribute0(), medium.getTitle(),
    					((Integer) r.getId()).toString()));
    		}
        }else*/{
		for (Exemplar r : selectedElements) {
			idcontent.add(new MediumTagContent(r.getId(),medium.getTitle(),r.getPlace()));
		}
        }
        Printer printer = new MediumTagPrinter();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        printer.printCards(idcontent, outStream);
        try {
            outStream.close();
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                     Messages.get("pdfFailure"),
                                     e.getLocalizedMessage()));
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

        return new DefaultStreamedContent(inStream, "application/pdf", "data.pdf");
    }


}

