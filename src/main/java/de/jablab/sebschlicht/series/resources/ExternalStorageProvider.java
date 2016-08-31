package de.jablab.sebschlicht.series.resources;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

/**
 * {@link ResourceProvider} using the external storage as file source.
 *
 * @author sebschlicht
 *
 */
public class ExternalStorageProvider extends StorageProvider {

    /**
     * Creates a new external storage provider.<br>
     * Users should use {@link #create} to create a provider for an application
     * instead.
     *
     * @param resourceDirectory
     *            root directory of resource files on the storage
     */
    public ExternalStorageProvider(
            final File resourceDirectory) {
        super(resourceDirectory);
    }

    /**
     * Checks if the external storage is writable.
     *
     * @return <code>true</code> if the external storage is writable,
     *         <code>false</code> otherwise
     */
    public static boolean isExternalStorageWritable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the external storage is readable.
     *
     * @return <code>true</code> if the external storage is readable,
     *         <code>false</code> otherwise
     */
    public static boolean isExternalStorageReadable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Creates a storage provider using the external storage.
     *
     * @param context
     *            application context
     * @return resource provider using the external storage
     */
    public static ExternalStorageProvider create(final Context context) {
        if (!isExternalStorageReadable()) {
            throw new IllegalStateException("External storage not mounted!");
        }

        final File[] externalFiles =
                ContextCompat.getExternalFilesDirs(context, null);
        return new ExternalStorageProvider(externalFiles[0]);
    }
}
