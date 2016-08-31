package de.jablab.sebschlicht.kits.resources;

import java.io.File;

import android.content.Context;

/**
 * {@link ResourceProvider} using the internal device storage as file source.
 *
 * @author sebschlicht
 *
 */
public class InternalStorageProvider extends StorageProvider {

    /**
     * Creates a new internal storage provider.<br>
     * Users should use {@link #create} to create a provider for an application
     * instead.
     *
     * @param resourceDirectory
     *            root directory of resource files on the storage
     */
    public InternalStorageProvider(
            final File resourceDirectory) {
        super(resourceDirectory);
    }

    /**
     * Creates a storage provider using the internal device storage.
     *
     * @param context
     *            application context
     * @return resource provider using the internal device storage
     */
    public static InternalStorageProvider create(final Context context) {
        final File resourceDirectory = context.getExternalFilesDir(null);
        return new InternalStorageProvider(resourceDirectory);
    }
}
