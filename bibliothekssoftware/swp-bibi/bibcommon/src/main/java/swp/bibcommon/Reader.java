package swp.bibcommon;

import java.io.Serializable;
import java.util.Date;

/**
 * Die Klasse Reader ist als Bean konzipiert und repräsentiert einen Benutzer im
 * System.
 *
 * @author Eike Externest
 */
public class Reader extends BusinessObject implements Serializable, Cloneable {

    /**
     * Serialisierungs-ID.
     */
    private static final long serialVersionUID = -2835684051415448369L;

    /**
     * Der Vorname des Benutzers.
     */
    private String firstName;

    /**
     * Der Nachname des Benutzers.
     */
    private String lastName;

    /**
     * Das Geburtsdatum des Benutzers.
     */
    private Date birthday;

    /**
     * Straße und Hausnummer oder Postfach des Benutzers.
     */
    private String street;

    /**
     * Die Postleitzahl des Benutzers.
     */
    private String zipcode;

    /**
     * Der Wohnort des Benutzers.
     */
    private String city;

    /**
     * Das Passwort des Benutzers.
     */
    private String password;

    /**
     * Der Benutzername des Benutzers.
     */
    private String username;

    /**
     * Die Telefonnummer des Benutzers.
     */
    private String phone;

    /**
     * Die E-Mail-Adresse des Benutzers.
     */
    private String email;

    /**
     * Das Anmeldungsdatum des Benutzers.
     */
    private Date entryDate;

    /**
     * Das Datum der letzten Aktivität des Benutzers.
     */
    private Date lastUse;

    /**
     * Der Status des Benutzers.
     */
    private String status;

    /**
     * De-/Aktivierung der Ausleihhistorie.
     */
    private boolean historyActivated;

    /**
     * Arbitrary note for a reader.
     */
    private String note;

    /**
     * Die Gruppe des Benutzers.
     */
    private String groupid;

    /**
     * Legt fest, ob der Leser Rückgabe-Mails erhalten möchte.
     */
    private boolean returnMail;

    /**
     * Legt fest, ob der Leser Mahnungs-Mails erhalten möchte.
     */
    private boolean reminderMail;

    /**
     * Legt fest, ob der Leser Reservierungs-Info-Mails erhalten möchte.
     */
    private boolean reservationMail;

    /**
     * Leerer Konstruktor für DBUtils.
     */
    public Reader() {

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Reader)) {
            return false;
        }

        Reader reader = (Reader) obj;
        return ((id == reader.id) && firstName.equals(reader.firstName)
                && lastName.equals(reader.lastName)
                && birthday == reader.birthday && street.equals(reader.street)
                && zipcode.equals(reader.zipcode) && city.equals(reader.city)
                && password.equals(reader.password)
                && username.equals(reader.username)
                && phone.equals(reader.phone) && email.equals(reader.email)
                && entryDate.equals(reader.entryDate)
                && lastUse.equals(reader.lastUse)
                && status.equals(reader.status)
                && historyActivated == reader.historyActivated
                && note.equals(reader.note) && groupid.equals(reader.groupid)
                && returnMail == reader.returnMail
                && reminderMail == reader.reminderMail
                && reservationMail == reader.reservationMail);
    }

    /**
     * Gibt das Geburtsdatum des Benutzers zurück.
     *
     * @return Das Geburtsdatum.
     */
    public final Date getBirthday() {
        return birthday;
    }

    /**
     * Gibt den Wohnort des Benutzers zurück.
     *
     * @return Der Wohnort.
     */
    public final String getCity() {
        return city;
    }

    /**
     * Gibt die E-Mail-Adresse des Benutzers zurück.
     *
     * @return Die E-Mail-Adresse
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Gibt das Anmeldungsdatum des Benutzers zurück.
     *
     * @return Das Anmeldungsdatum.
     */
    public final Date getEntryDate() {
        return entryDate;
    }

    /**
     * Gibt den Vornamen des Benutzers zurück.
     *
     * @return Der Vorname.
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * Gibt die Gruppe des Benutzers zurück.
     *
     * @return Die Benutzergruppe.
     */
    public final String getGroupid() {
        return groupid;
    }

    /**
     * Gibt den Nachnamen des Benutzers zurück.
     *
     * @return Der Nachname.
     */
    public final String getLastName() {
        return lastName;
    }

    /**
     * Gibt das Datum der letzten Aktivität des Benutzers zurück.
     *
     * @return Das Datum der letzten Aktivität.
     */
    public final Date getLastUse() {
        return lastUse;
    }

    /**
     * Gibt die Notiz zum Benutzer zurück.
     *
     * @return Die Notiz.
     */
    public final String getNote() {
        return note;
    }

    /**
     * Gibt das Passwort des Benutzers zurück.
     *
     * @return Das Passwort.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Gibt die Telefonnummer des Benutzers zurück.
     *
     * @return Die Telefonnummer.
     */
    public final String getPhone() {
        return phone;
    }

    /**
     * Gibt den Status des Benutzers zurück.
     *
     * @return Der Status.
     */
    public final String getStatus() {
        return status;
    }

    /**
     * Gibt die Straße und Hausnummer des Benutzers zurück.
     *
     * @return Straße und Hausnummer
     */
    public final String getStreet() {
        return street;
    }

    /**
     * Gibt den Benutzernamen des Benutzers zurück.
     *
     * @return Der Benutzername.
     */
    public final String getUsername() {
        return username;
    }

    /**
     * Gibt die Postleitzahl des Benutzers zurück.
     *
     * @return Die Postleitzahl.
     */
    public final String getZipcode() {
        return zipcode;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + birthday.hashCode();
        result = prime * result + city.hashCode();
        result = prime * result + email.hashCode();
        result = prime * result + entryDate.hashCode();
        result = prime * result + firstName.hashCode();
        result = prime * result + groupid.hashCode();
        result = prime * result + (historyActivated ? 1231 : 1237);
        result = prime * result + lastName.hashCode();
        result = prime * result + lastUse.hashCode();
        result = prime * result + note.hashCode();
        result = prime * result + password.hashCode();
        result = prime * result + phone.hashCode();
        result = prime * result + (reminderMail ? 1231 : 1237);
        result = prime * result + (reservationMail ? 1231 : 1237);
        result = prime * result + (returnMail ? 1231 : 1237);
        result = prime * result + status.hashCode();
        result = prime * result + street.hashCode();
        result = prime * result + username.hashCode();
        result = prime * result + zipcode.hashCode();
        return result;
    }

    /**
     * Gibt zurück, ob der Leser Mahnungs-Mails empfangen möchte.
     *
     * @return Mahnungs-Mails empfangen?
     */
    public final boolean hasReminderMail() {
        return reminderMail;
    }

    /**
     * Gibt zurück, ob der Leser Reservierungs-Info-Mails erhalten möchte.
     *
     * @return Reservierungs-Info-Mails empfangen?
     */
    public final boolean hasReservationMail() {
        return reservationMail;
    }

    /**
     * Gibt zurück, ob der Leser Rückgabe-Mails empfangen möchte.
     *
     * @return Rückgabe-Mails empfangen?
     */
    public final boolean hasReturnMail() {
        return returnMail;
    }

    /**
     * Gibt die Einstellung zur Ausleihhistorie des Benutzers zurück.
     *
     * @return the activatedHistory
     */
    public final boolean isHistoryActivated() {
        return historyActivated;
    }

    /**
     * Setzt das Geburtsdatum des Benutzers.
     *
     * @param birthday
     *            Das zu setzende Geburtsdatum.
     */
    public final void setBirthday(final Date birthday) {
        this.birthday = birthday;
    }

    /**
     * Setzt den Wohnort des Benutzers.
     *
     * @param city
     *            Den zu setzenden Wohnort.
     */
    public final void setCity(final String city) {
        this.city = city;
    }

    /**
     * Setzt die E-Mail-Adresse des Benutzers.
     *
     * @param email
     *            Die zu setzende E-Mail-Adresse.
     */
    public final void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Setzt das Anmeldungsdatum des Benutzers.
     *
     * @param entryDate
     *            Das zu setzende Anmeldungsdatum.
     */
    public final void setEntryDate(final Date entryDate) {
        this.entryDate = entryDate;
    }

    /**
     * Setzt den Vornamen des Benutzers.
     *
     * @param firstName
     *            Der zu setzende Vorname.
     */
    public final void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Setzt die Gruppe des Benutzers.
     *
     * @param groupid
     *            Die zu setzende Gruppe.
     */
    public final void setGroupid(final String groupid) {
        this.groupid = groupid;
    }

    /**
     * Setzt die Einstellung zur Ausleihhistorie des Benutzers.
     *
     * @param historyActivated
     *            Die zu setzende Einstellung.
     */
    public final void setHistoryActivated(final boolean historyActivated) {
        this.historyActivated = historyActivated;
    }

    /**
     * Setzt den Nachnamen des Benutzers.
     *
     * @param lastName
     *            Der zu setzende nachname.
     */
    public final void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Setzt das Datum der letzten Aktivität des Benutzers.
     *
     * @param lastUse
     *            Das zu setzende Datum.
     */
    public final void setLastUse(final Date lastUse) {
        this.lastUse = lastUse;
    }

    /**
     * Setzt die Notiz zum Benutzer.
     *
     * @param note
     *            Die zu setzende Notiz.
     */
    public final void setNote(final String note) {
        this.note = note;
    }

    /**
     * Setzt das Passwort des Benutzers.
     *
     * @param password
     *            Das zu setzende Passwort.
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Setzt die Telefonnummer des Benutzers.
     *
     * @param phone
     *            Die zu setzende Telefonnummer.
     */
    public final void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * Legt fest, ob der Leser Mahnungs-Mails empfangen möchte.
     *
     * @param reminderMail
     *            Der zu setzende Wert.
     */
    public final void setReminderMail(final boolean reminderMail) {
        this.reminderMail = reminderMail;
    }

    /**
     * Legt fest, ob der Leser Reservierungs-Info-Mails erhalten möchte.
     *
     * @param reservationMail
     *            Der zu setzende Wert.
     */
    public final void setReservationMail(final boolean reservationMail) {
        this.reservationMail = reservationMail;
    }

    /**
     * Legt fest, ob der Leser Rückgabe-Mails empfangen möchte.
     *
     * @param returnMail
     *            Der zu setzende Wert.
     */
    public final void setReturnMail(final boolean returnMail) {
        this.returnMail = returnMail;
    }

    /**
     * Setzt den Status des Benutzers.
     *
     * @param status
     *            Der zu setzende Status.
     */
    public final void setStatus(final String status) {
        this.status = status;
    }

    /**
     * Setzt die Straße und Hausnummer des Benutzers.
     *
     * @param street
     *            Die zu setzende Straße und Hausnummer.
     */
    public final void setStreet(final String street) {
        this.street = street;
    }

    /**
     * Setzt den Benutzernamen des Benutzers.
     *
     * @param username
     *            Der zu setzende Benutzername.
     */
    public final void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Setzt die Postleitzahl des Benutzers.
     *
     * @param zipcode
     *            Die zu setzende Postleitzahl.
     */
    public final void setZipcode(final String zipcode) {
        this.zipcode = zipcode;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Reader [firstName=" + firstName + ", lastName=" + lastName
                + ", birthday=" + birthday + ", street=" + street
                + ", zipcode=" + zipcode + ", city=" + city + ", password="
                + password + ", username=" + username + ", phone=" + phone
                + ", email=" + email + ", entryDate=" + entryDate
                + ", lastUse=" + lastUse + ", status=" + status
                + ", historyActivated=" + historyActivated + ", note=" + note
                + ", groupid=" + groupid + ", returnMail=" + returnMail
                + ", reminderMail=" + reminderMail + ", reservationMail="
                + reservationMail + "]";
    }
}
