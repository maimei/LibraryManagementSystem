package swp.bibjsf.presentation;

import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Property;
import swp.bibjsf.businesslogic.PropertyHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * Ändert eine existierende Properties-Einstellung.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class ChangePropertyForm extends PropertyForm {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -2631101021371125000L;

	/**
	 * Liste von Properties
	 */
	private HashMap<Integer, Property> properties = new HashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() throws NoSuchBusinessObjectExistsException {
		logger.debug("request to save new Medium "
				+ ((element == null) ? "NULL" : element.toString()));
			try {
				PropertyHandler ph = PropertyHandler.getInstance();
				for( Property property : properties.values()){
					element = property;
					if (element != null) {
						ph.update(element);
					}
				}
				FacesContext.getCurrentInstance().addMessage(null, 	new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Info", "Die Einstellungen wurden erfolgreich bearbeitet"));
				return "success2";
			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Einstellung konnte nicht bearbeitet werden"));
				return "error2";
			} catch (NoSuchBusinessObjectExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Einstellung existiert nicht und konnte nicht bearbeitet werden"));
				return "error2";
			}
		}
	
	/**
	 * Setzt die geänderte Einstellung.
	 * 
	 * @param newProperty
	 *            geänderte Einstellung
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public void edit(int id) {	
		try {
			PropertyHandler ph = PropertyHandler.getInstance();
			Property property = ph.get(id);
			properties.put(id, property);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, 
				"Fehler beim Zugriff auf die Datenbank", "Einstellung konnte nicht angezeigt werden"));
		} catch (NoSuchBusinessObjectExistsException e) {
			FacesContext.getCurrentInstance().addMessage(null, 	new FacesMessage(FacesMessage.SEVERITY_FATAL,
				"Fehler", "Einstellung existiert nicht und konnte nicht angezeigt werden"));
		}
	}
	
	/**
	 * Liefert die Property mit der übergebenen id zurück
	 * @param id
	 * 			die id der Property
	 * @return die Property
	 */
	public Property get(int id){
		return properties.get(id);
	}
}
