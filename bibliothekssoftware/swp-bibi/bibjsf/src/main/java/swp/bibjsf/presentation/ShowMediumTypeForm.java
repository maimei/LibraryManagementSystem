package swp.bibjsf.presentation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.MediumType;
import swp.bibjsf.businesslogic.MediumTypeHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Zeigt eine Detailansicht eines Mediumtyps.
 * 
 * @author Helena Meißner, Bernd Poppinga
 * 
 */
@ManagedBean
@SessionScoped
public class ShowMediumTypeForm extends MediumTypeForm {

	/**z
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 2377811677188408133L;
	
	/**
	 * Setzt den Mediumtyp, welcher angeguckt werden soll.
	 * 
	 * @param id ID des Typs
	 * @return war der Vorgang erfolgreich
	 */
	public String setMediumTypeShow(int id) {
		try{
	    	MediumTypeHandler mth = MediumTypeHandler.getInstance();
	        element = mth.get(id);
	        return "show";
	        }
	        catch(DataSourceException e){
	        	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
	    	    		"Fehler beim Zugriff auf die Datenbank", "Mediumtyp kann nicht gesetzt werden"));
	        	return "error";
	        }
	        catch( NoSuchBusinessObjectExistsException e1){
	        	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,
	    	    		"Fehler", "Es existiert kein solcher Mediumtyp."));
	        	return "error";
	        }
	}
	
	/**
	 * Setzt den Medientyp.
	 * @param type
	 * 			der zu setzende Typ.
	 * @return den Erfolg der Aktion für die Faces-Navigation.
	 */
	public String setMediumType(MediumType type){	
		element = type;	
		return "add";
	}
	
	/**
	 * Gibt den angeguckten Typ zurück.
	 * 
	 * @return der Typ
	 */
	public MediumType getMediumType() {   
		logger.info("getMediumType");
		return element;
	}

	/* (non-Javadoc)
     * @see swp.bibjsf.presentation.BusinessObjectForm#save()
     * Methode existiert nur aufgrund von Vererbung, wird jedoch nicht verwendet.
     */
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		throw new NotImplementedException();
	}
}
