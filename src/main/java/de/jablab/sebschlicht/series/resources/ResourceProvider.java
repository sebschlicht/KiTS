package de.jablab.sebschlicht.series.resources;

import java.io.File;

import android.net.Uri;
import de.jablab.sebschlicht.series.model.Series;

/**
 * Provides access to the resource files needed by this activity to handle a
 * series.
 *
 * @author sebschlicht
 *
 */
public interface ResourceProvider {

    /**
     * Checks if the audio file of a series is available.
     *
     * @param series
     *            series the audio file availability should be checked for
     * @return <code>true</code> if the audio file is available for the series
     *         passed, <code>false</code> otherwise
     */
    boolean hasAudioFile(Series series);

    /**
     * Checks if the image file of a series is available.
     *
     * @param series
     *            series the image file availability should be checked for
     * @return <code>true</code> if the image file is available for the series
     *         passed, <code>false</code> otherwise
     */
    boolean hasImageFile(Series series);

    /**
     * Checks if the video file of a series is available.
     *
     * @param series
     *            series the video file availability should be checked for
     * @return <code>true</code> if the video file is available for the series
     *         passed, <code>false</code> otherwise
     */
    boolean hasVideoFile(Series series);

    /**
     * Retrieves the audio file of a series.
     *
     * @param series
     *            series the audio file should be retrieved for
     * @return audio file of the series passed
     */
    File getAudioFile(Series series);

    /**
     * Retrieves the image file of a series.
     *
     * @param series
     *            series the image file should be retrieved for
     * @return image file of the series passed
     */
    File getImageFile(Series series);

    /**
     * Retrieves the video file of a series.
     *
     * @param series
     *            series the video file should be retrieved for
     * @return video file of the series passed
     */
    File getVideoFile(Series series);

    /**
     * Retrieves the Uri to the audio file of a series.
     *
     * @param series
     *            series the file Uri should be retrieved for
     * @return Uri to the audio file of the series passed
     */
    Uri getAudioFileUri(Series series);

    /**
     * Retrieves the Uri to the image file of a series.
     *
     * @param series
     *            series the file Uri should be retrieved for
     * @return Uri to the image file of the series passed
     */
    Uri getImageFileUri(Series series);

    /**
     * Retrieves the Uri to the video file of a series.
     *
     * @param series
     *            series the file Uri should be retrieved for
     * @return Uri to the video file of the series passed
     */
    Uri getVideoFileUri(Series series);
}
