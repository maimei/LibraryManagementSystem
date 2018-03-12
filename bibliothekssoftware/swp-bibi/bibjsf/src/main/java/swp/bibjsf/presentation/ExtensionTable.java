package swp.bibjsf.presentation;

import java.util.ArrayList;
import java.util.List;

import swp.bibcommon.Extension;

import swp.bibjsf.businesslogic.ExtensionHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.utils.Constraint;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * A table form for extensions. Supports selection, sorting, filtering, file
 * upload, CSV export, and lazy loading. Editing is not supported.
 * 
 * @author Bernd Poppinga, Helena Meißner
 */
@ManagedBean
@SessionScoped
public class ExtensionTable extends TableForm<Extension> {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 1L;

	public ExtensionTable() throws DataSourceException {
		super(ExtensionHandler.getInstance());
		try {
			model = new TableDataModel<Extension>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Verlängerungen können nicht angezeigt werden"));
		}
	}

	/**
	 * Bestimmt ob es schon einen Verlängerungswunsch des Lesers für das
	 * Exemplar gibt.
	 * 
	 * @author Bernd Poppinga
	 * @param readerid
	 *            Leser ID
	 * @param exemplarid
	 *            Exemplar ID
	 * @return ob es schon einen Verlängerungswunsch des Lesers für das Exemplar
	 *         gibt
	 */
	public boolean hasExtension(int readerid, int exemplarid) {
		try {
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("readerID", "=", "" + readerid, "AND",
					Constraint.AttributeType.INTEGER));
			cons.add(new Constraint("exemplarID", "=", "" + exemplarid, "AND",
					Constraint.AttributeType.INTEGER));
			int i = ((ExtensionHandler) handler).getNumber(cons);
			return i != 0;
		} catch (Exception e) {
								return true;
		}
	}

}