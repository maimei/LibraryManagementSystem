package swp.bibjsf.presentation;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import swp.bibjsf.businesslogic.Statistics;
import swp.bibjsf.exception.DataSourceException;

/**
 * ManagedBean, die die Daten für ein PieChartModel und ein BarChartModel für Ausleihzeiten bereithält.
 * 
 * @author Helena Meißner
 *
 */
@ManagedBean
@SessionScoped
public class LendingTimesChart implements Serializable {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 2444867366660299998L;
	
	/* Model für das Pie-Diagramm */
	private PieChartModel pieModel;
	
	/* Model für das Balken-Diagramm */
	private CartesianChartModel barModel;
	
	/* Statistics-Instanz */
	private Statistics stats;
	
	/* maximaler Wert im Balken-Diagramm */
	private int barMax;
	
	/**
     * Logger of this class
     */
    protected static final Logger logger = Logger.getLogger(LendingTimesChart.class);
	
	/**
	 * Konstruktor, der das PieModel und das BarModel befüllt.
	 * @throws DataSourceException 
	 */
	public LendingTimesChart() throws DataSourceException {
		stats = Statistics.getInstance();
		createPieModel();
		barMax = 0;
		createBarModel();
	}
	
	/**
	 * Liefert das Pie-Diagramm-Objekt zurück.
	 * @return das Diagramm
	 */
    public PieChartModel getPieModel() {  
        return pieModel;  
    }  
  
    /**
     * Befüllt das Pie-Diagramm-Objekt.
     */
    private void createPieModel() {  
        pieModel = new PieChartModel();  
        try {
			int[] lendingTimes = stats.lendingTimesPerWeek();
			
			pieModel.set("Montag", lendingTimes[2]);  
	        pieModel.set("Dienstag", lendingTimes[3]);  
	        pieModel.set("Mittwoch", lendingTimes[4]);  
	        pieModel.set("Donnerstag", lendingTimes[5]);  
	        pieModel.set("Freitag", lendingTimes[6]); 
	        pieModel.set("Samstag", lendingTimes[7]);
	        pieModel.set("Sonntag", lendingTimes[1]);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Statistik konnte nicht erstellt werden"));
		} 
    }  
    
    /**
     * Liefert das Balken-Diagramm-Objekt zurück.
     * @return
     */
    public CartesianChartModel getBarModel() {
    	return barModel;
    }
    
    /**
     * Befüllt das Balken-Diagramm-Objekt.
     */
    private void createBarModel() {  
        barModel = new CartesianChartModel();  
		try {
			int[] lendingTimes = stats.lendingTimesPerMonth();
			ChartSeries lendings = new ChartSeries();  
	        lendings.setLabel("Ausleihen");  
	  
	        for (int i = 1; i<lendingTimes.length; i++) {
	        	lendings.set(""+i, lendingTimes[i]);
	        	if (lendingTimes[i] > barMax) {
	        		setBarMax(lendingTimes[i]);
	        	}
	        }  
	        setBarMax(barMax+1+((int) (barMax*0.2)));
	        barModel.addSeries(lendings);
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler", "Die Statistik konnte nicht erstellt werden"));
		}
    }  
    
    /**
     * Liefert den maximalen Wert im Balkendiagramm zurück.
     * @return	der maximale Wert.
     */
    public int getBarMax() {
    	return barMax;
    }
   
    /**
     * Setzt den maximalen Wert im Balkendiagramm.
     * @param barMax
     * 			der maximale Wert.
     */
    public void setBarMax(int barMax) {
    	this.barMax = barMax;
    }
}
