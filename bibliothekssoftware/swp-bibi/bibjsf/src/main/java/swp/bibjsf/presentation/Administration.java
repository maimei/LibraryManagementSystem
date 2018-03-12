/*
 * Copyright (c) 2013 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package swp.bibjsf.presentation;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.ZipOutputStream;

import javax.annotation.security.DeclareRoles;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import swp.bibjsf.businesslogic.AdministrationHandler;
import swp.bibjsf.exception.DataSourceException;

/**
 * Functions reserved for the administrator, that is, administrations of
 * backups.
 * 
 * @author koschke, Bernd Poppinga
 */
@ManagedBean
@SessionScoped
@DeclareRoles({ "ADMIN" })
public class Administration extends ShowMediumForm implements Serializable {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = -649625566653922056L;

	/**
	 * Pfad der Backups
	 */
	private String path = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "backup"
			+ System.getProperty("file.separator");

	/**
	 * pass for encoding/decoding
	 */
	String pass = "hebeeini";

	/**
	 * Action to reset the database.
	 * 
	 * @return navigation result
	 */
	public final String resetDB() {
		try {
			AdministrationHandler.getInstance().resetDB();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Datenbank wurde erfolgreich zurückgesetzt"));
			return "success";
		} catch (DataSourceException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Es konnte nicht auf die Datenbank zugegriffen werden"
									+ e));
			return "error";
		}
	}

	/**
	 * Action to backup the database. *
	 * 
	 * @return navigation result
	 */
	public final String backupDB() {
		try {
			AdministrationHandler.getInstance().backupDB();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Backup wurde erfolgreich erstellt"));
			return "success";
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Es konnte nicht auf die Datenbank zugegriffen werden"));
			return "error";
		}
	}

	/**
	 * Action to restore the database from a previous backup.
	 * 
	 * @return navigation result
	 */
	public final String restoreDB(String backup) {
		try {
			AdministrationHandler.getInstance().restoreDB(
					path + backup + System.getProperty("file.separator"));
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Backupversion "
									+ "wurde erfolgreich wiederhergestellt"));
			return "success";
		} catch (DataSourceException e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Es konnte nicht auf die Datenbank zugegriffen werden"));
			return "error";
		}
	}

	/**
	 * Liest alle Backup Dateien ein und bildet daraus ein verschlüsseltes Zip
	 * Archiv
	 * 
	 * @author Bernd Poppinga
	 * @return Backup als Zip
	 */
	public final StreamedContent backupDownload(File dir) {
		boolean failed = false;
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				ZipOutputStream zipOutput = new ZipOutputStream(outStream);) {

			for (File file : dir.listFiles()) {
				try (FileInputStream fileInput = new FileInputStream(file)) {
					zipOutput.putNextEntry(new ZipEntry(file.getName()));
					int size;
					byte[] buffer = new byte[2048];
					while ((size = fileInput.read(buffer, 0, buffer.length)) > 0) {
						zipOutput.write(buffer, 0, size);
					}
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Zip konnte nicht erstellt werden."));
					failed = true;
				}
			}
			zipOutput.close();
			outStream.close();

			if (failed) {
				return null;
			}

			byte[] data = encode(outStream.toByteArray());
			ByteArrayInputStream inStream = new ByteArrayInputStream(data);
			return new DefaultStreamedContent(inStream, "application/backup",
					dir.getName() + ".backup");
		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Zip konnte nicht erstellt werden."));
			return null;
		}

	}

	/**
	 * Liest die Backup Dateien aus einem Zip-Archiv aus und speichert diese.
	 * 
	 * @author Bernd Poppinga
	 * @param event
	 *            Event, welches die Datei übergibt
	 */
	public void backupUpload(FileUploadEvent event) {
		final UploadedFile uploadedFile = event.getFile();
		String filename = uploadedFile.getFileName();
		if (filename
				.matches("^\\d\\d[-]\\d\\d[-]\\d\\d\\d\\d[ ]\\d\\d[-]\\d\\d[-]\\d\\d[.]backup$")) {
			String dirpath = path
					+ filename.substring(0, filename.length() - 7);
			File dir = new File(dirpath);
			if (dir.mkdir()) {
				ZipEntry entry = null;
				try (InputStream input = new ByteArrayInputStream(
						decode(uploadedFile.getContents()));
						ZipInputStream zip = new ZipInputStream(input)) {
					while ((entry = zip.getNextEntry()) != null) {

						File file = new File(dirpath
								+ System.getProperty("file.separator")
								+ entry.getName());
						logger.info(dir + entry.getName());
						int size;
						byte[] buffer = new byte[2048];
						try (FileOutputStream os = new FileOutputStream(file);
								BufferedOutputStream bos = new BufferedOutputStream(
										os, buffer.length)) {
							while ((size = zip.read(buffer, 0, buffer.length)) > 0) {
								bos.write(buffer, 0, size);
							}

						}
					}
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Fehler",
									"Datei konnte nicht eingelesen werden."));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
								"Diese Version liegt bereits auf dem Server."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fehler",
							"Falscher Dateiname."));
		}
	}

	/**
	 * Gibt ein Array aller Backups zurück
	 * 
	 * @author Bernd Poppinga
	 * @return Array mit allen Backups
	 */
	public final File[] getBackups() {
		File f = new File(path);
		return f.listFiles();
	}

	public void delete(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File temp : files) {
				delete(temp);
			}
		}
		file.delete();
	}

	/**
	 * Kodiert Daten und gibt die kodierten Daten wieder aus.
	 * 
	 * @author Bernd Poppinga
	 * @param bytes
	 *            Die zu kodierenden Daten
	 * @return Die kodierten Daten
	 */
	private byte[] encode(byte[] bytes) {

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Cipher cipher = Cipher.getInstance("DES");
			Key key = new SecretKeySpec(pass.getBytes(), "DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			try (OutputStream cos = new CipherOutputStream(out, cipher)) {
				cos.write(bytes);
				bytes = out.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return bytes;
	}

	/**
	 * Dekodiert Daten und gibt die dekodierten Daten wieder aus.
	 * 
	 * @author Bernd Poppinga
	 * @param bytes
	 *            Die zu dekodierenden Daten
	 * @return Die dekodierten Daten
	 */
	private final byte[] decode(byte[] bytes) {

		try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
			Cipher cipher = Cipher.getInstance("DES");
			Key key = new SecretKeySpec(pass.getBytes(), "DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
					CipherInputStream cis = new CipherInputStream(input, cipher)) {
				for (int b; (b = cis.read()) != -1;) {
					bos.write(b);
				}
				bytes = bos.toByteArray();
			} catch (IOException e) {
				logger.error(e);
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return bytes;
	}
}
