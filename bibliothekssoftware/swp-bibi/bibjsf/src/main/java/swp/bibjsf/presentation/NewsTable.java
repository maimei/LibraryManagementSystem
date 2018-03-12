package swp.bibjsf.presentation;

import swp.bibcommon.News;
import swp.bibjsf.businesslogic.NewsHandler;
import swp.bibjsf.exception.DataSourceException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * A table form for commentaries. Supports selection, sorting, filtering, file
 * upload, CSV export, and lazy loading. Editing is not supported.
 * 
 * @author Bernd Poppinga, Helena Meißner
 */
@ManagedBean
@SessionScoped
public class NewsTable extends TableForm<News> {

	public NewsTable() throws DataSourceException {
		super(NewsHandler.getInstance());
		try {
			model = new TableDataModel<News>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
	    		"Fehler beim Zugriff auf die Datenbank", "News können nicht angezeigt werden"));
		}
	}

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1L;

	


	/**
	 * Löscht die übergebene Nachricht
	 * 
	 * @author Bernd Poppinga
	 * @param news Die Nachrricht die gelöscht werden soll.
	 */
	public void delete(News news){
		try{
			List<News> list = new ArrayList<>();
			list.add(news);
			handler.delete(list);
		}catch ( Exception e){
			FacesContext
			.getCurrentInstance()
			.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler",
							"Nachricht konnte nicht gelöscht werden."));
		}
		
		
	}
}