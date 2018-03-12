package swp.bibjsf.presentation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibcommon.Exemplar;
import swp.bibcommon.Lending;
import swp.bibcommon.Medium;
import swp.bibcommon.MediumType;
import swp.bibjsf.businesslogic.ExemplarHandler;
import swp.bibjsf.businesslogic.LendingHandler;
import swp.bibjsf.businesslogic.MediumHandler;
import swp.bibjsf.businesslogic.Statistics;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.renderer.Content;
import swp.bibjsf.renderer.MediumTagContent;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.OrderBy;

/**
 * A table form for mediums. Supports selection, sorting, filtering, CSV import
 * and export, and lazy loading.
 * 
 * @author Bernd Poppinga, Helena Meißner
 * 
 */
@ManagedBean
@SessionScoped
public class MediumTable extends TableForm<Medium> {

	/**
	 * einzigartige Serialisierungs-ID
	 */
	private static final long serialVersionUID = 5523203113559964161L;

	private boolean popularity;

	private int number;

	private List<Medium> mediumList;

	public MediumTable() throws DataSourceException {
		super(MediumHandler.getInstance());
		popularity = true;
		number = 0;
		mediumList = new ArrayList<>();
		try {
			model = new TableDataModel<Medium>(handler);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Medien können nicht angezeigt werden"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see swp.bibjsf.presentation.TableForm#getContent()
	 */

	protected List<Content> getContent() {
		List<Content> idcontent = new ArrayList<Content>();
		for (Medium m : selectedElements) {
			idcontent.add(new MediumTagContent(m.getId(), m.getTitle(),
					((Integer) m.getId()).toString()));
		}
		return idcontent;
	}

	/**
	 * Liefert alle verfügbaren Exemplare zu einem Medium zurück.
	 * 
	 * @param mediumID
	 *            die Id des Mediums, zu dem die Exemplare gehören. Wird -1
	 *            zurückgeliefert, konnte nicht auf die Datenbank zugegriffen
	 *            werden.
	 * @return Anzahl der verfügbaren Exemplare.
	 */
	public int getAvailable(int mediumID) {
		try {
			return ((MediumHandler) handler).getAllAvailableExemplars(mediumID);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Exemplare können nicht angezeigt werden"));
			return 0;
		}
	}

	/**
	 * Prüft, ob das Medium mit der übergebenen ID verfügbar ist.
	 * 
	 * @param mediumID
	 *            die Id des Mediums.
	 * @return true, falls das Medium ausleihbar, sonst false.
	 */
	public boolean isAvailable(int mediumID) {
		return getAvailable(mediumID) != 0;
	}

	/**
	 * Liefert die x best (popularity=true) bzw. schlechtst (popularity=false)
	 * bewertesten Medien. x hat dabei den Wert von number.
	 * 
	 * @author Helena Meißner
	 */
	public void getRatedMedia() {
		try {
			Statistics stats = Statistics.getInstance();
			mediumList = stats.getRatedMedia(number, popularity);
		} catch (IllegalArgumentException e2) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler",
							"Die gewünschte Anzahl an Medien ist ungültig"));
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler beim Zugriff auf die Datenbank",
									"Die gewünschten Medien konnten nicht angezeigt werden"));
		}
	}

	/**
	 * Gibt die number Medien zurück, die am häufigsten bzw. am seltensten
	 * ausgeliehen werden.
	 * 
	 * @author Helena Meißner
	 */
	public void getLentMedia() {
		try {
			Statistics stats = Statistics.getInstance();
			mediumList = stats.getLentMedia(number, popularity);
		} catch (IllegalArgumentException e2) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler",
							"Die gewünschte Anzahl an Medien ist ungültig"));
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler beim Zugriff auf die Datenbank",
									"Die gewünschten Medien konnten nicht angezeigt werden"));
		}
	}

	/**
	 * Getter für die Popularität.
	 * 
	 * @return den Wert von popularity.
	 */
	public boolean getPopularity() {
		return this.popularity;
	}

	/**
	 * Setter für die Popularität.
	 * 
	 * @param popularity
	 *            setzt die Popularität.
	 */
	public void setPopularity(boolean popularity) {
		this.popularity = popularity;
	}

	/**
	 * Getter für die maximal Zahl an Werten für die Statistik.
	 * 
	 * @return die maximale Zahl
	 */
	public int getNumber() {
		return this.number;
	}

	/**
	 * Setter für die maximale Zahl an Werten für die Statistik.
	 * 
	 * @param number
	 *            der maximale Wert
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Getter für die Liste von Medien, die für die Statistik geladen werden.
	 * 
	 * @return die Liste von Medien
	 */
	public List<Medium> getMediumList() {
		return mediumList;
	}

	/**
	 * Setter für die Liste von Medien, die für die Statistik geladen werden.
	 * 
	 * @param mediumList
	 *            die Liste von Medien
	 */
	public void setMediumList(List<Medium> mediumList) {
		this.mediumList = mediumList;
	}

	/**
	 * Leert die Liste aktueller geladener Medien in der Statistik.
	 */
	public void resetMediumList() {
		mediumList = new ArrayList<>();
	}

	/**
	 * Liefert die Anzahl an Ausleihen zu einem Medium zurück.
	 * 
	 * @author Helena Meißner
	 * @param medium
	 *            das Medium, dessen Ausleihanzah bestimmt werden soll
	 * @return die Anzahl an Ausleihen
	 */
	public int getLendingCountForMedium(Medium medium) {
		try {
			Statistics stats = Statistics.getInstance();
			return stats.getLendingCountForMedium(medium);
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler beim Zugriff auf die Datenbank",
									"Die Anzahl an Ausleihen kann nicht ermittelt werden."));
			return -1;
		}
	}

	/**
	 * Gibt den Type eines Mediums zurück.
	 * 
	 * @author Bernd Poppinga
	 * @param medium
	 *            Das Medium dessen Typ man haben möchte.
	 * @return der Medientyp
	 */
	public String getType(Medium medium) {
		try {
			MediumType type = ((MediumHandler) handler).getType(medium.getId());
			return type.getName();
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Fehler beim Zugriff auf die Datenbank",
							"Der Medientyp konnte nicht ermittelt werden."));
			return null;
		}
	}

	/**
	 * Gibt an, ob oder ab wann ein Medium verfügbar ist.
	 * 
	 * @param medium
	 *            das Medium, dessen Verfügbarkeit ermittelt werden soll
	 * @return den Zeitpunkt der Verfügbarkeit
	 */
	public String getAvailability(Medium medium) {
		try {
			ExemplarHandler eh = ExemplarHandler.getInstance();
			List<Constraint> cons = new ArrayList<Constraint>();
			cons.add(new Constraint("mediumID", "=", "" + medium.getId(),
					"AND", null));
			List<Exemplar> exemplars = eh.get(cons, 0, Integer.MAX_VALUE, null);
			if (exemplars.size() == 0) {
				return "nicht verfügbar";
			}
			cons.clear();
			for (Exemplar exemplar : exemplars) {
				switch (exemplar.getStatus()) {
				case "leihbar":
					return "ab sofort";
				case "blockiert":
					exemplars.remove(exemplar);
					break;
				case "verliehen":
					cons.add(new Constraint("exemplarID", "=", ""
							+ exemplar.getId(), "OR", null));
					break;
				}
			}
			if (exemplars.isEmpty()) {
				return "blockiert";
			} else {
				LendingHandler lh = LendingHandler.getInstance();
				List<OrderBy> order = new ArrayList<>();
				order.add(new OrderBy("till"));
				List<Lending> lendings = lh.get(cons, 0, 1, order);
				if (lendings.isEmpty()) {
					return "nicht verfügbar";
				} else {
					SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyy");
					return "verfügbar ab "
							+ date.format(lendings.get(0).getTill());
				}
			}
		} catch (Exception e) {
			return "nicht verfügbar";
		}
	}
}