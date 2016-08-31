package de.jablab.sebschlicht.series;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
     */
    public SeriesSession(
            final ResourceProvider resourceProvider,
            final InputStream csvStream) {
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
     */
    protected static List<Series> loadSeriesFromResource(final InputStream csvStream) {
        //TODO use OpenCSV library
        Map<String, Series> series = new HashMap<String, Series>();
        InputStreamReader is = new InputStreamReader(csvStream);
        BufferedReader reader = new BufferedReader(is);

        String line;
        String[] lineParts;
        Series crrSeries;

        String name;
        String fileName;

        try {
            while ((line = reader.readLine()) != null) {
                lineParts = line.split("#");
                if (lineParts.length != 2) {
                    throw new IllegalArgumentException("series CSV file malformed: \""
                            + line + "\"");
                }

                name = lineParts[0];
                fileName = AndroidTools.toValidResourceName(name);

                crrSeries = new Series(name, fileName);
                series.put(name, crrSeries);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Series> list = new ArrayList<Series>(series.values());
        Collections.sort(list);
        return list;
    }
}
