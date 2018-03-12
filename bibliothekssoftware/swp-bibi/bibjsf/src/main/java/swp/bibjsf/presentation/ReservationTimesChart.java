package swp.bibjsf.presentation;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
public class ReservationTimesChart implements Serializable {

	/**
	 * Serialisierungs-ID.
	 */
	private static final long serialVersionUID = 7898965311407656533L;
	
	/* Model für das Pie-Diagramm */
	private PieChartModel pieModel;
	
	/* Model für das Balken-Diagramm */
	private CartesianChartModel barModel;
	
	/* Statistics-Instanz */
	private Statistics stats;
	
	/* maximaler Wert im Balken-Diagramm */
	private int barMax;
	
	/**
	 * Konstruktor, der das PieModel und das BarModel befüllt.
	 * @throws DataSourceException 
	 */
	public ReservationTimesChart() throws DataSourceException {
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
			int[] reservationTimes = stats.reservationTimesPerWeek();
			
			pieModel.set("Montag", reservationTimes[2]);  
	        pieModel.set("Dienstag", reservationTimes[3]);  
	        pieModel.set("Mittwoch", reservationTimes[4]);  
	        pieModel.set("Donnerstag", reservationTimes[5]);  
	        pieModel.set("Freitag", reservationTimes[6]); 
	        pieModel.set("Samstag", reservationTimes[7]);
	        pieModel.set("Sonntag", reservationTimes[1]);
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
			int[] reservationTimes = stats.reservationTimesPerMonth();
			ChartSeries reservations = new ChartSeries();  
	        reservations.setLabel("Vormerkungen");  
	  
	        for (int i = 1; i<reservationTimes.length; i++) {
	        	reservations.set(""+i, reservationTimes[i]);
	        	if (reservationTimes[i] > barMax) {
	        		setBarMax(reservationTimes[i]);
	        	}
	        }  
	        setBarMax(barMax+1+((int) (barMax*0.2)));
	        barModel.addSeries(reservations);
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
