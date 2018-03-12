package swp.bibjsf.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import swp.bibcommon.MediumType;
import swp.bibjsf.businesslogic.MediumTypeHandler;
import swp.bibjsf.exception.DataSourceException;

/**
 * Table für Medientypen. Unterstützt Auswählen, Sortieren, Filtern, CSV-Im- &
 * Export, Lazy Loading.
 * 
 * @author Helena Meißner
 */
@ManagedBean
@SessionScoped
public class MediumTypeTable extends TableForm<MediumType> {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = -3383117736660784373L;

	public MediumTypeTable() throws DataSourceException {
		super(MediumTypeHandler.getInstance());
		try {
			model = new TableDataModel<MediumType>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Mediumtyp können nicht angezeigt werden"));
		}
	}

	/**
	 * Gibt alle Medientypen als Liste von SelecItems zurück.
	 * 
	 * @author Bernd Poppinga
	 * @return Medientypen als Liste von SelectItems
	 */
	public List<SelectItem> getOptions() {
		List<SelectItem> options = new ArrayList<>();
		try {
			List<MediumType> types = handler.get(null, 0, Integer.MAX_VALUE,
					null);
			options.add(new SelectItem(null, ""));
			for (MediumType type : types) {
				options.add(new SelectItem(type.getId(), type.getName()));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Der Medientypen konnten nicht abgerufen werden."));
		}
		return options;
	}

	/**
	 * Gibt alle Medientypen als Liste zurück
	 * 
	 * @author Bernd Poppinga
	 * @return Medientypen als Liste
	 */
	public List<MediumType> getMediumTypes() {
		List<MediumType> types = new ArrayList<>();
		try {
			types = handler.get(null, 0, Integer.MAX_VALUE, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Der Medientypen konnten nicht abgerufen werden."));
		}
		return types;
	}

}
