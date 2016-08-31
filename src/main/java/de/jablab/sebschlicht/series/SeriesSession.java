package de.jablab.sebschlicht.series;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.Charsets;

import android.util.Log;
import de.jablab.sebschlicht.android.AndroidTools;
import de.jablab.sebschlicht.series.model.Series;
import de.jablab.sebschlicht.series.resources.ResourceProvider;

/**
 * KiTS session that provides access to the series and represents the
 * application state.
 *
 * @author sebschlicht
 *
 */
public class SeriesSession {

    private static final Random RANDOM = new Random();

    /**
     * IP address of the KiTS server the application is connected to
     */
    private String serverAddress;

    /**
     * series resource provider
     */
    private final ResourceProvider resourceProvider;

    /**
     * all series available
     */
    private final List<Series> series;

    /**
     * series that haven't been watched yet
     */
    protected final List<Series> unwatchedSeries;

    /**
     * Creates a new KiTS session.
     *
     * @param resourceProvider
     *            series resource provider
     * @param csvStream
     *            series CSV file stream
     * @throws IOException
     *             if the CSV file loading/parsing failed
     */
    public SeriesSession(
            final ResourceProvider resourceProvider,
            final InputStream csvStream) throws IOException {
        this.resourceProvider = resourceProvider;
        this.series = loadSeriesFromResource(csvStream);
        this.unwatchedSeries = new ArrayList<Series>(this.series);
    }

    /**
     * @return IP address of the KiTS server the application is connected to
     */
    public String getServerAddress() {
        return this.serverAddress;
    }

    /**
     * Sets the IP address of the KiTS server the application is connected to.
     *
     * @param serverAddress
     *            IP address of the KiTS server or <code>null</code> if the
     *            application isn't connected to a KiTS server
     */
    public void setServerAddress(final String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Checks if the application is connected to a KiTS server.
     *
     * @return <code>true</code> if the application is connected to a KiTS
     *         server, <code>false</code> otherwise
     */
    public boolean isConnected() {
        return (this.getServerAddress() != null);
    }

    /**
     * @return series resource provider
     */
    public ResourceProvider getResourceProvider() {
        return this.resourceProvider;
    }

    /**
     * @return all series available
     */
    public List<Series> getSeries() {
        return this.series;
    }

    /**
     * Retrieves a random series that hasn't been watched during the current
     * session.
     *
     * @return a series that hasn't been watched yet
     */
    public Series getRandomSeries() {
        if (this.unwatchedSeries.size() == 0) {
            return null;
        }
        final int numSeriesLeft = this.unwatchedSeries.size();
        final int seriesIndex = RANDOM.nextInt(numSeriesLeft);
        return this.unwatchedSeries.remove(seriesIndex);
    }

    /**
     * Checks the resource availability for all series.
     */
    protected void checkResourceAvailability() {
        List<Series> incomplete = new LinkedList<Series>();
        for (Series series : this.series) {
            boolean audioFileExists = this.resourceProvider.hasAudioFile(series);
            boolean imageFileExists = this.resourceProvider.hasImageFile(series);
            boolean videoFileExists = this.resourceProvider.hasVideoFile(series);

            if (!audioFileExists || !imageFileExists || !videoFileExists) {
                incomplete.add(series);

                StringBuilder message = new StringBuilder();
                message.append("\"");
                message.append(series.getName());
                message.append("\": ");
                if (!audioFileExists) {
                    message.append("audio ");
                }
                if (!imageFileExists) {
                    message.append("image ");
                }
                if (!videoFileExists) {
                    message.append("video");
                }
                Log.d("SeriesSession | missing series resources", message.toString());
            }
        }
        for (Series series : incomplete) {
            this.unwatchedSeries.remove(series);
        }
        Log.d("SeriesSession", this.unwatchedSeries.size() + " series fully available.");
    }

    /**
     * Loads the series from a CSV file.
     *
     * @param csvStream
     *            series CSV file stream
     * @return series loaded from the CSV file
     * @throws IOException
     *             if the CSV file loading/parsing fails
     */
    protected static List<Series> loadSeriesFromResource(final InputStream csvStream)
            throws IOException {
        Map<String, Series> series = new HashMap<String, Series>();

        final Reader reader = new InputStreamReader(csvStream, Charsets.UTF_8);
        final CSVParser parser =
                new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter('#').withHeader(
                        CSVHeaders.class));
        try {
            //TODO is this fail-safely streamed or in-memory?
            for (CSVRecord record : parser) {
                if (record.size() != 2) {
                    Log.w("SeriesSession", "series CSV file malformed: \"" + record
                            + "\"");
                    continue;
                }

                String name = record.get(CSVHeaders.NAME);
                String fileName = AndroidTools.toValidResourceName(name);
                Series crrSeries = new Series(name, fileName);
                series.put(name, crrSeries);
            }
        } finally {
            parser.close();
            reader.close();
        }

        List<Series> list = new ArrayList<Series>(series.values());
        Collections.sort(list);
        return list;
    }

    private enum CSVHeaders {
        NAME, SHORT;
    }
}
