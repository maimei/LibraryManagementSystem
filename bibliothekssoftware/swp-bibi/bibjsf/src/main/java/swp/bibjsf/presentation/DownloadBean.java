package swp.bibjsf.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@SessionScoped
public class DownloadBean implements Serializable
{
  private static final long serialVersionUID = 1L;
 
  /**
   * Bean zur Bereitstellung der Download-Funktion. Vorerst nur zum Downloaden
   * des User-Guides (Handbuch) entworfen.
   */
  public void downloadFile() throws IOException
  {
	 // Applikationspfad ermitteln
	 String pathname = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	
	 // Datei, Input-Stream, Byte-Buffer erstellen
     File userGuide = new File(pathname + "/resources/PDF/User-Guide.pdf");
     InputStream fis = new FileInputStream(userGuide);
     byte[] buf = new byte[(int)userGuide.length()];
     
     int offset = 0;
     int numRead = 0;
     while ((offset < buf.length) && ((numRead = fis.read(buf, offset, buf.length - offset)) >= 0)) 
     {
       offset += numRead;
     }
     
     // Input-Stream schließen
     fis.close();
     HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
    		 .getExternalContext().getResponse();
    
    // Dateityp mit octet-stream abstrakt machen
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename=User-Guide.pdf");
    
    
    response.getOutputStream().write(buf);
    response.getOutputStream().flush();
    response.getOutputStream().close();
    FacesContext.getCurrentInstance().responseComplete();

  }
}