package swp.bibjsf.presentation;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.OpeningTime;
import swp.bibjsf.businesslogic.OpeningTimeHandler;
import swp.bibjsf.exception.DataSourceException;

/**
 * Table für Öffnungszeiten. Unterstützt Auswählen, Sortieren, Filtern,
 * CSV-Im- & Export, Lazy Loading.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class OpeningTimeTable extends TableForm<OpeningTime> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 1L;

	public OpeningTimeTable() throws DataSourceException {
		super(OpeningTimeHandler.getInstance());
		try {
			model = new TableDataModel<OpeningTime>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
	    		"Fehler beim Zugriff auf die Datenbank", "Öffnungszeiten können nicht angezeigt werden"));
		}
	}
	
	/**
	 * Gibt alle Öffnungzeiten als Array von Strings zurück
	 * 
	 * @author Bernd Poppinga
	 * @return Array mit allen Öffnungzeiten als String
	 */
	public String[] getOpeningTimes(){
		String[] days = new String[7];
		try{
		OpeningTimeHandler op = OpeningTimeHandler.getInstance();
		List<OpeningTime> open = op.get(null, 0, 0, null);		
		for( int i = 0; i < 7; i++){
			days[i] = open.get(i).toString();
		}
		}catch(Exception e){
			logger.error(e);
		}
		return days;
	}



}
