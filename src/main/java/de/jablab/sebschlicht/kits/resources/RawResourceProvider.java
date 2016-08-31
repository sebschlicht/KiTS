package de.jablab.sebschlicht.kits.resources;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import de.jablab.sebschlicht.kits.model.Series;

/**
 * {@link ResourceProvider} using raw resource files of the APK as data source.
 *
 * @author sebschlicht
 *
 */
public class RawResourceProvider implements ResourceProvider {

    /**
     * prefix for audio resource files of series
     */
    public static final String AUDIO_RES_FILE_PREFIX = "aud_";

    /**
     * prefix for video resource files of series
     */
    public static final String VIDEO_RES_FILE_PREFIX = "vid_";

    /**
     * Android resources
     */
    protected Resources resources;

    /**
     * resource package name
     */
    protected String packageName;

    /**
     * Creates a resource provider using raw resource files of the APK.
     *
     * @param resources
     *            Android resources
     * @param packageName
     *            resource package name
     */
    public RawResourceProvider(
            final Resources resources,
            final String packageName) {
        this.resources = resources;
        this.packageName = packageName;
    }

    /**
     * Retrieves the id of a resource.
     *
     * @param identifier
     *            resource file identifier
     * @return resource id of the resource with the file identifier specified
     */
    protected int getResourceId(final String identifier) {
        return this.resources.getIdentifier(identifier, "raw", this.packageName);
    }

    /**
     * Retrieves the Uri of a resource.
     *
     * @param identifier
     *            resource file identifier
     * @return Uri of the resource with the file identifier specified
     * @throws FileNotFoundException
     *             if there is no such resource
     */
    protected Uri getResourceUri(final String identifier) throws FileNotFoundException {
        int resId = this.getResourceId(identifier);
        if (resId == 0) {
            throw new FileNotFoundException("Failed to load resource \"" + identifier
                    + "\"!");
        }
        return Uri.parse("android.resource://" + this.packageName + "/" + resId);
    }

    /**
     * Builds the audio resource file identifier of a series.
     *
     * @param series
     *            series the audio resource file identifier should be built for
     * @return audio resource file identifier of the series specified
     */
    protected String getAudioResourceIdentifier(final Series series) {
        return AUDIO_RES_FILE_PREFIX + series.getFileName();
    }

    /**
     * Builds the image resource file identifier of a series.
     *
     * @param series
     *            series the image resource file identifier should be built for
     * @return image resource file identifier of the series specified
     */
    protected String getImageResourceIdentifier(final Series series) {
        return series.getFileName();
    }

    /**
     * Builds the video resource file identifier of a series.
     *
     * @param series
     *            series the video resource file identifier should be built for
     * @return video resource file identifier of the series specified
     */
    protected String getVideoResourceIdentifier(final Series series) {
        return VIDEO_RES_FILE_PREFIX + series.getFileName();
    }

    /**
     * Retrieves the audio resource id of a series.
     *
     * @param series
     *            series the audio resource id should be retrieved for
     * @return audio resource id of the series specified
     */
    protected int getAudioResourceId(final Series series) {
        final String identifier = this.getAudioResourceIdentifier(series);
        return this.getResourceId(identifier);
    }

    /**
     * Retrieves the image resource id of a series.
     *
     * @param series
     *            series the image resource id should be retrieved for
     * @return image resource id of the series specified
     */
    protected int getImageResourceId(final Series series) {
        final String identifier = this.getImageResourceIdentifier(series);
        return this.getResourceId(identifier);
    }

    /**
     * Retrieves the video resource id of a series.
     *
     * @param series
     *            series the video resource id should be retrieved for
     * @return video resource id of the series specified
     */
    protected int getVideoResourceId(final Series series) {
        final String identifier = this.getVideoResourceIdentifier(series);
        return this.getResourceId(identifier);
    }

    @Override
    public boolean hasAudioFile(Series series) {
        return (this.getAudioResourceId(series) != 0);
    }

    @Override
    public boolean hasImageFile(Series series) {
        return (this.getImageResourceId(series) != 0);
    }

    @Override
    public boolean hasVideoFile(Series series) {
        return (this.getVideoResourceId(series) != 0);
    }

    /**
     * Converts a resource Uri to a file.
     *
     * @param uri
     *            resource Uri
     * @return file that points to the resource specified
     */
    protected File fileFromUri(final Uri uri) {
        if (uri == null) {
            return null;
        }
        return new File(uri.toString());
    }

    @Override
    public File getAudioFile(Series series) {
        return this.fileFromUri(this.getAudioFileUri(series));
    }

    @Override
    public File getImageFile(Series series) {
        return this.fileFromUri(this.getImageFileUri(series));
    }

    @Override
    public File getVideoFile(Series series) {
        return this.fileFromUri(this.getVideoFileUri(series));
    }

    @Override
    public Uri getAudioFileUri(Series series) {
        final String identifier = this.getAudioResourceIdentifier(series);
        try {
            return this.getResourceUri(identifier);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public Uri getImageFileUri(Series series) {
        final String identifier = this.getImageResourceIdentifier(series);
        try {
            return this.getResourceUri(identifier);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public Uri getVideoFileUri(Series series) {
        final String identifier = this.getVideoResourceIdentifier(series);
        try {
            return this.getResourceUri(identifier);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Creates a resource provider using raw APK resources.
     *
     * @param context
     *            application context
     * @return resource provider using raw APK resources
     */
    public static RawResourceProvider create(final Context context) {
        return new RawResourceProvider(context.getResources(), context.getPackageName());
    }
}
