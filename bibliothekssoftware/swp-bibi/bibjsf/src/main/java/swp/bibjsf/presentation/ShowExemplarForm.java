package swp.bibjsf.presentation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.NotImplementedException;

import swp.bibcommon.Exemplar;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;

/**
 * ManagedBean zur Anzeige von Exemplaren.
 * 
 * @author Bernd Poppinga
 * 
 */
@ManagedBean
@SessionScoped
public class ShowExemplarForm extends ExemplarForm {

	/**
	 * Unique serial number.
	 */
	private static final long serialVersionUID = -5486861182834276525L;

	/**
	 * Setzt das Exemplar mithilfe der Exemplar ID
	 * 
	 * @author Bernd Poppinga
	 * @param ID
	 *            des Exemplars
	 * @return Navigationsfall für Faces
	 */
	public String setExemplarByID(int id) {
		try {
			ExemplarHandler eh = ExemplarHandler.getInstance();
			element = eh.get(id);
			return "details";
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Exemplar konnte nicht geladen werden"));
			return "error";
		} catch (NoSuchBusinessObjectExistsException e1) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Exemplar existiert nicht und konnte nicht geladen werden"));
			return "error";
		}
	}

	/**
	 * Methode, die lediglich aufgrund von Vererbung besteht.
	 */
	@Override
	public String save() {
		throw new NotImplementedException();
	}

	/**
	 * Getter für das Exemplar
	 * 
	 * @return das Exemplar
	 */
	public Exemplar getExemplar() {
		return element;
	}

	/**
	 * Setter für das Exemplar.
	 * 
	 * @param exemplar
	 *            das Exemplar
	 */
	public void setExemplar(Exemplar exemplar) {
		element = exemplar;
	}
}
