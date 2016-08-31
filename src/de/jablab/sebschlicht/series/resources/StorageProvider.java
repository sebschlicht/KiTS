package de.jablab.sebschlicht.series.resources;

import java.io.File;

import android.net.Uri;
import de.jablab.sebschlicht.series.model.Series;

/**
 * Basic implementation for {@link ResourceProvider ResourceProviders} using a
 * storage.
 *
 * @author sebschlicht
 *
 */
public abstract class StorageProvider implements ResourceProvider {

    /**
     * file extension for audio files of series
     */
    public static final String AUDIO_FILE_EXTENSION = "ogg";

    /**
     * file extension for image files of series
     */
    public static final String IMAGE_FILE_EXTENSION = "png";

    /**
     * file extension for video files of series
     */
    public static final String VIDEO_FILE_EXTENSION = "mp4";

    /**
     * root directory of resource files on the storage
     */
    private final File resourceDirectory;

    /**
     * directory with audio files of the series
     */
    private final File audioDirectory;

    /**
     * directory with image files of the series
     */
    private final File imageDirectory;

    /**
     * directory with video files of the series
     */
    private final File videoDirectory;

    /**
     * Creates a resource provider that is using a storage.
     *
     * @param resourceDirectory
     *            root directory of resource files on the storage
     */
    public StorageProvider(
            File resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
        if (!resourceDirectory.isDirectory()) {
            throw new IllegalStateException("Series resource directory "
                    + resourceDirectory.getAbsolutePath() + " is missing!");
        }
        this.audioDirectory = new File(this.resourceDirectory, "audio");
        this.imageDirectory = new File(this.resourceDirectory, "images");
        this.videoDirectory = new File(this.resourceDirectory, "video");
    }

    @Override
    public boolean hasAudioFile(Series series) {
        return this.getAudioFile(series).exists();
    }

    @Override
    public boolean hasImageFile(Series series) {
        return this.getImageFile(series).exists();
    }

    @Override
    public boolean hasVideoFile(Series series) {
        return this.getVideoFile(series).exists();
    }

    @Override
    public File getAudioFile(final Series series) {
        return new File(this.audioDirectory, getAudioFileName(series));
    }

    @Override
    public File getImageFile(final Series series) {
        return new File(this.imageDirectory, getImageFileName(series));
    }

    @Override
    public File getVideoFile(final Series series) {
        return new File(this.videoDirectory, getVideoFileName(series));
    }

    @Override
    public Uri getAudioFileUri(final Series series) {
        final File audioFile = this.getAudioFile(series);
        return uriFromFile(audioFile);
    }

    @Override
    public Uri getImageFileUri(final Series series) {
        final File imageFile = this.getImageFile(series);
        return uriFromFile(imageFile);
    }

    @Override
    public Uri getVideoFileUri(final Series series) {
        final File videoFile = this.getVideoFile(series);
        return uriFromFile(videoFile);
    }

    /**
     * Builds the file name of an audio resource.
     *
     * @param series
     *            series the audio resource belongs to
     * @return audio resource file name of the series specified
     */
    protected static String getAudioFileName(final Series series) {
        return series.getName() + "." + AUDIO_FILE_EXTENSION;
    }

    /**
     * Builds the file name of an image resource.
     *
     * @param series
     *            series the image resource belongs to
     * @return image resource file name of the series specified
     */
    protected static String getImageFileName(final Series series) {
        return series.getName() + "." + IMAGE_FILE_EXTENSION;
    }

    /**
     * Builds the file name of a video resource.
     *
     * @param series
     *            series the video resource belongs to
     * @return video resource file name of the series specified
     */
    protected static String getVideoFileName(final Series series) {
        return series.getName() + "." + VIDEO_FILE_EXTENSION;
    }

    /**
     * Creates an Android Uri to a file.
     *
     * @param file
     *            file to create the Uri for
     * @return Uri to the file passed
     */
    public static Uri uriFromFile(final File file) {
        return Uri.fromFile(file);
    }
}
