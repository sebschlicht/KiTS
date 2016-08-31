package de.jablab.sebschlicht.series.model;

import java.io.Serializable;

/**
 * TV series that may be guessed during the guessing game.<br>
 * Series are serializable in order to be passed to activities.
 * Series are comparable alphabetically.
 *
 * @author sebschlicht
 *
 */
public class Series implements Comparable<Series>, Serializable {

    private static final long serialVersionUID = -4353002437117148819L;

    /**
     * series title
     */
    private String name;

    /**
     * file name used for series resources
     */
    private String fileName;

    /**
     * Creates a series.
     *
     * @param name
     *            series title
     * @param fileName
     *            file name used for series resources
     */
    public Series(
            final String name,
            final String fileName) {
        this.name = name;
        this.fileName = fileName;
    }

    /**
     * @return series title
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return file name used for series resources
     */
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public int compareTo(final Series series) {
        return this.getName().compareTo(series.getName());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Series)) {
            return false;
        }
        Series series = (Series) o;
        return this.getName().equals(series.getName());
    }
}
