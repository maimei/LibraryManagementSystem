package swp.bibjsf.presentation;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import swp.bibjsf.utils.JournalEntry;
import swp.bibjsf.utils.LogFilter;

/**
 * JournalForm implementiert die notwendigen ManagingBean-Elemente für das
 * Verlaufsfeature der journal.xhtml
 *
 * @author haukeolf
 *
 */

@ManagedBean
@SessionScoped
public class JournalForm implements Serializable {

	/**
	 * Globale Variablen
	 */
	private static final long serialVersionUID = 1L;

	// private final static List<String> VALID_COLUMN_KEYS =
	// Arrays.asList("datum", "aktion", "uhrzeit", "zusatzinfo");

	// private String columnTemplate = "datum uhrzeit aktion zusatzinfo";

	private final String logPath = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + ".."
			+ System.getProperty("file.separator") + "logs"
			+ System.getProperty("file.separator");

	private List<JournalEntry> filteredJournalEntries;

	private LogFilter logFilter;

	private List<JournalEntry> generatedJournal;

	/**
	 * Konstruktor ruft die entsprechenden Log-Datei-Auslesemethoden aus der für
	 * die Logik verantwortliche LogFilter-Klasse auf.
	 *
	 */

	public JournalForm() {
		try {
			logFilter = new LogFilter(logPath);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Daten konnten nicht geladne werden."));
		}

	}

	public List<JournalEntry> getGeneratedJournal() {
		return generatedJournal;
	}

	public List<JournalEntry> getFilteredJournalEntries() {
		return filteredJournalEntries;
	}

	public void setFilteredJournalEntries(
			List<JournalEntry> filteredJournalEntries) {
		this.filteredJournalEntries = filteredJournalEntries;
	}

	public void reload() {
		try {
			generatedJournal = logFilter.logFiltering();
			System.out.println("log holen");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Daten konnten nicht geladne werden."));
		}

	}

	public void deleteLog() {
		File file = new File(logPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File temp : files) {
				if (!temp.getName().equals("server.log"))
					temp.delete();
			}
		}
		file.delete();

		reload();
	}
}