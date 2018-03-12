package swp.bibcommon;

/**
 * Die Klasse openingTime ist als Bean konzipiert und repräsentiert
 * Öffnungszeiten im System.
 *
 * @author Helena Meißner, Eike Externest
 */
public class OpeningTime extends BusinessObject {

    /**
     * Der Tag der Öffnungszeit.
     */
    private String day;

    /**
     * Der Beginn der Vormittags-Öffnungszeit.
     */
    private String morningStart;

    /**
     * Das Ende der Vormittags-Öffnungszeit.
     */
    private String morningEnd;

    /**
     * Der Beginn der Nachmittags-Öffnungszeit.
     */
    private String afternoonStart;

    /**
     * Das Ende der Nachmittags-Öffnungszeit.
     */
    private String afternoonEnd;

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
        if (obj == null || !(obj instanceof OpeningTime)) {
            return false;
        }

        OpeningTime open = (OpeningTime) obj;
        return ((id == open.id) && morningStart.equals(open.morningStart)
                && morningEnd.equals(open.morningEnd)
                && afternoonStart.equals(afternoonStart)
                && afternoonEnd.equals(open.afternoonEnd) && day
                    .equals(open.day));
    }

    /**
     * Gibt das Ende der Nachmittags-Öffnungszeit zurück.
     *
     * @return Das Ende der Nachmittags-Öffnungszeit.
     */
    public final String getAfternoonEnd() {
        return afternoonEnd;
    }

    /**
     * Setzt den Beginn der Nachmittags-Öffnungszeit.
     *
     * @return Der Beginn der Nachmittags-Öffnungszeiten.
     */
    public final String getAfternoonStart() {
        return afternoonStart;
    }

    /**
     * Gibt den Tag der Öffnungszeit zurück.
     *
     * @return Der Tag der Öffnungszeit.
     */
    public final String getDay() {
        return day;
    }

    /**
     * Gibt das Ende der Vormittags-Öffnungszeit zurück.
     *
     * @return Das Ende der Vormittags-Öffnungszeit.
     */
    public final String getMorningEnd() {
        return morningEnd;
    }

    /**
     * Gibt den Beginn der Vormittags-Öffnungszeit zurück.
     *
     * @return Der Beginn der Vormittags-Öffnungszeiten.
     */
    public final String getMorningStart() {
        return morningStart;
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
        result = prime * result + afternoonEnd.hashCode();
        result = prime * result + afternoonStart.hashCode();
        result = prime * result + day.hashCode();
        result = prime * result + morningEnd.hashCode();
        result = prime * result + morningStart.hashCode();
        return result;
    }

    /**
     * Setzt das Ende der Nachmittags-Öffnungszeit.
     *
     * @param afternoonEnd
     *            Das Ende der Nachmittags-Öffnungszeit.
     */
    public final void setAfternoonEnd(final String afternoonEnd) {
        this.afternoonEnd = afternoonEnd;
    }

    /**
     * Setzt den Beginn der Nachmittags-Öffnungszeit.
     *
     * @param afternoonStart
     *            Der Beginn der Nachmittags-Öffnungszeiten.
     */
    public final void setAfternoonStart(final String afternoonStart) {
        this.afternoonStart = afternoonStart;
    }

    /**
     * Setzt den Tag der Öffnungszeit.
     *
     * @param day
     *            Der zu setzende Tag der Öffnungszeit.
     */
    public final void setDay(final String day) {
        this.day = day;
    }

    /**
     * Setzt das Ende der Vormittags-Öffnungszeit.
     *
     * @param morningEnd
     *            Das zu setzende Ende der Vormittags-Öffnungszeit.
     */
    public final void setMorningEnd(final String morningEnd) {
        this.morningEnd = morningEnd;
    }

    /**
     * Setzt den Beginn der Vormittags-Öffnungszeit.
     *
     * @param morningStart
     *            Der zu setzende Beginn der Vormittags-Öffnungszeit.
     */
    public final void setMorningStart(final String morningStart) {
        this.morningStart = morningStart;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        StringBuilder out = new StringBuilder(day + ": ");

        if (morningStart == null && afternoonStart == null) {
            out.append("Geschlossen.");
        } else {
            if (morningStart != null) {
                out.append(morningStart);
                out.append(" - ");
                out.append(morningEnd);
                out.append(" Uhr");
                if (afternoonStart != null) {
                    out.append(" & ");
                }
            }
            if (afternoonStart != null) {
                out.append(afternoonStart);
                out.append(" - ");
                out.append(afternoonEnd);
                out.append(" Uhr");
            }
        }
        return out.toString();
    }
}
