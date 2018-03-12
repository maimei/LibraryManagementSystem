package swp.bibjsf.utils;

import java.util.*;

/**
 * Klasse dient dazu, um zu Zählen wie viele E-Mail benachrichtigung für ein leibares Exemplar 
 * 
 * @author (Niklas Bruns) 
 * @version (a version number or a date)
 */
public class EmailExemplarHelper
{
    private Integer anzahl;
    HashMap<Integer, Integer> data = new HashMap<Integer, Integer>();
    
    /**
     * Konstruktor der Klasse EmailExemplarHelper
     * @param anzahl Maximalanzahl von Benachrichtigungen.
     */
    public EmailExemplarHelper(final Integer anzahl)
    {
        this.anzahl = anzahl;
    }
    
    /**
     * Funktion prüft, ob schon die maximalAnzahl erreicht ist.
     * @param iD betreffende ID
     * @return true wenn Anzahl von der ID unter Maximalanzahl
     */
    public boolean wantLoan(final Integer iD)
    {
        if(anzahl <= 0)
        {
            return false;
        }
        if(data.containsKey(iD))
        {
            int anzahlCount = data.get(iD);
            if(this.anzahl > anzahlCount)
            {
                anzahlCount++;
                data.put(iD,anzahlCount);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            data.put(iD,1);
            return true;
        }
    }
    
}
