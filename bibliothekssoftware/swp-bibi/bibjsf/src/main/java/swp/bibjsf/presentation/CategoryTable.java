package swp.bibjsf.presentation;

import swp.bibcommon.Category;
import swp.bibjsf.businesslogic.CategoryHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import javax.faces.model.*;
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
public class CategoryTable extends TableForm<Category> {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = -4778503984906218489L;

	public CategoryTable() throws DataSourceException {
		super(CategoryHandler.getInstance());
		try {
			model = new TableDataModel<Category>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
	    		"Fehler beim Zugriff auf die Datenbank", "Kategorien können nicht angezeigt werden"));
		}
	}

	
	
	
	/**
	 * Holt alle Kategorien aus der DB und gibt sie als SelectItem zurück
	 * 
	 * @author Bernd Poppinga
	 * @return Array mit den Kategorien
	 */
	public SelectItem[] getCategorysAsSelectItem() {
		SelectItem[] options = null;
		try {
	    	List<Category> categories = handler.get(null, 0, Integer.MAX_VALUE , null );
	    	options = new SelectItem[categories.size()];
	    	for (int i = 0; i < options.length; i++){
	    		options[i] = new SelectItem(categories.get(i).getName());
	    	}
	    	
	    }
	    catch (DataSourceException e) {
	    	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
		    		"Fehler beim Zugriff auf die Datenbank", "Kategorien können nicht angezeigt werden"));
	    }
		catch (NoSuchBusinessObjectExistsException f) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
		    		"Fehler", "Es existieren keine solchen Kategorien"));
	    }
		return options;
	}
}