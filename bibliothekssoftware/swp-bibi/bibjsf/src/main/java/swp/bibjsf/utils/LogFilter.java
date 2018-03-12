package swp.bibjsf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import swp.bibjsf.utils.JournalEntry;
import swp.bibjsf.exception.DataSourceException;

/**
 * LogFilter implementiert die Logik für die Verlaufsfunktion.
 * @author haukeolf
 *
 */
public class LogFilter {
	
	/**
	 * Globale Variablen
	 */
	
	// Aktuelle Zeile der Log-Datei
	private String currentLine;
	
	// Case-Zahl für Muster-erkennung
	private int patternCase;
	
	// Die verschiedenen zu erkennenden Muster
	private String [ ] actionCases= {	"add reader -1", 
										"medium added: ", 
										"exemplar added: ",
										"lending added: ",
										"category added: ",
										"commentary added: ",
										"extension added: ",
										"reservation added: ",
										"reader deleted: ",
										"medium deleted: ",
										"exemplar deleted: ",
										"category deleted: ",}; 
	
	// Liste der Verlaufseinträge
    private List<JournalEntry> entries;
   
    // Verzeichnis-Datei
    private File dir;
    
    // Pattern und Matcher für Datum und Zeit und allgemein
    private Pattern datePattern;
    private Pattern timePattern;    
    private Matcher dateMatcher;
    private Matcher timeMatcher;
    
    private Pattern pattern;
    private Matcher matcher;
    
    // Datum- und Zeitmuster in RegEx-Form
    private final String DATE_PATTERN = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
    private final String TIME_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	
    /**
     * Konstruktor initialisiert die Pattern für Datum und Zeit, die Eintrags-Liste und führt
     * setLogFile() aus.
     * @throws IOException
     * @throws InterruptedException
     * @throws DataSourceException
     * @throws NamingException
     */
	public LogFilter(String logPath) throws IOException, InterruptedException, DataSourceException, NamingException
	{
		dir = new File(logPath);
		datePattern = Pattern.compile(DATE_PATTERN);
		timePattern = Pattern.compile(TIME_PATTERN);
		entries = new ArrayList<JournalEntry>();
		
	}
	
	
	 
		
	
	/**
	 * Von außen aufgerufene Methode zum Liefern der Verlaufseinträge.
	 * Führt für jede Log-Datei im files-Array die parsende logCrunching()-Methode aus.
	 * @return Liste mit Verlaufseinträgen
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DataSourceException
	 */
	public List<JournalEntry> logFiltering() throws IOException, InterruptedException, DataSourceException
	{
		int limit = 0;
		entries.clear();
		if(dir != null)
		{
			// Alle Log-Dateien finden und in files speichern
			FileFilter fileFilter = new WildcardFileFilter("*.log*");
			File[] files = dir.listFiles(fileFilter);

			// Nach jünster Datei sortieren
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			
			// Falls mehr als 20 Log-Dateien, limit setzen
			if(files.length > 20)
			{
				limit = 20;
			}
			else
			{
				limit = files.length;
			}
			
			// Parser-Mechanismus auf jede Log-Datei anwenden
			for(int i = 0; i < limit; i++)
			{
				logCrunching(files[i]);
			}
		}
		
		return entries;
	}
	
	/**
	 * Eigentliche Methode zum Auslesen der .log-Dateien.
	 * Matcht die Zeilen der Log-Datei einzeln mit den definierten Mustern 
	 * und fügt entsprechenden Eintrag in entries hinzu.
	 * @param Zu parsende Log-Datei
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DataSourceException
	 */
	public void logCrunching(File logFile) throws IOException, InterruptedException, DataSourceException
	{
		BufferedReader br = new BufferedReader(new FileReader(logFile));
		
		// Aktuelle Zeile der .log*-Datei
		currentLine = br.readLine();
		
		// Lokale String-Hilfsvariablen
		String additional;		
		String reader;
		String medium;
		String exemplar;

		// Solange Datei nicht zuEnde
		while(currentLine != null)
		{
			try{
			// Wenn Zeile nicht leerer String ist
			if(!currentLine.equals(""))
			{
				// Aktuelle Zeile mit jedem Muster-Fall aus actionCases matchen.
				for(patternCase = 0; patternCase < actionCases.length; patternCase++)
				{
					 pattern = Pattern.compile(actionCases[patternCase]);
					 matcher = pattern.matcher(currentLine);	
					 
					 // Falls Muster in aktueller Zeile zu finden ist, entsprechenden Case ausführen und Eintrag hinzufügen
					 if(matcher.find())
					 {
						 
						 switch(patternCase){ 
					        case 0: 
					            entryAdding(currentLine, "Leser hinzugefügt", currentLine.substring(matcher.end() + 1, currentLine.length())); 
					            break; 
					        case 1: 
					            entryAdding(currentLine, "Medium hinzugefügt", currentLine.substring(matcher.end(), currentLine.length())); 
					            break; 
					        case 2: 
					            entryAdding(currentLine, "Exemplar hinzugefügt", currentLine.substring(matcher.end(), currentLine.length())); 
					            break; 
					        case 3: 
					        	reader = currentLine.substring((currentLine.indexOf("lending added:") + 15), currentLine.indexOf("; lending exemplar:"));
					        	exemplar = currentLine.substring((currentLine.indexOf("; lending exemplar:") + 19), currentLine.length()); 
					        	additional =  exemplar + ": " + reader;
										 
					            entryAdding(currentLine, "Ausleihe hinzugefügt", additional); 
					            break; 
					        case 4: 
					            entryAdding(currentLine, "Kategorie hinzugefügt", currentLine.substring(matcher.end(), currentLine.length())); 
					            break; 
					        case 5: 
					        	reader = currentLine.substring((currentLine.indexOf("commentary added:") + 18), currentLine.indexOf("; commented medium:"));
					        	medium = currentLine.substring((currentLine.indexOf("; commented medium:") + 19), currentLine.length()); 
					        	additional =  medium + ": " + reader;
										 
					            entryAdding(currentLine, "Kommentar eingegangen", additional); 
					            break; 
					        case 6: 
					        	reader = currentLine.substring((currentLine.indexOf("; extension reader:") + 19), currentLine.length());
					        	exemplar = currentLine.substring((currentLine.indexOf("extension added:") + 17), currentLine.indexOf("; extension reader:")); 
					        	additional =  exemplar + ": " + reader;
										 
					            entryAdding(currentLine, "Verlängerung bewilligt", additional); 
					            break; 
					        case 7: 
					        	reader = currentLine.substring((currentLine.indexOf("; reservation reader:") + 21), currentLine.length());
					        	medium = currentLine.substring((currentLine.indexOf("reservation added:") + 19), (currentLine.indexOf("; reservation reader:"))); 
					        	additional = medium + ": " + reader;
										 
					            entryAdding(currentLine, "Reservierung hinzugefügt", additional); 
					            break; 
					        case 8: 
					            entryAdding(currentLine, "Leser gelöscht", currentLine.substring(matcher.end(), currentLine.length())); 
					            break; 
					        case 9: 
					            entryAdding(currentLine, "Medium gelöscht", currentLine.substring(matcher.end(), currentLine.length())); 
					            break; 
					        case 10: 
					            entryAdding(currentLine, "Exemplar gelöscht", currentLine.substring(matcher.end(), currentLine.length())); 
					            break; 
					        case 11: 
					            entryAdding(currentLine, "Kategorie gelöscht", currentLine.substring(matcher.end(), currentLine.length())); 
					            break; 
					        default:
					        	
					        } 
						 break;
					 }
				}		 	
			}
			}catch(Exception e){
				
			}
			currentLine = br.readLine();
		}
		br.close();
	}

	/**
	 * Die Methode fügt der JournalEntry-Liste "entries" pro Aufruf einen Eintrag an.
	 * @param Aktuelle Zeile (currentLine)
	 * @param Muster-erkannte Aktion (z.B. Leser hinzufügen)
	 * @param Zusatzinfo (z.B. Mediumtitel)
	 */
	private void entryAdding(String cL, String action, String additional)
	{
		String date = "";
		String time = "";
		timeMatcher = timePattern.matcher(cL);
		dateMatcher = datePattern.matcher(cL);
		
		// Findet Datum in aktueller Zeile
		if(dateMatcher.find())
		{
			date = dateMatcher.group();
		}
		
		// Findet Zeit in aktueller Zeile
		if(timeMatcher.find())
		{
			time = timeMatcher.group();
		}
		
		// Verlaufs-Eintrag hinzufügen mit entsprechendem Datum, Zeit, Aktion, Zusatzinfo
		entries.add(new JournalEntry(date, time, action, additional));
	}
	
	/**
	 * Validen Pfad setzen.
	 * @param Auf Valide geprüftes Log-Verzeichnis
	 */
	public void setValidPath(String logPath)
	{
		dir = new File(logPath);
	}
	
	
	
	/**
	 * Überprüft, ob der übergebene Pfad .log*-Dateien enthält.
	 * @param Übergebener Verzeichnis-String
	 * @return True wenn Log-Dateien enthalten
	 */
	public boolean containsLogs(String givenPath)
	{
		return isLogContaining(new File(givenPath));		
	}
	
	/**
	 * Überprüft, ob der Verzeichnispfad der Variable "dir" .log*-Dateien enthält.
	 * @return True wenn Log-Dateien enthalten
	 */
	public boolean containsLogs()
	{
		return isLogContaining(dir);
	}
	
	/**
	 * Durchsucht die übergebene Verzeichnisdatei nach .log*-Dateien.
	 * @param übergebene Verzeichnisdatei
	 * @return Boolean, ob .log*-Dateien enthalten
	 */
	private boolean isLogContaining(File pathInQuestion)
	{
		// Defaultwert ist false
		boolean result = false;
		
		// Falls pathInQuestion eine richtiges File-Object ist
		if(pathInQuestion != null)
		{	
			// Falls File auch wirklich ein Verzeichnis ist
			if(pathInQuestion.isDirectory())
			{
				// Alle .log*-Dateien dem files-Array hinzufügen
				FileFilter fileFilter = new WildcardFileFilter("*.log*");
				File[] files = pathInQuestion.listFiles(fileFilter);
		
				// Wenn files-Array nicht leer, ist der pathInQuestion LogContaining
				if(files.length != 0)
				{
					result = true;
				}
				else
				{
					// Verlaufselemente löschen, da Verzeichnispfad keine Elemente enthalten kann
					entries.clear();
				}
			}
		}
		
		return result;	
		}
	
}
