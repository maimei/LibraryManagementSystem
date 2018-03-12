package swp.bibjsf.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Extension;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.MediumType;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.ExtensionHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.businesslogic.MediumTypeHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;

/**
 * Ändert eine existierende Ausleihe
 * 
 * @author Bernd Poppinga, Niklas Bruns
 */
@ManagedBean
@SessionScoped
public class ChangeLendingForm extends LendingForm {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 6558447953420364011L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.BusinessObjectForm#save()
	 */
	@Override
	public String save() {
		logger.debug("request to save new Lending "
				+ ((element == null) ? "NULL" : element.toString()));
		if (element != null) {
			try {
				LendingHandler lh = LendingHandler.getInstance();
				if (!checkLending()) {
					return "error";
				}
				lh.update(element);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
								"Ausleihe wurde erfolgreich bearbeitet"));
				return "success";

			} catch (DataSourceException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Fehler beim Zugriff auf die Datenbank",
								"Ausleihe konnte nicht bearbeitet werden"));
				return "error";
			} catch (NoSuchBusinessObjectExistsException e2) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL,
										"Fehler",
										"Ausleihe existiert nicht und konnte nicht bearbeitet werden"));
				return "error";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Achtung",
							"Es wurde keine Ausleihe übergeben."));
			return "error";
		}
	}

	/**
	 * Setzt die geänderte Ausleihe
	 * 
	 * @param newLending
	 *            geänderte Ausleihe
	 * @return "edit" als Navigationsfall für faces-config.xml
	 */
	public String edit(Lending newLending) {
		element = newLending;
		return "edit";
	}

	/**
	 * Verlängert eine Ausleihe indem das Enddatum nach hinten verschoben wird.
	 * Der Wert der Verlängerung wird über das Exemplar ermittelt.
	 * 
	 * @author Bernd Poppinga
	 */
	public void setExtension() {
		try {
			ExemplarHandler eh = ExemplarHandler.getInstance();
			Exemplar exemplar = eh.get(element.getExemplarID());
			if (exemplar == null) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_WARN,
										"Fehler",
										"Es konnte kein Exemplar mit passender ID gefunden werden."));
			}
			element.setTill(getExtensionEnd(exemplar.getMediumID()));
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank", ""));
		} catch (NoSuchBusinessObjectExistsException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Es konnte kein Exemplar mit passender ID gefunden werden."));

		}

	}

	/**
	 * Setzt die Ausleihe, welche zur zuvor mithilfe des Verlängerungswunsches
	 * ermittelt worden ist.
	 * 
	 * @param extension
	 *            der zur Ausleihe gehörende Verlängerungswunsch
	 * @return Navigationsfall für die Faces
	 */
	public String getByExtension(Extension extension) {
		try {
			int id = extension.getExemplarID();
			LendingHandler lh = LendingHandler.getInstance();
			List<Constraint> cons = new ArrayList<>();
			cons.add(new Constraint("exemplarID", "=", "" + id, "AND",
					Constraint.AttributeType.INTEGER));
			List<Lending> list = lh.get(cons, 0, 1, null);
			element = list.get(0);
			ExtensionHandler eh = ExtensionHandler.getInstance();
			List<Extension> list2 = new ArrayList<Extension>();
			list2.add(extension);
			eh.delete(list2);
			return "extend";

		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank", ""));
			return "error";
		}
	}

	/**
	 * Verlängert die Ausleihe um die Standardausleihzeit, wenn Verlängerungen noch
	 * möglich sind. Sollte dies nicht der Fall sein, so wird eine
	 * Verlängerungswunsch gesetzt.
	 * 
	 * @author Bernd Poppinga
	 * @param Die
	 *            zu verlängernde Ausleihe
	 * @return Ob die Verlängerung erfolgreich war.
	 */
	public String extend(Lending lending) {
		element = lending;
		try {
			ExemplarHandler eh = ExemplarHandler.getInstance();
			MediumHandler mh = MediumHandler.getInstance();
			MediumTypeHandler mth = MediumTypeHandler.getInstance();
			Exemplar exemplar = eh.get(element.getExemplarID());
			Medium medium = mh.get(exemplar.getMediumID());
			MediumType type = mth.get(medium.getMediumType());
			if (element.getExtensions() < type.getExtensions()) {
				element.setExtensions(element.getExtensions() + 1);
				element.setTill(getNextOpenDay(new Date(new Date().getTime()
						+ (1000 * 60 * 60 * 24 * type.getExtensionTime()))));
				return save();
			} else {
				AddExtensionForm extension = new AddExtensionForm();
				extension.setExtensionDate(new Date());
				extension.setReaderID(element.getReaderID());
				extension.setExemplarID(element.getExemplarID());
				return extension.save();
			}
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler beim Zugriff auf die Datenbank",
									"Verlängerungswunsch konnte nicht hinzugefügt werden."));
			return "error";
		} catch (NoSuchBusinessObjectExistsException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Objekt konnte nicht gefunden werden."));
			return "error";
		}
	}
}