package swp.bibjsf.utils;

public class JournalEntry {
	
	/**
	 * Datum des Log-Eintrages
	 */
	private final String date;
	/**
	 * Uhrzeit des Log-Eintrages
	 */
	private final String time;
	/**
	 * Aktion des Log-Eintrages
	 */
	private final String action;
	/**
	 * Zusatz des Log-Eintrages
	 */
	private final String additional;

	public JournalEntry(String date, String time, String action, String additional)
	{
		String[] data = date.split("-");
		this.date = data[2]+"."+data[1]+"."+data[0];
		this.time = time;
		this.action = action; 
		this.additional = additional;
	}

	
	/**
	 * Gibt das Datum des Log-Eintrages zurück
	 * 
	 * @return Datum des Log-Eintrages ( lokales Format)
	 */
	public String getDate()
	{
		
		return date;
	}
	
	/**
	 * Gibt die Uhrzeit des Log-Eintrages zurück
	 * 
	 * @return Uhrzeit des Log-Eintrages 
	 */
	public String getTime()
	{
		return time;
	}

	/**
	 * Gibt die Aktion des Log-Eintrages zurück
	 * 
	 * @return Aktion des Log-Eintrages
	 */
	public String getAction()
	{
		return action;
	}

	/**
	 * Gibt den Zusatz des Log-Eintrages zurück
	 * 
	 * @return Zusatz des Log-Eintrages 
	 */
	public String getAdditional()
	{
		return additional;
	}

}
